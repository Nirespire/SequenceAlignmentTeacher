(function () {
    'use strict';

    angular
        .module('seqalign')
        .config(['$stateProvider', '$urlRouterProvider', config])
        .run(function ($state, $rootScope) {
            $rootScope.$state = $state;
        });

    function config($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise('/home');
        $stateProvider
            .state('home', {
                url: '/home',
                templateUrl: 'main/main.html'
            })

            .state('info', {
                url: '/info',
                templateUrl: 'info/info.html'
            })
            .state('about', {
                url: '/about',
                templateUrl: 'about/about.html'
            });
    }
})();