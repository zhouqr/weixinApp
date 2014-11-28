
angular.module('weixin.wx.topic.edit',['services.topics'])
    .config(function($stateProvider){
        $stateProvider
            .state('wx.topic.edit',{
                url:'/edit/{id}',
                templateUrl: appPath + '/wx/topic/edit/view.html',
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
    //修改专题
  .controller('TopicEditCtrl', ['$scope','$http','$stateParams', '$modal','Topics',function($scope,$http,$stateParams,$modal,Topics) {
	  $scope.topic = {};
	  
	  function alertHelp(className,info){
  		  $("#alertinfo").slideDown(); 
  		  $scope.alertInfo ={
  				alertinfoClass:className,
  				info:info,
  		  };
  		  window.setTimeout(function(){$("#alertinfo").slideUp();},2000); //2秒钟自动关闭
	  };
	  
	  function defaultInfo(){
		  $http({method:'get',url:'/topic/topicDetail',params:{id:$stateParams.id}})
	    	.success(function(data){
	      	  if(data.code == 200){
	      		  $scope.topic = data.info;
	      	  }
	        }, function(x) {
	          //$scope.authError = 'Server Error';
	        });
  	  }
	  defaultInfo();
	  
	  
	  $scope.editYes = function(){
		  $scope.topic.id = $stateParams.id
		  
		  Topics.topicEdit($scope.topic)
		  .success(function(data){
		  		  if(data.code == 401){
		    		  alertHelp("alert-danger","专题名称重复！");
		    		  $scope.topic = {};
		    	  }
		    	  else if(data.code == 200){
		    		  alertHelp("alert-success","专题修改成功！");
		    		  $("#successinfo").slideDown();
		    	  }
		      }, function(x) {
		    }).error(function(data){
		    	  alertHelp("alert-danger","专题修改失败！");
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
