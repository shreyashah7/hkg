define(["hkg"],function(a){a.register.factory("FinalizeService",["$resource","$rootScope",function(d,b){var c=d(b.centerapipath+"finalize/:action",{},{retrieve:{method:"GET",params:{action:"/retrieve"}},searchLotFromWorkAllocation:{method:"POST",params:{action:"/retrievestockfromworkallocation"}},retrieveStockById:{method:"POST",params:{action:"/retrievestockbyid"}},retrieveRootNodeDesignationIds:{method:"POST",params:{action:"retrieverootnodedesignationid"}},retrieveValuesOfMaster:{method:"GET",isArray:true,params:{action:"retrievevaluesofmaster"}},checkCaratRange:{method:"POST",params:{action:"checkcaratrange"}},retrievePrice:{method:"POST",params:{action:"retrieveprice"}},savePlan:{method:"POST",params:{action:"save"}},submitPlan:{method:"PUT",params:{action:"submit"}},retrieveAccessiblePlan:{method:"POST",isArray:true,params:{action:"retrievesubmittedplan"}},retrieveCurrency:{method:"GET",isArray:true,params:{action:"retrievecurrencycode"}},retrieveSavedPlan:{method:"POST",isArray:true,params:{action:"retrievesavedplan"}},deletePlan:{method:"PUT",params:{action:"deleteplan"}},retrievePriceList:{method:"GET",params:{action:"retrievepricelist"}},retrievevaluefrompricelist:{method:"POST",params:{action:"retrievevaluefrompricelist"}},saveEditedPlan:{method:"POST",params:{action:"editsavedplan"}},});
return c
}])
});