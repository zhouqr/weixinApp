
angular.module('weixin.wx.search',['weixin.wx.search.home','weixin.wx.search.detail'])
    .config(function($stateProvider){
        $stateProvider
            .state('wx.search',{
                url:'/search',
                templateUrl: appPath + '/wx/search/view.html',
                controller:function($scope,$stateParams,$state){
                	
                }
            })
    })
