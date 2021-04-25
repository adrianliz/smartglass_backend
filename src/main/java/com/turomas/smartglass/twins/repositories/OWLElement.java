package com.turomas.smartglass.twins.repositories;

public enum OWLElement {
  MACHINE(OWLPrefix.SMARTGLASS, "Machine"),
  VALUE(OWLPrefix.SMARTGLASS, "value"),
  END_DATE(OWLPrefix.SMARTGLASS, "endDate"),
  IS_IN_STATE(OWLPrefix.SMARTGLASS, "isInState"),
  HAS_BRAND(OWLPrefix.SMARTGLASS, "hasBrand"),
  HAS_MODEL(OWLPrefix.SMARTGLASS, "hasModel"),
  HAS_RATIO(OWLPrefix.SMARTGLASS, "hasRatio");

  static final String SEPARATOR = ":";
  private final OWLPrefix prefix;
  private final String identifier;

  OWLElement(OWLPrefix prefix, String identifier) {
    this.prefix = prefix;
    this.identifier = identifier;
  }

  public String uri() {
    return prefix.getAbbreviation() + SEPARATOR + identifier;
  }
}
