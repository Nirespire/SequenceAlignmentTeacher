(function () {
    'use strict';

    angular
        .module('seqalign.modal')
        .controller('modalCtrl', main);

    function main($scope,$uibModalInstance) {

        $scope.ok = function () {
            $uibModalInstance.close();
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();