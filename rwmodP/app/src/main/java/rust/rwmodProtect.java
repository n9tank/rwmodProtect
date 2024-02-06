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
import java.util.Iterator;
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
 Map grops;
 static cpys defcs;
 static{
  cpys cp=new cpys();
  HashMap list=new HashMap();
  defcs = cp;
  list.put("@define *", "");
  cp.m = list;
 }
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
   file = set.getProperty("chars");
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
  cust = set.getProperty("loader").split(",");
  HashSet put=new HashSet();
  skip = put;
  Collections.addAll(put, set.getProperty("skip").split(","));
  HashMap res=new HashMap();
  Res = res;
  String[] list=set.getProperty("image").split(",");
  Integer rp=Integer.valueOf(-1);
  for (String str:list)
   res.put(str, rp);
  list = set.getProperty("images").split(",");
  rp = Integer.valueOf(0);
  for (String str:list)
   res.put(str, rp);
  list = set.getProperty("music").split(",");
  rp = Integer.valueOf(1);
  for (String str:list)
   res.put(str, rp);
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
   lod.src = str;
   lod.call();
   map.put(str, lod);
  } else lod = (loder)o;
  if (lod.str == null) {
   lod.use = true;
   replace(lod, new StringBuilder());
  }
  return lod;
 }
 void replace(loder ini, StringBuilder buff) throws Exception {
  boolean isini=ini.isini;
  ini.str = FileName(isini ?3: 0);
  loder alls=null;
  buff.setLength(0);
  String file = loder.getSuperPath(ini.src);
  tag:
  if (isini) {
   int i=file.length();
   buff.append(file);
   do{
    buff.append("all-units.template");
    ZipArchiveEntry en=toPath(buff.toString());
    if (en != null) {
     alls = replace(en);
     break;
    }
    i = file.lastIndexOf("/", --i);
    buff.setLength(i + 1);
   }while(i > 0);
   buff.setLength(0);
  }
  loder[] orr=null;
  HashMap map=ini.ini;
  cpys cp=(cpys)map.get("core");
  if (cp != null) {
   HashMap core=cp.m;
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
      copyKey copy=lod.copy;
      loder tk;
      if (copy != null && (tk = lod.put.all) != null) {
       //存在bug，请尽量避免对多态all-tmp的ini对象使用宏
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
  copyKey key=new copyKey(orr, null);
  ini.copy = key;
  iniobj old=(iniobj)coe.get(key);
  if (old != null && alls != old.all) {
   key = new copyKey(orr, alls);
   old = (iniobj)coe.get(key);
   if (old != null) {
    iniobj nw=new iniobj();
    coe.put(key, nw);
    nw.unclone = true;
    nw.put(old, null);
    old = nw;
   }
  }
  if (old == null) {
   key.all = alls;
   coe.put(key, old = new iniobj());
   old.unclone = true;
   if (orr != null)for (loder lod:orr)old.put(lod.put, lod);
  }
  old.mbuff = buff;
  old.all = alls;
  ini.old = old;
  key = new copyKey(new loder[]{ini}, null);
  key.all = alls;
  iniobj put = new iniobj(ini);
  coe.put(key, put);
  ini.put = put;
  put.all = alls;
  put.mbuff = buff;
  put.put(old, null);
 }
 void write(loder ini, StringBuilder buff) throws Throwable {
  Map gr=grops;
  String ms[]=null;
  String file=ini.src;
  if (ini.isini && gr != null) {
   String fin=file;
   while ((ms = (String[])gr.get(fin)) == null && fin.length() != 0)
    fin = loder.getSuperPath(fin);
  }
  file = loder.getSuperPath(file);
  HashMap map=ini.ini;
  buff.setLength(0);
  boolean ws=ini.allindex > 0;
  loder orr[];
  loder all;
  boolean notmp;
  if ((orr = ini.copy.copy) != null | (notmp = ((all = ini.put.all) != null && !ini.notmp))) {
   cpys cp=(cpys)map.get("core");
   HashMap core;
   if (cp == null) {
    cp = new cpys();
    cp.m = core = new HashMap();
    map.put("core", cp);
   } else core = cp.m;
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
      if (obj.put.all == all) {
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
  iniobj put=ini.put;
  HashMap as=put.put;
  iniobj old=ini.old;
  HashMap oldsrc=old.put;
  for (Map.Entry<String,cpys>en:(Set<Map.Entry<String,cpys>>)as.entrySet()) {
   String ac=en.getKey();
   cpys cpys=en.getValue();
   HashMap asmap = cpys.m;
   HashMap asput = cpys.skip;
   cpys asold=(cpys)oldsrc.get(ac);
   HashMap oldmap;
   HashMap lastcoe;
   if (asold != null) {
    oldmap = asold.m;
    lastcoe = asold.coe;
   } else {
    oldmap = null;
    lastcoe = null;
   }
   cpys licp=(cpys)map.get(ac);
   HashMap list=licp == null ?null: licp.m;
   for (Map.Entry<String,String> en2:(Set<Map.Entry<String,String>>)asmap.entrySet()) {
    String key=en2.getKey();
    if (!skip.contains(key)) {
     String value=en2.getValue();
     boolean eq= oldmap != null && value.equals(oldmap.get(key)); 
     boolean same=!asput.containsKey(key);
     HashMap<String, HashMap> res=rwmodProtect.Res;
     Object o=res.get(key);
     if (o != null) {
      int type = (Integer)o;
      String next=put.get(value, ac, cpys);
      if (next != null) {
       String[] nowlist=AllPath(next, key, file, type);
       String[] lastlist=null;
       loder coe;
       eq = eq && asold != null && next.equals(str = old.get(value, ac, asold)) && Arrays.equals(nowlist, lastlist = (str == null || lastcoe == null || (coe = (loder)lastcoe.get(key)) == null) ?null: AllPath(str, ac, (str = coe.src) == null ?null: loder.getSuperPath(str), type)) && !ws;
       if (!eq && !same && lastlist != nowlist) {
        if (list == null) {
         cpys cp=new cpys();
         cp.m = list = new HashMap();
         map.put(ac, cp);
        }
        char c=0;
        if (type == 0)c = '*';
        else if (type > 0)c = ':';
        if (nowlist != null) {
         buff.setLength(0);
         for (String add:nowlist) {
          int st=ResTry(add, type <= 0, buff);
          if (st >= 0) {
           int i = 0;
           if (c != 0)i = add.lastIndexOf(c);
           if (i <= 0)i = add.length();
           if (ws)buff.append("ROOT:");
           str = add.substring(st, i);
           ZipArchiveEntry ze = toPath(str);
           if (ze != null) {
            str = ze.getName();
            HashMap fmp = Filemap;
            res re=(res)fmp.get(str);
            if (re == null) {
             re = new res();
             fmp.put(str, re);
             int at;
             if (type <= 0)at = 0;
             else {
              at = getType(str);
              if (at != 4)at = 5;
             }
             re.str = str = FileName(at);
            } else str = re.str;
            if (!re.close) {
             re.close = true;
             cre.addArchiveEntry(lib.getArc(str), new inputsu(Zip, ze));
            }
           }
           buff.append(str);
           if (i > 0)buff.append(add, i, add.length());
          } else buff.append(add);
          buff.append(',');
         }
         buff.setLength(buff.length() - 1);
         value = buff.toString();
        }
        list.put(key, value);
       }
      }
     }
     if (list != null && eq)list.remove(key);
    }
   }
   if (ms != null) {
    for (String s:ms) {
     if (ac.startsWith(s)) {
      cpys cp;
      if (list == null)cp = defcs;
      else cp = (cpys)map.remove(s);
      map.put(s, cp);
     }
    }
   }
  }
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
 String[] AllPath(String str, String s, String path, int type) {
  if (str.equalsIgnoreCase("none") || str.equals("IGNORE") || (str.equalsIgnoreCase("auto") && s.equals("image_shadow")))
   return null;
  str = str.replace('\\', '/');
  StringBuilder buff=Buff;
  String list[];
  buff.setLength(0);
  boolean ru=false;
  list = str.split(",", type < 0 ?1: Integer.MAX_VALUE);
  int l=list.length,m=0;
  do {
   buff.setLength(0);
   str = list[m].trim();
   int st=ResTry(str, type <= 0, buff);
   boolean tag=st >= 0;
   if (tag && path == null)return new String[]{};
   ru = ru || tag;
   if (tag) {
    if (str.startsWith("ROOT:", st)) {
     st += 5;
     path = rootPath;
    }
    if (type <= 0) {
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
   list[m] = buff.toString();
  }while(++m < l);
  if (!ru)list = null;
  return list;
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
   Collection<loder> en = iniMap.values();
   try {
    StringBuilder buff=new StringBuilder();
    for (loder lod:en) {
     if (lod.isini && lod.str == null) {
      replace(lod, buff);
     }
    }
    Collection<iniobj> vl=(Collection<iniobj>)coeMap.values();
    for (iniobj put:vl) {
     loder all;
     if ((all = put.all) != null)put.put(all.put, all);
    }
    for (iniobj put:vl)put.as();
    for (loder lod:en) {
     if (!lod.isini && !lod.use)continue;
     write(lod, buff);
    }
    en = iniHide.values();
    for (loder lod:en) {
     if (!lod.use)continue;
     write(lod, buff);
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
   ZipArchiveEntry rules=zip.getEntry("rules.md");
   if (rules != null) {
    Properties grs=new Properties();
    grs.load(zip.getInputStream(rules));
    grops = (Map)grs;
    Iterator ite=grops.entrySet().iterator();
    while (ite.hasNext()) {
     Map.Entry en=(Map.Entry)ite.next();
     en.setValue(((String)en.getValue()).split(","));
    }
   }
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
   zipout = new zipout(Ou);
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
    if (zipEntry.getCompressedSize() != 0l) { 
     name = zipEntry.getName();
     int type=getType(name);
     boolean istm=type == 2;
     if (istm || type == 3) {
      loder lod=new loder(zip.getInputStream(zipEntry));
      lod.src = name;
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
