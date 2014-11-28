
angular.module('weixin.wx',['weixin.wx.topic','weixin.wx.search','weixin.wx.public'])
    .config(function ($stateProvider){
        $stateProvider
            .state('wx',{
            	abstract:true,
                url:'/wx',
                templateUrl: appPath + '/wx/view.html',
            })
        })
    
    
    



