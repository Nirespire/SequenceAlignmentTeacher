(function () {
    'use strict';

    angular.module('seqalign', [
        'ui.router',
        'seqalign.main',
        'seqalign.about',
        'ui.bootstrap',
        'seqalign.modal'
    ]);

    angular.module('seqalign')
        .factory('$', ['$window',
        function ($window) {
                return $window.jQuery;
        }
    ]);

})();