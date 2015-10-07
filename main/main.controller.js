(function () {
    'use strict';

    angular
        .module('seqalign.main')
        .controller('mainCtrl', main);

    function main($scope) {
        var vmMain = this;
        activate();

        function activate() {
        	console.log("vmMain here");
            
            // Default form values
            $scope.alignmentMethod = "global";
            $scope.scoringMethod = "editDistance";
            $scope.banded = false;
            
        }
        
        
        $scope.initialize = function(){
            console.log("initialize");
        }
        $scope.check = function(){
            console.log("check");
        }
        
        $scope.runNW = function(){
            console.log("run");
        }

    }
})();