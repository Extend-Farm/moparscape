package io.github.ffakira.rsps.client.desktop.itemicon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

final class FaceSorter {

  List<ProjectedFace> sort(List<ProjectedFace> projectedFaces) {
    ArrayList<ProjectedFace> sortedFaces = new ArrayList<>(projectedFaces);
    sortedFaces.sort(
        Comparator.comparingInt(ProjectedFace::priority)
            .thenComparing(ProjectedFace::averageDepth, Comparator.reverseOrder())
            .thenComparingInt(ProjectedFace::faceIndex)
    );
    return sortedFaces;
  }
}
