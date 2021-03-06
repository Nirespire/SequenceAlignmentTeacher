(function () {
    'use strict';

    angular
        .module('seqalign.main')
        .controller('mainCtrl', main);

    function main($scope, $, $uibModal) {
        var vmMain = this;

        $scope.animationsEnabled = true;

        $scope.$watch("alignmentMethod", function (newValue, oldValue) {
            if ($scope.alignmentMethod !== 'global' && $scope.scoringMethod === 'editDistance') {
                $scope.scoringMethod = 'score';
            }
        });

        $scope.open = function (subject) {
            var templateUrl;
            console.log(subject);

            switch (subject) {
            case "alignmentMethod":
                templateUrl = 'modal/aboutAlignmentMethod.html?bust=' + Math.random().toString(36).slice(2);
                break;

            case "scoringMethod":
                templateUrl = 'modal/aboutScoringMethod.html?bust=' + Math.random().toString(36).slice(2);
                break;

            case "blosum":
                templateUrl = 'modal/aboutBlosum.html?bust=' + Math.random().toString(36).slice(2);
                break;

            case "matrix":
                templateUrl = 'modal/aboutAlignmentMatrix.html?bust=' + Math.random().toString(36).slice(2);
                break;

            case "alignmentResult":
                templateUrl = 'modal/aboutAlignment.html?bust=' + Math.random().toString(36).slice(2);
                break;
            case "sequences":
                templateUrl = 'modal/aboutSequences.html?bust=' + Math.random().toString(36).slice(2);
                break;
            case "bandedAlignment":
                templateUrl = 'modal/aboutBandedAlignment.html?bust=' + Math.random().toString(36).slice(2);
                break;
            case "start":
                templateUrl = 'modal/start.html?bust=' + Math.random().toString(36).slice(2);
                break;
            }
            
            



            var modalInstance = $uibModal.open({
                animation: $scope.animationsEnabled,
                templateUrl: templateUrl,
                controller: 'modalCtrl'
            });

            modalInstance.result.then(function (selectedItem) {

            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        $scope.close = function () {
            $uibModal.close();
        }


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
            $scope.scoreInDelScore = -2;

            $scope.blossum45MismatchScore = -5;
            $scope.blossum62MismatchScore = -4;

            $scope.k = 3;

            $scope.Math = window.Math;

            $scope.showArrows = true;
            $scope.showPopups = true;

            $scope.solved = false;

            // Initialize popover info boxes over question mark icons
            $('#alignmentPopover').popover({
                trigger: "hover",
                html: true,
                content: "The method by which both sequences will be aligned. Click to learn more."
            });

            $('#scoringPopover').popover({
                trigger: "hover",
                html: true,
                content: "How the quality of the alignment will be evaluated. Click to learn more."
            });

            $('#blosumPopover').popover({
                trigger: "hover",
                html: true,
                content: "Click here to learn more about he BLOSUM family of scoring matrices and how they are created."
            });

            $('#matrixPopover').popover({
                trigger: "hover",
                html: true,
                content: "Click here to learn more about Needleman-Wunsch Dynamic Programming matrices."
            });

            $('#alignmentResultPopover').popover({
                trigger: "hover",
                html: true,
                content: "Click here to learn more about sequence alignment results."
            });

            $('#sequencesPopover').popover({
                trigger: "hover",
                html: true,
                content: "Click here to learn more about the input sequences"
            });

            $('#bandedAlignmentPopover').popover({
                trigger: "hover",
                html: true,
                content: "Click here to learn more about banded alignment"
            });


            // Prevent inputs of characters that are not amino acids
            var validateInput = function (e) {
                // Char codes for all valid amino chars
                if ($.inArray(e.keyCode, [65, 67, 68, 69, 70, 71, 72, 73, 75, 76, 77, 78, 80, 81, 82, 83, 84, 86, 87, 77, 89]) !== -1 ||
                    $.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 35, 36, 37, 39]) !== -1) {
                    // let it happen, don't do anything
                    return;
                } else {
                    e.preventDefault();
                }
            }


            $("#sequence1").keydown(validateInput);
            $("#sequence2").keydown(validateInput);

        }

        // Restrict inputs to only be valid bases
        $scope.aminoRegex = function (event) {
            var charCode = event.keyCode;
            if ($scope.aminoCharCodes.indexOf(charCode) != -1) {
                return true;
            } else {
                return false;
            }
        };

        // Dynamically creates a tooltip for each matrix entry
        $scope.tooltipContent = function (i, j) {
            var content = "";

            if ($scope.matrix[i][j].type === 'score' && i > 1 && j > 1) {
                
                if($scope.lastAlignmentMethod === 'local'){
                    content += "Start Over = 0 | ";
                }

                if ($scope.lastScoringMethod === 'editDistance') {
                    content += "Above : " + $scope.matrix[i - 1][j].value + " + " + $scope.lastMismatch + " = " + ($scope.matrix[i - 1][j].value + $scope.lastMismatch) + " | ";
                    content += "Left : " + $scope.matrix[i][j - 1].value + " + " + $scope.lastMismatch + " = " + ($scope.matrix[i][j - 1].value + $scope.lastMismatch) + " | ";
                    content += "Diagonal : " + $scope.matrix[i - 1][j - 1].value + " + ";

                    if ($scope.sequence1.charAt(i - 2) === $scope.sequence2.charAt(j - 2)) {
                        content += "0 = " + $scope.matrix[i - 1][j - 1].value + " because match";
                    } else {
                        content += $scope.lastMismatch + " = " + ($scope.matrix[i - 1][j - 1].value + $scope.lastMismatch) + " because mismatch";
                    }
                } 
                else if($scope.lastScoringMethod === 'blosum45'){
                    content += "Above : " + $scope.matrix[i - 1][j].value + " + " + $scope.lastIndel + " = " + ($scope.matrix[i - 1][j].value + $scope.lastIndel) + " | ";
                    content += "Left : " + $scope.matrix[i][j - 1].value + " + " + $scope.lastIndel + " = " + ($scope.matrix[i][j-1].value + $scope.lastIndel) + " | ";
                    content += "Diagonal : " + $scope.matrix[i - 1][j - 1].value + " + ";
                    
                    var temp = $scope.blosum45($scope.sequence1.charAt(i - 2), $scope.sequence2.charAt(j - 2));
                    
                    if ($scope.sequence1.charAt(i - 2) === $scope.sequence2.charAt(j - 2)) {
                        content += temp + " = " + ($scope.matrix[i - 1][j - 1].value + temp) + " because match";
                    } else {
                        content += temp + " = " + ($scope.matrix[i - 1][j - 1].value + temp) + " because mismatch";
                    }
                }
                else if($scope.lastScoringMethod === 'blosum62'){
                    content += "Above : " + $scope.matrix[i - 1][j].value + " + " + $scope.lastIndel + " = " + ($scope.matrix[i - 1][j].value + $scope.lastIndel) + " | ";
                    content += "Left : " + $scope.matrix[i][j - 1].value + " + " + $scope.lastIndel + " = " + ($scope.matrix[i][j-1].value + $scope.lastIndel) + " | ";
                    content += "Diagonal : " + $scope.matrix[i - 1][j - 1].value + " + ";
                    
                    var temp = $scope.blosum62($scope.sequence1.charAt(i - 2), $scope.sequence2.charAt(j - 2));
                    
                    if ($scope.sequence1.charAt(i - 2) === $scope.sequence2.charAt(j - 2)) {
                        content += temp + " = " + ($scope.matrix[i - 1][j - 1].value + temp) + " because match";
                    } else {
                        content += temp + " = " + ($scope.matrix[i - 1][j - 1].value + temp) + " because mismatch";
                    }
                }
                else {

                    content += "Above : " + $scope.matrix[i - 1][j].value + " + " + $scope.lastIndel + " = " + ($scope.matrix[i - 1][j].value + $scope.lastIndel) + " | ";
                    content += "Left : " + $scope.matrix[i][j - 1].value + " + " + $scope.lastIndel + " = " + ($scope.matrix[i][j-1].value + $scope.lastIndel) + " | ";
                    content += "Diagonal : " + $scope.matrix[i - 1][j - 1].value + " + ";

                    if ($scope.sequence1.charAt(i - 2) === $scope.sequence2.charAt(j - 2)) {
                        content += $scope.lastMatch + " = " + ($scope.matrix[i - 1][j - 1].value + $scope.lastMatch) + " because match";
                    } else {
                        content += $scope.lastMismatch + " = " + ($scope.matrix[i - 1][j - 1].value + $scope.lastMismatch) + " because mismatch";
                    }
                }
            } else if ($scope.matrix[i][j].type === 'score' && i == 1 && j > 1) {
                if($scope.lastAlignmentMethod !== 'global'){
                    content += "0 because not global alignment";
                }
                else if ($scope.lastScoringMethod === 'editDistance') {
                    content += "Left : " + $scope.matrix[i][j - 1].value + " + " + $scope.lastMismatch + " = " + ($scope.matrix[i][j - 1].value + $scope.lastMismatch) + " because delete";
                } else {
                    content += "Left : " + $scope.matrix[i][j - 1].value + " + " + $scope.lastIndel + " = " + ($scope.matrix[i][j-1].value + $scope.lastIndel) + " because delete";
                }
            } else if ($scope.matrix[i][j].type === 'score' && i > 1 && j == 1) {
                if($scope.lastAlignmentMethod !== 'global'){
                    content += "0 because not global alignment";
                }
                else if ($scope.lastScoringMethod === 'editDistance') {
                    content += "Above : " + $scope.matrix[i - 1][j].value + " + " + $scope.lastMismatch + " = " + ($scope.matrix[i - 1][j].value + $scope.lastMismatch) + " because insert";
                } else {
                    content += "Above : " + $scope.matrix[i - 1][j].value + " + " + $scope.lastIndel + " = " + ($scope.matrix[i - 1][j].value + $scope.lastIndel) + " because insert";
                }
            }

            return content;
        }


        // onClick the Initialize button
        $scope.initialize = function () {
            console.log("initialize");

            $scope.solved = false;

            // Clear any previous info
            $scope.matrix = null;
            $scope.alignment = [[], [], []];

            // Make sure inputs are provided
            if ($scope.sequence1 === undefined || $scope.sequence1.length == 0) {

                $("#sequence1").tooltip({

                    // place tooltip on the right edge
                    position: "center right",

                    // a little tweaking of the position
                    offset: [-2, 10],

                    // use the built-in fadeIn/fadeOut effect
                    effect: "fade",

                    // custom opacity setting
                    opacity: 0.7

                });

                $("#sequence1").tooltip("enable");
                $("#sequence1").focus();

                return;
            }

            $("#sequence1").tooltip('disable');

            if ($scope.sequence2 === undefined || $scope.sequence2.length == 0) {

                $("#sequence2").tooltip({

                    // place tooltip on the right edge
                    position: "center right",

                    // a little tweaking of the position
                    offset: [-2, 10],

                    // use the built-in fadeIn/fadeOut effect
                    effect: "fade",

                    // custom opacity setting
                    opacity: 0.7

                });

                $("#sequence2").tooltip("enable");
                $("#sequence2").focus();

                return;
            }

            $("#sequence2").tooltip('disable');

            $scope.sequence1 = $scope.sequence1.toUpperCase().trim();
            $scope.sequence2 = $scope.sequence2.toUpperCase().trim();


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
                        diag: false,
                        color: 'white'
                    };
                }
            }

            // Insert sequence
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

            // Disable top left corner
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

        // onClick the Run button
        $scope.initNW = function () {

            $scope.initialize();

            // Insert zeroes initially
            for (var i = 1; i < $scope.matrix.length; i++) {
                for (var j = 1; j < $scope.matrix[i].length; j++) {

                    if ($scope.banded && (i > j + $scope.k || j > i + $scope.k)) {
                        $scope.matrix[i][j].type = 'blank';
                        continue;
                    }

                    if (i == 1 && j > 0) {
                        $scope.matrix[i][j].value = 0;
                        $scope.matrix[i][j].type = 'score';

                    } else if (i > 0 && j == 1) {
                        $scope.matrix[i][j].value = 0;
                        $scope.matrix[i][j].type = 'score';
                    }
                }
            }


            // If it's global alignment, need to modify first col and row for del and inserts
            if ($scope.alignmentMethod === "global") {

                var mismatchScore;

                switch ($scope.scoringMethod) {

                case "editDistance":
                    mismatchScore = $scope.edMismatchScore;
                    break;

                case "score":
                    mismatchScore = $scope.scoreInDelScore;
                    break;

                case "blosum45":
                    mismatchScore = $scope.blossum45MismatchScore;
                    break;

                case "blosum62":
                    mismatchScore = $scope.blossum62MismatchScore;
                    break;

                }

                // vertical first column
                for (var i = 2; i < $scope.matrix.length; i++) {
                    $scope.matrix[i][1].value = $scope.matrix[i - 1][1].value + mismatchScore;
                }

                // horizontal first column
                for (var j = 2; j < $scope.matrix[2].length; j++) {
                    $scope.matrix[1][j].value = $scope.matrix[1][j - 1].value + mismatchScore;
                }
            }
            //            else if ($scope.alignmentMethod === "pattern") {
            //
            //                var mismatchScore;
            //
            //                switch ($scope.scoringMethod) {
            //
            //                case "editDistance":
            //                    mismatchScore = $scope.edMismatchScore;
            //                    break;
            //
            //                case "score":
            //                    mismatchScore = $scope.scoreInDelScore;
            //                    break;
            //
            //                case "blosum45":
            //                    mismatchScore = $scope.blossum45MismatchScore;
            //                    break;
            //
            //                case "blosum62":
            //                    mismatchScore = $scope.blossum62MismatchScore;
            //                    break;
            //
            //                }
            //
            //                // horizontal first column
            //                for (var j = 2; j < $scope.matrix[2].length; j++) {
            //                    $scope.matrix[1][j].value = $scope.matrix[1][j - 1].value + mismatchScore;
            //                }
            //            }


            // determine what scoring method will be used
            var mismatch, match, indel;
            switch ($scope.scoringMethod) {

            case "editDistance":
                mismatch = $scope.edMismatchScore;
                indel = $scope.edMismatchScore;
                match = $scope.edMatchScore;

                break;

            case "score":
                mismatch = $scope.scoreMismatchScore;
                indel = $scope.scoreInDelScore;
                match = $scope.scoreMatchScore;
                break;

            case "blosum45":
                mismatch = $scope.blossum45MismatchScore;
                indel = $scope.blossum45MismatchScore;
                match = $scope.blosum45;
                break;

            case "blosum62":
                mismatch = $scope.blossum62MismatchScore;
                indel = $scope.blossum62MismatchScore;
                match = $scope.blosum62;
                break;

                // use Edit Distance is some error happens
            default:
                mismatch = $scope.edMismatchScore;
                indel = $scope.edMismatchScore;
                match = $scope.edMatchScore;
                break;

            }

            console.log("run");

            $scope.lastAlignmentMethod = $scope.alignmentMethod;
            $scope.lastScoringMethod = $scope.scoringMethod;
            $scope.lastMismatch = mismatch;
            $scope.lastIndel = indel;
            $scope.lastMatch = match;

            // run Needleman-Wunsch with provided params
            $scope.nw(mismatch, match, indel, $scope.subMatrix, $scope.scoringMethod, $scope.alignmentMethod);

            // draw the traceback from the computed matrix
            if ($scope.alignmentMethod === "global") {
                $scope.drawTraceback($scope.subMatrix);
                $scope.createAlignment($scope.subMatrix);
            } else if ($scope.alignmentMethod === "local") {
                $scope.drawTracebackLocal($scope.subMatrix, "local");
                //                var iandj = $scope.drawTracebackLocal2($scope.subMatrix, "local");
                //                $scope.createAlignmentLocal($scope.subMatrix, iandj[0], iandj[1]);
            } else if ($scope.alignmentMethod === "dovetail") {
                $scope.drawTracebackLocal($scope.subMatrix, "dovetail");
            } else if ($scope.alignmentMethod === "pattern") {
                $scope.drawTracebackLocal($scope.subMatrix, "pattern");
            }

            $scope.solved = true;
        }

        $scope.nwLocal = function (mismatch, match, indel, matrix, scoring) {
            for (var i = 1; i < matrix.length; i++) {
                for (var j = 1; j < matrix[i].length; j++) {

                    if ($scope.banded && (i > j + $scope.k || j > i + $scope.k)) {
                        matrix[i][j].type = 'blank';
                        continue;
                    }

                    var up, left, diag, restart;

                    restart = 0;

                    up = matrix[i - 1][j].value + indel;
                    left = matrix[i][j - 1].value + indel;

                    // Diagonal scores are not constant
                    if (scoring === "blosum45" || scoring === "blosum62") {
                        diag = matrix[i - 1][j - 1].value + match($scope.sequence1.charAt(i - 1), $scope.sequence2.charAt(j - 1));
                    } else {
                        diag = ($scope.sequence1.charAt(i - 1) === $scope.sequence2.charAt(j - 1) ? matrix[i - 1][j - 1].value + match : matrix[i - 1][j - 1].value + mismatch);
                    }

                    // Looking for the min value
                    if (scoring === "editDistance") {
                        if (up <= left && up <= diag && up <= restart) {
                            matrix[i][j].value = up;
                            matrix[i][j].up = true;
                        }
                        if (left <= up && left <= diag && left <= restart) {
                            matrix[i][j].value = left;
                            matrix[i][j].left = true;
                        }
                        if (diag <= up && diag <= left && left <= restart) {
                            matrix[i][j].value = diag;
                            matrix[i][j].diag = true;
                        }
                        if (restart <= up && restart <= left && restart <= diag) {
                            matrix[i][j].value = restart;
                        }
                    }

                    // scoring or blosum is score
                    // Looking for max value
                    else {
                        console.log(up, left, diag);
                        if (up >= left && up >= diag && up >= restart) {
                            matrix[i][j].value = up;
                            matrix[i][j].up = true;
                        }
                        if (left >= up && left >= diag && left >= restart) {
                            matrix[i][j].value = left;
                            matrix[i][j].left = true;
                        }
                        if (diag >= up && diag >= left && diag >= restart) {
                            matrix[i][j].value = diag;
                            matrix[i][j].diag = true;
                        }
                        if (restart >= up && restart >= left && restart >= diag) {
                            matrix[i][j].value = restart;
                        }
                    }
                }
            }
        }


        $scope.nw = function (mismatch, match, indel, matrix, scoring, alignment) {

            if (alignment === "local") {
                return $scope.nwLocal(mismatch, match, indel, matrix, scoring);
            }


            for (var i = 1; i < matrix.length; i++) {
                for (var j = 1; j < matrix[i].length; j++) {

                    if ($scope.banded && (i > j + $scope.k || j > i + $scope.k)) {
                        matrix[i][j].type = 'blank';
                        continue;
                    }

                    var up, left, diag;

                    up = matrix[i - 1][j].value + indel;
                    left = matrix[i][j - 1].value + indel;

                    // Diagonal scores are not constant
                    if (scoring === "blosum45" || scoring === "blosum62") {
                        diag = matrix[i - 1][j - 1].value + match($scope.sequence1.charAt(i - 1), $scope.sequence2.charAt(j - 1));
                    } else {
                        diag = ($scope.sequence1.charAt(i - 1) === $scope.sequence2.charAt(j - 1) ? matrix[i - 1][j - 1].value + match : matrix[i - 1][j - 1].value + mismatch);
                    }

                    // Looking for the min value
                    if (scoring === "editDistance") {
                        if (up <= left && up <= diag) {
                            matrix[i][j].value = up;
                            matrix[i][j].up = true;
                        }
                        if (left <= up && left <= diag) {
                            matrix[i][j].value = left;
                            matrix[i][j].left = true;
                        }
                        if (diag <= up && diag <= left) {
                            matrix[i][j].value = diag;
                            matrix[i][j].diag = true;
                        }
                    }

                    // scoring is score or blossum
                    // Looking for max value
                    else {
                        console.log(up, left, diag);
                        if (up >= left && up >= diag) {
                            matrix[i][j].value = up;
                            matrix[i][j].up = true;
                        }
                        if (left >= up && left >= diag) {
                            matrix[i][j].value = left;
                            matrix[i][j].left = true;
                        }
                        if (diag >= up && diag >= left) {
                            matrix[i][j].value = diag;
                            matrix[i][j].diag = true;
                        }
                    }
                }
            }
        }

        $scope.drawTracebackLocal2 = function (matrix, alignment) {
            var max = -9999999;
            var maxI = -1;
            var maxJ = -1;

            // Start at max of whole matrix
            if (alignment === "local") {
                for (var i = 1; i < matrix.length; i++) {
                    for (var j = 1; j < matrix[i].length; j++) {
                        if (matrix[i][j].value > max) {
                            max = matrix[i][j].value;
                            maxI = i;
                            maxJ = j;
                        }
                    }
                }
            }
            // Start at max along last row and last column
            else if (alignment === "dovetail") {
                // last row
                for (var i = 0; i < matrix[matrix.length - 1].length; i++) {
                    console.log(matrix[matrix.length - 1][i]);
                    if (matrix[matrix.length - 1][i].value > max) {
                        max = matrix[matrix.length - 1][i].value;
                        maxI = matrix.length - 1;
                        maxJ = i;
                    }
                }
                // last column
                for (var i = 0; i < matrix.length - 1; i++) {
                    console.log(matrix[i][matrix[i].length - 1]);
                    if (matrix[i][matrix[i].length - 1].value > max) {
                        max = matrix[i][matrix[i].length - 1].value;
                        maxI = i;
                        maxJ = matrix[i].length - 1;
                    }
                }
            }
            // Find max value along last row
            else if (alignment === "pattern") {
                // last row
                for (var i = 0; i < matrix[matrix.length - 1].length; i++) {
                    console.log(matrix[matrix.length - 1][i]);
                    if (matrix[matrix.length - 1][i].value > max) {
                        max = matrix[matrix.length - 1][i].value;
                        maxI = matrix.length - 1;
                        maxJ = i;
                    }
                }
            }

            var i = maxI;
            var j = maxJ;

            while (true) {
                matrix[i][j].color = 'lightpink';

                if (matrix[i][j].diag) {
                    j--;
                    i--;

                } else if (matrix[i][j].left) {
                    j--;

                } else if (matrix[i][j].up) {
                    i--;

                } else {
                    break;
                }
            }

            return [i, j]
        }

        $scope.createAlignmentLocal = function (matrix, i, j) {
            var seq1Front = 0;
            var seq2Front = 0;


            if (i > j) {
                while (seq1Front < j + 1) {
                    $scope.alignment[0].push($scope.sequence1.charAt(seq1Front++));
                }
            } else if (i < j) {
                while (seq2Front < i + 1) {
                    $scope.alignment[2].push($scope.sequence2.charAt(seq2Front++));
                }
            }



            while (seq1Front !== i) {
                $scope.alignment[0].push($scope.sequence1.charAt(seq1Front++));
                $scope.alignment[1].push(' ');
            }

            while (seq2Front !== j) {
                $scope.alignment[2].push($scope.sequence2.charAt(seq2Front++));
                $scope.alignment[1].push(' ');
            }

            var seq1 = i;
            var seq2 = j;

            while (matrix[i][j].color === 'lightpink') {
                if (matrix[i + 1][j + 1].color === 'lightpink') {

                    if ($scope.sequence1.charAt(seq1) === $scope.sequence2.charAt(seq2)) {
                        $scope.alignment[1].push('|');
                    } else {
                        $scope.alignment[1].push('r');
                    }

                    $scope.alignment[0].push($scope.sequence1.charAt(seq1++));
                    $scope.alignment[2].push($scope.sequence2.charAt(seq2++));

                    i++;
                    j++;

                } else if (matrix[i + 1][j].color === 'lightpink') {

                    $scope.alignment[0].push($scope.sequence1.charAt(seq1++));
                    $scope.alignment[1].push('i');
                    $scope.alignment[2].push(' ');

                    i++

                } else if (matrix[i][j + 1].color === 'lightpink') {

                    $scope.alignment[0].push(' ');
                    $scope.alignment[1].push('d');
                    $scope.alignment[2].push($scope.sequence2.charAt(seq2++));

                    j++
                } else {
                    break;
                }
            }



        }

        $scope.drawTracebackLocal = function (matrix, alignment) {
            var max = -9999999;
            var maxI = -1;
            var maxJ = -1;

            // Start at max of whole matrix
            if (alignment === "local") {
                for (var i = 1; i < matrix.length; i++) {
                    for (var j = 1; j < matrix[i].length; j++) {
                        if (matrix[i][j].value > max) {
                            max = matrix[i][j].value;
                            maxI = i;
                            maxJ = j;
                        }
                    }
                }
            }
            // Start at max along last row and last column
            else if (alignment === "dovetail") {
                // last row
                for (var i = 0; i < matrix[matrix.length - 1].length; i++) {
                    console.log(matrix[matrix.length - 1][i]);
                    if (matrix[matrix.length - 1][i].value > max) {
                        max = matrix[matrix.length - 1][i].value;
                        maxI = matrix.length - 1;
                        maxJ = i;
                    }
                }
                // last column
                for (var i = 0; i < matrix.length - 1; i++) {
                    console.log(matrix[i][matrix[i].length - 1]);
                    if (matrix[i][matrix[i].length - 1].value > max) {
                        max = matrix[i][matrix[i].length - 1].value;
                        maxI = i;
                        maxJ = matrix[i].length - 1;
                    }
                }
            }
            // Find max value along last row
            else if (alignment === "pattern") {
                // last row
                for (var i = 0; i < matrix[matrix.length - 1].length; i++) {
                    console.log(matrix[matrix.length - 1][i]);
                    if (matrix[matrix.length - 1][i].value > max) {
                        max = matrix[matrix.length - 1][i].value;
                        maxI = matrix.length - 1;
                        maxJ = i;
                    }
                }
            }

            var seq1 = maxI - 1,
                i = maxI;
            var seq2 = maxJ - 1,
                j = maxJ;

            var seq1Tail = $scope.sequence1.length - 1 - seq1;
            var seq2Tail = $scope.sequence2.length - 1 - seq2;


            while (seq1Tail > 0) {
                $scope.alignment[0].push($scope.sequence1.charAt(seq1 + seq1Tail));
                seq1Tail--;
            }

            while (seq2Tail > 0) {
                $scope.alignment[2].push($scope.sequence2.charAt(seq2 + seq2Tail));
                seq2Tail--;
            }

            while (true) {
                matrix[i][j].color = 'lightpink';

                if (matrix[i][j].diag) {
                    j--;
                    i--;

                    if ($scope.sequence1.charAt(seq1) === $scope.sequence2.charAt(seq2)) {
                        $scope.alignment[1].push('|');
                    } else {
                        $scope.alignment[1].push('r');
                    }

                    $scope.alignment[0].push($scope.sequence1.charAt(seq1--));
                    $scope.alignment[2].push($scope.sequence2.charAt(seq2--));


                } else if (matrix[i][j].left) {
                    j--;

                    $scope.alignment[1].push('d')
                    $scope.alignment[0].push(' ');
                    $scope.alignment[2].push($scope.sequence2.charAt(seq2--));


                } else if (matrix[i][j].up) {
                    i--;

                    $scope.alignment[1].push('i')
                    $scope.alignment[0].push($scope.sequence1.charAt(seq1--));
                    $scope.alignment[2].push(' ');

                } else {
                    break;
                }
            }

            while (seq1 >= 0) {
                $scope.alignment[0].push($scope.sequence1.charAt(seq1--));
                $scope.alignment[1].push(' ');
                $scope.alignment[2].push(' ');
            }
            while (seq2 >= 0) {
                $scope.alignment[2].push($scope.sequence2.charAt(seq2--));
                $scope.alignment[1].push(' ');
                $scope.alignment[0].push(' ');
            }



            $scope.alignment[0].reverse();
            $scope.alignment[1].reverse();
            $scope.alignment[2].reverse();
        }

        // input should be submatrix of scores
        // color traceback cells red
        // create alignment display
        $scope.drawTraceback = function (matrix) {
            var seq1 = 0;
            var seq2 = 0;

            var i = matrix.length - 1;
            var j = matrix[i].length - 1;

            console.log(i, j);
            console.log(matrix[i][j]);

            while (i >= 0 && j >= 0) {
                console.log(i, j);

                matrix[i][j].color = 'lightpink';

                if (matrix[i][j].diag) {
                    j--;
                    i--;

                } else if (matrix[i][j].left) {
                    j--;

                } else if (matrix[i][j].up) {
                    i--;

                } else if (i == 0 && j >= 0) {
                    j--;

                } else if (j == 0 && i >= 0) {
                    i--;
                }
            }
        }

        $scope.createAlignment = function (matrix) {
            var i = 0;
            var j = 0;

            var seq1 = 0;
            var seq2 = 0;

            while (i != matrix.length - 1 && j != matrix[i].length - 1) {
                if (matrix[i + 1][j + 1] !== undefined && matrix[i + 1][j + 1].color === 'lightpink') {
                    if ($scope.sequence1.charAt(seq1) === $scope.sequence2.charAt(seq2)) {
                        $scope.alignment[1].push('|');
                    } else {
                        $scope.alignment[1].push('r');
                    }

                    $scope.alignment[0].push($scope.sequence1.charAt(seq1++));
                    $scope.alignment[2].push($scope.sequence2.charAt(seq2++));

                    i++;
                    j++;
                } else if (matrix[i + 1][j] !== undefined && matrix[i + 1][j].color === 'lightpink') {

                    $scope.alignment[1].push('i');
                    $scope.alignment[2].push(' ');
                    $scope.alignment[0].push($scope.sequence1.charAt(seq1++));


                    i++;
                } else if (matrix[i][j + 1] !== undefined && matrix[i][j + 1].color === 'lightpink') {
                    $scope.alignment[1].push('d')
                    $scope.alignment[0].push(' ');
                    $scope.alignment[2].push($scope.sequence2.charAt(seq2++));

                    j++;
                }
            }

            while (seq2 < $scope.sequence2.length) {
                $scope.alignment[1].push('d')
                $scope.alignment[0].push(' ');
                $scope.alignment[2].push($scope.sequence2.charAt(seq2++));
            }

            while (seq1 < $scope.sequence1.length) {
                $scope.alignment[1].push('i');
                $scope.alignment[2].push(' ');
                $scope.alignment[0].push($scope.sequence1.charAt(seq1++));
            }
        }

        // shallow copy out score part of full matrix for use in NW
        $scope.initSubmatrix = function () {
            // get just the score entries out of the full matrix
            $scope.subMatrix = $scope.matrix.slice(1, $scope.matrix.length);
            for (var i = 0; i < $scope.subMatrix.length; i++) {
                $scope.subMatrix[i] = $scope.subMatrix[i].slice(1, $scope.subMatrix[i].length);
            }
            console.log($scope.subMatrix);
        }
        
        $scope.check = function (){
            
            $scope.showPopups = false;
            
            $scope.initCheckMatrix();
            
            $scope.initNW();
            
            $scope.initCorrectMatrix();
            
            $scope.matrix = $scope.checkMatrix;
            
            console.log($scope.checkMatrix);
            console.log($scope.matrix)
            
            for(var i=1; i < $scope.matrix.length; i++){
                for(var j=1; j < $scope.matrix[i].length; j++){
                    if($scope.matrix[i][j].value === $scope.correctMatrix[i][j].value){
                        $scope.matrix[i][j].color = 'green';
                    }
                    else{
                        $scope.matrix[i][j].color = 'red';
                    }
                }
            }
            
        }
        
        $scope.initCheckMatrix = function(){
            $scope.checkMatrix = $.extend(true, [], $scope.matrix)
            console.log($scope.checkMatrix);
        }
        
        $scope.initCorrectMatrix = function(){
            $scope.correctMatrix = $.extend(true, [], $scope.matrix)
            console.log($scope.correctMatrix);
        }

    }
})();