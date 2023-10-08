package rust;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ui{
 public String show;
 //public Exception err;
 public long g;
 public static final ui def=new ui("def");
 public final static ExecutorService pool=Executors.newCachedThreadPool();
 public ui(String show){
  this.show=show;
  g=System.nanoTime();
 }
 public static void exec(File path){
  rwmodProtect rw=new rwmodProtect(path,new ui(path.getPath()));
  pool.execute(rw);
 }
 public void finsh(){
  System.out.println(show);
  System.out.print((System.nanoTime()-g)*0.000001D);
  System.out.println("ms");
 }
 public void fali(Exception e){
  e.printStackTrace();
  /*Exception ers=err;
  if(ers==null){
   err=e;
  }else ers.addSuppressed(e);*/
 }
}
