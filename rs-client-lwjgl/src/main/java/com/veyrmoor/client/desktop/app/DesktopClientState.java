package com.veyrmoor.client.desktop.app;

public final class DesktopClientState {

  private String titleScreenStatus;
  private double mouseX;
  private double mouseY;
  private String activeSceneKey;
  private boolean gameplayBootstrapApplied;

  public DesktopClientState(String titleScreenStatus) {
    this.titleScreenStatus = titleScreenStatus;
  }

  public String titleScreenStatus() {
    return titleScreenStatus;
  }

  public void setTitleScreenStatus(String titleScreenStatus) {
    this.titleScreenStatus = titleScreenStatus;
  }

  public double mouseX() {
    return mouseX;
  }

  public double mouseY() {
    return mouseY;
  }

  public void setMousePosition(double mouseX, double mouseY) {
    this.mouseX = mouseX;
    this.mouseY = mouseY;
  }

  public String activeSceneKey() {
    return activeSceneKey;
  }

  public void setActiveSceneKey(String activeSceneKey) {
    this.activeSceneKey = activeSceneKey;
  }

  public boolean gameplayBootstrapApplied() {
    return gameplayBootstrapApplied;
  }

  public void setGameplayBootstrapApplied(boolean gameplayBootstrapApplied) {
    this.gameplayBootstrapApplied = gameplayBootstrapApplied;
  }
}
