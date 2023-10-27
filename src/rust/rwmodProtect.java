package rust;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import rust.lodtask;
public class rwmodProtect implements Runnable,ui {
 File In;
 File Ou;
 ui Ui;
 ZipFile Zip;
 ZipArchiveOutputStream out;
 ParallelScatterZipCreator cre;
 HashMap iniMap;
 HashMap iniHide;
 int arr[];
 HashMap low;
 HashMap coeMap;
 HashMap Filemap;
 BufferedWriter Ow;
 StringBuilder Buff;
 String musicPath;
 String rootPath;
 TaskWait wait;
 static HashSet skip;
 static int max;
 static String fileD;
 static HashMap<String,HashMap> Res;
 public static TaskWait exec(File in, File ou, ui ui) {
  rwmodProtect rw=new rwmodProtect();
  rw.In = in;
  rw.Ou = ou;
  rw.Ui = ui;
  TaskWait tas= new TaskWait(rw);
  rw.wait = tas;
  return tas;
 }
 public static String out(File path) {
  String name=path.getName();
  int l=name.length();
  if (name.startsWith(".", l -= 6)) {
   name = name.substring(0, l);
  }
  return name.concat("_r.rwmod");
 }
 static HashMap set(Object o, int is) {
  HashMap map=(HashMap)o;
  Iterator ite=map.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   o = en.getValue();
   String value,key;
   if (o instanceof String) {
    value = (String)o;
    if (value.charAt(0) == '$') {
     key = value.substring(1);
     o = map.get(key);
     if (o instanceof String) {
      value = (String)o;
     } else {
      en.setValue(o);
      value = null;
     }
    } else key = null;
    if (value != null) {
     String list[]=value.split(",");
     HashMap vmap=new HashMap();
     o = vmap;
     int size=list.length;
     while (--size >= 0) {
      String str=list[size];
      int n=is;
      if (str.endsWith("_")) {
       str = str.substring(0, str.length() - 1);
       ++n;
      }
      vmap.put(str, n);
     }
     en.setValue(o);
     if (key != null)map.put(key, o);
    }
   }
  }
  return map;
 }
 public static void init(Reader io)throws Exception {
  loder lod=new loder(io);
  lod.call();
  HashMap<String,HashMap> map=lod.ini;
  HashMap<String,String> set=map.get("set");
  max = Integer.valueOf(set.get("cou"));
  String file= set.get("file");
  fileD = file;
  HashSet put=new HashSet();
  skip = put;
  Collections.addAll(put, set.get("skip").split(","));
  HashMap image=set(map.get("image"), -1);
  Iterator ite=image.values().iterator();
  HashMap tm=new HashMap();
  while (ite.hasNext()) {
   HashMap en=(HashMap)ite.next();
   tm.putAll(en);
  }
  HashMap music=set(map.get("music"), 1);
  loder.put(image, music);
  ite = music.values().iterator();
  while (ite.hasNext()) {
   HashMap en=(HashMap)ite.next();
   tm.putAll(en);
  }
  image.put("template_", tm);
  Res = image;
 }
 String FileName(int ini) {
  StringBuilder buff=Buff;
  buff.setLength(0);
  ini -= 2;
  if (ini < 0)ini = 0;
  int i=++arr[ini] - 2;
  if (ini == 4)buff.append("￸/");
  if (i >= 0) {
   String d=fileD;
   int l=d.length();
   do{
    int u=i % l;
    i /= l;
    buff.append(d.charAt(u));
   }while(i > 0);
  }
  if (ini == 1)buff.append(".ini");
  else if (ini > 2)buff.append(".ogg");
  else if (ini == 2)buff.append(".wav");
  if (ini < 2)buff.append('/');
  return buff.toString();
 }
 void doTask(lodtask task, String file, boolean isini) throws Throwable {
  StringBuilder buff=new StringBuilder();
  task.bf=buff;
  loder ini=task.lod;
  loder all=ini.all;
  if (all != null) {
   buff.append(all.str);
  }
  int st=buff.length();
  HashMap map=ini.ini;
  Object o=map.get("core");
  if (o != null) {
   HashMap core=(HashMap)o;
   o = core.get("copyFrom");
   String str;
   if (o != null && (str = (String)o).length() > 0 && !str.equals("IGNORE")) {
    TaskWait wak=wait;
    if (isini)wak.add(null);
    ArrayList sk=new ArrayList();
    task.sck = sk;
    str = str.replace('\\', '/');
    String list[]=str.split(",");
    int i=0,n=list.length;
    AtomicInteger at=new AtomicInteger(n + 1);
    task.ato = at;
    HashMap libs=lib.libMap;
    do {
     str = list[i].trim();
     String path=str;
     loder lod=null;
     boolean add=false;
     if (!str.startsWith("CORE:")) {
      String sup;
      if (str.startsWith("ROOT:")) {
       str = str.substring(5);
       sup = rootPath;
      } else sup = file;
      str = str.replaceFirst("^/+", "");
      if (sup.length() > 0)str = sup.concat(str);
      ZipArchiveEntry en = toPath(str);
      str = en.getName();
      isini = getType(str) > 2;
      if (isini)map = iniMap;
      else map = iniHide;
      o = map.get(str);
      if (o == null) {
       ZipFile zip=Zip;
       lodtask ad=new lodtask(lod = new loder(new inputsu(zip, en)), this, isini, str);
       wak.add(ad);
       map.put(str, add);
      } else lod = (loder)o;
      isini = isini && !lod.ishide;
      if (lod.str == null)write(lod, str, isini);
      str = loder.getSuperPath(str);
      path = lod.str;
      if (o instanceof loder) {
       lod = (loder)o;
      } else {
       lodtask as=(lodtask)o;
       lod = as.lod;
       if (add = as.ato != null)as.sus.add(task);
      }
     } else if (libs != null) {
      str = str.replaceFirst("^CORE:/*", "").toLowerCase();
      lod = (loder)libs.get(str);
      str = null;
     }
     if (!add)at.decrementAndGet();
     if (all == lod)all = null;
     sk.add(lod);
     sk.add(str);
     buff.append(path);
     buff.append(',');
    }while(++i < n);
   }
   int i=buff.length() - 1;
   if (i > 0) {
    if (all != null)st = 0;
    core.put("copyFrom", task.cup = buff.substring(st, i));
   }
  }
  task.down();
 }
 void write(loder ini, String path, boolean isini) throws Throwable {
  String r=FileName(isini ?3: 0);
  ini.str = r;
  path = loder.getSuperPath(path);
  doTask(new lodtask(ini,this,isini,path),path, isini);
  cre.addArchiveEntry(lib.getArc(r), new inputsu(ini));
 }
 static int ResTry(String file, boolean isimg, StringBuilder buff) {
  int st=0;
  if (isimg) {
   if (file.startsWith("SHADOW:")) {
    st = 7;
   }
   if (file.startsWith("CORE:", st) || file.startsWith("SHARED:", st))
    st = -1;
  } else if (!loder.ismusic(file))st = -1;
  if (st > 0) {
   buff.append("SHADOW:");
  }
  return st;
 }
 boolean addResPath(String str, String path, boolean isimg, StringBuilder buff) {
  int st=ResTry(str, isimg, buff);
  if (st >= 0) {
   if (str.startsWith("ROOT:", st)) {
    st += 5;
    path = rootPath;
   }
   if (isimg) {
    boolean shaow=st > 0;
    if (str.startsWith("SHADOW:", st)) {
     st += 7;
     if (!shaow)buff.append("SHADOW:");
    }
   }
   if (st != 0)str = str.substring(st);
   str = str.replaceFirst("^/+", "");
   buff.append(path);
  }
  buff.append(str);
  return st >= 0;
 }
 String[] AllPath(String str, String s, String path, int type) {
  if (str.equalsIgnoreCase("none") || str.equals("IGNORE") || (str.equalsIgnoreCase("auto") && s.equals("image_shadow")))
   return null;
  str = str.replace('\\', '/');
  StringBuilder buff=Buff;
  String list[];
  buff.setLength(0);
  boolean ru=false;
  if (type >= 0) {
   list = str.split(",");
   int l=list.length,m=0;
   char to;
   boolean isimg;
   if (isimg = type == 0) to = '*';
   else  to = ':';
   do {
    str = list[m].trim();
    int st;
    if (str.startsWith("ROOT:"))st = 5;
    else st = 0;
    st = str.indexOf(to, st);
    String add;
    if (st >= 0)add = str.substring(0, st);
    else add = str;
    ru = ru || addResPath(add, path, isimg, buff);
    if (st >= 0)buff.append(str, st, str.length());
    list[m] = buff.toString();
   }while(++m < l);
  } else {
   if (ru = addResPath(str, path, true, buff))
    list = new String[]{buff.toString()};
   else list = null;
  }
  if (!ru)list = null;
  return list;
 }
 void replaceR(String str, String path, StringBuilder buff, boolean isimg, boolean post) throws IOException {
  int st=ResTry(str, isimg, buff);
  if (st >= 0) {
   if (st > 0)str = str.substring(st);
   ZipArchiveEntry en = toPath(str);
   if (en != null) {
    str = en.getName();
    HashMap map;
    map = Filemap;
    Object o=map.get(str);
    res res;
    if (o == null) {
     res = new res();
     map.put(str, res);
     res.str = str = FileName(getType(str));
    } else {
     res = (res)o;
     str = res.str;
    }
    if (post && !res.close) {
     res.close = true;
     cre.addArchiveEntry(lib.getArc(str), new inputsu(Zip, en));
    }
   }
  }
  buff.append(str);
 }
 ZipArchiveEntry toPath(String str) {
  HashMap<String,ZipArchiveEntry> lowm=low;
  ZipFile zip=Zip;
  ZipArchiveEntry en=zip.getEntry(str);
  String lows=str.toLowerCase();
  if (en == null) {
   ZipArchiveEntry r=lowm.get(lows);
   if (r != null)return r;
  } else return en;
  if (!str.endsWith("/")) {
   str = str.concat("/");
   if ((en = zip.getEntry(str)) == null) {
    return lowm.get(lows.concat("/"));
   }
  }
  return en;
 }
 int getType(String file) {
  int i=file.length() - 4;
  int ed=i;
  if (file.endsWith("/"))--ed;
  if (file.regionMatches(true, ed, ".ini", 0, 4)) {
   return 3;
  } else if (file.regionMatches(true, ed, ".tmx", 0, 4)) {
   return 1;
  } else {
   ed -= 14;
   if (file.regionMatches(true, ed, "all-units.template", 0, 18) && (ed == 0 || file.startsWith("/", --ed)))
    return 2;
  }
  String path=musicPath;
  if (file.regionMatches(true, i, ".ogg", 0, 4)) {
   if (path != null && file.startsWith(path)) {
    return 6;
   } else {
    return 5;
   }
  } else if (file.regionMatches(true, i, ".wav", 0, 4)) {
   return 4;
  }
  return 0;
 }
 volatile boolean cou;
 public void end(Throwable e) {
  TaskWait task=wait;
  if (e == null) {
   boolean to=cou;
   cou = true;
   if (!to) {
    Iterator<Map.Entry> ite=iniMap.entrySet().iterator();
    try {
     while (ite.hasNext()) {
      Map.Entry<String,loder> ini=ite.next();
      loder loder=ini.getValue();
      if (loder.ishide)continue;
      String filename=ini.getKey();
      if (loder.str == null)write(loder, filename, true);
     }
     task.end();
    } catch (Throwable e2) {
     task.down(e2);
    }
   } else {
    e = close(false);
    Ui.end(e);
   }
  } else {
   Throwable add=close(true);
   if (add != null) {
    e.addSuppressed(add);
   }
   Ui.end(e);
  }
 }
 Throwable close(boolean v) {
  wait.back = null;
  Throwable err=null;
  ZipArchiveOutputStream zipout=out;
  ZipArchiveOutputStream close;
  if (v)close = null;
  else close = out;
  try {
   cre.writeTo(close);
  } catch (Throwable e) {
   err = e;
  }
  try {
   zipout.close();
  } catch (Throwable e) {
  }
  ZipFile zip=Zip;
  if (zip != null) {
   try {
    zip.close();
   } catch (Throwable e) {
   }
  }
  return err;
 }
 public void run() {
  arr = new int[5];
  arr[0] = 1;
  HashMap filemap = new HashMap();
  Filemap = filemap;
  HashMap inimap = new HashMap();
  iniMap = inimap;
  HashMap inihide = new HashMap();
  iniHide = inihide;
  HashMap lows= new HashMap();
  coeMap = new HashMap();
  low = lows;
  StringBuilder mbuff = new StringBuilder();
  Buff = mbuff;
  ZipArchiveOutputStream zipout=null;
  ParallelScatterZipCreator cr=null;
  TaskWait tas=wait;
  try {
   ZipFile zip=new ZipFile(In);
   Zip = zip;
   String name = null;
   Enumeration<? extends ZipArchiveEntry> zipEntrys=zip.getEntries();
   do{
    ZipArchiveEntry zipEntry=zipEntrys.nextElement();
    String fileName=zipEntry.getName();
    String root;
    int i=fileName.indexOf("/");
    if (i >= 0) {
     root = fileName.substring(0, ++i);
    } else root = "";
    fileName = fileName.toLowerCase();
    if (!lows.containsKey(fileName))lows.put(fileName, zipEntry);
    if (name == null) {
     name = root;
    } else if (name.length() == 0) {
     continue;
    } else if (!name.equals(root)) {
     name = "";
    }
   }while(zipEntrys.hasMoreElements());
   rootPath = name;
   ZipArchiveEntry inf=toPath(name.concat("mod-info.txt"));
   zipout = new ZipArchiveOutputStream(Ou);
   out = zipout;
   cr = new ParallelScatterZipCreator();
   cre = cr;
   if (inf != null) {
    loder ini=new loder(new InputStreamReader(zip.getInputStream(inf)));
    ini.call();
    HashMap info=ini.ini;
    Object o=info.get("music");
    if (o != null) {
     HashMap map=(HashMap)o;
     o = map.get("sourceFolder");
     if (o != null) {
      String musicpath = (String)o;
      musicpath = musicpath.replace("\\", "/").replaceFirst("^/+", "");
      if (musicpath.length() > 0 && !musicpath.endsWith("/"))musicpath = musicpath.concat("/");
      musicPath = musicpath;
      map.put("sourceFolder", "￸");
     }
    }
    cre.addArchiveEntry(lib.getArc("mod-info.txt/"),new inputsu(ini));
   }
   zipEntrys = zip.getEntries();
   StringBuilder buff=new StringBuilder();
   do{
    ZipArchiveEntry zipEntry=zipEntrys.nextElement();
    if (zipEntry.getSize() != 0l) { 
     name = zipEntry.getName();
     int type=getType(name);
     boolean istm=type == 2;
     if (istm || type == 3) {
      loder lod=new loder(new inputsu(zip, zipEntry));
      lod.task = tas;
      tas.add(lod);
      if (istm)inihide.put(name, lod);
      else inimap.put(name, lod);
     } else if (type == 1) {
      String loc = loder.getName(name);
      int i=name.length();
      if (name.endsWith("/"))--i;
      i -= 4;
      cr.addArchiveEntry(lib.getArc(loc.concat("/")), new inputsu(zip, zipEntry));
      buff.setLength(0);
      buff.append(name, 0, i);
      buff.append("_map.png");
      zipEntry = toPath(buff.toString());
      if (zipEntry != null) {
       name = loder.getName(zipEntry.getName());
       cr.addArchiveEntry(lib.getArc(name.concat("/")), new inputsu(zip, zipEntry));
      }
     } else if (type == 6) {
      cr.addArchiveEntry(lib.getArc(FileName(type)), new inputsu(zip, zipEntry));
     }
    }
   }while(zipEntrys.hasMoreElements());
   tas.end();
  } catch (Throwable e) {
   tas.down(e);
  }
 }
}
