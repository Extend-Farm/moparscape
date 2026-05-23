package com.veyrmoor.client.desktop.assets;

import com.veyrmoor.client.desktop.character.CharacterModelAssembler;
import com.veyrmoor.client.desktop.character.NpcModelAssembler;
import com.veyrmoor.client.desktop.gameplay.GameplayFrameAssets;
import com.veyrmoor.client.desktop.itemicon.ItemIconRenderer;
import com.veyrmoor.client.desktop.login.TitleScreenAssets;
import com.veyrmoor.client.desktop.platform.window.DesktopWindowConfig;
import com.veyrmoor.client.desktop.render.opengl.OpenGlTileRenderSystem;
import com.veyrmoor.client.desktop.world.CacheBackedWorldSceneLoader;
import com.veyrmoor.client.desktop.world.raster.SceneTextureAssets;
import com.veyrmoor.content.ItemDefinitionCatalog;

public record NativeClientAssets(
    TitleScreenAssets titleScreenAssets,
    GameplayFrameAssets gameplayFrameAssets,
    SceneTextureAssets sceneTextureAssets,
    ItemDefinitionCatalog itemDefinitionCatalog,
    ItemIconRenderer itemIconRenderer,
    CharacterModelAssembler characterModelAssembler,
    NpcModelAssembler npcModelAssembler,
    CacheBackedWorldSceneLoader worldSceneLoader
) {

  public OpenGlTileRenderSystem createRenderSystem(DesktopWindowConfig windowConfig) {
    return new OpenGlTileRenderSystem(
        windowConfig.width(),
        windowConfig.height(),
        titleScreenAssets,
        gameplayFrameAssets,
        sceneTextureAssets,
        itemDefinitionCatalog,
        itemIconRenderer,
        characterModelAssembler,
        npcModelAssembler
    );
  }
}
