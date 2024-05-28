package rust;

import carsh.log;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
public class rwmodProtect extends TaskWait implements Consumer {
 HashMap lowmap;
 Vector<rawcopy> rawq;
 ConcurrentHashMap resmap;
 ZipArchiveOutputStream out;
 ParallelScatterZipCreator cre;
 ConcurrentHashMap coeMap;
 int arr[];
 AtomicInteger adds[];
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
 static HashMap<String,Integer> Res;
 public rwmodProtect(File in, File ou, ui ui, boolean rw) {
  super(in, ou, ui);
  if (rw)rawq = new Vector();
  addN(this);
 }
 public loder getLoder(String str) throws Throwable {
  ZipArchiveEntry za=toPath(str);
  if (za == null)return null;
  str = za.getName();
  return addLoder(za, str, getType(str) == 2);
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
 String safeName(int ini, StringBuilder buff) {
  buff.setLength(0);
  if (ini < 5) {
   AtomicInteger add=adds[--ini];
   int i=((int)add.incrementAndGet() - 1);
   if (ini > 0)--i;
   appendName(i, buff);
   String ed;
   switch (ini) {
	case 1:
	 ed = ".ini";
	 break;
	case 2:
	 ed = ".wav";
	 break;
	case 3:
	 ed = ".ogg";
	 break;
	default:
	 ed = "";
	 break;
   }
   buff.append(ed);
   if (ini < 2)buff.append('/');
  } else {
   ini -= 5;
   int i=++arr[ini] - 2;
   buff.append("￸/");
   if (ini != 0)buff.append("[noloop]");
   appendName(i, buff);
   buff.append(".ogg");
  }
  return buff.toString();
 }
 final static iniobj em=new iniobj();
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
  copyKey ck=ini.copy;
  loder[] orr= ck.copy;
  loder alls=ck.all;
  tag:
  if (orr != null && alls != null && ini.acou == 0) {
   for (loder lod:orr) {
	if (alls == lod.copy.all && lod.acou == 0)break tag;
   }
   ini.notmp = false;
  }
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
     HashMap<String, Integer> res=rwmodProtect.Res;
     Object o=res.get(key);
     if (o != null) {
      int type = (Integer)o;
      String next=put.get(value, ac, cpys);
      if (next != null) {
       String[] nowlist=AllPath(next, file, type, buff);
       loder coe=null;
       boolean same=value.equals(next);
       eq &= same && (lastcoe != null && (coe = (loder)lastcoe.get(key)) != null && Arrays.equals(nowlist, AllPath(next, loder.getSuperPath(coe.src), type, buff)) && (!ws || (coe != null && coe.copy.all == all)));
       //补修宏绕过
       if (!same || !eq) {
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
			Object obj;
			do{
			 obj = resmap.putIfAbsent(name, "");
			}while(obj == "");
			if (obj == null) {
			 resmap.put(name, str = safeName(getType(name), bf));
			 ZipArchiveEntry outen=lib.getArc(str);
			 Vector raw=rawq;
			 if (raw != null) {
			  rawcopy craw=new rawcopy();
			  craw.form = ze;
			  craw.to = outen;
			  raw.add(craw);
			 } else cre.addArchiveEntry(outen, new inputsu(Zip, ze));
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
	 //补修all-tmp删除key冲突
	 if ((ini.isini && !ws) && list != null && eq)list.remove(key);
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
  if (str == null) {
   synchronized (ini) {
	if ((str = ini.str) == null) {
	 ini.str = str = safeName(ini.isini ?2: 1, bf);
	}
   }
  }
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
 String[] AllPath(String str, String path, int type, StringBuilder buff) {
  //不予修复非法auto图像
  if (str.length() == 0 || str.equalsIgnoreCase("none") || str.equals("IGNORE") || str.equalsIgnoreCase("auto"))
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
   str = buff.toString();
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
   return 2;
  } else if (file.regionMatches(true, ed, ".tmx", 0, 4) || file.regionMatches(true, ed - 4, "_map.png", 0, 8))
   return 0;
  String path=musicPath;
  if (file.regionMatches(true, i, ".ogg", 0, 4)) {
   if (path != null && file.startsWith(path)) {
    if (file.indexOf("[noloop]", path.length()) < 0)return 5;
    return 6;
   } else {
    return 4;
   }
  } else if (file.regionMatches(true, i, ".wav", 0, 4)) {
   return 3;
  }
  return 1;
 }
 int is;
 public void accept(Object o) {
  switch (is) {
   case 0:
	iniobj obj=(iniobj)o;
	loder all;
	obj.put((all = obj.all).put, all);
	break;
   case 1:
	loder lod=(loder)o;
	lod.put.as();
	break;
   case 2:
	try {
	 write((loder)o);
	} catch (Throwable e) {
	 is = 3;
	 log.e(this, e);
	}
	break;
  }
 }
 public void end(Throwable e) {
  if (e == null) {
   Collection<iniobj> vl=coeMap.values();
   int index=0;
   iniobj[] iniar=new iniobj[vl.size()];
   HashMap egl=new HashMap();
   for (iniobj obj:vl) {
    if (obj.all != null && obj.gl == null) {
     obj.gl = egl;
     iniar[index++] = obj;
    }
   }
   iniar = Arrays.copyOf(iniar, index);
   Stream.of(iniar).parallel().forEach(this);
   is = 1;
   Collection<loder> lods=Zipmap.values();
   lods.parallelStream().forEach(this);
   StringBuilder buf=new StringBuilder();
   for (loder ini:lods) {
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
		 appendName(arr[2]++, buf);
		 buf.append("/all-units.template/");
		 tk.str = buf.toString();
		 buf.setLength(buf.length() - 19);
		} else buf.append(fn, 0, fn.length() - 19);
		if (lod.str == null) {
		 appendName(++tk.acou - 2, buf);
		 buf.append(".ini/");
		 lod.str = buf.toString();
		}
	   } else ini.notmp = true;//不追加all-tmp
	  }
	 }
	}
   }
   is = 2;
   lods.parallelStream().forEach(this);
   Vector arr=rawq;
   if (arr != null) {
	try {
	 for (rawcopy arrs:rawq) {
	  InputStream io=Zip.getRawInputStream((ZipArchiveEntry)arrs.form);
	  try {
	   out.addRawArchiveEntry((ZipArchiveEntry)arrs.to, io);
	  } finally {
	   io.close();
	  }
	 }
	} catch (Throwable ex) {
	 log.e(this, e = ex);
	}
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
  resmap = new ConcurrentHashMap();
  AtomicInteger[] add=new AtomicInteger[4];
  adds = add;
  int i=4;
  while (i > 0)add[--i] = new AtomicInteger();
  arr = new int[3];
  HashMap lows=new HashMap();
  lowmap = lows;
  coeMap = new ConcurrentHashMap();
  StringBuilder mbuff = new StringBuilder();
  try {
   ZipFile zip=new ZipFile(In);
   Zip = zip;
   zipout zipout = new zipout(Ou);
   out = zipout;
   ParallelScatterZipCreator cr = lib.prc(9);
   cre = cr;
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
   String name=null;
   HashSet rset=new HashSet();
   Enumeration<? extends ZipArchiveEntry> zipEntrys=zip.getEntries();
   do{
    ZipArchiveEntry zipEntry=zipEntrys.nextElement();
    String fileName=zipEntry.getName();
    String root=loder.getSuperPath(fileName);
    if (!rset.add(root) && (name == null || root.length() < name.length()))name = root;
    lows.putIfAbsent(fileName.toLowerCase(), zipEntry);
   }while(zipEntrys.hasMoreElements());
   rootPath = name;
   ZipArchiveEntry inf=toPath(name.concat("mod-info.txt"));
   if (inf != null) {
    loder ini=new loder();
	ini.read = zip.getInputStream(inf);
	ini.task = this;
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
    ZipArchiveEntry zipEntry=zipEntrys.nextElement();     name = zipEntry.getName();
	int type=getType(name);
	if (type == 2) {
	 addLoder(zipEntry, name, true);
	} else if (type == 0) {
	 cr.addArchiveEntry(lib.getArc(loder.getName(name).concat("/")), new inputsu(zip, zipEntry));
	} else if (type >= 5) {
	 cr.addArchiveEntry(lib.getArc(safeName(type, mbuff)), new inputsu(zip, zipEntry));
	}
   }while(zipEntrys.hasMoreElements());
   ato.decrement();
  } catch (Throwable e) {
   down(e);
  }
 }
}
