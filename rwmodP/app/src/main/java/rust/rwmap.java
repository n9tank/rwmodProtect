package rust;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Base64;
import android.util.Log;
import carsh.log;
import com.googlecode.pngtastic.core.PngImage;
import com.googlecode.pngtastic.core.PngOptimizer;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class rwmap implements Runnable {
 ui ui;
 File in;
 File ou;
 public rwmap(File i, File u, ui uo) {
  in = i;
  ou = u;
  ui = uo;
 }
 public static Node getFirst(int i,Node map) {
  NodeList list=map.getChildNodes();
  for (int l=list.getLength();i < l;++i) {
   Node  item=list.item(i);
   if (item.getNodeType() == Node.ELEMENT_NODE)return item;
  }
  return null;
 }
 public static void cmpdata(Node data, ByteBuffer buff, ByteOut barr)throws Throwable {
  barr.reset() ; 
  DeflaterOutputStream zlb=new DeflaterOutputStream(barr, new Deflater(9));
  zlb.write(buff.array(), 0, buff.limit());
  zlb.close();
  data.getAttributes().getNamedItem("compression").setNodeValue("zlib");
  data.setTextContent(Base64.encodeToString(barr.get(), 0, barr.size(), Base64.DEFAULT));
 }
 public int Ipare(NamedNodeMap attr, String str) {
  return Integer.parseInt(attr.getNamedItem(str).getNodeValue());
 }
 //https://github.com/Timeree/RwMapCompressor
 public void run() {
  Throwable ex=null;
  try {
   DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
   Document document = docBuilder.parse(in);
   HashMap<Integer,Integer> tiles = new HashMap();
   Node  map= document.getFirstChild();
   Node  pr= getFirst(0,map);
   ByteOut out=new ByteOut();
   ByteOut barr=new ByteOut();
   ArrayList layer=new ArrayList();         
   if (pr.getNodeName().equals("properties"))map.removeChild(pr);
   NodeList nodeList =map.getChildNodes();
   byte brr[]=new byte[8192];
   PngOptimizer opt=new PngOptimizer();
   for (int i =nodeList.getLength();--i >= 0;) {
	Node item = nodeList.item(i);
	if (item.getNodeType() == Node.ELEMENT_NODE) {
	 if (item.getNodeName().equals("layer")) {
      String name;          
	  if ((name = item.getAttributes().getNamedItem("name").getNodeValue().toLowerCase()).equals("set")) {
	   map.removeChild(item);
	   continue;
	  }
	  Node data = getFirst(0,item);
	  String dataValue = data.getTextContent().trim();
	  InputStream in =new ByteArrayInputStream(Base64.decode(dataValue, Base64.DEFAULT));
	  if (data.getAttributes().getNamedItem("compression").getNodeValue().equals("gzip"))in = new GZIPInputStream(in);
	  else in = new InflaterInputStream(in);
      int len;
      out.reset();        
	  while ((len = in.read(brr)) > 0)out.write(brr, 0, len);
	  in.close();
	  ByteBuffer buffer;
      if (!name.equals("units")) {    
       buffer = ByteBuffer.wrap(out.toByteArray());               
       layer.add(buffer);
       layer.add(data);
      } else {
       buffer = ByteBuffer.wrap(out.get(), 0, out.size());         
       cmpdata(data, buffer, barr);
      }                           
	  buffer.order(ByteOrder.LITTLE_ENDIAN);
      Integer em=-1;
	  while (buffer.hasRemaining())tiles.putIfAbsent(buffer.getInt(), em);
	 }
    }
   }
   for (int i= nodeList.getLength(); --i >= 0;) {
	Node item = nodeList.item(i);
	if (item.getNodeType() == Node.ELEMENT_NODE) {
	 if (item.getNodeName().equals("tileset")) {
	  NamedNodeMap attr = item.getAttributes();
      Node tname=attr.getNamedItem("name");
     if(tname==null) continue; 
     String tlname=tname.getNodeValue().toLowerCase();      
      int firstgId = Ipare(attr, "firstgid");          
	  NodeList tilesetList = item.getChildNodes();
	  for (int i2= tilesetList.getLength();--i2 >= 0;) {
	   Node child = tilesetList.item(i2);
	   if (child.getNodeType() == Node.ELEMENT_NODE) {
		String childName = child.getNodeName();
		if (childName.equals("terraintypes")) {
         item.removeChild(child);
         continue;            
        }              
		if (childName.equals("properties")) {
         NodeList list=child.getChildNodes();    
         for (int i3=list.getLength();--i3 >= 0;) {                
          Node property = list.item(i3);
          if (property.getNodeType() == Node.ELEMENT_NODE) {  
           String name=property.getAttributes().getNamedItem("name").getNodeValue();         
           if (name.equals("layer") || name.equals("forced_autotile")) {
            child.removeChild(property);
            continue;                  
           }                                     
           if (name.equals("embedded_png")) {
          Node img= getFirst(i2+1,item);  
           if(img.getNodeName().equals("image"))item.removeChild(img);                  
            int tileWidth = Ipare(attr, "tilewidth");
            int tileHeight = Ipare(attr, "tileheight");
            Node columns=attr.getNamedItem("columns");          
            int tilew =Integer.parseInt(columns.getNodeValue());
            Node tilecount=attr.getNamedItem("tilecount");                 
            int tilec = Integer.parseInt(tilecount.getNodeValue());
           int size=0;
            for(int c=firstgId+tilec;--c>=firstgId;)
            if(tiles.containsKey(c))++size;
            String str=String.valueOf(size);          
            columns.setNodeValue("1");
            tilecount.setNodeValue(str);                   
            ByteArrayInputStream imgInput = new ByteArrayInputStream(Base64.decode(property.getTextContent().replaceAll("\\s", ""), Base64.DEFAULT));
           Bitmap.Config cf=tlname.equals("items")?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig =cf;
            Bitmap bmp=BitmapFactory.decodeStream(imgInput, null, options);
            Bitmap bm2= Bitmap.createBitmap(tileWidth, tileHeight * size, cf);                               
            Canvas cv= new Canvas(bm2);
            Paint pt= new Paint();
            pt.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));      
            int v=0,j=0;                   
            for(int c=tilec;--c>=0;){
           Integer key=c+firstgId;               
         if(tiles.containsKey(key)){      
          int left=c%tilew*tileWidth;   
           int top=c/tilew*tileHeight;
            int n=v+tileHeight;
            tiles.put(key,j++ +firstgId);                                                         
           cv.drawBitmap(bmp, new Rect(left, top, left + tileWidth, top + tileHeight), new Rect(0, v, tileWidth, n), pt);
             v=n;  
            }                           
            }
            out.reset();                      
            bm2.compress(Bitmap.CompressFormat.PNG, 100, out);
            bmp.recycle();
            bm2.recycle();                      
            PngImage png= opt.optimize(new PngImage(new ByteArrayInputStream(out.get(), 0, out.size())), false, 9); 
            out.reset();
            png.writeDataOutputStream(new DataOutputStream(out));                                                       
            property.setTextContent(Base64.encodeToString(out.get(), 0, out.size(), Base64.DEFAULT));
           }
          }                     
         }
		}
       }
      }
	 }
	}
   }
   int i=layer.size();
   while (--i > 0) {
 	Node data=(Node)layer.get(i);
   ByteBuffer warp= (ByteBuffer)layer.get(--i);   
    int j=warp.limit();
    while ((j -= 4) >= 0) {
     Integer  to= tiles.get(warp.getInt(j));    
     if (to != null) {
      int u = to;
      if (u >=0)warp.putInt(j,u);
     }  
    }
    cmpdata(data, warp, barr);
   }
   for (i = nodeList.getLength(); --i >= 0;) {
	Node item = nodeList.item(i);
	if (item.getNodeType() == Node.ELEMENT_NODE) {
	 if (item.getNodeName().equals("tileset")) {
    NamedNodeMap attr=item.getAttributes();
    Node tname=attr.getNamedItem("name");
     if(tname==null) continue;
	  int firstgId = Ipare(attr, "firstgid");
	  NodeList tilesetList = item.getChildNodes();
     boolean skip=true;         
	  for (int i2=tilesetList.getLength();--i2 >= 0;) {
	   Node child = tilesetList.item(i2);
	   if (child.getNodeType() == Node.ELEMENT_NODE) {
		String childName = child.getNodeName();
        if (childName.equals("tile")) {
           skip=false;     
		 NamedNodeMap childAttr = child.getAttributes();
         Node idn=  childAttr.getNamedItem("id"); 
           int id = Integer.parseInt(idn.getNodeValue());
         Integer key= tiles.get(firstgId + id);
        if (key == null) item.removeChild(child);
        else {
          id = key;             
          if (id >= 0)idn.setNodeValue(String.valueOf(id-firstgId));
         }               
      	}
       }
      }
    if(skip&&tilesetList.getLength()<=1)map.removeChild(item);
     }
    }
   }
   BufferedWriter buff=new BufferedWriter(new FileWriter(ou));
   try {
	outxml(document, buff);
   } finally {
	buff.close();
   }
  } catch (Throwable e) {
   log.e(this, e);
   ex = e;
  }
  if (ex != null)ou.delete();
  ui.end(ex);
 }
 public static void outxml(Node map, BufferedWriter out) throws Exception {
  NodeList list=map.getChildNodes();
  for (int i=0,l=list.getLength();i < l;++i) {
   Node item=list.item(i);
   if (item.getNodeType() == Node.TEXT_NODE) {
	out.write(item.getNodeValue().replaceAll("\\s", ""));
	continue;
   }
   out.write('<');
   out.write(item.getNodeName());
   NamedNodeMap maps=item.getAttributes();
   if (maps != null) {
	int j=maps.getLength();
	if (j > 0)out.write(' ');
	for (;--j >= 0;) {
	 Node kv=maps.item(j);
	 out.write(kv.getNodeName());
	 out.write("=\"");
	 out.write(kv.getNodeValue());
	 out.write('\"');
	}
   }
   NodeList ch=item.getChildNodes();
   if (ch.getLength() == 0)out.write('/');
   out.write('>');
   if (ch.getLength() > 0) {
	outxml(item, out);
	out.write("</");
	out.write(item.getNodeName());
	out.write('>');
   }
  }
 }
}
