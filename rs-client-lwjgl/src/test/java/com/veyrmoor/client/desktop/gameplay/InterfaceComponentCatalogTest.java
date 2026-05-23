package com.veyrmoor.client.desktop.gameplay;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.cache.CacheStoreReader;
import com.veyrmoor.content.ContentBootstrapService;
import com.veyrmoor.content.ContentManifest;
import com.veyrmoor.content.InterfaceComponentCatalog;
import com.veyrmoor.content.InterfaceComponentDefinition;
import com.veyrmoor.content.TopLevelArchiveId;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class InterfaceComponentCatalogTest {

  private static final int[] COMBAT_ROOT_INTERFACES = {328, 1764, 2423, 5570, 5855, 12290};
  private static final int EQUIPMENT_ROOT_INTERFACE = 1644;
  private static final int QUEST_ROOT_INTERFACE = 638;
  private static final int QUEST_SCROLL_CONTAINER = 639;
  private static final int FRIENDS_ROOT_INTERFACE = 5065;
  private static final int FRIENDS_SCROLL_CONTAINER = 5066;
  private static final int IGNORES_ROOT_INTERFACE = 5715;
  private static final int IGNORES_SCROLL_CONTAINER = 5716;
  private static final int SETTINGS_ROOT_INTERFACE = 4445;
  private static final int EMOTES_ROOT_INTERFACE = 147;
  private static final int EMOTES_SCROLL_CONTAINER = 665;
  private static final int PRAYER_ROOT_INTERFACE = 5608;
  private static final int MODERN_MAGIC_ROOT_INTERFACE = 1151;
  private static final int MODERN_MAGIC_SPELLBOOK_CONTAINER = 12424;
  private static final int MUSIC_ROOT_INTERFACE = 962;
  private static final int MUSIC_TRACK_LIST_CONTAINER = 4262;
  private static final int LOGOUT_ROOT_INTERFACE = 2449;
  private static final int REPORT_ABUSE_NAME_TRIGGER_ID = 600;

  private static InterfaceComponentCatalog catalog;
  private static Object[] referenceWidgets;

  @BeforeAll
  static void bootstrapReferenceCatalog() throws Exception {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    catalog = InterfaceComponentCatalog.load(manifest);

    byte[] interfaceArchiveBytes;
    byte[] mediaArchiveBytes;
    try (CacheStoreReader reader = new CacheStoreReader(manifest.cacheStore())) {
      interfaceArchiveBytes = reader.readArchive(0, TopLevelArchiveId.INTERFACE.archiveId()).bytes();
      mediaArchiveBytes = reader.readArchive(0, TopLevelArchiveId.MEDIA.archiveId()).bytes();
    }

    Class<?> archiveClass = Class.forName("io.github.ffakira.moparscape.cache.Archive");
    Object interfaceArchive = archiveClass.getConstructor(int.class, byte[].class).newInstance(44820, interfaceArchiveBytes);
    Object mediaArchive = archiveClass.getConstructor(int.class, byte[].class).newInstance(44820, mediaArchiveBytes);

    Class<?> widgetClass = Class.forName("io.github.ffakira.moparscape.client.Widget");
    Method decodeMethod = widgetClass.getDeclaredMethod(
        "method205",
        archiveClass,
        Class.forName("[Lio.github.ffakira.moparscape.client.FontRenderer;"),
        byte.class,
        archiveClass
    );
    decodeMethod.setAccessible(true);
    decodeMethod.invoke(null, interfaceArchive, null, (byte) -84, mediaArchive);

    Field widgetsField = widgetClass.getDeclaredField("aClass9Array210");
    widgetsField.setAccessible(true);
    referenceWidgets = (Object[]) widgetsField.get(null);
  }

  @Test
  void decodesCombatSidebarRootsAndDescendantsLikeTheReferenceClient() throws Exception {
    for (int rootId : COMBAT_ROOT_INTERFACES) {
      compareSubtree(rootId, new HashSet<>());
    }
  }

  @Test
  void combatSidebarInterfacesStayWithinTheCurrentNativeWidgetRenderSubset() {
    Set<Integer> componentTypes = new HashSet<>();
    for (int rootId : COMBAT_ROOT_INTERFACES) {
      collectComponentTypes(rootId, componentTypes, new HashSet<>());
    }
    assertThat(componentTypes).containsOnly(0, 3, 4, 5, 6);
  }

  @Test
  void decodesLogoutSidebarRootAndDescendantsLikeTheReferenceClient() throws Exception {
    compareSubtree(LOGOUT_ROOT_INTERFACE, new HashSet<>());
  }

  @Test
  void decodesPrayerSidebarRootAndDescendantsLikeTheReferenceClient() throws Exception {
    compareSubtree(PRAYER_ROOT_INTERFACE, new HashSet<>());
  }

  @Test
  void decodesEquipmentSidebarRootAndDescendantsLikeTheReferenceClient() throws Exception {
    compareSubtree(EQUIPMENT_ROOT_INTERFACE, new HashSet<>());
  }

  @Test
  void decodesQuestSidebarRootAndDescendantsLikeTheReferenceClient() throws Exception {
    compareSubtree(QUEST_ROOT_INTERFACE, new HashSet<>());
  }

  @Test
  void decodesFriendsSidebarRootAndDescendantsLikeTheReferenceClient() throws Exception {
    compareSubtree(FRIENDS_ROOT_INTERFACE, new HashSet<>());
  }

  @Test
  void decodesIgnoresSidebarRootAndDescendantsLikeTheReferenceClient() throws Exception {
    compareSubtree(IGNORES_ROOT_INTERFACE, new HashSet<>());
  }

  @Test
  void decodesSettingsSidebarRootAndDescendantsLikeTheReferenceClient() throws Exception {
    compareSubtree(SETTINGS_ROOT_INTERFACE, new HashSet<>());
  }

  @Test
  void decodesEmotesSidebarRootAndDescendantsLikeTheReferenceClient() throws Exception {
    compareSubtree(EMOTES_ROOT_INTERFACE, new HashSet<>());
  }

  @Test
  void settingsSidebarInterfaceStaysWithinTheCurrentNativeWidgetRenderSubset() {
    Set<Integer> componentTypes = new HashSet<>();
    collectComponentTypes(SETTINGS_ROOT_INTERFACE, componentTypes, new HashSet<>());
    assertThat(componentTypes).allMatch(componentType -> componentType == 0
        || componentType == 4
        || componentType == 5);
  }

  @Test
  void decodesMusicSidebarRootAndDescendantsLikeTheReferenceClient() throws Exception {
    compareSubtree(MUSIC_ROOT_INTERFACE, new HashSet<>());
  }

  @Test
  void decodesModernMagicSidebarRootAndDescendantsLikeTheReferenceClient() throws Exception {
    compareSubtree(MODERN_MAGIC_ROOT_INTERFACE, new HashSet<>());
  }

  @Test
  void decodesReportAbuseRootAndDescendantsLikeTheReferenceClient() throws Exception {
    compareSubtree(reportAbuseRootInterfaceId(), new HashSet<>());
  }

  @Test
  void logoutSidebarInterfaceStaysWithinTheCurrentNativeWidgetRenderSubset() {
    Set<Integer> componentTypes = new HashSet<>();
    collectComponentTypes(LOGOUT_ROOT_INTERFACE, componentTypes, new HashSet<>());
    assertThat(componentTypes).allMatch(componentType -> componentType == 0
        || componentType == 3
        || componentType == 4
        || componentType == 5
        || componentType == 6);
  }

  @Test
  void prayerSidebarInterfaceStaysWithinTheCurrentNativeWidgetRenderSubset() {
    Set<Integer> componentTypes = new HashSet<>();
    collectComponentTypes(PRAYER_ROOT_INTERFACE, componentTypes, new HashSet<>());
    assertThat(componentTypes).allMatch(componentType -> componentType == 0
        || componentType == 3
        || componentType == 4
        || componentType == 5
        || componentType == 6);
  }

  @Test
  void prayerSidebarContainsHoverTargetsForIconDescriptions() {
    assertThat(hasHoverTarget(PRAYER_ROOT_INTERFACE, new HashSet<>())).isTrue();
  }

  @Test
  void equipmentSidebarInterfaceStaysWithinTheCurrentNativeWidgetRenderSubset() {
    Set<Integer> componentTypes = new HashSet<>();
    collectComponentTypes(EQUIPMENT_ROOT_INTERFACE, componentTypes, new HashSet<>());
    assertThat(componentTypes).allMatch(componentType -> componentType == 0
        || componentType == 2
        || componentType == 3
        || componentType == 4
        || componentType == 5);
  }

  @Test
  void questSidebarInterfaceStaysWithinTheCurrentNativeWidgetRenderSubset() {
    Set<Integer> componentTypes = new HashSet<>();
    collectComponentTypes(QUEST_ROOT_INTERFACE, componentTypes, new HashSet<>());
    assertThat(componentTypes).allMatch(componentType -> componentType == 0
        || componentType == 4
        || componentType == 5);
  }

  @Test
  void friendsSidebarInterfaceStaysWithinTheCurrentNativeWidgetRenderSubset() {
    Set<Integer> componentTypes = new HashSet<>();
    collectComponentTypes(FRIENDS_ROOT_INTERFACE, componentTypes, new HashSet<>());
    assertThat(componentTypes).allMatch(componentType -> componentType == 0
        || componentType == 4
        || componentType == 5);
  }

  @Test
  void ignoresSidebarInterfaceStaysWithinTheCurrentNativeWidgetRenderSubset() {
    Set<Integer> componentTypes = new HashSet<>();
    collectComponentTypes(IGNORES_ROOT_INTERFACE, componentTypes, new HashSet<>());
    assertThat(componentTypes).allMatch(componentType -> componentType == 0
        || componentType == 4
        || componentType == 5);
  }

  @Test
  void emotesSidebarInterfaceStaysWithinTheCurrentNativeWidgetRenderSubset() {
    Set<Integer> componentTypes = new HashSet<>();
    collectComponentTypes(EMOTES_ROOT_INTERFACE, componentTypes, new HashSet<>());
    assertThat(componentTypes).allMatch(componentType -> componentType == 0
        || componentType == 4
        || componentType == 5);
  }

  @Test
  void musicSidebarInterfaceStaysWithinTheCurrentNativeWidgetRenderSubset() {
    Set<Integer> componentTypes = new HashSet<>();
    collectComponentTypes(MUSIC_ROOT_INTERFACE, componentTypes, new HashSet<>());
    assertThat(componentTypes).allMatch(componentType -> componentType == 0
        || componentType == 4
        || componentType == 5
        || componentType == 6);
  }

  @Test
  void modernMagicSidebarInterfaceStaysWithinTheCurrentNativeWidgetRenderSubset() {
    Set<Integer> componentTypes = new HashSet<>();
    collectComponentTypes(MODERN_MAGIC_ROOT_INTERFACE, componentTypes, new HashSet<>());
    assertThat(componentTypes).allMatch(componentType -> componentType == 0
        || componentType == 3
        || componentType == 4
        || componentType == 5
        || componentType == 6);
  }

  @Test
  void reportAbuseInterfaceStaysWithinTheCurrentNativeWidgetRenderSubset() {
    Set<Integer> componentTypes = new HashSet<>();
    collectComponentTypes(reportAbuseRootInterfaceId(), componentTypes, new HashSet<>());
    assertThat(componentTypes).allMatch(componentType -> componentType == 0
        || componentType == 3
        || componentType == 4
        || componentType == 5
        || componentType == 6);
  }

  @Test
  void modernMagicSpellbookContainerRequiresScrollbar() throws Exception {
    InterfaceComponentDefinition container = catalog.require(MODERN_MAGIC_SPELLBOOK_CONTAINER);
    Object referenceWidget = referenceWidgets[MODERN_MAGIC_SPELLBOOK_CONTAINER];

    assertThat(container.componentType()).isZero();
    assertThat(container.height()).isEqualTo(167);
    assertThat(container.container().scrollContentHeight()).isEqualTo(215);
    assertThat(container.container().scrollContentHeight()).isGreaterThan(container.height());
    assertThat(container.container().scrollContentHeight()).isEqualTo(readInt(referenceWidget, "anInt261"));
  }

  @Test
  void musicTrackListContainerRequiresScrollbar() throws Exception {
    InterfaceComponentDefinition container = catalog.require(MUSIC_TRACK_LIST_CONTAINER);
    Object referenceWidget = referenceWidgets[MUSIC_TRACK_LIST_CONTAINER];

    assertThat(container.componentType()).isZero();
    assertThat(container.height()).isEqualTo(190);
    assertThat(container.container().scrollContentHeight()).isGreaterThan(container.height());
    assertThat(container.container().scrollContentHeight()).isEqualTo(readInt(referenceWidget, "anInt261"));
  }

  @Test
  void emotesScrollContainerRequiresScrollbar() throws Exception {
    InterfaceComponentDefinition container = catalog.require(EMOTES_SCROLL_CONTAINER);
    Object referenceWidget = referenceWidgets[EMOTES_SCROLL_CONTAINER];

    assertThat(container.componentType()).isZero();
    assertThat(container.height()).isEqualTo(91);
    assertThat(container.container().scrollContentHeight()).isGreaterThan(container.height());
    assertThat(container.container().scrollContentHeight()).isEqualTo(readInt(referenceWidget, "anInt261"));
  }

  @Test
  void friendsScrollContainerRequiresScrollbar() throws Exception {
    InterfaceComponentDefinition container = catalog.require(FRIENDS_SCROLL_CONTAINER);
    Object referenceWidget = referenceWidgets[FRIENDS_SCROLL_CONTAINER];

    assertThat(container.componentType()).isZero();
    assertThat(container.height()).isEqualTo(204);
    assertThat(container.container().scrollContentHeight()).isGreaterThan(container.height());
    assertThat(container.container().scrollContentHeight()).isEqualTo(readInt(referenceWidget, "anInt261"));
  }

  @Test
  void ignoresScrollContainerRequiresScrollbar() throws Exception {
    InterfaceComponentDefinition container = catalog.require(IGNORES_SCROLL_CONTAINER);
    Object referenceWidget = referenceWidgets[IGNORES_SCROLL_CONTAINER];

    assertThat(container.componentType()).isZero();
    assertThat(container.height()).isEqualTo(204);
    assertThat(container.container().scrollContentHeight()).isGreaterThan(container.height());
    assertThat(container.container().scrollContentHeight()).isEqualTo(readInt(referenceWidget, "anInt261"));
  }

  @Test
  void questScrollContainerRequiresScrollbar() throws Exception {
    InterfaceComponentDefinition container = catalog.require(QUEST_SCROLL_CONTAINER);
    Object referenceWidget = referenceWidgets[QUEST_SCROLL_CONTAINER];

    assertThat(container.componentType()).isZero();
    assertThat(container.height()).isEqualTo(229);
    assertThat(container.container().scrollContentHeight()).isGreaterThan(container.height());
    assertThat(container.container().scrollContentHeight()).isEqualTo(readInt(referenceWidget, "anInt261"));
  }

  @Test
  void logoutSidebarContainsAtLeastOneActionableComponent() {
    assertThat(hasActionableComponent(LOGOUT_ROOT_INTERFACE, new HashSet<>())).isTrue();
  }

  @Test
  void questSidebarContainsActionableEntries() {
    assertThat(hasActionableComponent(QUEST_ROOT_INTERFACE, new HashSet<>())).isTrue();
  }

  @Test
  void friendsSidebarContainsActionableButtonsAndEntries() {
    assertThat(hasActionableComponent(FRIENDS_ROOT_INTERFACE, new HashSet<>())).isTrue();
  }

  @Test
  void ignoresSidebarContainsActionableButtonsAndEntries() {
    assertThat(hasActionableComponent(IGNORES_ROOT_INTERFACE, new HashSet<>())).isTrue();
  }

  @Test
  void settingsSidebarContainsActionableButtons() {
    assertThat(hasActionableComponent(SETTINGS_ROOT_INTERFACE, new HashSet<>())).isTrue();
  }

  @Test
  void emotesSidebarContainsActionableButtonsAndEmotes() {
    assertThat(hasActionableComponent(EMOTES_ROOT_INTERFACE, new HashSet<>())).isTrue();
  }

  @Test
  void musicSidebarContainsActionableTrackEntries() {
    assertThat(hasActionableComponent(MUSIC_ROOT_INTERFACE, new HashSet<>())).isTrue();
  }

  private void compareSubtree(int componentId, Set<Integer> visited) throws Exception {
    if (!visited.add(componentId)) {
      return;
    }
    InterfaceComponentDefinition component = catalog.require(componentId);
    Object referenceWidget = referenceWidgets[componentId];
    assertThat(referenceWidget).as("reference widget %s", componentId).isNotNull();

    assertThat(component.id()).isEqualTo(readInt(referenceWidget, "anInt250"));
    assertThat(component.parentId()).isEqualTo(readInt(referenceWidget, "anInt236"));
    assertThat(component.componentType()).isEqualTo(readInt(referenceWidget, "anInt262"));
    assertThat(component.optionType()).isEqualTo(readInt(referenceWidget, "anInt217"));
    assertThat(component.width()).isEqualTo(readInt(referenceWidget, "anInt220"));
    assertThat(component.height()).isEqualTo(readInt(referenceWidget, "anInt267"));
    assertThat(component.alpha()).isEqualTo(readByte(referenceWidget, "aByte254") & 0xff);
    assertThat(component.hoverTargetId()).isEqualTo(readInt(referenceWidget, "anInt230"));

    assertThat(component.container().scrollContentHeight()).isEqualTo(readInt(referenceWidget, "anInt261"));
    assertThat(component.container().hidden()).isEqualTo(readBoolean(referenceWidget, "aBoolean266"));
    assertThat(component.container().childIds()).isEqualTo(readIntArray(referenceWidget, "anIntArray240"));
    assertThat(component.container().childX()).isEqualTo(readIntArray(referenceWidget, "anIntArray241"));
    assertThat(component.container().childY()).isEqualTo(readIntArray(referenceWidget, "anIntArray272"));

    assertThat(component.inventoryGrid().swappable()).isEqualTo(readBoolean(referenceWidget, "aBoolean259"));
    assertThat(component.inventoryGrid().usable()).isEqualTo(readBoolean(referenceWidget, "aBoolean249"));
    assertThat(component.inventoryGrid().useTarget()).isEqualTo(readBoolean(referenceWidget, "aBoolean242"));
    assertThat(component.inventoryGrid().depthFlag()).isEqualTo(readBoolean(referenceWidget, "aBoolean235"));
    assertThat(component.inventoryGrid().paddingX()).isEqualTo(readInt(referenceWidget, "anInt231"));
    assertThat(component.inventoryGrid().paddingY()).isEqualTo(readInt(referenceWidget, "anInt244"));
    assertThat(component.inventoryGrid().options()).isEqualTo(readStringArray(referenceWidget, "aStringArray225"));

    assertThat(component.textBlock().centered()).isEqualTo(readBoolean(referenceWidget, "aBoolean223"));
    assertThat(component.textBlock().shadow()).isEqualTo(readBoolean(referenceWidget, "aBoolean268"));
    assertThat(component.textBlock().defaultText()).isEqualTo(readString(referenceWidget, "aString248"));
    assertThat(component.textBlock().activeText()).isEqualTo(readString(referenceWidget, "aString228"));

    assertThat(component.colors().defaultColor()).isEqualTo(readInt(referenceWidget, "anInt232"));
    assertThat(component.colors().activeColor()).isEqualTo(readInt(referenceWidget, "anInt219"));
    assertThat(component.colors().hoverColor()).isEqualTo(readInt(referenceWidget, "anInt216"));
    assertThat(component.colors().activeHoverColor()).isEqualTo(readInt(referenceWidget, "anInt239"));

    assertThat(component.horizontalFill()).isEqualTo(readBoolean(referenceWidget, "aBoolean227"));
    assertThat(component.staticModelId()).isEqualTo(readInt(referenceWidget, "anInt211"));
    assertThat(component.staticModelEnabled()).isEqualTo(readBoolean(referenceWidget, "aBoolean251"));

    if (component.componentType() == 2) {
      assertThat(component.inventoryGrid().slotDecorations()).hasSize(readIntArray(referenceWidget, "anIntArray215").length);
      int[] slotDecorationOffsetX = readIntArray(referenceWidget, "anIntArray215");
      int[] slotDecorationOffsetY = readIntArray(referenceWidget, "anIntArray247");
      Object[] slotDecorationSprites = readObjectArray(referenceWidget, "aClass30_Sub2_Sub1_Sub1Array209");
      for (int slotIndex = 0; slotIndex < slotDecorationOffsetX.length; slotIndex++) {
        InterfaceComponentDefinition.SlotDecoration slotDecoration = component.inventoryGrid().slotDecorations()[slotIndex];
        if (slotDecorationSprites[slotIndex] == null
            && slotDecorationOffsetX[slotIndex] == 0
            && slotDecorationOffsetY[slotIndex] == 0) {
          continue;
        }
        assertThat(slotDecoration).isNotNull();
        assertThat(slotDecoration.offsetX()).isEqualTo(slotDecorationOffsetX[slotIndex]);
        assertThat(slotDecoration.offsetY()).isEqualTo(slotDecorationOffsetY[slotIndex]);
      }
    }

    assertThat(component.modelDisplay().defaultSourceKind()).isEqualTo(readInt(referenceWidget, "anInt233"));
    assertThat(component.modelDisplay().defaultModelId()).isEqualTo(readInt(referenceWidget, "anInt234"));
    assertThat(component.modelDisplay().activeSourceKind()).isEqualTo(readInt(referenceWidget, "anInt255"));
    assertThat(component.modelDisplay().activeModelId()).isEqualTo(readInt(referenceWidget, "anInt256"));
    assertThat(component.modelDisplay().defaultAnimationId()).isEqualTo(readInt(referenceWidget, "anInt257"));
    assertThat(component.modelDisplay().activeAnimationId()).isEqualTo(readInt(referenceWidget, "anInt258"));
    assertThat(component.modelDisplay().zoom()).isEqualTo(readInt(referenceWidget, "anInt269"));
    assertThat(component.modelDisplay().yaw()).isEqualTo(readInt(referenceWidget, "anInt270"));
    assertThat(component.modelDisplay().roll()).isEqualTo(readInt(referenceWidget, "anInt271"));

    for (int childId : component.container().childIds()) {
      compareSubtree(childId, visited);
    }
  }

  private void collectComponentTypes(int componentId, Set<Integer> componentTypes, Set<Integer> visited) {
    if (!visited.add(componentId)) {
      return;
    }
    InterfaceComponentDefinition component = catalog.require(componentId);
    componentTypes.add(component.componentType());
    for (int childId : component.container().childIds()) {
      collectComponentTypes(childId, componentTypes, visited);
    }
  }

  private boolean hasActionableComponent(int componentId, Set<Integer> visited) {
    if (!visited.add(componentId)) {
      return false;
    }
    InterfaceComponentDefinition component = catalog.require(componentId);
    if (component.optionType() != 0 || !component.actionLabel().isEmpty()) {
      return true;
    }
    for (int childId : component.container().childIds()) {
      if (hasActionableComponent(childId, visited)) {
        return true;
      }
    }
    return false;
  }

  private boolean hasHoverTarget(int componentId, Set<Integer> visited) {
    if (!visited.add(componentId)) {
      return false;
    }
    InterfaceComponentDefinition component = catalog.require(componentId);
    if (component.hoverTargetId() >= 0 || component.colors().hoverColor() != 0) {
      return true;
    }
    for (int childId : component.container().childIds()) {
      if (hasHoverTarget(childId, visited)) {
        return true;
      }
    }
    return false;
  }

  private int reportAbuseRootInterfaceId() {
    for (int componentId = 0; componentId < catalog.size(); componentId++) {
      InterfaceComponentDefinition component = catalog.getOrNull(componentId);
      if (component != null && component.scriptTriggerId() == REPORT_ABUSE_NAME_TRIGGER_ID) {
        return component.parentId();
      }
    }
    throw new AssertionError("Expected report abuse interface in the cache-backed component catalog");
  }

  private int readInt(Object target, String fieldName) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.getInt(target);
  }

  private byte readByte(Object target, String fieldName) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.getByte(target);
  }

  private boolean readBoolean(Object target, String fieldName) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.getBoolean(target);
  }

  private String readString(Object target, String fieldName) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    Object value = field.get(target);
    return value == null ? "" : (String) value;
  }

  private int[] readIntArray(Object target, String fieldName) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    Object value = field.get(target);
    if (value == null) {
      return new int[0];
    }
    int length = Array.getLength(value);
    int[] copy = new int[length];
    for (int index = 0; index < length; index++) {
      copy[index] = Array.getInt(value, index);
    }
    return copy;
  }

  private String[] readStringArray(Object target, String fieldName) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    Object value = field.get(target);
    if (value == null) {
      return new String[0];
    }
    int length = Array.getLength(value);
    String[] copy = new String[length];
    for (int index = 0; index < length; index++) {
      Object entry = Array.get(value, index);
      copy[index] = entry == null ? null : entry.toString();
    }
    return copy;
  }

  private Object[] readObjectArray(Object target, String fieldName) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    Object value = field.get(target);
    if (value == null) {
      return new Object[0];
    }
    int length = Array.getLength(value);
    Object[] copy = new Object[length];
    for (int index = 0; index < length; index++) {
      copy[index] = Array.get(value, index);
    }
    return copy;
  }
}
