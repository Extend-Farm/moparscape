package com.veyrmoor.client.desktop.character;

import com.veyrmoor.cache.RawModelData;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

final class CharacterSkinGroupCatalog {

  private final Map<RawModelData, SkinGroups> skinGroupsByModel = new ConcurrentHashMap<>();
  private final WeakIdentityCache<List<CharacterModelSourceBuilder.PreparedContribution>, CombinedSkinGroups>
      combinedSkinGroupsByContributions = new WeakIdentityCache<>();

  SkinGroups skinGroups(CharacterModelSourceBuilder.PreparedContribution contribution) {
    RawModelData rawModelData = contribution.contribution().rawModelData();
    SkinGroups skinGroups = skinGroupsByModel.computeIfAbsent(rawModelData, CharacterSkinGroupCatalog::buildSkinGroups);
    if (skinGroups.vertexGroups().length == 0 && skinGroups.faceGroups().length == 0) {
      return null;
    }
    return skinGroups;
  }

  CombinedSkinGroups combinedSkinGroups(List<CharacterModelSourceBuilder.PreparedContribution> contributions) {
    CombinedSkinGroups skinGroups = combinedSkinGroupsByContributions.computeIfAbsent(
        contributions,
        this::buildCombinedSkinGroups
    );
    if (skinGroups.vertexGroups().length == 0 && skinGroups.faceGroups().length == 0) {
      return null;
    }
    return skinGroups;
  }

  private CombinedSkinGroups buildCombinedSkinGroups(List<CharacterModelSourceBuilder.PreparedContribution> contributions) {
    ArrayList<SkinGroups> contributionSkinGroups = new ArrayList<>(contributions.size());
    int maxVertexLabel = -1;
    int maxFaceLabel = -1;
    for (CharacterModelSourceBuilder.PreparedContribution contribution : contributions) {
      SkinGroups skinGroups = skinGroups(contribution);
      contributionSkinGroups.add(skinGroups);
      if (skinGroups == null) {
        continue;
      }
      maxVertexLabel = Math.max(maxVertexLabel, skinGroups.vertexGroups().length - 1);
      maxFaceLabel = Math.max(maxFaceLabel, skinGroups.faceGroups().length - 1);
    }
    if (maxVertexLabel < 0 && maxFaceLabel < 0) {
      return new CombinedSkinGroups(new VertexReference[0][], new FaceReference[0][]);
    }
    ArrayList<ArrayList<VertexReference>> vertexGroups = new ArrayList<>(Math.max(0, maxVertexLabel + 1));
    for (int label = 0; label <= maxVertexLabel; label++) {
      vertexGroups.add(new ArrayList<>());
    }
    ArrayList<ArrayList<FaceReference>> faceGroups = new ArrayList<>(Math.max(0, maxFaceLabel + 1));
    for (int label = 0; label <= maxFaceLabel; label++) {
      faceGroups.add(new ArrayList<>());
    }
    for (int contributionIndex = 0; contributionIndex < contributions.size(); contributionIndex++) {
      SkinGroups skinGroups = contributionSkinGroups.get(contributionIndex);
      if (skinGroups == null) {
        continue;
      }
      for (int label = 0; label < skinGroups.vertexGroups().length; label++) {
        for (int vertexIndex : skinGroups.vertexGroups()[label]) {
          vertexGroups.get(label).add(new VertexReference(contributionIndex, vertexIndex));
        }
      }
      for (int label = 0; label < skinGroups.faceGroups().length; label++) {
        for (int faceIndex : skinGroups.faceGroups()[label]) {
          faceGroups.get(label).add(new FaceReference(contributionIndex, faceIndex));
        }
      }
    }
    return new CombinedSkinGroups(toVertexReferenceArrays(vertexGroups), toFaceReferenceArrays(faceGroups));
  }

  private static SkinGroups buildSkinGroups(RawModelData rawModelData) {
    return new SkinGroups(
        groupIndices(rawModelData.vertexSkins()),
        groupIndices(rawModelData.faceSkins())
    );
  }

  private static int[][] groupIndices(int[] labels) {
    if (labels == null || labels.length == 0) {
      return new int[0][];
    }
    int maxLabel = -1;
    for (int label : labels) {
      maxLabel = Math.max(maxLabel, label);
    }
    if (maxLabel < 0) {
      return new int[0][];
    }
    int[] counts = new int[maxLabel + 1];
    for (int label : labels) {
      counts[label]++;
    }
    int[][] groups = new int[maxLabel + 1][];
    for (int label = 0; label <= maxLabel; label++) {
      groups[label] = new int[counts[label]];
      counts[label] = 0;
    }
    for (int index = 0; index < labels.length; index++) {
      int label = labels[index];
      groups[label][counts[label]++] = index;
    }
    return groups;
  }

  private static VertexReference[][] toVertexReferenceArrays(ArrayList<ArrayList<VertexReference>> groups) {
    VertexReference[][] arrays = new VertexReference[groups.size()][];
    for (int index = 0; index < groups.size(); index++) {
      arrays[index] = groups.get(index).toArray(VertexReference[]::new);
    }
    return arrays;
  }

  private static FaceReference[][] toFaceReferenceArrays(ArrayList<ArrayList<FaceReference>> groups) {
    FaceReference[][] arrays = new FaceReference[groups.size()][];
    for (int index = 0; index < groups.size(); index++) {
      arrays[index] = groups.get(index).toArray(FaceReference[]::new);
    }
    return arrays;
  }

  record SkinGroups(int[][] vertexGroups, int[][] faceGroups) {
  }

  record CombinedSkinGroups(VertexReference[][] vertexGroups, FaceReference[][] faceGroups) {
  }

  record VertexReference(int contributionIndex, int vertexIndex) {
  }

  record FaceReference(int contributionIndex, int faceIndex) {
  }

  private static final class WeakIdentityCache<K, V> {
    private final ReferenceQueue<K> staleKeys = new ReferenceQueue<>();
    private final Map<IdentityReference<K>, V> values = new HashMap<>();

    private synchronized V computeIfAbsent(K key, Function<? super K, ? extends V> builder) {
      expungeStaleEntries();
      IdentityReference<K> lookup = new StrongIdentityKey<>(key);
      V existing = values.get(lookup);
      if (existing != null) {
        return existing;
      }
      V created = builder.apply(key);
      values.put(new WeakIdentityKey<>(key, staleKeys), created);
      return created;
    }

    @SuppressWarnings("unchecked")
    private void expungeStaleEntries() {
      WeakIdentityKey<K> staleKey;
      while ((staleKey = (WeakIdentityKey<K>) staleKeys.poll()) != null) {
        values.remove(staleKey);
      }
    }
  }

  private sealed interface IdentityReference<K> permits StrongIdentityKey, WeakIdentityKey {
    K referent();
  }

  private static final class StrongIdentityKey<K> implements IdentityReference<K> {
    private final K referent;
    private final int identityHash;

    private StrongIdentityKey(K referent) {
      this.referent = referent;
      this.identityHash = System.identityHashCode(referent);
    }

    @Override
    public K referent() {
      return referent;
    }

    @Override
    public int hashCode() {
      return identityHash;
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof IdentityReference<?> other && referent == other.referent();
    }
  }

  private static final class WeakIdentityKey<K> extends WeakReference<K> implements IdentityReference<K> {
    private final int identityHash;

    private WeakIdentityKey(K referent, ReferenceQueue<K> staleKeys) {
      super(referent, staleKeys);
      this.identityHash = System.identityHashCode(referent);
    }

    @Override
    public K referent() {
      return get();
    }

    @Override
    public int hashCode() {
      return identityHash;
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof IdentityReference<?> other && referent() == other.referent();
    }
  }
}
