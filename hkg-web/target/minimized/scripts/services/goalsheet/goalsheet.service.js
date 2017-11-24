define(["hkg"],function(a){a.register.factory("GoalSheetService",["$resource","$rootScope",function(c,b){var d=c(b.apipath+"goalsheet/:action",{},{retrieve:{method:"GET",params:{action:"/retrieve"}}});
return d
}])
});