define(["angular"],function(){angular.module("hkg.directives").directive("agMenu",["$compile","$rootScope",function(b,a){return{restrict:"AE",replace:true,scope:{menu:"=","class":"@",parentName:"="},template:'<ul class="$class"><li ng-repeat="item in menu" ng-if="item.menuCategory == parentName"><a ng-controller="MenuController" ng-click="retrieveReportByFeature(item)" id="{{item.featureName}}" href="#{{item.featureURL}}" ng-if="item.menuLabel && item.menuCategory !== \'stock\' || (item.menuCategory === \'stock\' && $root.allocationMap[item.menuLabel] === undefined)"><span class="glyphicon glyphicon-edit">&nbsp;</span>{{"Menu."+item.menuLabel | translate}}</a><a ng-controller="MenuController" ng-click="retrieveReportByFeature(item)" id="{{item.featureName}}" href="#{{item.featureURL}}" ng-if="item.menuLabel && item.menuCategory === \'stock\' && $root.allocationMap[item.menuLabel] !== undefined"><span class="glyphicon glyphicon-edit">&nbsp;{{"Menu."+item.menuLabel | translate}}</span> <span class="menu-count">({{$root.allocationMap[item.menuLabel]}})<span></a></li></ul>',compile:function(c){var d=c.contents().remove();
var e;
return function(g,f){if(!e){e=b(d)
}e(g,function(h){f.append(h)
})
}
}}
}]).controller("MenuController",["$rootScope","$scope","$location","ReportMaster",function(b,c,d,a){c.retrieveReportByFeature=function(e){localStorage.setItem("featureName",e.featureName);
document.body.getElementsByClassName("menu-trigger")[0].click();
if(e.menuType==="RMI"){a.retrieveReportByFeature(e.id,function(f){if(f.data!==undefined){localStorage.setItem("menuReportId",f.data.id);
b.childMenu=e.featureName;
d.path("/viewreports")
}},function(){})
}}
}]).factory("ReportMaster",["$resource","$rootScope",function(c,b){var a=c(b.apipath+"report/:action",{},{retrieveReportByFeature:{method:"POST",params:{action:"retrievereportbyfeature"}}});
return a
}])
});