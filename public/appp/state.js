
angular.module('weixin',['weixin.wx',
                         'weixin.controllers',
                         'weixin.directives',
                         'weixin.filters',
                         'weixin.directives.user.userInfo',
                         'weixin.directives.topic.topicCount',
                         'weixin.directives.topic.chart'])
   .config(['$urlRouterProvider',function($urlRouterProvider){
        $urlRouterProvider.otherwise('/wx/topic/home');
    }]);