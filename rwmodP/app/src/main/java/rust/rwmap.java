package rust;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import carsh.log;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
 //https://github.com/Timeree/RwMapCompressor
 public void run() {
  Throwable ex=null;
  try {
   DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
   Document document = docBuilder.parse(in);
   HashSet tileIds = new HashSet();
   NodeList nodeList = document.getFirstChild().getChildNodes();
   byte brr[]=new byte[8192];
   for (int i = 0; i < nodeList.getLength(); i++) {
	Node item = nodeList.item(i);
	if (item.getNodeType() == Node.ELEMENT_NODE) {
	 if (item.getNodeName().equals("layer")) {
	  if (item.getAttributes().getNamedItem("name").getNodeValue().toLowerCase().equals("set")) {
	   item.getParentNode().removeChild(item);
	   continue;
	  }
	  Node data = findChildNode(item, "data");
	  String dataValue = data.getTextContent().trim();
	  InputStream in =new ByteArrayInputStream(Base64.decode(dataValue, Base64.DEFAULT));
	  Node acm=data.getAttributes().getNamedItem("compression");
	  if (acm.getNodeValue().equals("gzip"))in = new GZIPInputStream(in);
	  else in = new InflaterInputStream(in);
	  ByteOut outputStream = new ByteOut();
	  int len;
	  while ((len = in.read(brr)) > 0)outputStream.write(brr, 0, len);
	  in.close();
	  ByteOut barr=new ByteOut();
	  DeflaterOutputStream zlb=new DeflaterOutputStream(barr, new Deflater(9));
	  outputStream.writeTo(zlb);
	  zlb.close();
	  acm.setNodeValue("zlib");
	  data.setTextContent(Base64.encodeToString(barr.get(), 0, barr.size(), Base64.DEFAULT));
	  ByteBuffer buffer = ByteBuffer.wrap(outputStream.get(), 0, outputStream.size());
	  buffer.order(ByteOrder.LITTLE_ENDIAN);
	  while (buffer.hasRemaining())tileIds.add(buffer.getInt());
	 }
	}
   }
   for (int i= nodeList.getLength(); --i >= 0;) {
	Node item = nodeList.item(i);
	if (item.getNodeType() == Node.ELEMENT_NODE) {
	 if (item.getNodeName().equals("tileset")) {
	  NamedNodeMap attr = item.getAttributes();
	  NodeList tilesetList = item.getChildNodes();
	  for (int i2= tilesetList.getLength();--i2 >= 0;) {
	   Node child = tilesetList.item(i2);
	   if (child.getNodeType() == Node.ELEMENT_NODE) {
		String childName = child.getNodeName();
		int firstgId = Integer.valueOf(attr.getNamedItem("firstgid").getNodeValue());
		if (childName.equals("properties")) {
		 Node property = findChildNode(child, "property");
		 if (property.getAttributes().getNamedItem("name").getNodeValue().equals("embedded_png")) {
		  int tileWidth = Integer.valueOf(attr.getNamedItem("tilewidth").getNodeValue());
		  int tileHeight = Integer.valueOf(attr.getNamedItem("tileheight").getNodeValue());
		  ByteArrayInputStream imgInput = new ByteArrayInputStream(Base64.decode(property.getTextContent().replaceAll("\\s", ""), Base64.DEFAULT));
		  ByteOut imgOutput = new ByteOut();
		  BitmapFactory.Options options = new BitmapFactory.Options();
		  options.inMutable = true; // 设置为可变
		  Bitmap bmp=BitmapFactory.decodeStream(imgInput, null, options);
		  int imgWidth = bmp.getWidth();
		  int imgHeight = bmp.getHeight();
		  int tileColumnNum = (int) Math.floor(imgWidth / (float) tileWidth);
		  for (int x = 0; x < imgWidth; x++) {
		   for (int y = 0; y < imgHeight; y++) {
			int tileX = (int) Math.floor(x / (float) tileWidth);
			int tileY = (int) Math.floor(y / (float) tileHeight);
			if (! tileIds.contains(firstgId + tileX + (tileY * tileColumnNum))) {
			 bmp.setPixel(x, y, 0x00000000);
			}
		   }
		  }
		  bmp.compress(Bitmap.CompressFormat.PNG, 80, imgOutput);
		  bmp.recycle();
		  property.setTextContent(Base64.encodeToString(imgOutput.get(), 0, imgOutput.size(), Base64.DEFAULT));
		 }
		} else if (childName.equals("tile")) {
		 NamedNodeMap childAttr = child.getAttributes();
		 int id = Integer.valueOf(childAttr.getNamedItem("id").getNodeValue());
		 if (! tileIds.contains(firstgId + id))item.removeChild(child);
		}
	   }
	  }
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
 private static Node findChildNode(Node from, String name) {
  NodeList nodeList = from.getChildNodes();
  for (int i = 0; i < nodeList.getLength(); i++) {
   Node item = nodeList.item(i);
   if (item.getNodeType() == Node.ELEMENT_NODE)
	return item;
  }
  return null;
 }
}
