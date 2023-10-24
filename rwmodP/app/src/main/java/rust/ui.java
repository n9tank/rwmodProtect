package rust;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface ui {
 public final static ExecutorService pool=Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors()*2);
 public void poss(int i);
 public void end(Throwable e);
}
