(function() {
    'use strict';

    angular.module('busReader').config(function($stateProvider) {
        $stateProvider.state("recoverPassword", {
            parent:  'publicSite',
            url:     "/recoverPassword/:userId/:token",
            views:   {
                'content@': {
                    templateUrl: 'scripts/ui/content/recoverPassword/recoverPassword.html',
                    controller:  'RecoverPasswordCtrl'
                }
            },
            resolve: {}
        });
    });

})();