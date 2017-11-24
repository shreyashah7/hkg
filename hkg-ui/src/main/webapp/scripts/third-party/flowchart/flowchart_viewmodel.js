
//
// Global accessor.
//
var flowchart = {
};

// Module.
(function () {

    flowchart.width = 400;
    flowchart.height = 300;
    flowchart.boundryGap = 30;
    flowchart.boundryGapX = 15;
    flowchart.boundryGapY = 15;
    flowchart.boundryGapNodeX = 15;
    flowchart.boundryGapNodeY = 45;
    //
    // Width of a node.
    //
    flowchart.nodeWidth = 190;
    //
    // Width of a node.
    //
    flowchart.groupWidth = 150;
    //
    // Width of a node.
    //
    flowchart.groupHeight = 100;
    //
    // Amount of space reserved for displaying the node's name.
    //
    flowchart.nodeNameHeight = 40;
    //
    // Height of a connector in a node.
    //
    flowchart.connectorHeight = 35;

    flowchart.ruleLineV = 25;
    //
    // Compute the Y coordinate of a connector, given its index.
    //
    flowchart.computeConnectorY = function (connectorIndex) {
        return flowchart.nodeNameHeight + (connectorIndex * flowchart.connectorHeight);
    }

//
// Compute the position of a connector in the graph.
//
    flowchart.computeConnectorPos = function (node, connectorIndex, inputConnector) {
        return {
            x: node.x() + node.parentNode().x() + (inputConnector ? 0 : flowchart.nodeWidth),
            y: node.y() + node.parentNode().y() + flowchart.computeConnectorY(connectorIndex)
        };
    };
    //
    // View model for a connector.
    //
    flowchart.ConnectorViewModel = function (connectorDataModel, x, y, parentNode, id) {

        this.data = connectorDataModel;
        this._parentNode = parentNode;
        this._x = x;
        this._y = y;
        //
        // The name of the connector.
        //
        this.name = function () {
            return this.data.name;
        }

        //
        // X coordinate of the connector.
        //
        this.x = function () {
            return this._x;
        };
        //
        // Y coordinate of the connector.
        //
        this.y = function () {
            return this._y;
        };
        //
        // The parent node that the connector is attached to.
        //
        this.parentNode = function () {
            return this._parentNode;
        };
    };
    //
    // Create view model for a list of data models.
    //
    var createConnectorsViewModel = function (connectorDataModels, x, parentNode) {
        var viewModels = [];
        if (connectorDataModels) {
            for (var i = 0; i < connectorDataModels.length; ++i) {
                var connectorViewModel =
                        new flowchart.ConnectorViewModel(connectorDataModels[i], x, flowchart.computeConnectorY(i), parentNode);
                viewModels.push(connectorViewModel);
            }
        }

        return viewModels;
    };
    //
    // View model for a group.
    //
    flowchart.GroupViewModel = function (groupDataModel) {
        this.data = groupDataModel;
        this.nodes = createNodesViewModel(this.data.nodes, this);
        // Set to true when the node is selected.
        this._selected = false;
        // Name of the group.
        //
        this.name = function () {
            return this.data.name || "";
        };
        //
        // X coordinate of the node.
        //
        this.x = function () {
            if (this.data.x <= flowchart.boundryGapX) {
                this.data.x = flowchart.boundryGapX;
            }
            return this.data.x;
        };
        //
        // Y coordinate of the node.
        //
        this.y = function () {
            if (this.data.y <= flowchart.boundryGapY) {
                this.data.y = flowchart.boundryGapY;
            }
            return this.data.y;
        };
        //
        // Width of the node.
        //
        this.width = function () {

            var widthTemp = flowchart.groupWidth;
            for (var i = 0; i < this.nodes.length; i++) {
                var groupWidth = this.nodes[i].data.x + this.nodes[i].width() + flowchart.boundryGapNodeX;
                if (groupWidth > widthTemp) {
                    widthTemp = groupWidth;
                }
            }
            if (widthTemp < (this.data.name.length * 10)) {
                widthTemp = this.data.name.length * 10;
            } else if (widthTemp < 300) {
                widthTemp = 300;
            }
            var boundryWidth = this.data.x + widthTemp + flowchart.boundryGapX;
            var oldWidth = flowchart.width;
            if (boundryWidth > oldWidth) {
                flowchart.width = boundryWidth + 50;
                jQuery("#containerDiv").css("width", flowchart.width + "px");
            }
            return widthTemp;
        };
        //
        // Height of the node.
        //
        this.height = function () {

            var heightTemp = flowchart.groupHeight;
            for (var i = 0; i < this.nodes.length; i++) {
                var groupHeight = this.nodes[i].data.y + this.nodes[i].height() + flowchart.boundryGapY;
                if (groupHeight > heightTemp) {
                    heightTemp = groupHeight;
                }
            }
            if (heightTemp < 200) {
                heightTemp = 200;
            }
            var boundryHeight = this.data.y + heightTemp + flowchart.boundryGapY;
            var oldHeight = flowchart.height;
            if (boundryHeight > oldHeight) {
                flowchart.height = boundryHeight;
                jQuery("#containerDiv").css("height", flowchart.height + "px");
            }
            return heightTemp;
        };
        //
        // Select the node.
        //
        this.select = function () {
            this._selected = true;
        };
        //
        // Deselect the node.
        //
        this.deselect = function () {
            this._selected = false;
        };
        //
        // Toggle the selection state of the node.
        //
        this.toggleSelected = function () {
            this._selected = !this._selected;
        };
        //
        // Returns true if the node is selected.
        //
        this.selected = function () {
            return this._selected;
        };
//        //
//        // Add an input connector to the node.
//        //
        this.addNode = function (nodeDataModel) {

            if (!this.data.nodes) {
                this.data.nodes = [];
            }

            var nodeViewModel = new flowchart.NodeViewModel(nodeDataModel, this);
            this.nodes.push(nodeViewModel);
            // Add to node's view model.
            this.data.nodes.push(nodeDataModel);
        };
        
        if (this.data.active === true) {
            this._selected = true;
        }
    };
    //
    // View model for a node.
    //
    flowchart.NodeViewModel = function (nodeDataModel, parentNode) {

        this.data = nodeDataModel;
        this._parentNode = parentNode;
        this.inputConnectors = createConnectorsViewModel(this.data.inputConnectors, 0, this);
        this.outputConnectors = createConnectorsViewModel(this.data.outputConnectors, flowchart.nodeWidth, this);
        // Set to true when the node is selected.
        this._selected = false;
        this.id = function () {
            return this.data.id;
        }
        //
        // Name of the node.
        //
        this.name = function () {
            return this.data.name || "";
        };
        this.description = function () {
            return this.data.description || "";
        };
        //
        // X coordinate of the node.
        //
        this.x = function () {
            if (this.data.x <= flowchart.boundryGapNodeX) {
                this.data.x = flowchart.boundryGapNodeX;
            }
            return this.data.x;
        };
        //
        // Y coordinate of the node.
        //
        this.y = function () {
            if (this.data.y <= flowchart.boundryGapNodeY) {
                this.data.y = flowchart.boundryGapNodeY;
            }
            return this.data.y;
        };
        //
        // Width of the node.
        //
        this.width = function () {
            return flowchart.nodeWidth;
        };
        //
        // Height of the node.
        //
        this.height = function () {
            var numConnectors =
                    Math.max(
                            this.inputConnectors.length,
                            this.outputConnectors.length);
            return flowchart.computeConnectorY(numConnectors);
        };
        //
        // Select the node.
        //
        this.select = function () {
            this._selected = true;
        };
        //
        // Deselect the node.
        //
        this.deselect = function () {
            this._selected = false;
        };
        //
        // Toggle the selection state of the node.
        //
        this.toggleSelected = function () {
            this._selected = !this._selected;
        };
        //
        // Returns true if the node is selected.
        //
        this.selected = function () {
            return this._selected;
        };
        //
        // Internal function to add a connector.
        this._addConnector = function (connectorDataModel, x, connectorsDataModel, connectorsViewModel) {
            var connectorViewModel =
                    new flowchart.ConnectorViewModel(connectorDataModel, x,
                            flowchart.computeConnectorY(connectorsViewModel.length), this);
            connectorsDataModel.push(connectorDataModel);
            // Add to node's view model.
            connectorsViewModel.push(connectorViewModel);
        };
        //
        // Add an input connector to the node.
        //
        this.addInputConnector = function (connectorDataModel) {

            if (!this.data.inputConnectors) {
                this.data.inputConnectors = [];
            }
            this._addConnector(connectorDataModel, 0, this.data.inputConnectors, this.inputConnectors);
        };
        //
        // Add an ouput connector to the node.
        //
        this.addOutputConnector = function (connectorDataModel) {

            if (!this.data.outputConnectors) {
                this.data.outputConnectors = [];
            }
            this._addConnector(connectorDataModel, flowchart.nodeWidth, this.data.outputConnectors, this.outputConnectors);
        };
        //
        // The parent node that the connector is attached to.
        //
        this.parentNode = function () {
            return this._parentNode;
        };

        if (this.data.active === true) {
            this._selected = true;
        }
    };
    // 
    // Wrap the nodes data-model in a view-model.
    //
    var createNodesViewModel = function (nodesDataModel, group) {
        var nodesViewModel = [];
        if (nodesDataModel) {
            for (var i = 0; i < nodesDataModel.length; ++i) {
                nodesViewModel.push(new flowchart.NodeViewModel(nodesDataModel[i], group));
            }
        }

        return nodesViewModel;
    };
    // 
    // Wrap the groups data-model in a view-model.
    //
    var createGroupsViewModel = function (groupsDataModel) {
        var groupsViewModel = [];
        if (groupsDataModel) {
            for (var i = 0; i < groupsDataModel.length; ++i) {
                groupsViewModel.push(new flowchart.GroupViewModel(groupsDataModel[i]));
            }
        }

        return groupsViewModel;
    };
    //
    // View model for a connection.
    //
    flowchart.ConnectionViewModel = function (connectionDataModel, sourceConnector, destConnector) {

        this.data = connectionDataModel;
        this.source = sourceConnector;
        this.dest = destConnector;
        // Set to true when the connection is selected.
        this._selected = false;
        this.sourceCoordX = function () {
            return this.source.parentNode().x() + this.source.parentNode().parentNode().x() + this.source.x();
        };
        this.sourceCoordY = function () {
            return this.source.parentNode().y() + this.source.parentNode().parentNode().y() + this.source.y();
        };
        this.sourceCoord = function () {
            return {
                x: this.sourceCoordX(),
                y: this.sourceCoordY()
            };
        }

//        this.sourceTangentX = function() {
//            return flowchart.computeConnectionSourceTangentX(this.sourceCoord(), this.destCoord());
//        };
//        this.sourceTangentY = function() {
//            return flowchart.computeConnectionSourceTangentY(this.sourceCoord(), this.destCoord());
//        };
        this.destCoordX = function () {
            return this.dest.parentNode().x() + this.dest.parentNode().parentNode().x() + this.dest.x();
        };
        this.destCoordY = function () {
            return this.dest.parentNode().y() + this.dest.parentNode().parentNode().y() + this.dest.y();
        };
        this.destCoord = function () {
            return {
                x: this.destCoordX(),
                y: this.destCoordY()
            };
        }

//        this.destTangentX = function() {
//            return flowchart.computeConnectionDestTangentX(this.sourceCoord(), this.destCoord());
//        };
//        this.destTangentY = function() {
//            return flowchart.computeConnectionDestTangentY(this.sourceCoord(), this.destCoord());
//        };

        this.connectionArea = function () {
            var finalArea = "";
            if (this.sourceCoordX() < this.destCoordX()) {
                finalArea = "M " + this.sourceCoordX() + " " + this.sourceCoordY() + " L " + this.destCoordX() + " " + this.destCoordY();
            } else {
                finalArea = "M " + this.sourceCoordX() + " " + this.sourceCoordY() + " H " + (this.sourceCoordX() + flowchart.ruleLineV) + " V " + ((this.sourceCoordY() + this.destCoordY()) / 2) + " H " + (this.destCoordX() - flowchart.ruleLineV) + " V " + this.destCoordY() + " H " + this.destCoordX() + " H " + (this.destCoordX() - flowchart.ruleLineV) + " V " + ((this.sourceCoordY() + this.destCoordY()) / 2) + " H " + (this.sourceCoordX() + flowchart.ruleLineV) + " V " + this.sourceCoordY() + " Z";
            }
            return finalArea;
        };

        //
        // Select the connection.
        //
        this.select = function () {
            this._selected = true;
        };
        //
        // Deselect the connection.
        //
        this.deselect = function () {
            this._selected = false;
        };
        //
        // Toggle the selection state of the connection.
        //
        this.toggleSelected = function () {
            this._selected = !this._selected;
        };
        //
        // Returns true if the connection is selected.
        //
        this.selected = function () {
            return this._selected;
        };

        if (this.data.active === true) {
            this._selected = true;
        }
    };
    //
    // View model for the chart.
    //
    flowchart.ChartViewModel = function (chartDataModel) {

        jQuery("#containerDiv").css("width", flowchart.width + "px");
        jQuery("#containerDiv").css("height", flowchart.height + "px");
        //
        // Find a specific node within the chart.
        //
        this.findNode = function (nodeID) {

            for (var j = 0; j < this.groups.length; ++j) {
                for (var i = 0; i < this.groups[j].nodes.length; ++i) {
                    var node = this.groups[j].nodes[i];
                    if (node.data.id === nodeID) {
                        return node;
                    }
                }
            }

            throw new Error("Failed to find node " + nodeID);
        };
        //
        // Find a specific node within the chart.
        //
        this.findGroup = function (groupID) {

            for (var j = 0; j < this.groups.length; ++j) {
                var group = this.groups[j];
                if (group.data.id === groupID) {
                    return group;
                }
            }

            throw new Error("Failed to find group " + groupID);
        };
        //
        // Find a specific input connector within the chart.
        //
        this.findInputConnector = function (nodeID, connectorIndex) {

            var node = this.findNode(nodeID);
            if (!node.inputConnectors || node.inputConnectors.length <= connectorIndex) {
                throw new Error("Node " + nodeID + " has invalid input connectors.");
            }

            return node.inputConnectors[connectorIndex];
        };
        //
        // Find a specific output connector within the chart.
        //
        this.findOutputConnector = function (nodeID, connectorIndex) {

            var node = this.findNode(nodeID);
            if (!node.outputConnectors || node.outputConnectors.length <= connectorIndex) {
                throw new Error("Node " + nodeID + " has invalid output connectors.");
            }

            return node.outputConnectors[connectorIndex];
        };
        //
        // Create a view model for connection from the data model.
        //
        this._createConnectionViewModel = function (connectionDataModel) {
            var sourceConnector = this.findOutputConnector(connectionDataModel.source.nodeID, connectionDataModel.source.connectorIndex);
            var destConnector = this.findInputConnector(connectionDataModel.dest.nodeID, connectionDataModel.dest.connectorIndex);

            return new flowchart.ConnectionViewModel(connectionDataModel, sourceConnector, destConnector);
        };
        // 
        // Wrap the connections data-model in a view-model.
        //
        this._createConnectionsViewModel = function (connectionsDataModel) {

            var connectionsViewModel = [];
            if (connectionsDataModel) {
                for (var i = 0; i < connectionsDataModel.length; ++i) {
                    connectionsViewModel.push(this._createConnectionViewModel(connectionsDataModel[i]));
                }
            }

            return connectionsViewModel;
        };
        // Reference to the underlying data.
        this.data = chartDataModel;
        // Create a view-model for nodes.
        this.groups = createGroupsViewModel(this.data.groups);
        // Create a view-model for connections.
        this.connections = this._createConnectionsViewModel(this.data.connections);
        this.isreadonly = this.data.isreadonly;
        //
        // Create a view model for a new connection.
        //
        this.createNewConnection = function (filter, sourceConnector, destConnector) {

//			debug.assertObjectValid(sourceConnector);
//			debug.assertObjectValid(destConnector);

            var connectionsDataModel = this.data.connections;
            if (!connectionsDataModel) {
                connectionsDataModel = this.data.connections = [];
            }

            var connectionsViewModel = this.connections;
            if (!connectionsViewModel) {
                connectionsViewModel = this.connections = [];
            }

            var sourceNode = sourceConnector.parentNode();
            var sourceConnectorIndex = sourceNode.outputConnectors.indexOf(sourceConnector);
            if (sourceConnectorIndex == -1) {
                sourceConnectorIndex = sourceNode.inputConnectors.indexOf(sourceConnector);
                if (sourceConnectorIndex == -1) {
                    throw new Error("Failed to find source connector within either inputConnectors or outputConnectors of source node.");
                }
            }

            var destNode = destConnector.parentNode();
            var destConnectorIndex = destNode.inputConnectors.indexOf(destConnector);
            if (destConnectorIndex == -1) {
                destConnectorIndex = destNode.outputConnectors.indexOf(destConnector);
                if (destConnectorIndex == -1) {
                    throw new Error("Failed to find dest connector within inputConnectors or ouputConnectors of dest node.");
                }
            }
            //added by dhwani for not adding same connection again
            var conn = undefined;
            if (connectionsViewModel !== null) {
                conn = filter('filter')(connectionsViewModel, function (connection) {
                    return (connection.data.source.nodeID === sourceNode.data.id && connection.data.dest.nodeID === destNode.data.id);
                })[0];
            }
            if (conn === undefined) {
//                $('#rulesPopUp').modal('show');
                var connectionDataModel = {
                    source: {
                        nodeID: sourceNode.data.id,
                        connectorIndex: sourceConnectorIndex
                    },
                    dest: {
                        nodeID: destNode.data.id,
                        connectorIndex: destConnectorIndex
                    }
                };
                connectionsDataModel.push(connectionDataModel);
                var connectionViewModel = new flowchart.ConnectionViewModel(connectionDataModel, sourceConnector, destConnector);
                connectionsViewModel.push(connectionViewModel);
            }
        };

        // Add a group to the view model.        
        this.addGroup = function (groupDataModel) {
            if (!this.data.groups) {
                this.data.groups = [];
            }
            // 
            // Update the data model.
            //
            this.data.groups.push(groupDataModel);
            // 
            // Update the view model.
            //
            this.groups.push(new flowchart.GroupViewModel(groupDataModel));
        };

        // Add a connection to the view model.        
        this.addConnection = function (connectionDataModel, sourceConnector, destinationConnector) {
            if (!this.data.connections) {
                this.data.connections = [];
            }
            // 
            // Update the data model.
            //
            this.data.connections.push(connectionDataModel);
            // 
            // Update the view model.
            //
            this.connections.push(new flowchart.ConnectionViewModel()(connectionDataModel, sourceConnector, destinationConnector));
        };
        //
        // Select all nodes and connections in the chart.
        //
        this.selectAll = function () {

            var groups = this.groups;
            for (var i = 0; i < groups.length; ++i) {
                var group = groups[i];
                group.select();
                for (var j = 0; j < group.nodes.length; ++j) {
                    var node = group.nodes[j];
                    node.select();
                }
            }

            var connections = this.connections;
            for (var i = 0; i < connections.length; ++i) {
                var connection = connections[i];
                connection.select();
            }
        };
        //
        // Deselect all nodes and connections in the chart.
        //
        this.deselectAll = function () {

            var groups = this.groups;
            for (var i = 0; i < groups.length; ++i) {
                var group = groups[i];
                group.deselect();
                for (var j = 0; j < group.nodes.length; ++j) {
                    var node = group.nodes[j];
                    node.deselect();
                }
            }

            var connections = this.connections;
            for (var i = 0; i < connections.length; ++i) {
                var connection = connections[i];
                connection.deselect();
            }
        };
        //
        // Deselect all nodes of group and connections in the chart.
        //
        this.deselectAllNodesOfGroup = function (group) {

            group.deselect();
            for (var j = 0; j < group.nodes.length; ++j) {
                var node = group.nodes[j];
                node.deselect();
            }

            var connections = this.connections;
            for (var i = 0; i < connections.length; ++i) {
                var connection = connections[i];
                connection.deselect();
            }
        };
        //
        // Update the location of the node and its connectors.
        //
        this.updateSelectedNodesLocation = function (deltaX, deltaY, group) {
            var selectedNodes = this.getSelectedNodes(group);
            for (var i = 0; i < selectedNodes.length; ++i) {
                var node = selectedNodes[i];
                node.data.x += deltaX;
                node.data.y += deltaY;
            }
        };
        //
        // Update the location of the node and its connectors.
        //
        this.updateSelectedGroupsLocation = function (deltaX, deltaY) {
            var selectedGroups = this.getSelectedGroups();
            for (var i = 0; i < selectedGroups.length; ++i) {
                var group = selectedGroups[i];
                group.data.x += deltaX;
                group.data.y += deltaY;
            }
        };
        //
        // Handle mouse click on a particular node.
        //
        this.handleNodeClicked = function (group, node, ctrlKey) {

            if (ctrlKey) {
                node.toggleSelected();
            }
            else {
                this.deselectAllNodesOfGroup(group);
                node.select();
            }

            // Move node to the end of the list so it is rendered after all the other.
            // This is the way Z-order is done in SVG.

            var nodeIndex = group.nodes.indexOf(node);
            if (nodeIndex === -1) {
                throw new Error("Failed to find node in view model!");
            }
            group.nodes.splice(nodeIndex, 1);
            group.nodes.push(node);
        };
        //
        // Handle mouse click on a particular node.
        //
        this.handleGroupClicked = function (group, ctrlKey) {

            if (ctrlKey) {
                group.toggleSelected();
            }
            else {
                this.deselectAll();
                group.select();
            }

            // Move node to the end of the list so it is rendered after all the other.
            // This is the way Z-order is done in SVG.

            var groupIndex = this.groups.indexOf(group);
            if (groupIndex === -1) {
                throw new Error("Failed to find group in view model!");
            }
            this.groups.splice(groupIndex, 1);
            this.groups.push(group);
        };
        //
        // Handle mouse down on a connection.
        //
        this.handleConnectionMouseDown = function (connection, ctrlKey) {

            if (ctrlKey) {
                connection.toggleSelected();
            }
            else {
                this.deselectAll();
                connection.select();
            }
        };
        //
        // Delete all nodes and connections that are selected.
        //
        this.deleteSelected = function () {
            var newGroupViewModels = [];
            var newGroupDataModels = [];
            var deletedNodeIds = [];
            var selectedIds = {groupIds: [], nodeIds: [], connectorIds: [], rulesetIds: []};
            //
            // Sort nodes into:
            //		nodes to keep and 
            //		nodes to delete.
            //

            for (var groupIndex = 0; groupIndex < this.groups.length; ++groupIndex) {
                var newNodeViewModels = [];
                var newNodeDataModels = [];
                var group = this.groups[groupIndex];
                if (!group.selected()) {
                    // Only retain non-selected nodes.                    
                    for (var nodeIndex = 0; nodeIndex < group.nodes.length; ++nodeIndex) {

                        var node = group.nodes[nodeIndex];
                        if (!node.selected()) {
                            // Only retain non-selected nodes.
                            newNodeViewModels.push(node);
                            newNodeDataModels.push(node.data);
                        }
                        else {
                            // Keep track of nodes that were deleted, so their connections can also
                            // be deleted.
                            selectedIds.nodeIds.push(node.data.id);
                            deletedNodeIds.push(node.data.id);
                        }
                    }
                    group.data.nodes = newNodeDataModels;
                    group.nodes = newNodeViewModels;
                    newGroupViewModels.push(group);
                    newGroupDataModels.push(group.data);
                }
                else {
                    // Keep track of nodes that were deleted, so their connections can also
                    // be deleted.
                    selectedIds.groupIds.push(group.data.id);
                    for (var nodeIndex = 0; nodeIndex < group.nodes.length; ++nodeIndex) {
                        var node = group.nodes[nodeIndex];
                        deletedNodeIds.push(node.data.id);
                    }
                }

            }

            var newConnectionViewModels = [];
            var newConnectionDataModels = [];
            //
            // Remove connections that are selected.
            // Also remove connections for nodes that have been deleted.
            //
            for (var connectionIndex = 0; connectionIndex < this.connections.length; ++connectionIndex) {

                var connection = this.connections[connectionIndex];
                if (!connection.selected() &&
                        deletedNodeIds.indexOf(connection.data.source.nodeID) === -1 &&
                        deletedNodeIds.indexOf(connection.data.dest.nodeID) === -1)
                {
                    //
                    // The nodes this connection is attached to, where not deleted,
                    // so keep the connection.
                    //
                    newConnectionViewModels.push(connection);
                    newConnectionDataModels.push(connection.data);
                } else {
                    selectedIds.connectorIds.push(connection.data.id);
                    selectedIds.rulesetIds.push(connection.data.ruleset);
                }
            }

            //
            // Update nodes and connections.
            //
            this.groups = newGroupViewModels;
            this.data.groups = newGroupDataModels;
            this.connections = newConnectionViewModels;
            this.data.connections = newConnectionDataModels;

            return selectedIds;
        };

        this.cancelled = function (source, destination) {
            var newConnectionViewModels = [];
            var newConnectionDataModels = [];
            //
            // Remove connections that are selected.
            // Also remove connections for nodes that have been deleted.connection.data.source.nodeID
            //
            for (var connectionIndex = 0; connectionIndex < this.connections.length; ++connectionIndex) {

                var connection = this.connections[connectionIndex];
                if (!(connection.data.source.nodeID === source && connection.data.dest.nodeID === destination)) {
                    newConnectionViewModels.push(connection);
                    newConnectionDataModels.push(connection.data);
                }
            }
            //
            // Update nodes and connections.
            //           
            this.connections = newConnectionViewModels;
            this.data.connections = newConnectionDataModels;
        };

        this.addRouteId = function (source, destination, routeId, ruleset) {
            var newConnectionViewModels = [];
            var newConnectionDataModels = [];
            //
            // Remove connections that are selected.
            // Also remove connections for nodes that have been deleted.connection.data.source.nodeID
            //
            for (var connectionIndex = 0; connectionIndex < this.connections.length; ++connectionIndex) {
                var connection = this.connections[connectionIndex];
                if (connection.data.source.nodeID === source && connection.data.dest.nodeID === destination) {
                    connection.data.id = routeId;
                    connection.id = routeId;
                    connection.data.ruleset = ruleset;
                    connection.ruleset = ruleset;
                }
                newConnectionViewModels.push(connection);
                newConnectionDataModels.push(connection.data);
            }
            //
            // Update nodes and connections.
            //           
            this.connections = newConnectionViewModels;
            this.data.connections = newConnectionDataModels;
        };
        //
        // Select nodes and connections that fall within the selection rect.
        //
        this.applySelectionRect = function (selectionRect) {

            this.deselectAll();
            for (var i = 0; i < this.groups.length; ++i) {
                var group = this.groups[i];
                if (group.x() >= selectionRect.x &&
                        group.y() >= selectionRect.y &&
                        group.x() + group.width() <= selectionRect.x + selectionRect.width &&
                        group.y() + group.height() <= selectionRect.y + selectionRect.height)
                {
                    // Select nodes that are within the selection rect.
                    group.select();
                }
                for (var j = 0; j < group.nodes.length; ++j) {
                    var node = group.nodes[j];
                    if (node.x() >= selectionRect.x &&
                            node.y() >= selectionRect.y &&
                            node.x() + node.width() <= selectionRect.x + selectionRect.width &&
                            node.y() + node.height() <= selectionRect.y + selectionRect.height)
                    {
                        // Select nodes that are within the selection rect.
                        node.select();
                    }
                }
            }

            for (var i = 0; i < this.connections.length; ++i) {
                var connection = this.connections[i];
                if (connection.source.parentNode().selected() &&
                        connection.dest.parentNode().selected())
                {
                    // Select the connection if both its parent nodes are selected.
                    connection.select();
                }
            }

        };
        //
        // Get the array of nodes that are currently selected.
        //
        this.getSelectedNodes = function (group) {
            var selectedNodes = [];
            for (var j = 0; j < this.groups.length; ++j) {
                var groupObj = this.groups[j];
                if (groupObj === group) {
                    for (var i = 0; i < groupObj.nodes.length; ++i) {
                        var node = groupObj.nodes[i];
                        if (node.selected()) {
                            selectedNodes.push(node);
                        }
                    }
                }
            }

            return selectedNodes;
        };
        //
        // Get the array of nodes that are currently selected.
        //
        this.getSelectedGroups = function () {
            var selectedGroups = [];
            for (var i = 0; i < this.groups.length; ++i) {
                var group = this.groups[i];
                if (group.selected()) {
                    selectedGroups.push(group);
                }
            }

            return selectedGroups;
        };
        //
        // Get the array of connections that are currently selected.
        //
        this.getSelectedConnections = function () {
            var selectedConnections = [];
            for (var i = 0; i < this.connections.length; ++i) {
                var connection = this.connections[i];
                if (connection.selected()) {
                    selectedConnections.push(connection);
                }
            }

            return selectedConnections;
        };
    };
})();