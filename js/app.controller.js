(function () {
    'use strict';

    angular
        .module('seqalign')
        .controller('mainCtrl', main);

    function main() {
        var vmMain = this;
        activate();

        function activate() {
        	console.log("vmMain here");
        }

    }
})();