package io.github.ffakira.rsps.sim;

import io.github.ffakira.rsps.model.GameTick;
import java.util.List;

public interface GameSystem {

  List<WorldEvent> update(SimulationWorld world, GameTick tick);
}
