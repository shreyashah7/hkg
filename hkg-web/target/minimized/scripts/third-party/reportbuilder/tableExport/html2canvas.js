(function(h,k,d){var j={},f,b,g;
j.Util={};
j.Util.log=function(m){if(j.logging&&h.console&&h.console.log){h.console.log(m)
}};
j.Util.trimText=(function(m){return function(n){return m?m.apply(n):((n||"")+"").replace(/^\s+|\s+$/g,"")
}
})(String.prototype.trim);
j.Util.asFloat=function(m){return parseFloat(m)
};
(function(){var n=/((rgba|rgb)\([^\)]+\)(\s-?\d+px){0,})/g;
var m=/(-?\d+px)|(#.+)|(rgb\(.+\))|(rgba\(.+\))/g;
j.Util.parseTextShadows=function(t){if(!t||t==="none"){return[]
}var r=t.match(n),p=[];
for(var o=0;
r&&(o<r.length);
o++){var q=r[o].match(m);
p.push({color:q[0],offsetX:q[1]?q[1].replace("px",""):0,offsetY:q[2]?q[2].replace("px",""):0,blur:q[3]?q[3].replace("px",""):0})
}return p
}
})();
j.Util.parseBackgroundImage=function(x){var o=" \r\n\t",n,p,u,B,q,s=[],w,t=0,y=0,m,v;
var A=function(){if(n){if(p.substr(0,1)==='"'){p=p.substr(1,p.length-2)
}if(p){v.push(p)
}if(n.substr(0,1)==="-"&&(B=n.indexOf("-",1)+1)>0){u=n.substr(0,B);
n=n.substr(B)
}s.push({prefix:u,method:n.toLowerCase(),value:q,args:v})
}v=[];
n=u=p=q=""
};
A();
for(var r=0,z=x.length;
r<z;
r++){w=x[r];
if(t===0&&o.indexOf(w)>-1){continue
}switch(w){case'"':if(!m){m=w
}else{if(m===w){m=null
}}break;
case"(":if(m){break
}else{if(t===0){t=1;
q+=w;
continue
}else{y++
}}break;
case")":if(m){break
}else{if(t===1){if(y===0){t=0;
q+=w;
A();
continue
}else{y--
}}}break;
case",":if(m){break
}else{if(t===0){A();
continue
}else{if(t===1){if(y===0&&!n.match(/^url$/i)){v.push(p);
p="";
q+=w;
continue
}}}}break
}q+=w;
if(t===0){n+=w
}else{p+=w
}}A();
return s
};
j.Util.Bounds=function(m){var o,n={};
if(m.getBoundingClientRect){o=m.getBoundingClientRect();
n.top=o.top;
n.bottom=o.bottom||(o.top+o.height);
n.left=o.left;
n.width=m.offsetWidth;
n.height=m.offsetHeight
}return n
};
j.Util.OffsetBounds=function(m){var n=m.offsetParent?j.Util.OffsetBounds(m.offsetParent):{top:0,left:0};
return{top:m.offsetTop+n.top,bottom:m.offsetTop+m.offsetHeight+n.top,left:m.offsetLeft+n.left,width:m.offsetWidth,height:m.offsetHeight}
};
function c(n,p,q){var m=n.runtimeStyle&&n.runtimeStyle[p],r,o=n.style;
if(!/^-?[0-9]+\.?[0-9]*(?:px)?$/i.test(q)&&/^-?\d/.test(q)){r=o.left;
if(m){n.runtimeStyle.left=n.currentStyle.left
}o.left=p==="fontSize"?"1em":(q||0);
q=o.pixelLeft+"px";
o.left=r;
if(m){n.runtimeStyle.left=m
}}if(!/^(thin|medium|thick)$/i.test(q)){return Math.round(parseFloat(q))+"px"
}return q
}function a(m){return parseInt(m,10)
}function i(m){return m.toString().indexOf("%")!==-1
}function l(p,n,o,m){p=(p||"").split(",");
p=p[m||0]||p[0]||"auto";
p=j.Util.trimText(p).split(" ");
if(o==="backgroundSize"&&(p[0]&&p[0].match(/^(cover|contain|auto)$/))){return p
}else{p[0]=(p[0].indexOf("%")===-1)?c(n,o+"X",p[0]):p[0];
if(p[1]===d){if(o==="backgroundSize"){p[1]="auto";
return p
}else{p[1]=p[0]
}}p[1]=(p[1].indexOf("%")===-1)?c(n,o+"Y",p[1]):p[1]
}return p
}j.Util.getCSS=function(o,p,n){if(f!==o){b=k.defaultView.getComputedStyle(o,null)
}var q=b[p];
if(/^background(Size|Position)$/.test(p)){return l(q,o,p,n)
}else{if(/border(Top|Bottom)(Left|Right)Radius/.test(p)){var m=q.split(" ");
if(m.length<=1){m[1]=m[0]
}return m.map(a)
}}return q
};
j.Util.resizeBounds=function(r,m,s,t,u){var p=s/t,o=r/m,n,q;
if(!u||u==="auto"){n=s;
q=t
}else{if(p<o^u==="contain"){q=t;
n=t*o
}else{n=s;
q=s/o
}}return{width:n,height:q}
};
j.Util.BackgroundPosition=function(q,s,t,m,n){var o=j.Util.getCSS(q,"backgroundPosition",m),r,p;
if(o.length===1){o=[o[0],o[0]]
}if(i(o[0])){r=(s.width-(n||t).width)*(parseFloat(o[0])/100)
}else{r=parseInt(o[0],10)
}if(o[1]==="auto"){p=r/t.width*t.height
}else{if(i(o[1])){p=(s.height-(n||t).height)*parseFloat(o[1])/100
}else{p=parseInt(o[1],10)
}}if(o[0]==="auto"){r=p/t.height*t.width
}return{left:r,top:p}
};
j.Util.BackgroundSize=function(p,r,s,n){var o=j.Util.getCSS(p,"backgroundSize",n),q,m;
if(o.length===1){o=[o[0],o[0]]
}if(i(o[0])){q=r.width*parseFloat(o[0])/100
}else{if(/contain|cover/.test(o[0])){return j.Util.resizeBounds(s.width,s.height,r.width,r.height,o[0])
}else{q=parseInt(o[0],10)
}}if(o[0]==="auto"&&o[1]==="auto"){m=s.height
}else{if(o[1]==="auto"){m=q/s.width*s.height
}else{if(i(o[1])){m=r.height*parseFloat(o[1])/100
}else{m=parseInt(o[1],10)
}}}if(o[0]==="auto"){q=m/s.height*s.width
}return{width:q,height:m}
};
j.Util.BackgroundRepeat=function(n,m){var o=j.Util.getCSS(n,"backgroundRepeat").split(",").map(j.Util.trimText);
return o[m]||o[0]
};
j.Util.Extend=function(m,o){for(var n in m){if(m.hasOwnProperty(n)){o[n]=m[n]
}}return o
};
j.Util.Children=function(o){var n;
try{n=(o.nodeName&&o.nodeName.toUpperCase()==="IFRAME")?o.contentDocument||o.contentWindow.document:(function(q){var p=[];
if(q!==null){(function(v,t){var u=v.length,s=0;
if(typeof t.length==="number"){for(var r=t.length;
s<r;
s++){v[u++]=t[s]
}}else{while(t[s]!==d){v[u++]=t[s++]
}}v.length=u;
return v
})(p,q)
}return p
})(o.childNodes)
}catch(m){j.Util.log("html2canvas.Util.Children failed with exception: "+m.message);
n=[]
}return n
};
j.Util.isTransparent=function(m){return(!m||m==="transparent"||m==="rgba(0, 0, 0, 0)")
};
j.Util.Font=(function(){var m={};
return function(p,w,t){if(m[p+"-"+w]!==d){return m[p+"-"+w]
}var n=t.createElement("div"),q=t.createElement("img"),u=t.createElement("span"),o="Hidden Text",s,v,r;
n.style.visibility="hidden";
n.style.fontFamily=p;
n.style.fontSize=w;
n.style.margin=0;
n.style.padding=0;
t.body.appendChild(n);
q.src="data:image/gif;base64,R0lGODlhAQABAIABAP///wAAACwAAAAAAQABAAACAkQBADs=";
q.width=1;
q.height=1;
q.style.margin=0;
q.style.padding=0;
q.style.verticalAlign="baseline";
u.style.fontFamily=p;
u.style.fontSize=w;
u.style.margin=0;
u.style.padding=0;
u.appendChild(t.createTextNode(o));
n.appendChild(u);
n.appendChild(q);
s=(q.offsetTop-u.offsetTop)+1;
n.removeChild(u);
n.appendChild(t.createTextNode(o));
n.style.lineHeight="normal";
q.style.verticalAlign="super";
v=(q.offsetTop-n.offsetTop)+1;
r={baseline:s,lineWidth:1,middle:v};
m[p+"-"+w]=r;
t.body.removeChild(n);
return r
}
})();
(function(){var o=j.Util,m={};
j.Generate=m;
var p=[/^(-webkit-linear-gradient)\(([a-z\s]+)([\w\d\.\s,%\(\)]+)\)$/,/^(-o-linear-gradient)\(([a-z\s]+)([\w\d\.\s,%\(\)]+)\)$/,/^(-webkit-gradient)\((linear|radial),\s((?:\d{1,3}%?)\s(?:\d{1,3}%?),\s(?:\d{1,3}%?)\s(?:\d{1,3}%?))([\w\d\.\s,%\(\)\-]+)\)$/,/^(-moz-linear-gradient)\(((?:\d{1,3}%?)\s(?:\d{1,3}%?))([\w\d\.\s,%\(\)]+)\)$/,/^(-webkit-radial-gradient)\(((?:\d{1,3}%?)\s(?:\d{1,3}%?)),\s(\w+)\s([a-z\-]+)([\w\d\.\s,%\(\)]+)\)$/,/^(-moz-radial-gradient)\(((?:\d{1,3}%?)\s(?:\d{1,3}%?)),\s(\w+)\s?([a-z\-]*)([\w\d\.\s,%\(\)]+)\)$/,/^(-o-radial-gradient)\(((?:\d{1,3}%?)\s(?:\d{1,3}%?)),\s(\w+)\s([a-z\-]+)([\w\d\.\s,%\(\)]+)\)$/];
m.parseGradient=function(v,q){var z,u,w=p.length,E,y,B,r,t,A,D,x,C,s;
for(u=0;
u<w;
u+=1){E=v.match(p[u]);
if(E){break
}}if(E){switch(E[1]){case"-webkit-linear-gradient":case"-o-linear-gradient":z={type:"linear",x0:null,y0:null,x1:null,y1:null,colorStops:[]};
B=E[2].match(/\w+/g);
if(B){r=B.length;
for(u=0;
u<r;
u+=1){switch(B[u]){case"top":z.y0=0;
z.y1=q.height;
break;
case"right":z.x0=q.width;
z.x1=0;
break;
case"bottom":z.y0=q.height;
z.y1=0;
break;
case"left":z.x0=0;
z.x1=q.width;
break
}}}if(z.x0===null&&z.x1===null){z.x0=z.x1=q.width/2
}if(z.y0===null&&z.y1===null){z.y0=z.y1=q.height/2
}B=E[3].match(/((?:rgb|rgba)\(\d{1,3},\s\d{1,3},\s\d{1,3}(?:,\s[0-9\.]+)?\)(?:\s\d{1,3}(?:%|px))?)+/g);
if(B){r=B.length;
t=1/Math.max(r-1,1);
for(u=0;
u<r;
u+=1){A=B[u].match(/((?:rgb|rgba)\(\d{1,3},\s\d{1,3},\s\d{1,3}(?:,\s[0-9\.]+)?\))\s*(\d{1,3})?(%|px)?/);
if(A[2]){y=parseFloat(A[2]);
if(A[3]==="%"){y/=100
}else{y/=q.width
}}else{y=u*t
}z.colorStops.push({color:A[1],stop:y})
}}break;
case"-webkit-gradient":z={type:E[2]==="radial"?"circle":E[2],x0:0,y0:0,x1:0,y1:0,colorStops:[]};
B=E[3].match(/(\d{1,3})%?\s(\d{1,3})%?,\s(\d{1,3})%?\s(\d{1,3})%?/);
if(B){z.x0=(B[1]*q.width)/100;
z.y0=(B[2]*q.height)/100;
z.x1=(B[3]*q.width)/100;
z.y1=(B[4]*q.height)/100
}B=E[4].match(/((?:from|to|color-stop)\((?:[0-9\.]+,\s)?(?:rgb|rgba)\(\d{1,3},\s\d{1,3},\s\d{1,3}(?:,\s[0-9\.]+)?\)\))+/g);
if(B){r=B.length;
for(u=0;
u<r;
u+=1){A=B[u].match(/(from|to|color-stop)\(([0-9\.]+)?(?:,\s)?((?:rgb|rgba)\(\d{1,3},\s\d{1,3},\s\d{1,3}(?:,\s[0-9\.]+)?\))\)/);
y=parseFloat(A[2]);
if(A[1]==="from"){y=0
}if(A[1]==="to"){y=1
}z.colorStops.push({color:A[3],stop:y})
}}break;
case"-moz-linear-gradient":z={type:"linear",x0:0,y0:0,x1:0,y1:0,colorStops:[]};
B=E[2].match(/(\d{1,3})%?\s(\d{1,3})%?/);
if(B){z.x0=(B[1]*q.width)/100;
z.y0=(B[2]*q.height)/100;
z.x1=q.width-z.x0;
z.y1=q.height-z.y0
}B=E[3].match(/((?:rgb|rgba)\(\d{1,3},\s\d{1,3},\s\d{1,3}(?:,\s[0-9\.]+)?\)(?:\s\d{1,3}%)?)+/g);
if(B){r=B.length;
t=1/Math.max(r-1,1);
for(u=0;
u<r;
u+=1){A=B[u].match(/((?:rgb|rgba)\(\d{1,3},\s\d{1,3},\s\d{1,3}(?:,\s[0-9\.]+)?\))\s*(\d{1,3})?(%)?/);
if(A[2]){y=parseFloat(A[2]);
if(A[3]){y/=100
}}else{y=u*t
}z.colorStops.push({color:A[1],stop:y})
}}break;
case"-webkit-radial-gradient":case"-moz-radial-gradient":case"-o-radial-gradient":z={type:"circle",x0:0,y0:0,x1:q.width,y1:q.height,cx:0,cy:0,rx:0,ry:0,colorStops:[]};
B=E[2].match(/(\d{1,3})%?\s(\d{1,3})%?/);
if(B){z.cx=(B[1]*q.width)/100;
z.cy=(B[2]*q.height)/100
}B=E[3].match(/\w+/);
A=E[4].match(/[a-z\-]*/);
if(B&&A){switch(A[0]){case"farthest-corner":case"cover":case"":D=Math.sqrt(Math.pow(z.cx,2)+Math.pow(z.cy,2));
x=Math.sqrt(Math.pow(z.cx,2)+Math.pow(z.y1-z.cy,2));
C=Math.sqrt(Math.pow(z.x1-z.cx,2)+Math.pow(z.y1-z.cy,2));
s=Math.sqrt(Math.pow(z.x1-z.cx,2)+Math.pow(z.cy,2));
z.rx=z.ry=Math.max(D,x,C,s);
break;
case"closest-corner":D=Math.sqrt(Math.pow(z.cx,2)+Math.pow(z.cy,2));
x=Math.sqrt(Math.pow(z.cx,2)+Math.pow(z.y1-z.cy,2));
C=Math.sqrt(Math.pow(z.x1-z.cx,2)+Math.pow(z.y1-z.cy,2));
s=Math.sqrt(Math.pow(z.x1-z.cx,2)+Math.pow(z.cy,2));
z.rx=z.ry=Math.min(D,x,C,s);
break;
case"farthest-side":if(B[0]==="circle"){z.rx=z.ry=Math.max(z.cx,z.cy,z.x1-z.cx,z.y1-z.cy)
}else{z.type=B[0];
z.rx=Math.max(z.cx,z.x1-z.cx);
z.ry=Math.max(z.cy,z.y1-z.cy)
}break;
case"closest-side":case"contain":if(B[0]==="circle"){z.rx=z.ry=Math.min(z.cx,z.cy,z.x1-z.cx,z.y1-z.cy)
}else{z.type=B[0];
z.rx=Math.min(z.cx,z.x1-z.cx);
z.ry=Math.min(z.cy,z.y1-z.cy)
}break
}}B=E[5].match(/((?:rgb|rgba)\(\d{1,3},\s\d{1,3},\s\d{1,3}(?:,\s[0-9\.]+)?\)(?:\s\d{1,3}(?:%|px))?)+/g);
if(B){r=B.length;
t=1/Math.max(r-1,1);
for(u=0;
u<r;
u+=1){A=B[u].match(/((?:rgb|rgba)\(\d{1,3},\s\d{1,3},\s\d{1,3}(?:,\s[0-9\.]+)?\))\s*(\d{1,3})?(%|px)?/);
if(A[2]){y=parseFloat(A[2]);
if(A[3]==="%"){y/=100
}else{y/=q.width
}}else{y=u*t
}z.colorStops.push({color:A[1],stop:y})
}}break
}}return z
};
function n(q){return function(r){try{q.addColorStop(r.stop,r.color)
}catch(s){o.log(["failed to add color stop: ",s,"; tried to add: ",r])
}}
}m.Gradient=function(q,r){if(r.width===0||r.height===0){return
}var t=k.createElement("canvas"),z=t.getContext("2d"),x,y;
t.width=r.width;
t.height=r.height;
x=j.Generate.parseGradient(q,r);
if(x){switch(x.type){case"linear":y=z.createLinearGradient(x.x0,x.y0,x.x1,x.y1);
x.colorStops.forEach(n(y));
z.fillStyle=y;
z.fillRect(0,0,r.width,r.height);
break;
case"circle":y=z.createRadialGradient(x.cx,x.cy,0,x.cx,x.cy,x.rx);
x.colorStops.forEach(n(y));
z.fillStyle=y;
z.fillRect(0,0,r.width,r.height);
break;
case"ellipse":var u=k.createElement("canvas"),s=u.getContext("2d"),w=Math.max(x.rx,x.ry),v=w*2;
u.width=u.height=v;
y=s.createRadialGradient(x.rx,x.ry,0,x.rx,x.ry,w);
x.colorStops.forEach(n(y));
s.fillStyle=y;
s.fillRect(0,0,v,v);
z.fillStyle=x.colorStops[x.colorStops.length-1].color;
z.fillRect(0,0,t.width,t.height);
z.drawImage(u,x.cx-x.rx,x.cy-x.ry,2*x.rx,2*x.ry);
break
}}return t
};
m.ListAlpha=function(s){var r="",q;
do{q=s%26;
r=String.fromCharCode((q)+64)+r;
s=s/26
}while((s*26)>26);
return r
};
m.ListRoman=function(u){var t=["M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"],r=[1000,900,500,400,100,90,50,40,10,9,5,4,1],w="",s,q=t.length;
if(u<=0||u>=4000){return u
}for(s=0;
s<q;
s+=1){while(u>=r[s]){u-=r[s];
w+=t[s]
}}return w
}
})();
function e(n,m){var o=[];
return{storage:o,width:n,height:m,clip:function(){o.push({type:"function",name:"clip","arguments":arguments})
},translate:function(){o.push({type:"function",name:"translate","arguments":arguments})
},fill:function(){o.push({type:"function",name:"fill","arguments":arguments})
},save:function(){o.push({type:"function",name:"save","arguments":arguments})
},restore:function(){o.push({type:"function",name:"restore","arguments":arguments})
},fillRect:function(){o.push({type:"function",name:"fillRect","arguments":arguments})
},createPattern:function(){o.push({type:"function",name:"createPattern","arguments":arguments})
},drawShape:function(){var p=[];
o.push({type:"function",name:"drawShape","arguments":p});
return{moveTo:function(){p.push({name:"moveTo","arguments":arguments})
},lineTo:function(){p.push({name:"lineTo","arguments":arguments})
},arcTo:function(){p.push({name:"arcTo","arguments":arguments})
},bezierCurveTo:function(){p.push({name:"bezierCurveTo","arguments":arguments})
},quadraticCurveTo:function(){p.push({name:"quadraticCurveTo","arguments":arguments})
}}
},drawImage:function(){o.push({type:"function",name:"drawImage","arguments":arguments})
},fillText:function(){o.push({type:"function",name:"fillText","arguments":arguments})
},setVariable:function(p,q){o.push({type:"variable",name:p,"arguments":q});
return q
}}
}j.Parse=function(w,Z,O){h.scroll(0,0);
var aF=((Z.elements===d)?k.body:Z.elements[0]),ac=0,J=aF.ownerDocument,aG=j.Util,aw=aG.Support(Z,J),av=new RegExp("("+Z.ignoreElements+")"),D=J.body,p=aG.getCSS,az="___html2canvas___pseudoelement",I=J.createElement("style");
I.innerHTML="."+az+'-parent:before { content: "" !important; display: none !important; }.'+az+'-parent:after { content: "" !important; display: none !important; }';
D.appendChild(I);
w=w||{};
ad();
function ad(){var aL=p(k.documentElement,"backgroundColor"),aK=(aG.isTransparent(aL)&&aF===k.body),aJ=aA(aF,null,false,aK);
ap(aF);
S(aF,aJ,function(){if(aK){aL=aJ.backgroundColor
}ah();
aG.log("Done parsing, moving to Render.");
O({backgroundColor:aL,stack:aJ})
})
}function ap(aN){var aJ=[],aM=[];
aP();
aL(aN);
aO();
function aP(){var aW=/:before|:after/;
var aV=k.styleSheets;
for(var aT=0,aS=aV.length;
aT<aS;
aT++){try{var aX=aV[aT].cssRules;
for(var aR=0,aQ=aX.length;
aR<aQ;
aR++){if(aW.test(aX[aR].selectorText)){aM.push(aX[aR].selectorText)
}}}catch(aU){}}for(aT=0,aS=aM.length;
aT<aS;
aT++){aM[aT]=aM[aT].match(/(^[^:]*)/)[1]
}}function aL(aT){var aS=k.querySelectorAll(aM.join(","));
for(var aR=0,aQ=aS.length;
aR<aQ;
aR++){aK(aS[aR])
}}function aK(aQ){var aR=af(aQ,":before"),aS=af(aQ,":after");
if(aR){aJ.push({type:"before",pseudo:aR,el:aQ})
}if(aS){aJ.push({type:"after",pseudo:aS,el:aQ})
}}function aO(){aJ.forEach(function(aQ){ax(aQ.el,az+"-parent")
});
aJ.forEach(function(aQ){if(aQ.type==="before"){aQ.el.insertBefore(aQ.pseudo,aQ.el.firstChild)
}else{aQ.el.appendChild(aQ.pseudo)
}})
}}function ah(){D.removeChild(I);
var aK=k.getElementsByClassName(az+"-element");
while(aK.length){aK[0].parentNode.removeChild(aK[0])
}var aJ=k.getElementsByClassName(az+"-parent");
while(aJ.length){aB(aJ[0],az+"-parent")
}}function ax(aK,aJ){if(aK.classList){aK.classList.add(aJ)
}else{aK.className=aK.className+" "+aJ
}}function aB(aK,aJ){if(aK.classList){aK.classList.remove(aJ)
}else{aK.className=aK.className.replace(aJ,"").trim()
}}function aI(aK,aJ){return aK.className.indexOf(aJ)>-1
}function al(aJ){return Array.prototype.slice.call(aJ)
}function P(){return Math.max(Math.max(J.body.scrollWidth,J.documentElement.scrollWidth),Math.max(J.body.offsetWidth,J.documentElement.offsetWidth),Math.max(J.body.clientWidth,J.documentElement.clientWidth))
}function aC(){return Math.max(Math.max(J.body.scrollHeight,J.documentElement.scrollHeight),Math.max(J.body.offsetHeight,J.documentElement.offsetHeight),Math.max(J.body.clientHeight,J.documentElement.clientHeight))
}function ay(aJ,aK){var aL=parseInt(p(aJ,aK),10);
return(isNaN(aL))?0:aL
}function ag(aM,aJ,aO,aL,aN,aK){if(aK!=="transparent"){aM.setVariable("fillStyle",aK);
aM.fillRect(aJ,aO,aL,aN);
ac+=1
}}function H(aJ,aL,aK){if(aJ.length>0){return aL+aK.toUpperCase()
}}function aj(aK,aJ){switch(aJ){case"lowercase":return aK.toLowerCase();
case"capitalize":return aK.replace(/(^|\s|:|-|\(|\))([a-z])/g,H);
case"uppercase":return aK.toUpperCase();
default:return aK
}}function r(aJ){return(/^(normal|none|0px)$/.test(aJ))
}function aH(aL,aJ,aM,aK){if(aL!==null&&aG.trimText(aL).length>0){aK.fillText(aL,aJ,aM);
ac+=1
}}function s(aQ,aK,aJ,aL){var aO=false,aP=p(aK,"fontWeight"),aN=p(aK,"fontFamily"),aR=p(aK,"fontSize"),aM=aG.parseTextShadows(p(aK,"textShadow"));
switch(parseInt(aP,10)){case 401:aP="bold";
break;
case 400:aP="normal";
break
}aQ.setVariable("fillStyle",aL);
aQ.setVariable("font",[p(aK,"fontStyle"),p(aK,"fontVariant"),aP,aR,aN].join(" "));
aQ.setVariable("textAlign",(aO)?"right":"left");
if(aM.length){aQ.setVariable("shadowColor",aM[0].color);
aQ.setVariable("shadowOffsetX",aM[0].offsetX);
aQ.setVariable("shadowOffsetY",aM[0].offsetY);
aQ.setVariable("shadowBlur",aM[0].blur)
}if(aJ!=="none"){return aG.Font(aN,aR,J)
}}function C(aJ,aN,aM,aL,aK){switch(aN){case"underline":ag(aJ,aM.left,Math.round(aM.top+aL.baseline+aL.lineWidth),aM.width,1,aK);
break;
case"overline":ag(aJ,aM.left,Math.round(aM.top),aM.width,1,aK);
break;
case"line-through":ag(aJ,aM.left,Math.ceil(aM.top+aL.middle+aL.lineWidth),aM.width,1,aK);
break
}}function am(aN,aP,aO,aM,aJ){var aL;
if(aw.rangeBounds&&!aJ){if(aO!=="none"||aG.trimText(aP).length!==0){aL=z(aP,aN.node,aN.textOffset)
}aN.textOffset+=aP.length
}else{if(aN.node&&typeof aN.node.nodeValue==="string"){var aK=(aM)?aN.node.splitText(aP.length):null;
aL=n(aN.node,aJ);
aN.node=aK
}}return aL
}function z(aM,aL,aK){var aJ=J.createRange();
aJ.setStart(aL,aK);
aJ.setEnd(aL,aK+aM.length);
return aJ.getBoundingClientRect()
}function n(aK,aJ){var aL=aK.parentNode,aO=J.createElement("wrapper"),aN=aK.cloneNode(true);
aO.appendChild(aK.cloneNode(true));
aL.replaceChild(aO,aK);
var aM=aJ?aG.OffsetBounds(aO):aG.Bounds(aO);
aL.replaceChild(aN,aO);
return aM
}function L(aL,aM,aQ){var aR=aQ.ctx,aN=p(aL,"color"),aS=p(aL,"textDecoration"),aK=p(aL,"textAlign"),aP,aO,aJ={node:aM,textOffset:0};
if(aG.trimText(aM.nodeValue).length>0){aM.nodeValue=aj(aM.nodeValue,p(aL,"textTransform"));
aK=aK.replace(["-webkit-auto"],["auto"]);
aO=(!Z.letterRendering&&/^(left|right|justify|auto)$/.test(aK)&&r(p(aL,"letterSpacing")))?aM.nodeValue.split(/(\b| )/):aM.nodeValue.split("");
aP=s(aR,aL,aS,aN);
if(Z.chinese){aO.forEach(function(aU,aT){if(/.*[\u4E00-\u9FA5].*$/.test(aU)){aU=aU.split("");
aU.unshift(aT,1);
aO.splice.apply(aO,aU)
}})
}aO.forEach(function(aV,aT){var aU=am(aJ,aV,aS,(aT<aO.length-1),aQ.transform.matrix);
if(aU){aH(aV,aU.left,aU.bottom,aR);
C(aR,aS,aU,aP,aN)
}})
}}function K(aJ,aN){var aM=J.createElement("boundelement"),aK,aL;
aM.style.display="inline";
aK=aJ.style.listStyleType;
aJ.style.listStyleType="none";
aM.appendChild(J.createTextNode(aN));
aJ.insertBefore(aM,aJ.firstChild);
aL=aG.Bounds(aM);
aJ.removeChild(aM);
aJ.style.listStyleType=aK;
return aL
}function at(aK){var aJ=-1,aL=1,aM=aK.parentNode.childNodes;
if(aK.parentNode){while(aM[++aJ]!==aK){if(aM[aJ].nodeType===1){aL++
}}return aL
}else{return -1
}}function aD(aK,aL){var aJ=at(aK),aM;
switch(aL){case"decimal":aM=aJ;
break;
case"decimal-leading-zero":aM=(aJ.toString().length===1)?aJ="0"+aJ.toString():aJ.toString();
break;
case"upper-roman":aM=j.Generate.ListRoman(aJ);
break;
case"lower-roman":aM=j.Generate.ListRoman(aJ).toLowerCase();
break;
case"lower-alpha":aM=j.Generate.ListAlpha(aJ).toLowerCase();
break;
case"upper-alpha":aM=j.Generate.ListAlpha(aJ);
break
}return aM+". "
}function M(aM,aK,aP){var aJ,aQ,aL=aK.ctx,aN=p(aM,"listStyleType"),aO;
if(/^(decimal|decimal-leading-zero|upper-alpha|upper-latin|upper-roman|lower-alpha|lower-greek|lower-latin|lower-roman)$/i.test(aN)){aQ=aD(aM,aN);
aO=K(aM,aQ);
s(aL,aM,"none",p(aM,"color"));
if(p(aM,"listStylePosition")==="inside"){aL.setVariable("textAlign","left");
aJ=aP.left
}else{return
}aH(aQ,aJ,aO.bottom,aL)
}}function T(aK){var aJ=w[aK];
return(aJ&&aJ.succeeded===true)?aJ.img:false
}function x(aM,aO){var aJ=Math.max(aM.left,aO.left),aN=Math.max(aM.top,aO.top),aK=Math.min((aM.left+aM.width),(aO.left+aO.width)),aL=Math.min((aM.top+aM.height),(aO.top+aO.height));
return{left:aJ,top:aN,width:aK-aJ,height:aL-aN}
}function U(aO,aM,aL){var aQ,aK=aM.cssPosition!=="static",aP=aK?p(aO,"zIndex"):"auto",aN=p(aO,"opacity"),aJ=p(aO,"cssFloat")!=="none";
aM.zIndex=aQ=E(aP);
aQ.isPositioned=aK;
aQ.isFloated=aJ;
aQ.opacity=aN;
aQ.ownStacking=(aP!=="auto"||aN<1);
aQ.depth=aL?(aL.zIndex.depth+1):0;
if(aL){aL.zIndex.children.push(aM)
}}function E(aJ){return{depth:0,zindex:aJ,children:[]}
}function A(aR,aO,aN,aJ,aP){var aM=ay(aO,"paddingLeft"),aQ=ay(aO,"paddingTop"),aL=ay(aO,"paddingRight"),aK=ay(aO,"paddingBottom");
q(aR,aN,0,0,aN.width,aN.height,aJ.left+aM+aP[3].width,aJ.top+aQ+aP[0].width,aJ.width-(aP[1].width+aP[3].width+aM+aL),aJ.height-(aP[0].width+aP[2].width+aQ+aK))
}function V(aJ){return["Top","Right","Bottom","Left"].map(function(aK){return{width:ay(aJ,"border"+aK+"Width"),color:p(aJ,"border"+aK+"Color")}
})
}function Q(aJ){return["TopLeft","TopRight","BottomRight","BottomLeft"].map(function(aK){return p(aJ,"border"+aK+"Radius")
})
}function W(aR,aQ,aN,aM){var aP=4*((Math.sqrt(2)-1)/3);
var aL=(aN)*aP,aJ=(aM)*aP,aO=aR+aN,aK=aQ+aM;
return{topLeft:F({x:aR,y:aK},{x:aR,y:aK-aJ},{x:aO-aL,y:aQ},{x:aO,y:aQ}),topRight:F({x:aR,y:aQ},{x:aR+aL,y:aQ},{x:aO,y:aK-aJ},{x:aO,y:aK}),bottomRight:F({x:aO,y:aQ},{x:aO,y:aQ+aJ},{x:aR+aL,y:aK},{x:aR,y:aK}),bottomLeft:F({x:aO,y:aK},{x:aO-aL,y:aK},{x:aR,y:aQ+aJ},{x:aR,y:aQ})}
}function F(aN,aM,aL,aJ){var aK=function(aP,aO,aQ){return{x:aP.x+(aO.x-aP.x)*aQ,y:aP.y+(aO.y-aP.y)*aQ}
};
return{start:aN,startControl:aM,endControl:aL,end:aJ,subdivide:function(aQ){var aS=aK(aN,aM,aQ),aT=aK(aM,aL,aQ),aU=aK(aL,aJ,aQ),aR=aK(aS,aT,aQ),aO=aK(aT,aU,aQ),aP=aK(aR,aO,aQ);
return[F(aN,aS,aR,aP),F(aP,aO,aU,aJ)]
},curveTo:function(aO){aO.push(["bezierCurve",aM.x,aM.y,aL.x,aL.y,aJ.x,aJ.y])
},curveToReversed:function(aO){aO.push(["bezierCurve",aL.x,aL.y,aM.x,aM.y,aN.x,aN.y])
}}
}function ae(aN,aO,aM,aL,aK,aJ,aP){if(aO[0]>0||aO[1]>0){aN.push(["line",aL[0].start.x,aL[0].start.y]);
aL[0].curveTo(aN);
aL[1].curveTo(aN)
}else{aN.push(["line",aJ,aP])
}if(aM[0]>0||aM[1]>0){aN.push(["line",aK[0].start.x,aK[0].start.y])
}}function y(aP,aN,aM,aQ,aK,aO,aJ){var aL=[];
if(aN[0]>0||aN[1]>0){aL.push(["line",aQ[1].start.x,aQ[1].start.y]);
aQ[1].curveTo(aL)
}else{aL.push(["line",aP.c1[0],aP.c1[1]])
}if(aM[0]>0||aM[1]>0){aL.push(["line",aO[0].start.x,aO[0].start.y]);
aO[0].curveTo(aL);
aL.push(["line",aJ[0].end.x,aJ[0].end.y]);
aJ[0].curveToReversed(aL)
}else{aL.push(["line",aP.c2[0],aP.c2[1]]);
aL.push(["line",aP.c3[0],aP.c3[1]])
}if(aN[0]>0||aN[1]>0){aL.push(["line",aK[1].end.x,aK[1].end.y]);
aK[1].curveToReversed(aL)
}else{aL.push(["line",aP.c4[0],aP.c4[1]])
}return aL
}function aE(aM,aJ,aL){var aQ=aM.left,aN=aM.top,aV=aM.width,aU=aM.height,aW=aJ[0][0],aP=aJ[0][1],aZ=aJ[1][0],aS=aJ[1][1],a0=aJ[2][0],aT=aJ[2][1],aY=aJ[3][0],aR=aJ[3][1],aO=aV-aZ,a1=aU-aT,aK=aV-a0,aX=aU-aR;
return{topLeftOuter:W(aQ,aN,aW,aP).topLeft.subdivide(0.5),topLeftInner:W(aQ+aL[3].width,aN+aL[0].width,Math.max(0,aW-aL[3].width),Math.max(0,aP-aL[0].width)).topLeft.subdivide(0.5),topRightOuter:W(aQ+aO,aN,aZ,aS).topRight.subdivide(0.5),topRightInner:W(aQ+Math.min(aO,aV+aL[3].width),aN+aL[0].width,(aO>aV+aL[3].width)?0:aZ-aL[3].width,aS-aL[0].width).topRight.subdivide(0.5),bottomRightOuter:W(aQ+aK,aN+a1,a0,aT).bottomRight.subdivide(0.5),bottomRightInner:W(aQ+Math.min(aK,aV+aL[3].width),aN+Math.min(a1,aU+aL[0].width),Math.max(0,a0-aL[1].width),Math.max(0,aT-aL[2].width)).bottomRight.subdivide(0.5),bottomLeftOuter:W(aQ,aN+aX,aY,aR).bottomLeft.subdivide(0.5),bottomLeftInner:W(aQ+aL[3].width,aN+aX,Math.max(0,aY-aL[3].width),Math.max(0,aR-aL[2].width)).bottomLeft.subdivide(0.5)}
}function aq(aM,aO,aP,aJ,aN){var aL=p(aM,"backgroundClip"),aK=[];
switch(aL){case"content-box":case"padding-box":ae(aK,aJ[0],aJ[1],aO.topLeftInner,aO.topRightInner,aN.left+aP[3].width,aN.top+aP[0].width);
ae(aK,aJ[1],aJ[2],aO.topRightInner,aO.bottomRightInner,aN.left+aN.width-aP[1].width,aN.top+aP[0].width);
ae(aK,aJ[2],aJ[3],aO.bottomRightInner,aO.bottomLeftInner,aN.left+aN.width-aP[1].width,aN.top+aN.height-aP[2].width);
ae(aK,aJ[3],aJ[0],aO.bottomLeftInner,aO.topLeftInner,aN.left+aP[3].width,aN.top+aN.height-aP[2].width);
break;
default:ae(aK,aJ[0],aJ[1],aO.topLeftOuter,aO.topRightOuter,aN.left,aN.top);
ae(aK,aJ[1],aJ[2],aO.topRightOuter,aO.bottomRightOuter,aN.left+aN.width,aN.top);
ae(aK,aJ[2],aJ[3],aO.bottomRightOuter,aO.bottomLeftOuter,aN.left+aN.width,aN.top+aN.height);
ae(aK,aJ[3],aJ[0],aO.bottomLeftOuter,aO.topLeftOuter,aN.left,aN.top+aN.height);
break
}return aK
}function ak(aP,aK,aS){var aW=aK.left,aV=aK.top,aM=aK.width,aY=aK.height,aL,aT,aR,aU,aO,aJ,aN=Q(aP),aQ=aE(aK,aN,aS),aX={clip:aq(aP,aQ,aS,aN,aK),borders:[]};
for(aL=0;
aL<4;
aL++){if(aS[aL].width>0){aT=aW;
aR=aV;
aU=aM;
aO=aY-(aS[2].width);
switch(aL){case 0:aO=aS[0].width;
aJ=y({c1:[aT,aR],c2:[aT+aU,aR],c3:[aT+aU-aS[1].width,aR+aO],c4:[aT+aS[3].width,aR+aO]},aN[0],aN[1],aQ.topLeftOuter,aQ.topLeftInner,aQ.topRightOuter,aQ.topRightInner);
break;
case 1:aT=aW+aM-(aS[1].width);
aU=aS[1].width;
aJ=y({c1:[aT+aU,aR],c2:[aT+aU,aR+aO+aS[2].width],c3:[aT,aR+aO],c4:[aT,aR+aS[0].width]},aN[1],aN[2],aQ.topRightOuter,aQ.topRightInner,aQ.bottomRightOuter,aQ.bottomRightInner);
break;
case 2:aR=(aR+aY)-(aS[2].width);
aO=aS[2].width;
aJ=y({c1:[aT+aU,aR+aO],c2:[aT,aR+aO],c3:[aT+aS[3].width,aR],c4:[aT+aU-aS[3].width,aR]},aN[2],aN[3],aQ.bottomRightOuter,aQ.bottomRightInner,aQ.bottomLeftOuter,aQ.bottomLeftInner);
break;
case 3:aU=aS[3].width;
aJ=y({c1:[aT,aR+aO+aS[2].width],c2:[aT,aR],c3:[aT+aU,aR+aS[0].width],c4:[aT+aU,aR+aO]},aN[3],aN[0],aQ.bottomLeftOuter,aQ.bottomLeftInner,aQ.topLeftOuter,aQ.topLeftInner);
break
}aX.borders.push({args:aJ,color:aS[aL].color})
}}return aX
}function N(aJ,aL){var aK=aJ.drawShape();
aL.forEach(function(aN,aM){aK[(aM===0)?"moveTo":aN[0]+"To"].apply(null,aN.slice(1))
});
return aK
}function Y(aJ,aL,aK){if(aK!=="transparent"){aJ.setVariable("fillStyle",aK);
N(aJ,aL);
aJ.fill();
ac+=1
}}function X(aM,aN,aK){var aP=J.createElement("valuewrap"),aL=["lineHeight","textAlign","fontFamily","color","fontSize","paddingLeft","paddingTop","width","height","border","borderLeftWidth","borderTopWidth"],aJ,aO;
aL.forEach(function(aQ){try{aP.style[aQ]=p(aM,aQ)
}catch(aR){aG.log("html2canvas: Parse: Exception caught in renderFormValue: "+aR.message)
}});
aP.style.borderColor="black";
aP.style.borderStyle="solid";
aP.style.display="block";
aP.style.position="absolute";
if(/^(submit|reset|button|text|password)$/.test(aM.type)||aM.nodeName==="SELECT"){aP.style.lineHeight=p(aM,"height")
}aP.style.top=aN.top+"px";
aP.style.left=aN.left+"px";
aJ=(aM.nodeName==="SELECT")?(aM.options[aM.selectedIndex]||0).text:aM.value;
if(!aJ){aJ=aM.placeholder
}aO=J.createTextNode(aJ);
aP.appendChild(aO);
D.appendChild(aP);
L(aM,aO,aK);
D.removeChild(aP)
}function q(aJ){aJ.drawImage.apply(aJ,Array.prototype.slice.call(arguments,1));
ac+=1
}function af(aM,aO){var aK=h.getComputedStyle(aM,aO);
var aL=h.getComputedStyle(aM);
if(!aK||!aK.content||aK.content==="none"||aK.content==="-moz-alt-content"||aK.display==="none"||aL.content===aK.content){return
}var aN=aK.content+"";
if(aN[0]==="'"||aN[0]==='"'){aN=aN.replace(/(^['"])|(['"]$)/g,"")
}var aP=aN.substr(0,3)==="url",aJ=k.createElement(aP?"img":"span");
aJ.className=az+"-element ";
Object.keys(aK).filter(B).forEach(function(aR){try{aJ.style[aR]=aK[aR]
}catch(aQ){aG.log(["Tried to assign readonly property ",aR,"Error:",aQ])
}});
if(aP){aJ.src=aG.parseBackgroundImage(aN)[0].args[0]
}else{aJ.innerHTML=aN
}return aJ
}function B(aJ){return(isNaN(h.parseInt(aJ,10)))
}function m(aK,aN,aL,aM){var aJ=Math.round(aM.left+aL.left),aO=Math.round(aM.top+aL.top);
aK.createPattern(aN);
aK.translate(aJ,aO);
aK.fill();
aK.translate(-aJ,-aO)
}function ar(aR,aN,aL,aJ,aM,aP,aK,aQ){var aO=[];
aO.push(["line",Math.round(aM),Math.round(aP)]);
aO.push(["line",Math.round(aM+aK),Math.round(aP)]);
aO.push(["line",Math.round(aM+aK),Math.round(aQ+aP)]);
aO.push(["line",Math.round(aM),Math.round(aQ+aP)]);
N(aR,aO);
aR.save();
aR.clip();
m(aR,aN,aL,aJ);
aR.restore()
}function ab(aK,aL,aJ){ag(aK,aL.left,aL.top,aL.width,aL.height,aJ)
}function R(aN,aO,aK,aQ,aJ){var aL=aG.BackgroundSize(aN,aO,aQ,aJ),aM=aG.BackgroundPosition(aN,aO,aQ,aJ,aL),aP=aG.BackgroundRepeat(aN,aJ);
aQ=u(aQ,aL);
switch(aP){case"repeat-x":case"repeat no-repeat":ar(aK,aQ,aM,aO,aO.left,aO.top+aM.top,99999,aQ.height);
break;
case"repeat-y":case"no-repeat repeat":ar(aK,aQ,aM,aO,aO.left+aM.left,aO.top,aQ.width,99999);
break;
case"no-repeat":ar(aK,aQ,aM,aO,aO.left+aM.left,aO.top+aM.top,aQ.width,aQ.height);
break;
default:m(aK,aQ,aM,{top:aO.top,left:aO.left,width:aQ.width,height:aQ.height});
break
}}function v(aN,aO,aL){var aQ=p(aN,"backgroundImage"),aK=aG.parseBackgroundImage(aQ),aP,aJ=aK.length;
while(aJ--){aQ=aK[aJ];
if(!aQ.args||aQ.args.length===0){continue
}var aM=aQ.method==="url"?aQ.args[0]:aQ.value;
aP=T(aM);
if(aP){R(aN,aO,aL,aP,aJ)
}else{aG.log("html2canvas: Error loading background:",aQ)
}}}function u(aM,aL){if(aM.width===aL.width&&aM.height===aL.height){return aM
}var aJ,aK=J.createElement("canvas");
aK.width=aL.width;
aK.height=aL.height;
aJ=aK.getContext("2d");
q(aJ,aM,0,0,aM.width,aM.height,0,0,aL.width,aL.height);
return aK
}function G(aK,aL,aJ){return aK.setVariable("globalAlpha",p(aL,"opacity")*((aJ)?aJ.opacity:1))
}function aa(aJ){return aJ.replace("px","")
}function ai(aP,aJ){var aO=/(matrix)\((.+)\)/;
var aN=p(aP,"transform")||p(aP,"-webkit-transform")||p(aP,"-moz-transform")||p(aP,"-ms-transform")||p(aP,"-o-transform");
var aM=p(aP,"transform-origin")||p(aP,"-webkit-transform-origin")||p(aP,"-moz-transform-origin")||p(aP,"-ms-transform-origin")||p(aP,"-o-transform-origin")||"0px 0px";
aM=aM.split(" ").map(aa).map(aG.asFloat);
var aK;
if(aN&&aN!=="none"){var aL=aN.match(aO);
if(aL){switch(aL[1]){case"matrix":aK=aL[2].split(",").map(aG.trimText).map(aG.asFloat);
break
}}}return{origin:aM,matrix:aK}
}function an(aN,aK,aO,aM){var aL=e((!aK)?P():aO.width,(!aK)?aC():aO.height),aJ={ctx:aL,opacity:G(aL,aN,aK),cssPosition:p(aN,"position"),borders:V(aN),transform:aM,clip:(aK&&aK.clip)?aG.Extend({},aK.clip):null};
U(aN,aJ,aK);
if(Z.useOverflow===true&&/(hidden|scroll|auto)/.test(p(aN,"overflow"))===true&&/(BODY)/i.test(aN.nodeName)===false){aJ.clip=(aJ.clip)?x(aJ.clip,aO):aO
}return aJ
}function au(aM,aK,aJ){var aL={left:aK.left+aM[3].width,top:aK.top+aM[0].width,width:aK.width-(aM[1].width+aM[3].width),height:aK.height-(aM[0].width+aM[2].width)};
if(aJ){aL=x(aL,aJ)
}return aL
}function ao(aK,aJ){var aL=(aJ.matrix)?aG.OffsetBounds(aK):aG.Bounds(aK);
aJ.origin[0]+=aL.left;
aJ.origin[1]+=aL.top;
return aL
}function aA(aN,aO,aQ){var aK=ai(aN,aO),aJ=ao(aN,aK),aM,aR=an(aN,aO,aJ,aK),aP=aR.borders,aU=aR.ctx,aL=au(aP,aJ,aR.clip),aT=ak(aN,aJ,aP),aS=(av.test(aN.nodeName))?"#efefef":p(aN,"backgroundColor");
N(aU,aT.clip);
aU.save();
aU.clip();
if(aL.height>0&&aL.width>0&&!aQ){ab(aU,aJ,aS);
v(aN,aL,aU)
}else{if(aQ){aR.backgroundColor=aS
}}aU.restore();
aT.borders.forEach(function(aV){Y(aU,aV.args,aV.color)
});
switch(aN.nodeName){case"IMG":if((aM=T(aN.getAttribute("src")))){A(aU,aN,aM,aJ,aP)
}else{aG.log("html2canvas: Error loading <img>:"+aN.getAttribute("src"))
}break;
case"INPUT":if(/^(text|url|email|submit|button|reset)$/.test(aN.type)&&(aN.value||aN.placeholder||"").length>0){X(aN,aJ,aR)
}break;
case"TEXTAREA":if((aN.value||aN.placeholder||"").length>0){X(aN,aJ,aR)
}break;
case"SELECT":if((aN.options||aN.placeholder||"").length>0){X(aN,aJ,aR)
}break;
case"LI":M(aN,aR,aL);
break;
case"CANVAS":A(aU,aN,aN,aJ,aP);
break
}return aR
}function o(aJ){return(p(aJ,"display")!=="none"&&p(aJ,"visibility")!=="hidden"&&!aJ.hasAttribute("data-html2canvas-ignore"))
}function t(aL,aK,aJ){if(!aJ){aJ=function(){}
}if(o(aL)){aK=aA(aL,aK,false)||aK;
if(!av.test(aL.nodeName)){return S(aL,aK,aJ)
}}aJ()
}function S(aO,aL,aK){var aN=aG.Children(aO);
var aJ=aN.length+1;
aP();
if(Z.async){aN.forEach(function(aQ){setTimeout(function(){aM(aQ)
},0)
})
}else{aN.forEach(aM)
}function aM(aQ){if(aQ.nodeType===aQ.ELEMENT_NODE){t(aQ,aL,aP)
}else{if(aQ.nodeType===aQ.TEXT_NODE){L(aO,aQ,aL);
aP()
}else{aP()
}}}function aP(aQ){if(--aJ<=0){aG.log("finished rendering "+aN.length+" children.");
aK()
}}}};
j.Preload=function(p){var y={numLoaded:0,numFailed:0,numTotal:0,cleanupDone:false},G,r=j.Util,H,F,s=0,n=p.elements[0]||k.body,J=n.ownerDocument,x=n.getElementsByTagName("img"),z=x.length,t=J.createElement("a"),w=(function(K){return(K.crossOrigin!==d)
})(new Image()),B;
t.href=h.location.href;
G=t.protocol+t.host;
function I(L){t.href=L;
t.href=t.href;
var K=t.protocol+t.host;
return(K===G)
}function q(){r.log("html2canvas: start: images: "+y.numLoaded+" / "+y.numTotal+" (failed: "+y.numFailed+")");
if(!y.firstRun&&y.numLoaded>=y.numTotal){r.log("Finished loading images: # "+y.numTotal+" (failed: "+y.numFailed+")");
if(typeof p.complete==="function"){p.complete(y)
}}}function A(N,M,O){var K,P=p.proxy,L;
t.href=N;
N=t.href;
K="html2canvas_"+(s++);
O.callbackname=K;
if(P.indexOf("?")>-1){P+="&"
}else{P+="?"
}P+="url="+encodeURIComponent(N)+"&callback="+K;
L=J.createElement("script");
h[K]=function(Q){if(Q.substring(0,6)==="error:"){O.succeeded=false;
y.numLoaded++;
y.numFailed++;
q()
}else{E(M,O);
M.src=Q
}h[K]=d;
try{delete h[K]
}catch(R){}L.parentNode.removeChild(L);
L=null;
delete O.script;
delete O.callbackname
};
L.setAttribute("type","text/javascript");
L.setAttribute("src",P);
O.script=L;
h.document.body.appendChild(L)
}function v(K,M){var L=h.getComputedStyle(K,M),N=L.content;
if(N.substr(0,3)==="url"){H.loadImage(j.Util.parseBackgroundImage(N)[0].args[0])
}D(L.backgroundImage,K)
}function C(K){v(K,":before");
v(K,":after")
}function o(M,L){var K=j.Generate.Gradient(M,L);
if(K!==d){y[M]={img:K,succeeded:true};
y.numTotal++;
y.numLoaded++;
q()
}}function m(K){return(K&&K.method&&K.args&&K.args.length>0)
}function D(L,K){var M;
j.Util.parseBackgroundImage(L).filter(m).forEach(function(N){if(N.method==="url"){H.loadImage(N.args[0])
}else{if(N.method.match(/\-?gradient$/)){if(M===d){M=j.Util.Bounds(K)
}o(N.value,M)
}}})
}function u(M){var K=false;
try{r.Children(M).forEach(u)
}catch(N){}try{K=M.nodeType
}catch(L){K=false;
r.log("html2canvas: failed to access some element's nodeType - Exception: "+L.message)
}if(K===1||K===d){C(M);
try{D(r.getCSS(M,"backgroundImage"),M)
}catch(N){r.log("html2canvas: failed to get background-image - Exception: "+N.message)
}D(M)
}}function E(K,L){K.onload=function(){if(L.timer!==d){h.clearTimeout(L.timer)
}y.numLoaded++;
L.succeeded=true;
K.onerror=K.onload=null;
q()
};
K.onerror=function(){if(K.crossOrigin==="anonymous"){h.clearTimeout(L.timer);
if(p.proxy){var M=K.src;
K=new Image();
L.img=K;
K.src=M;
A(K.src,K,L);
return
}}y.numLoaded++;
y.numFailed++;
L.succeeded=false;
K.onerror=K.onload=null;
q()
}
}H={loadImage:function(M){var K,L;
if(M&&y[M]===d){K=new Image();
if(M.match(/data:image\/.*;base64,/i)){K.src=M.replace(/url\(['"]{0,}|['"]{0,}\)$/ig,"");
L=y[M]={img:K};
y.numTotal++;
E(K,L)
}else{if(I(M)||p.allowTaint===true){L=y[M]={img:K};
y.numTotal++;
E(K,L);
K.src=M
}else{if(w&&!p.allowTaint&&p.useCORS){K.crossOrigin="anonymous";
L=y[M]={img:K};
y.numTotal++;
E(K,L);
K.src=M
}else{if(p.proxy){L=y[M]={img:K};
y.numTotal++;
A(M,K,L)
}}}}}},cleanupDOM:function(M){var K,N;
if(!y.cleanupDone){if(M&&typeof M==="string"){r.log("html2canvas: Cleanup because: "+M)
}else{r.log("html2canvas: Cleanup after timeout: "+p.timeout+" ms.")
}for(N in y){if(y.hasOwnProperty(N)){K=y[N];
if(typeof K==="object"&&K.callbackname&&K.succeeded===d){h[K.callbackname]=d;
try{delete h[K.callbackname]
}catch(L){}if(K.script&&K.script.parentNode){K.script.setAttribute("src","about:blank");
K.script.parentNode.removeChild(K.script)
}y.numLoaded++;
y.numFailed++;
r.log("html2canvas: Cleaned up failed img: '"+N+"' Steps: "+y.numLoaded+" / "+y.numTotal)
}}}if(h.stop!==d){h.stop()
}else{if(k.execCommand!==d){k.execCommand("Stop",false)
}}if(k.close!==d){k.close()
}y.cleanupDone=true;
if(!(M&&typeof M==="string")){q()
}}},renderingDone:function(){if(B){h.clearTimeout(B)
}}};
if(p.timeout>0){B=h.setTimeout(H.cleanupDOM,p.timeout)
}r.log("html2canvas: Preload starts: finding background-images");
y.firstRun=true;
u(n);
r.log("html2canvas: Preload: Finding images");
for(F=0;
F<z;
F+=1){H.loadImage(x[F].getAttribute("src"))
}y.firstRun=false;
r.log("html2canvas: Preload: Done.");
if(y.numTotal===y.numLoaded){q()
}return H
};
j.Renderer=function(p,o){function q(s,r){if(s==="children"){return -1
}else{if(r==="children"){return 1
}else{return s-r
}}}function m(u){var s=[],r;
r=(function t(x){var w={};
function y(z,B,F){var H=(B.zIndex.zindex==="auto")?0:Number(B.zIndex.zindex),D=z,G=B.zIndex.isPositioned,C=B.zIndex.isFloated,A={node:B},E=F;
if(B.zIndex.ownStacking){D=A.context={children:[{node:B,children:[]}]};
E=d
}else{if(G||C){E=A.children=[]
}}if(H===0&&F){F.push(A)
}else{if(!z[H]){z[H]=[]
}z[H].push(A)
}B.zIndex.children.forEach(function(I){y(D,I,E)
})
}y(w,x);
return w
})(u);
function v(w){Object.keys(w).sort(q).forEach(function(A){var y=[],C=[],z=[],B=[];
w[A].forEach(function(D){if(D.node.zIndex.isPositioned||D.node.zIndex.opacity<1){z.push(D)
}else{if(D.node.zIndex.isFloated){C.push(D)
}else{y.push(D)
}}});
(function x(D){D.forEach(function(E){B.push(E);
if(E.children){x(E.children)
}})
})(y.concat(C,z));
B.forEach(function(D){if(D.context){v(D.context)
}else{s.push(D.node)
}})
})
}v(r);
return s
}function n(r){var s;
if(typeof o.renderer==="string"&&j.Renderer[r]!==d){s=j.Renderer[r](o)
}else{if(typeof r==="function"){s=r(o)
}else{throw new Error("Unknown renderer")
}}if(typeof s!=="function"){throw new Error("Invalid renderer defined")
}return s
}return n(o.renderer)(p,o,k,m(p.stack),j)
};
j.Util.Support=function(n,p){function m(){var r=new Image(),s=p.createElement("canvas"),q=(s.getContext===d)?false:s.getContext("2d");
if(q===false){return false
}s.width=s.height=10;
r.src=["data:image/svg+xml,","<svg xmlns='http://www.w3.org/2000/svg' width='10' height='10'>","<foreignObject width='10' height='10'>","<div xmlns='http://www.w3.org/1999/xhtml' style='width:10;height:10;'>","sup","</div>","</foreignObject>","</svg>"].join("");
try{q.drawImage(r,0,0);
s.toDataURL()
}catch(t){return false
}j.Util.log("html2canvas: Parse: SVG powered rendering available");
return true
}function o(){var t,v,u,q,s=false;
if(p.createRange){t=p.createRange();
if(t.getBoundingClientRect){v=p.createElement("boundtest");
v.style.height="123px";
v.style.display="block";
p.body.appendChild(v);
t.selectNode(v);
u=t.getBoundingClientRect();
q=u.height;
if(q===123){s=true
}p.body.removeChild(v)
}}return s
}return{rangeBounds:o(),svgRendering:n.svgRendering&&m()}
};
h.html2canvas=function(q,p){q=(q.length)?q:[q];
var m,o,n={logging:false,elements:q,background:"#fff",proxy:null,timeout:0,useCORS:false,allowTaint:false,svgRendering:false,ignoreElements:"IFRAME|OBJECT|PARAM",useOverflow:true,letterRendering:false,chinese:false,async:false,width:null,height:null,taintTest:true,renderer:"Canvas"};
n=j.Util.Extend(p,n);
j.logging=n.logging;
n.complete=function(r){if(typeof n.onpreloaded==="function"){if(n.onpreloaded(r)===false){return
}}j.Parse(r,n,function(s){if(typeof n.onparsed==="function"){if(n.onparsed(s)===false){return
}}o=j.Renderer(s,n);
if(typeof n.onrendered==="function"){n.onrendered(o)
}})
};
h.setTimeout(function(){j.Preload(n)
},0);
return{render:function(r,s){return j.Renderer(r,j.Util.Extend(s,n))
},parse:function(r,s){return j.Parse(r,j.Util.Extend(s,n))
},preload:function(r){return j.Preload(j.Util.Extend(r,n))
},log:j.Util.log}
};
h.html2canvas.log=j.Util.log;
h.html2canvas.Renderer={Canvas:d};
j.Renderer.Canvas=function(v){v=v||{};
var t=k,p=[],u=k.createElement("canvas"),q=u.getContext("2d"),s=j.Util,m=v.canvas||t.createElement("canvas");
function o(w,x){w.beginPath();
x.forEach(function(y){w[y.name].apply(w,y["arguments"])
});
w.closePath()
}function r(w){if(p.indexOf(w["arguments"][0].src)===-1){q.drawImage(w["arguments"][0],0,0);
try{q.getImageData(0,0,1,1)
}catch(x){u=t.createElement("canvas");
q=u.getContext("2d");
return false
}p.push(w["arguments"][0].src)
}return true
}function n(w,x){switch(x.type){case"variable":w[x.name]=x["arguments"];
break;
case"function":switch(x.name){case"createPattern":if(x["arguments"][0].width>0&&x["arguments"][0].height>0){try{w.fillStyle=w.createPattern(x["arguments"][0],"repeat")
}catch(y){s.log("html2canvas: Renderer: Error creating pattern",y.message)
}}break;
case"drawShape":o(w,x["arguments"]);
break;
case"drawImage":if(x["arguments"][8]>0&&x["arguments"][7]>0){if(!v.taintTest||(v.taintTest&&r(x))){w.drawImage.apply(w,x["arguments"])
}}break;
default:w[x.name].apply(w,x["arguments"])
}break
}}return function(D,F,B,z,A){var E=m.getContext("2d"),x,w,y,C=D.stack;
m.width=m.style.width=F.width||C.ctx.width;
m.height=m.style.height=F.height||C.ctx.height;
y=E.fillStyle;
E.fillStyle=(s.isTransparent(D.backgroundColor)&&F.background!==d)?F.background:D.backgroundColor;
E.fillRect(0,0,m.width,m.height);
E.fillStyle=y;
z.forEach(function(G){E.textBaseline="bottom";
E.save();
if(G.transform.matrix){E.translate(G.transform.origin[0],G.transform.origin[1]);
E.transform.apply(E,G.transform.matrix);
E.translate(-G.transform.origin[0],-G.transform.origin[1])
}if(G.clip){E.beginPath();
E.rect(G.clip.left,G.clip.top,G.clip.width,G.clip.height);
E.clip()
}if(G.ctx.storage){G.ctx.storage.forEach(function(H){n(E,H)
})
}E.restore()
});
s.log("html2canvas: Renderer: Canvas renderer done - returning canvas obj");
if(F.elements.length===1){if(typeof F.elements[0]==="object"&&F.elements[0].nodeName!=="BODY"){w=A.Util.Bounds(F.elements[0]);
x=B.createElement("canvas");
x.width=Math.ceil(w.width);
x.height=Math.ceil(w.height);
E=x.getContext("2d");
E.drawImage(m,w.left,w.top,w.width,w.height,0,0,w.width,w.height);
m=null;
return x
}}return m
}
}
})(window,document);