define(["angular","rapCalcyService"],function(){globalProvider.compileProvider.directive("rapCalcy",["DynamicCenterMasterService","RapCalcyService","$timeout",function(a,c,b){return{restrict:"E",replace:true,scope:{outputData:"=",inputData:"=",rules:"=conditions",calculateRateFnc:"&calculateRate"},templateUrl:"scripts/directives/rapcalcy/rapcalcy.tmpl.html",link:function(d){d.entity="RAPCALCY.";
d.orderSeq={"shape$DRP$String":1,"clarity$DRP$String":2,"color$DRP$String":3,"carat_type$DRP$Long":4};
if(d.inputData!==undefined&&d.inputData!==null&&d.inputData.length>0){var f=[];
var h=[];
for(var e=d.inputData.length-1;
e>=0;
e--){var g=angular.copy(d.inputData[e]);
if(g.model!=="empname$DRP$String"){if(d.$root.primaryFields.indexOf(g.model)!==-1){g.order=d.orderSeq[g.model];
f.push(g)
}else{h.push(g)
}}d.inputData.splice(e,1)
}d.inputData=[];
d.secondaryData=[];
if(f.length>0){d.inputData=angular.copy(f)
}if(h.length>0){d.secondaryData=angular.copy(h)
}}d.flag=false;
d.valExist=false;
d.pointerFieldIds=[];
if(d.secondaryData!==undefined&&d.secondaryData!==null&&d.secondaryData.length>0){for(var e=d.secondaryData.length-1;
e>=0;
e--){if(d.secondaryData[e].type!==null&&d.secondaryData[e].type!=="select"&&d.secondaryData[e].type!=="pointerselect"){d.secondaryData.splice(e,1)
}else{if(d.secondaryData[e].type!==null&&d.secondaryData[e].type==="pointerselect"){d.pointerFieldIds.push(d.secondaryData[e].pointerFieldId)
}}}}if(d.inputData!==undefined&&d.inputData!==null&&d.inputData.length>0){for(var i=d.inputData.length-1;
i>=0;
i--){if(d.inputData[i].type!==null&&d.inputData[i].type!=="select"){d.inputData.splice(i,1)
}}}d.priceCalcyDtl=angular.copy(d.inputData);
d.changeData=function(k){if(k!==undefined&&k!==null){var j="";
if(k.caratFirst!==null&&k.caratFirst!==undefined){j+=k.caratFirst
}else{j+="0"
}if(k.caratSecond!==null&&k.caratSecond!==undefined){j+=k.caratSecond
}else{j+="0"
}j+=".";
if(k.centFirst!==null&&k.centFirst!==undefined){j+=k.centFirst
}else{j+="0"
}if(k.centSecond!==null&&k.centSecond!==undefined){j+=k.centSecond
}else{j+="0"
}if(k.centThird!==null&&k.centThird!==undefined){j+=k.centThird
}else{j+="0"
}d.outputData=d.outputData||{};
if(d.outputData.carat_type$DRP$Long===d.caratTypeValues[0].value){d.outputData.size$DRP$Long=parseFloat(j)
}else{if(d.outputData.carat_type$DRP$Long===d.caratTypeValues[1].value){d.outputData.gsize$DRP$long=parseFloat(j)
}}}};
d.changeCaratValues=function(k){if(k!==undefined&&k!==null){var j=parseFloat(k).toFixed(3);
d.sampleData=d.sampleData||{};
if(j.toString().split(".")[0]<10&&j.toString().split(".")[0]>=0){d.sampleData.caratFirst=0;
d.sampleData.caratSecond=parseInt((""+j.toString().split(".")[0])[0])
}else{if(j.toString().split(".")[0]>=10&&j.toString().split(".")[0]<=99){d.sampleData.caratFirst=parseInt((""+j.toString().split(".")[0])[0]);
d.sampleData.caratSecond=parseInt((""+j.toString().split(".")[0])[1])
}}d.sampleData.centFirst=parseInt((""+j.toString().split(".")[1])[0]);
d.sampleData.centSecond=parseInt((""+j.toString().split(".")[1])[1]);
d.sampleData.centThird=parseInt((""+j.toString().split(".")[1])[2])
}else{d.sampleData={}
}};
d.$watch("outputData.size$DRP$Long",function(k){if(k!==undefined&&k!==null){var j=parseFloat(k).toFixed(3);
d.sampleData=d.sampleData||{};
if(j.toString().split(".")[0]<10&&j.toString().split(".")[0]>=0){d.sampleData.caratFirst=0;
d.sampleData.caratSecond=parseInt((""+j.toString().split(".")[0])[0])
}else{if(j.toString().split(".")[0]>=10&&j.toString().split(".")[0]<=99){d.sampleData.caratFirst=parseInt((""+j.toString().split(".")[0])[0]);
d.sampleData.caratSecond=parseInt((""+j.toString().split(".")[0])[1])
}}d.sampleData.centFirst=parseInt((""+j.toString().split(".")[1])[0]);
d.sampleData.centSecond=parseInt((""+j.toString().split(".")[1])[1]);
d.sampleData.centThird=parseInt((""+j.toString().split(".")[1])[2])
}else{d.sampleData={}
}});
d.$watch("outputData.gsize$DRP$long",function(k){if(k!==undefined&&k!==null){var j=parseFloat(k).toFixed(3);
d.sampleData=d.sampleData||{};
if(j.toString().split(".")[0]<10&&j.toString().split(".")[0]>=0){d.sampleData.caratFirst=0;
d.sampleData.caratSecond=parseInt((""+j.toString().split(".")[0])[0])
}else{if(j.toString().split(".")[0]>=10&&j.toString().split(".")[0]<=99){d.sampleData.caratFirst=parseInt((""+j.toString().split(".")[0])[0]);
d.sampleData.caratSecond=parseInt((""+j.toString().split(".")[0])[1])
}}d.sampleData.centFirst=parseInt((""+j.toString().split(".")[1])[0]);
d.sampleData.centSecond=parseInt((""+j.toString().split(".")[1])[1]);
d.sampleData.centThird=parseInt((""+j.toString().split(".")[1])[2])
}else{d.sampleData={}
}});
d.manageCaratTypeVal=function(l){if(l===undefined||l===null){if(d.caratTypeValues!==undefined&&d.caratTypeValues!==null&&d.caratTypeValues.length>=1){d.outputData.carat_type$DRP$Long=d.caratTypeValues[0].value
}}else{if(d.outputData.carat_type$DRP$Long===d.caratTypeValues[0].value){d.changeCaratValues(d.outputData.size$DRP$Long)
}else{if(d.outputData.carat_type$DRP$Long===d.caratTypeValues[1].value){d.changeCaratValues(d.outputData.gsize$DRP$long)
}}}if(d.caratTypeValues!==undefined&&d.caratTypeValues!==null&&d.caratTypeValues.length>=1){if(d.outputData.carat_type$DRP$Long===d.caratTypeValues[0].value){d.secondaryData=angular.copy(d.copySecondaryData);
if(d.secondaryData!==undefined&&d.secondaryData!==null){for(var j=d.secondaryData.length-1;
j>=0;
j--){var k=d.secondaryData[j];
if(k.type==="pointerselect"){d.secondaryData.splice(j,1)
}}}}else{if(d.outputData.carat_type$DRP$Long===d.caratTypeValues[1].value){d.secondaryData=angular.copy(d.copySecondaryData);
if(d.secondaryData!==undefined&&d.secondaryData!==null){for(var j=d.secondaryData.length-1;
j>=0;
j--){var k=d.secondaryData[j];
if(d.pointerFieldIds.indexOf(k.fieldId)!==-1){d.secondaryData.splice(j,1)
}}}}}b(function(){d.$root.unMaskLoading();
d.flag=true
},2)
}};
d.$watch("outputData",function(l,k){if(l!==undefined&&l!==null){if(d.caratTypeValues!==undefined&&d.caratTypeValues!==null&&d.caratTypeValues.length>0){var j=l.carat_type$DRP$Long===d.caratTypeValues[0].value&&(l["size$DRP$Long"]!==undefined&&l["size$DRP$Long"]!==null);
if(j===false){j=l.carat_type$DRP$Long===d.caratTypeValues[1].value&&(l["gsize$DRP$long"]!==undefined&&l["gsize$DRP$long"]!==null)
}if(j===true){angular.forEach(d.$root.primaryFields,function(m){if(l[m]===undefined||l[m]===null){j=false
}})
}if(j===true){d.calculateRateFnc()
}}}if(k!==undefined&&k!==null&&k.carat_type$DRP$Long!==l.carat_type$DRP$Long){d.$root.maskLoading();
d.flag=false
}d.manageCaratTypeVal(l.carat_type$DRP$Long)
},true);
d.defineLabelForSingleSelect=function(j){d.dropdown=[];
d.fieldIds=j;
var l=function(m){console.log("all values:::"+JSON.stringify(m));
d.dropdownValuesForPrimary={};
d.dropdownValuesForSecondary={};
angular.forEach(m.data,function(q,p){if(d.primaryFieldIds.indexOf(p.toString())>=0){d.dropdownValuesForPrimary[p]=q
}else{if(d.secondaryFieldIds.indexOf(p.toString())>=0){d.dropdownValuesForSecondary[p]=q
}}if(d.caratTypeItem!==undefined&&d.caratTypeItem!==null&&p.toString()===d.caratTypeItem.fieldId.toString()){d.caratTypeValues=angular.copy(q)
}});
if(d.caratTypeItem!==undefined&&d.caratTypeItem!==null&&d.caratTypeValues!==undefined&&d.caratTypeValues!==null&&d.caratTypeValues.length>0){d.valExist=true
}else{var o="Please enter carat type values before proceed";
var n=d.$root.failure;
d.$root.addMessage(o,n)
}d.manageCaratTypeVal(null)
};
var k=function(){};
console.log("retrieveing........."+JSON.stringify(d.fieldIds));
a.retrieveAllValuesForAllMasters(d.fieldIds,l,k)
};
if((d.secondaryData!==undefined&&d.secondaryData!==null)||(d.inputData!==undefined&&d.inputData!==null)){d.fieldIds=[];
d.primaryFieldIds=[];
d.secondaryFieldIds=[];
angular.forEach(d.secondaryData,function(j){if((j.type!==null&&j.type==="select")||(j.type!==null&&j.type==="pointerselect")){d.fieldIds.push(j.fieldId.toString());
d.secondaryFieldIds.push(j.fieldId.toString())
}});
angular.forEach(d.inputData,function(j){if((j.type!==null&&j.type==="select")||(j.type!==null&&j.type==="pointerselect")){d.fieldIds.push(j.fieldId.toString());
d.primaryFieldIds.push(j.fieldId.toString())
}if(j.modelWithoutSeperators==="carat_type"){d.caratTypeItem=angular.copy(j)
}});
d.defineLabelForSingleSelect(d.fieldIds)
}d.copySecondaryData=angular.copy(d.secondaryData)
}}
}])
});