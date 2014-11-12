

angular.module('app', ['ngAnimate',
                       'ngCookies',
                       'ngStorage',
                       'ui.router',
                       'ui.bootstrap',
                       'ui.load',
                       'ui.jq',
                       'ui.validate',
                       'oc.lazyLoad',
                       'pascalprecht.translate',
                       'weixin'])
    .run(
  [          '$rootScope', '$state', '$stateParams',
    function ($rootScope,   $state,   $stateParams) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;        
    }
  ]
);