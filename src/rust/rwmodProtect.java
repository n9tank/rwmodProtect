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
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class rwmodProtect implements Runnable {
 public String In;
 public int iniIndex=-2;
 public int fileIndex=-1;
 public int oggIndex=-2;
 public ZipFile Zip;
 public ZipOutputStream Zipout;
 public HashMap Filemap;
 public HashMap Oggmap;
 public HashMap iniMap;
 public HashMap iniHide;
 public ByteBuffer Warp;
 public WritableByteChannel Out;
 public OutputStreamWriter Ow;
 public StringBuilder Buff;
 public String musicPath;
 public ui ui;
 public static int max;
 public static String fileD;
 public static String iniD;
 public static HashMap<String,HashMap> Res;
 public static HashSet music;
 public static HashMap set(Object o, int i) {
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
 public static void init(String def) {
  HashMap<String,HashMap> map;
  try {
   map = new loder(new FileReader(def)).ini;
   HashMap<String,String> set=map.get("set");
   String str=set.get("line");
   String list[]=str.split(",");
   loder.max = Integer.valueOf(list[0]);
   loder.vlmax = Integer.valueOf(list[1]);
   max = Integer.valueOf(list[2]);
   String file= set.get("file");
   fileD = file;
   String ini=set.get("ini");
   if (ini == null) {
    ini = file;
   } else if (ini.startsWith("+")) {
    StringBuilder buff=new StringBuilder(file);
    buff.append(ini, 1, ini.length());
    ini = buff.toString();
   }
   iniD = ini;
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
   loder.put(image, music, false);
   Res = image;
  } catch (Exception e) {
  }
 }
 public rwmodProtect(String in, ui def) {
  In = in;
  ui = def;
 }
 public String FileName(int ini) {
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
  if(i>=0){
  String d=ini > 0 ?iniD: fileD;
  int l=d.length();
  do{
   int u=i % l;
   i /= l;
   buff.append(d.charAt(u));
  }while(i > 0);
  if (ini > 0) {
   buff.append(".ini");
  } else if (ini == -1) {
   buff.append(".ogg");
  }
  }
  buff.append('/');
  return buff.toString();
 }
 public void copy(String name, ZipEntry en) {
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
   }
   in.close();
   zipw.closeEntry();
  } catch (Exception e) {
  }
 }
 public loder replace(String str,boolean isini) {
  loder lod=null;
  str = toPath(str);
  byte type;
  Object o;
  if (!isini||(o=iniMap.get(str)) == null) {
   type = 0;
   HashMap map=iniHide;
   o = map.get(str);
   if (o == null) {
    ZipFile zip=Zip;
    try {
     lod = new loder(zip.getInputStream(zip.getEntry(str)));
     map.put(str, lod);
    } catch (Exception e) {
    }
   }else lod=(loder)o;
  } else{
  type = 1;
  lod=(loder)o;
  }
  if (lod.str == null) {
   String r=FileName(type);
   lod.str = r;
   StringBuilder buff=new StringBuilder();
   replaceCopy(lod, str, getType(str) > 0, buff);
   replaceAllRes(lod, str, buff);
   write(lod, r);
  }
  return lod;
 }
 public loder getloder(String filename) {
  Object o;
  o = iniMap.get(filename);
  if (o == null) {
   o = iniHide.get(filename);
  }
  return (loder)o;
 }
 public void replaceR(String str, String path, StringBuilder buff, boolean isimg) {
  String file;
  tag: {
   if (!isimg) {
    if (music.contains(str))break tag;
    file = loder.getPath(str, path);
   } else file = loder.getImagePath(str, path, buff);
   if (file != null) {
    file = toPath(file);
    byte type=getType(file);
    HashMap map;
    if (type == -2) {
     map = Filemap;
    } else {
     map = Oggmap;
    }
    Object o=map.get(file);
    if (o == null) {
     str = FileName(type);
     copy(str, Zip.getEntry(file));
     map.put(file, str);
    } else str = (String)o;
   }
  }
  buff.append(str);
  buff.append(',');
 }
 public void replaceCopy(loder ini, String file, boolean isini, StringBuilder buff) {
  HashMap inihide=iniHide;
  loder put;
  tag:
  if (isini) {
   String str=file;
   int i=str.length();
   buff.setLength(0);
   buff.append(file);
   do{
    i = str.lastIndexOf("/", --i);
    buff.setLength(i + 1);
    buff.append("all-units.template");
    str = buff.toString();
    if (inihide.containsKey(str)) {
     put = replace(str,false);
     ini.put(put);
     buff.setLength(0);
     buff.append(put.str);
     buff.append(',');
     break tag;
    }
   }while(i > 0);
   buff.setLength(0);
  }
  file = loder.getSuperPath(file);
  boolean v=buff.length() > 0;
  HashMap map=ini.ini;
  Object o=map.get("core");
  if (o != null) {
   HashMap core=(HashMap)o;
   o = core.get("copyFrom");
   String str;
   if (o != null && (str = (String)o).length() != 0 && !str.equals("IGNORE")) {
    String list[]=str.split(",");
    int i=0,n=list.length;
    do {
     String path=list[i].trim();
     str = toPath(loder.getPath(str, file));
     loder lod=null;
     if (str != null) {
      lod =replace(str,getType(str)>0);
      path = lod.str;
     } else lod = getloder(str);
     ini.put(lod);
     buff.append(path);
     buff.append(',');
    }while(++i < n);
   }
   int i=buff.length();
   if (--i >= 0)buff.setLength(i);
   if (o != null || v)core.put("copyFrom", buff.toString());
  }
 }
 public void write(loder ini, String name) {
  ZipOutputStream zip=Zipout;
  OutputStreamWriter out=Ow;
  try {
   zip.putNextEntry(new ZipEntry(name));
   ini.write(out);
   zip.closeEntry();
  } catch (Exception e) {
  }
 }
 public byte getType(String file) {
  int i=file.length();
  if (file.endsWith("/"))--i;
  int ed=i;
  i -= 4;
  if (file.startsWith(".ini", i)) {
   if (!iniHide.containsKey(file)) {
    return 1;
   } else return -2;
  } else if (file.startsWith(".tmx", i)) {
   return -1;
  } else {
   i -= 14;
   String end="all-units.template";
   if (file.startsWith(end, i)) {
    if (i > 0) {
     if (file.startsWith("/", --i))return 0;
    } else if (i == 0)return 0;
   }
  }
  String path=musicPath;
  if (path != null && file.startsWith(musicPath) && file.startsWith(".ogg", ed) && !iniHide.containsKey(file)) {
   return -3;
  }
  return -2;
 }
 public String toPath(String str) {
  str = str.replaceAll("//+", "/").replaceAll("^//", "");
  ZipFile zip=Zip;
  if (!str.endsWith("/")) {
   ZipEntry en=zip.getEntry(str);
   if (en == null) {
    str = str.concat("/");
   }
  }
  return str;
 }
 public void replaceAllRes(loder ini, String filename, StringBuilder buff) {
  String str;
  HashMap<String, HashMap> res=Res;
  HashMap need=ini.eqz();
  HashMap as=ini.as;
  Iterator ite2=as.entrySet().iterator();
  while (ite2.hasNext()) {
   Map.Entry<String,HashMap> en=(Map.Entry)ite2.next();
   String o=en.getKey();
   str = o;
   int i;
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
     str = ini.get(str, as, list);
     if (str==null||str.equals("IGNORE") || str.equalsIgnoreCase("none"))continue;
     if (str.equalsIgnoreCase("auto") && s.equals("image_shadow"))continue;
     i = en2.getValue();
     buff.setLength(0);
     switch (i) {
      case 1:
       replaceR(str, loder.getSuperPath(filename), buff, true);
       break;
      case 2:
       String path = loder.getSuperPath(filename);
       String list2[]=str.split(",");
       int l=0,size=list2.length;
       do {
        str = list2[l].trim();
        replaceR(str, path, buff, true);
       }while(++l < size);
       break;
      case 3:
       replaceR(str, loder.getSuperPath(filename), buff, false);
       break;
     }
     i = buff.length();
     if (--i >= 0)buff.setLength(i);
     list.put(s,buff.toString());
    }
   }
  }
  ini.put(need);
 }
 public void run() {
  HashMap filemap = new HashMap();
  Filemap = filemap;
  HashMap inimap = new HashMap();
  iniMap = inimap;
  HashMap inihide = new HashMap();
  iniHide = inihide;
  Oggmap = new HashMap();
  ByteBuffer warp = ByteBuffer.allocateDirect(8192);
  Warp = warp;
  Buff = new StringBuilder();
  StringBuilder buff=new StringBuilder();
  String musicpath=null;
  File ou=new File(In);
  try {
   ZipFile zip = new ZipFile(ou);
   String name=ou.getName();
   int l=name.length();
   if (name.startsWith(".", l -= 6)) {
    name = name.substring(0, l);
   }
   ou = new File(ou.getParent(), name.concat("_r.rwmod"));
   Zip = zip;
   ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(ou)));
   Zipout = zipout;
   zipout.setLevel(9);
   OutputStreamWriter wt=new OutputStreamWriter(zipout);
   Ow = wt;
   ZipEntry inf=zip.getEntry("mod-info.txt");
   if (inf != null) {
    loder ini=new loder(zip.getInputStream(inf));
    HashMap info=ini.ini;
    Object o=info.get("music");
    if (o != null) {
     HashMap map=(HashMap)o;
     o = map.get("sourceFolder");
     if (o != null) {
      musicpath = (String)o;
      if (musicpath.length() != 0) {
       if (!musicpath.endsWith("/")) {
        musicpath = musicpath.concat("/");
       }
      }
      musicPath = musicpath;
      map.put("sourceFolder", "");
     }
    }
    ini.as=ini.put;
    write(ini,"mod-info.txt/");
   }
   WritableByteChannel out = Channels.newChannel(zipout);
   Out = out;
   try {
    Enumeration<? extends ZipEntry> zipEntrys=zip.entries();
    while (zipEntrys.hasMoreElements()) {
     ZipEntry zipEntry=zipEntrys.nextElement();
     if (zipEntry.getSize() != 0) {
      String fileName=zipEntry.getName();   
      byte type=getType(fileName);
      if (type >= 0) {
       loder lod=new loder(zip.getInputStream(zipEntry));
       Object o=lod.ini.get("core");
       if (o != null) {
        HashMap map=(HashMap)o;
        String str=(String)map.get("dont_load");
        if ("1".equals(str) || "true".equalsIgnoreCase(str)){
         type = 0;
         map.remove("dont_load");
        }
       }
       if (type > 0) {
        inimap.put(fileName, lod);
       } else {
        inihide.put(fileName, lod);
       }
      }
     }
    }
    Iterator<Map.Entry> ite=inimap.entrySet().iterator();
    wh:
    while (ite.hasNext()) {
     Map.Entry<String,loder> ini=ite.next();
     String filename=ini.getKey();
     loder loder=ini.getValue();
     byte type=1;
     String r=loder.str;
     if (r == null) {
      r = FileName(type);
      loder.str = r;
      replaceCopy(loder, filename, true, buff);
      replaceAllRes(loder, filename, buff);
      write(loder, r);
     }
    }
    zipEntrys = zip.entries();
    while (zipEntrys.hasMoreElements()) {
     ZipEntry en=zipEntrys.nextElement();
     String file=en.getName();
     byte type= getType(file);
     if (type < 0) {
      if (type == -1 && !filemap.containsKey(file)) {
       copy(file.concat("/"), en);
      } else if (type == -3) {
       copy(FileName(type), en);
      }
     }
    }
   } catch (Exception e) {
   }
   wt.close();
   out.close();
   zip.close();
  } catch (Exception e) {
  }
  ui.finsh();
 }
}
