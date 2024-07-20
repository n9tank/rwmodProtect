package rust;

import android.util.Log;
import carsh.log;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.zipOut;
import rust.zippack;

public class zipunpack implements Runnable {
public static class name{
  String name;
  boolean conts;  
   public name(String str,boolean con){
     name=str;
     conts=con;
   } 
}  
 File in;
 File ou;
 ui ui;
 boolean raw;
 static int boomlen; 
 static Field unwarp;
 static{
  try{
  Field fid= FilterInputStream.class.getDeclaredField("in");
  fid.setAccessible(true);
  unwarp=fid;   
  }catch(Throwable e){}  
 }
 public zipunpack(File i, File o,boolean fast, ui u) {
  in = i;
  ou = o;
  ui = u;
  raw=fast;  
 }
 public static int toName(String name){
   int i=name.length();
   while(name.charAt(--i)=='/');
    return ++i;
 } 
 public static name getName(String name,HashSet set,StringBuilder buf,Random ran) {
    boolean conts=false;
    int i=toName(name);
    if(name.length()>i)name=name.substring(0,i);
 	  if (!set.add(name)) {  
      buf.setLength(0);      
      buf.append(name);
      if(i>0){
      while(--i>0)buf.append('-');	
      }else buf.append('_');
      while(!set.add(name = buf.toString())){
       conts=true; 
       char c=(char)(ran.nextInt(93) + 33);
       if(c=='-')++c;
       buf.append(c);
      }
     }
    return new name(name,conts);
 }
 public static InputStream getRaw(InputStream io,ZipEntry en,int src)throws Exception{    
  return en.getMethod()==0||src==0?io:(InputStream)unwarp.get(io);
 } 
 public void run() {
  Throwable ex=null;
  Charset encode=StandardCharsets.ISO_8859_1;  
  try(
  ZipFile zip= new ZipFile(in,encode);
  ZipArchiveOutputStream zipout=new ZipArchiveOutputStream(ou)){
  zipout.setEncoding(encode);
   HashSet set=new HashSet();
   StringBuilder buf=new StringBuilder();
   Enumeration all=zip.entries();   
   Random ran=new Random();
   long time=System.currentTimeMillis();   
   while (all.hasMoreElements()) {
    ZipEntry en=(ZipEntry)all.nextElement();
    String name=en.getName();
    if(name.endsWith("\\"))continue;
    try(InputStream input= zip.getInputStream(en)){
    int mode=raw?en.getMethod():0;
    InputStream io=getRaw(input,en,mode);
    int size=io.read(zipout.copyBuffer);
    if(size<=0||(en.getMethod()==ZipEntry.DEFLATED&&size<=2))continue;
    name obj=getName(name,set,buf,ran);
    ZipArchiveEntry put=lib.getArc(obj.name);
    put.setTime(obj.conts?time:en.getTime()); 
    put.setMethod(mode);
    if(raw){
    put.setSize(en.getSize());
    put.setCrc(en.getCrc());
    put.setCompressedSize(en.getCompressedSize());
    }
    try{
    zipout.addRawArchiveEntry(put,io,size,raw?Integer.MAX_VALUE:boomlen);
    }catch(Exception e){
     log.e(this,ex=e);  
     break; 
    }        
    }catch(Throwable e){
    }
   }
  } catch (Throwable e) {
   log.e(this, ex = e);
  }
  if (ex != null)ou.delete();
  ui.end(ex);
 }
}
