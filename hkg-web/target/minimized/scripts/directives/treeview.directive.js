define(["angular"],function(){angular.module("hkg.directives").directive("agTreemodel",["$compile",function(a){return{restrict:"A",link:function(l,f,j){var h=j.treeId;
var b=j.agTreemodel;
var d=j.nodeId||"id";
var g=j.nodeLabel||"displayName";
if(angular.isDefined(j.searchId)){function e(o,p){if(o===null||o===undefined){return
}for(var n=0;
n<o.length;
n++){if(o[n].selected=="selected"){o[n].selected=undefined
}if(o[n].id==p){o[n].selected="selected"
}else{for(var m=0;
m<o.length;
m++){if(o[m].children!==null){e(o[m].children,p)
}}}}}l.$watch(j.searchId,function(m){e(l[j.agTreemodel],m)
});
l.$watch(j.agTreemodel,function(){e(l[j.agTreemodel],l[j.searchId])
})
}l.displayCount=j.displayCount;
if(j.entityName!=undefined){l.entityName=j.entityName
}else{l.entityName=""
}var c=j.nodeChildren||"children";
var k=j.searchQuery;
var i='<ul><li data-ng-repeat="node in '+b+"| filter:"+k+' "><span class="col-xs-12 hkg-nopadding"><span style="display : inline-block" title="Click to Expand" class="glyphicon glyphicon-plus-sign text-hkg" data-ng-show="node.'+c+'.length && node.collapsed" data-ng-click="'+h+'.selectNodeHead(node, $event)"></span><span  style=";display : inline-block" title="Click to Collapse" class="glyphicon glyphicon-minus-sign text-hkg" data-ng-show="node.'+c+'.length && !node.collapsed" data-ng-click="'+h+'.selectNodeHead(node, $event)"></span><span style="display : inline-block" title="No Children Exists" class="glyphicon glyphicon-file text-hkg"  data-ng-hide="node.'+c+'.length" data-ng-click="'+h+'.selectNodeLabel(node, $event)"></span> <span class="tree-model-list"><span style="width: 90%;float: right;" class="wordRap" data-ng-class="{\'selected\':node.selected,\'text-success\':node.'+j.existFlag+'}" data-ng-click="'+h+'.selectNodeLabel(node)">{{"'+l.entityName+'"+node.'+g+'| translate}}<span ng-if="node.categoryCount!==undefined && '+l.displayCount+'">({{node.categoryCount}})</span></span></span><div data-ng-hide="node.collapsed" data-tree-id="'+h+'" ag-treemodel="node.'+c+'" data-node-id='+d+" data-node-label="+g+" data-node-children="+c+" display-count="+l.displayCount+" entity-name="+l.entityName+"></div><span></li></ul>";
if(h&&b){if(j.angularTreeview){l[h]=l[h]||{};
l[h].selectNodeHead=l[h].selectNodeHead||function(n,m){n.collapsed=!n.collapsed;
m.preventDefault();
m.stopPropagation()
};
l[h].selectNodeLabel=l[h].selectNodeLabel||function(n,m){if(l[h].currentNode&&l[h].currentNode.selected){l[h].currentNode.selected=undefined
}n.selected="selected";
l[h].currentNode=n
}
}f.html("").append(a(i)(l))
}}}
}])
});