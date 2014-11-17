angular.module('weixin.wx.topic.detail',[])
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
  .controller('TopicDetailCtrl', ['$scope','$http','$stateParams', function($scope,$http,$stateParams) {
	  $scope.imgDeg  = "public/img/topics/default.jpg";
	  $scope.list =function(){
		  $http({
			  method: 'GET',
			  url: 'Topic/topicDetail',
			  params:{
				  id:$stateParams.id
			  }
		  }).success(function(data, status) {
			 if(data.code == 200){
				 $scope.topic = data.info;
			 }
		 });
		  
		  $http({
			  method: 'GET',
			  url: 'Topic/getTopicDocs',
			  params:{
				  id:$stateParams.id,
				  page:1,
				  pageSize:10,
			  }
		  }).success(function(data, status) {
			 if(data.code == 200){
				 $scope.topics = data.info;
			 }
		 });
			
	  };
	 $scope.list();
	 
	 
	 $scope.radioModel = 7;
	 $scope.topicFlot = function(day){
		 console.log(day);
		 $http({
			  method: 'GET',
			  url: 'Topic/docCountAnalysis',
			  params:{
				  id:$stateParams.id,
				  day:day
			  }
		  }).success(function(data, status) {
			 if(data.code == 200){
				 lineChart(data.info.categories,data.info.data)
			 }
		 });
		 
	 }
	 $scope.topicFlot(7);
	 
	 
	 function lineChart(categories,data){
		 $('#container').highcharts({
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
	 

	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	  
  }])
  