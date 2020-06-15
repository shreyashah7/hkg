$.extend(true,$.fn.dataTable.defaults,{sDom:"<'row'<'col-xs-6'l><'col-xs-6'f>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",oLanguage:{sLengthMenu:"_MENU_ records per page"}});
$.extend($.fn.dataTableExt.oStdClasses,{sWrapper:"dataTables_wrapper form-inline",sFilterInput:"form-control input-sm",sLengthSelect:"form-control input-sm"});
if($.fn.dataTable.Api){$.fn.dataTable.defaults.renderer="bootstrap";
$.fn.dataTable.ext.renderer.pageButton.bootstrap=function(e,l,k,j,i,d){var h=new $.fn.dataTable.Api(e);
var f=e.oClasses;
var c=e.oLanguage.oPaginate;
var b,a;
var g=function(n,r){var p,m,q,o;
var s=function(t){t.preventDefault();
if(t.data.action!=="ellipsis"){h.page(t.data.action).draw(false)
}};
for(p=0,m=r.length;
p<m;
p++){o=r[p];
if($.isArray(o)){g(n,o)
}else{b="";
a="";
switch(o){case"ellipsis":b="&hellip;";
a="disabled";
break;
case"first":b=c.sFirst;
a=o+(i>0?"":" disabled");
break;
case"previous":b=c.sPrevious;
a=o+(i>0?"":" disabled");
break;
case"next":b=c.sNext;
a=o+(i<d-1?"":" disabled");
break;
case"last":b=c.sLast;
a=o+(i<d-1?"":" disabled");
break;
default:b=o+1;
a=i===o?"active":"";
break
}if(b){q=$("<li>",{"class":f.sPageButton+" "+a,"aria-controls":e.sTableId,tabindex:e.iTabIndex,id:k===0&&typeof o==="string"?e.sTableId+"_"+o:null}).append($("<a>",{href:"#"}).html(b)).appendTo(n);
e.oApi._fnBindAction(q,{action:o},s)
}}}};
g($(l).empty().html('<ul class="pagination"/>').children("ul"),j)
}
}else{$.fn.dataTable.defaults.sPaginationType="bootstrap";
$.fn.dataTableExt.oApi.fnPagingInfo=function(a){return{iStart:a._iDisplayStart,iEnd:a.fnDisplayEnd(),iLength:a._iDisplayLength,iTotal:a.fnRecordsTotal(),iFilteredTotal:a.fnRecordsDisplay(),iPage:a._iDisplayLength===-1?0:Math.ceil(a._iDisplayStart/a._iDisplayLength),iTotalPages:a._iDisplayLength===-1?0:Math.ceil(a.fnRecordsDisplay()/a._iDisplayLength)}
};
$.extend($.fn.dataTableExt.oPagination,{bootstrap:{fnInit:function(e,b,d){var a=e.oLanguage.oPaginate;
var f=function(g){g.preventDefault();
if(e.oApi._fnPageChange(e,g.data.action)){d(e)
}};
$(b).append('<ul class="pagination"><li class="prev disabled"><a href="#">&larr; '+a.sPrevious+'</a></li><li class="next disabled"><a href="#">'+a.sNext+" &rarr; </a></li></ul>");
var c=$("a",b);
$(c[0]).bind("click.DT",{action:"previous"},f);
$(c[1]).bind("click.DT",{action:"next"},f)
},fnUpdate:function(c,k){var l=5;
var e=c.oInstance.fnPagingInfo();
var h=c.aanFeatures.p;
var g,m,f,d,a,n,b=Math.floor(l/2);
if(e.iTotalPages<l){a=1;
n=e.iTotalPages
}else{if(e.iPage<=b){a=1;
n=l
}else{if(e.iPage>=(e.iTotalPages-b)){a=e.iTotalPages-l+1;
n=e.iTotalPages
}else{a=e.iPage-b+1;
n=a+l-1
}}}for(g=0,m=h.length;
g<m;
g++){$("li:gt(0)",h[g]).filter(":not(:last)").remove();
for(f=a;
f<=n;
f++){d=(f==e.iPage+1)?'class="active"':"";
$("<li "+d+'><a href="#">'+f+"</a></li>").insertBefore($("li:last",h[g])[0]).bind("click",function(i){i.preventDefault();
c._iDisplayStart=(parseInt($("a",this).text(),10)-1)*e.iLength;
k(c)
})
}if(e.iPage===0){$("li:first",h[g]).addClass("disabled")
}else{$("li:first",h[g]).removeClass("disabled")
}if(e.iPage===e.iTotalPages-1||e.iTotalPages===0){$("li:last",h[g]).addClass("disabled")
}else{$("li:last",h[g]).removeClass("disabled")
}}}}})
}if($.fn.DataTable.TableTools){$.extend(true,$.fn.DataTable.TableTools.classes,{container:"DTTT btn-group",buttons:{normal:"btn btn-default",disabled:"disabled"},collection:{container:"DTTT_dropdown dropdown-menu",buttons:{normal:"",disabled:"disabled"}},print:{info:"DTTT_print_info modal"},select:{row:"active"}});
$.extend(true,$.fn.DataTable.TableTools.DEFAULTS.oTags,{collection:{container:"ul",button:"li",liner:"a"}})
};