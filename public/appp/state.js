
angular.module('weixin',['weixin.wx','weixin.controllers','weixin.directives','weixin.filters',])
   .config(['$urlRouterProvider',function($urlRouterProvider){
        $urlRouterProvider.otherwise('/wx/topic/home');
    }]);