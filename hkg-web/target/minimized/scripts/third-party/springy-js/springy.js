var Springy={};
var Graph=Springy.Graph=function(){this.nodeSet={};
this.nodes=[];
this.edges=[];
this.adjacency={};
this.nextNodeId=0;
this.nextEdgeId=0;
this.eventListeners=[]
};
var Node=Springy.Node=function(b,a){this.id=b;
this.data=(a!==undefined)?a:{}
};
var Edge=Springy.Edge=function(d,b,c,a){this.id=d;
this.source=b;
this.target=c;
this.data=(a!==undefined)?a:{}
};
Graph.prototype.addNode=function(a){if(!(a.id in this.nodeSet)){this.nodes.push(a)
}this.nodeSet[a.id]=a;
this.notify();
return a
};
Graph.prototype.addNodes=function(){for(var a=0;
a<arguments.length;
a++){var c=arguments[a].id;
var b=new Node(c,arguments[a].data);
this.addNode(b)
}};
Graph.prototype.addEdge=function(b){var a=false;
this.edges.forEach(function(c){if(b.id===c.id){a=true
}});
if(!a){this.edges.push(b)
}if(!(b.source.id in this.adjacency)){this.adjacency[b.source.id]={}
}if(!(b.target.id in this.adjacency[b.source.id])){this.adjacency[b.source.id][b.target.id]=[]
}a=false;
this.adjacency[b.source.id][b.target.id].forEach(function(c){if(b.id===c.id){a=true
}});
if(!a){this.adjacency[b.source.id][b.target.id].push(b)
}this.notify();
return b
};
Graph.prototype.addEdges=function(){for(var d=0;
d<arguments.length;
d++){var f=arguments[d];
var c=this.nodeSet[f.fromNode];
if(c==undefined){throw new TypeError("invalid node name: "+f.fromNode)
}var b=this.nodeSet[f.toNode];
if(b==undefined){throw new TypeError("invalid node name: "+f.toNode)
}var a=f.data;
this.newEdge(c,b,a)
}};
Graph.prototype.newNode=function(b){var a=new Node(this.nextNodeId++,b);
this.addNode(a);
return a
};
Graph.prototype.newEdge=function(c,d,b){var a=new Edge(this.nextEdgeId++,c,d,b);
this.addEdge(a);
return a
};
Graph.prototype.loadJSON=function(a){if(typeof a=="string"||a instanceof String){a=JSON.parse(a)
}if("nodes" in a||"edges" in a){this.addNodes.apply(this,a.nodes);
this.addEdges.apply(this,a.edges)
}};
Graph.prototype.getEdges=function(b,a){if(b.id in this.adjacency&&a.id in this.adjacency[b.id]){return this.adjacency[b.id][a.id]
}return[]
};
Graph.prototype.removeNode=function(b){if(b.id in this.nodeSet){delete this.nodeSet[b.id]
}for(var a=this.nodes.length-1;
a>=0;
a--){if(this.nodes[a].id===b.id){this.nodes.splice(a,1)
}}this.detachNode(b)
};
Graph.prototype.detachNode=function(a){var b=this.edges.slice();
b.forEach(function(c){if(c.source.id===a.id||c.target.id===a.id){this.removeEdge(c)
}},this);
this.notify()
};
Graph.prototype.removeEdge=function(e){for(var d=this.edges.length-1;
d>=0;
d--){if(this.edges[d].id===e.id){this.edges.splice(d,1)
}}for(var a in this.adjacency){for(var f in this.adjacency[a]){var b=this.adjacency[a][f];
for(var c=b.length-1;
c>=0;
c--){if(this.adjacency[a][f][c].id===e.id){this.adjacency[a][f].splice(c,1)
}}if(this.adjacency[a][f].length==0){delete this.adjacency[a][f]
}}if(isEmpty(this.adjacency[a])){delete this.adjacency[a]
}}this.notify()
};
Graph.prototype.merge=function(b){var a=[];
b.nodes.forEach(function(c){a.push(this.addNode(new Node(c.id,c.data)))
},this);
b.edges.forEach(function(d){var h=a[d.from];
var g=a[d.to];
var f=(d.directed)?(f=d.type+"-"+h.id+"-"+g.id):(h.id<g.id)?d.type+"-"+h.id+"-"+g.id:d.type+"-"+g.id+"-"+h.id;
var c=this.addEdge(new Edge(f,h,g,d.data));
c.data.type=d.type
},this)
};
Graph.prototype.filterNodes=function(a){var b=this.nodes.slice();
b.forEach(function(c){if(!a(c)){this.removeNode(c)
}},this)
};
Graph.prototype.filterEdges=function(a){var b=this.edges.slice();
b.forEach(function(c){if(!a(c)){this.removeEdge(c)
}},this)
};
Graph.prototype.addGraphListener=function(a){this.eventListeners.push(a)
};
Graph.prototype.notify=function(){this.eventListeners.forEach(function(a){a.graphChanged()
})
};
var Layout=Springy.Layout={};
Layout.ForceDirected=function(e,a,b,c,d){this.graph=e;
this.stiffness=a;
this.repulsion=b;
this.damping=c;
this.minEnergyThreshold=d||0.01;
this.nodePoints={};
this.edgeSprings={}
};
Layout.ForceDirected.prototype.point=function(b){if(!(b.id in this.nodePoints)){var a=(b.data.mass!==undefined)?b.data.mass:1;
this.nodePoints[b.id]=new Layout.ForceDirected.Point(Vector.random(),a)
}return this.nodePoints[b.id]
};
Layout.ForceDirected.prototype.spring=function(b){if(!(b.id in this.edgeSprings)){var a=(b.data.length!==undefined)?b.data.length:1;
var c=false;
var e=this.graph.getEdges(b.source,b.target);
e.forEach(function(f){if(c===false&&f.id in this.edgeSprings){c=this.edgeSprings[f.id]
}},this);
if(c!==false){return new Layout.ForceDirected.Spring(c.point1,c.point2,0,0)
}var d=this.graph.getEdges(b.target,b.source);
e.forEach(function(f){if(c===false&&f.id in this.edgeSprings){c=this.edgeSprings[f.id]
}},this);
if(c!==false){return new Layout.ForceDirected.Spring(c.point2,c.point1,0,0)
}this.edgeSprings[b.id]=new Layout.ForceDirected.Spring(this.point(b.source),this.point(b.target),a,this.stiffness)
}return this.edgeSprings[b.id]
};
Layout.ForceDirected.prototype.eachNode=function(b){var a=this;
this.graph.nodes.forEach(function(c){b.call(a,c,a.point(c))
})
};
Layout.ForceDirected.prototype.eachEdge=function(b){var a=this;
this.graph.edges.forEach(function(c){b.call(a,c,a.spring(c))
})
};
Layout.ForceDirected.prototype.eachSpring=function(b){var a=this;
this.graph.edges.forEach(function(c){b.call(a,a.spring(c))
})
};
Layout.ForceDirected.prototype.applyCoulombsLaw=function(){this.eachNode(function(b,a){this.eachNode(function(e,c){if(a!==c){var g=a.p.subtract(c.p);
var h=g.magnitude()+0.1;
var f=g.normalise();
a.applyForce(f.multiply(this.repulsion).divide(h*h*0.5));
c.applyForce(f.multiply(this.repulsion).divide(h*h*-0.5))
}})
})
};
Layout.ForceDirected.prototype.applyHookesLaw=function(){this.eachSpring(function(a){var e=a.point2.p.subtract(a.point1.p);
var b=a.length-e.magnitude();
var c=e.normalise();
a.point1.applyForce(c.multiply(a.k*b*-0.5));
a.point2.applyForce(c.multiply(a.k*b*0.5))
})
};
Layout.ForceDirected.prototype.attractToCentre=function(){this.eachNode(function(b,a){var c=a.p.multiply(-1);
a.applyForce(c.multiply(this.repulsion/50))
})
};
Layout.ForceDirected.prototype.updateVelocity=function(a){this.eachNode(function(c,b){b.v=b.v.add(b.a.multiply(a)).multiply(this.damping);
b.a=new Vector(0,0)
})
};
Layout.ForceDirected.prototype.updatePosition=function(a){this.eachNode(function(c,b){b.p=b.p.add(b.v.multiply(a))
})
};
Layout.ForceDirected.prototype.totalEnergy=function(a){var b=0;
this.eachNode(function(d,c){var e=c.v.magnitude();
b+=0.5*c.m*e*e
});
return b
};
var __bind=function(a,b){return function(){return a.apply(b,arguments)
}
};
Springy.requestAnimationFrame=__bind(this.requestAnimationFrame||this.webkitRequestAnimationFrame||this.mozRequestAnimationFrame||this.oRequestAnimationFrame||this.msRequestAnimationFrame||(function(b,a){this.setTimeout(b,10)
}),this);
Layout.ForceDirected.prototype.start=function(c,e,b){var a=this;
if(this._started){return
}this._started=true;
this._stop=false;
if(b!==undefined){b()
}Springy.requestAnimationFrame(function d(){a.tick(0.03);
if(c!==undefined){c()
}if(a._stop||a.totalEnergy()<a.minEnergyThreshold){a._started=false;
if(e!==undefined){e()
}}else{Springy.requestAnimationFrame(d)
}})
};
Layout.ForceDirected.prototype.stop=function(){this._stop=true
};
Layout.ForceDirected.prototype.tick=function(a){this.applyCoulombsLaw();
this.applyHookesLaw();
this.attractToCentre();
this.updateVelocity(a);
this.updatePosition(a)
};
Layout.ForceDirected.prototype.nearest=function(c){var b={node:null,point:null,distance:null};
var a=this;
this.graph.nodes.forEach(function(f){var d=a.point(f);
var e=d.p.subtract(c).magnitude();
if(b.distance===null||e<b.distance){b={node:f,point:d,distance:e}
}if(f.id===5){}});
return b
};
Layout.ForceDirected.prototype.getBoundingBox=function(){var a=new Vector(-2,-2);
var c=new Vector(2,2);
this.eachNode(function(e,d){if(d.p.x<a.x){a.x=d.p.x
}if(d.p.y<a.y){a.y=d.p.y
}if(d.p.x>c.x){c.x=d.p.x
}if(d.p.y>c.y){c.y=d.p.y
}});
var b=c.subtract(a).multiply(0.07);
return{bottomleft:a.subtract(b),topright:c.add(b)}
};
var Vector=Springy.Vector=function(a,b){this.x=a;
this.y=b
};
Vector.random=function(){return new Vector(10*(Math.random()-0.5),10*(Math.random()-0.5))
};
Vector.prototype.add=function(a){return new Vector(this.x+a.x,this.y+a.y)
};
Vector.prototype.subtract=function(a){return new Vector(this.x-a.x,this.y-a.y)
};
Vector.prototype.multiply=function(a){return new Vector(this.x*a,this.y*a)
};
Vector.prototype.divide=function(a){return new Vector((this.x/a)||0,(this.y/a)||0)
};
Vector.prototype.magnitude=function(){return Math.sqrt(this.x*this.x+this.y*this.y)
};
Vector.prototype.normal=function(){return new Vector(-this.y,this.x)
};
Vector.prototype.normalise=function(){return this.divide(this.magnitude())
};
Layout.ForceDirected.Point=function(a,b){this.p=a;
this.m=b;
this.v=new Vector(0,0);
this.a=new Vector(0,0)
};
Layout.ForceDirected.Point.prototype.applyForce=function(a){this.a=this.a.add(a.divide(this.m))
};
Layout.ForceDirected.Spring=function(c,b,d,a){this.point1=c;
this.point2=b;
this.length=d;
this.k=a
};
var Renderer=Springy.Renderer=function(e,a,d,c,f,b){this.layout=e;
this.clear=a;
this.drawEdge=d;
this.drawNode=c;
this.onRenderStop=f;
this.onRenderStart=b;
this.layout.graph.addGraphListener(this)
};
Renderer.prototype.graphChanged=function(a){this.start()
};
Renderer.prototype.start=function(a){var b=this;
this.layout.start(function c(){b.clear();
b.layout.eachEdge(function(e,d){b.drawEdge(e,d.point1.p,d.point2.p)
});
b.layout.eachNode(function(e,d){b.drawNode(e,d.p)
})
},this.onRenderStop,this.onRenderStart)
};
Renderer.prototype.stop=function(){this.layout.stop()
};
if(!Array.prototype.forEach){Array.prototype.forEach=function(g,b){var d,c;
if(this==null){throw new TypeError(" this is null or not defined")
}var f=Object(this);
var a=f.length>>>0;
if({}.toString.call(g)!="[object Function]"){throw new TypeError(g+" is not a function")
}if(b){d=b
}c=0;
while(c<a){var e;
if(c in f){e=f[c];
g.call(d,e,c,f)
}c++
}}
}var isEmpty=function(b){for(var a in b){if(b.hasOwnProperty(a)){return false
}}return true
};