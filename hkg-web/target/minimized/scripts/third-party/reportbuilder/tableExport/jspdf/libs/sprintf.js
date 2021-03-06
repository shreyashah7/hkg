function sprintf(){var g=/%%|%(\d+\$)?([-+\'#0 ]*)(\*\d+\$|\*|\d+)?(\.(\*\d+\$|\*|\d+))?([scboxXuidfegEG])/g;
var h=arguments,f=0,k=h[f++];
var b=function(n,a,i,m){if(!i){i=" "
}var l=(n.length>=a)?"":Array(1+a-n.length>>>0).join(i);
return m?n+l:l+n
};
var c=function(m,l,p,a,i,o){var n=a-m.length;
if(n>0){if(p||!i){m=b(m,a,o,p)
}else{m=m.slice(0,l.length)+b("",n,"0",true)+m.slice(l.length)
}}return m
};
var j=function(p,o,n,q,i,a,m){var l=p>>>0;
n=n&&l&&{"2":"0b","8":"0","16":"0x"}[o]||"";
p=n+b(l.toString(o),a||0,"0",false);
return c(p,n,q,i,m)
};
var e=function(m,o,i,a,l,n){if(a!=null){m=m.slice(0,a)
}return c(m,"",o,i,l,n)
};
var d=function(z,m,n,r,B,w,l){var a;
var v;
var i;
var A;
var t;
if(z=="%%"){return"%"
}var s=false,o="",q=false,y=false,x=" ";
var p=n.length;
for(var u=0;
n&&u<p;
u++){switch(n.charAt(u)){case" ":o=" ";
break;
case"+":o="+";
break;
case"-":s=true;
break;
case"'":x=n.charAt(u+1);
break;
case"0":q=true;
break;
case"#":y=true;
break
}}if(!r){r=0
}else{if(r=="*"){r=+h[f++]
}else{if(r.charAt(0)=="*"){r=+h[r.slice(1,-1)]
}else{r=+r
}}}if(r<0){r=-r;
s=true
}if(!isFinite(r)){throw new Error("sprintf: (minimum-)width must be finite")
}if(!w){w="fFeE".indexOf(l)>-1?6:(l=="d")?0:void (0)
}else{if(w=="*"){w=+h[f++]
}else{if(w.charAt(0)=="*"){w=+h[w.slice(1,-1)]
}else{w=+w
}}}t=m?h[m.slice(0,-1)]:h[f++];
switch(l){case"s":return e(String(t),s,r,w,q,x);
case"c":return e(String.fromCharCode(+t),s,r,w,q);
case"b":return j(t,2,y,s,r,w,q);
case"o":return j(t,8,y,s,r,w,q);
case"x":return j(t,16,y,s,r,w,q);
case"X":return j(t,16,y,s,r,w,q).toUpperCase();
case"u":return j(t,10,y,s,r,w,q);
case"i":case"d":a=parseInt(+t);
v=a<0?"-":o;
t=v+b(String(Math.abs(a)),w,"0",false);
return c(t,v,s,r,q);
case"e":case"E":case"f":case"F":case"g":case"G":a=+t;
v=a<0?"-":o;
i=["toExponential","toFixed","toPrecision"]["efg".indexOf(l.toLowerCase())];
A=["toString","toUpperCase"]["eEfFgG".indexOf(l)%2];
t=v+Math.abs(a)[i](w);
return c(t,v,s,r,q)[A]();
default:return z
}};
return k.replace(g,d)
};