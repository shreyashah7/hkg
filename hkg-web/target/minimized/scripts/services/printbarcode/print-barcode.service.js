define(["hkg"],function(a){a.register.factory("PrintBarcodeService",["$resource","$rootScope",function(d,b){var c=d(b.centerapipath+"printbarcode/:action",{},{retrieveSearchedStock:{method:"POST",isArray:true,params:{action:"searchlotsorpackets"}},prepareBarcode:{method:"POST",params:{action:"preparebarcode"}}});
return c
}])
});