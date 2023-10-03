package rust;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ui{
 public String show;
 public Exception err;
 public static final ui def=new ui("def");
 public final static ExecutorService pool=Executors.newCachedThreadPool();
 public ui(String show){
  this.show=show;
 }
 public static void exec(File path){
  rwmodProtect rw=new rwmodProtect(path,new ui(path.getPath()));
  pool.execute(rw);
 }
 public void finsh(){
  Exception er=err;
  System.out.print(show);
  if(err==null){
  System.out.println(":finsh");
  }else{
  System.out.println(":error");
  er.printStackTrace();
  err=null;
  }
 }
 public void fali(Exception e){
  Exception ers=err;
  if(ers==null){
   err=e;
  }else ers.addSuppressed(e);
 }
}
