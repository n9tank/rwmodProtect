package rust;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
public class rwmodProtect implements Runnable {
 File In;
 File Ou;
 ZipFile Zip;
 ui Ui;
 int arr[];
 ZipOutputStream Zipout;
 HashMap low;
 HashMap iniMap;
 HashMap iniHide;
 HashMap coeMap;
 HashMap Filemap;
 ByteBuffer Warp;
 WritableByteChannel Out;
 OutputStreamWriter Ow;
 StringBuilder Buff;
 String musicPath;
 String rootPath;
 static HashSet skip;
 static byte max;
 static String fileD;
 static HashMap<String,HashMap> Res;
 static HashSet music;
 static HashMap wmap;
 public static void lib(File file) throws IOException {
  if (file.exists()) {
   HashMap inimap=new HashMap();
   wmap = inimap;
   StringBuilder buf=new StringBuilder();
   ZipInputStream zip=new ZipInputStream(new BufferedInputStream(new FileInputStream(file)));
   BufferedReader red=new BufferedReader(new InputStreamReader(zip));
   try {
    ZipEntry zipEntry;
    while ((zipEntry = zip.getNextEntry()) != null) {
     String fileName=zipEntry.getName().toLowerCase();
     loder lod=new loder(red, buf);
     zip.closeEntry();
     inimap.put(fileName, lod);
    }
    Iterator ite=inimap.entrySet().iterator();
    while (ite.hasNext()) {
     Map.Entry en=(Map.Entry)ite.next();
     String key=(String)en.getKey();
     loder lod=(loder)en.getValue();
     if (lod.str == null) {
      lod.str = "";
      lodAllCopy(lod, key, inimap);
     }
    }
   } finally {
    red.close();
   }
  }
 }
 static loder getlod(String str, HashMap iniMap) {
  str = str.toLowerCase();
  Object o=iniMap.get(str);
  loder lod=(loder)o;
  if (lod.str == null) {
   lod.str = "";
   lodAllCopy(lod, str, iniMap);
  }
  return lod;
 }
 static void lodAllCopy(loder lod, String file, HashMap iniMap) {
  file = loder.getSuperPath(file);
  HashMap ini=lod.ini;
  Object o=ini.get("core");
  HashMap put=null;
  if (o != null) {
   HashMap map=(HashMap)o;
   o = map.get("copyFrom");
   if (o != null) {
    String str=(String)o;
    put = new HashMap();
    String list[]=str.split(",");
    int i=0,l=list.length;
    do{
     str = list[i];
     loder loder=getlod(file.concat(str), iniMap);
     loder.putAnd(put, loder.put, null, false);
    }while(++i < l);
   }
  }
  if (put != null) {
   loder.putAnd(put, ini, null, false);
  } else put = ini;
  lod.put = put;
  lod.ini = null;
 }
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
 static HashMap set(Object o, byte i) {
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
       byte n=i;
       if (str.endsWith("_")) {
        str = str.substring(0, str.length() - 1);
        ++n;
       }
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
 public static void init(File path)throws IOException {
  HashMap<String,HashMap> map;
  map = new loder(new FileReader(path), null).ini;
  HashMap<String,String> set=map.get("set");
  String str=set.get("line");
  String list[]=str.split(",");
  loder.max = Byte.valueOf(list[0]);
  max = Byte.valueOf(list[1]);
  String file= set.get("file");
  fileD = file;
  HashSet musics=new HashSet();
  music = musics;
  Collections.addAll(musics, list);
  list = set.get("music").split(",");
  list = set.get("skip").split(",");
  musics = new HashSet();
  skip = musics;
  Collections.addAll(musics, list);
  loder.line = set(map.get("line"), (byte)0);
  HashMap image=set(map.get("image"), (byte)1);
  Iterator ite=image.values().iterator();
  HashMap tm=new HashMap();
  while (ite.hasNext()) {
   HashMap en=(HashMap)ite.next();
   tm.putAll(en);
  }
  HashMap music=set(map.get("music"), (byte)3);
  loder.put(image, music);
  ite = music.values().iterator();
  while (ite.hasNext()) {
   HashMap en=(HashMap)ite.next();
   tm.putAll(en);
  }
  image.put("template_", tm);
  Res = image;
 }
 String FileName(byte ini) {
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
 void copy(String name, ZipEntry en) throws IOException {
  ByteBuffer warp=Warp;
  WritableByteChannel wt=Out;
  ZipOutputStream zipw=Zipout;
  ReadableByteChannel in=Channels.newChannel(Zip.getInputStream(en));
  zipw.putNextEntry(new ZipEntry(name));
  try {
   while (in.read(warp) > 0) {
    warp.flip();
    wt.write(warp);
    warp.clear();
   }
   zipw.closeEntry();
  } finally {
   in.close();
  }
 }
 loder replace(String str, boolean isini) throws IOException {
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
   lod = new loder(new InputStreamReader(zip.getInputStream(en)), Buff);
   map.put(str, lod);
  } else lod = (loder)o;
  if (lod.str == null)write(lod, str, isini, new StringBuilder());
  return lod;
 }
 void write(loder ini, String path, boolean isini, StringBuilder buff) throws IOException {
  String r=FileName((byte)(isini ?3: 0));
  ini.str = r;
  path = loder.getSuperPath(path);
  replaceAll(ini, path, isini, buff);
  write(ini);
  ini.ini = null;
 }
 void replaceR(String str, String path, StringBuilder buff, boolean isimg, boolean post) throws IOException {
  String file;
  tag: {
   if (!isimg) {
    if (music.contains(str))break tag;
    file = getPath(str, path);
   } else {
    boolean shadow=false;
    int st;
    if (str.startsWith("SHADOW:")) {
     shadow = true;
     st = 7;
    } else st = 0;
    if (str.startsWith("CORE:", st) || str.startsWith("SHARED:", st)) {
     file = null;
    } else {
     if (str.startsWith("ROOT:", st)) {
      st += 5;
      path = rootPath;
     }
     if (str.startsWith("SHADOW:", st)) {
      shadow = true;
      st += 7;
     }
     if (st != 0)file = str.substring(st);
     else file = str;
     file = file.replaceFirst("^/+", "");
     if (path.length() > 0)file = path.concat(file);
     if (shadow && buff != null) {
      buff.append("SHADOW:");
     }
    }
   }
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
 void replaceAll(loder ini, String file, boolean isini, StringBuilder buff) throws IOException {
  int st=0;
  HashMap put=new HashMap();
  HashMap cou=new HashMap();
  HashMap alls=null;
  ini.put = put;
  buff.setLength(0);
  tag:
  if (isini) {
   loder all=null;
   int i=file.length();
   buff.append(file);
   do{
    i = file.lastIndexOf("/", --i);
    buff.setLength(i + 1);
    buff.append("all-units.template");
    String str= buff.toString();
    ZipEntry en=toPath(str);
    if (en != null) {
     all = (loder)iniHide.get(str = en.getName());
     if (all.str == null)write(all, str, false, new StringBuilder());
    }
   }while(i > 0);
   buff.setLength(0);
   if (all != null) {
    buff.append(all.str);
    buff.append(',');
    st = buff.length();
    ini.all = alls = all.put;
    ini.putAnd(put, all.put, cou, false);
   }
  }
  HashMap map=ini.ini;
  String cput=null;
  Object o=map.get("core");
  if (o != null) {
   HashMap core=(HashMap)o;
   o = core.get("copyFrom");
   String str;
   if (o != null && (str = (String)o).length() > 0 && !str.equals("IGNORE")) {
    str = str.replace('\\', '/');
    String list[]=str.split(",");
    int i=0,n=list.length;
    HashMap libs=wmap;
    do {
     String path=list[i].trim();
     str = getPath(path, file);
     loder lod=null;
     boolean s=str == null;
     if (!s) {
      ZipEntry en = toPath(str);
      str = en.getName();
      lod = replace(str, getType(str) == 3);
      path = lod.str;
     } else if (libs != null) {
      str = path.replaceFirst("^CORE:/*", "").toLowerCase();
      lod = (loder)libs.get(str);
     }
     if (lod != null) {
      ini.putAnd(put, lod.put, cou, s);
      if (!s && alls == lod.all)alls = null;
     }
     buff.append(path);
     buff.append(',');
    }while(++i < n);
   }
   int i=buff.length() - 1;
   if (i > 0) {
    if (alls != null)st = 0;
    core.put("copyFrom", cput = buff.substring(st, i));
   }
  }
  String str;
  HashMap<String, HashMap> reu=Res;
  HashMap as,coe;
  HashMap cache=coeMap;
  o = cache.get(cput);
  if (o == null) {
   coe = new HashMap();
   loder.put(coe, put);
   loder.as(coe);
   cache.put(cput, coe);
  } else {
   coe = (HashMap)o;
  }
  as = new HashMap();
  loder.putAnd(put, map, cou, false);
  loder.put(as, put);
  loder.as(as);
  cache.put(ini.str, as);
  HashSet skp=skip;
  Iterator ite = as.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String ac=(String)en.getKey();
   HashMap tr=(HashMap)loder.wh(ac, reu, rwmodProtect.max);
   o = en.getValue();
   HashMap put2=null;
   HashMap list;
   if (o instanceof HashMap) {
    list = (HashMap)o;
   } else {
    cpys cpy=(cpys)o;
    list = cpy.m;
    put2 = cpy.skip;
   }
   o = cou.get(ac);
   HashMap re;
   if (o != null) {
    if (o instanceof HashMap) {
     re = (HashMap)o;
    } else {
     cpys cpy=(cpys)o;
     re = cpy.is ?cpy.skip: cpy.m;
    }
   } else re = null;
   HashMap list2=null;
   HashMap find2=null;
   HashMap find3=null;
   o = coe.get(ac);
   if (o != null) {
    if (o instanceof HashMap) {
     list2 = (HashMap)o;
    } else {
     cpys cpy=(cpys)o;
     list2 = cpy.m;
     find2 = cpy.skip;
     find3 = cpy.hash;
    }
   }
   HashMap list3=(HashMap)map.get(ac);
   HashMap listv=list3;
   if (listv == null) {
    listv = new HashMap();
    map.put(ac, listv);
   }
   Iterator ite2=list.entrySet().iterator();
   boolean sikp=list3 != null && (o = list3.get("@copyFrom_skipThisSection")) != null && ("1".equals(o) || "true".equalsIgnoreCase((String)o));
   while (ite2.hasNext()) {
    en = (Map.Entry) ite2.next();
    String key=(String)en.getKey(),v=(String)en.getValue(),ov=null;
    boolean islib=re != null && re.get(key) == true;
    if (list2 != null) {
     str = (String)list2.get(key);
     if (str != null)ov = loder.get(str, ac, coe, list2, buff);
    } else str = null;
    boolean img=islib | (tr != null && (o = tr.get(key)) != null);
    String vl = loder.get(v, ac, as, list, buff);
    boolean eq=v.equals(str);
    boolean same=put2 != null && (str = (String)put2.get(key)) != null && v.equals(str);
    if (vl != null && img && (islib || !vl.equals(ov))) {
     boolean last=ov != null && !loder.isV(ov, key, o);
     if (same && ov != null && ((last && ((str = (String)find2.get(key)) == null || !ov.equals(loder.get(str, ac, coe, find2, buff)))) || (find3 != null && (ov = (String)find3.get(key)) != null && !ov.equals(str)))) {
      same = false;
     }
     if (!same && (last || !loder.isV(vl, key, o) || !eq)) {
      eq = false;
      if (list3 == null || !list3.containsKey(key))listv.put(key, null);
     }
    }
    if (list3 != null && !sikp && (eq || (same && !skp.contains(key)))) {
     list3.remove(key);
    }
   }
  }
  Iterator ite2=as.entrySet().iterator();
  while (ite2.hasNext()) {
   Map.Entry en=(Map.Entry)ite2.next();
   String key = (String)en.getKey();
   o = loder.wh(key, reu, max);
   if (o == null)continue;
   HashMap hash=(HashMap)o;
   o = en.getValue();
   HashMap list;
   if (o instanceof HashMap) {
    list = (HashMap)o;
   } else {
    list = ((cpys)o).m;
   }
   HashMap list3=(HashMap)map.get(key);
   Iterator ite3=hash.entrySet().iterator();
   boolean post=isini && !key.startsWith("te");
   while (ite3.hasNext()) {
    Map.Entry en2=(Map.Entry)ite3.next();
    String s=(String)en2.getKey();
    o = list.get(s);
    if (o != null) {
     String old = (String)o;
     str = ini.get(old, key, as, list, buff);
     if (str == null)continue;
     tag: {
      byte i = en2.getValue();
      if (loder.isV(str, s, i))break tag;
      buff.setLength(0);
      str = str.replace('\\', '/');
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
        replaceR(listr[0], file, buff, img, post);
        if (listr.length > 1) {
         buff.append(to);
         buff.append(listr[1]);
        }
        buff.append(",");
       }while(++l < size);
       buff.setLength(buff.length() - 1);
      } else replaceR(str, file, buff, true, post);
      old = buff.toString();
     }
     if (list3 != null && list3.containsKey(s)) {
      list3.put(s, old);
     }
    }
   }
  }
 }
 ZipEntry toPath(String str) {
  HashMap<String,ZipEntry> lowm=low;
  ZipFile zip=Zip;
  ZipEntry en=zip.getEntry(str);
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
 void write(loder ini) throws IOException {
  ZipOutputStream zip=Zipout;
  OutputStreamWriter out=Ow;
  zip.putNextEntry(new ZipEntry(ini.str));
  HashMap map=ini.ini;
  HashMap gloab=(HashMap)map.get("");
  map.remove("");
  Iterator<Map.Entry<String,HashMap>> ite=map.entrySet().iterator();
  boolean wt=false;
  if (ite.hasNext()) {
   Map.Entry<String,HashMap> en = ite.next();
   map = en.getValue();
   boolean size=map.size() > 0;
   while (true) {
    if (size) {
     wt = true;
     out.write('[');
     out.write(en.getKey());
     out.write("]\n");
     loder.writeKeys(map, out);
    }
    if (!ite.hasNext())break;
    en = ite.next();
    map = en.getValue();
    size = map.size() > 0;
    if (size)out.write('\n');
   }
  }
  if (gloab.size() > 0) {
   if (!wt)out.write("[]");
   out.write('\n');
   loder.writeKeys(gloab, out);
  }
  out.flush();
  zip.closeEntry();
 }
 byte getType(String file) {
  int i=file.length() - 4;
  int ed=i;
  if (file.endsWith("/"))--ed;
  if (file.regionMatches(true, ed, ".ini", 0, 4)) {
   if (!iniHide.containsKey(file)) {
    return 3;
   } else return 0;
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
    do{
     ZipEntry zipEntry=zipEntrys.nextElement();
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
    ZipEntry inf=toPath(name.concat("mod-info.txt"));
    if (inf != null) {
     loder ini=new loder(new InputStreamReader(zip.getInputStream(inf)), mbuff);
     HashMap info=ini.ini;
     ini.str = "mod-info.txt/";
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
     write(ini);
    }
    zipEntrys = zip.entries();
    do{
     ZipEntry zipEntry=zipEntrys.nextElement();
     if (zipEntry.getSize() != 0l) { 
      name = zipEntry.getName();
      byte type=getType(name);
      boolean istm=type == 2;
      if (istm || type == 3) {
       loder lod=new loder(new InputStreamReader(zip.getInputStream(zipEntry)), mbuff);
       Object o=lod.ini.get("core");
       if (o != null) {
        HashMap map=(HashMap)o;
        o = map.remove("dont_load");
        if (o != null) {
         String str=(String)o;
         if ("1".equals(str) || "true".equalsIgnoreCase(str))istm = true;
        }
       }
       HashMap put;
       if (istm)put = inihide;
       else put = iniMap;
       put.put(name, lod);
      } else if (type == 1) {
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
      } else if (type == 6) {
       copy(FileName(type), zipEntry);
      }
     }
    }while(zipEntrys.hasMoreElements());
    Iterator<Map.Entry> ite=inimap.entrySet().iterator();
    while (ite.hasNext()) {
     Map.Entry<String,loder> ini=ite.next();
     String filename=ini.getKey();
     loder loder=ini.getValue();
     if (loder.str == null)write(loder, filename, true, buff);
    }
   } finally {
    wt.close();
    out.close();
    zip.close();
   }
  } catch (Throwable e) {
   ui.fali(e);
  }
  ui.finsh();
 }
}
