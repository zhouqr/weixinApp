
angular.module('weixin.wx.search.detail',[])
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
    .controller('WeiXinDetailCtrl', ['$scope','$http','$stateParams', function($scope,$http,$stateParams) {
    			$scope.imgDeg  = "public/img/topics/fang.jpg";
    			$scope.list =function(){
    				  $http({
    					  method: 'GET',
    					  url: 'search/get_wx_openid',
    					  params:{
    						  open_id : $stateParams.open_id
    					  }
    				  }).success(function(data, status) {
    					 if(data.code == 200){
    						 $scope.topics = data.info;
    					 }
    				  });
    			  };
    			  $scope.list();
        }])
