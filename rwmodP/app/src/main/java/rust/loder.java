package rust;

import carsh.log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import rust.copyKey;
class loder extends InputStream implements Callable,InputStreamSupplier {
 ByteOut buff;
 BufferedWriter wt;
 int of;
 Iterator<Map.Entry<String,cpys>> sections;
 Iterator<Map.Entry<String,String>> keys;
 public void next(int i) throws IOException {
  do{
   if (keys == null || !keys.hasNext()) {
	Map.Entry<String, cpys> kvs;
	HashMap v;
	do{
	 if (!sections.hasNext()) {
	  wt.close();
	  buff.sub();
	  return;
	 }
	 kvs = sections.next();
	 v = kvs.getValue().m;
	}while(v.size() == 0);
	wt.write('[');
	wt.write(kvs.getKey());
	wt.write(']');
	wt.write('\n');
	keys = v.entrySet().iterator();
   } else {
	Map.Entry<String, String> kv=keys.next();
	wt.write(kv.getKey());
	wt.write(':');
	wt.write(kv.getValue());
	wt.write('\n');
   }
   wt.flush();
  }while(i > buff.size());
 }
 public int read() {
  return 0;
 }
 public int read(byte[] b, int off, int len) throws IOException {
  int s=buff.size() - of;
  if (s <= 0) {
   buff.reset();
   of = 0;
  }
  try {
   if (s < len)next(len + of);
  } catch (Throwable e) {
   wt.close();
   log.e(this, e);
   return -1;
  }
  s = buff.size() - of;
  if (s > len)s = len;
  if (s > 0)System.arraycopy(buff.get(), of, b, off, s);
  of += s;
  return s;
 }
 public InputStream get() {
  buff = new ByteOut();
  wt = new BufferedWriter(new OutputStreamWriter(buff));
  sections = ini.entrySet().iterator();
  return this;
 }
 public Object call() throws Exception {
  try {
   if (ini == null) {
    BufferedReader buff=new BufferedReader(new InputStreamReader(read));
    try {
     StringBuilder bf=new StringBuilder();
     String str;
     HashMap list=null;
     String last=null;
     HashMap table=new LinkedHashMap();
     ini = table;
     wh:
     while ((str = buff.readLine()) != null) {
      str = str.trim();
      if (str.length() == 0 || str.startsWith("#"))continue;
      boolean skip=str.startsWith("\"");
      boolean c=false;
      bf.setLength(0);
      tag:
      while (true) {
       int j=0;
       while (j >= 0) {
        int k = str.indexOf("\"\"\"", j);
        int m;
        if (k < 0) {
         if (bf.length() == 0 && !c)break tag;
         m = k;
         k = str.length();
        } else {
         c = !c;
         m = k + 3;
        }
        if (!skip)bf.append(str, j, k);
        j = m;
       }
       if (c)str = buff.readLine().trim();
       else {
        str = bf.toString();
        break;
       }
      }
      if (skip)continue;
      int i=str.length() - 1;
      if (str.startsWith("[") && str.indexOf(']', 1) == i) {
       if (str.startsWith("comment_", 1))last = null;
       else {
        last = str.substring(1, i).trim();
        cpys cpy=((cpys)table.get(last));
        list = cpy == null ?null: cpy.m;
       }
      } else if (last != null) {
       String value[]=str.split("[=:]", 2);
       if (value.length > 1) {
        if (list == null) {
         cpys cpy=new cpys();
         cpy.m = list = new HashMap();
         table.put(last, cpy);
        }
        String key=value[0].trim();
        String set=value[1].trim();
        list.put(key, set);
       }
      }
     }
     String file=src;
     if (file == null)return null;
     loder all=null;
     loder[] orr=null;
     //请不要定义未使用的ini。该版本移除了检查，便于并行
     file = getSuperPath(file);
     cpys cp=(cpys)table.get("core");
     if (cp != null) {
      HashMap m=cp.m;
      str = (String)m.remove("dont_load");
      isini = isini && !"1".equals(str) && !"true".equalsIgnoreCase(str);
      str = (String)m.get("copyFrom");
      if (str != null && str.length() > 0 && !str.equals("IGNORE")) {
       String lrr[]=str.replace('\\', '/').split(",");
       int i = lrr.length;
       orr = new loder[i];
       while (--i >= 0) {
        str = lrr[i].trim();
        loder lod;
        if (!str.startsWith("CORE:")) {
         String con;
         if (str.startsWith("ROOT:")) {
          str = str.substring(5);
          con = task.rootPath;
         } else con = file;
         str = str.replaceFirst("^/+", "");
         lod = task.getLoder(con.concat(str));
        } else lod = (loder)lib.libMap.get(str.replaceFirst("^CORE:/*", "").toLowerCase());
        orr[i] = lod;
       }
      }
     }
     if (isini) {
      bf.setLength(0);
      bf.append(file);
      int i=file.length();
      while (true) {
       bf.append("all-units.template");
       String fin = bf.toString();
       all = task.getLoder(fin);
       if (all != null)break;
       i = fin.lastIndexOf("/", --i);
       if (i < 0)break;
       bf.setLength(i + 1);
      }
     }
     copy = new copyKey(orr, all);
    } finally {
     buff.close();
    }
   }
   TaskWait tas=task;
   loder all=null;
   tag2: {
    tag: {
     loder[] or=copy.copy;
     if (or != null) {
      for (loder orr:or)
       if (!orr.finsh)break tag;
     }
     all = copy.all;
     if (all != null && !all.finsh)break tag;
     if (tas.lod(this))break tag2;
    }
    tas.addN(this);
    return null;
   }
   finsh = true;
   tas.down(null);
  } catch (Throwable ex) {
   TaskWait tas=task;
   if (tas != null)tas.down(ex);
   throw (Exception)ex;
  }
  return null;
 }
 int acou;
 iniobj put;
 iniobj old;
 boolean notmp;
 boolean finsh;
 boolean isini;
 HashMap ini;
 copyKey copy;
 String str;
 String src;
 InputStream read;
 TaskWait task;
 loder(InputStream inp) {
  read = inp;
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
