package rust;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
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
 static String cust[];
 static HashSet skip;
 static int[] cr;
 static ArrayList<String> ds;
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
 public static void dictionary(Reader io) throws IOException {
  BufferedReader buff= new BufferedReader(io);
  try {
   ArrayList srr=new ArrayList();
   ds = srr;
   String str;
   while ((str = buff.readLine()) != null) {
    srr.add(str);
   }
  } finally {
   buff.close();
  }
 }
 public static void init(Reader io)throws Exception {
  Properties set=new Properties();
  set.load(io);
  String file;
  if (ds == null) {
   file = set.getProperty("file");
   int i=0,len=file.length();
   int irr[]= new int[len];
   int cou=0;
   do{
    irr[cou] = file.codePointAt(i);
    i = file.offsetByCodePoints(i, 1);
    ++cou;
   }while(i < len);
   if (cou < len)irr = Arrays.copyOf(irr, cou);
   cr = irr;
  }
  cust = set.getProperty("cust").split(",");
  HashSet put=new HashSet();
  skip = put;
  Collections.addAll(put, set.getProperty("skip").split(","));
  HashMap res=new HashMap();
  Res = res;
  String[] list=set.getProperty("image").split(",");
  for (String str:list) {
   res.put(str, -1);
  }
  list = set.getProperty("images").split(",");
  for (String str:list) {
   res.put(str, 0);
  }
  list = set.getProperty("music").split(",");
  for (String str:list) {
   res.put(str, 1);
  }
 }
 void appendName(int i) {
  if (i >= 0) {
   ArrayList srr=ds;
   StringBuilder buff=Buff;
   int l;
   int[] irr=cr;
   if (srr == null)l = irr.length;
   else l = srr.size();
   do{
    int u=i % l;
    i /= l;
    if (srr == null)buff.appendCodePoint(irr[u]);
    else buff.append(srr.get(u));
   }while(i > 0);
  }
 }
 String FileName(int ini) {
  StringBuilder buff=Buff;
  buff.setLength(0);
  ini -= 2;
  if (ini < 0)ini = 0;
  int i=++arr[ini] - 2;
  if (ini > 4)buff.append("￸/");
  if (ini == 5)buff.append("[noloop]");
  appendName(i);
  if (ini == 1)buff.append(".ini");
  else if (ini > 2)buff.append(".ogg");
  else if (ini == 2)buff.append(".wav");
  if (ini < 2)buff.append('/');
  return buff.toString();
 }
 loder replace(ZipArchiveEntry en) throws Exception {
  loder lod=null;
  String str=en.getName();
  int i=getType(str);
  HashMap map;
  if (i == 3)map = iniMap;
  else map = iniHide;
  Object o=map.get(str);
  if (o == null) {
   ZipFile zip=Zip;
   lod = new loder(zip.getInputStream(en));
   lod.call();
   map.put(str, lod);
  } else lod = (loder)o;
  if (lod.str == null) {
   lod.use = true;
   replace(lod, str, lod.isini, new StringBuilder());
  }
  return lod;
 }
 void replace(loder ini, String file, boolean isini, StringBuilder buff) throws Exception {
  ini.str = FileName(isini ?3: 0);
  file = loder.getSuperPath(file);
  iniobj put=ini.put;
  put.mbuff = buff;
  HashMap cou=new HashMap();
  ini.cou = cou;
  loder alls=null;
  buff.setLength(0);
  tag:
  if (isini) {
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
     alls = replace(en);
     alls.src = en.getName();
     break;
    }
   }while(i > 0);
   buff.setLength(0);
  }
  loder[] orr=null;
  HashMap map=ini.ini;
  HashMap core=(HashMap)map.get("core");
  if (core != null) {
   String str=(String)core.get("copyFrom");
   StringBuilder buf=Buff;
   if (str != null && str.length() > 0 && !str.equals("IGNORE")) {
    str = str.replace('\\', '/');
    String list[]=str.split(",");
    int i=list.length;
    orr = new loder[i];
    HashMap libs=lib.libMap;
    while (--i >= 0) {
     str = list[i].trim();
     loder lod=null;
     if (!str.startsWith("CORE:")) {
      String sup;
      if (str.startsWith("ROOT:")) {
       str = str.substring(5);
       sup = rootPath;
      } else sup = file;
      str = str.replaceFirst("^/+", "");
      if (sup.length() > 0)str = sup.concat(str);
      ZipArchiveEntry en = toPath(str);
      lod = replace(en);
      str = en.getName();
     } else if (libs != null) {
      str = str.replaceFirst("^CORE:/*", "").toLowerCase();
      lod = (loder)libs.get(str);
      str = null;
     }
     orr[i] = lod;
     if (lod != null) {
      put.put(lod.put.put, cou, str);
      loder tk=lod.copy.all;
      if (tk != null) {
       if (alls != tk) {
        lod.notmp = true;
        lod.allindex = 1;//不在根目录
        buf.setLength(0);
        String fn=tk.allD;
        if (fn == null) {
         appendName(arr[6]++);
         buf.append('/');
         tk.allD = fn = buf.toString();
         buf.append("all-units.template/");
         tk.str = buff.toString();
         buf.setLength(buff.length() - 19);
        } else buff.append(fn);
        if (lod.str == null) {
         appendName(tk.allindex++ - 1);
         buf.append(".ini/");
         lod.str = buf.toString();
        }
       } else ini.notmp = true;//不追加all-tmp
      }
     }
    }
   }
  }
  HashMap coe=coeMap;
  coe.put(new copyKey(new loder[]{ini}, alls), put);
  copyKey key=new copyKey(orr, alls);
  ini.copy = key;
  iniobj old=(iniobj)coe.get(key);
  if (old == null) {
   coe.put(key, old = new iniobj());
   old.mbuff = buff;
   old.put(put.put, null, null);
   if (alls != null)old.put(alls.put.put, null, null);
   old.as();
  }
  ini.old = old;
  put.put(map, cou, null);
 }
 void write(loder ini, String file, StringBuilder buff) throws IOException {
  file = loder.getSuperPath(file);
  HashMap map=ini.ini;
  buff.setLength(0);
  int index=ini.allindex;
  copyKey keycopy=ini.copy;
  boolean ws=index > 0;
  loder orr[];
  loder all;
  boolean notmp;
  if ((orr = keycopy.copy) != null | (notmp = ((all = keycopy.all) != null && !ini.notmp))) {
   HashMap core=(HashMap)map.get("core");
   if (core == null)map.put("core", core = new HashMap());
   if (notmp) {
    buff.append(all.str);
    buff.append(',');
   }
   if (orr != null) {
    int i=0,len=orr.length;
    while (i < len) {
     loder obj=orr[i++];
     String str=obj.str;
     int st=0;
     if (obj.ini == null)buff.append("CORE:");
     else if (ws) {
      if (obj.copy.all == all) {
       st = str.indexOf('/') + 1;
      } else buff.append("ROOT:");
     }
     buff.append(str, st, str.length());
     buff.append(',');
    }
   }
   int i=buff.length();
   if (--i > 0)buff.setLength(i);
   core.put("copyFrom", buff.toString());
  }
  String str;
  HashMap<String, HashMap> reu=Res;
  iniobj put=ini.put;
  iniobj old=ini.old;
  HashMap coe,cou=ini.cou,cache=coeMap,as=put.put;
  /*
   HashSet skp=skip;
   Iterator ite = as.entrySet().iterator();
   while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String ac=(String)en.getKey();
   Object cp = en.getValue();
   HashMap put2=null;
   HashMap list;
   if (cp instanceof HashMap) {
   list = (HashMap)cp;
   } else {
   cpys cpy=(cpys)cp;
   list = cpy.m;
   put2 = cpy.skip;
   }
   HashMap re=(HashMap)cou.get(ac);
   HashMap list2=null;
   HashMap find2=null;
   HashMap find3=null;
   Object or = coe.get(ac);
   if (or != null) {
   if (or instanceof HashMap) {
   list2 = (HashMap)or;
   } else {
   cpys cpy=(cpys)or;
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
   Object o;
   Iterator ite2=list.entrySet().iterator();
   boolean post=ini.isini && !ac.startsWith("te");
   boolean sikp=list3 != null && (o = list3.get("@copyFrom_skipThisSection")) != null && ("1".equals(o) || "true".equalsIgnoreCase((String)o));
   while (ite2.hasNext()) {
   en = (Map.Entry) ite2.next();
   String key=(String)en.getKey(),v=(String)en.getValue();
   String ov;
   if (list2 != null)ov = (String)list2.get(key);
   else ov = null;
   boolean eq=v.equals(ov);
   boolean same=put2 != null && (str = (String)put2.get(key)) != null && v.equals(str);
   boolean img=(o = reu.get(key)) != null;
   if (img) {
   boolean lastRoot;
   String pathRel,path;
   if (re == null || (pathRel = (String)re.get(key)) == null) {
   path = null;
   lastRoot = false;
   } else {
   HashMap where;
   if (getType(pathRel) == 3) {
   where = iniMap;
   } else where = iniHide;
   loder ty=(loder)where.get(pathRel);
   path = loder.getSuperPath(pathRel);
   lastRoot = ws && (ty == null || ((index = ty.allindex) != -2 && index <= 0));
   }
   int type;
   String vl=put.get(v, ac, cp);
   if (vl != null) {
   if (ov != null)ov = old.get(ov, ac, or);
   type = (int)o;
   String vll[]=vl == null ?null: AllPath(vl, key, file, type);
   tag:
   if (vll != null) {
   buff.setLength(0);
   if (type >= 0) {
   int l=0,size=vll.length;
   char to;
   if (img = type == 0) to = '*';
   else to = ':';
   do {
   str = vll[l]; 
   int st;
   if (str.startsWith("ROOT:"))st = 5;
   else st = 0;
   st = str.indexOf(to, st);
   String add;
   if (st >= 0)add = str.substring(0, st);
   else add = str;
   if (!replaceR(add, file, buff, img, post, ws)) {
   v = null;
   break tag;
   }
   if (st >= 0)buff.append(str, st, str.length());
   buff.append(",");
   }while(++l < size);
   buff.setLength(buff.length() - 1);
   } else {
   if (!replaceR(vll[0], file, buff, true, post, ws)) {
   v = null;
   break tag;
   }
   }
   v = buff.toString();
   }
   String ovl[]=ov == null || path == null ?null: AllPath(ov, key, path, type);
   if (v != null && (lastRoot || path == null || !vl.equals(ov) || (!Arrays.equals(vll, ovl)))) {
   str = null;
   if (same && ov != null && ((ovl != null && ((str = (String)find2.get(key)) == null || !ov.equals(old.get(str, ac, find2)))) || (find3 != null && (ov = (String)find3.get(key)) != null && !ov.equals(str)))) {
   same = false;
   }
   if (!same && (ovl != null || vll != null || !eq)) {
   eq = false;
   listv.put(key, v);
   }
   }
   }
   }
   if (list3 != null && !sikp && (v == null || eq || (same && !skp.contains(key)))) {
   list3.remove(key);
   }
   }
   }*/
  cre.addArchiveEntry(lib.getArc(ini.str), ini);
 }
 static int ResTry(String file, boolean isimg, StringBuilder buff) {
  int st=0;
  if (isimg) {
   if (file.startsWith("SHADOW:")) {
    st = 7;
   }
   if (file.startsWith("CORE:", st) || file.startsWith("SHARED:", st))
    st = -1;
  } else {
   int i;
   if (file.startsWith("ROOT:"))i = 5;
   else i = 0;
   i = file.indexOf(':', i);
   if (i < 0)i = file.length();
   i -= 4;
   if (!(file.regionMatches(true, i, ".ogg", 0, 4) || file.regionMatches(true, i, ".wav", 0, 4)))st = -1;
  }
  if (buff != null && st > 0) {
   buff.append("SHADOW:");
  }
  return st;
 }
 boolean addResPath(String str, String path, boolean isimg) {
  StringBuilder buff=Buff;
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
   boolean isimg=type == 0;
   do {
    buff.setLength(0);
    str = list[m].trim();
    ru = addResPath(str, path, isimg) || ru;
    list[m] = buff.toString();
   }while(++m < l);
  } else {
   if (ru = addResPath(str, path, true))
    list = new String[]{buff.toString()};
   else list = null;
  }
  if (!ru)list = null;
  return list;
 }
 boolean replaceR(String str, String path, StringBuilder buff, boolean isimg, boolean post, boolean ws) throws IOException {
  int st=ResTry(str, isimg, buff);
  if (st >= 0) {
   if (ws)buff.append("ROOT:");
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
     res.str = str = FileName(isimg ?0: getType(str));
    } else {
     res = (res)o;
     str = res.str;
    }
    if (post && !res.close) {
     res.close = true;
     cre.addArchiveEntry(lib.getArc(str), new inputsu(Zip, en));
    }
   } else return false;
  }
  buff.append(str);
  return true;
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
  } else if (file.regionMatches(true, ed, ".tmx", 0, 4) || file.regionMatches(true, ed - 4, "_map.png", 0, 8)) {
   return 1;
  } else {
   String[] srr=cust;
   int len=srr.length;
   ed += 4;
   while (--len >= 0) {
    String str=srr[len];
    int le=str.length();
    if (file.regionMatches(true, ed - le, str, 0, le))return 2;
   }
  }
  String path=musicPath;
  if (file.regionMatches(true, i, ".ogg", 0, 4)) {
   if (path != null && file.startsWith(path)) {
    if (file.indexOf("[noloop]", path.length()) < 0)return 6;
    return 7;
   } else {
    return 5;
   }
  } else if (file.regionMatches(true, i, ".wav", 0, 4)) {
   return 4;
  }
  return 0;
 }
 public void end(Throwable e) {
  if (e == null) {
   Set<Map.Entry<String,loder>> en = iniMap.entrySet();
   try {
    StringBuilder buff=new StringBuilder();
    for (Map.Entry<String,loder>ini:en) {
     loder lod=ini.getValue();
     if (!lod.isini)continue;
     if (lod.str == null) {
      String key=ini.getKey();
      replace(lod, key, true, buff);
     }
    }
    Collection<loder> vls=(Collection<loder>)iniMap.values();
    for (loder lod:vls) {
     loder all;
     if ((all = lod.copy.all) != null)lod.put.put(all.put.put, lod.cou, null);
    }
    for (loder lod:vls)
     if (lod.isini || lod.use)lod.put.as();
    for (loder lod:(Collection<loder>)iniHide.values())
     if (lod.use)lod.put.as();
    for (Map.Entry<String,loder>ini:en) {
     loder lod=ini.getValue();
     if (!lod.isini && !lod.use)continue;
     String key=(String)ini.getKey();
     write(lod, key, buff);
    }
    en = iniHide.entrySet();
    for (Map.Entry<String,loder>ini:en) {
     loder lod=ini.getValue();
     if (!lod.use)continue;
     String key=(String)ini.getKey();
     write(lod, key, buff);
    }
   } catch (Throwable e2) {
    e = e2;
   }
  }
  Throwable add=null;
  ZipArchiveOutputStream zipout=out;
  if (zipout != null) {
   try {
    cre.writeTo(e != null ?null: zipout);
   } catch (Throwable e2) {
    add = e2;
   }
   try {
    zipout.close();
   } catch (Throwable e2) {
   }
  }
  ZipFile zip=Zip;
  if (zip != null) {
   try {
    zip.close();
   } catch (Throwable e2) {
   }
  }
  if (e != null) {
   if (add != null)e.addSuppressed(add);
  } else e = add;
  if (e != null)Ou.delete();
  if (!(e instanceof InterruptedException))Ui.end(e);
 }
 public void run() {
  arr = new int[7];
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
   String name=null;
   HashSet rset=new HashSet();
   Enumeration<? extends ZipArchiveEntry> zipEntrys=zip.getEntries();
   do{
    ZipArchiveEntry zipEntry=zipEntrys.nextElement();
    String fileName=zipEntry.getName();
    String root=loder.getSuperPath(fileName);
    if (!rset.add(root) && (name == null || root.length() < name.length()))name = root;
    fileName = fileName.toLowerCase();
    if (!lows.containsKey(fileName))lows.put(fileName, zipEntry);
   }while(zipEntrys.hasMoreElements());
   //if (name == null)name = ""; 仅单个ini的rwmod
   rootPath = name;
   ZipArchiveEntry inf=toPath(name.concat("mod-info.txt"));
   zipout = new ZipArchiveOutputStream(Ou);
   out = zipout;
   cr = new ParallelScatterZipCreator();
   cre = cr;
   if (inf != null) {
    loder ini=new loder(zip.getInputStream(inf));
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
    cre.addArchiveEntry(lib.getArc("mod-info.txt/"), ini);
   }
   zipEntrys = zip.getEntries();
   do{
    ZipArchiveEntry zipEntry=zipEntrys.nextElement();
    if (zipEntry.getSize() != 0l) { 
     name = zipEntry.getName();
     int type=getType(name);
     boolean istm=type == 2;
     if (istm || type == 3) {
      loder lod=new loder(zip.getInputStream(zipEntry));
      lod.task = tas;
      HashMap map;
      if (!istm) {
       lod.isini = true;
       map = inimap;
      } else map = inihide;
      tas.add(lod);
      map.put(name, lod);
     } else if (type == 1) {
      cr.addArchiveEntry(lib.getArc(loder.getName(name).concat("/")), new inputsu(zip, zipEntry));
     } else if (type == 6) {
      cr.addArchiveEntry(lib.getArc(FileName(type)), new inputsu(zip, zipEntry));
     }
    }
   }while(zipEntrys.hasMoreElements());
   // tas.end();
  } catch (Throwable e) {
   tas.down(e);
  }
 }
}
