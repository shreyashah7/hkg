(function(a){a.fn.extend({tableExport:function(f){var m={separator:",",ignoreColumn:[],tableName:"yourTableName",type:"csv",pdfFontSize:14,pdfLeftMargin:20,escape:"true",htmlContent:"false",consoleLog:"false"};
var f=a.extend(m,f);
var c=this;
if(m.type=="csv"||m.type=="txt"){var e="";
a(c).find("thead").find("tr").each(function(){e+="\n";
a(this).filter(":visible").find("th").each(function(t,u){if(a(this).css("display")!="none"){if(m.ignoreColumn.indexOf(t)==-1){e+='"'+j(a(this))+'"'+m.separator
}}});
e=a.trim(e);
e=a.trim(e).substring(0,e.length-1)
});
a(c).find("tbody").find("tr").each(function(){e+="\n";
a(this).filter(":visible").find("td").each(function(t,u){if(a(this).css("display")!="none"){if(m.ignoreColumn.indexOf(t)==-1){e+='"'+j(a(this))+'"'+m.separator
}}});
e=a.trim(e).substring(0,e.length-1)
});
if(m.consoleLog=="true"){console.log(e)
}var b="base64,"+a.base64.encode(e);
window.open("data:application/"+m.type+";filename=exportData;"+b)
}else{if(m.type=="sql"){var e="INSERT INTO `"+m.tableName+"` (";
a(c).find("thead").find("tr").each(function(){a(this).filter(":visible").find("th").each(function(t,u){if(a(this).css("display")!="none"){if(m.ignoreColumn.indexOf(t)==-1){e+="`"+j(a(this))+"`,"
}}});
e=a.trim(e);
e=a.trim(e).substring(0,e.length-1)
});
e+=") VALUES ";
a(c).find("tbody").find("tr").each(function(){e+="(";
a(this).filter(":visible").find("td").each(function(t,u){if(a(this).css("display")!="none"){if(m.ignoreColumn.indexOf(t)==-1){e+='"'+j(a(this))+'",'
}}});
e=a.trim(e).substring(0,e.length-1);
e+="),"
});
e=a.trim(e).substring(0,e.length-1);
e+=";";
if(m.consoleLog=="true"){console.log(e)
}var b="base64,"+a.base64.encode(e);
window.open("data:application/sql;filename=exportData;"+b)
}else{if(m.type=="json"){var d=[];
a(c).find("thead").find("tr").each(function(){var u="";
var t=[];
a(this).filter(":visible").find("th").each(function(v,w){if(a(this).css("display")!="none"){if(m.ignoreColumn.indexOf(v)==-1){t.push(j(a(this)))
}}});
d.push(t)
});
var o=[];
a(c).find("tbody").find("tr").each(function(){var u="";
var t=[];
a(this).filter(":visible").find("td").each(function(v,w){if(a(this).css("display")!="none"){if(m.ignoreColumn.indexOf(v)==-1){t.push(j(a(this)))
}}});
o.push(t)
});
var q=[];
q.push({header:d,data:o});
if(m.consoleLog=="true"){console.log(JSON.stringify(q))
}var b="base64,"+a.base64.encode(JSON.stringify(q));
window.open("data:application/json;filename=exportData;"+b)
}else{if(m.type=="xml"){var g='<?xml version="1.0" encoding="utf-8"?>';
g+="<tabledata><fields>";
a(c).find("thead").find("tr").each(function(){a(this).filter(":visible").find("th").each(function(t,u){if(a(this).css("display")!="none"){if(m.ignoreColumn.indexOf(t)==-1){g+="<field>"+j(a(this))+"</field>"
}}})
});
g+="</fields><data>";
var l=1;
a(c).find("tbody").find("tr").each(function(){g+='<row id="'+l+'">';
var t=0;
a(this).filter(":visible").find("td").each(function(u,v){if(a(this).css("display")!="none"){if(m.ignoreColumn.indexOf(u)==-1){g+="<column-"+t+">"+j(a(this))+"</column-"+t+">"
}}t++
});
l++;
g+="</row>"
});
g+="</data></tabledata>";
if(m.consoleLog=="true"){console.log(g)
}var b="base64,"+a.base64.encode(g);
window.open("data:application/xml;filename=exportData;"+b)
}else{if(m.type=="excel"||m.type=="doc"||m.type=="powerpoint"){var n="";
a(c).find("table").each(function(){var u="<table border='1'>";
a(this).find("thead").each(function(){u+="<tr style='background-color:#d9edf7'>";
a(this).filter(":visible").find("th").each(function(v,w){if(a(this).css("display")!="none"){if(m.ignoreColumn.indexOf(v)==-1){console.log(a(this));
u+="<td>"+j(a(this))+"</td>"
}}});
u+="</tr>"
});
var t=1;
a(this).find("tbody").find("tr").each(function(){u+="<tr>";
var v=0;
a(this).filter(":visible").find("td").each(function(w,x){if(a(this).css("display")!="none"){if(m.ignoreColumn.indexOf(w)==-1){var y=a(this).find("span");
console.log("-------val------"+j(y));
u+="<td>"+j(y)+"</td>"
}}v++
});
t++;
u+="</tr>"
});
u+="</table>";
n+="<table><tr></tr></table>"+u
});
console.log(n);
if(m.consoleLog=="true"){}var h="<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:"+m.type+"' xmlns='http://www.w3.org/TR/REC-html40'>";
h+="<head>";
h+="<!--[if gte mso 9]>";
h+="<xml>";
h+="<x:ExcelWorkbook>";
h+="<x:ExcelWorksheets>";
h+="<x:ExcelWorksheet>";
h+="<x:Name>";
h+="{worksheet}";
h+="</x:Name>";
h+="<x:WorksheetOptions>";
h+="<x:DisplayGridlines/>";
h+="</x:WorksheetOptions>";
h+="</x:ExcelWorksheet>";
h+="</x:ExcelWorksheets>";
h+="</x:ExcelWorkbook>";
h+="</xml>";
h+="<![endif]-->";
h+="</head>";
h+="<body>";
h+=n;
h+="</body>";
h+="</html>";
console.log("defaults.type----"+m.type);
var b="base64,"+a.base64.encode(h);
window.open("data:application/vnd.ms-"+m.type+";filename=exportData.doc;"+b)
}else{if(m.type=="png"){html2canvas(a(c),{onrendered:function(u){var t=u.toDataURL("image/png");
window.open(t)
}})
}else{if(m.type=="pdf"){var r=new jsPDF("p","pt","a4",true);
r.setFontSize(m.pdfFontSize);
var k=m.pdfLeftMargin;
a(c).find("thead").find("tr").each(function(){a(this).filter(":visible").find("th").each(function(t,v){if(a(this).css("display")!="none"){if(m.ignoreColumn.indexOf(t)==-1){var u=k+(t*50);
r.text(u,20,j(a(this)))
}}})
});
var p=20;
var i=1;
var s=0;
a(c).find("tbody").find("tr").each(function(t,u){rowCalc=t+1;
if(rowCalc%26==0){r.addPage();
i++;
p=p+10
}s=(p+(rowCalc*10))-((i-1)*280);
a(this).filter(":visible").find("td").each(function(v,x){if(a(this).css("display")!="none"){if(m.ignoreColumn.indexOf(v)==-1){var w=k+(v*50);
r.text(w,s,j(a(this)))
}}})
});
r.output("datauri")
}}}}}}}function j(t){if(m.htmlContent=="true"){content_data=t.html().trim()
}else{content_data=t.text().trim()
}if(m.escape=="true"){content_data=escape(content_data)
}return content_data
}}})
})(jQuery);