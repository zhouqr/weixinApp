
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
	  
	  
	  $scope.bigTotalItemsTopic = 0;
	  $scope.bigCurrentPageTopic = 1;
	  $scope.maxSizeTopic = 5;
	  
      $scope.pageChangedTopic = function(bigCurrentPage) {
    	  $scope.topicList($scope.keywords,bigCurrentPage);
      };
      
      $scope.bigTotalItemsWx = 0;
      $scope.bigCurrentPageWx = 1;
      $scope.maxSizeWx = 5;
      
      $scope.pageChangedWx = function(bigCurrentPage) {
    	  $scope.wxList($scope.keywords,bigCurrentPage);
      };
      
      function init(){
		  $scope.topicLoading = true;
		  $scope.topics = [];
		  $scope.topic_no_data = false;
		  
		  $scope.weixinLoading = true;
		  $scope.weixins = [];
		  $scope.topic_no_data = false;
	  }
      
	  
	  
	  $scope.topicList = function(keywords,page){
		  
		  Search.searchArtical(keywords==undefined?$scope.keywords:keywords,page).success(function(data){
			  $scope.topicLoading = false;
			  if(data.code == 200){
				  $scope.topics = data.info.articals;
				  $scope.topicsCount = data.info.bigTotalItems;
				  $scope.bigTotalItemsTopic = data.info.bigTotalItems;
				  if ($scope.topics.length == 0) {
						$scope.topic_no_data = true;
				  }else{
					    $scope.topic_no_data = false;
				  }
			  }else{
				  $scope.topicLoading = false;
				  $scope.topic_no_data = true;
			  }
          }).error(function(data){
        	  $scope.topicLoading = false;
        	  $scope.topic_no_data = true;
          });
	  };
	  
	  $scope.wxList = function(keywords,page){
		  
		  Search.searchWxs(keywords==undefined?$scope.keywords:keywords,page).success(function(data){
			  $scope.weixinLoading = false;
			  if(data.code == 200){
        		  $scope.weixins  = data.info.wxlist;
        		  $scope.weixinsCount = data.info.bigTotalItems;
        		  $scope.bigTotalItemsWx = data.info.bigTotalItems;
        		  if ($scope.weixins.length == 0) {
						$scope.weixin_no_data = true;
				  }else{
					    $scope.weixin_no_data = false;
				  }
			  }else{
				  $scope.weixinLoading = false;
	        	  $scope.weixin_no_data = true;
			  }
          }).error(function(data){
        	  $scope.weixinLoading = false;
        	  $scope.weixin_no_data = true;
          });
	  }
	  
	  
	  $scope.search = function(keywords,page){
		  
		  $scope.keywords = keywords;
		  init();
		  
		  $scope.topicList(keywords,page);
		  
		  $scope.wxList(keywords,page);
          
		  
	   };
	   $scope.search($scope.searchKeywords,1);
	  
    }])
