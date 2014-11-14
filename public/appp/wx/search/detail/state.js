
angular.module('weixin.wx.search.detail',['services.search','weixin.wx.topic.search.directives.search'])
    .config(function($stateProvider){
        $stateProvider
            .state('wx.search.detail',{
                url:'/detail/{open_id}',
                templateUrl: appPath + '/wx/search/detail/view.html',
                controller:function($scope,$stateParams,$state){
                	console.log("");
                }
            })
    })
    .controller('WeiXinDetailCtrl', ['$scope','$http','$stateParams','Search', function($scope,$http,$stateParams,Search) {
    			$scope.imgDeg  = "public/img/topics/default.jpg";
    			$scope.topics = [];
    			$scope.list =function(){
    				$scope.topicLoading = true;
    				Search.searchWxDetail($stateParams.open_id).success(function(data){
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
    			  $scope.list();
        }])
