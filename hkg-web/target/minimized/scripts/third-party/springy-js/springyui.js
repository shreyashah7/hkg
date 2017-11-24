(function(){function d(r,o,n,p,j,i){var m=0.5522848,g=(p/2)*m,e=(j/2)*m,q=o+p,l=n+j,k=o+p/2,f=n+j/2;
r.beginPath();
r.moveTo(o,f);
r.bezierCurveTo(o,f-e,k-g,n,k,n);
r.bezierCurveTo(k+g,n,q,f-e,q,f);
r.bezierCurveTo(q,f+e,k+g,l,k,l);
r.bezierCurveTo(k-g,l,o,f+e,o,f);
r.strokeStyle=i==undefined?"#D7D7D7":i;
r.stroke()
}function b(o,g,m,f,l,j){if(j==undefined){j=2
}o.moveTo(g,m);
var i=f-g;
var h=l-m;
var k=Math.floor(Math.sqrt(i*i+h*h)/j);
var p=i/k;
var n=h/k;
var e=0;
while(e++<k){g+=p;
m+=n;
o[e%2==0?"moveTo":"lineTo"](g,m)
}o[e%2==0?"moveTo":"lineTo"](f,l)
}function c(l,j,p,f){var h=p/f,k=Math.PI*2/h,e={},q=[],m=-1;
while(m<h){var g=k*m,o=k*(m+1);
q.push({x:(Math.cos(g)*p)+l,y:(Math.sin(g)*p)+j,ex:(Math.cos(o)*p)+l,ey:(Math.sin(o)*p)+j});
m+=2
}return q
}function a(g,f,k,e,i){var h=c(f,k,e,i);
for(var j=0;
j<h.length;
j++){g.moveTo(h[j].x,h[j].y);
g.lineTo(h[j].ex,h[j].ey);
g.stroke()
}}jQuery.fn.springy=function(J){var g=this.graph=J.graph||new Springy.Graph();
var u="16px Verdana, sans-serif";
var C="8px Verdana, sans-serif";
var G=J.stiffness||400;
var n=J.repulsion||400;
var k=J.damping||0.5;
var A=J.minEnergyThreshold||0.00001;
var F=J.nodeSelected||null;
var m={};
var r=true;
var l=this[0];
var z=l.getContext("2d");
var H=this.layout=new Springy.Layout.ForceDirected(g,G,n,k,A);
var h=H.getBoundingBox();
var t={bottomleft:new Springy.Vector(-2,-2),topright:new Springy.Vector(2,2)};
Springy.requestAnimationFrame(function B(){t=H.getBoundingBox();
h={bottomleft:h.bottomleft.add(t.bottomleft.subtract(h.bottomleft).divide(10)),topright:h.topright.add(t.topright.subtract(h.topright).divide(10))};
Springy.requestAnimationFrame(B)
});
var E=function(M){var L=h.topright.subtract(h.bottomleft);
var O=M.subtract(h.bottomleft).divide(L.x).x*l.width;
var N=M.subtract(h.bottomleft).divide(L.y).y*l.height;
return new Springy.Vector(O,N)
};
var e=function(O){var N=h.topright.subtract(h.bottomleft);
var M=(O.x/l.width)*N.x+h.bottomleft.x;
var L=(O.y/l.height)*N.y+h.bottomleft.y;
return new Springy.Vector(M,L)
};
var w=null;
var j=null;
var s=null;
jQuery(l).mousedown(function(M){var N=jQuery(this).offset();
var L=e({x:M.pageX-N.left,y:M.pageY-N.top});
w=j=s=H.nearest(L);
if(w.node!==null){s.point.m=15;
if(F){F(w.node)
}}x.start()
});
jQuery(l).dblclick(function(M){var N=jQuery(this).offset();
var L=e({x:M.pageX-N.left,y:M.pageY-N.top});
w=H.nearest(L);
node=w.node;
if(node&&node.data&&node.data.ondoubleclick){node.data.ondoubleclick(M)
}});
function i(L,M,Q,R,P){var O=L.x;
var N=L.y;
return(O-Q/2-6<=R)&&(O+Q/2+6>=R)&&(N-M/2-6<=P)&&(N+M/2+6>=P)
}jQuery(l).mousemove(function(M){var N=jQuery(this).offset();
var L=e({x:M.pageX-N.left,y:M.pageY-N.top});
j=H.nearest(L);
if(s!==null&&s.node!==null){s.point.p.x=L.x;
s.point.p.y=L.y
}x.start()
});
jQuery(window).bind("mouseup",function(L){s=null
});
var q=function(M){var N=(M.data.label!==undefined)?M.data.label:M.id;
if(M._width&&M._width[N]){return M._width[N]
}z.save();
z.font=(M.data.font!==undefined)?M.data.font:u;
var L=z.measureText(N).width;
z.restore();
M._width||(M._width={});
M._width[N]=L;
return L
};
var o=function(L){return 16
};
var y=function(M){var L=(M.data.image.width!==undefined)?M.data.image.width:m[M.data.image.src].object.width;
return L
};
var f=function(M){var L=(M.data.image.height!==undefined)?M.data.image.height:m[M.data.image.src].object.height;
return L
};
Springy.Node.prototype.getHeight=function(){var L;
if(this.data.image==undefined){L=o(this)
}else{if(this.data.image.src in m&&m[this.data.image.src].loaded){L=f(this)
}else{L=10
}}return L
};
Springy.Node.prototype.getWidth=function(){var L;
if(this.data.image==undefined){L=q(this)
}else{if(this.data.image.src in m&&m[this.data.image.src].loaded){L=y(this)
}else{L=10
}}return L
};
jQuery(window).resize(function(){setTimeout(function(){x.start()
},300)
});
var x=this.renderer=new Springy.Renderer(H,function v(){z.clearRect(0,0,l.width,l.height)
},function p(U,N,L){var al=E(N).x;
var R=E(N).y;
var aj=E(L).x;
var Q=E(L).y;
var an=new Springy.Vector(aj-al,Q-R);
var ap=an.normal().normalise();
var ac=g.getEdges(U.source,U.target);
var P=g.getEdges(U.target,U.source);
var aq=ac.length+P.length;
var ab=0;
for(var ah=0;
ah<ac.length;
ah++){if(ac[ah].id===U.id){ab=ah
}}var S=12;
var T=ap.multiply(-((aq-1)*S)/2+(ab*S));
var ai=6;
var ae=6;
var O=E(N).add(T);
var M=E(L).add(T);
var Y=U.target.getWidth()+ai;
var V=U.target.getHeight()+ae;
var X=I(O,M,{x:aj-Y/2,y:Q-V/2},Y,V);
if(!X){X=M
}var W=(U.data.color!==undefined)?U.data.color:"#000000";
var ak;
var aa;
var Z=(U.data.weight!==undefined)?U.data.weight/2:1;
z.lineWidth=Math.max(Z*2,0.1);
ak=1+z.lineWidth;
aa=8;
var ad=(U.data.directional!==undefined)?U.data.directional:true;
var ag;
if(ad){ag=X.subtract(an.normalise().multiply(aa*0.5))
}else{ag=M
}z.strokeStyle=W;
z.beginPath();
if(U.data.self===true){if(U.data.type==="default"){if(O.y>=50){z.arc(O.x,O.y-28,18,0.7*Math.PI,0.2*Math.PI);
X.x=O.x+Math.cos(0.3*Math.PI)*18;
X.y=(O.y-28)+Math.sin(0.3*Math.PI)*18;
Q=Q+10;
aj=aj-10
}else{z.arc(O.x,O.y+28,18,1.7*Math.PI,1.2*Math.PI);
X.x=O.x+Math.cos(1.3*Math.PI)*18;
X.y=(O.y+28)+Math.sin(1.3*Math.PI)*18;
Q=Q-10;
aj=aj+10
}}else{if(O.y>=50){a(z,O.x,O.y-28,18,0.7);
X.x=O.x+Math.cos(0.3*Math.PI)*18;
X.y=(O.y-28)+Math.sin(0.3*Math.PI)*18;
Q=Q+10;
aj=aj-10
}else{a(z,O.x,O.y+28,18,0.7);
X.x=O.x+Math.cos(1.3*Math.PI)*18;
X.y=(O.y+28)+Math.sin(1.3*Math.PI)*18;
Q=Q-10;
aj=aj+10
}}}else{if(U.data.type==="default"||U.data.type==="designation"){z.moveTo(O.x,O.y);
z.lineTo(ag.x,ag.y)
}else{b(z,O.x,O.y,ag.x,ag.y,4)
}}z.stroke();
if(ad){z.save();
z.fillStyle=W;
z.translate(X.x,X.y);
z.rotate(Math.atan2(Q-R,aj-al));
z.beginPath();
z.moveTo(-aa,ak);
z.lineTo(0,0);
z.lineTo(-aa,-ak);
z.lineTo(-aa*0.8,-0);
z.closePath();
z.fill();
z.restore()
}if(U.data.label!==undefined){text=U.data.label;
z.save();
z.textAlign="center";
z.textBaseline="top";
z.font=(U.data.font!==undefined)?U.data.font:C;
z.fillStyle=W;
var am=Math.atan2(M.y-O.y,M.x-O.x);
var ao=-8;
if(r&&(am>Math.PI/2||am<-Math.PI/2)){ao=8;
am+=Math.PI
}var af=O.add(M).divide(2).add(ap.multiply(ao));
z.translate(af.x,af.y);
z.rotate(am);
z.fillText(text,0,-2);
z.restore()
}},function D(O,N){var W=E(N);
z.save();
var Q=12;
var P=12;
var M=O.getWidth();
var U=O.getHeight();
var S=M+Q;
var T=U+P;
if(O.data.isDesignation===undefined||O.data.isDesignation===null||O.data.isDesignation===false){d(z,W.x-S/2,W.y-T/2,S,T,O.data.backgroundColor)
}else{z.clearRect(W.x-S/2,W.y-T/2,S,T)
}if(O.data.backgroundColor!==undefined){z.fillStyle=O.data.backgroundColor
}else{z.fillStyle="#FFFFFF"
}if(O.data.isDesignation===undefined||O.data.isDesignation===null||O.data.isDesignation===false){z.fill()
}else{z.fillRect(W.x-S/2,W.y-T/2,S,T)
}if(O.data.image==undefined){z.textAlign="left";
z.textBaseline="top";
z.font=(O.data.font!==undefined)?O.data.font:u;
z.fillStyle=(O.data.color!==undefined)?O.data.color:"#000000";
var V=(O.data.label!==undefined)?O.data.label:O.id;
z.fillText(V,W.x-M/2,W.y-U/2)
}else{var L=O.data.image.src;
if(L in m){if(m[L].loaded){z.drawImage(m[L].object,W.x-M/2,W.y-U/2,M,U)
}}else{m[L]={};
var R=new Image();
m[L].object=R;
R.addEventListener("load",function(){m[L].loaded=true
});
R.src=L
}}z.restore()
});
x.start();
function K(R,Q,P,O){var L=((O.y-P.y)*(Q.x-R.x)-(O.x-P.x)*(Q.y-R.y));
if(L===0){return false
}var N=((O.x-P.x)*(R.y-P.y)-(O.y-P.y)*(R.x-P.x))/L;
var M=((Q.x-R.x)*(R.y-P.y)-(Q.y-R.y)*(R.x-P.x))/L;
if(N<0||N>1||M<0||M>1){return false
}return new Springy.Vector(R.x+N*(Q.x-R.x),R.y+N*(Q.y-R.y))
}function I(R,Q,O,P,M){var T={x:O.x,y:O.y};
var N={x:O.x+P,y:O.y};
var L={x:O.x,y:O.y+M};
var S={x:O.x+P,y:O.y+M};
var U;
if(U=K(R,Q,T,N)){return U
}if(U=K(R,Q,N,S)){return U
}if(U=K(R,Q,S,L)){return U
}if(U=K(R,Q,L,T)){return U
}return false
}return this
}
})();