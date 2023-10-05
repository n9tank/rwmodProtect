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
import java.util.BitSet;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class rwmodProtect extends rwmodLib implements Runnable {
 public File In;
 public int iniIndex=-2;
 public int fileIndex=-1;
 public int oggIndex=-2;
 public ZipOutputStream Zipout;
 public HashMap Filemap;
 public HashMap Oggmap;
 public ByteBuffer Warp;
 public WritableByteChannel Out;
 public OutputStreamWriter Ow;
 public StringBuilder Buff;
 public String musicPath;
 public String rootPath;
 public static int max;
 public static String fileD;
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
 static{
  String str=System.getProperty("user.dir");
  if (str.length() == 1) {
   str = "sdcard/rustedWarfare/rwmod";
  }
  init(new File(str, ".ini"));
  ui.def.finsh();
 }
 public static void init(File def) {
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
   ui.def.fali(e);
  }
  rwmodLib.init(new File(def.getParent(), "lib.zip"));
 }
 public rwmodProtect(File in, ui def) {
  In = in;
  Ui = def;
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
    Ui.fali(e);
   }
   in.close();
   zipw.closeEntry();
  } catch (Exception e) {
   Ui.fali(e);
  }
 }
 public loder replace(String str, boolean isini) {
  loder lod=null;
  ZipEntry en=toPath(str);
  str = en.getName();
  Object o;
  tag: {
   if (!isini || (o = iniMap.get(str)) == null) {
    HashMap map=iniHide;
    o = map.get(str);
    if (o == null) {
     ZipFile zip=Zip;
     try {
      lod = new loder(zip.getInputStream(en));
      map.put(str, lod);
     } catch (Exception e) {
      Ui.fali(e);
     }
     break tag;
    }
   }
   lod = (loder)o;
  }
  if (lod.str == null) {
   String r=FileName(isini ?1: 0);
   lod.str = r;
   StringBuilder buff=new StringBuilder();
   replaceCopy(lod, str,isini, buff);
   replaceAllRes(lod, str,buff,isini);
   write(lod, r);
  }
  return lod;
 }
 public void replaceR(String str, String path, StringBuilder buff,int isimg,boolean post) {
  String file;
  String list[]=null;
  tag: {
   if (isimg == 2) {
    list = str.split("\\*");
    str = list[0];
   }
   if (isimg > 2) {
    boolean is=str.startsWith("ROOT:");
    if (is) {
     file = str.substring(5);
    } else if (path != null) {
     file = path.concat(str);
    } else file = str;
    list = file.split(":", 2);
    file = list[0];
    if (!is) {
     if (music.contains(str.split(":", 2)[0]))break tag;
    }
   } else file = loder.getImagePath(str, path, buff);
   if (file != null) {
    ZipEntry en = toPath(file);
    if (en != null) {
     byte type=getType(file);
     HashMap map;
    // if (type == -2) {
      map = Filemap;
     /*} else {
      map = Oggmap;
     }*/
     Object o=map.get(file);
     res res;
     if (o == null) {
      res=new res();
      res.str=str=FileName(type);
      map.put(file,res);
     } else{
     res=(res)o;
     str=res.str;
     }
     if(post&&!res.close){
     res.close=true;
     copy(str,en);
     }
    }
   }
  }
  buff.append(str);
  if (list != null && list.length > 1) {
   if (isimg == 2) {
    buff.append("*");
   } else if (isimg > 2) {
    buff.append(":");
   }
   buff.append(list[1]);
  }
  buff.append(',');
 }
 public void replaceCopy(loder ini, String file, boolean isini, StringBuilder buff) {
  file = loder.getSuperPath(file);
  BitSet bit;
  int index=0;
  tag:
  if (isini) {
   loder lod=getSpuerAll(file, buff);
   if (lod != null) {
    buff.append(lod.str);
    buff.append(',');
    index = 1;
    ini.putoff(ini.put, lod.put, true);
   }
  }
  boolean v=buff.length() > 0;
  HashMap map=ini.ini;
  Object o=map.get("core");
  if (o != null) {
   HashMap core=(HashMap)o;
   o = core.get("copyFrom");
   String str;
   if (o != null && (str = (String)o).length() > 0 && !str.equals("IGNORE")) {
    String list[]=str.split(",");
    int i=0,n=list.length;
    bit = new BitSet(index + n);
    rwmodLib libs=rwmodLib.lib;
    do {
     String path=list[i].trim();
     str = loder.getPath(path, file);
     if (str != null) {
      ZipEntry en = toPath(str);
      str = en.getName();
      loder lod =replace(str, getType(str) > 0);
      path = lod.str;
      ini.putoff(ini.put, lod.put, true);
     } else if (libs != null) {
      bit.set(index++);
      map = libs.iniMap;
      str = path.substring(5);
      loder lod=(loder)map.get(libs.toPath(str).getName());
      ini.putoff(ini.put, lod.put, true);
     }
     buff.append(path);
     buff.append(',');
    }while(++i < n);
   } else bit = null;
   int i=buff.length();
   if (--i >= 0)buff.setLength(i);
   if (o != null || v)core.put("copyFrom", buff.toString());
  } else bit = null;
  if (bit == null)bit = new BitSet(index);
  ini.bit = bit;
 }
 public void write(loder ini, String name) {
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
 public byte getType(String file) {
  int i=file.length();
  if (file.endsWith("/"))--i;
  i -= 4;
  int ed=i;
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
  if (path != null && file.startsWith(musicPath) && file.startsWith(".ogg",ed)) {
   return -3;
  }
  return -2;
 }
 public void replaceAllRes(loder ini, String filename, StringBuilder buff,boolean get) {
  String str;
  HashMap<String, HashMap> res=Res;
  ini.eqz();
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
     if (str == null || str.equalsIgnoreCase("none") || str.equals("IGNORE"))continue;
     if (str.equalsIgnoreCase("auto") && s.equals("image_shadow"))continue;
     i = en2.getValue();
     buff.setLength(0);
     switch (i) {
      case 2:
       String path = loder.getSuperPath(filename);
       String list2[]=str.split(",");
       int l=0,size=list2.length;
       do {
        str = list2[l].trim();
        replaceR(str, path, buff, i,get);
       }while(++l < size);
       break;
      case 4:
       path = loder.getSuperPath(filename);
       list2 = str.split(",");
       l = 0;
       size = list2.length;
       do {
        str = list2[l].trim();
        replaceR(str, path, buff, i,get);
       }while(++l < size);
       break;
      default:
       replaceR(str, loder.getSuperPath(filename), buff, i,get);
       break;
     }
     i = buff.length();
     if (--i >= 0)buff.setLength(i);
     list.put(s, buff.toString());
    }
   }
  }
  ini.put();
 }
 public ZipEntry toPath(String str) {
 //使用"/"根路径，游戏会出现奇奇妙妙的bug 暂不考虑兼容
  ZipEntry file=super.toPath(str);
  if (file == null) {
   return super.toPath(rootPath.concat(str));
  }
  return file;
 }
 /*
 .tmx .ogg
 这些行为对游戏有影响，我认为没有这个必要
 _map.png 没有使用场景，不考虑优化
 public boolean used(String str){
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
  Oggmap = new HashMap();
  HashMap lows= new HashMap();
  low = lows;
  ByteBuffer warp = ByteBuffer.allocateDirect(8192);
  Warp = warp;
  Buff = new StringBuilder();
  StringBuilder buff=new StringBuilder();
  File ou=In;
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
   Enumeration<? extends ZipEntry> ZipEntrys=zip.entries();
   while (ZipEntrys.hasMoreElements()) {
    ZipEntry ZipEntry=ZipEntrys.nextElement();
    name = ZipEntry.getName();
    name = loder.getRoot(name);
    if ("".equals(name)) {
     rootPath = "";
     break;
    } else if (rootPath == null) {
     rootPath = name;
    } else if (!rootPath.equals(name)) {
     rootPath = "";
     break;
    }
   }
   WritableByteChannel out = Channels.newChannel(zipout);
   Out = out;
   try {
    Enumeration<? extends ZipEntry> zipEntrys=zip.entries();
    while (zipEntrys.hasMoreElements()) {
     ZipEntry zipEntry=zipEntrys.nextElement();
     String fileName=zipEntry.getName();  
     String lowr=fileName.toLowerCase();
     if (!lows.containsValue(lowr))lows.put(lowr,zipEntry);
     if (zipEntry.getSize() != 0) {
      byte type=getType(fileName);
      if (type >= 0) {
       try {
        loder lod=new loder(zip.getInputStream(zipEntry));
        if (rwmodLib.dontlod(lod))type = 0;
        if (type > 0) {
         inimap.put(fileName, lod);
        } else {
         inihide.put(fileName, lod);
        }
       } catch (Exception e) {
        ui.fali(e);
       }
      }
     }
    }
    ZipEntry inf=super.toPath(rootPath.concat("mod-info.txt"));
    if (inf != null) {
     String path=inf.getName();
     if (!inihide.containsKey(path)) {
      loder ini=new loder(zip.getInputStream(inf));
      HashMap info=ini.ini;
      Object o=info.get("music");
      if (o != null) {
       HashMap map=(HashMap)o;
       o = map.get("sourceFolder");
       if (o != null) {
        String musicpath = (String)o;
        if (musicpath.length() != 0) {
         ZipEntry en=super.toPath(musicpath);
         if(en!=null){
          musicPath=en.getName();
          map.put("sourceFolder","");
         }
        }
       }
      }
      ini.put = ini.ini;
      ini.str = path;
      inihide.put(path, ini);
      write(ini, "mod-info.txt/");
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
      replaceAllRes(loder, filename, buff,true);
      write(loder, r);
     }
    }
    zipEntrys = zip.entries();
    while (zipEntrys.hasMoreElements()) {
     ZipEntry en=zipEntrys.nextElement();
     String file=en.getName();
     byte type= getType(file);
     if (type < 0) {
      if (type == -1) {
       name = loder.getName(file);
       int i=file.length();
       if (!file.endsWith("/")) {
        name = name.concat("/");
       } else --i;
       i -= 4;
       copy(name, en);
       buff.setLength(0);
       buff.append(file, 0, i);
       buff.append("_map.png");
       en=super.toPath(buff.toString());
       if (en!=null) {
        name=loder.getName(en.getName());
        copy(name.concat("/"), en);
       }
      } else{
       if (type==-3) {
       copy(FileName(type), en);
      }
      }
     }
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
