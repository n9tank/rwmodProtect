package rust;
import java.io.File;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.LongAdder;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import carsh.log;

public abstract class TaskWait implements ui {
 LongAdder ato;
 String rootPath;
 File Ou;
 volatile Throwable err;
 Vector<Future> ar;
 ZipFile Zip;
 ui back;
 ConcurrentHashMap Zipmap;
 public static final InterruptedException cancel=new InterruptedException();
 TaskWait(ui ui) {
  ato = new LongAdder();
  ar = new Vector();
  Zipmap = new ConcurrentHashMap();
  back = ui;
  addN(this);
 }
 public Object getRes(String str) {
  Object obj=Zipmap.get(str);
  if (obj == null)obj = Zip.getEntry(str);
  if (obj == null) {
   str = str.toLowerCase();
   obj = Zipmap.get(str);
   if (obj == null)obj = Zip.getEntry(str);
  }
  return obj;
 }
 public loder getLoder(String str) throws Throwable {
  Object obj=getRes(str);
  if (obj == null)return null;
  if (obj instanceof loder)return(loder)obj;
  ZipArchiveEntry za=(ZipArchiveEntry)obj;
  str = za.getName();
  loder lod=new loder(Zip.getInputStream(za));
  lod.src = str;
  lod.task = this;
  ConcurrentHashMap map=Zipmap;
  map.put(str, lod);
  map.put(str.toLowerCase(), lod);
  add(lod);
  return lod;
 }
 public boolean lod(loder ini) {
  HashMap map=ini.ini;
  loder all=ini.copy.all;
  if (Ou != null)map = iniobj.clone(map);
  iniobj obj= new iniobj(map, ini);
  obj.all = all;
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
   } else {
    old.all = all;
    obj.put(old, null);
   }
  }
  return true;
 }
 Future addN(Object o) {
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
   at.reset();
   end(e);
  }
 }
}
