define(["hkg"],function(a){a.register.factory("EstPredictService",["$resource","$rootScope",function(c,b){var d=c(b.centerapipath+"estpredict/:action",{},{});
return d
}])
});