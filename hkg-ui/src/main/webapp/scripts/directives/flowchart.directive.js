
angular.module('mouseCapture', [])

//
// Service used to acquire 'mouse capture' then receive dragging events while the mouse is captured.
//
        .factory('mouseCapture', ["$rootScope", function($rootScope) {

            //
            // Element that the mouse capture applies to, defaults to 'document' 
            // unless the 'mouse-capture' directive is used.
            //
            var $element = document;

            //
            // Set when mouse capture is acquired to an object that contains 
            // handlers for 'mousemove' and 'mouseup' events.
            //
            var mouseCaptureConfig = null;

            //
            // Handler for mousemove events while the mouse is 'captured'.
            //
            var mouseMove = function (evt) {

                if (mouseCaptureConfig && mouseCaptureConfig.mouseMove) {

                    mouseCaptureConfig.mouseMove(evt);

                    $rootScope.$digest();
                }
            };

            //
            // Handler for mouseup event while the mouse is 'captured'.
            //
            var mouseUp = function (evt) {

                if (mouseCaptureConfig && mouseCaptureConfig.mouseUp) {

                    mouseCaptureConfig.mouseUp(evt);

                    $rootScope.$digest();
                }
            };

            return {
                // 
                // Register an element to use as the mouse capture element instead of 
                // the default which is the document.
                //
                registerElement: function (element) {

                    $element = element;
                },
                //
                // Acquire the 'mouse capture'.
                // After acquiring the mouse capture mousemove and mouseup events will be 
                // forwarded to callbacks in 'config'.
                //
                acquire: function (evt, config) {

                    //
                    // Release any prior mouse capture.
                    //
                    this.release();

                    mouseCaptureConfig = config;

                    // 
                    // In response to the mousedown event register handlers for mousemove and mouseup 
                    // during 'mouse capture'.
                    //
                    $element.mousemove(mouseMove);
                    $element.mouseup(mouseUp);
                },
                //
                // Release the 'mouse capture'.
                //
                release: function () {

                    if (mouseCaptureConfig) {

                        if (mouseCaptureConfig.released) {
                            //
                            // Let the client know that their 'mouse capture' has been released.
                            //
                            mouseCaptureConfig.released();
                        }

                        mouseCaptureConfig = null;
                    }

                    $element.unbind("mousemove", mouseMove);
                    $element.unbind("mouseup", mouseUp);
                }
            };
        }])

//
// Directive that marks the mouse capture element.
//
        .directive('mouseCapture', function () {
            return {
                restrict: 'A',
                controller: ["$scope","$element","$attrs", "mouseCapture",function ($scope, $element, $attrs, mouseCapture) {
                    // 
                    // Register the directives element as the mouse capture element.
                    //
                    mouseCapture.registerElement($element);

                }]
            };
        });

angular.module('dragging', ['mouseCapture'])

//
// Service used to help with dragging and clicking on elements.
//
        .factory('dragging', ["$rootScope","mouseCapture",function ($rootScope, mouseCapture) {

            //
            // Threshold for dragging.
            // When the mouse moves by at least this amount dragging starts.
            //
            var threshold = 5;

            return {
                //
                // Called by users of the service to register a mousedown event and start dragging.
                // Acquires the 'mouse capture' until the mouseup event.
                //
                startDrag: function (evt, config) {

                    var dragging = false;
                    var x = evt.pageX;
                    var y = evt.pageY;

                    //
                    // Handler for mousemove events while the mouse is 'captured'.
                    //
                    var mouseMove = function (evt) {

                        if (!dragging) {
//                            if (evt.pageX - x > threshold ||
//                                    evt.pageY - y > threshold)
//                            {
                            dragging = true;

                            if (config.dragStarted) {
                                config.dragStarted(x, y, evt);
                            }

                            if (config.dragging) {
                                // First 'dragging' call to take into account that we have 
                                // already moved the mouse by a 'threshold' amount.
                                config.dragging(evt.pageX, evt.pageY, evt);
                            }
//                            }
                        }
                        else {
                            if (config.dragging) {
                                config.dragging(evt.pageX, evt.pageY, evt);
                            }

                            x = evt.pageX;
                            y = evt.pageY;
                        }
};

                    //
                    // Handler for when mouse capture is released.
                    //
                    var released = function () {

                        if (dragging) {
                            if (config.dragEnded) {
                                config.dragEnded();
                            }
                        }
                        else {
                            if (config.clicked) {
                                config.clicked();
                            }
                        }
                    };

//
                    // Handler for mouseup event while the mouse is 'captured'.
                    // Mouseup releases the mouse capture.
//
                    var mouseUp = function (evt) {

                        mouseCapture.release();

                        evt.stopPropagation();
                        evt.preventDefault();
                    };

//
                    // Acquire the mouse capture and start handling mouse events.
//
                    mouseCapture.acquire(evt, {
                        mouseMove: mouseMove,
                        mouseUp: mouseUp,
                        released: released
                    });

                    evt.stopPropagation();
                    evt.preventDefault();
                }
            };

        }])

        ;


//
//
//
//
////
// Flowchart module.
//
angular.module('flowChart', ['dragging'])

//
// Directive that generates the rendered chart from the data model.
//
        .directive('flowChart', function () {
            return {
                restrict: 'E',
                templateUrl: "scripts/directives/flowchart/flowchart_template.html",
                replace: true,
                scope: {
                    chart: "=chart"
                },
                //
                // Controller for the flowchart directive.
                // Having a separate controller is better for unit testing, otherwise
                // it is painful to unit test a directive without instantiating the DOM 
                // (which is possible, just not ideal).
                //
                controller: 'FlowChartController'
            };
})

//
// Directive that allows the chart to be edited as json in a textarea.
//
        .directive('chartJsonEdit', function () {
            return {
                restrict: 'A',
                scope: {
                    viewModel: "="
                },
                link: function (scope, elem, attr) {

                    //
                    // Serialize the data model as json and update the textarea.
                    //
                    var updateJson = function () {
                        if (scope.viewModel) {
                            var json = JSON.stringify(scope.viewModel.data, null, 4);
                            $(elem).val(json);
                        }
};

                    //
                    // First up, set the initial value of the textarea.
                    //
                    updateJson();

                    //
                    // Watch for changes in the data model and update the textarea whenever necessary.
                    //
                    scope.$watch("viewModel.data", updateJson, true);

                    //
                    // Handle the change event from the textarea and update the data model
                    // from the modified json.
                    //
                    $(elem).bind("input propertychange", function () {
                        var json = $(elem).val();
                        var dataModel = JSON.parse(json);
                        scope.viewModel = new flowchart.ChartViewModel(dataModel);

                        scope.$digest();
                    });
                }
};

        })

//
// Controller for the flowchart directive.
// Having a separate controller is better for unit testing, otherwise
// it is painful to unit test a directive without instantiating the DOM 
// (which is possible, just not ideal).
//
        .controller('FlowChartController', ['$rootScope', '$scope', 'dragging', '$element', '$filter', 'ActivityFlowService', function FlowChartController($rootScope, $scope, dragging, $element, filter, ActivityFlowService) {

                var controller = this;
                //
                // Reference to the document and jQuery, can be overridden for testting.
                //
                this.document = document;

                //
                // Wrap jQuery so it can easily be  mocked for testing.
                //
                this.jQuery = function (element) {
                    return $(element);
};

                //
                // Init data-model variables.
                //
                $scope.draggingConnection = false;
                $scope.connectorSize = 15;
                $scope.dragSelecting = false;
                /* Can use this to test the drag selection rect.
                 $scope.dragSelectionRect = {
                 x: 0,
                 y: 0,
                 width: 0,
                 height: 0,
};
                 */

                //
                // Reference to the connection, connector or node that the mouse is currently over.
                //
                $scope.mouseOverConnector = null;
                $scope.mouseOverConnection = null;
                $scope.mouseOverNode = null;
                $scope.mouseOverGroup = null;

                //
                // The class for connections and connectors.
                //
                this.connectionClass = 'connection';
                this.connectorClass = 'connector';
                this.nodeClass = 'node';
                this.groupClass = 'group';

                //
                // Search up the HTML element tree for an element the requested class.
                //
                this.searchUp = function (element, parentClass) {

                    //
                    // Reached the root.
                    //
                    if (element === null || element.length === 0) {
                        return null;
                    }

                    // 
                    // Check if the element has the class that identifies it as a connector.
                    //
                    if (hasClassSVG(element, parentClass)) {
                        //
                        // Found the connector element.
                        //
                        return element;
                    }

                    //
                    // Recursively search parent elements.
                    //
                    return this.searchUp(element.parent(), parentClass);
};

                //
                // Hit test and retreive node and connector that was hit at the specified coordinates.
                //
                this.hitTest = function (clientX, clientY) {
                    //
                    // Retreive the element the mouse is currently over.
                    //
                    return this.document.elementFromPoint(clientX, clientY);
};

                //
                // Hit test and retreive node and connector that was hit at the specified coordinates.
                //
                this.checkForHit = function (mouseOverElement, whichClass) {

                    //
                    // Find the parent element, if any, that is a connector.
                    //
                    var hoverElement = this.searchUp(this.jQuery(mouseOverElement), whichClass);
                    if (!hoverElement) {
                        return null;
                    }

                    return hoverElement.scope();
};

                //
                // Translate the coordinates so they are relative to the svg element.
                //
                this.translateCoordinates = function (x, y) {
                    var svg_elem = $element.get(0);
                    var matrix = svg_elem.getScreenCTM();
                    var point = svg_elem.createSVGPoint();
                    point.x = x;
                    point.y = y - jQuery(document).scrollTop();
                    return point.matrixTransform(matrix.inverse());
};

                //
                // Called on mouse down in the chart.
                //
                $scope.mouseDown = function (evt, readOnly) {

                    if ($scope.chart !== null && !readOnly) {
                        $scope.chart.deselectAll();

                        dragging.startDrag(evt, {
                            //
                            // Commence dragging... setup variables to display the drag selection rect.
                            //
                            dragStarted: function (x, y) {
                                $scope.dragSelecting = true;
                                var startPoint = controller.translateCoordinates(x, y);
                                $scope.dragSelectionStartPoint = startPoint;
                                $scope.dragSelectionRect = {
                                    x: startPoint.x,
                                    y: startPoint.y,
                                    width: 0,
                                    height: 0
};
                            },
                            //
                            // Update the drag selection rect while dragging continues.
                            //
                            dragging: function (x, y) {
                                var startPoint = $scope.dragSelectionStartPoint;
                                var curPoint = controller.translateCoordinates(x, y);

                                $scope.dragSelectionRect = {
                                    x: curPoint.x > startPoint.x ? startPoint.x : curPoint.x,
                                    y: curPoint.y > startPoint.y ? startPoint.y : curPoint.y,
                                    width: curPoint.x > startPoint.x ? curPoint.x - startPoint.x : startPoint.x - curPoint.x,
                                    height: curPoint.y > startPoint.y ? curPoint.y - startPoint.y : startPoint.y - curPoint.y
};
                            },
                            //
                            // Dragging has ended... select all that are within the drag selection rect.
                            //
                            dragEnded: function () {
                                $scope.dragSelecting = false;
                                $scope.chart.applySelectionRect($scope.dragSelectionRect);
                                delete $scope.dragSelectionStartPoint;
                                delete $scope.dragSelectionRect;
                            }
                        });
                    }
};

                //
                // Called for each mouse move on the svg element.
                //
                $scope.mouseMove = function (evt, readOnly) {

                    //
                    // Clear out all cached mouse over elements.
                    //
                    if (!readOnly) {
                        $scope.mouseOverConnection = null;
                        $scope.mouseOverConnector = null;
                        $scope.mouseOverNode = null;
                        $scope.mouseOverGroup = null;

                        var mouseOverElement = controller.hitTest(evt.clientX, evt.clientY);
                        if (mouseOverElement === null) {
                            // Mouse isn't over anything, just clear all.
                            return;
                        }

                        if (!$scope.draggingConnection) { // Only allow 'connection mouse over' when not dragging out a connection.

                            // Figure out if the mouse is over a connection.
                            var scope = controller.checkForHit(mouseOverElement, controller.connectionClass);
                            $scope.mouseOverConnection = (scope && scope.connection) ? scope.connection : null;
                            if ($scope.mouseOverConnection) {
                                // Don't attempt to mouse over anything else.
                                return;
                            }
                        }

                        // Figure out if the mouse is over a connector.
                        var scope = controller.checkForHit(mouseOverElement, controller.connectorClass);
                        $scope.mouseOverConnector = (scope && scope.connector) ? scope.connector : null;
//                    if ($scope.mouseOverConnector) {
//                        // Don't attempt to mouse over anything else.                        
//                        return;
//                    }

                        // Figure out if the mouse is over a node.
                        var scope = controller.checkForHit(mouseOverElement, controller.nodeClass);
                        $scope.mouseOverNode = (scope && scope.node) ? scope.node : null;

                        // Figure out if the mouse is over a group.
                        var scope = controller.checkForHit(mouseOverElement, controller.groupClass);
                        $scope.mouseOverGroup = (scope && scope.group) ? scope.group : null;

                        if ($scope.mouseOverGroup !== null) {
                            $rootScope.mouseOverGroup = $scope.mouseOverGroup;
                        } else if ($scope.mouseOverNode !== null) {
                            $rootScope.mouseOverGroup = $scope.mouseOverNode.parentNode();
                        } else if ($scope.mouseOverConnector !== null) {
                            $rootScope.mouseOverGroup = $scope.mouseOverConnector.parentNode().parentNode();
                        } else {
                            $rootScope.mouseOverGroup = null;
                        }
                    }
                };

                $scope.editGroupConfiguration = function (group) {
                    $rootScope.flowGroupName = group.data.name;
                    $rootScope.groupId = group.data.id;
                    $('#activityEditPopUp').modal('show');
                };

                $rootScope.sample = function () {
//                    if ($scope.chart !== null && $scope.chart !== undefined) {
//                        $scope.chart.deselectAll();
//                    }
                };

                $scope.editNodeConfiguration = function (node) {
                    $rootScope.id = angular.copy(node.data.id);
                    $rootScope.serviceName = angular.copy(node.data.name);
                    $rootScope.serviceCode = angular.copy(node.data.serviceCode);
                    $rootScope.designationId = angular.copy(node.data.designation);
                    $rootScope.desgName = angular.copy(node.data.desigName);
                    $rootScope.modifierCode = angular.copy(node.data.modifier);
                    $rootScope.nonModifierCodes = angular.copy(node.data.nonModifierCodes);
                    $rootScope.plans = angular.copy(node.data.plans);
                    $rootScope.noOfPlans = angular.copy(node.data.noOfPlans);
                    $('#serviceEditPopUp').modal('show');
                };

                $scope.editRuleConfiguration = function (connection) {
                    $rootScope.ruleSetId = connection.data.ruleset;
                    $('#rulesPopUp').modal('show');
                };

                //
                // Handle mousedown on a node.
                //
                $scope.nodeMouseDown = function (evt, node, group) {

                    var chart = $scope.chart;
                    var lastMouseCoords;
                    dragging.startDrag(evt, {
                        //
                        // Node dragging has commenced.
                        //
                        dragStarted: function (x, y) {
                            lastMouseCoords = controller.translateCoordinates(x, y);
                            //
                            // If nothing is selected when dragging starts, 
                            // at least select the node we are dragging.
                            //
                            if (!node.selected()) {
                                chart.deselectAll();
                                node.select();
                            }
                        },
                        //
                        // Dragging selected nodes... update their x,y coordinates.
                        //
                        dragging: function (x, y) {

                            var curCoords = controller.translateCoordinates(x, y);
                            var deltaX = curCoords.x - lastMouseCoords.x;
                            var deltaY = curCoords.y - lastMouseCoords.y;

                            if (curCoords.x < 0 || (curCoords.y - 5) < 0) {
                                chart.deselectAll();
                            }

                            chart.updateSelectedNodesLocation(deltaX, deltaY, group);

                            lastMouseCoords = curCoords;
                        },
                        dragEnded: function () {

//                            node.data.x=lastMouseCoords.x;
//                            node.data.y=lastMouseCoords.y;
                            var nodeMap = new Object();
                            nodeMap['nodeId'] = node.data.id;
                            nodeMap['x'] = node.data.x;
                            nodeMap['y'] = node.data.y;
                            ActivityFlowService.updateCoordinateOfNode(nodeMap);
                        },
                        //
                        // The node wasn't dragged... it was clicked.
                        //
                        clicked: function () {
                            chart.handleNodeClicked(group, node, evt.ctrlKey);
                        }
                    });
                };

                //
                // Handle mousedown on a node.
                //
                $scope.groupMouseDown = function (evt, group) {

                    var chart = $scope.chart;
                    var lastMouseCoords;
                    dragging.startDrag(evt, {
                        //
                        // Node dragging has commenced.
                        //
                        dragStarted: function (x, y) {
                            lastMouseCoords = controller.translateCoordinates(x, y);
                            //
                            // If nothing is selected when dragging starts, 
                            // at least select the node we are dragging.
                            //
                            if (!group.selected()) {
                                chart.deselectAll();
                                group.select();
                            }
                        },
                        //
                        // Dragging selected nodes... update their x,y coordinates.
                        //
                        dragging: function (x, y) {

                            var curCoords = controller.translateCoordinates(x, y);
                            var deltaX = curCoords.x - lastMouseCoords.x;
                            var deltaY = curCoords.y - lastMouseCoords.y;
                            var frameWidth = jQuery("#activityFlowFrame").width();

                            if (curCoords.x < 0 || (curCoords.y - 5) < 0 || (frameWidth + 5) < curCoords.x) {
                                chart.deselectAll();
                            }

                            chart.updateSelectedGroupsLocation(deltaX, deltaY);

                            lastMouseCoords = curCoords;
                        },
                        dragEnded: function () {
//                            node.data.x=lastMouseCoords.x;
//                            node.data.y=lastMouseCoords.y;
                            var groupMap = new Object();
                            groupMap['groupId'] = group.data.id;
                            groupMap['x'] = group.data.x;
                            groupMap['y'] = group.data.y;
                            ActivityFlowService.updateCoordinateOfGroup(groupMap);
                        },
                        //
                        // The node wasn't dragged... it was clicked.
                        //
                        clicked: function () {
                            chart.handleGroupClicked(group, evt.ctrlKey);
                        }
                    });
                };

                //
                // Handle mousedown on a connection.
                //
                $scope.connectionMouseDown = function (evt, connection) {
                    var chart = $scope.chart;
                    chart.handleConnectionMouseDown(connection, evt.ctrlKey);

                    // Don't let the chart handle the mouse down.
                    evt.stopPropagation();
                    evt.preventDefault();
                };

                //
                // Handle mousedown on an input connector.
                //
                $scope.connectorMouseDown = function (evt, node, connector, connectorIndex, isInputConnector) {
                    //
                    // Initiate dragging out of a connection.
                    //
                    if (!isInputConnector) {
                        dragging.startDrag(evt, {
                            //
                            // Called when the mouse has moved greater than the threshold distance
                            // and dragging has commenced.
                            //
                            dragStarted: function (x, y) {
                                var curCoords = controller.translateCoordinates(x, y);
                                $scope.draggingConnection = true;
                                $scope.dragPoint1 = flowchart.computeConnectorPos(node, connectorIndex, isInputConnector);
                                $scope.dragPoint2 = {
                                    x: curCoords.x,
                                    y: curCoords.y
                                };
                                //$scope.dragTangent1 = flowchart.computeConnectionSourceTangent($scope.dragPoint1, $scope.dragPoint2);
                                //$scope.dragTangent2 = flowchart.computeConnectionDestTangent($scope.dragPoint1, $scope.dragPoint2);
                            },
                            //
                            // Called on mousemove while dragging out a connection.
                            //
                            dragging: function (x, y, evt) {
                                var startCoords = controller.translateCoordinates(x, y);
                                $scope.dragPoint1 = flowchart.computeConnectorPos(node, connectorIndex, isInputConnector);
                                $scope.dragPoint2 = {
                                    x: startCoords.x,
                                    y: startCoords.y
                                };
                                //$scope.dragTangent1 = flowchart.computeConnectionSourceTangent($scope.dragPoint1, $scope.dragPoint2);
                                //$scope.dragTangent2 = flowchart.computeConnectionDestTangent($scope.dragPoint1, $scope.dragPoint2);
                            },
                            //
                            // Clean up when dragging has finished.
                            //
                            dragEnded: function () {
                                if ($scope.mouseOverConnector &&
                                        $scope.mouseOverConnector !== connector) {

                                    //
                                    // Dragging has ended...
                                    // The mouse is over a valid connector...
                                    // Create a new connection.
                                    //
                                    $rootScope.sourceNodeId = connector.parentNode().id();
                                    $rootScope.destinationNodeId = $scope.mouseOverConnector.parentNode().id();

                                    if ($rootScope.sourceNodeId !== $rootScope.destinationNodeId && $scope.mouseOverConnector.x() === 0) {                                      
                                        var obj = {
                                            curentNode: $rootScope.sourceNodeId,
                                            nextNode: $rootScope.destinationNodeId,
                                            activityVersion: JSON.parse(localStorage.getItem("activityFlowVersion")).value,                                            
                                            ruleSet: {rules: [], franchise: JSON.parse(localStorage.getItem("activityFlowFranchise")).otherId}
                                        };
                                        $scope.chart.createNewConnection(filter, connector, $scope.mouseOverConnector);
                                        ActivityFlowService.saveActivityNodeRoute(obj, function (res) {
                                            if (res.data !== null) {
                                                $scope.chart.addRouteId($rootScope.sourceNodeId, $rootScope.destinationNodeId, res.data["routeId"], res.data["ruleset"]);
                                            } else {
                                                $rootScope.addMessage(res.messages[0].message, $rootScope.failure);
                                            }
                                        });
                                    }
                                }

                                $scope.draggingConnection = false;
                                delete $scope.dragPoint1;
                                // delete $scope.dragTangent1;
                                delete $scope.dragPoint2;
                                // delete $scope.dragTangent2;
                            }
                        });
                    }
                };
            }])
        ;
