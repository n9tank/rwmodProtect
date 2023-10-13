package rust;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
public class rwmodProtect extends rwmodLib implements Runnable {
 File In;
 File Ou;
 ZipFile Zip;
 ui Ui;
 int iniIndex=-2;
 int fileIndex=-1;
 int oggIndex=-2;
 ZipOutputStream Zipout;
 HashMap low;
 HashMap Filemap;
 ByteBuffer Warp;
 WritableByteChannel Out;
 OutputStreamWriter Ow;
 StringBuilder Buff;
 String musicPath;
 String rootPath;
 static int max;
 static String fileD;
 static HashMap<String,HashMap> Res;
 static HashSet music;
 String getPath(String str, String path) {
  if (str.startsWith("CORE:"))return null;
  if (str.startsWith("ROOT:")) {
   str = str.substring(5);
   path = rootPath;
  }
  str = str.replaceFirst("^/+", "");
  if (path.length() > 0)str = path.concat(str);
  return str;
 }
 String getImagePath(String str, String path, StringBuilder buff) {
  boolean shadow=false;
  int st=0;
  if (str.startsWith("SHADOW:", st)) {
   shadow = true;
   st = 7;
  }
  if (str.startsWith("CORE:", st) || str.startsWith("SHARED:", st)) {
   return null;
  }
  if (str.startsWith("ROOT:", st)) {
   st += 5;
   path = rootPath;
  }
  if (str.startsWith("SHADOW:", st)) {
   shadow = true;
   st += 7;
  }
  if (st != 0)str = str.substring(st);
  str = str.replaceFirst("^/+", "");
  if (path.length() > 0)str = path.concat(str);
  if (shadow && buff != null) {
   buff.append("SHADOW:");
  }
  return str;
 }
 loder getSpuerAll(String str) {
  int i=str.length();
  StringBuilder buff=Buff;
  buff.setLength(0);
  buff.append(str);
  do{
   i = str.lastIndexOf("/", --i);
   buff.setLength(i + 1);
   buff.append("all-units.template");
   str = buff.toString();
   loder lod;
   ZipEntry en=toPath(str);
   if (en != null) {
    lod = (loder)iniHide.get(str = en.getName());
    buff.setLength(0);
    if (lod.str == null)write(lod, str, false, new StringBuilder());
    return lod;
   }
  }while(i > 0);
  buff.setLength(0);
  return null;
 }
 static HashMap set(Object o, int i) {
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
     if (i == 0) {
      HashSet set=new HashSet();
      Collections.addAll(set, list);
      o = set;
     } else {
      HashMap vmap=new HashMap();
      o = vmap;
      int size=list.length;
      while (--size >= 0) {
       String str=list[size];
       int n;
       if (str.endsWith("_")) {
        str = str.substring(0, str.length() - 1);
        n = i + 1;
       } else n = i;
       vmap.put(str, n);
      }
     }
     en.setValue(o);
     if (key != null)map.put(key, o);
    }
   }
  }
  return map;
 }
 public static void init(File path, ui def) {
  HashMap<String,HashMap> map;
  try {
   map = new loder(new FileReader(path), null).ini;
   HashMap<String,String> set=map.get("set");
   String str=set.get("line");
   String list[]=str.split(",");
   loder.max = Integer.valueOf(list[0]);
   loder.vlmax = Integer.valueOf(list[1]);
   max = Integer.valueOf(list[2]);
   String file= set.get("file");
   fileD = file;
   list = set.get("music").split(",");
   HashSet musics=new HashSet();
   music = musics;
   Collections.addAll(musics, list);
   list = set.get("value").split(",");
   HashSet value=new HashSet();
   loder.vlset = value;
   Collections.addAll(value, list);
   loder.line = set(map.get("line"), 0);
   HashMap image=set(map.get("image"), 1);
   HashMap music=set(map.get("music"), 3);
   loder.put(image, music);
   Res = image;
  } catch (Exception e) {
   def.fali(e);
  }
 }
 rwmodProtect(File in, File ou, ui def) {
  In = in;
  Ou = ou;
  Ui = def;
 }
 String FileName(int ini) {
  StringBuilder buff=Buff;
  buff.setLength(0);
  int i;
  switch (ini) {
   case 1:
    i = ++iniIndex;
    break;
   case -3:
    i = ++oggIndex;
    break;
   default:
    i = ++fileIndex;
    break;
  }
  if (i >= 0) {
   String d=fileD;
   int l=d.length();
   do{
    int u=i % l;
    i /= l;
    buff.append(d.charAt(u));
   }while(i > 0);
  }
  if (ini > 0)buff.append(".ini");
  else if (ini == -3)buff.append(".ogg");
  buff.append('/');
  return buff.toString();
 }
 void copy(String name, ZipEntry en) {
  ByteBuffer warp=Warp;
  WritableByteChannel wt=Out;
  ZipOutputStream zipw=Zipout;
  try {
   ReadableByteChannel in=Channels.newChannel(Zip.getInputStream(en));
   zipw.putNextEntry(new ZipEntry(name));
   try {
    while (in.read(warp) > 0) {
     warp.flip();
     wt.write(warp);
     warp.clear();
    }
   } catch (Exception e) {
    Ui.fali(e);
   }
   in.close();
   zipw.closeEntry();
  } catch (Exception e) {
   Ui.fali(e);
  }
 }
 loder replace(String str, boolean isini) {
  loder lod=null;
  ZipEntry en=toPath(str);
  str = en.getName();
  HashMap map;
  if (isini) {
   map = iniMap;
  } else {
   map = iniHide;
  }
  Object o=map.get(str);
  if (!isini && o == null) {
   ZipFile zip=Zip;
   try {
    lod = new loder(new InputStreamReader(zip.getInputStream(en)),Buff);
    map.put(str, lod);
   } catch (Exception e) {
    Ui.fali(e);
   }
  } else lod = (loder)o;
  if (lod.str == null)write(lod, str, isini, new StringBuilder());
  return lod;
 }
 void write(loder ini, String path, boolean isini, StringBuilder buff) {
  String r=FileName(isini ?1: 0);
  ini.str = r;
  path = loder.getSuperPath(path);
  replaceAll(ini, path, isini, buff);
  write(ini, r);
  ini.ini = null;
 }
 void replaceR(String str, String path, StringBuilder buff, boolean isimg, boolean post) {
  String file;
  tag: {
   if (!isimg) {
    if (music.contains(str))break tag;
    file = getPath(str, path);
   } else file = getImagePath(str, path, buff);
   if (file != null) {
    ZipEntry en = toPath(file);
    if (en != null) {
     file = en.getName();
     HashMap map;
     map = Filemap;
     Object o=map.get(file);
     res res;
     if (o == null) {
      res = new res();
      res.str = str = FileName(getType(file));
      map.put(file, res);
     } else {
      res = (res)o;
      str = res.str;
     }
     if (post && !res.close) {
      res.close = true;
      copy(str, en);
     }
    }
   }
  }
  buff.append(str);
 }
 void replaceAll(loder ini, String file, boolean isini, StringBuilder buff) {
  int st=0;
  HashMap cou=new HashMap();
  HashMap put=new HashMap();
  HashMap alls=null;
  ini.put = put;
  buff.setLength(0);
  tag:
  if (isini) {
   loder all=getSpuerAll(file);
   if (all != null) {
    buff.append(all.str);
    buff.append(',');
    st = buff.length();
    ini.all = alls = all.put;
    ini.putAnd(put, all.put, cou, (byte)1);
   }
  }
  HashMap map=ini.ini;
  Object o=map.get("core");
  if (o != null) {
   HashMap core=(HashMap)o;
   o = core.get("copyFrom");
   String str;
   if (o != null && (str = (String)o).length() > 0 && !str.equals("IGNORE")) {
    String list[]=str.split(",");
    int i=0,n=list.length;
    HashMap libs=rwmodLib.wmap;
    do {
     String path=list[i].trim();
     str = getPath(path, file);
     loder lod=null;
     boolean s=str == null;
     if (!s) {
      ZipEntry en = toPath(str);
      str = en.getName();
      lod = replace(str, getType(str) > 0);
      path = lod.str;
     } else if (libs != null) {
      lod = rwmodLib.get(str);
     }
     if (lod != null) {
      ini.putAnd(put, lod.put, cou, s ?(byte)0: (byte)1);
      if (!s && alls == lod.all)alls = null;
     }
     buff.append(path);
     buff.append(',');
    }while(++i < n);
   }
   int i=buff.length() - 1;
   if (i > 0) {
    if (alls != null)st = 0;
    core.put("copyFrom", buff.subSequence(st, i));
   }
  }
  String str;
  HashMap<String, HashMap> res=Res;
  HashMap as;
  ArrayList need=ini.find(cou, ini.getPut(), as = ini.getAs(cou), buff);
  Iterator ite2=as.entrySet().iterator();
  while (ite2.hasNext()) {
   Map.Entry<String,HashMap> en=(Map.Entry)ite2.next();
   str = en.getKey();
   Object j;
   j = loder.wh(str, res, max);
   if (j == null)continue;
   HashMap list=en.getValue();
   HashMap hash=(HashMap)j;
   Iterator ite3=hash.entrySet().iterator();
   while (ite3.hasNext()) {
    Map.Entry en2=(Map.Entry)ite3.next();
    String s=(String)en2.getKey();
    j = list.get(s);
    if (j != null) {
     str = (String)j;
     str = ini.get(str, as, list, buff);
     if (str == null || str.equalsIgnoreCase("none") || str.equals("IGNORE"))continue;
     if (str.equalsIgnoreCase("auto") && s.equals("image_shadow"))continue;
     int i = en2.getValue();
     buff.setLength(0);
     if (i > 1) {
      String list2[]=str.split(",");
      int l=0,size=list2.length;
      boolean img=i == 2;
      String sp;
      char to;
      if (img) {
       sp = "\\*";
       to = '*';
      } else {
       sp = "(?<!^ROOT):";
       to = ':';
      }
      do {
       str = list2[l].trim();
       String listr[] = str.split(sp, 2);
       replaceR(listr[0], file, buff, img, isini);
       if (listr.length > 1) {
        buff.append(to);
        buff.append(listr[1]);
       }
       buff.append(",");
      }while(++l < size);
      i = buff.length() - 1;
      buff.setLength(i);
     } else replaceR(str, file, buff, true, isini);
     list.put(s, buff.toString());
    }
   }
  }
  ini.put(as, need);
 }
 ZipEntry toPath(String str) {
  HashMap<String,ZipEntry> lowm=low;
  ZipFile zip=Zip;
  ZipEntry en=Zip.getEntry(str);
  String lows=str.toLowerCase();
  if (en == null) {
   ZipEntry r=lowm.get(lows);
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
 void write(loder ini, String name) {
  ZipOutputStream zip=Zipout;
  OutputStreamWriter out=Ow;
  try {
   zip.putNextEntry(new ZipEntry(name));
   ini.write(out);
   zip.closeEntry();
  } catch (Exception e) {
   Ui.fali(e);
  }
 }
 byte getType(String file) {
  int i=file.length();
  if (file.endsWith("/"))--i;
  i -= 4;
  int ed=i;
  String filel=file.toLowerCase();
  if (filel.startsWith(".ini", i)) {
   if (!iniHide.containsKey(file)) {
    return 1;
   } else return -2;
  } else if (filel.startsWith(".tmx", i)) {
   return -1;
  } else {
   i -= 14;
   String end="all-units.template";
   if (filel.startsWith(end, i)) {
    if (i > 0) {
     if (file.startsWith("/", --i))return 0;
    } else if (i == 0)return 0;
   }
  }
  String path=musicPath;
  if (path != null && file.startsWith(path) && filel.startsWith(".ogg", ed)) {
   return -3;
  }
  return -2;
 }
 /*
  .tmx .ogg
  这些行为对游戏有影响，我认为没有这个必要
  _map.png 没有使用场景，不考虑优化
  boolean used(String str){
  res res;
  if(iniHide.get(str)!=null){
  return true;
  }else if((res=(res)Filemap.get(str))!=null&&!res.close){
  return true;
  }
  return false;
  }*/
 public void run() {
  ui ui=Ui;
  HashMap filemap = new HashMap();
  Filemap = filemap;
  HashMap inimap = new HashMap();
  iniMap = inimap;
  HashMap inihide = new HashMap();
  iniHide = inihide;
  HashMap lows= new HashMap();
  low = lows;
  ByteBuffer warp = ByteBuffer.allocateDirect(8192);
  Warp = warp;
  StringBuilder mbuff = new StringBuilder();
  Buff = mbuff;
  StringBuilder buff=new StringBuilder();
  try {
   ZipFile zip = new ZipFile(In);
   Zip = zip;
   ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(Ou)));
   Zipout = zipout;
   zipout.setLevel(9);
   OutputStreamWriter wt=new OutputStreamWriter(zipout);
   Ow = wt;
   WritableByteChannel out = Channels.newChannel(zipout);
   Out = out;
   String name = null;
   try {
    Enumeration<? extends ZipEntry> zipEntrys=zip.entries();
    while (zipEntrys.hasMoreElements()) {
     ZipEntry zipEntry=zipEntrys.nextElement();
     String fileName=zipEntry.getName();
     String root=loder.getRoot(fileName);
     fileName = fileName.toLowerCase();
     if (!lows.containsKey(fileName))lows.put(fileName, zipEntry);
     if (name == null) {
      name = root;
     } else if (name.length() == 0) {
      continue;
     } else if (!name.equals(root)) {
      name = "";
     }
    }
    rootPath = name;
    ZipEntry inf=toPath(name.concat("mod-info.txt"));
    if (inf != null) {
     loder ini=new loder(new InputStreamReader(zip.getInputStream(inf)), mbuff);
     HashMap info=ini.ini;
     Object o=info.get("music");
     if (o != null) {
      HashMap map=(HashMap)o;
      o = map.get("sourceFolder");
      if (o != null) {
       String musicpath = (String)o;
       musicpath = musicpath.replaceFirst("^[/\\]", "");
       if (musicpath.length() > 0)musicpath = musicpath.concat("/");
       musicPath = musicpath;
       map.put("sourceFolder", "");
      }
     }
     write(ini, "mod-info.txt/");
    }
    zipEntrys = zip.entries();
    while (zipEntrys.hasMoreElements()) {
     ZipEntry zipEntry=zipEntrys.nextElement();
     if (zipEntry.getSize() != 0l) { 
      name = zipEntry.getName();
      byte type=getType(name);
      if (type >= 0) {
       loder lod=new loder(new InputStreamReader(zip.getInputStream(zipEntry)), mbuff);
       if (rwmodLib.dontlod(lod))type = 0;
       if (type > 0) {
        inimap.put(name, lod);
       } else {
        inihide.put(name, lod);
       }
      } else {
       if (type == -1) {
        String loc = loder.getName(name);
        int i=name.length();
        if (name.endsWith("/"))--i;
        i -= 4;
        copy(loc.concat("/"), zipEntry);
        buff.setLength(0);
        buff.append(name, 0, i);
        buff.append("_map.png");
        zipEntry = toPath(buff.toString());
        if (zipEntry != null) {
         name = loder.getName(zipEntry.getName());
         copy(name.concat("/"), zipEntry);
        }
       } else {
        if (type == -3) {
         copy(FileName(type), zipEntry);
        }
       }
      }
     }
    }
    Iterator<Map.Entry> ite=inimap.entrySet().iterator();
    while (ite.hasNext()) {
     Map.Entry<String,loder> ini=ite.next();
     String filename=ini.getKey();
     loder loder=ini.getValue();
     if (loder.str == null)write(loder, filename, true, buff);
    }
   } catch (Exception e) {
    ui.fali(e);
   }
   wt.close();
   out.close();
   zip.close();
  } catch (Exception e) {
   ui.fali(e);
  }
  ui.finsh();
 }
}
