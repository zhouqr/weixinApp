
angular.module('weixin.wx.topic.search.directives.search',[])
    .directive('search', function ($http, $q) {
        return {
            restrict: 'EA',
            templateUrl: appPath + '/wx/search/directives/search.html',
            controller:function($scope,$http){
            	$(document).keydown(function (event) {
                    if (event.keyCode == 13) {
                        $("#search").click();
                        return false;
                    }
                });
            },
            link: function (scope, element, attrs) {
            	scope.header = attrs.header;
            }
        };
    })