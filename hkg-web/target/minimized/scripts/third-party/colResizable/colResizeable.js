(function(f){var y=f(document);
var v=f("head");
var u=null;
var m=[];
var i=0;
var n="id";
var l="px";
var j="JColResizer";
var b="JCLRFlex";
var o=parseInt;
var k=Math;
var p=navigator.userAgent.indexOf("Trident/4.0")>0;
var g;
try{g=sessionStorage
}catch(x){}v.append("<style type='text/css'>  .JColResizer{table-layout:fixed;} .JColResizer td, .JColResizer th{overflow:hidden;padding-left:0!important; padding-right:0!important;}  .JCLRgrips{ height:0px; position:relative;} .JCLRgrip{margin-left:-5px; position:absolute; z-index:5; } .JCLRgrip .JColResizer{position:absolute;background-color:red;filter:alpha(opacity=1);opacity:0;width:10px;height:100%;cursor: e-resize;top:0px} .JCLRLastGrip{position:absolute; width:1px; } .JCLRgripDrag{ border-left:1px dotted black;	}</style>");
var t=function(d,e){var h=f(d);
h.opt=e;
if(h.opt.disable){return C(h)
}var D=h.id=h.attr(n)||j+i++;
h.p=h.opt.postbackSafe;
if(!h.is("table")||m[D]&&!h.opt.partialRefresh){return
}h.addClass(j).attr(n,D).before('<div class="JCLRgrips"/>');
h.g=[];
h.c=[];
h.w=h.width();
h.gc=h.prev();
h.f=h.opt.fixed;
if(e.marginLeft){h.gc.css("marginLeft",e.marginLeft)
}if(e.marginRight){h.gc.css("marginRight",e.marginRight)
}h.cs=o(p?d.cellSpacing||d.currentStyle.borderSpacing:h.css("border-spacing"))||2;
h.b=o(p?d.border||d.currentStyle.borderLeftWidth:h.css("border-left-width"))||1;
m[D]=h;
q(h)
};
var C=function(d){var e=d.attr(n),d=m[e];
if(!d||!d.is("table")){return
}d.removeClass(j+" "+b).gc.remove();
delete m[e]
};
var q=function(d){var e=d.find(">thead>tr>th,>thead>tr>td");
if(!e.length){e=d.find(">tbody>tr:first>th,>tr:first>th,>tbody>tr:first>td, >tr:first>td")
}e=e.filter(":visible");
d.cg=d.find("col");
d.ln=e.length;
if(d.p&&g&&g[d.id]){a(d,e)
}e.each(function(h){var E=f(this);
var D=f(d.gc.append('<div class="JCLRgrip"></div>')[0].lastChild);
D.append(d.opt.gripInnerHtml).append('<div class="'+j+'"></div>');
if(h==d.ln-1){D.addClass("JCLRLastGrip");
if(d.f){D.html("")
}}D.bind("touchstart mousedown",z);
D.t=d;
D.i=h;
D.c=E;
E.w=E.width();
d.g.push(D);
d.c.push(E);
E.width(E.w).removeAttr("width");
D.data(j,{i:h,t:d.attr(n),last:h==d.ln-1})
});
d.cg.removeAttr("width");
s(d);
d.find("td, th").not(e).not("table th, table td").each(function(){f(this).removeAttr("width")
});
if(!d.f){d.removeAttr("width").addClass(b)
}};
var a=function(F,G){var h,e=0,E=0,d=[],D;
if(G){F.cg.removeAttr("width");
if(F.opt.flush){g[F.id]="";
return
}h=g[F.id].split(";");
D=h[F.ln+1];
if(!F.f&&D){F.width(D)
}for(;
E<F.ln;
E++){d.push(100*h[E]/h[F.ln]+"%");
G.eq(E).css("width",d[E])
}for(E=0;
E<F.ln;
E++){F.cg.eq(E).css("width",d[E])
}}else{g[F.id]="";
for(;
E<F.c.length;
E++){h=F.c[E].width();
g[F.id]+=h+";";
e+=h
}g[F.id]+=e;
if(!F.f){g[F.id]+=";"+F.width()
}}};
var s=function(e){e.gc.width(e.w);
for(var d=0;
d<e.ln;
d++){var h=e.c[d];
e.g[d].css({left:h.offset().left-e.offset().left+h.outerWidth(false)+e.cs/2+l,height:e.opt.headerOnly?e.c[0].outerHeight(false):e.outerHeight(false)})
}};
var B=function(E,D,G){var F=u.x-u.l,H=E.c[D],h=E.c[D+1];
var e=H.w+F;
var d=h.w-F;
H.width(e+l);
E.cg.eq(D).width(e+l);
if(E.f){h.width(d+l);
E.cg.eq(D+1).width(d+l)
}if(G){H.w=e;
h.w=E.f?d:h.w
}};
var w=function(e){var d=f.map(e.c,function(h){return h.width()
});
e.width(e.width()).removeClass(b);
f.each(e.c,function(h,D){D.width(d[h]).w=d[h]
});
e.addClass(b)
};
var A=function(G){if(!u){return
}var M=u.t;
var N=G.originalEvent.touches;
var d=N?N[0].pageX:G.pageX;
var J=d-u.ox+u.l;
var L=M.opt.minWidth,F=u.i;
var D=M.cs*1.5+L+M.b;
var K=F==M.ln-1;
var E=F?M.g[F-1].position().left+M.cs+L:D;
var I=M.f?F==M.ln-1?M.w-D:M.g[F+1].position().left-M.cs-L:Infinity;
J=k.max(E,k.min(I,J));
u.x=J;
u.css("left",J+l);
if(K){var H=M.c[u.i];
u.w=H.w+J-u.l
}if(M.opt.liveDrag){if(K){H.width(u.w);
M.w=M.width()
}else{B(M,F)
}s(M);
var h=M.opt.onDrag;
if(h){G.currentTarget=M[0];
h(G)
}}return false
};
var r=function(F){y.unbind("touchend."+j+" mouseup."+j).unbind("touchmove."+j+" mousemove."+j);
f("head :last-child").remove();
if(!u){return
}u.removeClass(u.t.opt.draggingClass);
var D=u.t;
var d=D.opt.onResize;
var h=u.i;
var E=h==D.ln-1;
var G=D.g[h].c;
if(E){G.width(u.w);
G.w=u.w
}else{B(D,h,true)
}if(!D.f){w(D)
}s(D);
if(d){F.currentTarget=D[0];
d(F)
}if(D.p&&g){a(D)
}u=null
};
var z=function(E){var F=f(this).data(j);
var h=m[F.t],D=h.g[F.i];
var H=E.originalEvent.touches;
D.ox=H?H[0].pageX:E.pageX;
D.l=D.position().left;
y.bind("touchmove."+j+" mousemove."+j,A).bind("touchend."+j+" mouseup."+j,r);
v.append("<style type='text/css'>*{cursor:"+h.opt.dragCursor+"!important}</style>");
D.addClass(h.opt.draggingClass);
u=D;
if(h.c[F.i].l){for(var d=0,G;
d<h.ln;
d++){G=h.c[d];
G.l=false;
G.w=G.width()
}}return false
};
var c=function(){for(e in m){var e=m[e],d,h=0;
e.removeClass(j);
if(e.f&&e.w!=e.width()){e.w=e.width();
for(d=0;
d<e.ln;
d++){h+=e.c[d].w
}for(d=0;
d<e.ln;
d++){e.c[d].css("width",k.round(1000*e.c[d].w/h)/10+"%").l=true
}}s(e.addClass(j))
}};
f(window).bind("resize."+j,c);
f.fn.extend({colResizable:function(d){var e={draggingClass:"JCLRgripDrag",gripInnerHtml:"",liveDrag:false,fixed:true,minWidth:15,headerOnly:false,hoverCursor:"e-resize",dragCursor:"e-resize",postbackSafe:false,flush:false,marginLeft:null,marginRight:null,disable:false,partialRefresh:true,onDrag:null,onResize:null};
var d=f.extend(e,d);
return this.each(function(){t(this,d)
})
}})
})(jQuery);