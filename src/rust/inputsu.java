package rust;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.parallel.InputStreamSupplier;

class inputsu implements InputStreamSupplier {
 loder ini;
 ZipFile zip;
 ZipArchiveEntry en;
 inputsu(loder lod) {
  ini = lod;
 }
 inputsu(ZipFile in, ZipArchiveEntry e) {
  zip = in;
  en = e;
 }
 public InputStream get() {
  InputStream inr=null;
  ZipArchiveEntry ens=en;
  if (ens == null) {
   try {
    ByteArrayOutputStream rus = new ByteArrayOutputStream();
    BufferedWriter out=new BufferedWriter(new OutputStreamWriter(rus));
    loder lod=ini;
    if (lod.ini == null)lod.call();
    try {
     HashMap map=lod.ini;
     Iterator<Map.Entry<String,HashMap>> ite=map.entrySet().iterator();
     boolean st=false;
     while (ite.hasNext()) {
      Map.Entry en = ite.next();
      map = (HashMap)en.getValue();
      if (map.size() > 0) {
       if (!st)out.write('\n');
       st = true;
       String key=(String)en.getKey();
       out.write('[');
       out.write(key);
       out.write("]\n");
       Iterator<Map.Entry> ite2=map.entrySet().iterator();
       boolean nx=false;
       while (ite2.hasNext()) {
        en = ite2.next();
        out.write((String)en.getKey());
        out.write(':');
        out.write((String)en.getValue());
        nx = ite2.hasNext();
        if (nx)out.write('\n');
       }
      }
     }
     out.flush();
     return new ByteArrayInputStream(rus.toByteArray());
    } finally {
     out.close();
    }
   } catch (Throwable e) {
   }
  } else {
   try {
    inr = zip.getInputStream(en);
   } catch (Throwable e) {
   }
  }
  return inr;
 }
}
