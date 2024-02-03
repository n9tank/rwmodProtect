package rust;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import java.io.ByteArrayInputStream;
class loder implements Callable,InputStreamSupplier {
 public InputStream get() {
  ByteOut brr=new ByteOut();
  try {
   BufferedWriter out=new BufferedWriter(new OutputStreamWriter(brr));
   if (ini == null)call();
   try {
    HashMap map=ini;
    Iterator<Map.Entry<String,HashMap>> ite=map.entrySet().iterator();
    boolean st=false;
    while (ite.hasNext()) {
     Map.Entry en = ite.next();
     map = (HashMap)en.getValue();
     if (map.size() > 0) {
      if (st)out.write('\n');
      st = true;
      String key=(String)en.getKey();
      out.write('[');
      out.write(key);
      out.write("]\n");
      Iterator<Map.Entry> ite2=map.entrySet().iterator();
      boolean nx=ite2.hasNext();
      while (nx) {
       en = ite2.next();
       out.write((String)en.getKey());
       out.write(':');
       out.write((String)en.getValue());
       nx = ite2.hasNext();
       if (nx)out.write('\n');
      }
     }
    }
    out.flush();
    return brr.toInput();
   } finally {
    out.close();
   }
  } catch (Throwable e) {
  }
  return null;
 }
 public Object call() throws Exception {
  Reader red=new InputStreamReader(read);
  BufferedReader buff=new BufferedReader(red);
  StringBuilder bf=new StringBuilder();
  Exception ex=null;
  try {
   try {
    HashMap core=new HashMap();
    String str;
    HashMap list=null;
    String last=null;
    HashMap table=new LinkedHashMap();
    ini = table;
    table.put("core", core);
    wh:
    while ((str = buff.readLine()) != null) {
     str = str.trim();
     int i=str.length();
     if (i == 0 || str.startsWith("#"))continue;
     String with;
     if (str.startsWith(with = "\"\"\"") || str.startsWith(with = "'''")) {
      int len=str.length();
      if (len <= 6)len = 3;else len -= 3;
      while (true) {
       if (str.startsWith(with, len))continue wh;
       str = buff.readLine();
       if (str == null)return null;
       str = str.trim();
       len = str.length() - 3;
      }
     }
     if (str.startsWith("[") && str.indexOf(']', 1) == --i) {
      if (str.startsWith("comment_", 1))last = null;
      else {
       last = str.substring(1, i).trim();
       list = (HashMap)table.get(last);
      }
     } else if (last != null) {
      String value[]=str.split("[=:]", 2);
      if (value.length > 1) {
       if (list == null)table.put(last, list = new HashMap());
       String key=value[0].trim();
       String set=value[1].trim();
       if (key.startsWith("@global ")) {
        core.put(key, set);
       } else {
        if (set.startsWith(with = "\"\"\"") || set.startsWith(with = "\'\'\'")) {
         bf.setLength(0);
         int len=set.length();
         int ed=len;
         int st=3;
         if (ed <= 6)ed = 3;else ed -= 3;
         while (true) {
          boolean now;
          if (now = set.startsWith(with, ed))len = ed;
          bf.append(set, st, len);
          if (now)break;
          st = 0;
          set = buff.readLine();
          if (set == null)return null;
          set = set.trim();
          len = set.length();
          ed = len - 3;
         }
         set = bf.toString();
        }
        list.put(key, set);
       }
      }
     }
    }
    boolean istm=isini;
    Object o = core.remove("dont_load");
    if (o != null) {
     str = (String)o;
     istm &= !("1".equals(str) || "true".equalsIgnoreCase(str));
    }
    isini = istm;
   } finally {
    if (red != null)buff.close();
   }
  } catch (InterruptedIOException e) {
  } catch (Exception e) {
   ex = e;
  }
  TaskWait tas=task;
  if (tas != null)task.down(ex);
  if (ex != null)throw ex;
  return null;
 }
 iniobj put;
 HashMap cou;
 HashMap ini;
 iniobj old;
 String str;
 String src;
 String allD;
 int allindex;
 boolean notmp;
 InputStream read;
 TaskWait task;
 boolean isini;
 boolean use;
 copyKey copy;
 loder(InputStream inp) {
  read = inp;
  put = new iniobj();
 }
 static String getName(String file) {
  int len=file.length();
  if (file.endsWith("/"))--len;
  int i=file.lastIndexOf("/", len - 1);
  return file.substring(++i, len);
 }
 static String getSuperPath(String str) {
  int i=str.lastIndexOf('/', str.length() - 2);
  if (i > 0)return str.substring(0, i + 1);
  return "";
 }
}
