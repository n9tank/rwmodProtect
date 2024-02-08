package rust;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import rust.copyKey;
class loder implements Callable,InputStreamSupplier {
 public InputStream get() {
  try {
   if (ini == null)call();
   ByteOut brr=new ByteOut();
   BufferedWriter out=new BufferedWriter(new OutputStreamWriter(brr));
   try {
    HashMap hash=ini;
    boolean st=false;
    for (Map.Entry<String,cpys>en:(Set<Map.Entry<String,cpys>>)hash.entrySet()) {
     HashMap ou=en.getValue().m;
     if (ou.size() > 0) {
      if (st)out.write('\n');
      st = true;
      out.write('[');
      out.write(en.getKey());
      out.write(']');
      for (Map.Entry<String,String> en2:(Set<Map.Entry<String,String>>)ou.entrySet()) {
       out.write('\n');
       out.write(en2.getKey());
       out.write(':');
       out.write(en2.getValue());
      }
     }
    }
    out.flush();
    return brr.toInput();
   } finally {
    out.close();
   }
  } catch (Exception e) {
  }
  return null;
 }
 public Object call() throws Exception {
  Exception ex=null;
  if (ini == null) {
   try {
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
     if (task == null)return null;
     loder all=null;
     loder[] orr=null;
     //请不要在定义未使用的ini该版本移除了检查，便于并行
     String file = getSuperPath(src);
     cpys cp=(cpys)table.get("core");
     if (cp != null) {
      HashMap m=cp.m;
      str = (String)m.remove("dont_load");
      boolean is;
      isini = is = isini && !"1".equals(str) && !"true".equalsIgnoreCase(str);
      if (is) {
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
   }  catch (Exception e) {
    ex = e;
   } catch (Throwable e) {
    return null;
   }
  }
  TaskWait tas=task;
  if (ex != null) {
   if (tas != null)tas.down(ex);
   throw ex;
  }
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
   //放弃所有权，避免堵塞线程
   tas.addN(this);
   return null;
  }
  finsh = true;
  tas.down(null);
  return null;
 }
 boolean finsh;
 iniobj put;
 HashMap ini;
 iniobj old;
 String str;
 String src;
 int allindex;
 boolean notmp;
 InputStream read;
 TaskWait task;
 boolean isini;
 copyKey copy;
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
