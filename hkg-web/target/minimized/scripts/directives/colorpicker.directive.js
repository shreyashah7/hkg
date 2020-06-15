"use strict";
globalProvider.provide.factory("Helper",function(){return{closestSlider:function(b){var a=b.matches||b.webkitMatchesSelector||b.mozMatchesSelector||b.msMatchesSelector;
if(a.bind(b)("I")){return b.parentNode
}return b
},getOffset:function(b){var a=0,e=0,d=0,c=0;
while(b&&!isNaN(b.offsetLeft)&&!isNaN(b.offsetTop)){a+=b.offsetLeft;
e+=b.offsetTop;
d+=b.scrollLeft;
c+=b.scrollTop;
b=b.offsetParent
}return{top:e,left:a,scrollX:d,scrollY:c}
},stringParsers:[{re:/rgba?\(\s*(\d{1,3})\s*,\s*(\d{1,3})\s*,\s*(\d{1,3})\s*(?:,\s*(\d+(?:\.\d+)?)\s*)?\)/,parse:function(a){return[a[1],a[2],a[3],a[4]]
}},{re:/rgba?\(\s*(\d+(?:\.\d+)?)\%\s*,\s*(\d+(?:\.\d+)?)\%\s*,\s*(\d+(?:\.\d+)?)\%\s*(?:,\s*(\d+(?:\.\d+)?)\s*)?\)/,parse:function(a){return[2.55*a[1],2.55*a[2],2.55*a[3],a[4]]
}},{re:/#([a-fA-F0-9]{2})([a-fA-F0-9]{2})([a-fA-F0-9]{2})/,parse:function(a){return[parseInt(a[1],16),parseInt(a[2],16),parseInt(a[3],16)]
}},{re:/#([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])/,parse:function(a){return[parseInt(a[1]+a[1],16),parseInt(a[2]+a[2],16),parseInt(a[3]+a[3],16)]
}}]}
});
globalProvider.provide.factory("Color",["Helper",function(a){return{value:{h:1,s:1,b:1,a:1},rgb:function(){var b=this.toRGB();
return"rgb("+b.r+","+b.g+","+b.b+")"
},rgba:function(){var b=this.toRGB();
return"rgba("+b.r+","+b.g+","+b.b+","+b.a+")"
},hex:function(){return this.toHex()
},RGBtoHSB:function(j,i,c,e){j/=255;
i/=255;
c/=255;
var h,f,d,k;
d=Math.max(j,i,c);
k=d-Math.min(j,i,c);
h=(k===0?null:d==j?(i-c)/k:d==i?(c-j)/k+2:(j-i)/k+4);
h=((h+360)%6)*60/360;
f=k===0?0:k/d;
return{h:h||1,s:f,b:d,a:e||1}
},setColor:function(f){f=f.toLowerCase();
for(var d in a.stringParsers){if(a.stringParsers.hasOwnProperty(d)){var g=a.stringParsers[d];
var c=g.re.exec(f),b=c&&g.parse(c),e=g.space||"rgba";
if(b){this.value=this.RGBtoHSB.apply(null,b);
return false
}}}},setHue:function(b){this.value.h=1-b
},setSaturation:function(b){this.value.s=b
},setLightness:function(c){this.value.b=1-c
},setAlpha:function(b){this.value.a=parseInt((1-b)*100,10)/100
},toRGB:function(g,l,i,j){if(!g){g=this.value.h;
l=this.value.s;
i=this.value.b
}g*=360;
var f,k,d,e,c;
g=(g%360)/60;
c=i*l;
e=c*(1-Math.abs(g%2-1));
f=k=d=i-c;
g=~~g;
f+=[c,e,0,0,e,c][g];
k+=[e,c,c,e,0,0][g];
d+=[0,0,e,c,c,e][g];
return{r:Math.round(f*255),g:Math.round(k*255),b:Math.round(d*255),a:j||this.value.a}
},toHex:function(g,f,c,d){var e=this.toRGB(g,f,c,d);
return"#"+((1<<24)|(parseInt(e.r,10)<<16)|(parseInt(e.g,10)<<8)|parseInt(e.b,10)).toString(16).substr(1)
}}
}]);
globalProvider.provide.factory("Slider",["Helper",function(a){var b={maxLeft:0,maxTop:0,callLeft:null,callTop:null,knob:{top:0,left:0}},c={};
return{getSlider:function(){return b
},getLeftPosition:function(d){return Math.max(0,Math.min(b.maxLeft,b.left+((d.pageX||c.left)-c.left)))
},getTopPosition:function(d){return Math.max(0,Math.min(b.maxTop,b.top+((d.pageY||c.top)-c.top)))
},setSlider:function(d){var e=a.closestSlider(d.target),f=a.getOffset(e);
b.knob=e.children[0].style;
b.left=d.pageX-f.left-window.pageXOffset+f.scrollX;
b.top=d.pageY-f.top-window.pageYOffset+f.scrollY;
c={left:d.pageX,top:d.pageY}
},setSaturation:function(d){b={maxLeft:100,maxTop:100,callLeft:"setSaturation",callTop:"setLightness"};
this.setSlider(d)
},setHue:function(d){b={maxLeft:0,maxTop:100,callLeft:false,callTop:"setHue"};
this.setSlider(d)
},setAlpha:function(d){b={maxLeft:0,maxTop:100,callLeft:false,callTop:"setAlpha"};
this.setSlider(d)
},setKnob:function(e,d){b.knob.top=e+"px";
b.knob.left=d+"px"
}}
}]);
globalProvider.compileProvider.directive("colorpicker",["$document","$compile","Color","Slider","Helper",function(e,a,d,c,b){return{require:"?ngModel",restrict:"A",link:function(s,w,r,m){var v=r.colorpicker?r.colorpicker:"hex",C=angular.isDefined(r.colorpickerPosition)?r.colorpickerPosition:"bottom",f=angular.isDefined(r.colorpickerFixedPosition)?r.colorpickerFixedPosition:false,B=angular.isDefined(r.colorpickerParent)?w.parent():angular.element(document.body),i=angular.isDefined(r.colorpickerWithInput)?r.colorpickerWithInput:false,h=i?'<input type="text" name="colorpicker-input">':"",A='<div class="colorpicker dropdown"><div class="dropdown-menu"><colorpicker-saturation><i></i></colorpicker-saturation><colorpicker-hue><i></i></colorpicker-hue><colorpicker-alpha><i></i></colorpicker-alpha><colorpicker-preview></colorpicker-preview>'+h+'<button class="close close-colorpicker">&times;</button></div></div>',t=angular.element(A),z=d,q,E=t.find("colorpicker-hue"),k=t.find("colorpicker-saturation"),g=t.find("colorpicker-preview"),n=t.find("i");
a(t)(s);
if(i){var o=t.find("input");
o.on("mousedown",function(){event.stopPropagation()
}).on("keyup",function(G){var F=this.value;
w.val(F);
if(m){s.$apply(m.$setViewValue(F))
}G.stopPropagation();
G.preventDefault()
});
w.on("keyup",function(){o.val(w.val())
})
}var j=function(){e.on("mousemove",y);
e.on("mouseup",u)
};
if(v==="rgba"){t.addClass("alpha");
q=t.find("colorpicker-alpha");
q.on("click",function(F){c.setAlpha(F);
y(F)
}).on("mousedown",function(F){c.setAlpha(F);
j()
})
}E.on("click",function(F){c.setHue(F);
y(F)
}).on("mousedown",function(F){c.setHue(F);
j()
});
k.on("click",function(F){c.setSaturation(F);
y(F)
}).on("mousedown",function(F){c.setSaturation(F);
j()
});
if(f){t.addClass("colorpicker-fixed-position")
}t.addClass("colorpicker-position-"+C);
B.append(t);
if(m){m.$render=function(){w.val(m.$viewValue)
};
s.$watch(r.ngModel,function(){l()
})
}w.on("$destroy",function(){t.remove()
});
var p=function(){try{g.css("backgroundColor",z[v]())
}catch(F){g.css("backgroundColor",z.toHex())
}k.css("backgroundColor",z.toHex(z.value.h,1,1,1));
if(v==="rgba"){q.css.backgroundColor=z.toHex()
}};
var y=function(H){var J=c.getLeftPosition(H),I=c.getTopPosition(H),G=c.getSlider();
c.setKnob(I,J);
if(G.callLeft){z[G.callLeft].call(z,J/100)
}if(G.callTop){z[G.callTop].call(z,I/100)
}p();
var F=z[v]();
w.val(F);
if(m){s.$apply(m.$setViewValue(F))
}if(i){o.val(F)
}return false
};
var u=function(){e.off("mousemove",y);
e.off("mouseup",u)
};
var l=function(){z.setColor(w.val());
n.eq(0).css({left:z.value.s*100+"px",top:100-z.value.b*100+"px"});
n.eq(1).css("top",100*(1-z.value.h)+"px");
n.eq(2).css("top",100*(1-z.value.a)+"px");
p()
};
var x=function(){var G,F=b.getOffset(w[0]);
if(angular.isDefined(r.colorpickerParent)){F.left=0;
F.top=0
}if(C==="top"){G={top:F.top-147,left:F.left}
}else{if(C==="right"){G={top:F.top,left:F.left+126}
}else{if(C==="bottom"){G={top:F.top+w[0].offsetHeight+2,left:F.left}
}else{if(C==="left"){G={top:F.top,left:F.left-150}
}}}}return{top:G.top+"px",left:G.left+"px"}
};
w.on("click",function(){l();
t.addClass("colorpicker-visible").css(x())
});
t.on("mousedown",function(F){F.stopPropagation();
F.preventDefault()
});
var D=function(){if(t.hasClass("colorpicker-visible")){t.removeClass("colorpicker-visible")
}};
t.find("button").on("click",function(){D()
});
e.on("mousedown",function(){D()
})
}}
}]);