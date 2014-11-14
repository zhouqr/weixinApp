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
	            title: {
	                text: '专题文章趋势',
	                x: -20 //center
	            },
	            xAxis: {
	                categories: categories
	            },
	            yAxis: {
	                title: {
	                    text: '文章数'
	                },
	                plotLines: [{
	                    value: 0,
	                    width: 1,
	                    color: '#808080'
	                }]
	            },
	            tooltip: {
	                valueSuffix: '篇'
	            },
	            legend: {
	                layout: 'vertical',
	                align: 'right',
	                verticalAlign: 'middle',
	                borderWidth: 0
	            },
	            series: [{
	                name: '专题文章趋势',
	                data: data
	            }]
	        });
	 }
	 

	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	  
  }])
  