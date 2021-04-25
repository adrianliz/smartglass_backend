package com.turomas.smartglass.twins.repositories;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum OWLPrefix {
  SMARTGLASS("smartglass", "http://www.semanticweb.org/turomas/smartglass#"),
  RDF("rdf", org.apache.jena.vocabulary.RDF.getURI()),
  RDFS("rdfs", org.apache.jena.vocabulary.RDFS.getURI()),
  XSD("xsd", org.apache.jena.vocabulary.XSD.getURI());

  @Getter
  private final String abbreviation;
  private final String uri;

  OWLPrefix(String abbreviation, String uri) {
    this.abbreviation = abbreviation;
    this.uri = uri;
  }

  public static Map<String, String> all() {
    return toMap(Set.of(OWLPrefix.values()));
  }

  public static Map<String, String> toMap(Set<OWLPrefix> prefixes) {
    Map<String, String> mapPrefixes = new HashMap<>();
    for (OWLPrefix prefix : prefixes) {
      mapPrefixes.put(prefix.abbreviation, prefix.uri);
    }

    return mapPrefixes;
  }

  public String append(String variable) {
    return abbreviation + OWLElement.SEPARATOR + variable;
  }
}
