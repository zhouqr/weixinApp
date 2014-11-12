
angular.module('weixin.wx.topic.home',['weixin.wx.topic.home.directives.list'])
    .config(function($stateProvider){
        $stateProvider
            .state('wx.topic.home',{
                url:'/home',
                templateUrl: appPath + '/wx/topic/home/view.html',
                controller:function($scope,$stateParams,$state){
                	console.log("");
                }
            })
    })
   //获取专题列表
  .controller('TopicListCtrl', ['$scope','$http', function($scope,$http) {
	  $scope.imgDeg  = "public/img/topics/yantai.jpg";
	  var List =function(){
		  $http({
			  method: 'GET',
			  url: 'topic/topicList',
			  params:{
			  }
		  }).success(function(data, status) {
			 if(data.code == 200){
				 $scope.topics = data.info;
			 }
		  });
	  };
	  List();
	  /*var path = appPath + '/wx/topic/home/topics.json';
	  var topics = $http.get(path).then(function (resp) {
	    $scope.topics = resp.data.topics;
	  });*/
  }])
