package io.github.ffakira.rsps.content;

import java.util.List;

public record ItemDefinition(
    int id,
    String name,
    String description,
    boolean stackable,
    int value,
    boolean membersOnly,
    boolean noted,
    int noteLinkItemId,
    int noteTemplateItemId,
    List<Integer> recolorSources,
    List<Integer> recolorTargets,
    List<Integer> maleBodyModelIds,
    int maleBodyOffsetY,
    List<Integer> femaleBodyModelIds,
    int femaleBodyOffsetY
) {

  public ItemDefinition {
    name = name == null ? "" : name;
    description = description == null ? "" : description;
    recolorSources = List.copyOf(recolorSources);
    recolorTargets = List.copyOf(recolorTargets);
    maleBodyModelIds = List.copyOf(maleBodyModelIds);
    femaleBodyModelIds = List.copyOf(femaleBodyModelIds);
  }

  public List<Integer> bodyModelIds(boolean female) {
    return female ? femaleBodyModelIds : maleBodyModelIds;
  }

  public int bodyOffsetY(boolean female) {
    return female ? femaleBodyOffsetY : maleBodyOffsetY;
  }
}
