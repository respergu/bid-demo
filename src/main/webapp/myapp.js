//Define a function scope, variables used inside it will NOT be globally visible.
(function() {

//See: https://github.com/pablojim/highcharts-ng
var myapp = angular.module('myapp', ['highcharts-ng', 'ui.bootstrap','ngResource', 'ngRoute',  'parseResource']);

myapp.config(function($routeProvider, $httpProvider, $provide) {
    $routeProvider.
        when('/graph', {templateUrl: 'partials/graph.html', controller: 'myctrl'}).
        when('/power', {templateUrl: 'partials/power.html', controller: 'queryCtrl3'}).
        otherwise({redirectTo: '/graph'});
});

myapp.controller('myctrl', function ($scope, $log, $http) {
    $log.info('myctrl');
    
    $scope.chartRequest = {};
    $scope.chartRequest.range1FromDate = new Date();
    $scope.chartRequest.range1ToDate = new Date();
    $scope.chartRequest.range2FromDate = new Date();
    $scope.chartRequest.range2ToDate = new Date();
    $scope.chartRequest.compareDates = false; 
    $scope.graph = {};
    $scope.dateList = [{id : 0, name : 'Last 7 Days'},
                       {id : 1, name : 'Today'},
                       {id : 2, name : 'Yesterday'},
                       {id : 3, name : 'Last Week'},
                       {id : 4, name : 'Current Month'},
                       {id : 5, name : 'Last Month'},
                       {id : 6, name : 'Custom'}
                       ];
    $scope.graph.dateFilter = $scope.dateList[0];
    
    $scope.addPast2WeeksCostSerie = function () {
    	$scope.chartRequest.dateRange = $scope.graph.dateFilter.id;
        $http.post('keywordCosts', $scope.chartRequest).success(function(costs) {
    		$log.info('costs : ' + costs.series[0].data);
    		
    		$scope.chartConfig.xAxis =  costs.options.xAxis;
    		$scope.chartConfig.series = costs.series;
    		
    		var chart = $('#chart1').highcharts();
    		if ($scope.chartRequest.compareDates) {
    			chart.xAxis[1].update(costs.options.xAxis2, true);
    		} else {
    			chart.xAxis[1].update({categories: []}, true);
    		}
    	});
    };
    



    $scope.chartConfig = {
    	//This is not a highcharts object. It just looks a little like one!
        options: {
        	//This is the Main Highcharts chart config. Any Highchart options are valid here.
            //will be ovverriden by values specified below.
            chart: {
                type: 'line'
            },
            plotOptions: {
                series: {
                    events: {
                    	legendItemClick: function(event) {
                            if (!$scope.chartRequest.compareDates)
                                return true;
                            var seriesIndex = this.index;
                            var series = this.chart.series;
                            var sname = ''; 
                            for (var i = 0; i < series.length; i++)
                            {
                            	var serie = series[i];
                                if (serie.index == seriesIndex)
                                {
                                	serie.show();
                                	sname = serie.name; 
                                }
                            }
                            
                            for (var i = 0; i < series.length; i++)
                            {
                            	var serie = series[i];
                                if (serie.name == sname)
                                {
                                	serie.show();
                                	
                                } else {
                                	serie.hide();
                                }
                            }
                            return false;
                        }
                    }
                }
            }
        },
        tooltip: {
            backgroundColor: null,
            borderWidth: 0,
            shadow: false,
            useHTML: true,
            style: {
                padding: 0
            }
        },
        yAxis: [{
            labels: {
                formatter: function() {
                    return '$' + this.value;
                }
            },
            title: {
                text: 'Dollars'
            }
        }, {
            title: {
                text: 'Units'
            },
            opposite: true
        }, {
            title: {
                text: 'Misc'
            },
            opposite: true
        }, {
            labels: {
                formatter: function() {
                    return '$' + this.value;
                }
            },
            title: {
                text: 'Dollar Ratios'
            }
        }, {
            labels: {
                formatter: function() {
                    return this.value + '%';
                }
            },
            title: {
                text: 'Percentages'
            },
            opposite: true
        }],
        xAxis: [{
            labels: {
                formatter: function() {
                    return this.value;
                }
            },
            title: {
                text: 'Last 7 Days'
            },
            opposite: false
        }, {
            title: {
                text: ''
            },
            opposite: true
        }],
        title: {
            text: 'Cost Metrics'
        },

        loading: false
    };
    
    $scope.$watch("graph.dateFilter", function(newValue, oldValue) {
    	if (newValue != undefined) {
    		if (newValue.id == 0) {
    			$log.info('newValue : ' + newValue.name);
    			if ($scope.chartRequest.compareDates) {
    				$scope.chartRequest.compareDates = false;
    				var chart = $('#chart1').highcharts();
    				chart.xAxis[1].update({categories: []}, true);
    			}
    			var today = new Date();
    			var lastWeek = new Date(today.getFullYear(), today.getMonth(), today.getDate() - 7);
    			
    			$scope.chartRequest.range1FromDate = lastWeek;
    			$scope.chartRequest.range1ToDate = today;
    			$scope.chartConfig.loading = true;		
    			$scope.chartRequest.dateRange = newValue.id;
    			$http.post('keywordCosts', $scope.chartRequest).success(function(costs) {
    	    		$log.info('costs : ' + costs.series[0].data);
    	    		$scope.chartConfig.xAxis =  costs.options.xAxis;
    	    		$scope.chartConfig.series = costs.series;
    	    		$scope.chartConfig.loading = false;	
    	    	});
    		}
    		if (newValue.id == 1) {
    			$log.info('newValue : ' + newValue.name);
    			if ($scope.chartRequest.compareDates) {
    				$scope.chartRequest.compareDates = false;
    				var chart = $('#chart1').highcharts();
    				chart.xAxis[1].update({categories: []}, true);
    			}
    			var today = new Date();
    			today.setHours(0,0,0,0);
    			var tomorrow = new Date(today.getTime() + 24 * 60 * 60 * 1000);
    			
    			$scope.chartRequest.range1FromDate = today;
    			$scope.chartRequest.range1ToDate = tomorrow;
    			$scope.chartConfig.loading = true;
    			$scope.chartRequest.dateRange = newValue.id;
    			$http.post('keywordCosts', $scope.chartRequest).success(function(costs) {
    	    		$log.info('costs : ' + costs.series[0].data);
    	    		$scope.chartConfig.xAxis =  costs.options.xAxis;
    	    		$scope.chartConfig.series = costs.series;
    	    		$scope.chartConfig.loading = false;	
    	    	});
    		}
    		if (newValue.id == 2) {
    			$log.info('newValue : ' + newValue.name);
    			if ($scope.chartRequest.compareDates) {
    				$scope.chartRequest.compareDates = false;
    				var chart = $('#chart1').highcharts();
    				chart.xAxis[1].update({categories: []}, true);
    			}
    			var today = new Date();
    			var yesterday = new Date(today.getFullYear(), today.getMonth(), today.getDate() - 1);
    			
    			$scope.chartRequest.range1FromDate = yesterday;
    			$scope.chartRequest.range1ToDate = today;
    			$scope.chartConfig.loading = true;
    			$scope.chartRequest.dateRange = newValue.id;
    			$http.post('keywordCosts', $scope.chartRequest).success(function(costs) {
    	    		$log.info('costs : ' + costs.series[0].data);
    	    		$scope.chartConfig.xAxis =  costs.options.xAxis;
    	    		$scope.chartConfig.series = costs.series;
    	    		$scope.chartConfig.loading = false;
    	    	});
    		}
    		if (newValue.id == 3) {
    			$log.info('newValue : ' + newValue.name);
    			if ($scope.chartRequest.compareDates) {
    				$scope.chartRequest.compareDates = false;
    				var chart = $('#chart1').highcharts();
    				chart.xAxis[1].update({categories: []}, true);
    			}
    			var beforeOneWeek = new Date(new Date().getTime() - 60 * 60 * 24 * 7 * 1000)
    			  , day = beforeOneWeek.getDay()
    			  , diffToMonday = beforeOneWeek.getDate() - day + (day === 0 ? -6 : 1)
    			  , lastMonday = new Date(beforeOneWeek.setDate(diffToMonday))
    			  , lastSunday = new Date(beforeOneWeek.setDate(diffToMonday + 6));
    			
    			
    			$scope.chartRequest.range1FromDate = lastMonday;
    			$scope.chartRequest.range1ToDate = lastSunday;
    			$scope.chartConfig.loading = true;
    			$scope.chartRequest.dateRange = newValue.id;
    			$http.post('keywordCosts', $scope.chartRequest).success(function(costs) {
    	    		$log.info('costs : ' + costs.series[0].data);
    	    		$scope.chartConfig.xAxis =  costs.options.xAxis;
    	    		$scope.chartConfig.series = costs.series;
    	    		$scope.chartConfig.loading = false;	
    	    	});
    		}
    		if (newValue.id == 4) {
    			$log.info('newValue : ' + newValue.name);
    			if ($scope.chartRequest.compareDates) {
    				$scope.chartRequest.compareDates = false;
    				var chart = $('#chart1').highcharts();
    				chart.xAxis[1].update({categories: []}, true);
    			}
    			var date = new Date();
    			var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
    			var today = new Date();
    			today.setHours(0,0,0,0);
    			
    			$scope.chartRequest.range1FromDate = firstDay;
    			$scope.chartRequest.range1ToDate = today;
    			$scope.chartConfig.loading = true;
    			$scope.chartRequest.dateRange = newValue.id;
    			$http.post('keywordCosts', $scope.chartRequest).success(function(costs) {
    	    		$log.info('costs : ' + costs.series[0].data);
    	    		$scope.chartConfig.xAxis =  costs.options.xAxis;
    	    		$scope.chartConfig.series = costs.series;
    	    		$scope.chartConfig.loading = false;	
    	    	});
    		}
    		if (newValue.id == 5) {
    			$log.info('newValue : ' + newValue.name);
    			if ($scope.chartRequest.compareDates) {
    				$scope.chartRequest.compareDates = false;
    				var chart = $('#chart1').highcharts();
    				chart.xAxis[1].update({categories: []}, true);
    			}
    			var date = new Date();
    			var lastMonthfirstDay = new Date(date.getFullYear(), date.getMonth()-1, 1);
    			var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
    			var today = new Date();
    			today.setHours(0,0,0,0);
    			
    			$scope.chartRequest.range1FromDate = lastMonthfirstDay;
    			$scope.chartRequest.range1ToDate = firstDay;
    			$scope.chartConfig.loading = true;
    			$scope.chartRequest.dateRange = newValue.id;
    			$http.post('keywordCosts', $scope.chartRequest).success(function(costs) {
    	    		$log.info('costs : ' + costs.series[0].data);
    	    		$scope.chartConfig.xAxis =  costs.options.xAxis;
    	    		$scope.chartConfig.series = costs.series;
    	    		$scope.chartConfig.loading = false;	
    	    	});
    		}
    	}
    });

});


myapp.factory('Parse', function ($parseResource) { return $parseResource(); });
myapp.service('App', appService);


// To use the angularjs $resource layer, add parse.com keys here
// These keys are not used for the Query Builder gui.  For the gui you will be prompted to enter keys at runtime.
myapp.constant("PARSE_CONFIG",{
        defaultHeaders: {
            "X-Parse-Application-Id" : "L0YCZWxFcGYhYThJCj3JmSjs66kA7Y9vof2bOxdr",
            "X-Parse-REST-API-Key" : "dP8U9d0owruArkuZfmslfghEaCHy1DhUyI8Gdrd5"
        },
        defaultParams: {}
    });

}());
