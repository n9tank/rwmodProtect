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
     HashMap gloab=(HashMap)map.remove("");
     Iterator<Map.Entry<String,HashMap>> ite=map.entrySet().iterator();
     boolean wt=false;
     if (ite.hasNext()) {
      Map.Entry<String,HashMap> en = ite.next();
      map = en.getValue();
      boolean size=map.size() > 0;
      while (true) {
       if (size) {
        String key=en.getKey();
        wt = true;
        out.write('[');
        out.write(key);
        out.write("]\n");
        loder.writeKeys(map, out);
       }
       if (!ite.hasNext())break;
       en = ite.next();
       map = en.getValue();
       size = map.size() > 0;
       if (size)out.write('\n');
      }
     }
     if (gloab.size() > 0) {
      if (!wt)out.write("[]");
      out.write('\n');
      loder.writeKeys(gloab, out);
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
