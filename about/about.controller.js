(function () {
    'use strict';

    angular
        .module('seqalign.about')
        .controller('aboutCtrl', about);

    function about($scope) {
        var vmAbout = this;
        activate();

        function activate() {
        	console.log("vmAbout here");
            
        }

    }
})();