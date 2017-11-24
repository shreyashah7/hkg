define(["angular"],function(){globalProvider.compileProvider.directive("printBarcodeValue",["$parse","$compile","$rootScope","$filter","MasterPrintBarcodeService","CenterPrintBarcodeService",function(a,b,g,e,d,h){var i={modelValue:"="};
var f=function(l,k,j){};
var c=["$scope","$element","$attrs",function(l,k,j){console.log("inside directive");
if(angular.isDefined(j.isImageName)){try{l.isImageName=JSON.parse(j.isImageName.toLowerCase())
}catch(m){l.isImageName=false;
console.log("Can not parse isImageName"+m)
}}else{l.isImageName=false
}if(angular.isDefined(j.isDiamond)){l.isDiamond=j.isDiamond
}else{l.isDiamond=false
}var n;
if(l.isDiamond){n=h
}else{n=d
}l.printBarcode=function(){if(l.modelValue!==null&&l.modelValue!==undefined&&l.modelValue!==""){if(l.isDiamond){if(l.isImageName===false){var p=l.modelValue.toString();
n.printBarcode(p,function(r){if(r!==null&&r!==undefined&&r!==""&&r.tempFilePath!==null){var s=window.open(g.appendAuthToken(g.centerapipath+"printbarcode/getimage/"+r.tempFilePath),"_blank");
s.print()
}})
}else{var o=window.open(g.appendAuthToken(g.apipath+"printbarcode/getimage/"+l.modelValue),"_blank");
o.print()
}}else{var q;
if(g.apipath.indexOf("http://")>=0||g.apipath.indexOf("https://")>=0){q=g.apipath
}else{q=window.location.href.split("#/")[0]+g.apipath
}window.open("views/unsecure/printpage.html?getimage="+(g.appendAuthToken(q+"asset/getimage/"+l.modelValue)),"_blank")
}}}
}];
return{restrict:"E",scope:i,link:f,template:'<div class="control-group"><span class="glyphicon glyphicon-barcode pull-right pointer" title="Print Barcode" style="font-size: 20px" ng-click="printBarcode()"></span><div>',controller:c}
}]);
globalProvider.provide.factory("MasterPrintBarcodeService",["$resource","$rootScope",function(b,a){var c=b(a.apipath+"asset/:action",{},{retrieveListOfMaster:{method:"GET",isArray:true,params:{action:"retrieve"}}});
return c
}]);
globalProvider.provide.factory("CenterPrintBarcodeService",["$resource","$rootScope",function(b,a){var c=b(a.centerapipath+"printbarcode/:action",{},{printBarcode:{method:"POST",params:{action:"printbarcode"}}});
return c
}])
});