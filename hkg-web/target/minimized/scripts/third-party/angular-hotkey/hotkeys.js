/* 
 * angular-hotkeys v1.5.0
 * https://chieffancypants.github.io/angular-hotkeys
 * Copyright (c) 2015 Wes Cruver
 * License: MIT
 */
(function(){angular.module("cfp.hotkeys",[]).provider("hotkeys",["$injector",function(a){this.includeCheatSheet=true;
this.useNgRoute=a.has("ngViewDirective");
this.templateTitle="Keyboard Shortcuts:";
this.templateHeader=null;
this.templateFooter=null;
this.template='<div class="cfp-hotkeys-container fade" ng-class="{in: helpVisible}" style="display: none;"><div class="cfp-hotkeys"><h4 class="cfp-hotkeys-title" ng-if="!header">{{ title }}</h4><div ng-bind-html="header" ng-if="header"></div><table><tbody><tr ng-repeat="hotkey in hotkeys | filter:{ description: \'!$$undefined$$\' }"><td class="cfp-hotkeys-keys"><span ng-repeat="key in hotkey.format() track by $index" class="cfp-hotkeys-key">{{ key }}</span></td><td class="cfp-hotkeys-text">{{ hotkey.description }}</td></tr></tbody></table><div ng-bind-html="footer" ng-if="footer"></div><div class="cfp-hotkeys-close" ng-click="toggleCheatSheet()">×</div></div></div>';
this.cheatSheetHotkey="?";
this.cheatSheetDescription="Show / hide this help menu";
this.$get=["$rootElement","$rootScope","$compile","$window","$document",function(h,i,u,l,v){Mousetrap.prototype.stopCallback=function(x,w){if((" "+w.className+" ").indexOf(" mousetrap ")>-1){return false
}return(w.contentEditable&&w.contentEditable=="true")
};
function q(y){var x={command:"⌘",shift:"⇧",left:"←",right:"→",up:"↑",down:"↓","return":"↩",backspace:"⌫"};
y=y.split("+");
for(var w=0;
w<y.length;
w++){if(y[w]==="mod"){if(l.navigator&&l.navigator.platform.indexOf("Mac")>=0){y[w]="command"
}else{y[w]="ctrl"
}}y[w]=x[y[w]]||y[w]
}return y.join(" + ")
}function j(z,x,B,y,A,w){this.combo=z instanceof Array?z:[z];
this.description=x;
this.callback=B;
this.action=y;
this.allowIn=A;
this.persistent=w;
this._formated=null
}j.prototype.format=function(){if(this._formated===null){var x=this.combo[0];
var y=x.split(/[\s]/);
for(var w=0;
w<y.length;
w++){y[w]=q(y[w])
}this._formated=y
}return this._formated
};
var d=i.$new();
d.hotkeys=[];
d.helpVisible=false;
d.title=this.templateTitle;
d.header=this.templateHeader;
d.footer=this.templateFooter;
d.toggleCheatSheet=t;
var g=[];
if(this.useNgRoute){i.$on("$routeChangeSuccess",function(x,w){s();
if(w&&w.hotkeys){angular.forEach(w.hotkeys,function(y){var z=y[2];
if(typeof(z)==="string"||z instanceof String){y[2]=[z,w]
}y[5]=false;
r.apply(this,y)
})
}})
}if(this.includeCheatSheet){var o=v[0];
var f=h[0];
var c=angular.element(this.template);
r(this.cheatSheetHotkey,this.cheatSheetDescription,t);
if(f===o||f===o.documentElement){f=o.body
}angular.element(f).append(u(c)(d))
}function s(){var x=d.hotkeys.length;
while(x--){var w=d.hotkeys[x];
if(w&&!w.persistent){m(w)
}}}var k=false;
function t(){d.helpVisible=!d.helpVisible;
if(d.helpVisible){k=b("esc");
m("esc");
r("esc",k.description,t,null,["INPUT","SELECT","TEXTAREA"])
}else{m("esc");
if(k!==false){r(k)
}}}function r(y,F,G,A,w,z){var H;
var C=["INPUT","SELECT","TEXTAREA"];
var x=Object.prototype.toString.call(y);
if(x==="[object Object]"){F=y.description;
G=y.callback;
A=y.action;
z=y.persistent;
w=y.allowIn;
y=y.combo
}if(F instanceof Function){A=G;
G=F;
F="$$undefined$$"
}else{if(angular.isUndefined(F)){F="$$undefined$$"
}}if(z===undefined){z=true
}if(typeof G==="function"){H=G;
if(!(w instanceof Array)){w=[]
}var D;
for(var B=0;
B<w.length;
B++){w[B]=w[B].toUpperCase();
D=C.indexOf(w[B]);
if(D!==-1){C.splice(D,1)
}}G=function(J){var L=true;
var K=J.target||J.srcElement;
var M=K.nodeName.toUpperCase();
if((" "+K.className+" ").indexOf(" mousetrap ")>-1){L=true
}else{for(var I=0;
I<C.length;
I++){if(C[I]===M){L=false;
break
}}}if(L){e(H.apply(this,arguments))
}}
}if(typeof(A)==="string"){Mousetrap.bind(y,e(G),A)
}else{Mousetrap.bind(y,e(G))
}var E=new j(y,F,G,A,w,z);
d.hotkeys.push(E);
return E
}function m(w){var A=(w instanceof j)?w.combo:w;
Mousetrap.unbind(A);
if(angular.isArray(A)){var y=true;
var z=A.length;
while(z--){y=m(A[z])&&y
}return y
}else{var x=d.hotkeys.indexOf(b(A));
if(x>-1){if(d.hotkeys[x].combo.length>1){d.hotkeys[x].combo.splice(d.hotkeys[x].combo.indexOf(A),1)
}else{d.hotkeys.splice(x,1)
}return true
}}return false
}function b(y){if(!y){return d.hotkeys
}var w;
for(var x=0;
x<d.hotkeys.length;
x++){w=d.hotkeys[x];
if(w.combo.indexOf(y)>-1){return w
}}return false
}function p(w){if(!(w.$id in g)){g[w.$id]=[];
w.$on("$destroy",function(){var x=g[w.$id].length;
while(x--){m(g[w.$id].pop())
}})
}return{add:function(y){var x;
if(arguments.length>1){x=r.apply(this,arguments)
}else{x=r(y)
}g[w.$id].push(x);
return this
}}
}function e(w){return function(z,A){if(w instanceof Array){var x=w[0];
var y=w[1];
w=function(B){y.scope.$eval(x)
}
}i.$apply(function(){w(z,b(A))
})
}
}var n={add:r,del:m,get:b,bindTo:p,template:this.template,toggleCheatSheet:t,includeCheatSheet:this.includeCheatSheet,cheatSheetHotkey:this.cheatSheetHotkey,cheatSheetDescription:this.cheatSheetDescription,useNgRoute:this.useNgRoute,purgeHotkeys:s,templateTitle:this.templateTitle};
return n
}]
}]).directive("hotkey",["hotkeys",function(a){return{restrict:"A",link:function(e,d,b){var c,f;
angular.forEach(e.$eval(b.hotkey),function(h,g){f=typeof b.hotkeyAllowIn==="string"?b.hotkeyAllowIn.split(/[\s,]+/):[];
c=g;
a.add({combo:g,description:b.hotkeyDescription,callback:h,action:b.hotkeyAction,allowIn:f})
});
d.bind("$destroy",function(){a.del(c)
})
}}
}]).run(["hotkeys",function(a){}])
})();
(function(j,m,e){var b={8:"backspace",9:"tab",13:"enter",16:"shift",17:"ctrl",18:"alt",20:"capslock",27:"esc",32:"space",33:"pageup",34:"pagedown",35:"end",36:"home",37:"left",38:"up",39:"right",40:"down",45:"ins",46:"del",91:"meta",93:"meta",224:"meta"};
var h={106:"*",107:"+",109:"-",110:".",111:"/",186:";",187:"=",188:",",189:"-",190:".",191:"/",192:"`",219:"[",220:"\\",221:"]",222:"'"};
var v={"~":"`","!":"1","@":"2","#":"3","$":"4","%":"5","^":"6","&":"7","*":"8","(":"9",")":"0",_:"-","+":"=",":":";",'"':"'","<":",",">":".","?":"/","|":"\\"};
var p={option:"alt",command:"meta","return":"enter",escape:"esc",plus:"+",mod:/Mac|iPod|iPhone|iPad/.test(navigator.platform)?"meta":"ctrl"};
var w;
for(var r=1;
r<20;
++r){b[111+r]="f"+r
}for(r=0;
r<=9;
++r){b[r+96]=r
}function s(i,x,y){if(i.addEventListener){i.addEventListener(x,y,false);
return
}i.attachEvent("on"+x,y)
}function u(x){if(x.type=="keypress"){var i=String.fromCharCode(x.which);
if(!x.shiftKey){i=i.toLowerCase()
}return i
}if(b[x.which]){return b[x.which]
}if(h[x.which]){return h[x.which]
}return String.fromCharCode(x.which).toLowerCase()
}function k(x,i){return x.sort().join(",")===i.sort().join(",")
}function f(x){var i=[];
if(x.shiftKey){i.push("shift")
}if(x.altKey){i.push("alt")
}if(x.ctrlKey){i.push("ctrl")
}if(x.metaKey){i.push("meta")
}return i
}function l(i){if(i.preventDefault){i.preventDefault();
return
}i.returnValue=false
}function t(i){if(i.stopPropagation){i.stopPropagation();
return
}i.cancelBubble=true
}function o(i){return i=="shift"||i=="ctrl"||i=="alt"||i=="meta"
}function d(){if(!w){w={};
for(var i in b){if(i>95&&i<112){continue
}if(b.hasOwnProperty(i)){w[b[i]]=i
}}}return w
}function n(x,i,y){if(!y){y=d()[x]?"keydown":"keypress"
}if(y=="keypress"&&i.length){y="keydown"
}return y
}function c(i){if(i==="+"){return["+"]
}i=i.replace(/\+{2}/g,"+plus");
return i.split("+")
}function q(A,C){var B;
var z;
var y;
var x=[];
B=c(A);
for(y=0;
y<B.length;
++y){z=B[y];
if(p[z]){z=p[z]
}if(C&&C!="keypress"&&v[z]){z=v[z];
x.push("shift")
}if(o(z)){x.push(z)
}}C=n(z,x,C);
return{key:z,modifiers:x,action:C}
}function a(x,i){if(x===m){return false
}if(x===i){return true
}return a(x.parentNode,i)
}function g(G){var J=this;
G=G||m;
if(!(J instanceof g)){return new g(G)
}J.target=G;
J._callbacks={};
J._directMap={};
var B={};
var A;
var x=false;
var z=false;
var E=false;
function H(K){K=K||{};
var M=false,L;
for(L in B){if(K[L]){M=true;
continue
}B[L]=0
}if(!M){E=false
}}function D(P,T,Q,V,R,K){var N;
var U;
var O=[];
var L=Q.type;
if(!J._callbacks[P]){return[]
}if(L=="keyup"&&o(P)){T=[P]
}for(N=0;
N<J._callbacks[P].length;
++N){U=J._callbacks[P][N];
if(!V&&U.seq&&B[U.seq]!=U.level){continue
}if(L!=U.action){continue
}if((L=="keypress"&&!Q.metaKey&&!Q.ctrlKey)||k(T,U.modifiers)){var S=!V&&U.combo==R;
var M=V&&U.seq==V&&U.level==K;
if(S||M){J._callbacks[P].splice(N,1)
}O.push(U)
}}return O
}function i(N,L,K,M){if(J.stopCallback(L,L.target||L.srcElement,K,M)){return
}if(N(L,K)===false){l(L);
t(L)
}}J._handleKey=function(O,R,P){var Q=D(O,R,P);
var M;
var L={};
var S=0;
var N=false;
for(M=0;
M<Q.length;
++M){if(Q[M].seq){S=Math.max(S,Q[M].level)
}}for(M=0;
M<Q.length;
++M){if(Q[M].seq){if(Q[M].level!=S){continue
}N=true;
L[Q[M].seq]=1;
i(Q[M].callback,P,Q[M].combo,Q[M].seq);
continue
}if(!N){i(Q[M].callback,P,Q[M].combo)
}}var K=P.type=="keypress"&&z;
if(P.type==E&&!o(O)&&!K){H(L)
}z=N&&P.type=="keydown"
};
function F(L){if(typeof L.which!=="number"){L.which=L.keyCode
}var K=u(L);
if(!K){return
}if(L.type=="keyup"&&x===K){x=false;
return
}J.handleKey(K,f(L),L)
}function C(){clearTimeout(A);
A=setTimeout(H,1000)
}function I(M,R,P,N){B[M]=0;
function Q(T){return function(){E=T;
++B[M];
C()
}
}function S(T){i(P,T,M);
if(N!=="keyup"){x=u(T)
}setTimeout(H,10)
}for(var O=0;
O<R.length;
++O){var L=O+1===R.length;
var K=L?S:Q(N||q(R[O+1]).action);
y(R[O],K,N,M,O)
}}function y(K,Q,L,N,P){J._directMap[K+":"+L]=Q;
K=K.replace(/\s+/g," ");
var O=K.split(" ");
var M;
if(O.length>1){I(K,O,Q,L);
return
}M=q(K,L);
J._callbacks[M.key]=J._callbacks[M.key]||[];
D(M.key,M.modifiers,{type:M.action},N,K,P);
J._callbacks[M.key][N?"unshift":"push"]({callback:Q,modifiers:M.modifiers,action:M.action,seq:N,level:P,combo:K})
}J._bindMultiple=function(L,N,M){for(var K=0;
K<L.length;
++K){y(L[K],N,M)
}};
s(G,"keypress",F);
s(G,"keydown",F);
s(G,"keyup",F)
}g.prototype.bind=function(x,z,y){var i=this;
x=x instanceof Array?x:[x];
i._bindMultiple.call(i,x,z,y);
return i
};
g.prototype.unbind=function(x,y){var i=this;
return i.bind.call(i,x,function(){},y)
};
g.prototype.trigger=function(x,y){var i=this;
if(i._directMap[x+":"+y]){i._directMap[x+":"+y]({},x)
}return i
};
g.prototype.reset=function(){var i=this;
i._callbacks={};
i._directMap={};
return i
};
g.prototype.stopCallback=function(y,x){var i=this;
if((" "+x.className+" ").indexOf(" mousetrap ")>-1){return false
}if(a(x,i.target)){return false
}return x.tagName=="INPUT"||x.tagName=="SELECT"||x.tagName=="TEXTAREA"||x.isContentEditable
};
g.prototype.handleKey=function(){var i=this;
return i._handleKey.apply(i,arguments)
};
g.init=function(){var x=g(m);
for(var i in x){if(i.charAt(0)!=="_"){g[i]=(function(y){return function(){return x[y].apply(x,arguments)
}
}(i))
}}};
g.init();
j.Mousetrap=g;
if(typeof module!=="undefined"&&module.exports){module.exports=g
}if(typeof define==="function"&&define.amd){define(function(){return g
})
}})(window,document);