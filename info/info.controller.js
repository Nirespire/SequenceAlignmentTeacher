(function () {
    'use strict';

    angular
        .module('seqalign.info')
        .controller('infoCtrl', info);

    function info($scope) {
        var vmInfo = this;
        activate();

        function activate() {
        	console.log("vmInfohere");
            
        }

    }
})();