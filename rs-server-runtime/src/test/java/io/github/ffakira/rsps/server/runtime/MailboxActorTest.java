package io.github.ffakira.rsps.server.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class MailboxActorTest {

  @Test
  void processesMessagesInMailboxOrder() throws Exception {
    CountDownLatch latch = new CountDownLatch(3);
    RecordingActor actor = new RecordingActor(latch);
    actor.start();
    actor.tell(new RecordingMessage(1));
    actor.tell(new RecordingMessage(2));
    actor.tell(new RecordingMessage(3));

    try {
      assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue();
      assertThat(actor.receivedValues()).containsExactly(1, 2, 3);
    } finally {
      actor.close();
    }
  }

  private record RecordingMessage(int value) implements ActorMessage {
  }

  private static final class RecordingActor extends MailboxActor<RecordingMessage> {

    private final CountDownLatch latch;
    private final List<Integer> receivedValues = new ArrayList<>();

    private RecordingActor(CountDownLatch latch) {
      this.latch = latch;
    }

    @Override
    protected void onMessage(RecordingMessage message) {
      receivedValues.add(message.value());
      latch.countDown();
    }

    private List<Integer> receivedValues() {
      return List.copyOf(receivedValues);
    }
  }
}
