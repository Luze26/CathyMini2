angular.module('account')
.controller('accountSettingsCtrl', ['$scope', '$http', 'consumerService', function($scope, $http, consumerService) {

    $scope.newAddress = {};
    
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
    var modal = angular.element("#addAddressModal");
    
    $scope.addAddress = function() {
        var newAddress = $scope.newAddress;
        consumerService.addAddress(newAddress).success(function() {
            $scope.address.push(newAddress);
            modal.modal('hide');
        });
    };
    
    consumerService.getAddress().success(
            function(data) {
                $scope.address = data;
            });
}]);
