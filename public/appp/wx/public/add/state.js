
angular.module('weixin.wx.public.add',['services.public'])
    .config(function($stateProvider){
        $stateProvider
            .state('wx.public.add',{
                url:'/add',
                templateUrl: appPath + '/wx/public/add/view.html',
                controller:function($scope,$stateParams,$state){
                }
            })
    })
    //添加公众号 预览弹出层
    .controller('ModalPublicCtrl', ['$scope', '$modalInstance', 'open_id','$http','Search',function($scope, $modalInstance, open_id,$http,Search) {
    	$scope.imgDeg  = "public/img/topics/default.jpg";
		$scope.topics = [];
    	$scope.search = function(){
    		$scope.topicLoading = true;
			Search.searchWxDetail(open_id).success(function(data){
				  if(data.code == 200){
					  $scope.topicLoading = false;
					  $scope.topics = data.info;
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
  	   $scope.search();
  	   $scope.cancel = function () {
  		   	$modalInstance.dismiss('cancel');
  	   };
  }])
    //添加公众号
  .controller('PublicAddCtrl', ['$scope','$http','$modal','Publics', function($scope,$http,$modal,Publics) {
	  $scope.wxinfo = {};
	  $scope.alertInfo = {};
	  function alertHelp(className,info){
		  $("#alertinfo").slideDown(); 
		  $scope.alertInfo ={
				alertinfoClass:className,
				info:info,
		  };
		  window.setTimeout(function(){$("#alertinfo").slideUp();},2000); //2秒钟自动关闭
	  };
	  $scope.check = function(){
		  Publics.publicCheck($scope.wxinfo.open_id)
		  .success(function(data){
	    	  if(data.code == 200){
	    		  if(data.info.weixin_id == ""){
	    			  alertHelp("alert-danger","没有找到此公众号ID...");
	    		  }
	    		  else{
	    			  alertHelp("alert-success","校验成功！");
	    			  $scope.wxinfo = {
	    					  open_id:data.info.open_id,
	    					  sourcename:data.info.sourcename,
	    					  headim:data.info.headim,
	    					  intro:data.info.intro,
	    			  }
	    		  }
	    	  }
          })
	  };
	  
	  $scope.addYes = function(){
		  Publics.publicAdd($scope.wxinfo)
		  .success(function(data){
			  if(data.code == 200){
				  alertHelp("alert-success","添加关注成功！");
			  }else if(data.code == 401){
				  alertHelp("alert-danger","该公众号已经关注！");
	    	  }
          })
	  }
	  $scope.reset = function(){
		  $scope.wxinfo = {};
	  };
	  
	  $scope.preview = function (size) {
	      var modalInstance = $modal.open({
	        templateUrl: 'modalPublicContent.html',
	        controller: 'ModalPublicCtrl',
	        size: size,
	        resolve: {
	        	open_id: function () {
	            return $scope.wxinfo.open_id;
	          },
	        }
	      });

	      modalInstance.result.then(function (selectedItem) {
	      }, function () {
	      });
	   };
	  
	  
	 
  }])
