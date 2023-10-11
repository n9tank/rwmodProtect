
package rust;

final class cmp {
 int a = -1;
 int b;
 final String c;
 private double d() {
  String fi=c;
  while (a(43)) {}
  if (a(45)) {
   return -d();
  }
  double b;
  int i = this.a;
  if (a(40)) {
   b = b();
   a(41);
  } else if ((this.b >= 48 && this.b <= 57) || this.b == 46) {
   while (true) {
    if ((this.b < 48 || this.b > 57) && this.b != 46) {
     break;
    }
    a();
   }
   b = Double.parseDouble(fi.substring(i, this.a));
  } else {
   while (this.b >= 97 && this.b <= 122) {
    a();
   }
   double d = d();
   char c=fi.charAt(i + 2);
   switch (c) {
    case 'q':
     b = Math.sqrt(d);
     break;
    case 'i':
     b = Math.sin(Math.toRadians(d));
     break;
    case 'o':
     b = Math.cos(Math.toRadians(d));
     break;
    default:
     b = (int)d;
     break;
   }
  }
  return a(94) ? Math.pow(b, d()) : b;
 }

 private boolean a(int i) {
  while (this.b == 32) {
   a();
  }
  if (this.b != i) {
   return false;
  }
  a();
  return true;
 }

 private double c() {
  double d = d();
  while (true) {
   if (a(42)) {
    d *= d();
   } else if (a(47)) {
    d /= d();
   } else if (!a(37)) {
    return d;
   } else {
    d %= d();
   }
  }
 }

 final double b() {
  double c = c();
  while (true) {
   if (a(43)) {
    c += c();
   } else if (!a(45)) {
    return c;
   } else {
    c -= c();
   }
  }
 }

 final void a() {
  int i = this.a + 1;
  this.a = i;
  this.b = i < this.c.length() ? this.c.charAt(this.a) : -1;
 }

 cmp(String str) {
  this.c = str;
 }
}
