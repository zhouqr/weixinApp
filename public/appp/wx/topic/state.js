
angular.module('weixin.wx.topic',['weixin.wx.topic.home','weixin.wx.topic.add','weixin.wx.topic.detail'])
    .config(function($stateProvider){
        $stateProvider
            .state('wx.topic',{
                url:'/topic',
                templateUrl: appPath + '/wx/topic/view.html',
                controller:function($scope,$stateParams,$state){
                	console.log("");
                }
            })
    })
