(function() {
    'use strict';

    angular.module('busReader')
        .directive('smartTableButtons', function() {
            return {
                scope:       {
                    showFilters:      '=',
                    showVisibility:   '=',
                    applyFilters:     '=',
                    clearFilters:     '=',
                    toggleFilters:    '=',
                    toggleVisibility: "="
                },
                transclude:  true,
                restrict:    'E',
                templateUrl: 'scripts/ui/common/smartTable/buttons/smartTableButtons.html',
                link:        function(scope, elm) {


                }
            };
        });

})();
