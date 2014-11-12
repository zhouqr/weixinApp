
angular.module('weixin.wx.search.home',[])
    .config(function($stateProvider){
        $stateProvider
            .state('wx.search.home',{
                url:'/home',
                templateUrl: appPath + '/wx/search/home/view.html',
                controller:function($scope,$stateParams,$state){
                	console.log("");
                }
            })
    })
    //获取微信检索数据
  .controller('WeiXinSearchCtrl', ['$scope','$http', function($scope,$http) {
	/*var path = appPath + '/wx/search/home/topics.json';
    var topics = $http.get(path).then(function (resp) {
        $scope.topic = resp.data.topics[0];
        $scope.topic.date="2014-10-20 12:12:12";

    });
    //检索公众号
    var weixins = $http.get(appPath + '/wx/search/home/weixin.json').then(function (resp) {
        $scope.weixins = resp.data.weixins;

    });*/
	  $scope.imgDeg  = "public/img/topics/fang.jpg";
	  $scope.topics = [];
	  $scope.weixins = [];
	  $scope.search =function(){
		  $http({
			  method: 'GET',
			  url: 'search/searchArticals',
			  params:{
				  keywords : $scope.keywords
			  }
		  }).success(function(data, status) {
			 if(data.code == 200){
				 $scope.topics = data.info;
			 }
		  });
		  
		  $http({
			  method: 'GET',
			  url: 'search/searchWxs',
			  params:{
				  keywords : $scope.keywords
			  }
		  }).success(function(data, status) {
			 if(data.code == 200){
				 $scope.weixins = data.info;
			 }
		  });
		  
		  
	   };
	  
    }])
