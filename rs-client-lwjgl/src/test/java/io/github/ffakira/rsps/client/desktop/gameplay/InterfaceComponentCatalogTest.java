package io.github.ffakira.rsps.client.desktop.gameplay;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.cache.CacheStoreReader;
import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.ContentManifest;
import io.github.ffakira.rsps.content.InterfaceComponentCatalog;
import io.github.ffakira.rsps.content.InterfaceComponentDefinition;
import io.github.ffakira.rsps.content.TopLevelArchiveId;
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

  private static InterfaceComponentCatalog catalog;
  private static Object[] referenceWidgets;

  @BeforeAll
  static void bootstrapReferenceCatalog() throws Exception {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    catalog = InterfaceComponentCatalog.load(manifest);

    byte[] interfaceArchiveBytes;
    try (CacheStoreReader reader = new CacheStoreReader(manifest.cacheStore())) {
      interfaceArchiveBytes = reader.readArchive(0, TopLevelArchiveId.INTERFACE.archiveId()).bytes();
    }

    Class<?> archiveClass = Class.forName("io.github.ffakira.moparscape.cache.Archive");
    Object interfaceArchive = archiveClass.getConstructor(int.class, byte[].class).newInstance(44820, interfaceArchiveBytes);

    Class<?> widgetClass = Class.forName("io.github.ffakira.moparscape.client.Widget");
    Method decodeMethod = widgetClass.getDeclaredMethod(
        "method205",
        archiveClass,
        Class.forName("[Lio.github.ffakira.moparscape.client.FontRenderer;"),
        byte.class,
        archiveClass
    );
    decodeMethod.setAccessible(true);
    decodeMethod.invoke(null, interfaceArchive, null, (byte) -84, null);

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
}
