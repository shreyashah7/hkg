define(["hkg"],function(a){a.register.factory("prompt",function(){return prompt
});
a.register.controller("FlowChartController12",["$scope","prompt",function(j,e){var h=46;
var f=65;
var i=false;
var g=17;
var b=27;
var c=10;
var d={groups:[{name:"A",description:"hello",id:11,x:82,y:88,nodes:[{name:"A1",description:"hello",id:1,x:82,y:88,inputConnectors:[{name:"X"}],outputConnectors:[{name:"1"}]},{name:"A2",description:"hello223",id:2,x:89,y:114,inputConnectors:[{name:"X"}],outputConnectors:[{name:"1"}]}]},{name:"B",description:"hello",id:12,x:282,y:288,nodes:[{name:"B1",description:"hello",id:3,x:282,y:288,inputConnectors:[{name:"X"}],outputConnectors:[{name:"1"}]},{name:"B2",description:"hello223",id:4,x:348,y:114,inputConnectors:[{name:"X"}],outputConnectors:[{name:"1"}]}]},{name:"C",id:11,x:482,y:488,nodes:[]}]};
j.keyDown=function(k){if(k.keyCode===f){i=true;
k.stopPropagation();
k.preventDefault()
}};
j.keyUp=function(k){if(k.keyCode===h){j.chartViewModel.deleteSelected()
}if(k.keyCode==g&&i){j.chartViewModel.selectAll()
}if(k.keyCode==b){j.chartViewModel.deselectAll()
}if(k.keyCode===f){i=false;
k.stopPropagation();
k.preventDefault()
}};
j.addNewNode=function(){var l=e("Enter a node name:","New node");
if(!l){return
}var k={name:l,id:c++,x:0,y:0,inputConnectors:[{name:"X"}],outputConnectors:[{name:"1"}]};
j.chartViewModel.addNode(k)
};
j.addNewGroup=function(){var l=e("Enter a group name:","New group");
if(!l){return
}var k={name:l,id:c++,x:0,y:0,inputConnectors:[{name:"X"}],outputConnectors:[{name:"1"}]};
j.chartViewModel.addNode(k)
};
j.addNewInputConnector=function(){var n=e("Enter a connector name:","New connector");
if(!n){return
}var m=j.chartViewModel.getSelectedNodes();
for(var k=0;
k<m.length;
++k){var l=m[k];
l.addInputConnector({name:n})
}};
j.addNewOutputConnector=function(){var n=e("Enter a connector name:","New connector");
if(!n){return
}var m=j.chartViewModel.getSelectedNodes();
for(var k=0;
k<m.length;
++k){var l=m[k];
l.addOutputConnector({name:n})
}};
j.deleteSelected=function(){j.chartViewModel.deleteSelected()
};
j.chartViewModel=new flowchart.ChartViewModel(d)
}])
});