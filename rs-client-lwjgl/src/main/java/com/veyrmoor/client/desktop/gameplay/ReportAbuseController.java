package com.veyrmoor.client.desktop.gameplay;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.content.InterfaceComponentCatalog;
import com.veyrmoor.content.InterfaceComponentDefinition;
import com.veyrmoor.model.StaffRole;
import java.util.HashMap;
import java.util.Map;

public final class ReportAbuseController {

  public static final int NAME_INPUT_TRIGGER_ID = 600;
  public static final int FIRST_RULE_TRIGGER_ID = 601;
  public static final int LAST_RULE_TRIGGER_ID = 613;
  public static final int MUTE_TOGGLE_TRIGGER_ID = 620;
  static final String EMPTY_NAME_PLACEHOLDER = "<click here to enter name>";
  static final int MAX_REPORTED_NAME_LENGTH = 12;

  private final int interfaceId;
  private final int interfaceWidth;
  private final int interfaceHeight;
  private final int nameFieldComponentId;
  private final int muteToggleComponentId;
  private final Map<Integer, Integer> actionTriggerIdsByComponentId;
  private final StringBuilder reportedName = new StringBuilder(MAX_REPORTED_NAME_LENGTH);
  private boolean open;
  private boolean muteRequested;

  public ReportAbuseController(InterfaceComponentCatalog interfaceComponents) {
    this(resolveInterface(interfaceComponents));
  }

  public ReportAbuseController(ResolvedInterface resolvedInterface) {
    interfaceId = resolvedInterface.interfaceId();
    interfaceWidth = resolvedInterface.width();
    interfaceHeight = resolvedInterface.height();
    nameFieldComponentId = resolvedInterface.nameFieldComponentId();
    muteToggleComponentId = resolvedInterface.muteToggleComponentId();
    actionTriggerIdsByComponentId = Map.copyOf(resolvedInterface.actionTriggerIdsByComponentId());
  }

  public boolean isAvailable() {
    return interfaceId >= 0 && interfaceWidth > 0 && interfaceHeight > 0;
  }

  public boolean isOpen() {
    return open;
  }

  public int interfaceId() {
    return interfaceId;
  }

  public int interfaceWidth() {
    return interfaceWidth;
  }

  public int interfaceHeight() {
    return interfaceHeight;
  }

  public int nameFieldComponentId() {
    return nameFieldComponentId;
  }

  public int muteToggleComponentId() {
    return muteToggleComponentId;
  }

  public String reportedName() {
    return reportedName.toString();
  }

  public boolean muteRequested() {
    return muteRequested;
  }

  public void open() {
    if (!isAvailable()) {
      return;
    }
    open = true;
    reportedName.setLength(0);
    muteRequested = false;
  }

  public void close() {
    open = false;
    reportedName.setLength(0);
    muteRequested = false;
  }

  public void appendCodePoint(int codePoint) {
    if (!open || !Character.isValidCodePoint(codePoint) || !isAllowedNameCodePoint(codePoint)) {
      return;
    }
    if (reportedName.codePointCount(0, reportedName.length()) >= MAX_REPORTED_NAME_LENGTH) {
      return;
    }
    reportedName.appendCodePoint(codePoint);
  }

  public void backspace() {
    if (!open || reportedName.isEmpty()) {
      return;
    }
    int nextEndIndex = reportedName.offsetByCodePoints(reportedName.length(), -1);
    reportedName.delete(nextEndIndex, reportedName.length());
  }

  public String nameFieldText() {
    if (reportedName.isEmpty()) {
      return EMPTY_NAME_PLACEHOLDER;
    }
    return reportedName + "*";
  }

  public boolean canToggleMute(ClientViewModel viewModel) {
    return staffRole(viewModel).id() >= StaffRole.MODERATOR.id();
  }

  public String muteToggleText(ClientViewModel viewModel) {
    if (!canToggleMute(viewModel)) {
      return "";
    }
    return muteRequested
        ? "Moderator option: Mute player for 48 hours: @gre@<ON>"
        : "Moderator option: Mute player for 48 hours: @red@<OFF>";
  }

  public int muteToggleRgb() {
    return 0xffffff;
  }

  public boolean handleActionWidget(int widgetId, ClientViewModel viewModel) {
    if (!open || widgetId < 0) {
      return false;
    }
    if (widgetId == muteToggleComponentId) {
      if (canToggleMute(viewModel)) {
        muteRequested = !muteRequested;
      }
      return true;
    }
    if (isRuleWidget(widgetId)) {
      close();
      return true;
    }
    close();
    return true;
  }

  static ResolvedInterface resolveInterface(InterfaceComponentCatalog interfaceComponents) {
    if (interfaceComponents == null) {
      return ResolvedInterface.unavailable();
    }
    for (int componentId = 0; componentId < interfaceComponents.size(); componentId++) {
      InterfaceComponentDefinition component = interfaceComponents.getOrNull(componentId);
      if (component == null || component.scriptTriggerId() != NAME_INPUT_TRIGGER_ID) {
        continue;
      }
      InterfaceComponentDefinition root = interfaceComponents.getOrNull(component.parentId());
      if (root == null) {
        return ResolvedInterface.unavailable();
      }
      int rootId = root.id();
      int nameFieldComponentId = component.id();
      int muteToggleComponentId = -1;
      Map<Integer, Integer> actionTriggerIdsByComponentId = new HashMap<>();
      for (int nestedComponentId = 0; nestedComponentId < interfaceComponents.size(); nestedComponentId++) {
        InterfaceComponentDefinition nestedComponent = interfaceComponents.getOrNull(nestedComponentId);
        if (nestedComponent == null || nestedComponent.parentId() != rootId) {
          continue;
        }
        if (nestedComponent.scriptTriggerId() == MUTE_TOGGLE_TRIGGER_ID) {
          muteToggleComponentId = nestedComponent.id();
          continue;
        }
        if (nestedComponent.scriptTriggerId() >= FIRST_RULE_TRIGGER_ID
            && nestedComponent.scriptTriggerId() <= LAST_RULE_TRIGGER_ID) {
          actionTriggerIdsByComponentId.put(nestedComponent.id(), nestedComponent.scriptTriggerId());
        }
      }
      return new ResolvedInterface(
          rootId,
          root.width(),
          root.height(),
          nameFieldComponentId,
          muteToggleComponentId,
          actionTriggerIdsByComponentId
      );
    }
    return ResolvedInterface.unavailable();
  }

  private static boolean isAllowedNameCodePoint(int codePoint) {
    return Character.isLetterOrDigit(codePoint) || codePoint == ' ';
  }

  private static StaffRole staffRole(ClientViewModel viewModel) {
    if (viewModel == null || viewModel.bootstrap() == null || viewModel.bootstrap().profile() == null) {
      return StaffRole.NONE;
    }
    return viewModel.bootstrap().profile().staffRole();
  }

  public record ResolvedInterface(
      int interfaceId,
      int width,
      int height,
      int nameFieldComponentId,
      int muteToggleComponentId,
      Map<Integer, Integer> actionTriggerIdsByComponentId
  ) {

    public ResolvedInterface {
      actionTriggerIdsByComponentId = actionTriggerIdsByComponentId == null
          ? Map.of()
          : Map.copyOf(actionTriggerIdsByComponentId);
    }

    public ResolvedInterface(int interfaceId, int width, int height) {
      this(interfaceId, width, height, NAME_INPUT_TRIGGER_ID, MUTE_TOGGLE_TRIGGER_ID, Map.of());
    }

    static ResolvedInterface unavailable() {
      return new ResolvedInterface(-1, 0, 0, -1, -1, Map.of());
    }
  }

  private boolean isRuleWidget(int widgetId) {
    Integer triggerId = actionTriggerIdsByComponentId.get(widgetId);
    return triggerId != null
        && triggerId >= FIRST_RULE_TRIGGER_ID
        && triggerId <= LAST_RULE_TRIGGER_ID;
  }
}
