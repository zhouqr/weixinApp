var list = null;
angular.module('weixin.wx.topic.home',['weixin.wx.topic.home.directives.list'])
    .config(function($stateProvider){
        $stateProvider
            .state('wx.topic.home',{
                url:'/home',
                templateUrl: appPath + '/wx/topic/home/view.html',
                controller:function($scope,$stateParams,$state){
                	console.log("");
                }
            })
    })
    .controller('InstanceCtrl', ['$scope', '$modalInstance', 'id','$http',function($scope, $modalInstance, id,$http) {
	    $scope.ok = function () {
	    	$http({
				  method: 'GET',
				  url: 'Topic/delTopic',
				  params:{
					  id:id
				  }
			  }).success(function(data, status) {
				 if(data.code == 200){
					 $modalInstance.close();
				     list();
				 }
			 });
	    };
	
	    $scope.cancel = function () {
	      $modalInstance.dismiss('cancel');
	    };
  }])
    
   //获取专题列表
  .controller('TopicListCtrl', ['$scope','$http','$modal', function($scope,$http,$modal) {
	  $scope.imgDeg  = "public/img/topics/yantai.jpg";
	  list =function(){
		  $http({
			  method: 'GET',
			  url: 'topic/topicList',
			  params:{
			  }
		  }).success(function(data, status) {
			 if(data.code == 200){
				 $scope.topics = data.info;
				 angular.forEach($scope.topics, function(n,i) {
					 if(i>7){
						 n.image = "public/img/topics/yantai.jpg";
					 }
					 else{
						 n.image = "public/img/topic_img/0"+n.id+".jpg";
					 }
	       		 });
			 }
		  });
	  };
	  list();
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
