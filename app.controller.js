(function () {
    'use strict';

    angular
        .module('seqalign')
        .controller('appCtrl', main);

    function main($scope) {
        var vmApp = this;
        activate();

        function activate() {
        	console.log("vmApp here");
            
        }

    }
})();