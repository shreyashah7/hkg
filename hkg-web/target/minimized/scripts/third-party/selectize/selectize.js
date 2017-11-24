(function(a,b){if(typeof define==="function"&&define.amd){define("sifter",b)
}else{if(typeof exports==="object"){module.exports=b()
}else{a.Sifter=b()
}}}(this,function(){var f=function(h,i){this.items=h;
this.settings=i||{diacritics:true}
};
f.prototype.tokenize=function(l){l=b(String(l||"").toLowerCase());
if(!l||!l.length){return[]
}var h,p,k,j;
var m=[];
var o=l.split(/ +/);
for(h=0,p=o.length;
h<p;
h++){k=c(o[h]);
if(this.settings.diacritics){for(j in a){if(a.hasOwnProperty(j)){k=k.replace(new RegExp(j,"g"),a[j])
}}}m.push({string:o[h],regex:new RegExp(k,"i")})
}return m
};
f.prototype.iterator=function(h,j){var i;
if(d(h)){i=Array.prototype.forEach||function(m){for(var k=0,l=this.length;
k<l;
k++){m(this[k],k,this)
}}
}else{i=function(l){for(var k in this){if(this.hasOwnProperty(k)){l(this[k],k,this)
}}}
}i.apply(h,[j])
};
f.prototype.getScoreFunction=function(m,k){var j,i,o,n;
j=this;
m=j.prepareSearch(m,k);
o=m.tokens;
i=m.options.fields;
n=o.length;
var h=function(q,p){var r,s;
if(!q){return 0
}q=String(q||"");
s=q.search(p.regex);
if(s===-1){return 0
}r=p.string.length/q.length;
if(s===0){r+=0.5
}return r
};
var l=(function(){var p=i.length;
if(!p){return function(){return 0
}
}if(p===1){return function(q,r){return h(r[i[0]],q)
}
}return function(r,t){for(var q=0,s=0;
q<p;
q++){s+=h(t[i[q]],r)
}return s/p
}
})();
if(!n){return function(){return 0
}
}if(n===1){return function(p){return l(o[0],p)
}
}if(m.options.conjunction==="and"){return function(r){var s;
for(var p=0,q=0;
p<n;
p++){s=l(o[p],r);
if(s<=0){return 0
}q+=s
}return q/n
}
}else{return function(r){for(var p=0,q=0;
p<n;
p++){q+=l(o[p],r)
}return q/n
}
}};
f.prototype.getSortFunction=function(u,v){var p,h,t,r,q,k,s,m,j,l,o;
t=this;
u=t.prepareSearch(u,v);
o=(!u.query&&v.sort_empty)||v.sort;
j=function(n,i){if(n==="$score"){return i.score
}return t.items[i.id][n]
};
q=[];
if(o){for(p=0,h=o.length;
p<h;
p++){if(u.query||o[p].field!=="$score"){q.push(o[p])
}}}if(u.query){l=true;
for(p=0,h=q.length;
p<h;
p++){if(q[p].field==="$score"){l=false;
break
}}if(l){q.unshift({field:"$score",direction:"desc"})
}}else{for(p=0,h=q.length;
p<h;
p++){if(q[p].field==="$score"){q.splice(p,1);
break
}}}m=[];
for(p=0,h=q.length;
p<h;
p++){m.push(q[p].direction==="desc"?-1:1)
}k=q.length;
if(!k){return null
}else{if(k===1){r=q[0].field;
s=m[0];
return function(n,i){return s*e(j(r,n),j(r,i))
}
}else{return function(y,w){var A,n,x,z,B;
for(A=0;
A<k;
A++){B=q[A].field;
n=m[A]*e(j(B,y),j(B,w));
if(n){return n
}}return 0
}
}}};
f.prototype.prepareSearch=function(k,h){if(typeof k==="object"){return k
}h=g({},h);
var j=h.fields;
var l=h.sort;
var i=h.sort_empty;
if(j&&!d(j)){h.fields=[j]
}if(l&&!d(l)){h.sort=[l]
}if(i&&!d(i)){h.sort_empty=[i]
}return{options:h,query:String(k||"").toLowerCase(),tokens:this.tokenize(k),total:0,items:[]}
};
f.prototype.search=function(k,p){var n=this,m,i,o,j;
var h;
var l;
o=this.prepareSearch(k,p);
p=o.options;
k=o.query;
l=p.score||n.getScoreFunction(o);
if(k.length){n.iterator(n.items,function(q,r){i=l(q);
if(p.filter===false||i>0){o.items.push({score:i,id:r})
}})
}else{n.iterator(n.items,function(q,r){o.items.push({score:1,id:r})
})
}h=n.getSortFunction(o,p);
if(h){o.items.sort(h)
}o.total=o.items.length;
if(typeof p.limit==="number"){o.items=o.items.slice(0,p.limit)
}return o
};
var e=function(i,h){if(typeof i==="number"&&typeof h==="number"){return i>h?1:(i<h?-1:0)
}i=String(i||"").toLowerCase();
h=String(h||"").toLowerCase();
if(i>h){return 1
}if(h>i){return -1
}return 0
};
var g=function(j,h){var o,p,l,m;
for(o=1,p=arguments.length;
o<p;
o++){m=arguments[o];
if(!m){continue
}for(l in m){if(m.hasOwnProperty(l)){j[l]=m[l]
}}}return j
};
var b=function(h){return(h+"").replace(/^\s+|\s+$|/g,"")
};
var c=function(h){return(h+"").replace(/([.?*+^$[\]\\(){}|-])/g,"\\$1")
};
var d=Array.isArray||($&&$.isArray)||function(h){return Object.prototype.toString.call(h)==="[object Array]"
};
var a={a:"[aÀÁÂÃÄÅàáâãäå]",c:"[cÇçćĆčČ]",d:"[dđĐďĎ]",e:"[eÈÉÊËèéêëěĚ]",i:"[iÌÍÎÏìíîï]",n:"[nÑñňŇ]",o:"[oÒÓÔÕÕÖØòóôõöø]",r:"[rřŘ]",s:"[sŠš]",t:"[tťŤ]",u:"[uÙÚÛÜùúûüůŮ]",y:"[yŸÿýÝ]",z:"[zŽž]"};
return f
}));
(function(a,b){if(typeof define==="function"&&define.amd){define("microplugin",b)
}else{if(typeof exports==="object"){module.exports=b()
}else{a.MicroPlugin=b()
}}}(this,function(){var a={};
a.mixin=function(c){c.plugins={};
c.prototype.initializePlugins=function(e){var h,j,g;
var f=this;
var d=[];
f.plugins={names:[],settings:{},requested:{},loaded:{}};
if(b.isArray(e)){for(h=0,j=e.length;
h<j;
h++){if(typeof e[h]==="string"){d.push(e[h])
}else{f.plugins.settings[e[h].name]=e[h].options;
d.push(e[h].name)
}}}else{if(e){for(g in e){if(e.hasOwnProperty(g)){f.plugins.settings[g]=e[g];
d.push(g)
}}}}while(d.length){f.require(d.shift())
}};
c.prototype.loadPlugin=function(f){var e=this;
var d=e.plugins;
var g=c.plugins[f];
if(!c.plugins.hasOwnProperty(f)){throw new Error('Unable to find "'+f+'" plugin')
}d.requested[f]=true;
d.loaded[f]=g.fn.apply(e,[e.plugins.settings[f]||{}]);
d.names.push(f)
};
c.prototype.require=function(f){var e=this;
var d=e.plugins;
if(!e.plugins.loaded.hasOwnProperty(f)){if(d.requested[f]){throw new Error('Plugin has circular dependency ("'+f+'")')
}e.loadPlugin(f)
}return d.loaded[f]
};
c.define=function(d,e){c.plugins[d]={name:d,fn:e}
}
};
var b={isArray:Array.isArray||function(c){return Object.prototype.toString.call(c)==="[object Array]"
}};
return a
}));
(function(a,b){if(typeof define==="function"&&define.amd){define("selectize",["jquery","sifter","microplugin"],b)
}else{if(typeof exports==="object"){module.exports=b(require("jquery"),require("sifter"),require("microplugin"))
}else{a.Selectize=b(a.jQuery,a.Sifter,a.MicroPlugin)
}}}(this,function(v,m,i){var K=function(N,Q){if(typeof Q==="string"&&!Q.length){return
}var P=(typeof Q==="string")?new RegExp(Q,"i"):Q;
var O=function(T){var Z=0;
if(T.nodeType===3){var Y=T.data.search(P);
if(Y>=0&&T.data.length>0){var X=T.data.match(P);
var W=document.createElement("span");
W.className="highlight";
var U=T.splitText(Y);
var R=U.splitText(X[0].length);
var S=U.cloneNode(true);
W.appendChild(S);
U.parentNode.replaceChild(W,U);
Z=1
}}else{if(T.nodeType===1&&T.childNodes&&!/(script|style)/i.test(T.tagName)){for(var V=0;
V<T.childNodes.length;
++V){V+=O(T.childNodes[V])
}}}return Z
};
return N.each(function(){O(this)
})
};
var E=function(){};
E.prototype={on:function(O,N){this._events=this._events||{};
this._events[O]=this._events[O]||[];
this._events[O].push(N)
},off:function(O,N){var P=arguments.length;
if(P===0){return delete this._events
}if(P===1){return delete this._events[O]
}this._events=this._events||{};
if(O in this._events===false){return
}this._events[O].splice(this._events[O].indexOf(N),1)
},trigger:function(O){this._events=this._events||{};
if(O in this._events===false){return
}for(var N=0;
N<this._events[O].length;
N++){this._events[O][N].apply(this,Array.prototype.slice.call(arguments,1))
}}};
E.mixin=function(N){var P=["on","off","trigger"];
for(var O=0;
O<P.length;
O++){N.prototype[P[O]]=E.prototype[P[O]]
}};
var c=/Mac/.test(navigator.userAgent);
var G=65;
var L=188;
var z=13;
var w=27;
var p=37;
var F=38;
var x=80;
var t=39;
var u=40;
var B=78;
var o=8;
var q=46;
var j=16;
var H=c?91:17;
var e=c?18:17;
var D=9;
var k=1;
var h=2;
var M=function(N){return typeof N!=="undefined"
};
var y=function(N){if(typeof N==="undefined"||N===null){return""
}if(typeof N==="boolean"){return N?"1":"0"
}return N+""
};
var C=function(N){return(N+"").replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/"/g,"&quot;")
};
var f=function(N){return(N+"").replace(/\$/g,"$$$$")
};
var s={};
s.before=function(N,Q,P){var O=N[Q];
N[Q]=function(){P.apply(N,arguments);
return O.apply(N,arguments)
}
};
s.after=function(N,Q,P){var O=N[Q];
N[Q]=function(){var R=O.apply(N,arguments);
P.apply(N,arguments);
return R
}
};
var a=function(O,Q){if(!v.isArray(Q)){return Q
}var N,R,P={};
for(N=0,R=Q.length;
N<R;
N++){if(Q[N].hasOwnProperty(O)){P[Q[N][O]]=Q[N]
}}return P
};
var r=function(N){var O=false;
return function(){if(O){return
}O=true;
N.apply(this,arguments)
}
};
var g=function(O,N){var P;
return function(){var Q=this;
var R=arguments;
window.clearTimeout(P);
P=window.setTimeout(function(){O.apply(Q,R)
},N)
}
};
var b=function(N,P,R){var Q;
var O=N.trigger;
var S={};
N.trigger=function(){var T=arguments[0];
if(P.indexOf(T)!==-1){S[T]=arguments
}else{return O.apply(N,arguments)
}};
R.apply(N,[]);
N.trigger=O;
for(Q in S){if(S.hasOwnProperty(Q)){O.apply(N,S[Q])
}}};
var A=function(Q,P,N,O){Q.on(P,N,function(R){var S=R.target;
while(S&&S.parentNode!==Q[0]){S=S.parentNode
}R.currentTarget=S;
return O.apply(this,[R])
})
};
var l=function(P){var O={};
if("selectionStart" in P){O.start=P.selectionStart;
O.length=P.selectionEnd-O.start
}else{if(document.selection){P.focus();
var Q=document.selection.createRange();
var N=document.selection.createRange().text.length;
Q.moveStart("character",-P.value.length);
O.start=Q.text.length-N;
O.length=N
}}return O
};
var n=function(Q,R,O){var N,S,P={};
if(O){for(N=0,S=O.length;
N<S;
N++){P[O[N]]=Q.css(O[N])
}}else{P=Q.css()
}R.css(P)
};
var I=function(Q,P){if(!Q){return 0
}var O=v("<test>").css({position:"absolute",top:-99999,left:-99999,width:"auto",padding:0,whiteSpace:"pre"}).text(Q).appendTo("body");
n(P,O,["letterSpacing","fontSize","fontFamily","fontWeight","textTransform"]);
var N=O.width();
O.remove();
return N
};
var J=function(P){var N=null;
var O=function(T,Z){var X,Y,U,W,Q;
var R,S,V;
T=T||window.event||{};
Z=Z||{};
if(T.metaKey||T.altKey){return
}if(!Z.force&&P.data("grow")===false){return
}X=P.val();
if(T.type&&T.type.toLowerCase()==="keydown"){Y=T.keyCode;
U=((Y>=97&&Y<=122)||(Y>=65&&Y<=90)||(Y>=48&&Y<=57)||Y===32);
if(Y===q||Y===o){V=l(P[0]);
if(V.length){X=X.substring(0,V.start)+X.substring(V.start+V.length)
}else{if(Y===o&&V.start){X=X.substring(0,V.start-1)+X.substring(V.start+1)
}else{if(Y===q&&typeof V.start!=="undefined"){X=X.substring(0,V.start)+X.substring(V.start+1)
}}}}else{if(U){R=T.shiftKey;
S=String.fromCharCode(T.keyCode);
if(R){S=S.toUpperCase()
}else{S=S.toLowerCase()
}X+=S
}}}W=P.attr("placeholder");
if(!X&&W){X=W
}Q=I(X,P)+4;
if(Q!==N){N=Q;
P.width(Q);
P.triggerHandler("resize")
}};
P.on("keydown keyup update blur",O);
O()
};
var d=function(U,S){var R,Q,T,P,O,N=this;
O=U[0];
O.selectize=N;
P=window.getComputedStyle?window.getComputedStyle(O,null).getPropertyValue("direction"):O.currentStyle&&O.currentStyle.direction;
P=P||U.parents("[dir]:first").attr("dir")||"";
v.extend(N,{settings:S,$input:U,tagType:O.tagName.toLowerCase()==="select"?k:h,rtl:/rtl/i.test(P),eventNS:".selectize"+(++d.count),highlightedValue:null,isOpen:false,isDisabled:false,isRequired:U.is("[required]"),isInvalid:false,isLocked:false,isFocused:false,isInputHidden:false,isSetup:false,isShiftDown:false,isCmdDown:false,isCtrlDown:false,ignoreFocus:false,ignoreHover:false,hasOptions:false,currentResults:null,lastValue:"",caretPos:0,loading:0,loadedSearches:{},$activeOption:null,$activeItems:[],optgroups:{},options:{},userOptions:{},items:[],renderCache:{},onSearchChange:S.loadThrottle===null?N.onSearchChange:g(N.onSearchChange,S.loadThrottle)});
N.sifter=new m(this.options,{diacritics:S.diacritics});
v.extend(N.options,a(S.valueField,S.options));
delete N.settings.options;
v.extend(N.optgroups,a(S.optgroupValueField,S.optgroups));
delete N.settings.optgroups;
N.settings.mode=N.settings.mode||(N.settings.maxItems===1?"single":"multi");
if(typeof N.settings.hideSelected!=="boolean"){N.settings.hideSelected=N.settings.mode==="multi"
}N.initializePlugins(N.settings.plugins);
N.setupCallbacks();
N.setupTemplates();
N.setup()
};
E.mixin(d);
i.mixin(d);
v.extend(d.prototype,{setup:function(){var ad=this;
var T=ad.settings;
var O=ad.eventNS;
var P=v(window);
var U=v(document);
var X;
var aa;
var ab;
var Y;
var ac;
var Q;
var N;
var Z;
var R;
var W;
var S;
var V;
N=ad.settings.mode;
W=ad.$input.attr("tabindex")||"";
S=ad.$input.attr("class")||"";
X=v("<div>").addClass(T.wrapperClass).addClass(S).addClass(N);
aa=v("<div>").addClass(T.inputClass).addClass("items").appendTo(X);
ab=v('<input type="text" autocomplete="off" />').appendTo(aa).attr("tabindex",W);
Q=v(T.dropdownParent||X);
Y=v("<div>").addClass(T.dropdownClass).addClass(S).addClass(N).hide().appendTo(Q);
ac=v("<div>").addClass(T.dropdownContentClass).appendTo(Y);
X.css({width:ad.$input[0].style.width});
if(ad.plugins.names.length){V="plugin-"+ad.plugins.names.join(" plugin-");
X.addClass(V);
Y.addClass(V)
}if((T.maxItems===null||T.maxItems>1)&&ad.tagType===k){ad.$input.attr("multiple","multiple")
}if(ad.settings.placeholder){ab.attr("placeholder",T.placeholder)
}if(ad.$input.attr("autocorrect")){ab.attr("autocorrect",ad.$input.attr("autocorrect"))
}if(ad.$input.attr("autocapitalize")){ab.attr("autocapitalize",ad.$input.attr("autocapitalize"))
}ad.$wrapper=X;
ad.$control=aa;
ad.$control_input=ab;
ad.$dropdown=Y;
ad.$dropdown_content=ac;
Y.on("mouseenter","[data-selectable]",function(){return ad.onOptionHover.apply(ad,arguments)
});
Y.on("mousedown","[data-selectable]",function(){return ad.onOptionSelect.apply(ad,arguments)
});
A(aa,"mousedown","*:not(input)",function(){return ad.onItemSelect.apply(ad,arguments)
});
J(ab);
aa.on({mousedown:function(){return ad.onMouseDown.apply(ad,arguments)
},click:function(){return ad.onClick.apply(ad,arguments)
}});
ab.on({mousedown:function(ae){ae.stopPropagation()
},keydown:function(){return ad.onKeyDown.apply(ad,arguments)
},keyup:function(){return ad.onKeyUp.apply(ad,arguments)
},keypress:function(){return ad.onKeyPress.apply(ad,arguments)
},resize:function(){ad.positionDropdown.apply(ad,[])
},blur:function(){return ad.onBlur.apply(ad,arguments)
},focus:function(){return ad.onFocus.apply(ad,arguments)
},paste:function(){return ad.onPaste.apply(ad,arguments)
}});
U.on("keydown"+O,function(ae){ad.isCmdDown=ae[c?"metaKey":"ctrlKey"];
ad.isCtrlDown=ae[c?"altKey":"ctrlKey"];
ad.isShiftDown=ae.shiftKey
});
U.on("keyup"+O,function(ae){if(ae.keyCode===e){ad.isCtrlDown=false
}if(ae.keyCode===j){ad.isShiftDown=false
}if(ae.keyCode===H){ad.isCmdDown=false
}});
U.on("mousedown"+O,function(ae){if(ad.isFocused){if(ae.target===ad.$dropdown[0]||ae.target.parentNode===ad.$dropdown[0]){return false
}if(!ad.$control.has(ae.target).length&&ae.target!==ad.$control[0]){ad.blur()
}}});
P.on(["scroll"+O,"resize"+O].join(" "),function(){if(ad.isOpen){ad.positionDropdown.apply(ad,arguments)
}});
P.on("mousemove"+O,function(){ad.ignoreHover=false
});
this.revertSettings={$children:ad.$input.children().detach(),tabindex:ad.$input.attr("tabindex")};
ad.$input.attr("tabindex",-1).hide().after(ad.$wrapper);
if(v.isArray(T.items)){ad.setValue(T.items);
delete T.items
}if(ad.$input[0].validity){ad.$input.on("invalid"+O,function(ae){ae.preventDefault();
ad.isInvalid=true;
ad.refreshState()
})
}ad.updateOriginalInput();
ad.refreshItems();
ad.refreshState();
ad.updatePlaceholder();
ad.isSetup=true;
if(ad.$input.is(":disabled")){ad.disable()
}ad.on("change",this.onChange);
ad.trigger("initialize");
if(T.preload===true){ad.onSearchChange("")
}},setupTemplates:function(){var O=this;
var N=O.settings.labelField;
var P=O.settings.optgroupLabelField;
var Q={optgroup:function(R){return'<div class="optgroup">'+R.html+"</div>"
},optgroup_header:function(S,R){return'<div class="optgroup-header">'+R(S[P])+"</div>"
},option:function(S,R){return'<div class="option">'+R(S[N])+"</div>"
},item:function(S,R){return'<div class="item">'+R(S[N])+"</div>"
},option_create:function(S,R){return'<div class="create">Add <strong>'+R(S.input)+"</strong>&hellip;</div>"
}};
O.settings.render=v.extend({},Q,O.settings.render)
},setupCallbacks:function(){var N,O,P={initialize:"onInitialize",change:"onChange",item_add:"onItemAdd",item_remove:"onItemRemove",clear:"onClear",option_add:"onOptionAdd",option_remove:"onOptionRemove",option_clear:"onOptionClear",dropdown_open:"onDropdownOpen",dropdown_close:"onDropdownClose",type:"onType"};
for(N in P){if(P.hasOwnProperty(N)){O=this.settings[P[N]];
if(O){this.on(N,O)
}}}},onClick:function(O){var N=this;
if(!N.isFocused){N.focus();
O.preventDefault()
}},onMouseDown:function(Q){var O=this;
var P=Q.isDefaultPrevented();
var N=v(Q.target);
if(O.isFocused){if(Q.target!==O.$control_input[0]){if(O.settings.mode==="single"){O.isOpen?O.close():O.open()
}else{if(!P){O.setActiveItem(null)
}}return false
}}else{if(!P){window.setTimeout(function(){O.focus()
},0)
}}},onChange:function(){this.$input.trigger("change")
},onPaste:function(O){var N=this;
if(N.isFull()||N.isInputHidden||N.isLocked){O.preventDefault()
}},onKeyPress:function(O){if(this.isLocked){return O&&O.preventDefault()
}var N=String.fromCharCode(O.keyCode||O.which);
if(this.settings.create&&N===this.settings.delimiter){this.createItem();
O.preventDefault();
return false
}},onKeyDown:function(R){var Q=R.target===this.$control_input[0];
var P=this;
if(P.isLocked){if(R.keyCode!==D){R.preventDefault()
}return
}switch(R.keyCode){case G:if(P.isCmdDown){P.selectAll();
return
}break;
case w:P.close();
return;
case B:if(!R.ctrlKey||R.altKey){break
}case u:if(!P.isOpen&&P.hasOptions){P.open()
}else{if(P.$activeOption){P.ignoreHover=true;
var N=P.getAdjacentOption(P.$activeOption,1);
if(N.length){P.setActiveOption(N,true,true)
}}}R.preventDefault();
return;
case x:if(!R.ctrlKey||R.altKey){break
}case F:if(P.$activeOption){P.ignoreHover=true;
var O=P.getAdjacentOption(P.$activeOption,-1);
if(O.length){P.setActiveOption(O,true,true)
}}R.preventDefault();
return;
case z:if(P.isOpen&&P.$activeOption){P.onOptionSelect({currentTarget:P.$activeOption})
}R.preventDefault();
return;
case p:P.advanceSelection(-1,R);
return;
case t:P.advanceSelection(1,R);
return;
case D:if(P.settings.selectOnTab&&P.isOpen&&P.$activeOption){P.onOptionSelect({currentTarget:P.$activeOption})
}if(P.settings.create&&P.createItem()){R.preventDefault()
}return;
case o:case q:P.deleteSelection(R);
return
}if((P.isFull()||P.isInputHidden)&&!(c?R.metaKey:R.ctrlKey)){R.preventDefault();
return
}},onKeyUp:function(P){var N=this;
if(N.isLocked){return P&&P.preventDefault()
}var O=N.$control_input.val()||"";
if(N.lastValue!==O){N.lastValue=O;
N.onSearchChange(O);
N.refreshOptions();
N.trigger("type",O)
}},onSearchChange:function(P){var N=this;
var O=N.settings.load;
if(!O){return
}if(N.loadedSearches.hasOwnProperty(P)){return
}N.loadedSearches[P]=true;
N.load(function(Q){O.apply(N,[P,Q])
})
},onFocus:function(O){var N=this;
N.isFocused=true;
if(N.isDisabled){N.blur();
O&&O.preventDefault();
return false
}if(N.ignoreFocus){return
}if(N.settings.preload==="focus"){N.onSearchChange("")
}if(!N.$activeItems.length){N.showInput();
N.setActiveItem(null);
N.refreshOptions(!!N.settings.openOnFocus)
}N.refreshState()
},onBlur:function(O){var N=this;
N.isFocused=false;
if(N.ignoreFocus){return
}if(N.settings.create&&N.settings.createOnBlur){N.createItem(false)
}N.close();
N.setTextboxValue("");
N.setActiveItem(null);
N.setActiveOption(null);
N.setCaret(N.items.length);
N.refreshState()
},onOptionHover:function(N){if(this.ignoreHover){return
}this.setActiveOption(N.currentTarget,false)
},onOptionSelect:function(R){var P,N,Q,O=this;
if(R.preventDefault){R.preventDefault();
R.stopPropagation()
}N=v(R.currentTarget);
if(N.hasClass("create")){O.createItem()
}else{P=N.attr("data-value");
if(P){O.lastQuery=null;
O.setTextboxValue("");
O.addItem(P);
if(!O.settings.hideSelected&&R.type&&/mouse/.test(R.type)){O.setActiveOption(O.getOption(P))
}}}},onItemSelect:function(O){var N=this;
if(N.isLocked){return
}if(N.settings.mode==="multi"){O.preventDefault();
N.setActiveItem(O.currentTarget,O)
}},load:function(O){var N=this;
var P=N.$wrapper.addClass("loading");
N.loading++;
O.apply(N,[function(Q){N.loading=Math.max(N.loading-1,0);
if(Q&&Q.length){N.addOption(Q);
N.refreshOptions(N.isFocused&&!N.isInputHidden)
}if(!N.loading){P.removeClass("loading")
}N.trigger("load",Q)
}])
},setTextboxValue:function(N){var P=this.$control_input;
var O=P.val()!==N;
if(O){P.val(N).triggerHandler("update");
this.lastValue=N
}},getValue:function(){if(this.tagType===k&&this.$input.attr("multiple")){return this.items
}else{return this.items.join(this.settings.delimiter)
}},setValue:function(N){b(this,["change"],function(){this.clear();
this.addItems(N)
})
},setActiveItem:function(U,S){var X=this;
var R;
var Q,V,N,P,W,O;
var T;
if(X.settings.mode==="single"){return
}U=v(U);
if(!U.length){v(X.$activeItems).removeClass("active");
X.$activeItems=[];
if(X.isFocused){X.showInput()
}return
}R=S&&S.type.toLowerCase();
if(R==="mousedown"&&X.isShiftDown&&X.$activeItems.length){T=X.$control.children(".active:last");
N=Array.prototype.indexOf.apply(X.$control[0].childNodes,[T[0]]);
P=Array.prototype.indexOf.apply(X.$control[0].childNodes,[U[0]]);
if(N>P){O=N;
N=P;
P=O
}for(Q=N;
Q<=P;
Q++){W=X.$control[0].childNodes[Q];
if(X.$activeItems.indexOf(W)===-1){v(W).addClass("active");
X.$activeItems.push(W)
}}S.preventDefault()
}else{if((R==="mousedown"&&X.isCtrlDown)||(R==="keydown"&&this.isShiftDown)){if(U.hasClass("active")){V=X.$activeItems.indexOf(U[0]);
X.$activeItems.splice(V,1);
U.removeClass("active")
}else{X.$activeItems.push(U.addClass("active")[0])
}}else{v(X.$activeItems).removeClass("active");
X.$activeItems=[U.addClass("active")[0]]
}}X.hideInput();
if(!this.isFocused){X.focus()
}},setActiveOption:function(N,T,P){var O,U,S;
var R,Q;
var V=this;
if(V.$activeOption){V.$activeOption.removeClass("active")
}V.$activeOption=null;
N=v(N);
if(!N.length){return
}V.$activeOption=N.addClass("active");
if(T||!M(T)){O=V.$dropdown_content.height();
U=V.$activeOption.outerHeight(true);
T=V.$dropdown_content.scrollTop()||0;
S=V.$activeOption.offset().top-V.$dropdown_content.offset().top+T;
R=S;
Q=S-O+U;
if(S+U>O+T){V.$dropdown_content.stop().animate({scrollTop:Q},P?V.settings.scrollDuration:0)
}else{if(S<T){V.$dropdown_content.stop().animate({scrollTop:R},P?V.settings.scrollDuration:0)
}}}},selectAll:function(){var N=this;
if(N.settings.mode==="single"){return
}N.$activeItems=Array.prototype.slice.apply(N.$control.children(":not(input)").addClass("active"));
if(N.$activeItems.length){N.hideInput();
N.close()
}N.focus()
},hideInput:function(){var N=this;
N.setTextboxValue("");
N.$control_input.css({opacity:0,position:"absolute",left:N.rtl?10000:-10000});
N.isInputHidden=true
},showInput:function(){this.$control_input.css({opacity:1,position:"relative",left:0});
this.isInputHidden=false
},focus:function(){var N=this;
if(N.isDisabled){return
}N.ignoreFocus=true;
N.$control_input[0].focus();
window.setTimeout(function(){N.ignoreFocus=false;
N.onFocus()
},0)
},blur:function(){this.$control_input.trigger("blur")
},getScoreFunction:function(N){return this.sifter.getScoreFunction(N,this.getSearchOptions())
},getSearchOptions:function(){var O=this.settings;
var N=O.sortField;
if(typeof N==="string"){N={field:N}
}return{fields:O.searchField,conjunction:O.searchConjunction,sort:N}
},search:function(S){var U=this;
var N=/^([A-Za-z])$/;
var R,T,O,W,Q;
var P=U.settings;
var V=this.getSearchOptions();
if(P.score){Q=U.settings.score.apply(this,[S]);
if(typeof Q!=="function"){throw new Error('Selectize "score" setting must be a function that returns a function')
}}if(S!==U.lastQuery){U.lastQuery=S;
W=U.sifter.search(S,v.extend(V,{score:Q}));
U.currentResults=W
}else{W=v.extend(true,{},U.currentResults)
}if(P.hideSelected){for(R=W.items.length-1;
R>=0;
R--){if(U.items.indexOf(y(W.items[R].id))!==-1){W.items.splice(R,1)
}}}return W
},refreshOptions:function(X){var ae,ad,ac,aa,ah,N,T,af,P,ab,R,ag,S;
var Q,W,Y;
if(typeof X==="undefined"){X=true
}var V=this;
var O=V.$control_input.val();
var Z=V.search(O);
var U=V.$dropdown_content;
var ai=V.$activeOption&&y(V.$activeOption.attr("data-value"));
aa=Z.items.length;
if(typeof V.settings.maxOptions==="number"){aa=Math.min(aa,V.settings.maxOptions)
}ah={};
if(V.settings.optgroupOrder){N=V.settings.optgroupOrder;
for(ae=0;
ae<N.length;
ae++){ah[N[ae]]=[]
}}else{N=[]
}for(ae=0;
ae<aa;
ae++){T=V.options[Z.items[ae].id];
af=V.render("option",T);
P=T[V.settings.optgroupField]||"";
ab=v.isArray(P)?P:[P];
for(ad=0,ac=ab&&ab.length;
ad<ac;
ad++){P=ab[ad];
if(!V.optgroups.hasOwnProperty(P)){P=""
}if(!ah.hasOwnProperty(P)){ah[P]=[];
N.push(P)
}ah[P].push(af)
}}R=[];
for(ae=0,aa=N.length;
ae<aa;
ae++){P=N[ae];
if(V.optgroups.hasOwnProperty(P)&&ah[P].length){ag=V.render("optgroup_header",V.optgroups[P])||"";
ag+=ah[P].join("");
R.push(V.render("optgroup",v.extend({},V.optgroups[P],{html:ag})))
}else{R.push(ah[P].join(""))
}}U.html(R.join(""));
if(V.settings.highlight&&Z.query.length&&Z.tokens.length){for(ae=0,aa=Z.tokens.length;
ae<aa;
ae++){K(U,Z.tokens[ae].regex)
}}if(!V.settings.hideSelected){for(ae=0,aa=V.items.length;
ae<aa;
ae++){V.getOption(V.items[ae]).addClass("selected")
}}S=V.settings.create&&Z.query.length;
if(S){U.prepend(V.render("option_create",{input:O}));
Y=v(U[0].childNodes[0])
}V.hasOptions=Z.items.length>0||S;
if(V.hasOptions){if(Z.items.length>0){W=ai&&V.getOption(ai);
if(W&&W.length){Q=W
}else{if(V.settings.mode==="single"&&V.items.length){Q=V.getOption(V.items[0])
}}if(!Q||!Q.length){if(Y&&!V.settings.addPrecedence){Q=V.getAdjacentOption(Y,1)
}else{Q=U.find("[data-selectable]:first")
}}}else{Q=Y
}V.setActiveOption(Q);
if(X&&!V.isOpen){V.open()
}}else{V.setActiveOption(null);
if(X&&V.isOpen){V.close()
}}},addOption:function(R){var P,S,O,Q,N=this;
if(v.isArray(R)){for(P=0,S=R.length;
P<S;
P++){N.addOption(R[P])
}return
}Q=y(R[N.settings.valueField]);
if(!Q||N.options.hasOwnProperty(Q)){return
}N.userOptions[Q]=true;
N.options[Q]=R;
N.lastQuery=null;
N.trigger("option_add",Q,R)
},addOptionGroup:function(O,N){this.optgroups[O]=N;
this.trigger("optgroup_add",O,N)
},updateOption:function(T,R){var V=this;
var U,N;
var P,Q,S,O;
T=y(T);
P=y(R[V.settings.valueField]);
if(!V.options.hasOwnProperty(T)){return
}if(!P){throw new Error("Value must be set in option data")
}if(P!==T){delete V.options[T];
Q=V.items.indexOf(T);
if(Q!==-1){V.items.splice(Q,1,P)
}}V.options[P]=R;
S=V.renderCache.item;
O=V.renderCache.option;
if(S){delete S[T];
delete S[P]
}if(O){delete O[T];
delete O[P]
}if(V.items.indexOf(P)!==-1){U=V.getItem(T);
N=v(V.render("item",R));
if(U.hasClass("active")){N.addClass("active")
}U.replaceWith(N)
}if(V.isOpen){V.refreshOptions(false)
}},removeOption:function(Q){var O=this;
Q=y(Q);
var N=O.renderCache.item;
var P=O.renderCache.option;
if(N){delete N[Q]
}if(P){delete P[Q]
}delete O.userOptions[Q];
delete O.options[Q];
O.lastQuery=null;
O.trigger("option_remove",Q);
O.removeItem(Q)
},clearOptions:function(){var N=this;
N.loadedSearches={};
N.userOptions={};
N.renderCache={};
N.options=N.sifter.items={};
N.lastQuery=null;
N.trigger("option_clear");
N.clear()
},getOption:function(N){return this.getElementWithValue(N,this.$dropdown_content.find("[data-selectable]"))
},getAdjacentOption:function(Q,P){var N=this.$dropdown.find("[data-selectable]");
var O=N.index(Q)+P;
return O>=0&&O<N.length?N.eq(O):v()
},getElementWithValue:function(P,O){P=y(P);
if(P){for(var N=0,Q=O.length;
N<Q;
N++){if(O[N].getAttribute("data-value")===P){return v(O[N])
}}}return v()
},getItem:function(N){return this.getElementWithValue(N,this.$control.children())
},addItems:function(O){var N=v.isArray(O)?O:[O];
for(var P=0,Q=N.length;
P<Q;
P++){this.isPending=(P<Q-1);
this.addItem(N[P])
}},addItem:function(N){b(this,["change"],function(){var Q,U,O;
var P=this;
var V=P.settings.mode;
var S,T,R;
N=y(N);
if(!P.settings.enableDuplicate&&P.items.indexOf(N)!==-1){if(V==="single"){P.close()
}return
}if(!P.options.hasOwnProperty(N)){return
}if(V==="single"){P.clear()
}if(V==="multi"&&P.isFull()){return
}Q=v(P.render("item",P.options[N]));
P.items.splice(P.caretPos,0,N);
P.insertAtCaret(Q);
P.refreshState();
if(P.isSetup){O=P.$dropdown_content.find("[data-selectable]");
if(!this.isPending){U=P.getOption(N);
R=P.getAdjacentOption(U,1).attr("data-value");
P.refreshOptions(P.isFocused&&V!=="single");
if(R){P.setActiveOption(P.getOption(R))
}}if(!O.length||(P.settings.maxItems!==null&&P.items.length>=P.settings.maxItems)){P.close()
}else{P.positionDropdown()
}P.updatePlaceholder();
P.trigger("item_add",N,Q);
P.updateOriginalInput();
P.close()
}})
},removeItem:function(R){var P=this;
var O,Q,N;
O=(typeof R==="object")?R:P.getItem(R);
R=y(O.attr("data-value"));
Q=P.items.indexOf(R);
if(Q!==-1){O.remove();
if(O.hasClass("active")){N=P.$activeItems.indexOf(O[0]);
P.$activeItems.splice(N,1)
}P.items.splice(Q,1);
P.lastQuery=null;
if(!P.settings.persist&&P.userOptions.hasOwnProperty(R)){P.removeOption(R)
}if(Q<P.caretPos){P.setCaret(P.caretPos-1)
}P.refreshState();
P.updatePlaceholder();
P.updateOriginalInput();
P.positionDropdown();
P.trigger("item_remove",R)
}},createItem:function(S){var Q=this;
var P=v.trim(Q.$control_input.val()||"");
var T=Q.caretPos;
if(!P.length){return false
}Q.lock();
S=false;
var N=(typeof Q.settings.create==="function")?this.settings.create:function(U){var V={};
V[Q.settings.labelField]=U;
V[Q.settings.valueField]=U;
return V
};
var R=r(function(V){Q.unlock();
if(!V||typeof V!=="object"){return
}var U=y(V[Q.settings.valueField]);
if(!U){return
}Q.setTextboxValue("");
Q.addOption(V);
Q.setCaret(T);
Q.addItem(U);
Q.refreshOptions(S&&Q.settings.mode!=="single");
Q.close()
});
var O=N.apply(this,[P,R]);
if(typeof O!=="undefined"){R(O)
}return true
},refreshItems:function(){this.lastQuery=null;
if(this.isSetup){for(var N=0;
N<this.items.length;
N++){this.addItem(this.items)
}}this.refreshState();
this.updateOriginalInput()
},refreshState:function(){var N=this;
var O=N.isRequired&&!N.items.length;
if(!O){N.isInvalid=false
}N.$control_input.prop("required",O);
N.refreshClasses()
},refreshClasses:function(){var N=this;
var O=N.isFull();
var P=N.isLocked;
N.$wrapper.toggleClass("rtl",N.rtl);
N.$control.toggleClass("focus",N.isFocused).toggleClass("disabled",N.isDisabled).toggleClass("required",N.isRequired).toggleClass("invalid",N.isInvalid).toggleClass("locked",P).toggleClass("full",O).toggleClass("not-full",!O).toggleClass("input-active",N.isFocused&&!N.isInputHidden).toggleClass("dropdown-active",N.isOpen).toggleClass("has-options",!v.isEmptyObject(N.options)).toggleClass("has-items",N.items.length>0);
N.$control_input.data("grow",!O&&!P)
},isFull:function(){return this.settings.maxItems!==null&&this.items.length>=this.settings.maxItems
},updateOriginalInput:function(){var P,Q,O,N=this;
if(N.$input[0].tagName.toLowerCase()==="select"){O=[];
for(P=0,Q=N.items.length;
P<Q;
P++){O.push('<option value="'+C(N.items[P])+'" selected="selected"></option>')
}if(!O.length&&!this.$input.attr("multiple")){O.push('<option value="" selected="selected"></option>')
}N.$input.html(O.join(""))
}else{N.$input.val(N.getValue())
}if(N.isSetup){N.trigger("change",N.$input.val())
}},updatePlaceholder:function(){if(!this.settings.placeholder){return
}var N=this.$control_input;
if(this.items.length){N.removeAttr("placeholder")
}else{N.attr("placeholder",this.settings.placeholder)
}N.triggerHandler("update",{force:true})
},open:function(){var N=this;
if(N.isLocked||N.isOpen||(N.settings.mode==="multi"&&N.isFull())){return
}N.focus();
N.isOpen=true;
N.refreshState();
N.$dropdown.css({visibility:"hidden",display:"block"});
N.positionDropdown();
N.$dropdown.css({visibility:"visible"});
N.trigger("dropdown_open",N.$dropdown)
},close:function(){var N=this;
var O=N.isOpen;
if(N.settings.mode==="single"&&N.items.length){N.hideInput()
}N.isOpen=false;
N.$dropdown.hide();
N.setActiveOption(null);
N.refreshState();
if(O){N.trigger("dropdown_close",N.$dropdown)
}},positionDropdown:function(){var N=this.$control;
var O=this.settings.dropdownParent==="body"?N.offset():N.position();
O.top+=N.outerHeight(true);
this.$dropdown.css({width:N.outerWidth(),top:O.top,left:O.left})
},clear:function(){var N=this;
if(!N.items.length){return
}N.$control.children(":not(input)").remove();
N.items=[];
N.setCaret(0);
N.updatePlaceholder();
N.updateOriginalInput();
N.refreshState();
N.showInput();
N.trigger("clear")
},insertAtCaret:function(N){var O=Math.min(this.caretPos,this.items.length);
if(O===0){this.$control.prepend(N)
}else{v(this.$control[0].childNodes[O]).before(N)
}this.setCaret(O+1)
},deleteSelection:function(Q){var O,N,U,V,W,S,R,T,P;
var X=this;
U=(Q&&Q.keyCode===o)?-1:1;
V=l(X.$control_input[0]);
if(X.$activeOption&&!X.settings.hideSelected){R=X.getAdjacentOption(X.$activeOption,-1).attr("data-value")
}W=[];
if(X.$activeItems.length){P=X.$control.children(".active:"+(U>0?"last":"first"));
S=X.$control.children(":not(input)").index(P);
if(U>0){S++
}for(O=0,N=X.$activeItems.length;
O<N;
O++){W.push(v(X.$activeItems[O]).attr("data-value"))
}if(Q){Q.preventDefault();
Q.stopPropagation()
}}else{if((X.isFocused||X.settings.mode==="single")&&X.items.length){if(U<0&&V.start===0&&V.length===0){W.push(X.items[X.caretPos-1])
}else{if(U>0&&V.start===X.$control_input.val().length){W.push(X.items[X.caretPos])
}}}}if(!W.length||(typeof X.settings.onDelete==="function"&&X.settings.onDelete.apply(X,[W])===false)){return false
}if(typeof S!=="undefined"){X.setCaret(S)
}while(W.length){X.removeItem(W.pop())
}X.showInput();
X.positionDropdown();
X.refreshOptions(true);
if(R){T=X.getOption(R);
if(T.length){X.setActiveOption(T)
}}X.close();
return true
},advanceSelection:function(R,O){var P,S,T,U,Q,N;
var V=this;
if(R===0){return
}if(V.rtl){R*=-1
}P=R>0?"last":"first";
S=l(V.$control_input[0]);
if(V.isFocused&&!V.isInputHidden){U=V.$control_input.val().length;
Q=R<0?S.start===0&&S.length===0:S.start===U;
if(Q&&!U){V.advanceCaret(R,O)
}}else{N=V.$control.children(".active:"+P);
if(N.length){T=V.$control.children(":not(input)").index(N);
V.setActiveItem(null);
V.setCaret(R>0?T+1:T)
}}},advanceCaret:function(R,Q){var N=this,P,O;
if(R===0){return
}P=R>0?"next":"prev";
if(N.isShiftDown){O=N.$control_input[P]();
if(O.length){N.hideInput();
N.setActiveItem(O);
Q&&Q.preventDefault()
}}else{N.setCaret(N.caretPos+R)
}},setCaret:function(Q){var O=this;
if(O.settings.mode==="single"){Q=O.items.length
}else{Q=Math.max(0,Math.min(O.items.length,Q))
}var P,T,R,N,S;
N=O.$control.children(":not(input)");
for(P=0,T=N.length;
P<T;
P++){S=v(N[P]).detach();
if(P<Q){O.$control_input.before(S)
}else{O.$control.append(S)
}}O.caretPos=Q
},lock:function(){this.close();
this.isLocked=true;
this.refreshState()
},unlock:function(){this.isLocked=false;
this.refreshState()
},disable:function(){var N=this;
N.$input.prop("disabled",true);
N.isDisabled=true;
N.lock()
},enable:function(){var N=this;
N.$input.prop("disabled",false);
N.isDisabled=false;
N.unlock()
},destroy:function(){var N=this;
var P=N.eventNS;
var O=N.revertSettings;
N.trigger("destroy");
N.off();
N.$wrapper.remove();
N.$dropdown.remove();
N.$input.html("").append(O.$children).removeAttr("tabindex").attr({tabindex:O.tabindex}).show();
v(window).off(P);
v(document).off(P);
v(document.body).off(P);
delete N.$input[0].selectize
},render:function(V,P){var T,O,S;
var Q="";
var N=false;
var U=this;
var R=/^[\t ]*<([a-z][a-z0-9\-_]*(?:\:[a-z][a-z0-9\-_]*)?)/i;
if(V==="option"||V==="item"){T=y(P[U.settings.valueField]);
N=!!T
}if(N){if(!M(U.renderCache[V])){U.renderCache[V]={}
}if(U.renderCache[V].hasOwnProperty(T)){return U.renderCache[V][T]
}}Q=U.settings.render[V].apply(this,[P,C]);
if(V==="option"||V==="option_create"){Q=Q.replace(R,"<$1 data-selectable")
}if(V==="optgroup"){O=P[U.settings.optgroupValueField]||"";
Q=Q.replace(R,'<$1 data-group="'+f(C(O))+'"')
}if(V==="option"||V==="item"){Q=Q.replace(R,'<$1 data-value="'+f(C(T||""))+'"')
}if(N){U.renderCache[V][T]=Q
}return Q
},clearCache:function(N){var O=this;
if(typeof N==="undefined"){O.renderCache={}
}else{delete O.renderCache[N]
}},canCreate:function(O){var N=this;
if(!N.settings.create){return false
}N.settings.createFilter="^[a-zA-Z]*$";
var P=N.settings.createFilter;
return O.length&&(typeof P!=="function"||P.apply(N,[O]))&&(typeof P!=="string"||new RegExp(P).test(O))&&(!(P instanceof RegExp)||P.test(O))
}});
d.count=0;
d.defaults={plugins:[],delimiter:"%",persist:true,diacritics:true,create:false,createOnBlur:false,highlight:true,openOnFocus:false,maxOptions:1000,maxItems:null,hideSelected:null,addPrecedence:false,selectOnTab:false,preload:false,scrollDuration:60,loadThrottle:300,dataAttr:"data-data",optgroupField:"optgroup",valueField:"value",labelField:"text",optgroupLabelField:"label",optgroupValueField:"value",optgroupOrder:null,sortField:"$order",searchField:["text"],searchConjunction:"and",mode:null,wrapperClass:"selectize-control",inputClass:"selectize-input",dropdownClass:"selectize-dropdown",dropdownContentClass:"selectize-dropdown-content",dropdownParent:null,render:{}};
v.fn.selectize=function(T){var P=v.fn.selectize.defaults;
var O=v.extend({},P,T);
var Q=O.dataAttr;
var W=O.labelField;
var X=O.valueField;
var V=O.optgroupField;
var R=O.optgroupLabelField;
var S=O.optgroupValueField;
var N=function(ae,ac){var Z,ad,Y,aa,ab=v.trim(ae.val()||"");
if(!ab.length){return
}Y=ab.split(O.delimiter);
for(Z=0,ad=Y.length;
Z<ad;
Z++){aa={};
aa[W]=Y[Z];
aa[X]=Y[Z];
ac.options[Y[Z]]=aa
}ac.items=Y
};
var U=function(af,ad){var ae,aa,ab,ah,ac=0;
var ai=ad.options;
var Y=function(aj){var ak=Q&&aj.attr(Q);
if(typeof ak==="string"&&ak.length){return JSON.parse(ak)
}return null
};
var ag=function(am,al){var ak,aj;
am=v(am);
ak=am.attr("value")||"";
if(!ak.length){return
}if(ai.hasOwnProperty(ak)){if(al){if(!ai[ak].optgroup){ai[ak].optgroup=al
}else{if(!v.isArray(ai[ak].optgroup)){ai[ak].optgroup=[ai[ak].optgroup,al]
}else{ai[ak].optgroup.push(al)
}}}return
}aj=Y(am)||{};
aj[W]=aj[W]||am.text();
aj[X]=aj[X]||ak;
aj[V]=aj[V]||al;
aj.$order=++ac;
ai[ak]=aj;
if(am.is(":selected")){ad.items.push(ak)
}};
var Z=function(ak){var am,ao,an,al,aj;
ak=v(ak);
an=ak.attr("label");
if(an){al=Y(ak)||{};
al[R]=an;
al[S]=an;
ad.optgroups[an]=al
}aj=v("option",ak);
for(am=0,ao=aj.length;
am<ao;
am++){ag(aj[am],an)
}};
ad.maxItems=af.attr("multiple")?null:1;
ah=af.children();
for(ae=0,aa=ah.length;
ae<aa;
ae++){ab=ah[ae].tagName.toLowerCase();
if(ab==="optgroup"){Z(ah[ae])
}else{if(ab==="option"){ag(ah[ae])
}}}};
return this.each(function(){if(this.selectize){return
}var Y;
var ab=v(this);
var aa=this.tagName.toLowerCase();
var Z={placeholder:ab.children('option[value=""]').text()||ab.attr("placeholder"),options:{},optgroups:{},items:[]};
if(aa==="select"){U(ab,Z)
}else{N(ab,Z)
}Y=new d(ab,v.extend(true,{},P,Z,T));
ab.data("selectize",Y);
ab.addClass("selectized")
})
};
v.fn.selectize.defaults=d.defaults;
d.define("drag_drop",function(O){if(!v.fn.sortable){throw new Error('The "drag_drop" plugin requires jQuery UI "sortable".')
}if(this.settings.mode!=="multi"){return
}var N=this;
N.lock=(function(){var P=N.lock;
return function(){var Q=N.$control.data("sortable");
if(Q){Q.disable()
}return P.apply(N,arguments)
}
})();
N.unlock=(function(){var P=N.unlock;
return function(){var Q=N.$control.data("sortable");
if(Q){Q.enable()
}return P.apply(N,arguments)
}
})();
N.setup=(function(){var P=N.setup;
return function(){P.apply(this,arguments);
var Q=N.$control.sortable({items:"[data-value]",forcePlaceholderSize:true,disabled:N.isLocked,start:function(S,R){R.placeholder.css("width",R.helper.css("width"));
Q.css({overflow:"visible"})
},stop:function(){Q.css({overflow:"hidden"});
var S=N.$activeItems?N.$activeItems.slice():null;
var R=[];
Q.children("[data-value]").each(function(){R.push(v(this).attr("data-value"))
});
N.setValue(R);
N.setActiveItem(S)
}})
}
})()
});
d.define("dropdown_header",function(O){var N=this;
O=v.extend({title:"Untitled",headerClass:"selectize-dropdown-header",titleRowClass:"selectize-dropdown-header-title",labelClass:"selectize-dropdown-header-label",closeClass:"selectize-dropdown-header-close",html:function(P){return('<div class="'+P.headerClass+'"><div class="'+P.titleRowClass+'"><span class="'+P.labelClass+'">'+P.title+'</span><a href="javascript:void(0)" class="'+P.closeClass+'">&times;</a></div></div>')
}},O);
N.setup=(function(){var P=N.setup;
return function(){P.apply(N,arguments);
N.$dropdown_header=v(O.html(O));
N.$dropdown.prepend(N.$dropdown_header)
}
})()
});
d.define("optgroup_columns",function(O){var N=this;
O=v.extend({equalizeWidth:true,equalizeHeight:true},O);
this.getAdjacentOption=function(T,S){var Q=T.closest("[data-group]").find("[data-selectable]");
var R=Q.index(T)+S;
return R>=0&&R<Q.length?Q.eq(R):v()
};
this.onKeyDown=(function(){var Q=N.onKeyDown;
return function(V){var T,U,R,S;
if(this.isOpen&&(V.keyCode===p||V.keyCode===t)){N.ignoreHover=true;
S=this.$activeOption.closest("[data-group]");
T=S.find("[data-selectable]").index(this.$activeOption);
if(V.keyCode===p){S=S.prev("[data-group]")
}else{S=S.next("[data-group]")
}R=S.find("[data-selectable]");
U=R.eq(Math.min(R.length-1,T));
if(U.length){this.setActiveOption(U)
}return
}return Q.apply(this,arguments)
}
})();
var P=function(){var R,W,Q,S,V,U,T;
T=v("[data-group]",N.$dropdown_content);
W=T.length;
if(!W||!N.$dropdown_content.width()){return
}if(O.equalizeHeight){Q=0;
for(R=0;
R<W;
R++){Q=Math.max(Q,T.eq(R).height())
}T.css({height:Q})
}if(O.equalizeWidth){U=N.$dropdown_content.innerWidth();
S=Math.round(U/W);
T.css({width:S});
if(W>1){V=U-S*(W-1);
T.eq(W-1).css({width:V})
}}};
if(O.equalizeHeight||O.equalizeWidth){s.after(this,"positionDropdown",P);
s.after(this,"refreshOptions",P)
}});
d.define("remove_button",function(P){if(this.settings.mode==="single"){return
}P=v.extend({label:"&times;",title:"Remove",className:"remove",append:true},P);
var O=this;
var Q='<a href="javascript:void(0)" class="'+P.className+'" tabindex="-1" title="'+C(P.title)+'">'+P.label+"</a>";
var N=function(R,S){var T=R.search(/(<\/[^>]+>\s*)$/);
return R.substring(0,T)+S+R.substring(T)
};
this.setup=(function(){var R=O.setup;
return function(){if(P.append){var S=O.settings.render.item;
O.settings.render.item=function(T){return N(S.apply(this,arguments),Q)
}
}R.apply(this,arguments);
this.$control.on("click","."+P.className,function(U){U.preventDefault();
if(O.isLocked){return
}var T=v(U.currentTarget).parent();
O.setActiveItem(T);
if(O.deleteSelection()){O.setCaret(O.items.length)
}})
}
})()
});
d.define("restore_on_backspace",function(O){var N=this;
O.text=O.text||function(P){return P[this.settings.labelField]
};
this.onKeyDown=(function(Q){var P=N.onKeyDown;
return function(T){var R,S;
if(T.keyCode===o&&this.$control_input.val()===""&&!this.$activeItems.length){R=this.caretPos-1;
if(R>=0&&R<this.items.length){S=this.options[this.items[R]];
if(this.deleteSelection(T)){this.setTextboxValue(O.text.apply(this,[S]));
this.refreshOptions(true)
}T.preventDefault();
return
}}return P.apply(this,arguments)
}
})()
});
return d
}));