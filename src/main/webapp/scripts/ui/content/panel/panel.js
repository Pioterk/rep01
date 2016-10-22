(function() {
    'use strict';

    angular.module('busReader').config(function($stateProvider) {
        $stateProvider.state("panel", {
            parent:  'restrictedSite',
            url:     "/panel",
            views:   {
                'content@': {
                    templateUrl: 'scripts/ui/content/panel/panel.html'
                }
            },
            resolve: {}
        });
    });

})();