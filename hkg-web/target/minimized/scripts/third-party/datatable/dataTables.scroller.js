/* Scroller 1.2.1
 * 2011-2014 SpryMedia Ltd - datatables.net/license
 */
(function(c,a,d){var b=function(h,g){var f=function(j,i){if(!this instanceof f){alert("Scroller warning: Scroller must be initialised with the 'new' keyword.");
return
}if(typeof i=="undefined"){i={}
}this.s={dt:j,tableTop:0,tableBottom:0,redrawTop:0,redrawBottom:0,autoHeight:true,viewportRows:0,stateTO:null,drawTO:null,heights:{jump:null,page:null,virtual:null,scroll:null,row:null,viewport:null},topRowFloat:0,scrollDrawDiff:null};
this.s=h.extend(this.s,f.oDefaults,i);
this.s.heights.row=this.s.rowHeight;
this.dom={force:a.createElement("div"),scroller:null,table:null};
this.s.dt.oScroller=this;
this._fnConstruct()
};
f.prototype={fnRowToPixels:function(i,l,k){var m;
if(k){m=this._domain("virtualToPhysical",i*this.s.heights.row)
}else{var j=i-this.s.baseRowTop;
m=this.s.baseScrollTop+(j*this.s.heights.row)
}return l||l===d?parseInt(m,10):m
},fnPixelsToRow:function(m,k,j){var i=m-this.s.baseScrollTop;
var l=j?this._domain("physicalToVirtual",m)/this.s.heights.row:(i/this.s.heights.row)+this.s.baseRowTop;
return k||k===d?parseInt(l,10):l
},fnScrollToRow:function(o,n){var m=this;
var j=false;
var l=this.fnRowToPixels(o);
var i=((this.s.displayBuffer-1)/2)*this.s.viewportRows;
var k=o-i;
if(k<0){k=0
}if((l>this.s.redrawBottom||l<this.s.redrawTop)&&this.s.dt._iDisplayStart!==k){j=true;
l=this.fnRowToPixels(o,false,true)
}if(typeof n=="undefined"||n){this.s.ani=j;
h(this.dom.scroller).animate({scrollTop:l},function(){setTimeout(function(){m.s.ani=false
},0)
})
}else{h(this.dom.scroller).scrollTop(l)
}},fnMeasure:function(j){if(this.s.autoHeight){this._fnCalcRowHeight()
}var i=this.s.heights;
i.viewport=h(this.dom.scroller).height();
this.s.viewportRows=parseInt(i.viewport/i.row,10)+1;
this.s.dt._iDisplayLength=this.s.viewportRows*this.s.displayBuffer;
if(typeof j=="undefined"||j){this.s.dt.oInstance.fnDraw()
}},_fnConstruct:function(){var i=this;
if(!this.s.dt.oFeatures.bPaginate){this.s.dt.oApi._fnLog(this.s.dt,0,"Pagination must be enabled for Scroller");
return
}this.dom.force.style.position="absolute";
this.dom.force.style.top="0px";
this.dom.force.style.left="0px";
this.dom.force.style.width="1px";
this.dom.scroller=h("div."+this.s.dt.oClasses.sScrollBody,this.s.dt.nTableWrapper)[0];
this.dom.scroller.appendChild(this.dom.force);
this.dom.scroller.style.position="relative";
this.dom.table=h(">table",this.dom.scroller)[0];
this.dom.table.style.position="absolute";
this.dom.table.style.top="0px";
this.dom.table.style.left="0px";
h(this.s.dt.nTableWrapper).addClass("DTS");
if(this.s.loadingIndicator){h(this.dom.scroller.parentNode).css("position","relative").append('<div class="DTS_Loading">'+this.s.dt.oLanguage.sLoadingRecords+"</div>")
}if(this.s.heights.row&&this.s.heights.row!="auto"){this.s.autoHeight=false
}this.fnMeasure(false);
h(this.dom.scroller).on("scroll.DTS",function(){i._fnScroll.call(i)
});
h(this.dom.scroller).on("touchstart.DTS",function(){i._fnScroll.call(i)
});
this.s.dt.aoDrawCallback.push({fn:function(){if(i.s.dt.bInitialised){i._fnDrawCallback.call(i)
}},sName:"Scroller"});
h(c).on("resize.DTS",function(){i._fnInfo()
});
var j=true;
this.s.dt.oApi._fnCallbackReg(this.s.dt,"aoStateSaveParams",function(k,l){if(j&&i.s.dt.oLoadedState){l.iScroller=i.s.dt.oLoadedState.iScroller;
j=false
}else{l.iScroller=i.dom.scroller.scrollTop
}},"Scroller_State");
this.s.dt.aoDestroyCallback.push({sName:"Scroller",fn:function(){h(c).off("resize.DTS");
h(i.dom.scroller).off("touchstart.DTS scroll.DTS");
h(i.s.dt.nTableWrapper).removeClass("DTS");
h("div.DTS_Loading",i.dom.scroller.parentNode).remove();
i.dom.table.style.position="";
i.dom.table.style.top="";
i.dom.table.style.left=""
}})
},_fnScroll:function(){var l=this,m=this.s.heights,k=this.dom.scroller.scrollTop,n;
if(this.s.skip){return
}if(this.s.dt.bFiltered||this.s.dt.bSorted){this.s.lastScrollTop=0;
return
}this._fnInfo();
clearTimeout(this.s.stateTO);
this.s.stateTO=setTimeout(function(){l.s.dt.oApi._fnSaveState(l.s.dt)
},250);
if(k<this.s.redrawTop||k>this.s.redrawBottom){var j=Math.ceil(((this.s.displayBuffer-1)/2)*this.s.viewportRows);
if(Math.abs(k-this.s.lastScrollTop)>m.viewport||this.s.ani){n=parseInt(this._domain("physicalToVirtual",k)/m.row,10)-j;
this.s.topRowFloat=(this._domain("physicalToVirtual",k)/m.row)
}else{n=this.fnPixelsToRow(k)-j;
this.s.topRowFloat=this.fnPixelsToRow(k,false)
}if(n<=0){n=0
}else{if(n+this.s.dt._iDisplayLength>this.s.dt.fnRecordsDisplay()){n=this.s.dt.fnRecordsDisplay()-this.s.dt._iDisplayLength;
if(n<0){n=0
}}else{if(n%2!==0){n++
}}}if(n!=this.s.dt._iDisplayStart){this.s.tableTop=h(this.s.dt.nTable).offset().top;
this.s.tableBottom=h(this.s.dt.nTable).height()+this.s.tableTop;
var i=function(){if(l.s.scrollDrawReq===null){l.s.scrollDrawReq=k
}l.s.dt._iDisplayStart=n;
if(l.s.dt.oApi._fnCalculateEnd){l.s.dt.oApi._fnCalculateEnd(l.s.dt)
}l.s.dt.oApi._fnDraw(l.s.dt)
};
if(this.s.dt.oFeatures.bServerSide){clearTimeout(this.s.drawTO);
this.s.drawTO=setTimeout(i,this.s.serverWait)
}else{i()
}}}this.s.lastScrollTop=k
},_domain:function(j,m){var l=this.s.heights;
var i;
if(l.virtual===l.scroll){i=(l.virtual-l.viewport)/(l.scroll-l.viewport);
if(j==="virtualToPhysical"){return m/i
}else{if(j==="physicalToVirtual"){return m*i
}}}var n=(l.scroll-l.viewport)/2;
var k=(l.virtual-l.viewport)/2;
i=k/(n*n);
if(j==="virtualToPhysical"){if(m<k){return Math.pow(m/i,0.5)
}else{m=(k*2)-m;
return m<0?l.scroll:(n*2)-Math.pow(m/i,0.5)
}}else{if(j==="physicalToVirtual"){if(m<n){return m*m*i
}else{m=(n*2)-m;
return m<0?l.virtual:(k*2)-(m*m*i)
}}}},_fnDrawCallback:function(){var m=this,n=this.s.heights,l=this.dom.scroller.scrollTop,s=l,j=l+n.viewport,q=h(this.s.dt.nTable).height(),t=this.s.dt._iDisplayStart,k=this.s.dt._iDisplayLength,i=this.s.dt.fnRecordsDisplay();
this.s.skip=true;
this._fnScrollForce();
if(t===0){l=this.s.topRowFloat*n.row
}else{if(t+k>=i){l=n.scroll-((i-this.s.topRowFloat)*n.row)
}else{l=this._domain("virtualToPhysical",this.s.topRowFloat*n.row)
}}this.dom.scroller.scrollTop=l;
this.s.baseScrollTop=l;
this.s.baseRowTop=this.s.topRowFloat;
var r=l-((this.s.topRowFloat-t)*n.row);
if(t===0){r=0
}else{if(t+k>=i){r=n.scroll-q
}}this.dom.table.style.top=r+"px";
this.s.tableTop=r;
this.s.tableBottom=q+this.s.tableTop;
var p=(l-this.s.tableTop)*this.s.boundaryScale;
this.s.redrawTop=l-p;
this.s.redrawBottom=l+p;
this.s.skip=false;
setTimeout(function(){m._fnInfo.call(m)
},0);
if(this.s.dt.oFeatures.bStateSave&&this.s.dt.oLoadedState!==null&&typeof this.s.dt.oLoadedState.iScroller!="undefined"){var o=this.s.dt.sAjaxSource||m.s.dt.ajax?true:false;
if((o&&this.s.dt.iDraw==2)||(!o&&this.s.dt.iDraw==1)){setTimeout(function(){h(m.dom.scroller).scrollTop(m.s.dt.oLoadedState.iScroller);
m.s.redrawTop=m.s.dt.oLoadedState.iScroller-(n.viewport/2)
},0)
}}},_fnScrollForce:function(){var j=this.s.heights;
var i=1000000;
j.virtual=j.row*this.s.dt.fnRecordsDisplay();
j.scroll=j.virtual;
if(j.scroll>i){j.scroll=i
}this.dom.force.style.height=j.scroll+"px"
},_fnCalcRowHeight:function(){var i=this.s.dt.nTable;
var l=i.cloneNode(false);
var k=h("<tbody/>").appendTo(l);
var j=h('<div class="'+this.s.dt.oClasses.sWrapper+' DTS"><div class="'+this.s.dt.oClasses.sScrollWrapper+'"><div class="'+this.s.dt.oClasses.sScrollBody+'"></div></div></div>');
h("tbody tr:lt(4)",i).clone().appendTo(k);
while(h("tr",k).length<3){k.append("<tr><td>&nbsp;</td></tr>")
}h("div."+this.s.dt.oClasses.sScrollBody,j).append(l);
j.appendTo(this.s.dt.nHolding);
this.s.heights.row=h("tr",k).eq(1).outerHeight();
j.remove()
},_fnInfo:function(){if(!this.s.dt.oFeatures.bInfo){return
}var k=this.s.dt,q=this.dom.scroller.scrollTop,j=Math.floor(this.fnPixelsToRow(q,false,this.s.ani)+1),r=k.fnRecordsTotal(),v=k.fnRecordsDisplay(),s=Math.ceil(this.fnPixelsToRow(q+this.s.heights.viewport,false,this.s.ani)),y=v<s?v:s,l=k.fnFormatNumber(j),p=k.fnFormatNumber(y),x=k.fnFormatNumber(r),w=k.fnFormatNumber(v),u;
if(k.fnRecordsDisplay()===0&&k.fnRecordsDisplay()==k.fnRecordsTotal()){u=k.oLanguage.sInfoEmpty+k.oLanguage.sInfoPostFix
}else{if(k.fnRecordsDisplay()===0){u=k.oLanguage.sInfoEmpty+" "+k.oLanguage.sInfoFiltered.replace("_MAX_",x)+k.oLanguage.sInfoPostFix
}else{if(k.fnRecordsDisplay()==k.fnRecordsTotal()){u=k.oLanguage.sInfo.replace("_START_",l).replace("_END_",p).replace("_TOTAL_",w)+k.oLanguage.sInfoPostFix
}else{u=k.oLanguage.sInfo.replace("_START_",l).replace("_END_",p).replace("_TOTAL_",w)+" "+k.oLanguage.sInfoFiltered.replace("_MAX_",k.fnFormatNumber(k.fnRecordsTotal()))+k.oLanguage.sInfoPostFix
}}}var o=k.aanFeatures.i;
if(typeof o!="undefined"){for(var t=0,m=o.length;
t<m;
t++){h(o[t]).html(u)
}}}};
f.defaults={trace:false,rowHeight:"auto",serverWait:200,displayBuffer:9,boundaryScale:0.5,loadingIndicator:false};
f.oDefaults=f.defaults;
f.version="1.2.1";
if(typeof h.fn.dataTable=="function"&&typeof h.fn.dataTableExt.fnVersionCheck=="function"&&h.fn.dataTableExt.fnVersionCheck("1.9.0")){h.fn.dataTableExt.aoFeatures.push({fnInit:function(l){var k=l.oInit;
var j=k.scroller||k.oScroller||{};
var i=new f(l,j);
return i.dom.wrapper
},cFeature:"S",sFeature:"Scroller"})
}else{alert("Warning: Scroller requires DataTables 1.9.0 or greater - www.datatables.net/download")
}h.fn.dataTable.Scroller=f;
h.fn.DataTable.Scroller=f;
if(h.fn.dataTable.Api){var e=h.fn.dataTable.Api;
e.register("scroller().rowToPixels()",function(j,l,k){var i=this.context;
if(i.length&&i[0].oScroller){return i[0].oScroller.fnRowToPixels(j,l,k)
}});
e.register("scroller().pixelsToRow()",function(l,k,j){var i=this.context;
if(i.length&&i[0].oScroller){return i[0].oScroller.fnPixelsToRow(l,k,j)
}});
e.register("scroller().scrollToRow()",function(j,i){this.iterator("table",function(k){if(k.oScroller){k.oScroller.fnScrollToRow(j,i)
}});
return this
});
e.register("scroller().measure()",function(i){this.iterator("table",function(j){if(j.oScroller){j.oScroller.fnMeasure(i)
}});
return this
})
}return f
};
if(typeof define==="function"&&define.amd){define(["jquery","jqueryDataTable"],b)
}else{if(jQuery&&!jQuery.fn.dataTable.Scroller){b(jQuery,jQuery.fn.dataTable)
}}})(window,document);