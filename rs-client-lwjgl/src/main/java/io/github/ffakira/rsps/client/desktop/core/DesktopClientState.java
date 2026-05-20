package io.github.ffakira.rsps.client.desktop.core;

final class DesktopClientState {

  private String titleScreenStatus;
  private double mouseX;
  private double mouseY;
  private String activeSceneKey;
  private boolean gameplayBootstrapApplied;

  DesktopClientState(String titleScreenStatus) {
    this.titleScreenStatus = titleScreenStatus;
  }

  String titleScreenStatus() {
    return titleScreenStatus;
  }

  void setTitleScreenStatus(String titleScreenStatus) {
    this.titleScreenStatus = titleScreenStatus;
  }

  double mouseX() {
    return mouseX;
  }

  double mouseY() {
    return mouseY;
  }

  void setMousePosition(double mouseX, double mouseY) {
    this.mouseX = mouseX;
    this.mouseY = mouseY;
  }

  String activeSceneKey() {
    return activeSceneKey;
  }

  void setActiveSceneKey(String activeSceneKey) {
    this.activeSceneKey = activeSceneKey;
  }

  boolean gameplayBootstrapApplied() {
    return gameplayBootstrapApplied;
  }

  void setGameplayBootstrapApplied(boolean gameplayBootstrapApplied) {
    this.gameplayBootstrapApplied = gameplayBootstrapApplied;
  }
}
