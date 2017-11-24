/* jQuery UI - v1.10.4 - 2014-07-22
* http://jqueryui.com
* Includes: jquery.ui.core.js, jquery.ui.widget.js, jquery.ui.mouse.js, jquery.ui.draggable.js, jquery.ui.droppable.js, jquery.ui.resizable.js, jquery.ui.sortable.js
* Copyright 2014 jQuery Foundation and other contributors; Licensed MIT */
(function(b,f){var a=0,e=/^ui-id-\d+$/;
b.ui=b.ui||{};
b.extend(b.ui,{version:"1.10.4",keyCode:{BACKSPACE:8,COMMA:188,DELETE:46,DOWN:40,END:35,ENTER:13,ESCAPE:27,HOME:36,LEFT:37,NUMPAD_ADD:107,NUMPAD_DECIMAL:110,NUMPAD_DIVIDE:111,NUMPAD_ENTER:108,NUMPAD_MULTIPLY:106,NUMPAD_SUBTRACT:109,PAGE_DOWN:34,PAGE_UP:33,PERIOD:190,RIGHT:39,SPACE:32,TAB:9,UP:38}});
b.fn.extend({focus:(function(g){return function(h,i){return typeof h==="number"?this.each(function(){var j=this;
setTimeout(function(){b(j).focus();
if(i){i.call(j)
}},h)
}):g.apply(this,arguments)
}
})(b.fn.focus),scrollParent:function(){var g;
if((b.ui.ie&&(/(static|relative)/).test(this.css("position")))||(/absolute/).test(this.css("position"))){g=this.parents().filter(function(){return(/(relative|absolute|fixed)/).test(b.css(this,"position"))&&(/(auto|scroll)/).test(b.css(this,"overflow")+b.css(this,"overflow-y")+b.css(this,"overflow-x"))
}).eq(0)
}else{g=this.parents().filter(function(){return(/(auto|scroll)/).test(b.css(this,"overflow")+b.css(this,"overflow-y")+b.css(this,"overflow-x"))
}).eq(0)
}return(/fixed/).test(this.css("position"))||!g.length?b(document):g
},zIndex:function(j){if(j!==f){return this.css("zIndex",j)
}if(this.length){var h=b(this[0]),g,i;
while(h.length&&h[0]!==document){g=h.css("position");
if(g==="absolute"||g==="relative"||g==="fixed"){i=parseInt(h.css("zIndex"),10);
if(!isNaN(i)&&i!==0){return i
}}h=h.parent()
}}return 0
},uniqueId:function(){return this.each(function(){if(!this.id){this.id="ui-id-"+(++a)
}})
},removeUniqueId:function(){return this.each(function(){if(e.test(this.id)){b(this).removeAttr("id")
}})
}});
function d(i,g){var k,j,h,l=i.nodeName.toLowerCase();
if("area"===l){k=i.parentNode;
j=k.name;
if(!i.href||!j||k.nodeName.toLowerCase()!=="map"){return false
}h=b("img[usemap=#"+j+"]")[0];
return !!h&&c(h)
}return(/input|select|textarea|button|object/.test(l)?!i.disabled:"a"===l?i.href||g:g)&&c(i)
}function c(g){return b.expr.filters.visible(g)&&!b(g).parents().addBack().filter(function(){return b.css(this,"visibility")==="hidden"
}).length
}b.extend(b.expr[":"],{data:b.expr.createPseudo?b.expr.createPseudo(function(g){return function(h){return !!b.data(h,g)
}
}):function(j,h,g){return !!b.data(j,g[3])
},focusable:function(g){return d(g,!isNaN(b.attr(g,"tabindex")))
},tabbable:function(i){var g=b.attr(i,"tabindex"),h=isNaN(g);
return(h||g>=0)&&d(i,!h)
}});
if(!b("<a>").outerWidth(1).jquery){b.each(["Width","Height"],function(j,g){var h=g==="Width"?["Left","Right"]:["Top","Bottom"],k=g.toLowerCase(),m={innerWidth:b.fn.innerWidth,innerHeight:b.fn.innerHeight,outerWidth:b.fn.outerWidth,outerHeight:b.fn.outerHeight};
function l(o,n,i,p){b.each(h,function(){n-=parseFloat(b.css(o,"padding"+this))||0;
if(i){n-=parseFloat(b.css(o,"border"+this+"Width"))||0
}if(p){n-=parseFloat(b.css(o,"margin"+this))||0
}});
return n
}b.fn["inner"+g]=function(i){if(i===f){return m["inner"+g].call(this)
}return this.each(function(){b(this).css(k,l(this,i)+"px")
})
};
b.fn["outer"+g]=function(i,n){if(typeof i!=="number"){return m["outer"+g].call(this,i)
}return this.each(function(){b(this).css(k,l(this,i,true,n)+"px")
})
}
})
}if(!b.fn.addBack){b.fn.addBack=function(g){return this.add(g==null?this.prevObject:this.prevObject.filter(g))
}
}if(b("<a>").data("a-b","a").removeData("a-b").data("a-b")){b.fn.removeData=(function(g){return function(h){if(arguments.length){return g.call(this,b.camelCase(h))
}else{return g.call(this)
}}
})(b.fn.removeData)
}b.ui.ie=!!/msie [\w.]+/.exec(navigator.userAgent.toLowerCase());
b.support.selectstart="onselectstart" in document.createElement("div");
b.fn.extend({disableSelection:function(){return this.bind((b.support.selectstart?"selectstart":"mousedown")+".ui-disableSelection",function(g){g.preventDefault()
})
},enableSelection:function(){return this.unbind(".ui-disableSelection")
}});
b.extend(b.ui,{plugin:{add:function(h,j,l){var g,k=b.ui[h].prototype;
for(g in l){k.plugins[g]=k.plugins[g]||[];
k.plugins[g].push([j,l[g]])
}},call:function(g,j,h){var k,l=g.plugins[j];
if(!l||!g.element[0].parentNode||g.element[0].parentNode.nodeType===11){return
}for(k=0;
k<l.length;
k++){if(g.options[l[k][0]]){l[k][1].apply(g.element,h)
}}}},hasScroll:function(j,h){if(b(j).css("overflow")==="hidden"){return false
}var g=(h&&h==="left")?"scrollLeft":"scrollTop",i=false;
if(j[g]>0){return true
}j[g]=1;
i=(j[g]>0);
j[g]=0;
return i
}})
})(jQuery);
(function(b,e){var a=0,d=Array.prototype.slice,c=b.cleanData;
b.cleanData=function(f){for(var g=0,h;
(h=f[g])!=null;
g++){try{b(h).triggerHandler("remove")
}catch(j){}}c(f)
};
b.widget=function(f,g,n){var k,l,i,m,h={},j=f.split(".")[0];
f=f.split(".")[1];
k=j+"-"+f;
if(!n){n=g;
g=b.Widget
}b.expr[":"][k.toLowerCase()]=function(o){return !!b.data(o,k)
};
b[j]=b[j]||{};
l=b[j][f];
i=b[j][f]=function(o,p){if(!this._createWidget){return new i(o,p)
}if(arguments.length){this._createWidget(o,p)
}};
b.extend(i,l,{version:n.version,_proto:b.extend({},n),_childConstructors:[]});
m=new g();
m.options=b.widget.extend({},m.options);
b.each(n,function(p,o){if(!b.isFunction(o)){h[p]=o;
return
}h[p]=(function(){var q=function(){return g.prototype[p].apply(this,arguments)
},r=function(s){return g.prototype[p].apply(this,s)
};
return function(){var u=this._super,s=this._superApply,t;
this._super=q;
this._superApply=r;
t=o.apply(this,arguments);
this._super=u;
this._superApply=s;
return t
}
})()
});
i.prototype=b.widget.extend(m,{widgetEventPrefix:l?(m.widgetEventPrefix||f):f},h,{constructor:i,namespace:j,widgetName:f,widgetFullName:k});
if(l){b.each(l._childConstructors,function(p,q){var o=q.prototype;
b.widget(o.namespace+"."+o.widgetName,i,q._proto)
});
delete l._childConstructors
}else{g._childConstructors.push(i)
}b.widget.bridge(f,i)
};
b.widget.extend=function(k){var g=d.call(arguments,1),j=0,f=g.length,h,i;
for(;
j<f;
j++){for(h in g[j]){i=g[j][h];
if(g[j].hasOwnProperty(h)&&i!==e){if(b.isPlainObject(i)){k[h]=b.isPlainObject(k[h])?b.widget.extend({},k[h],i):b.widget.extend({},i)
}else{k[h]=i
}}}}return k
};
b.widget.bridge=function(g,f){var h=f.prototype.widgetFullName||g;
b.fn[g]=function(k){var i=typeof k==="string",j=d.call(arguments,1),l=this;
k=!i&&j.length?b.widget.extend.apply(null,[k].concat(j)):k;
if(i){this.each(function(){var n,m=b.data(this,h);
if(!m){return b.error("cannot call methods on "+g+" prior to initialization; attempted to call method '"+k+"'")
}if(!b.isFunction(m[k])||k.charAt(0)==="_"){return b.error("no such method '"+k+"' for "+g+" widget instance")
}n=m[k].apply(m,j);
if(n!==m&&n!==e){l=n&&n.jquery?l.pushStack(n.get()):n;
return false
}})
}else{this.each(function(){var m=b.data(this,h);
if(m){m.option(k||{})._init()
}else{b.data(this,h,new f(k,this))
}})
}return l
}
};
b.Widget=function(){};
b.Widget._childConstructors=[];
b.Widget.prototype={widgetName:"widget",widgetEventPrefix:"",defaultElement:"<div>",options:{disabled:false,create:null},_createWidget:function(f,g){g=b(g||this.defaultElement||this)[0];
this.element=b(g);
this.uuid=a++;
this.eventNamespace="."+this.widgetName+this.uuid;
this.options=b.widget.extend({},this.options,this._getCreateOptions(),f);
this.bindings=b();
this.hoverable=b();
this.focusable=b();
if(g!==this){b.data(g,this.widgetFullName,this);
this._on(true,this.element,{remove:function(h){if(h.target===g){this.destroy()
}}});
this.document=b(g.style?g.ownerDocument:g.document||g);
this.window=b(this.document[0].defaultView||this.document[0].parentWindow)
}this._create();
this._trigger("create",null,this._getCreateEventData());
this._init()
},_getCreateOptions:b.noop,_getCreateEventData:b.noop,_create:b.noop,_init:b.noop,destroy:function(){this._destroy();
this.element.unbind(this.eventNamespace).removeData(this.widgetName).removeData(this.widgetFullName).removeData(b.camelCase(this.widgetFullName));
this.widget().unbind(this.eventNamespace).removeAttr("aria-disabled").removeClass(this.widgetFullName+"-disabled ui-state-disabled");
this.bindings.unbind(this.eventNamespace);
this.hoverable.removeClass("ui-state-hover");
this.focusable.removeClass("ui-state-focus")
},_destroy:b.noop,widget:function(){return this.element
},option:function(j,k){var f=j,l,h,g;
if(arguments.length===0){return b.widget.extend({},this.options)
}if(typeof j==="string"){f={};
l=j.split(".");
j=l.shift();
if(l.length){h=f[j]=b.widget.extend({},this.options[j]);
for(g=0;
g<l.length-1;
g++){h[l[g]]=h[l[g]]||{};
h=h[l[g]]
}j=l.pop();
if(arguments.length===1){return h[j]===e?null:h[j]
}h[j]=k
}else{if(arguments.length===1){return this.options[j]===e?null:this.options[j]
}f[j]=k
}}this._setOptions(f);
return this
},_setOptions:function(f){var g;
for(g in f){this._setOption(g,f[g])
}return this
},_setOption:function(f,g){this.options[f]=g;
if(f==="disabled"){this.widget().toggleClass(this.widgetFullName+"-disabled ui-state-disabled",!!g).attr("aria-disabled",g);
this.hoverable.removeClass("ui-state-hover");
this.focusable.removeClass("ui-state-focus")
}return this
},enable:function(){return this._setOption("disabled",false)
},disable:function(){return this._setOption("disabled",true)
},_on:function(i,h,g){var j,f=this;
if(typeof i!=="boolean"){g=h;
h=i;
i=false
}if(!g){g=h;
h=this.element;
j=this.widget()
}else{h=j=b(h);
this.bindings=this.bindings.add(h)
}b.each(g,function(p,o){function m(){if(!i&&(f.options.disabled===true||b(this).hasClass("ui-state-disabled"))){return
}return(typeof o==="string"?f[o]:o).apply(f,arguments)
}if(typeof o!=="string"){m.guid=o.guid=o.guid||m.guid||b.guid++
}var n=p.match(/^(\w+)\s*(.*)$/),l=n[1]+f.eventNamespace,k=n[2];
if(k){j.delegate(k,l,m)
}else{h.bind(l,m)
}})
},_off:function(g,f){f=(f||"").split(" ").join(this.eventNamespace+" ")+this.eventNamespace;
g.unbind(f).undelegate(f)
},_delay:function(i,h){function g(){return(typeof i==="string"?f[i]:i).apply(f,arguments)
}var f=this;
return setTimeout(g,h||0)
},_hoverable:function(f){this.hoverable=this.hoverable.add(f);
this._on(f,{mouseenter:function(g){b(g.currentTarget).addClass("ui-state-hover")
},mouseleave:function(g){b(g.currentTarget).removeClass("ui-state-hover")
}})
},_focusable:function(f){this.focusable=this.focusable.add(f);
this._on(f,{focusin:function(g){b(g.currentTarget).addClass("ui-state-focus")
},focusout:function(g){b(g.currentTarget).removeClass("ui-state-focus")
}})
},_trigger:function(f,g,h){var k,j,i=this.options[f];
h=h||{};
g=b.Event(g);
g.type=(f===this.widgetEventPrefix?f:this.widgetEventPrefix+f).toLowerCase();
g.target=this.element[0];
j=g.originalEvent;
if(j){for(k in j){if(!(k in g)){g[k]=j[k]
}}}this.element.trigger(g,h);
return !(b.isFunction(i)&&i.apply(this.element[0],[g].concat(h))===false||g.isDefaultPrevented())
}};
b.each({show:"fadeIn",hide:"fadeOut"},function(g,f){b.Widget.prototype["_"+g]=function(j,i,l){if(typeof i==="string"){i={effect:i}
}var k,h=!i?g:i===true||typeof i==="number"?f:i.effect||f;
i=i||{};
if(typeof i==="number"){i={duration:i}
}k=!b.isEmptyObject(i);
i.complete=l;
if(i.delay){j.delay(i.delay)
}if(k&&b.effects&&b.effects.effect[h]){j[g](i)
}else{if(h!==g&&j[h]){j[h](i.duration,i.easing,l)
}else{j.queue(function(m){b(this)[g]();
if(l){l.call(j[0])
}m()
})
}}}
})
})(jQuery);
(function(b,c){var a=false;
b(document).mouseup(function(){a=false
});
b.widget("ui.mouse",{version:"1.10.4",options:{cancel:"input,textarea,button,select,option",distance:1,delay:0},_mouseInit:function(){var d=this;
this.element.bind("mousedown."+this.widgetName,function(e){return d._mouseDown(e)
}).bind("click."+this.widgetName,function(e){if(true===b.data(e.target,d.widgetName+".preventClickEvent")){b.removeData(e.target,d.widgetName+".preventClickEvent");
e.stopImmediatePropagation();
return false
}});
this.started=false
},_mouseDestroy:function(){this.element.unbind("."+this.widgetName);
if(this._mouseMoveDelegate){b(document).unbind("mousemove."+this.widgetName,this._mouseMoveDelegate).unbind("mouseup."+this.widgetName,this._mouseUpDelegate)
}},_mouseDown:function(f){if(a){return
}(this._mouseStarted&&this._mouseUp(f));
this._mouseDownEvent=f;
var e=this,g=(f.which===1),d=(typeof this.options.cancel==="string"&&f.target.nodeName?b(f.target).closest(this.options.cancel).length:false);
if(!g||d||!this._mouseCapture(f)){return true
}this.mouseDelayMet=!this.options.delay;
if(!this.mouseDelayMet){this._mouseDelayTimer=setTimeout(function(){e.mouseDelayMet=true
},this.options.delay)
}if(this._mouseDistanceMet(f)&&this._mouseDelayMet(f)){this._mouseStarted=(this._mouseStart(f)!==false);
if(!this._mouseStarted){f.preventDefault();
return true
}}if(true===b.data(f.target,this.widgetName+".preventClickEvent")){b.removeData(f.target,this.widgetName+".preventClickEvent")
}this._mouseMoveDelegate=function(h){return e._mouseMove(h)
};
this._mouseUpDelegate=function(h){return e._mouseUp(h)
};
b(document).bind("mousemove."+this.widgetName,this._mouseMoveDelegate).bind("mouseup."+this.widgetName,this._mouseUpDelegate);
f.preventDefault();
a=true;
return true
},_mouseMove:function(d){if(b.ui.ie&&(!document.documentMode||document.documentMode<9)&&!d.button){return this._mouseUp(d)
}if(this._mouseStarted){this._mouseDrag(d);
return d.preventDefault()
}if(this._mouseDistanceMet(d)&&this._mouseDelayMet(d)){this._mouseStarted=(this._mouseStart(this._mouseDownEvent,d)!==false);
(this._mouseStarted?this._mouseDrag(d):this._mouseUp(d))
}return !this._mouseStarted
},_mouseUp:function(d){b(document).unbind("mousemove."+this.widgetName,this._mouseMoveDelegate).unbind("mouseup."+this.widgetName,this._mouseUpDelegate);
if(this._mouseStarted){this._mouseStarted=false;
if(d.target===this._mouseDownEvent.target){b.data(d.target,this.widgetName+".preventClickEvent",true)
}this._mouseStop(d)
}return false
},_mouseDistanceMet:function(d){return(Math.max(Math.abs(this._mouseDownEvent.pageX-d.pageX),Math.abs(this._mouseDownEvent.pageY-d.pageY))>=this.options.distance)
},_mouseDelayMet:function(){return this.mouseDelayMet
},_mouseStart:function(){},_mouseDrag:function(){},_mouseStop:function(){},_mouseCapture:function(){return true
}})
})(jQuery);
(function(a,b){a.widget("ui.draggable",a.ui.mouse,{version:"1.10.4",widgetEventPrefix:"drag",options:{addClasses:true,appendTo:"parent",axis:false,connectToSortable:false,containment:false,cursor:"auto",cursorAt:false,grid:false,handle:false,helper:"original",iframeFix:false,opacity:false,refreshPositions:false,revert:false,revertDuration:500,scope:"default",scroll:true,scrollSensitivity:20,scrollSpeed:20,snap:false,snapMode:"both",snapTolerance:20,stack:false,zIndex:false,drag:null,start:null,stop:null},_create:function(){if(this.options.helper==="original"&&!(/^(?:r|a|f)/).test(this.element.css("position"))){this.element[0].style.position="relative"
}if(this.options.addClasses){this.element.addClass("ui-draggable")
}if(this.options.disabled){this.element.addClass("ui-draggable-disabled")
}this._mouseInit()
},_destroy:function(){this.element.removeClass("ui-draggable ui-draggable-dragging ui-draggable-disabled");
this._mouseDestroy()
},_mouseCapture:function(c){var d=this.options;
if(this.helper||d.disabled||a(c.target).closest(".ui-resizable-handle").length>0){return false
}this.handle=this._getHandle(c);
if(!this.handle){return false
}a(d.iframeFix===true?"iframe":d.iframeFix).each(function(){a("<div class='ui-draggable-iframeFix' style='background: #fff;'></div>").css({width:this.offsetWidth+"px",height:this.offsetHeight+"px",position:"absolute",opacity:"0.001",zIndex:1000}).css(a(this).offset()).appendTo("body")
});
return true
},_mouseStart:function(c){var d=this.options;
this.helper=this._createHelper(c);
this.helper.addClass("ui-draggable-dragging");
this._cacheHelperProportions();
if(a.ui.ddmanager){a.ui.ddmanager.current=this
}this._cacheMargins();
this.cssPosition=this.helper.css("position");
this.scrollParent=this.helper.scrollParent();
this.offsetParent=this.helper.offsetParent();
this.offsetParentCssPosition=this.offsetParent.css("position");
this.offset=this.positionAbs=this.element.offset();
this.offset={top:this.offset.top-this.margins.top,left:this.offset.left-this.margins.left};
this.offset.scroll=false;
a.extend(this.offset,{click:{left:c.pageX-this.offset.left,top:c.pageY-this.offset.top},parent:this._getParentOffset(),relative:this._getRelativeOffset()});
this.originalPosition=this.position=this._generatePosition(c);
this.originalPageX=c.pageX;
this.originalPageY=c.pageY;
(d.cursorAt&&this._adjustOffsetFromHelper(d.cursorAt));
this._setContainment();
if(this._trigger("start",c)===false){this._clear();
return false
}this._cacheHelperProportions();
if(a.ui.ddmanager&&!d.dropBehaviour){a.ui.ddmanager.prepareOffsets(this,c)
}this._mouseDrag(c,true);
if(a.ui.ddmanager){a.ui.ddmanager.dragStart(this,c)
}return true
},_mouseDrag:function(c,e){if(this.offsetParentCssPosition==="fixed"){this.offset.parent=this._getParentOffset()
}this.position=this._generatePosition(c);
this.positionAbs=this._convertPositionTo("absolute");
if(!e){var d=this._uiHash();
if(this._trigger("drag",c,d)===false){this._mouseUp({});
return false
}this.position=d.position
}if(!this.options.axis||this.options.axis!=="y"){this.helper[0].style.left=this.position.left+"px"
}if(!this.options.axis||this.options.axis!=="x"){this.helper[0].style.top=this.position.top+"px"
}if(a.ui.ddmanager){a.ui.ddmanager.drag(this,c)
}return false
},_mouseStop:function(d){var c=this,e=false;
if(a.ui.ddmanager&&!this.options.dropBehaviour){e=a.ui.ddmanager.drop(this,d)
}if(this.dropped){e=this.dropped;
this.dropped=false
}if(this.options.helper==="original"&&!a.contains(this.element[0].ownerDocument,this.element[0])){return false
}if((this.options.revert==="invalid"&&!e)||(this.options.revert==="valid"&&e)||this.options.revert===true||(a.isFunction(this.options.revert)&&this.options.revert.call(this.element,e))){a(this.helper).animate(this.originalPosition,parseInt(this.options.revertDuration,10),function(){if(c._trigger("stop",d)!==false){c._clear()
}})
}else{if(this._trigger("stop",d)!==false){this._clear()
}}return false
},_mouseUp:function(c){a("div.ui-draggable-iframeFix").each(function(){this.parentNode.removeChild(this)
});
if(a.ui.ddmanager){a.ui.ddmanager.dragStop(this,c)
}return a.ui.mouse.prototype._mouseUp.call(this,c)
},cancel:function(){if(this.helper.is(".ui-draggable-dragging")){this._mouseUp({})
}else{this._clear()
}return this
},_getHandle:function(c){return this.options.handle?!!a(c.target).closest(this.element.find(this.options.handle)).length:true
},_createHelper:function(d){var e=this.options,c=a.isFunction(e.helper)?a(e.helper.apply(this.element[0],[d])):(e.helper==="clone"?this.element.clone().removeAttr("id"):this.element);
if(!c.parents("body").length){c.appendTo((e.appendTo==="parent"?this.element[0].parentNode:e.appendTo))
}if(c[0]!==this.element[0]&&!(/(fixed|absolute)/).test(c.css("position"))){c.css("position","absolute")
}return c
},_adjustOffsetFromHelper:function(c){if(typeof c==="string"){c=c.split(" ")
}if(a.isArray(c)){c={left:+c[0],top:+c[1]||0}
}if("left" in c){this.offset.click.left=c.left+this.margins.left
}if("right" in c){this.offset.click.left=this.helperProportions.width-c.right+this.margins.left
}if("top" in c){this.offset.click.top=c.top+this.margins.top
}if("bottom" in c){this.offset.click.top=this.helperProportions.height-c.bottom+this.margins.top
}},_getParentOffset:function(){var c=this.offsetParent.offset();
if(this.cssPosition==="absolute"&&this.scrollParent[0]!==document&&a.contains(this.scrollParent[0],this.offsetParent[0])){c.left+=this.scrollParent.scrollLeft();
c.top+=this.scrollParent.scrollTop()
}if((this.offsetParent[0]===document.body)||(this.offsetParent[0].tagName&&this.offsetParent[0].tagName.toLowerCase()==="html"&&a.ui.ie)){c={top:0,left:0}
}return{top:c.top+(parseInt(this.offsetParent.css("borderTopWidth"),10)||0),left:c.left+(parseInt(this.offsetParent.css("borderLeftWidth"),10)||0)}
},_getRelativeOffset:function(){if(this.cssPosition==="relative"){var c=this.element.position();
return{top:c.top-(parseInt(this.helper.css("top"),10)||0)+this.scrollParent.scrollTop(),left:c.left-(parseInt(this.helper.css("left"),10)||0)+this.scrollParent.scrollLeft()}
}else{return{top:0,left:0}
}},_cacheMargins:function(){this.margins={left:(parseInt(this.element.css("marginLeft"),10)||0),top:(parseInt(this.element.css("marginTop"),10)||0),right:(parseInt(this.element.css("marginRight"),10)||0),bottom:(parseInt(this.element.css("marginBottom"),10)||0)}
},_cacheHelperProportions:function(){this.helperProportions={width:this.helper.outerWidth(),height:this.helper.outerHeight()}
},_setContainment:function(){var e,g,d,f=this.options;
if(!f.containment){this.containment=null;
return
}if(f.containment==="window"){this.containment=[a(window).scrollLeft()-this.offset.relative.left-this.offset.parent.left,a(window).scrollTop()-this.offset.relative.top-this.offset.parent.top,a(window).scrollLeft()+a(window).width()-this.helperProportions.width-this.margins.left,a(window).scrollTop()+(a(window).height()||document.body.parentNode.scrollHeight)-this.helperProportions.height-this.margins.top];
return
}if(f.containment==="document"){this.containment=[0,0,a(document).width()-this.helperProportions.width-this.margins.left,(a(document).height()||document.body.parentNode.scrollHeight)-this.helperProportions.height-this.margins.top];
return
}if(f.containment.constructor===Array){this.containment=f.containment;
return
}if(f.containment==="parent"){f.containment=this.helper[0].parentNode
}g=a(f.containment);
d=g[0];
if(!d){return
}e=g.css("overflow")!=="hidden";
this.containment=[(parseInt(g.css("borderLeftWidth"),10)||0)+(parseInt(g.css("paddingLeft"),10)||0),(parseInt(g.css("borderTopWidth"),10)||0)+(parseInt(g.css("paddingTop"),10)||0),(e?Math.max(d.scrollWidth,d.offsetWidth):d.offsetWidth)-(parseInt(g.css("borderRightWidth"),10)||0)-(parseInt(g.css("paddingRight"),10)||0)-this.helperProportions.width-this.margins.left-this.margins.right,(e?Math.max(d.scrollHeight,d.offsetHeight):d.offsetHeight)-(parseInt(g.css("borderBottomWidth"),10)||0)-(parseInt(g.css("paddingBottom"),10)||0)-this.helperProportions.height-this.margins.top-this.margins.bottom];
this.relative_container=g
},_convertPositionTo:function(f,g){if(!g){g=this.position
}var e=f==="absolute"?1:-1,c=this.cssPosition==="absolute"&&!(this.scrollParent[0]!==document&&a.contains(this.scrollParent[0],this.offsetParent[0]))?this.offsetParent:this.scrollParent;
if(!this.offset.scroll){this.offset.scroll={top:c.scrollTop(),left:c.scrollLeft()}
}return{top:(g.top+this.offset.relative.top*e+this.offset.parent.top*e-((this.cssPosition==="fixed"?-this.scrollParent.scrollTop():this.offset.scroll.top)*e)),left:(g.left+this.offset.relative.left*e+this.offset.parent.left*e-((this.cssPosition==="fixed"?-this.scrollParent.scrollLeft():this.offset.scroll.left)*e))}
},_generatePosition:function(d){var c,i,j,f,e=this.options,k=this.cssPosition==="absolute"&&!(this.scrollParent[0]!==document&&a.contains(this.scrollParent[0],this.offsetParent[0]))?this.offsetParent:this.scrollParent,h=d.pageX,g=d.pageY;
if(!this.offset.scroll){this.offset.scroll={top:k.scrollTop(),left:k.scrollLeft()}
}if(this.originalPosition){if(this.containment){if(this.relative_container){i=this.relative_container.offset();
c=[this.containment[0]+i.left,this.containment[1]+i.top,this.containment[2]+i.left,this.containment[3]+i.top]
}else{c=this.containment
}if(d.pageX-this.offset.click.left<c[0]){h=c[0]+this.offset.click.left
}if(d.pageY-this.offset.click.top<c[1]){g=c[1]+this.offset.click.top
}if(d.pageX-this.offset.click.left>c[2]){h=c[2]+this.offset.click.left
}if(d.pageY-this.offset.click.top>c[3]){g=c[3]+this.offset.click.top
}}if(e.grid){j=e.grid[1]?this.originalPageY+Math.round((g-this.originalPageY)/e.grid[1])*e.grid[1]:this.originalPageY;
g=c?((j-this.offset.click.top>=c[1]||j-this.offset.click.top>c[3])?j:((j-this.offset.click.top>=c[1])?j-e.grid[1]:j+e.grid[1])):j;
f=e.grid[0]?this.originalPageX+Math.round((h-this.originalPageX)/e.grid[0])*e.grid[0]:this.originalPageX;
h=c?((f-this.offset.click.left>=c[0]||f-this.offset.click.left>c[2])?f:((f-this.offset.click.left>=c[0])?f-e.grid[0]:f+e.grid[0])):f
}}return{top:(g-this.offset.click.top-this.offset.relative.top-this.offset.parent.top+(this.cssPosition==="fixed"?-this.scrollParent.scrollTop():this.offset.scroll.top)),left:(h-this.offset.click.left-this.offset.relative.left-this.offset.parent.left+(this.cssPosition==="fixed"?-this.scrollParent.scrollLeft():this.offset.scroll.left))}
},_clear:function(){this.helper.removeClass("ui-draggable-dragging");
if(this.helper[0]!==this.element[0]&&!this.cancelHelperRemoval){this.helper.remove()
}this.helper=null;
this.cancelHelperRemoval=false
},_trigger:function(c,d,e){e=e||this._uiHash();
a.ui.plugin.call(this,c,[d,e]);
if(c==="drag"){this.positionAbs=this._convertPositionTo("absolute")
}return a.Widget.prototype._trigger.call(this,c,d,e)
},plugins:{},_uiHash:function(){return{helper:this.helper,position:this.position,originalPosition:this.originalPosition,offset:this.positionAbs}
}});
a.ui.plugin.add("draggable","connectToSortable",{start:function(d,f){var e=a(this).data("ui-draggable"),g=e.options,c=a.extend({},f,{item:e.element});
e.sortables=[];
a(g.connectToSortable).each(function(){var h=a.data(this,"ui-sortable");
if(h&&!h.options.disabled){e.sortables.push({instance:h,shouldRevert:h.options.revert});
h.refreshPositions();
h._trigger("activate",d,c)
}})
},stop:function(d,f){var e=a(this).data("ui-draggable"),c=a.extend({},f,{item:e.element});
a.each(e.sortables,function(){if(this.instance.isOver){this.instance.isOver=0;
e.cancelHelperRemoval=true;
this.instance.cancelHelperRemoval=false;
if(this.shouldRevert){this.instance.options.revert=this.shouldRevert
}this.instance._mouseStop(d);
this.instance.options.helper=this.instance.options._helper;
if(e.options.helper==="original"){this.instance.currentItem.css({top:"auto",left:"auto"})
}}else{this.instance.cancelHelperRemoval=false;
this.instance._trigger("deactivate",d,c)
}})
},drag:function(d,f){var e=a(this).data("ui-draggable"),c=this;
a.each(e.sortables,function(){var g=false,h=this;
this.instance.positionAbs=e.positionAbs;
this.instance.helperProportions=e.helperProportions;
this.instance.offset.click=e.offset.click;
if(this.instance._intersectsWith(this.instance.containerCache)){g=true;
a.each(e.sortables,function(){this.instance.positionAbs=e.positionAbs;
this.instance.helperProportions=e.helperProportions;
this.instance.offset.click=e.offset.click;
if(this!==h&&this.instance._intersectsWith(this.instance.containerCache)&&a.contains(h.instance.element[0],this.instance.element[0])){g=false
}return g
})
}if(g){if(!this.instance.isOver){this.instance.isOver=1;
this.instance.currentItem=a(c).clone().removeAttr("id").appendTo(this.instance.element).data("ui-sortable-item",true);
this.instance.options._helper=this.instance.options.helper;
this.instance.options.helper=function(){return f.helper[0]
};
d.target=this.instance.currentItem[0];
this.instance._mouseCapture(d,true);
this.instance._mouseStart(d,true,true);
this.instance.offset.click.top=e.offset.click.top;
this.instance.offset.click.left=e.offset.click.left;
this.instance.offset.parent.left-=e.offset.parent.left-this.instance.offset.parent.left;
this.instance.offset.parent.top-=e.offset.parent.top-this.instance.offset.parent.top;
e._trigger("toSortable",d);
e.dropped=this.instance.element;
e.currentItem=e.element;
this.instance.fromOutside=e
}if(this.instance.currentItem){this.instance._mouseDrag(d)
}}else{if(this.instance.isOver){this.instance.isOver=0;
this.instance.cancelHelperRemoval=true;
this.instance.options.revert=false;
this.instance._trigger("out",d,this.instance._uiHash(this.instance));
this.instance._mouseStop(d,true);
this.instance.options.helper=this.instance.options._helper;
this.instance.currentItem.remove();
if(this.instance.placeholder){this.instance.placeholder.remove()
}e._trigger("fromSortable",d);
e.dropped=false
}}})
}});
a.ui.plugin.add("draggable","cursor",{start:function(){var c=a("body"),d=a(this).data("ui-draggable").options;
if(c.css("cursor")){d._cursor=c.css("cursor")
}c.css("cursor",d.cursor)
},stop:function(){var c=a(this).data("ui-draggable").options;
if(c._cursor){a("body").css("cursor",c._cursor)
}}});
a.ui.plugin.add("draggable","opacity",{start:function(d,e){var c=a(e.helper),f=a(this).data("ui-draggable").options;
if(c.css("opacity")){f._opacity=c.css("opacity")
}c.css("opacity",f.opacity)
},stop:function(c,d){var e=a(this).data("ui-draggable").options;
if(e._opacity){a(d.helper).css("opacity",e._opacity)
}}});
a.ui.plugin.add("draggable","scroll",{start:function(){var c=a(this).data("ui-draggable");
if(c.scrollParent[0]!==document&&c.scrollParent[0].tagName!=="HTML"){c.overflowOffset=c.scrollParent.offset()
}},drag:function(e){var d=a(this).data("ui-draggable"),f=d.options,c=false;
if(d.scrollParent[0]!==document&&d.scrollParent[0].tagName!=="HTML"){if(!f.axis||f.axis!=="x"){if((d.overflowOffset.top+d.scrollParent[0].offsetHeight)-e.pageY<f.scrollSensitivity){d.scrollParent[0].scrollTop=c=d.scrollParent[0].scrollTop+f.scrollSpeed
}else{if(e.pageY-d.overflowOffset.top<f.scrollSensitivity){d.scrollParent[0].scrollTop=c=d.scrollParent[0].scrollTop-f.scrollSpeed
}}}if(!f.axis||f.axis!=="y"){if((d.overflowOffset.left+d.scrollParent[0].offsetWidth)-e.pageX<f.scrollSensitivity){d.scrollParent[0].scrollLeft=c=d.scrollParent[0].scrollLeft+f.scrollSpeed
}else{if(e.pageX-d.overflowOffset.left<f.scrollSensitivity){d.scrollParent[0].scrollLeft=c=d.scrollParent[0].scrollLeft-f.scrollSpeed
}}}}else{if(!f.axis||f.axis!=="x"){if(e.pageY-a(document).scrollTop()<f.scrollSensitivity){c=a(document).scrollTop(a(document).scrollTop()-f.scrollSpeed)
}else{if(a(window).height()-(e.pageY-a(document).scrollTop())<f.scrollSensitivity){c=a(document).scrollTop(a(document).scrollTop()+f.scrollSpeed)
}}}if(!f.axis||f.axis!=="y"){if(e.pageX-a(document).scrollLeft()<f.scrollSensitivity){c=a(document).scrollLeft(a(document).scrollLeft()-f.scrollSpeed)
}else{if(a(window).width()-(e.pageX-a(document).scrollLeft())<f.scrollSensitivity){c=a(document).scrollLeft(a(document).scrollLeft()+f.scrollSpeed)
}}}}if(c!==false&&a.ui.ddmanager&&!f.dropBehaviour){a.ui.ddmanager.prepareOffsets(d,e)
}}});
a.ui.plugin.add("draggable","snap",{start:function(){var c=a(this).data("ui-draggable"),d=c.options;
c.snapElements=[];
a(d.snap.constructor!==String?(d.snap.items||":data(ui-draggable)"):d.snap).each(function(){var f=a(this),e=f.offset();
if(this!==c.element[0]){c.snapElements.push({item:this,width:f.outerWidth(),height:f.outerHeight(),top:e.top,left:e.left})
}})
},drag:function(u,p){var c,z,j,k,s,n,m,A,v,h,g=a(this).data("ui-draggable"),q=g.options,y=q.snapTolerance,x=p.offset.left,w=x+g.helperProportions.width,f=p.offset.top,e=f+g.helperProportions.height;
for(v=g.snapElements.length-1;
v>=0;
v--){s=g.snapElements[v].left;
n=s+g.snapElements[v].width;
m=g.snapElements[v].top;
A=m+g.snapElements[v].height;
if(w<s-y||x>n+y||e<m-y||f>A+y||!a.contains(g.snapElements[v].item.ownerDocument,g.snapElements[v].item)){if(g.snapElements[v].snapping){(g.options.snap.release&&g.options.snap.release.call(g.element,u,a.extend(g._uiHash(),{snapItem:g.snapElements[v].item})))
}g.snapElements[v].snapping=false;
continue
}if(q.snapMode!=="inner"){c=Math.abs(m-e)<=y;
z=Math.abs(A-f)<=y;
j=Math.abs(s-w)<=y;
k=Math.abs(n-x)<=y;
if(c){p.position.top=g._convertPositionTo("relative",{top:m-g.helperProportions.height,left:0}).top-g.margins.top
}if(z){p.position.top=g._convertPositionTo("relative",{top:A,left:0}).top-g.margins.top
}if(j){p.position.left=g._convertPositionTo("relative",{top:0,left:s-g.helperProportions.width}).left-g.margins.left
}if(k){p.position.left=g._convertPositionTo("relative",{top:0,left:n}).left-g.margins.left
}}h=(c||z||j||k);
if(q.snapMode!=="outer"){c=Math.abs(m-f)<=y;
z=Math.abs(A-e)<=y;
j=Math.abs(s-x)<=y;
k=Math.abs(n-w)<=y;
if(c){p.position.top=g._convertPositionTo("relative",{top:m,left:0}).top-g.margins.top
}if(z){p.position.top=g._convertPositionTo("relative",{top:A-g.helperProportions.height,left:0}).top-g.margins.top
}if(j){p.position.left=g._convertPositionTo("relative",{top:0,left:s}).left-g.margins.left
}if(k){p.position.left=g._convertPositionTo("relative",{top:0,left:n-g.helperProportions.width}).left-g.margins.left
}}if(!g.snapElements[v].snapping&&(c||z||j||k||h)){(g.options.snap.snap&&g.options.snap.snap.call(g.element,u,a.extend(g._uiHash(),{snapItem:g.snapElements[v].item})))
}g.snapElements[v].snapping=(c||z||j||k||h)
}}});
a.ui.plugin.add("draggable","stack",{start:function(){var c,e=this.data("ui-draggable").options,d=a.makeArray(a(e.stack)).sort(function(g,f){return(parseInt(a(g).css("zIndex"),10)||0)-(parseInt(a(f).css("zIndex"),10)||0)
});
if(!d.length){return
}c=parseInt(a(d[0]).css("zIndex"),10)||0;
a(d).each(function(f){a(this).css("zIndex",c+f)
});
this.css("zIndex",(c+d.length))
}});
a.ui.plugin.add("draggable","zIndex",{start:function(d,e){var c=a(e.helper),f=a(this).data("ui-draggable").options;
if(c.css("zIndex")){f._zIndex=c.css("zIndex")
}c.css("zIndex",f.zIndex)
},stop:function(c,d){var e=a(this).data("ui-draggable").options;
if(e._zIndex){a(d.helper).css("zIndex",e._zIndex)
}}})
})(jQuery);
(function(b,c){function a(e,d,f){return(e>d)&&(e<(d+f))
}b.widget("ui.droppable",{version:"1.10.4",widgetEventPrefix:"drop",options:{accept:"*",activeClass:false,addClasses:true,greedy:false,hoverClass:false,scope:"default",tolerance:"intersect",activate:null,deactivate:null,drop:null,out:null,over:null},_create:function(){var e,f=this.options,d=f.accept;
this.isover=false;
this.isout=true;
this.accept=b.isFunction(d)?d:function(g){return g.is(d)
};
this.proportions=function(){if(arguments.length){e=arguments[0]
}else{return e?e:e={width:this.element[0].offsetWidth,height:this.element[0].offsetHeight}
}};
b.ui.ddmanager.droppables[f.scope]=b.ui.ddmanager.droppables[f.scope]||[];
b.ui.ddmanager.droppables[f.scope].push(this);
(f.addClasses&&this.element.addClass("ui-droppable"))
},_destroy:function(){var e=0,d=b.ui.ddmanager.droppables[this.options.scope];
for(;
e<d.length;
e++){if(d[e]===this){d.splice(e,1)
}}this.element.removeClass("ui-droppable ui-droppable-disabled")
},_setOption:function(d,e){if(d==="accept"){this.accept=b.isFunction(e)?e:function(f){return f.is(e)
}
}b.Widget.prototype._setOption.apply(this,arguments)
},_activate:function(e){var d=b.ui.ddmanager.current;
if(this.options.activeClass){this.element.addClass(this.options.activeClass)
}if(d){this._trigger("activate",e,this.ui(d))
}},_deactivate:function(e){var d=b.ui.ddmanager.current;
if(this.options.activeClass){this.element.removeClass(this.options.activeClass)
}if(d){this._trigger("deactivate",e,this.ui(d))
}},_over:function(e){var d=b.ui.ddmanager.current;
if(!d||(d.currentItem||d.element)[0]===this.element[0]){return
}if(this.accept.call(this.element[0],(d.currentItem||d.element))){if(this.options.hoverClass){this.element.addClass(this.options.hoverClass)
}this._trigger("over",e,this.ui(d))
}},_out:function(e){var d=b.ui.ddmanager.current;
if(!d||(d.currentItem||d.element)[0]===this.element[0]){return
}if(this.accept.call(this.element[0],(d.currentItem||d.element))){if(this.options.hoverClass){this.element.removeClass(this.options.hoverClass)
}this._trigger("out",e,this.ui(d))
}},_drop:function(e,f){var d=f||b.ui.ddmanager.current,g=false;
if(!d||(d.currentItem||d.element)[0]===this.element[0]){return false
}this.element.find(":data(ui-droppable)").not(".ui-draggable-dragging").each(function(){var h=b.data(this,"ui-droppable");
if(h.options.greedy&&!h.options.disabled&&h.options.scope===d.options.scope&&h.accept.call(h.element[0],(d.currentItem||d.element))&&b.ui.intersect(d,b.extend(h,{offset:h.element.offset()}),h.options.tolerance)){g=true;
return false
}});
if(g){return false
}if(this.accept.call(this.element[0],(d.currentItem||d.element))){if(this.options.activeClass){this.element.removeClass(this.options.activeClass)
}if(this.options.hoverClass){this.element.removeClass(this.options.hoverClass)
}this._trigger("drop",e,this.ui(d));
return this.element
}return false
},ui:function(d){return{draggable:(d.currentItem||d.element),helper:d.helper,position:d.position,offset:d.positionAbs}
}});
b.ui.intersect=function(q,j,o){if(!j.offset){return false
}var h,i,f=(q.positionAbs||q.position.absolute).left,n=(q.positionAbs||q.position.absolute).top,e=f+q.helperProportions.width,m=n+q.helperProportions.height,g=j.offset.left,p=j.offset.top,d=g+j.proportions().width,k=p+j.proportions().height;
switch(o){case"fit":return(g<=f&&e<=d&&p<=n&&m<=k);
case"intersect":return(g<f+(q.helperProportions.width/2)&&e-(q.helperProportions.width/2)<d&&p<n+(q.helperProportions.height/2)&&m-(q.helperProportions.height/2)<k);
case"pointer":h=((q.positionAbs||q.position.absolute).left+(q.clickOffset||q.offset.click).left);
i=((q.positionAbs||q.position.absolute).top+(q.clickOffset||q.offset.click).top);
return a(i,p,j.proportions().height)&&a(h,g,j.proportions().width);
case"touch":return((n>=p&&n<=k)||(m>=p&&m<=k)||(n<p&&m>k))&&((f>=g&&f<=d)||(e>=g&&e<=d)||(f<g&&e>d));
default:return false
}};
b.ui.ddmanager={current:null,droppables:{"default":[]},prepareOffsets:function(g,k){var f,e,d=b.ui.ddmanager.droppables[g.options.scope]||[],h=k?k.type:null,l=(g.currentItem||g.element).find(":data(ui-droppable)").addBack();
droppablesLoop:for(f=0;
f<d.length;
f++){if(d[f].options.disabled||(g&&!d[f].accept.call(d[f].element[0],(g.currentItem||g.element)))){continue
}for(e=0;
e<l.length;
e++){if(l[e]===d[f].element[0]){d[f].proportions().height=0;
continue droppablesLoop
}}d[f].visible=d[f].element.css("display")!=="none";
if(!d[f].visible){continue
}if(h==="mousedown"){d[f]._activate.call(d[f],k)
}d[f].offset=d[f].element.offset();
d[f].proportions({width:d[f].element[0].offsetWidth,height:d[f].element[0].offsetHeight})
}},drop:function(d,e){var f=false;
b.each((b.ui.ddmanager.droppables[d.options.scope]||[]).slice(),function(){if(!this.options){return
}if(!this.options.disabled&&this.visible&&b.ui.intersect(d,this,this.options.tolerance)){f=this._drop.call(this,e)||f
}if(!this.options.disabled&&this.visible&&this.accept.call(this.element[0],(d.currentItem||d.element))){this.isout=true;
this.isover=false;
this._deactivate.call(this,e)
}});
return f
},dragStart:function(d,e){d.element.parentsUntil("body").bind("scroll.droppable",function(){if(!d.options.refreshPositions){b.ui.ddmanager.prepareOffsets(d,e)
}})
},drag:function(d,e){if(d.options.refreshPositions){b.ui.ddmanager.prepareOffsets(d,e)
}b.each(b.ui.ddmanager.droppables[d.options.scope]||[],function(){if(this.options.disabled||this.greedyChild||!this.visible){return
}var i,g,f,h=b.ui.intersect(d,this,this.options.tolerance),j=!h&&this.isover?"isout":(h&&!this.isover?"isover":null);
if(!j){return
}if(this.options.greedy){g=this.options.scope;
f=this.element.parents(":data(ui-droppable)").filter(function(){return b.data(this,"ui-droppable").options.scope===g
});
if(f.length){i=b.data(f[0],"ui-droppable");
i.greedyChild=(j==="isover")
}}if(i&&j==="isover"){i.isover=false;
i.isout=true;
i._out.call(i,e)
}this[j]=true;
this[j==="isout"?"isover":"isout"]=false;
this[j==="isover"?"_over":"_out"].call(this,e);
if(i&&j==="isout"){i.isout=false;
i.isover=true;
i._over.call(i,e)
}})
},dragStop:function(d,e){d.element.parentsUntil("body").unbind("scroll.droppable");
if(!d.options.refreshPositions){b.ui.ddmanager.prepareOffsets(d,e)
}}}
})(jQuery);
(function(c,d){function b(e){return parseInt(e,10)||0
}function a(e){return !isNaN(parseInt(e,10))
}c.widget("ui.resizable",c.ui.mouse,{version:"1.10.4",widgetEventPrefix:"resize",options:{alsoResize:false,animate:false,animateDuration:"slow",animateEasing:"swing",aspectRatio:false,autoHide:false,containment:false,ghost:false,grid:false,handles:"e,s,se",helper:false,maxHeight:null,maxWidth:null,minHeight:10,minWidth:10,zIndex:90,resize:null,start:null,stop:null},_create:function(){var l,f,j,g,e,h=this,k=this.options;
this.element.addClass("ui-resizable");
c.extend(this,{_aspectRatio:!!(k.aspectRatio),aspectRatio:k.aspectRatio,originalElement:this.element,_proportionallyResizeElements:[],_helper:k.helper||k.ghost||k.animate?k.helper||"ui-resizable-helper":null});
if(this.element[0].nodeName.match(/canvas|textarea|input|select|button|img/i)){this.element.wrap(c("<div class='ui-wrapper' style='overflow: hidden;'></div>").css({position:this.element.css("position"),width:this.element.outerWidth(),height:this.element.outerHeight(),top:this.element.css("top"),left:this.element.css("left")}));
this.element=this.element.parent().data("ui-resizable",this.element.data("ui-resizable"));
this.elementIsWrapper=true;
this.element.css({marginLeft:this.originalElement.css("marginLeft"),marginTop:this.originalElement.css("marginTop"),marginRight:this.originalElement.css("marginRight"),marginBottom:this.originalElement.css("marginBottom")});
this.originalElement.css({marginLeft:0,marginTop:0,marginRight:0,marginBottom:0});
this.originalResizeStyle=this.originalElement.css("resize");
this.originalElement.css("resize","none");
this._proportionallyResizeElements.push(this.originalElement.css({position:"static",zoom:1,display:"block"}));
this.originalElement.css({margin:this.originalElement.css("margin")});
this._proportionallyResize()
}this.handles=k.handles||(!c(".ui-resizable-handle",this.element).length?"e,s,se":{n:".ui-resizable-n",e:".ui-resizable-e",s:".ui-resizable-s",w:".ui-resizable-w",se:".ui-resizable-se",sw:".ui-resizable-sw",ne:".ui-resizable-ne",nw:".ui-resizable-nw"});
if(this.handles.constructor===String){if(this.handles==="all"){this.handles="n,e,s,w,se,sw,ne,nw"
}l=this.handles.split(",");
this.handles={};
for(f=0;
f<l.length;
f++){j=c.trim(l[f]);
e="ui-resizable-"+j;
g=c("<div class='ui-resizable-handle "+e+"'></div>");
g.css({zIndex:k.zIndex});
if("se"===j){g.addClass("ui-icon ui-icon-gripsmall-diagonal-se")
}this.handles[j]=".ui-resizable-"+j;
this.element.append(g)
}}this._renderAxis=function(q){var n,o,m,p;
q=q||this.element;
for(n in this.handles){if(this.handles[n].constructor===String){this.handles[n]=c(this.handles[n],this.element).show()
}if(this.elementIsWrapper&&this.originalElement[0].nodeName.match(/textarea|input|select|button/i)){o=c(this.handles[n],this.element);
p=/sw|ne|nw|se|n|s/.test(n)?o.outerHeight():o.outerWidth();
m=["padding",/ne|nw|n/.test(n)?"Top":/se|sw|s/.test(n)?"Bottom":/^e$/.test(n)?"Right":"Left"].join("");
q.css(m,p);
this._proportionallyResize()
}if(!c(this.handles[n]).length){continue
}}};
this._renderAxis(this.element);
this._handles=c(".ui-resizable-handle",this.element).disableSelection();
this._handles.mouseover(function(){if(!h.resizing){if(this.className){g=this.className.match(/ui-resizable-(se|sw|ne|nw|n|e|s|w)/i)
}h.axis=g&&g[1]?g[1]:"se"
}});
if(k.autoHide){this._handles.hide();
c(this.element).addClass("ui-resizable-autohide").mouseenter(function(){if(k.disabled){return
}c(this).removeClass("ui-resizable-autohide");
h._handles.show()
}).mouseleave(function(){if(k.disabled){return
}if(!h.resizing){c(this).addClass("ui-resizable-autohide");
h._handles.hide()
}})
}this._mouseInit()
},_destroy:function(){this._mouseDestroy();
var f,e=function(g){c(g).removeClass("ui-resizable ui-resizable-disabled ui-resizable-resizing").removeData("resizable").removeData("ui-resizable").unbind(".resizable").find(".ui-resizable-handle").remove()
};
if(this.elementIsWrapper){e(this.element);
f=this.element;
this.originalElement.css({position:f.css("position"),width:f.outerWidth(),height:f.outerHeight(),top:f.css("top"),left:f.css("left")}).insertAfter(f);
f.remove()
}this.originalElement.css("resize",this.originalResizeStyle);
e(this.originalElement);
return this
},_mouseCapture:function(g){var f,h,e=false;
for(f in this.handles){h=c(this.handles[f])[0];
if(h===g.target||c.contains(h,g.target)){e=true
}}return !this.options.disabled&&e
},_mouseStart:function(g){var k,h,j,i=this.options,f=this.element.position(),e=this.element;
this.resizing=true;
if((/absolute/).test(e.css("position"))){e.css({position:"absolute",top:e.css("top"),left:e.css("left")})
}else{if(e.is(".ui-draggable")){e.css({position:"absolute",top:f.top,left:f.left})
}}this._renderProxy();
k=b(this.helper.css("left"));
h=b(this.helper.css("top"));
if(i.containment){k+=c(i.containment).scrollLeft()||0;
h+=c(i.containment).scrollTop()||0
}this.offset=this.helper.offset();
this.position={left:k,top:h};
this.size=this._helper?{width:this.helper.width(),height:this.helper.height()}:{width:e.width(),height:e.height()};
this.originalSize=this._helper?{width:e.outerWidth(),height:e.outerHeight()}:{width:e.width(),height:e.height()};
this.originalPosition={left:k,top:h};
this.sizeDiff={width:e.outerWidth()-e.width(),height:e.outerHeight()-e.height()};
this.originalMousePosition={left:g.pageX,top:g.pageY};
this.aspectRatio=(typeof i.aspectRatio==="number")?i.aspectRatio:((this.originalSize.width/this.originalSize.height)||1);
j=c(".ui-resizable-"+this.axis).css("cursor");
c("body").css("cursor",j==="auto"?this.axis+"-resize":j);
e.addClass("ui-resizable-resizing");
this._propagate("start",g);
return true
},_mouseDrag:function(e){var k,g=this.helper,l={},i=this.originalMousePosition,m=this.axis,o=this.position.top,f=this.position.left,n=this.size.width,j=this.size.height,q=(e.pageX-i.left)||0,p=(e.pageY-i.top)||0,h=this._change[m];
if(!h){return false
}k=h.apply(this,[e,q,p]);
this._updateVirtualBoundaries(e.shiftKey);
if(this._aspectRatio||e.shiftKey){k=this._updateRatio(k,e)
}k=this._respectSize(k,e);
this._updateCache(k);
this._propagate("resize",e);
if(this.position.top!==o){l.top=this.position.top+"px"
}if(this.position.left!==f){l.left=this.position.left+"px"
}if(this.size.width!==n){l.width=this.size.width+"px"
}if(this.size.height!==j){l.height=this.size.height+"px"
}g.css(l);
if(!this._helper&&this._proportionallyResizeElements.length){this._proportionallyResize()
}if(!c.isEmptyObject(l)){this._trigger("resize",e,this.ui())
}return false
},_mouseStop:function(h){this.resizing=false;
var g,e,f,k,n,j,m,i=this.options,l=this;
if(this._helper){g=this._proportionallyResizeElements;
e=g.length&&(/textarea/i).test(g[0].nodeName);
f=e&&c.ui.hasScroll(g[0],"left")?0:l.sizeDiff.height;
k=e?0:l.sizeDiff.width;
n={width:(l.helper.width()-k),height:(l.helper.height()-f)};
j=(parseInt(l.element.css("left"),10)+(l.position.left-l.originalPosition.left))||null;
m=(parseInt(l.element.css("top"),10)+(l.position.top-l.originalPosition.top))||null;
if(!i.animate){this.element.css(c.extend(n,{top:m,left:j}))
}l.helper.height(l.size.height);
l.helper.width(l.size.width);
if(this._helper&&!i.animate){this._proportionallyResize()
}}c("body").css("cursor","auto");
this.element.removeClass("ui-resizable-resizing");
this._propagate("stop",h);
if(this._helper){this.helper.remove()
}return false
},_updateVirtualBoundaries:function(g){var i,h,f,k,e,j=this.options;
e={minWidth:a(j.minWidth)?j.minWidth:0,maxWidth:a(j.maxWidth)?j.maxWidth:Infinity,minHeight:a(j.minHeight)?j.minHeight:0,maxHeight:a(j.maxHeight)?j.maxHeight:Infinity};
if(this._aspectRatio||g){i=e.minHeight*this.aspectRatio;
f=e.minWidth/this.aspectRatio;
h=e.maxHeight*this.aspectRatio;
k=e.maxWidth/this.aspectRatio;
if(i>e.minWidth){e.minWidth=i
}if(f>e.minHeight){e.minHeight=f
}if(h<e.maxWidth){e.maxWidth=h
}if(k<e.maxHeight){e.maxHeight=k
}}this._vBoundaries=e
},_updateCache:function(e){this.offset=this.helper.offset();
if(a(e.left)){this.position.left=e.left
}if(a(e.top)){this.position.top=e.top
}if(a(e.height)){this.size.height=e.height
}if(a(e.width)){this.size.width=e.width
}},_updateRatio:function(g){var h=this.position,f=this.size,e=this.axis;
if(a(g.height)){g.width=(g.height*this.aspectRatio)
}else{if(a(g.width)){g.height=(g.width/this.aspectRatio)
}}if(e==="sw"){g.left=h.left+(f.width-g.width);
g.top=null
}if(e==="nw"){g.top=h.top+(f.height-g.height);
g.left=h.left+(f.width-g.width)
}return g
},_respectSize:function(j){var g=this._vBoundaries,m=this.axis,p=a(j.width)&&g.maxWidth&&(g.maxWidth<j.width),k=a(j.height)&&g.maxHeight&&(g.maxHeight<j.height),h=a(j.width)&&g.minWidth&&(g.minWidth>j.width),n=a(j.height)&&g.minHeight&&(g.minHeight>j.height),f=this.originalPosition.left+this.originalSize.width,l=this.position.top+this.size.height,i=/sw|nw|w/.test(m),e=/nw|ne|n/.test(m);
if(h){j.width=g.minWidth
}if(n){j.height=g.minHeight
}if(p){j.width=g.maxWidth
}if(k){j.height=g.maxHeight
}if(h&&i){j.left=f-g.minWidth
}if(p&&i){j.left=f-g.maxWidth
}if(n&&e){j.top=l-g.minHeight
}if(k&&e){j.top=l-g.maxHeight
}if(!j.width&&!j.height&&!j.left&&j.top){j.top=null
}else{if(!j.width&&!j.height&&!j.top&&j.left){j.left=null
}}return j
},_proportionallyResize:function(){if(!this._proportionallyResizeElements.length){return
}var h,f,l,e,k,g=this.helper||this.element;
for(h=0;
h<this._proportionallyResizeElements.length;
h++){k=this._proportionallyResizeElements[h];
if(!this.borderDif){this.borderDif=[];
l=[k.css("borderTopWidth"),k.css("borderRightWidth"),k.css("borderBottomWidth"),k.css("borderLeftWidth")];
e=[k.css("paddingTop"),k.css("paddingRight"),k.css("paddingBottom"),k.css("paddingLeft")];
for(f=0;
f<l.length;
f++){this.borderDif[f]=(parseInt(l[f],10)||0)+(parseInt(e[f],10)||0)
}}k.css({height:(g.height()-this.borderDif[0]-this.borderDif[2])||0,width:(g.width()-this.borderDif[1]-this.borderDif[3])||0})
}},_renderProxy:function(){var e=this.element,f=this.options;
this.elementOffset=e.offset();
if(this._helper){this.helper=this.helper||c("<div style='overflow:hidden;'></div>");
this.helper.addClass(this._helper).css({width:this.element.outerWidth()-1,height:this.element.outerHeight()-1,position:"absolute",left:this.elementOffset.left+"px",top:this.elementOffset.top+"px",zIndex:++f.zIndex});
this.helper.appendTo("body").disableSelection()
}else{this.helper=this.element
}},_change:{e:function(f,e){return{width:this.originalSize.width+e}
},w:function(g,e){var f=this.originalSize,h=this.originalPosition;
return{left:h.left+e,width:f.width-e}
},n:function(h,f,e){var g=this.originalSize,i=this.originalPosition;
return{top:i.top+e,height:g.height-e}
},s:function(g,f,e){return{height:this.originalSize.height+e}
},se:function(g,f,e){return c.extend(this._change.s.apply(this,arguments),this._change.e.apply(this,[g,f,e]))
},sw:function(g,f,e){return c.extend(this._change.s.apply(this,arguments),this._change.w.apply(this,[g,f,e]))
},ne:function(g,f,e){return c.extend(this._change.n.apply(this,arguments),this._change.e.apply(this,[g,f,e]))
},nw:function(g,f,e){return c.extend(this._change.n.apply(this,arguments),this._change.w.apply(this,[g,f,e]))
}},_propagate:function(f,e){c.ui.plugin.call(this,f,[e,this.ui()]);
(f!=="resize"&&this._trigger(f,e,this.ui()))
},plugins:{},ui:function(){return{originalElement:this.originalElement,element:this.element,helper:this.helper,position:this.position,size:this.size,originalSize:this.originalSize,originalPosition:this.originalPosition}
}});
c.ui.plugin.add("resizable","animate",{stop:function(h){var m=c(this).data("ui-resizable"),j=m.options,g=m._proportionallyResizeElements,e=g.length&&(/textarea/i).test(g[0].nodeName),f=e&&c.ui.hasScroll(g[0],"left")?0:m.sizeDiff.height,l=e?0:m.sizeDiff.width,i={width:(m.size.width-l),height:(m.size.height-f)},k=(parseInt(m.element.css("left"),10)+(m.position.left-m.originalPosition.left))||null,n=(parseInt(m.element.css("top"),10)+(m.position.top-m.originalPosition.top))||null;
m.element.animate(c.extend(i,n&&k?{top:n,left:k}:{}),{duration:j.animateDuration,easing:j.animateEasing,step:function(){var o={width:parseInt(m.element.css("width"),10),height:parseInt(m.element.css("height"),10),top:parseInt(m.element.css("top"),10),left:parseInt(m.element.css("left"),10)};
if(g&&g.length){c(g[0]).css({width:o.width,height:o.height})
}m._updateCache(o);
m._propagate("resize",h)
}})
}});
c.ui.plugin.add("resizable","containment",{start:function(){var m,g,q,e,l,h,r,n=c(this).data("ui-resizable"),k=n.options,j=n.element,f=k.containment,i=(f instanceof c)?f.get(0):(/parent/.test(f))?j.parent().get(0):f;
if(!i){return
}n.containerElement=c(i);
if(/document/.test(f)||f===document){n.containerOffset={left:0,top:0};
n.containerPosition={left:0,top:0};
n.parentData={element:c(document),left:0,top:0,width:c(document).width(),height:c(document).height()||document.body.parentNode.scrollHeight}
}else{m=c(i);
g=[];
c(["Top","Right","Left","Bottom"]).each(function(p,o){g[p]=b(m.css("padding"+o))
});
n.containerOffset=m.offset();
n.containerPosition=m.position();
n.containerSize={height:(m.innerHeight()-g[3]),width:(m.innerWidth()-g[1])};
q=n.containerOffset;
e=n.containerSize.height;
l=n.containerSize.width;
h=(c.ui.hasScroll(i,"left")?i.scrollWidth:l);
r=(c.ui.hasScroll(i)?i.scrollHeight:e);
n.parentData={element:i,left:q.left,top:q.top,width:h,height:r}
}},resize:function(f){var k,q,j,i,l=c(this).data("ui-resizable"),h=l.options,n=l.containerOffset,m=l.position,p=l._aspectRatio||f.shiftKey,e={top:0,left:0},g=l.containerElement;
if(g[0]!==document&&(/static/).test(g.css("position"))){e=n
}if(m.left<(l._helper?n.left:0)){l.size.width=l.size.width+(l._helper?(l.position.left-n.left):(l.position.left-e.left));
if(p){l.size.height=l.size.width/l.aspectRatio
}l.position.left=h.helper?n.left:0
}if(m.top<(l._helper?n.top:0)){l.size.height=l.size.height+(l._helper?(l.position.top-n.top):l.position.top);
if(p){l.size.width=l.size.height*l.aspectRatio
}l.position.top=l._helper?n.top:0
}l.offset.left=l.parentData.left+l.position.left;
l.offset.top=l.parentData.top+l.position.top;
k=Math.abs((l._helper?l.offset.left-e.left:(l.offset.left-e.left))+l.sizeDiff.width);
q=Math.abs((l._helper?l.offset.top-e.top:(l.offset.top-n.top))+l.sizeDiff.height);
j=l.containerElement.get(0)===l.element.parent().get(0);
i=/relative|absolute/.test(l.containerElement.css("position"));
if(j&&i){k-=Math.abs(l.parentData.left)
}if(k+l.size.width>=l.parentData.width){l.size.width=l.parentData.width-k;
if(p){l.size.height=l.size.width/l.aspectRatio
}}if(q+l.size.height>=l.parentData.height){l.size.height=l.parentData.height-q;
if(p){l.size.width=l.size.height*l.aspectRatio
}}},stop:function(){var k=c(this).data("ui-resizable"),f=k.options,l=k.containerOffset,e=k.containerPosition,g=k.containerElement,i=c(k.helper),n=i.offset(),m=i.outerWidth()-k.sizeDiff.width,j=i.outerHeight()-k.sizeDiff.height;
if(k._helper&&!f.animate&&(/relative/).test(g.css("position"))){c(this).css({left:n.left-e.left-l.left,width:m,height:j})
}if(k._helper&&!f.animate&&(/static/).test(g.css("position"))){c(this).css({left:n.left-e.left-l.left,width:m,height:j})
}}});
c.ui.plugin.add("resizable","alsoResize",{start:function(){var e=c(this).data("ui-resizable"),g=e.options,f=function(h){c(h).each(function(){var i=c(this);
i.data("ui-resizable-alsoresize",{width:parseInt(i.width(),10),height:parseInt(i.height(),10),left:parseInt(i.css("left"),10),top:parseInt(i.css("top"),10)})
})
};
if(typeof(g.alsoResize)==="object"&&!g.alsoResize.parentNode){if(g.alsoResize.length){g.alsoResize=g.alsoResize[0];
f(g.alsoResize)
}else{c.each(g.alsoResize,function(h){f(h)
})
}}else{f(g.alsoResize)
}},resize:function(g,i){var f=c(this).data("ui-resizable"),j=f.options,h=f.originalSize,l=f.originalPosition,k={height:(f.size.height-h.height)||0,width:(f.size.width-h.width)||0,top:(f.position.top-l.top)||0,left:(f.position.left-l.left)||0},e=function(m,n){c(m).each(function(){var q=c(this),r=c(this).data("ui-resizable-alsoresize"),p={},o=n&&n.length?n:q.parents(i.originalElement[0]).length?["width","height"]:["width","height","top","left"];
c.each(o,function(s,u){var t=(r[u]||0)+(k[u]||0);
if(t&&t>=0){p[u]=t||null
}});
q.css(p)
})
};
if(typeof(j.alsoResize)==="object"&&!j.alsoResize.nodeType){c.each(j.alsoResize,function(m,n){e(m,n)
})
}else{e(j.alsoResize)
}},stop:function(){c(this).removeData("resizable-alsoresize")
}});
c.ui.plugin.add("resizable","ghost",{start:function(){var f=c(this).data("ui-resizable"),g=f.options,e=f.size;
f.ghost=f.originalElement.clone();
f.ghost.css({opacity:0.25,display:"block",position:"relative",height:e.height,width:e.width,margin:0,left:0,top:0}).addClass("ui-resizable-ghost").addClass(typeof g.ghost==="string"?g.ghost:"");
f.ghost.appendTo(f.helper)
},resize:function(){var e=c(this).data("ui-resizable");
if(e.ghost){e.ghost.css({position:"relative",height:e.size.height,width:e.size.width})
}},stop:function(){var e=c(this).data("ui-resizable");
if(e.ghost&&e.helper){e.helper.get(0).removeChild(e.ghost.get(0))
}}});
c.ui.plugin.add("resizable","grid",{resize:function(){var r=c(this).data("ui-resizable"),i=r.options,s=r.size,k=r.originalSize,n=r.originalPosition,t=r.axis,f=typeof i.grid==="number"?[i.grid,i.grid]:i.grid,p=(f[0]||1),m=(f[1]||1),h=Math.round((s.width-k.width)/p)*p,g=Math.round((s.height-k.height)/m)*m,l=k.width+h,e=k.height+g,j=i.maxWidth&&(i.maxWidth<l),u=i.maxHeight&&(i.maxHeight<e),q=i.minWidth&&(i.minWidth>l),v=i.minHeight&&(i.minHeight>e);
i.grid=f;
if(q){l=l+p
}if(v){e=e+m
}if(j){l=l-p
}if(u){e=e-m
}if(/^(se|s|e)$/.test(t)){r.size.width=l;
r.size.height=e
}else{if(/^(ne)$/.test(t)){r.size.width=l;
r.size.height=e;
r.position.top=n.top-g
}else{if(/^(sw)$/.test(t)){r.size.width=l;
r.size.height=e;
r.position.left=n.left-h
}else{if(e-m>0){r.size.height=e;
r.position.top=n.top-g
}else{r.size.height=m;
r.position.top=n.top+k.height-m
}if(l-p>0){r.size.width=l;
r.position.left=n.left-h
}else{r.size.width=p;
r.position.left=n.left+k.width-p
}}}}}})
})(jQuery);
(function(b,d){function a(f,e,g){return(f>e)&&(f<(e+g))
}function c(e){return(/left|right/).test(e.css("float"))||(/inline|table-cell/).test(e.css("display"))
}b.widget("ui.sortable",b.ui.mouse,{version:"1.10.4",widgetEventPrefix:"sort",ready:false,options:{appendTo:"parent",axis:false,connectWith:false,containment:false,cursor:"auto",cursorAt:false,dropOnEmpty:true,forcePlaceholderSize:false,forceHelperSize:false,grid:false,handle:false,helper:"original",items:"> *",opacity:false,placeholder:false,revert:false,scroll:true,scrollSensitivity:20,scrollSpeed:20,scope:"default",tolerance:"intersect",zIndex:1000,activate:null,beforeStop:null,change:null,deactivate:null,out:null,over:null,receive:null,remove:null,sort:null,start:null,stop:null,update:null},_create:function(){var e=this.options;
this.containerCache={};
this.element.addClass("ui-sortable");
this.refresh();
this.floating=this.items.length?e.axis==="x"||c(this.items[0].item):false;
this.offset=this.element.offset();
this._mouseInit();
this.ready=true
},_destroy:function(){this.element.removeClass("ui-sortable ui-sortable-disabled");
this._mouseDestroy();
for(var e=this.items.length-1;
e>=0;
e--){this.items[e].item.removeData(this.widgetName+"-item")
}return this
},_setOption:function(e,f){if(e==="disabled"){this.options[e]=f;
this.widget().toggleClass("ui-sortable-disabled",!!f)
}else{b.Widget.prototype._setOption.apply(this,arguments)
}},_mouseCapture:function(g,h){var e=null,i=false,f=this;
if(this.reverting){return false
}if(this.options.disabled||this.options.type==="static"){return false
}this._refreshItems(g);
b(g.target).parents().each(function(){if(b.data(this,f.widgetName+"-item")===f){e=b(this);
return false
}});
if(b.data(g.target,f.widgetName+"-item")===f){e=b(g.target)
}if(!e){return false
}if(this.options.handle&&!h){b(this.options.handle,e).find("*").addBack().each(function(){if(this===g.target){i=true
}});
if(!i){return false
}}this.currentItem=e;
this._removeCurrentsFromItems();
return true
},_mouseStart:function(h,j,f){var g,e,k=this.options;
this.currentContainer=this;
this.refreshPositions();
this.helper=this._createHelper(h);
this._cacheHelperProportions();
this._cacheMargins();
this.scrollParent=this.helper.scrollParent();
this.offset=this.currentItem.offset();
this.offset={top:this.offset.top-this.margins.top,left:this.offset.left-this.margins.left};
b.extend(this.offset,{click:{left:h.pageX-this.offset.left,top:h.pageY-this.offset.top},parent:this._getParentOffset(),relative:this._getRelativeOffset()});
this.helper.css("position","absolute");
this.cssPosition=this.helper.css("position");
this.originalPosition=this._generatePosition(h);
this.originalPageX=h.pageX;
this.originalPageY=h.pageY;
(k.cursorAt&&this._adjustOffsetFromHelper(k.cursorAt));
this.domPosition={prev:this.currentItem.prev()[0],parent:this.currentItem.parent()[0]};
if(this.helper[0]!==this.currentItem[0]){this.currentItem.hide()
}this._createPlaceholder();
if(k.containment){this._setContainment()
}if(k.cursor&&k.cursor!=="auto"){e=this.document.find("body");
this.storedCursor=e.css("cursor");
e.css("cursor",k.cursor);
this.storedStylesheet=b("<style>*{ cursor: "+k.cursor+" !important; }</style>").appendTo(e)
}if(k.opacity){if(this.helper.css("opacity")){this._storedOpacity=this.helper.css("opacity")
}this.helper.css("opacity",k.opacity)
}if(k.zIndex){if(this.helper.css("zIndex")){this._storedZIndex=this.helper.css("zIndex")
}this.helper.css("zIndex",k.zIndex)
}if(this.scrollParent[0]!==document&&this.scrollParent[0].tagName!=="HTML"){this.overflowOffset=this.scrollParent.offset()
}this._trigger("start",h,this._uiHash());
if(!this._preserveHelperProportions){this._cacheHelperProportions()
}if(!f){for(g=this.containers.length-1;
g>=0;
g--){this.containers[g]._trigger("activate",h,this._uiHash(this))
}}if(b.ui.ddmanager){b.ui.ddmanager.current=this
}if(b.ui.ddmanager&&!k.dropBehaviour){b.ui.ddmanager.prepareOffsets(this,h)
}this.dragging=true;
this.helper.addClass("ui-sortable-helper");
this._mouseDrag(h);
return true
},_mouseDrag:function(j){var g,h,f,l,k=this.options,e=false;
this.position=this._generatePosition(j);
this.positionAbs=this._convertPositionTo("absolute");
if(!this.lastPositionAbs){this.lastPositionAbs=this.positionAbs
}if(this.options.scroll){if(this.scrollParent[0]!==document&&this.scrollParent[0].tagName!=="HTML"){if((this.overflowOffset.top+this.scrollParent[0].offsetHeight)-j.pageY<k.scrollSensitivity){this.scrollParent[0].scrollTop=e=this.scrollParent[0].scrollTop+k.scrollSpeed
}else{if(j.pageY-this.overflowOffset.top<k.scrollSensitivity){this.scrollParent[0].scrollTop=e=this.scrollParent[0].scrollTop-k.scrollSpeed
}}if((this.overflowOffset.left+this.scrollParent[0].offsetWidth)-j.pageX<k.scrollSensitivity){this.scrollParent[0].scrollLeft=e=this.scrollParent[0].scrollLeft+k.scrollSpeed
}else{if(j.pageX-this.overflowOffset.left<k.scrollSensitivity){this.scrollParent[0].scrollLeft=e=this.scrollParent[0].scrollLeft-k.scrollSpeed
}}}else{if(j.pageY-b(document).scrollTop()<k.scrollSensitivity){e=b(document).scrollTop(b(document).scrollTop()-k.scrollSpeed)
}else{if(b(window).height()-(j.pageY-b(document).scrollTop())<k.scrollSensitivity){e=b(document).scrollTop(b(document).scrollTop()+k.scrollSpeed)
}}if(j.pageX-b(document).scrollLeft()<k.scrollSensitivity){e=b(document).scrollLeft(b(document).scrollLeft()-k.scrollSpeed)
}else{if(b(window).width()-(j.pageX-b(document).scrollLeft())<k.scrollSensitivity){e=b(document).scrollLeft(b(document).scrollLeft()+k.scrollSpeed)
}}}if(e!==false&&b.ui.ddmanager&&!k.dropBehaviour){b.ui.ddmanager.prepareOffsets(this,j)
}}this.positionAbs=this._convertPositionTo("absolute");
if(!this.options.axis||this.options.axis!=="y"){this.helper[0].style.left=this.position.left+"px"
}if(!this.options.axis||this.options.axis!=="x"){this.helper[0].style.top=this.position.top+"px"
}for(g=this.items.length-1;
g>=0;
g--){h=this.items[g];
f=h.item[0];
l=this._intersectsWithPointer(h);
if(!l){continue
}if(h.instance!==this.currentContainer){continue
}if(f!==this.currentItem[0]&&this.placeholder[l===1?"next":"prev"]()[0]!==f&&!b.contains(this.placeholder[0],f)&&(this.options.type==="semi-dynamic"?!b.contains(this.element[0],f):true)){this.direction=l===1?"down":"up";
if(this.options.tolerance==="pointer"||this._intersectsWithSides(h)){this._rearrange(j,h)
}else{break
}this._trigger("change",j,this._uiHash());
break
}}this._contactContainers(j);
if(b.ui.ddmanager){b.ui.ddmanager.drag(this,j)
}this._trigger("sort",j,this._uiHash());
this.lastPositionAbs=this.positionAbs;
return false
},_mouseStop:function(g,i){if(!g){return
}if(b.ui.ddmanager&&!this.options.dropBehaviour){b.ui.ddmanager.drop(this,g)
}if(this.options.revert){var f=this,j=this.placeholder.offset(),e=this.options.axis,h={};
if(!e||e==="x"){h.left=j.left-this.offset.parent.left-this.margins.left+(this.offsetParent[0]===document.body?0:this.offsetParent[0].scrollLeft)
}if(!e||e==="y"){h.top=j.top-this.offset.parent.top-this.margins.top+(this.offsetParent[0]===document.body?0:this.offsetParent[0].scrollTop)
}this.reverting=true;
b(this.helper).animate(h,parseInt(this.options.revert,10)||500,function(){f._clear(g)
})
}else{this._clear(g,i)
}return false
},cancel:function(){if(this.dragging){this._mouseUp({target:null});
if(this.options.helper==="original"){this.currentItem.css(this._storedCSS).removeClass("ui-sortable-helper")
}else{this.currentItem.show()
}for(var e=this.containers.length-1;
e>=0;
e--){this.containers[e]._trigger("deactivate",null,this._uiHash(this));
if(this.containers[e].containerCache.over){this.containers[e]._trigger("out",null,this._uiHash(this));
this.containers[e].containerCache.over=0
}}}if(this.placeholder){if(this.placeholder[0].parentNode){this.placeholder[0].parentNode.removeChild(this.placeholder[0])
}if(this.options.helper!=="original"&&this.helper&&this.helper[0].parentNode){this.helper.remove()
}b.extend(this,{helper:null,dragging:false,reverting:false,_noFinalSort:null});
if(this.domPosition.prev){b(this.domPosition.prev).after(this.currentItem)
}else{b(this.domPosition.parent).prepend(this.currentItem)
}}return this
},serialize:function(g){var e=this._getItemsAsjQuery(g&&g.connected),f=[];
g=g||{};
b(e).each(function(){var h=(b(g.item||this).attr(g.attribute||"id")||"").match(g.expression||(/(.+)[\-=_](.+)/));
if(h){f.push((g.key||h[1]+"[]")+"="+(g.key&&g.expression?h[1]:h[2]))
}});
if(!f.length&&g.key){f.push(g.key+"=")
}return f.join("&")
},toArray:function(g){var e=this._getItemsAsjQuery(g&&g.connected),f=[];
g=g||{};
e.each(function(){f.push(b(g.item||this).attr(g.attribute||"id")||"")
});
return f
},_intersectsWith:function(q){var g=this.positionAbs.left,f=g+this.helperProportions.width,o=this.positionAbs.top,n=o+this.helperProportions.height,h=q.left,e=h+q.width,s=q.top,m=s+q.height,u=this.offset.click.top,k=this.offset.click.left,j=(this.options.axis==="x")||((o+u)>s&&(o+u)<m),p=(this.options.axis==="y")||((g+k)>h&&(g+k)<e),i=j&&p;
if(this.options.tolerance==="pointer"||this.options.forcePointerForContainers||(this.options.tolerance!=="pointer"&&this.helperProportions[this.floating?"width":"height"]>q[this.floating?"width":"height"])){return i
}else{return(h<g+(this.helperProportions.width/2)&&f-(this.helperProportions.width/2)<e&&s<o+(this.helperProportions.height/2)&&n-(this.helperProportions.height/2)<m)
}},_intersectsWithPointer:function(g){var h=(this.options.axis==="x")||a(this.positionAbs.top+this.offset.click.top,g.top,g.height),f=(this.options.axis==="y")||a(this.positionAbs.left+this.offset.click.left,g.left,g.width),j=h&&f,e=this._getDragVerticalDirection(),i=this._getDragHorizontalDirection();
if(!j){return false
}return this.floating?(((i&&i==="right")||e==="down")?2:1):(e&&(e==="down"?2:1))
},_intersectsWithSides:function(h){var f=a(this.positionAbs.top+this.offset.click.top,h.top+(h.height/2),h.height),g=a(this.positionAbs.left+this.offset.click.left,h.left+(h.width/2),h.width),e=this._getDragVerticalDirection(),i=this._getDragHorizontalDirection();
if(this.floating&&i){return((i==="right"&&g)||(i==="left"&&!g))
}else{return e&&((e==="down"&&f)||(e==="up"&&!f))
}},_getDragVerticalDirection:function(){var e=this.positionAbs.top-this.lastPositionAbs.top;
return e!==0&&(e>0?"down":"up")
},_getDragHorizontalDirection:function(){var e=this.positionAbs.left-this.lastPositionAbs.left;
return e!==0&&(e>0?"right":"left")
},refresh:function(e){this._refreshItems(e);
this.refreshPositions();
return this
},_connectWith:function(){var e=this.options;
return e.connectWith.constructor===String?[e.connectWith]:e.connectWith
},_getItemsAsjQuery:function(e){var g,f,n,k,l=[],h=[],m=this._connectWith();
if(m&&e){for(g=m.length-1;
g>=0;
g--){n=b(m[g]);
for(f=n.length-1;
f>=0;
f--){k=b.data(n[f],this.widgetFullName);
if(k&&k!==this&&!k.options.disabled){h.push([b.isFunction(k.options.items)?k.options.items.call(k.element):b(k.options.items,k.element).not(".ui-sortable-helper").not(".ui-sortable-placeholder"),k])
}}}}h.push([b.isFunction(this.options.items)?this.options.items.call(this.element,null,{options:this.options,item:this.currentItem}):b(this.options.items,this.element).not(".ui-sortable-helper").not(".ui-sortable-placeholder"),this]);
function o(){l.push(this)
}for(g=h.length-1;
g>=0;
g--){h[g][0].each(o)
}return b(l)
},_removeCurrentsFromItems:function(){var e=this.currentItem.find(":data("+this.widgetName+"-item)");
this.items=b.grep(this.items,function(g){for(var f=0;
f<e.length;
f++){if(e[f]===g.item[0]){return false
}}return true
})
},_refreshItems:function(e){this.items=[];
this.containers=[this];
var k,g,p,l,o,f,r,q,m=this.items,h=[[b.isFunction(this.options.items)?this.options.items.call(this.element[0],e,{item:this.currentItem}):b(this.options.items,this.element),this]],n=this._connectWith();
if(n&&this.ready){for(k=n.length-1;
k>=0;
k--){p=b(n[k]);
for(g=p.length-1;
g>=0;
g--){l=b.data(p[g],this.widgetFullName);
if(l&&l!==this&&!l.options.disabled){h.push([b.isFunction(l.options.items)?l.options.items.call(l.element[0],e,{item:this.currentItem}):b(l.options.items,l.element),l]);
this.containers.push(l)
}}}}for(k=h.length-1;
k>=0;
k--){o=h[k][1];
f=h[k][0];
for(g=0,q=f.length;
g<q;
g++){r=b(f[g]);
r.data(this.widgetName+"-item",o);
m.push({item:r,instance:o,width:0,height:0,left:0,top:0})
}}},refreshPositions:function(e){if(this.offsetParent&&this.helper){this.offset.parent=this._getParentOffset()
}var g,h,f,j;
for(g=this.items.length-1;
g>=0;
g--){h=this.items[g];
if(h.instance!==this.currentContainer&&this.currentContainer&&h.item[0]!==this.currentItem[0]){continue
}f=this.options.toleranceElement?b(this.options.toleranceElement,h.item):h.item;
if(!e){h.width=f.outerWidth();
h.height=f.outerHeight()
}j=f.offset();
h.left=j.left;
h.top=j.top
}if(this.options.custom&&this.options.custom.refreshContainers){this.options.custom.refreshContainers.call(this)
}else{for(g=this.containers.length-1;
g>=0;
g--){j=this.containers[g].element.offset();
this.containers[g].containerCache.left=j.left;
this.containers[g].containerCache.top=j.top;
this.containers[g].containerCache.width=this.containers[g].element.outerWidth();
this.containers[g].containerCache.height=this.containers[g].element.outerHeight()
}}return this
},_createPlaceholder:function(f){f=f||this;
var e,g=f.options;
if(!g.placeholder||g.placeholder.constructor===String){e=g.placeholder;
g.placeholder={element:function(){var i=f.currentItem[0].nodeName.toLowerCase(),h=b("<"+i+">",f.document[0]).addClass(e||f.currentItem[0].className+" ui-sortable-placeholder").removeClass("ui-sortable-helper");
if(i==="tr"){f.currentItem.children().each(function(){b("<td>&#160;</td>",f.document[0]).attr("colspan",b(this).attr("colspan")||1).appendTo(h)
})
}else{if(i==="img"){h.attr("src",f.currentItem.attr("src"))
}}if(!e){h.css("visibility","hidden")
}return h
},update:function(h,i){if(e&&!g.forcePlaceholderSize){return
}if(!i.height()){i.height(f.currentItem.innerHeight()-parseInt(f.currentItem.css("paddingTop")||0,10)-parseInt(f.currentItem.css("paddingBottom")||0,10))
}if(!i.width()){i.width(f.currentItem.innerWidth()-parseInt(f.currentItem.css("paddingLeft")||0,10)-parseInt(f.currentItem.css("paddingRight")||0,10))
}}}
}f.placeholder=b(g.placeholder.element.call(f.element,f.currentItem));
f.currentItem.after(f.placeholder);
g.placeholder.update(f,f.placeholder)
},_contactContainers:function(e){var l,h,p,m,n,r,f,s,k,o,g=null,q=null;
for(l=this.containers.length-1;
l>=0;
l--){if(b.contains(this.currentItem[0],this.containers[l].element[0])){continue
}if(this._intersectsWith(this.containers[l].containerCache)){if(g&&b.contains(this.containers[l].element[0],g.element[0])){continue
}g=this.containers[l];
q=l
}else{if(this.containers[l].containerCache.over){this.containers[l]._trigger("out",e,this._uiHash(this));
this.containers[l].containerCache.over=0
}}}if(!g){return
}if(this.containers.length===1){if(!this.containers[q].containerCache.over){this.containers[q]._trigger("over",e,this._uiHash(this));
this.containers[q].containerCache.over=1
}}else{p=10000;
m=null;
o=g.floating||c(this.currentItem);
n=o?"left":"top";
r=o?"width":"height";
f=this.positionAbs[n]+this.offset.click[n];
for(h=this.items.length-1;
h>=0;
h--){if(!b.contains(this.containers[q].element[0],this.items[h].item[0])){continue
}if(this.items[h].item[0]===this.currentItem[0]){continue
}if(o&&!a(this.positionAbs.top+this.offset.click.top,this.items[h].top,this.items[h].height)){continue
}s=this.items[h].item.offset()[n];
k=false;
if(Math.abs(s-f)>Math.abs(s+this.items[h][r]-f)){k=true;
s+=this.items[h][r]
}if(Math.abs(s-f)<p){p=Math.abs(s-f);
m=this.items[h];
this.direction=k?"up":"down"
}}if(!m&&!this.options.dropOnEmpty){return
}if(this.currentContainer===this.containers[q]){return
}m?this._rearrange(e,m,null,true):this._rearrange(e,null,this.containers[q].element,true);
this._trigger("change",e,this._uiHash());
this.containers[q]._trigger("change",e,this._uiHash(this));
this.currentContainer=this.containers[q];
this.options.placeholder.update(this.currentContainer,this.placeholder);
this.containers[q]._trigger("over",e,this._uiHash(this));
this.containers[q].containerCache.over=1
}},_createHelper:function(f){var g=this.options,e=b.isFunction(g.helper)?b(g.helper.apply(this.element[0],[f,this.currentItem])):(g.helper==="clone"?this.currentItem.clone():this.currentItem);
if(!e.parents("body").length){b(g.appendTo!=="parent"?g.appendTo:this.currentItem[0].parentNode)[0].appendChild(e[0])
}if(e[0]===this.currentItem[0]){this._storedCSS={width:this.currentItem[0].style.width,height:this.currentItem[0].style.height,position:this.currentItem.css("position"),top:this.currentItem.css("top"),left:this.currentItem.css("left")}
}if(!e[0].style.width||g.forceHelperSize){e.width(this.currentItem.width())
}if(!e[0].style.height||g.forceHelperSize){e.height(this.currentItem.height())
}return e
},_adjustOffsetFromHelper:function(e){if(typeof e==="string"){e=e.split(" ")
}if(b.isArray(e)){e={left:+e[0],top:+e[1]||0}
}if("left" in e){this.offset.click.left=e.left+this.margins.left
}if("right" in e){this.offset.click.left=this.helperProportions.width-e.right+this.margins.left
}if("top" in e){this.offset.click.top=e.top+this.margins.top
}if("bottom" in e){this.offset.click.top=this.helperProportions.height-e.bottom+this.margins.top
}},_getParentOffset:function(){this.offsetParent=this.helper.offsetParent();
var e=this.offsetParent.offset();
if(this.cssPosition==="absolute"&&this.scrollParent[0]!==document&&b.contains(this.scrollParent[0],this.offsetParent[0])){e.left+=this.scrollParent.scrollLeft();
e.top+=this.scrollParent.scrollTop()
}if(this.offsetParent[0]===document.body||(this.offsetParent[0].tagName&&this.offsetParent[0].tagName.toLowerCase()==="html"&&b.ui.ie)){e={top:0,left:0}
}return{top:e.top+(parseInt(this.offsetParent.css("borderTopWidth"),10)||0),left:e.left+(parseInt(this.offsetParent.css("borderLeftWidth"),10)||0)}
},_getRelativeOffset:function(){if(this.cssPosition==="relative"){var e=this.currentItem.position();
return{top:e.top-(parseInt(this.helper.css("top"),10)||0)+this.scrollParent.scrollTop(),left:e.left-(parseInt(this.helper.css("left"),10)||0)+this.scrollParent.scrollLeft()}
}else{return{top:0,left:0}
}},_cacheMargins:function(){this.margins={left:(parseInt(this.currentItem.css("marginLeft"),10)||0),top:(parseInt(this.currentItem.css("marginTop"),10)||0)}
},_cacheHelperProportions:function(){this.helperProportions={width:this.helper.outerWidth(),height:this.helper.outerHeight()}
},_setContainment:function(){var f,h,e,g=this.options;
if(g.containment==="parent"){g.containment=this.helper[0].parentNode
}if(g.containment==="document"||g.containment==="window"){this.containment=[0-this.offset.relative.left-this.offset.parent.left,0-this.offset.relative.top-this.offset.parent.top,b(g.containment==="document"?document:window).width()-this.helperProportions.width-this.margins.left,(b(g.containment==="document"?document:window).height()||document.body.parentNode.scrollHeight)-this.helperProportions.height-this.margins.top]
}if(!(/^(document|window|parent)$/).test(g.containment)){f=b(g.containment)[0];
h=b(g.containment).offset();
e=(b(f).css("overflow")!=="hidden");
this.containment=[h.left+(parseInt(b(f).css("borderLeftWidth"),10)||0)+(parseInt(b(f).css("paddingLeft"),10)||0)-this.margins.left,h.top+(parseInt(b(f).css("borderTopWidth"),10)||0)+(parseInt(b(f).css("paddingTop"),10)||0)-this.margins.top,h.left+(e?Math.max(f.scrollWidth,f.offsetWidth):f.offsetWidth)-(parseInt(b(f).css("borderLeftWidth"),10)||0)-(parseInt(b(f).css("paddingRight"),10)||0)-this.helperProportions.width-this.margins.left,h.top+(e?Math.max(f.scrollHeight,f.offsetHeight):f.offsetHeight)-(parseInt(b(f).css("borderTopWidth"),10)||0)-(parseInt(b(f).css("paddingBottom"),10)||0)-this.helperProportions.height-this.margins.top]
}},_convertPositionTo:function(g,i){if(!i){i=this.position
}var f=g==="absolute"?1:-1,e=this.cssPosition==="absolute"&&!(this.scrollParent[0]!==document&&b.contains(this.scrollParent[0],this.offsetParent[0]))?this.offsetParent:this.scrollParent,h=(/(html|body)/i).test(e[0].tagName);
return{top:(i.top+this.offset.relative.top*f+this.offset.parent.top*f-((this.cssPosition==="fixed"?-this.scrollParent.scrollTop():(h?0:e.scrollTop()))*f)),left:(i.left+this.offset.relative.left*f+this.offset.parent.left*f-((this.cssPosition==="fixed"?-this.scrollParent.scrollLeft():h?0:e.scrollLeft())*f))}
},_generatePosition:function(h){var j,i,k=this.options,g=h.pageX,f=h.pageY,e=this.cssPosition==="absolute"&&!(this.scrollParent[0]!==document&&b.contains(this.scrollParent[0],this.offsetParent[0]))?this.offsetParent:this.scrollParent,l=(/(html|body)/i).test(e[0].tagName);
if(this.cssPosition==="relative"&&!(this.scrollParent[0]!==document&&this.scrollParent[0]!==this.offsetParent[0])){this.offset.relative=this._getRelativeOffset()
}if(this.originalPosition){if(this.containment){if(h.pageX-this.offset.click.left<this.containment[0]){g=this.containment[0]+this.offset.click.left
}if(h.pageY-this.offset.click.top<this.containment[1]){f=this.containment[1]+this.offset.click.top
}if(h.pageX-this.offset.click.left>this.containment[2]){g=this.containment[2]+this.offset.click.left
}if(h.pageY-this.offset.click.top>this.containment[3]){f=this.containment[3]+this.offset.click.top
}}if(k.grid){j=this.originalPageY+Math.round((f-this.originalPageY)/k.grid[1])*k.grid[1];
f=this.containment?((j-this.offset.click.top>=this.containment[1]&&j-this.offset.click.top<=this.containment[3])?j:((j-this.offset.click.top>=this.containment[1])?j-k.grid[1]:j+k.grid[1])):j;
i=this.originalPageX+Math.round((g-this.originalPageX)/k.grid[0])*k.grid[0];
g=this.containment?((i-this.offset.click.left>=this.containment[0]&&i-this.offset.click.left<=this.containment[2])?i:((i-this.offset.click.left>=this.containment[0])?i-k.grid[0]:i+k.grid[0])):i
}}return{top:(f-this.offset.click.top-this.offset.relative.top-this.offset.parent.top+((this.cssPosition==="fixed"?-this.scrollParent.scrollTop():(l?0:e.scrollTop())))),left:(g-this.offset.click.left-this.offset.relative.left-this.offset.parent.left+((this.cssPosition==="fixed"?-this.scrollParent.scrollLeft():l?0:e.scrollLeft())))}
},_rearrange:function(j,h,f,g){f?f[0].appendChild(this.placeholder[0]):h.item[0].parentNode.insertBefore(this.placeholder[0],(this.direction==="down"?h.item[0]:h.item[0].nextSibling));
this.counter=this.counter?++this.counter:1;
var e=this.counter;
this._delay(function(){if(e===this.counter){this.refreshPositions(!g)
}})
},_clear:function(f,h){this.reverting=false;
var e,j=[];
if(!this._noFinalSort&&this.currentItem.parent().length){this.placeholder.before(this.currentItem)
}this._noFinalSort=null;
if(this.helper[0]===this.currentItem[0]){for(e in this._storedCSS){if(this._storedCSS[e]==="auto"||this._storedCSS[e]==="static"){this._storedCSS[e]=""
}}this.currentItem.css(this._storedCSS).removeClass("ui-sortable-helper")
}else{this.currentItem.show()
}if(this.fromOutside&&!h){j.push(function(i){this._trigger("receive",i,this._uiHash(this.fromOutside))
})
}if((this.fromOutside||this.domPosition.prev!==this.currentItem.prev().not(".ui-sortable-helper")[0]||this.domPosition.parent!==this.currentItem.parent()[0])&&!h){j.push(function(i){this._trigger("update",i,this._uiHash())
})
}if(this!==this.currentContainer){if(!h){j.push(function(i){this._trigger("remove",i,this._uiHash())
});
j.push((function(i){return function(k){i._trigger("receive",k,this._uiHash(this))
}
}).call(this,this.currentContainer));
j.push((function(i){return function(k){i._trigger("update",k,this._uiHash(this))
}
}).call(this,this.currentContainer))
}}function g(l,i,k){return function(m){k._trigger(l,m,i._uiHash(i))
}
}for(e=this.containers.length-1;
e>=0;
e--){if(!h){j.push(g("deactivate",this,this.containers[e]))
}if(this.containers[e].containerCache.over){j.push(g("out",this,this.containers[e]));
this.containers[e].containerCache.over=0
}}if(this.storedCursor){this.document.find("body").css("cursor",this.storedCursor);
this.storedStylesheet.remove()
}if(this._storedOpacity){this.helper.css("opacity",this._storedOpacity)
}if(this._storedZIndex){this.helper.css("zIndex",this._storedZIndex==="auto"?"":this._storedZIndex)
}this.dragging=false;
if(this.cancelHelperRemoval){if(!h){this._trigger("beforeStop",f,this._uiHash());
for(e=0;
e<j.length;
e++){j[e].call(this,f)
}this._trigger("stop",f,this._uiHash())
}this.fromOutside=false;
return false
}if(!h){this._trigger("beforeStop",f,this._uiHash())
}this.placeholder[0].parentNode.removeChild(this.placeholder[0]);
if(this.helper[0]!==this.currentItem[0]){this.helper.remove()
}this.helper=null;
if(!h){for(e=0;
e<j.length;
e++){j[e].call(this,f)
}this._trigger("stop",f,this._uiHash())
}this.fromOutside=false;
return true
},_trigger:function(){if(b.Widget.prototype._trigger.apply(this,arguments)===false){this.cancel()
}},_uiHash:function(e){var f=e||this;
return{helper:f.helper,placeholder:f.placeholder||b([]),position:f.position,originalPosition:f.originalPosition,offset:f.positionAbs,item:f.currentItem,sender:e?e.element:null}
}})
})(jQuery);