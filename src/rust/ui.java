package rust;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface ui {
 public final static ExecutorService pool=Executors.newCachedThreadPool();
 public abstract void finsh();
 public abstract void fali(Throwable e);
}
