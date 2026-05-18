package io.github.ffakira.rsps.content;

public class ContentDefinitionCatalog {

  public ContentDefinitionSummary loadSummary(ContentManifest manifest) {
    return new ContentArchiveCatalog().load(manifest).definitionSummary();
  }
}
