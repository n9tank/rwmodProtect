package rust;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.LongAdder;

public class TaskWait {
 LongAdder ato;
 volatile Throwable err;
 ArrayList<Future> ar;
 //volatile boolean end;
 ui back;
 public static final InterruptedException cancel=new InterruptedException();
 TaskWait(ui ui) {
  ato = new LongAdder();
  ar = new ArrayList();
  back = ui;
  if (ui != null)addN(ui);
 }
 Future addN(Object o) {
  Future fu;
  ExecutorService pool=ui.pool;
  if (o instanceof Runnable) {
   fu = pool.submit((Runnable)o);
  } else {
   fu = pool.submit((Callable)o);
  }
  ar.add(fu);
  return fu;
 }
 void add(Object o) throws Throwable {
  Throwable er=err;
  if (er != null)throw er;
  ato.increment();
  Future fu=addN(o);
  er=err;
  if (er != null){
   fu.cancel(true);
   throw er;
  }
 }
 public void down(Throwable e) {
  if (err != null)return;
  if (e != null) {
   err = e;
   ArrayList<Future> arr=ar;
   int s=arr.size();
   while(--s>=0)arr.get(s).cancel(true);
   arr.clear();
  }
  ui ui=back;
  LongAdder at=ato;
  if (e == null) {
   at.decrement();
   if (/*!end||*/at.sum() > 0l)ui = null;
  }
  if (ui != null) {
   at.reset();
  // end = false;
   ui.end(e);
  }
 }
 /*void end() {
  if (ato.sum() <= 0l) {
   back.end(null);
  } else end = true;
 }*/
}
