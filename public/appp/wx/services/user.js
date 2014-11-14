
angular.module('services.users', [])
    .factory('Users',function($http){
        return {
            defaultuserInfo:function(){
                return $http({method:'get',url:'/LoginService/currentUser'})
            },
            login:function(username,password) {
                return $http({method:'post',url:'/auth/login',data:{
                    username:username,
                    password:password
                }})
            },
            logout:function(){
                return $http({method: 'get', url: '/LoginService/logout '});
            }
        }
    });

    