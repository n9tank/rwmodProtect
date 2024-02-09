package rust;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface ui {
 public final static ExecutorService pool=//Executors.newWorkStealingPool();
 Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
 public void end(Throwable e);
}
