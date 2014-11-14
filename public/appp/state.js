
angular.module('weixin',['weixin.wx','weixin.controllers','weixin.directives','weixin.filters','weixin.directives.user.userInfo'])
   .config(['$urlRouterProvider',function($urlRouterProvider){
        $urlRouterProvider.otherwise('/wx/topic/home');
    }]);