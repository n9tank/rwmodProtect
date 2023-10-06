package rust;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import rust.res;
import java.io.InputStreamReader;
import java.io.InputStream;

public class loder extends loderLib {
 public HashMap as;
 public HashMap cou;
 public ArrayList needs;
 public BitSet bit;
 public byte end;
 public loder(InputStream in) throws Exception {
  super(in);
  put=new HashMap();
  cou = new HashMap();
  bit = new BitSet(0);
 }
 public void putoff(HashMap map, HashMap map2, boolean skip) {
  byte ed=end;
  HashMap cous=cou;
  Iterator ite=map2.entrySet().iterator();
  HashMap<String, HashMap> res=rwmodProtect.Res;
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String key=(String)en.getKey();
   Object o=map.get(key);
   Object hash=en.getValue();
   HashMap map3;
   if (o == null) {
    map3 = (HashMap)hash;
    map.put(key, map3.clone());
   } else {
    HashMap set=(HashMap)o;
    String k;
    if (skip || (o = set.get("@copyFrom_skipThisSection")) == null || (!(k = (String)o).equals("1") && !k.equalsIgnoreCase("true"))) {
     map3 = (HashMap)hash;
     set.putAll(map3);
    } else map3 = null;
   }
   if (map3 != null) {
    o = wh(key, res, rwmodProtect.max);
    if (o != null) {
     HashMap find=(HashMap)o;
     HashMap list=(HashMap)cous.get(key);
     if (list == null) {
      list = new HashMap();
      cous.put(key, list);
     }
     Iterator ite2=map3.keySet().iterator();
     while (ite2.hasNext()) {
      key = (String) ite2.next();
      if (find.get(key) != null) {
       list.put(key, ed);
      }
     }
    }
   }
  }
  end = ++ed;
 }
 public void put() {
  HashMap hash=as;
  HashMap put=ini;
  ArrayList need=needs;
  int l=need.size();
  while (--l >= 0) {
   Map.Entry en=(Map.Entry)need.get(l);
   String str=(String)en.getKey();
   HashMap map=(HashMap)hash.get(str);
   if (map == null) {
    System.out.println(str);
   }
   Object o=put.get(str);
   HashMap putall;
   if (o == null) {
    putall = new HashMap();
    put.put(str, putall);
   } else putall = (HashMap)o;
   ArrayList list=(ArrayList)en.getValue();
   int i=list.size();
   while (--i >= 0) {
    str = (String)list.get(i);
    putall.put(str, map.get(str));
   }
  }
  as = null;
  need = null;
  bit = null;
  cou = null;
 }
 public void eqz() {
  HashMap pu=put;
  HashMap re=ini;
  HashMap coe=new HashMap();
  put(coe, pu, true);
  as(coe);
  HashMap hash=new HashMap();
  as = hash;
  put(hash, pu, true);
  int ed=end;
  putoff(hash, re, false);
  as(hash);
  HashMap cous=cou;
  ArrayList<Map.Entry> need=new ArrayList();
  needs = need;
  HashMap def=rwmodProtect.Res;
  Iterator ite = hash.entrySet().iterator();
  BitSet bset=bit;
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String ac=(String)en.getKey();
   Object o=wh(ac, def, rwmodProtect.max);
   if (o != null) {
    ArrayList arr=new ArrayList();
    HashMap map=(HashMap)cous.get(ac);
    HashMap list=(HashMap)en.getValue();
    HashMap list2=(HashMap)coe.get(ac);
    HashMap tr=(HashMap)o;
    Iterator ite2=list.entrySet().iterator();
    while (ite2.hasNext()) {
     en = (Map.Entry) ite2.next();
     String key=(String)en.getKey();
     if (tr.get(key) == null)continue;
     o = map.get(key);
     String str;
     byte in;
     if (o == null || (in = o) == ed || bset.get(in) || list2 == null || (str = (String)list2.get(key)) == null || !loderLib.get((String)en.getValue(), hash, list).equals(loderLib.get(str, coe, list2))) {
      arr.add(key);
     }
    }
    if (arr.size() > 0) {
     need.add(Map.entry(ac, arr));
    }
   }
  }
 }
 public static void as(HashMap map) {
  Iterator ite=map.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String key=(String)en.getKey();
   HashMap hash=(HashMap)en.getValue();
   String vl;
   HashMap mapput=new HashMap();
   if ((vl = wh(key, vlset, vlmax)) != null) {
    Object o=hash.get("copyFrom");
    if (o != null) {
     key = (String)o;
     HashMap set=(HashMap)map.get(vl.concat(key));
     if (set != null)mapput.putAll(set);
    }
   }
   Object o=hash.get("@copyFromSection");
   if (o != null) {
    key = (String)o;
    HashMap set=(HashMap)map.get(key);
    if (set != null)mapput.putAll(set);
   }
   if (mapput.size() > 0) {
    mapput.putAll(hash);
    en.setValue(mapput);
   }
  }
 }
}
