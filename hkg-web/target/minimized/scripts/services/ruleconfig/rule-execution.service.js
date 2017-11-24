define(["hkg"],function(a){a.register.factory("RuleExecutionService",["$resource","$rootScope",function(c,b){var d=c(b.centerapipath+"executerule/:action",{action:"@actionName"},{executePreRule:{method:"POST",params:{action:"prerule"}},executePostRule:{method:"POST",params:{action:"postrule"}}});
return d
}])
});