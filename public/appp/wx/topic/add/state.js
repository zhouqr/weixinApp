
angular.module('weixin.wx.topic.add',[])
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
    //添加专题
  .controller('TopicAddCtrl', ['$scope','$http', function($scope,$http) {
	  $scope.topic = {};
	  
	  $scope.addYes = function(){
		  $http({method:'get',url:'/Topic/addTopic',params:$scope.topic})
		  	.success(function(data){
		    	  if(data.code == 401){
		    		  $("#errorinfo").slideDown();
					  window.setTimeout(function(){$("#errorinfo").slideUp();},2000); //2秒钟自动关闭
		    		  $scope.topic = {};
		    	  }
		    	  else if(data.code == 200){
		    		  $("#successinfo").slideDown();
					  window.setTimeout(function(){$(".alert-success").slideUp();},2000); //2秒钟自动关闭
		    		  $scope.topic = {};
		    	  }
		      }, function(x) {
		    }).error(function(data){
		    	$("#errorinfo").slideDown();
				  window.setTimeout(function(){$("#errorinfo").slideUp();},2000); //2秒钟自动关闭
	    		  $scope.topic = {};
		    });
	  }
	  
	 
  }])
