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
            }
            
            // Fill in initial zeroes
            for (var i = 0; i < $scope.matrix.length; i++) {
                if (i == 0) {
                    for (var j = 0; j < $scope.matrix[i].length; j++) {
                        $scope.matrix[i][j] = 0;
                    }
                } else {
                    $scope.matrix[i][0] = 0;
                }
            }

            switch ($scope.alignmentMethod) {

            case "local" || "dovetail" || "pattern":

                break;
            case "global":
                for(var i = 1; i <$scope.matrix.length; i++){
                    $scope.matrix[i][0] = $scope.matrix[i-1][0] + $scope.edMismatchScore;
                }
                    
                for (var j = 1; j < $scope.matrix[0].length; j++) {
                    $scope.matrix[0][j] = $scope.matrix[0][j-1] + $scope.edMismatchScore;
                }
                

                break;
            }

            console.log($scope.matrix);

            createTable($scope.matrix);
        }
        $scope.check = function () {
            console.log("check");
        }

        $scope.runNW = function () {
            console.log("run");
        }


        $scope.nw = function () {

        }



        function createTable(tableData) {
            var table = document.createElement('table'),
                tableBody = document.createElement('tbody');

            table.className = "wikitable";
            table.id = "matrix";

            tableData.forEach(function (rowData) {
                var row = document.createElement('tr');

                rowData.forEach(function (cellData) {
                    var cell = document.createElement('td');
                    cell.appendChild(document.createTextNode((cellData == null) ? " " : cellData));
                    row.appendChild(cell);
                });

                tableBody.appendChild(row);
            });
            
            table.appendChild(tableBody);
            
            $('table.wikitable#matrix').replaceWith(table);
            
            console.log(table);
        }

    }
})();