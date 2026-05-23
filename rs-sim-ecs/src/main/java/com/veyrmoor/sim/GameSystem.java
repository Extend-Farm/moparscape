package com.veyrmoor.sim;

import com.veyrmoor.model.GameTick;
import java.util.List;

public interface GameSystem {

  List<WorldEvent> update(SimulationWorld world, GameTick tick);
}
