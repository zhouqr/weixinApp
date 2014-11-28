
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
    			$scope.wxinfo = {};
    			
    			/*$("#d4311").val();
    			console.log(new Date().Format("yyyy-MM-dd hh:mm:ss"));
    			$("#d4312").val("2014-11-28 16:11:43");*/
    			var today = new Date();
    			$scope.endTime = today.Format("yyyy-MM-dd hh:mm:ss");
    			$scope.startTime =new Date(today.getTime()-1*1000*60*60*24*30).Format("yyyy-MM-dd hh:mm:ss");
    			
    			$scope.bigTotalItems = 0;
    			$scope.bigCurrentPage = 1;
			    $scope.maxSize = 5;
			    
			    $scope.isSearch = false;
			    function init(){
					  $scope.topicLoading = true;
					  $scope.topics = [];
					  $scope.topic_no_data = false;
			    }
			    
    			$scope.list =function(page){
    				init();
    				Search.searchWxDetail($stateParams.open_id,page).success(function(data){
    					  $scope.topicLoading = false;
    					  if(data.code == 200){
    						  $scope.topics = data.info.articals;
    						  $scope.bigTotalItems = data.info.bigTotalItems;
    						  $scope.wxinfo = {
    								  open_id : data.info.open_id,
    								  sourcename : data.info.sourcename,
    								  headim :data.info.headim,
    								  intro :data.info.intro,
    								  weixin_id :data.info.weixin_id,
    								  verti :data.info.verti,
    								  isAttr :data.info.isAttr,
    						  };
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
			    	  if(!$scope.isSearch){
			    		  $scope.list($scope.bigCurrentPage);
			    	  }
			    	  else{
			    		  $scope.search($scope.bigCurrentPage);
			    	  }
			    	 
			      };
    			  
    			  $scope.isAttrWx = function(){
    				  if($scope.wxinfo.isAttr){
    					  $http({method:'get',url:'/AttrWeixin/cancelAttr',params:$scope.wxinfo}).success(function(data){
        		        	  console.log(data)
        		          })
    				  }else{
    					  $http({method:'get',url:'/AttrWeixin/attr',params:$scope.wxinfo}).success(function(data){
        		        	  console.log(data)
        		          })
    				  }
    			  }
    			  $scope.toggleDropdown = function($event) {
    			      $event.preventDefault();
    			      $event.stopPropagation();
    			      //$scope.status.isopen = !$scope.status.isopen;
    			  };
    			  $(document).keydown(function (event) {
                      if (event.keyCode == 13) {
                          $("#search").click();
                          return false;
                      }
                  });
    			  $scope.search = function(page){
    				  if($("#d4311").val()==""||$("#d4312").val()==""){
    					  alert("检索条件不能为空！");
    					  return;
    				  }
    				  $scope.topics = [];
      				  $scope.topicLoading = true;
      				  $scope.topic_no_data = false;
      				  $scope.isSearch = true;
      				  console.log(page == undefined||page ==null);
      				  $scope.bigCurrentPage = page == undefined||page ==null ? 1: page
      				  var params = {
      						keywords:$scope.keywords,
      						open_id:$scope.wxinfo.open_id,
      						st:$("#d4311").val(),
      						et:$("#d4312").val(),
      						page:$scope.bigCurrentPage,
      				  }
      				  
      				  Search.searchWxDetailByOpenid(params).success(function(data){
	  					  $scope.topicLoading = false;
	  					  if(data.code == 200){
	  						  $scope.topics = data.info.articals;
	  						  $scope.bigTotalItems = data.info.bigTotalItems;
	  						  if ($scope.topics.length == 0) {
	  								$scope.topic_no_data = true;
	  						  }else{
	  							    $scope.topic_no_data = false;
	  						  }
	  					  }
	  					  else{
	  						  $scope.topicLoading = false;
		  		        	  $scope.topic_no_data = true;
	  					  }
	  		          }).error(function(data){
	  		        	  $scope.topicLoading = false;
	  		        	  $scope.topic_no_data = true;
	  		          });
      				  
      				  
    			  }
        }])
