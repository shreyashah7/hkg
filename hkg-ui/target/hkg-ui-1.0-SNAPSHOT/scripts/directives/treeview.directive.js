define(['angular'], function () {
    angular.module('hkg.directives').directive('agTreemodel', ['$compile', function ($compile) {
            return {
                restrict: 'A',
                link: function (scope, element, attrs) {


                    //tree id
                    var treeId = attrs.treeId;

                    //tree model
                    var agTreemodel = attrs.agTreemodel;

                    //node id
                    var nodeId = attrs.nodeId || 'id';

                    //node label
                    var nodeLabel = attrs.nodeLabel || 'displayName';


                    if (angular.isDefined(attrs.searchId)) {

                        function setSelectedNode(node, searchId) {

                            if (node === null || node === undefined) {
                                return;
                            }
                            for (var i = 0; i < node.length; i++) {

                                if (node[i].selected == "selected") {
                                    node[i].selected = undefined;
                                }

                                if (node[i].id == searchId) {
                                    node[i].selected = "selected";
                                }
                                else {
                                    for (var j = 0; j < node.length; j++) {
                                        if (node[j].children !== null) {
                                            setSelectedNode(node[j].children, searchId);
                                        }
                                    }

                                }
                            }
                        }

                        scope.$watch(attrs.searchId, function (searchId) {
                            setSelectedNode(scope[attrs.agTreemodel], searchId);
                        });
                        scope.$watch(attrs.agTreemodel, function () {
                            setSelectedNode(scope[attrs.agTreemodel], scope[attrs.searchId]);
                        });

                    }
                    scope.displayCount = attrs.displayCount;
                    if (attrs.entityName != undefined) {
                        scope.entityName = attrs.entityName;
                    } else {
                        scope.entityName = '';
                    }
                    //children
                    var nodeChildren = attrs.nodeChildren || 'children';
                    //Search query
                    var searchQuery = attrs.searchQuery;
                    //tree template
                    var template =
                            '<ul>' +
                            '<li data-ng-repeat="node in ' + agTreemodel + '| filter:' + searchQuery + ' ">' +
                            '<span class="col-xs-12 hkg-nopadding">' +
                            '<span style="display : inline-block" title="Click to Expand" class="glyphicon glyphicon-plus-sign text-hkg" data-ng-show="node.' + nodeChildren + '.length && node.collapsed" data-ng-click="' + treeId + '.selectNodeHead(node, $event)"></span>' +
                            '<span  style=";display : inline-block" title="Click to Collapse" class="glyphicon glyphicon-minus-sign text-hkg" data-ng-show="node.' + nodeChildren + '.length && !node.collapsed" data-ng-click="' + treeId + '.selectNodeHead(node, $event)"></span>' +
                            '<span style="display : inline-block" title="No Children Exists" class="glyphicon glyphicon-file text-hkg"  data-ng-hide="node.' + nodeChildren + '.length" data-ng-click="' + treeId + '.selectNodeLabel(node, $event)"></span> ' +
//                            '<span style="width:85%;display : inline-block;" data-ng-class="node.selected" data-ng-click="' + treeId + '.selectNodeLabel(node)">{{node.' + nodeLabel + '}}<span ng-if="node.categoryCount!==undefined && '+ scope.displayCount + '">({{node.categoryCount}})</span></span>' +
                            '<span class="tree-model-list"><span style="width: 90%;float: right;" class="wordRap" data-ng-class="{\'selected\':node.selected,\'text-success\':node.'+attrs.existFlag+'}" data-ng-click="' + treeId + '.selectNodeLabel(node)">{{"' + scope.entityName + '"+node.' + nodeLabel + '| translate}}<span ng-if="node.categoryCount!==undefined && ' + scope.displayCount + '">({{node.categoryCount}})</span></span></span>' +
                            '<div data-ng-hide="node.collapsed" data-tree-id="' + treeId + '" ag-treemodel="node.' + nodeChildren + '" data-node-id=' + nodeId + ' data-node-label=' + nodeLabel + ' data-node-children=' + nodeChildren + ' display-count=' + scope.displayCount + ' entity-name=' + scope.entityName + '></div>' +
                            '<span>' +
                            '</li>' +
                            '</ul>';


                    //check tree id, tree model
                    if (treeId && agTreemodel) {

                        //root node
                        if (attrs.angularTreeview) {

                            //create tree object if not exists
                            scope[treeId] = scope[treeId] || {};

                            //if node head clicks,
                            scope[treeId].selectNodeHead = scope[treeId].selectNodeHead || function (selectedNode, $event) {

                                //Collapse or Expand
                                selectedNode.collapsed = !selectedNode.collapsed;
                                $event.preventDefault();
                                $event.stopPropagation();
                            };

                            //if node label clicks,
                            scope[treeId].selectNodeLabel = scope[treeId].selectNodeLabel || function (selectedNode, $event) {

                                //remove highlight from previous node
                                if (scope[treeId].currentNode && scope[treeId].currentNode.selected) {
                                    scope[treeId].currentNode.selected = undefined;
                                }

                                //set highlight to selected node
                                selectedNode.selected = 'selected';

                                //set currentNode
                                scope[treeId].currentNode = selectedNode;
                            };
                        }

                        //Rendering template.
                        element.html('').append($compile(template)(scope));
                    }
                }
            };
        }]);
});