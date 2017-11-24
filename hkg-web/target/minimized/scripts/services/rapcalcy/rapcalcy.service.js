define(["hkg"],function(a){a.register.factory("RapCalcyService",["$resource","$rootScope",function(c,b){var d=c(b.centerapipath+"rapcalc/:action",{},{create:{method:"PUT",params:{action:"/create"}},calculateDiamondPrice:{method:"POST",params:{action:"/calculatediamondprice"}}});
return d
}])
});