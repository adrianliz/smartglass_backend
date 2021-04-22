package com.turomas.smartglass.twins.repositories;

public enum OWLElement {
  MACHINE(OWLPrefix.SMARTGLASS, "Machine"),
  STATE(OWLPrefix.SMARTGLASS, "state"),
  HAS_BRAND(OWLPrefix.SMARTGLASS, "hasBrand"),
  HAS_MODEL(OWLPrefix.SMARTGLASS, "hasModel");

  static final String SEPARATOR = ":";
  private final OWLPrefix prefix;
  private final String identifier;

  OWLElement(OWLPrefix prefix, String identifier) {
    this.prefix = prefix;
    this.identifier = identifier;
  }

  @Override
  public String toString() {
    return prefix + SEPARATOR + identifier;
  }
}
