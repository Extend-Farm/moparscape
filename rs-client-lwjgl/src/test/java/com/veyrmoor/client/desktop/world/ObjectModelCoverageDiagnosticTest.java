package com.veyrmoor.client.desktop.world;

import com.veyrmoor.cache.RawModelRepository;
import com.veyrmoor.content.ContentArchiveCatalog;
import com.veyrmoor.content.ContentBootstrapService;
import com.veyrmoor.content.ObjectDefinition;
import com.veyrmoor.content.ObjectDefinitionCatalog;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Read-only diagnostic for the missing-model investigation. For each named object that the
 * Lumbridge-area diagnostic flagged as rendering with the fallback proxy, this test dumps:
 *
 * <ul>
 *   <li>whether the ObjectDefinition exists at all,</li>
 *   <li>its {@code modelIds} and {@code modelTypes} arrays,</li>
 *   <li>which placement-types (5..10..22) would resolve to a non-empty model list,</li>
 *   <li>whether each model id can actually be decoded from the model archive.</li>
 * </ul>
 *
 * <p>Output goes to stderr so we can see exactly WHY each named object resolves to no geometry —
 * is the definition missing models entirely, or is {@code modelTypes} mismatched against the
 * placement type used in the map, or do the referenced model ids fail to decode?
 */
class ObjectModelCoverageDiagnosticTest {

  // From the prior Lumbridge bridge diagnostic — these are the named objects that still rendered
  // as fallback proxies (not anonymous "object-NNNN" entries).
  private static final int[] CANDIDATE_OBJECT_IDS = {
      2456, // firetemple_ruined
      7771, // farming_cactus_patch
      9057, // roguetrader_alim_multi_stall
      9065, // roguetrader_alim_multicrate_runes
      9067, // roguetrader_alim_multicrate_bjacks
      9069, // roguetrader_alim_multicrate_clothes
      9251  // garden_lumbridge_statue
  };

  // The placement types we've seen in the actual scene for these IDs (from the prior diagnostic).
  private static final int[] PLACEMENT_TYPES_TO_PROBE = {0, 9, 10, 11, 22};

  @Test
  void printsAggregateModelCoverageStatistics() {
    var manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    var archiveSnapshot = new ContentArchiveCatalog().load(manifest);
    ObjectDefinitionCatalog catalog = ObjectDefinitionCatalog.parse(
        archiveSnapshot.readConfigEntry("loc.idx"),
        archiveSnapshot.readConfigEntry("loc.dat")
    );
    int total = 0;
    int withModels = 0;
    int emptyModels = 0;
    int firstFewEmptyWithName = 0;
    for (int id = 0; id < 60_000; id++) {
      var maybe = catalog.find(id);
      if (maybe.isEmpty()) {
        continue;
      }
      total++;
      if (maybe.get().modelIds().isEmpty()) {
        emptyModels++;
        if (firstFewEmptyWithName < 6 && !maybe.get().name().equals("null")
            && !maybe.get().name().startsWith("object-")) {
          System.err.printf("  empty-models example: id=%5d name=\"%s\"%n",
              id, maybe.get().name());
          firstFewEmptyWithName++;
        }
      } else {
        withModels++;
      }
    }
    System.err.println();
    System.err.println("=== Aggregate object model coverage ===");
    System.err.printf("  totalDefinitions=%d  withModels=%d  emptyModels=%d  ratio=%.1f%%%n",
        total, withModels, emptyModels, 100.0 * emptyModels / Math.max(1, total));
    System.err.println();
  }

  @Test
  void printsObjectTypeDistributionAtLumbridgeBridge() {
    var loader = new com.veyrmoor.client.desktop.world.CacheBackedWorldSceneLoader(Path.of("."));
    var worldScene = loader.load(new com.veyrmoor.model.WorldPoint(3233, 3219, 0));
    java.util.TreeMap<Integer, Integer> byType = new java.util.TreeMap<>();
    java.util.TreeMap<Integer, Integer> nullGeometryByType = new java.util.TreeMap<>();
    int totalWallDecor = 0;
    int wallDecorWithGeometry = 0;
    java.util.List<String> wallDecorSamples = new java.util.ArrayList<>();
    for (var object : worldScene.objects()) {
      byType.merge(object.type(), 1, Integer::sum);
      if (object.geometry() == null) {
        nullGeometryByType.merge(object.type(), 1, Integer::sum);
      }
      if (object.type() >= 4 && object.type() <= 8) {
        totalWallDecor++;
        if (object.geometry() != null) {
          wallDecorWithGeometry++;
        }
        if (wallDecorSamples.size() < 8) {
          wallDecorSamples.add(String.format("id=%d name=\"%s\" type=%d orient=%d localXY=(%d,%d) geomNull=%b",
              object.objectId(), object.name(), object.type(), object.orientation(),
              object.localX(), object.localY(), object.geometry() == null));
        }
      }
    }
    System.err.println();
    System.err.println("=== Scene object type distribution at (3233, 3219, 0) ===");
    byType.forEach((type, count) -> {
      int nullGeom = nullGeometryByType.getOrDefault(type, 0);
      System.err.printf("  type=%2d  count=%4d  nullGeometry=%d%n", type, count, nullGeom);
    });
    System.err.printf("  wallDecor (type 4-8) total=%d, withGeometry=%d%n",
        totalWallDecor, wallDecorWithGeometry);
    wallDecorSamples.forEach(s -> System.err.println("    " + s));
    System.err.println();
  }

  @Test
  void printsAnimationCoverageForCommonAnimatedObjects() {
    var manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    var archiveSnapshot = new ContentArchiveCatalog().load(manifest);
    var catalog = com.veyrmoor.content.ObjectDefinitionCatalog.parse(
        archiveSnapshot.readConfigEntry("loc.idx"),
        archiveSnapshot.readConfigEntry("loc.dat")
    );
    String[] searchTerms = {"fountain", "fire", "candle", "torch", "water_wheel",
        "windmill", "door", "altar", "lava", "smelting"};
    System.err.println();
    System.err.println("=== Probable animated objects in this cache ===");
    int total = 0;
    for (int id = 0; id < 60_000; id++) {
      var maybe = catalog.find(id);
      if (maybe.isEmpty()) continue;
      String name = maybe.get().name().toLowerCase();
      for (String term : searchTerms) {
        if (name.contains(term)) {
          total++;
          if (total <= 20) {
            System.err.printf("  id=%5d  name=\"%s\"  models=%d%n",
                id, maybe.get().name(), maybe.get().modelIds().size());
          }
          break;
        }
      }
    }
    System.err.printf("  ... %d total potentially-animated objects in catalog%n", total);
    System.err.println();
  }

  @Test
  void printsModelCoverageForKnownMissingObjects() {
    var manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    var archiveSnapshot = new ContentArchiveCatalog().load(manifest);
    ObjectDefinitionCatalog catalog = ObjectDefinitionCatalog.parse(
        archiveSnapshot.readConfigEntry("loc.idx"),
        archiveSnapshot.readConfigEntry("loc.dat")
    );
    RawModelRepository models = new RawModelRepository(manifest.cacheStore());

    System.err.println();
    System.err.println("=== Object model coverage diagnostic ===");
    for (int objectId : CANDIDATE_OBJECT_IDS) {
      var maybeDefinition = catalog.find(objectId);
      if (maybeDefinition.isEmpty()) {
        System.err.printf("  id=%-5d  DEFINITION MISSING%n", objectId);
        continue;
      }
      ObjectDefinition definition = maybeDefinition.get();
      System.err.printf("  id=%-5d  name=\"%s\"%n", objectId, definition.name());
      System.err.printf("    modelIds=%s%n", definition.modelIds());
      System.err.printf("    modelTypes=%s%n", definition.modelTypes());
      System.err.printf("    sizeX=%d sizeY=%d  contouredGround=%b%n",
          definition.sizeX(), definition.sizeY(), definition.contouredGround());
      for (int placementType : PLACEMENT_TYPES_TO_PROBE) {
        List<Integer> resolved = definition.modelIdsForType(placementType);
        System.err.printf("    modelIdsForType(%2d) -> %s%n", placementType, resolved);
      }
      // Try to decode each model id so we can tell missing-from-archive apart from
      // empty-modelIds-for-this-type.
      for (Integer modelId : definition.modelIds()) {
        try {
          var raw = models.loadModel(modelId);
          System.err.printf("    model %d  DECODE OK   vertices=%d faces=%d%n",
              modelId, raw.vertexCount(), raw.faceCount());
        } catch (RuntimeException exception) {
          System.err.printf("    model %d  DECODE FAIL  %s%n",
              modelId, exception.getMessage());
        }
      }
    }
    System.err.println();
  }
}
