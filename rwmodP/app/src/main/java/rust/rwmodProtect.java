package rust;

import java.io.BufferedReader;
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
  loder lod=new loder(io);
  lod.call();
  HashMap<String,HashMap> map=lod.ini;
  HashMap<String,String> set=map.get("set");
  String file;
  if (ds == null) {
   file = set.get("file");
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
  cust = set.get("cust").split(",");
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
 loder replace(ZipArchiveEntry en, String str) throws Exception {
  loder lod=null;
  int i=getType(str);
  HashMap map;
  if (i == 3)map = iniMap;
  else map = iniHide;
  Object o=map.get(str);
  if (o == null) {
   ZipFile zip=Zip;
   lod = new loder(new InputStreamReader(zip.getInputStream(en)));
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
  HashMap put=new HashMap();
  HashMap cou=new HashMap();
  loder alls=null;
  ini.put = put;
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
     String allstr;
     ini.all = alls = replace(en, allstr = en.getName());
     alls.src = allstr;
     break;
    }
   }while(i > 0);
   buff.setLength(0);
  }
  HashMap map=ini.ini;
  Object o=map.get("core");
  if (o != null) {
   HashMap core=(HashMap)o;
   o = core.get("copyFrom");
   String str;
   if (o != null && (str = (String)o).length() > 0 && !str.equals("IGNORE")) {
    StringBuilder buf=Buff;
    str = str.replace('\\', '/');
    String list[]=str.split(",");
    int i=0,n=list.length;
    Object[] orr=new Object[n];
    ini.copy = orr;
    HashMap libs=lib.libMap;
    do {
     str = list[i].trim();
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
      orr[i] = lod;
     } else if (libs != null) {
      orr[i] = str;
      str = str.replaceFirst("^CORE:/*", "").toLowerCase();
      lod = (loder)libs.get(str);
     }
     if (lod != null) {
      String ss=!s ?null: str;
      ini.putAnd(put, lod.put, cou, ss);
      loder tk=lod.all;
      if (tk != null) {
       if (alls != tk) {
        lod.allindex = -2;
        buf.setLength(0);
        loder allt=lod.all;
        String fn=allt.allD;
        if (fn == null) {
         appendName(arr[6]++);
         buf.append('/');
         allt.allD = fn = buf.toString();
         buf.append("all-units.template/");
         allt.str = buf.toString();
         buf.setLength(buf.length() - 19);
        } else buf.append(fn);
        appendName(++allt.allindex - 2);
        buf.append(".ini/");
        lod.str = buf.toString();
       } else ini.allindex = -1;
      }
     }
    }while(++i < n);
   }
  }
  HashMap old=new HashMap();
  ini.old = old;
  if (alls != null)loder.putAnd(old, alls.put, null, null);
  loder.putAnd(old, put, null, null);
  ini.putAnd(put, ini.ini, cou, null);
  ini.cou = cou;
 }
 void add(Object orr[], loder ini, boolean ws, StringBuilder buff) {
  if (orr != null) {
   int i=0,len=orr.length;
   while (i < len) {
    Object o=orr[i++];
    if (o instanceof String) buff.append((String)o);
    else {
     loder obj=(loder)o;
     String str=obj.str;
     int st=0;
     if (ws) {
      loder all=ini.all;
      if (all == null)all = ini;
      if (obj.all == all && obj.allD == ini.allD) {
       st = str.indexOf('/') + 1;
      } else buff.append("ROOT:");
     }
     buff.append(str, st, str.length());
    }
    buff.append(',');
   }
  }
  int i=buff.length();
  if (--i > 0)buff.setLength(i);
 }
 void write(loder ini, String file, StringBuilder buff) throws IOException {
  file = loder.getSuperPath(file);
  loder all=ini.all;
  HashMap map=ini.ini;
  String cput=null;
  buff.setLength(0);
  HashMap core=(HashMap)map.get("core");
  int index=ini.allindex;
  boolean ws=index == -2 || index > 0;
  if (core != null) {
   Object[] orr=ini.copy;
   int st=0;
   if (all != null) {
    if (index == 0) {
     String str=all.str;
     st = str.length() + 1;
     buff.append(str);
     buff.append(',');
    }
   }
   add(orr, ini, ws, buff);
   if (buff.length() > 0)core.put("copyFrom", cput = buff.toString());
   if (index == -2)buff.insert(0, all.str.concat(","));
   if (ws) {
    buff.setLength(st);
    add(orr, ini, false, buff);
    cput = buff.toString();
   }
  }
  HashMap put=ini.put;
  String str;
  HashMap<String, HashMap> reu=Res;
  HashMap coe,cou=ini.cou;
  HashMap cache=coeMap;
  HashMap as=new HashMap();
  if (all != null) {
   String src=all.src;
   HashMap tmp2=new HashMap();
   loder.putAnd(as, all.put, tmp2, src);
   loder.put(tmp2, cou);
   cou = tmp2;
  }
  loder.putAnd(as, put, null, null);
  loder.as(as);
  Object o;
  if (cput != null && cput.length() == 0)o = cput = null;
  else o = cache.get(cput);
  if (o == null) {
   loder.as(coe = ini.old);
   if (cput != null)cache.put(cput, coe);
  } else coe = (HashMap)o;
  cache.put(ini.str, as);
  HashSet skp=skip;
  Iterator ite = as.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String ac=(String)en.getKey();
   HashMap tr=(HashMap)loder.wh(ac, reu);
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
   HashMap re=(HashMap)cou.get(ac);
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
   boolean post=ini.isini && !ac.startsWith("te");
   boolean sikp=list3 != null && (o = list3.get("@copyFrom_skipThisSection")) != null && ("1".equals(o) || "true".equalsIgnoreCase((String)o));
   while (ite2.hasNext()) {
    en = (Map.Entry) ite2.next();
    String key=(String)en.getKey(),v=(String)en.getValue();
    String ov;
    if (list2 != null)ov = (String)list2.get(key);
    else ov = null;
    boolean eq=v.equals(ov);
    boolean same=put2 != null && (str = (String)put2.get(key)) != null && v.equals(ov);
    boolean img=tr != null && (o = tr.get(key)) != null;
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
     String vl=loder.get(v, ac, as, list, buff);
     if (vl != null) {
      if (ov != null)ov = loder.get(ov, ac, coe, list2, buff);
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
       if (same && ov != null && ((ovl != null && ((str = (String)find2.get(key)) == null || !ov.equals(loder.get(str, ac, coe, find2, buff)))) || (find3 != null && (ov = (String)find3.get(key)) != null && !ov.equals(str)))) {
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
  }
  cre.addArchiveEntry(lib.getArc(ini.str), new inputsu(ini));
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
   boolean isimg=type == 0;
   do {
    buff.setLength(0);
    str = list[m].trim();
    ru = addResPath(str, path, isimg, buff) || ru;
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
   Set en=iniMap.entrySet();
   Iterator<Map.Entry> ite=en.iterator();
   try {
    StringBuilder buff=new StringBuilder();
    Map.Entry<String,loder> ini;
    while (ite.hasNext()) {
     ini = ite.next();
     loder lod=ini.getValue();
     if (!lod.isini)continue;
     if (lod.str == null) {
      String key=ini.getKey();
      replace(lod, key, true, buff);
     }
    }
    ite = en.iterator();
    while (ite.hasNext()) {
     ini = ite.next();
     loder lod=ini.getValue();
     if (!lod.isini && !lod.use)continue;
     String key=(String)ini.getKey();
     write(lod, key, buff);
    }
    ite = iniHide.entrySet().iterator();
    while (ite.hasNext()) {
     ini = ite.next();
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
   do{
    ZipArchiveEntry zipEntry=zipEntrys.nextElement();
    if (zipEntry.getSize() != 0l) { 
     name = zipEntry.getName();
     int type=getType(name);
     boolean istm=type == 2;
     if (istm || type == 3) {
      loder lod=new loder(new inputsu(zip, zipEntry));
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
   tas.end();
  } catch (Throwable e) {
   tas.down(e);
  }
 }
}
