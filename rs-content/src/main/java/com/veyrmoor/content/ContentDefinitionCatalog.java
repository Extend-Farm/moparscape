package com.veyrmoor.content;

public class ContentDefinitionCatalog {

  public ContentDefinitionSummary loadSummary(ContentManifest manifest) {
    return new ContentArchiveCatalog().load(manifest).definitionSummary();
  }
}
