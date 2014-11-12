angular.module('weixin.wx.topic.detail',[])
    .config(function($stateProvider){
        $stateProvider
            .state('wx.topic.detail',{
                url:'/detail',
                templateUrl: appPath + '/wx/topic/detail/view.html',
                controller:function($scope,$stateParams,$state){
                	console.log("");
                }
            })
    })
   	//获取专题列表
  .controller('TopicDetailCtrl', ['$scope','$http', function($scope,$http) {
	  var path = appPath + '/wx/topic/home/topics.json';
	  var topics = $http.get(path).then(function (resp) {
	    $scope.topic = resp.data.topics[0];
	    $scope.topic.date="2014-10-20 12:12:12";
	    
	  });
  }])
  