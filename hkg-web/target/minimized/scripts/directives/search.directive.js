define(["angular"],function(){angular.module("hkg.directives").directive("agSearch",["$compile","$filter","$rootScope","$location",function(b,c,a,d){return{restrict:"A",scope:{agSearch:"@",searchListArray:"=",onSelect:"=",onEnter:"=",onReset:"="},link:function(p,i,n){p.isSelect=false;
p.typedWord="";
p.localSearchedResultForDisplay=[];
p.localSearchedResult=[];
p.searchListArray=[];
p.serverSearchList=[];
p.isServerSearch=true;
var g=function(t){var r;
var s=new Date(t);
if(typeof t!=="string"&&Object.prototype.toString.call(s)==="[object Date]"){if(isNaN(s.getTime())){r=t
}else{r=a.ConvertTimeStamptodate(t)
}}else{r=t
}return r
};
var l=function(v){var u=new Date(v);
var s=u.getDate();
s=(s<10)?"0"+s:s;
var t=u.getMonth()+1;
t=(t<10)?"0"+t:t;
var r=["january","february","march","april","may","june","july","august","september","october","november","december"];
return s+""+r[u.getMonth()]+""+u.getFullYear()
};
var j=function(v){var u=new Date(v);
var s=u.getDate();
s=(s<10)?"0"+s:s;
var t=u.getMonth()+1;
t=(t<10)?"0"+t:t;
var r=["january","february","march","april","may","june","july","august","september","october","november","december"];
if(s==="01"){s+="st"
}else{if(s==="02"){s+="nd"
}else{if(s==="03"){s+="rd"
}else{if(s==="04"){s+="th"
}else{if(s==="05"){s+="th"
}else{if(s==="06"){s+="th"
}else{if(s==="07"){s+="th"
}else{if(s==="08"){s+="th"
}else{if(s==="09"){s+="th"
}else{if(s===10){s+="th"
}else{if(s===11){s+="th"
}else{if(s===12){s+="th"
}else{if(s===13){s+="th"
}else{if(s===14){s+="th"
}else{if(s===15){s+="th"
}else{if(s===16){s+="th"
}else{if(s===17){s+="th"
}else{if(s===18){s+="th"
}else{if(s===19){s+="th"
}else{if(s===20){s+="th"
}else{if(s===21){s+="st"
}else{if(s===22){s+="nd"
}else{if(s===23){s+="rd"
}else{if(s===24){s+="th"
}else{if(s===25){s+="th"
}else{if(s===26){s+="th"
}else{if(s===27){s+="th"
}else{if(s===28){s+="th"
}else{if(s===29){s+="th"
}else{if(s===30){s+="th"
}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}return s+""+r[u.getMonth()]+""+u.getFullYear()
};
var m=function(u){var t=new Date(u);
var r=t.getDate();
r=(r<10)?"0"+r:r;
var s=t.getMonth()+1;
s=(s<10)?"0"+s:s;
return r+"-"+s+"-"+t.getFullYear()
};
var e=function(u){var t=new Date(u);
var r=t.getDate();
r=(r<10)?"0"+r:r;
var s=t.getMonth()+1;
s=(s<10)?"0"+s:s;
return r+"/"+s+"/"+(t.getFullYear())%100
};
var h=function(u){var t=new Date(u);
var r=t.getDate();
r=(r<10)?"0"+r:r;
var s=t.getMonth()+1;
s=(s<10)?"0"+s:s;
return r+"-"+s+"-"+(t.getFullYear())%100
};
var k=function(t,z,w){var y=z;
y=y.split(" ");
var x=0;
for(var v=0;
v<y.length;
v++){for(var s=0;
s<w.length;
s++){var r=t[w[s]];
var u=r.toString();
if(typeof r!=="string"&&(typeof r==="number"&&u.length>11)){r=g(r)
}else{if(typeof r==="number"){r=r.toString()
}}y[v]=y[v].replace(/\s+/g," ").trim();
if(r.toUpperCase().indexOf(y[v].toUpperCase())>=0||t.dateHyphenFormat.toUpperCase().indexOf(y[v].toUpperCase())>=0||t.dateStringFormat.toUpperCase().indexOf(y[v].toUpperCase())>=0||t.dateStringFormatWithSuffix.toUpperCase().indexOf(y[v].toUpperCase())>=0||t.dateShortYearHyphenFormat.toUpperCase().indexOf(y[v].toUpperCase())>=0||t.dateShortYearSlashFormat.toUpperCase().indexOf(y[v].toUpperCase())>=0){x++;
break
}}}if(x>=y.length){return true
}else{return false
}};
if(angular.isDefined(p.agSearch)&&angular.isDefined(n.resultFormat)){var o=new Bloodhound({datumTokenizer:Bloodhound.tokenizers.obj.whitespace("name"),queryTokenizer:Bloodhound.tokenizers.whitespace,limit:50,remote:{url:a.appendAuthToken(p.agSearch+"?q=%QUERY"),rateLimitBy:"throttle",replace:function(t,w){o.clearRemoteCache();
p.typedWord=w;
var v=p.typedWord.split(" ");
var x=v[1];
var s=v[v.length-1];
if(angular.isDefined(x)&&x.length>0&&v[0].length>=3||v.length>=3){p.localSearchedResult=[];
p.localSearchedResultForDisplay=[];
var r=n.resultFormat.split(",");
var u="";
p.typedWord=p.typedWord.replace(/\s+/g," ").trim();
angular.forEach(p.serverSearchList,function(B,C){var A=k(B,p.typedWord,r);
if(A){for(var z=0;
z<r.length;
z++){var D;
D=B[r[z]];
var y=D.toString();
if(typeof D!=="string"&&(typeof D==="number"&&y.length>11)){D=g(D)
}if(z===r.length-1){u+=D
}else{u+=D+", "
}}u=u.replace(/\s+/g," ").trim();
p.localSearchedResult.push({id:B.id,name:u});
p.localSearchedResultForDisplay.push(B);
u=""
}});
return[]
}else{console.log("attrs.agExtraparam :"+n.agExtraparam);
if(n.agExtraparam!==undefined&&n.agExtraparam!==null&&n.agExtraparam!==""){return a.appendAuthToken(p.agSearch+"?q="+w.replace(/[/]/g,"%2F")+"&extraparam="+n.agExtraparam)
}else{return a.appendAuthToken(p.agSearch+"?q="+w.replace(/[/]/g,"%2F"))
}}},filter:function(s){var r=p.typedWord.split(" ");
var t=r[1];
if(angular.isDefined(t)&&t.length>0&&r[0].length>=3||r.length>=3){p.searchListArray.splice(0,p.searchListArray.length);
p.isServerSearch=false;
angular.forEach(p.localSearchedResultForDisplay,function(u,v){p.searchListArray.push(u)
});
return $.map(p.localSearchedResult,function(u){return{id:u.id,name:u.name}
})
}else{p.searchListArray.splice(0,p.searchListArray.length);
p.serverSearchList=[];
return $.map(s,function(z){var u=n.resultFormat.split(",");
var y="";
for(var x=0;
x<u.length;
x++){var w=new Date(z[u[x]]);
if(z[u[x]]!=null){if(typeof z[u[x]]!=="string"&&Object.prototype.toString.call(w)==="[object Date]"){var v=z[u[x]].toString();
if(isNaN(w.getTime())||v.length<11){if(x===u.length-1){y+=z[u[x]]
}else{y+=z[u[x]]+", "
}}else{if(x===u.length-1){y+=a.ConvertTimeStamptodate(z[u[x]])
}else{y+=a.ConvertTimeStamptodate(z[u[x]])+", "
}z.dateHyphenFormat=m(z[u[x]]);
z.dateStringFormat=l(z[u[x]]);
z.dateStringFormatWithSuffix=j(z[u[x]]);
z.dateShortYearHyphenFormat=h(z[u[x]]);
z.dateShortYearSlashFormat=e(z[u[x]])
}}else{z.dateHyphenFormat="";
z.dateStringFormat="";
z.dateStringFormatWithSuffix="";
z.dateShortYearHyphenFormat="";
z.dateShortYearSlashFormat="";
if(x===u.length-1){y+=z[u[x]]
}else{y+=z[u[x]]+", "
}}}else{if(x===u.length-1){y+="N/A"
}else{y+="N/A, "
}}}p.searchListArray.push(z);
p.serverSearchList.push(z);
y=y.replace(/\s+/g," ").trim();
return{id:z.id,name:y}
})
}}}});
var q=o.initialize(true);
q.done(function(){}).fail(function(){});
var f;
if(angular.isDefined(n.id)){f=n.id
}$("#"+f+" .typeahead").typeahead({hint:true,highlight:true,minLength:3},{name:"searchWord",displayKey:"name",source:o.ttAdapter(),templates:{empty:['<div class="empty-message">',"unable to find any records","</div>"].join("\n")}});
$("#"+f+" .typeahead").on("typeahead:selected",function(r,s){p.isSelect=true;
if(angular.isDefined(n.onSelect)){p.$apply(function(){p.onSelect(s.id)
})
}$("#"+f+".typeahead").typeahead("val","")
});
$("#"+f+" .typeahead").on("keydown",function(s){var t=jQuery.Event("keydown");
var r=$("#"+f+".typeahead").typeahead("val");
t.keyCode=t.which=9;
if(angular.isDefined(n.onEnter)&&s.which===13&&!p.isSelect){$("#"+f+" .typeahead").typeahead("close");
p.$apply(function(){if(!p.isServerSearch){p.onEnter(p.localSearchedResultForDisplay);
p.isServerSearch=true
}else{if(r.length<3){p.searchListArray=[];
p.onEnter(p.searchListArray)
}else{p.onEnter(p.searchListArray)
}}});
p.isSelect=false
}if(angular.isDefined(n.onReset)&&r.length===1){$("#"+f+" .typeahead").typeahead("close");
p.$apply(function(){p.onReset()
})
}p.isSelect=false
})
}}}
}])
});