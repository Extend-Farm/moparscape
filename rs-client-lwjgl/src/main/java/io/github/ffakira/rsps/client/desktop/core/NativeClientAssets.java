package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.client.desktop.character.CharacterModelAssembler;
import io.github.ffakira.rsps.client.desktop.gameplay.GameplayFrameAssets;
import io.github.ffakira.rsps.client.desktop.itemicon.ItemIconRenderer;
import io.github.ffakira.rsps.client.desktop.login.TitleScreenAssets;
import io.github.ffakira.rsps.client.desktop.world.CacheBackedWorldSceneLoader;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTextureAssets;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;

record NativeClientAssets(
    TitleScreenAssets titleScreenAssets,
    GameplayFrameAssets gameplayFrameAssets,
    SceneTextureAssets sceneTextureAssets,
    ItemDefinitionCatalog itemDefinitionCatalog,
    ItemIconRenderer itemIconRenderer,
    CharacterModelAssembler characterModelAssembler,
    CacheBackedWorldSceneLoader worldSceneLoader
) {

  OpenGlTileRenderSystem createRenderSystem(DesktopWindowConfig windowConfig) {
    return new OpenGlTileRenderSystem(
        windowConfig.width(),
        windowConfig.height(),
        titleScreenAssets,
        gameplayFrameAssets,
        sceneTextureAssets,
        itemDefinitionCatalog,
        itemIconRenderer,
        characterModelAssembler
    );
  }
}
