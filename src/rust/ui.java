package rust;

import java.util.LinkedHashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ui{
 public String path;
 public Exception err;
 public static ui def=new ui();
 public final static ExecutorService pool=Executors.newCachedThreadPool();
 public static void exec(String path){
  ui ui=new ui();
  ui.path=path;
  rwmodProtect rw=new rwmodProtect(path, ui);
  pool.execute(rw);
 }
 public void finsh(){
  Exception er=err;
  System.out.print(path);
  if(err==null){
  System.out.println(":finsh");
  }else{
  System.out.println(":error");
  er.printStackTrace();
  }
 }
 public void fali(Exception e){
  Exception ers=err;
  if(ers==null){
   err=e;
  }else ers.addSuppressed(e);
 }
}
