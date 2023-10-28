package rust;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskWait {
 AtomicInteger ato;
 Throwable err;
 ArrayList<Future> ar;
 volatile boolean end;
 ui back;
 public static final InterruptedException cancel=new InterruptedException();
 TaskWait(ui ui) {
  ato = new AtomicInteger();
  ar = new ArrayList();
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
  synchronized (this) {
   Throwable er=err;
   if (er != null)throw er;
   ato.incrementAndGet();
   if (o != null) {
    addN(o);
   }
  }
 }
 public void down(Throwable e) {
  if (err == null)err = e;
  else return;
  if (e != null) {
   synchronized (this) {
    ArrayList<Future> arr=ar;
    int s=arr.size();
    while (--s >= 0)arr.get(s).cancel(true);
    arr.clear();
   }    
  }
  ui ui=back;
  AtomicInteger at=ato;
  if (e == null) {
   int i=at.decrementAndGet();
   if (i <= 0 && end) {
   } else ui = null;
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
