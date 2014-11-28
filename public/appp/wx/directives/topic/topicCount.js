
angular.module('weixin.directives.topic.topicCount',['services.topics'])
    .directive('topicCount', function ($http, $q) {
        return {
            restrict: 'EA',
            templateUrl: appPath + '/wx/directives/topic/topicCount.html',
            controller:function($scope,Topics){
            	function defaultInfo(){
            		Topics.topicCount().success(function(data){
            			if(data.code == 200){
            				$scope.topicCount = data.info;
            			}
            		})
            	}
            	defaultInfo();
            },
            link: function (scope, element, attrs) {
            	
            }
        };
    })