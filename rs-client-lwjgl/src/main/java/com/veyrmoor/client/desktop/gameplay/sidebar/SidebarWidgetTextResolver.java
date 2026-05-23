package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.content.InterfaceComponentDefinition;
import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.protocol.bootstrap.BootstrapProfile;

final class SidebarWidgetTextResolver {

  private static final int MAX_INTERPOLATED_VALUES = 5;
  private static final int WIDGET_VALUE_WILDCARD = 0x3b9ac9ff;

  private static final int OPCODE_END = 0;
  private static final int OPCODE_CURRENT_SKILL_LEVEL = 1;
  private static final int OPCODE_BASE_SKILL_LEVEL = 2;
  private static final int OPCODE_SKILL_EXPERIENCE = 3;
  private static final int OPCODE_BASE_LEVEL_EXPERIENCE = 6;
  private static final int OPCODE_COMBAT_LEVEL = 8;
  private static final int OPCODE_TOTAL_LEVEL = 9;
  private static final int OPCODE_RUN_ENERGY = 11;
  private static final int OPCODE_WORLD_X = 18;
  private static final int OPCODE_WORLD_Y = 19;
  private static final int OPCODE_LITERAL = 20;

  private static final int OPERATION_ADD = 0;
  private static final int OPERATION_SUBTRACT = 1;
  private static final int OPERATION_DIVIDE = 2;
  private static final int OPERATION_MULTIPLY = 3;

  private SidebarWidgetTextResolver() {
  }

  static String interpolate(
      InterfaceComponentDefinition component,
      String text,
      ClientViewModel viewModel
  ) {
    return interpolate(
        component,
        text,
        viewModel,
        viewModel == null ? null : GameplayStatsSidebarModel.from(viewModel)
    );
  }

  static String interpolate(
      InterfaceComponentDefinition component,
      String text,
      ClientViewModel viewModel,
      GameplayStatsSidebarModel statsModel
  ) {
    if (text == null || text.indexOf('%') < 0) {
      return text;
    }
    WidgetScriptContext scriptContext = new WidgetScriptContext(viewModel, statsModel);
    String resolved = text;
    for (int valueIndex = 0; valueIndex < MAX_INTERPOLATED_VALUES; valueIndex++) {
      String token = "%" + (valueIndex + 1);
      if (resolved.indexOf(token) >= 0) {
        resolved = resolved.replace(token, formatWidgetValue(evaluateClientScript(component, valueIndex, scriptContext)));
      }
    }
    return resolved;
  }

  private static String formatWidgetValue(int value) {
    return value < WIDGET_VALUE_WILDCARD ? Integer.toString(value) : "*";
  }

  // Sidebar text widgets use the reference client's compact client-script opcodes for `%1..%5`.
  // The native client only exposes a subset of that runtime state today, so unsupported opcodes
  // intentionally resolve against the available bootstrap snapshot instead of leaving raw tokens onscreen.
  private static int evaluateClientScript(
      InterfaceComponentDefinition component,
      int valueIndex,
      WidgetScriptContext context
  ) {
    if (component == null || valueIndex < 0 || valueIndex >= component.clientScripts().length) {
      return -2;
    }
    int[] script = component.clientScripts()[valueIndex];
    if (script == null || script.length == 0) {
      return -2;
    }
    try {
      int accumulator = 0;
      int pointer = 0;
      int pendingOperation = OPERATION_ADD;
      while (pointer < script.length) {
        int opcode = script[pointer++];
        int value = 0;
        int nextOperation = OPERATION_ADD;
        switch (opcode) {
          case OPCODE_END -> {
            return accumulator;
          }
          case OPCODE_CURRENT_SKILL_LEVEL -> value = currentSkillLevel(context.statsModel(), script[pointer++]);
          case OPCODE_BASE_SKILL_LEVEL -> value = baseSkillLevel(context.statsModel(), script[pointer++]);
          case OPCODE_SKILL_EXPERIENCE -> value = skillExperience(context.statsModel(), script[pointer++]);
          case 4, 10, 13 -> pointer += 2;
          case 5, 7, 14 -> pointer++;
          case OPCODE_BASE_LEVEL_EXPERIENCE ->
              value = GameplayStatsSidebarModel.experienceForLevel(baseSkillLevel(context.statsModel(), script[pointer++]));
          case OPCODE_COMBAT_LEVEL -> value = combatLevel(context.statsModel());
          case OPCODE_TOTAL_LEVEL -> value = totalLevel(context.statsModel());
          case OPCODE_RUN_ENERGY -> value = runEnergy(context.viewModel());
          case 12 -> value = 0;
          case 15 -> nextOperation = OPERATION_SUBTRACT;
          case 16 -> nextOperation = OPERATION_DIVIDE;
          case 17 -> nextOperation = OPERATION_MULTIPLY;
          case OPCODE_WORLD_X -> value = worldCoordinate(context.viewModel(), true);
          case OPCODE_WORLD_Y -> value = worldCoordinate(context.viewModel(), false);
          case OPCODE_LITERAL -> value = script[pointer++];
          default -> value = 0;
        }
        if (nextOperation == OPERATION_ADD) {
          accumulator = applyPendingOperation(accumulator, pendingOperation, value);
          pendingOperation = OPERATION_ADD;
          continue;
        }
        pendingOperation = nextOperation;
      }
      return accumulator;
    } catch (RuntimeException exception) {
      return -1;
    }
  }

  private static int applyPendingOperation(int accumulator, int pendingOperation, int value) {
    return switch (pendingOperation) {
      case OPERATION_SUBTRACT -> accumulator - value;
      case OPERATION_DIVIDE -> value == 0 ? accumulator : accumulator / value;
      case OPERATION_MULTIPLY -> accumulator * value;
      default -> accumulator + value;
    };
  }

  private static int currentSkillLevel(GameplayStatsSidebarModel statsModel, int skillId) {
    GameplayStatsSidebarModel.Entry entry = skillEntry(statsModel, skillId);
    return entry == null ? 0 : entry.currentLevel();
  }

  private static int baseSkillLevel(GameplayStatsSidebarModel statsModel, int skillId) {
    GameplayStatsSidebarModel.Entry entry = skillEntry(statsModel, skillId);
    return entry == null ? 0 : entry.baseLevel();
  }

  private static int skillExperience(GameplayStatsSidebarModel statsModel, int skillId) {
    GameplayStatsSidebarModel.Entry entry = skillEntry(statsModel, skillId);
    return entry == null ? 0 : entry.experience();
  }

  private static GameplayStatsSidebarModel.Entry skillEntry(GameplayStatsSidebarModel statsModel, int skillId) {
    return statsModel == null ? null : statsModel.entryForSkill(skillId);
  }

  private static int combatLevel(GameplayStatsSidebarModel statsModel) {
    return statsModel == null ? 0 : statsModel.combatLevel();
  }

  private static int totalLevel(GameplayStatsSidebarModel statsModel) {
    return statsModel == null ? 0 : statsModel.totalLevel();
  }

  private static int runEnergy(ClientViewModel viewModel) {
    BootstrapProfile profile = viewModel != null && viewModel.bootstrap() != null ? viewModel.bootstrap().profile() : null;
    return profile == null ? 0 : Math.max(0, profile.runEnergy());
  }

  private static int worldCoordinate(ClientViewModel viewModel, boolean xCoordinate) {
    WorldPoint worldPoint = viewModel == null ? null : viewModel.localPlayerPosition();
    if (worldPoint == null && viewModel != null && viewModel.bootstrap() != null) {
      worldPoint = viewModel.bootstrap().worldPoint();
    }
    if (worldPoint == null) {
      return 0;
    }
    return xCoordinate ? worldPoint.x() : worldPoint.y();
  }

  private record WidgetScriptContext(
      ClientViewModel viewModel,
      GameplayStatsSidebarModel statsModel
  ) {
  }
}
