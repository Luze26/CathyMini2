angular.module('account')
.controller('accountSettingsCtrl', ['$scope', '$http', 'consumerService', function($scope, $http, consumerService) {

    $scope.fields = [{label: "Name", key: "username", placeholder: "", success: false, loading: false, editable: true},
        {label: "Email", key: "mail", placeholder: "", success: false, loading: false, editable: true}];
    
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
        field.editable = true;
        $http.post("/webresources/consumer/update", $scope.editConsumer).success(function(consumer) {
            field.loading = false;
            field.success = true;
            $scope.editConsumer[field.key] = $scope.consumer[field.key];
            $scope.consumer[field.key] = consumer[field.key];
        });
    };
    
    $scope.cancelEdit = function(field) {
        $scope.editConsumer[field.key] = $scope.consumer[field.key];
        field.editable = true;
    };
  
}]);
