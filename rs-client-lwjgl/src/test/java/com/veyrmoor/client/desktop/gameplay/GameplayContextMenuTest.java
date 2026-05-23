package com.veyrmoor.client.desktop.gameplay;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.model.WorldPoint;
import org.junit.jupiter.api.Test;

class GameplayContextMenuTest {

  @Test
  void clampsMenuInsideViewportBounds() {
    ScreenRect bounds = GameplayLayout.worldViewportInnerRect();

    GameplayContextMenu menu = GameplayContextMenu.walkHereMenu(
        bounds,
        bounds.left() + bounds.width() - 2.0,
        bounds.top() + bounds.height() - 2.0,
        new WorldPoint(3200, 3200, 0),
        72,
        50,
        32
    );

    assertThat(menu.rect().left()).isGreaterThanOrEqualTo(bounds.left());
    assertThat(menu.rect().top()).isGreaterThanOrEqualTo(bounds.top());
    assertThat(menu.rect().left() + menu.rect().width()).isLessThanOrEqualTo(bounds.left() + bounds.width());
    assertThat(menu.rect().top() + menu.rect().height()).isLessThanOrEqualTo(bounds.top() + bounds.height());
    assertThat(menu.entries()).hasSize(2);
    assertThat(menu.entries())
        .extracting(GameplayContextMenu.Entry::plainText)
        .containsExactly("Walk here", "Cancel");
  }

  @Test
  void onlyEntryRowsSelectActions() {
    GameplayContextMenu menu = GameplayContextMenu.walkHereMenu(
        GameplayLayout.worldViewportInnerRect(),
        40.0,
        50.0,
        new WorldPoint(3210, 3212, 0),
        72,
        50,
        32
    );

    ScreenRect entryRect = menu.entryHoverRect(0);
    ScreenRect cancelRect = menu.entryHoverRect(1);

    assertThat(menu.entryAt(menu.rect().left() + 2.0, menu.rect().top() + 2.0)).isNull();
    assertThat(menu.entryAt(entryRect.left() + 2.0, entryRect.top() + 2.0))
        .extracting(GameplayContextMenu.Entry::plainText, entry -> entry.action().kind(), entry -> entry.action().targetWorldPoint())
        .containsExactly("Walk here", GameplayMenuAction.Kind.WALK_TO_WORLD_POINT, new WorldPoint(3210, 3212, 0));
    assertThat(menu.entryAt(cancelRect.left() + 2.0, cancelRect.top() + 2.0))
        .extracting(GameplayContextMenu.Entry::plainText, entry -> entry.action().kind(), entry -> entry.action().targetWorldPoint())
        .containsExactly("Cancel", GameplayMenuAction.Kind.CANCEL, null);
  }

  @Test
  void centersMenuAroundPreferredHorizontalClickPosition() {
    GameplayContextMenu menu = GameplayContextMenu.walkHereMenu(
        GameplayLayout.worldViewportInnerRect(),
        200.0,
        50.0,
        new WorldPoint(3210, 3212, 0),
        80,
        48,
        32
    );

    assertThat(menu.rect().left()).isEqualTo(200.0f - menu.rect().width() / 2.0f);
  }

  @Test
  void supportsActionAndSubjectColourSegmentation() {
    GameplayContextMenu.Entry entry = GameplayContextMenu.subjectEntry(
        GameplayMenuAction.walkTo(new WorldPoint(3200, 3201, 0)),
        "Examine ",
        "Pew",
        GameplayContextMenu.NPC_SUBJECT_RGB
    );

    assertThat(entry.textRuns())
        .extracting(GameplayContextMenu.TextRun::text, GameplayContextMenu.TextRun::rgb, GameplayContextMenu.TextRun::actionColorControlled)
        .containsExactly(
            org.assertj.core.groups.Tuple.tuple("Examine ", GameplayContextMenu.ACTION_RGB, true),
            org.assertj.core.groups.Tuple.tuple("Pew", GameplayContextMenu.NPC_SUBJECT_RGB, false)
        );
  }

  @Test
  void tracksHoveredEntryRowsWithoutSelectingTheHeader() {
    GameplayContextMenu menu = GameplayContextMenu.walkHereMenu(
        GameplayLayout.worldViewportInnerRect(),
        40.0,
        50.0,
        new WorldPoint(3210, 3212, 0),
        72,
        50,
        32
    );

    ScreenRect walkHereRect = menu.entryHoverRect(0);
    ScreenRect cancelRect = menu.entryHoverRect(1);

    assertThat(menu.entryIndexAt(menu.rect().left() + 2.0, menu.rect().top() + 2.0)).isEqualTo(-1);
    assertThat(menu.entryIndexAt(walkHereRect.left() + 2.0, walkHereRect.top() + 2.0)).isZero();
    assertThat(menu.entryIndexAt(cancelRect.left() + 2.0, cancelRect.top() + 2.0)).isEqualTo(1);
  }
}
