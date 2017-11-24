define(["hkg"],function(a){a.register.factory("GenerateSlipService",["$resource","$rootScope",function(d,c){var b=d(c.centerapipath+"generateslip/:action",{},{retrieveLotsAndPackets:{method:"POST",params:{action:"searchlotsorpackets"}},generateSlip:{method:"POST",params:{action:"generateslip"}}});
return b
}])
});