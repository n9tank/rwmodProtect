package rust;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface ui {
 public final static ExecutorService pool=Executors.newWorkStealingPool();
 //public void poss(int i);
 public void end(Throwable e);
}
