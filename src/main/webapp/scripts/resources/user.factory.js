(function() {
    'use strict';

    angular.module('busReader').factory('userListFactory', function($resource) {
        return $resource('api/user/list');
    });

    angular.module('busReader').factory('userDetailsFactory', function($resource) {
        return $resource('api/user/details');
    });

})();
