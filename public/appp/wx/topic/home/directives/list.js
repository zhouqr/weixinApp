
angular.module('weixin.wx.topic.home.directives.list',[])
    .directive('list', function ($http, $q) {
        return {
            restrict: 'EA',
            templateUrl: appPath + '/wx/topic/home/directives/list.html',
            scope: {
                id: '=',
            },
            controller:function($scope,$http){
            	 $scope.loading = "public/img/loading.gif";
            	 $scope.topDocs = [];
            	 var List =function(){
             		  $http({
             			  method: 'GET',
             			  url: 'topic/getTopicDocsTop5',
             			  params:{
             				  id:$scope.id,
             			  }
             		  }).success(function(data, status) {
             			 if(data.code == 200){
             				 $scope.topDocs = data.info;
             			 }
             		  }).error(function(){
             			 $scope.topDocs = [];
             		  });
             	  };
             	  List();
            },
            link: function (scope, element, attrs) {
                var defaultConfig = {
                    pagesize: 20,
                    getCount: function () {
                        if (!attrs.counturl)return 0;
                        return $http.get(attrs.counturl);
                    },
                    current: 1
                };
                
               

            }
        };
    })