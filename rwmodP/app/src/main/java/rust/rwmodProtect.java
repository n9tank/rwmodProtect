package rust;

import android.util.Log;
import carsh.log;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.zipOut;
import rust.copyKey;
import rust.iniobj;
import rust.loder;
import rust.rwmap;
import rust.savedump;
import rust.zippack;
import rust.zipunpack;
public class rwmodProtect extends TaskWait implements Consumer {
 HashMap lowmap;
 Vector<rawcopy> rawq;
 ConcurrentHashMap resmap;
 zipOut out;
 ParallelScatterZipCreator cre;
 ConcurrentHashMap coeMap;
 int arr[];
 AtomicInteger adds[];
 String musicPath;
 int musicPut=-1; 
 String oggput; 
 boolean raw; 
 static int maxSplit;
 static int splitMod; 
 static HashSet skip;
 static char[] cr;
 static HashMap<String,Integer> Res;
 public rwmodProtect(File in, File ou, ui ui, boolean rw) {
  super(in, ou, ui);
  raw = rw;  
 }
 public loder getLoder(String str) throws Throwable {
  ZipEntry za=toPath(str);
  if (za == null)return null;
  str = za.getName();
  return addLoder(za, str, getType(str) == 4);
 }
 public static void init(InputStream io)throws Exception {
  loder ini=new loder();
  ini.read = io;
  ini.call(); 
  HashMap<String,cpys> src=ini.ini;
  HashMap re=src.get("tmx").m;
  for (Map.Entry<String,Object> en:(Set<Map.Entry>)re.entrySet()) {
   HashSet add=new HashSet();
   Collections.addAll(add, ((String)en.getValue()).split(","));  
   en.setValue(add);
  }
  rwmap.remove = re;
  HashMap<String,String> set=src.get("dump").m;
  savedump.xmlencode = set.get("encode"); 
  zipunpack.boomlen = Integer.parseInt(set.get("boomlen"));    
  set = src.get("ini").m;
  String str[]=set.get("head").split(",");
  int len=str.length;  
  if (len > 1) {
   zipOut.head = new byte[]{(byte)0x50,(byte)0x4B,(byte)0x03,(byte)0x04,(byte)0x14,(byte)0x00,(byte)0x01,(byte)0x08,(byte)0x08,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
   int[] irr=new int[len];
   while (--len >= 0)irr[len] = Integer.parseInt(str[len]);
   zipOut.ran = irr;
  } else if (str[0].length() > 0)zipOut.head = new byte[]{(byte) 0x50,(byte) 0x4B,(byte) 0x03, (byte) 0x04};
  char irr[]=set.get("split").toCharArray();
  if (irr.length > 0) {
   maxSplit = irr[0] - '/';
   splitMod = (int)Math.pow(maxSplit, irr[1] - '/');  
  }
  zippack.keepUnSize = set.get("keep").length() > 0;
  cr = set.get("chars").toCharArray();
  HashSet put=new HashSet();
  skip = put;
  Collections.addAll(put, set.get("skip").split(","));
  HashMap res=new HashMap();
  Res = res;
  putType(res, set, "image", -1);
  putType(res, set, "images", 0);
  putType(res, set, "music", 1);  
 }
 public static void putType(HashMap res, HashMap set, String key, int type) {
  String[] list=((String)set.get(key)).split(",");
  Integer rp=Integer.valueOf(type);
  for (String str:list)
   res.put(str, rp);
 } 
 public ZipEntry toPath(String str) {
  ZipEntry za=Zip.getEntry(str);
  if (za == null) {
   String low=str.toLowerCase();
   za = (ZipEntry)lowmap.get(low);
  }
  return za;
 }
 void append(int i, StringBuilder buff) {
  if (i >= 0) {
   char[] irr=cr;
   int l = irr.length;
   do{
    int u=i % l;
    i /= l;
    buff.append(irr[u]);
   }while(i > 0);
  }
 }
 void appendName(int i, boolean checkOgg, StringBuilder buff) {
  int max=splitMod;
  if (max > 0) {
   int u=maxSplit;   
   int music=musicPut;   
   if (checkOgg && music >= 0 && (i - (i % u)) == music)i += u;
   int end=i % max;
   append(i /= max, buff);
   while ((max /= u) > 0) {
    buff.append('/');
    append(end % u, buff);
    end /= u;    
   }
  } else append(i, buff);
 }
 String safeName(int ini, StringBuilder buff) {
  buff.setLength(0);
  if (ini < 4) {
   AtomicInteger add=adds[--ini];
   int i=((int)add.incrementAndGet() - 1);
   appendName(i, ini == 2, buff);
   String ed;
   switch (ini) {
	case 1:
	 ed = ".wav";
	 break;
	case 2:
	 ed = ".ogg";
	 break;
	default:
	 ed = "";
	 break;
   }
   buff.append(ed);
  } else {
   int i=arr[ini -= 4]++;
   if (ini > 0) {  
    buff.append(oggput);
    buff.append('/');  
    if (ini > 1)buff.append("[noloop]");
   } 
   if (ini == 0) {
    appendName(i, false, buff);
    buff.append(".ini");
   } else {
    append(i, buff);
    buff.append(".ogg"); 
   }    
  }
  if (ini == 0)buff.append('/');  
  return buff.toString();
 }
 public boolean lod(loder ini) {
  //便于改动到并行加载，没多大优化，主要耗时为io流。
  ConcurrentHashMap coe=coeMap;
  copyKey key=ini.copy;
  iniobj old=new iniobj();
  Object obj=coe.putIfAbsent(key, ini);
  if (obj == null) {
   lod(old, key.copy, ini.all);
   coe.put(key, old); 
  } else if (obj instanceof iniobj) {
   old.put((iniobj)obj, null);
  } else return false;
  ini.old = old;
  super.lod(ini);
  return true;
 }
 public void appstr(String str, int last, loder put, StringBuilder buff) {
  String add=put.str;
  int st=0;  
  if (put.task != this)buff.append("CORE:");
  else if (maxSplit > 0) {
   if (add.regionMatches(0, str, 0, last))
    st = add.lastIndexOf('/', add.length() - 3) + 1;
   else buff.append("ROOT:");
  }
  buff.append(add, st, add.length());
  buff.append(',');
 } 
 public static iniobj em=new iniobj();
 void write(loder ini) throws Throwable {
  copyKey ck=ini.copy;
  loder[] orr= ck.copy;
  loder alls=ini.all;
  StringBuilder buff=new StringBuilder();
  StringBuilder bf=new StringBuilder();
  String file=ini.src;
  boolean ws=maxSplit > 0;  
  file = loder.getSuperPath(file);
  HashMap map=ini.ini;
  copyKey copy=ini.copy;
  if ((orr = copy.copy) != null || alls != null) {
   cpys cp=(cpys)map.get("core");
   HashMap core;
   if (cp == null) {
    cp = new cpys();
    cp.m = core = new HashMap();
    map.put("core", cp);
   } else core = cp.m;
   String str=ini.str;
   int last=str.lastIndexOf('/', str.length() - 3); 
   if (alls != null) {
    boolean notmp=false;
    if (orr != null) {
     for (loder lod:orr) {
      if (alls == lod.all) {
       notmp = true;        
       break;
      }
     }
    }
    if (!notmp)appstr(str, last, alls, buff);
   }
   if (orr != null) {
    for (loder obj:orr)appstr(str, last, obj, buff);  
   }
   buff.setLength(buff.length() - 1);
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
       eq &= same && (lastcoe != null && (coe = (loder)lastcoe.get(key)) != null && Arrays.equals(nowlist, AllPath(next, loder.getSuperPath(coe.src), type, buff)));
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
           ZipEntry ze = toPath(str);
           if (ze != null) {
            String name=ze.getName();
			Object obj;
			do{
			 obj = resmap.putIfAbsent(name, "");
			}while(obj == "");
			if (obj == null) {
			 resmap.put(name, str = safeName(getType(name), bf));
			 ZipArchiveEntry outen=lib.getArc(str);
             if (type <= 0)outen.setMethod(0);
			 if (raw || type <= 0) {
              Vector raw=rawq;                            
			  rawcopy craw=new rawcopy();
			  craw.form = ze;
			  craw.to = outen;
			  raw.add(craw);
			 } else {
              cre.addArchiveEntry(outen, new inputsu(Zip, ze));
             }} else str = (String)obj;
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
	}}}
  cre.addArchiveEntry(lib.getArc(ini.str), ini);
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
   return 4;
  } else if (file.regionMatches(true, ed, ".tmx", 0, 4) || file.regionMatches(true, ed - 4, "_map.png", 0, 8))
   return 0;
  String path=musicPath;
  if (file.regionMatches(true, i, ".ogg", 0, 4)) {
   if (path != null && file.startsWith(path)) {
    if (file.indexOf("[noloop]", path.length()) < 0)return 5;
    return 6;
   } else {
    return 3;
   }
  } else if (file.regionMatches(true, i, ".wav", 0, 4)) {
   return 2;
  }
  return 1;
 }
 int is;
 public void accept(Object o) {
  switch (is) {
   case 0:
	loder lod=(loder)o;
	lod.put.as();
	break;
   case 1:
	try {
	 write((loder)o);
	} catch (Throwable e) {
	 is = 2;
	 log.e(this, e);
	}
	break;
  }
 }
 public void end(Throwable e) {
  if (e == null) {
   List vl=Arrays.asList(Zipmap.values().toArray());
   vl.parallelStream().forEach(this);
   is = 1;
   Collections.shuffle(vl); 
   StringBuilder bf=new StringBuilder();
   for (Object obj:vl) {
    loder lod=(loder)obj;
    lod.str = safeName(lod.isini ?4: 1, bf);
   }   
   vl.parallelStream().forEach(this);
   Vector arr=rawq;
   if (arr != null) { 
	try {
	 for (rawcopy arrs:rawq) {
      ZipEntry from=arrs.form;         
	  try(InputStream io=Zip.getInputStream(from)){
       ZipArchiveEntry put=arrs.to;     
       put.setSize(from.getSize());
       if(zippack.keepUnSize)put.setCompressedSize(from.getCompressedSize());     
	   out.addRawArchiveEntry(put, zipunpack.getRaw(io, from, put.getMethod()));
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
  rawq = new Vector();  
  char irr[]=cr;  
  resmap = new ConcurrentHashMap();
  AtomicInteger[] add=new AtomicInteger[3];
  adds = add;
  int i=3;
  while (i > 0)add[--i] = new AtomicInteger();
  arr = new int[4];
  HashMap lows=new HashMap();
  lowmap = lows;
  coeMap = new ConcurrentHashMap();
  StringBuilder mbuff = new StringBuilder();
  try {
   ZipFile zip=new ZipFile(In);
   Zip = zip;
   zipOut zipout = new zipOut(Ou);
   out = zipout;
   ParallelScatterZipCreator cr = lib.prc(9);
   cre = cr;
   String name=null;
   HashSet rset=new HashSet();
   Enumeration<? extends ZipEntry> zipEntrys=zip.entries();
   do{
    ZipEntry zipEntry=zipEntrys.nextElement();
    String fileName=zipEntry.getName();
    String root=loder.getSuperPath(fileName);
    if (!rset.add(root) && (name == null || root.length() < name.length()))name = root;
    lows.putIfAbsent(fileName.toLowerCase(), zipEntry);
   }while(zipEntrys.hasMoreElements());
   rootPath = name;
   ZipEntry inf=toPath(name.concat("mod-info.txt"));
   if (inf != null) {
    loder ini=new loder();
    ini.read = zip.getInputStream(inf);
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
      int max=maxSplit;      
      appendName(musicPut = new Random().nextInt(5 * Math.max(1, max)), false, mbuff);
      if (max > 0)mbuff.setLength(mbuff.length() - 2);    
      map.put("sourceFolder", oggput = mbuff.toString());
     }
    }
    cre.addArchiveEntry(lib.getArc("mod-info.txt/"), ini);
   }
   ArrayList<ZipEntry> ogg=new ArrayList();
   zipEntrys = zip.entries();
   do{
    ZipEntry zipEntry=zipEntrys.nextElement();
    name = zipEntry.getName();
	int type=getType(name);
	if (type == 4) {
	 addLoder(zipEntry, name, true);
	} else if (type == 0) {
	 cr.addArchiveEntry(lib.getArc(loder.getName(name).concat("/")), new inputsu(zip, zipEntry));
	} else if (type >= 5) {
     ogg.add(zipEntry);     
    }
   }while(zipEntrys.hasMoreElements());
   Collections.shuffle(ogg);
   for (ZipEntry en:ogg)
    cr.addArchiveEntry(lib.getArc(safeName(getType(en.getName()), mbuff)), new inputsu(zip, en));
   down(null);
  } catch (Throwable e) {
   down(e);
  }
 }
}
