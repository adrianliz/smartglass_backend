package com.turomas.smartglass.twins.repositories;

import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.DateRange;
import com.turomas.smartglass.twins.domain.EventsStatistics;
import com.turomas.smartglass.twins.domain.StatesStatistics;
import com.turomas.smartglass.twins.domain.Twin;
import com.turomas.smartglass.twins.domain.dtos.statistics.RatioDTO;
import com.turomas.smartglass.twins.domain.dtos.twins.TwinInfoDTO;
import com.turomas.smartglass.twins.domain.factories.StatesMachineFactory;
import com.turomas.smartglass.twins.domain.statesmachine.StatesMachine;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateType;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;
import com.turomas.smartglass.twins.services.StatesService;
import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.arq.querybuilder.UpdateBuilder;
import org.apache.jena.atlas.web.HttpException;
import org.apache.jena.query.*;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.shared.JenaException;
import org.apache.jena.system.Txn;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Repository
public class OWLRepository implements TwinsRepository {
  private static final String X = "?x";
  private static final String Y = "?y";
  private static final String Z = "?z";
  private static final String MACHINE_MODEL = "?machineModel";
  private static final String MACHINE_BRAND = "?machineBrand";
  private static final String MACHINE_STATE = "?machineState";
  private static final String MACHINE_NAME = "?machineName";
  private static final String VALUE = "?value";
  private static final String END_DATE = "?endDate";

  private final String ontologyUri;
  private final Map<String, Twin> twins;

  public OWLRepository(StatesService statesService, EventsService eventsService,
                       StatesMachineFactory statesMachineFactory, @Value("${ontology.uri}") String ontologyUri) {

    twins = new HashMap<>();
    this.ontologyUri = ontologyUri;

    createTwins(statesService, eventsService, statesMachineFactory);
  }

  private Optional<ResultSet> executeQuery(SelectBuilder query) {
    try (RDFConnection connection = RDFConnectionFactory.connectFuseki(ontologyUri)) {
      return Optional.ofNullable(Txn.calculateRead(connection, () -> {
        try (QueryExecution queryExec = QueryExecutionFactory.create(query.build(), connection.fetchDataset())) {
          return ResultSetFactory.copyResults(queryExec.execSelect());
        }
      }));
    } catch (JenaException | HttpException ex) {
      System.err.println(ex.getMessage());
      return Optional.empty();
    }
  }

  private void executeUpdate(UpdateBuilder update) {
    try (RDFConnection connection = RDFConnectionFactory.connectFuseki(ontologyUri)) {
      Txn.executeWrite(connection, () -> connection.update(update.build()));
    } catch (JenaException | HttpException ex) {
      System.err.println(ex.getMessage());
    }
  }

  private void createTwins(StatesService statesService, EventsService eventsService,
                           StatesMachineFactory statesMachineFactory) {

    Optional<ResultSet> triples =
      executeQuery(new SelectBuilder().addPrefixes(OWLPrefix.all()).addVar(MACHINE_NAME)
                                      .addWhere(X, RDF.type, OWLElement.MACHINE.uri())
                                      .addWhere(X, RDFS.label, MACHINE_NAME));

    triples.ifPresent(machines -> machines.forEachRemaining(machine -> {
      String machineName = machine.getLiteral(MACHINE_NAME).getString();
      StatesMachine statesMachine = statesMachineFactory.createFor(machineName);
      twins.put(machineName, new Twin(statesMachine, new StatesStatistics(machineName, statesService),
                                      new EventsStatistics(machineName, eventsService)));
    }));
  }

  @Override
  public Collection<Twin> getTwins() {
    return twins.values();
  }

  @Override
  public Twin getTwin(String twinName) throws TwinNotFound {
    return Optional.ofNullable(twins.get(twinName)).orElseThrow(() -> new TwinNotFound(twinName));
  }

  @Override
  public TwinInfoDTO getTwinInfo(String twinName) throws TwinNotFound {
    if (! twins.containsKey(twinName)) throw new TwinNotFound(twinName);

    Optional<ResultSet> triples =
      executeQuery(new SelectBuilder().addPrefixes(OWLPrefix.all())
                                      .addVar(MACHINE_MODEL).addVar(MACHINE_BRAND).addVar(MACHINE_STATE)
                                      .addWhere(OWLPrefix.SMARTGLASS.append(twinName), OWLElement.HAS_BRAND.uri(), X)
                                      .addWhere(X, RDFS.label, MACHINE_BRAND)
                                      .addWhere(OWLPrefix.SMARTGLASS.append(twinName), OWLElement.HAS_MODEL.uri(), Y)
                                      .addWhere(Y, RDFS.label, MACHINE_MODEL)
                                      .addWhere(OWLPrefix.SMARTGLASS.append(twinName), OWLElement.IS_IN_STATE.uri(), Z)
                                      .addWhere(Z, RDFS.label, MACHINE_STATE));

    if (triples.isPresent()) {
      QuerySolution twinInfo = triples.get().nextSolution();
      return new TwinInfoDTO(twinName, twinInfo.getLiteral(MACHINE_BRAND).getString(),
                             twinInfo.getLiteral(MACHINE_MODEL).getString(),
                             TwinStateType.of(twinInfo.getLiteral(MACHINE_STATE).getString()));
    }
    return new TwinInfoDTO(twinName, TwinStateType.UNDEFINED);
  }

  @Override
  public Collection<TwinInfoDTO> getTwinsInfo() {
    List<TwinInfoDTO> twinsModels = new ArrayList<>();

    for (String twinName : twins.keySet()) {
      twinsModels.add(getTwinInfo(twinName));
    }

    return twinsModels;
  }

  @Override
  public void updateLastState(String twinName, StatesService statesService) throws TwinNotFound {
    if (! twins.containsKey(twinName)) throw new TwinNotFound(twinName);

    statesService.getLastState(twinName).ifPresent(
      lastState -> executeUpdate(
        new UpdateBuilder().addPrefixes(OWLPrefix.all())
                           .addDelete(OWLPrefix.SMARTGLASS.append(twinName), OWLElement.IS_IN_STATE.uri(), X)
                           .addInsert(OWLPrefix.SMARTGLASS.append(twinName), OWLElement.IS_IN_STATE.uri(),
                                      OWLPrefix.SMARTGLASS.append(lastState.getType().name()))
                           .addWhere(OWLPrefix.SMARTGLASS.append(twinName), OWLElement.IS_IN_STATE.uri(), X)));

  }

  @Override
  public void updateRatios(String twinName) throws TwinNotFound {
    Twin twin = twins.get(twinName);
    if (twin == null) throw new TwinNotFound(twinName);

    LocalDateTime endDate = LocalDateTime.now();
    DateRange dateRange =
      new DateRange(LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atTime(0, 0, 0),
                    endDate);

    Collection<RatioDTO> ratios = twin.getRatios(dateRange);
    ratios.forEach(ratio ->
                     executeUpdate(new UpdateBuilder().addPrefixes(OWLPrefix.all())
                                                      .addDelete(X, OWLElement.VALUE.uri(), VALUE)
                                                      .addDelete(X, OWLElement.END_DATE.uri(), END_DATE)
                                                      .addInsert(X, OWLElement.VALUE.uri(), ratio.getValue())
                                                      .addInsert(X, OWLElement.END_DATE.uri(),
                                                                 DateTimeFormatter.ISO_LOCAL_DATE_TIME
                                                                   .format(endDate))
                                                      .addWhere(OWLPrefix.SMARTGLASS.append(twinName),
                                                                OWLElement.HAS_RATIO.uri(), X)
                                                      .addWhere(X, OWLElement.VALUE.uri(), VALUE)
                                                      .addWhere(X, OWLElement.END_DATE.uri(), END_DATE)
                                                      .addWhere(X, RDF.type,
                                                                OWLPrefix.SMARTGLASS.append(ratio.getRatio().name()))));
  }
}
