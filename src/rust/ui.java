package rust;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class ui {
 public final static ExecutorService pool=Executors.newCachedThreadPool();
 public abstract void finsh();
 public abstract void fali(Throwable e);
 public static Future exec(File path, ui ui) {
  String name=path.getName();
  int l=name.length();
  if (name.startsWith(".", l -= 6)) {
   name = name.substring(0, l);
  }
  File ou = new File(path.getParent(), name.concat("_r.rwmod"));
  return ui.pool.submit(new rwmodProtect(path,ou,ui));
 }
}
