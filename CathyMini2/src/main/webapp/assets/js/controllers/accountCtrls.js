angular.module('account')
.controller('accountSettingsCtrl', ['$scope', '$http', 'consumerService', function($scope, $http, consumerService) {
    
    $scope.fields = [{label: "Name", key: "username", placeholder: "", success: false, loading: false, editable: true},
        {label: "Email", key: "mail", placeholder: "", success: false, loading: false, editable: true}];
    
    $scope.address = [];
    
    var copyConsumer = function() {
        $scope.editConsumer = {"username": $scope.consumer.username, "mail": $scope.consumer.mail};
    };
    
    var clearConnectWatcher = $scope.$on('consumerConnect', function(event, consumer) {
        $scope.consumer = consumer;
        copyConsumer();
        clearConnectWatcher();
    });
    
    $scope.$on('consumerDisconnect', function() {
        window.location = "/index.xhtml";
    });
    
    $scope.showEdit = function(field) {
        field.visible = false;
        field.loading = false;    
        field.editable = false;
    };
    
    $scope.edit = function(field) {
        field.visible = false;
        field.loading = true;
        field.error = null;
        field.editable = true;
        $http.post("/webresources/consumer/update", $scope.editConsumer).success(function(consumer) {
            field.loading = false;
            if(consumer) {
                field.success = true;
                $scope.consumer = consumer;
                $scope.editConsumer[field.key] = $scope.consumer[field.key];
                $scope.consumer[field.key] = consumer[field.key];
            }
        }).error(function(error ,status) {
            $scope.editConsumer[field.key] = $scope.consumer[field.key];
            field.loading = false;
            field.error = error;
        });
    };
    
    $scope.cancelEdit = function(field) {
        $scope.editConsumer[field.key] = $scope.consumer[field.key];
        field.editable = true;
    };
  
    /** Popup element **/
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
    
       /**
       * Delete the adress
       * @param {Adress} adress to delete
       */
      $scope.deleteAddress = function(address) {
        $http.post("/webresources/consumer/deleteAddress", address)
          .success(function() {
                  consumerService.getAddress().success(
                        function(data) {
                            $scope.address = data;
                     });
                 
          })
          .error(function(data) {
              console.log("errpr");
            $scope.displayConnectionError = true; // display error feedback
          });
      };
      
    consumerService.getAddress().success(
            function(data) {
                $scope.address = data;
                console.log($scope.address);
            });
            
    $scope.editModal = {title: "Editer l'adresse", address: {}, action: $scope.editAddress};
    $scope.newModal = {title: "Ajouter une adresse", address: {}, action: $scope.addAddress};
    $scope.modal = $scope.newModal;
}]);
