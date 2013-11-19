 angular.module('common').
  controller('consumerCtrl', ['$scope', '$http', function($scope, $http) {
  
    /** Initialize consumer form's attributes */
    $scope.suscriber = {};
    $scope.consumer = {};

    /** Connected user info */
    $scope.isConnected = {display: false, text: ""};
    $scope.user = "";
    
    /** Failure feedback */
    $scope.feedbackKo = {display: false, text: ""};
    
    /** Connection error on modals */
    $scope.displayConnectionError = false;
    
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
            .error(function(data, status, headers, config) { 
                if (status === 400) {
                    var sliceAfter = data.indexOf("<b>message</b>") + 14;
                    var sliceBefore = data.indexOf("</p><p><b>description</b>");
                    $scope.feedbackKo.display = true;
                    $scope.feedbackKo.text = data.slice(sliceAfter, sliceBefore);
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
            .error(function(data, status, headers, config) { 
                if (status === 400) {
                    var sliceAfter = data.indexOf("<b>message</b>") + 14;
                    var sliceBefore = data.indexOf("</p><p><b>description</b>");
                    $scope.feedbackKo.display = true;
                    $scope.feedbackKo.text = data.slice(sliceAfter, sliceBefore);
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
                if (user === "") {
                    console.log("You are not connected.");
                    $scope.isConnected.display = false;  
                    $scope.isConnected.text = "";
                    $scope.user = "";
                } else {
                    console.log("You are connected as "+user);
                    $scope.isConnected.display = true;
                    $scope.isConnected.text = "You are connected";
                    $scope.user = user;
                }
            })
            .error(function(data, status, headers, config) { 
                if (status === 400) {
                    var sliceAfter = data.indexOf("<b>message</b>") + 14;
                    var sliceBefore = data.indexOf("</p><p><b>description</b>");
                    $scope.feedbackKo.display = true;
                    $scope.feedbackKo.text = data.slice(sliceAfter, sliceBefore);
                } else {
                    $scope.displayConnectionError = true;
                }
            });
    };
    
    $scope.getCurrentUser();
  }])
    .controller('subscribeModalCtrl', ['$scope', '$http', function($scope, $http) {
              /**
     * Subscribe a new user
     * @param {type} dismiss function for the modal
     */
    $scope.subscribe = function() {
       $scope.displayConnectionError = false;
        $scope.feedbackKo.display = false;
        
        $http.post("http://localhost:8080//webresources/consumer/suscribe", $scope.suscriber)
            .success(function() { 
                $scope.isConnected.display = true;  
                $scope.isConnected.text = "You correctly suscribe to CathyMini";
                $scope.getCurrentUser();})
            .error(function(data, status, headers, config) {
                if (status === 400) {
                    var sliceAfter = data.indexOf("<b>message</b>") + 14;
                    var sliceBefore = data.indexOf("</p><p><b>description</b>");
                    $scope.feedbackKo.display = true;
                    $scope.feedbackKo.text = data.slice(sliceAfter, sliceBefore);
                } else {
                    $scope.displayConnectionError = true;
                }
            });
    };
  }]);