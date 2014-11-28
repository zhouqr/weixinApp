var list = null;
angular.module('weixin.wx.public.home',['services.public'])
    .config(function($stateProvider){
        $stateProvider
            .state('wx.public.home',{
                url:'/home',
                templateUrl: appPath + '/wx/public/home/view.html',
                controller:function($scope,$stateParams,$state){
                }
            })
    })
        
   //获取公众号列表
  .controller('PublicListCtrl', ['$scope','$http','$modal','Publics', function($scope,$http,$modal,Publics) {
	  $scope.imgDeg  = "public/img/topics/default.jpg";
	  
	  $scope.bigCurrentPage = 1;
      $scope.maxSize = 5;
      $scope.topics = [];
      
	  $scope.list =function(page){
		  $scope.topics = [];
		  $scope.topicLoading = true;
		  $scope.topic_no_data = false;
		  Publics.publicList(page,10)
		  .success(function(data, status) {
			 $scope.topicLoading = false;
			 if(data.code == 200){
				 $scope.topics = data.info.weixinPersons;
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
		 
      $scope.pageChanged = function() {
    	   $scope.list($scope.bigCurrentPage);
      };
	   
  }])
