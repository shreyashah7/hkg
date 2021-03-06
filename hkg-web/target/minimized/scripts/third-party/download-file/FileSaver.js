/* FileSaver.js
 *  A saveAs() FileSaver implementation.
 *  2014-01-24
 *
 *  By Eli Grey, http://eligrey.com
 *  License: X11/MIT
 *    See LICENSE.md
 */
/* @source http://purl.eligrey.com/github/FileSaver.js/blob/master/FileSaver.js */
var saveAs=saveAs||(typeof navigator!=="undefined"&&navigator.msSaveOrOpenBlob&&navigator.msSaveOrOpenBlob.bind(navigator))||(function(h){if(typeof navigator!=="undefined"&&/MSIE [1-9]\./.test(navigator.userAgent)){return
}var r=h.document,l=function(){return h.URL||h.webkitURL||h
},e=h.URL||h.webkitURL||h,n=r.createElementNS("http://www.w3.org/1999/xhtml","a"),g=!h.externalHost&&"download" in n,j=function(t){var s=r.createEvent("MouseEvents");
s.initMouseEvent("click",true,false,h,0,0,0,0,0,false,false,false,false,0,null);
t.dispatchEvent(s)
},o=h.webkitRequestFileSystem,p=h.requestFileSystem||o||h.mozRequestFileSystem,m=function(s){(h.setImmediate||h.setTimeout)(function(){throw s
},0)
},c="application/octet-stream",k=0,b=[],i=function(){var t=b.length;
while(t--){var s=b[t];
if(typeof s==="string"){e.revokeObjectURL(s)
}else{s.remove()
}}b.length=0
},q=function(t,s,w){s=[].concat(s);
var v=s.length;
while(v--){var x=t["on"+s[v]];
if(typeof x==="function"){try{x.call(t,w||t)
}catch(u){m(u)
}}}},f=function(t,v){var w=this,C=t.type,F=false,y,x,s=function(){var G=l().createObjectURL(t);
b.push(G);
return G
},B=function(){q(w,"writestart progress write writeend".split(" "))
},E=function(){if(F||!y){y=s(t)
}if(x){x.location.href=y
}else{window.open(y,"_blank")
}w.readyState=w.DONE;
B()
},A=function(G){return function(){if(w.readyState!==w.DONE){return G.apply(this,arguments)
}}
},z={create:true,exclusive:false},D;
w.readyState=w.INIT;
if(!v){v="download"
}if(g){y=s(t);
r=h.document;
n=r.createElementNS("http://www.w3.org/1999/xhtml","a");
n.href=y;
n.download=v;
var u=r.createEvent("MouseEvents");
u.initMouseEvent("click",true,false,h,0,0,0,0,0,false,false,false,false,0,null);
n.dispatchEvent(u);
w.readyState=w.DONE;
B();
return
}if(h.chrome&&C&&C!==c){D=t.slice||t.webkitSlice;
t=D.call(t,0,t.size,c);
F=true
}if(o&&v!=="download"){v+=".download"
}if(C===c||o){x=h
}if(!p){E();
return
}k+=t.size;
p(h.TEMPORARY,k,A(function(G){G.root.getDirectory("saved",z,A(function(H){var I=function(){H.getFile(v,z,A(function(J){J.createWriter(A(function(K){K.onwriteend=function(L){x.location.href=J.toURL();
b.push(J);
w.readyState=w.DONE;
q(w,"writeend",L)
};
K.onerror=function(){var L=K.error;
if(L.code!==L.ABORT_ERR){E()
}};
"writestart progress write abort".split(" ").forEach(function(L){K["on"+L]=w["on"+L]
});
K.write(t);
w.abort=function(){K.abort();
w.readyState=w.DONE
};
w.readyState=w.WRITING
}),E)
}),E)
};
H.getFile(v,{create:false},A(function(J){J.remove();
I()
}),A(function(J){if(J.code===J.NOT_FOUND_ERR){I()
}else{E()
}}))
}),E)
}),E)
},d=f.prototype,a=function(s,t){return new f(s,t)
};
d.abort=function(){var s=this;
s.readyState=s.DONE;
q(s,"abort")
};
d.readyState=d.INIT=0;
d.WRITING=1;
d.DONE=2;
d.error=d.onwritestart=d.onprogress=d.onwrite=d.onabort=d.onerror=d.onwriteend=null;
h.addEventListener("unload",i,false);
a.unload=function(){i();
h.removeEventListener("unload",i,false)
};
return a
}(typeof self!=="undefined"&&self||typeof window!=="undefined"&&window||this.content));
if(typeof module!=="undefined"){module.exports=saveAs
};