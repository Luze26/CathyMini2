/**
 * Controller for the account page
 */
angular.module('account')
.controller('accountSettingsCtrl', ['$scope', '$http', 'consumerService', function($scope, $http, consumerService) {
    
    //////////////////////////////////////////////////
    //  ATTRIBUTES
    //////////////////////////////////////////////////
    
    $scope.fields = [{label: "Name", key: "username", placeholder: "", success: false, loading: false, editable: true},
        {label: "Email", key: "mail", placeholder: "", success: false, loading: false, editable: true}];
    
    $scope.address = [];
    
    
    //////////////////////////////////////////////////
    //  METHODS
    //////////////////////////////////////////////////
    
    /**
     * Copy the current consumer into editConsumer
     * @returns {consumer}
     */
    var copyConsumer = function() {
        $scope.editConsumer = {"username": $scope.consumer.username, "mail": $scope.consumer.mail};
    };
    
    /**
     * Wait for the user to connect
     * @type @exp;$scope@call;$on
     */
    var clearConnectWatcher = $scope.$on('consumerConnect', function(event, consumer) {
        $scope.consumer = consumer;
        copyConsumer();
        clearConnectWatcher();
    });
    
    /**
     * Watch if the user disconnect, in this case redirect on the index
     */
    $scope.$on('consumerDisconnect', function() {
        window.location = "/index.xhtml";
    });
    
    /**
     * Called when a field is edited
     * @param {type} field edited
     */
    $scope.showEdit = function(field) {
        field.visible = false;
        field.loading = false;    
        field.editable = false;
    };
    
    /**
     * Called when an edition is finish
     * @param {type} field edited
     */
    $scope.edit = function(field) {
        field.visible = false;
        field.loading = true;
        field.error = null;
        field.editable = true;
        $http.post("/webresources/consumer/update", $scope.editConsumer).success(function(consumer) {
            field.loading = false;
            if(consumer) { //Consumer returned by the server
                field.success = true;
                $scope.consumer = consumer; //change the current consumer
                $scope.editConsumer[field.key] = $scope.consumer[field.key]; //change the edited field
            }
        }).error(function(error ,status) {
            $scope.editConsumer[field.key] = $scope.consumer[field.key]; //reset the edited value for the field
            field.loading = false;
            field.error = error;
        });
    };
    
    /**
     * Cancel the edition
     * @param {type} field
     */
    $scope.cancelEdit = function(field) {
        $scope.editConsumer[field.key] = $scope.consumer[field.key];
        field.editable = true;
    };
  
    /** Popup element for address **/
    var modal = angular.element("#addressModal");
    
    /**
     * Add address
     */
    $scope.addAddress = function() {
        var newAddress = $scope.newModal.address;
        consumerService.addAddress(newAddress).success(function() {
            $scope.address.push(newAddress);
            modal.modal('hide');
            $scope.newModal.address = {};
        });
    };
    
    /**
     * Init the modal with the given address info
     * @param {type} address
     * @returns {undefined}
     */
    $scope.initEditAddress = function(address) {
        $scope.modal = $scope.editModal;
        $scope.editModal.address.id = address.id; 
        $scope.editModal.address.address = address.address;
        $scope.editModal.address.zipCode = parseInt(address.zipCode);
        $scope.editModal.address.city = address.city;
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
            oldAddress.address = editedAddress.address;
            oldAddress.zipCode = editedAddress.zipCode;
            oldAddress.city = editedAddress.city;
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
    $scope.editModal = {title: "Editer l'adresse", address: {}, action: $scope.editAddress};
    
    /**
     * New modal object
     */
    $scope.newModal = {title: "Ajouter une adresse", address: {}, action: $scope.addAddress};
    
    /**
     * Modal
     */
    $scope.modal = $scope.newModal;
}]);
