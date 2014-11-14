
angular.module('weixin.wx.search.home',['services.search','weixin.wx.topic.search.directives.search'])
    .config(function($stateProvider){
        $stateProvider
            .state('wx.search.home',{
                url:'/home/{keywords}',
                templateUrl: appPath + '/wx/search/home/view.html',
                controller:function($scope,$stateParams,$state){
                	console.log("");
                }
            })
    })
    //获取微信检索数据
  .controller('WeiXinSearchCtrl', ['$scope','$http','$stateParams','Search', function($scope,$http,$stateParams,Search) {
	  $scope.searchKeywords = $stateParams.keywords
	  $scope.imgDeg  = "public/img/topics/default.jpg";
	  $scope.topics = [];
	  $scope.weixins = [];
	  
	  $scope.search = function(keywords){
		  
		  $scope.topicLoading = true;
		  $scope.weixinLoading = true;
		  Search.searchArtical(keywords==undefined?$scope.keywords:keywords).success(function(data){
			  if(data.code == 200){
				  $scope.topicLoading = false;
				  $scope.topics = data.info.articals;
				  $scope.topicsCount = data.info.total_articals;
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
          
          Search.searchWxs(keywords==undefined?$scope.keywords:keywords).success(function(data){
        	  if(data.code == 200){
        		  $scope.weixinLoading = false;
        		  $scope.weixins  = data.info.wxlist;
        		  $scope.weixinsCount = data.info.total_num;
        		  if ($scope.weixins.length == 0) {
						$scope.weixin_no_data = true;
				  }else{
					    $scope.weixin_no_data = false;
				  }
			  }
          }).error(function(data){
        	  $scope.weixinLoading = false;
        	  $scope.weixin_no_data = true;
          });
		  
	   };
	   $scope.search($scope.searchKeywords);
	  
    }])
