/**
 * Controller for the address list of an user
 */
angular.module('address')
    .controller('addressCtrl', ['$scope', '$http', 'consumerService', function($scope, $http, consumerService) {
    
        /**
         * List of address
         */
        $scope.address = [];
        
        /**
         * Address modal
         */
        var modal = angular.element("#addressModal");

        $scope.addAddress = function() {
            var newAddress = $scope.newModal.address;

            consumerService.addAddress(newAddress).success(function() {
                $scope.address.push(newAddress);
                modal.modal('hide');
                $scope.newModal.address = {};
            });
        };

        $scope.initEditAddress = function(address) {
            $scope.modal = $scope.editModal;
            $scope.editModal.address.id = address.id; 
            $scope.editModal.address.address = address.address;
            $scope.editModal.address.zipCode = parseInt(address.zipCode);
            $scope.editModal.address.city = address.city;
            $scope.editModal.oldAddress = address;
        };

        $scope.editAddress = function(address) {
            var editedAddress = $scope.editModal.address;
            var oldAddress = $scope.editModal.oldAddress;
            consumerService.editAddress(editedAddress).success(function() {
                oldAddress.address = editedAddress.address;
                oldAddress.zipCode = editedAddress.zipCode;
                oldAddress.city = editedAddress.city;
                modal.modal('hide');
                $scope.editAddress.address = {};
            }); 
        };

        consumerService.getAddress().success(
                function(data) {
                    $scope.address = data;
                });

        $scope.editModal = {title: "Editer l'adresse", address: {}, action: $scope.editAddress};
        $scope.newModal = {title: "Ajouter une adresse", address: {}, action: $scope.addAddress};
        $scope.modal = $scope.newModal;
    }]);

