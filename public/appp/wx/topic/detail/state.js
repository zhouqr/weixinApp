angular.module('weixin.wx.topic.detail',['services.topics'])
    .config(function($stateProvider){
        $stateProvider
            .state('wx.topic.detail',{
                url:'/detail/{id}',
                templateUrl: appPath + '/wx/topic/detail/view.html',
                controller:function($scope,$stateParams,$state){
                }
            })
    })
   	//获取专题列表
  .controller('TopicDetailCtrl', ['$scope','$http','$stateParams','Topics', function($scope,$http,$stateParams,Topics) {
	  $scope.imgDeg  = "public/img/topics/default.jpg";
	  
	  
	  function init(){
		  $scope.topicLoading = true;
		  $scope.topics = [];
		  $scope.topic_no_data = false;
	  }
	  
	  $scope.topicDetail = function(){
		  Topics.topicDetail($stateParams.id).success(function(data, status) {
			 if(data.code == 200){
				 $scope.topic = data.info;
			 }
		 });
	  }
	  
	  $scope.topicDetail();
	  
	  $scope.list =function(page){
		  init();
		  Topics.topicDetailList($stateParams.id,page,10)
		  .success(function(data, status) {
			 $scope.topicLoading = false;
			 if(data.code == 200){
				 $scope.topics = data.info.articles;
				 $scope.bigTotalItems =  data.info.bigTotalItems;
				 if ($scope.topics.length == 0) {
					   $scope.topic_no_data = true;
				 }else{
					   $scope.topic_no_data = false;
				 }
			 }
		 }).error(function(data){
	       	  $scope.topicLoading = false;
	    	  $scope.topic_no_data = true;
		 });
			
	  };
	 $scope.list(1);
	 
	 //$scope.bigTotalItems = 60;
     $scope.bigCurrentPage = 1;
     $scope.maxSize = 5;
     $scope.pageChanged = function() {
    	 $scope.list($scope.bigCurrentPage);
     };
	 
	 $scope.radioModel = 7;
	 $scope.topicLine = function(day){
		 $("#lineContainerLoading").show();
		 $("#lineContainer").empty();
		 $http({
			  method: 'GET',
			  url: 'Topic/docCountAnalysis',
			  params:{
				  id:$stateParams.id,
				  day:day
			  }
		  }).success(function(data, status) {
			  $("#lineContainerLoading").hide();
			  if(data.code == 200){
				  lineChart(data.info.categories,data.info.data)
			  }
		 });
		 
	 }
	 $scope.topicLine($scope.radioModel);
	 
	 $scope.topicPie = function(){
		 $("#pieContainerLoading").show();
		 $("#pieContainer").empty();
		 $http({
			  method: 'GET',
			  url: 'Topic/topPerson',
			  params:{
				  id:$stateParams.id,
			  }
		  }).success(function(data, status) {
			 $("#pieContainerLoading").hide();
			 if(data.code == 200){
				 pieChart(data.info);
			 }
		 });
	 };
	 $scope.topicPie();
	 
	 
	 
	 function lineChart(categories,data){
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
		 
		 $('#lineContainer').highcharts({
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
	 
	 
	 function pieChart(data){
		// pieContainer
		 $('#pieContainer').highcharts({
		        chart: {
		            plotBackgroundColor: null,
		            plotBorderWidth: null,
		            plotShadow: false
		        },
		        title: {
		            text: '热点关注分布图'
		        },
		        credits:{
		        	enabled:false
		        },
		        tooltip: {
		    	    pointFormat: '比例: <b>{point.percentage:.1f}%</b>'
		        },
		        plotOptions: {
		            pie: {
		                allowPointSelect: true,
		                cursor: 'pointer',
		                dataLabels: {
		                    enabled: true,
		                    color: '#000000',
		                    connectorColor: '#000000',
		                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
		                }
		            }
		        },
		        series: [{
		            type: 'pie',
		            //name: 'Browser share',
		            data: data
		        }]
		    });
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	  
  }])
  