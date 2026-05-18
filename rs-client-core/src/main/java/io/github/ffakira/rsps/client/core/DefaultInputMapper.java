package io.github.ffakira.rsps.client.core;

import io.github.ffakira.rsps.model.MovementMode;
import java.util.List;

public final class DefaultInputMapper implements InputMapper {

  @Override
  public List<ClientCommand> mapInput(String inputActionId) {
    return switch (inputActionId) {
      case "move-north" -> List.of(new MoveLocalPlayerCommand(0, 1, MovementMode.WALK));
      case "move-south" -> List.of(new MoveLocalPlayerCommand(0, -1, MovementMode.WALK));
      case "move-west" -> List.of(new MoveLocalPlayerCommand(-1, 0, MovementMode.WALK));
      case "move-east" -> List.of(new MoveLocalPlayerCommand(1, 0, MovementMode.WALK));
      default -> List.of();
    };
  }
}
