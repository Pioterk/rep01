(function() {
    'use strict';

    angular.module('busReader').config(function($stateProvider) {
        $stateProvider.state("login", {
            parent:  'publicSite',
            url:     "/login",
            views:   {
                'content@': {
                    templateUrl: 'scripts/ui/content/login/login.html',
                    controller:  'LoginCtrl'
                }
            },
            resolve: {}
        });
    });

})();