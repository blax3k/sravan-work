
public class Vec3 {
/*
 * var vec3={};
  vec3.create=function(a)
  {
  var b=new glMatrixArrayType(3);
  if(a){
  b[0]=a[0];
  b[1]=a[1];
  b[2]=a[2]
  }
  return b
  };
  
  vec3.set=function(a,b){
  b[0]=a[0];
  b[1]=a[1];
  b[2]=a[2];
  return b
  };
  
vec3.add=function(a,b,c){
if(!c||a==c){
a[0]+=b[0];
a[1]+=b[1];
a[2]+=b[2];
return a
}
c[0]=a[0]+b[0];
c[1]=a[1]+b[1];
c[2]=a[2]+b[2];
return c
};
vec3.subtract=function(a,b,c)
{
if(!c||a==c){
a[0]-=b[0];
a[1]-=b[1];
a[2]-=b[2];
return a
}
c[0]=a[0]-b[0];
c[1]=a[1]-b[1];
c[2]=a[2]-b[2];
return c
};

vec3.negate=function(a,b){b||(b=a);b[0]=-a[0];b[1]=-a[1];b[2]=-a[2];return b};

vec3.scale=function(a,b,c){
if(!c||a==c){
a[0]*=b;
a[1]*=b;
a[2]*=b;
return a
}
c[0]=a[0]*b;
c[1]=a[1]*b;
c[2]=a[2]*b;
return c
};

vec3.normalize=function(a,b)
{
b||(b=a);
var c=a[0],d=a[1],e=a[2],g=Math.sqrt(c*c+d*d+e*e);
if(g){
if(g==1){
b[0]=c;
b[1]=d;
b[2]=e;
return b
}
}
else{
b[0]=0;
b[1]=0;
b[2]=0;return b
}
g=1/g;
b[0]=c*g;
b[1]=d*g;
b[2]=e*g;
return b
};

vec3.cross=function(a,b,c)
{
c||(c=a);
var d=a[0],e=a[1];a=a[2];
var g=b[0],f=b[1];b=b[2];
c[0]=e*b-a*f;
c[1]=a*g-d*b;
c[2]=d*f-e*g;
return c
};

vec3.length=function(a){var b=a[0],c=a[1];a=a[2];return Math.sqrt(b*b+c*c+a*a)};

vec3.dot=function(a,b){return a[0]*b[0]+a[1]*b[1]+a[2]*b[2]};

vec3.direction=function(a,b,c){c||(c=a);var d=a[0]-b[0],e=a[1]-b[1];a=a[2]-b[2];b=Math.sqrt(d*d+e*e+a*a);if(!b){c[0]=0;c[1]=0;c[2]=0;return c}b=1/b;c[0]=d*b;c[1]=e*b;c[2]=a*b;return c};

vec3.lerp=function(a,b,c,d){d||(d=a);d[0]=a[0]+c*(b[0]-a[0]);d[1]=a[1]+c*(b[1]-a[1]);d[2]=a[2]+c*(b[2]-a[2]);return d};

vec3.str=function(a){return"["+a[0]+", "+a[1]+", "+a[2]+"]"};
 * */
}
