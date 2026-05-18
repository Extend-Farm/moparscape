package io.github.ffakira.rsps.client.core;

import java.util.List;

public interface InputMapper {

  List<ClientCommand> mapInput(String inputActionId);
}
