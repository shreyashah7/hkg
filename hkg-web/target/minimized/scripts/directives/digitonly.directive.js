define(["angular"],function(){angular.module("hkg.directives").directive("numbersOnly",["$parse","$compile",function(b,a){return{require:["^form","ngModel"],link:function(g,e,d,c){var f=c[1];
g.$watch(d.ngModel,function(j,h){var l=c[1];
var m=b(d.ngModel);
var n=String(j).split("");
if(n.length===0){l.$setValidity("min",true);
l.$setValidity("max",true);
return
}if(n.length===1&&(((n[0]==="-")&&(d.negativeallowed!==undefined&&d.negativeallowed.toString()==="true"))||((n[0]===".")&&(d.decimalallowed!==undefined&&d.decimalallowed.toString()==="true")))){return
}if(n.length>1&&(((n[0]==="-")&&(d.negativeallowed!==undefined&&(d.negativeallowed.toString()==="false")&&(d.negativeallowed!==true)&&(d.negativeallowed.toString()!=="true"))))){if((j!==undefined)){transformedNewValue=j.toString().replace("-","");
if(j!==transformedNewValue){m.assign(g,transformedNewValue)
}}}if((n.length===2&&j==="-.")&&((d.decimalallowed!==undefined&&d.decimalallowed.toString()==="true"))){return
}if(d.currencyallowed===undefined){if(d.negativeallowed===undefined||d.negativeallowed.toString()===false){if((j!==undefined&&j!==null)){transformedNewValue=j.toString().replace("-","");
if(j!==transformedNewValue){m.assign(g,transformedNewValue)
}}}if(d.decimalallowed===undefined||d.decimalallowed.toString()===false){if((j!==undefined&&j!==null)){transformedNewValue=j.toString().replace(".","");
if(j!==transformedNewValue){m.assign(g,transformedNewValue)
}}}}if(d.currencyallowed!==undefined&&d.currencyallowed.toString()==="true"){consoel.log("6");
if(j===undefined){return""
}var q=j.replace(/[^0-9.]/g,"");
while(q.charAt(0)==="0"){q=q.substr(1)
}var o;
var i=q.toString().split(".")[0];
var k=q.toString().split(".")[1];
var p=i.replace(/(\d)(?=(\d{3})+(?!\d))/g,"$1,");
if(k===undefined){o=p
}else{o=""+p+"."+k
}if(o!==j){f.$setViewValue(o);
f.$render()
}}if(d.currencyallowed===undefined){if(isNaN(j)){if((j===undefined)){m.assign(g,j)
}else{m.assign(g,h)
}}else{if(d.min!==undefined&&((parseInt(d.min)<=parseInt(j)))){l.$setValidity("min",true)
}else{if(d.min===undefined){}else{l.$setValidity("min",false)
}}if(d.max!==undefined&&((parseInt(d.max)>=parseInt(j)))){l.$setValidity("max",true)
}else{if(d.max===undefined){}else{l.$setValidity("max",false)
}}}}})
}}
}]).directive("onlyDigits",function(){return{restrict:"EA",require:"?ngModel",scope:{allowDecimal:"@",allowNegative:"@",minNum:"@",maxNum:"@"},link:function(c,b,a,d){if(!d){return
}d.$parsers.unshift(function(e){var g=false;
var f=e.split("").filter(function(k,j){var h=(!isNaN(k)&&k!=" ");
if(!h&&a.allowDecimal&&a.allowDecimal=="true"){if(k=="."&&g==false){g=true;
h=true
}}if(!h&&a.allowNegative&&a.allowNegative=="true"){h=(k=="-"&&j==0)
}return h
}).join("");
if(a.maxNum&&!isNaN(a.maxNum)&&parseFloat(f)>parseFloat(a.maxNum)){f=a.maxNum
}if(a.minNum&&!isNaN(a.minNum)&&parseFloat(f)<parseFloat(a.minNum)){f=a.minNum
}d.$viewValue=f;
d.$render();
return f
})
}}
})
});