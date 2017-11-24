define(["hkg"],function(a){a.register.factory("PriceListService",["$resource","$rootScope",function(d,c){var b=d(c.apipath+"pricelist/:action",{},{downloadtemplate:{method:"GET",params:{action:"/downloadtemplate"}},savePriceList:{method:"POST",params:{action:"/savepricelist"}},retrieveallPriceList:{method:"POST",isArray:true,params:{action:"/retrieveallpricelist"}},retrievepricelistByMonthYear:{method:"POST",isArray:true,params:{action:"/retrievepricelistByMonthYear"}}});
return b
}])
});