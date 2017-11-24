angular.module("mouseCapture",[]).factory("mouseCapture",["$rootScope",function(b){var d=document;
var c=null;
var e=function(f){if(c&&c.mouseMove){c.mouseMove(f);
b.$digest()
}};
var a=function(f){if(c&&c.mouseUp){c.mouseUp(f);
b.$digest()
}};
return{registerElement:function(f){d=f
},acquire:function(f,g){this.release();
c=g;
d.mousemove(e);
d.mouseup(a)
},release:function(){if(c){if(c.released){c.released()
}c=null
}d.unbind("mousemove",e);
d.unbind("mouseup",a)
}}
}]).directive("mouseCapture",function(){return{restrict:"A",controller:["$scope","$element","$attrs","mouseCapture",function(c,b,a,d){d.registerElement(b)
}]}
});
angular.module("dragging",["mouseCapture"]).factory("dragging",["$rootScope","mouseCapture",function(b,c){var a=5;
return{startDrag:function(f,h){var j=false;
var e=f.pageX;
var k=f.pageY;
var g=function(l){if(!j){j=true;
if(h.dragStarted){h.dragStarted(e,k,l)
}if(h.dragging){h.dragging(l.pageX,l.pageY,l)
}}else{if(h.dragging){h.dragging(l.pageX,l.pageY,l)
}e=l.pageX;
k=l.pageY
}};
var i=function(){if(j){if(h.dragEnded){h.dragEnded()
}}else{if(h.clicked){h.clicked()
}}};
var d=function(l){c.release();
l.stopPropagation();
l.preventDefault()
};
c.acquire(f,{mouseMove:g,mouseUp:d,released:i});
f.stopPropagation();
f.preventDefault()
}}
}]);
angular.module("flowChart",["dragging"]).directive("flowChart",function(){return{restrict:"E",templateUrl:"scripts/directives/flowchart/flowchart_template.html",replace:true,scope:{chart:"=chart"},controller:"FlowChartController"}
}).directive("chartJsonEdit",function(){return{restrict:"A",scope:{viewModel:"="},link:function(c,d,b){var a=function(){if(c.viewModel){var e=JSON.stringify(c.viewModel.data,null,4);
$(d).val(e)
}};
a();
c.$watch("viewModel.data",a,true);
$(d).bind("input propertychange",function(){var f=$(d).val();
var e=JSON.parse(f);
c.viewModel=new flowchart.ChartViewModel(e);
c.$digest()
})
}}
}).controller("FlowChartController",["$rootScope","$scope","dragging","$element","$filter","ActivityFlowService",function FlowChartController(a,d,f,c,e,g){var b=this;
this.document=document;
this.jQuery=function(h){return $(h)
};
d.draggingConnection=false;
d.connectorSize=15;
d.dragSelecting=false;
d.mouseOverConnector=null;
d.mouseOverConnection=null;
d.mouseOverNode=null;
d.mouseOverGroup=null;
this.connectionClass="connection";
this.connectorClass="connector";
this.nodeClass="node";
this.groupClass="group";
this.searchUp=function(h,i){if(h===null||h.length===0){return null
}if(hasClassSVG(h,i)){return h
}return this.searchUp(h.parent(),i)
};
this.hitTest=function(i,h){return this.document.elementFromPoint(i,h)
};
this.checkForHit=function(j,h){var i=this.searchUp(this.jQuery(j),h);
if(!i){return null
}return i.scope()
};
this.translateCoordinates=function(i,l){var k=c.get(0);
var j=k.getScreenCTM();
var h=k.createSVGPoint();
h.x=i;
h.y=l-jQuery(document).scrollTop();
return h.matrixTransform(j.inverse())
};
d.mouseDown=function(h,i){if(d.chart!==null&&!i){d.chart.deselectAll();
f.startDrag(h,{dragStarted:function(j,l){d.dragSelecting=true;
var k=b.translateCoordinates(j,l);
d.dragSelectionStartPoint=k;
d.dragSelectionRect={x:k.x,y:k.y,width:0,height:0}
},dragging:function(j,m){var l=d.dragSelectionStartPoint;
var k=b.translateCoordinates(j,m);
d.dragSelectionRect={x:k.x>l.x?l.x:k.x,y:k.y>l.y?l.y:k.y,width:k.x>l.x?k.x-l.x:l.x-k.x,height:k.y>l.y?k.y-l.y:l.y-k.y}
},dragEnded:function(){d.dragSelecting=false;
d.chart.applySelectionRect(d.dragSelectionRect);
delete d.dragSelectionStartPoint;
delete d.dragSelectionRect
}})
}};
d.mouseMove=function(h,k){if(!k){d.mouseOverConnection=null;
d.mouseOverConnector=null;
d.mouseOverNode=null;
d.mouseOverGroup=null;
var j=b.hitTest(h.clientX,h.clientY);
if(j===null){return
}if(!d.draggingConnection){var i=b.checkForHit(j,b.connectionClass);
d.mouseOverConnection=(i&&i.connection)?i.connection:null;
if(d.mouseOverConnection){return
}}var i=b.checkForHit(j,b.connectorClass);
d.mouseOverConnector=(i&&i.connector)?i.connector:null;
var i=b.checkForHit(j,b.nodeClass);
d.mouseOverNode=(i&&i.node)?i.node:null;
var i=b.checkForHit(j,b.groupClass);
d.mouseOverGroup=(i&&i.group)?i.group:null;
if(d.mouseOverGroup!==null){a.mouseOverGroup=d.mouseOverGroup
}else{if(d.mouseOverNode!==null){a.mouseOverGroup=d.mouseOverNode.parentNode()
}else{if(d.mouseOverConnector!==null){a.mouseOverGroup=d.mouseOverConnector.parentNode().parentNode()
}else{a.mouseOverGroup=null
}}}}};
d.editGroupConfiguration=function(h){a.flowGroupName=h.data.name;
a.groupId=h.data.id;
$("#activityEditPopUp").modal("show")
};
a.sample=function(){};
d.editNodeConfiguration=function(h){a.id=angular.copy(h.data.id);
a.serviceName=angular.copy(h.data.name);
a.serviceCode=angular.copy(h.data.serviceCode);
a.designationId=angular.copy(h.data.designation);
a.desgName=angular.copy(h.data.desigName);
a.modifierCode=angular.copy(h.data.modifier);
a.nonModifierCodes=angular.copy(h.data.nonModifierCodes);
a.plans=angular.copy(h.data.plans);
a.noOfPlans=angular.copy(h.data.noOfPlans);
$("#serviceEditPopUp").modal("show")
};
d.editRuleConfiguration=function(h){a.ruleSetId=h.data.ruleset;
$("#rulesPopUp").modal("show")
};
d.nodeMouseDown=function(i,k,l){var j=d.chart;
var h;
f.startDrag(i,{dragStarted:function(m,n){h=b.translateCoordinates(m,n);
if(!k.selected()){j.deselectAll();
k.select()
}},dragging:function(n,q){var p=b.translateCoordinates(n,q);
var o=p.x-h.x;
var m=p.y-h.y;
if(p.x<0||(p.y-5)<0){j.deselectAll()
}j.updateSelectedNodesLocation(o,m,l);
h=p
},dragEnded:function(){var m=new Object();
m.nodeId=k.data.id;
m.x=k.data.x;
m.y=k.data.y;
g.updateCoordinateOfNode(m)
},clicked:function(){j.handleNodeClicked(l,k,i.ctrlKey)
}})
};
d.groupMouseDown=function(i,k){var j=d.chart;
var h;
f.startDrag(i,{dragStarted:function(l,m){h=b.translateCoordinates(l,m);
if(!k.selected()){j.deselectAll();
k.select()
}},dragging:function(m,q){var p=b.translateCoordinates(m,q);
var o=p.x-h.x;
var l=p.y-h.y;
var n=jQuery("#activityFlowFrame").width();
if(p.x<0||(p.y-5)<0||(n+5)<p.x){j.deselectAll()
}j.updateSelectedGroupsLocation(o,l);
h=p
},dragEnded:function(){var l=new Object();
l.groupId=k.data.id;
l.x=k.data.x;
l.y=k.data.y;
g.updateCoordinateOfGroup(l)
},clicked:function(){j.handleGroupClicked(k,i.ctrlKey)
}})
};
d.connectionMouseDown=function(h,i){var j=d.chart;
j.handleConnectionMouseDown(i,h.ctrlKey);
h.stopPropagation();
h.preventDefault()
};
d.connectorMouseDown=function(i,k,h,l,j){if(!j){f.startDrag(i,{dragStarted:function(m,o){var n=b.translateCoordinates(m,o);
d.draggingConnection=true;
d.dragPoint1=flowchart.computeConnectorPos(k,l,j);
d.dragPoint2={x:n.x,y:n.y}
},dragging:function(m,p,n){var o=b.translateCoordinates(m,p);
d.dragPoint1=flowchart.computeConnectorPos(k,l,j);
d.dragPoint2={x:o.x,y:o.y}
},dragEnded:function(){if(d.mouseOverConnector&&d.mouseOverConnector!==h){a.sourceNodeId=h.parentNode().id();
a.destinationNodeId=d.mouseOverConnector.parentNode().id();
if(a.sourceNodeId!==a.destinationNodeId&&d.mouseOverConnector.x()===0){var m={curentNode:a.sourceNodeId,nextNode:a.destinationNodeId,activityVersion:JSON.parse(localStorage.getItem("activityFlowVersion")).value,ruleSet:{rules:[],franchise:JSON.parse(localStorage.getItem("activityFlowFranchise")).otherId}};
d.chart.createNewConnection(e,h,d.mouseOverConnector);
g.saveActivityNodeRoute(m,function(n){if(n.data!==null){d.chart.addRouteId(a.sourceNodeId,a.destinationNodeId,n.data.routeId,n.data.ruleset)
}else{a.addMessage(n.messages[0].message,a.failure)
}})
}}d.draggingConnection=false;
delete d.dragPoint1;
delete d.dragPoint2
}})
}}
}]);