var jsPDF=function(){var K="20090504";
var q="";
var x="1.3";
var g="a4";
var v={a3:[841.89,1190.55],a4:[595.28,841.89],a5:[420.94,595.28],letter:[612,792],legal:[612,1008]};
var F="0 g";
var b=0;
var e=2;
var N=0;
var a=new Array();
var r=new Array();
var m=0.200025;
var o;
var J;
var G="mm";
var H;
var w={};
var j=16;
var M=16;
if(G=="pt"){J=1
}else{if(G=="mm"){J=72/25.4
}else{if(G=="cm"){J=72/2.54
}else{if(G=="in"){J=72
}}}}var h=function(){e++;
r[e]=q.length;
D(e+" 0 obj")
};
var E=function(){D("%PDF-"+x)
};
var C=function(){var P=pageWidth*J;
var k=o*J;
for(n=1;
n<=b;
n++){h();
D("<</Type /Page");
D("/Parent 1 0 R");
D("/Resources 2 0 R");
D("/Contents "+(e+1)+" 0 R>>");
D("endobj");
p=a[n];
h();
D("<</Length "+p.length+">>");
t(p);
D("endobj")
}r[1]=q.length;
D("1 0 obj");
D("<</Type /Pages");
var O="/Kids [";
for(i=0;
i<b;
i++){O+=(3+2*i)+" 0 R "
}D(O+"]");
D("/Count "+b);
D(sprintf("/MediaBox [0 0 %.2f %.2f]",P,k));
D(">>");
D("endobj")
};
var t=function(k){D("stream");
D(k);
D("endstream")
};
var c=function(){s();
A();
r[2]=q.length;
D("2 0 obj");
D("<<");
l();
D(">>");
D("endobj")
};
var s=function(){h();
H=e;
name="Helvetica";
D("<</Type /Font");
D("/BaseFont /"+name);
D("/Subtype /Type1");
D("/Encoding /WinAnsiEncoding");
D(">>");
D("endobj")
};
var A=function(){};
var l=function(){D("/ProcSet [/PDF /Text /ImageB /ImageC /ImageI]");
D("/Font <<");
D("/F1 "+H+" 0 R");
D(">>");
D("/XObject <<");
u();
D(">>")
};
var u=function(){};
var z=function(){D("/Producer (jsPDF "+K+")");
if(w.title!=undefined){D("/Title ("+B(w.title)+")")
}if(w.subject!=undefined){D("/Subject ("+B(w.subject)+")")
}if(w.author!=undefined){D("/Author ("+B(w.author)+")")
}if(w.keywords!=undefined){D("/Keywords ("+B(w.keywords)+")")
}if(w.creator!=undefined){D("/Creator ("+B(w.creator)+")")
}var T=new Date();
var Q=T.getFullYear();
var R=(T.getMonth()+1);
var O=T.getDate();
var k=T.getHours();
var S=T.getMinutes();
var P=T.getSeconds();
D("/CreationDate (D:"+sprintf("%02d%02d%02d%02d%02d%02d",Q,R,O,k,S,P)+")")
};
var y=function(){D("/Type /Catalog");
D("/Pages 1 0 R");
D("/OpenAction [3 0 R /FitH null]");
D("/PageLayout /OneColumn")
};
function d(){D("/Size "+(e+1));
D("/Root "+e+" 0 R");
D("/Info "+(e-1)+" 0 R")
}var f=function(){N=1;
E();
C();
c();
h();
D("<<");
z();
D(">>");
D("endobj");
h();
D("<<");
y();
D(">>");
D("endobj");
var O=q.length;
D("xref");
D("0 "+(e+1));
D("0000000000 65535 f ");
for(var k=1;
k<=e;
k++){D(sprintf("%010d 00000 n ",r[k]))
}D("trailer");
D("<<");
d();
D(">>");
D("startxref");
D(O);
D("%%EOF");
N=3
};
var L=function(){b++;
N=2;
a[b]="";
o=v.a4[1]/J;
pageWidth=v.a4[0]/J
};
var D=function(k){if(N==2){a[b]+=k+"\n"
}else{q+=k+"\n"
}};
var I=function(){L();
D(sprintf("%.2f w",(m*J)));
M=j;
D("BT /F1 "+parseInt(j)+".00 Tf ET")
};
I();
var B=function(k){return k.replace(/\\/g,"\\\\").replace(/\(/g,"\\(").replace(/\)/g,"\\)")
};
return{addPage:function(){I()
},text:function(k,Q,P){if(M!=j){D("BT /F1 "+parseInt(j)+".00 Tf ET");
M=j
}var O=sprintf("BT %.2f %.2f Td (%s) Tj ET",k*J,(o-Q)*J,B(P));
D(O)
},setProperties:function(k){w=k
},addImage:function(S,Q,k,R,O,P){},output:function(O,k){f();
if(O==undefined){return q
}if(O=="datauri"){document.location.href="data:application/pdf;base64,"+Base64.encode(q)
}},setFontSize:function(k){j=k
}}
};