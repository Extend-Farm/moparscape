package io.github.ffakira.rsps.client.desktop.world.object;

import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.content.MapObjectPlacement;
import io.github.ffakira.rsps.content.ObjectDefinition;
import io.github.ffakira.rsps.content.ObjectDefinitionCatalog;
import java.util.List;

public final class WorldSceneObjectAssembler {

  private final ObjectDefinitionCatalog objectDefinitions;
  private final ObjectSceneGeometryBuilder objectGeometryBuilder;

  public WorldSceneObjectAssembler(
      ObjectDefinitionCatalog objectDefinitions,
      RawModelRepository rawModelRepository
  ) {
    this.objectDefinitions = objectDefinitions;
    this.objectGeometryBuilder = new ObjectSceneGeometryBuilder(rawModelRepository);
  }

  public WorldSceneObject assemble(
      MapObjectPlacement placement,
      int originWorldX,
      int originWorldY
  ) {
    int localX = placement.worldX() - originWorldX;
    int localY = placement.worldY() - originWorldY;
    ObjectDefinition definition = objectDefinitions.find(placement.objectId()).orElse(null);
    int sizeX = definition == null ? 1 : definition.footprintWidth(placement.orientation());
    int sizeY = definition == null ? 1 : definition.footprintHeight(placement.orientation());
    String name = definition == null ? "object-" + placement.objectId() : definition.name();
    List<Integer> modelIds = definition == null ? List.of() : definition.modelIdsForType(placement.type());
    // Preserve every colocated placement as its own scene entry. Walls, decorations, interactives,
    // and ground objects can legitimately share a tile, so assembly must not collapse them by tile
    // key before later visibility/ordering work gets a chance to see the full scene input.
    WorldSceneObjectGeometry geometry = definition == null
        ? null
        : objectGeometryBuilder.build(definition, placement.orientation(), modelIds);
    boolean allowFallbackProxy = geometry == null && supportsFallbackProxy(placement.type());
    return new WorldSceneObject(
        placement.objectId(),
        name,
        localX,
        localY,
        placement.plane(),
        placement.type(),
        placement.orientation(),
        sizeX,
        sizeY,
        definition != null && definition.contouredGround(),
        definition == null ? -1 : definition.mapSceneId(),
        definition == null ? -1 : definition.mapFunctionId(),
        modelIds,
        allowFallbackProxy,
        definition == null || definition.castsShadow(),
        geometry
    );
  }

  private static boolean supportsFallbackProxy(int objectType) {
    return switch (objectType) {
      case 0, 1, 2, 3, 9, 10, 11, 12, 13, 14, 15, 16, 17 -> true;
      default -> false;
    };
  }
}
