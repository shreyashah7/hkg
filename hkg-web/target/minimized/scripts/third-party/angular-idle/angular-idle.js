(function(d,c,e){function b(){var f={http:null,interval:10*60};
this.http=function(g){if(!g){throw new Error("Argument must be a string containing a URL, or an object containing the HTTP request configuration.")
}if(c.isString(g)){g={url:g,method:"GET"}
}g.cache=false;
f.http=g
};
this.interval=function(g){g=parseInt(g);
if(isNaN(g)||g<=0){throw new Error("Interval must be expressed in seconds and be greater than 0.")
}f.interval=g
};
this.$get=function(g,j,l,m){var i={ping:null};
function h(o,n){g.$broadcast("$keepaliveResponse",o,n)
}function k(){g.$broadcast("$keepalive");
if(c.isObject(f.http)){m(f.http).success(h).error(h)
}}return{_options:function(){return f
},start:function(){l.cancel(i.ping);
i.ping=l(k,f.interval*1000)
},stop:function(){l.cancel(i.ping)
},ping:function(){k()
}}
};
this.$get.$inject=["$rootScope","$log","$interval","$http"]
}c.module("ngIdle.keepalive",[]).provider("$keepalive",b);
function a(){var f={idleDuration:20*60,warningDuration:30,autoResume:true,events:"mousemove keydown DOMMouseScroll mousewheel mousedown touchstart",keepalive:true};
this.activeOn=function(g){f.events=g
};
this.idleDuration=function(g){if(g<=0){throw new Error("idleDuration must be a value in seconds, greater than 0.")
}f.idleDuration=g
};
this.warningDuration=function(g){if(g<0){throw new Error("warning must be a value in seconds, greater than 0.")
}f.warningDuration=g
};
this.autoResume=function(g){f.autoResume=g===true
};
this.keepalive=function(g){f.keepalive=g===true
};
this.$get=function(k,r,o,h,n){var g={idle:null,warning:null,idling:false,running:false,countdown:null};
function i(){if(!f.keepalive){return
}if(g.running){n.ping()
}n.start()
}function q(){if(!f.keepalive){return
}n.stop()
}function m(){g.idling=!g.idling;
var s=g.idling?"Start":"End";
o.$broadcast("$idle"+s);
if(g.idling){q();
g.countdown=f.warningDuration;
p();
g.warning=k(p,1000,f.warningDuration)
}else{i()
}k.cancel(g.idle)
}function p(){if(g.countdown<=0){o.$broadcast("$idleTimeout")
}else{o.$broadcast("$idleWarn",g.countdown)
}g.countdown--
}var l={_options:function(){return f
},running:function(){return g.running
},idling:function(){return g.idling
},watch:function(){k.cancel(g.idle);
k.cancel(g.warning);
if(g.idling){m()
}else{if(!g.running){i()
}}g.running=true;
g.idle=k(m,f.idleDuration*1000)
},unwatch:function(){k.cancel(g.idle);
k.cancel(g.warning);
g.idling=false;
g.running=false
}};
var j=function(){if(g.running&&f.autoResume){l.watch()
}};
h.find("body").on(f.events,j);
return l
};
this.$get.$inject=["$interval","$log","$rootScope","$document","$keepalive"]
}c.module("ngIdle.idle",[]).provider("$idle",a);
c.module("ngIdle.ngIdleCountdown",[]).directive("ngIdleCountdown",function(){return{restrict:"A",scope:{value:"=ngIdleCountdown"},link:function(f){f.$on("$idleWarn",function(h,g){f.value=g
});
f.$on("$idleTimeout",function(){f.value=0
})
}}
});
c.module("ngIdle",["ngIdle.keepalive","ngIdle.idle","ngIdle.ngIdleCountdown"])
})(window,window.angular);