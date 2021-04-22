package com.turomas.smartglass.twins.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum OWLPrefix {
  SMARTGLASS("smartglass", "http://www.semanticweb.org/turomas/smartglass#"),
  RDF("rdf", org.apache.jena.vocabulary.RDF.getURI()),
  RDFS("rdfs", org.apache.jena.vocabulary.RDFS.getURI());

  private final String name;
  private final String uri;

  OWLPrefix(String name, String uri) {
    this.name = name;
    this.uri = uri;
  }

  public static Map<String, String> all() {
    return toMap(Set.of(OWLPrefix.values()));
  }

  public static Map<String, String> toMap(Set<OWLPrefix> prefixes) {
    Map<String, String> mapPrefixes = new HashMap<>();
    for (OWLPrefix prefix : prefixes) {
      mapPrefixes.put(prefix.name, prefix.uri);
    }

    return mapPrefixes;
  }

  public String append(String variable) {
    return name + OWLElement.SEPARATOR + variable;
  }

  @Override
  public String toString() {
    return name;
  }
}
