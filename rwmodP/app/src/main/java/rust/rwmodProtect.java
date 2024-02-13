package rust;

import android.util.Log;
import carsh.log;
import java.io.BufferedReader;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
public class rwmodProtect extends TaskWait {
 HashMap lowmap;
 ZipArchiveOutputStream out;
 ParallelScatterZipCreator cre;
 ConcurrentHashMap coeMap;
 int arr[];
 String musicPath;
 Map grops;
 static cpys defcs;
 static{
  cpys cp=new cpys();
  HashMap list=new HashMap();
  defcs = cp;
  list.put("@define *", "");
  cp.m = list;
 }
 static HashSet skip;
 static int[] cr;
 static ArrayList<String> ds;
 static HashMap<String,HashMap> Res;
 public rwmodProtect(File in, File ou, ui ui) {
  super(in, ou, ui);
 }
 public static String out(File path) {
  String name=path.getName();
  int l=name.length();
  if (name.startsWith(".", l -= 6)) {
   name = name.substring(0, l);
  }
  return name.concat("_r.rwmod");
 }
 public loder getLoder(String str) throws Throwable {
  ZipArchiveEntry za=toPath(str);
  if (za == null)return null;
  str = za.getName();
  return addLoder(za, str, getType(str) == 3);
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
 public ZipArchiveEntry toPath(String str) {
  ZipArchiveEntry za=Zip.getEntry(str);
  if (za == null) {
   String low=str.toLowerCase();
   za = (ZipArchiveEntry)lowmap.get(low);
   if (za == null && !str.endsWith("/")) {
    za = Zip.getEntry(str.concat("/"));
    if (za == null)za = (ZipArchiveEntry)lowmap.get(low.concat("/"));
   }
  }
  return za;
 }
 void appendName(int i, StringBuilder buff) {
  if (i >= 0) {
   ArrayList srr=ds;
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
 String FileName(int ini, StringBuilder buff) {
  buff.setLength(0);
  ini -= 2;
  if (ini < 0)ini = 0;
  int i=++arr[ini] - 2;
  if (ini > 4)buff.append("￸/");
  if (ini == 5)buff.append("[noloop]");
  appendName(i, buff);
  if (ini == 1)buff.append(".ini");
  else if (ini > 2)buff.append(".ogg");
  else if (ini == 2)buff.append(".wav");
  if (ini < 2)buff.append('/');
  return buff.toString();
 }
 final static iniobj em;
 static{
  iniobj wr=new iniobj();
  wr.gl = Collections.emptyMap();
  em = wr;
 }
 public boolean lod(loder ini) {
  //便于改动到并行加载，没多大优化，主要耗时为io流。
  ConcurrentHashMap coe=coeMap;
  copyKey key= ini.copy;
  loder alls=key.all;
  loder[] lods=new loder[]{ini};
  copyKey ik=new copyKey(lods, alls);
  coe.put(ik, ini);
  copyKey iu =null;
  if (alls != null) {
   iu = new copyKey(lods, null);
   iu.all = alls;
   coe.put(iu, ini);
  }
  loder orr[];
  iniobj old;
  if ((orr = key.copy) != null || alls != null) {
   Object obj = coe.putIfAbsent(key, ini);
   if ((obj instanceof loder) && obj != ini)return false;
   old = (iniobj)obj;
   copyKey key2=null;
   if (old == null || alls != old.all) {
    if (old != null)coe.put(key, ini);
    iniobj nw = new iniobj();
    nw.all = alls;
    if (old == null) {
     if (alls != null) {
      key2 = new copyKey(orr, null);
      obj = coe.putIfAbsent(key2, ini);
      if ((obj instanceof loder) && obj != ini)return false;
      old = (iniobj)obj;
     }
     if (old == null && orr != null) {
      int i=orr.length;
      while (--i >= 0) {
       loder lod=orr[i];
       nw.put(lod.put, lod);
      }
     }
    }
    if (old != null)nw.put(old, null);
    else if (key2 != null)coe.put(key2, nw);
    coe.put(key, nw);
    old = nw;
   }
   ini.old = old;
  }
  super.lod(ini);
  iniobj obj=ini.put;
  obj.all = alls;
  coe.put(ik, obj);
  if (iu != null)coe.put(iu, obj);
  return true;
 }
 void write(loder ini) throws Throwable {
  StringBuilder buff=new StringBuilder();
  StringBuilder bf=new StringBuilder();
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
  boolean ws=ini.acou > 0;
  loder orr[];
  loder all;
  boolean notmp;
  copyKey copy=ini.copy;
  if ((orr = copy.copy) != null | (notmp = ((all = copy.all) != null && !ini.notmp))) {
   cpys cp=(cpys)map.get("core");
   HashMap core;
   if (cp == null) {
    cp = new cpys();
    cp.m = core = new HashMap();
    map.put("core", cp);
   } else core = cp.m;
   if (notmp) {
    String str=get(all, bf);
    buff.append(str);
    buff.append(',');
   }
   if (orr != null) {
    int i=0,len=orr.length;
    while (i < len) {
     loder obj=orr[i++];
     String str=get(obj, bf);
     int st=0;
     if (obj.task != this)buff.append("CORE:");
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
  if (old == null)old = em;
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
     boolean same=asput != null && !asput.containsKey(key);
     HashMap<String, HashMap> res=rwmodProtect.Res;
     Object o=res.get(key);
     if (o != null) {
      int type = (Integer)o;
      String next=put.get(value, ac, cpys);
      if (next != null) {
       String[] nowlist=AllPath(next, key, file, type, buff);
       String[] lastlist=null;
       loder coe;
       eq = eq && asold != null && next.equals(str = old.get(value, ac, asold)) && (lastcoe != null && (coe = (loder)lastcoe.get(key)) != null && Arrays.equals(nowlist, lastlist = AllPath(next, ac, loder.getSuperPath(coe.src), type, buff))) && (!ws || (coe != null && coe.copy.all == all));
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
            String name=ze.getName();
            Object obj=Zipmap.get(name);
            if (obj == null) {
             str = FileName(getType(name), bf);
             Zipmap.put(name, str);
             cre.addArchiveEntry(lib.getArc(str), new inputsu(Zip, ze));
            } else str = (String)obj;
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
  cre.addArchiveEntry(lib.getArc(get(ini, bf)), ini);
 }
 public String get(loder ini, StringBuilder bf) {
  String str = ini.str;
  if (str == null)ini.str = str = FileName(ini.isini ?3: 0, bf);
  return str;
 }
 static int ResTry(String file, boolean isimg, StringBuilder buff) {
  int st=0;
  if (isimg) {
   if (file.startsWith("SHADOW:")) {
    st = 7;
   }
   if (file.startsWith("CORE:", st) || file.startsWith("SHARED:", st))st = -1;
   if (buff != null && st > 0)buff.append("SHADOW:");
  } else {
   if (file.startsWith("ROOT:"))return 0;
   int i = file.lastIndexOf(':');
   if (i < 0)i = file.length();
   i -= 4;
   if (!(file.regionMatches(true, i, ".ogg", 0, 4) || file.regionMatches(true, i, ".wav", 0, 4)))st = -1;
  }
  return st;
 }
 String[] AllPath(String str, String s, String path, int type, StringBuilder buff) {
  if (str.equalsIgnoreCase("none") || str.equals("IGNORE") || (str.equalsIgnoreCase("auto") && s.equals("image_shadow")))
   return null;
  str = str.replace('\\', '/');
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
 int getType(String file) {
  int i=file.length() - 4;
  int ed=i;
  if (file.endsWith("/"))--ed;
  if (file.regionMatches(true, ed, ".ini", 0, 4)) {
   return 3;
  } else if (file.regionMatches(true, ed, ".tmx", 0, 4) || file.regionMatches(true, ed - 4, "_map.png", 0, 8))
   return 1;
  // else if (file.regionMatches(true, ed -5, ".template", 0, 9))return 2;
  //type==2 自定义加载类型，已被移除。
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
 static class fore implements Consumer {
  boolean is;
  public void accept(Object t) {
   iniobj ini=((iniobj)t);
   if (!is) {
    loder all=ini.all;
    if (all != null)ini.put(all.put, all);
   } else ini.as();
  }
 }
 public void end(Throwable e) {
  if (e == null) {
   try {
    Collection vl=coeMap.values();
    iniobj nolockarr[]=new iniobj[vl.size()];
    int cou=0;
    for (iniobj ob:(Collection<iniobj>)vl) {
     if (ob.gl == null) {
      ob.gl = new HashMap();
      nolockarr[cou++] = ob;
     }
    }
    nolockarr = Arrays.copyOf(nolockarr, cou);
    fore f=new fore();
    Stream.of(nolockarr).parallel().forEach(f);
    f.is = true;
    Stream.of(nolockarr).parallel().forEach(f);
    StringBuilder buf=new StringBuilder();
    vl = Zipmap.values();
    for (Object t:vl) {
     if (t instanceof loder) {
      loder ini=(loder)t;
      while (!ini.finsh);
     }
    }
    for (Object t:vl) {
     if (t instanceof loder) {
      loder ini=(loder)t;
      copyKey key=ini.copy;
      loder[] orr=key.copy;
      loder alls=key.all;
      if (orr != null) {
       for (loder lod:orr) {
        loder tk;
        if ((tk = lod.copy.all) != null) {
         //存在bug，请尽量避免对多态all-tmp的ini对象使用宏
         if (alls != tk) {
          lod.notmp = true;
          lod.acou = 1;//不在根目录
          buf.setLength(0);
          String fn=tk.str;
          if (fn == null) {
           appendName(arr[6]++, buf);
           buf.append("/all-units.template/");
           tk.acou = -1;
           tk.str = buf.toString();
           buf.setLength(buf.length() - 19);
          } else buf.append(fn, 0, fn.length() - 19);
          if (lod.str == null) {
           appendName(tk.acou++, buf);
           buf.append(".ini/");
           lod.str = buf.toString();
          }
         } else ini.notmp = true;//不追加all-tmp
        }
       }
      }
     }
    }
    for (Object t:vl) {
     if (t instanceof loder) {
      loder lod=(loder)t;
      write(lod);
     }
    }
   } catch (Throwable e2) {
    log.e(this, e = e2);
   }
  }
  ZipArchiveOutputStream zipout=out;
  if (zipout != null) {
   try {
    try {
     cre.writeTo(e != null ?null: zipout);
    } finally {
     zipout.close();
    }
   } catch (Throwable e2) {
    log.e(this, e = e2);
   }
  }
  if (e != null)Ou.delete();
  //if (!(e instanceof InterruptedException))
  back.end(e);
 }
 public void run() {
  arr = new int[7];
  arr[0] = 1;
  HashMap lows=new HashMap();
  lowmap = lows;
  coeMap = new ConcurrentHashMap();
  StringBuilder mbuff = new StringBuilder();
  ZipArchiveOutputStream zipout=null;
  ParallelScatterZipCreator cr=null;
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
    lows.putIfAbsent(fileName.toLowerCase(), zipEntry);
   }while(zipEntrys.hasMoreElements());
   //if (name == null)name = "";
   rootPath = name;
   ZipArchiveEntry inf=toPath(name.concat("mod-info.txt"));
   zipout = new zipout(Ou);
   out = zipout;
   cr = new ParallelScatterZipCreator();
   cre = cr;
   if (inf != null) {
    loder ini=new loder(zip.getInputStream(inf));
    //ini.task = this;
    ini.call();
    HashMap info=ini.ini;
    cpys cp=(cpys)info.get("music");
    if (cp != null) {
     HashMap map=cp.m;
     String str =(String)map.get("sourceFolder");
     if (str != null) {
      str = str.replace("\\", "/").replaceFirst("^/+", "");
      if (str.length() > 0 && !str.endsWith("/"))str = str.concat("/");
      musicPath = str;
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
     if (type == 3) {
      addLoder(zipEntry, name, true);
     } else if (type == 1) {
      cr.addArchiveEntry(lib.getArc(loder.getName(name).concat("/")), new inputsu(zip, zipEntry));
     } else if (type == 6) {
      cr.addArchiveEntry(lib.getArc(FileName(type, mbuff)), new inputsu(zip, zipEntry));
     }
    }
   }while(zipEntrys.hasMoreElements());
   // tas.end();
  } catch (Throwable e) {
   down(e);
  }
 }
}
