package rust;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.PrintStream;

public class ui{
 public String show;
 //public Exception err;
 public long g;
 public static ui def;
 public final static ExecutorService pool=Executors.newCachedThreadPool();
 public ui(String show){
  this.show=show;
  g=System.currentTimeMillis();
 }
 public static void exec(File path){
  rwmodProtect rw=new rwmodProtect(path,new ui(path.getPath()));
  pool.execute(rw);
 }
 public void finsh(){
  PrintStream out=System.out;
  out.print(show);
  out.print(':');
  out.print(System.currentTimeMillis() - g);
  out.println("ms");
 }
 public void fali(Exception e){
  e.printStackTrace();
  /*Exception ers=err;
  if(ers==null){
   err=e;
  }else ers.addSuppressed(e);*/
 }
}
