 angular.module('common').
  controller('consumerCtrl', ['$scope', '$http', function($scope, $http) {
  
    /** Initialize consumer form's attributes */
    $scope.consumer = {};

    /** Success feedback*/
    $scope.feedbackOk = {display: false, text: ""};
    
    /** Failure feedback*/
    $scope.feedbackKo = {display: false, text: ""};
    
    /** Connection error on modals */
    $scope.displayConnectionError = false;
    
    /**
     * Suscribe a new user
     * @param {type} dismiss function for the modal
     */
    $scope.suscribe = function(dismiss) {
        $scope.displayConnectionError = false;
        $scope.feedbackOk.display = false;
        $scope.feedbackKo.display = false;
        
        $http.post("http://localhost:8080//webresources/consumer/suscribe", $scope.consumer)
            .success(function() { 
                $scope.feedbackOk.display = true;  
                $scope.feedbackOk.text = "You correctly suscribe to CathyMini";
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
     * @param {type} dismiss function for the modal
     */
    $scope.connect = function(dismiss) {
        $scope.displayConnectionError = false;
        $scope.feedbackOk.display = false;
        $scope.feedbackKo.display = false;
        
        $http.post("http://localhost:8080//webresources/consumer/logout", null)
            .success(function() { 
                $scope.feedbackOk.display = true;  
                $scope.feedbackOk.text = "You logout";
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
        $scope.feedbackOk.display = false;
        $scope.feedbackKo.display = false;
        
        $http.post("http://localhost:8080//webresources/consumer/connect", $scope.consumer)
            .success(function() { 
                $scope.feedbackOk.display = true;  
                $scope.feedbackOk.text = "You connect to CathyMini";
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
    
  }]);