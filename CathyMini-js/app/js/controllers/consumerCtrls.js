 angular.module('common').
  controller('consumerCtrl', ['$scope', '$http', function($scope, $http) {
  
    /** Initialize consumer form's attributes */
    $scope.consumer = {};

    /** Connected user info */
    $scope.isConnected = {display: false, text: ""};
    $scope.user = "";
    
    /** Failure feedback */
    $scope.feedbackKo = {display: false, text: ""};
    
    /** Connection error on modals */
    $scope.displayConnectionError = false;
    
    /**
     * Suscribe a new user
     * @param {type} dismiss function for the modal
     */
    $scope.suscribe = function(dismiss) {
        $scope.displayConnectionError = false;
        $scope.feedbackKo.display = false;
        
        $http.post("http://localhost:8080//webresources/consumer/suscribe", $scope.consumer)
            .success(function() { 
                $scope.isConnected.display = true;  
                $scope.isConnected.text = "You correctly suscribe to CathyMini";
                $scope.getCurrentUser();
                dismiss();})
            .error(function(errorMsg, status) { 
                if (status === 400) {
                    $scope.feedbackKo.display = true;
                    $scope.feedbackKo.text = errorMsg;
                } else {
                    $scope.displayConnectionError = true;
                }
            });
    };
    
    /**
     * Connect a user
     * @param {type} dismiss function for the modal
     */
    $scope.connect = function(dismiss) {
        $scope.displayConnectionError = false;
        $scope.feedbackKo.display = false;
        
        $http.post("http://localhost:8080//webresources/consumer/connect", $scope.consumer)
            .success(function() { 
                $scope.isConnected.display = true;  
                $scope.isConnected.text = "You connect to CathyMini";
                $scope.getCurrentUser();
                dismiss();})
            .error(function(errorMsg, status) { 
                if (status === 400) {
                    $scope.feedbackKo.display = true;
                    $scope.feedbackKo.text = errorMsg;
                } else {
                    $scope.displayConnectionError = true;
                }
            });
    };
    
    /**
     * Logout a connected user
     */
    $scope.disconnect = function() {
        $scope.displayConnectionError = false;
        $scope.feedbackKo.display = false;
        
        $http.post("http://localhost:8080//webresources/consumer/logout", null)
            .success(function() { 
                $scope.isConnected.display = false;  
                $scope.isConnected.text = "You logout";
                $scope.getCurrentUser();
            })
            .error(function(errorMsg, status) { 
                if (status === 400) {
                    $scope.feedbackKo.display = true;
                    $scope.feedbackKo.text = errorMsg;
                } else {
                    $scope.displayConnectionError = true;
                }
            });
    };
    
    /**
     * Get user session infos if the user is connected
     */
    $scope.getCurrentUser = function() {
        $scope.displayConnectionError = false;
        $scope.feedbackKo.display = false;
        
        $http.get("http://localhost:8080//webresources/consumer/seeCurrent", null)
            .success(function(user) { 
                console.log("user -> "+user);
                if (user === "") {
                    $scope.isConnected.display = false;  
                    $scope.isConnected.text = "";
                    $scope.user = "";
                } else {
                    $scope.isConnected.display = true;
                    $scope.isConnected.text = "You are connected";
                    $scope.user = user;
                }
            })
            .error(function(errorMsg, status) { 
                if (status === 400) {
                    $scope.feedbackKo.display = true;
                    $scope.feedbackKo.text = errorMsg;
                } else {
                    $scope.displayConnectionError = true;
                }
            });
    };
    
    $scope.getCurrentUser();
  }]);