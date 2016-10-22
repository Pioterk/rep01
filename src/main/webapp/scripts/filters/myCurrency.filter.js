(function() {
    'use strict';

    angular.module('busReader').filter('myCurrency', function() {
        return function(input, currency) {
            return input + " " + currency;
        };
    });

})();