(function () {
    'use strict';

    angular.module('seqalign', [
        'ui.router',
        'seqalign.main',
        'seqalign.info',
        'ui.bootstrap',
        'seqalign.modal',
        'seqalign.about'
    ]);

    angular.module('seqalign')
        .factory('$', ['$window',
        function ($window) {
                return $window.jQuery;
        }
    ]);

})();