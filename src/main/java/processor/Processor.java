package processor;

import Hub.DAO;
import messaging.Messenger;

import java.util.concurrent.Executor;

public class Processor {
    protected Messenger messenger;
    protected volatile boolean running;
    protected DAO dao;
    protected Executor threadPool;

    public Processor(Executor threadPool, DAO dao, Messenger messenger) {
        this.threadPool = threadPool;
        this.dao = dao;
        this.messenger = messenger;
    }
}
