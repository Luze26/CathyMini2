 angular.module('common').
  controller('consumerCtrl', ['$scope', 'consumerService', function($scope, consumerService) {
    
    $scope.isConnected = function() {
        return consumerService.isConnected;
    };
    
    /**
     * Logout a connected user
     */
    $scope.disconnect = function() {
        consumerService.disconnect();
    };
  }])
    .controller('subscribeModalCtrl', ['$scope', 'consumerService', function($scope, consumerService) {
            
   /** Display error message */
    $scope.displayError = false;
    
    /** Error message to display **/
    $scope.error = {title: "", message: ""};
    
    /** Form information **/
    $scope.subscriber = {};
    
    /** Popup element **/
    var modal = angular.element("#subscribeModal");
    
    /**
     * Subscribe a new user
     */
    $scope.subscribe = function() {
        $scope.displayError = false;
        consumerService.subscribe($scope.subscriber)
            .then(function() {
                modal.modal('hide');
            },
            function(error) {
                $scope.error.message = error;
            });
    };
  }])
  .controller('connectionModalCtrl', ['$scope', 'consumerService',  function($scope, consumerService) {
          
    /** Display error message */
    $scope.displayError = false;
    
    /** Error message to display **/
    $scope.error = {title: "", message: ""};
    
    /** Consuler information **/
    $scope.consumer = {user: "", pwd: ""};
    
    /**
     * Connect a user
     */
    $scope.connect = function() {   
        $scope.displayError = false;
        consumerService.connect($scope.consumer)
            .then(function() {
                
            },
            function(error) {
                $scope.error.message = error;
            });
    };
  }]);