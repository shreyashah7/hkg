(function(){(function(a){if(typeof define==="function"&&define.amd){return define(["jquery"],a)
}else{return a(window.jQuery)
}})(function(f){var j,l,a,i,e,d,g,k,b,h,c;
h="caret";
j=(function(){function m(n){this.$inputor=n;
this.domInputor=this.$inputor[0]
}m.prototype.setPos=function(n){return this.domInputor
};
m.prototype.getIEPosition=function(){return this.getPosition()
};
m.prototype.getPosition=function(){var n,o;
o=this.getOffset();
n=this.$inputor.offset();
o.left-=n.left;
o.top-=n.top;
return o
};
m.prototype.getOldIEPos=function(){var o,n;
n=g.selection.createRange();
o=g.body.createTextRange();
o.moveToElementText(this.domInputor);
o.setEndPoint("EndToEnd",n);
return o.text.length
};
m.prototype.getPos=function(){var n,p,o;
if(o=this.range()){n=o.cloneRange();
n.selectNodeContents(this.domInputor);
n.setEnd(o.endContainer,o.endOffset);
p=n.toString().length;
n.detach();
return p
}else{if(g.selection){return this.getOldIEPos()
}}};
m.prototype.getOldIEOffset=function(){var n,o;
n=g.selection.createRange().duplicate();
n.moveStart("character",-1);
o=n.getBoundingClientRect();
return{height:o.bottom-o.top,left:o.left,top:o.top}
};
m.prototype.getOffset=function(s){var n,r,o,q,p;
if(b.getSelection&&(o=this.range())){if(o.endOffset-1>0&&o.endContainer===!this.domInputor){n=o.cloneRange();
n.setStart(o.endContainer,o.endOffset-1);
n.setEnd(o.endContainer,o.endOffset);
q=n.getBoundingClientRect();
r={height:q.height,left:q.left+q.width,top:q.top};
n.detach()
}if(!r||(r!=null?r.height:void 0)===0){n=o.cloneRange();
p=f(g.createTextNode("|"));
n.insertNode(p[0]);
n.selectNode(p[0]);
q=n.getBoundingClientRect();
r={height:q.height,left:q.left,top:q.top};
p.remove();
n.detach()
}}else{if(g.selection){r=this.getOldIEOffset()
}}if(r){r.top+=f(b).scrollTop();
r.left+=f(b).scrollLeft()
}return r
};
m.prototype.range=function(){var n;
if(!b.getSelection){return
}n=b.getSelection();
if(n.rangeCount>0){return n.getRangeAt(0)
}else{return null
}};
return m
})();
l=(function(){function m(n){this.$inputor=n;
this.domInputor=this.$inputor[0]
}m.prototype.getIEPos=function(){var q,s,o,r,t,p,n;
s=this.domInputor;
p=g.selection.createRange();
t=0;
if(p&&p.parentElement()===s){r=s.value.replace(/\r\n/g,"\n");
o=r.length;
n=s.createTextRange();
n.moveToBookmark(p.getBookmark());
q=s.createTextRange();
q.collapse(false);
if(n.compareEndPoints("StartToEnd",q)>-1){t=o
}else{t=-n.moveStart("character",-o)
}}return t
};
m.prototype.getPos=function(){if(g.selection){return this.getIEPos()
}else{return this.domInputor.selectionStart
}};
m.prototype.setPos=function(p){var o,n;
o=this.domInputor;
if(g.selection){n=o.createTextRange();
n.move("character",p);
n.select()
}else{if(o.setSelectionRange){o.setSelectionRange(p,p)
}}return o
};
m.prototype.getIEOffset=function(r){var o,p,n,q;
p=this.domInputor.createTextRange();
r||(r=this.getPos());
p.move("character",r);
n=p.boundingLeft;
q=p.boundingTop;
o=p.boundingHeight;
return{left:n,top:q,height:o}
};
m.prototype.getOffset=function(q){var o,p,n;
o=this.$inputor;
if(g.selection){p=this.getIEOffset(q);
p.top+=f(b).scrollTop()+o.scrollTop();
p.left+=f(b).scrollLeft()+o.scrollLeft();
return p
}else{p=o.offset();
n=this.getPosition(q);
return p={left:p.left+n.left-o.scrollLeft(),top:p.top+n.top-o.scrollTop(),height:n.height}
}};
m.prototype.getPosition=function(u){var s,t,o,r,p,q,n;
s=this.$inputor;
r=function(v){return f("<div></div>").text(v).html().replace(/\r\n|\r|\n/g,"<br/>").replace(/\s/g,"&nbsp;")
};
if(u===void 0){u=this.getPos()
}n=s.val().slice(0,u);
o=s.val().slice(u);
p="<span style='position: relative; display: inline;'>"+r(n)+"</span>";
p+="<span id='caret' style='position: relative; display: inline;'>|</span>";
p+="<span style='position: relative; display: inline;'>"+r(o)+"</span>";
q=new a(s);
return t=q.create(p).rect()
};
m.prototype.getIEPosition=function(s){var o,r,p,n,q;
p=this.getIEOffset(s);
r=this.$inputor.offset();
n=p.left-r.left;
q=p.top-r.top;
o=p.height;
return{left:n,top:q,height:o}
};
return m
})();
a=(function(){m.prototype.css_attr=["borderBottomWidth","borderLeftWidth","borderRightWidth","borderTopStyle","borderRightStyle","borderBottomStyle","borderLeftStyle","borderTopWidth","boxSizing","fontFamily","fontSize","fontWeight","height","letterSpacing","lineHeight","marginBottom","marginLeft","marginRight","marginTop","outlineWidth","overflow","overflowX","overflowY","paddingBottom","paddingLeft","paddingRight","paddingTop","textAlign","textOverflow","textTransform","whiteSpace","wordBreak","wordWrap"];
function m(n){this.$inputor=n
}m.prototype.mirrorCss=function(){var n,o=this;
n={position:"absolute",left:-9999,top:0,zIndex:-20000};
if(this.$inputor.prop("tagName")==="TEXTAREA"){this.css_attr.push("width")
}f.each(this.css_attr,function(q,r){return n[r]=o.$inputor.css(r)
});
return n
};
m.prototype.create=function(n){this.$mirror=f("<div></div>");
this.$mirror.css(this.mirrorCss());
this.$mirror.html(n);
this.$inputor.after(this.$mirror);
return this
};
m.prototype.rect=function(){var o,p,n;
o=this.$mirror.find("#caret");
p=o.position();
n={left:p.left,top:p.top,height:o.height()};
this.$mirror.remove();
return n
};
return m
})();
i={contentEditable:function(m){return !!(m[0].contentEditable&&m[0].contentEditable==="true")
}};
d={pos:function(m){if(m||m===0){return this.setPos(m)
}else{return this.getPos()
}},position:function(m){if(g.selection){return this.getIEPosition(m)
}else{return this.getPosition(m)
}},offset:function(n){var m;
m=this.getOffset(n);
return m
}};
g=null;
b=null;
k=null;
c=function(n){var m;
if(m=n!=null?n.iframe:void 0){k=m;
b=m.contentWindow;
return g=m.contentDocument||b.document
}else{k=void 0;
b=window;
return g=document
}};
e=function(m){var n;
g=m[0].ownerDocument;
b=g.defaultView||g.parentWindow;
try{return k=b.frameElement
}catch(o){n=o
}};
f.fn.caret=function(p,n,m){var o;
if(d[p]){if(f.isPlainObject(n)){c(n);
n=void 0
}else{c(m)
}o=i.contentEditable(this)?new j(this):new l(this);
return d[p].apply(o,[n])
}else{return f.error("Method "+p+" does not exist on jQuery.caret")
}};
f.fn.caret.EditableCaret=j;
f.fn.caret.InputCaret=l;
f.fn.caret.Utils=i;
return f.fn.caret.apis=d
})
}).call(this);