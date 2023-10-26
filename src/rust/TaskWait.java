package rust;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

class TaskWait {
 AtomicInteger ato;
 volatile Throwable err;
 volatile ArrayList<Future> ar;
 TaskWait() {
  ato = new AtomicInteger();
  ar = new ArrayList();
 }
 void add(loder f) throws Throwable {
  synchronized (this) {
   Throwable er=err;
   if (er != null)throw er;
  }
  f.task=this;
  ar.add(ui.pool.submit(f));
  ato.incrementAndGet();
 }
 void down(Throwable e) {
  synchronized (this) {
   Throwable er=err;
   if (er == null)err = er = e;
   else er.addSuppressed(e);
  }
  if (e != null) {
   ArrayList<Future> arr=ar;
   if (arr != null) {
    synchronized (arr) {
     int s=arr.size();
     while (--s >= 0)arr.get(s).cancel(true);
     arr = null;
    }
   }
  }
  int i=ato.decrementAndGet();
  if (i <= 0||e != null) {
   synchronized (ato) {
    ato.notifyAll();
   }
  }
 }
 void lock()throws Throwable {
  int i=ato.get();
  if (i > 0) {
   synchronized (ato) {
    ato.wait();
   }
  }
  Throwable e=err;
  if (e != null)throw e;
 }
}
