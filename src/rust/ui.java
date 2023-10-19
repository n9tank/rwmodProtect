package rust;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface ui {
 public final static ExecutorService pool=Executors.newCachedThreadPool();
 public void finsh();
 public void poss(int i,int e);
 public void fali(Throwable e);
}
