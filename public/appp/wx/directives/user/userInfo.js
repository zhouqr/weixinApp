
angular.module('weixin.directives.user.userInfo',['services.users'])
    .directive('userInfo', function ($http, $q) {
        return {
            restrict: 'EA',
            templateUrl: appPath + '/wx/directives/user/userInfo.html',
            controller:function($scope,Users){
            	
            	function defaultInfo(){
            		Users.defaultuserInfo().success(function(data){
            			if(data.code == 200){
            				$scope.user = data.info;
            			}
            			else{
            				$scope.user = {id:'',username:"无用户"}
            			}
            		})
            	}
            	defaultInfo();
            	$scope.signup = function() {
            		Users.logout().success(function(data){
                  	  if(data.code == 401){
                  	  }
                  	  else if(data.code == 200){
                  		location.href = "/";
                  	  }
                    }, function(x) {
                        //$scope.authError = 'Server Error';
                    });
            	};
            },
            link: function (scope, element, attrs) {
            	
            }
        };
    })