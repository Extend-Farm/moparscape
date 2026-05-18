package io.github.ffakira.rsps.server.runtime;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class MailboxActor<T extends ActorMessage> implements ActorRef<T>, AutoCloseable {

  private final BlockingQueue<T> mailbox = new LinkedBlockingQueue<>();
  private final AtomicBoolean running = new AtomicBoolean();
  private Thread workerThread;

  public void start() {
    if (!running.compareAndSet(false, true)) {
      return;
    }
    workerThread = Thread.ofVirtual().name(actorName()).start(this::runLoop);
  }

  @Override
  public void tell(T message) {
    mailbox.add(message);
  }

  @Override
  public void close() {
    running.set(false);
    if (workerThread != null && workerThread != Thread.currentThread()) {
      workerThread.interrupt();
    }
  }

  protected String actorName() {
    return getClass().getSimpleName();
  }

  protected abstract void onMessage(T message);

  private void runLoop() {
    while (running.get()) {
      try {
        onMessage(mailbox.take());
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        running.set(false);
      }
    }
  }
}
