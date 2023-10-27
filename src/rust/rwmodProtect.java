package rust;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
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
 TaskWait wait;
 BufferedWriter Ow;
 StringBuilder Buff;
 String musicPath;
 String rootPath;
 static HashSet skip;
 static int max;
 static String fileD;
 static HashMap<String,HashMap> Res;
 public static TaskWait exec(File in, File ou, ui ui) {
  rwmodProtect rw=new rwmodProtect();
  rw.In = in;
  rw.Ou = ou;
  rw.Ui = ui;
  return rw.wait = new TaskWait(rw);
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
 loder replace(ZipArchiveEntry en, String str) throws Exception {
  loder lod=null;
  boolean isini=getType(str) > 2;
  HashMap map;
  if (isini)map = iniMap;
  else map = iniHide;
  Object o=map.get(str);
  if (o == null) {
   ZipFile zip=Zip;
   lod = new loder(new InputStreamReader(zip.getInputStream(en)));
   lod.call();
   map.put(str, lod);
  } else lod = (loder)o;
  isini = isini && lod.isini;
  if (lod.str == null)write(lod, str, isini, new StringBuilder());
  return lod;
 }
 void write(loder ini, String file, boolean isini, StringBuilder buff) throws Exception {
  String r=FileName(isini ?3: 0);
  ini.str = r;
  ini.isini = false;
  file = loder.getSuperPath(file);
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
   String str;
   do{
    i = file.lastIndexOf("/", --i);
    buff.setLength(i + 1);
    buff.append("all-units.template");
    str = buff.toString();
    ZipArchiveEntry en=toPath(str);
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
    ini.putAnd(put, all.put, cou, loder.getSuperPath(str));
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
    HashMap libs=lib.libMap;
    do {
     str = list[i].trim();
     String path=str;
     loder lod=null;
     boolean s;
     if (s = !str.startsWith("CORE:")) {
      String sup;
      if (str.startsWith("ROOT:")) {
       str = str.substring(5);
       sup = rootPath;
      } else sup = file;
      str = str.replaceFirst("^/+", "");
      if (sup.length() > 0)str = sup.concat(str);
      ZipArchiveEntry en = toPath(str);
      str = en.getName();
      lod = replace(en, str);
      path = lod.str;
     } else if (libs != null) {
      str = str.replaceFirst("^CORE:/*", "").toLowerCase();
      lod = (loder)libs.get(str);
     }
     if (lod != null) {
      ini.putAnd(put, lod.put, cou, !s ?null: loder.getSuperPath(str));
      if (alls == lod.all)alls = null;
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
  loder.putAnd(put, map, cou, null);
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
   boolean post=isini && !ac.startsWith("te");
   boolean sikp=list3 != null && (o = list3.get("@copyFrom_skipThisSection")) != null && ("1".equals(o) || "true".equalsIgnoreCase((String)o));
   while (ite2.hasNext()) {
    en = (Map.Entry) ite2.next();
    String key=(String)en.getKey(),v=(String)en.getValue(),ov=null;
    String path=re == null ?null: (String)re.get(key);
    if (list2 != null) {
     str = (String)list2.get(key);
     if (str != null)ov = loder.get(str, ac, coe, list2, buff);
    } else str = null;
    boolean img=tr != null && (o = tr.get(key)) != null;
    String vl =loder.get(v, ac, as, list, buff);
    boolean eq=v.equals(str);
    boolean same=put2 != null && (str = (String)put2.get(key)) != null && v.equals(str);
    int type;
    if (vl != null && img) {
     type = (int)o;
     String vll[]=vl == null ?null: AllPath(vl, key, file, type);
     if (vll != null) {
      buff.setLength(0);
      if (type >= 0) {
       int l=0,size=vll.length;
       char to;
       if (img = type == 0) to = '*';
       else to = ':';
       do {
        str = vll[l].trim(); 
        if (str.startsWith("ROOT:"))st = 5;
        else st = 0;
        st = str.indexOf(to, st);
        String add;
        if (st >= 0)add = str.substring(0, st);
        else add = str;
        replaceR(add, file, buff, img, post);
        if (st >= 0)buff.append(str, st, str.length());
        buff.append(",");
       }while(++l < size);
       buff.setLength(buff.length() - 1);
      } else replaceR(vll[0], file, buff, true, post);
      v = buff.toString();
     }
     String ovl[]=ov == null || path == null ?null: AllPath(ov, key, path, type);
     if (path == null || !vl.equals(ov) || (!Arrays.equals(vll, ovl))) {
      if (same && ov != null && ((ovl != null && ((str = (String)find2.get(key)) == null || !ov.equals(loder.get(str, ac, coe, find2, buff)))) || (find3 != null && (ov = (String)find3.get(key)) != null && !ov.equals(str)))) {
       same = false;
      }
      if (!same && (ovl != null || vll != null || !eq)) {
       eq = false;
       listv.put(key, v);
      }
     }
    }
    if (list3 != null && !sikp && (eq || (same && !skp.contains(key)))) {
     list3.remove(key);
    }
   }
  }
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
 public void end(Throwable e) {
  TaskWait task=wait;
  if (e == null) {
   Iterator<Map.Entry> ite=iniMap.entrySet().iterator();
   try {
    StringBuilder buff=new StringBuilder();
    while (ite.hasNext()) {
     Map.Entry<String,loder> ini=ite.next();
     loder lod=ini.getValue();
     if (!lod.isini)continue;
     String key=ini.getKey();
     write(lod, key, true, buff);
    }
   } catch (Throwable e2) {
    task.down(e2);
    return;
   }
   e = close(false);
   Ui.end(e);
  } else {
   Throwable add=close(true);
   if (add != null)e.addSuppressed(add);
   Ui.end(e);
  }
 }
 Throwable close(boolean v) {
  wait.back = null;
  Throwable err=null;
  ZipArchiveOutputStream zipout=out;
  try {
   cre.writeTo(v ?null: zipout);
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
    cre.addArchiveEntry(lib.getArc("mod-info.txt/"), new inputsu(ini));
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
      istm = !istm;
      lod.isini = istm;
      HashMap map;
      if (istm)map = inimap;
      else map = iniHide;
      map.put(name, lod);
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
