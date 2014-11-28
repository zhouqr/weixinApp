
angular.module('weixin.wx.public',['weixin.wx.public.home','weixin.wx.public.add'])
    .config(function($stateProvider){
        $stateProvider
            .state('wx.public',{
                url:'/public',
                templateUrl: appPath + '/wx/public/view.html',
                controller:function($scope,$stateParams,$state){
                }
            })
    })
