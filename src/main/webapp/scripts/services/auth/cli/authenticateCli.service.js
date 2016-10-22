(function() {
    'use strict';

    angular.module('busService').factory('AuthenticateCli', function($resource) {
        return $resource('api/authenticate', {}, {
            'login': {method: 'POST'}
        });
    });

})();
