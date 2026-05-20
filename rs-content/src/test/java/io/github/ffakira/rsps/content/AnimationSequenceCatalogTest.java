package io.github.ffakira.rsps.content;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.cache.AnimationFrameCatalog;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class AnimationSequenceCatalogTest {

  private static final int REFERENCE_STAND_SEQUENCE_ID = 808;
  private static final int REFERENCE_WALK_SEQUENCE_ID = 819;

  @Test
  void loadsReferencePlayerSequencesAndResolvesTheirFirstFrames() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    AnimationFrameCatalog animationFrames = AnimationFrameCatalog.load(manifest.cacheStore());
    AnimationSequenceCatalog animationSequences = AnimationSequenceCatalog.load(manifest, animationFrames);

    AnimationSequenceDefinition stand = animationSequences.require(REFERENCE_STAND_SEQUENCE_ID);
    AnimationSequenceDefinition walk = animationSequences.require(REFERENCE_WALK_SEQUENCE_ID);

    assertThat(stand.frameCount()).isGreaterThan(0);
    assertThat(walk.frameCount()).isGreaterThan(0);
    assertThat(stand.frameIdAt(0)).isGreaterThanOrEqualTo(0);
    assertThat(walk.frameIdAt(0)).isGreaterThanOrEqualTo(0);
    assertThat(animationFrames.find(stand.frameIdAt(0))).isPresent();
    assertThat(animationFrames.find(walk.frameIdAt(0))).isPresent();
  }
}
