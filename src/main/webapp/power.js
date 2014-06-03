(function () {
    var myapp = angular.module('myapp');
   
function queryCtrl3($q, $scope, $http, App, Parse, $timeout, $location, $log) {

    $scope.queries = [];

    $scope.operators = [
        { name: "$e", type: "equality", description: "=" },
        { name: "$gt", type: "equality", description: ">" },
        { name: "$gte", type: "equality", description: ">=" },
        { name: "$lt", type: "equality", description: "<" },
        { name: "$lte", type: "equality", description: "<=" },
        { name: "$ne", type: "equality", description: "not equal" },
        { name: "$in", type: "set", description: "in list" },
        { name: "$nin", type: "set", description: "not in list" },
        { name: "$all", type: "set", description: "all in list" },
        { name: "$regex", type: "regex", description: "regex" },
        { name: "$exists", type: "exists", description: "exists" },
        { name: "$select", type: "query", description: "select" },
        { name: "$dontSelect", type: "query", description: "not select" },
        { name: "$and", type: "group", description: "and" },
        { name: "$or", type: "group", description: "or" },
        { name: "EXACT", type: "equality", description: "EXACTLY MATCH" },
        { name: "CONTAIN", type: "equality", description: "CONTAIN" },
        { name: "BEGIN", type: "equality", description: "BEGIN WITH" },
        { name: "END", type: "equality", description: "END WITH" },
        { name: "LIST", type: "equality", description: "APPEAR IN LIST" },
        { name: "EQUAL", type: "equality", description: "EQUAL TO" },
        { name: "GREATER", type: "equality", description: "GREATER THAN" },
        { name: "LESS", type: "equality", description: "LESS THAN" }
    ];
    
    $scope.psBooleanMatches = [{metricsLabel : 'IS', name : 'DOES' ,value : true}, {metricsLabel : 'IS NOT',name : 'DOES NOT' ,value : false}];
    
    $scope.psOperators = [
                        { name: "EXACT", type: "equality", description: "EXACTLY MATCH" },
                        { name: "CONTAIN", type: "equality", description: "CONTAIN" },
                        { name: "BEGIN", type: "equality", description: "BEGIN WITH" },
                        { name: "END", type: "equality", description: "END WITH" },
                        { name: "LIST", type: "equality", description: "APPEAR IN LIST" }
                    ];
    
    $scope.metricOperators = [
                          { name: "EQUAL", type: "equality", description: "EQUAL TO" },
                          { name: "GREATER", type: "equality", description: "GREATER THAN" },
                          { name: "LESS", type: "equality", description: "LESS THAN" }
                      ];
    
    
    var operatorsAllowed = {
    	attribute : ["EXACT", "CONTAIN", "BEGIN", "END", "LIST"],
        string:["$e", "$ne", "$exists", "$gt", "$gte", "$lt", "$lte", "$in", "$nin", "$regex"],
        number:["$e", "$ne", "$exists", "$gt", "$gte", "$lt", "$lte", "$in", "$nin"],
        date:["$e", "$ne", "$exists", "$gt", "$gte", "$lt", "$lte", "$in", "$nin"],
        pointer:["$e", "$ne", "$exists", "$in", "$nin"],
        array:["$e", "$ne", "$exists", "$all"],
        boolean:["$e", "$ne", "$exists"],
        file:["$e", "$ne", "$exists", "$in", "$nin"]
    };

    $scope.formats = [
        { name: "REST", description: "", transform: restTransform },
        { name: "Javascript", description: "Parse Javascript SDK", transform: jsTransform },
        { name: "Objective-C", description: "Parse iOS SDK", transform: iOSTransform },
        { name: "Normalized", description: "Normalized JSON query syntax", transform: normalForm }
    ];

	
    $scope.metricsSchemaInfo = [{propColumn : 'impressions', propLabel  : 'IMPRESSIONS', propType : 'string'},
                        {propColumn : 'clicks', propLabel  : 'CLICKS', propType   : 'string'},
                        {propColumn : 'inquiries', propLabel  : 'INQUIRIES', propType   : 'string'},
                        {propColumn : 'revenue', propLabel  : 'REVENUE', propType   : 'string'},
                        {propColumn : 'costDollars', propLabel  : 'COST', propType   : 'string'},
                        {propColumn : 'netContribution', propLabel  : 'NC', propType   : 'string'}
    ];
    
    $scope.outputFormat = $scope.formats[0];
    
    $scope.executePowerQuery = function(query) {
    	$log.info("executePowerQuery : " + query);
    	var powerQuery = normalForm(query);
        $http.post('/powerSearch', powerQuery).success(function(results) {
    		$log.info('results : ' + results);
    		$scope.results = results;
    	});
    };

    $scope.executeQuery = function(query) {
        if (!App.hasAppInfo()) {
            $location.path('/ParseApp');
            return;
        }

        $scope.status = "sending request...";
        $scope.busy = true;

        var url = getBaseUrl(query);
        var expression = restTransformExpression(query);
        var params = { where : expression, limit:25 };
        var countParams = { where : expression, limit:0, count:1 };

        $http.get(url, {params: countParams, headers:App.getHeaders()}).
        then(function (data) {
            $scope.status = "found " + data.data.count;
            $http.get(url, {params: params,headers:App.getHeaders()}).
            then(function (data) {
                $scope.result = data.data.results;
                $scope.busy = false;
                setColumns(query, $scope.gridOptions);
            },
            function(error) {
                throw error;
            });
        },function(error) {
            $scope.status = "error status: " + error.status + " " + error.data.error;
            $scope.busy = false;
        });
    };
    

    $scope.transformQuery = function(query, format) {
        if (!query || !query.collection) {
            return "";
        }
        return format.transform(query);
    };

    $scope.addCondition = function(query, node) {
        var condition = getDefaultCondition(query, node, 1);
        node.nodes.push(condition);
        condition.show = true;
        annotateQuery(query, 1);
        $timeout(function() {
            condition.show = false;
        }, 500);
    };
    
    $scope.addMetricCondition = function(query, node) {
        var condition = getDefaultCondition(query, node, 2);
        node.metricNodes.push(condition);
        condition.show = true;
        annotateQuery(query, 2);
        $timeout(function() {
            condition.show = false;
        }, 500);
    };

    $scope.removeCondition = function(query, node, nodeType) {
        removeNode(query, node, nodeType);
    };

    $scope.addAndGroup = function(query, node) {
        addGroup(query, node, "$and");
    };

    $scope.addOrGroup = function(query, node) {
        addGroup(query, node, "$or");
    };

    $scope.getParentOperator = function(query, node, nodeType) {
        var parent = getNodeById(query, node.parentId, nodeType);
        var operator = getOperator(parent.op);
        return operator;
    };


    // This only exists because angular-ui doesn't support binding to UTC dates.
    // Once this is fixed, displayDate should be removed to let everything auto bind.
    $scope.setUtcDate = function(query, node) {
        node.val.iso = node.displayDate.toISOString();
    };
    $scope.setDisplayDate = function(query, node) {
        node.displayDate = new Date(Date.parse(node.val.iso));
    };

    // select2 drop down item template
    $scope.format = function(item) {
        if (!item.id) {
            return item.text; // optgroup
        }
        else {
            return "<span class='prop_name'>" + item.text + "<span class='prop_type'>" + $(item.element).data('schema-type') + "</span>";
        }
    };

    // select2 drop down item template
    $scope.escape = function(item) {
        return item;
    };

    function removeNode(query, node, nodeType) {
        node.hide = true;
        $timeout(function() {
            var parent = getNodeById(query, node.parentId, nodeType);
            var index = -1;
            for (index=0; index<parent.nodes.length; index++) {
                var condition = parent.nodes[index];
                if (condition.id === node.id)
                    break;
            }
            if (index < 0) {
                throw "can't find condition " + conditionPropName;
            }
            parent.nodes.splice(index,1);
            if (parent.id!==0 && parent.nodes.length===0) {
                removeNode(query, parent, nodeType);
            }
        }, 500);
    }

    function isCSVList(s) {
        if (angular.isObject()) {
            return false;
        }
        var arr = String(s).split(",");
        if (arr.length > 1) {
            return true;
        }
        else {
            return false;
        }
    }


    function getCollection(query) {
        if (query.hasOwnProperty("className")===false) {
            throw "query does not have className";
        }
        for (var i = 0; i<App.collections.length; i++) {
            if (App.collections[i].id===query.className) {
                return App.collections[i];
            }
        }
        return null;
    }

    function getNodeByPropertyName(tree, propertyName) {
        if (!tree) {
            return null;
        }
        var foundNode = null;
        getNodeR(tree, propertyName);
        return foundNode;
        function getNodeR(tree, propertyName) {
            if (tree.hasOwnProperty("prop") && tree.prop===propertyName) {
                foundNode = tree;
                return;
            }
            if (tree.hasOwnProperty("nodes")) {
                for (var i=0; i<tree.nodes.length; i++) {
                    var node = tree.nodes[i];
                    getNodeR(node, propertyName);
                }
            } if (tree.hasOwnProperty("metricNodes")) {
                for (var i=0; i<tree.metricNodes.length; i++) {
                    var node = tree.metricNodes[i];
                    getNodeR(node, propertyName);
                }
            }
            
        }
    }

    function getNodeById(tree, id, nodeType) {
        if (!tree) {
            return null;
        }
        if (tree.id===id && tree.nodeType==nodeType) {
            return tree;
        }
        var foundNode = null;
        getNodeR(tree, id);
        return foundNode;
        function getNodeR(tree, id) {
            if (tree.id===id) {
                foundNode = tree;
                return;
            }
            if (tree.hasOwnProperty("nodes")) {
                for (var i=0; i<tree.nodes.length; i++) {
                    var node = tree.nodes[i];
                    getNodeR(node, id);
                }
            } 
            if (tree.hasOwnProperty("metricNodes")) {
                for (var i=0; i<tree.metricNodes.length; i++) {
                    var node = tree.metricNodes[i];
                    getNodeR(node, propertyName);
                }
            }
        }
    }

    function addGroup(query, parent, op) {
//        var node = { op: op, nodes: []};
    	var orOp = { name: "$or", type: "group", description: "or" };
    	var node = { op: orOp, nodes: []};
        parent.nodes.push(node);
        $scope.addCondition(query, node);
    }

    function getDefaultCondition(query, parent, nodeType) {
        var collection = getCollection(query);
        var condition = { };
        for (var propertyName in collection.schema) {
            condition.prop = propertyName;
            if (getNodeByPropertyName(query, propertyName)===null)
                break;
        }
        condition.nodeType = nodeType;
        condition.op = $scope.psOperators[0];
        condition.booleanMatch = $scope.psBooleanMatches[0];
        return condition;
    }

    function getProperty(collection, propertyName) {
    	console.log("getProperty : " + propertyName);
        var property = { prop: name };
        property.type = collection.schema[propertyName];

        if (!property.type) {
            throw "property not found in schema: " + name;
        }
        if (property.type.indexOf("*")===0) {
            property.subType = property.type.substring(1);
            property.type = "pointer";
        }
        return property;
    }

    function restTransformExpression(query) {
        var s = "";
        restTransformExpressionR(query);
        return s;
        function restTransformExpressionR(query, parent) {
            if (query.op==="$and") {
                s += "{";
            }
            else if (query.op==="$or") {
                s += qt("$or") + ":[";
            }
            else {
                if (parent && parent.op==="$or") {
                    s += "{";
                }
                s += qt(query.prop);
                restTransformValue(query);
                if (parent && parent.op==="$or") {
                    s += "}";
                }
            }

            if (query.hasOwnProperty("nodes")) {
                var specialKeys = {};

                for (var j = 0; j<query.nodes.length; j++) {
                    var node = query.nodes[j];

                    if (node.op==="$e" || node.op==="$and" || node.op==="$or") {
                        restTransformExpressionR(node, query);
                        if (j < query.nodes.length-1 || Object.keys(specialKeys).length>0) {
                            s += ",";
                        }
                    }
                    else {
                        if (!specialKeys.hasOwnProperty(node.prop)) {
                            specialKeys[node.prop] = [];
                        }
                        specialKeys[node.prop].push(node);
                    }
                }

                var count = 0;
                for (var propertyName in specialKeys) {
                    if (query.op==="$or") {
                        for (var i=0; i<specialKeys[propertyName].length; i++) {
                            s += "{";
                            s += qt(propertyName);
                            s += ":{";

                            var specialNode = specialKeys[propertyName][i];
                            s += qt(specialNode.op);
                            restTransformValue(specialNode);
                            s += "}}";
                            if (i < specialKeys[propertyName].length-1) {
                                s += ",";
                            }
                        }
                    }
                    else {
                        s += qt(propertyName);
                        s += ":{";
                        for (var i=0; i<specialKeys[propertyName].length; i++) {
                            var specialNode = specialKeys[propertyName][i];
                            s += qt(specialNode.op);
                            restTransformValue(specialNode);
                            if (i < specialKeys[propertyName].length-1) {
                                s += ",";
                            }
                        }
                        s += "}";
                    }
                    if (count < Object.keys(specialKeys).length-1) {
                        s += ",";
                    }
                    count++;
                }
            }

            if (query.op==="$or") {
                s += "]";
            }
            if (query.op==="$and") {
                s += "}";
            }
        }

        function restTransformValue(node) {
            s += ":";
            if (node.opType==="set" || node.type==="array") {
                s += "[";
                var arr = node.val.split(",");
                for (var i=0; i<arr.length; i++) {
                    var val = arr[i];
                    if (node.type==="string") {
                        s += qt(val);
                    }
                    else {
                        s += val;
                    }
                    if (i < arr.length-1) {
                        s += ",";
                    }
                }
                s += "]";
            }
            else if (node.opType==="exists") {
                s += Boolean(node.val);
            }
            else {
                switch (node.type) {
                    case "string":
                    case "attribute":
                        s += qt(node.val);
                        break;
                    case "number":
                    case "boolean":
                        s += node.val;
                        break;
                    case "pointer":
                    case "date":
                        s += JSON.stringify(node.val);
                        break;
                    default:
                        s += qt("type not supported: " + node.type);
                }
            }
            if (node.hasOwnProperty("options")) {
                s += ",";
                s += qt("options");
                s += ":";
                s += qt(node.options);
            }
        }
    }

    // Add meta info to make the query easier to edit and display
    function annotateQuery(query, nodeType) {
        query.collection = getCollection(query);
        if (!query.hasOwnProperty("nextId")) {
            query.nextId = 0;
        }
        if (!query.hasOwnProperty("metricsNextId")) {
            query.metricsNextId = 0;
        }
        annotateQueryR(query, null, nodeType);
        function annotateQueryR(tree, parent, nodeType) {
            if (!tree.hasOwnProperty("id") && nodeType == 1) {
                tree.id = query.nextId++;
            } else if (!tree.hasOwnProperty("id") && nodeType == 2) {
                tree.id = query.metricsNextId++;
            } 
            if (parent && !tree.hasOwnProperty("parentId") && nodeType == 1) {
                tree.parentId = parent.id;
            }
            if (parent && !tree.hasOwnProperty("parentId") && nodeType == 2) {
                tree.parentId = parent.id;
            }
            if (tree.hasOwnProperty("prop")) {
                var property = getProperty(query.collection, tree.prop);
                tree.type = property.type;
                if (property.subType) {
                    tree.subType = property.subType;
                }
                if (tree.type==="pointer") {
                    tree.val.__type = "Pointer";
                }
                if (tree.type==="date") {
                    tree.val.__type = "Date";
                    tree.displayDate = new Date(Date.parse(tree.val.iso));
                }
                if (tree.type==="file") {
                    tree.val.__type = "File";
                }
            }
            if (tree.hasOwnProperty("op")) {
                tree.opType = getOperator(tree.op).type;
            }
            if (tree.hasOwnProperty("nodes")) {
                for (var i=0; i<tree.nodes.length; i++) {
                    var node = tree.nodes[i];
                    annotateQueryR(node, tree, nodeType);
                }
            }  
            if (tree.hasOwnProperty("metricNodes")) {
                for (var i=0; i<tree.metricNodes.length; i++) {
                    var node = tree.metricNodes[i];
                    annotateQueryR(node, tree, nodeType);
                }
            }
        }
    }

    function deAnnotateQuery(query) {
        if (query.hasOwnProperty("collection")) {
            delete query.collection;
        }
        if (query.hasOwnProperty("nextId")) {
            delete query.nextId;
        }
        if (query.hasOwnProperty("metricsNextId")) {
            delete query.metricsNextId;
        }
        deAnnotateQueryR(query, null);
        function deAnnotateQueryR(tree, parent) {
            if (tree.hasOwnProperty("id")) {
                delete tree.id;
            }
            if (tree.hasOwnProperty("parentId")) {
                delete tree.parentId;
            }
            if (tree.hasOwnProperty("type")) {
                delete tree.type;
            }
            if (tree.hasOwnProperty("opType")) {
                delete tree.opType;
            }
            if (tree.hasOwnProperty("subType")) {
                delete tree.subType;
            }
            if (tree.hasOwnProperty("displayDate")) {
                delete tree.displayDate;
            }
            if (tree.hasOwnProperty("nodes")) {
                for (var i=0; i<tree.nodes.length; i++) {
                    var node = tree.nodes[i];
                    deAnnotateQueryR(node, tree);
                }
            } 
            if (tree.hasOwnProperty("metricNodes")) {
                for (var i=0; i<tree.metricNodes.length; i++) {
                    var node = tree.metricNodes[i];
                    deAnnotateQueryR(node, tree);
                }
            } 
        }
    }


    var operatorGroups = {};
    init();

    // Create a default query for each data class
    function init() {
        App.loadSchema();
        
        //"schema":{"weight":"number","date":"date","workout":"string"}," 
        
        var coll = App.collections[0];
        coll.schema = {"ANY TEXT":"attribute","KEYWORD":"string","MATCH TYPE":"attribute", "SOURCE ID":"string", "ACCOUNT NAME":"string",
        			   "CAMPAIGN NAME":"attribute", "GROUP NAME":"string", "LIST":"attribute", "DESTINATION URL" :"attribute", "SITE NAME" :"attribute", "VERTICAL":"attribute",
        			   "IMPRESSIONS":"string", "CLICKS":"string", "INQUIRIES":"string", "REVENUE":"string", "COST":"string", "NC":"string"};
        
        createOperatorGroups();

        for (var j = 0; j<App.collections.length; j++) {
            var collection = App.collections[j];
            var query = createQuery(collection.id);
            query.schemaInfo = [{propColumn : 'any', propLabel  : 'ANY TEXT', propType : 'any'},
                               {propColumn : 'keyword', propLabel  : 'KEYWORD', propType   : 'string'},
                               {propColumn : 'keywordType', propLabel  : 'MATCH TYPE', propType   : 'string'},
                               {propColumn : 'sourceId', propLabel  : 'SOURCE ID', propType   : 'string'},
                               {propColumn : 'account', propLabel  : 'ACCOUNT NAME', propType   : 'string'},
                               {propColumn : 'campaignName', propLabel  : 'CAMPAIGN NAME', propType   : 'string'},
                               {propColumn : 'groupName', propLabel  : 'GROUP NAME', propType   : 'string'},
                               {propColumn : 'destinationUrl', propLabel  : 'DESTINATION URL', propType   : 'string'},
                               {propColumn : 'engineName', propLabel  : 'SITE NAME', propType   : 'string'},
                               {propColumn : 'vertical', propLabel  : 'VERTICAL', propType   : 'string'},
                               {propColumn : 'lists', propLabel  : 'LIST', propType   : 'string'},
                               {propColumn : 'impressions', propLabel  : 'IMPRESSIONS', propType : 'Long'},
                               {propColumn : 'clicks', propLabel  : 'CLICKS', propType   : 'Integer'},
                               {propColumn : 'inquiries', propLabel  : 'INQUIRIES', propType   : 'Long'},
                               {propColumn : 'revenue', propLabel  : 'REVENUE', propType   : 'BigDecimal'},
                               {propColumn : 'costDollars', propLabel  : 'COST', propType   : 'BigDecimal'},
                               {propColumn : 'netContribution', propLabel  : 'NC', propType   : 'BigDecimal'}
                              ];
            $scope.queries.push(query);
        }

    }

    function createOperatorGroups() {
        for (var type in operatorsAllowed) {
            var operators = [];
            var allowed = operatorsAllowed[type];
            for (var i = 0; i<allowed.length; i++) {
                var operator = getOperator(allowed[i]);
                operators.push(operator);
            }
            operatorGroups[type] = operators;
        }
    }

    function getOperator(name) {
    	var opName = name.name ? name.name : name;
    	console.log('getOperator : ' + name + ' opName : ' + opName);
        for (var i = 0; i<$scope.operators.length; i++) {
            if ($scope.operators[i].name===opName) {
                return $scope.operators[i];
            }
        }
        throw "operator not found: " + name;
    }

    function createQuery(className) {
    	var andOp = { name: "$and", type: "group", description: "and" };
        var query = {
            className:className,
//            op: "$and",
            op : andOp,
            nodes: [],
            metricNodes: []
        };
        //$scope.addCondition(query, query);
        annotateQuery(query);
        return query;
    }

    function getBaseUrl(query) {
        var s = App.dataUrl + query.collection.id;
        return s;
    }

    function normalForm(query) {
        var copy = JSON.stringify(query);
        copy = JSON.parse(copy);
        deAnnotateQuery(copy);
        return copy;
    }

    function restTransform(query) {
        var base = getBaseUrl(query) + "?where=";
        var expression = restTransformExpression(query);
        var t = base + expression;
        return t;
    }

    function jsTransform(query) {
        return "TODO: Javascript transform";
    }

    function iOSTransform(query) {
        return "TODO: iOS transform";
    }

    $scope.$on('ngGridEventColumns', function(newColumns) {

    });

    function qt(s) {
        return "\"" + s + "\"";
    }

    function setColumns(query) {
        var cols = [];
        for (var propertyName in query.collection.schema) {
            var col = {};
            col.field = propertyName;
            col.displayName = propertyName;
            //col.minWidth = 100;
            col.width = 100;

            var prop = getProperty(query.collection, propertyName);
            if (prop.type==="date") {
                col.cellTemplate = "<div class='ngCellText'>{{ COL_FIELD.iso | date:'MM/dd/yyyy' }}</div>";
            }
            else if (prop.type==="array") {
                col.cellTemplate = "<div class='ngCellText'>array ({{ COL_FIELD.length }})</div>";
            }
            else if (prop.type==="pointer") {
                col.cellTemplate = "<div class='ngCellText'>" + prop.subType + "* {{ COL_FIELD.objectId }}</div>";
                col.width = 200;
            }
            else if (prop.type==="file") {
                col.cellTemplate = "<div class='ngCellText'>{{ COL_FIELD.url.substring(97) }}</div>";
                col.width = 200;
            }
            else if (prop.type==="number") {
                col.width = 80;
            }
            else if (prop.type==="boolean") {
                col.width = 50;
            }
            cols.push(col);
        }
        $scope.columnDefs = cols;
    }

    $scope.columnDefs = [
        { field: 'columns', displayName: 'Results' }
    ];

    $scope.gridOptions = {
        data:'result',
        columnDefs: 'columnDefs',
        enableColumnResize: true
    }
}

myapp.controller('queryCtrl3',  queryCtrl3);

}());