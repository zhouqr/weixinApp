
angular.module('weixin.directives.topic.chart',['services.topics'])
    .directive('chart', function ($http, $q) {
        return {
            restrict: 'EA',
            templateUrl: appPath + '/wx/directives/topic/chart.html',
            scope:{
	        	id:'=',
  	      	}, 
            controller:function($scope,Topics,$attrs){
            	
            },
            link: function (scope, element, attrs) {
            	
            	scope.topicLine = function(){
           		 $http({
           			  method: 'GET',
           			  url: 'Topic/docCountAnalysis',
           			  params:{
           				  id:scope.id,
           				  day:7
           			  }
           		  }).success(function(data, status) {
           			 if(data.code == 200){
           				line7Chart(data.info.categories,data.info.data)
           			 }
           		 });
           	 	}
           	 	scope.topicLine();
            	function line7Chart(categories,data){
            		Highcharts.setOptions({
        				lang: {
        					contextButtonTitle:"下载图片",
        					printChart:"打印图片",
        					downloadJPEG: "下载JPEG 图片",  
        		            downloadPDF: "下载PDF文档" , 
        		            downloadPNG: "下载PNG 图片" , 
        		            downloadSVG: "下载SVG 矢量图" , 
        		            exportButtonTitle: "导出图片",
        		            resetZoom:"重置缩放"
        				}
        		 });
        		 
        		 $('#container'+scope.id).highcharts({
        			 	chart: {
        		            zoomType: 'x',
        		            spacingRight: 20
        		        },
        		        credits:{
        		        	enabled:false
        		        },
        		        title: {
        		            text: '专题文章趋势'
        		        },
        		        xAxis: {
        		        	 labels: { 
        		                 formatter: function() { 
        		                                return  Highcharts.dateFormat('%Y-%m-%d', this.value); 
        		                 } 
        		             } ,
        		        	title: {
                                text: '日期'
                              },
                            //linear" or "datetime"
                            type: 'datetime',
                            //坐标间隔
                            tickPixelInterval: 150,
        		        },
        		        yAxis: {
        		            title: {
        		                text: '文章数'
        		            }
        		        },
        		        tooltip: {
        		        	formatter: function() {
                                return '<b>时间：</b>' +
                                        Highcharts.dateFormat('%Y-%m-%d ', this.x) + '<br/>文章数：' +
                                        Highcharts.numberFormat(this.y, 0);
                              }
        		        },
        		        legend: {
        		            enabled: false
        		        },
        		        plotOptions: {
        		            area: {
        		                fillColor: {
        		                    linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
        		                    stops: [
        		                        [0, Highcharts.getOptions().colors[0]],
        		                        [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
        		                    ]
        		                },
        		                lineWidth: 1,
        		                marker: {
        		                    enabled: false
        		                },
        		                shadow: false,
        		                states: {
        		                    hover: {
        		                        lineWidth: 1
        		                    }
        		                },
        		                threshold: null
        		            }
        		        },
        		        series: [{
        		            type: 'line',
        		            pointInterval: 24 * 3600 * 1000,
        		            pointStart: new Date(categories[0]).getTime(),
        		            data:data,
        		        }]
        	        });
           	 	}
            }
        };
    })