(function() {
    'use strict';

    angular.module('busReader').factory('dictionaryFactory', function($resource) {
        return $resource('api/dictionary');
    });

})();
