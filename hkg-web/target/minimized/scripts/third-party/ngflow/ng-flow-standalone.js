(function(f,i,b){function e(k){this.support=(typeof File!=="undefined"&&typeof Blob!=="undefined"&&typeof FileList!=="undefined"&&(!!Blob.prototype.slice||!!Blob.prototype.webkitSlice||!!Blob.prototype.mozSlice||false));
if(!this.support){return
}this.supportDirectory=/WebKit/.test(f.navigator.userAgent);
this.files=[];
this.defaults={chunkSize:1024*1024,forceChunkSize:false,simultaneousUploads:3,singleFile:false,fileParameterName:"file",progressCallbacksInterval:500,speedSmoothingFactor:0.1,query:{},headers:{},withCredentials:false,preprocess:null,method:"multipart",prioritizeFirstAndLastChunk:false,target:"/",testChunks:true,generateUniqueIdentifier:null,maxChunkRetries:0,chunkRetryInterval:null,permanentErrors:[404,415,500,501],onDropStopPropagation:false};
this.opts={};
this.events={};
var l=this;
this.onDrop=function(m){if(l.opts.onDropStopPropagation){m.stopPropagation()
}m.preventDefault();
var n=m.dataTransfer;
if(n.items&&n.items[0]&&n.items[0].webkitGetAsEntry){l.webkitReadDataTransfer(m)
}else{l.addFiles(n.files,m)
}};
this.preventEvent=function(m){m.preventDefault()
};
this.opts=e.extend({},this.defaults,k||{})
}e.prototype={on:function(k,l){k=k.toLowerCase();
if(!this.events.hasOwnProperty(k)){this.events[k]=[]
}this.events[k].push(l)
},off:function(l,k){if(l!==b){l=l.toLowerCase();
if(k!==b){if(this.events.hasOwnProperty(l)){h(this.events[l],k)
}}else{delete this.events[l]
}}else{this.events={}
}},fire:function(m,l){l=Array.prototype.slice.call(arguments);
m=m.toLowerCase();
var k=false;
if(this.events.hasOwnProperty(m)){j(this.events[m],function(n){k=n.apply(this,l.slice(1))===false||k
})
}if(m!="catchall"){l.unshift("catchAll");
k=this.fire.apply(this,l)===false||k
}return !k
},webkitReadDataTransfer:function(p){var r=this;
var l=p.dataTransfer.items.length;
var o=[];
j(p.dataTransfer.items,function(t){var s=t.webkitGetAsEntry();
if(!s){q();
return
}if(s.isFile){n(t.getAsFile(),s.fullPath)
}else{s.createReader().readEntries(k,m)
}});
function k(s){l+=s.length;
j(s,function(u){if(u.isFile){var t=u.fullPath;
u.file(function(v){n(v,t)
},m)
}else{if(u.isDirectory){u.createReader().readEntries(k,m)
}}});
q()
}function n(t,s){t.relativePath=s.substring(1);
o.push(t);
q()
}function m(s){throw s
}function q(){if(--l==0){r.addFiles(o,p)
}}},generateUniqueIdentifier:function(l){var m=this.opts.generateUniqueIdentifier;
if(typeof m==="function"){return m(l)
}var k=l.relativePath||l.webkitRelativePath||l.fileName||l.name;
return l.size+"-"+k.replace(/[^0-9a-zA-Z_-]/img,"")
},uploadNextChunk:function(l){var m=false;
if(this.opts.prioritizeFirstAndLastChunk){j(this.files,function(n){if(!n.paused&&n.chunks.length&&n.chunks[0].status()==="pending"&&n.chunks[0].preprocessState===0){n.chunks[0].send();
m=true;
return false
}if(!n.paused&&n.chunks.length>1&&n.chunks[n.chunks.length-1].status()==="pending"&&n.chunks[0].preprocessState===0){n.chunks[n.chunks.length-1].send();
m=true;
return false
}});
if(m){return m
}}j(this.files,function(n){if(!n.paused){j(n.chunks,function(o){if(o.status()==="pending"&&o.preprocessState===0){o.send();
m=true;
return false
}})
}if(m){return false
}});
if(m){return true
}var k=false;
j(this.files,function(n){if(!n.isComplete()){k=true;
return false
}});
if(!k&&!l){d(function(){this.fire("complete")
},this)
}return false
},assignBrowse:function(k,m,l){if(typeof k.length==="undefined"){k=[k]
}j(k,function(o){var n;
if(o.tagName==="INPUT"&&o.type==="file"){n=o
}else{n=i.createElement("input");
n.setAttribute("type","file");
g(n.style,{visibility:"hidden",position:"absolute",display:"none"});
o.appendChild(n);
o.addEventListener("click",function(){n.click()
},false)
}if(!this.opts.singleFile&&!l){n.setAttribute("multiple","multiple")
}if(m){n.setAttribute("webkitdirectory","webkitdirectory")
}var p=this;
n.addEventListener("change",function(q){p.addFiles(q.target.files,q);
q.target.value=""
},false)
},this)
},assignDrop:function(k){if(typeof k.length==="undefined"){k=[k]
}j(k,function(l){l.addEventListener("dragover",this.preventEvent,false);
l.addEventListener("dragenter",this.preventEvent,false);
l.addEventListener("drop",this.onDrop,false)
},this)
},unAssignDrop:function(k){if(typeof k.length==="undefined"){k=[k]
}j(k,function(l){l.removeEventListener("dragover",this.preventEvent);
l.removeEventListener("dragenter",this.preventEvent);
l.removeEventListener("drop",this.onDrop)
},this)
},isUploading:function(){var k=false;
j(this.files,function(l){if(l.isUploading()){k=true;
return false
}});
return k
},upload:function(){if(this.isUploading()){return
}this.fire("uploadStart");
var k=false;
for(var l=1;
l<=this.opts.simultaneousUploads;
l++){k=this.uploadNextChunk(true)||k
}if(!k){d(function(){this.fire("complete")
},this)
}},resume:function(){j(this.files,function(k){k.resume()
})
},pause:function(){j(this.files,function(k){k.pause()
})
},cancel:function(){for(var k=this.files.length-1;
k>=0;
k--){this.files[k].cancel()
}},progress:function(){var l=0;
var k=0;
j(this.files,function(m){l+=m.progress()*m.size;
k+=m.size
});
return k>0?l/k:0
},addFile:function(k,l){this.addFiles([k],l)
},addFiles:function(k,m){var l=[];
j(k,function(n){if(!(n.size%4096===0&&(n.name==="."||n.fileName==="."))&&!this.getFromUniqueIdentifier(this.generateUniqueIdentifier(n))){var o=new a(this,n);
if(this.fire("fileAdded",o,m)){l.push(o)
}}},this);
if(this.fire("filesAdded",l,m)){j(l,function(n){if(this.opts.singleFile&&this.files.length>0){this.removeFile(this.files[0])
}this.files.push(n)
},this)
}this.fire("filesSubmitted",l,m)
},removeFile:function(l){for(var k=this.files.length-1;
k>=0;
k--){if(this.files[k]===l){this.files.splice(k,1);
l.abort()
}}},getFromUniqueIdentifier:function(k){var l=false;
j(this.files,function(m){if(m.uniqueIdentifier===k){l=m
}});
return l
},getSize:function(){var k=0;
j(this.files,function(l){k+=l.size
});
return k
},sizeUploaded:function(){var k=0;
j(this.files,function(l){k+=l.sizeUploaded()
});
return k
},timeRemaining:function(){var k=0;
var l=0;
j(this.files,function(m){if(!m.paused&&!m.error){k+=m.size-m.sizeUploaded();
l+=m.averageSpeed
}});
if(k&&!l){return Number.POSITIVE_INFINITY
}if(!k&&!l){return 0
}return Math.floor(k/l)
}};
function a(k,l){this.flowObj=k;
this.file=l;
this.name=l.fileName||l.name;
this.size=l.size;
this.relativePath=l.relativePath||l.webkitRelativePath||this.name;
this.uniqueIdentifier=k.generateUniqueIdentifier(l);
this.chunks=[];
this.paused=false;
this.error=false;
this.averageSpeed=0;
this.currentSpeed=0;
this._lastProgressCallback=Date.now();
this._prevUploadedSize=0;
this._prevProgress=0;
this.bootstrap()
}a.prototype={measureSpeed:function(){var m=Date.now()-this._lastProgressCallback;
if(!m){return
}var l=this.flowObj.opts.speedSmoothingFactor;
var k=this.sizeUploaded();
this.currentSpeed=Math.max((k-this._prevUploadedSize)/m*1000,0);
this.averageSpeed=l*this.currentSpeed+(1-l)*this.averageSpeed;
this._prevUploadedSize=k
},chunkEvent:function(l,k){switch(l){case"progress":if(Date.now()-this._lastProgressCallback<this.flowObj.opts.progressCallbacksInterval){break
}this.measureSpeed();
this.flowObj.fire("fileProgress",this);
this.flowObj.fire("progress");
this._lastProgressCallback=Date.now();
break;
case"error":this.error=true;
this.abort(true);
this.flowObj.fire("fileError",this,k);
this.flowObj.fire("error",k,this);
break;
case"success":if(this.error){return
}this.measureSpeed();
this.flowObj.fire("fileProgress",this);
this.flowObj.fire("progress");
this._lastProgressCallback=Date.now();
if(this.isComplete()){this.currentSpeed=0;
this.averageSpeed=0;
this.flowObj.fire("fileSuccess",this,k)
}break;
case"retry":this.flowObj.fire("fileRetry",this);
break
}},pause:function(){this.paused=true;
this.abort()
},resume:function(){this.paused=false;
this.flowObj.upload()
},abort:function(k){this.currentSpeed=0;
this.averageSpeed=0;
var l=this.chunks;
if(k){this.chunks=[]
}j(l,function(m){if(m.status()==="uploading"){m.abort();
this.flowObj.uploadNextChunk()
}},this)
},cancel:function(){this.flowObj.removeFile(this)
},retry:function(){this.bootstrap();
this.flowObj.upload()
},bootstrap:function(){this.abort(true);
this.error=false;
this._prevProgress=0;
var k=this.flowObj.opts.forceChunkSize?Math.ceil:Math.floor;
var m=Math.max(k(this.file.size/this.flowObj.opts.chunkSize),1);
for(var l=0;
l<m;
l++){this.chunks.push(new c(this.flowObj,this,l))
}},progress:function(){if(this.error){return 1
}if(this.chunks.length===1){this._prevProgress=Math.max(this._prevProgress,this.chunks[0].progress());
return this._prevProgress
}var l=0;
j(this.chunks,function(m){l+=m.progress()*(m.endByte-m.startByte)
});
var k=l/this.size;
this._prevProgress=Math.max(this._prevProgress,k>0.999?1:k);
return this._prevProgress
},isUploading:function(){var k=false;
j(this.chunks,function(l){if(l.status()==="uploading"){k=true;
return false
}});
return k
},isComplete:function(){var k=false;
j(this.chunks,function(m){var l=m.status();
if(l==="pending"||l==="uploading"||m.preprocessState===1){k=true;
return false
}});
return !k
},sizeUploaded:function(){var k=0;
j(this.chunks,function(l){k+=l.sizeUploaded()
});
return k
},timeRemaining:function(){if(this.paused||this.error){return 0
}var k=this.size-this.sizeUploaded();
if(k&&!this.averageSpeed){return Number.POSITIVE_INFINITY
}if(!k&&!this.averageSpeed){return 0
}return Math.floor(k/this.averageSpeed)
},getType:function(){return this.file.type&&this.file.type.split("/")[1]
},getExtension:function(){return this.name.substr((~-this.name.lastIndexOf(".")>>>0)+2).toLowerCase()
}};
function c(k,l,n){this.flowObj=k;
this.fileObj=l;
this.fileObjSize=l.size;
this.offset=n;
this.tested=false;
this.retries=0;
this.pendingRetry=false;
this.preprocessState=0;
this.loaded=0;
this.total=0;
var o=this.flowObj.opts.chunkSize;
this.startByte=this.offset*o;
this.endByte=Math.min(this.fileObjSize,(this.offset+1)*o);
this.xhr=null;
if(this.fileObjSize-this.endByte<o&&!this.flowObj.opts.forceChunkSize){this.endByte=this.fileObjSize
}var m=this;
this.progressHandler=function(p){if(p.lengthComputable){m.loaded=p.loaded;
m.total=p.total
}m.fileObj.chunkEvent("progress")
};
this.testHandler=function(q){var p=m.status();
if(p==="success"){m.tested=true;
m.fileObj.chunkEvent(p,m.message());
m.flowObj.uploadNextChunk()
}else{if(!m.fileObj.paused){m.tested=true;
m.send()
}}};
this.doneHandler=function(q){var p=m.status();
if(p==="success"||p==="error"){m.fileObj.chunkEvent(p,m.message());
m.flowObj.uploadNextChunk()
}else{m.fileObj.chunkEvent("retry",m.message());
m.pendingRetry=true;
m.abort();
m.retries++;
var r=m.flowObj.opts.chunkRetryInterval;
if(r!==null){setTimeout(function(){m.send()
},r)
}else{m.send()
}}}
}c.prototype={getParams:function(){return{flowChunkNumber:this.offset+1,flowChunkSize:this.flowObj.opts.chunkSize,flowCurrentChunkSize:this.endByte-this.startByte,flowTotalSize:this.fileObjSize,flowIdentifier:this.fileObj.uniqueIdentifier,flowFilename:this.fileObj.name,flowRelativePath:this.fileObj.relativePath,flowTotalChunks:this.fileObj.chunks.length}
},getTarget:function(l){var k=this.flowObj.opts.target;
if(k.indexOf("?")<0){k+="?"
}else{k+="&"
}return k+l.join("&")
},test:function(){this.xhr=new XMLHttpRequest();
this.xhr.addEventListener("load",this.testHandler,false);
this.xhr.addEventListener("error",this.testHandler,false);
var k=this.prepareXhrRequest("GET");
this.xhr.send(k)
},preprocessFinished:function(){this.preprocessState=2;
this.send()
},send:function(){var m=this.flowObj.opts.preprocess;
if(typeof m==="function"){switch(this.preprocessState){case 0:m(this);
this.preprocessState=1;
return;
case 1:return;
case 2:break
}}if(this.flowObj.opts.testChunks&&!this.tested){this.test();
return
}this.loaded=0;
this.total=0;
this.pendingRetry=false;
var l=(this.fileObj.file.slice?"slice":(this.fileObj.file.mozSlice?"mozSlice":(this.fileObj.file.webkitSlice?"webkitSlice":"slice")));
var k=this.fileObj.file[l](this.startByte,this.endByte);
this.xhr=new XMLHttpRequest();
this.xhr.upload.addEventListener("progress",this.progressHandler,false);
this.xhr.addEventListener("load",this.doneHandler,false);
this.xhr.addEventListener("error",this.doneHandler,false);
var n=this.prepareXhrRequest("POST",this.flowObj.opts.method,k);
this.xhr.withCredentials=true;
this.xhr.send(n)
},abort:function(){var k=this.xhr;
this.xhr=null;
if(k){k.abort()
}},status:function(){if(this.pendingRetry){return"uploading"
}else{if(!this.xhr){return"pending"
}else{if(this.xhr.readyState<4){return"uploading"
}else{if(this.xhr.status==200){return"success"
}else{if(this.flowObj.opts.permanentErrors.indexOf(this.xhr.status)>-1||this.retries>=this.flowObj.opts.maxChunkRetries){return"error"
}else{this.abort();
return"pending"
}}}}}},message:function(){return this.xhr?this.xhr.responseText:""
},progress:function(){if(this.pendingRetry){return 0
}var k=this.status();
if(k==="success"||k==="error"){return 1
}else{if(k==="pending"){return 0
}else{return this.total>0?this.loaded/this.total:0
}}},sizeUploaded:function(){var k=this.endByte-this.startByte;
if(this.status()!=="success"){k=this.progress()*k
}return k
},prepareXhrRequest:function(q,l,k){var n=this.flowObj.opts.query;
if(typeof n==="function"){n=n(this.fileObj,this)
}n=g(this.getParams(),n);
var o=this.flowObj.opts.target;
var m=null;
if(q==="GET"||l==="octet"){var p=[];
j(n,function(s,r){p.push([encodeURIComponent(r),encodeURIComponent(s)].join("="))
});
o=this.getTarget(p);
m=k||null
}else{m=new FormData();
j(n,function(s,r){m.append(r,s)
});
m.append(this.flowObj.opts.fileParameterName,k)
}this.xhr.open(q,o);
this.xhr.withCredentials=this.flowObj.opts.withCredentials;
j(this.flowObj.opts.headers,function(s,r){this.xhr.setRequestHeader(r,s)
},this);
return m
}};
function h(m,l){var k=m.indexOf(l);
if(k>-1){m.splice(k,1)
}}function d(l,k){setTimeout(l.bind(k),0)
}function g(l,k){j(arguments,function(m){if(m!==l){j(m,function(o,n){l[n]=o
})
}});
return l
}e.extend=g;
function j(m,n,l){if(!m){return
}var k;
if(typeof(m.length)!=="undefined"){for(k=0;
k<m.length;
k++){if(n.call(l,m[k],k)===false){return
}}}else{for(k in m){if(m.hasOwnProperty(k)&&n.call(l,m[k],k)===false){return
}}}}e.each=j;
e.FlowFile=a;
e.FlowChunk=c;
e.version="2.4.0";
if(typeof module==="object"&&module&&typeof module.exports==="object"){module.exports=e
}else{f.Flow=e;
if(typeof define==="function"&&define.amd){define("flow",[],function(){return e
})
}}})(window,document);
angular.module("flow.provider",[]).provider("flowFactory",function(){this.defaults={};
this.factory=function(a){return new Flow(a)
};
this.events=[];
this.on=function(a,b){this.events.push([a,b])
};
this.$get=function(){var b=this.factory;
var c=this.defaults;
var a=this.events;
return{create:function(e){var d=b(angular.extend({},c,e));
angular.forEach(a,function(f){d.on(f[0],f[1])
});
return d
}}
}
});
angular.module("flow.init",["flow.provider"]).controller("flowCtrl",["$scope","$attrs","$parse","flowFactory",function(c,a,e,f){var d=angular.extend({},c.$eval(a.flowInit));
var b=f.create(d);
b.on("catchAll",function(g){var h=Array.prototype.slice.call(arguments);
h.shift();
var i=c.$broadcast.apply(c,["flow::"+g,b].concat(h));
if({progress:1,filesSubmitted:1,fileSuccess:1,fileError:1,complete:1}[g]){c.$apply()
}if(i.defaultPrevented){return false
}});
c.$flow=b;
if(a.hasOwnProperty("flowName")){e(a.flowName).assign(c,b);
c.$on("$destroy",function(){e(a.flowName).assign(c)
})
}}]).directive("flowInit",[function(){return{scope:true,controller:"flowCtrl"}
}]);
angular.module("flow.btn",["flow.init"]).directive("flowBtn",[function(){return{restrict:"EA",scope:false,require:"^flowInit",link:function(e,c,b){var d=b.hasOwnProperty("flowDirectory");
var a=b.hasOwnProperty("flowSingleFile");
e.$flow.assignBrowse(c,d,a)
}}
}]);
angular.module("flow.dragEvents",["flow.init"]).directive("flowPreventDrop",function(){return{scope:false,link:function(c,b,a){b.bind("drop dragover",function(d){d.preventDefault()
})
}}
}).directive("flowDragEnter",["$timeout",function(a){return{scope:false,link:function(d,c,b){var f;
var e=false;
c.bind("dragover",function(h){if(!g(h)){return
}if(!e){d.$apply(b.flowDragEnter);
e=true
}a.cancel(f);
h.preventDefault()
});
c.bind("dragleave drop",function(h){f=a(function(){d.$eval(b.flowDragLeave);
f=null;
e=false
},100)
});
function g(j){var h=false;
var i=j.dataTransfer||j.originalEvent.dataTransfer;
angular.forEach(i&&i.types,function(k){if(k==="Files"){h=true
}});
return h
}}}
}]);
angular.module("flow.drop",["flow.init"]).directive("flowDrop",function(){return{scope:false,require:"^flowInit",link:function(c,b,a){if(a.flowDropEnabled){c.$watch(a.flowDropEnabled,function(f){if(f){e()
}else{d()
}})
}else{e()
}function e(){c.$flow.assignDrop(b)
}function d(){c.$flow.unAssignDrop(b)
}}}
});
!function(a){var c=a.module("flow.events",["flow.init"]);
var b={fileSuccess:["$file","$message"],fileProgress:["$file"],fileAdded:["$file","$event"],filesAdded:["$files","$event"],filesSubmitted:["$files","$event"],fileRetry:["$file"],fileError:["$file","$message"],uploadStart:[],complete:[],progress:[],error:["$message","$file"]};
a.forEach(b,function(f,e){var g="flow"+d(e);
if(g=="flowUploadStart"){g="flowUploadStarted"
}c.directive(g,[function(){return{require:"^flowInit",controller:["$scope","$attrs",function(i,h){i.$on("flow::"+e,function(){var l=Array.prototype.slice.call(arguments);
var k=l.shift();
if(i.$flow!==l.shift()){return
}var j={};
a.forEach(f,function(n,m){j[n]=l[m]
});
if(i.$eval(h[g],j)===false){k.preventDefault()
}})
}]}
}])
});
function d(e){return e.charAt(0).toUpperCase()+e.slice(1)
}}(angular);
angular.module("flow.img",["flow.init"]).directive("flowImg",[function(){return{scope:false,require:"^flowInit",link:function(d,c,a){var b=a.flowImg;
d.$watch(b,function(f){if(!f){return
}var e=new FileReader();
e.readAsDataURL(f.file);
e.onload=function(g){d.$apply(function(){a.$set("src",g.target.result)
})
}
})
}}
}]);
angular.module("flow.transfers",["flow.init"]).directive("flowTransfers",[function(){return{scope:true,require:"^flowInit",link:function(a){a.transfers=a.$flow.files
}}
}]);
angular.module("flow",["flow.provider","flow.init","flow.events","flow.btn","flow.drop","flow.transfers","flow.img","flow.dragEvents"]);