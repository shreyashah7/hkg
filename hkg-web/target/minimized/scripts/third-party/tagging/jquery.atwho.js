/* jquery.atwho - v0.5.2 %>
 * Copyright (c) 2014 chord.luo <chord.luo@gmail.com>;
 * homepage: http://ichord.github.com/At.js
 * Licensed MIT
 */
(function(a,b){if(typeof define==="function"&&define.amd){define(["jquery"],function(c){return(a.returnExportsGlobal=b(c))
})
}else{if(typeof exports==="object"){module.exports=b(require("jquery"))
}else{b(jQuery)
}}}(this,function(e){var g,c,d,h,i,b,a,f=[].slice;
c=(function(){function j(k){this.current_flag=null;
this.controllers={};
this.alias_maps={};
this.$inputor=e(k);
this.setIframe();
this.listen()
}j.prototype.createContainer=function(k){if((this.$el=e("#atwho-container",k)).length===0){return e(k.body).append(this.$el=e("<div id='atwho-container'></div>"))
}};
j.prototype.setIframe=function(l,k){var m;
if(k==null){k=false
}if(l){this.window=l.contentWindow;
this.document=l.contentDocument||this.window.document;
this.iframe=l
}else{this.document=document;
this.window=window;
this.iframe=null
}if(this.iframeStandalone=k){if((m=this.$el)!=null){m.remove()
}return this.createContainer(this.document)
}else{return this.createContainer(document)
}};
j.prototype.controller=function(k){var o,n,l,m;
if(this.alias_maps[k]){n=this.controllers[this.alias_maps[k]]
}else{m=this.controllers;
for(l in m){o=m[l];
if(l===k){n=o;
break
}}}if(n){return n
}else{return this.controllers[this.current_flag]
}};
j.prototype.set_context_for=function(k){this.current_flag=k;
return this
};
j.prototype.reg=function(l,n){var k,m;
k=(m=this.controllers)[l]||(m[l]=new d(this,l));
if(n.alias){this.alias_maps[n.alias]=l
}k.init(n);
return this
};
j.prototype.listen=function(){return this.$inputor.on("keyup.atwhoInner",(function(k){return function(l){return k.on_keyup(l)
}
})(this)).on("keydown.atwhoInner",(function(k){return function(l){return k.on_keydown(l)
}
})(this)).on("scroll.atwhoInner",(function(k){return function(m){var l;
return(l=k.controller())!=null?l.view.hide(m):void 0
}
})(this)).on("blur.atwhoInner",(function(k){return function(l){var m;
if(m=k.controller()){return m.view.hide(l,m.get_opt("display_timeout"))
}}
})(this)).on("click.atwhoInner",(function(k){return function(l){return k.dispatch()
}
})(this))
};
j.prototype.shutdown=function(){var m,k,l;
l=this.controllers;
for(k in l){m=l[k];
m.destroy();
delete this.controllers[k]
}this.$inputor.off(".atwhoInner");
return this.$el.remove()
};
j.prototype.dispatch=function(){return e.map(this.controllers,(function(k){return function(m){var l;
if(l=m.get_opt("delay")){clearTimeout(k.delayedCallback);
return k.delayedCallback=setTimeout(function(){if(m.look_up()){return k.set_context_for(m.at)
}},l)
}else{if(m.look_up()){return k.set_context_for(m.at)
}}}
})(this))
};
j.prototype.on_keyup=function(l){var k;
switch(l.keyCode){case i.ESC:l.preventDefault();
if((k=this.controller())!=null){k.view.hide()
}break;
case i.DOWN:case i.UP:case i.CTRL:e.noop();
break;
case i.P:case i.N:if(!l.ctrlKey){this.dispatch()
}break;
default:this.dispatch()
}};
j.prototype.on_keydown=function(m){var k,l;
k=(l=this.controller())!=null?l.view:void 0;
if(!(k&&k.visible())){return
}switch(m.keyCode){case i.ESC:m.preventDefault();
k.hide(m);
break;
case i.UP:m.preventDefault();
k.prev();
break;
case i.DOWN:m.preventDefault();
k.next();
break;
case i.P:if(!m.ctrlKey){return
}m.preventDefault();
k.prev();
break;
case i.N:if(!m.ctrlKey){return
}m.preventDefault();
k.next();
break;
case i.TAB:case i.ENTER:if(!k.visible()){return
}m.preventDefault();
k.choose(m);
break;
default:e.noop()
}};
return j
})();
d=(function(){j.prototype.uid=function(){return(Math.random().toString(16)+"000000000").substr(2,8)+(new Date().getTime())
};
function j(l,k){this.app=l;
this.at=k;
this.$inputor=this.app.$inputor;
this.id=this.$inputor[0].id||this.uid();
this.setting=null;
this.query=null;
this.pos=0;
this.cur_rect=null;
this.range=null;
if((this.$el=e("#atwho-ground-"+this.id,this.app.$el)).length===0){this.app.$el.append(this.$el=e("<div id='atwho-ground-"+this.id+"'></div>"))
}this.model=new b(this);
this.view=new a(this)
}j.prototype.init=function(k){this.setting=e.extend({},this.setting||e.fn.atwho["default"],k);
this.view.init();
return this.model.reload(this.setting.data)
};
j.prototype.destroy=function(){this.trigger("beforeDestroy");
this.model.destroy();
this.view.destroy();
return this.$el.remove()
};
j.prototype.call_default=function(){var m,l,k;
k=arguments[0],m=2<=arguments.length?f.call(arguments,1):[];
try{return h[k].apply(this,m)
}catch(n){l=n;
return e.error(""+l+" Or maybe At.js doesn't have function "+k)
}};
j.prototype.trigger=function(k,m){var l,n;
if(m==null){m=[]
}m.push(this);
l=this.get_opt("alias");
n=l?""+k+"-"+l+".atwho":""+k+".atwho";
return this.$inputor.trigger(n,m)
};
j.prototype.callbacks=function(k){return this.get_opt("callbacks")[k]||h[k]
};
j.prototype.get_opt=function(l,k){var m;
try{return this.setting[l]
}catch(n){m=n;
return null
}};
j.prototype.content=function(){var k;
if(this.$inputor.is("textarea, input")){return this.$inputor.val()
}else{if(!(k=this.mark_range())){return
}return(k.startContainer.textContent||"").slice(0,k.startOffset)
}};
j.prototype.catch_query=function(){var k,n,l,o,p,m;
n=this.content();
k=this.$inputor.caret("pos",{iframe:this.app.iframe});
m=n.slice(0,k);
o=this.callbacks("matcher").call(this,this.at,m,this.get_opt("start_with_space"));
if(typeof o==="string"&&o.length<=this.get_opt("max_len",20)){p=k-o.length;
l=p+o.length;
this.pos=p;
o={text:o,head_pos:p,end_pos:l};
this.trigger("matched",[this.at,o.text])
}else{o=null;
this.view.hide()
}return this.query=o
};
j.prototype.rect=function(){var m,k,l;
if(!(m=this.$inputor.caret("offset",this.pos-1,{iframe:this.app.iframe}))){return
}if(this.app.iframe&&!this.app.iframeStandalone){k=e(this.app.iframe).offset();
m.left+=k.left;
m.top+=k.top
}if(this.$inputor.is("[contentEditable]")){m=this.cur_rect||(this.cur_rect=m)
}l=this.app.document.selection?0:2;
return{left:m.left,top:m.top,bottom:m.top+m.height+l}
};
j.prototype.reset_rect=function(){if(this.$inputor.is("[contentEditable]")){return this.cur_rect=null
}};
j.prototype.mark_range=function(){var k;
if(!this.$inputor.is("[contentEditable]")){return
}if(this.app.window.getSelection&&(k=this.app.window.getSelection()).rangeCount>0){return this.range=k.getRangeAt(0)
}else{if(this.app.document.selection){return this.ie8_range=this.app.document.selection.createRange()
}}};
j.prototype.insert_content_for=function(n){var m,l,k;
l=n.data("value")+"$";
k=this.get_opt("insert_tpl");
if(this.$inputor.is("textarea, input")||!k){return l
}m=e.extend({},n.data("item-data"),{"atwho-data-value":l,"atwho-at":this.at});
return this.callbacks("tpl_eval").call(this,k,m)
};
j.prototype.insert=function(s,t){var v,m,u,q,l,k,p,x,r,n,w,o;
v=this.$inputor;
r=this.callbacks("inserting_wrapper").call(this,v,s,this.get_opt("suffix"));
if(v.is("textarea, input")){k=v.val();
p=k.slice(0,Math.max(this.query.head_pos-this.at.length,0));
x=""+p+r+(k.slice(this.query.end_pos||0));
v.val(x);
v.caret("pos",p.length+r.length,{iframe:this.app.iframe})
}else{if(q=this.range){u=q.startOffset-(this.query.end_pos-this.query.head_pos)-this.at.length;
q.setStart(q.endContainer,Math.max(u,0));
q.setEnd(q.endContainer,q.endOffset);
q.deleteContents();
o=e(r,this.app.document);
for(n=0,w=o.length;
n<w;
n++){m=o[n];
q.insertNode(m);
q.setEndAfter(m);
q.collapse(false)
}l=this.app.window.getSelection();
l.removeAllRanges();
l.addRange(q)
}else{if(q=this.ie8_range){q.moveStart("character",this.query.end_pos-this.query.head_pos-this.at.length);
q.pasteHTML(r);
q.collapse(false);
q.select()
}}}if(!v.is(":focus")){v.focus()
}return v.change()
};
j.prototype.render_view=function(l){var k;
k=this.get_opt("search_key");
l=this.callbacks("sorter").call(this,this.query.text,l.slice(0,1001),k);
return this.view.render(l.slice(0,this.get_opt("limit")))
};
j.prototype.look_up=function(){var l,k;
if(!(l=this.catch_query())){return
}k=function(m){if(m&&m.length>0){return this.render_view(m)
}else{return this.view.hide()
}};
this.model.query(l.text,e.proxy(k,this));
return l
};
return j
})();
b=(function(){function j(k){this.context=k;
this.at=this.context.at;
this.storage=this.context.$inputor
}j.prototype.destroy=function(){return this.storage.data(this.at,null)
};
j.prototype.saved=function(){return this.fetch()>0
};
j.prototype.query=function(m,o){var l,k,n;
l=this.fetch();
k=this.context.get_opt("search_key");
l=this.context.callbacks("filter").call(this.context,m,l,k)||[];
n=this.context.callbacks("remote_filter");
if(l.length>0||(!n&&l.length===0)){return o(l)
}else{return n.call(this.context,m,o)
}};
j.prototype.fetch=function(){return this.storage.data(this.at)||[]
};
j.prototype.save=function(k){return this.storage.data(this.at,this.context.callbacks("before_save").call(this.context,k||[]))
};
j.prototype.load=function(k){if(!(this.saved()||!k)){return this._load(k)
}};
j.prototype.reload=function(k){return this._load(k)
};
j.prototype._load=function(k){if(typeof k==="string"){return e.ajax(k,{dataType:"json"}).done((function(l){return function(m){return l.save(m)
}
})(this))
}else{return this.save(k)
}};
return j
})();
a=(function(){function j(k){this.context=k;
this.$el=e("<div class='atwho-view'><ul class='atwho-view-ul'></ul></div>");
this.timeout_id=null;
this.context.$el.append(this.$el);
this.bind_event()
}j.prototype.init=function(){var k;
k=this.context.get_opt("alias")||this.context.at.charCodeAt(0);
return this.$el.attr({id:"at-view-"+k})
};
j.prototype.destroy=function(){return this.$el.remove()
};
j.prototype.bind_event=function(){var k;
k=this.$el.find("ul");
return k.on("mouseenter.atwho-view","li",function(l){k.find(".cur").removeClass("cur");
return e(l.currentTarget).addClass("cur")
}).on("click.atwho-view","li",(function(l){return function(m){k.find(".cur").removeClass("cur");
e(m.currentTarget).addClass("cur");
return m.preventDefault()
}
})(this))
};
j.prototype.visible=function(){return this.$el.is(":visible")
};
j.prototype.choose=function(l){var m,k;
if((m=this.$el.find(".cur")).length){k=this.context.insert_content_for(m);
this.context.insert(this.context.callbacks("before_insert").call(this.context,k,m),m);
this.context.trigger("inserted",[m,l]);
this.hide(l)
}if(this.context.get_opt("hide_without_suffix")){return this.stop_showing=true
}};
j.prototype.reposition=function(l){var n,k,m,o;
o=this.context.app.iframeStandalone?this.context.app.window:window;
if(l.bottom+this.$el.height()-e(o).scrollTop()>e(o).height()){l.bottom=l.top-this.$el.height()
}if(l.left>(k=e(o).width()-this.$el.width()-5)){l.left=k
}n={left:l.left,top:l.bottom};
if((m=this.context.callbacks("before_reposition"))!=null){m.call(this.context,n)
}this.$el.offset(n);
return this.context.trigger("reposition",[n])
};
j.prototype.next=function(){var l,k;
l=this.$el.find(".cur").removeClass("cur");
k=l.next();
if(!k.length){k=this.$el.find("li:first")
}k.addClass("cur");
return this.$el.animate({scrollTop:Math.max(0,l.innerHeight()*(k.index()+2)-this.$el.height())},150)
};
j.prototype.prev=function(){var l,k;
l=this.$el.find(".cur").removeClass("cur");
k=l.prev();
if(!k.length){k=this.$el.find("li:last")
}k.addClass("cur");
return this.$el.animate({scrollTop:Math.max(0,l.innerHeight()*(k.index()+2)-this.$el.height())},150)
};
j.prototype.show=function(){var k;
if(this.stop_showing){this.stop_showing=false;
return
}this.context.mark_range();
if(!this.visible()){this.$el.show();
this.$el.scrollTop(0);
this.context.trigger("shown")
}if(k=this.context.rect()){return this.reposition(k)
}};
j.prototype.hide=function(l,k){var m;
if(!this.visible()){return
}if(isNaN(k)){this.context.reset_rect();
this.$el.hide();
return this.context.trigger("hidden",[l])
}else{m=(function(n){return function(){return n.hide()
}
})(this);
clearTimeout(this.timeout_id);
return this.timeout_id=setTimeout(m,k)
}};
j.prototype.render=function(q){var r,n,o,k,m,p,l;
if(!(e.isArray(q)&&q.length>0)){this.hide();
return
}this.$el.find("ul").empty();
n=this.$el.find("ul");
m=this.context.get_opt("tpl");
for(p=0,l=q.length;
p<l;
p++){o=q[p];
o=e.extend({},o,{"atwho-at":this.context.at});
k=this.context.callbacks("tpl_eval").call(this.context,m,o);
r=e(this.context.callbacks("highlighter").call(this.context,k,this.context.query.text));
r.data("item-data",o);
n.append(r)
}this.show();
if(this.context.get_opt("highlight_first")){return n.find("li:first").addClass("cur")
}};
return j
})();
i={DOWN:40,UP:38,ESC:27,TAB:9,ENTER:13,CTRL:17,P:80,N:78};
h={before_save:function(n){var l,m,k,j;
if(!e.isArray(n)){return n
}j=[];
for(m=0,k=n.length;
m<k;
m++){l=n[m];
if(e.isPlainObject(l)){j.push(l)
}else{j.push({name:l})
}}return j
},matcher:function(k,o,j){var m,p,n,l;
k=k.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g,"\\$&");
if(j){k="(?:^|\\s)"+k
}n=decodeURI("%C3%80");
l=decodeURI("%C3%BF");
p=new RegExp(""+k+"([A-Za-z"+n+"-"+l+"0-9_+-]*)$|"+k+"([^\\x00-\\xff]*)$","gi");
m=p.exec(o);
if(m){return m[2]||m[1]
}else{return null
}},filter:function(p,o,m){var l,n,k,j;
j=[];
for(n=0,k=o.length;
n<k;
n++){l=o[n];
if(~new String(l[m]).toLowerCase().indexOf(p.toLowerCase())){j.push(l)
}}return j
},remote_filter:null,sorter:function(p,l,n){var m,o,k,j;
if(!p){return l
}j=[];
for(o=0,k=l.length;
o<k;
o++){m=l[o];
m.atwho_order=new String(m[n]).toLowerCase().indexOf(p.toLowerCase());
if(m.atwho_order>-1){j.push(m)
}}return j.sort(function(r,q){return r.atwho_order-q.atwho_order
})
},tpl_eval:function(k,l){var j;
try{return k.replace(/\$\{([^\}]*)\}/g,function(n,o,p){return l[o]
})
}catch(m){j=m;
return""
}},highlighter:function(j,l){var k;
if(!l){return j
}k=new RegExp(">\\s*(\\w*?)("+l.replace("+","\\+")+")(\\w*)\\s*<","ig");
return j.replace(k,function(o,m,p,n){return"> "+m+"<strong>"+p+"</strong>"+n+" <"
})
},before_insert:function(j,k){return j
},inserting_wrapper:function(l,j,k){var m;
if(l.is("textarea, input")){return""+j
}else{if(l.attr("contentEditable")==="true"){if(/firefox/i.test(navigator.userAgent)){}else{}if(this.app.document.selection){m=j
}m="<span>"+j+"</span>";
return m
}}}};
g={load:function(j,k){var l;
if(l=this.controller(j)){return l.model.load(k)
}},setIframe:function(k,j){this.setIframe(k,j);
return null
},run:function(){return this.dispatch()
},destroy:function(){this.shutdown();
return this.$inputor.data("atwho",null)
}};
e.fn.atwho=function(l){var j,k;
k=arguments;
j=null;
this.filter('textarea, input, [contenteditable=""], [contenteditable=true]').each(function(){var m,n;
if(!(n=(m=e(this)).data("atwho"))){m.data("atwho",(n=new c(this)))
}if(typeof l==="object"||!l){return n.reg(l.at,l)
}else{if(g[l]&&n){return j=g[l].apply(n,Array.prototype.slice.call(k,1))
}else{return e.error("Method "+l+" does not exist on jQuery.caret")
}}});
return j||this
};
e.fn.atwho["default"]={at:void 0,alias:void 0,data:null,tpl:"<li data-value='${atwho-at}${name}'>${name}</li>",insert_tpl:"${atwho-data-value}",callbacks:h,search_key:"name",suffix:void 0,hide_without_suffix:false,start_with_space:false,highlight_first:true,max_len:20,display_timeout:300,delay:null}
}));