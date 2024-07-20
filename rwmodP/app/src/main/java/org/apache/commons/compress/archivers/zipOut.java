package org.apache.commons.compress.archivers.zip;
import carsh.log;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import rust.zippack;

public class zipOut extends ZipArchiveOutputStream {
  public static byte[] head;
  public static int[] ran;
  public void makeRndHead(int min,int ranoff,Random ran) throws Exception{
    byte[] brr=new byte[min+ran.nextInt(ranoff)];
    ran.nextBytes(brr);
    writeCounted(head);
    StreamCompressor io=streamCompressor;
    io.write(brr,0, brr.length, ZipArchiveEntry.DEFLATED);
    io.flushDeflater();
    io.reset();
  }
  public zipOut(File in) throws Exception {
  	super(in);
    byte arr[]=head;
    if(head!=null){
    int len=arr.length; 
    if(len>4){
      int[] ranList=ran;
      Random random=new Random();
      len=ranList.length;
      int i=0;
      while(i<len)
      makeRndHead(ranList[i++],ranList[i++],random);
      headOff=(int)streamCompressor.getTotalBytesWritten();
    }else{
    writeCounted(arr);
    headOff=arr.length; 
    }}
  }
  public void addRawArchiveEntry(ZipArchiveEntry ae,InputStream io) throws Exception{
    ae.setCrc(0); 
   //修改Size严重影响zipFile解压性能建议保留 
    if(!zippack.keepUnSize)ae.setCompressedSize(-1);
    super.addRawArchiveEntry(ae,io);  
  }
}
