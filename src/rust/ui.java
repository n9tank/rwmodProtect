package rust;

import java.util.LinkedHashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ui{
 public String path;
 public final static LinkedHashSet list=new LinkedHashSet();
 public final static ExecutorService pool=Executors.newCachedThreadPool();
 public static void exec(String path){
 ui ui=new ui();
 ui.path=path;
 rwmodProtect rw=new rwmodProtect(path,ui);
 pool.execute(rw);
 }
 public void finsh(){
  System.out.println(path);
  System.out.println("finsh");
 }
}
