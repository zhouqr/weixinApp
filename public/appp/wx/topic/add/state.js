
angular.module('weixin.wx.topic.add',['services.search'])
    .config(function($stateProvider){
        $stateProvider
            .state('wx.topic.add',{
                url:'/add',
                templateUrl: appPath + '/wx/topic/add/view.html',
                controller:function($scope,$stateParams,$state){
                	console.log("");
                }
            })
    })
    
    .controller('ModalInstanceCtrl', ['$scope', '$modalInstance', 'keywords','$http','Search',function($scope, $modalInstance, keywords,$http,Search) {
	    
    	$scope.search = function(keywords){
  		  
  		  $scope.topicLoading = true;
  		  $scope.weixinLoading = true;
  		  Search.searchArtical(keywords).success(function(data){
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
            
  		  
  	   };
  	   $scope.search(keywords);
    	
	    $scope.cancel = function () {
	      $modalInstance.dismiss('cancel');
	    };
  }])
    //添加专题
  .controller('TopicAddCtrl', ['$scope','$http','$modal', function($scope,$http,$modal) {
	  $scope.topic = {};
	  
	  $scope.addYes = function(){
		  $scope.topic.startTime = $("#d4311").val();
		  $scope.topic.endTime = $("#d4312").val();
		  $http({method:'post',url:'/Topic/addTopic',params:$scope.topic})
		  	.success(function(data){
		    	  if(data.code == 401){
		    		  $("#errorinfo").slideDown();
					  window.setTimeout(function(){$("#errorinfo").slideUp();},2000); //2秒钟自动关闭
		    		  $scope.topic = {};
		    	  }
		    	  else if(data.code == 200){
		    		  $("#successinfo").slideDown();
					  window.setTimeout(function(){$(".alert-success").slideUp();},2000); //2秒钟自动关闭
		    	  }
		      }, function(x) {
		    }).error(function(data){
		    	$("#errorinfo").slideDown();
				  window.setTimeout(function(){$("#errorinfo").slideUp();},2000); //2秒钟自动关闭
	    		  $scope.topic = {};
		    });
	  };
	  $scope.reset = function(){
		  $scope.topic = {};
	  };
	  
	  
	  $scope.preview = function (size,keywords) {
	      var modalInstance = $modal.open({
	        templateUrl: 'previewContent.html',
	        controller: 'ModalInstanceCtrl',
	        size: size,
	        resolve: {
	          keywords: function () {
	            return keywords;
	          },
	        }
	      });

	      modalInstance.result.then(function (selectedItem) {
	      }, function () {
	      });
	   };
	  
	 
  }])
