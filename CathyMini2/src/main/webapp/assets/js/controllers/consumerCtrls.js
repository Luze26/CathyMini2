/**
 * Controllers to manage consumer connection and subscription
 */
angular.module('common').
  controller('consumerCtrl', ['$scope', 'consumerService', function($scope, consumerService) {
    
    /**
     * Consumer information
     */
    $scope.consumer = consumerService.consumer;
    
    /**
     * Return if the user is conencted
     * @returns {Boolean}
     */
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
    .controller('subscribeModalCtrl', ['$scope', 'consumerService', 'notificationService', function($scope, consumerService, notificationService) {
            
   /** Display error message */
    $scope.nameError = false;
    $scope.mailError = false;
    
    /** Error message to display **/
    $scope.error = null;
    
    /** Form information **/
    $scope.subscriber = {};
    
    /** Popup element **/
    var modal = angular.element("#subscribeModal");
    
    /**
     * Subscribe a new user
     */
    $scope.subscribe = function() {
        $scope.error = null;
        $scope.nameError = false;
        $scope.mailError = false;
        consumerService.subscribe($scope.subscriber)
            .then(function() { //success
                modal.modal('hide'); //hide the modal
                notificationService.displayMessage("Bienvenue sur CathyMini !", 4000);
            },
            function(data) { //error
                if(data.status == 400) {
                    console.log(data);
                    if(data.data == "mail error") {
                        $scope.mailError = true;
                    }
                    else if(data.data == "username error") {
                        $scope.nameError = true;
                    }
                }
                else {
                    $scope.error = "Problème de connexion, vérifier votre connexion internet.";
                }
            });
    };
    
    $scope.and = function(x,y) {
        return x && y;
    };
  }])
  .controller('connectionModalCtrl', ['$scope', 'consumerService', 'notificationService', function($scope, consumerService, notificationService) {
          
    /** Display error message */
    $scope.displayError = false;
    
    /** Error message to display **/
    $scope.error = null;
    
    /** Consuler information **/
    $scope.consumer = {user: "", pwd: ""};
    
    /** Popup element **/
    var modal = angular.element("#connectModal");
    
    /**
     * Connect a user
     */
    $scope.connect = function() {   
        $scope.error = null;
        consumerService.connect($scope.consumer)
            .then(function() { //success
                modal.modal('hide'); //hide the modal
                notificationService.displayMessage("Je suis contente de te revoir " + $scope.consumer.user + "!", 4000);
            },
            function(data) { //error
                if(data.status == 400) {
                    $scope.error = "Mauvais nom d'utilisateur ou mot de passe.";
                }
                else {
                    $scope.error = "Problème de connexion, vérifier votre connexion internet.";
                }
            });
    };
  }]);