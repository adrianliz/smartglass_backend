package com.turomas.smartglass.twins.repositories;

import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.EventsStatistics;
import com.turomas.smartglass.twins.domain.StatesStatistics;
import com.turomas.smartglass.twins.domain.Twin;
import com.turomas.smartglass.twins.domain.dtos.twins.TwinModelDTO;
import com.turomas.smartglass.twins.domain.factories.StatesMachineFactory;
import com.turomas.smartglass.twins.domain.statesmachine.StatesMachine;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateType;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;
import com.turomas.smartglass.twins.services.StatesService;
import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.arq.querybuilder.UpdateBuilder;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.sparql.engine.http.QueryExceptionHTTP;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OWLRepository implements TwinsRepository {
  private static final String X = "?x";
  private static final String Y = "?y";
  private static final String MACHINE_MODEL = "?machineModel";
  private static final String MACHINE_BRAND = "?machineBrand";
  private static final String MACHINE_STATE = "?machineState";
  private static final String MACHINE_NAME = "?machineName";

  private final String ontologyUri;
  private final Map<String, Twin> twins;

  public OWLRepository(StatesService statesService, EventsService eventsService,
                       StatesMachineFactory statesMachineFactory, @Value("${ontology.uri}") String ontologyUri) {

    twins = new HashMap<>();
    this.ontologyUri = ontologyUri;

    retrieveMachines(statesService, eventsService, statesMachineFactory);
  }

  private void retrieveMachines(StatesService statesService, EventsService eventsService,
                                StatesMachineFactory statesMachineFactory) {

    try (RDFConnection connection = RDFConnectionFactory.connectFuseki(ontologyUri)) {
      SelectBuilder selectTwins =
        new SelectBuilder().addPrefixes(OWLPrefix.all())
                           .addVar(MACHINE_NAME)
                           .addWhere(X, RDF.type, OWLElement.MACHINE.toString())
                           .addWhere(X, RDFS.label, MACHINE_NAME);

      ResultSet triples = connection.query(selectTwins.build()).execSelect();
      triples.forEachRemaining(triple -> {
        String machineName = triple.getLiteral(MACHINE_NAME).getString();
        StatesMachine statesMachine = statesMachineFactory.createFor(machineName);
        twins.put(machineName, new Twin(statesMachine, new StatesStatistics(machineName, statesService),
                                        new EventsStatistics(machineName, eventsService)));
      });
    } catch (QueryExceptionHTTP ex) {
      System.err.println(ex.getMessage());
    }
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
  public TwinModelDTO getTwinModel(String twinName) throws TwinNotFound {
    if (! twins.containsKey(twinName)) throw new TwinNotFound(twinName);
    QuerySolution triple = null;

    try (RDFConnection connection = RDFConnectionFactory.connectFuseki(ontologyUri)) {
      SelectBuilder selectTwinModel =
        new SelectBuilder().addPrefixes(OWLPrefix.toMap(Set.of(OWLPrefix.SMARTGLASS, OWLPrefix.RDFS)))
                           .addVar(MACHINE_MODEL).addVar(MACHINE_BRAND).addVar(MACHINE_STATE)
                           .addWhere(OWLPrefix.SMARTGLASS.append(twinName), OWLElement.HAS_BRAND, X)
                           .addWhere(X, RDFS.label, MACHINE_BRAND)
                           .addWhere(OWLPrefix.SMARTGLASS.append(twinName), OWLElement.HAS_MODEL, Y)
                           .addWhere(Y, RDFS.label, MACHINE_MODEL)
                           .addWhere(OWLPrefix.SMARTGLASS.append(twinName), OWLElement.STATE, MACHINE_STATE);

      ResultSet triples = connection.query(selectTwinModel.build()).execSelect();
      triple = triples.nextSolution();
    } catch (QueryExceptionHTTP ex) {
      System.err.println(ex.getMessage());
    }

    if (triple != null) {
      return new TwinModelDTO(twinName, triple.getLiteral(MACHINE_BRAND).getString(),
                              triple.getLiteral(MACHINE_MODEL).getString(),
                              TwinStateType.of(triple.getLiteral(MACHINE_MODEL).getString()));
    }
    return new TwinModelDTO(twinName, "", "", TwinStateType.UNDEFINED);
  }

  @Override
  public Collection<TwinModelDTO> getTwinModels() {
    List<TwinModelDTO> twinsModels = new ArrayList<>();

    for (String twinName : twins.keySet()) {
      twinsModels.add(getTwinModel(twinName));
    }

    return twinsModels;
  }

  @Override
  public void updateState(TwinState state) throws TwinNotFound {
    String twinName = state.getTwinName();
    if (! twins.containsKey(twinName)) throw new TwinNotFound(twinName);

    try (RDFConnection connection = RDFConnectionFactory.connectFuseki(ontologyUri)) {
      UpdateBuilder updateMachineState =
        new UpdateBuilder().addPrefixes(OWLPrefix.toMap(Set.of(OWLPrefix.SMARTGLASS)))
                           .addDelete(OWLPrefix.SMARTGLASS.append(twinName), OWLElement.STATE.toString(), X)
                           .addInsert(OWLPrefix.SMARTGLASS.append(twinName), OWLElement.STATE.toString(),
                                      state.getType().name())
                           .addWhere(OWLPrefix.SMARTGLASS.append(twinName), OWLElement.STATE.toString(), X);

      connection.update(updateMachineState.build());
    } catch (QueryExceptionHTTP ex) {
      System.err.println(ex.getMessage());
    }
  }
}
