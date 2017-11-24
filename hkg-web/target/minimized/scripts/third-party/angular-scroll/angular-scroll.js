var duScrollDefaultEasing=function(a){if(a<0.5){return Math.pow(a*2,2)/2
}return 1-Math.pow((1-a)*2,2)/2
};
angular.module("duScroll",["duScroll.smoothScroll","duScroll.scrollHelpers"]).value("duScrollDuration",350).value("duScrollSpyWait",100).value("duScrollGreedy",false).value("duScrollOffset",0).value("duScrollEasing",duScrollDefaultEasing);
angular.module("duScroll.scrollHelpers",["duScroll.requestAnimation"]).run(["$window","$q","cancelAnimation","requestAnimation","duScrollEasing","duScrollDuration","duScrollOffset",function(b,j,a,i,g,d,c){var h={};
var f=function(n){return(typeof HTMLDocument!=="undefined"&&n instanceof HTMLDocument)||(n.nodeType&&n.nodeType===n.DOCUMENT_NODE)
};
var k=function(n){return(typeof HTMLElement!=="undefined"&&n instanceof HTMLElement)||(n.nodeType&&n.nodeType===n.ELEMENT_NODE)
};
var e=function(n){return k(n)||f(n)?n:n[0]
};
h.duScrollTo=function(q,p,o,s){var r;
if(angular.isElement(q)){r=this.duScrollToElement
}else{if(angular.isDefined(o)){r=this.duScrollToAnimated
}}if(r){return r.apply(this,arguments)
}var n=e(this);
if(f(n)){return b.scrollTo(q,p)
}n.scrollLeft=q;
n.scrollTop=p
};
var l,m;
h.duScrollToAnimated=function(r,A,s,z){if(s&&!z){z=g
}var q=this.duScrollLeft(),w=this.duScrollTop(),v=Math.round(r-q),t=Math.round(A-w);
var p=null,n=0;
var o=this;
var u="scroll mousedown mousewheel touchmove keydown";
var y=function(B){if(!B||(n&&B.which>0)){o.unbind(u,y);
a(l);
m.reject();
l=null
}};
if(l){y()
}m=j.defer();
if(s===0||(!v&&!t)){if(s===0){o.duScrollTo(r,A)
}m.resolve();
return m.promise
}var x=function(C){if(p===null){p=C
}n=C-p;
var B=(n>=s?1:z(n/s));
o.scrollTo(q+Math.ceil(v*B),w+Math.ceil(t*B));
if(B<1){l=i(x)
}else{o.unbind(u,y);
l=null;
m.resolve()
}};
o.duScrollTo(q,w);
o.bind(u,y);
l=i(x);
return m.promise
};
h.duScrollToElement=function(q,r,p,s){var n=e(this);
if(!angular.isNumber(r)||isNaN(r)){r=c
}var o=this.duScrollTop()+e(q).getBoundingClientRect().top-r;
if(k(n)){o-=n.getBoundingClientRect().top
}return this.duScrollTo(0,o,p,s)
};
h.duScrollLeft=function(o,p,q){if(angular.isNumber(o)){return this.duScrollTo(o,this.duScrollTop(),p,q)
}var n=e(this);
if(f(n)){return b.scrollX||document.documentElement.scrollLeft||document.body.scrollLeft
}return n.scrollLeft
};
h.duScrollTop=function(o,p,q){if(angular.isNumber(o)){return this.duScrollTo(this.duScrollLeft(),o,p,q)
}var n=e(this);
if(f(n)){return b.scrollY||document.documentElement.scrollTop||document.body.scrollTop
}return n.scrollTop
};
h.duScrollToElementAnimated=function(o,p,n,q){return this.duScrollToElement(o,p,n||d,q)
};
h.duScrollTopAnimated=function(o,n,p){return this.duScrollTop(o,n||d,p)
};
h.duScrollLeftAnimated=function(o,n,p){return this.duScrollLeft(o,n||d,p)
};
angular.forEach(h,function(o,n){angular.element.prototype[n]=o;
var p=n.replace(/^duScroll/,"scroll");
if(angular.isUndefined(angular.element.prototype[p])){angular.element.prototype[p]=o
}})
}]);
angular.module("duScroll.polyfill",[]).factory("polyfill",["$window",function(a){var b=["webkit","moz","o","ms"];
return function(g,f){if(a[g]){return a[g]
}var e=g.substr(0,1).toUpperCase()+g.substr(1);
for(var d,c=0;
c<b.length;
c++){d=b[c]+e;
if(a[d]){return a[d]
}}return f
}
}]);
angular.module("duScroll.requestAnimation",["duScroll.polyfill"]).factory("requestAnimation",["polyfill","$timeout",function(c,a){var b=0;
var d=function(i,f){var e=new Date().getTime();
var g=Math.max(0,16-(e-b));
var h=a(function(){i(e+g)
},g);
b=e+g;
return h
};
return c("requestAnimationFrame",d)
}]).factory("cancelAnimation",["polyfill","$timeout",function(b,a){var c=function(d){a.cancel(d)
};
return b("cancelAnimationFrame",c)
}]);
angular.module("duScroll.scrollContainerAPI",[]).factory("scrollContainerAPI",["$document",function(e){var c={};
var b=function(h,g){var i=h.$id;
c[i]=g;
return i
};
var a=function(g){if(c[g.$id]){return g.$id
}if(g.$parent){return a(g.$parent)
}return
};
var d=function(g){var h=a(g);
return h?c[h]:e
};
var f=function(g){var h=a(g);
if(h){delete c[h]
}};
return{getContainerId:a,getContainer:d,setContainer:b,removeContainer:f}
}]);
angular.module("duScroll.smoothScroll",["duScroll.scrollHelpers","duScroll.scrollContainerAPI"]).directive("duSmoothScroll",["$window","duScrollDuration","duScrollOffset","scrollContainerAPI",function(d,c,b,a){return{link:function(f,e,g){e.hide();
angular.element(d).bind("scroll",function(){if($(this).scrollTop()>100){e.fadeIn()
}else{e.fadeOut()
}});
e.on("click",function(k){if(!g.href||g.href.indexOf("#")===-1){return
}var j=document.getElementById(g.href.replace(/.*(?=#[^\s]+$)/,"").substring(1));
if(!j||!j.getBoundingClientRect){return
}if(k.stopPropagation){k.stopPropagation()
}if(k.preventDefault){k.preventDefault()
}var l=g.offset?parseInt(g.offset,10):b;
var i=g.duration?parseInt(g.duration,10):c;
var h=a.getContainer(f);
h.duScrollToElement(angular.element(j),isNaN(l)?0:l,isNaN(i)?0:i)
})
}}
}]);