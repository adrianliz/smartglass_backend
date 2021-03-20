package com.turomas.smartglass.twins.repositories;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.Twin;
import com.turomas.smartglass.twins.domain.TwinOntology;
import com.turomas.smartglass.twins.domain.statesmachine.StatesMachine;
import com.turomas.smartglass.twins.domain.statesmachine.TransitionTrigger;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateId;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

// TODO replace with ontology model
@Repository
public class OWLTwinsRepository implements TwinsRepository {
	private final List<Twin> twins;
	private final Twin twin;

	public OWLTwinsRepository(EventsService eventsService, @Value("classpath:transitions.json") Resource resourceFile)
		throws IOException, JsonIOException, JsonSyntaxException {
		Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> transitions =
			loadTransitions(new FileReader(resourceFile.getFile()));

		twin = new Twin(
			new TwinOntology("Turomas1", "RUBI 300 SERIES", "RUBI 303BA"),
			new StatesMachine(transitions), eventsService);

		twins = List.of(twin);
	}

	private Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> loadTransitions(Reader file)
		throws JsonIOException, JsonSyntaxException {

		Type type =
			new TypeToken<Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId>>() {
			}.getType();

		return new GsonBuilder().enableComplexMapKeySerialization().create().fromJson(file, type);
	}

	@Override
	public List<Twin> getTwins() {
		return twins;
	}

	@Override
	public Twin getTwin(String name) throws TwinNotFound {
		if (name.equals("Turomas1")) return twin;
		throw new TwinNotFound(name);
	}
}