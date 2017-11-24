define(["angular"],function(){globalProvider.compileProvider.directive("agTreeviewMultiselect",["$compile",function(a){return{restrict:"E",terminal:true,transclude:true,link:function(n,g,k){if(n.treeViewMultiselectTemplate===undefined){n.treeViewMultiselectTemplate="";
var f=n.treeViewMultiselectTemplate;
n.mainTreeViewData=n[k.val];
n.firstScopeObj=n;
n.myob={};
n.myob.defaultSelectedids=n[k.defaultselectedids]
}else{f="";
n.myob.defaultSelectedids=n[k.defaultselectedids]
}var c=k.val;
var i=k.parentData;
var e=k.parentDataObject;
var m=k.selecteditem;
var b=f;
var l='<span style="display : inline-block" title="No Children Exists" ng-class="{\'ng-click-active\':('+c+'.isChecked )}" class="glyphicon glyphicon-file text-hkg ng-click-active" data-ng-hide="node.children.length" data-ng-click="selectedDepartmentDropdown.selectNodeLabel(node, $event)"></span>';
var j=b+'<input ng-show="false" type="checkbox" ng-click="checkOrUncheckMe('+c+'.isChecked)" ng-model="'+c+'.isChecked"/>\n'+l+'<span ng-class = "'+c+".isChecked ? '':'tree-model-list'\"><span ng-click=\"checkOrUncheckMe(!"+c+'.isChecked)" ng-class = "'+c+".isChecked ? 'hkg-selected':''\" style=\"cursor:pointer;width:90%;float:right;\">{{"+c+".text}}</span></span>";
if(angular.isArray(n[c].items)){j+='<ul class="indent" style="list-style-type: none;">\n                            <li ng-repeat="item in '+c+'.items| filter:searchItem" style="margin: 3px 0px;">\n                            <ag-treeview-multiselect val="item" parent-data="'+c+'.items" parent-data-object="'+c+'" selecteditem='+m+">\n                            </ag-treeview-multiselect></li>\n                            </ul>"
}n.$watch(k.defaultselectedids,function(p,o){d(n.mainTreeViewData.items,p)
},true);
var d=function(r,p){if(r!==undefined&&r!==null){for(var o=0;
o<r.length;
o++){var q=r[o];
if($.inArray(q.id+"",p)!==-1||$.inArray(q.id,p)!==-1){q.isChecked=true
}else{q.isChecked=false
}if(angular.isDefined(q.items)&&q.items!==null){d(q.items,p)
}}}};
n.checkOrUncheckMe=function(q){var p=n[c];
p.isChecked=q;
if(p.items&&q){for(var o=0;
o<p.items.length;
o++){p.items[o].isChecked=q;
if(p.items[o].items){n.iterateChild(p.items[o].items,q)
}}}else{if(q&&n.parentDataObject===undefined){n.isChecked=true
}}if(!q){n.verifyOnUncheck();
if(n[c].items){n.iterateChild(n[c].items,false)
}}else{if(q){n.verifyOnCheck()
}}n.toggleData(m)
};
n.iterateChild=function(r,q){var p=r;
for(var o=0;
o<p.length;
o++){p[o].isChecked=q;
if(p[o].items){n.iterateChild(p[o].items,q)
}}};
n.checkOrUncheckAllItems=function(o){var q=n.mainTreeViewData;
q.isChecked=o;
if(q.items){for(var p=0;
p<q.items.length;
p++){q.items[p].isChecked=o;
if(q.items[p].items){n.iterateChild(q.items[p].items,o)
}}}n.toggleData(m)
};
var h=angular.element(j);
a(h)(n);
g.replaceWith(h);
n.verifyOnUncheck=function(){var p=n.mainTreeViewData;
if(p.items){var o=n.processChild(p,0,p.items.length,0)
}};
n.processChild=function(t,q,r,p){for(var o=0;
o<t.items.length;
o++){if(t.items[o].items){if(t.items[o].isChecked){q++
}var s=n.processChild(t.items[o],0,t.items[o].items.length,0);
q=q+s[0];
if(t.items[o+1]===undefined){if(q!==r){t.isChecked=false;
p=-1
}}}else{if(t.items[o].isChecked){q++
}if(t.items[o+1]===undefined){if(q!==r){t.isChecked=false;
p=-1
}}}}return[p,r,t.items[0]]
};
n.verifyOnCheck=function(){var o=n.mainTreeViewData;
if(o.items){n.processChildVerifyOnCheck(o,0,o.items.length,0)
}};
n.processChildVerifyOnCheck=function(t,q,r,p){for(var o=0;
o<t.items.length;
o++){if(t.items[o].items){var s=n.processChildVerifyOnCheck(t.items[o],0,t.items[o].items.length,0);
q=q+s[0];
if(t.items[o+1]===undefined){if(q===r){t.isChecked=true;
p=1
}}}else{if(t.items[o].isChecked){q++
}if(t.items[o+1]===undefined){if(q===r){t.isChecked=true;
p=1
}}}}return[p,r,t.items[0]]
};
n.toggleData=function(o){n.tempdata=angular.copy(n.mainTreeViewData);
if(n.tempdata.items&&!n.tempdata.isChecked){n.processItems(n.tempdata.items,n.tempdata)
}n.firstScopeObj.$parent[o]=n.tempdata
};
n.processItems=function(o,s){for(var p=0;
p<o.length;
p++){if(o[p].isChecked&&o[p].items){}else{if(o[p].items&&!o[p].isChecked){n.processItems(o[p].items,o[p]);
if(s.items===undefined){var r=s.indexOf(o[p]);
if(o[p].items.length===0){s.splice(r,1);
p--
}}else{var r=s.items.indexOf(o[p]);
if(o[p].items.length===0){s.items.splice(r,1);
p--
}}}else{var q=n.processItem(o[p],s);
p=p-q
}}}};
n.processItem=function(o,q){if(!o.isChecked){var p=q.items.indexOf(o);
q.items.splice(p,1);
return 1
}return 0
}
}}
}])
});