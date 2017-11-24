define(["hkg"],function(a){a.register.factory("PlanService",["$resource","$rootScope",function(c,b){var d=c(b.centerapipath+"plan/:action",{},{savePlans:{method:"POST",params:{action:"/save"}},retrievePlansByPacket:{method:"POST",isArray:false,params:{action:"/retrievebypacket"}},retrieveModifiers:{method:"POST",params:{action:"/retrievemodifiers"}},retrieveFinalPlans:{method:"POST",isArray:true,params:{action:"/retrievefinalplans"}},finalizePlan:{method:"POST",params:{action:"/finalizeplan"}}});
return d
}])
});