define(["angular"],function(){angular.module("hkg.directives").directive("tabs",function(){return{restrict:"A",transclude:true,scope:{},controller:["$scope","$element",function(b,a){var c=b.panes=[];
b.select=function(d){angular.forEach(c,function(e){e.selected=false
});
d.selected=true
};
this.addPane=function(d){if(c.length===0){b.select(d)
}c.push(d)
}
}],template:'<div class="tabbable"><ul class="nav nav-tabs"><li ng-repeat="pane in panes" ng-class="{active:pane.$parent.tabInfo.selected}"><a href="" ng-click="pane.$parent.tabManager.select($index)">{{pane.title}}</a></li></ul><div class="tab-content" ng-transclude></div></div>',replace:true}
});
angular.module("hkg.directives").directive("pane",function(){return{require:"^tabs",restrict:"A",transclude:true,scope:{title:"@"},link:function(d,c,b,a){a.addPane(d)
},template:'<div class="tab-pane" ng-class="{active: $parent.tabInfo.selected}" ng-transclude></div>',replace:true}
})
});