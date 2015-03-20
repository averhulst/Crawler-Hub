package processor;

import dao.CrawledDomainImpl;
import messaging.Messenger;

import java.util.concurrent.Executor;

public abstract class Processor implements Runnable{
    protected Messenger messenger;
    protected volatile boolean running;
    protected Executor threadPool;

}
