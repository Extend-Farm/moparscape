package com.veyrmoor.client.core;

import java.util.List;

public interface InputMapper {

  List<ClientCommand> mapInput(String inputActionId);
}
