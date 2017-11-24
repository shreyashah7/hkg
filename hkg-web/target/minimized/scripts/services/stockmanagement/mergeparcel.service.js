define(["hkg"],function(a){a.register.factory("MergeParcelService",["$resource","$rootScope",function(c,b){var d=c(b.centerapipath+"mergeparcel/:action",{},{search:{method:"POST",isArray:true,params:{action:"/retrievesearchedparcels"}},retrieveParcelsByIds:{method:"POST",params:{action:"/retrieveparcelsbyids"}},mergeParcel:{method:"POST",params:{action:"/mergeparcel"}}});
return d
}])
});