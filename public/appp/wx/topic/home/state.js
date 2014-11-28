var list = null;
angular.module('weixin.wx.topic.home',['weixin.wx.topic.home.directives.list','services.topics'])
    .config(function($stateProvider){
        $stateProvider
            .state('wx.topic.home',{
                url:'/home',
                templateUrl: appPath + '/wx/topic/home/view.html',
                controller:function($scope,$stateParams,$state){
                }
            })
    })
    .controller('InstanceCtrl', ['$scope', '$modalInstance', 'id','$http','Topics',function($scope, $modalInstance, id,$http,Topics) {
	    $scope.ok = function () {
	    	Topics.topicDel(id).success(function(data, status) {
				 if(data.code == 200){
					 $modalInstance.close();
				     list(1);
				 }
			 });
	    };
	    $scope.cancel = function () {
	      $modalInstance.dismiss('cancel');
	    };
  }])
    
   //获取专题列表
  .controller('TopicListCtrl', ['$scope','$http','$modal','Topics', function($scope,$http,$modal,Topics) {
	  $scope.imgDeg  = "public/img/topics/yantai.jpg";
	  
	  $scope.bigTotalItems = 0;
	  $scope.bigCurrentPage = 1;
	  $scope.maxSize = 5;
	  
	  list =function(page){
		  Topics.topicList(page,5).success(function(data, status) {
			 if(data.code == 200){
				 $scope.topics = data.info.topics;
				 $scope.bigTotalItems = data.info.bigTotalItems;
			 }
		  });
	  };
	  list(1);
	  
	  $scope.pageChanged = function() {
		  list($scope.bigCurrentPage);
	  };
	  
	  $scope.open = function (size,id) {
	      var modalInstance = $modal.open({
	        templateUrl: 'myModalContent.html',
	        controller: 'InstanceCtrl',
	        size: size,
	        resolve: {
	          id: function () {
	            return id;
	          },
	        }
	      });

	      modalInstance.result.then(function (selectedItem) {
	      }, function () {
	      });
	   };
	   
  }])
