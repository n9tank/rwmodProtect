package rust;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskWait {
 AtomicInteger ato;
 volatile Throwable err;
 Vector<Future> ar;
 volatile boolean end;
 ui back;
 public static final InterruptedException cancel=new InterruptedException();
 TaskWait(ui ui) {
  ato = new AtomicInteger();
  ar = new Vector();
  back = ui;
  if (ui != null)addN(ui);
 }
 void addN(Object o) {
  Future fu;
  ExecutorService pool=ui.pool;
  if (o instanceof Runnable) {
   fu = pool.submit((Runnable)o);
  } else {
   fu = pool.submit((Callable)o);
  }
  ar.add(fu);
 }
 void add(Object o) throws Throwable {
  Throwable er=err;
  if (er != null)throw er;
  ato.incrementAndGet();
  addN(o);
  er=err;
  if(er!=null)down(er);
 }
 public void down(Throwable e) {
  Throwable e2=err;
  if (e != null) {
   err=e;
   Vector<Future> arr=ar;
   do{
   for (Future fu:arr)fu.cancel(true);
   arr.clear();
   }while(arr.size()>0);
  }
  if (e2 != null)return;
  ui ui=back;
  AtomicInteger at=ato;
  if (e == null) {
   int i=at.decrementAndGet();
   if (i > 0 || !end)ui = null;
  }
  if (ui != null) {
   end = false;
   ui.end(e);
  }
 }
 void end() {
  AtomicInteger at=ato;
  if (at.get() <= 0) {
   back.end(null);
  } else end = true;
 }
}
