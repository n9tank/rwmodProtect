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
  if(str.length()==1){
   str="sdcard/rustedWarfare/rwmod";
  }
  init(new File(str,".ini"));
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
  rwmodLib.init(new File(def.getParent(),"lib.zip"));
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
   else if (ini==-3)buff.append(".ogg");
 // buff.append('/');
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
  str = toPath(str);
  Object o;
  tag: {
   if (!isini || (o = iniMap.get(str)) == null) {
    HashMap map=iniHide;
    o = map.get(str);
    if (o == null) {
     ZipFile zip=Zip;
     try {
      lod = new loder(zip.getInputStream(zip.getEntry(str)));
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
   String r=FileName(isini?1: 0);
   lod.str = r;
   StringBuilder buff=new StringBuilder();
   replaceCopy(lod, str,isini, buff);
   replaceAllRes(lod, str, buff);
   write(lod, r);
  }
  return lod;
 }
 public void replaceR(String str, String path, StringBuilder buff,int isimg) {
  String file;
  String list[]=null;
  tag: {
   if(isimg==2){
    list=str.split("\\*");
    str=list[0];
   }
   if (isimg>2) {
    if (music.contains(str))break tag;
    file = loder.getPath(str, path);
   } else file = loder.getImagePath(str, path, buff);
   if (file != null) {
    file = toPath(file);
    if(file==null){
     System.out.println(str);
    }
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
     copy(str,Zip.getEntry(file));
     map.put(file, str);
    } else str = (String)o;
   }
  }
  buff.append(str);
  if(list!=null&&list.length>1){
  buff.append("*");
  buff.append(list[1]);
  }
  buff.append(',');
 }
 public void replaceCopy(loder ini, String file, boolean isini, StringBuilder buff) {
  file = loder.getSuperPath(file);
  tag:
  if (isini) {
   loder lod=getSpuerAll(file, buff);
   if (lod != null) {
    buff.append(lod.str);
    buff.append(',');
    loder.put(ini.put, lod.put, true);
   }
  }
  boolean v=buff.length() > 0;
  HashMap map=ini.ini;
  Object o=map.get("core");
  if (o != null) {
   HashMap core=(HashMap)o;
   o = core.get("copyFrom");
   String str;
   if (o!=null&&(str=(String)o).length()>0&&!str.equals("IGNORE")) {
    String list[]=str.split(",");
    int i=0,n=list.length;
    rwmodLib libs=rwmodLib.lib;
    do {
     String path=list[i].trim();
     str = loder.getPath(path, file);
     if (str != null) {
      str = toPath(str);
      loder lod =replace(str, getType(str)>0);
      path = lod.str;
      loder.put(ini.put, lod.put, true);
     } else if (libs != null) {
      map = libs.iniMap;
      str = path.substring(5);
      loder lod=(loder)map.get(libs.toPath(str));
      loder.put(ini.put, lod.put, true);
     }
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
   Ui.fali(e);
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
     if(str==null||str.equalsIgnoreCase("none")||str.equals("IGNORE"))continue;
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
        replaceR(str,path, buff, i);
       }while(++l < size);
       break;
      case 4:
       path = loder.getSuperPath(filename);
       list2=str.split(",");
       l=0;
       size=list2.length;
       do {
        str = list2[l].trim();
        replaceR(str,path, buff,i);
       }while(++l < size);
      break;
      default:
      replaceR(str,loder.getSuperPath(filename),buff,i);
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
 public String toPath(String str){
  String file=super.toPath(str);
  if(file==null||Zip.getEntry(file)==null){
  return super.toPath(rootPath.concat(str));
  }
  return file;
 }
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
  low=lows;
  ByteBuffer warp = ByteBuffer.allocateDirect(8192);
  Warp = warp;
  Buff = new StringBuilder();
  StringBuilder buff=new StringBuilder();
  String musicpath=null;
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
   while(ZipEntrys.hasMoreElements()){
    ZipEntry ZipEntry=ZipEntrys.nextElement();
    name=ZipEntry.getName();
    name=loder.getRoot(name);
    if("".equals(name)){
     rootPath="";
     break;
    }else if(rootPath==null){
     rootPath=name;
    }else if(!rootPath.equals(name)){
     rootPath="";
     break;
    }
   }
   WritableByteChannel out = Channels.newChannel(zipout);
   Out = out;
   try {
    Enumeration<? extends ZipEntry> zipEntrys=zip.entries();
    while (zipEntrys.hasMoreElements()) {
     ZipEntry zipEntry=zipEntrys.nextElement();
     if (zipEntry.getSize() != 0) {
      String fileName=zipEntry.getName();  
      String lowr=fileName.toLowerCase();
      if(!lows.containsValue(lowr))lows.put(lowr, fileName);
      byte type=getType(fileName);
      if (type >= 0){
       try{
       loder lod=new loder(zip.getInputStream(zipEntry));
        if (rwmodLib.dontlod(lod))type=0;
        if (type > 0) {
         inimap.put(fileName, lod);
        } else {
         inihide.put(fileName, lod);
        }
      }catch(Exception e){
       ui.fali(e);
      }
      }
     }
    }
    String path=super.toPath(rootPath.concat("mod-info.txt"));
    if(path!=null){
     if(!inihide.containsKey(path)){
     ZipEntry inf=zip.getEntry(path);
     if (inf != null) {
      loder ini=new loder(zip.getInputStream(inf));
      HashMap info=ini.ini;
      Object o=info.get("music");
      if (o != null) {
       HashMap map=(HashMap)o;
       o = map.get("sourceFolder");
       if (o != null) {
        musicpath = (String)o;
        if (musicpath.length()!= 0) {
         if (!musicpath.endsWith("/")) {
          musicpath = musicpath.concat("/");
         }
        }
        musicPath = musicpath;
        map.put("sourceFolder","");
       }
      }
      String str=inf.getName();
      ini.put = ini.ini;
      ini.str = str;
      inihide.put(str, ini);
      write(ini,"mod-info.txt/");
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
      write(loder,r);
     }
    }
    zipEntrys = zip.entries();
    while (zipEntrys.hasMoreElements()) {
     ZipEntry en=zipEntrys.nextElement();
     String file=en.getName();
     byte type= getType(file);
     if (type < 0) {
      if (type == -1 && !inihide.containsKey(file)) {
       name=loder.getName(file);
       int i=file.length();
      if(!file.endsWith("/")){
       name=name.concat("/");
       --i;
       }
       i-=3;
       copy(name,en);
       buff.setLength(0);
       buff.append(file,0,i);
       buff.append("_map.png");
       name=super.toPath(buff.toString());
       if(name!=null){
        en=zip.getEntry(name);
        copy(loder.getName(name),en);
       }
      } else if (type == -3) {
       copy(FileName(type), en);
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
