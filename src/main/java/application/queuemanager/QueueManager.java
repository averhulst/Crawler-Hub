package application.queuemanager;

import application.hub.Config;
import service.messaging.Queue;
import java.util.concurrent.Executor;

public abstract class QueueManager implements Runnable{
    protected volatile boolean running;
    protected final int REQUEST_INTERVAL_MS = Config.QUEUE_MANAGER_TICK_INTERVAL;
    protected Executor threadPool;
    protected Queue queue;

    abstract void tick();

    public void run(){
        running = true;
        Runnable r = () -> {

            while(running){
                Long startTime = System.currentTimeMillis();
                tick();
                Long elapsedTime = System.currentTimeMillis() - startTime;

                if(elapsedTime < REQUEST_INTERVAL_MS){
                    try {
                        Thread.sleep(REQUEST_INTERVAL_MS - elapsedTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        threadPool.execute(r);
    }
}
