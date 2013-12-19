/**
 * Controller for the address list of an user
 */
angular.module('address')
    .controller('addressCtrl', ['$scope', '$http', 'consumerService', function($scope, $http, consumerService) {
    
    /**
     * List of address
     */
    $scope.address = [];

    /** Popup element for address **/
    var modal = angular.element("#addressModal");
    
    /**
     * Add address
     */
    $scope.addAddress = function() {
        var newAddress = $scope.newModal.address;
        consumerService.addAddress(newAddress).success(function() {
            modal.modal('hide');
            $scope.newModal.address = {};
            consumerService.getAddress().success(function(data) {
                $scope.address = data;
            });
        });
    };
    
    /**
     * Init the modal with the given address info
     * @param {type} address
     */
    $scope.initEditAddress = function(address) {
        $scope.modal = $scope.editModal;
        $scope.editModal.address.id = address.id; 
        $scope.editModal.address.firstname = address.firstname;
        $scope.editModal.address.lastname = address.lastname;
        $scope.editModal.address.address = address.address;
        $scope.editModal.address.zipCode = parseInt(address.zipCode);
        $scope.editModal.address.city = address.city;
        $scope.editModal.address.country = address.country;
        
        $scope.editModal.oldAddress = address;
    };
    
    /**
     * Edition for an address finish
     * @param {type} address
     */
    $scope.editAddress = function(address) {
        var editedAddress = $scope.editModal.address;
        var oldAddress = $scope.editModal.oldAddress;
        consumerService.editAddress(editedAddress).success(function() {
            oldAddress.firstname = editedAddress.firstname;
            oldAddress.lastname = editedAddress.lastname;
            oldAddress.address = editedAddress.address;
            oldAddress.zipCode = editedAddress.zipCode;
            oldAddress.city = editedAddress.city;
            oldAddress.country = editedAddress.country;
            modal.modal('hide');
            $scope.editAddress.address = {};
        }); 
    };
    
       /**
       * Delete the adress
       * @param {Adress} adress to delete
       */
      $scope.deleteAddress = function(address) {
          consumerService.deleteAddress(address)
            .success(function() {
                    consumerService.getAddress().success(
                          function(data) {
                              $scope.address = data;
                       });

            })
            .error(function(data) {
              $scope.displayConnectionError = true; // display error feedback
            });
      };
      
    /**
     * Initialize list of address
     */
    consumerService.getAddress().success(function(data) {
        $scope.address = data;
    });
    
    /**
     * Edit modal object
     */
    $scope.editModal = {title: "Edition d'adresse", okLabel: "Editer l'adresse", address: {}, action: $scope.editAddress};
    
    /**
     * New modal object
     */
    $scope.newModal = {title: "Ajout d'une adresse", okLabel: "Ajouter l'adresse", address: {}, action: $scope.addAddress};
    
    /**
     * Modal
     */
    $scope.modal = $scope.newModal;
    
    /**
     * And function, because xhtml not allow & in attributes
     * @param {type} x
     * @param {type} y
     * @returns {boolean} x && y
     */
    $scope.and = function(x,y) {
        return x && y;
    };
    
    /**
     * Or function, because xhtml not allow | in attributes
     * @param {type} x
     * @param {type} y
     * @returns {boolean} x || y
     */
    $scope.or = function(x,y) {
        return x || y;
    };
}]);

