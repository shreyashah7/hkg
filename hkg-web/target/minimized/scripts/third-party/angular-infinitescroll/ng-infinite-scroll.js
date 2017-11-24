var mod;
mod=angular.module("infinite-scroll",[]);
mod.value("THROTTLE_MILLISECONDS",null);
mod.directive("infiniteScroll",["$rootScope","$window","$interval","THROTTLE_MILLISECONDS",function(a,d,c,b){return{scope:{infiniteScroll:"&",infiniteScrollContainer:"=",infiniteScrollDistance:"=",infiniteScrollDisabled:"=",infiniteScrollUseDocumentBottom:"=",infiniteScrollListenForEvent:"@"},link:function(f,w,t){var v,x,o,m,j,i,y,g,r,q,u,h,n,p,l,s,e,k;
k=angular.element(d);
n=null;
p=null;
x=null;
o=null;
q=true;
e=false;
s=null;
r=function(z){z=z[0]||z;
if(isNaN(z.offsetHeight)){return z.document.documentElement.clientHeight
}else{return z.offsetHeight
}};
u=function(z){if(!z[0].getBoundingClientRect||z.css("none")){return
}return z[0].getBoundingClientRect().top+h(z)
};
h=function(z){z=z[0]||z;
if(isNaN(window.pageYOffset)){return z.document.documentElement.scrollTop
}else{return z.ownerDocument.defaultView.pageYOffset
}};
g=function(){var D,z,C,B,A;
if(o===k){D=r(o)+h(o[0].document.documentElement);
C=u(w)+r(w)
}else{D=r(o);
z=0;
if(u(o)!==void 0){z=u(o)
}C=u(w)-z+r(w)
}if(e){C=r((w[0].ownerDocument||w[0].document).documentElement)
}B=C-D;
A=B<=r(o)*n+1;
if(A){x=true;
if(p){if(f.$$phase||a.$$phase){return f.infiniteScroll()
}else{return f.$apply(f.infiniteScroll)
}}}else{return x=false
}};
l=function(B,D){var z,A,C;
C=null;
A=0;
z=function(){var E;
A=new Date().getTime();
c.cancel(C);
C=null;
B.call();
return E=null
};
return function(){var E,F;
E=new Date().getTime();
F=D-(E-A);
if(F<=0){clearTimeout(C);
c.cancel(C);
C=null;
A=E;
return B.call()
}else{if(!C){return C=c(z,F,1)
}}}
};
if(b!=null){g=l(g,b)
}f.$on("$destroy",function(){o.unbind("scroll",g);
if(s!=null){s();
return s=null
}});
i=function(z){return n=parseFloat(z)||0
};
f.$watch("infiniteScrollDistance",i);
i(f.infiniteScrollDistance);
j=function(z){p=!z;
if(p&&x){x=false;
return g()
}};
f.$watch("infiniteScrollDisabled",j);
j(f.infiniteScrollDisabled);
y=function(z){return e=z
};
f.$watch("infiniteScrollUseDocumentBottom",y);
y(f.infiniteScrollUseDocumentBottom);
v=function(z){if(o!=null){o.unbind("scroll",g)
}o=z;
if(z!=null){return o.bind("scroll",g)
}};
v(k);
if(f.infiniteScrollListenForEvent){s=a.$on(f.infiniteScrollListenForEvent,g)
}m=function(z){if((z==null)||z.length===0){return
}if(z instanceof HTMLElement){z=angular.element(z)
}else{if(typeof z.append==="function"){z=angular.element(z[z.length-1])
}else{if(typeof z==="string"){z=angular.element(document.querySelector(z))
}}}if(z!=null){return v(z)
}else{throw new Exception("invalid infinite-scroll-container attribute.")
}};
f.$watch("infiniteScrollContainer",m);
m(f.infiniteScrollContainer||[]);
if(t.infiniteScrollParent!=null){v(angular.element(w.parent()))
}if(t.infiniteScrollImmediateCheck!=null){q=f.$eval(t.infiniteScrollImmediateCheck)
}return c((function(){if(q){return g()
}}),0,1)
}}
}]);