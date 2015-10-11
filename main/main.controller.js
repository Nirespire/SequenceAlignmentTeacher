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
            
            $scope.edMatchScore = 1;
            $scope.edMismatchScore = -1;
            
            $scope.scoreMatchScore = 1;
            $scope.scoreMismatchScore = -1;
            $scope.scoreDeleteScore = -2;
            $scope.scoreInsertScore = -2;
            

        }


        $scope.initialize = function () {

            console.log("initialize");

            // construct correct sized matrix
            $scope.matrix = new Array($scope.sequence1.length + 2);
            for (var i = 0; i < $scope.matrix.length; i++) {
                $scope.matrix[i] = new Array($scope.sequence2.length + 2);
                for(var j = 0; j < $scope.matrix[i].length; j++){
                    $scope.matrix[i][j] = {value:'', type:'score', dir:'NA'};
                }
            }
            
            // Insert sequence
            // Insert zeroes initially
            var seq1 = 0, seq2 = 0;
            for (var i = 0; i < $scope.matrix.length; i++) {
                for(var j = 0; j < $scope.matrix[i].length; j++){
                    if(i==0 && j>1){
                        $scope.matrix[i][j].value = $scope.sequence2.charAt(seq2++);
                        $scope.matrix[i][j].type = 'seq';
                    }
                    else if(i > 1 && j == 0){
                        $scope.matrix[i][j].value = $scope.sequence1.charAt(seq1++);
                        $scope.matrix[i][j].type = 'seq';
                    }
                    else if(i==1 && j>0){
                        $scope.matrix[i][j].value = 0;
                        $scope.matrix[i][j].type = 'score';
                        
                    }
                    else if(i>0 && j == 1){
                        $scope.matrix[i][j].value = 0;
                        $scope.matrix[i][j].type = 'score';
                        
                    }
                }
            }
            
            //disable top left corner
            $scope.matrix[0][0] = {value:'', type:'blank'};
            $scope.matrix[0][1] = {value:'', type:'blank'};
            $scope.matrix[1][0] = {value:'', type:'blank'};
            

            

            switch ($scope.alignmentMethod) {

            case "local" || "dovetail" || "pattern":

                break;
            case "global":
                for(var i = 2; i <$scope.matrix.length; i++){
                    $scope.matrix[i][1].value = $scope.matrix[i-1][1].value + $scope.edMismatchScore;
                    $scope.matrix[i][1].dir = 'up';
                }
                    
                for (var j = 2; j < $scope.matrix[2].length; j++) {
                    $scope.matrix[1][j].value = $scope.matrix[1][j-1].value + $scope.edMismatchScore;
                    $scope.matrix[1][j].dir = 'left';
                }
                

                break;
            }
            
            //$scope.sequence2 = ' ' + $scope.sequence2;

            console.log($scope.matrix);
            
        }
        $scope.check = function () {
            console.log("check");
        }

        $scope.runNW = function () {
            console.log("run");
        }


        $scope.nw = function () {

        }

    }
})();