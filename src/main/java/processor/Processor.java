package processor;

import messaging.Queue;
import java.util.concurrent.Executor;

public abstract class Processor implements Runnable{
    protected volatile boolean running;
    protected Executor threadPool;
    protected Queue queue;
}
