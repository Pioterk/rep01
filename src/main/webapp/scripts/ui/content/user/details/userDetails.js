(function() {
    'use strict';

    angular.module('busReader').config(function($stateProvider) {
        $stateProvider.state("userDetails", {
            parent:  'restrictedSite',
            url:     "/user/details/:state",
            views:   {
                'content@': {
                    templateUrl: 'scripts/ui/content/user/details/userDetails.html',
                    controller:  'UserDetailsCtrl'
                }
            },
            resolve: {}
        });
    });

})();