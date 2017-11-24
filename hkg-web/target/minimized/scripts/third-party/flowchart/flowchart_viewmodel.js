var flowchart={};
(function(){flowchart.width=400;
flowchart.height=300;
flowchart.boundryGap=30;
flowchart.boundryGapX=15;
flowchart.boundryGapY=15;
flowchart.boundryGapNodeX=15;
flowchart.boundryGapNodeY=45;
flowchart.nodeWidth=190;
flowchart.groupWidth=150;
flowchart.groupHeight=100;
flowchart.nodeNameHeight=40;
flowchart.connectorHeight=35;
flowchart.ruleLineV=25;
flowchart.computeConnectorY=function(d){return flowchart.nodeNameHeight+(d*flowchart.connectorHeight)
};
flowchart.computeConnectorPos=function(d,e,f){return{x:d.x()+d.parentNode().x()+(f?0:flowchart.nodeWidth),y:d.y()+d.parentNode().y()+flowchart.computeConnectorY(e)}
};
flowchart.ConnectorViewModel=function(f,e,h,d,g){this.data=f;
this._parentNode=d;
this._x=e;
this._y=h;
this.name=function(){return this.data.name
};
this.x=function(){return this._x
};
this.y=function(){return this._y
};
this.parentNode=function(){return this._parentNode
}
};
var c=function(f,e,d){var g=[];
if(f){for(var j=0;
j<f.length;
++j){var h=new flowchart.ConnectorViewModel(f[j],e,flowchart.computeConnectorY(j),d);
g.push(h)
}}return g
};
flowchart.GroupViewModel=function(d){this.data=d;
this.nodes=a(this.data.nodes,this);
this._selected=false;
this.name=function(){return this.data.name||""
};
this.x=function(){if(this.data.x<=flowchart.boundryGapX){this.data.x=flowchart.boundryGapX
}return this.data.x
};
this.y=function(){if(this.data.y<=flowchart.boundryGapY){this.data.y=flowchart.boundryGapY
}return this.data.y
};
this.width=function(){var j=flowchart.groupWidth;
for(var g=0;
g<this.nodes.length;
g++){var h=this.nodes[g].data.x+this.nodes[g].width()+flowchart.boundryGapNodeX;
if(h>j){j=h
}}if(j<(this.data.name.length*10)){j=this.data.name.length*10
}else{if(j<300){j=300
}}var f=this.data.x+j+flowchart.boundryGapX;
var e=flowchart.width;
if(f>e){flowchart.width=f+50;
jQuery("#containerDiv").css("width",flowchart.width+"px")
}return j
};
this.height=function(){var e=flowchart.groupHeight;
for(var g=0;
g<this.nodes.length;
g++){var f=this.nodes[g].data.y+this.nodes[g].height()+flowchart.boundryGapY;
if(f>e){e=f
}}if(e<200){e=200
}var h=this.data.y+e+flowchart.boundryGapY;
var j=flowchart.height;
if(h>j){flowchart.height=h;
jQuery("#containerDiv").css("height",flowchart.height+"px")
}return e
};
this.select=function(){this._selected=true
};
this.deselect=function(){this._selected=false
};
this.toggleSelected=function(){this._selected=!this._selected
};
this.selected=function(){return this._selected
};
this.addNode=function(e){if(!this.data.nodes){this.data.nodes=[]
}var f=new flowchart.NodeViewModel(e,this);
this.nodes.push(f);
this.data.nodes.push(e)
};
if(this.data.active===true){this._selected=true
}};
flowchart.NodeViewModel=function(e,d){this.data=e;
this._parentNode=d;
this.inputConnectors=c(this.data.inputConnectors,0,this);
this.outputConnectors=c(this.data.outputConnectors,flowchart.nodeWidth,this);
this._selected=false;
this.id=function(){return this.data.id
};
this.name=function(){return this.data.name||""
};
this.description=function(){return this.data.description||""
};
this.x=function(){if(this.data.x<=flowchart.boundryGapNodeX){this.data.x=flowchart.boundryGapNodeX
}return this.data.x
};
this.y=function(){if(this.data.y<=flowchart.boundryGapNodeY){this.data.y=flowchart.boundryGapNodeY
}return this.data.y
};
this.width=function(){return flowchart.nodeWidth
};
this.height=function(){var f=Math.max(this.inputConnectors.length,this.outputConnectors.length);
return flowchart.computeConnectorY(f)
};
this.select=function(){this._selected=true
};
this.deselect=function(){this._selected=false
};
this.toggleSelected=function(){this._selected=!this._selected
};
this.selected=function(){return this._selected
};
this._addConnector=function(j,f,h,g){var i=new flowchart.ConnectorViewModel(j,f,flowchart.computeConnectorY(g.length),this);
h.push(j);
g.push(i)
};
this.addInputConnector=function(f){if(!this.data.inputConnectors){this.data.inputConnectors=[]
}this._addConnector(f,0,this.data.inputConnectors,this.inputConnectors)
};
this.addOutputConnector=function(f){if(!this.data.outputConnectors){this.data.outputConnectors=[]
}this._addConnector(f,flowchart.nodeWidth,this.data.outputConnectors,this.outputConnectors)
};
this.parentNode=function(){return this._parentNode
};
if(this.data.active===true){this._selected=true
}};
var a=function(e,g){var d=[];
if(e){for(var f=0;
f<e.length;
++f){d.push(new flowchart.NodeViewModel(e[f],g))
}}return d
};
var b=function(f){var e=[];
if(f){for(var d=0;
d<f.length;
++d){e.push(new flowchart.GroupViewModel(f[d]))
}}return e
};
flowchart.ConnectionViewModel=function(f,e,d){this.data=f;
this.source=e;
this.dest=d;
this._selected=false;
this.sourceCoordX=function(){return this.source.parentNode().x()+this.source.parentNode().parentNode().x()+this.source.x()
};
this.sourceCoordY=function(){return this.source.parentNode().y()+this.source.parentNode().parentNode().y()+this.source.y()
};
this.sourceCoord=function(){return{x:this.sourceCoordX(),y:this.sourceCoordY()}
};
this.destCoordX=function(){return this.dest.parentNode().x()+this.dest.parentNode().parentNode().x()+this.dest.x()
};
this.destCoordY=function(){return this.dest.parentNode().y()+this.dest.parentNode().parentNode().y()+this.dest.y()
};
this.destCoord=function(){return{x:this.destCoordX(),y:this.destCoordY()}
};
this.connectionArea=function(){var g="";
if(this.sourceCoordX()<this.destCoordX()){g="M "+this.sourceCoordX()+" "+this.sourceCoordY()+" L "+this.destCoordX()+" "+this.destCoordY()
}else{g="M "+this.sourceCoordX()+" "+this.sourceCoordY()+" H "+(this.sourceCoordX()+flowchart.ruleLineV)+" V "+((this.sourceCoordY()+this.destCoordY())/2)+" H "+(this.destCoordX()-flowchart.ruleLineV)+" V "+this.destCoordY()+" H "+this.destCoordX()+" H "+(this.destCoordX()-flowchart.ruleLineV)+" V "+((this.sourceCoordY()+this.destCoordY())/2)+" H "+(this.sourceCoordX()+flowchart.ruleLineV)+" V "+this.sourceCoordY()+" Z"
}return g
};
this.select=function(){this._selected=true
};
this.deselect=function(){this._selected=false
};
this.toggleSelected=function(){this._selected=!this._selected
};
this.selected=function(){return this._selected
};
if(this.data.active===true){this._selected=true
}};
flowchart.ChartViewModel=function(d){jQuery("#containerDiv").css("width",flowchart.width+"px");
jQuery("#containerDiv").css("height",flowchart.height+"px");
this.findNode=function(h){for(var e=0;
e<this.groups.length;
++e){for(var f=0;
f<this.groups[e].nodes.length;
++f){var g=this.groups[e].nodes[f];
if(g.data.id===h){return g
}}}throw new Error("Failed to find node "+h)
};
this.findGroup=function(f){for(var e=0;
e<this.groups.length;
++e){var g=this.groups[e];
if(g.data.id===f){return g
}}throw new Error("Failed to find group "+f)
};
this.findInputConnector=function(g,f){var e=this.findNode(g);
if(!e.inputConnectors||e.inputConnectors.length<=f){throw new Error("Node "+g+" has invalid input connectors.")
}return e.inputConnectors[f]
};
this.findOutputConnector=function(g,f){var e=this.findNode(g);
if(!e.outputConnectors||e.outputConnectors.length<=f){throw new Error("Node "+g+" has invalid output connectors.")
}return e.outputConnectors[f]
};
this._createConnectionViewModel=function(g){var f=this.findOutputConnector(g.source.nodeID,g.source.connectorIndex);
var e=this.findInputConnector(g.dest.nodeID,g.dest.connectorIndex);
return new flowchart.ConnectionViewModel(g,f,e)
};
this._createConnectionsViewModel=function(e){var g=[];
if(e){for(var f=0;
f<e.length;
++f){g.push(this._createConnectionViewModel(e[f]))
}}return g
};
this.data=d;
this.groups=b(this.data.groups);
this.connections=this._createConnectionsViewModel(this.data.connections);
this.isreadonly=this.data.isreadonly;
this.createNewConnection=function(f,e,o){var k=this.data.connections;
if(!k){k=this.data.connections=[]
}var g=this.connections;
if(!g){g=this.connections=[]
}var j=e.parentNode();
var p=j.outputConnectors.indexOf(e);
if(p==-1){p=j.inputConnectors.indexOf(e);
if(p==-1){throw new Error("Failed to find source connector within either inputConnectors or outputConnectors of source node.")
}}var l=o.parentNode();
var i=l.inputConnectors.indexOf(o);
if(i==-1){i=l.outputConnectors.indexOf(o);
if(i==-1){throw new Error("Failed to find dest connector within inputConnectors or ouputConnectors of dest node.")
}}var h=undefined;
if(g!==null){h=f("filter")(g,function(q){return(q.data.source.nodeID===j.data.id&&q.data.dest.nodeID===l.data.id)
})[0]
}if(h===undefined){var m={source:{nodeID:j.data.id,connectorIndex:p},dest:{nodeID:l.data.id,connectorIndex:i}};
k.push(m);
var n=new flowchart.ConnectionViewModel(m,e,o);
g.push(n)
}};
this.addGroup=function(e){if(!this.data.groups){this.data.groups=[]
}this.data.groups.push(e);
this.groups.push(new flowchart.GroupViewModel(e))
};
this.addConnection=function(f,e,g){if(!this.data.connections){this.data.connections=[]
}this.data.connections.push(f);
this.connections.push(new flowchart.ConnectionViewModel()(f,e,g))
};
this.selectAll=function(){var e=this.groups;
for(var k=0;
k<e.length;
++k){var m=e[k];
m.select();
for(var g=0;
g<m.nodes.length;
++g){var l=m.nodes[g];
l.select()
}}var h=this.connections;
for(var k=0;
k<h.length;
++k){var f=h[k];
f.select()
}};
this.deselectAll=function(){var e=this.groups;
for(var k=0;
k<e.length;
++k){var m=e[k];
m.deselect();
for(var g=0;
g<m.nodes.length;
++g){var l=m.nodes[g];
l.deselect()
}}var h=this.connections;
for(var k=0;
k<h.length;
++k){var f=h[k];
f.deselect()
}};
this.deselectAllNodesOfGroup=function(l){l.deselect();
for(var f=0;
f<l.nodes.length;
++f){var k=l.nodes[f];
k.deselect()
}var h=this.connections;
for(var g=0;
g<h.length;
++g){var e=h[g];
e.deselect()
}};
this.updateSelectedNodesLocation=function(f,e,j){var k=this.getSelectedNodes(j);
for(var g=0;
g<k.length;
++g){var h=k[g];
h.data.x+=f;
h.data.y+=e
}};
this.updateSelectedGroupsLocation=function(g,f){var e=this.getSelectedGroups();
for(var h=0;
h<e.length;
++h){var j=e[h];
j.data.x+=g;
j.data.y+=f
}};
this.handleNodeClicked=function(f,e,g){if(g){e.toggleSelected()
}else{this.deselectAllNodesOfGroup(f);
e.select()
}var h=f.nodes.indexOf(e);
if(h===-1){throw new Error("Failed to find node in view model!")
}f.nodes.splice(h,1);
f.nodes.push(e)
};
this.handleGroupClicked=function(f,g){if(g){f.toggleSelected()
}else{this.deselectAll();
f.select()
}var e=this.groups.indexOf(f);
if(e===-1){throw new Error("Failed to find group in view model!")
}this.groups.splice(e,1);
this.groups.push(f)
};
this.handleConnectionMouseDown=function(e,f){if(f){e.toggleSelected()
}else{this.deselectAll();
e.select()
}};
this.deleteSelected=function(){var e=[];
var l=[];
var p=[];
var o={groupIds:[],nodeIds:[],connectorIds:[],rulesetIds:[]};
for(var r=0;
r<this.groups.length;
++r){var m=[];
var g=[];
var q=this.groups[r];
if(!q.selected()){for(var j=0;
j<q.nodes.length;
++j){var i=q.nodes[j];
if(!i.selected()){m.push(i);
g.push(i.data)
}else{o.nodeIds.push(i.data.id);
p.push(i.data.id)
}}q.data.nodes=g;
q.nodes=m;
e.push(q);
l.push(q.data)
}else{o.groupIds.push(q.data.id);
for(var j=0;
j<q.nodes.length;
++j){var i=q.nodes[j];
p.push(i.data.id)
}}}var n=[];
var h=[];
for(var k=0;
k<this.connections.length;
++k){var f=this.connections[k];
if(!f.selected()&&p.indexOf(f.data.source.nodeID)===-1&&p.indexOf(f.data.dest.nodeID)===-1){n.push(f);
h.push(f.data)
}else{o.connectorIds.push(f.data.id);
o.rulesetIds.push(f.data.ruleset)
}}this.groups=e;
this.data.groups=l;
this.connections=n;
this.data.connections=h;
return o
};
this.cancelled=function(j,f){var h=[];
var i=[];
for(var e=0;
e<this.connections.length;
++e){var g=this.connections[e];
if(!(g.data.source.nodeID===j&&g.data.dest.nodeID===f)){h.push(g);
i.push(g.data)
}}this.connections=h;
this.data.connections=i
};
this.addRouteId=function(k,f,l,g){var i=[];
var j=[];
for(var e=0;
e<this.connections.length;
++e){var h=this.connections[e];
if(h.data.source.nodeID===k&&h.data.dest.nodeID===f){h.data.id=l;
h.id=l;
h.data.ruleset=g;
h.ruleset=g
}i.push(h);
j.push(h.data)
}this.connections=i;
this.data.connections=j
};
this.applySelectionRect=function(k){this.deselectAll();
for(var g=0;
g<this.groups.length;
++g){var l=this.groups[g];
if(l.x()>=k.x&&l.y()>=k.y&&l.x()+l.width()<=k.x+k.width&&l.y()+l.height()<=k.y+k.height){l.select()
}for(var f=0;
f<l.nodes.length;
++f){var h=l.nodes[f];
if(h.x()>=k.x&&h.y()>=k.y&&h.x()+h.width()<=k.x+k.width&&h.y()+h.height()<=k.y+k.height){h.select()
}}}for(var g=0;
g<this.connections.length;
++g){var e=this.connections[g];
if(e.source.parentNode().selected()&&e.dest.parentNode().selected()){e.select()
}}};
this.getSelectedNodes=function(k){var l=[];
for(var e=0;
e<this.groups.length;
++e){var h=this.groups[e];
if(h===k){for(var f=0;
f<h.nodes.length;
++f){var g=h.nodes[f];
if(g.selected()){l.push(g)
}}}}return l
};
this.getSelectedGroups=function(){var e=[];
for(var f=0;
f<this.groups.length;
++f){var g=this.groups[f];
if(g.selected()){e.push(g)
}}return e
};
this.getSelectedConnections=function(){var e=[];
for(var g=0;
g<this.connections.length;
++g){var f=this.connections[g];
if(f.selected()){e.push(f)
}}return e
}
}
})();