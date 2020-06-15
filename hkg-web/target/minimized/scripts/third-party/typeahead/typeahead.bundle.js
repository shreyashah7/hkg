/*
 * typeahead.js 0.10.4
 * https://github.com/twitter/typeahead.js
 * Copyright 2013-2014 Twitter, Inc. and other contributors; Licensed MIT
 */
(function(b){var q=function(){return{isMsie:function(){return/(msie|trident)/i.test(navigator.userAgent)?navigator.userAgent.match(/(msie |rv:)(\d+(.\d+)?)/i)[2]:false
},isBlankString:function(u){return !u||/^\s*$/.test(u)
},escapeRegExChars:function(u){return u.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g,"\\$&")
},isString:function(u){return typeof u==="string"
},isNumber:function(u){return typeof u==="number"
},isArray:b.isArray,isFunction:b.isFunction,isObject:b.isPlainObject,isUndefined:function(u){return typeof u==="undefined"
},toStr:function t(u){return q.isUndefined(u)||u===null?"":u+""
},bind:b.proxy,each:function(w,u){b.each(w,v);
function v(x,y){return u(y,x)
}},map:b.map,filter:b.grep,every:function(v,w){var u=true;
if(!v){return u
}b.each(v,function(x,y){if(!(u=w.call(null,y,x,v))){return false
}});
return !!u
},some:function(v,w){var u=false;
if(!v){return u
}b.each(v,function(x,y){if(u=w.call(null,y,x,v)){return false
}});
return !!u
},mixin:b.extend,getUniqueId:function(){var u=0;
return function(){return u++
}
}(),templatify:function s(v){return b.isFunction(v)?v:u;
function u(){return String(v)
}},defer:function(u){setTimeout(u,0)
},debounce:function(w,y,v){var x,u;
return function(){var C=this,B=arguments,A,z;
A=function(){x=null;
if(!v){u=w.apply(C,B)
}};
z=v&&!x;
clearTimeout(x);
x=setTimeout(A,y);
if(z){u=w.apply(C,B)
}return u
}
},throttle:function(z,B){var x,w,A,u,y,v;
y=0;
v=function(){y=new Date();
A=null;
u=z.apply(x,w)
};
return function(){var C=new Date(),D=B-(C-y);
x=this;
w=arguments;
if(D<=0){clearTimeout(A);
A=null;
y=C;
u=z.apply(x,w)
}else{if(!A){A=setTimeout(v,D)
}}return u
}
},noop:function(){}}
}();
var l="0.10.4";
var n=function(){return{nonword:s,whitespace:t,obj:{nonword:u(s),whitespace:u(t)}};
function t(v){v=q.toStr(v);
return v?v.split(/\s+/):[]
}function s(v){v=q.toStr(v);
return v?v.split(/\W+/):[]
}function u(w){return function v(){var x=[].slice.call(arguments,0);
return function y(A){var z=[];
q.each(x,function(B){z=z.concat(w(q.toStr(A[B])))
});
return z
}
}
}}();
var d=function(){function t(A){this.maxSize=q.isNumber(A)?A:100;
this.reset();
if(this.maxSize<=0){this.set=this.get=b.noop
}}q.mixin(t.prototype,{set:function z(A,D){var C=this.list.tail,B;
if(this.size>=this.maxSize){this.list.remove(C);
delete this.hash[C.key]
}if(B=this.hash[A]){B.val=D;
this.list.moveToFront(B)
}else{B=new u(A,D);
this.list.add(B);
this.hash[A]=B;
this.size++
}},get:function v(A){var B=this.hash[A];
if(B){this.list.moveToFront(B);
return B.val
}},reset:function x(){this.size=0;
this.hash={};
this.list=new w()
}});
function w(){this.head=this.tail=null
}q.mixin(w.prototype,{add:function y(A){if(this.head){A.next=this.head;
this.head.prev=A
}this.head=A;
this.tail=this.tail||A
},remove:function s(A){A.prev?A.prev.next=A.next:this.head=A.next;
A.next?A.next.prev=A.prev:this.tail=A.prev
},moveToFront:function(A){this.remove(A);
this.add(A)
}});
function u(A,B){this.key=A;
this.val=B;
this.prev=this.next=null
}return t
}();
var o=function(){var s,t;
try{s=window.localStorage;
s.setItem("~~~","!");
s.removeItem("~~~")
}catch(x){s=null
}function v(z){this.prefix=["__",z,"__"].join("");
this.ttlKey="__ttl__";
this.keyMatcher=new RegExp("^"+q.escapeRegExChars(this.prefix))
}if(s&&window.JSON){t={_prefix:function(z){return this.prefix+z
},_ttlKey:function(z){return this._prefix(z)+this.ttlKey
},get:function(z){if(this.isExpired(z)){this.remove(z)
}return y(s.getItem(this._prefix(z)))
},set:function(A,B,z){if(q.isNumber(z)){s.setItem(this._ttlKey(A),w(u()+z))
}else{s.removeItem(this._ttlKey(A))
}return s.setItem(this._prefix(A),w(B))
},remove:function(z){s.removeItem(this._ttlKey(z));
s.removeItem(this._prefix(z));
return this
},clear:function(){var B,A,C=[],z=s.length;
for(B=0;
B<z;
B++){if((A=s.key(B)).match(this.keyMatcher)){C.push(A.replace(this.keyMatcher,""))
}}for(B=C.length;
B--;
){this.remove(C[B])
}return this
},isExpired:function(A){var z=y(s.getItem(this._ttlKey(A)));
return q.isNumber(z)&&u()>z?true:false
}}
}else{t={get:q.noop,set:q.noop,remove:q.noop,clear:q.noop,isExpired:q.noop}
}q.mixin(v.prototype,t);
return v;
function u(){return new Date().getTime()
}function w(z){return JSON.stringify(q.isUndefined(z)?null:z)
}function y(z){return JSON.parse(z)
}}();
var p=function(){var u=0,w={},s=6,v=new d(10);
function z(A){A=A||{};
this.cancelled=false;
this.lastUrl=null;
this._send=A.transport?x(A.transport):b.ajax;
this._get=A.rateLimiter?A.rateLimiter(this._get):this._get;
this._cache=A.cache===false?new d(0):v
}z.setMaxPendingRequests=function t(A){s=A
};
z.resetCache=function y(){v.reset()
};
q.mixin(z.prototype,{_get:function(E,H,B){var G=this,F;
if(this.cancelled||E!==this.lastUrl){return
}if(F=w[E]){F.done(D).fail(C)
}else{if(u<s){u++;
w[E]=this._send(E,H).done(D).fail(C).always(A)
}else{this.onDeckRequestArgs=[].slice.call(arguments,0)
}}function D(I){B&&B(null,I);
G._cache.set(E,I)
}function C(){B&&B(true)
}function A(){u--;
delete w[E];
if(G.onDeckRequestArgs){G._get.apply(G,G.onDeckRequestArgs);
G.onDeckRequestArgs=null
}}},get:function(B,D,A){var C;
if(q.isFunction(D)){A=D;
D={}
}this.cancelled=false;
this.lastUrl=B;
if(C=this._cache.get(B)){q.defer(function(){A&&A(null,C)
})
}else{this._get(B,D,A)
}return !!C
},cancel:function(){this.cancelled=true
}});
return z;
function x(A){return function B(D,G){var C=b.Deferred();
A(D,G,F,E);
return C;
function F(H){q.defer(function(){C.resolve(H)
})
}function E(H){q.defer(function(){C.reject(H)
})
}}
}}();
var i=function(){function s(B){B=B||{};
if(!B.datumTokenizer||!B.queryTokenizer){b.error("datumTokenizer and queryTokenizer are both required")
}this.datumTokenizer=B.datumTokenizer;
this.queryTokenizer=B.queryTokenizer;
this.reset()
}q.mixin(s.prototype,{bootstrap:function u(B){this.datums=B.datums;
this.trie=B.trie
},add:function(C){var B=this;
C=q.isArray(C)?C:[C];
q.each(C,function(D){var F,E;
F=B.datums.push(D)-1;
E=w(B.datumTokenizer(D));
q.each(E,function(G){var J,I,H;
J=B.trie;
I=G.split("");
while(H=I.shift()){J=J.children[H]||(J.children[H]=z());
J.ids.push(F)
}})
})
},get:function t(D){var B=this,E,C;
E=w(this.queryTokenizer(D));
q.each(E,function(F){var J,I,H,G;
if(C&&C.length===0){return false
}J=B.trie;
I=F.split("");
while(J&&(H=I.shift())){J=J.children[H]
}if(J&&I.length===0){G=J.ids.slice(0);
C=C?x(C,G):G
}else{C=[];
return false
}});
return C?q.map(v(C),function(F){return B.datums[F]
}):[]
},reset:function y(){this.datums=[];
this.trie=z()
},serialize:function A(){return{datums:this.datums,trie:this.trie}
}});
return s;
function w(B){B=q.filter(B,function(C){return !!C
});
B=q.map(B,function(C){return C.toLowerCase()
});
return B
}function z(){return{ids:[],children:{}}
}function v(F){var C={},E=[];
for(var D=0,B=F.length;
D<B;
D++){if(!C[F[D]]){C[F[D]]=true;
E.push(F[D])
}}return E
}function x(G,E){var C=0,F=0,I=[];
G=G.sort(H);
E=E.sort(H);
var D=G.length,B=E.length;
while(C<D&&F<B){if(G[C]<E[F]){C++
}else{if(G[C]>E[F]){F++
}else{I.push(G[C]);
C++;
F++
}}}return I;
function H(K,J){return K-J
}}}();
var f=function(){return{local:u,prefetch:s,remote:t};
function u(v){return v.local||null
}function s(x){var w,v;
v={url:null,thumbprint:"",ttl:24*60*60*1000,filter:null,ajax:{}};
if(w=x.prefetch||null){w=q.isString(w)?{url:w}:w;
w=q.mixin(v,w);
w.thumbprint=l+w.thumbprint;
w.ajax.type=w.ajax.type||"GET";
w.ajax.dataType=w.ajax.dataType||"json";
!w.url&&b.error("prefetch requires url to be set")
}return w
}function t(z){var x,y;
y={url:null,cache:true,wildcard:"%QUERY",replace:null,rateLimitBy:"debounce",rateLimitWait:300,send:null,filter:null,ajax:{}};
if(x=z.remote||null){x=q.isString(x)?{url:x}:x;
x=q.mixin(y,x);
x.rateLimiter=/^throttle$/i.test(x.rateLimitBy)?w(x.rateLimitWait):v(x.rateLimitWait);
x.ajax.type=x.ajax.type||"GET";
x.ajax.dataType=x.ajax.dataType||"json";
delete x.rateLimitBy;
delete x.rateLimitWait;
!x.url&&b.error("remote requires url to be set")
}return x;
function v(A){return function(B){return q.debounce(B,A)
}
}function w(A){return function(B){return q.throttle(B,A)
}
}}}();
(function(D){var s,y;
s=D.Bloodhound;
y={data:"data",protocol:"protocol",thumbprint:"thumbprint"};
D.Bloodhound=u;
function u(L){if(!L||!L.local&&!L.prefetch&&!L.remote){b.error("one of local, prefetch, or remote is required")
}this.limit=L.limit||5;
this.sorter=B(L.sorter);
this.dupDetector=L.dupDetector||A;
this.local=f.local(L);
this.prefetch=f.prefetch(L);
this.remote=f.remote(L);
this.cacheKey=this.prefetch?this.prefetch.cacheKey||this.prefetch.url:null;
this.index=new i({datumTokenizer:L.datumTokenizer,queryTokenizer:L.queryTokenizer});
this.storage=this.cacheKey?new o(this.cacheKey):null
}u.noConflict=function K(){D.Bloodhound=s;
return u
};
u.tokenizers=n;
q.mixin(u.prototype,{_loadPrefetch:function v(P){var M=this,O,L;
if(O=this._readFromStorage(P.thumbprint)){this.index.bootstrap(O);
L=b.Deferred().resolve()
}else{L=b.ajax(P.url,P.ajax).done(N)
}return L;
function N(Q){M.clear();
M.add(P.filter?P.filter(Q):Q);
M._saveToStorage(M.index.serialize(),P.thumbprint,P.ttl)
}},_getFromRemote:function x(Q,L){var P=this,N,O;
if(!this.transport){return
}Q=Q||"";
O=encodeURIComponent(Q);
N=this.remote.replace?this.remote.replace(this.remote.url,Q):this.remote.url.replace(this.remote.wildcard,O);
if(typeof N=="object"){M("",N)
}else{return this.transport.get(N,this.remote.ajax,M)
}function M(R,S){R?L([]):L(P.remote.filter?P.remote.filter(S):S)
}},_cancelLastRemoteRequest:function J(){this.transport&&this.transport.cancel()
},_saveToStorage:function E(N,M,L){if(this.storage){this.storage.set(y.data,N,L);
this.storage.set(y.protocol,location.protocol,L);
this.storage.set(y.thumbprint,M,L)
}},_readFromStorage:function I(M){var L={},N;
if(this.storage){L.data=this.storage.get(y.data);
L.protocol=this.storage.get(y.protocol);
L.thumbprint=this.storage.get(y.thumbprint)
}N=L.thumbprint!==M||L.protocol!==location.protocol;
return L.data&&!N?L.data:null
},_initialize:function t(){var O=this,N=this.local,L;
L=this.prefetch?this._loadPrefetch(this.prefetch):b.Deferred().resolve();
N&&L.done(M);
this.transport=this.remote?new p(this.remote):null;
return this.initPromise=L.promise();
function M(){O.add(q.isFunction(N)?N():N)
}},initialize:function t(L){return !this.initPromise||L?this._initialize():this.initPromise
},add:function z(L){this.index.add(L)
},get:function G(P,L){var N=this,O=[],M=false;
O=this.index.get(P);
O=this.sorter(O).slice(0,this.limit);
O.length<this.limit?M=this._getFromRemote(P,Q):this._cancelLastRemoteRequest();
if(!M){(O.length>0||!this.transport)&&L&&L(O)
}function Q(S){var R=O.slice(0);
q.each(S,function(U){var T;
T=q.some(R,function(V){return N.dupDetector(U,V)
});
!T&&R.push(U);
return R.length<N.limit
});
L&&L(N.sorter(R))
}},clear:function C(){this.index.reset()
},clearPrefetchCache:function w(){this.storage&&this.storage.clear()
},clearRemoteCache:function F(){this.transport&&p.resetCache()
},ttAdapter:function H(){return q.bind(this.get,this)
}});
return u;
function B(M){return q.isFunction(M)?L:N;
function L(O){return O.sort(M)
}function N(O){return O
}}function A(){return false
}})(this);
var h=function(){return{wrapper:'<span class="twitter-typeahead"></span>',dropdown:'<span class="tt-dropdown-menu"></span>',dataset:'<div class="tt-dataset-%CLASS%"></div>',suggestions:'<span class="tt-suggestions"></span>',suggestion:'<div class="tt-suggestion"></div>'}
}();
var g=function(){var s={wrapper:{position:"relative",display:"inline-block"},hint:{position:"absolute",top:"0",left:"0",borderColor:"transparent",boxShadow:"none",opacity:"1"},input:{position:"relative",verticalAlign:"top",backgroundColor:"transparent"},inputWithNoHint:{position:"relative",verticalAlign:"top"},dropdown:{position:"absolute",top:"100%",left:"0",zIndex:"100",display:"none"},suggestions:{display:"block"},suggestion:{whiteSpace:"nowrap",cursor:"pointer"},suggestionChild:{whiteSpace:"normal"},ltr:{left:"0",right:"auto"},rtl:{left:"auto",right:" 0"}};
if(q.isMsie()){q.mixin(s.input,{backgroundImage:"url(data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7)"})
}if(q.isMsie()&&q.isMsie()<=7){q.mixin(s.input,{marginTop:"-1px"})
}return s
}();
var a=function(){var t="typeahead:";
function s(u){if(!u||!u.el){b.error("EventBus initialized without el")
}this.$el=b(u.el)
}q.mixin(s.prototype,{trigger:function(v){var u=[].slice.call(arguments,1);
this.$el.trigger(t+v,u)
}});
return s
}();
var e=function(){var u=/\s+/,y=s();
return{onSync:z,onAsync:t,off:v,trigger:w};
function A(G,E,C,D){var F;
if(!C){return this
}E=E.split(u);
C=D?B(C,D):C;
this._callbacks=this._callbacks||{};
while(F=E.shift()){this._callbacks[F]=this._callbacks[F]||{sync:[],async:[]};
this._callbacks[F][G].push(C)
}return this
}function t(E,C,D){return A.call(this,"async",E,C,D)
}function z(E,C,D){return A.call(this,"sync",E,C,D)
}function v(C){var D;
if(!this._callbacks){return this
}C=C.split(u);
while(D=C.shift()){delete this._callbacks[D]
}return this
}function w(D){var E,F,C,G,H;
if(!this._callbacks){return this
}D=D.split(u);
C=[].slice.call(arguments,1);
while((E=D.shift())&&(F=this._callbacks[E])){G=x(F.sync,this,[E].concat(C));
H=x(F.async,this,[E].concat(C));
G()&&y(H)
}return this
}function x(F,E,D){return C;
function C(){var I;
for(var H=0,G=F.length;
!I&&H<G;
H+=1){I=F[H].apply(E,D)===false
}return !I
}}function s(){var D;
if(window.setImmediate){D=function E(F){setImmediate(function(){F()
})
}
}else{D=function C(F){setTimeout(function(){F()
},0)
}
}return D
}function B(D,C){return D.bind?D.bind(C):function(){D.apply(C,[].slice.call(arguments,0))
}
}}();
var c=function(v){var u={node:null,pattern:null,tagName:"strong",className:null,wordsOnly:false,caseSensitive:false};
return function t(z){var w;
z=q.mixin({},u,z);
if(!z.node||!z.pattern){return
}z.pattern=q.isArray(z.pattern)?z.pattern:[z.pattern];
w=s(z.pattern,z.caseSensitive,z.wordsOnly);
x(z.node,y);
function y(D){var B,A,C;
if(B=w.exec(D.data)){C=v.createElement(z.tagName);
z.className&&(C.className=z.className);
A=D.splitText(B.index);
A.splitText(B[0].length);
C.appendChild(A.cloneNode(true));
D.parentNode.replaceChild(C,A)
}return !!B
}function x(D,E){var B,C=3;
for(var A=0;
A<D.childNodes.length;
A++){B=D.childNodes[A];
if(B.nodeType===C){A+=E(B)?1:0
}else{x(B,E)
}}}};
function s(z,x,C){var B=[],A;
for(var y=0,w=z.length;
y<w;
y++){B.push(q.escapeRegExChars(z[y]))
}A=C?"\\b("+B.join("|")+")\\b":"("+B.join("|")+")";
return x?new RegExp(A):new RegExp(A,"i")
}}(window.document);
var k=function(){var O;
O={9:"tab",27:"esc",37:"left",39:"right",13:"enter",38:"up",40:"down"};
function L(X){var V=this,W,S,U,T;
X=X||{};
if(!X.input){b.error("input is missing")
}W=q.bind(this._onBlur,this);
S=q.bind(this._onFocus,this);
U=q.bind(this._onKeydown,this);
T=q.bind(this._onInput,this);
this.$hint=b(X.hint);
this.$input=b(X.input).on("blur.tt",W).on("focus.tt",S).on("keydown.tt",U);
if(this.$hint.length===0){this.setHint=this.getHint=this.clearHint=this.clearHintIfInvalid=q.noop
}if(!q.isMsie()){this.$input.on("input.tt",T)
}else{this.$input.on("keydown.tt keypress.tt cut.tt paste.tt",function(Y){if(O[Y.which||Y.keyCode]){return
}q.defer(q.bind(V._onInput,V,Y))
})
}this.query=this.$input.val();
this.$overflowHelper=E(this.$input)
}L.normalizeQuery=function(S){return(S||"").replace(/^\s*/g,"").replace(/\s{2,}/g," ")
};
q.mixin(L.prototype,e,{_onBlur:function C(){this.resetInputValue();
this.trigger("blurred")
},_onFocus:function w(){this.trigger("focused")
},_onKeydown:function Q(S){var T=O[S.which||S.keyCode];
this._managePreventDefault(T,S);
if(T&&this._shouldTrigger(T,S)){this.trigger(T+"Keyed",S)
}},_onInput:function t(){this._checkInputValue()
},_managePreventDefault:function y(W,V){var U,T,S;
switch(W){case"tab":T=this.getHint();
S=this.getInputValue();
U=T&&T!==S&&!F(V);
break;
case"up":case"down":U=!F(V);
break;
default:U=false
}U&&V.preventDefault()
},_shouldTrigger:function R(U,T){var S;
switch(U){case"tab":S=!F(T);
break;
default:S=true
}return S
},_checkInputValue:function z(){var S,T,U;
S=this.getInputValue();
T=B(S,this.query);
U=T?this.query.length!==S.length:false;
this.query=S;
if(!T){this.trigger("queryChanged",this.query)
}else{if(U){this.trigger("whitespaceChanged",this.query)
}}},focus:function H(){this.$input.focus()
},blur:function M(){this.$input.blur()
},getQuery:function J(){return this.query
},setQuery:function I(S){this.query=S
},getInputValue:function x(){return this.$input.val()
},setInputValue:function A(T,S){this.$input.val(T);
S?this.clearHint():this._checkInputValue()
},resetInputValue:function v(){this.setInputValue(this.query,true)
},getHint:function u(){return this.$hint.val()
},setHint:function N(S){this.$hint.val(S)
},clearHint:function D(){this.setHint("")
},clearHintIfInvalid:function s(){var V,U,S,T;
V=this.getInputValue();
U=this.getHint();
S=V!==U&&U.indexOf(V)===0;
T=V!==""&&S&&!this.hasOverflow();
!T&&this.clearHint()
},getLanguageDirection:function G(){return(this.$input.css("direction")||"ltr").toLowerCase()
},hasOverflow:function K(){var S=this.$input.width()-2;
this.$overflowHelper.text(this.getInputValue());
return this.$overflowHelper.width()>=S
},isCursorAtEnd:function(){var T,U,S;
T=this.$input.val().length;
U=this.$input[0].selectionStart;
if(q.isNumber(U)){return U===T
}else{if(document.selection){S=document.selection.createRange();
S.moveStart("character",-T);
return T===S.text.length
}}return true
},destroy:function P(){this.$hint.off(".tt");
this.$input.off(".tt");
this.$hint=this.$input=this.$overflowHelper=null
}});
return L;
function E(S){return b('<pre aria-hidden="true"></pre>').css({position:"absolute",visibility:"hidden",whiteSpace:"pre",fontFamily:S.css("font-family"),fontSize:S.css("font-size"),fontStyle:S.css("font-style"),fontVariant:S.css("font-variant"),fontWeight:S.css("font-weight"),wordSpacing:S.css("word-spacing"),letterSpacing:S.css("letter-spacing"),textIndent:S.css("text-indent"),textRendering:S.css("text-rendering"),textTransform:S.css("text-transform")}).insertAfter(S)
}function B(T,S){return L.normalizeQuery(T)===L.normalizeQuery(S)
}function F(S){return S.altKey||S.ctrlKey||S.metaKey||S.shiftKey
}}();
var r=function(){var t="ttDataset",H="ttValue",B="ttDatum";
function s(I){I=I||{};
I.templates=I.templates||{};
if(!I.source){b.error("missing source")
}if(I.name&&!v(I.name)){b.error("invalid dataset name: "+I.name)
}this.query=null;
this.highlight=!!I.highlight;
this.name=I.name||q.getUniqueId();
this.source=I.source;
this.displayFn=w(I.display||I.displayKey);
this.templates=x(I.templates,this.displayFn);
this.$el=b(h.dataset.replace("%CLASS%",this.name))
}s.extractDatasetName=function F(I){return b(I).data(t)
};
s.extractValue=function C(I){return b(I).data(H)
};
s.extractDatum=function C(I){return b(I).data(B)
};
q.mixin(s.prototype,e,{_render:function u(O,I){if(!this.$el){return
}var N=this,K;
this.$el.empty();
K=I&&I.length;
if(!K&&this.templates.empty){this.$el.html(J()).prepend(N.templates.header?M():null).append(N.templates.footer?L():null)
}else{if(K){this.$el.html(P()).prepend(N.templates.header?M():null).append(N.templates.footer?L():null)
}}this.trigger("rendered");
function J(){return N.templates.empty({query:O,isEmpty:true})
}function P(){var R,Q;
R=b(h.suggestions).css(g.suggestions);
Q=q.map(I,S);
R.append.apply(R,Q);
N.highlight&&c({className:"tt-highlight",node:R[0],pattern:O});
return R;
function S(T){var U;
U=b(h.suggestion).append(N.templates.suggestion(T)).data(t,N.name).data(H,N.displayFn(T)).data(B,T);
U.children().each(function(){b(this).css(g.suggestionChild)
});
return U
}}function M(){return N.templates.header({query:O,isEmpty:!K})
}function L(){return N.templates.footer({query:O,isEmpty:!K})
}},getRoot:function D(){return this.$el
},update:function y(K){var J=this;
this.query=K;
this.canceled=false;
this.source(K,I);
function I(L){if(!J.canceled&&K===J.query){J._render(K,L)
}}},cancel:function G(){this.canceled=true
},clear:function A(){this.cancel();
this.$el.empty();
this.trigger("rendered")
},isEmpty:function z(){return this.$el.is(":empty")
},destroy:function E(){this.$el=null
}});
return s;
function w(I){I=I||"value";
return q.isFunction(I)?I:J;
function J(K){return K[I]
}}function x(J,K){return{empty:J.empty&&q.templatify(J.empty),header:J.header&&q.templatify(J.header),footer:J.footer&&q.templatify(J.footer),suggestion:J.suggestion||I};
function I(L){return"<p>"+K(L)+"</p>"
}}function v(I){return/^[_a-zA-Z0-9-]+$/.test(I)
}}();
var m=function(){function L(U){var S=this,Q,T,R;
U=U||{};
if(!U.menu){b.error("menu is required")
}this.isOpen=false;
this.isEmpty=true;
this.datasets=q.map(U.datasets,C);
Q=q.bind(this._onSuggestionClick,this);
T=q.bind(this._onSuggestionMouseEnter,this);
R=q.bind(this._onSuggestionMouseLeave,this);
this.$menu=b(U.menu).on("click.tt",".tt-suggestion",Q).on("mouseenter.tt",".tt-suggestion",T).on("mouseleave.tt",".tt-suggestion",R);
q.each(this.datasets,function(V){S.$menu.append(V.getRoot());
V.onSync("rendered",S._onRendered,S)
})
}q.mixin(L.prototype,e,{_onSuggestionClick:function K(Q){this.trigger("suggestionClicked",b(Q.currentTarget))
},_onSuggestionMouseEnter:function s(Q){this._removeCursor();
this._setCursor(b(Q.currentTarget),true)
},_onSuggestionMouseLeave:function F(){this._removeCursor()
},_onRendered:function y(){this.isEmpty=q.every(this.datasets,Q);
this.isEmpty?this._hide():this.isOpen&&this._show();
this.trigger("datasetRendered");
function Q(R){return R.isEmpty()
}},_hide:function(){this.$menu.hide()
},_show:function(){this.$menu.css("display","block")
},_getSuggestions:function x(){return this.$menu.find(".tt-suggestion")
},_getCursor:function v(){return this.$menu.find(".tt-cursor").first()
},_setCursor:function N(R,Q){R.first().addClass("tt-cursor");
!Q&&this.trigger("cursorMoved")
},_removeCursor:function u(){this._getCursor().removeClass("tt-cursor")
},_moveCursor:function t(Q){var S,R,U,T;
if(!this.isOpen){return
}R=this._getCursor();
S=this._getSuggestions();
this._removeCursor();
U=S.index(R)+Q;
U=(U+1)%(S.length+1)-1;
if(U===-1){this.trigger("cursorRemoved");
return
}else{if(U<-1){U=S.length-1
}}this._setCursor(T=S.eq(U));
this._ensureVisible(T)
},_ensureVisible:function J(T){var Q,S,R,U;
Q=T.position().top;
S=Q+T.outerHeight(true);
R=this.$menu.scrollTop();
U=this.$menu.height()+parseInt(this.$menu.css("paddingTop"),10)+parseInt(this.$menu.css("paddingBottom"),10);
if(Q<0){this.$menu.scrollTop(R+Q)
}else{if(U<S){this.$menu.scrollTop(R+(S-U))
}}},close:function G(){if(this.isOpen){this.isOpen=false;
this._removeCursor();
this._hide();
this.trigger("closed")
}},open:function E(){if(!this.isOpen){this.isOpen=true;
!this.isEmpty&&this._show();
this.trigger("opened")
}},setLanguageDirection:function O(Q){this.$menu.css(Q==="ltr"?g.ltr:g.rtl)
},moveCursorUp:function z(){this._moveCursor(-1)
},moveCursorDown:function P(){this._moveCursor(+1)
},getDatumForSuggestion:function B(R){var Q=null;
if(R.length){Q={raw:r.extractDatum(R),value:r.extractValue(R),datasetName:r.extractDatasetName(R)}
}return Q
},getDatumForCursor:function w(){return this.getDatumForSuggestion(this._getCursor().first())
},getDatumForTopSuggestion:function I(){return this.getDatumForSuggestion(this._getSuggestions().first())
},update:function A(Q){q.each(this.datasets,R);
function R(S){S.update(Q)
}},empty:function D(){q.each(this.datasets,Q);
this.isEmpty=true;
function Q(R){R.clear()
}},isVisible:function H(){return this.isOpen&&!this.isEmpty
},destroy:function M(){this.$menu.off(".tt");
this.$menu=null;
q.each(this.datasets,Q);
function Q(R){R.destroy()
}}});
return L;
function C(Q){return new r(Q)
}}();
var j=function(){var T="ttAttrs";
function D(Z){var Y,aa,X;
Z=Z||{};
if(!Z.input){b.error("missing input")
}this.isActivated=false;
this.autoselect=!!Z.autoselect;
this.minLength=q.isNumber(Z.minLength)?Z.minLength:1;
this.$node=N(Z.input,Z.withHint);
Y=this.$node.find(".tt-dropdown-menu");
aa=this.$node.find(".tt-input");
X=this.$node.find(".tt-hint");
aa.on("blur.tt",function(ac){var ae,ad,ab;
ae=document.activeElement;
ad=Y.is(ae);
ab=Y.has(ae).length>0;
if(q.isMsie()&&(ad||ab)){ac.preventDefault();
ac.stopImmediatePropagation();
q.defer(function(){aa.focus()
})
}});
Y.on("mousedown.tt",function(ab){ab.preventDefault()
});
this.eventBus=Z.eventBus||new a({el:aa});
this.dropdown=new m({menu:Y,datasets:Z.datasets}).onSync("suggestionClicked",this._onSuggestionClicked,this).onSync("cursorMoved",this._onCursorMoved,this).onSync("cursorRemoved",this._onCursorRemoved,this).onSync("opened",this._onOpened,this).onSync("closed",this._onClosed,this).onAsync("datasetRendered",this._onDatasetRendered,this);
this.input=new k({input:aa,hint:X}).onSync("focused",this._onFocused,this).onSync("blurred",this._onBlurred,this).onSync("enterKeyed",this._onEnterKeyed,this).onSync("tabKeyed",this._onTabKeyed,this).onSync("escKeyed",this._onEscKeyed,this).onSync("upKeyed",this._onUpKeyed,this).onSync("downKeyed",this._onDownKeyed,this).onSync("leftKeyed",this._onLeftKeyed,this).onSync("rightKeyed",this._onRightKeyed,this).onSync("queryChanged",this._onQueryChanged,this).onSync("whitespaceChanged",this._onWhitespaceChanged,this);
this._setLanguageDirection()
}q.mixin(D.prototype,{_onSuggestionClicked:function u(Z,Y){var X;
if(X=this.dropdown.getDatumForSuggestion(Y)){this._select(X)
}},_onCursorMoved:function O(){var X=this.dropdown.getDatumForCursor();
this.input.setInputValue(X.value,true);
this.eventBus.trigger("cursorchanged",X.raw,X.datasetName)
},_onCursorRemoved:function x(){this.input.resetInputValue();
this._updateHint()
},_onDatasetRendered:function J(){this._updateHint()
},_onOpened:function v(){this._updateHint();
this.eventBus.trigger("opened")
},_onClosed:function A(){this.input.clearHint();
this.eventBus.trigger("closed")
},_onFocused:function L(){this.isActivated=true;
this.dropdown.open()
},_onBlurred:function M(){this.isActivated=false;
this.dropdown.empty();
this.dropdown.close()
},_onEnterKeyed:function H(Y,X){var Z,aa;
Z=this.dropdown.getDatumForCursor();
aa=this.dropdown.getDatumForTopSuggestion();
if(Z){this._select(Z);
X.preventDefault()
}else{if(this.autoselect&&aa){this._select(aa);
X.preventDefault()
}}},_onTabKeyed:function W(Z,Y){var X;
if(X=this.dropdown.getDatumForCursor()){this._select(X);
Y.preventDefault()
}else{this._autocomplete(true)
}},_onEscKeyed:function R(){this.dropdown.close();
this.input.resetInputValue()
},_onUpKeyed:function t(){var X=this.input.getQuery();
this.dropdown.isEmpty&&X.length>=this.minLength?this.dropdown.update(X):this.dropdown.moveCursorUp();
this.dropdown.open()
},_onDownKeyed:function y(){var X=this.input.getQuery();
this.dropdown.isEmpty&&X.length>=this.minLength?this.dropdown.update(X):this.dropdown.moveCursorDown();
this.dropdown.open()
},_onLeftKeyed:function V(){this.dir==="rtl"&&this._autocomplete()
},_onRightKeyed:function I(){this.dir==="ltr"&&this._autocomplete()
},_onQueryChanged:function F(Y,X){this.input.clearHintIfInvalid();
X.length>=this.minLength?this.dropdown.update(X):this.dropdown.empty();
this.dropdown.open();
this._setLanguageDirection()
},_onWhitespaceChanged:function S(){this._updateHint();
this.dropdown.open()
},_setLanguageDirection:function Q(){var X;
if(this.dir!==(X=this.input.getLanguageDirection())){this.dir=X;
this.$node.css("direction",X);
this.dropdown.setLanguageDirection(X)
}},_updateHint:function K(){var Y,ac,ab,aa,X,Z;
Y=this.dropdown.getDatumForTopSuggestion();
if(Y&&this.dropdown.isVisible()&&!this.input.hasOverflow()){ac=this.input.getInputValue();
ab=k.normalizeQuery(ac);
aa=q.escapeRegExChars(ab);
X=new RegExp("^(?:"+aa+")(.+$)","i");
Z=X.exec(Y.value);
Z?this.input.setHint(ac+Z[1]):this.input.clearHint()
}else{this.input.clearHint()
}},_autocomplete:function U(Z){var ab,aa,X,Y;
ab=this.input.getHint();
aa=this.input.getQuery();
X=Z||this.input.isCursorAtEnd();
if(ab&&aa!==ab&&X){Y=this.dropdown.getDatumForTopSuggestion();
Y&&this.input.setInputValue(Y.value);
this.eventBus.trigger("autocompleted",Y.raw,Y.datasetName)
}},_select:function G(X){this.input.setQuery(X.value);
this.input.setInputValue(X.value,true);
this._setLanguageDirection();
this.eventBus.trigger("selected",X.raw,X.datasetName);
this.dropdown.close();
q.defer(q.bind(this.dropdown.empty,this.dropdown))
},open:function C(){this.dropdown.open()
},close:function E(){this.dropdown.close()
},setVal:function s(X){X=q.toStr(X);
if(this.isActivated){this.input.setInputValue(X)
}else{this.input.setQuery(X);
this.input.setInputValue(X,true)
}this._setLanguageDirection()
},getVal:function B(){return this.input.getQuery()
},destroy:function P(){this.input.destroy();
this.dropdown.destroy();
w(this.$node);
this.$node=null
}});
return D;
function N(X,ac){var ad,Z,ab,Y;
ad=b(X);
Z=b(h.wrapper).css(g.wrapper);
ab=b(h.dropdown).css(g.dropdown);
Y=ad.clone().css(g.hint).css(z(ad));
Y.val("").removeData().addClass("tt-hint").removeAttr("id name placeholder required").prop("readonly",true).attr({autocomplete:"off",spellcheck:"false",tabindex:-1});
ad.data(T,{dir:ad.attr("dir"),autocomplete:ad.attr("autocomplete"),spellcheck:ad.attr("spellcheck"),style:ad.attr("style")});
ad.addClass("tt-input").attr({autocomplete:"off",spellcheck:false}).css(ac?g.input:g.inputWithNoHint);
try{!ad.attr("dir")&&ad.attr("dir","auto")
}catch(aa){}return ad.wrap(Z).parent().prepend(ac?Y:null).append(ab)
}function z(X){return{backgroundAttachment:X.css("background-attachment"),backgroundClip:X.css("background-clip"),backgroundColor:X.css("background-color"),backgroundImage:X.css("background-image"),backgroundOrigin:X.css("background-origin"),backgroundPosition:X.css("background-position"),backgroundRepeat:X.css("background-repeat"),backgroundSize:X.css("background-size")}
}function w(X){var Y=X.find(".tt-input");
q.each(Y.data(T),function(aa,Z){q.isUndefined(aa)?Y.removeAttr(Z):Y.attr(Z,aa)
});
Y.detach().removeData(T).removeClass("tt-input").insertAfter(X);
X.remove()
}}();
(function(){var u,s,v;
u=b.fn.typeahead;
s="ttTypeahead";
v={initialize:function w(D,C){C=q.isArray(C)?C:[].slice.call(arguments,1);
D=D||{};
return this.each(B);
function B(){var G=b(this),E,F;
q.each(C,function(H){H.highlight=!!D.highlight
});
F=new j({input:G,eventBus:E=new a({el:G}),withHint:q.isUndefined(D.hint)?true:!!D.hint,minLength:D.minLength,autoselect:D.autoselect,datasets:C});
G.data(s,F)
}},open:function x(){return this.each(B);
function B(){var D=b(this),C;
if(C=D.data(s)){C.open()
}}},close:function A(){return this.each(B);
function B(){var D=b(this),C;
if(C=D.data(s)){C.close()
}}},val:function t(B){return !arguments.length?D(this.first()):this.each(C);
function C(){var F=b(this),E;
if(E=F.data(s)){E.setVal(B)
}}function D(G){var F,E;
if(F=G.data(s)){E=F.getVal()
}return E
}},destroy:function z(){return this.each(B);
function B(){var D=b(this),C;
if(C=D.data(s)){C.destroy();
D.removeData(s)
}}}};
b.fn.typeahead=function(C){var B;
if(v[C]&&C!=="initialize"){B=this.filter(function(){return !!b(this).data(s)
});
return v[C].apply(B,[].slice.call(arguments,1))
}else{return v.initialize.apply(this,arguments)
}};
b.fn.typeahead.noConflict=function y(){b.fn.typeahead=u;
return this
}
})()
})(window.jQuery);