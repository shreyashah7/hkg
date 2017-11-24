/*
 * ui-grid - v3.0.0-rc.20-dfb451a - 2015-03-09
 * Copyright (c) 2015 ; License: MIT 
 */
(function(){angular.module("ui.grid.i18n",[]);
angular.module("ui.grid",["ui.grid.i18n"])
})();
(function(){angular.module("ui.grid").constant("uiGridConstants",{LOG_DEBUG_MESSAGES:true,LOG_WARN_MESSAGES:true,LOG_ERROR_MESSAGES:true,CUSTOM_FILTERS:/CUSTOM_FILTERS/g,COL_FIELD:/COL_FIELD/g,MODEL_COL_FIELD:/MODEL_COL_FIELD/g,DISPLAY_CELL_TEMPLATE:/DISPLAY_CELL_TEMPLATE/g,TEMPLATE_REGEXP:/<.+>/,FUNC_REGEXP:/(\([^)]*\))?$/,DOT_REGEXP:/\./g,APOS_REGEXP:/'/g,BRACKET_REGEXP:/^(.*)((?:\s*\[\s*\d+\s*\]\s*)|(?:\s*\[\s*"(?:[^"\\]|\\.)*"\s*\]\s*)|(?:\s*\[\s*'(?:[^'\\]|\\.)*'\s*\]\s*))(.*)$/,COL_CLASS_PREFIX:"ui-grid-col",events:{GRID_SCROLL:"uiGridScroll",COLUMN_MENU_SHOWN:"uiGridColMenuShown",ITEM_DRAGGING:"uiGridItemDragStart",COLUMN_HEADER_CLICK:"uiGridColumnHeaderClick"},keymap:{TAB:9,STRG:17,CTRL:17,CTRLRIGHT:18,CTRLR:18,SHIFT:16,RETURN:13,ENTER:13,BACKSPACE:8,BCKSP:8,ALT:18,ALTR:17,ALTRIGHT:17,SPACE:32,WIN:91,MAC:91,FN:null,PG_UP:33,PG_DOWN:34,UP:38,DOWN:40,LEFT:37,RIGHT:39,ESC:27,DEL:46,F1:112,F2:113,F3:114,F4:115,F5:116,F6:117,F7:118,F8:119,F9:120,F10:121,F11:122,F12:123},ASC:"asc",DESC:"desc",filter:{STARTS_WITH:2,ENDS_WITH:4,EXACT:8,CONTAINS:16,GREATER_THAN:32,GREATER_THAN_OR_EQUAL:64,LESS_THAN:128,LESS_THAN_OR_EQUAL:256,NOT_EQUAL:512},aggregationTypes:{sum:2,count:4,avg:8,min:16,max:32},CURRENCY_SYMBOLS:["ƒ","$","£","$","¤","¥","៛","₩","₱","฿","₫"],scrollDirection:{UP:"up",DOWN:"down",LEFT:"left",RIGHT:"right",NONE:"none"},dataChange:{ALL:"all",EDIT:"edit",ROW:"row",COLUMN:"column",OPTIONS:"options"},scrollbars:{NEVER:0,ALWAYS:1}})
})();
angular.module("ui.grid").directive("uiGridCell",["$compile","$parse","gridUtil","uiGridConstants",function(c,d,e,b){var a={priority:0,scope:false,require:"?^uiGrid",compile:function(){return{pre:function(i,h,f,l){function k(){var m=i.col.compiledElementFn;
m(i,function(n,o){h.append(n)
})
}if(l&&i.col.compiledElementFn){k()
}else{if(l&&!i.col.compiledElementFn){i.col.getCompiledElementFn().then(function(m){m(i,function(n,o){h.append(n)
})
})
}else{var j=i.col.cellTemplate.replace(b.MODEL_COL_FIELD,"row.entity."+e.preEval(i.col.field)).replace(b.COL_FIELD,"grid.getCellValue(row, col)");
var g=c(j)(i);
h.append(g)
}}},post:function(p,k,o,l){var n=p.col.getColClass(false);
k.addClass(n);
var g;
var j=function(r){var s=k;
if(g){s.removeClass(g);
g=null
}if(angular.isFunction(p.col.cellClass)){g=p.col.cellClass(p.grid,p.row,p.col,p.rowRenderIndex,p.colRenderIndex)
}else{g=p.col.cellClass
}s.addClass(g)
};
if(p.col.cellClass){j()
}var h=p.grid.registerDataChangeCallback(j,[b.dataChange.COLUMN,b.dataChange.EDIT]);
var m=function(t,s){if(t!==s){if(g||p.col.cellClass){j()
}var r=p.col.getColClass(false);
if(r!==n){k.removeClass(n);
k.addClass(r);
n=r
}}};
var i=p.$watch("col",m);
var q=p.$watch("row",m);
var f=function(){h();
i();
q()
};
p.$on("$destroy",f);
k.on("$destroy",f)
}}
}};
return a
}]);
(function(){angular.module("ui.grid").service("uiGridColumnMenuService",["i18nService","uiGridConstants","gridUtil",function(c,b,d){var a={initialize:function(e,f){e.grid=f.grid;
f.columnMenuScope=e;
e.menuShown=false
},setColMenuItemWatch:function(e){var f=e.$watch("col.menuItems",function(h,g){if(typeof(h)!=="undefined"&&h&&angular.isArray(h)){h.forEach(function(i){if(typeof(i.context)==="undefined"||!i.context){i.context={}
}i.context.col=e.col
});
e.menuItems=e.defaultMenuItems.concat(h)
}else{e.menuItems=e.defaultMenuItems
}});
e.$on("$destroy",f)
},sortable:function(e){if(e.grid.options.enableSorting&&typeof(e.col)!=="undefined"&&e.col&&e.col.enableSorting){return true
}else{return false
}},isActiveSort:function(e,f){return(typeof(e.col)!=="undefined"&&typeof(e.col.sort)!=="undefined"&&typeof(e.col.sort.direction)!=="undefined"&&e.col.sort.direction===f)
},suppressRemoveSort:function(e){if(e.col&&e.col.suppressRemoveSort){return true
}else{return false
}},hideable:function(e){if(typeof(e.col)!=="undefined"&&e.col&&e.col.colDef&&e.col.colDef.enableHiding===false){return false
}else{return true
}},getDefaultMenuItems:function(e){return[{title:c.getSafeText("sort.ascending"),icon:"ui-grid-icon-sort-alt-up",action:function(f){f.stopPropagation();
e.sortColumn(f,b.ASC)
},shown:function(){return a.sortable(e)
},active:function(){return a.isActiveSort(e,b.ASC)
}},{title:c.getSafeText("sort.descending"),icon:"ui-grid-icon-sort-alt-down",action:function(f){f.stopPropagation();
e.sortColumn(f,b.DESC)
},shown:function(){return a.sortable(e)
},active:function(){return a.isActiveSort(e,b.DESC)
}},{title:c.getSafeText("sort.remove"),icon:"ui-grid-icon-cancel",action:function(f){f.stopPropagation();
e.unsortColumn()
},shown:function(){return a.sortable(e)&&typeof(e.col)!=="undefined"&&(typeof(e.col.sort)!=="undefined"&&typeof(e.col.sort.direction)!=="undefined")&&e.col.sort.direction!==null&&!a.suppressRemoveSort(e)
}},{title:c.getSafeText("column.hide"),icon:"ui-grid-icon-cancel",shown:function(){return a.hideable(e)
},action:function(f){f.stopPropagation();
e.hideColumn()
}}]
},getColumnElementPosition:function(f,g,h){var e={};
e.left=h[0].offsetLeft;
e.top=h[0].offsetTop;
e.parentLeft=h[0].offsetParent.offsetLeft;
e.offset=0;
if(g.grid.options.offsetLeft){e.offset=g.grid.options.offsetLeft
}e.height=d.elementHeight(h,true);
e.width=d.elementWidth(h,true);
return e
},repositionMenu:function(r,k,l,j,m){var g=j[0].querySelectorAll(".ui-grid-menu");
var n=k.renderContainer?k.renderContainer:"body";
var o=k.grid.renderContainers[n];
var f=d.closestElm(m,".ui-grid-render-container");
var p=f.getBoundingClientRect().left-r.grid.element[0].getBoundingClientRect().left;
var s=f.querySelectorAll(".ui-grid-viewport")[0].scrollLeft;
var e=k.lastMenuWidth?k.lastMenuWidth:(r.lastMenuWidth?r.lastMenuWidth:170);
var h=k.lastMenuPaddingRight?k.lastMenuPaddingRight:(r.lastMenuPaddingRight?r.lastMenuPaddingRight:10);
if(g.length!==0){var q=g[0].querySelectorAll(".ui-grid-menu-mid");
if(q.length!==0&&!angular.element(q).hasClass("ng-hide")){e=d.elementWidth(g,true);
r.lastMenuWidth=e;
k.lastMenuWidth=e;
h=parseInt(d.getStyles(angular.element(g)[0])["paddingRight"],10);
r.lastMenuPaddingRight=h;
k.lastMenuPaddingRight=h
}}var i=l.left+p-s+l.parentLeft+l.width-e+h;
if(i<l.offset){i=l.offset
}j.css("left",i+"px");
j.css("top",(l.top+l.height)+"px")
}};
return a
}]).directive("uiGridColumnMenu",["$timeout","gridUtil","uiGridConstants","uiGridColumnMenuService",function(c,e,b,a){var d={priority:0,scope:true,require:"?^uiGrid",templateUrl:"ui-grid/uiGridColumnMenu",replace:true,link:function(i,g,f,j){var h=this;
a.initialize(i,j);
i.defaultMenuItems=a.getDefaultMenuItems(i);
i.menuItems=i.defaultMenuItems;
a.setColMenuItemWatch(i);
i.showMenu=function(k,m,l){i.col=k;
var n=a.getColumnElementPosition(i,k,m);
if(i.menuShown){i.colElement=m;
i.colElementPosition=n;
i.hideThenShow=true;
i.$broadcast("hide-menu",{originalEvent:l})
}else{h.shown=i.menuShown=true;
a.repositionMenu(i,k,n,g,m);
i.colElement=m;
i.colElementPosition=n;
i.$broadcast("show-menu",{originalEvent:l})
}};
i.hideMenu=function(k){i.menuShown=false;
if(!k){i.$broadcast("hide-menu")
}};
i.$on("menu-hidden",function(){if(i.hideThenShow){delete i.hideThenShow;
a.repositionMenu(i,i.col,i.colElementPosition,g,i.colElement);
i.$broadcast("show-menu");
i.menuShown=true
}else{i.hideMenu(true)
}});
i.$on("menu-shown",function(){c(function(){a.repositionMenu(i,i.col,i.colElementPosition,g,i.colElement);
delete i.colElementPosition;
delete i.columnElement
},200)
});
i.sortColumn=function(l,k){l.stopPropagation();
i.grid.sortColumn(i.col,k,true).then(function(){i.grid.refresh();
i.hideMenu()
})
};
i.unsortColumn=function(){i.col.unsort();
i.grid.refresh();
i.hideMenu()
};
i.hideColumn=function(){i.col.colDef.visible=false;
i.grid.refresh();
i.hideMenu();
i.grid.api.core.notifyDataChange(b.dataChange.COLUMN);
i.grid.api.core.raise.columnVisibilityChanged(i.col)
}
},controller:["$scope",function(g){var f=this;
g.$watch("menuItems",function(i,h){f.menuItems=i
})
}]};
return d
}])
})();
(function(){angular.module("ui.grid").directive("uiGridFooterCell",["$timeout","gridUtil","uiGridConstants","$compile",function(d,f,a,c){var b={priority:0,scope:{col:"=",row:"=",renderIndex:"="},replace:true,require:"^uiGrid",compile:function e(h,i,g){return{pre:function(l,k,j,m){var n=c(l.col.footerCellTemplate)(l);
k.append(n)
},post:function(n,m,j,p){n.grid=p.grid;
m.addClass(n.col.getColClass(false));
var o;
var l=function(q){var r=m;
if(o){r.removeClass(o);
o=null
}if(angular.isFunction(n.col.footerCellClass)){o=n.col.footerCellClass(n.grid,n.row,n.col,n.rowRenderIndex,n.colRenderIndex)
}else{o=n.col.footerCellClass
}r.addClass(o)
};
if(n.col.footerCellClass){l()
}var k=n.grid.registerDataChangeCallback(l,[a.dataChange.COLUMN]);
n.$on("$destroy",k)
}}
}};
return b
}])
})();
(function(){angular.module("ui.grid").directive("uiGridFooter",["$templateCache","$compile","uiGridConstants","gridUtil","$timeout",function(b,d,a,f,c){var e="ui-grid/ui-grid-footer";
return{restrict:"EA",replace:true,require:["^uiGrid","^uiGridRenderContainer"],scope:true,compile:function(h,g){return{pre:function(m,l,i,o){var n=o[0];
var k=o[1];
m.grid=n.grid;
m.colContainer=k.colContainer;
k.footer=l;
var j=(m.grid.options.footerTemplate)?m.grid.options.footerTemplate:e;
f.getTemplate(j).then(function(r){var q=angular.element(r);
var p=d(q)(m);
l.append(p);
if(k){var s=l[0].getElementsByClassName("ui-grid-footer-viewport")[0];
if(s){k.footerViewport=s
}}})
},post:function(l,k,i,p){var o=p[0];
var j=p[1];
var m=o.grid;
f.disableAnimations(k);
j.footer=k;
var n=k[0].getElementsByClassName("ui-grid-footer-viewport")[0];
if(n){j.footerViewport=n
}}}
}}
}])
})();
(function(){angular.module("ui.grid").directive("uiGridGridFooter",["$templateCache","$compile","uiGridConstants","gridUtil","$timeout",function(b,d,a,f,c){var e="ui-grid/ui-grid-grid-footer";
return{restrict:"EA",replace:true,require:"^uiGrid",scope:true,compile:function(h,g){return{pre:function(l,k,i,m){l.grid=m.grid;
var j=(l.grid.options.gridFooterTemplate)?l.grid.options.gridFooterTemplate:e;
f.getTemplate(j).then(function(p){var o=angular.element(p);
var n=d(o)(l);
k.append(n)
})
},post:function(k,j,i,l){}}
}}
}])
})();
(function(){angular.module("ui.grid").directive("uiGridGroupPanel",["$compile","uiGridConstants","gridUtil",function(b,a,d){var c="ui-grid/ui-grid-group-panel";
return{restrict:"EA",replace:true,require:"?^uiGrid",scope:false,compile:function(f,e){return{pre:function(i,h,g,j){var k=i.grid.options.groupPanelTemplate||c;
d.getTemplate(k).then(function(n){var m=angular.element(n);
var l=b(m)(i);
h.append(l)
})
},post:function(i,h,g,j){h.bind("$destroy",function(){})
}}
}}
}])
})();
(function(){angular.module("ui.grid").directive("uiGridHeaderCell",["$compile","$timeout","$window","$document","gridUtil","uiGridConstants","ScrollEvent",function(f,b,a,c,h,g,i){var d=500;
var e={priority:0,scope:{col:"=",row:"=",renderIndex:"="},require:["?^uiGrid","^uiGridRenderContainer"],replace:true,compile:function(){return{pre:function(l,k,j){var m=f(l.col.headerCellTemplate)(l);
k.append(m)
},post:function(w,x,p,j){var v=j[0];
var A=j[1];
w.grid=v.grid;
w.renderContainer=v.grid.renderContainers[A.containerId];
var z=w.col.getColClass(false);
x.addClass(z);
w.menuShown=false;
w.asc=g.ASC;
w.desc=g.DESC;
var m=angular.element(x[0].querySelectorAll(".ui-grid-header-cell-menu"));
var n=angular.element(x[0].querySelectorAll(".ui-grid-cell-contents"));
var y;
var s=function(C){var D=x;
if(y){D.removeClass(y);
y=null
}if(angular.isFunction(w.col.headerCellClass)){y=w.col.headerCellClass(w.grid,w.row,w.col,w.rowRenderIndex,w.colRenderIndex)
}else{y=w.col.headerCellClass
}D.addClass(y);
var E=w.grid.renderContainers.right?w.grid.renderContainers.right:w.grid.renderContainers.body;
w.isLastCol=(w.col===E.visibleColumnCache[E.visibleColumnCache.length-1])
};
w.$watch("col",function(E,D){if(E!==D){var C=w.col.getColClass(false);
if(C!==z){x.removeClass(z);
x.addClass(C);
z=C
}}});
s();
var t=w.grid.registerDataChangeCallback(s,[g.dataChange.COLUMN]);
w.$on("$destroy",t);
if(v.grid.options.enableSorting&&w.col.enableSorting){w.sortable=true
}else{w.sortable=false
}if(v.grid.options.enableFiltering&&w.col.enableFiltering){w.filterable=true
}else{w.filterable=false
}if(w.col.grid.options&&w.col.grid.options.enableColumnMenus!==false&&w.col.colDef&&w.col.colDef.enableColumnMenu!==false){w.colMenu=true
}else{w.colMenu=false
}function r(C){var D=false;
if(C.shiftKey){D=true
}v.grid.sortColumn(w.col,D).then(function(){if(v.columnMenuScope){v.columnMenuScope.hideMenu()
}v.grid.refresh()
})
}if(w.sortable||w.colMenu){var k;
var B=0;
var q=h.isTouchEnabled()?"touchstart":"mousedown";
n.on(q,function(C){C.stopPropagation();
if(typeof(C.originalEvent)!=="undefined"&&C.originalEvent!==undefined){C=C.originalEvent
}if(C.button&&C.button!==0){return
}B=(new Date()).getTime();
k=b(function(){},d);
k.then(function(){if(w.colMenu){v.columnMenuScope.showMenu(w.col,x,C)
}});
v.fireEvent(g.events.COLUMN_HEADER_CLICK,{event:C,columnName:w.col.colDef.name})
});
var o=h.isTouchEnabled()?"touchend":"mouseup";
n.on(o,function(){b.cancel(k)
});
w.$on("$destroy",function(){n.off("mousedown touchstart")
})
}w.toggleMenu=function(C){C.stopPropagation();
if(v.columnMenuScope.menuShown){if(v.columnMenuScope.col===w.col){v.columnMenuScope.hideMenu()
}else{v.columnMenuScope.showMenu(w.col,x)
}}else{v.columnMenuScope.showMenu(w.col,x)
}};
if(w.sortable){var l=h.isTouchEnabled()?"touchend":"click";
n.on(l,function(D){D.stopPropagation();
b.cancel(k);
var C=(new Date()).getTime();
var E=C-B;
if(E>d){}else{r(D)
}});
w.$on("$destroy",function(){b.cancel(k)
})
}if(w.filterable){var u=[];
angular.forEach(w.col.filters,function(D,C){u.push(w.$watch("col.filters["+C+"].term",function(F,E){if(F!==E){v.grid.api.core.raise.filterChanged();
v.grid.refresh(true)
}}))
});
w.$on("$destroy",function(){angular.forEach(u,function(C){C()
})
})
}}}
}};
return e
}])
})();
(function(){angular.module("ui.grid").directive("uiGridHeader",["$templateCache","$compile","uiGridConstants","gridUtil","$timeout",function(c,e,b,g,d){var f="ui-grid/ui-grid-header";
var a="ui-grid/ui-grid-no-header";
return{restrict:"EA",replace:true,require:["^uiGrid","^uiGridRenderContainer"],scope:true,compile:function(i,h){return{pre:function(m,l,j,p){var o=p[0];
var k=p[1];
m.grid=o.grid;
m.colContainer=k.colContainer;
n();
var q;
if(!m.grid.options.showHeader){q=a
}else{q=(m.grid.options.headerTemplate)?m.grid.options.headerTemplate:f
}g.getTemplate(q).then(function(u){var t=angular.element(u);
var r=e(t)(m);
l.replaceWith(r);
l=r;
n();
if(k){var s=l[0].getElementsByClassName("ui-grid-header-viewport")[0];
if(s){k.headerViewport=s
}}m.grid.queueRefresh()
});
function n(){k.header=k.colContainer.header=l;
var r=l[0].getElementsByClassName("ui-grid-header-canvas");
if(r.length>0){k.headerCanvas=k.colContainer.headerCanvas=r[0]
}else{k.headerCanvas=null
}}},post:function(r,m,q,l){var n=l[0];
var k=l[1];
var j=n.grid;
g.disableAnimations(m);
function p(){var C=k.colContainer.getViewportWidth()-j.scrollbarWidth;
var s=k.colContainer.visibleColumnCache,t=0,x=0,y=0,v=C,A=false;
var D=function(E){if(E.widthType==="manual"){return +E.width
}else{if(E.widthType==="percent"){return parseInt(E.width.replace(/%/g,""),10)*C/100
}else{if(E.widthType==="auto"){if(y===0){y=parseInt(v/x,10)
}return E.width.length*y
}}}};
s.forEach(function(E){E.widthType=null;
if(isFinite(+E.width)){E.widthType="manual"
}else{if(g.endsWith(E.width,"%")){E.widthType="percent";
A=true
}else{if(angular.isString(E.width)&&E.width.indexOf("*")!==-1){E.widthType="auto";
x+=E.width.length;
A=true
}}}});
var B=["manual","percent","auto"];
s.filter(function(E){return(E.visible&&E.widthType)
}).sort(function(F,E){return B.indexOf(F.widthType)-B.indexOf(E.widthType)
}).forEach(function(F){var E=D(F);
if(F.minWidth){E=Math.max(E,F.minWidth)
}if(F.maxWidth){E=Math.min(E,F.maxWidth)
}F.drawnWidth=Math.floor(E);
t+=F.drawnWidth;
v-=F.drawnWidth
});
if(A&&v>0&&t>0&&t<C){var u=function(E){if(v>0&&(E.widthType==="auto"||E.widthType==="percent")){E.drawnWidth=E.drawnWidth+1;
t=t+1;
v--
}};
var z=0;
do{z=v;
s.forEach(u)
}while(v>0&&v!==z)
}t=Math.max(t,C);
var w="";
s.forEach(function(E){w=w+E.getColClassDefinition()
});
if(s.length>0){s[s.length-1].headerWidth=s[s.length-1].drawnWidth-30
}k.colContainer.canvasWidth=parseInt(t,10);
return w
}k.header=m;
var o=m[0].getElementsByClassName("ui-grid-header-viewport")[0];
if(o){k.headerViewport=o
}if(n){n.grid.registerStyleComputation({priority:5,func:p})
}}}
}}
}])
})();
(function(){angular.module("ui.grid").service("uiGridGridMenuService",["gridUtil","i18nService","uiGridConstants",function(d,c,b){var a={initialize:function(e,f){f.gridMenuScope=e;
e.grid=f;
e.registeredMenuItems=[];
e.$on("$destroy",function(){if(e.grid&&e.grid.gridMenuScope){e.grid.gridMenuScope=null
}if(e.grid){e.grid=null
}if(e.registeredMenuItems){e.registeredMenuItems=null
}});
e.registeredMenuItems=[];
f.api.registerMethod("core","addToGridMenu",a.addToGridMenu);
f.api.registerMethod("core","removeFromGridMenu",a.removeFromGridMenu)
},addToGridMenu:function(f,e){if(!angular.isArray(e)){d.logError("addToGridMenu: menuItems must be an array, and is not, not adding any items")
}else{if(f.gridMenuScope){f.gridMenuScope.registeredMenuItems=f.gridMenuScope.registeredMenuItems?f.gridMenuScope.registeredMenuItems:[];
f.gridMenuScope.registeredMenuItems=f.gridMenuScope.registeredMenuItems.concat(e)
}else{d.logError("Asked to addToGridMenu, but gridMenuScope not present.  Timing issue?  Please log issue with ui-grid")
}}},removeFromGridMenu:function(e,g){var f=-1;
if(e&&e.gridMenuScope){e.gridMenuScope.registeredMenuItems.forEach(function(i,h){if(i.id===g){if(f>-1){d.logError("removeFromGridMenu: found multiple items with the same id, removing only the last")
}else{f=h
}}})
}if(f>-1){e.gridMenuScope.registeredMenuItems.splice(f,1)
}},getMenuItems:function(e){var f=[];
if(e.grid.options.gridMenuCustomItems){if(!angular.isArray(e.grid.options.gridMenuCustomItems)){d.logError("gridOptions.gridMenuCustomItems must be an array, and is not")
}else{f=f.concat(e.grid.options.gridMenuCustomItems)
}}f=f.concat(e.registeredMenuItems);
if(e.grid.options.gridMenuShowHideColumns!==false){f=f.concat(a.showHideColumns(e))
}return f
},showHideColumns:function(e){var f=[];
if(!e.grid.options.columnDefs||e.grid.options.columnDefs.length===0||e.grid.columns.length===0){return f
}f.push({title:c.getSafeText("gridMenu.columns")});
e.grid.options.gridMenuTitleFilter=e.grid.options.gridMenuTitleFilter?e.grid.options.gridMenuTitleFilter:function(g){return g
};
e.grid.options.columnDefs.forEach(function(i,g){if(i.enableHiding!==false){var h={icon:"ui-grid-icon-ok",action:function(j){j.stopPropagation();
a.toggleColumnVisibility(this.context.gridCol)
},shown:function(){return this.context.gridCol.colDef.visible===true||this.context.gridCol.colDef.visible===undefined
},context:{gridCol:e.grid.getColumn(i.name||i.field)}};
a.setMenuItemTitle(h,i,e.grid);
f.push(h);
h={icon:"ui-grid-icon-cancel",action:function(j){j.stopPropagation();
a.toggleColumnVisibility(this.context.gridCol)
},shown:function(){return !(this.context.gridCol.colDef.visible===true||this.context.gridCol.colDef.visible===undefined)
},context:{gridCol:e.grid.getColumn(i.name||i.field)}};
a.setMenuItemTitle(h,i,e.grid);
f.push(h)
}});
return f
},setMenuItemTitle:function(f,g,e){var h=e.options.gridMenuTitleFilter(g.displayName||g.name||g.field);
if(typeof(h)==="string"){f.title=h
}else{if(h.then){f.title="";
h.then(function(i){f.title=i
},function(i){f.title=i
})
}else{d.logError("Expected gridMenuTitleFilter to return a string or a promise, it has returned neither, bad config");
f.title="badconfig"
}}},toggleColumnVisibility:function(e){e.colDef.visible=!(e.colDef.visible===true||e.colDef.visible===undefined);
e.grid.refresh();
e.grid.api.core.notifyDataChange(b.dataChange.COLUMN);
e.grid.api.core.raise.columnVisibilityChanged(e)
}};
return a
}]).directive("uiGridMenuButton",["gridUtil","uiGridConstants","uiGridGridMenuService",function(c,a,b){return{priority:0,scope:true,require:["?^uiGrid"],templateUrl:"ui-grid/ui-grid-menu-button",replace:true,link:function(f,e,d,h){var g=h[0];
b.initialize(f,g.grid);
f.shown=false;
f.toggleMenu=function(){if(f.shown){f.$broadcast("hide-menu");
f.shown=false
}else{f.menuItems=b.getMenuItems(f);
f.$broadcast("show-menu");
f.shown=true
}};
f.$on("menu-hidden",function(){f.shown=false
})
}}
}])
})();
(function(){angular.module("ui.grid").directive("uiGridMenu",["$compile","$timeout","$window","$document","gridUtil","uiGridConstants",function(c,b,g,f,e,a){var d={priority:0,scope:{menuItems:"=",autoHide:"=?"},require:"?^uiGrid",templateUrl:"ui-grid/uiGridMenu",replace:false,link:function(l,j,i,m){var k=this;
var h;
var o;
k.showMenu=l.showMenu=function(q,p){if(!l.shown){l.shown=true;
b(function(){l.shownMid=true;
l.$emit("menu-shown")
})
}else{if(!l.shownMid){l.shownMid=true;
l.$emit("menu-shown")
}}var r="click";
if(p&&p.originalEvent&&p.originalEvent.type&&p.originalEvent.type==="touchstart"){r=p.originalEvent.type
}angular.element(document).off("click touchstart",n);
b(function(){angular.element(document).on(r,n)
})
};
k.hideMenu=l.hideMenu=function(q,p){if(l.shown){l.shownMid=false;
b(function(){if(!l.shownMid){l.shown=false;
l.$emit("menu-hidden")
}},200)
}angular.element(document).off("click touchstart",n)
};
l.$on("hide-menu",function(q,p){l.hideMenu(q,p)
});
l.$on("show-menu",function(q,p){l.showMenu(q,p)
});
var n=function(){if(l.shown){l.$apply(function(){l.hideMenu()
})
}};
if(typeof(l.autoHide)==="undefined"||l.autoHide===undefined){l.autoHide=true
}if(l.autoHide){angular.element(g).on("resize",n)
}l.$on("$destroy",function(){angular.element(document).off("click touchstart",n)
});
l.$on("$destroy",function(){angular.element(g).off("resize",n)
});
if(m){l.$on("$destroy",m.grid.api.core.on.scrollEvent(l,n))
}l.$on("$destroy",l.$on(a.events.ITEM_DRAGGING,n))
},controller:["$scope","$element","$attrs",function(k,i,h){var j=this
}]};
return d
}]).directive("uiGridMenuItem",["gridUtil","$compile","i18nService",function(d,c,a){var b={priority:0,scope:{name:"=",active:"=",action:"=",icon:"=",shown:"=",context:"=",templateUrl:"="},require:["?^uiGrid","^uiGridMenu"],templateUrl:"ui-grid/uiGridMenuItem",replace:true,compile:function(f,e){return{pre:function(i,h,g,l){var k=l[0],j=l[1];
if(i.templateUrl){d.getTemplate(i.templateUrl).then(function(o){var n=angular.element(o);
var m=c(n)(i);
h.replaceWith(m)
})
}},post:function(i,h,g,l){var k=l[0],j=l[1];
if(typeof(i.shown)==="undefined"||i.shown===null){i.shown=function(){return true
}
}i.itemShown=function(){var m={};
if(i.context){m.context=i.context
}if(typeof(k)!=="undefined"&&k){m.grid=k.grid
}return i.shown.call(m)
};
i.itemAction=function(m,o){m.stopPropagation();
if(typeof(i.action)==="function"){var n={};
if(i.context){n.context=i.context
}if(typeof(k)!=="undefined"&&k){n.grid=k.grid
}i.action.call(n,m,o);
i.$emit("hide-menu")
}};
i.i18n=a.get()
}}
}};
return b
}])
})();
(function(){var a=angular.module("ui.grid");
a.directive("uiGridRenderContainer",["$timeout","$document","uiGridConstants","gridUtil","ScrollEvent",function(c,f,b,e,d){return{replace:true,transclude:true,templateUrl:"ui-grid/uiGridRenderContainer",require:["^uiGrid","uiGridRenderContainer"],scope:{containerId:"=",rowContainerName:"=",colContainerName:"=",bindScrollHorizontal:"=",bindScrollVertical:"=",enableVerticalScrollbar:"=",enableHorizontalScrollbar:"="},controller:"uiGridRenderContainer as RenderContainer",compile:function(){return{pre:function g(p,m,o,l){var n=l[0];
var j=l[1];
var i=p.grid=n.grid;
if(!p.rowContainerName){throw"No row render container name specified"
}if(!p.colContainerName){throw"No column render container name specified"
}if(!i.renderContainers[p.rowContainerName]){throw"Row render container '"+p.rowContainerName+"' is not registered."
}if(!i.renderContainers[p.colContainerName]){throw"Column render container '"+p.colContainerName+"' is not registered."
}var q=p.rowContainer=i.renderContainers[p.rowContainerName];
var k=p.colContainer=i.renderContainers[p.colContainerName];
j.containerId=p.containerId;
j.rowContainer=q;
j.colContainer=k
},post:function h(s,n,r,m){var q=m[0];
var j=m[1];
var i=q.grid;
var t=j.rowContainer;
var l=j.colContainer;
var p=i.renderContainers[s.containerId];
n.addClass("ui-grid-render-container-"+s.containerId);
if(s.bindScrollHorizontal||s.bindScrollVertical){i.api.core.on.scrollEvent(s,k)
}function k(v){if(v.grid&&v.grid.id!==i.id){return
}if(v.y&&s.bindScrollVertical){j.prevScrollArgs=v;
var u=v.getNewScrollTop(t,j.viewport);
if(v.source!==d.Sources.ViewPortScroll||v.sourceColContainer!==l){j.viewport[0].scrollTop=u
}}if(v.x&&s.bindScrollHorizontal){j.prevScrollArgs=v;
var w=v.getNewScrollLeft(l,j.viewport);
s.newScrollLeft=w;
if(j.headerViewport){j.headerViewport.scrollLeft=e.denormalizeScrollLeft(j.headerViewport,w)
}if(j.footerViewport){j.footerViewport.scrollLeft=e.denormalizeScrollLeft(j.footerViewport,w)
}if(v.source!==d.Sources.ViewPortScroll){j.viewport[0].scrollLeft=w
}j.prevScrollLeft=w
}}e.on.mousewheel(n,function(x){var v=new d(i,t,l,d.Sources.RenderContainerMouseWheel);
if(x.deltaY!==0){var A=x.deltaY*-1*x.deltaFactor;
var y=(j.viewport[0].scrollTop+A)/t.getVerticalScrollLength();
if(y<0){y=0
}else{if(y>1){y=1
}}v.y={percentage:y,pixels:A}
}if(x.deltaX!==0){var w=x.deltaX*x.deltaFactor;
var z=e.normalizeScrollLeft(j.viewport);
var u=(z+w)/(l.getCanvasWidth()-l.getViewportWidth());
if(u<0){u=0
}else{if(u>1){u=1
}}v.x={percentage:u,pixels:w}
}if((v.y&&v.y.percentage!==0&&v.y.percentage!==1)||(v.x&&v.x.percentage!==0&&v.x.percentage!==1)){x.preventDefault();
v.fireThrottledScrollingEvent()
}});
n.bind("$destroy",function(){n.unbind("keydown");
["touchstart","touchmove","touchend","keydown","wheel","mousewheel","DomMouseScroll","MozMousePixelScroll"].forEach(function(u){n.unbind(u)
})
});
function o(){var z="";
var u=l.getCanvasWidth();
var v=l.getViewportWidth();
var y=t.getCanvasHeight();
if(s.containerId!=="body"){y+=i.scrollbarHeight
}var w=t.getViewportHeight();
var A=l.getHeaderViewportWidth();
var x=l.getHeaderViewportWidth();
z+="\n .grid"+q.grid.id+" .ui-grid-render-container-"+s.containerId+" .ui-grid-canvas { width: "+u+"px; height: "+y+"px; }";
z+="\n .grid"+q.grid.id+" .ui-grid-render-container-"+s.containerId+" .ui-grid-header-canvas { width: "+(u+i.scrollbarWidth)+"px; }";
if(p.explicitHeaderCanvasHeight){z+="\n .grid"+q.grid.id+" .ui-grid-render-container-"+s.containerId+" .ui-grid-header-canvas { height: "+p.explicitHeaderCanvasHeight+"px; }"
}z+="\n .grid"+q.grid.id+" .ui-grid-render-container-"+s.containerId+" .ui-grid-viewport { width: "+v+"px; height: "+w+"px; }";
z+="\n .grid"+q.grid.id+" .ui-grid-render-container-"+s.containerId+" .ui-grid-header-viewport { width: "+A+"px; }";
z+="\n .grid"+q.grid.id+" .ui-grid-render-container-"+s.containerId+" .ui-grid-footer-canvas { width: "+u+i.scrollbarWidth+"px; }";
z+="\n .grid"+q.grid.id+" .ui-grid-render-container-"+s.containerId+" .ui-grid-footer-viewport { width: "+x+"px; }";
return z
}q.grid.registerStyleComputation({priority:6,func:o})
}}
}}
}]);
a.controller("uiGridRenderContainer",["$scope","gridUtil",function(b,c){}])
})();
(function(){angular.module("ui.grid").directive("uiGridRow",["gridUtil",function(a){return{replace:true,require:["^uiGrid","^uiGridRenderContainer"],scope:{row:"=uiGridRow",rowRenderIndex:"="},compile:function(){return{pre:function(k,e,i,d){var g=d[0];
var c=d[1];
var b=g.grid;
k.grid=g.grid;
k.colContainer=c.colContainer;
var f,h;
function j(){k.row.getRowTemplateFn.then(function(l){var m=k.$new();
l(m,function(n,o){if(f){f.remove();
h.$destroy()
}e.empty().append(n);
f=n;
h=m
})
})
}j();
k.$watch("row.getRowTemplateFn",function(l,m){if(l!==m){j()
}})
},post:function(d,c,b,e){}}
}}
}])
})();
(function(){angular.module("ui.grid").directive("uiGridStyle",["gridUtil","$interpolate",function(b,a){return{link:function(f,d,c,g){var e=a(d.text(),true);
if(e){f.$watch(e,function(h){d.text(h)
})
}}}
}])
})();
(function(){angular.module("ui.grid").directive("uiGridViewport",["gridUtil","ScrollEvent","uiGridConstants",function(c,b,a){return{replace:true,scope:{},controllerAs:"Viewport",templateUrl:"ui-grid/uiGridViewport",require:["^uiGrid","^uiGridRenderContainer"],link:function(k,h,j,f){var i=f[0];
var e=f[1];
k.containerCtrl=e;
var l=e.rowContainer;
var g=e.colContainer;
var d=i.grid;
k.grid=i.grid;
k.rowContainer=e.rowContainer;
k.colContainer=e.colContainer;
e.viewport=h;
h.on("scroll",function(u){var m=h[0].scrollTop;
var v=c.normalizeScrollLeft(h);
var n=-1;
var o=-1;
if(v!==g.prevScrollLeft){d.flagScrollingHorizontally();
var t=v-g.prevScrollLeft;
if(t>0){d.scrollDirection=a.scrollDirection.RIGHT
}if(t<0){d.scrollDirection=a.scrollDirection.LEFT
}var r=(g.getCanvasWidth()-g.getViewportWidth());
if(r!==0){n=v/r
}else{n=0
}g.adjustScrollHorizontal(v,n)
}if(m!==l.prevScrollTop){d.flagScrollingVertically();
var s=m-l.prevScrollTop;
if(s>0){d.scrollDirection=a.scrollDirection.DOWN
}if(s<0){d.scrollDirection=a.scrollDirection.UP
}var q=l.getVerticalScrollLength();
o=m/q;
if(o>1){o=1
}if(o<0){o=0
}l.adjustScrollVertical(m,o)
}var p=new b(d,l,g,b.Sources.ViewPortScroll);
p.newScrollLeft=v;
p.newScrollTop=m;
if(n>-1){p.x={percentage:n}
}if(o>-1){p.y={percentage:o}
}p.fireScrollingEvent()
})
},controller:["$scope",function(d){this.rowStyle=function(g){var f=d.rowContainer;
var i=d.colContainer;
var h={};
if(g===0&&f.currentTopRow!==0){var e=(f.currentTopRow)*f.grid.options.rowHeight;
h["margin-top"]=e+"px"
}if(i.currentFirstColumn!==0){if(i.grid.isRTL()){h["margin-right"]=i.columnOffset+"px"
}else{h["margin-left"]=i.columnOffset+"px"
}}return h
}
}]}
}])
})();
(function(){angular.module("ui.grid").directive("uiGridVisible",function a(){return function(c,b,d){c.$watch(d.uiGridVisible,function(e){b[e?"removeClass":"addClass"]("ui-grid-invisible")
})
}
})
})();
(function(){angular.module("ui.grid").controller("uiGridController",["$scope","$element","$attrs","gridUtil","$q","uiGridConstants","$templateCache","gridClassFactory","$timeout","$parse","$compile","ScrollEvent",function(n,o,g,r,p,k,i,h,s,e,q,d){var l=this;
l.grid=h.createGrid(n.uiGrid);
l.grid.appScope=l.grid.appScope||n.$parent;
o.addClass("grid"+l.grid.id);
l.grid.rtl=r.getStyles(o[0])["direction"]==="rtl";
n.grid=l.grid;
if(g.uiGridColumns){g.$observe("uiGridColumns",function(u){l.grid.options.columnDefs=u;
l.grid.buildColumns().then(function(){l.grid.preCompileCellTemplates();
l.grid.refreshCanvas(true)
})
})
}var f;
if(angular.isString(n.uiGrid.data)){f=n.$parent.$watchCollection(n.uiGrid.data,j)
}else{f=n.$parent.$watchCollection(function(){return n.uiGrid.data
},j)
}var m=n.$parent.$watchCollection(function(){return n.uiGrid.columnDefs
},a);
function a(v,u){if(v&&v!==u){l.grid.options.columnDefs=v;
l.grid.buildColumns({orderByColumnDefs:true}).then(function(){l.grid.preCompileCellTemplates();
l.grid.callDataChangeCallbacks(k.dataChange.COLUMN)
})
}}function t(v){var x=new d(l.grid,null,null,"ui.grid.adjustInfiniteScrollPosition");
var w=l.grid.renderContainers.body.visibleRowCache.length;
var u=(v+(v/(w-1)))/w;
if(u===0){x.y={pixels:1}
}else{x.y={percentage:u}
}x.fireScrollingEvent()
}function j(v){var u=[];
if(v){if(l.grid.columns.length===(l.grid.rowHeaderColumns?l.grid.rowHeaderColumns.length:0)&&!g.uiGridColumns&&l.grid.options.columnDefs.length===0&&v.length>0){l.grid.buildColumnDefsFromData(v)
}if(l.grid.options.columnDefs.length>0||v.length>0){u.push(l.grid.buildColumns().then(function(){l.grid.preCompileCellTemplates()
}))
}p.all(u).then(function(){l.grid.modifyRows(v).then(function(){l.grid.redrawInPlace(true);
n.$evalAsync(function(){l.grid.refreshCanvas(true);
l.grid.callDataChangeCallbacks(k.dataChange.ROW);
s(function(){if(l.grid.options.enableInfiniteScroll){if(l.grid.renderContainers.body.prevRowScrollIndex===0){t(0)
}if(l.grid.scrollDirection===k.scrollDirection.UP){t(l.grid.renderContainers.body.prevRowScrollIndex+1+l.grid.options.excessRows)
}}},0)
})
})
})
}}var c=n.$watch(function(){return l.grid.styleComputations
},function(){l.grid.refreshCanvas(true)
});
n.$on("$destroy",function(){f();
m();
c()
});
l.fireEvent=function(u,v){if(typeof(v)==="undefined"||v===undefined){v={}
}if(typeof(v.grid)==="undefined"||v.grid===undefined){v.grid=l.grid
}n.$broadcast(u,v)
};
l.innerCompile=function b(u){q(u)(n)
}
}]);
angular.module("ui.grid").directive("uiGrid",["$compile","$templateCache","gridUtil","$window","uiGridConstants",function(c,b,e,d,a){return{templateUrl:"ui-grid/ui-grid",scope:{uiGrid:"="},replace:true,transclude:true,controller:"uiGridController",compile:function(){return{post:function(r,j,q,m){var h=m.grid;
m.scrollbars=[];
h.renderingComplete();
h.element=j;
h.gridWidth=r.gridWidth=e.elementWidth(j);
h.canvasWidth=m.grid.gridWidth;
h.gridHeight=r.gridHeight=e.elementHeight(j);
if(h.gridHeight<h.options.rowHeight){var p=h.options.minRowsToShow*h.options.rowHeight;
var l=h.options.showHeader?h.options.headerRowHeight:0;
var o=h.calcFooterHeight();
var i=0;
if(h.options.enableHorizontalScrollbar===a.scrollbars.ALWAYS){i=e.getScrollbarWidth()
}var n=0;
angular.forEach(h.options.columnDefs,function(s){if(s.hasOwnProperty("filter")){if(n<1){n=1
}}else{if(s.hasOwnProperty("filters")){if(n<s.filters.length){n=s.filters.length
}}}});
var g=n*l;
var f=l+p+o+i+g;
j.css("height",f+"px");
h.gridHeight=r.gridHeight=e.elementHeight(j)
}h.refreshCanvas();
r.$watch(function(){return h.hasLeftContainer()
},function(t,s){if(t===s){return
}h.refreshCanvas(true)
});
r.$watch(function(){return h.hasRightContainer()
},function(t,s){if(t===s){return
}h.refreshCanvas(true)
});
function k(s){h.gridWidth=r.gridWidth=e.elementWidth(j);
h.gridHeight=r.gridHeight=e.elementHeight(j);
h.refreshCanvas(true)
}angular.element(d).on("resize",k);
j.on("$destroy",function(){angular.element(d).off("resize",k)
})
}}
}}
}])
})();
(function(){angular.module("ui.grid").directive("uiGridPinnedContainer",["gridUtil",function(b){return{restrict:"EA",replace:true,template:'<div class="ui-grid-pinned-container"><div ui-grid-render-container container-id="side" row-container-name="\'body\'" col-container-name="side" bind-scroll-vertical="true" class="{{ side }} ui-grid-render-container-{{ side }}"></div></div>',scope:{side:"=uiGridPinnedContainer"},require:"^uiGrid",compile:function a(){return{post:function(f,d,c,j){var h=j.grid;
var e=0;
d.addClass("ui-grid-pinned-container-"+f.side);
function g(){if(f.side==="left"||f.side==="right"){var n=h.renderContainers[f.side].visibleColumnCache;
var m=0;
for(var l=0;
l<n.length;
l++){var k=n[l];
m+=k.drawnWidth||k.width||0
}e=m
}}function i(){var l="";
if(f.side==="left"||f.side==="right"){g();
d.attr("style",null);
var k=h.renderContainers.body.getViewportHeight();
l+=".grid"+h.id+" .ui-grid-pinned-container-"+f.side+", .grid"+h.id+" .ui-grid-pinned-container-"+f.side+" .ui-grid-render-container-"+f.side+" .ui-grid-viewport { width: "+e+"px; height: "+k+"px; } "
}return l
}h.renderContainers.body.registerViewportAdjuster(function(k){if(e===0||!e){g()
}k.width-=e;
return k
});
h.registerStyleComputation({priority:15,func:i})
}}
}}
}])
})();
(function(){angular.module("ui.grid").factory("Grid",["$q","$compile","$parse","gridUtil","uiGridConstants","GridOptions","GridColumn","GridRow","GridApi","rowSorter","rowSearcher","GridRenderContainer","$timeout",function(f,a,am,q,i,Y,e,B,k,p,R,H,aa){var W=function W(ar){var aq=this;
if(ar!==undefined&&typeof(ar.id)!=="undefined"&&ar.id){if(!/^[_a-zA-Z0-9-]+$/.test(ar.id)){throw new Error("Grid id '"+ar.id+'" is invalid. It must follow CSS selector syntax rules.')
}}else{throw new Error("No ID provided. An ID must be given when creating a grid.")
}aq.id=ar.id;
delete ar.id;
aq.options=Y.initialize(ar);
aq.appScope=aq.options.appScopeProvider;
aq.headerHeight=aq.options.headerRowHeight;
aq.footerHeight=aq.calcFooterHeight();
aq.rtl=false;
aq.gridHeight=0;
aq.gridWidth=0;
aq.columnBuilders=[];
aq.rowBuilders=[];
aq.rowsProcessors=[];
aq.columnsProcessors=[];
aq.styleComputations=[];
aq.viewportAdjusters=[];
aq.rowHeaderColumns=[];
aq.dataChangeCallbacks={};
aq.renderContainers={};
aq.renderContainers.body=new H("body",aq);
aq.cellValueGetterCache={};
aq.getRowTemplateFn=null;
aq.rows=[];
aq.columns=[];
aq.isScrollingVertically=false;
aq.isScrollingHorizontally=false;
aq.scrollDirection=i.scrollDirection.NONE;
var ap=q.debounce(function(){aq.isScrollingVertically=false;
aq.scrollDirection=i.scrollDirection.NONE
},1000);
var at=q.debounce(function(){aq.isScrollingHorizontally=false;
aq.scrollDirection=i.scrollDirection.NONE
},1000);
aq.flagScrollingVertically=function(){aq.isScrollingVertically=true;
ap()
};
aq.flagScrollingHorizontally=function(){aq.isScrollingHorizontally=true;
at()
};
aq.scrollbarHeight=0;
aq.scrollbarWidth=0;
if(aq.options.enableHorizontalScrollbar===i.scrollbars.ALWAYS){aq.scrollbarHeight=q.getScrollbarWidth()
}if(aq.options.enableVerticalScrollbar===i.scrollbars.ALWAYS){aq.scrollbarWidth=q.getScrollbarWidth()
}aq.api=new k(aq);
aq.api.registerMethod("core","refresh",this.refresh);
aq.api.registerMethod("core","queueGridRefresh",this.queueGridRefresh);
aq.api.registerMethod("core","refreshRows",this.refreshRows);
aq.api.registerMethod("core","refreshRows",this.queueRefresh);
aq.api.registerMethod("core","handleWindowResize",this.handleWindowResize);
aq.api.registerMethod("core","addRowHeaderColumn",this.addRowHeaderColumn);
aq.api.registerMethod("core","sortHandleNulls",p.handleNulls);
aq.api.registerEvent("core","sortChanged");
aq.api.registerEvent("core","columnVisibilityChanged");
aq.api.registerMethod("core","notifyDataChange",this.notifyDataChange);
aq.registerDataChangeCallback(aq.columnRefreshCallback,[i.dataChange.COLUMN]);
aq.registerDataChangeCallback(aq.processRowsCallback,[i.dataChange.EDIT]);
aq.registerStyleComputation({priority:10,func:aq.getFooterStyles})
};
W.prototype.calcFooterHeight=function(){if(!this.hasFooter()){return 0
}var ap=0;
if(this.options.showGridFooter){ap+=this.options.gridFooterHeight
}if(this.options.showColumnFooter){ap+=this.options.columnFooterHeight
}return ap
};
W.prototype.getFooterStyles=function(){var ap=".grid"+this.id+" .ui-grid-footer-aggregates-row { height: "+this.options.columnFooterHeight+"px; }";
ap+=" .grid"+this.id+" .ui-grid-footer-info { height: "+this.options.gridFooterHeight+"px; }";
return ap
};
W.prototype.hasFooter=function(){return this.options.showGridFooter||this.options.showColumnFooter
};
W.prototype.isRTL=function(){return this.rtl
};
W.prototype.registerColumnBuilder=function ab(ap){this.columnBuilders.push(ap)
};
W.prototype.buildColumnDefsFromData=function(ap){this.options.columnDefs=q.getColumnsFromData(ap,this.options.excludeProperties)
};
W.prototype.registerRowBuilder=function n(ap){this.rowBuilders.push(ap)
};
W.prototype.registerDataChangeCallback=function ah(av,ar,au){var aq=q.nextUid();
if(!ar){ar=[i.dataChange.ALL]
}if(!Array.isArray(ar)){q.logError("Expected types to be an array or null in registerDataChangeCallback, value passed was: "+ar)
}this.dataChangeCallbacks[aq]={callback:av,types:ar,_this:au};
var ap=this;
var at=function(){delete ap.dataChangeCallbacks[aq]
};
return at
};
W.prototype.callDataChangeCallbacks=function J(aq,ap){angular.forEach(this.dataChangeCallbacks,function(at,ar){if(at.types.indexOf(i.dataChange.ALL)!==-1||at.types.indexOf(aq)!==-1||aq===i.dataChange.ALL){if(at._this){at.callback.apply(at._this,this)
}else{at.callback(this)
}}},this)
};
W.prototype.notifyDataChange=function aj(aq){var ap=i.dataChange;
if(aq===ap.ALL||aq===ap.COLUMN||aq===ap.EDIT||aq===ap.ROW||aq===ap.OPTIONS){this.callDataChangeCallbacks(aq)
}else{q.logError("Notified of a data change, but the type was not recognised, so no action taken, type was: "+aq)
}};
W.prototype.columnRefreshCallback=function U(ap){ap.buildColumns();
ap.queueGridRefresh()
};
W.prototype.processRowsCallback=function b(ap){ap.queueGridRefresh()
};
W.prototype.getColumn=function C(ap){var aq=this.columns.filter(function(ar){return ar.colDef.name===ap
});
return aq.length>0?aq[0]:null
};
W.prototype.getColDef=function ac(aq){var ap=this.options.columnDefs.filter(function(ar){return ar.name===aq
});
return ap.length>0?ap[0]:null
};
W.prototype.assignTypes=function(){var ap=this;
ap.options.columnDefs.forEach(function(at,ar){if(!at.type){var aq=new e(at,ar,ap);
var au=ap.rows.length>0?ap.rows[0]:null;
if(au){at.type=q.guessType(ap.getCellValue(au,aq))
}else{q.logWarn("Unable to assign type from data, so defaulting to string");
at.type="string"
}}})
};
W.prototype.addRowHeaderColumn=function c(ar){var aq=this;
var ap=new e(ar,q.nextUid(),aq);
ap.isRowHeader=true;
if(aq.isRTL()){aq.createRightContainer();
ap.renderContainer="right"
}else{aq.createLeftContainer();
ap.renderContainer="left"
}aq.columnBuilders[0](ar,ap,aq.options).then(function(){ap.enableFiltering=false;
ap.enableSorting=false;
ap.enableHiding=false;
aq.rowHeaderColumns.push(ap);
aq.buildColumns().then(function(){aq.preCompileCellTemplates();
aq.queueGridRefresh()
})
})
};
W.prototype.buildColumns=function ad(av){var ar={orderByColumnDefs:false};
angular.extend(ar,av);
var aq=this;
var aw=[];
var ax=aq.rowHeaderColumns.length;
var au;
for(au=0;
au<aq.columns.length;
au++){if(!aq.getColDef(aq.columns[au].name)){aq.columns.splice(au,1);
au--
}}aq.rowHeaderColumns.forEach(function(ay){aq.columns.unshift(ay)
});
aq.options.columnDefs.forEach(function(aA,az){aq.preprocessColDef(aA);
var ay=aq.getColumn(aA.name);
if(!ay){ay=new e(aA,q.nextUid(),aq);
aq.columns.splice(az+ax,0,ay)
}else{ay.updateColumnDef(aA,false)
}aq.columnBuilders.forEach(function(aB){aw.push(aB.call(aq,aA,ay,aq.options))
})
});
if(!!ar.orderByColumnDefs){var at=aq.columns.slice(0);
var ap=Math.min(aq.options.columnDefs.length,aq.columns.length);
for(au=0;
au<ap;
au++){if(aq.columns[au+ax].name!==aq.options.columnDefs[au].name){at[au+ax]=aq.getColumn(aq.options.columnDefs[au].name)
}else{at[au+ax]=aq.columns[au+ax]
}}aq.columns.length=0;
Array.prototype.splice.apply(aq.columns,[0,0].concat(at))
}return f.all(aw).then(function(){if(aq.rows.length>0){aq.assignTypes()
}})
};
W.prototype.preCompileCellTemplates=function(){var ap=this;
this.columns.forEach(function(aq){var ar=aq.cellTemplate.replace(i.MODEL_COL_FIELD,ap.getQualifiedColField(aq));
ar=ar.replace(i.COL_FIELD,"grid.getCellValue(row, col)");
var at=a(ar);
aq.compiledElementFn=at;
if(aq.compiledElementFnDefer){aq.compiledElementFnDefer.resolve(aq.compiledElementFn)
}})
};
W.prototype.getQualifiedColField=function(ap){return"row.entity."+q.preEval(ap.field)
};
W.prototype.createLeftContainer=function(){if(!this.hasLeftContainer()){this.renderContainers.left=new H("left",this,{disableColumnOffset:true})
}};
W.prototype.createRightContainer=function(){if(!this.hasRightContainer()){this.renderContainers.right=new H("right",this,{disableColumnOffset:true})
}};
W.prototype.hasLeftContainer=function(){return this.renderContainers.left!==undefined
};
W.prototype.hasRightContainer=function(){return this.renderContainers.right!==undefined
};
W.prototype.preprocessColDef=function al(au){var ar=this;
if(!au.field&&!au.name){throw new Error("colDef.name or colDef.field property is required")
}if(au.name===undefined&&au.field!==undefined){var av=ar.getColumn(au.field);
if(av){var aq=new RegExp("^"+au.field+"(\\d+)$","i");
var at=ar.columns.filter(function(aw){return aq.test(aw.displayName)
}).sort(function(ax,aw){if(ax===aw){return 0
}else{var az=ax.displayName.match(aq)[1];
var ay=aw.displayName.match(aq)[1];
return parseInt(az,10)>parseInt(ay,10)?1:-1
}});
if(at.length===0){au.name=au.field+"2"
}else{var ap=at[at.length-1].displayName.match(aq)[1];
ap=parseInt(ap,10);
au.name=au.field+(ap+1)
}}else{au.name=au.field
}}};
W.prototype.newInN=function af(aq,ar,ap,aw){var ay=this;
var ax=[];
for(var au=0;
au<ar.length;
au++){var aA=aw?ar[au][aw]:ar[au];
var az=false;
for(var at=0;
at<aq.length;
at++){var av=ap?aq[at][ap]:aq[at];
if(ay.options.rowEquality(aA,av)){az=true;
break
}}if(!az){ax.push(aA)
}}return ax
};
W.prototype.getRow=function l(ar){var ap=this;
var aq=this.rows.filter(function(at){return ap.options.rowEquality(at.entity,ar)
});
return aq.length>0?aq[0]:null
};
W.prototype.modifyRows=function x(aF){var aD=this,aH,aw,aC,au;
if((aD.options.useExternalSorting||aD.getColumnSorting().length===0)&&aF.length>0){var az=aD.rowHashMap;
if(!az){az={get:function(){return null
}}
}aD.createRowHashMap();
aw=aD.rowHashMap;
var aK=aD.rows.length===0;
aD.rows.length=0;
for(aH=0;
aH<aF.length;
aH++){var ax=aF[aH];
aC=az.get(ax);
if(aC){au=aC.row
}else{au=aD.processRowBuilders(new B(ax,aH,aD))
}aD.rows.push(au);
aw.put(ax,{i:aH,entity:ax,row:au})
}aD.assignTypes()
}else{if(aD.rows.length===0&&aF.length>0){if(aD.options.enableRowHashing){if(!aD.rowHashMap){aD.createRowHashMap()
}for(aH=0;
aH<aF.length;
aH++){au=aF[aH];
aD.rowHashMap.put(au,{i:aH,entity:au})
}}aD.addRows(aF);
aD.assignTypes()
}else{if(aF.length>0){var aE,aI,aA;
if(aD.options.enableRowHashing){aE=[];
aA=[];
var ap={};
aI=[];
if(!aD.rowHashMap){aD.createRowHashMap()
}aw=aD.rowHashMap;
for(aH=0;
aH<aF.length;
aH++){au=aF[aH];
var aB=false;
if(!aD.options.getRowIdentity(au)){aB=true
}aC=aw.get(au);
if(aC){if(aC.row){ap[aD.options.rowIdentity(au)]=true
}}else{aw.put(au,{i:aH,entity:au});
if(aB){aA.push(au)
}else{aE.push(au)
}}}for(aH=0;
aH<aD.rows.length;
aH++){var ay=aD.rows[aH];
var at=aD.options.rowIdentity(ay.entity);
if(!ap[at]){aI.push(ay)
}}}var aJ=aE||[];
var av=(aA||aF);
aJ=aJ.concat(aD.newInN(aD.rows,av,"entity"));
aD.addRows(aJ);
var aG=aD.getDeletedRows((aI||aD.rows),aF);
for(aH=0;
aH<aG.length;
aH++){if(aD.options.enableRowHashing){aD.rowHashMap.remove(aG[aH].entity)
}aD.rows.splice(aD.rows.indexOf(aG[aH]),1)
}}else{aD.createRowHashMap();
aD.rows.length=0
}}}var ar=f.when(aD.processRowsProcessors(aD.rows)).then(function(aL){return aD.setVisibleRows(aL)
});
var aq=f.when(aD.processColumnsProcessors(aD.columns)).then(function(aL){return aD.setVisibleColumns(aL)
});
return f.all([ar,aq])
};
W.prototype.getDeletedRows=function(at,ar){var aq=this;
var ap=at.filter(function(au){return !ar.some(function(av){return aq.options.rowEquality(av,au.entity)
})
});
return ap
};
W.prototype.addRows=function t(au){var ap=this;
var ar=ap.rows.length;
for(var at=0;
at<au.length;
at++){var aq=ap.processRowBuilders(new B(au[at],at+ar,ap));
if(ap.options.enableRowHashing){var av=ap.rowHashMap.get(aq.entity);
if(av){av.row=aq
}}ap.rows.push(aq)
}};
W.prototype.processRowBuilders=function an(aq){var ap=this;
ap.rowBuilders.forEach(function(ar){ar.call(ap,aq,ap.options)
});
return aq
};
W.prototype.registerStyleComputation=function w(ap){this.styleComputations.push(ap)
};
W.prototype.registerRowsProcessor=function y(ap){if(!angular.isFunction(ap)){throw"Attempt to register non-function rows processor: "+ap
}this.rowsProcessors.push(ap)
};
W.prototype.removeRowsProcessor=function ae(aq){var ap=this.rowsProcessors.indexOf(aq);
if(typeof(ap)!=="undefined"&&ap!==undefined){this.rowsProcessors.splice(ap,1)
}};
W.prototype.processRowsProcessors=function P(ar){var ap=this;
var at=ar.slice(0);
if(ap.rowsProcessors.length===0){return f.when(at)
}var aq=0;
var av=f.defer();
function au(aw,ay){var ax=ap.rowsProcessors[aw];
return f.when(ax.call(ap,ay,ap.columns)).then(function az(aA){if(!aA){throw"Processor at index "+aw+" did not return a set of renderable rows"
}if(!angular.isArray(aA)){throw"Processor at index "+aw+" did not return an array"
}aw++;
if(aw<=ap.rowsProcessors.length-1){return au(aw,aA)
}else{av.resolve(aA)
}})
}au(0,at);
return av.promise
};
W.prototype.setVisibleRows=function ag(av){var ar=this;
for(var at in ar.renderContainers){var aq=ar.renderContainers[at];
aq.canvasHeightShouldUpdate=true;
if(typeof(aq.visibleRowCache)==="undefined"){aq.visibleRowCache=[]
}else{aq.visibleRowCache.length=0
}}for(var ap=0;
ap<av.length;
ap++){var aw=av[ap];
var au=(typeof(aw.renderContainer)!=="undefined"&&aw.renderContainer)?aw.renderContainer:"body";
if(aw.visible){ar.renderContainers[au].visibleRowCache.push(aw)
}}ar.api.core.raise.rowsRendered(this.api)
};
W.prototype.registerColumnsProcessor=function ai(ap){if(!angular.isFunction(ap)){throw"Attempt to register non-function rows processor: "+ap
}this.columnsProcessors.push(ap)
};
W.prototype.removeColumnsProcessor=function j(aq){var ap=this.columnsProcessors.indexOf(aq);
if(typeof(ap)!=="undefined"&&ap!==undefined){this.columnsProcessors.splice(ap,1)
}};
W.prototype.processColumnsProcessors=function Q(ar){var aq=this;
var ap=ar.slice(0);
if(aq.columnsProcessors.length===0){return f.when(ap)
}var at=0;
var av=f.defer();
function au(aw,ay){var ax=aq.columnsProcessors[aw];
return f.when(ax.call(aq,ay,aq.rows)).then(function az(aA){if(!aA){throw"Processor at index "+aw+" did not return a set of renderable rows"
}if(!angular.isArray(aA)){throw"Processor at index "+aw+" did not return an array"
}aw++;
if(aw<=aq.columnsProcessors.length-1){return au(aw,ap)
}else{av.resolve(ap)
}})
}au(0,ap);
return av.promise
};
W.prototype.setVisibleColumns=function u(au){var aq=this;
for(var at in aq.renderContainers){var ap=aq.renderContainers[at];
ap.visibleColumnCache.length=0
}for(var ar=0;
ar<au.length;
ar++){var av=au[ar];
if(av.visible){if(typeof(av.renderContainer)!=="undefined"&&av.renderContainer){aq.renderContainers[av.renderContainer].visibleColumnCache.push(av)
}else{aq.renderContainers.body.visibleColumnCache.push(av)
}}}};
W.prototype.handleWindowResize=function K(aq){var ap=this;
ap.gridWidth=q.elementWidth(ap.element);
ap.gridHeight=q.elementHeight(ap.element);
ap.queueRefresh()
};
W.prototype.queueRefresh=function S(){var ap=this;
if(ap.refreshCanceller){aa.cancel(ap.refreshCanceller)
}ap.refreshCanceller=aa(function(){ap.refreshCanvas(true)
});
ap.refreshCanceller.then(function(){ap.refreshCanceller=null
});
return ap.refreshCanceller
};
W.prototype.queueGridRefresh=function F(){var ap=this;
if(ap.gridRefreshCanceller){aa.cancel(ap.gridRefreshCanceller)
}ap.gridRefreshCanceller=aa(function(){ap.refresh(true)
});
ap.gridRefreshCanceller.then(function(){ap.gridRefreshCanceller=null
});
return ap.gridRefreshCanceller
};
W.prototype.updateCanvasHeight=function E(){var ar=this;
for(var aq in ar.renderContainers){if(ar.renderContainers.hasOwnProperty(aq)){var ap=ar.renderContainers[aq];
ap.canvasHeightShouldUpdate=true
}}};
W.prototype.buildStyles=function V(){var ap=this;
ap.customStyles="";
ap.styleComputations.sort(function(ar,aq){if(ar.priority===null){return 1
}if(aq.priority===null){return -1
}if(ar.priority===null&&aq.priority===null){return 0
}return ar.priority-aq.priority
}).forEach(function(ar){var aq=ar.func.call(ap);
if(angular.isString(aq)){ap.customStyles+="\n"+aq
}})
};
W.prototype.minColumnsToRender=function v(){var ar=this;
var aq=this.getViewportWidth();
var at=0;
var ap=0;
ar.columns.forEach(function(aw,ax){if(ap<aq){ap+=aw.drawnWidth;
at++
}else{var au=0;
for(var av=ax;
av>=ax-at;
av--){au+=ar.columns[av].drawnWidth
}if(au<aq){at++
}}});
return at
};
W.prototype.getBodyHeight=function ak(){var ap=this.getViewportHeight();
return ap
};
W.prototype.getViewportHeight=function o(){var ap=this;
var ar=this.gridHeight-this.headerHeight-this.footerHeight;
var aq=ap.getViewportAdjustment();
ar=ar+aq.height;
return ar
};
W.prototype.getViewportWidth=function A(){var aq=this;
var ap=this.gridWidth;
var ar=aq.getViewportAdjustment();
ap=ap+ar.width;
return ap
};
W.prototype.getHeaderViewportWidth=function T(){var ap=this.getViewportWidth();
return ap
};
W.prototype.registerViewportAdjuster=function M(ap){this.viewportAdjusters.push(ap)
};
W.prototype.removeViewportAdjuster=function M(aq){var ap=this.viewportAdjusters.indexOf(aq);
if(typeof(ap)!=="undefined"&&ap!==undefined){this.viewportAdjusters.splice(ap,1)
}};
W.prototype.getViewportAdjustment=function O(){var ap=this;
var aq={height:0,width:0};
ap.viewportAdjusters.forEach(function(ar){aq=ar.call(this,aq)
});
return aq
};
W.prototype.getVisibleRowCount=function N(){return this.renderContainers.body.visibleRowCache.length
};
W.prototype.getVisibleRows=function h(){return this.renderContainers.body.visibleRowCache
};
W.prototype.getVisibleColumnCount=function s(){return this.renderContainers.body.visibleColumnCache.length
};
W.prototype.searchRows=function m(ap){return R.search(this,ap,this.columns)
};
W.prototype.sortByColumn=function r(ap){return p.sort(this,ap,this.columns)
};
W.prototype.getCellValue=function Z(ar,aq){var ap=this;
if(!ap.cellValueGetterCache[aq.colDef.name]){ap.cellValueGetterCache[aq.colDef.name]=am(ar.getEntityQualifiedColField(aq))
}return ap.cellValueGetterCache[aq.colDef.name](ar)
};
W.prototype.getNextColumnSortPriority=function D(){var ap=this,aq=0;
ap.columns.forEach(function(ar){if(ar.sort&&ar.sort.priority&&ar.sort.priority>aq){aq=ar.sort.priority
}});
return aq+1
};
W.prototype.resetColumnSorting=function L(aq){var ap=this;
ap.columns.forEach(function(ar){if(ar!==aq&&!ar.suppressRemoveSort){ar.sort={}
}})
};
W.prototype.getColumnSorting=function G(){var ap=this;
var aq=[],ar;
ar=ap.columns.slice(0);
ar.sort(p.prioritySort).forEach(function(at){if(at.sort&&typeof(at.sort.direction)!=="undefined"&&at.sort.direction&&(at.sort.direction===i.ASC||at.sort.direction===i.DESC)){aq.push(at)
}});
return aq
};
W.prototype.sortColumn=function X(aq,au,at){var ap=this,ar=null;
if(typeof(aq)==="undefined"||!aq){throw new Error("No column parameter provided")
}if(typeof(au)==="boolean"){at=au
}else{ar=au
}if(!at){ap.resetColumnSorting(aq);
aq.sort.priority=0
}else{aq.sort.priority=ap.getNextColumnSortPriority()
}if(!ar){if(aq.sort.direction&&aq.sort.direction===i.ASC){aq.sort.direction=i.DESC
}else{if(aq.sort.direction&&aq.sort.direction===i.DESC){if(aq.colDef&&aq.suppressRemoveSort){aq.sort.direction=i.ASC
}else{aq.sort.direction=null
}}else{aq.sort.direction=i.ASC
}}}else{aq.sort.direction=ar
}ap.api.core.raise.sortChanged(ap,ap.getColumnSorting());
return f.when(aq)
};
W.prototype.renderingComplete=function(){if(angular.isFunction(this.options.onRegisterApi)){this.options.onRegisterApi(this.api)
}this.api.core.raise.renderingComplete(this.api)
};
W.prototype.createRowHashMap=function I(){var ap=this;
var aq=new z();
aq.grid=ap;
ap.rowHashMap=aq
};
W.prototype.refresh=function ao(at){var ap=this;
var ar=ap.processRowsProcessors(ap.rows).then(function(au){ap.setVisibleRows(au)
});
var aq=ap.processColumnsProcessors(ap.columns).then(function(au){ap.setVisibleColumns(au)
});
return f.all([ar,aq]).then(function(){ap.redrawInPlace(at);
ap.refreshCanvas(true)
})
};
W.prototype.refreshRows=function d(){var ap=this;
return ap.processRowsProcessors(ap.rows).then(function(aq){ap.setVisibleRows(aq);
ap.redrawInPlace();
ap.refreshCanvas(true)
})
};
W.prototype.refreshCanvas=function(at){var ar=this;
if(at){ar.buildStyles()
}var au=f.defer();
var av=[];
for(var aq in ar.renderContainers){if(ar.renderContainers.hasOwnProperty(aq)){var ap=ar.renderContainers[aq];
if(ap.canvasWidth===null||isNaN(ap.canvasWidth)){continue
}if(ap.header||ap.headerCanvas){av.push(ap)
}}}if(av.length>0){aa(function(){var aD=false;
var ay=0;
var az=0;
var aB,aw;
for(aB=0;
aB<av.length;
aB++){aw=av[aB];
if(aw.canvasWidth===null||isNaN(aw.canvasWidth)){continue
}if(aw.header){var aH=aw.headerHeight;
var aC=q.outerElementHeight(aw.header);
aw.headerHeight=parseInt(aC,10);
if(aH!==aC){aD=true
}var aA=q.getBorderSize(aw.header,"top");
var ax=q.getBorderSize(aw.header,"bottom");
var aG=parseInt(aC-aA-ax,10);
aG=aG<0?0:aG;
aw.innerHeaderHeight=aG;
if(aG>ay){ay=aG
}}if(aw.headerCanvas){var aF=aw.headerCanvasHeight;
var aE=q.outerElementHeight(aw.headerCanvas);
aw.headerCanvasHeight=parseInt(aE,10);
if(aF!==aE){aD=true
}if(aE>az){az=aE
}}}for(aB=0;
aB<av.length;
aB++){aw=av[aB];
if(ay>0&&typeof(aw.headerHeight)!=="undefined"&&aw.headerHeight!==null&&aw.headerHeight<ay){aw.explicitHeaderHeight=ay
}if(typeof(aw.headerCanvasHeight)!=="undefined"&&aw.headerCanvasHeight!==null&&az>0&&aw.headerCanvasHeight<az){aw.explicitHeaderCanvasHeight=az
}}if(at&&aD){ar.buildStyles()
}au.resolve()
})
}else{aa(function(){au.resolve()
})
}return au.promise
};
W.prototype.redrawInPlace=function g(at){var aq=this;
for(var ar in aq.renderContainers){var ap=aq.renderContainers[ar];
if(at){ap.adjustRows(ap.prevScrollTop,null);
ap.adjustColumns(ap.prevScrollLeft,null)
}else{ap.adjustRows(null,ap.prevScrolltopPercentage);
ap.adjustColumns(null,ap.prevScrollleftPercentage)
}}};
W.prototype.hasLeftContainerColumns=function(){return this.hasLeftContainer()&&this.renderContainers.left.renderedColumns.length>0
};
W.prototype.hasRightContainerColumns=function(){return this.hasRightContainer()&&this.renderContainers.right.renderedColumns.length>0
};
function z(){}z.prototype={put:function(ap,aq){this[this.grid.options.rowIdentity(ap)]=aq
},get:function(ap){return this[this.grid.options.rowIdentity(ap)]
},remove:function(ap){var aq=this[ap=this.grid.options.rowIdentity(ap)];
delete this[ap];
return aq
}};
return W
}])
})();
(function(){angular.module("ui.grid").factory("GridApi",["$q","$rootScope","gridUtil","uiGridConstants","GridRow","uiGridGridMenuService",function(d,b,h,a,f,g){var c=function c(i){this.grid=i;
this.listeners=[];
this.registerEvent("core","renderingComplete");
this.registerEvent("core","filterChanged");
this.registerMethod("core","setRowInvisible",f.prototype.setRowInvisible);
this.registerMethod("core","clearRowInvisible",f.prototype.clearRowInvisible);
this.registerMethod("core","getVisibleRows",this.grid.getVisibleRows);
this.registerEvent("core","rowsVisibleChanged");
this.registerEvent("core","rowsRendered");
this.registerEvent("core","scrollEvent");
this.registerEvent("core","canvasHeightChanged")
};
c.prototype.suppressEvents=function(k,i){var j=this;
var l=angular.isArray(k)?k:[k];
var m=[];
l.forEach(function(n){m=j.listeners.filter(function(o){return n===o.handler
})
});
m.forEach(function(n){n.dereg()
});
i();
m.forEach(function(n){n.dereg=e(n.eventId,n.handler,j.grid,n._this)
})
};
c.prototype.registerEvent=function(k,j){var i=this;
if(!i[k]){i[k]={}
}var l=i[k];
if(!l.on){l.on={};
l.raise={}
}var m=i.grid.id+k+j;
l.raise[j]=function(){b.$emit.apply(b,[m].concat(Array.prototype.slice.call(arguments)))
};
l.on[j]=function(p,o,s){var r=e(m,o,i.grid,s);
var q={handler:o,dereg:r,eventId:m,scope:p,_this:s};
i.listeners.push(q);
var n=function(){q.dereg();
var t=i.listeners.indexOf(q);
i.listeners.splice(t,1)
};
p.$on("$destroy",function(){n()
});
return n
}
};
function e(k,j,i,l){return b.$on(k,function(n){var m=Array.prototype.slice.call(arguments);
m.splice(0,1);
j.apply(l?l:i.api,m)
})
}c.prototype.registerEventsFromObject=function(j){var i=this;
var k=[];
angular.forEach(j,function(n,m){var l={name:m,events:[]};
angular.forEach(n,function(p,o){l.events.push(o)
});
k.push(l)
});
k.forEach(function(l){l.events.forEach(function(m){i.registerEvent(l.name,m)
})
})
};
c.prototype.registerMethod=function(k,j,i,m){if(!this[k]){this[k]={}
}var l=this[k];
l[j]=h.createBoundedWrapper(m||this.grid,i)
};
c.prototype.registerMethodsFromObject=function(l,k){var i=this;
var j=[];
angular.forEach(l,function(o,n){var m={name:n,methods:[]};
angular.forEach(o,function(q,p){m.methods.push({name:p,fn:q})
});
j.push(m)
});
j.forEach(function(m){m.methods.forEach(function(n){i.registerMethod(m.name,n.name,n.fn,k)
})
})
};
return c
}])
})();
(function(){angular.module("ui.grid").factory("GridColumn",["gridUtil","uiGridConstants","i18nService",function(e,b,a){function d(i,h,g){var f=this;
f.grid=g;
f.uid=h;
f.updateColumnDef(i,true)
}d.prototype.setPropertyOrDefault=function(h,i,f){var g=this;
if(typeof(h[i])!=="undefined"&&h[i]){g[i]=h[i]
}else{if(typeof(g[i])!=="undefined"){g[i]=g[i]
}else{g[i]=f?f:{}
}}};
d.prototype.updateColumnDef=function(l,f){var g=this;
g.colDef=l;
if(l.name===undefined){throw new Error("colDef.name is required for column at index "+g.grid.options.columnDefs.indexOf(l))
}g.displayName=(l.displayName===undefined)?e.readableColumnName(l.name):l.displayName;
var h="Cannot parse column width '"+l.width+"' for column named '"+l.name+"'";
if(e.isNullOrUndefined(g.width)||!angular.isNumber(g.width)){if(e.isNullOrUndefined(l.width)){g.width="*"
}else{if(!angular.isNumber(l.width)){if(e.endsWith(l.width,"%")){var j=l.width.replace(/%/g,"");
var k=parseInt(j,10);
if(isNaN(k)){throw new Error(h)
}g.width=l.width
}else{if(l.width.match(/^(\d+)$/)){g.width=parseInt(l.width.match(/^(\d+)$/)[1],10)
}else{if(l.width.match(/^\*+$/)){g.width=l.width
}else{throw new Error(h)
}}}}else{g.width=l.width
}}}g.minWidth=!l.minWidth?30:l.minWidth;
g.maxWidth=!l.maxWidth?9000:l.maxWidth;
g.field=(l.field===undefined)?l.name:l.field;
if(typeof(g.field)!=="string"){e.logError("Field is not a string, this is likely to break the code, Field is: "+g.field)
}g.name=l.name;
g.displayName=(l.displayName===undefined)?e.readableColumnName(l.name):l.displayName;
g.aggregationType=angular.isDefined(l.aggregationType)?l.aggregationType:null;
g.footerCellTemplate=angular.isDefined(l.footerCellTemplate)?l.footerCellTemplate:null;
g.footerCellClass=l.footerCellClass;
g.cellClass=l.cellClass;
g.headerCellClass=l.headerCellClass;
g.cellFilter=l.cellFilter?l.cellFilter:"";
g.headerCellFilter=l.headerCellFilter?l.headerCellFilter:"";
g.footerCellFilter=l.footerCellFilter?l.footerCellFilter:"";
g.visible=e.isNullOrUndefined(l.visible)||l.visible;
g.headerClass=l.headerClass;
g.enableSorting=typeof(l.enableSorting)!=="undefined"?l.enableSorting:true;
g.sortingAlgorithm=l.sortingAlgorithm;
if(typeof(g.suppressRemoveSort)==="undefined"){g.suppressRemoveSort=typeof(l.suppressRemoveSort)!=="undefined"?l.suppressRemoveSort:false
}g.enableFiltering=typeof(l.enableFiltering)!=="undefined"?l.enableFiltering:true;
g.setPropertyOrDefault(l,"menuItems",[]);
if(f){g.setPropertyOrDefault(l,"sort")
}var i=[];
if(l.filter){i.push(l.filter)
}else{if(g.enableFiltering&&g.grid.options.enableFiltering){i.push({})
}}if(f){g.setPropertyOrDefault(l,"filter");
g.setPropertyOrDefault(l,"filters",i)
}d.prototype.unsort=function(){this.sort={};
g.grid.api.core.raise.sortChanged(g,g.grid.getColumnSorting())
}
};
d.prototype.getColClass=function(g){var f=b.COL_CLASS_PREFIX+this.uid;
return g?"."+f:f
};
d.prototype.getColClassDefinition=function(){return" .grid"+this.grid.id+" "+this.getColClass(true)+" { min-width: "+this.drawnWidth+"px; max-width: "+this.drawnWidth+"px; }"
};
d.prototype.getRenderContainer=function c(){var g=this;
var f=g.renderContainer;
if(f===null||f===""||f===undefined){f="body"
}return g.grid.renderContainers[f]
};
d.prototype.showColumn=function(){this.colDef.visible=true
};
d.prototype.hideColumn=function(){this.colDef.visible=false
};
d.prototype.getAggregationValue=function(){var h=this;
var f=0;
var i=h.grid.getVisibleRows();
var g=function(){var j=[];
angular.forEach(i,function(m){var l=h.grid.getCellValue(m,h);
var k=Number(l);
if(!isNaN(k)){j.push(k)
}});
return j
};
if(angular.isFunction(h.aggregationType)){return h.aggregationType(i,h)
}else{if(h.aggregationType===b.aggregationTypes.count){return h.grid.getVisibleRowCount()
}else{if(h.aggregationType===b.aggregationTypes.sum){angular.forEach(g(),function(j){f+=j
});
return f
}else{if(h.aggregationType===b.aggregationTypes.avg){angular.forEach(g(),function(j){f+=j
});
f=f/g().length;
return f
}else{if(h.aggregationType===b.aggregationTypes.min){return Math.min.apply(null,g())
}else{if(h.aggregationType===b.aggregationTypes.max){return Math.max.apply(null,g())
}else{return"\u00A0"
}}}}}}};
d.prototype.getAggregationText=function(){var f=this;
if(f.colDef.aggregationHideLabel){return""
}else{if(f.colDef.aggregationLabel){return f.colDef.aggregationLabel
}else{switch(f.colDef.aggregationType){case b.aggregationTypes.count:return a.getSafeText("aggregation.count");
case b.aggregationTypes.sum:return a.getSafeText("aggregation.sum");
case b.aggregationTypes.avg:return a.getSafeText("aggregation.avg");
case b.aggregationTypes.min:return a.getSafeText("aggregation.min");
case b.aggregationTypes.max:return a.getSafeText("aggregation.max");
default:return""
}}}};
d.prototype.getCellTemplate=function(){var f=this;
return f.cellTemplatePromise
};
d.prototype.getCompiledElementFn=function(){var f=this;
return f.compiledElementFnDefer.promise
};
return d
}])
})();
(function(){angular.module("ui.grid").factory("GridOptions",["gridUtil","uiGridConstants",function(b,a){return{initialize:function(c){c.onRegisterApi=c.onRegisterApi||angular.noop();
c.data=c.data||[];
c.columnDefs=c.columnDefs||[];
c.excludeProperties=c.excludeProperties||["$$hashKey"];
c.enableRowHashing=c.enableRowHashing!==false;
c.rowIdentity=c.rowIdentity||function e(f){return b.hashKey(f)
};
c.getRowIdentity=c.getRowIdentity||function d(f){return f.$$hashKey
};
c.showHeader=typeof(c.showHeader)!=="undefined"?c.showHeader:true;
if(!c.showHeader){c.headerRowHeight=0
}else{c.headerRowHeight=typeof(c.headerRowHeight)!=="undefined"?c.headerRowHeight:30
}c.rowHeight=c.rowHeight||30;
c.minRowsToShow=typeof(c.minRowsToShow)!=="undefined"?c.minRowsToShow:10;
c.showGridFooter=c.showGridFooter===true;
c.showColumnFooter=c.showColumnFooter===true;
c.columnFooterHeight=typeof(c.columnFooterHeight)!=="undefined"?c.columnFooterHeight:30;
c.gridFooterHeight=typeof(c.gridFooterHeight)!=="undefined"?c.gridFooterHeight:30;
c.columnWidth=typeof(c.columnWidth)!=="undefined"?c.columnWidth:50;
c.maxVisibleColumnCount=typeof(c.maxVisibleColumnCount)!=="undefined"?c.maxVisibleColumnCount:200;
c.virtualizationThreshold=typeof(c.virtualizationThreshold)!=="undefined"?c.virtualizationThreshold:20;
c.columnVirtualizationThreshold=typeof(c.columnVirtualizationThreshold)!=="undefined"?c.columnVirtualizationThreshold:10;
c.excessRows=typeof(c.excessRows)!=="undefined"?c.excessRows:4;
c.scrollThreshold=typeof(c.scrollThreshold)!=="undefined"?c.scrollThreshold:4;
c.excessColumns=typeof(c.excessColumns)!=="undefined"?c.excessColumns:4;
c.horizontalScrollThreshold=typeof(c.horizontalScrollThreshold)!=="undefined"?c.horizontalScrollThreshold:2;
c.scrollThrottle=typeof(c.scrollThrottle)!=="undefined"?c.scrollThrottle:70;
c.enableSorting=c.enableSorting!==false;
c.enableFiltering=c.enableFiltering===true;
c.enableColumnMenus=c.enableColumnMenus!==false;
c.enableVerticalScrollbar=typeof(c.enableVerticalScrollbar)!=="undefined"?c.enableVerticalScrollbar:a.scrollbars.ALWAYS;
c.enableHorizontalScrollbar=typeof(c.enableHorizontalScrollbar)!=="undefined"?c.enableHorizontalScrollbar:a.scrollbars.ALWAYS;
c.minimumColumnSize=typeof(c.minimumColumnSize)!=="undefined"?c.minimumColumnSize:10;
c.rowEquality=c.rowEquality||function(g,f){return g===f
};
c.headerTemplate=c.headerTemplate||null;
c.footerTemplate=c.footerTemplate||null;
c.rowTemplate=c.rowTemplate||"ui-grid/ui-grid-row";
c.appScopeProvider=c.appScopeProvider||null;
return c
}}
}])
})();
(function(){angular.module("ui.grid").factory("GridRenderContainer",["gridUtil","uiGridConstants",function(v,m){function o(A,B,z){var y=this;
y.name=A;
y.grid=B;
y.visibleRowCache=[];
y.visibleColumnCache=[];
y.renderedRows=[];
y.renderedColumns=[];
y.prevScrollTop=0;
y.prevScrolltopPercentage=0;
y.prevRowScrollIndex=0;
y.prevScrollLeft=0;
y.prevScrollleftPercentage=0;
y.prevColumnScrollIndex=0;
y.columnStyles="";
y.viewportAdjusters=[];
y.canvasHeightShouldUpdate=true;
y.$$canvasHeight=0;
if(z&&angular.isObject(z)){angular.extend(y,z)
}B.registerStyleComputation({priority:5,func:function(){return y.columnStyles
}})
}o.prototype.reset=function w(){this.visibleColumnCache.length=0;
this.visibleRowCache.length=0;
this.renderedRows.length=0;
this.renderedColumns.length=0
};
o.prototype.minRowsToRender=function j(){var z=this;
var y=0;
var B=0;
var C=z.getViewportHeight();
for(var A=z.visibleRowCache.length-1;
B<C&&A>=0;
A--){B+=z.visibleRowCache[A].height;
y++
}return y
};
o.prototype.minColumnsToRender=function f(){var B=this;
var A=this.getViewportWidth();
var F=0;
var z=0;
for(var E=0;
E<B.visibleColumnCache.length;
E++){var D=B.visibleColumnCache[E];
if(z<A){z+=D.drawnWidth?D.drawnWidth:0;
F++
}else{var y=0;
for(var C=E;
C>=E-F;
C--){y+=B.visibleColumnCache[C].drawnWidth?B.visibleColumnCache[C].drawnWidth:0
}if(y<A){F++
}}}return F
};
o.prototype.getVisibleRowCount=function e(){return this.visibleRowCache.length
};
o.prototype.registerViewportAdjuster=function n(y){this.viewportAdjusters.push(y)
};
o.prototype.removeViewportAdjuster=function n(z){var y=this.viewportAdjusters.indexOf(z);
if(typeof(y)!=="undefined"&&y!==undefined){this.viewportAdjusters.splice(y,1)
}};
o.prototype.getViewportAdjustment=function a(){var y=this;
var z={height:0,width:0};
y.viewportAdjusters.forEach(function(A){z=A.call(this,z)
});
return z
};
o.prototype.getViewportHeight=function t(){var z=this;
var y=(z.headerHeight)?z.headerHeight:z.grid.headerHeight;
var B=z.grid.gridHeight-y-z.grid.footerHeight;
var A=z.getViewportAdjustment();
B=B+A.height;
return B
};
o.prototype.getViewportWidth=function i(){var z=this;
var y=z.grid.gridWidth;
var A=z.getViewportAdjustment();
y=y+A.width;
return y
};
o.prototype.getHeaderViewportWidth=function s(){var z=this;
var y=this.getViewportWidth();
return y
};
o.prototype.getCanvasHeight=function b(){var y=this;
if(!y.canvasHeightShouldUpdate){return y.$$canvasHeight
}var z=y.$$canvasHeight;
y.$$canvasHeight=0;
y.visibleRowCache.forEach(function(A){y.$$canvasHeight+=A.height
});
y.canvasHeightShouldUpdate=false;
y.grid.api.core.raise.canvasHeightChanged(z,y.$$canvasHeight);
return y.$$canvasHeight
};
o.prototype.getVerticalScrollLength=function k(){return this.getCanvasHeight()-this.getViewportHeight()
};
o.prototype.getCanvasWidth=function p(){var y=this;
var z=y.canvasWidth;
return z
};
o.prototype.setRenderedRows=function r(z){this.renderedRows.length=z.length;
for(var y=0;
y<z.length;
y++){this.renderedRows[y]=z[y]
}};
o.prototype.setRenderedColumns=function g(A){var y=this;
this.renderedColumns.length=A.length;
for(var z=0;
z<A.length;
z++){this.renderedColumns[z]=A[z]
}this.updateColumnOffset()
};
o.prototype.updateColumnOffset=function h(){var y=0;
for(var z=0;
z<this.currentFirstColumn;
z++){y+=this.visibleColumnCache[z].drawnWidth
}this.columnOffset=y
};
o.prototype.adjustScrollVertical=function u(A,y,z){if(this.prevScrollTop===A&&!z){return
}if(typeof(A)==="undefined"||A===undefined||A===null){A=(this.getCanvasHeight()-this.getCanvasWidth())*y
}this.adjustRows(A,y,false);
this.prevScrollTop=A;
this.prevScrolltopPercentage=y;
this.grid.queueRefresh()
};
o.prototype.adjustScrollHorizontal=function x(A,y,z){if(this.prevScrollLeft===A&&!z){return
}if(typeof(A)==="undefined"||A===undefined||A===null){A=(this.getCanvasWidth()-this.getViewportWidth())*y
}this.adjustColumns(A,y);
this.prevScrollLeft=A;
this.prevScrollleftPercentage=y;
this.grid.queueRefresh()
};
o.prototype.adjustRows=function c(B,H,G){var L=this;
var A=L.minRowsToRender();
var C=L.visibleRowCache;
var F=C.length-A;
if((typeof(H)==="undefined"||H===null)&&B){H=B/L.getVerticalScrollLength()
}var I=Math.ceil(Math.min(F,F*H));
if(I>F){I=F
}var J=[];
if(C.length>L.grid.options.virtualizationThreshold){if(!(typeof(B)==="undefined"||B===null)){if(!L.grid.options.enableInfiniteScroll&&L.prevScrollTop<B&&I<L.prevRowScrollIndex+L.grid.options.scrollThreshold&&I<F){return
}if(!L.grid.options.enableInfiniteScroll&&L.prevScrollTop>B&&I>L.prevRowScrollIndex-L.grid.options.scrollThreshold&&I<F){return
}}var K={};
var z={};
if(L.grid.options.enableInfiniteScroll&&L.grid.scrollDirection!==m.scrollDirection.NONE&&G){var y=null;
var E=null;
if(L.grid.scrollDirection===m.scrollDirection.UP){y=I>0?L.grid.options.excessRows:0;
for(E=0;
E<C.length;
E++){if(C[E].entity.$$hashKey===L.renderedRows[y].entity.$$hashKey){I=E;
break
}}K=Math.max(0,I);
z=Math.min(C.length,K+L.grid.options.excessRows+A)
}else{if(L.grid.scrollDirection===m.scrollDirection.DOWN){y=A;
for(E=0;
E<C.length;
E++){if(C[E].entity.$$hashKey===L.renderedRows[y].entity.$$hashKey){I=E;
break
}}K=Math.max(0,I-L.grid.options.excessRows-A);
z=Math.min(C.length,I+A+L.grid.options.excessRows)
}}}else{K=Math.max(0,I-L.grid.options.excessRows);
z=Math.min(C.length,I+A+L.grid.options.excessRows)
}J=[K,z]
}else{var D=L.visibleRowCache.length;
J=[0,Math.max(D,A+L.grid.options.excessRows)]
}L.updateViewableRowRange(J);
L.prevRowScrollIndex=I
};
o.prototype.adjustColumns=function q(B,C){var I=this;
var G=I.minColumnsToRender();
var z=I.visibleColumnCache;
var D=z.length-G;
if((typeof(C)==="undefined"||C===null)&&B){C=B/I.getCanvasWidth()
}var F=Math.ceil(Math.min(D,D*C));
if(F>D){F=D
}var E=[];
if(z.length>I.grid.options.columnVirtualizationThreshold&&I.getCanvasWidth()>I.getViewportWidth()){var H=Math.max(0,F-I.grid.options.excessColumns);
var y=Math.min(z.length,F+G+I.grid.options.excessColumns);
E=[H,y]
}else{var A=I.visibleColumnCache.length;
E=[0,Math.max(A,G+I.grid.options.excessColumns)]
}I.updateViewableColumnRange(E);
I.prevColumnScrollIndex=F
};
o.prototype.updateViewableRowRange=function l(z){var y=this.visibleRowCache.slice(z[0],z[1]);
this.currentTopRow=z[0];
this.setRenderedRows(y)
};
o.prototype.updateViewableColumnRange=function d(z){var y=this.visibleColumnCache.slice(z[0],z[1]);
this.currentFirstColumn=z[0];
this.setRenderedColumns(y)
};
o.prototype.headerCellWrapperStyle=function(){var y=this;
if(y.currentFirstColumn!==0){var z=y.columnOffset;
if(y.grid.isRTL()){return{"margin-right":z+"px"}
}else{return{"margin-left":z+"px"}
}}return null
};
o.prototype.updateColumnWidths=function(){var L=this;
var H=[],C=[],K=[],z=0,G=0;
var A=L.getViewportWidth()-L.grid.scrollbarWidth;
var J;
var N=0;
var Q=0;
var S="";
var E=L.visibleColumnCache;
E.forEach(function(V,T){if(!V.visible){return
}var U,W=false;
if(!angular.isNumber(V.width)){W=isNaN(V.width)&&v.endsWith(V.width,"%")
}if(angular.isString(V.width)&&V.width.indexOf("*")!==-1){z=parseInt(z+V.width.length,10);
H.push(V)
}else{if(W){C.push(V)
}else{if(angular.isNumber(V.width)){N=parseInt(N+V.width,10);
Q=parseInt(Q,10)+parseInt(V.width,10);
V.drawnWidth=V.width
}}}});
var I=A-N;
var O,D,R;
if(C.length>0){for(O=0;
O<C.length;
O++){D=C[O];
var B=parseInt(D.width.replace(/%/g,""),10)/100;
R=parseInt(B*I,10);
if(D.colDef.minWidth&&R<D.colDef.minWidth){R=D.colDef.minWidth;
I=I-R;
Q+=R;
D.drawnWidth=R;
C.splice(O,1)
}else{if(D.colDef.maxWidth&&R>D.colDef.maxWidth){R=D.colDef.maxWidth;
I=I-R;
Q+=R;
D.drawnWidth=R;
C.splice(O,1)
}}}C.forEach(function(U){var V=parseInt(U.width.replace(/%/g,""),10)/100;
var T=parseInt(V*I,10);
Q+=T;
U.drawnWidth=T
})
}if(H.length>0){var M=parseInt(I/z,10);
for(O=0;
O<H.length;
O++){D=H[O];
R=parseInt(M*D.width.length,10);
if(D.colDef.minWidth&&R<D.colDef.minWidth){R=D.colDef.minWidth;
I=I-R;
z--;
Q+=R;
D.drawnWidth=R;
J=D;
H.splice(O,1)
}else{if(D.colDef.maxWidth&&R>D.colDef.maxWidth){R=D.colDef.maxWidth;
I=I-R;
z--;
Q+=R;
D.drawnWidth=R;
H.splice(O,1)
}}}M=parseInt(I/z,10);
H.forEach(function(U){var T=parseInt(M*U.width.length,10);
Q+=T;
U.drawnWidth=T
})
}var P=A-parseInt(Q,10);
if(P>0&&Q>0&&Q<A){var y=false;
E.forEach(function(T){if(T.width&&!angular.isNumber(T.width)){y=true
}});
if(y){var F=function(T){if(P>0){T.drawnWidth=T.drawnWidth+1;
Q=Q+1;
P--
}};
while(P>0){E.forEach(F)
}}}if(Q<A){Q=A
}E.forEach(function(T){S=S+T.getColClassDefinition()
});
L.canvasWidth=parseInt(Q,10);
this.columnStyles=S
};
o.prototype.getViewPortStyle=function(){var y=this;
var z={};
if(y.name==="body"){z["overflow-x"]=y.grid.options.enableHorizontalScrollbar===m.scrollbars.NEVER?"hidden":"scroll";
if(!y.grid.isRTL()){if(y.grid.hasRightContainerColumns()){z["overflow-y"]="hidden"
}else{z["overflow-y"]=y.grid.options.enableVerticalScrollbar===m.scrollbars.NEVER?"hidden":"scroll"
}}else{if(y.grid.hasLeftContainerColumns()){z["overflow-y"]="hidden"
}else{z["overflow-y"]=y.grid.options.enableVerticalScrollbar===m.scrollbars.NEVER?"hidden":"scroll"
}}}else{if(y.name==="left"){z["overflow-x"]="hidden";
z["overflow-y"]=y.grid.isRTL()?(y.grid.options.enableVerticalScrollbar===m.scrollbars.NEVER?"hidden":"scroll"):"hidden"
}else{z["overflow-x"]="hidden";
z["overflow-y"]=!y.grid.isRTL()?(y.grid.options.enableVerticalScrollbar===m.scrollbars.NEVER?"hidden":"scroll"):"hidden"
}}return z
};
return o
}])
})();
(function(){angular.module("ui.grid").factory("GridRow",["gridUtil",function(b){function a(c,d,e){this.grid=e;
this.entity=c;
this.uid=b.nextUid();
this.visible=true;
this.$$height=e.options.rowHeight
}Object.defineProperty(a.prototype,"height",{get:function(){return this.$$height
},set:function(c){if(c!==this.$$height){this.grid.updateCanvasHeight();
this.$$height=c
}}});
a.prototype.getQualifiedColField=function(c){return"row."+this.getEntityQualifiedColField(c)
};
a.prototype.getEntityQualifiedColField=function(c){return b.preEval("entity."+c.field)
};
a.prototype.setRowInvisible=function(c){if(c&&c.setThisRowInvisible){c.setThisRowInvisible("user")
}};
a.prototype.clearRowInvisible=function(c){if(c&&c.clearThisRowInvisible){c.clearThisRowInvisible("user")
}};
a.prototype.setThisRowInvisible=function(d,c){if(!this.invisibleReason){this.invisibleReason={}
}this.invisibleReason[d]=true;
this.evaluateRowVisibility(c)
};
a.prototype.clearThisRowInvisible=function(d,c){delete this.invisibleReason.user;
this.evaluateRowVisibility(c)
};
a.prototype.evaluateRowVisibility=function(c){var d=true;
if(this.invisibleReason){angular.forEach(this.invisibleReason,function(f,e){if(f){d=false
}})
}if(this.visible!==d){this.visible=d;
if(!c){this.grid.queueGridRefresh();
this.grid.api.core.raise.rowsVisibleChanged(this)
}}};
return a
}])
})();
(function(){angular.module("ui.grid").factory("ScrollEvent",["gridUtil",function(b){function a(e,f,c,g){var d=this;
if(!e){throw new Error("grid argument is required")
}d.grid=e;
d.source=g;
d.sourceRowContainer=f;
d.sourceColContainer=c;
d.newScrollLeft=null;
d.newScrollTop=null;
d.x=null;
d.y=null;
d.fireThrottledScrollingEvent=b.throttle(function(){d.grid.api.core.raise.scrollEvent(d)
},d.grid.options.scrollThrottle,{trailing:true})
}a.prototype.fireScrollingEvent=function(){this.grid.api.core.raise.scrollEvent(this)
};
a.prototype.getNewScrollLeft=function(g,c){var d=this;
if(!d.newScrollLeft){var e=(g.getCanvasWidth()-g.getViewportWidth());
var h=b.normalizeScrollLeft(c);
var f;
if(typeof(d.x.percentage)!=="undefined"&&d.x.percentage!==undefined){f=d.x.percentage
}else{if(typeof(d.x.pixels)!=="undefined"&&d.x.pixels!==undefined){f=d.x.percentage=(h+d.x.pixels)/e
}else{throw new Error("No percentage or pixel value provided for scroll event X axis")
}}return Math.max(0,f*e)
}return d.newScrollLeft
};
a.prototype.getNewScrollTop=function(f,d){var e=this;
if(!e.newScrollTop){var c=f.getVerticalScrollLength();
var g=d[0].scrollTop;
var h;
if(typeof(e.y.percentage)!=="undefined"&&e.y.percentage!==undefined){h=e.y.percentage
}else{if(typeof(e.y.pixels)!=="undefined"&&e.y.pixels!==undefined){h=e.y.percentage=(g+e.y.pixels)/c
}else{throw new Error("No percentage or pixel value provided for scroll event Y axis")
}}return Math.max(0,h*c)
}return e.newScrollTop
};
a.Sources={ViewPortScroll:"ViewPortScroll",RenderContainerMouseWheel:"RenderContainerMouseWheel",RenderContainerTouchMove:"RenderContainerTouchMove",Other:99};
return a
}])
})();
(function(){angular.module("ui.grid").service("gridClassFactory",["gridUtil","$q","$compile","$templateCache","uiGridConstants","Grid","GridColumn","GridRow",function(i,g,e,b,h,c,d,a){var f={createGrid:function(k){k=(typeof(k)!=="undefined")?k:{};
k.id=i.newId();
var l=new c(k);
if(l.options.rowTemplate){var o=g.defer();
l.getRowTemplateFn=o.promise;
i.getTemplate(l.options.rowTemplate).then(function(p){var q=e(p);
o.resolve(q)
},function(p){throw new Error("Couldn't fetch/use row template '"+l.options.rowTemplate+"'")
})
}l.registerColumnBuilder(f.defaultColumnBuilder);
l.registerRowBuilder(f.rowTemplateAssigner);
l.registerRowsProcessor(function n(p){p.forEach(function(q){q.visible=!q.forceInvisible
});
return p
});
l.registerColumnsProcessor(function m(p){p.forEach(function(q){q.visible=true
});
return p
});
l.registerColumnsProcessor(function(p){p.forEach(function(q){if(q.colDef.visible===false){q.visible=false
}});
return p
});
if(l.options.enableFiltering){l.registerRowsProcessor(l.searchRows)
}if(l.options.externalSort&&angular.isFunction(l.options.externalSort)){l.registerRowsProcessor(l.options.externalSort)
}else{l.registerRowsProcessor(l.sortByColumn)
}return l
},defaultColumnBuilder:function(m,k,n){var l=[];
if(!m.headerCellTemplate){k.providedHeaderCellTemplate="ui-grid/uiGridHeaderCell"
}else{k.providedHeaderCellTemplate=m.headerCellTemplate
}if(!m.cellTemplate){k.providedCellTemplate="ui-grid/uiGridCell"
}else{k.providedCellTemplate=m.cellTemplate
}if(!m.footerCellTemplate){k.providedFooterCellTemplate="ui-grid/uiGridFooterCell"
}else{k.providedFooterCellTemplate=m.footerCellTemplate
}k.cellTemplatePromise=i.getTemplate(k.providedCellTemplate);
l.push(k.cellTemplatePromise.then(function(o){k.cellTemplate=o.replace(h.CUSTOM_FILTERS,k.cellFilter?"|"+k.cellFilter:"")
},function(o){throw new Error("Couldn't fetch/use colDef.cellTemplate '"+m.cellTemplate+"'")
}));
l.push(i.getTemplate(k.providedHeaderCellTemplate).then(function(o){k.headerCellTemplate=o.replace(h.CUSTOM_FILTERS,k.headerCellFilter?"|"+k.headerCellFilter:"")
},function(o){throw new Error("Couldn't fetch/use colDef.headerCellTemplate '"+m.headerCellTemplate+"'")
}));
l.push(i.getTemplate(k.providedFooterCellTemplate).then(function(o){k.footerCellTemplate=o.replace(h.CUSTOM_FILTERS,k.footerCellFilter?"|"+k.footerCellFilter:"")
},function(o){throw new Error("Couldn't fetch/use colDef.footerCellTemplate '"+m.footerCellTemplate+"'")
}));
k.compiledElementFnDefer=g.defer();
return g.all(l)
},rowTemplateAssigner:function j(m){var k=this;
if(!m.rowTemplate){m.rowTemplate=k.options.rowTemplate;
m.getRowTemplateFn=k.getRowTemplateFn
}else{var l=g.defer();
m.getRowTemplateFn=l.promise;
i.getTemplate(m.rowTemplate).then(function(n){var o=e(n);
l.resolve(o)
},function(n){throw new Error("Couldn't fetch/use row template '"+m.rowTemplate+"'")
})
}return m.getRowTemplateFn
}};
return f
}])
})();
(function(){var b=angular.module("ui.grid");
function a(c){return c.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g,"\\$&")
}b.service("rowSearcher",["gridUtil","uiGridConstants",function(k,i){var l=i.filter.STARTS_WITH;
var h={};
h.getTerm=function d(o){if(typeof(o.term)==="undefined"){return o.term
}var n=o.term;
if(typeof(n)==="string"){n=n.trim()
}return n
};
h.stripTerm=function j(o){var n=h.getTerm(o);
if(typeof(n)==="string"){return a(n.replace(/(^\*|\*$)/g,""))
}else{return n
}};
h.guessCondition=function c(p){if(typeof(p.term)==="undefined"||!p.term){return l
}var n=h.getTerm(p);
if(/\*/.test(n)){var q="";
if(!p.flags||!p.flags.caseSensitive){q+="i"
}var o=n.replace(/(\\)?\*/g,function(s,r){return r?s:"[\\s\\S]*?"
});
return new RegExp("^"+o+"$",q)
}else{return l
}};
h.setupFilters=function e(s){var n=[];
var t=s.length;
for(var o=0;
o<t;
o++){var p=s[o];
if(p.noTerm||p.term){var r={};
var q="";
if(!p.flags||!p.flags.caseSensitive){q+="i"
}if(p.term){r.term=h.stripTerm(p)
}if(p.condition){r.condition=p.condition
}else{r.condition=h.guessCondition(p)
}r.flags=angular.extend({caseSensitive:false,date:false},p.flags);
if(r.condition===i.filter.STARTS_WITH){r.startswithRE=new RegExp("^"+r.term,q)
}if(r.condition===i.filter.ENDS_WITH){r.endswithRE=new RegExp(r.term+"$",q)
}if(r.condition===i.filter.CONTAINS){r.containsRE=new RegExp(r.term,q)
}if(r.condition===i.filter.EXACT){r.exactRE=new RegExp("^"+r.term+"$",q)
}n.push(r)
}}return n
};
h.runColumnFilter=function f(n,v,p,o){var r=typeof(o.condition);
var q=o.term;
var u=n.getCellValue(v,p);
if(o.condition instanceof RegExp){return o.condition.test(u)
}if(r==="function"){return o.condition(q,u,v,p)
}if(o.startswithRE){return o.startswithRE.test(u)
}if(o.endswithRE){return o.endswithRE.test(u)
}if(o.containsRE){return o.containsRE.test(u)
}if(o.exactRE){return o.exactRE.test(u)
}if(o.condition===i.filter.NOT_EQUAL){var t=new RegExp("^"+q+"$");
return !t.exec(u)
}if(typeof(u)==="number"){var s=parseFloat(q.replace(/\\\./,".").replace(/\\\-/,"-"));
if(!isNaN(s)){q=s
}}if(o.flags.date===true){u=new Date(u);
q=new Date(q.replace(/\\/g,""))
}if(o.condition===i.filter.GREATER_THAN){return(u>q)
}if(o.condition===i.filter.GREATER_THAN_OR_EQUAL){return(u>=q)
}if(o.condition===i.filter.LESS_THAN){return(u<q)
}if(o.condition===i.filter.LESS_THAN_OR_EQUAL){return(u<=q)
}return true
};
h.searchColumn=function g(p,u,r,s){if(p.options.useExternalFiltering){return true
}var t=s.length;
for(var o=0;
o<t;
o++){var q=s[o];
var n=h.runColumnFilter(p,u,r,q);
if(!n){return false
}}return true
};
h.search=function m(n,x,q){if(!x){return
}var v=[];
var w=q.length;
for(var t=0;
t<w;
t++){var o=q[t];
if(typeof(o.filters)!=="undefined"&&(o.filters.length>1||o.filters.length===1&&(typeof(o.filters[0].term)!=="undefined"&&o.filters[0].term||o.filters[0].noTerm))){v.push({col:o,filters:h.setupFilters(o.filters)})
}else{if(typeof(o.filter)!=="undefined"&&o.filter&&(typeof(o.filter.term)!=="undefined"&&o.filter.term||o.filter.noTerm)){v.push({col:o,filters:h.setupFilters([o.filter])})
}}}if(v.length>0){var p=function(z,B,y,A){if(!h.searchColumn(z,B,y,A)){B.setThisRowInvisible("filtered",true)
}};
var u=function(z,B){var A=x.length;
for(var y=0;
y<A;
y++){p(z,x[y],B.col,B.filters)
}};
var r=v.length;
for(var s=0;
s<r;
s++){u(n,v[s])
}if(n.api.core.raise.rowsVisibleChanged){n.api.core.raise.rowsVisibleChanged()
}}return x
};
return h
}])
})();
(function(){var a=angular.module("ui.grid");
a.service("rowSorter",["$parse","uiGridConstants",function(g,l){var h="("+l.CURRENCY_SYMBOLS.map(function(q){return"\\"+q
}).join("|")+")?";
var i=new RegExp("^[-+]?"+h+"[\\d,.]+"+h+"%?$");
var d={colSortFnCache:[]};
d.guessSortFn=function m(q){switch(q){case"number":return d.sortNumber;
case"boolean":return d.sortBool;
case"string":return d.sortAlpha;
case"date":return d.sortDate;
case"object":return d.basicSort;
default:throw new Error("No sorting function found for type:"+q)
}};
d.handleNulls=function j(r,q){if((!r&&r!==0&&r!==false)||(!q&&q!==0&&q!==false)){if((!r&&r!==0&&r!==false)&&(!q&&q!==0&&q!==false)){return 0
}else{if(!r&&r!==0&&r!==false){return 1
}else{if(!q&&q!==0&&q!==false){return -1
}}}}return null
};
d.basicSort=function n(r,q){var s=d.handleNulls(r,q);
if(s!==null){return s
}else{if(r===q){return 0
}if(r<q){return -1
}return 1
}};
d.sortNumber=function e(r,q){var s=d.handleNulls(r,q);
if(s!==null){return s
}else{return r-q
}};
d.sortNumberStr=function c(s,q){var w=d.handleNulls(s,q);
if(w!==null){return w
}else{var v,u,t=false,r=false;
v=parseFloat(s.replace(/[^0-9.-]/g,""));
if(isNaN(v)){t=true
}u=parseFloat(q.replace(/[^0-9.-]/g,""));
if(isNaN(u)){r=true
}if(t&&r){return 0
}if(t){return 1
}if(r){return -1
}return v-u
}};
d.sortAlpha=function f(s,q){var u=d.handleNulls(s,q);
if(u!==null){return u
}else{var t=s.toString().toLowerCase(),r=q.toString().toLowerCase();
return t===r?0:(t<r?-1:1)
}};
d.sortDate=function p(r,q){var u=d.handleNulls(r,q);
if(u!==null){return u
}else{var t=r.getTime(),s=q.getTime();
return t===s?0:(t<s?-1:1)
}};
d.sortBool=function o(r,q){var s=d.handleNulls(r,q);
if(s!==null){return s
}else{if(r&&q){return 0
}if(!r&&!q){return 0
}else{return r?1:-1
}}};
d.getSortFn=function k(r,q,u){var t,s;
if(d.colSortFnCache[q.colDef.name]){t=d.colSortFnCache[q.colDef.name]
}else{if(q.sortingAlgorithm!==undefined){t=q.sortingAlgorithm;
d.colSortFnCache[q.colDef.name]=q.sortingAlgorithm
}else{t=d.guessSortFn(q.colDef.type);
if(t){d.colSortFnCache[q.colDef.name]=t
}else{t=d.sortAlpha
}}}return t
};
d.prioritySort=function(r,q){if(r.sort.priority!==undefined&&q.sort.priority!==undefined){if(r.sort.priority<q.sort.priority){return -1
}else{if(r.sort.priority===q.sort.priority){return 0
}else{return 1
}}}else{if(r.sort.priority||r.sort.priority===0){return -1
}else{if(q.sort.priority||q.sort.priority===0){return 1
}else{return 0
}}}};
d.sort=function b(s,z,v){if(!z){return
}if(s.options.useExternalSorting){return z
}var w=[];
v.forEach(function(r){if(r.sort&&r.sort.direction&&(r.sort.direction===l.ASC||r.sort.direction===l.DESC)){w.push(r)
}});
w=w.sort(d.prioritySort);
if(w.length===0){return z
}var u,y;
var q=z.slice(0);
angular.forEach(z,function(A,r){A.entity.$uiGridIndex=r
});
var x=z.sort(function t(D,B){var C=0,A=0,E;
while(C===0&&A<w.length){u=w[A];
y=w[A].sort.direction;
E=d.getSortFn(s,u,q);
var r=s.getCellValue(D,u);
var F=s.getCellValue(B,u);
C=E(r,F);
A++
}if(C===0){return D.entity.$uiGridIndex-B.entity.$uiGridIndex
}if(y===l.ASC){return C
}else{return 0-C
}});
angular.forEach(x,function(A,r){delete A.entity.$uiGridIndex
});
return x
};
return d
}])
})();
(function(){var b=angular.module("ui.grid");
var d;
if(typeof Function.prototype.bind!=="function"){d=function(){var l=Array.prototype.slice;
return function(n){var o=this,m=l.call(arguments,1);
if(m.length){return function(){return arguments.length?o.apply(n,m.concat(l.call(arguments))):o.apply(n,m)
}
}return function(){return arguments.length?o.apply(n,arguments):o.call(n)
}
}
}
}function g(l){var m=l;
if(typeof(m.length)!=="undefined"&&m.length){m=l[0]
}return m.ownerDocument.defaultView.getComputedStyle(m,null)
}var k=new RegExp("^("+(/[+-]?(?:\d*\.|)\d+(?:[eE][+-]?\d+|)/).source+")(?!px)[a-z%]+$","i"),i=/^(block|none|table(?!-c[ea]).+)/,a={position:"absolute",visibility:"hidden",display:"block"};
function c(o,l,p,x,y){var r=p===(x?"border":"content")?4:l==="width"?1:0,m=0;
var n=["Top","Right","Bottom","Left"];
for(;
r<4;
r+=2){var v=n[r];
if(p==="margin"){var s=parseFloat(y[p+v]);
if(!isNaN(s)){m+=s
}}if(x){if(p==="content"){var w=parseFloat(y["padding"+v]);
if(!isNaN(w)){m-=w
}}if(p!=="margin"){var u=parseFloat(y["border"+v+"Width"]);
if(!isNaN(u)){m-=u
}}}else{var t=parseFloat(y["padding"+v]);
if(!isNaN(t)){m+=t
}if(p!=="padding"){var q=parseFloat(y["border"+v+"Width"]);
if(!isNaN(q)){m+=q
}}}}return m
}function e(q,n,l){var p=true,r,o=g(q),s=o.boxSizing==="border-box";
if(r<=0||r==null){r=o[n];
if(r<0||r==null){r=q.style[n]
}if(k.test(r)){return r
}p=s&&(true||r===q.style[n]);
r=parseFloat(r)||0
}var m=(r+c(q,n,l||(s?"border":"content"),p,o));
return m
}function f(m){m=angular.element(m)[0];
var l=m.offsetParent;
if(!l){l=document.getElementsByTagName("body")[0]
}return parseInt(g(l).fontSize)||parseInt(g(m).fontSize)||16
}var h=["0","0","0"];
var j="uiGrid-";
b.service("gridUtil",["$log","$window","$document","$http","$templateCache","$timeout","$injector","$q","$interpolate","uiGridConstants",function(z,x,I,J,q,H,D,G,u,t){var w={getStyles:g,createBoundedWrapper:function(s,K){return function(){return K.apply(s,arguments)
}
},readableColumnName:function(s){if(typeof(s)==="undefined"||s===undefined||s===null){return s
}if(typeof(s)!=="string"){s=String(s)
}return s.replace(/_+/g," ").replace(/^[A-Z]+$/,function(K){return angular.lowercase(angular.uppercase(K.charAt(0))+K.slice(1))
}).replace(/([\w\u00C0-\u017F]+)/g,function(K){return angular.uppercase(K.charAt(0))+K.slice(1)
}).replace(/(\w+?(?=[A-Z]))/g,"$1 ")
},getColumnsFromData:function(L,K){var M=[];
if(!L||typeof(L[0])==="undefined"||L[0]===undefined){return[]
}if(angular.isUndefined(K)){K=[]
}var s=L[0];
angular.forEach(s,function(O,N){if(K.indexOf(N)===-1){M.push({name:N})
}});
return M
},newId:(function(){var s=new Date().getTime();
return function(){return s+=1
}
})(),getTemplate:function(s){if(q.get(s)){return w.postProcessTemplate(q.get(s))
}if(s.hasOwnProperty("then")){return s.then(w.postProcessTemplate)
}try{if(angular.element(s).length>0){return G.when(s).then(w.postProcessTemplate)
}}catch(K){}w.logDebug("fetching url",s);
return J({method:"GET",url:s}).then(function(L){var M=L.data.trim();
q.put(s,M);
return M
},function(L){throw new Error("Could not get template "+s+": "+L)
}).then(w.postProcessTemplate)
},postProcessTemplate:function(s){var L=u.startSymbol(),K=u.endSymbol();
if(L!=="{{"||K!=="}}"){s=s.replace(/\{\{/g,L);
s=s.replace(/\}\}/g,K)
}return G.when(s)
},guessType:function(s){var K=typeof(s);
switch(K){case"number":case"boolean":case"string":return K;
default:if(angular.isDate(s)){return"date"
}return"object"
}},elementWidth:function(s){},elementHeight:function(s){},getScrollbarWidth:function(){var M=document.createElement("div");
M.style.visibility="hidden";
M.style.width="100px";
M.style.msOverflowStyle="scrollbar";
document.body.appendChild(M);
var K=M.offsetWidth;
M.style.overflow="scroll";
var s=document.createElement("div");
s.style.width="100%";
M.appendChild(s);
var L=s.offsetWidth;
M.parentNode.removeChild(M);
return K-L
},swap:function(O,N,P,M){var L,K,s={};
for(K in N){s[K]=O.style[K];
O.style[K]=N[K]
}L=P.apply(O,M||[]);
for(K in N){O.style[K]=s[K]
}return L
},fakeElement:function(N,M,P,L){var K,s,O=angular.element(N).clone()[0];
for(s in M){O.style[s]=M[s]
}angular.element(document.body).append(O);
K=P.call(O,O);
angular.element(O).remove();
return K
},normalizeWheelEvent:function(K){var L,s;
var M=K||window.event,R=[].slice.call(arguments,1),T=0,O=0,N=0,Q=0,P=0,S;
if(M.originalEvent){M=M.originalEvent
}if(M.wheelDelta){T=M.wheelDelta
}if(M.detail){T=M.detail*-1
}N=T;
if(M.axis!==undefined&&M.axis===M.HORIZONTAL_AXIS){N=0;
O=T*-1
}if(M.deltaY){N=M.deltaY*-1;
T=N
}if(M.deltaX){O=M.deltaX;
T=O*-1
}if(M.wheelDeltaY!==undefined){N=M.wheelDeltaY
}if(M.wheelDeltaX!==undefined){O=M.wheelDeltaX
}Q=Math.abs(T);
if(!L||Q<L){L=Q
}P=Math.max(Math.abs(N),Math.abs(O));
if(!s||P<s){s=P
}S=T>0?"floor":"ceil";
T=Math[S](T/L);
O=Math[S](O/s);
N=Math[S](N/s);
return{delta:T,deltaX:O,deltaY:N}
},isTouchEnabled:function(){var s;
if(("ontouchstart" in x)||x.DocumentTouch&&I instanceof DocumentTouch){s=true
}return s
},isNullOrUndefined:function(s){if(s===undefined||s===null){return true
}return false
},endsWith:function(K,s){if(!K||!s||typeof K!=="string"){return false
}return K.indexOf(s,K.length-s.length)!==-1
},arrayContainsObjectWithProperty:function(M,K,s){var L=false;
angular.forEach(M,function(N){if(N[K]===s){L=true
}});
return L
},requestAnimationFrame:x.requestAnimationFrame&&x.requestAnimationFrame.bind(x)||x.webkitRequestAnimationFrame&&x.webkitRequestAnimationFrame.bind(x)||function(s){return H(s,10,false)
},numericAndNullSort:function(K,s){if(K===null){return 1
}if(s===null){return -1
}if(K===null&&s===null){return 0
}return K-s
},disableAnimations:function(s){var L;
try{L=D.get("$animate");
L.enabled(false,s)
}catch(K){}},enableAnimations:function(s){var L;
try{L=D.get("$animate");
L.enabled(true,s);
return L
}catch(K){}},nextUid:function B(){var s=h.length;
var K;
while(s){s--;
K=h[s].charCodeAt(0);
if(K===57){h[s]="A";
return j+h.join("")
}if(K===90){h[s]="0"
}else{h[s]=String.fromCharCode(K+1);
return j+h.join("")
}}h.unshift("0");
return j+h.join("")
},hashKey:function r(K){var L=typeof K,s;
if(L==="object"&&K!==null){if(typeof(s=K.$$hashKey)==="function"){s=K.$$hashKey()
}else{if(typeof(K.$$hashKey)!=="undefined"&&K.$$hashKey){s=K.$$hashKey
}else{if(s===undefined){s=K.$$hashKey=w.nextUid()
}}}}else{s=K
}return L+":"+s
},resetUids:function(){h=["0","0","0"]
},logError:function(s){if(t.LOG_ERROR_MESSAGES){z.error(s)
}},logWarn:function(s){if(t.LOG_WARN_MESSAGES){z.warn(s)
}},logDebug:function(){if(t.LOG_DEBUG_MESSAGES){z.debug.apply(z,arguments)
}}};
["width","height"].forEach(function(K){var s=angular.uppercase(K.charAt(0))+K.substr(1);
w["element"+s]=function(N,L){var O=N;
if(O&&typeof(O.length)!=="undefined"&&O.length){O=N[0]
}if(O){var M=g(O);
return O.offsetWidth===0&&i.test(M.display)?w.fakeElement(O,a,function(P){return e(P,K,L)
}):e(O,K,L)
}else{return null
}};
w["outerElement"+s]=function(L,M){return L?w["element"+s].call(this,L,M?"margin":"border"):null
}
});
w.closestElm=function m(L,s){if(typeof(L.length)!=="undefined"&&L.length){L=L[0]
}var M;
["matches","webkitMatchesSelector","mozMatchesSelector","msMatchesSelector","oMatchesSelector"].some(function(N){if(typeof document.body[N]==="function"){M=N;
return true
}return false
});
var K;
while(L!==null){K=L.parentElement;
if(K!==null&&K[M](s)){return K
}L=K
}return null
};
w.type=function(s){var K=Function.prototype.toString.call(s.constructor);
return K.match(/function (.*?)\(/)[1]
};
w.getBorderSize=function C(L,s){if(typeof(L.length)!=="undefined"&&L.length){L=L[0]
}var K=g(L);
if(s){s="border"+s.charAt(0).toUpperCase()+s.slice(1)
}else{s="border"
}s+="Width";
var M=parseInt(K[s],10);
if(isNaN(M)){return 0
}else{return M
}};
w.detectBrowser=function A(){var L=x.navigator.userAgent;
var s={chrome:/chrome/i,safari:/safari/i,firefox:/firefox/i,ie:/internet explorer|trident\//i};
for(var K in s){if(s[K].test(L)){return K
}}return"unknown"
};
w.normalizeScrollLeft=function n(L){if(typeof(L.length)!=="undefined"&&L.length){L=L[0]
}var K=w.detectBrowser();
var N=L.scrollLeft;
var s=w.getStyles(L)["direction"];
if(K==="ie"){return N
}else{if(K==="chrome"){if(s==="rtl"){var M=L.scrollWidth-L.clientWidth;
return M-N
}else{return N
}}else{if(K==="firefox"){return Math.abs(N)
}else{return N
}}}};
w.denormalizeScrollLeft=function v(L,N){if(typeof(L.length)!=="undefined"&&L.length){L=L[0]
}var K=w.detectBrowser();
var s=w.getStyles(L)["direction"];
if(K==="ie"){return N
}else{if(K==="chrome"){if(s==="rtl"){var M=L.scrollWidth-L.clientWidth;
return M-N
}else{return N
}}else{if(K==="firefox"){if(s==="rtl"){return N*-1
}else{return N
}}else{return N
}}}};
w.preEval=function(M){var s=t.BRACKET_REGEXP.exec(M);
if(s){return(s[1]?w.preEval(s[1]):s[1])+s[2]+(s[3]?w.preEval(s[3]):s[3])
}else{M=M.replace(t.APOS_REGEXP,"\\'");
var L=M.split(t.DOT_REGEXP);
var K=[L.shift()];
angular.forEach(L,function(N){K.push(N.replace(t.FUNC_REGEXP,"']$1"))
});
return K.join("['")
}};
w.debounce=function(O,Q,L){var P,M,N,s;
function K(){N=this;
M=arguments;
var S=function(){P=null;
if(!L){s=O.apply(N,M)
}};
var R=L&&!P;
if(P){H.cancel(P)
}P=H(S,Q);
if(R){s=O.apply(N,M)
}return s
}K.cancel=function(){H.cancel(P);
P=null
};
return K
};
w.throttle=function(O,P,L){L=L||{};
var N=0,Q=null,M,K;
function s(R){N=+new Date();
O.apply(M,K);
H(function(){Q=null
},0)
}return function(){M=this;
K=arguments;
if(Q===null){var R=+new Date()-N;
if(R>P){s()
}else{if(L.trailing){Q=H(s,P-R)
}}}}
};
w.on={};
w.off={};
w._events={};
w.addOff=function(s){w.off[s]=function(M,L){var K=w._events[s].indexOf(L);
if(K>0){w._events[s].removeAt(K)
}}
};
var E=("onwheel" in document||document.documentMode>=9)?["wheel"]:["mousewheel","DomMouseScroll","MozMousePixelScroll"],o,p;
w.on.mousewheel=function(N,M){if(!N||!M){return
}var s=angular.element(N);
s.data("mousewheel-line-height",f(s));
s.data("mousewheel-page-height",w.elementHeight(s));
if(!s.data("mousewheel-callbacks")){s.data("mousewheel-callbacks",{})
}var K=s.data("mousewheel-callbacks");
K[M]=(Function.prototype.bind||d).call(l,s[0],M);
for(var L=E.length;
L;
){s.on(E[--L],K[M])
}};
w.off.mousewheel=function(O,N){var s=angular.element(this);
var K=s.data("mousewheel-callbacks");
var M=K[N];
if(M){for(var L=E.length;
L;
){s.off(E[--L],M)
}}delete K[N];
if(Object.keys(K).length===0){s.removeData("mousewheel-line-height");
s.removeData("mousewheel-page-height");
s.removeData("mousewheel-callbacks")
}};
function l(S,s){var K=angular.element(this);
var U=0,O=0,M=0,R=0,Q=0,P=0;
if(s.originalEvent){s=s.originalEvent
}if("detail" in s){M=s.detail*-1
}if("wheelDelta" in s){M=s.wheelDelta
}if("wheelDeltaY" in s){M=s.wheelDeltaY
}if("wheelDeltaX" in s){O=s.wheelDeltaX*-1
}if("axis" in s&&s.axis===s.HORIZONTAL_AXIS){O=M*-1;
M=0
}U=M===0?O:M;
if("deltaY" in s){M=s.deltaY*-1;
U=M
}if("deltaX" in s){O=s.deltaX;
if(M===0){U=O*-1
}}if(M===0&&O===0){return
}if(s.deltaMode===1){var T=K.data("mousewheel-line-height");
U*=T;
M*=T;
O*=T
}else{if(s.deltaMode===2){var L=K.data("mousewheel-page-height");
U*=L;
M*=L;
O*=L
}}R=Math.max(Math.abs(M),Math.abs(O));
if(!p||R<p){p=R;
if(y(s,R)){p/=40
}}U=Math[U>=1?"floor":"ceil"](U/p);
O=Math[O>=1?"floor":"ceil"](O/p);
M=Math[M>=1?"floor":"ceil"](M/p);
s.deltaMode=0;
var N={originalEvent:s,deltaX:O,deltaY:M,deltaFactor:p,preventDefault:function(){s.preventDefault()
}};
if(o){clearTimeout(o)
}o=setTimeout(F,200);
S.call(K[0],N)
}function F(){p=null
}function y(K,s){return K.type==="mousewheel"&&s%120===0
}return w
}]);
b.filter("px",function(){return function(l){if(l.match(/^[\d\.]+$/)){return l+"px"
}else{return l
}}
})
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("da",{aggregate:{label:"artikler"},groupPanel:{description:"Grupér rækker udfra en kolonne ved at trække dens overskift hertil."},search:{placeholder:"Søg...",showingItems:"Viste rækker:",selectedItems:"Valgte rækker:",totalItems:"Rækker totalt:",size:"Side størrelse:",first:"Første side",next:"Næste side",previous:"Forrige side",last:"Sidste side"},menu:{text:"Vælg kolonner:"},column:{hide:"Skjul kolonne"},aggregation:{count:"samlede rækker: ",sum:"smalede: ",avg:"gns: ",min:"min: ",max:"max: "},gridMenu:{columns:"Columns:",importerTitle:"Import file",exporterAllAsCsv:"Export all data as csv",exporterVisibleAsCsv:"Export visible data as csv",exporterSelectedAsCsv:"Export selected data as csv",exporterAllAsPdf:"Export all data as pdf",exporterVisibleAsPdf:"Export visible data as pdf",exporterSelectedAsPdf:"Export selected data as pdf"},importer:{noHeaders:"Column names were unable to be derived, does the file have a header?",noObjects:"Objects were not able to be derived, was there data in the file other than headers?",invalidCsv:"File was unable to be processed, is it valid CSV?",invalidJson:"File was unable to be processed, is it valid Json?",jsonNotArray:"Imported json file must contain an array, aborting."}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("de",{aggregate:{label:"Eintrag"},groupPanel:{description:"Ziehen Sie eine Spaltenüberschrift hierhin, um nach dieser Spalte zu gruppieren."},search:{placeholder:"Suche...",showingItems:"Zeige Einträge:",selectedItems:"Ausgewählte Einträge:",totalItems:"Einträge gesamt:",size:"Einträge pro Seite:",first:"Erste Seite",next:"Nächste Seite",previous:"Vorherige Seite",last:"Letzte Seite"},menu:{text:"Spalten auswählen:"},sort:{ascending:"aufsteigend sortieren",descending:"absteigend sortieren",remove:"Sortierung zurücksetzen"},column:{hide:"Spalte ausblenden"},aggregation:{count:"Zeilen insgesamt: ",sum:"gesamt: ",avg:"Durchschnitt: ",min:"min: ",max:"max: "},pinning:{pinLeft:"Links anheften",pinRight:"Rechts anheften",unpin:"Lösen"},gridMenu:{columns:"Spalten:",importerTitle:"Datei importieren",exporterAllAsCsv:"Alle Daten als CSV exportieren",exporterVisibleAsCsv:"sichtbare Daten als CSV exportieren",exporterSelectedAsCsv:"markierte Daten als CSV exportieren",exporterAllAsPdf:"Alle Daten als PDF exportieren",exporterVisibleAsPdf:"sichtbare Daten als PDF exportieren",exporterSelectedAsPdf:"markierte Daten als CSV exportieren"},importer:{noHeaders:"Es konnten keine Spaltennamen ermittelt werden. Sind in der Datei Spaltendefinitionen enthalten?",noObjects:"Es konnten keine Zeileninformationen gelesen werden, Sind in der Datei außer den Spaltendefinitionen auch Daten enthalten?",invalidCsv:"Die Datei konnte nicht eingelesen werden, ist es eine gültige CSV-Datei?",invalidJson:"Die Datei konnte nicht eingelesen werden. Enthält sie gültiges JSON?",jsonNotArray:"Die importierte JSON-Datei muß ein Array enthalten. Breche Import ab."},pagination:{sizes:"Einträge pro Seite",totalItems:"Einträge"}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("en",{aggregate:{label:"items"},groupPanel:{description:"Drag a column header here and drop it to group by that column."},search:{placeholder:"Search...",showingItems:"Showing Items:",selectedItems:"Selected Items:",totalItems:"Total Items:",size:"Page Size:",first:"First Page",next:"Next Page",previous:"Previous Page",last:"Last Page"},menu:{text:"Choose Columns:"},sort:{ascending:"Sort Ascending",descending:"Sort Descending",remove:"Remove Sort"},column:{hide:"Hide Column"},aggregation:{count:"total rows: ",sum:"total: ",avg:"avg: ",min:"min: ",max:"max: "},pinning:{pinLeft:"Pin Left",pinRight:"Pin Right",unpin:"Unpin"},gridMenu:{columns:"Columns:",importerTitle:"Import file",exporterAllAsCsv:"Export all data as csv",exporterVisibleAsCsv:"Export visible data as csv",exporterSelectedAsCsv:"Export selected data as csv",exporterAllAsPdf:"Export all data as pdf",exporterVisibleAsPdf:"Export visible data as pdf",exporterSelectedAsPdf:"Export selected data as pdf"},importer:{noHeaders:"Column names were unable to be derived, does the file have a header?",noObjects:"Objects were not able to be derived, was there data in the file other than headers?",invalidCsv:"File was unable to be processed, is it valid CSV?",invalidJson:"File was unable to be processed, is it valid Json?",jsonNotArray:"Imported json file must contain an array, aborting."},pagination:{sizes:"items per page",totalItems:"items"},grouping:{group:"Group",ungroup:"Ungroup",aggregate_count:"Agg: Count",aggregate_sum:"Agg: Sum",aggregate_max:"Agg: Max",aggregate_min:"Agg: Min",aggregate_avg:"Agg: Avg",aggregate_remove:"Agg: Remove"}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("es",{aggregate:{label:"Artículos"},groupPanel:{description:"Arrastre un encabezado de columna aquí y suéltelo para agrupar por esa columna."},search:{placeholder:"Buscar...",showingItems:"Artículos Mostrados:",selectedItems:"Artículos Seleccionados:",totalItems:"Artículos Totales:",size:"Tamaño de Página:",first:"Primera Página",next:"Página Siguiente",previous:"Página Anterior",last:"Última Página"},menu:{text:"Elegir columnas:"},sort:{ascending:"Orden Ascendente",descending:"Orden Descendente",remove:"Sin Ordenar"},column:{hide:"Ocultar la columna"},aggregation:{count:"filas totales: ",sum:"total: ",avg:"media: ",min:"min: ",max:"max: "},pinning:{pinLeft:"Fijar a la Izquierda",pinRight:"Fijar a la Derecha",unpin:"Quitar Fijación"},gridMenu:{columns:"Columnas:",importerTitle:"Importar archivo",exporterAllAsCsv:"Exportar todo como csv",exporterVisibleAsCsv:"Exportar vista como csv",exporterSelectedAsCsv:"Exportar selección como csv",exporterAllAsPdf:"Exportar todo como pdf",exporterVisibleAsPdf:"Exportar vista como pdf",exporterSelectedAsPdf:"Exportar selección como pdf"},importer:{noHeaders:"No fue posible derivar los nombres de las columnas, ¿tiene encabezados el archivo?",noObjects:"No fue posible obtener registros, ¿contiene datos el archivo, aparte de los encabezados?",invalidCsv:"No fue posible procesar el archivo, ¿es un CSV válido?",invalidJson:"No fue posible procesar el archivo, ¿es un Json válido?",jsonNotArray:"El archivo json importado debe contener un array, abortando."}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("fa",{aggregate:{label:"موردها"},groupPanel:{description:"یک عنوان ستون اینجا را بردار و به گروهی از آن ستون بیانداز."},search:{placeholder:"جستجو...",showingItems:"نمایش موردها:",selectedItems:"موردهای انتخاب\u200cشده:",totalItems:"همهٔ موردها:",size:"اندازهٔ صفحه:",first:"صفحهٔ اول",next:"صفحهٔ بعد",previous:"صفحهٔ قبل",last:"آخرین صفحه"},menu:{text:"انتخاب ستون\u200cها:"},column:{hide:"ستون پنهان کن"},aggregation:{count:"total rows: ",sum:"total: ",avg:"avg: ",min:"min: ",max:"max: "},gridMenu:{columns:"Columns:",importerTitle:"Import file",exporterAllAsCsv:"Export all data as csv",exporterVisibleAsCsv:"Export visible data as csv",exporterSelectedAsCsv:"Export selected data as csv",exporterAllAsPdf:"Export all data as pdf",exporterVisibleAsPdf:"Export visible data as pdf",exporterSelectedAsPdf:"Export selected data as pdf"},importer:{noHeaders:"Column names were unable to be derived, does the file have a header?",noObjects:"Objects were not able to be derived, was there data in the file other than headers?",invalidCsv:"File was unable to be processed, is it valid CSV?",invalidJson:"File was unable to be processed, is it valid Json?",jsonNotArray:"Imported json file must contain an array, aborting."}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("fi",{aggregate:{label:"rivit"},groupPanel:{description:"Raahaa ja pudota otsikko tähän ryhmittääksesi sarakkeen mukaan."},search:{placeholder:"Hae...",showingItems:"Näytetään rivejä:",selectedItems:"Valitut rivit:",totalItems:"Rivejä yht.:",size:"Näytä:",first:"Ensimmäinen sivu",next:"Seuraava sivu",previous:"Edellinen sivu",last:"Viimeinen sivu"},menu:{text:"Valitse sarakkeet:"},sort:{ascending:"Järjestä nouseva",descending:"Järjestä laskeva",remove:"Poista järjestys"},column:{hide:"Piilota sarake"},aggregation:{count:"Rivejä yht.: ",sum:"Summa: ",avg:"K.a.: ",min:"Min: ",max:"Max: "},pinning:{pinLeft:"Lukitse vasemmalle",pinRight:"Lukitse oikealle",unpin:"Poista lukitus"},gridMenu:{columns:"Sarakkeet:",importerTitle:"Tuo tiedosto",exporterAllAsCsv:"Vie tiedot csv-muodossa",exporterVisibleAsCsv:"Vie näkyvä tieto csv-muodossa",exporterSelectedAsCsv:"Vie valittu tieto csv-muodossa",exporterAllAsPdf:"Vie tiedot pdf-muodossa",exporterVisibleAsPdf:"Vie näkyvä tieto pdf-muodossa",exporterSelectedAsPdf:"Vie valittu tieto pdf-muodossa"},importer:{noHeaders:"Sarakkeen nimiä ei voitu päätellä, onko tiedostossa otsikkoriviä?",noObjects:"Tietoja ei voitu lukea, onko tiedostossa muuta kuin otsikkot?",invalidCsv:"Tiedostoa ei voitu käsitellä, oliko se CSV-muodossa?",invalidJson:"Tiedostoa ei voitu käsitellä, oliko se JSON-muodossa?",jsonNotArray:"Tiedosto ei sisältänyt taulukkoa, lopetetaan."}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("fr",{aggregate:{label:"articles"},groupPanel:{description:"Faites glisser un en-tête de colonne ici et déposez-le vers un groupe par cette colonne."},search:{placeholder:"Recherche...",showingItems:"Articles Affichage des:",selectedItems:"Éléments Articles:",totalItems:"Nombre total d'articles:",size:"Taille de page:",first:"Première page",next:"Page Suivante",previous:"Page précédente",last:"Dernière page"},menu:{text:"Choisir des colonnes:"},sort:{ascending:"Trier par ordre croissant",descending:"Trier par ordre décroissant",remove:"Enlever le tri"},column:{hide:"Cacher la colonne"},aggregation:{count:"total lignes: ",sum:"total: ",avg:"moy: ",min:"min: ",max:"max: "},pinning:{pinLeft:"Épingler à gauche",pinRight:"Épingler à droite",unpin:"Détacher"},gridMenu:{columns:"Colonnes:",importerTitle:"Importer un fichier",exporterAllAsCsv:"Exporter toutes les données en CSV",exporterVisibleAsCsv:"Exporter les données visibles en CSV",exporterSelectedAsCsv:"Exporter les données sélectionnées en CSV",exporterAllAsPdf:"Exporter toutes les données en PDF",exporterVisibleAsPdf:"Exporter les données visibles en PDF",exporterSelectedAsPdf:"Exporter les données sélectionnées en PDF"},importer:{noHeaders:"Impossible de déterminer le nom des colonnes, le fichier possède-t-il un en-tête ?",noObjects:"Aucun objet trouvé, le fichier possède-t-il des données autres que l'en-tête ?",invalidCsv:"Le fichier n'a pas pu être traité, le CSV est-il valide ?",invalidJson:"Le fichier n'a pas pu être traité, le JSON est-il valide ?",jsonNotArray:"Le fichier JSON importé doit contenir un tableau. Abandon."},pagination:{sizes:"articles par page",totalItems:"articles"}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("he",{aggregate:{label:"items"},groupPanel:{description:"גרור עמודה לכאן ושחרר בכדי לקבץ עמודה זו."},search:{placeholder:"חפש...",showingItems:"מציג:",selectedItems:'סה"כ נבחרו:',totalItems:'סה"כ רשומות:',size:"תוצאות בדף:",first:"דף ראשון",next:"דף הבא",previous:"דף קודם",last:"דף אחרון"},menu:{text:"בחר עמודות:"},sort:{ascending:"סדר עולה",descending:"סדר יורד",remove:"בטל"},column:{hide:"טור הסתר"},aggregation:{count:"total rows: ",sum:"total: ",avg:"avg: ",min:"min: ",max:"max: "},gridMenu:{columns:"Columns:",importerTitle:"Import file",exporterAllAsCsv:"Export all data as csv",exporterVisibleAsCsv:"Export visible data as csv",exporterSelectedAsCsv:"Export selected data as csv",exporterAllAsPdf:"Export all data as pdf",exporterVisibleAsPdf:"Export visible data as pdf",exporterSelectedAsPdf:"Export selected data as pdf"},importer:{noHeaders:"Column names were unable to be derived, does the file have a header?",noObjects:"Objects were not able to be derived, was there data in the file other than headers?",invalidCsv:"File was unable to be processed, is it valid CSV?",invalidJson:"File was unable to be processed, is it valid Json?",jsonNotArray:"Imported json file must contain an array, aborting."}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("hy",{aggregate:{label:"տվյալներ"},groupPanel:{description:"Ըստ սյան խմբավորելու համար քաշեք և գցեք վերնագիրն այստեղ։"},search:{placeholder:"Փնտրում...",showingItems:"Ցուցադրված տվյալներ՝",selectedItems:"Ընտրված:",totalItems:"Ընդամենը՝",size:"Տողերի քանակը էջում՝",first:"Առաջին էջ",next:"Հաջորդ էջ",previous:"Նախորդ էջ",last:"Վերջին էջ"},menu:{text:"Ընտրել սյուները:"},sort:{ascending:"Աճման կարգով",descending:"Նվազման կարգով",remove:"Հանել "},column:{hide:"Թաքցնել սյունը"},aggregation:{count:"ընդամենը տող՝ ",sum:"ընդամենը՝ ",avg:"միջին՝ ",min:"մին՝ ",max:"մաքս՝ "},pinning:{pinLeft:"Կպցնել ձախ կողմում",pinRight:"Կպցնել աջ կողմում",unpin:"Արձակել"},gridMenu:{columns:"Սյուներ:",importerTitle:"Ներմուծել ֆայլ",exporterAllAsCsv:"Արտահանել ամբողջը CSV",exporterVisibleAsCsv:"Արտահանել երևացող տվյալները CSV",exporterSelectedAsCsv:"Արտահանել ընտրված տվյալները CSV",exporterAllAsPdf:"Արտահանել PDF",exporterVisibleAsPdf:"Արտահանել երևացող տվյալները PDF",exporterSelectedAsPdf:"Արտահանել ընտրված տվյալները PDF"},importer:{noHeaders:"Հնարավոր չեղավ որոշել սյան վերնագրերը։ Արդյո՞ք ֆայլը ունի վերնագրեր։",noObjects:"Հնարավոր չեղավ կարդալ տվյալները։ Արդյո՞ք ֆայլում կան տվյալներ։",invalidCsv:"Հնարավոր չեղավ մշակել ֆայլը։ Արդյո՞ք այն վավեր CSV է։",invalidJson:"Հնարավոր չեղավ մշակել ֆայլը։ Արդյո՞ք այն վավեր Json է։",jsonNotArray:"Ներմուծված json ֆայլը պետք է պարունակի զանգված, կասեցվում է։"}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("it",{aggregate:{label:"elementi"},groupPanel:{description:"Trascina un'intestazione all'interno del gruppo della colonna."},search:{placeholder:"Ricerca...",showingItems:"Mostra:",selectedItems:"Selezionati:",totalItems:"Totali:",size:"Tot Pagine:",first:"Prima",next:"Prossima",previous:"Precedente",last:"Ultima"},menu:{text:"Scegli le colonne:"},sort:{ascending:"Asc.",descending:"Desc.",remove:"Annulla ordinamento"},column:{hide:"Nascondi"},aggregation:{count:"righe totali: ",sum:"tot: ",avg:"media: ",min:"minimo: ",max:"massimo: "},pinning:{pinLeft:"Blocca a sx",pinRight:"Blocca a dx",unpin:"Blocca in alto"},gridMenu:{columns:"Colonne:",importerTitle:"Importa",exporterAllAsCsv:"Esporta tutti i dati in CSV",exporterVisibleAsCsv:"Esporta i dati visibili in CSV",exporterSelectedAsCsv:"Esporta i dati selezionati in CSV",exporterAllAsPdf:"Esporta tutti i dati in PDF",exporterVisibleAsPdf:"Esporta i dati visibili in PDF",exporterSelectedAsPdf:"Esporta i dati selezionati in PDF"},importer:{noHeaders:"Impossibile reperire i nomi delle colonne, sicuro che siano indicati all'interno del file?",noObjects:"Impossibile reperire gli oggetti, sicuro che siano indicati all'interno del file?",invalidCsv:"Impossibile elaborare il file, sicuro che sia un CSV?",invalidJson:"Impossibile elaborare il file, sicuro che sia un JSON valido?",jsonNotArray:"Errore! Il file JSON da importare deve contenere un array."}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("ja",{aggregate:{label:"件"},groupPanel:{description:"列名部分をここにドラッグアンドドロップすることで列ごとにグループ分けを行うことができます。"},search:{placeholder:"検索...",showingItems:"絞込み件数:",selectedItems:"選択件数:",totalItems:"全件数:",size:"ページサイズ: ",first:"最初のページ",next:"次のページ",previous:"前のページ",last:"最後のページ"},menu:{text:"列選択:"},sort:{ascending:"昇順ソート",descending:"降順ソート",remove:"ソート取消"},column:{hide:"列を隠す"},aggregation:{count:"合計件数: ",sum:"合計: ",avg:"平均: ",min:"最小値: ",max:"最大値: "},pinning:{pinLeft:"左にピン留め",pinRight:"右にピン留め",unpin:"ピン留め取消"},gridMenu:{columns:"列:",importerTitle:"インポートファイル",exporterAllAsCsv:"全てのデータをCSV形式でエクスポート",exporterVisibleAsCsv:"絞込み済みデータをCSV形式でエクスポート",exporterSelectedAsCsv:"選択しているデータをCSV形式でエクスポート",exporterAllAsPdf:"全てのデータをPDFでエクスポート",exporterVisibleAsPdf:"絞込み済みデータをPDFでエクスポート",exporterSelectedAsPdf:"選択しているデータをPDFでエクスポート"},importer:{noHeaders:"列名が抽出できません。ヘッダーは設定されていますか？",noObjects:"データが抽出できません。ファイルにデータは含まれていますか？",invalidCsv:"処理を行うことができません。ファイルは有効なCSVファイルですか？",invalidJson:"処理を行うことができません。ファイルは有効なJSONファイルですか？",jsonNotArray:"JSONファイルは配列を含んでいる必要があります。処理を中断します。"},pagination:{sizes:"件 / ページ",totalItems:"件"}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("ko",{aggregate:{label:"아이템"},groupPanel:{description:"컬럼으로 그룹핑하기 위해서는 컬럼 헤더를 끌어 떨어뜨려 주세요."},search:{placeholder:"검색...",showingItems:"항목 보여주기:",selectedItems:"선택 항목:",totalItems:"전체 항목:",size:"페이지 크기:",first:"첫번째 페이지",next:"다음 페이지",previous:"이전 페이지",last:"마지막 페이지"},menu:{text:"컬럼을 선택하세요:"},sort:{ascending:"오름차순 정렬",descending:"내림차순 정렬",remove:"소팅 제거"},column:{hide:"컬럼 제거"},aggregation:{count:"전체 갯수: ",sum:"전체: ",avg:"평균: ",min:"최소: ",max:"최대: "},pinning:{pinLeft:"왼쪽 핀",pinRight:"오른쪽 핀",unpin:"핀 제거"},gridMenu:{columns:"컬럼:",importerTitle:"파일 가져오기",exporterAllAsCsv:"csv로 모든 데이터 내보내기",exporterVisibleAsCsv:"csv로 보이는 데이터 내보내기",exporterSelectedAsCsv:"csv로 선택된 데이터 내보내기",exporterAllAsPdf:"pdf로 모든 데이터 내보내기",exporterVisibleAsPdf:"pdf로 보이는 데이터 내보내기",exporterSelectedAsPdf:"pdf로 선택 데이터 내보내기"},importer:{noHeaders:"컬럼명이 지정되어 있지 않습니다. 파일에 헤더가 명시되어 있는지 확인해 주세요.",noObjects:"데이터가 지정되어 있지 않습니다. 데이터가 파일에 있는지 확인해 주세요.",invalidCsv:"파일을 처리할 수 없습니다. 올바른 csv인지 확인해 주세요.",invalidJson:"파일을 처리할 수 없습니다. 올바른 json인지 확인해 주세요.",jsonNotArray:"json 파일은 배열을 포함해야 합니다."},pagination:{sizes:"페이지당 항목",totalItems:"전체 항목"}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("nl",{aggregate:{label:"items"},groupPanel:{description:"Sleep hier een kolomnaam heen om op te groeperen."},search:{placeholder:"Zoeken...",showingItems:"Getoonde items:",selectedItems:"Geselecteerde items:",totalItems:"Totaal aantal items:",size:"Items per pagina:",first:"Eerste pagina",next:"Volgende pagina",previous:"Vorige pagina",last:"Laatste pagina"},menu:{text:"Kies kolommen:"},sort:{ascending:"Sorteer oplopend",descending:"Sorteer aflopend",remove:"Verwijder sortering"},column:{hide:"Verberg kolom"},aggregation:{count:"Aantal rijen: ",sum:"Som: ",avg:"Gemiddelde: ",min:"Min: ",max:"Max: "},pinning:{pinLeft:"Zet links vast",pinRight:"Zet rechts vast",unpin:"Maak los"},gridMenu:{columns:"Kolommen:",importerTitle:"Importeer bestand",exporterAllAsCsv:"Exporteer alle data als csv",exporterVisibleAsCsv:"Exporteer zichtbare data als csv",exporterSelectedAsCsv:"Exporteer geselecteerde data als csv",exporterAllAsPdf:"Exporteer alle data als pdf",exporterVisibleAsPdf:"Exporteer zichtbare data als pdf",exporterSelectedAsPdf:"Exporteer geselecteerde data als pdf"},importer:{noHeaders:"Kolomnamen kunnen niet worden afgeleid. Heeft het bestand een header?",noObjects:"Objecten kunnen niet worden afgeleid. Bevat het bestand data naast de headers?",invalidCsv:"Het bestand kan niet verwerkt worden. Is het een valide csv bestand?",invalidJson:"Het bestand kan niet verwerkt worden. Is het valide json?",jsonNotArray:"Het json bestand moet een array bevatten. De actie wordt geannuleerd."}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("pt-br",{aggregate:{label:"itens"},groupPanel:{description:"Arraste e solte uma coluna aqui para agrupar por essa coluna"},search:{placeholder:"Procurar...",showingItems:"Mostrando os Itens:",selectedItems:"Items Selecionados:",totalItems:"Total de Itens:",size:"Tamanho da Página:",first:"Primeira Página",next:"Próxima Página",previous:"Página Anterior",last:"Última Página"},menu:{text:"Selecione as colunas:"},sort:{ascending:"Ordenar Ascendente",descending:"Ordenar Descendente",remove:"Remover Ordenação"},column:{hide:"Esconder coluna"},aggregation:{count:"total de linhas: ",sum:"total: ",avg:"med: ",min:"min: ",max:"max: "},pinning:{pinLeft:"Fixar Esquerda",pinRight:"Fixar Direita",unpin:"Desprender"},gridMenu:{columns:"Colunas:",exporterAllAsCsv:"Exportar todos os dados como csv",exporterVisibleAsCsv:"Exportar dados visíveis como csv",exporterSelectedAsCsv:"Exportar dados selecionados como csv",exporterAllAsPdf:"Exportar todos os dados como pdf",exporterVisibleAsPdf:"Exportar dados visíveis como pdf",exporterSelectedAsPdf:"Exportar dados selecionados como pdf"},importer:{noHeaders:"Nomes de colunas não puderam ser derivados. O arquivo tem um cabeçalho?",noObjects:"Objetos não puderam ser derivados. Havia dados no arquivo, além dos cabeçalhos?",invalidCsv:"Arquivo não pode ser processado. É um CSV válido?",invalidJson:"Arquivo não pode ser processado. É um Json válido?",jsonNotArray:"Arquivo json importado tem que conter um array. Abortando."}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("ru",{aggregate:{label:"элементы"},groupPanel:{description:"Для группировки по столбцу перетащите сюда его название."},search:{placeholder:"Поиск...",showingItems:"Показать элементы:",selectedItems:"Выбранные элементы:",totalItems:"Всего элементов:",size:"Размер страницы:",first:"Первая страница",next:"Следующая страница",previous:"Предыдущая страница",last:"Последняя страница"},menu:{text:"Выбрать столбцы:"},sort:{ascending:"По возрастанию",descending:"По убыванию",remove:"Убрать сортировку"},column:{hide:"спрятать столбец"},aggregation:{count:"всего строк: ",sum:"итого: ",avg:"среднее: ",min:"мин: ",max:"макс: "},gridMenu:{columns:"Столбцы:",importerTitle:"Import file",exporterAllAsCsv:"Экспортировать всё в CSV",exporterVisibleAsCsv:"Экспортировать видимые данные в CSV",exporterSelectedAsCsv:"Экспортировать выбранные данные в CSV",exporterAllAsPdf:"Экспортировать всё в PDF",exporterVisibleAsPdf:"Экспортировать видимые данные в PDF",exporterSelectedAsPdf:"Экспортировать выбранные данные в PDF"},importer:{noHeaders:"Column names were unable to be derived, does the file have a header?",noObjects:"Objects were not able to be derived, was there data in the file other than headers?",invalidCsv:"File was unable to be processed, is it valid CSV?",invalidJson:"File was unable to be processed, is it valid Json?",jsonNotArray:"Imported json file must contain an array, aborting."}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("sk",{aggregate:{label:"items"},groupPanel:{description:"Pretiahni sem názov stĺpca pre zoskupenie podľa toho stĺpca."},search:{placeholder:"Hľadaj...",showingItems:"Zobrazujem položky:",selectedItems:"Vybraté položky:",totalItems:"Počet položiek:",size:"Počet:",first:"Prvá strana",next:"Ďalšia strana",previous:"Predchádzajúca strana",last:"Posledná strana"},menu:{text:"Vyberte stĺpce:"},sort:{ascending:"Zotriediť vzostupne",descending:"Zotriediť zostupne",remove:"Vymazať triedenie"},aggregation:{count:"total rows: ",sum:"total: ",avg:"avg: ",min:"min: ",max:"max: "},gridMenu:{columns:"Columns:",importerTitle:"Import file",exporterAllAsCsv:"Export all data as csv",exporterVisibleAsCsv:"Export visible data as csv",exporterSelectedAsCsv:"Export selected data as csv",exporterAllAsPdf:"Export all data as pdf",exporterVisibleAsPdf:"Export visible data as pdf",exporterSelectedAsPdf:"Export selected data as pdf"},importer:{noHeaders:"Column names were unable to be derived, does the file have a header?",noObjects:"Objects were not able to be derived, was there data in the file other than headers?",invalidCsv:"File was unable to be processed, is it valid CSV?",invalidJson:"File was unable to be processed, is it valid Json?",jsonNotArray:"Imported json file must contain an array, aborting."}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("sv",{aggregate:{label:"Artiklar"},groupPanel:{description:"Dra en kolumnrubrik hit och släpp den för att gruppera efter den kolumnen."},search:{placeholder:"Sök...",showingItems:"Visar artiklar:",selectedItems:"Valda artiklar:",totalItems:"Antal artiklar:",size:"Sidstorlek:",first:"Första sidan",next:"Nästa sida",previous:"Föregående sida",last:"Sista sidan"},menu:{text:"Välj kolumner:"},sort:{ascending:"Sortera stigande",descending:"Sortera fallande",remove:"Inaktivera sortering"},column:{hide:"Göm kolumn"},aggregation:{count:"Antal rader: ",sum:"Summa: ",avg:"Genomsnitt: ",min:"Min: ",max:"Max: "},pinning:{pinLeft:"Fäst vänster",pinRight:"Fäst höger",unpin:"Lösgör"},gridMenu:{columns:"Kolumner:",importerTitle:"Importera fil",exporterAllAsCsv:"Exportera all data som CSV",exporterVisibleAsCsv:"Exportera synlig data som CSV",exporterSelectedAsCsv:"Exportera markerad data som CSV",exporterAllAsPdf:"Exportera all data som PDF",exporterVisibleAsPdf:"Exportera synlig data som PDF",exporterSelectedAsPdf:"Exportera markerad data som PDF"},importer:{noHeaders:"Kolumnnamn kunde inte härledas. Har filen ett sidhuvud?",noObjects:"Objekt kunde inte härledas. Har filen data undantaget sidhuvud?",invalidCsv:"Filen kunde inte behandlas, är den en giltig CSV?",invalidJson:"Filen kunde inte behandlas, är den en giltig JSON?",jsonNotArray:"Importerad JSON-fil måste innehålla ett fält. Import avbruten."},pagination:{sizes:"Artiklar per sida",totalItems:"Artiklar"}});
return b
}])
}])
})();
(function(){var f=["uiT","uiTranslate"];
var a=["t","uiTranslate"];
var d=angular.module("ui.grid.i18n");
d.constant("i18nConstants",{MISSING:"[MISSING]",UPDATE_EVENT:"$uiI18n",LOCALE_DIRECTIVE_ALIAS:"uiI18n",DEFAULT_LANG:"en"});
d.service("i18nService",["$log","i18nConstants","$rootScope",function(j,i,h){var k={_langs:{},current:null,get:function(l){return this._langs[l.toLowerCase()]
},add:function(n,l){var m=n.toLowerCase();
if(!this._langs[m]){this._langs[m]={}
}angular.extend(this._langs[m],l)
},getAllLangs:function(){var m=[];
if(!this._langs){return m
}for(var l in this._langs){m.push(l)
}return m
},setCurrent:function(l){this.current=l.toLowerCase()
},getCurrentLang:function(){return this.current
}};
var g={add:function(m,l){if(typeof(m)==="object"){angular.forEach(m,function(n){if(n){k.add(n,l)
}})
}else{k.add(m,l)
}},getAllLangs:function(){return k.getAllLangs()
},get:function(l){var m=l?l:g.getCurrentLang();
return k.get(m)
},getSafeText:function(o,q){var r=q?q:g.getCurrentLang();
var m=k.get(r);
if(!m){return i.MISSING
}var p=o.split(".");
var n=m;
for(var l=0;
l<p.length;
++l){if(n[p[l]]===undefined||n[p[l]]===null){return i.MISSING
}else{n=n[p[l]]
}}return n
},setCurrentLang:function(l){if(l){k.setCurrent(l);
h.$broadcast(i.UPDATE_EVENT)
}},getCurrentLang:function(){var l=k.getCurrentLang();
if(!l){l=i.DEFAULT_LANG;
k.setCurrent(l)
}return l
}};
return g
}]);
var e=function(g,h){return{compile:function(){return{pre:function(k,j,i){var l=h.LOCALE_DIRECTIVE_ALIAS;
var m=k.$eval(i[l]);
if(m){k.$watch(i[l],function(){g.setCurrentLang(m)
})
}else{if(i.$$observers){i.$observe(l,function(){g.setCurrentLang(i[l]||h.DEFAULT_LANG)
})
}}}}
}}
};
d.directive("uiI18n",["i18nService","i18nConstants",e]);
var b=function(i,g,h){return{restrict:"EA",compile:function(){return{pre:function(s,l,q){var t=f[0],r=f[1];
var m=q[t]||q[r]||l.html();
var n=h.MISSING+m;
var o;
if(q.$$observers){var j=q[t]?t:r;
o=q.$observe(j,function(u){if(u){l.html(i(u)(g.getCurrentLang())||n)
}})
}var p=i(m);
var k=s.$on(h.UPDATE_EVENT,function(u){if(o){o(q[t]||q[r])
}else{l.html(p(g.get())||n)
}});
s.$on("$destroy",k);
l.html(p(g.get())||n)
}}
}}
};
angular.forEach(f,function(g){d.directive(g,["$parse","i18nService","i18nConstants",b])
});
var c=function(i,g,h){return function(k){var j=i(k);
return j(g.get())||h.MISSING+k
}
};
angular.forEach(a,function(g){d.filter(g,["$parse","i18nService","i18nConstants",c])
})
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("zh-cn",{aggregate:{label:"行"},groupPanel:{description:"拖曳表头到此处进行分组"},search:{placeholder:"查找",showingItems:"已显示行数：",selectedItems:"已选择行数：",totalItems:"总行数：",size:"每页显示行数：",first:"首页",next:"下一页",previous:"上一页",last:"末页"},menu:{text:"选择列："},sort:{ascending:"升序",descending:"降序",remove:"取消排序"},column:{hide:"隐藏列"},aggregation:{count:"计数：",sum:"求和：",avg:"均值：",min:"最小值：",max:"最大值："},pinning:{pinLeft:"左侧固定",pinRight:"右侧固定",unpin:"取消固定"},gridMenu:{columns:"列：",importerTitle:"导入文件",exporterAllAsCsv:"导出全部数据到CSV",exporterVisibleAsCsv:"导出可见数据到CSV",exporterSelectedAsCsv:"导出已选数据到CSV",exporterAllAsPdf:"导出全部数据到PDF",exporterVisibleAsPdf:"导出可见数据到PDF",exporterSelectedAsPdf:"导出已选数据到PDF"},importer:{noHeaders:"无法获取列名，确定文件包含表头？",noObjects:"无法获取数据，确定文件包含数据？",invalidCsv:"无法处理文件，确定是合法的CSV文件？",invalidJson:"无法处理文件，确定是合法的JSON文件？",jsonNotArray:"导入的文件不是JSON数组！"},pagination:{sizes:"行每页",totalItems:"行"}});
return b
}])
}])
})();
(function(){angular.module("ui.grid").config(["$provide",function(a){a.decorator("i18nService",["$delegate",function(b){b.add("zh-tw",{aggregate:{label:"行"},groupPanel:{description:"拖曳表頭到此處進行分組"},search:{placeholder:"查找",showingItems:"已顯示行數：",selectedItems:"已選擇行數：",totalItems:"總行數：",size:"每頁顯示行數：",first:"首頁",next:"下壹頁",previous:"上壹頁",last:"末頁"},menu:{text:"選擇列："},sort:{ascending:"升序",descending:"降序",remove:"取消排序"},column:{hide:"隱藏列"},aggregation:{count:"計數：",sum:"求和：",avg:"均值：",min:"最小值：",max:"最大值："},pinning:{pinLeft:"左側固定",pinRight:"右側固定",unpin:"取消固定"},gridMenu:{columns:"列：",importerTitle:"導入文件",exporterAllAsCsv:"導出全部數據到CSV",exporterVisibleAsCsv:"導出可見數據到CSV",exporterSelectedAsCsv:"導出已選數據到CSV",exporterAllAsPdf:"導出全部數據到PDF",exporterVisibleAsPdf:"導出可見數據到PDF",exporterSelectedAsPdf:"導出已選數據到PDF"},importer:{noHeaders:"無法獲取列名，確定文件包含表頭？",noObjects:"無法獲取數據，確定文件包含數據？",invalidCsv:"無法處理文件，確定是合法的CSV文件？",invalidJson:"無法處理文件，確定是合法的JSON文件？",jsonNotArray:"導入的文件不是JSON數組！"},pagination:{sizes:"行每頁",totalItems:"行"}});
return b
}])
}])
})();
(function(){var a=angular.module("ui.grid.autoResize",["ui.grid"]);
a.directive("uiGridAutoResize",["$timeout","gridUtil",function(b,c){return{require:"uiGrid",scope:false,link:function(l,f,j,h){var e,i;
function g(){i=c.elementHeight(f);
e=c.elementWidth(f)
}g();
var k;
function d(){clearTimeout(k);
k=setTimeout(function(){var m=c.elementHeight(f);
var n=c.elementWidth(f);
if(m!==i||n!==e){h.grid.gridHeight=m;
h.grid.gridWidth=n;
l.$apply(function(){h.grid.refresh().then(function(){g();
d()
})
})
}else{d()
}},250)
}d();
l.$on("$destroy",function(){clearTimeout(k)
})
}}
}])
})();
(function(){var a=angular.module("ui.grid.cellNav",["ui.grid"]);
function b(d,c){this.row=d;
this.col=c
}a.constant("uiGridCellNavConstants",{FEATURE_NAME:"gridCellNav",CELL_NAV_EVENT:"cellNav",direction:{LEFT:0,RIGHT:1,UP:2,DOWN:3,PG_UP:4,PG_DOWN:5},EVENT_TYPE:{KEYDOWN:0,CLICK:1,CLEAR:2}});
a.factory("uiGridCellNavFactory",["gridUtil","uiGridConstants","uiGridCellNavConstants","$q",function(g,c,e,d){var f=function f(h,j,k,i){this.rows=h.visibleRowCache;
this.columns=j.visibleColumnCache;
this.leftColumns=k?k.visibleColumnCache:[];
this.rightColumns=i?i.visibleColumnCache:[];
this.bodyContainer=h
};
f.prototype.getFocusableCols=function(){var h=this.leftColumns.concat(this.columns,this.rightColumns);
return h.filter(function(i){return i.colDef.allowCellFocus
})
};
f.prototype.getFocusableRows=function(){return this.rows.filter(function(h){return h.allowCellFocus!==false
})
};
f.prototype.getNextRowCol=function(i,h,j){switch(i){case e.direction.LEFT:return this.getRowColLeft(h,j);
case e.direction.RIGHT:return this.getRowColRight(h,j);
case e.direction.UP:return this.getRowColUp(h,j);
case e.direction.DOWN:return this.getRowColDown(h,j);
case e.direction.PG_UP:return this.getRowColPageUp(h,j);
case e.direction.PG_DOWN:return this.getRowColPageDown(h,j)
}};
f.prototype.getRowColLeft=function(k,m){var j=this.getFocusableCols();
var i=this.getFocusableRows();
var h=j.indexOf(m);
var l=i.indexOf(k);
if(h===-1){h=1
}var n=h===0?j.length-1:h-1;
if(n>h){if(l===0){return new b(k,j[n])
}else{return new b(i[l-1],j[n])
}}else{return new b(k,j[n])
}};
f.prototype.getRowColRight=function(k,m){var j=this.getFocusableCols();
var i=this.getFocusableRows();
var h=j.indexOf(m);
var l=i.indexOf(k);
if(h===-1){h=0
}var n=h===j.length-1?0:h+1;
if(n<h){if(l===i.length-1){return new b(k,j[n])
}else{return new b(i[l+1],j[n])
}}else{return new b(k,j[n])
}};
f.prototype.getRowColDown=function(k,m){var j=this.getFocusableCols();
var i=this.getFocusableRows();
var h=j.indexOf(m);
var l=i.indexOf(k);
if(h===-1){h=0
}if(l===i.length-1){return new b(k,j[h])
}else{return new b(i[l+1],j[h])
}};
f.prototype.getRowColPageDown=function(l,n){var k=this.getFocusableCols();
var j=this.getFocusableRows();
var h=k.indexOf(n);
var m=j.indexOf(l);
if(h===-1){h=0
}var i=this.bodyContainer.minRowsToRender();
if(m>=j.length-i){return new b(j[j.length-1],k[h])
}else{return new b(j[m+i],k[h])
}};
f.prototype.getRowColUp=function(k,m){var j=this.getFocusableCols();
var i=this.getFocusableRows();
var h=j.indexOf(m);
var l=i.indexOf(k);
if(h===-1){h=0
}if(l===0){return new b(k,j[h])
}else{return new b(i[l-1],j[h])
}};
f.prototype.getRowColPageUp=function(l,n){var k=this.getFocusableCols();
var j=this.getFocusableRows();
var h=k.indexOf(n);
var m=j.indexOf(l);
if(h===-1){h=0
}var i=this.bodyContainer.minRowsToRender();
if(m-i<0){return new b(j[0],k[h])
}else{return new b(j[m-i],k[h])
}};
return f
}]);
a.service("uiGridCellNavService",["gridUtil","uiGridConstants","uiGridCellNavConstants","$q","uiGridCellNavFactory","ScrollEvent",function(i,d,f,e,g,h){var c={initializeGrid:function(j){j.registerColumnBuilder(c.cellNavColumnBuilder);
j.cellNav={};
j.cellNav.lastRowCol=null;
j.cellNav.focusedCells=[];
c.defaultGridOptions(j.options);
var k={events:{cellNav:{navigate:function(m,l){}}},methods:{cellNav:{scrollTo:function(m,l){c.scrollTo(j,m,l)
},scrollToFocus:function(m,l){c.scrollToFocus(j,m,l)
},scrollToIfNecessary:function(m,l){c.scrollToIfNecessary(j,m,l)
},getFocusedCell:function(){return j.cellNav.lastRowCol
},getCurrentSelection:function(){return j.cellNav.focusedCells
},rowColSelectIndex:function(n){var l=-1;
for(var m=0;
m<j.cellNav.focusedCells.length;
m++){if(j.cellNav.focusedCells[m].col.uid===n.col.uid&&j.cellNav.focusedCells[m].row.uid===n.row.uid){l=m;
break
}}return l
}}}};
j.api.registerEventsFromObject(k.events);
j.api.registerMethodsFromObject(k.methods)
},defaultGridOptions:function(j){j.modifierKeysToMultiSelectCells=j.modifierKeysToMultiSelectCells===true
},decorateRenderContainers:function(k){var j=k.hasRightContainer()?k.renderContainers.right:null;
var l=k.hasLeftContainer()?k.renderContainers.left:null;
if(l!==null){k.renderContainers.left.cellNav=new g(k.renderContainers.body,l,j,k.renderContainers.body)
}if(j!==null){k.renderContainers.right.cellNav=new g(k.renderContainers.body,j,k.renderContainers.body,l)
}k.renderContainers.body.cellNav=new g(k.renderContainers.body,k.renderContainers.body,l,j)
},getDirection:function(j){if(j.keyCode===d.keymap.LEFT||(j.keyCode===d.keymap.TAB&&j.shiftKey)){return f.direction.LEFT
}if(j.keyCode===d.keymap.RIGHT||j.keyCode===d.keymap.TAB){return f.direction.RIGHT
}if(j.keyCode===d.keymap.UP||(j.keyCode===d.keymap.ENTER&&j.shiftKey)){return f.direction.UP
}if(j.keyCode===d.keymap.PG_UP){return f.direction.PG_UP
}if(j.keyCode===d.keymap.DOWN||j.keyCode===d.keymap.ENTER){return f.direction.DOWN
}if(j.keyCode===d.keymap.PG_DOWN){return f.direction.PG_DOWN
}return null
},cellNavColumnBuilder:function(l,j,m){var k=[];
l.allowCellFocus=l.allowCellFocus===undefined?true:l.allowCellFocus;
return e.all(k)
},scrollTo:function(k,m,l){var n=null,j=null;
if(m!==null&&typeof(m)!=="undefined"){n=k.getRow(m)
}if(l!==null&&typeof(l)!=="undefined"){j=k.getColumn(l.name?l.name:l.field)
}this.scrollToInternal(k,n,j)
},scrollToFocus:function(k,m,l){var o=null,j=null;
if(m!==null){o=k.getRow(m)
}if(l!==null){j=k.getColumn(l.name?l.name:l.field)
}this.scrollToInternal(k,o,j);
var n={row:o,col:j};
k.cellNav.broadcastCellNav(n)
},scrollToInternal:function(n,p,k){var m=new h(n,null,null,"uiGridCellNavService.scrollToInternal");
if(p!==null){var o=n.renderContainers.body.visibleRowCache.indexOf(p);
var l=n.renderContainers.body.visibleRowCache.length;
var j=(o+(o/(l-1)))/l;
m.y={percentage:j}
}if(k!==null){m.x={percentage:this.getLeftWidth(n,k)/this.getLeftWidth(n,n.renderContainers.body.visibleColumnCache[n.renderContainers.body.visibleColumnCache.length-1])}
}if(m.y||m.x){m.fireScrollingEvent()
}},scrollToIfNecessary:function(j,n,l){var y=new h(j,"uiGridCellNavService.scrollToIfNecessary");
var m=j.renderContainers.body.visibleRowCache;
var A=j.renderContainers.body.visibleColumnCache;
var E=j.renderContainers.body.prevScrollTop+j.headerHeight;
E=(E<0)?0:E;
var s=j.renderContainers.body.prevScrollLeft;
var x=j.renderContainers.body.prevScrollTop+j.gridHeight-j.headerHeight;
var t=j.renderContainers.body.prevScrollLeft+Math.ceil(j.gridWidth);
if(n!==null){var u=m.indexOf(n);
var v=(j.renderContainers.body.getCanvasHeight()-j.renderContainers.body.getViewportHeight());
var p=((u+1)*j.options.rowHeight);
p=(p<0)?0:p;
var F,w;
if(p<E){F=j.renderContainers.body.prevScrollTop-(E-p);
w=F/v;
y.y={percentage:w}
}else{if(p>x){F=p-x+j.renderContainers.body.prevScrollTop;
w=F/v;
y.y={percentage:w}
}}}if(l!==null){var D=A.indexOf(l);
var z=(j.renderContainers.body.getCanvasWidth()-j.renderContainers.body.getViewportWidth());
var r=0;
for(var C=0;
C<D;
C++){var q=A[C];
r+=q.drawnWidth
}r=(r<0)?0:r;
var o=r+l.drawnWidth;
o=(o<0)?0:o;
var k,B;
if(r<s){k=j.renderContainers.body.prevScrollLeft-(s-r);
B=k/z;
B=(B>1)?1:B;
y.x={percentage:B}
}else{if(o>t){k=o-t+j.renderContainers.body.prevScrollLeft;
B=k/z;
B=(B>1)?1:B;
y.x={percentage:B}
}}}if(y.y||y.x){y.fireScrollingEvent()
}},getLeftWidth:function(k,m){var l=0;
if(!m){return l
}var n=k.renderContainers.body.visibleColumnCache.indexOf(m);
k.renderContainers.body.visibleColumnCache.forEach(function(p,o){if(o<n){l+=p.drawnWidth
}});
var j=n===0?0:(n+1)/k.renderContainers.body.visibleColumnCache.length;
l+=m.drawnWidth*j;
return l
}};
return c
}]);
a.directive("uiGridCellnav",["gridUtil","uiGridCellNavService","uiGridCellNavConstants","uiGridConstants",function(f,d,e,c){return{replace:true,priority:-150,require:"^uiGrid",scope:false,controller:function(){},compile:function(){return{pre:function(j,i,h,l){var g=j;
var k=l.grid;
d.initializeGrid(k);
l.cellNav={};
l.cellNav.focusCell=function(n,m){l.cellNav.broadcastCellNav({row:n,col:m})
};
l.cellNav.broadcastCellNav=k.cellNav.broadcastCellNav=function(n,m){m=!(m===undefined||!m);
l.cellNav.broadcastFocus(n,m);
g.$broadcast(e.CELL_NAV_EVENT,n,m)
};
l.cellNav.clearFocus=k.cellNav.clearFocus=function(){g.$broadcast(e.CELL_NAV_EVENT,{eventType:e.EVENT_TYPE.CLEAR})
};
l.cellNav.broadcastFocus=function(q,o){o=!(o===undefined||!o);
var p=q.row,n=q.col;
var m=l.grid.api.cellNav.rowColSelectIndex(q);
if(k.cellNav.lastRowCol===null||m===-1){var r=new b(p,n);
k.api.cellNav.raise.navigate(r,k.cellNav.lastRowCol);
k.cellNav.lastRowCol=r;
if(l.grid.options.modifierKeysToMultiSelectCells&&o){k.cellNav.focusedCells.push(q)
}else{k.cellNav.focusedCells=[q]
}}else{if(k.options.modifierKeysToMultiSelectCells&&o&&m>=0){k.cellNav.focusedCells.splice(m,1)
}}};
l.cellNav.handleKeyDown=function(n){var p=d.getDirection(n);
if(p===null){return true
}var m="body";
if(n.uiGridTargetRenderContainerId){m=n.uiGridTargetRenderContainerId
}var o=l.grid.api.cellNav.getFocusedCell();
if(o){var q=l.grid.renderContainers[m].cellNav.getNextRowCol(p,o.row,o.col);
if(p===e.direction.LEFT&&q.row===o.row&&n.keyCode===c.keymap.TAB&&n.shiftKey){l.cellNav.clearFocus();
return true
}else{if(p===e.direction.RIGHT&&q.row===o.row&&n.keyCode===c.keymap.TAB&&!n.shiftKey){l.cellNav.clearFocus();
return true
}}q.eventType=e.EVENT_TYPE.KEYDOWN;
l.cellNav.broadcastCellNav(q);
d.scrollToIfNecessary(k,q.row,q.col);
n.stopPropagation();
n.preventDefault();
return false
}}
},post:function(i,h,g,j){}}
}}
}]);
a.directive("uiGridRenderContainer",["$timeout","$document","gridUtil","uiGridConstants","uiGridCellNavService","uiGridCellNavConstants",function(f,h,g,c,d,e){return{replace:true,priority:-99999,require:["^uiGrid","uiGridRenderContainer","?^uiGridCellnav"],scope:false,compile:function(){return{post:function(q,l,p,k){var n=k[0],j=k[1];
if(!n.grid.api.cellNav){return
}var m=j.containerId;
var i=n.grid;
d.decorateRenderContainers(i);
l.attr("tabindex",-1);
l.on("keydown",function(r){r.uiGridTargetRenderContainerId=m;
return n.cellNav.handleKeyDown(r)
});
var o=false;
i.api.core.on.scrollEvent(q,function(r){if(r.grid&&r.grid.id!==n.grid.id){return
}if(n.grid.api.cellNav.getFocusedCell()==null){return
}if(r.source==="uiGridCellNavService.scrollToIfNecessary"){o=true
}else{if(o){f(function(){f(function(){var s=n.grid.api.cellNav.getFocusedCell();
if(h.activeElement===h.body){l[0].focus()
}n.cellNav.broadcastCellNav(s);
o=false
})
})
}}})
}}
}}
}]);
a.directive("uiGridCell",["$timeout","$document","uiGridCellNavService","gridUtil","uiGridCellNavConstants","uiGridConstants",function(f,h,d,g,e,c){return{priority:-150,restrict:"A",require:"^uiGrid",scope:false,link:function(l,k,i,n){if(!n.grid.api.cellNav){return
}if(!l.col.colDef.allowCellFocus){return
}j();
k.find("div").on("click",function(p){n.cellNav.broadcastCellNav(new b(l.row,l.col),p.ctrlKey||p.metaKey);
p.stopPropagation()
});
k.find("div").on("focus",function(p){n.cellNav.broadcastCellNav(new b(l.row,l.col),p.ctrlKey||p.metaKey)
});
l.$on(e.CELL_NAV_EVENT,function(p,r,q){if(p.eventType===e.EVENT_TYPE.CLEAR){m();
return
}if(r.row===l.row&&r.col===l.col){if(n.grid.options.modifierKeysToMultiSelectCells&&q&&n.grid.api.cellNav.rowColSelectIndex(r)===-1){m()
}else{o()
}if(r.hasOwnProperty("eventType")&&r.eventType===e.EVENT_TYPE.KEYDOWN){k.find("div")[0].focus()
}}else{if(!(n.grid.options.modifierKeysToMultiSelectCells&&q)){m()
}}});
function j(){k.find("div").attr("tabindex",0)
}function o(){var p=k.find("div");
p.addClass("ui-grid-cell-focus")
}function m(){var p=k.find("div");
p.removeClass("ui-grid-cell-focus")
}l.$on("$destroy",function(){k.find("div").off("click")
})
}}
}])
})();
(function(){var a=angular.module("ui.grid.edit",["ui.grid"]);
a.constant("uiGridEditConstants",{EDITABLE_CELL_TEMPLATE:/EDITABLE_CELL_TEMPLATE/g,EDITABLE_CELL_DIRECTIVE:/editable_cell_directive/g,events:{BEGIN_CELL_EDIT:"uiGridEventBeginCellEdit",END_CELL_EDIT:"uiGridEventEndCellEdit",CANCEL_CELL_EDIT:"uiGridEventCancelCellEdit"}});
a.service("uiGridEditService",["$q","$templateCache","uiGridConstants","gridUtil",function(e,d,c,f){var b={initializeGrid:function(g){b.defaultGridOptions(g.options);
g.registerColumnBuilder(b.editColumnBuilder);
var h={events:{edit:{afterCellEdit:function(l,j,k,i){},beginCellEdit:function(j,i){},cancelCellEdit:function(j,i){}}},methods:{edit:{}}};
g.api.registerEventsFromObject(h.events)
},defaultGridOptions:function(g){g.cellEditableCondition=g.cellEditableCondition===undefined?true:g.cellEditableCondition;
g.enableCellEditOnFocus=g.enableCellEditOnFocus===undefined?false:g.enableCellEditOnFocus
},editColumnBuilder:function(i,g,j){var h=[];
i.enableCellEdit=i.enableCellEdit===undefined?(j.enableCellEdit===undefined?(i.type!=="object"):j.enableCellEdit):i.enableCellEdit;
i.cellEditableCondition=i.cellEditableCondition===undefined?j.cellEditableCondition:i.cellEditableCondition;
if(i.enableCellEdit){i.editableCellTemplate=i.editableCellTemplate||j.editableCellTemplate||"ui-grid/cellEditor";
h.push(f.getTemplate(i.editableCellTemplate).then(function(k){g.editableCellTemplate=k
},function(k){throw new Error("Couldn't fetch/use colDef.editableCellTemplate '"+i.editableCellTemplate+"'")
}))
}i.enableCellEditOnFocus=i.enableCellEditOnFocus===undefined?j.enableCellEditOnFocus:i.enableCellEditOnFocus;
return e.all(h)
},isStartEditKey:function(g){if(g.keyCode===c.keymap.LEFT||(g.keyCode===c.keymap.TAB&&g.shiftKey)||g.keyCode===c.keymap.RIGHT||g.keyCode===c.keymap.TAB||g.keyCode===c.keymap.UP||(g.keyCode===c.keymap.ENTER&&g.shiftKey)||g.keyCode===c.keymap.DOWN||g.keyCode===c.keymap.ENTER){return false
}return true
}};
return b
}]);
a.directive("uiGridEdit",["gridUtil","uiGridEditService",function(c,b){return{replace:true,priority:0,require:"^uiGrid",scope:false,compile:function(){return{pre:function(f,e,d,g){b.initializeGrid(g.grid)
},post:function(f,e,d,g){}}
}}
}]);
a.directive("uiGridCell",["$compile","$injector","$timeout","uiGridConstants","uiGridEditConstants","gridUtil","$parse","uiGridEditService",function(d,h,b,f,e,g,c,j){var i=500;
return{priority:-100,restrict:"A",scope:false,require:"?^uiGrid",link:function(C,D,q,B){if(!C.col.colDef.enableCellEdit||C.row.enableCellEdit===false){return
}var v;
var G;
var z=false;
var t=false;
var s;
var w;
var l;
u();
function u(){D.on("dblclick",k);
D.on("keydown",F);
if(C.col.colDef.enableCellEditOnFocus){D.find("div").on("focus",H)
}D.on("touchstart",m)
}function m(I){if(typeof(I.originalEvent)!=="undefined"&&I.originalEvent!==undefined){I=I.originalEvent
}D.on("touchend",p);
w=b(function(){},i);
w.then(function(){setTimeout(k,0);
D.off("touchend",p)
})
}function p(I){b.cancel(w);
D.off("touchend",p)
}function n(){D.off("dblclick",k);
D.off("keydown",F);
if(C.col.colDef.enableCellEditOnFocus){D.find("div").off("focus",H)
}D.off("touchstart",m)
}function H(I){if(B&&B.cellNav){B.cellNav.focusCell(C.row,C.col)
}I.stopPropagation();
k()
}try{var A=h.get("uiGridCellNavConstants");
if(C.col.colDef.enableCellEditOnFocus){C.$on(A.CELL_NAV_EVENT,function(I,J){if(J.row===C.row&&J.col===C.col){k()
}else{x()
}})
}}catch(E){}function F(I){if(j.isStartEditKey(I)){k()
}}function r(I,J){return !J.isSaving&&(angular.isFunction(I.colDef.cellEditableCondition)?I.colDef.cellEditableCondition(C):I.colDef.cellEditableCondition)
}function k(){if(z){return
}if(!r(C.col,C.row)){return
}if(C.grid.api.cellNav){C.grid.api.cellNav.scrollToIfNecessary(C.row,C.col)
}s=c(C.row.getQualifiedColField(C.col));
G=s(C);
v=C.col.editableCellTemplate;
v=v.replace(f.MODEL_COL_FIELD,C.row.getQualifiedColField(C.col));
var L=C.col.colDef.editDropdownFilter?"|"+C.col.colDef.editDropdownFilter:"";
v=v.replace(f.CUSTOM_FILTERS,L);
var M="text";
switch(C.col.colDef.type){case"boolean":M="checkbox";
break;
case"number":M="number";
break;
case"date":M="date";
break
}v=v.replace("INPUT_TYPE",M);
var O=C.col.colDef.editDropdownRowEntityOptionsArrayPath;
if(O){C.editDropdownOptionsArray=o(C.row.entity,O)
}else{C.editDropdownOptionsArray=C.col.colDef.editDropdownOptionsArray
}C.editDropdownIdLabel=C.col.colDef.editDropdownIdLabel?C.col.colDef.editDropdownIdLabel:"id";
C.editDropdownValueLabel=C.col.colDef.editDropdownValueLabel?C.col.colDef.editDropdownValueLabel:"value";
var K;
C.$apply(function(){z=true;
n();
var P=angular.element(v);
D.append(P);
l=C.$new();
d(P)(l);
var Q=angular.element(D.children()[0]);
t=Q.hasClass("ui-grid-cell-focus");
Q.addClass("ui-grid-cell-contents-hidden")
});
var J=C.col.grid.api.core.on.scrollEvent(C,function(){x(true);
C.grid.api.edit.raise.afterCellEdit(C.row.entity,C.col.colDef,s(C),G);
J();
N();
I()
});
var N=C.$on(e.events.END_CELL_EDIT,function(P,Q){x(Q);
C.grid.api.edit.raise.afterCellEdit(C.row.entity,C.col.colDef,s(C),G);
N();
J();
I()
});
var I=C.$on(e.events.CANCEL_CELL_EDIT,function(){y();
I();
J();
N()
});
C.$broadcast(e.events.BEGIN_CELL_EDIT);
C.grid.api.edit.raise.beginCellEdit(C.row.entity,C.col.colDef)
}function x(I){if(!z){return
}var J=angular.element(D.children()[0]);
l.$destroy();
angular.element(D.children()[1]).remove();
J.removeClass("ui-grid-cell-contents-hidden");
if(I&&t){J[0].focus()
}t=false;
z=false;
u();
C.grid.api.core.notifyDataChange(f.dataChange.EDIT)
}function y(){if(!z){return
}s.assign(C,G);
C.$apply();
C.grid.api.edit.raise.cancelCellEdit(C.row.entity,C.col.colDef);
x(true)
}function o(J,K){K=K.replace(/\[(\w+)\]/g,".$1");
K=K.replace(/^\./,"");
var I=K.split(".");
while(I.length){var L=I.shift();
if(L in J){J=J[L]
}else{return
}}return J
}}}
}]);
a.directive("uiGridEditor",["gridUtil","uiGridConstants","uiGridEditConstants",function(d,b,c){return{scope:true,require:["?^uiGrid","?^uiGridRenderContainer"],compile:function(){return{pre:function(g,f,e){},post:function(h,g,f,j){var i,e;
if(j[0]){i=j[0]
}if(j[1]){e=j[1]
}h.$on(c.events.BEGIN_CELL_EDIT,function(){g[0].focus();
g[0].select();
g.on("blur",function(k){h.stopEdit(k)
})
});
h.deepEdit=false;
h.stopEdit=function(k){if(h.inputForm&&!h.inputForm.$valid){k.stopPropagation();
h.$emit(c.events.CANCEL_CELL_EDIT)
}else{h.$emit(c.events.END_CELL_EDIT)
}h.deepEdit=false
};
g.on("click",function(k){h.deepEdit=true
});
g.on("keydown",function(k){switch(k.keyCode){case b.keymap.ESC:k.stopPropagation();
h.$emit(c.events.CANCEL_CELL_EDIT);
break;
case b.keymap.ENTER:h.stopEdit(k);
break;
case b.keymap.TAB:h.stopEdit(k);
break
}if(h.deepEdit){switch(k.keyCode){case b.keymap.LEFT:k.stopPropagation();
break;
case b.keymap.RIGHT:k.stopPropagation();
break;
case b.keymap.UP:k.stopPropagation();
break;
case b.keymap.DOWN:k.stopPropagation();
break
}}else{if(i&&i.hasOwnProperty("cellNav")&&e){k.uiGridTargetRenderContainerId=e.containerId;
i.cellNav.handleKeyDown(k)
}}return true
})
}}
}}
}]);
a.directive("uiGridEditor",["$filter",function(c){function b(f){if(typeof(f)==="undefined"||f===""){return null
}var h=f.split("-");
if(h.length!==3){return null
}var e=parseInt(h[0],10);
var g=parseInt(h[1],10);
var d=parseInt(h[2],10);
if(g<1||e<1||d<1){return null
}return new Date(e,(g-1),d)
}return{priority:-100,require:"?ngModel",link:function(f,e,d,g){if(angular.version.minor===2&&d.type&&d.type==="date"&&g){g.$formatters.push(function(h){g.$setValidity(null,(!h||!isNaN(h.getTime())));
return c("date")(h,"yyyy-MM-dd")
});
g.$parsers.push(function(i){if(i&&i.length>0){var h=b(i);
g.$setValidity(null,(h&&!isNaN(h.getTime())));
return h
}else{g.$setValidity(null,true);
return null
}})
}}}
}]);
a.directive("uiGridEditDropdown",["uiGridConstants","uiGridEditConstants",function(b,c){return{scope:true,compile:function(){return{pre:function(f,e,d){},post:function(f,e,d){f.$on(c.events.BEGIN_CELL_EDIT,function(){e[0].focus();
e[0].style.width=(e[0].parentElement.offsetWidth-1)+"px";
e.on("blur",function(g){f.stopEdit(g)
})
});
f.stopEdit=function(g){f.$emit(c.events.END_CELL_EDIT)
};
e.on("keydown",function(g){switch(g.keyCode){case b.keymap.ESC:g.stopPropagation();
f.$emit(c.events.CANCEL_CELL_EDIT);
break;
case b.keymap.ENTER:f.stopEdit(g);
break;
case b.keymap.LEFT:f.stopEdit(g);
break;
case b.keymap.RIGHT:f.stopEdit(g);
break;
case b.keymap.UP:g.stopPropagation();
break;
case b.keymap.DOWN:g.stopPropagation();
break;
case b.keymap.TAB:f.stopEdit(g);
break
}return true
})
}}
}}
}])
})();
(function(){var a=angular.module("ui.grid.expandable",["ui.grid"]);
a.service("uiGridExpandableService",["gridUtil","$compile",function(d,c){var b={initializeGrid:function(e){e.expandable={};
e.expandable.expandedAll=false;
e.options.enableExpandable=e.options.enableExpandable!==false;
e.options.expandableRowHeight=e.options.expandableRowHeight||150;
e.options.expandableRowHeaderWidth=e.options.expandableRowHeaderWidth||40;
if(e.options.enableExpandable&&!e.options.expandableRowTemplate){d.logError("You have not set the expandableRowTemplate, disabling expandable module");
e.options.enableExpandable=false
}var f={events:{expandable:{rowExpandedStateChanged:function(g,h){}}},methods:{expandable:{toggleRowExpansion:function(g){var h=e.getRow(g);
if(h!==null){b.toggleRowExpansion(e,h)
}},expandAllRows:function(){b.expandAllRows(e)
},collapseAllRows:function(){b.collapseAllRows(e)
},toggleAllRows:function(){b.toggleAllRows(e)
}}}};
e.api.registerEventsFromObject(f.events);
e.api.registerMethodsFromObject(f.methods)
},toggleRowExpansion:function(e,f){f.isExpanded=!f.isExpanded;
if(f.isExpanded){f.height=f.grid.options.rowHeight+e.options.expandableRowHeight
}else{f.height=f.grid.options.rowHeight;
e.expandable.expandedAll=false
}e.api.expandable.raise.rowExpandedStateChanged(f)
},expandAllRows:function(f,e){angular.forEach(f.renderContainers.body.visibleRowCache,function(g){if(!g.isExpanded){b.toggleRowExpansion(f,g)
}});
f.expandable.expandedAll=true;
f.queueGridRefresh()
},collapseAllRows:function(e){angular.forEach(e.renderContainers.body.visibleRowCache,function(f){if(f.isExpanded){b.toggleRowExpansion(e,f)
}});
e.expandable.expandedAll=false;
e.queueGridRefresh()
},toggleAllRows:function(e){if(e.expandable.expandedAll){b.collapseAllRows(e)
}else{b.expandAllRows(e)
}}};
return b
}]);
a.directive("uiGridExpandable",["uiGridExpandableService","$templateCache",function(c,b){return{replace:true,priority:0,require:"^uiGrid",scope:false,compile:function(){return{pre:function(f,e,d,g){if(g.grid.options.enableExpandableRowHeader!==false){var h={name:"expandableButtons",displayName:"",exporterSuppressExport:true,enableColumnResizing:false,enableColumnMenu:false,width:g.grid.options.expandableRowHeaderWidth||40};
h.cellTemplate=b.get("ui-grid/expandableRowHeader");
h.headerCellTemplate=b.get("ui-grid/expandableTopRowHeader");
g.grid.addRowHeaderColumn(h)
}c.initializeGrid(g.grid)
},post:function(f,e,d,g){}}
}}
}]);
a.directive("uiGrid",["uiGridExpandableService","$templateCache",function(c,b){return{replace:true,priority:1000,require:"^uiGrid",scope:false,compile:function(){return{pre:function(f,e,d,g){g.grid.api.core.on.renderingComplete(f,function(){if(f.row&&f.row.grid&&f.row.grid.options&&f.row.grid.options.enableExpandable){g.grid.parentRow=f.row
}})
},post:function(f,e,d,g){}}
}}
}]);
a.directive("uiGridExpandableRow",["uiGridExpandableService","$timeout","$compile","uiGridConstants","gridUtil","$interval","$log",function(e,d,c,b,h,g,f){return{replace:false,priority:0,scope:false,compile:function(){return{pre:function(k,j,i,l){h.getTemplate(k.grid.options.expandableRowTemplate).then(function(n){if(k.grid.options.expandableRowScope){var m=k.grid.options.expandableRowScope;
for(var o in m){if(m.hasOwnProperty(o)){k[o]=m[o]
}}}var p=c(n)(k);
j.append(p);
k.row.expandedRendered=true
})
},post:function(k,j,i,l){k.$on("$destroy",function(){k.row.expandedRendered=false
})
}}
}}
}]);
a.directive("uiGridRow",["$compile","gridUtil","$templateCache",function(c,d,b){return{priority:-200,scope:false,compile:function(f,e){return{pre:function(i,h,g,k){i.expandableRow={};
i.expandableRow.shouldRenderExpand=function(){var l=i.colContainer.name==="body"&&i.grid.options.enableExpandable!==false&&i.row.isExpanded&&(!i.grid.isScrollingVertically||i.row.expandedRendered);
return l
};
i.expandableRow.shouldRenderFiller=function(){var l=i.row.isExpanded&&(i.colContainer.name!=="body"||(i.grid.isScrollingVertically&&!i.row.expandedRendered));
return l
};
function j(){var l=i.grid;
var m=0;
angular.forEach(l.columns,function(n){if(n.renderContainer==="left"){m+=n.width
}});
m=Math.floor(m);
return".grid"+l.id+" .ui-grid-pinned-container-"+i.colContainer.name+", .grid"+l.id+" .ui-grid-pinned-container-"+i.colContainer.name+" .ui-grid-render-container-"+i.colContainer.name+" .ui-grid-viewport .ui-grid-canvas .ui-grid-row { width: "+m+"px; }"
}if(i.colContainer.name==="left"){i.grid.registerStyleComputation({priority:15,func:j})
}},post:function(i,h,g,j){}}
}}
}]);
a.directive("uiGridViewport",["$compile","gridUtil","$templateCache",function(c,d,b){return{priority:-200,scope:false,compile:function(g,e){var f=angular.element(g.children().children()[0]);
var i=b.get("ui-grid/expandableScrollFiller");
var h=b.get("ui-grid/expandableRow");
f.append(h);
f.append(i);
return{pre:function(l,k,j,m){},post:function(l,k,j,m){}}
}}
}])
})();
(function(){var a=angular.module("ui.grid.exporter",["ui.grid"]);
a.constant("uiGridExporterConstants",{featureName:"exporter",ALL:"all",VISIBLE:"visible",SELECTED:"selected",CSV_CONTENT:"CSV_CONTENT",BUTTON_LABEL:"BUTTON_LABEL",FILE_NAME:"FILE_NAME"});
a.service("uiGridExporterService",["$q","uiGridExporterConstants","uiGridSelectionConstants","gridUtil","$compile","$interval","i18nService",function(d,f,i,h,e,g,c){var b={initializeGrid:function(j){j.exporter={};
this.defaultGridOptions(j.options);
var k={events:{exporter:{}},methods:{exporter:{csvExport:function(l,m){b.csvExport(j,l,m)
},pdfExport:function(l,m){b.pdfExport(j,l,m)
}}}};
j.api.registerEventsFromObject(k.events);
j.api.registerMethodsFromObject(k.methods);
if(j.api.core.addToGridMenu){b.addToMenu(j)
}else{g(function(){if(j.api.core.addToGridMenu){b.addToMenu(j)
}},100,1)
}},defaultGridOptions:function(j){j.exporterSuppressMenu=j.exporterSuppressMenu===true;
j.exporterMenuLabel=j.exporterMenuLabel?j.exporterMenuLabel:"Export";
j.exporterSuppressColumns=j.exporterSuppressColumns?j.exporterSuppressColumns:[];
j.exporterCsvColumnSeparator=j.exporterCsvColumnSeparator?j.exporterCsvColumnSeparator:",";
j.exporterCsvFilename=j.exporterCsvFilename?j.exporterCsvFilename:"download.csv";
j.exporterOlderExcelCompatibility=j.exporterOlderExcelCompatibility===true;
j.exporterPdfDefaultStyle=j.exporterPdfDefaultStyle?j.exporterPdfDefaultStyle:{fontSize:11};
j.exporterPdfTableStyle=j.exporterPdfTableStyle?j.exporterPdfTableStyle:{margin:[0,5,0,15]};
j.exporterPdfTableHeaderStyle=j.exporterPdfTableHeaderStyle?j.exporterPdfTableHeaderStyle:{bold:true,fontSize:12,color:"black"};
j.exporterPdfHeader=j.exporterPdfHeader?j.exporterPdfHeader:null;
j.exporterPdfFooter=j.exporterPdfFooter?j.exporterPdfFooter:null;
j.exporterPdfOrientation=j.exporterPdfOrientation?j.exporterPdfOrientation:"landscape";
j.exporterPdfPageSize=j.exporterPdfPageSize?j.exporterPdfPageSize:"A4";
j.exporterPdfMaxGridWidth=j.exporterPdfMaxGridWidth?j.exporterPdfMaxGridWidth:720;
j.exporterMenuCsv=j.exporterMenuCsv!==undefined?j.exporterMenuCsv:true;
j.exporterMenuPdf=j.exporterMenuPdf!==undefined?j.exporterMenuPdf:true;
j.exporterPdfCustomFormatter=(j.exporterPdfCustomFormatter&&typeof(j.exporterPdfCustomFormatter)==="function")?j.exporterPdfCustomFormatter:function(k){return k
};
j.exporterHeaderFilterUseName=j.exporterHeaderFilterUseName===true;
j.exporterFieldCallback=j.exporterFieldCallback?j.exporterFieldCallback:function(l,n,k,m){return m
}
},addToMenu:function(j){j.api.core.addToGridMenu(j,[{title:c.getSafeText("gridMenu.exporterAllAsCsv"),action:function(k){this.grid.api.exporter.csvExport(f.ALL,f.ALL)
},shown:function(){return this.grid.options.exporterMenuCsv
}},{title:c.getSafeText("gridMenu.exporterVisibleAsCsv"),action:function(k){this.grid.api.exporter.csvExport(f.VISIBLE,f.VISIBLE)
},shown:function(){return this.grid.options.exporterMenuCsv
}},{title:c.getSafeText("gridMenu.exporterSelectedAsCsv"),action:function(k){this.grid.api.exporter.csvExport(f.SELECTED,f.VISIBLE)
},shown:function(){return this.grid.options.exporterMenuCsv&&(this.grid.api.selection&&this.grid.api.selection.getSelectedRows().length>0)
}},{title:c.getSafeText("gridMenu.exporterAllAsPdf"),action:function(k){this.grid.api.exporter.pdfExport(f.ALL,f.ALL)
},shown:function(){return this.grid.options.exporterMenuPdf
}},{title:c.getSafeText("gridMenu.exporterVisibleAsPdf"),action:function(k){this.grid.api.exporter.pdfExport(f.VISIBLE,f.VISIBLE)
},shown:function(){return this.grid.options.exporterMenuPdf
}},{title:c.getSafeText("gridMenu.exporterSelectedAsPdf"),action:function(k){this.grid.api.exporter.pdfExport(f.SELECTED,f.VISIBLE)
},shown:function(){return this.grid.options.exporterMenuPdf&&(this.grid.api.selection&&this.grid.api.selection.getSelectedRows().length>0)
}}])
},csvExport:function(m,j,l){var o=this.getColumnHeaders(m,l);
var k=this.getData(m,j,l);
var n=this.formatAsCsv(o,k,m.options.exporterCsvColumnSeparator);
this.downloadFile(m.options.exporterCsvFilename,n,m.options.exporterOlderExcelCompatibility)
},getColumnHeaders:function(k,j){var l=[];
angular.forEach(k.columns,function(n,m){if((n.visible||j===f.ALL)&&n.colDef.exporterSuppressExport!==true&&k.options.exporterSuppressColumns.indexOf(n.name)===-1){l.push({name:n.field,displayName:k.options.exporterHeaderFilter?(k.options.exporterHeaderFilterUseName?k.options.exporterHeaderFilter(n.name):k.options.exporterHeaderFilter(n.displayName)):n.displayName,width:n.drawnWidth?n.drawnWidth:n.width,align:n.colDef.type==="number"?"right":"left"})
}});
return l
},getData:function(l,j,k){var n=[];
var m;
switch(j){case f.ALL:m=l.rows;
break;
case f.VISIBLE:m=l.getVisibleRows();
break;
case f.SELECTED:if(l.api.selection){m=l.api.selection.getSelectedGridRows()
}else{h.logError("selection feature must be enabled to allow selected rows to be exported")
}break
}angular.forEach(m,function(q,o){if(q.exporterEnableExporting!==false){var p=[];
angular.forEach(l.columns,function(s,r){if((s.visible||k===f.ALL)&&s.colDef.exporterSuppressExport!==true&&l.options.exporterSuppressColumns.indexOf(s.name)===-1){var t={value:l.options.exporterFieldCallback(l,q,s,l.getCellValue(q,s))};
if(s.colDef.exporterPdfAlign){t.alignment=s.colDef.exporterPdfAlign
}p.push(t)
}});
n.push(p)
}});
return n
},formatAsCsv:function(n,l,m){var k=this;
var o=n.map(function(p){return{value:p.displayName}
});
var j=k.formatRowAsCsv(this,m)(o)+"\n";
j+=l.map(this.formatRowAsCsv(this,m)).join("\n");
return j
},formatRowAsCsv:function(j,k){return function(l){return l.map(j.formatFieldAsCsv).join(k)
}
},formatFieldAsCsv:function(j){if(j.value==null){return""
}if(typeof(j.value)==="number"){return j.value
}if(typeof(j.value)==="boolean"){return(j.value?"TRUE":"FALSE")
}if(typeof(j.value)==="string"){return'"'+j.value.replace(/"/g,'""')+'"'
}return JSON.stringify(j.value)
},isIE:function(){var j=navigator.userAgent.toLowerCase();
return(j.indexOf("msie")!==-1)?parseInt(j.split("msie")[1]):false
},downloadFile:function(n,q,p){var k=document;
var s=k.createElement("a");
var r="application/octet-stream;charset=utf-8";
var t;
var o;
if(!n){var l=new Date();
n="CWS Export - "+l.getFullYear()+(l.getMonth()+1)+l.getDate()+l.getHours()+l.getMinutes()+l.getSeconds()+".csv"
}o=this.isIE();
if(o&&o<10){var m=k.createElement("iframe");
document.body.appendChild(m);
m.contentWindow.document.open("text/html","replace");
m.contentWindow.document.write("sep=,\r\n"+q);
m.contentWindow.document.close();
m.contentWindow.focus();
m.contentWindow.document.execCommand("SaveAs",true,n);
document.body.removeChild(m);
return true
}if(navigator.msSaveBlob){return navigator.msSaveBlob(new Blob(["\uFEFF",q],{type:r}),n)
}if("download" in s){var j=new Blob(["\uFEFF",q],{type:r});
t=URL.createObjectURL(j);
s.setAttribute("download",n)
}else{t="data:"+r+","+encodeURIComponent(q);
s.setAttribute("target","_blank")
}s.href=t;
s.setAttribute("style","display:none;");
k.body.appendChild(s);
setTimeout(function(){if(s.click){s.click()
}else{if(document.createEvent){var u=document.createEvent("MouseEvents");
u.initEvent("click",true,true);
s.dispatchEvent(u)
}}k.body.removeChild(s)
},100)
},pdfExport:function(m,j,l){var o=this.getColumnHeaders(m,l);
var k=this.getData(m,j,l);
var n=this.prepareAsPdf(m,o,k);
if(this.isIE()){pdfMake.createPdf(n).download()
}else{pdfMake.createPdf(n).open()
}},prepareAsPdf:function(m,q,l){var p=this.calculatePdfHeaderWidths(m,q);
var k=q.map(function(r){return{text:r.displayName,style:"tableHeader"}
});
var j=l.map(this.formatRowAsPdf(this));
var n=[k].concat(j);
var o={pageOrientation:m.options.exporterPdfOrientation,pageSize:m.options.exporterPdfPageSize,content:[{style:"tableStyle",table:{headerRows:1,widths:p,body:n}}],styles:{tableStyle:m.options.exporterPdfTableStyle,tableHeader:m.options.exporterPdfTableHeaderStyle},defaultStyle:m.options.exporterPdfDefaultStyle};
if(m.options.exporterPdfLayout){o.layout=m.options.exporterPdfLayout
}if(m.options.exporterPdfHeader){o.header=m.options.exporterPdfHeader
}if(m.options.exporterPdfFooter){o.footer=m.options.exporterPdfFooter
}if(m.options.exporterPdfCustomFormatter){o=m.options.exporterPdfCustomFormatter(o)
}return o
},calculatePdfHeaderWidths:function(l,k){var j=0;
angular.forEach(k,function(o){if(typeof(o.width)==="number"){j+=o.width
}});
var m=0;
angular.forEach(k,function(p){if(p.width==="*"){m+=100
}if(typeof(p.width)==="string"&&p.width.match(/(\d)*%/)){var o=parseInt(p.width.match(/(\d)*%/)[0]);
p.width=j*o/100;
m+=p.width
}});
var n=j+m;
return k.map(function(o){return o.width==="*"?o.width:o.width*l.options.exporterPdfMaxGridWidth/n
})
},formatRowAsPdf:function(j){return function(k){return k.map(j.formatFieldAsPdfString)
}
},formatFieldAsPdfString:function(k){var j;
if(k.value==null){j=""
}else{if(typeof(k.value)==="number"){j=k.value.toString()
}else{if(typeof(k.value)==="boolean"){j=(k.value?"TRUE":"FALSE")
}else{if(typeof(k.value)==="string"){j=k.value.replace(/"/g,'""')
}else{j=JSON.stringify(k.value).replace(/^"/,"").replace(/"$/,"")
}}}}if(k.alignment&&typeof(k.alignment)==="string"){j={text:j,alignment:k.alignment}
}return j
}};
return b
}]);
a.directive("uiGridExporter",["uiGridExporterConstants","uiGridExporterService","gridUtil","$compile",function(d,b,e,c){return{replace:true,priority:0,require:"^uiGrid",scope:false,link:function(h,g,f,i){b.initializeGrid(i.grid);
i.grid.exporter.$scope=h
}}
}])
})();
(function(){var a=angular.module("ui.grid.grouping",["ui.grid"]);
a.constant("uiGridGroupingConstants",{featureName:"grouping",groupingRowHeaderColName:"groupingRowHeaderCol",EXPANDED:"expanded",COLLAPSED:"collapsed",aggregation:{COUNT:"count",SUM:"sum",MAX:"max",MIN:"min",AVG:"avg"}});
a.service("uiGridGroupingService",["$q","uiGridGroupingConstants","gridUtil","GridRow","gridClassFactory","i18nService","uiGridConstants",function(e,h,i,g,f,d,c){var b={initializeGrid:function(k,j){k.grouping={};
k.grouping.numberLevels=0;
k.grouping.expandAll=false;
k.grouping.rowExpandedStates={};
b.defaultGridOptions(k.options);
k.registerRowsProcessor(b.groupRows);
var l={events:{grouping:{}},methods:{grouping:{expandAllRows:function(){b.expandAllRows(k)
},collapseAllRows:function(){b.collapseAllRows(k)
},toggleRowGroupingState:function(m){b.toggleRowGroupingState(k,m)
},expandRow:function(m){b.expandRow(k,m)
},expandRowChildren:function(m){b.expandRowChildren(k,m)
},collapseRow:function(m){b.collapseRow(k,m)
}}}};
k.api.registerEventsFromObject(l.events);
k.api.registerMethodsFromObject(l.methods);
k.api.core.on.sortChanged(j,b.tidyPriorities)
},defaultGridOptions:function(j){j.enableGrouping=j.enableGrouping!==false;
j.groupingRowHeaderWidth=j.groupingRowHeaderWidth||40;
j.groupingIndent=j.groupingIndent||10
},groupingColumnBuilder:function(m,k,o){if(m.enableGrouping===false){return
}if(typeof(k.grouping)==="undefined"&&typeof(m.grouping)!=="undefined"){k.grouping=angular.copy(m.grouping)
}else{if(typeof(k.grouping)==="undefined"){k.grouping={}
}}if(typeof(k.grouping)!=="undefined"&&typeof(k.grouping.groupPriority)!==undefined&&k.grouping.groupPriority>=0){k.suppressRemoveSort=true
}k.groupingSuppressAggregationText=m.groupingSuppressAggregationText===true;
var p={name:"ui.grid.grouping.group",title:d.get().grouping.group,icon:"ui-grid-icon-indent-right",shown:function(){return typeof(this.context.col.grouping)==="undefined"||typeof(this.context.col.grouping.groupPriority)==="undefined"||this.context.col.grouping.groupPriority<0
},action:function(){b.groupColumn(this.context.col.grid,this.context.col)
}};
var l={name:"ui.grid.grouping.ungroup",title:d.get().grouping.ungroup,icon:"ui-grid-icon-indent-left",shown:function(){return typeof(this.context.col.grouping)!=="undefined"&&typeof(this.context.col.grouping.groupPriority)!=="undefined"&&this.context.col.grouping.groupPriority>=0
},action:function(){b.ungroupColumn(this.context.col.grid,this.context.col)
}};
var n={name:"ui.grid.grouping.aggregateRemove",title:d.get().grouping.aggregate_remove,shown:function(){return typeof(this.context.col.grouping)!=="undefined"&&typeof(this.context.col.grouping.aggregation)!=="undefined"&&this.context.col.grouping.aggregation!==null
},action:function(){b.aggregateColumn(this.context.col.grid,this.context.col,null)
}};
var j=function(r){var q={name:"ui.grid.grouping.aggregate"+r,title:d.get().grouping["aggregate_"+r],shown:function(){return typeof(this.context.col.grouping)==="undefined"||typeof(this.context.col.grouping.aggregation)==="undefined"||this.context.col.grouping.aggregation!==r
},action:function(){b.aggregateColumn(this.context.col.grid,this.context.col,r)
}};
if(!i.arrayContainsObjectWithProperty(k.menuItems,"name","ui.grid.grouping.aggregate"+r)){k.menuItems.push(q)
}};
if(!i.arrayContainsObjectWithProperty(k.menuItems,"name","ui.grid.grouping.group")){k.menuItems.push(p)
}if(!i.arrayContainsObjectWithProperty(k.menuItems,"name","ui.grid.grouping.ungroup")){k.menuItems.push(l)
}j(h.aggregation.COUNT);
j(h.aggregation.SUM);
j(h.aggregation.MAX);
j(h.aggregation.MIN);
j(h.aggregation.AVG);
if(!i.arrayContainsObjectWithProperty(k.menuItems,"name","ui.grid.grouping.aggregateRemove")){k.menuItems.push(n)
}},groupColumn:function(j,k){if(typeof(k.grouping)==="undefined"){k.grouping={}
}var l=b.getGrouping(j);
k.grouping.groupPriority=l.grouping.length;
if(!k.sort){k.sort={direction:c.ASC}
}else{if(typeof(k.sort.direction)==="undefined"||k.sort.direction===null){k.sort.direction=c.ASC
}}b.tidyPriorities(j);
b.moveGroupColumns(j);
j.queueGridRefresh()
},ungroupColumn:function(j,k){if(typeof(k.grouping)==="undefined"){return
}delete k.grouping.groupPriority;
b.tidyPriorities(j);
b.moveGroupColumns(j);
j.queueGridRefresh()
},aggregateColumn:function(k,l,j){if(typeof(l.grouping)==="undefined"){l.grouping={}
}if(typeof(l.grouping.groupPriority)!=="undefined"&&l.grouping.groupPriority>=0){b.ungroupColumn(k,l)
}l.grouping.aggregation=j;
k.queueGridRefresh()
},tidyPriorities:function(m){var j=[];
var l=[];
angular.forEach(m.columns,function(o,n){if(typeof(o.grouping)!=="undefined"&&typeof(o.grouping.groupPriority)!=="undefined"&&o.grouping.groupPriority>=0){j.push(o)
}else{if(typeof(o.sort)!=="undefined"&&typeof(o.sort.priority)!=="undefined"&&o.sort.priority>=0){l.push(o)
}}});
j.sort(function(o,n){return o.grouping.groupPriority-n.grouping.groupPriority
});
angular.forEach(j,function(o,n){o.grouping.groupPriority=n;
o.suppressRemoveSort=true;
if(typeof(o.sort)==="undefined"){o.sort={}
}o.sort.priority=n
});
var k=j.length;
l.sort(function(o,n){return o.sort.priority-n.sort.priority
});
angular.forEach(l,function(o,n){o.sort.priority=k;
o.suppressRemoveSort=o.colDef.suppressRemoveSort;
k++
})
},moveGroupColumns:function(j){if(j.options.moveGroupColumns===false){return
}j.columns.sort(function(l,k){var m,n;
if(typeof(l.grouping)==="undefined"||typeof(l.grouping.groupPriority)==="undefined"||l.grouping.groupPriority<0){m=null
}else{m=l.grouping.groupPriority
}if(typeof(k.grouping)==="undefined"||typeof(k.grouping.groupPriority)==="undefined"||k.grouping.groupPriority<0){n=null
}else{n=k.grouping.groupPriority
}if(m!==null&&n===null){return -1
}if(n!==null&&m===null){return 1
}if(m!==null&&n!==null){return m-n
}});
j.api.core.notifyDataChange(c.dataChange.COLUMN)
},expandAllRows:function(j){b.setAllNodes(j.grouping.rowExpandedStates,h.EXPANDED);
j.queueGridRefresh()
},collapseAllRows:function(j){b.setAllNodes(j.grouping.rowExpandedStates,h.COLLAPSED);
j.queueGridRefresh()
},setAllNodes:function(k,j){k.state=j;
angular.forEach(k,function(m,l){if(l!=="state"){b.setAllNodes(m,j)
}})
},toggleRowGroupingState:function(j,k){if(!k.groupHeader){return
}if(k.expandedState.state===h.EXPANDED){k.expandedState.state=h.COLLAPSED
}else{k.expandedState.state=h.EXPANDED
}j.queueGridRefresh()
},expandRow:function(j,k){if(!k.groupHeader){return
}k.expandedState.state=h.EXPANDED;
j.queueGridRefresh()
},expandRowChildren:function(j,k){if(!k.groupHeader){return
}b.setAllNodes(k.expandedState,h.COLLAPSED);
j.queueGridRefresh()
},collapseRow:function(j,k){if(!k.groupHeader){return
}k.expandedState.state=h.COLLAPSED;
j.queueGridRefresh()
},collapseRowChildren:function(j,k){if(!k.groupHeader){return
}b.setAllNodes(k.expandedState,h.COLLAPSED);
j.queueGridRefresh()
},groupRows:function(n){if(n.length===0){return n
}var m=this;
var k=b.initialiseProcessingState(m);
var j=function(p,q){var r=m.getCellValue(o,p.col);
if(typeof(r)==="undefined"||r===null){return
}if(!o.visible){return
}if(!p.initialised||r!==p.currentValue){b.insertGroupHeader(m,n,l,k,q);
l++
}b.aggregate(m,o,p)
};
for(var l=0;
l<n.length;
l++){var o=n[l];
angular.forEach(k,j);
b.setVisibility(m,o,k)
}b.writeOutAggregations(m,k,0);
return n
},initialiseProcessingState:function(l){var j=[];
var k=b.getGrouping(l);
angular.forEach(k.grouping,function(n,o){var m=[];
angular.forEach(k.aggregations,function(q,p){if(q.aggregation===h.aggregation.AVG){m.push({type:q.aggregation,fieldName:q.field,col:q.col,value:null,sum:null,count:null})
}else{m.push({type:q.aggregation,fieldName:q.field,col:q.col,value:null})
}});
j.push({fieldName:n.field,col:n.col,initialised:false,currentValue:null,currentGroupHeader:null,runningAggregations:m})
});
return j
},getGrouping:function(k){var j=[];
var l=[];
angular.forEach(k.columns,function(n,m){if(n.grouping){if(typeof(n.grouping.groupPriority)!=="undefined"&&n.grouping.groupPriority>=0){j.push({field:n.field,col:n,groupPriority:n.grouping.groupPriority,grouping:n.grouping})
}else{if(n.grouping.aggregation){l.push({field:n.field,col:n,aggregation:n.grouping.aggregation})
}}}});
j.sort(function(n,m){return n.groupPriority-m.groupPriority
});
angular.forEach(j,function(n,m){n.grouping.groupPriority=m;
n.groupPriority=m;
delete n.grouping
});
return{grouping:j,aggregations:l}
},insertGroupHeader:function(j,n,q,o,l){b.writeOutAggregations(j,o,l);
var p=new g({},null,j);
f.rowTemplateAssigner.call(j,p);
var r=o[l].fieldName;
var m=o[l].col;
var k=j.getCellValue(n[q],m);
p.entity[r]=k;
p.groupLevel=l;
p.groupHeader=true;
p.internalRow=true;
p.enableEditing=false;
p.enableSelection=false;
o[l].initialised=true;
o[l].currentValue=k;
o[l].currentGroupHeader=p;
p.expandedState=b.getExpandedState(j,o,l);
b.setVisibility(j,p,o);
n.splice(q,0,p)
},writeOutAggregations:function(l,j,m){for(var k=m;
k<j.length;
k++){b.writeOutAggregation(l,j[k])
}},writeOutAggregation:function(k,j){if(j.currentGroupHeader){angular.forEach(j.runningAggregations,function(m,l){if(m.col.groupingSuppressAggregationText){j.currentGroupHeader.entity[m.fieldName]=m.value
}else{j.currentGroupHeader.entity[m.fieldName]=d.get().aggregation[m.type]+m.value
}m.value=null;
if(m.sum){m.sum=null
}if(m.count){m.count=null
}})
}j.currentGroupHeader=null;
j.currentValue=null;
j.initialised=false
},getExpandedState:function(m,j,n){var l=m.grouping.rowExpandedStates;
for(var k=0;
k<=n;
k++){if(!l[j[k].currentValue]){l[j[k].currentValue]={state:h.COLLAPSED}
}l=l[j[k].currentValue]
}return l
},setVisibility:function(m,o,k){if(!o.visible){return
}var n=true;
var j=typeof(o.groupLevel)!=="undefined"?o.groupLevel:k.length;
for(var l=0;
l<j;
l++){if(k[l].currentGroupHeader.expandedState.state===h.COLLAPSED){n=false
}}if(!n){o.setThisRowInvisible("grouping",true)
}},aggregate:function(k,l,j){angular.forEach(j.runningAggregations,function(o,m){var n=k.getCellValue(l,o.col);
var p=Number(n);
switch(o.type){case h.aggregation.COUNT:o.value++;
break;
case h.aggregation.SUM:if(!isNaN(p)){o.value+=p
}break;
case h.aggregation.MIN:if(n!==null&&(n<o.value||o.value===null)){o.value=n
}break;
case h.aggregation.MAX:if(n>o.value){o.value=n
}break;
case h.aggregation.AVG:o.count++;
if(!isNaN(p)){o.sum+=p
}o.value=o.sum/o.count;
break
}})
}};
return b
}]);
a.directive("uiGridGrouping",["uiGridGroupingConstants","uiGridGroupingService","$templateCache",function(d,c,b){return{replace:true,priority:0,require:"^uiGrid",scope:false,compile:function(){return{pre:function(g,f,e,h){if(h.grid.options.enableGrouping!==false){c.initializeGrid(h.grid,g);
var i={name:d.groupingRowHeaderColName,displayName:"",width:h.grid.options.groupingRowHeaderWidth,minWidth:10,cellTemplate:"ui-grid/groupingRowHeader",headerCellTemplate:"ui-grid/groupingHeaderCell",enableColumnResizing:false,enableColumnMenu:false,exporterSuppressExport:true,allowCellFocus:true};
h.grid.addRowHeaderColumn(i);
h.grid.registerColumnBuilder(c.groupingColumnBuilder)
}},post:function(g,f,e,h){}}
}}
}]);
a.directive("uiGridGroupingRowHeaderButtons",["$templateCache","uiGridGroupingService",function(c,b){return{replace:true,restrict:"E",template:c.get("ui-grid/groupingRowHeaderButtons"),scope:true,require:"^uiGrid",link:function(g,e,d,h){var f=h.grid;
g.groupButtonClick=function(j,i){b.toggleRowGroupingState(f,j,i)
}
}}
}]);
a.directive("uiGridGroupingExpandAllButtons",["$templateCache","uiGridGroupingService",function(c,b){return{replace:true,restrict:"E",template:c.get("ui-grid/groupingExpandAllButtons"),scope:false,link:function(g,e,d,h){var f=g.col.grid;
g.headerButtonClick=function(j,i){if(f.grouping.expandAll){b.collapseAllRows(f,i);
f.grouping.expandAll=false
}else{b.expandAllRows(f,i);
f.grouping.expandAll=true
}}
}}
}]);
a.directive("uiGridViewport",["$compile","uiGridConstants","uiGridGroupingConstants","gridUtil","$parse","uiGridGroupingService",function(d,c,f,g,e,b){return{priority:-200,scope:false,compile:function(j,h){var i=angular.element(j.children().children()[0]);
var k=i.attr("ng-class");
var l="";
if(k){l=k.slice(0,-1)+",'ui-grid-group-header-row': row.groupLevel > -1}"
}else{l="{'ui-grid-group-header-row': row.groupLevel > -1}"
}i.attr("ng-class",l);
return{pre:function(o,n,m,p){},post:function(o,n,m,p){}}
}}
}])
})();
(function(){var a=angular.module("ui.grid.importer",["ui.grid"]);
a.constant("uiGridImporterConstants",{featureName:"importer"});
a.service("uiGridImporterService",["$q","uiGridConstants","uiGridImporterConstants","gridUtil","$compile","$interval","i18nService","$window",function(g,h,i,j,c,f,d,b){var e={initializeGrid:function(k,l){l.importer={$scope:k};
this.defaultGridOptions(l.options);
var m={events:{importer:{}},methods:{importer:{importFile:function(n){e.importThisFile(l,n)
}}}};
l.api.registerEventsFromObject(m.events);
l.api.registerMethodsFromObject(m.methods);
if(l.options.enableImporter&&l.options.importerShowMenu){if(l.api.core.addToGridMenu){e.addToMenu(l)
}else{f(function(){if(l.api.core.addToGridMenu){e.addToMenu(l)
}},100,1)
}}},defaultGridOptions:function(k){if(k.enableImporter||k.enableImporter===undefined){if(!(b.hasOwnProperty("File")&&b.hasOwnProperty("FileReader")&&b.hasOwnProperty("FileList")&&b.hasOwnProperty("Blob"))){j.logError("The File APIs are not fully supported in this browser, grid importer cannot be used.");
k.enableImporter=false
}else{k.enableImporter=true
}}else{k.enableImporter=false
}k.importerProcessHeaders=k.importerProcessHeaders||e.processHeaders;
k.importerHeaderFilter=k.importerHeaderFilter||function(l){return l
};
if(!k.importerErrorCallback||typeof(k.importerErrorCallback)!=="function"){delete k.importerErrorCallback
}if(k.enableImporter===true&&!k.importerDataAddCallback){j.logError("You have not set an importerDataAddCallback, importer is disabled");
k.enableImporter=false
}k.importerShowMenu=k.importerShowMenu!==false;
k.importerObjectCallback=k.importerObjectCallback||function(m,l){return l
}
},addToMenu:function(k){k.api.core.addToGridMenu(k,[{title:d.getSafeText("gridMenu.importerTitle")},{templateUrl:"ui-grid/importerMenuItemContainer",action:function(l){this.grid.api.importer.importAFile(k)
}}])
},importThisFile:function(m,l){if(!l){j.logError("No file object provided to importThisFile, should be impossible, aborting");
return
}var k=new FileReader();
switch(l.type){case"application/json":k.onload=e.importJsonClosure(m);
break;
default:k.onload=e.importCsvClosure(m);
break
}k.readAsText(l)
},importJsonClosure:function(k){return function(n){var l=[];
var m;
angular.forEach(e.parseJson(k,n),function(p,o){m=e.newObject(k);
angular.extend(m,p);
m=k.options.importerObjectCallback(k,m);
l.push(m)
});
e.addObjects(k,l)
}
},parseJson:function(l,n){var k;
try{k=JSON.parse(n.target.result)
}catch(m){e.alertError(l,"importer.invalidJson","File could not be processed, is it valid json? Content was: ",n.target.result);
return
}if(!Array.isArray(k)){e.alertError(l,"importer.jsonNotarray","Import failed, file is not an array, file was: ",n.target.result);
return[]
}else{return k
}},importCsvClosure:function(k){return function(n){var m=e.parseCsv(n);
if(!m||m.length<1){e.alertError(k,"importer.invalidCsv","File could not be processed, is it valid csv? Content was: ",n.target.result);
return
}var l=e.createCsvObjects(k,m);
if(!l||l.length===0){e.alertError(k,"importer.noObjects","Objects were not able to be derived, content was: ",n.target.result);
return
}e.addObjects(k,l)
}
},parseCsv:function(l){var k=l.target.result;
return CSV.parse(k)
},createCsvObjects:function(n,o){var k=n.options.importerProcessHeaders(n,o.shift());
if(!k||k.length===0){e.alertError(n,"importer.noHeaders","Column names could not be derived, content was: ",o);
return[]
}var l=[];
var m;
angular.forEach(o,function(q,p){m=e.newObject(n);
angular.forEach(q,function(s,r){if(k[r]!==null){m[k[r]]=s
}});
m=n.options.importerObjectCallback(n,m);
l.push(m)
});
return l
},processHeaders:function(k,l){var m=[];
if(!k.options.columnDefs||k.options.columnDefs.length===0){angular.forEach(l,function(p,o){m.push(p.replace(/[^0-9a-zA-Z\-_]/g,"_"))
});
return m
}else{var n=e.flattenColumnDefs(k,k.options.columnDefs);
angular.forEach(l,function(p,o){if(n[p]){m.push(n[p])
}else{if(n[p.toLowerCase()]){m.push(n[p.toLowerCase()])
}else{m.push(null)
}}});
return m
}},flattenColumnDefs:function(l,m){var k={};
angular.forEach(m,function(n,o){if(n.name){k[n.name]=n.field||n.name;
k[n.name.toLowerCase()]=n.field||n.name
}if(n.field){k[n.field]=n.field||n.name;
k[n.field.toLowerCase()]=n.field||n.name
}if(n.displayName){k[n.displayName]=n.field||n.name;
k[n.displayName.toLowerCase()]=n.field||n.name
}if(n.displayName&&l.options.importerHeaderFilter){k[l.options.importerHeaderFilter(n.displayName)]=n.field||n.name;
k[l.options.importerHeaderFilter(n.displayName).toLowerCase()]=n.field||n.name
}});
return k
},addObjects:function(n,m,l){if(n.api.rowEdit){var k=n.registerDataChangeCallback(function(){n.api.rowEdit.setRowsDirty(m);
k()
},[h.dataChange.ROW]);
n.importer.$scope.$on("$destroy",k)
}n.importer.$scope.$apply(n.options.importerDataAddCallback(n,m))
},newObject:function(k){if(typeof(k.options)!=="undefined"&&typeof(k.options.importerNewObject)!=="undefined"){return new k.options.importerNewObject()
}else{return{}
}},alertError:function(n,l,k,m){if(n.options.importerErrorCallback){n.options.importerErrorCallback(n,l,k,m)
}else{b.alert(d.getSafeText(l));
j.logError(k+m)
}}};
return e
}]);
a.directive("uiGridImporter",["uiGridImporterConstants","uiGridImporterService","gridUtil","$compile",function(b,d,e,c){return{replace:true,priority:0,require:"^uiGrid",scope:false,link:function(h,g,f,i){d.initializeGrid(h,i.grid)
}}
}]);
a.directive("uiGridImporterMenuItem",["uiGridImporterConstants","uiGridImporterService","gridUtil","$compile",function(b,d,e,c){return{replace:true,priority:0,require:"^uiGrid",scope:false,templateUrl:"ui-grid/importerMenuItem",link:function(h,g,f,k){var j=function(n){var o=n.srcElement||n.target;
if(o&&o.files&&o.files.length===1){var m=o.files[0];
d.importThisFile(i,m);
o.form.reset()
}};
var l=g[0].querySelectorAll(".ui-grid-importer-file-chooser");
var i=k.grid;
if(l.length!==1){e.logError("Found > 1 or < 1 file choosers within the menu item, error, cannot continue")
}else{l[0].addEventListener("change",j,false)
}}}
}])
})();
(function(){var a=angular.module("ui.grid.infiniteScroll",["ui.grid"]);
a.service("uiGridInfiniteScrollService",["gridUtil","$compile","$timeout","uiGridConstants",function(f,e,d,c){var b={initializeGrid:function(g){b.defaultGridOptions(g.options);
var h={events:{infiniteScroll:{needLoadMoreData:function(i,j){},needLoadMoreDataTop:function(i,j){}}},methods:{infiniteScroll:{dataLoaded:function(){g.options.loadTimout=false
}}}};
g.options.loadTimout=false;
g.api.registerEventsFromObject(h.events);
g.api.registerMethodsFromObject(h.methods)
},defaultGridOptions:function(g){g.enableInfiniteScroll=g.enableInfiniteScroll!==false
},loadData:function(g){g.options.loadTimout=true;
if(g.scrollDirection===c.scrollDirection.UP){g.api.infiniteScroll.raise.needLoadMoreDataTop();
return
}g.api.infiniteScroll.raise.needLoadMoreData()
},checkScroll:function(h,i){var g=h.options.infiniteScrollPercentage?h.options.infiniteScrollPercentage:20;
if(!h.options.loadTimout&&i<=g){this.loadData(h);
return true
}return false
}};
return b
}]);
a.directive("uiGridInfiniteScroll",["uiGridInfiniteScrollService",function(b){return{priority:-200,scope:false,require:"^uiGrid",compile:function(d,c,e){return{pre:function(g,f,i,h){b.initializeGrid(h.grid)
},post:function(g,f,h){}}
}}
}]);
a.directive("uiGridViewport",["$compile","gridUtil","uiGridInfiniteScrollService","uiGridConstants",function(c,e,d,b){return{priority:-200,scope:false,link:function(g,f,h){if(g.grid.options.enableInfiniteScroll){g.grid.api.core.on.scrollEvent(g,function(j){if(j.y&&(j.source!=="ui.grid.adjustInfiniteScrollPosition")){var i=100-(j.y.percentage*100);
if(g.grid.scrollDirection===b.scrollDirection.UP){i=(j.y.percentage*100)
}d.checkScroll(g.grid,i)
}})
}}}
}])
})();
(function(){var a=angular.module("ui.grid.moveColumns",["ui.grid"]);
a.service("uiGridMoveColumnService",["$q","$timeout","$log","ScrollEvent","uiGridConstants","gridUtil",function(d,e,f,g,c,h){var b={initializeGrid:function(j){var i=this;
this.registerPublicApi(j);
this.defaultGridOptions(j.options);
j.registerColumnBuilder(i.movableColumnBuilder)
},registerPublicApi:function(j){var i=this;
var k={events:{colMovable:{columnPositionChanged:function(n,m,l){}}},methods:{colMovable:{moveColumn:function(q,p){var n=j.columns;
if(!angular.isNumber(q)||!angular.isNumber(p)){h.logError("MoveColumn: Please provide valid values for originalPosition and finalPosition");
return
}var l=0;
for(var m=0;
m<n.length;
m++){if((angular.isDefined(n[m].colDef.visible)&&n[m].colDef.visible===false)||n[m].isRowHeader===true){l++
}}if(q>=(n.length-l)||p>=(n.length-l)){h.logError("MoveColumn: Invalid values for originalPosition, finalPosition");
return
}var o=function(s){var r=s;
for(var t=0;
t<=r;
t++){if(angular.isDefined(n[t])&&((angular.isDefined(n[t].colDef.visible)&&n[t].colDef.visible===false)||n[t].isRowHeader===true)){r++
}}return r
};
i.redrawColumnAtPosition(j,o(q),o(p))
}}}};
j.api.registerEventsFromObject(k.events);
j.api.registerMethodsFromObject(k.methods)
},defaultGridOptions:function(i){i.enableColumnMoving=i.enableColumnMoving!==false
},movableColumnBuilder:function(k,i,l){var j=[];
k.enableColumnMoving=k.enableColumnMoving===undefined?l.enableColumnMoving:k.enableColumnMoving;
return d.all(j)
},redrawColumnAtPosition:function(l,n,k){var j=l.columns;
var i=j[n];
if(i.colDef.enableColumnMoving){if(n>k){for(var o=n;
o>k;
o--){j[o]=j[o-1]
}}else{if(k>n){for(var m=n;
m<k;
m++){j[m]=j[m+1]
}}}j[k]=i;
e(function(){l.api.core.notifyDataChange(c.dataChange.COLUMN);
l.queueGridRefresh();
l.api.colMovable.raise.columnPositionChanged(i.colDef,n,k)
})
}}};
return b
}]);
a.directive("uiGridMoveColumns",["uiGridMoveColumnService",function(b){return{replace:true,priority:0,require:"^uiGrid",scope:false,compile:function(){return{pre:function(e,d,c,f){b.initializeGrid(f.grid)
},post:function(e,d,c,f){}}
}}
}]);
a.directive("uiGridHeaderCell",["$q","gridUtil","uiGridMoveColumnService","$document","$log","uiGridConstants","ScrollEvent",function(c,h,d,g,e,b,f){return{priority:-10,require:"^uiGrid",compile:function(){return{post:function(k,j,i,l){if(k.col.colDef.enableColumnMoving){k.$on(b.events.COLUMN_HEADER_CLICK,function(n,s){if(s.columnName===k.col.colDef.name&&!k.col.renderContainer){var x=s.event;
if(x.target.className!=="ui-grid-icon-angle-down"&&x.target.tagName!=="I"&&x.target.className.indexOf("ui-grid-filter-input")<0){var r=k.grid.element[0].getBoundingClientRect().left;
var t=x.pageX;
var o=0;
var v=r+k.grid.getViewportWidth();
var z=false;
var u;
var w;
var p=function(){z=true;
u=j.clone();
j.parent().append(u);
u.addClass("movingColumn");
var A={};
var B=j[0].getBoundingClientRect().left;
A.left=(B-r)+"px";
var D=k.grid.element[0].getBoundingClientRect().right;
var C=j[0].getBoundingClientRect().right;
if(C>D){w=k.col.drawnWidth+(D-C);
A.width=w+"px"
}u.css(A)
};
var m=function(G){l.fireEvent("hide-menu");
var C=k.grid.columns;
var B=0;
for(var D=0;
D<C.length;
D++){if(angular.isUndefined(C[D].colDef.visible)||C[D].colDef.visible===true){B+=C[D].drawnWidth||C[D].width||C[D].colDef.width
}}var A=u[0].getBoundingClientRect().left-1;
var F=u[0].getBoundingClientRect().right;
var J;
if(h.detectBrowser()==="ie"){J=A+G
}else{J=A-r+G
}J=J<v?J:v;
if((A>=r||G>0)&&(F<=v||G<0)){u.css({visibility:"visible",left:J+"px"})
}else{if(B>Math.ceil(l.grid.gridWidth)){G*=8;
var E=new f(k.col.grid,null,null,"uiGridHeaderCell.moveElement");
E.x={pixels:G};
E.fireScrollingEvent()
}}var H=0;
for(var I=0;
I<C.length;
I++){if(angular.isUndefined(C[I].colDef.visible)||C[I].colDef.visible===true){if(C[I].colDef.name!==k.col.colDef.name){H+=C[I].drawnWidth||C[I].width||C[I].colDef.width
}else{break
}}}if(k.newScrollLeft===undefined){o+=G
}else{o=k.newScrollLeft+J-H
}if(w<k.col.drawnWidth){w+=Math.abs(G);
u.css({width:w+"px"})
}};
var q=function(A){document.onselectstart=function(){return false
};
var B=A.pageX-t;
if(!z&&Math.abs(B)>50){p()
}else{if(z){m(B);
t=A.pageX
}}};
g.on("mousemove",q);
var y=function(A){document.onselectstart=null;
if(u){u.remove()
}var E=k.grid.columns;
var F=0;
for(var D=0;
D<E.length;
D++){if(E[D].colDef.name!==k.col.colDef.name){F++
}else{break
}}if(o<0){var C=0;
for(var B=F-1;
B>=0;
B--){if(angular.isUndefined(E[B].colDef.visible)||E[B].colDef.visible===true){C+=E[B].drawnWidth||E[B].width||E[B].colDef.width;
if(C>Math.abs(o)){d.redrawColumnAtPosition(k.grid,F,B+1);
break
}}}if(C<Math.abs(o)){d.redrawColumnAtPosition(k.grid,F,0)
}}else{if(o>0){var H=0;
for(var G=F+1;
G<E.length;
G++){if(angular.isUndefined(E[G].colDef.visible)||E[G].colDef.visible===true){H+=E[G].drawnWidth||E[G].width||E[G].colDef.width;
if(H>o){d.redrawColumnAtPosition(k.grid,F,G-1);
break
}}}if(H<o){d.redrawColumnAtPosition(k.grid,F,E.length-1)
}}}g.off("mousemove",q);
g.off("mouseup",y)
};
g.on("mouseup",y)
}}})
}}}
}}
}])
})();
(function(){var a=angular.module("ui.grid.pagination",["ng","ui.grid"]);
a.service("uiGridPaginationService",["gridUtil",function(c){var b={initializeGrid:function(d){b.defaultGridOptions(d.options);
var e={events:{pagination:{paginationChanged:function(g,f){}}},methods:{pagination:{getPage:function(){return d.options.enablePagination?d.options.paginationCurrentPage:null
},getTotalPages:function(){if(!d.options.enablePagination){return null
}return(d.options.totalItems===0)?1:Math.ceil(d.options.totalItems/d.options.paginationPageSize)
},nextPage:function(){if(!d.options.enablePagination){return
}if(d.options.totalItems>0){d.options.paginationCurrentPage=Math.min(d.options.paginationCurrentPage+1,e.methods.pagination.getTotalPages())
}else{d.options.paginationCurrentPage++
}},previousPage:function(){if(!d.options.enablePagination){return
}d.options.paginationCurrentPage=Math.max(d.options.paginationCurrentPage-1,1)
},seek:function(f){if(!d.options.enablePagination){return
}if(!angular.isNumber(f)||f<1){throw"Invalid page number: "+f
}d.options.paginationCurrentPage=Math.min(f,e.methods.pagination.getTotalPages())
}}}};
d.api.registerEventsFromObject(e.events);
d.api.registerMethodsFromObject(e.methods);
d.registerRowsProcessor(function(g){if(d.options.useExternalPagination||!d.options.enablePagination){return g
}var f=parseInt(d.options.paginationPageSize,10);
var h=parseInt(d.options.paginationCurrentPage,10);
var j=g.filter(function(k){return k.visible
});
d.options.totalItems=j.length;
var i=(h-1)*f;
if(i>j.length){h=d.options.paginationCurrentPage=1;
i=(h-1)*f
}return j.slice(i,i+f)
})
},defaultGridOptions:function(d){d.enablePagination=d.enablePagination!==false;
d.enablePaginationControls=d.enablePaginationControls!==false;
d.useExternalPagination=d.useExternalPagination===true;
if(c.isNullOrUndefined(d.totalItems)){d.totalItems=0
}if(c.isNullOrUndefined(d.paginationPageSizes)){d.paginationPageSizes=[250,500,1000]
}if(c.isNullOrUndefined(d.paginationPageSize)){if(d.paginationPageSizes.length>0){d.paginationPageSize=d.paginationPageSizes[0]
}else{d.paginationPageSize=0
}}if(c.isNullOrUndefined(d.paginationCurrentPage)){d.paginationCurrentPage=1
}if(c.isNullOrUndefined(d.paginationTemplate)){d.paginationTemplate="ui-grid/pagination"
}},onPaginationChanged:function(e,f,d){e.api.pagination.raise.paginationChanged(f,d);
if(!e.options.useExternalPagination){e.queueGridRefresh()
}}};
return b
}]);
a.directive("uiGridPagination",["gridUtil","uiGridPaginationService",function(c,b){return{priority:-200,scope:false,require:"uiGrid",link:{pre:function(e,d,g,f){b.initializeGrid(f.grid);
c.getTemplate(f.grid.options.paginationTemplate).then(function(i){var h=angular.element(i);
d.append(h);
f.innerCompile(h)
})
}}}
}]);
a.directive("uiGridPager",["uiGridPaginationService","uiGridConstants","gridUtil","i18nService",function(d,c,e,b){return{priority:-200,scope:true,require:"^uiGrid",link:function(m,g,i,j){m.paginationApi=j.grid.api.pagination;
m.sizesLabel=b.getSafeText("pagination.sizes");
m.totalItemsLabel=b.getSafeText("pagination.totalItems");
var n=j.grid.options;
j.grid.renderContainers.body.registerViewportAdjuster(function(o){o.height=o.height-e.elementHeight(g);
return o
});
var f=j.grid.registerDataChangeCallback(function(o){if(!o.options.useExternalPagination){o.options.totalItems=o.rows.length
}},[c.dataChange.ROW]);
m.$on("$destroy",f);
var h=function(){m.showingLow=((n.paginationCurrentPage-1)*n.paginationPageSize)+1;
m.showingHigh=Math.min(n.paginationCurrentPage*n.paginationPageSize,n.totalItems)
};
var k=m.$watch("grid.options.totalItems + grid.options.paginationPageSize",h);
var l=m.$watch("grid.options.paginationCurrentPage + grid.options.paginationPageSize",function(p,o){if(p===o){return
}if(!angular.isNumber(n.paginationCurrentPage)||n.paginationCurrentPage<1){n.paginationCurrentPage=1;
return
}if(n.totalItems>0&&n.paginationCurrentPage>m.paginationApi.getTotalPages()){n.paginationCurrentPage=m.paginationApi.getTotalPages();
return
}h();
d.onPaginationChanged(m.grid,n.paginationCurrentPage,n.paginationPageSize)
});
m.$on("$destroy",function(){k();
l()
});
m.cantPageForward=function(){if(n.totalItems>0){return n.paginationCurrentPage>=m.paginationApi.getTotalPages()
}else{return n.data.length<1
}};
m.cantPageToLast=function(){if(n.totalItems>0){return m.cantPageForward()
}else{return true
}};
m.cantPageBackward=function(){return n.paginationCurrentPage<=1
}
}}
}])
})();
(function(){var a=angular.module("ui.grid.pinning",["ui.grid"]);
a.service("uiGridPinningService",["gridUtil","GridRenderContainer","i18nService",function(e,d,c){var b={initializeGrid:function(f){b.defaultGridOptions(f.options);
f.registerColumnBuilder(b.pinningColumnBuilder)
},defaultGridOptions:function(f){f.enablePinning=f.enablePinning!==false
},pinningColumnBuilder:function(i,h,j){i.enablePinning=i.enablePinning===undefined?j.enablePinning:i.enablePinning;
if(i.pinnedLeft){if(h.width==="*"){h.grid.refresh().then(function(){h.renderContainer="left";
h.width=h.grid.canvasWidth/h.grid.columns.length;
h.grid.createLeftContainer()
})
}else{h.renderContainer="left";
h.grid.createLeftContainer()
}}else{if(i.pinnedRight){if(h.width==="*"){h.grid.refresh().then(function(){h.renderContainer="right";
h.width=h.grid.canvasWidth/h.grid.columns.length;
h.grid.createRightContainer()
})
}else{h.renderContainer="right";
h.grid.createRightContainer()
}}}if(!i.enablePinning){return
}var k={name:"ui.grid.pinning.pinLeft",title:c.get().pinning.pinLeft,icon:"ui-grid-icon-left-open",shown:function(){return typeof(this.context.col.renderContainer)==="undefined"||!this.context.col.renderContainer||this.context.col.renderContainer!=="left"
},action:function(){this.context.col.renderContainer="left";
this.context.col.width=this.context.col.drawnWidth;
this.context.col.grid.createLeftContainer();
h.grid.refresh().then(function(){h.grid.refresh()
})
}};
var g={name:"ui.grid.pinning.pinRight",title:c.get().pinning.pinRight,icon:"ui-grid-icon-right-open",shown:function(){return typeof(this.context.col.renderContainer)==="undefined"||!this.context.col.renderContainer||this.context.col.renderContainer!=="right"
},action:function(){this.context.col.renderContainer="right";
this.context.col.width=this.context.col.drawnWidth;
this.context.col.grid.createRightContainer();
h.grid.refresh().then(function(){h.grid.refresh()
})
}};
var f={name:"ui.grid.pinning.unpin",title:c.get().pinning.unpin,icon:"ui-grid-icon-cancel",shown:function(){return typeof(this.context.col.renderContainer)!=="undefined"&&this.context.col.renderContainer!==null&&this.context.col.renderContainer!=="body"
},action:function(){this.context.col.renderContainer=null;
h.grid.refresh().then(function(){h.grid.refresh()
})
}};
if(!e.arrayContainsObjectWithProperty(h.menuItems,"name","ui.grid.pinning.pinLeft")){h.menuItems.push(k)
}if(!e.arrayContainsObjectWithProperty(h.menuItems,"name","ui.grid.pinning.pinRight")){h.menuItems.push(g)
}if(!e.arrayContainsObjectWithProperty(h.menuItems,"name","ui.grid.pinning.unpin")){h.menuItems.push(f)
}}};
return b
}]);
a.directive("uiGridPinning",["gridUtil","uiGridPinningService",function(c,b){return{require:"uiGrid",scope:false,compile:function(){return{pre:function(f,e,d,g){b.initializeGrid(g.grid)
},post:function(f,e,d,g){}}
}}
}])
})();
(function(){var a=angular.module("ui.grid.resizeColumns",["ui.grid"]);
a.service("uiGridResizeColumnsService",["gridUtil","$q","$timeout",function(e,c,d){var b={defaultGridOptions:function(f){f.enableColumnResizing=f.enableColumnResizing!==false;
if(f.enableColumnResize===false){f.enableColumnResizing=false
}},colResizerColumnBuilder:function(h,f,i){var g=[];
h.enableColumnResizing=h.enableColumnResizing===undefined?i.enableColumnResizing:h.enableColumnResizing;
if(h.enableColumnResize===false){h.enableColumnResizing=false
}return c.all(g)
},registerPublicApi:function(f){var g={events:{colResizable:{columnSizeChanged:function(h,i){}}}};
f.api.registerEventsFromObject(g.events)
},fireColumnSizeChanged:function(f,g,h){d(function(){f.api.colResizable.raise.columnSizeChanged(g,h)
})
},findTargetCol:function(h,f,j){var i=h.getRenderContainer();
if(f==="left"){var g=i.visibleColumnCache.indexOf(h);
return i.visibleColumnCache[g-1*j]
}else{return h
}}};
return b
}]);
a.directive("uiGridResizeColumns",["gridUtil","uiGridResizeColumnsService",function(c,b){return{replace:true,priority:0,require:"^uiGrid",scope:false,compile:function(){return{pre:function(f,e,d,g){b.defaultGridOptions(g.grid.options);
g.grid.registerColumnBuilder(b.colResizerColumnBuilder);
b.registerPublicApi(g.grid)
},post:function(f,e,d,g){}}
}}
}]);
a.directive("uiGridHeaderCell",["gridUtil","$templateCache","$compile","$q","uiGridResizeColumnsService","uiGridConstants","$timeout",function(h,c,g,d,f,b,e){return{priority:-10,require:"^uiGrid",compile:function(){return{post:function(r,m,q,o){var i=o.grid;
if(i.options.enableColumnResizing){var k=c.get("ui-grid/columnResizer");
var p=1;
if(i.isRTL()){r.position="left";
p=-1
}var l=function(){var t=m[0].getElementsByClassName("ui-grid-column-resizer");
for(var v=0;
v<t.length;
v++){angular.element(t[v]).remove()
}var w=f.findTargetCol(r.col,"left",p);
var u=r.col.getRenderContainer();
if(w&&u.visibleColumnCache.indexOf(r.col)!==0&&w.colDef.enableColumnResizing!==false){var s=angular.element(k).clone();
s.attr("position","left");
m.prepend(s);
g(s)(r)
}if(r.col.colDef.enableColumnResizing!==false){var x=angular.element(k).clone();
x.attr("position","right");
m.append(x);
g(x)(r)
}};
l();
var n=function(){e(l)
};
var j=i.registerDataChangeCallback(n,[b.dataChange.COLUMN]);
r.$on("$destroy",j)
}}}
}}
}]);
a.directive("uiGridColumnResizer",["$document","gridUtil","uiGridConstants","uiGridResizeColumnsService",function(c,i,h,e){var g=angular.element('<div class="ui-grid-resize-overlay"></div>');
var d,f,b;
if(i.isTouchEnabled()){d="touchstart";
f="touchend";
b="touchmove"
}else{d="mousedown";
f="mouseup";
b="mousemove"
}var j={priority:0,scope:{col:"=",position:"@",renderIndex:"="},require:"?^uiGrid",link:function(w,k,u,q){var n=0,t=0,p=0,r=1;
if(q.grid.isRTL()){w.position="left";
r=-1
}if(w.position==="left"){k.addClass("left")
}else{if(w.position==="right"){k.addClass("right")
}}function l(x){var y=x.getRenderContainer();
y.visibleColumnCache.forEach(function(z){if(z===x){return
}var A=z.colDef;
if(!A.width||(angular.isString(A.width)&&(A.width.indexOf("*")!==-1||A.width.indexOf("%")!==-1))){z.width=z.drawnWidth
}})
}function s(x){q.grid.buildColumns().then(function(){q.grid.refreshCanvas(true).then(function(){q.grid.queueGridRefresh()
})
})
}function v(x,y){var z=y;
if(x.colDef.minWidth&&z<x.colDef.minWidth){z=x.colDef.minWidth
}else{if(x.colDef.maxWidth&&z>x.colDef.maxWidth){z=x.colDef.maxWidth
}}return z
}function o(A,y){if(A.originalEvent){A=A.originalEvent
}A.preventDefault();
t=(A.targetTouches?A.targetTouches[0]:A).clientX-p;
if(t<0){t=0
}else{if(t>q.grid.gridWidth){t=q.grid.gridWidth
}}var x=e.findTargetCol(w.col,w.position,r);
if(x.colDef.enableColumnResizing===false){return
}if(!q.grid.element.hasClass("column-resizing")){q.grid.element.addClass("column-resizing")
}var B=t-n;
var z=parseInt(x.drawnWidth+B*r,10);
t=t+(v(x,z)-z)*r;
g.css({left:t+"px"});
q.fireEvent(h.events.ITEM_DRAGGING)
}function m(A,y){if(A.originalEvent){A=A.originalEvent
}A.preventDefault();
q.grid.element.removeClass("column-resizing");
g.remove();
t=(A.changedTouches?A.changedTouches[0]:A).clientX-p;
var B=t-n;
if(B===0){c.off(f,m);
c.off(b,o);
return
}var x=e.findTargetCol(w.col,w.position,r);
if(x.colDef.enableColumnResizing===false){return
}var z=parseInt(x.drawnWidth+B*r,10);
x.width=v(x,z);
l(x);
s(B);
e.fireColumnSizeChanged(q.grid,x.colDef,B);
c.off(f,m);
c.off(b,o)
}k.on(d,function(y,x){if(y.originalEvent){y=y.originalEvent
}y.stopPropagation();
p=q.grid.element[0].getBoundingClientRect().left;
n=(y.targetTouches?y.targetTouches[0]:y).clientX-p;
q.grid.element.append(g);
g.css({left:n});
c.on(f,m);
c.on(b,o)
});
k.on("dblclick",function(C,A){C.stopPropagation();
var z=e.findTargetCol(w.col,w.position,r);
if(z.colDef.enableColumnResizing===false){return
}var B=0;
var D=0;
var x=i.closestElm(k,".ui-grid-render-container");
var y=x.querySelectorAll("."+h.COL_CLASS_PREFIX+z.uid+" .ui-grid-cell-contents");
Array.prototype.forEach.call(y,function(E){var F;
if(angular.element(E).parent().hasClass("ui-grid-header-cell")){F=angular.element(E).parent()[0].querySelectorAll(".ui-grid-column-menu-button")
}i.fakeElement(E,{},function(H){var J=angular.element(H);
J.attr("style","float: left");
var I=i.elementWidth(J);
if(F){var G=i.elementWidth(F);
I=I+G
}if(I>B){B=I;
D=B-I
}})
});
z.width=v(z,B);
l(z);
s(D);
e.fireColumnSizeChanged(q.grid,z.colDef,D)
});
k.on("$destroy",function(){k.off(d);
k.off("dblclick");
c.off(b,o);
c.off(f,m)
})
}};
return j
}])
})();
(function(){var a=angular.module("ui.grid.rowEdit",["ui.grid","ui.grid.edit","ui.grid.cellNav"]);
a.constant("uiGridRowEditConstants",{});
a.service("uiGridRowEditService",["$interval","$q","uiGridConstants","uiGridRowEditConstants","gridUtil",function(g,e,c,d,f){var b={initializeGrid:function(i,h){h.rowEdit={};
var j={events:{rowEdit:{saveRow:function(k){}}},methods:{rowEdit:{setSavePromise:function(k,l){b.setSavePromise(h,k,l)
},getDirtyRows:function(){return h.rowEdit.dirtyRows?h.rowEdit.dirtyRows:[]
},getErrorRows:function(){return h.rowEdit.errorRows?h.rowEdit.errorRows:[]
},flushDirtyRows:function(){return b.flushDirtyRows(h)
},setRowsDirty:function(k){b.setRowsDirty(h,k)
}}}};
h.api.registerEventsFromObject(j.events);
h.api.registerMethodsFromObject(j.methods);
h.api.core.on.renderingComplete(i,function(k){h.api.edit.on.afterCellEdit(i,b.endEditCell);
h.api.edit.on.beginCellEdit(i,b.beginEditCell);
h.api.edit.on.cancelCellEdit(i,b.cancelEditCell);
if(h.api.cellNav){h.api.cellNav.on.navigate(i,b.navigate)
}})
},defaultGridOptions:function(h){},saveRow:function(i,j){var h=this;
return function(){j.isSaving=true;
var k=i.api.rowEdit.raise.saveRow(j.entity);
if(j.rowEditSavePromise){j.rowEditSavePromise.then(h.processSuccessPromise(i,j),h.processErrorPromise(i,j))
}else{f.logError("A promise was not returned when saveRow event was raised, either nobody is listening to event, or event handler did not return a promise")
}return k
}
},setSavePromise:function(h,i,k){var j=h.getRow(i);
j.rowEditSavePromise=k
},processSuccessPromise:function(i,j){var h=this;
return function(){delete j.isSaving;
delete j.isDirty;
delete j.isError;
delete j.rowEditSaveTimer;
h.removeRow(i.rowEdit.errorRows,j);
h.removeRow(i.rowEdit.dirtyRows,j)
}
},processErrorPromise:function(h,i){return function(){delete i.isSaving;
delete i.rowEditSaveTimer;
i.isError=true;
if(!h.rowEdit.errorRows){h.rowEdit.errorRows=[]
}if(!b.isRowPresent(h.rowEdit.errorRows,i)){h.rowEdit.errorRows.push(i)
}}
},removeRow:function(i,h){angular.forEach(i,function(k,j){if(k.uid===h.uid){i.splice(j,1)
}})
},isRowPresent:function(i,h){var j=false;
angular.forEach(i,function(l,k){if(l.uid===h.uid){j=true
}});
return j
},flushDirtyRows:function(i){var h=[];
angular.forEach(i.rowEdit.dirtyRows,function(j){b.saveRow(i,j)();
h.push(j.rowEditSavePromise)
});
return e.all(h)
},endEditCell:function(l,j,k,h){var i=this.grid;
var m=i.getRow(l);
if(!m){f.logError("Unable to find rowEntity in grid data, dirty flag cannot be set");
return
}if(k!==h||m.isDirty){if(!i.rowEdit.dirtyRows){i.rowEdit.dirtyRows=[]
}if(!m.isDirty){m.isDirty=true;
i.rowEdit.dirtyRows.push(m)
}delete m.isError;
b.considerSetTimer(i,m)
}},beginEditCell:function(j,i){var h=this.grid;
var k=h.getRow(j);
if(!k){f.logError("Unable to find rowEntity in grid data, timer cannot be cancelled");
return
}b.cancelTimer(h,k)
},cancelEditCell:function(j,i){var h=this.grid;
var k=h.getRow(j);
if(!k){f.logError("Unable to find rowEntity in grid data, timer cannot be set");
return
}b.considerSetTimer(h,k)
},navigate:function(j,i){var h=this.grid;
if(j.row.rowEditSaveTimer){b.cancelTimer(h,j.row)
}if(i&&i.row&&i.row!==j.row){b.considerSetTimer(h,i.row)
}},considerSetTimer:function(i,j){b.cancelTimer(i,j);
if(j.isDirty&&!j.isSaving){if(i.options.rowEditWaitInterval!==-1){var h=i.options.rowEditWaitInterval?i.options.rowEditWaitInterval:2000;
j.rowEditSaveTimer=g(b.saveRow(i,j),h,1)
}}},cancelTimer:function(h,i){if(i.rowEditSaveTimer&&!i.isSaving){g.cancel(i.rowEditSaveTimer);
delete i.rowEditSaveTimer
}},setRowsDirty:function(i,h){var j;
h.forEach(function(l,k){j=i.getRow(l);
if(j){if(!i.rowEdit.dirtyRows){i.rowEdit.dirtyRows=[]
}if(!j.isDirty){j.isDirty=true;
i.rowEdit.dirtyRows.push(j)
}delete j.isError;
b.considerSetTimer(i,j)
}else{f.logError("requested row not found in rowEdit.setRowsDirty, row was: "+l)
}})
}};
return b
}]);
a.directive("uiGridRowEdit",["gridUtil","uiGridRowEditService","uiGridEditConstants",function(d,b,c){return{replace:true,priority:0,require:"^uiGrid",scope:false,compile:function(){return{pre:function(g,f,e,h){b.initializeGrid(g,h.grid)
},post:function(g,f,e,h){}}
}}
}]);
a.directive("uiGridViewport",["$compile","uiGridConstants","gridUtil","$parse",function(c,b,e,d){return{priority:-200,scope:false,compile:function(h,f){var g=angular.element(h.children().children()[0]);
var i=g.attr("ng-class");
var j="";
if(i){j=i.slice(0,-1)+", 'ui-grid-row-dirty': row.isDirty, 'ui-grid-row-saving': row.isSaving, 'ui-grid-row-error': row.isError}"
}else{j="{'ui-grid-row-dirty': row.isDirty, 'ui-grid-row-saving': row.isSaving, 'ui-grid-row-error': row.isError}"
}g.attr("ng-class",j);
return{pre:function(m,l,k,n){},post:function(m,l,k,n){}}
}}
}])
})();
(function(){var a=angular.module("ui.grid.saveState",["ui.grid","ui.grid.selection","ui.grid.cellNav"]);
a.constant("uiGridSaveStateConstants",{featureName:"saveState"});
a.service("uiGridSaveStateService",["$q","uiGridSaveStateConstants","gridUtil","$compile","$interval","uiGridConstants",function(d,f,h,e,g,c){var b={initializeGrid:function(i){i.saveState={};
this.defaultGridOptions(i.options);
var j={events:{saveState:{}},methods:{saveState:{save:function(){return b.save(i)
},restore:function(k,l){b.restore(i,k,l)
}}}};
i.api.registerEventsFromObject(j.events);
i.api.registerMethodsFromObject(j.methods)
},defaultGridOptions:function(i){i.saveWidths=i.saveWidths!==false;
i.saveOrder=i.saveOrder!==false;
i.saveScroll=i.saveScroll===true;
i.saveFocus=i.saveScroll!==true&&i.saveFocus!==false;
i.saveVisible=i.saveVisible!==false;
i.saveSort=i.saveSort!==false;
i.saveFilter=i.saveFilter!==false;
i.saveSelection=i.saveSelection!==false
},save:function(i){var j={};
j.columns=b.saveColumns(i);
j.scrollFocus=b.saveScrollFocus(i);
j.selection=b.saveSelection(i);
return j
},restore:function(j,i,k){if(k.columns){b.restoreColumns(j,k.columns)
}if(k.scrollFocus){b.restoreScrollFocus(j,i,k.scrollFocus)
}if(k.selection){b.restoreSelection(j,k.selection)
}j.queueGridRefresh()
},saveColumns:function(j){var i=[];
angular.forEach(j.columns,function(l){var k={};
k.name=l.name;
k.visible=l.visible;
k.width=l.width;
k.sort=angular.copy(l.sort);
k.filters=angular.copy(l.filters);
i.push(k)
});
return i
},saveScrollFocus:function(i){if(!i.api.cellNav){return{}
}var k={};
if(i.options.saveFocus){k.focus=true;
var j=i.api.cellNav.getFocusedCell();
if(j!==null){k.colName=j.col.colDef.name;
k.rowVal=b.getRowVal(i,j.row)
}}else{if(i.options.saveScroll){k.focus=false;
if(i.renderContainers.body.prevRowScrollIndex){k.rowVal=b.getRowVal(i,i.renderContainers.body.visibleRowCache[i.renderContainers.body.prevRowScrollIndex])
}if(i.renderContainers.body.prevColScrollIndex){k.colName=i.renderContainers.body.visibleColumnCache[i.renderContainers.body.prevColScrollIndex].name
}}}return k
},saveSelection:function(i){if(!i.api.selection||!i.options.saveSelection){return{}
}var j=i.api.selection.getSelectedGridRows().map(function(k){return b.getRowVal(i,k)
});
return j
},getRowVal:function(j,k){if(!k){return null
}var i={};
if(j.options.saveRowIdentity){i.identity=true;
i.row=j.options.saveRowIdentity(k.entity)
}else{i.identity=false;
i.row=j.renderContainers.body.visibleRowCache.indexOf(k)
}return i
},restoreColumns:function(i,j){angular.forEach(j,function(o,l){var n=i.columns.filter(function(p){return p.name===o.name
});
if(n.length>0){var k=i.columns.indexOf(n[0]);
if(i.columns[k].visible!==o.visible||i.columns[k].colDef.visible!==o.visible){i.columns[k].visible=o.visible;
i.columns[k].colDef.visible=o.visible;
i.api.core.raise.columnVisibilityChanged(i.columns[k])
}i.columns[k].width=o.width;
if(!angular.equals(i.columns[k].sort,o.sort)&&!(i.columns[k].sort===undefined&&angular.isEmpty(o.sort))){i.columns[k].sort=angular.copy(o.sort);
i.api.core.raise.sortChanged()
}if(!angular.equals(i.columns[k].filters,o.filters)){i.columns[k].filters=angular.copy(o.filters);
i.api.core.raise.filterChanged()
}if(k!==l){var m=i.columns.splice(k,1)[0];
i.columns.splice(l,0,m)
}}})
},restoreScrollFocus:function(m,l,k){if(!m.api.cellNav){return
}var n,o;
if(k.colName){var j=m.options.columnDefs.filter(function(p){return p.name===k.colName
});
if(j.length>0){n=j[0]
}}if(k.rowVal&&k.rowVal.row){if(k.rowVal.identity){o=b.findRowByIdentity(m,k.rowVal)
}else{o=m.renderContainers.body.visibleRowCache[k.rowVal.row]
}}var i=o&&o.entity?o.entity:null;
if(n||i){if(k.focus){m.api.cellNav.scrollToFocus(i,n)
}else{m.api.cellNav.scrollTo(i,n)
}}},restoreSelection:function(j,i){if(!j.api.selection){return
}j.api.selection.clearSelectedRows();
angular.forEach(i,function(k){if(k.identity){var l=b.findRowByIdentity(j,k);
if(l){j.api.selection.selectRow(l.entity)
}}else{j.api.selection.selectRowByVisibleIndex(k.row)
}})
},findRowByIdentity:function(j,i){if(!j.options.saveRowIdentity){return null
}var k=j.rows.filter(function(l){if(j.options.saveRowIdentity(l.entity)===i.row){return true
}else{return false
}});
if(k.length>0){return k[0]
}else{return null
}}};
return b
}]);
a.directive("uiGridSaveState",["uiGridSaveStateConstants","uiGridSaveStateService","gridUtil","$compile",function(d,c,e,b){return{replace:true,priority:0,require:"^uiGrid",scope:false,link:function(h,g,f,i){c.initializeGrid(i.grid)
}}
}])
})();
(function(){var a=angular.module("ui.grid.selection",["ui.grid"]);
a.constant("uiGridSelectionConstants",{featureName:"selection",selectionRowHeaderColName:"selectionRowHeaderCol"});
angular.module("ui.grid").config(["$provide",function(b){b.decorator("GridRow",["$delegate",function(c){c.prototype.setSelected=function(d){this.isSelected=d;
if(d){this.grid.selection.selectedCount++
}else{this.grid.selection.selectedCount--
}};
return c
}])
}]);
a.service("uiGridSelectionService",["$q","$templateCache","uiGridSelectionConstants","gridUtil",function(d,c,f,e){var b={initializeGrid:function(g){g.selection={};
g.selection.lastSelectedRow=null;
g.selection.selectAll=false;
g.selection.selectedCount=0;
b.defaultGridOptions(g.options);
var h={events:{selection:{rowSelectionChanged:function(j,k,i){},rowSelectionChangedBatch:function(j,k,i){}}},methods:{selection:{toggleRowSelection:function(j,i){var k=g.getRow(j);
if(k!==null&&k.enableSelection!==false){b.toggleRowSelection(g,k,i,g.options.multiSelect,g.options.noUnselect)
}},selectRow:function(j,i){var k=g.getRow(j);
if(k!==null&&!k.isSelected&&k.enableSelection!==false){b.toggleRowSelection(g,k,i,g.options.multiSelect,g.options.noUnselect)
}},selectRowByVisibleIndex:function(k,i){var j=g.renderContainers.body.visibleRowCache[k];
if(j!==null&&typeof(j)!=="undefined"&&!j.isSelected&&j.enableSelection!==false){b.toggleRowSelection(g,j,i,g.options.multiSelect,g.options.noUnselect)
}},unSelectRow:function(j,i){var k=g.getRow(j);
if(k!==null&&k.isSelected){b.toggleRowSelection(g,k,i,g.options.multiSelect,g.options.noUnselect)
}},selectAllRows:function(i){if(g.options.multiSelect===false){return
}var j=[];
g.rows.forEach(function(k){if(!k.isSelected&&k.enableSelection!==false){k.setSelected(true);
b.decideRaiseSelectionEvent(g,k,j,i)
}});
b.decideRaiseSelectionBatchEvent(g,j,i);
g.selection.selectAll=true
},selectAllVisibleRows:function(i){if(g.options.multiSelect===false){return
}var j=[];
g.rows.forEach(function(k){if(k.visible){if(!k.isSelected&&k.enableSelection!==false){k.setSelected(true);
b.decideRaiseSelectionEvent(g,k,j,i)
}}else{if(k.isSelected){k.setSelected(false);
b.decideRaiseSelectionEvent(g,k,j,i)
}}});
b.decideRaiseSelectionBatchEvent(g,j,i);
g.selection.selectAll=true
},clearSelectedRows:function(i){b.clearSelectedRows(g,i)
},getSelectedRows:function(){return b.getSelectedRows(g).map(function(i){return i.entity
})
},getSelectedGridRows:function(){return b.getSelectedRows(g)
},setMultiSelect:function(i){g.options.multiSelect=i
},setModifierKeysToMultiSelect:function(i){g.options.modifierKeysToMultiSelect=i
},getSelectAllState:function(){return g.selection.selectAll
}}}};
g.api.registerEventsFromObject(h.events);
g.api.registerMethodsFromObject(h.methods)
},defaultGridOptions:function(g){g.enableRowSelection=g.enableRowSelection!==false;
g.multiSelect=g.multiSelect!==false;
g.noUnselect=g.noUnselect===true;
g.modifierKeysToMultiSelect=g.modifierKeysToMultiSelect===true;
g.enableRowHeaderSelection=g.enableRowHeaderSelection!==false;
g.enableSelectAll=g.enableSelectAll!==false;
g.enableSelectionBatchEvent=g.enableSelectionBatchEvent!==false;
g.selectionRowHeaderWidth=angular.isDefined(g.selectionRowHeaderWidth)?g.selectionRowHeaderWidth:30;
g.enableFooterTotalSelected=g.enableFooterTotalSelected!==false;
g.isRowSelectable=angular.isDefined(g.isRowSelectable)?g.isRowSelectable:angular.noop
},toggleRowSelection:function(h,m,g,l,j){var i=m.isSelected;
if(!l&&!i){b.clearSelectedRows(h,g)
}else{if(!l&&i){var k=b.getSelectedRows(h);
if(k.length>1){i=false;
b.clearSelectedRows(h,g)
}}}if(i&&j){}else{if(m.enableSelection!==false){m.setSelected(!i);
if(m.isSelected===true){h.selection.lastSelectedRow=m
}else{h.selection.selectAll=false
}h.api.selection.raise.rowSelectionChanged(m,g)
}}},shiftSelect:function(g,q,p,j){if(!j){return
}var k=b.getSelectedRows(g);
var h=k.length>0?g.renderContainers.body.visibleRowCache.indexOf(g.selection.lastSelectedRow):0;
var r=g.renderContainers.body.visibleRowCache.indexOf(q);
if(h>r){var n=h;
h=r;
r=n
}var o=[];
for(var m=h;
m<=r;
m++){var l=g.renderContainers.body.visibleRowCache[m];
if(l){if(!l.isSelected&&l.enableSelection!==false){l.setSelected(true);
g.selection.lastSelectedRow=l;
b.decideRaiseSelectionEvent(g,l,o,p)
}}}b.decideRaiseSelectionBatchEvent(g,o,p)
},getSelectedRows:function(g){return g.rows.filter(function(h){return h.isSelected
})
},clearSelectedRows:function(i,g){var h=[];
b.getSelectedRows(i).forEach(function(j){if(j.isSelected){j.setSelected(false);
b.decideRaiseSelectionEvent(i,j,h,g)
}});
b.decideRaiseSelectionBatchEvent(i,h,g);
i.selection.selectAll=false
},decideRaiseSelectionEvent:function(i,j,h,g){if(!i.options.enableSelectionBatchEvent){i.api.selection.raise.rowSelectionChanged(j,g)
}else{h.push(j)
}},decideRaiseSelectionBatchEvent:function(i,h,g){if(h.length>0){i.api.selection.raise.rowSelectionChangedBatch(h,g)
}}};
return b
}]);
a.directive("uiGridSelection",["uiGridSelectionConstants","uiGridSelectionService","$templateCache",function(d,c,b){return{replace:true,priority:0,require:"^uiGrid",scope:false,compile:function(){return{pre:function(g,f,e,i){c.initializeGrid(i.grid);
if(i.grid.options.enableRowHeaderSelection){var h={name:d.selectionRowHeaderColName,displayName:"",width:i.grid.options.selectionRowHeaderWidth,minWidth:10,cellTemplate:"ui-grid/selectionRowHeader",headerCellTemplate:"ui-grid/selectionHeaderCell",enableColumnResizing:false,enableColumnMenu:false,exporterSuppressExport:true,allowCellFocus:true};
i.grid.addRowHeaderColumn(h)
}if(i.grid.options.isRowSelectable!==angular.noop){i.grid.registerRowBuilder(function(k,j){k.enableSelection=i.grid.options.isRowSelectable(k)
})
}},post:function(g,f,e,h){}}
}}
}]);
a.directive("uiGridSelectionRowHeaderButtons",["$templateCache","uiGridSelectionService",function(b,c){return{replace:true,restrict:"E",template:b.get("ui-grid/selectionRowHeaderButtons"),scope:true,require:"^uiGrid",link:function(g,e,d,h){var f=h.grid;
g.selectButtonClick=function(j,i){if(i.shiftKey){c.shiftSelect(f,j,i,f.options.multiSelect)
}else{if(i.ctrlKey||i.metaKey){c.toggleRowSelection(f,j,i,f.options.multiSelect,f.options.noUnselect)
}else{c.toggleRowSelection(f,j,i,(f.options.multiSelect&&!f.options.modifierKeysToMultiSelect),f.options.noUnselect)
}}}
}}
}]);
a.directive("uiGridSelectionSelectAllButtons",["$templateCache","uiGridSelectionService",function(b,c){return{replace:true,restrict:"E",template:b.get("ui-grid/selectionSelectAllButtons"),scope:false,link:function(g,e,d,h){var f=g.col.grid;
g.headerButtonClick=function(j,i){if(f.selection.selectAll){c.clearSelectedRows(f,i);
if(f.options.noUnselect){f.api.selection.selectRowByVisibleIndex(0,i)
}f.selection.selectAll=false
}else{if(f.options.multiSelect){f.api.selection.selectAllVisibleRows(i);
f.selection.selectAll=true
}}}
}}
}]);
a.directive("uiGridViewport",["$compile","uiGridConstants","uiGridSelectionConstants","gridUtil","$parse","uiGridSelectionService",function(d,b,g,f,e,c){return{priority:-200,scope:false,compile:function(j,h){var i=angular.element(j.children().children()[0]);
var k=i.attr("ng-class");
var l="";
if(k){l=k.slice(0,-1)+",'ui-grid-row-selected': row.isSelected}"
}else{l="{'ui-grid-row-selected': row.isSelected}"
}i.attr("ng-class",l);
return{pre:function(o,n,m,p){},post:function(o,n,m,p){}}
}}
}]);
a.directive("uiGridCell",["$compile","uiGridConstants","uiGridSelectionConstants","gridUtil","$parse","uiGridSelectionService",function(d,b,g,f,e,c){return{priority:-200,restrict:"A",scope:false,link:function(r,m,p){var o=0;
var k=300;
m.bind("keydown",function(s){if(s.keyCode===32&&r.col.colDef.name==="selectionRowHeaderCol"){c.toggleRowSelection(r.grid,r.row,s,(r.grid.options.multiSelect&&!r.grid.options.modifierKeysToMultiSelect),r.grid.options.noUnselect);
r.$apply()
}});
var j=function(s){if(s.shiftKey){c.shiftSelect(r.grid,r.row,s,r.grid.options.multiSelect)
}else{if(s.ctrlKey||s.metaKey){c.toggleRowSelection(r.grid,r.row,s,r.grid.options.multiSelect,r.grid.options.noUnselect)
}else{c.toggleRowSelection(r.grid,r.row,s,(r.grid.options.multiSelect&&!r.grid.options.modifierKeysToMultiSelect),r.grid.options.noUnselect)
}}r.$apply()
};
var l=function(s){o=(new Date()).getTime()
};
var q=function(s){var u=(new Date()).getTime();
var t=u-o;
if(t<k){j(s)
}};
function n(){if(r.grid.options.enableRowSelection&&!r.grid.options.enableRowHeaderSelection){m.addClass("ui-grid-disable-selection");
m.on("touchstart",l);
m.on("touchend",q);
m.on("click",j);
r.registered=true
}}function i(){if(r.registered){m.removeClass("ui-grid-disable-selection");
m.off("touchstart",l);
m.off("touchend",q);
m.off("click",j);
r.registered=false
}}n();
var h=r.grid.registerDataChangeCallback(function(){if(r.grid.options.enableRowSelection&&!r.grid.options.enableRowHeaderSelection&&!r.registered){n()
}else{if((!r.grid.options.enableRowSelection||r.grid.options.enableRowHeaderSelection)&&r.registered){i()
}}},[b.dataChange.OPTIONS]);
m.on("$destroy",h)
}}
}]);
a.directive("uiGridGridFooter",["$compile","uiGridConstants","gridUtil",function(c,b,d){return{restrict:"EA",replace:true,priority:-1000,require:"^uiGrid",scope:true,compile:function(f,e){return{pre:function(i,h,g,j){if(!j.grid.options.showGridFooter){return
}d.getTemplate("ui-grid/gridFooterSelectedItems").then(function(m){var l=angular.element(m);
var k=c(l)(i);
angular.element(h[0].getElementsByClassName("ui-grid-grid-footer")[0]).append(k)
})
},post:function(i,h,g,j){}}
}}
}])
})();
angular.module("ui.grid").run(["$templateCache",function(a){a.put("ui-grid/ui-grid-footer",'<div class="ui-grid-footer-panel ui-grid-footer-aggregates-row"><div class="ui-grid-footer ui-grid-footer-viewport"><div class="ui-grid-footer-canvas"><div class="ui-grid-footer-cell-wrapper" ng-style="colContainer.headerCellWrapperStyle()"><div class="ui-grid-footer-cell-row"><div ng-repeat="col in colContainer.renderedColumns track by col.colDef.name" ui-grid-footer-cell col="col" render-index="$index" class="ui-grid-footer-cell ui-grid-clearfix"></div></div></div></div></div></div>');
a.put("ui-grid/ui-grid-grid-footer",'<div class="ui-grid-footer-info ui-grid-grid-footer"><span>{{\'search.totalItems\' | t}} {{grid.rows.length}}</span> <span ng-if="grid.renderContainers.body.visibleRowCache.length !== grid.rows.length" class="ngLabel">({{"search.showingItems" | t}} {{grid.renderContainers.body.visibleRowCache.length}})</span></div>');
a.put("ui-grid/ui-grid-group-panel",'<div class="ui-grid-group-panel"><div ui-t="groupPanel.description" class="description" ng-show="groupings.length == 0"></div><ul ng-show="groupings.length > 0" class="ngGroupList"><li class="ngGroupItem" ng-repeat="group in configGroups"><span class="ngGroupElement"><span class="ngGroupName">{{group.displayName}} <span ng-click="removeGroup($index)" class="ngRemoveGroup">x</span></span> <span ng-hide="$last" class="ngGroupArrow"></span></span></li></ul></div>');
a.put("ui-grid/ui-grid-header",'<div class="ui-grid-header"><div class="ui-grid-top-panel"><div class="ui-grid-header-viewport"><div class="ui-grid-header-canvas"><div class="ui-grid-header-cell-wrapper" ng-style="colContainer.headerCellWrapperStyle()"><div class="ui-grid-header-cell-row"><div class="ui-grid-header-cell ui-grid-clearfix" ng-repeat="col in colContainer.renderedColumns track by col.colDef.name" ui-grid-header-cell col="col" render-index="$index"></div></div></div></div></div><div ui-grid-menu></div></div></div>');
a.put("ui-grid/ui-grid-menu-button",'<div class="ui-grid-menu-button" ng-click="toggleMenu()"><div class="ui-grid-icon-container"><i class="ui-grid-icon-menu">&nbsp;</i></div><div ui-grid-menu menu-items="menuItems"></div></div>');
a.put("ui-grid/ui-grid-no-header",'<div class="ui-grid-top-panel"></div>');
a.put("ui-grid/ui-grid-row",'<div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" ui-grid-cell></div>');
a.put("ui-grid/ui-grid",'<div ui-i18n="en" class="ui-grid"><!-- TODO (c0bra): add "scoped" attr here, eventually? --><style ui-grid-style>.grid{{ grid.id }} {\n      /* Styles for the grid */\n    }\n\n    .grid{{ grid.id }} .ui-grid-row, .grid{{ grid.id }} .ui-grid-cell, .grid{{ grid.id }} .ui-grid-cell .ui-grid-vertical-bar {\n      height: {{ grid.options.rowHeight }}px;\n    }\n\n    .grid{{ grid.id }} .ui-grid-row:last-child .ui-grid-cell {\n      border-bottom-width: {{ ((grid.getTotalRowHeight() < grid.getViewportHeight()) && \'1\') || \'0\' }}px;\n    }\n\n    {{ grid.verticalScrollbarStyles }}\n    {{ grid.horizontalScrollbarStyles }}\n\n    /*\n    .ui-grid[dir=rtl] .ui-grid-viewport {\n      padding-left: {{ grid.verticalScrollbarWidth }}px;\n    }\n    */\n\n    {{ grid.customStyles }}</style><div ui-grid-menu-button ng-if="grid.options.enableGridMenu"></div><div ng-if="grid.hasLeftContainer()" style="width: 0" ui-grid-pinned-container="\'left\'"></div><div ui-grid-render-container container-id="\'body\'" col-container-name="\'body\'" row-container-name="\'body\'" bind-scroll-horizontal="true" bind-scroll-vertical="true" enable-horizontal-scrollbar="grid.options.enableHorizontalScrollbar" enable-vertical-scrollbar="grid.options.enableVerticalScrollbar"></div><div ng-if="grid.hasRightContainer()" style="width: 0" ui-grid-pinned-container="\'right\'"></div><div ui-grid-grid-footer ng-if="grid.options.showGridFooter"></div><div ui-grid-column-menu ng-if="grid.options.enableColumnMenus"></div><div ng-transclude></div></div>');
a.put("ui-grid/uiGridCell",'<div class="ui-grid-cell-contents">{{COL_FIELD CUSTOM_FILTERS}}</div>');
a.put("ui-grid/uiGridColumnFilter",'<li class="ui-grid-menu-item ui-grid-clearfix ui-grid-column-filter" ng-show="itemShown()" ng-click="$event.stopPropagation();"><div class="input-container"><input class="column-filter-input" type="text" ng-model="item.model" placeholder="{{ i18n.search.placeholder }}"><div class="column-filter-cancel-icon-container"><i class="ui-grid-filter-cancel ui-grid-icon-cancel column-filter-cancel-icon">&nbsp;</i></div></div><div style="button-container" ng-click="item.action($event)"><div class="ui-grid-button"><i class="ui-grid-icon-search">&nbsp;</i></div></div></li>');
a.put("ui-grid/uiGridColumnMenu",'<div class="ui-grid-column-menu"><div ui-grid-menu menu-items="menuItems"><!-- <div class="ui-grid-column-menu">\n    <div class="inner" ng-show="menuShown">\n      <ul>\n        <div ng-show="grid.options.enableSorting">\n          <li ng-click="sortColumn($event, asc)" ng-class="{ \'selected\' : col.sort.direction == asc }"><i class="ui-grid-icon-sort-alt-up"></i> Sort Ascending</li>\n          <li ng-click="sortColumn($event, desc)" ng-class="{ \'selected\' : col.sort.direction == desc }"><i class="ui-grid-icon-sort-alt-down"></i> Sort Descending</li>\n          <li ng-show="col.sort.direction" ng-click="unsortColumn()"><i class="ui-grid-icon-cancel"></i> Remove Sort</li>\n        </div>\n      </ul>\n    </div>\n  </div> --></div></div>');
a.put("ui-grid/uiGridFooterCell",'<div class="ui-grid-cell-contents" col-index="renderIndex"><div>{{ col.getAggregationText() + ( col.getAggregationValue() CUSTOM_FILTERS ) }}</div></div>');
a.put("ui-grid/uiGridHeaderCell",'<div ng-class="{ \'sortable\': sortable }"><!-- <div class="ui-grid-vertical-bar">&nbsp;</div> --><div class="ui-grid-cell-contents" col-index="renderIndex"><span>{{ col.displayName CUSTOM_FILTERS }}</span> <span ui-grid-visible="col.sort.direction" ng-class="{ \'ui-grid-icon-up-dir\': col.sort.direction == asc, \'ui-grid-icon-down-dir\': col.sort.direction == desc, \'ui-grid-icon-blank\': !col.sort.direction }">&nbsp;</span></div><div class="ui-grid-column-menu-button" ng-if="grid.options.enableColumnMenus && !col.isRowHeader  && col.colDef.enableColumnMenu !== false" ng-click="toggleMenu($event)" ng-class="{\'ui-grid-column-menu-button-last-col\': isLastCol}"><i class="ui-grid-icon-angle-down">&nbsp;</i></div><div ng-if="filterable" class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><input type="text" class="ui-grid-filter-input" ng-model="colFilter.term" ng-attr-placeholder="{{colFilter.placeholder || \'\'}}"><div class="ui-grid-filter-button" ng-click="colFilter.term = null"><i class="ui-grid-icon-cancel" ng-show="!!colFilter.term">&nbsp;</i><!-- use !! because angular interprets \'f\' as false --></div></div></div>');
a.put("ui-grid/uiGridMenu",'<div class="ui-grid-menu" ng-if="shown"><div class="ui-grid-menu-mid" ng-show="shownMid"><div class="ui-grid-menu-inner"><ul class="ui-grid-menu-items"><li ng-repeat="item in menuItems" ui-grid-menu-item action="item.action" name="item.title" active="item.active" icon="item.icon" shown="item.shown" context="item.context" template-url="item.templateUrl"></li></ul></div></div></div>');
a.put("ui-grid/uiGridMenuItem",'<li class="ui-grid-menu-item" ng-click="itemAction($event, title)" ng-show="itemShown()" ng-class="{ \'ui-grid-menu-item-active\' : active() }"><i ng-class="icon"></i> {{ name }}</li>');
a.put("ui-grid/uiGridRenderContainer",'<div class="ui-grid-render-container"><div ui-grid-header></div><div ui-grid-viewport></div><div ui-grid-footer ng-if="grid.options.showColumnFooter"></div></div>');
a.put("ui-grid/uiGridViewport",'<div class="ui-grid-viewport" ng-style="colContainer.getViewPortStyle()"><div class="ui-grid-canvas"><div ng-repeat="(rowRenderIndex, row) in rowContainer.renderedRows track by $index" class="ui-grid-row" ng-style="Viewport.rowStyle(rowRenderIndex)"><div ui-grid-row="row" row-render-index="rowRenderIndex"></div></div></div></div>');
a.put("ui-grid/cellEditor",'<div><form name="inputForm"><input type="INPUT_TYPE" ng-class="\'colt\' + col.uid" ui-grid-editor ng-model="MODEL_COL_FIELD"></form></div>');
a.put("ui-grid/dropdownEditor",'<div><form name="inputForm"><select ng-class="\'colt\' + col.uid" ui-grid-edit-dropdown ng-model="MODEL_COL_FIELD" ng-options="field[editDropdownIdLabel] as field[editDropdownValueLabel] CUSTOM_FILTERS for field in editDropdownOptionsArray"></select></form></div>');
a.put("ui-grid/expandableRow",'<div ui-grid-expandable-row ng-if="expandableRow.shouldRenderExpand()" class="expandableRow" style="float:left; margin-top: 1px; margin-bottom: 1px" ng-style="{width: (grid.renderContainers.body.getCanvasWidth()) + \'px\'\n     , height: grid.options.expandableRowHeight + \'px\'}"></div>');
a.put("ui-grid/expandableRowHeader",'<div class="ui-grid-row-header-cell ui-grid-expandable-buttons-cell"><div class="ui-grid-cell-contents"><i ng-class="{ \'ui-grid-icon-plus-squared\' : !row.isExpanded, \'ui-grid-icon-minus-squared\' : row.isExpanded }" ng-click="grid.api.expandable.toggleRowExpansion(row.entity)"></i></div></div>');
a.put("ui-grid/expandableScrollFiller","<div ng-if=\"expandableRow.shouldRenderFiller()\" style=\"float:left; margin-top: 2px; margin-bottom: 2px\" ng-style=\"{ width: (grid.getViewportWidth()) + 'px',\n              height: grid.options.expandableRowHeight + 'px', 'margin-left': grid.options.rowHeader.rowHeaderWidth + 'px' }\"><i class=\"ui-grid-icon-spin5 ui-grid-animate-spin\" ng-style=\"{ 'margin-top': ( grid.options.expandableRowHeight/2 - 5) + 'px',\n            'margin-left' : ((grid.getViewportWidth() - grid.options.rowHeader.rowHeaderWidth)/2 - 5) + 'px' }\"></i></div>");
a.put("ui-grid/expandableTopRowHeader",'<div class="ui-grid-row-header-cell ui-grid-expandable-buttons-cell"><div class="ui-grid-cell-contents"><i ng-class="{ \'ui-grid-icon-plus-squared\' : !grid.expandable.expandedAll, \'ui-grid-icon-minus-squared\' : grid.expandable.expandedAll }" ng-click="grid.api.expandable.toggleAllRows()"></i></div></div>');
a.put("ui-grid/csvLink",'<span class="ui-grid-exporter-csv-link-span"><a href="data:text/csv;charset=UTF-8,CSV_CONTENT" download="FILE_NAME">LINK_LABEL</a></span>');
a.put("ui-grid/groupingExpandAllButtons",'<div class="ui-grid-grouping-row-header-buttons" ng-class="{\'ui-grid-icon-minus-squared\': grid.grouping.expandAll, \'ui-grid-icon-plus-squared\': !grid.grouping.expandAll}" ng-click="headerButtonClick($event)"></div>');
a.put("ui-grid/groupingHeaderCell",'<div><!-- <div class="ui-grid-vertical-bar">&nbsp;</div> --><div class="ui-grid-cell-contents" col-index="renderIndex"><ui-grid-grouping-expand-all-buttons></ui-grid-grouping-expand-all-buttons></div></div>');
a.put("ui-grid/groupingRowHeader",'<div class="ui-grid-cell-contents"><ui-grid-grouping-row-header-buttons></ui-grid-grouping-row-header-buttons></div>');
a.put("ui-grid/groupingRowHeaderButtons","<div class=\"ui-grid-grouping-row-header-buttons\" ng-class=\"{'ui-grid-group-header': row.groupLevel > - 1}\" ng-click=\"groupButtonClick(row, $event)\"><i ng-class=\"{'ui-grid-icon-minus-squared': row.expandedState.state === 'expanded', 'ui-grid-icon-plus-squared': row.expandedState.state === 'collapsed'}\" ng-style=\"{'padding-left': grid.options.groupingIndent * row.groupLevel + 'px'}\"></i> &nbsp;</div>");
a.put("ui-grid/importerMenuItem",'<li class="ui-grid-menu-item"><form><input class="ui-grid-importer-file-chooser" type="file" id="files" name="files[]"></form></li>');
a.put("ui-grid/importerMenuItemContainer","<div ui-grid-importer-menu-item></div>");
a.put("ui-grid/pagination",'<div class="ui-grid-pager-panel" ui-grid-pager ng-show="grid.options.enablePaginationControls"><div class="ui-grid-pager-container"><div class="ui-grid-pager-control"><button type="button" ng-click="paginationApi.seek(1)" ng-disabled="cantPageBackward()"><div class="first-triangle"><div class="first-bar"></div></div></button> <button type="button" ng-click="paginationApi.previousPage()" ng-disabled="cantPageBackward()"><div class="first-triangle prev-triangle"></div></button> <input type="number" ng-model="grid.options.paginationCurrentPage" min="1" max="{{ paginationApi.getTotalPages() }}" required> <span class="ui-grid-pager-max-pages-number" ng-show="paginationApi.getTotalPages() > 0">/ {{ paginationApi.getTotalPages() }}</span> <button type="button" ng-click="paginationApi.nextPage()" ng-disabled="cantPageForward()"><div class="last-triangle next-triangle"></div></button> <button type="button" ng-click="paginationApi.seek(paginationApi.getTotalPages())" ng-disabled="cantPageToLast()"><div class="last-triangle"><div class="last-bar"></div></div></button></div><div class="ui-grid-pager-row-count-picker"><select ng-model="grid.options.paginationPageSize" ng-options="o as o for o in grid.options.paginationPageSizes"></select><span class="ui-grid-pager-row-count-label">&nbsp;{{sizesLabel}}</span></div></div><div class="ui-grid-pager-count-container"><div class="ui-grid-pager-count"><span ng-show="grid.options.totalItems > 0">{{showingLow}} - {{showingHigh}} of {{grid.options.totalItems}} {{totalItemsLabel}}</span></div></div></div>');
a.put("ui-grid/columnResizer",'<div ui-grid-column-resizer ng-if="grid.options.enableColumnResizing" class="ui-grid-column-resizer" col="col" position="right" render-index="renderIndex"></div>');
a.put("ui-grid/gridFooterSelectedItems",'<span ng-if="grid.selection.selectedCount !== 0 && grid.options.enableFooterTotalSelected">({{"search.selectedItems" | t}} {{grid.selection.selectedCount}})</span>');
a.put("ui-grid/selectionHeaderCell",'<div><!-- <div class="ui-grid-vertical-bar">&nbsp;</div> --><div class="ui-grid-cell-contents" col-index="renderIndex"><ui-grid-selection-select-all-buttons ng-if="grid.options.enableSelectAll"></ui-grid-selection-select-all-buttons></div></div>');
a.put("ui-grid/selectionRowHeader",'<div class="ui-grid-disable-selection"><div class="ui-grid-cell-contents"><ui-grid-selection-row-header-buttons></ui-grid-selection-row-header-buttons></div></div>');
a.put("ui-grid/selectionRowHeaderButtons",'<div class="ui-grid-selection-row-header-buttons ui-grid-icon-ok" ng-class="{\'ui-grid-row-selected\': row.isSelected}" ng-click="selectButtonClick(row, $event)">&nbsp;</div>');
a.put("ui-grid/selectionSelectAllButtons",'<div class="ui-grid-selection-row-header-buttons ui-grid-icon-ok" ng-class="{\'ui-grid-all-selected\': grid.selection.selectAll}" ng-click="headerButtonClick($event)"></div>')
}]);