package rust;
import carsh.log;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.LongAdder;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

public abstract class TaskWait implements Runnable,ui {
 LongAdder ato;
 String rootPath;
 File Ou;
 File In;
 Throwable err;
 Vector<Future> ar;
 ZipFile Zip;
 ui back;
 Map Zipmap;
 public TaskWait(File in, File ou, ui ui) {
  In = in;
  Ou = ou;
  ato = new LongAdder();
  ar = new Vector();
  Zipmap = new ConcurrentHashMap();
  back = ui;
  ato.increment();
 }
 public loder addLoder(ZipArchiveEntry za, String putkey, boolean isini) throws Throwable {
  loder lod = new loder();
  loder obj=(loder)Zipmap.putIfAbsent(putkey, lod);
  if (obj == null) {
   lod.isini = isini;
   lod.src = za.getName();
   lod.task = this;
   lod.read = Zip.getInputStream(za);
   add(lod);
  } else lod = obj;
  return lod;
 }
 public abstract loder getLoder(String str) throws Throwable;
 public boolean lod(loder ini) {
  HashMap map=ini.ini;
  if (Ou != null)map = iniobj.clone(map);
  iniobj obj= new iniobj(map, ini);
  ini.put = obj;
  iniobj old=ini.old;
  copyKey key=ini.copy;
  loder[] orr=key.copy;
  if (orr != null) {
   if (old == null) {
    int i=orr.length;
    while (--i >= 0) {
     loder lod=orr[i];
     iniobj put = lod.put;
     obj.put(put, lod);
    }
   } else obj.put(old, null);
  }
  return true;
 }
 public Future addN(Object o) {
  Future fu;
  ExecutorService pool=ui.pool;
  if (o instanceof Runnable)fu = pool.submit((Runnable)o);
  else fu = pool.submit((Callable)o);
  ar.add(fu);
  return fu;
 }
 void add(Object o) throws Throwable {
  Throwable er=err;
  if (er != null)throw er;
  ato.increment();
  Future fu=addN(o);
  er = err;
  if (er != null) {
   fu.cancel(true);
   throw er;
  }
 }
 public void down(Throwable e) {
  if (e != null)log.e(this, e);
  if (err != null)return;
  if (e != null) {
   err = e;
   Vector<Future> arr=ar;
   int s=arr.size();
   while (--s >= 0)arr.get(s).cancel(true);
   arr.clear();
  }
  ui ui=back;
  LongAdder at=ato;
  if (e == null) {
   at.decrement();
   if (at.sum() > 0l)ui = null;
  }
  if (ui != null) {
   end(e);
   if (Zip != null) {
    try {
     Zip.close();
    } catch (Throwable e2) {
     log.e(this, e2);
    }
   }
  }
 }
}
