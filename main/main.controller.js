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

            $scope.edMatchScore = 0;
            $scope.edMismatchScore = 1;

            $scope.scoreMatchScore = 1;
            $scope.scoreMismatchScore = -1;
            $scope.scoreDeleteScore = -2;
            $scope.scoreInsertScore = -2;


        }


        // onClick the Initialize button
        $scope.initialize = function () {

            console.log("initialize");

            // construct correct sized matrix
            $scope.matrix = new Array($scope.sequence1.length + 2);
            for (var i = 0; i < $scope.matrix.length; i++) {
                $scope.matrix[i] = new Array($scope.sequence2.length + 2);
                for (var j = 0; j < $scope.matrix[i].length; j++) {
                    $scope.matrix[i][j] = {
                        value: null,
                        type: 'score',
                        up: false,
                        left: false,
                        diag: false
                    };
                }
            }

            // Insert sequence
            // Insert zeroes initially
            var seq1 = 0,
                seq2 = 0;
            for (var i = 0; i < $scope.matrix.length; i++) {
                for (var j = 0; j < $scope.matrix[i].length; j++) {
                    if (i == 0 && j > 1) {
                        $scope.matrix[i][j].value = $scope.sequence2.charAt(seq2++);
                        $scope.matrix[i][j].type = 'seq';
                    } else if (i > 1 && j == 0) {
                        $scope.matrix[i][j].value = $scope.sequence1.charAt(seq1++);
                        $scope.matrix[i][j].type = 'seq';
                    }
                }
            }

            //disable top left corner
            $scope.matrix[0][0] = {
                value: '',
                type: 'blank'
            };
            $scope.matrix[0][1] = {
                value: '',
                type: 'blank'
            };
            $scope.matrix[1][0] = {
                value: '',
                type: 'blank'
            };

            console.log($scope.matrix);
            
            $scope.initSubmatrix();


        }
        
        
        //onClick the Check button
        $scope.check = function () {
            console.log("check");
        }
        
        // onClick the Run button
        $scope.runNW = function () {
            
            $scope.initialize();
            
            // Insert zeroes initially
            for (var i = 0; i < $scope.matrix.length; i++) {
                for (var j = 0; j < $scope.matrix[i].length; j++) {
                    if (i == 1 && j > 0) {
                        $scope.matrix[i][j].value = 0;
                        $scope.matrix[i][j].type = 'score';

                    } else if (i > 0 && j == 1) {
                        $scope.matrix[i][j].value = 0;
                        $scope.matrix[i][j].type = 'score';
                    }
                }
            }
            
            // change zeroes if alignment type is global
            switch ($scope.alignmentMethod) {

            case "local" || "dovetail" || "pattern":

                break;
            case "global":
                for (var i = 2; i < $scope.matrix.length; i++) {
                    $scope.matrix[i][1].value = $scope.matrix[i - 1][1].value + $scope.edMismatchScore;
                    //$scope.matrix[i][1].up = true;
                }

                for (var j = 2; j < $scope.matrix[2].length; j++) {
                    $scope.matrix[1][j].value = $scope.matrix[1][j - 1].value + $scope.edMismatchScore;
                    //$scope.matrix[1][j].left = true;
                }


                break;
            }            
            
            // determine what scoring method will be used
            var mismatch,match,insert,del;
            switch($scope.scoringMethod){
            
                case "editDistance":
                    mismatch = $scope.edMismatchScore;
                    insert = $scope.edMismatchScore;
                    del = $scope.edMismatchScore;
                    match = $scope.edMatchScore;
                    
                    break;
                
                case "score":
                    mismatch = $scope.scoreMismatchScore;
                    insert = $scope.scoreInsertScore;
                    del = $scope.scoreDeleteScore;
                    match = $scope.scoreMatchScore;
                    break;
                    
                case "blosum45":
                    
                    break;
                    
                // use Edit Distance is some error happens
                default:
                    mismatch = $scope.edMismatchScore;
                    insert = $scope.edMismatchScore;
                    del = $scope.edMismatchScore;
                    match = $scope.edMatchScore;
                    break;
            
            }
            
            console.log("run");
            
            // run Needleman-Wunsch with provided params
            $scope.nw(mismatch, match, insert, del, $scope.subMatrix, $scope.scoringMethod);
        }


        $scope.nw = function (mismatch, match, insert, del, matrix, scoring) {
            for(var i = 1; i < $scope.subMatrix.length; i++){
                for(var j = 1; j < $scope.subMatrix[i].length; j++){
                                        
                    var up = $scope.subMatrix[i-1][j].value + insert;
                    var left = $scope.subMatrix[i][j-1].value + del;
                    var diag = ($scope.sequence1.charAt(j-1) === $scope.sequence2.charAt(i-1) ? $scope.subMatrix[i-1][j-1].value + match : $scope.subMatrix[i-1][j-1].value + mismatch);
                    
                    console.log(up,left,diag);
                    
                    if(scoring === "editDistance"){
                        if(up <= left && up <= diag){
                            $scope.subMatrix[i][j].value = up;
                            $scope.subMatrix[i][j].up = true;
                        }
                        if(left <= up && left <= diag){
                            $scope.subMatrix[i][j].value = left;
                            $scope.subMatrix[i][j].left = true;
                        }
                        if(diag <= up && diag <= left){
                            $scope.subMatrix[i][j].value = diag;
                            $scope.subMatrix[i][j].diag = true;
                        }
                    }
                    // scoring is score or blossum
                    else{
                        if(up >= left && up >= diag){
                            $scope.subMatrix[i][j].value = up;
                            $scope.subMatrix[i][j].up = true;
                        }
                        if(left >= up && left >= diag){
                            $scope.subMatrix[i][j].value = left;
                            $scope.subMatrix[i][j].left = true;
                        }
                        if(diag >= up && diag >= left){
                            $scope.subMatrix[i][j].value = diag;
                            $scope.subMatrix[i][j].diag = true;
                        }
                    }
                    
                }
            }
            
            

        }
        
        // shallow copy out score part of full matrix for use in NW
        $scope.initSubmatrix = function(){
            // get just the score entries out of the full matrix
            $scope.subMatrix = $scope.matrix.slice(1, $scope.matrix.length);
            for (var i = 0; i < $scope.subMatrix.length; i++) {
                $scope.subMatrix[i] = $scope.subMatrix[i].slice(1, $scope.subMatrix[i].length);
            }
            console.log($scope.subMatrix);
        }

    }
})();