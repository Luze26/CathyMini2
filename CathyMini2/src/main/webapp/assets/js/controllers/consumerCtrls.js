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
                    if(data.data == "This mail address is already used by another user.") {
                        $scope.mailError = true;
                    }
                    else if(data.data == "This username already exist.") {
                        $scope.nameError = true;
                    }
                }
                else {
                    $scope.error = "Problème de connexion, vérifier votre connexion internet.";
                }
            });
    };
    
    /**
     * And function, because xhtml not allow & in attributes
     * @param {type} x
     * @param {type} y
     * @returns {boolean} x && y
     */
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
    
    /**
     * Displayed or not the reset password form
     */
    $scope.displayResetForm = false;
    
    /**
     * Feedback reset password
     */
    $scope.resetPasswordInfo = null;
    
    /**
     * Username for reset password
     */
    $scope.resetPwdUsername = "";
    var resetModal = {title: "Demande de nouveau mot de passe", okLabel: "Demander un nouveau mot de passe"};
    var connectModal = {title: "Connexion", okLabel: "Se connecter"};
    
    /**
     * Reset password
     */
    $scope.resetPassword = function() {
        $scope.resetPasswordInfo = null;
        $scope.error = null;
        $scope.errorReset = null;
        consumerService.resetPassword($scope.resetPwdUsername).success(function() { //success
               $scope.displayResetForm = false;
               $scope.modal = connectModal;
               $scope.resetPasswordInfo = "Un e-mail vous a été envoyé, avec la marche à suivre pour changer votre mot de passe."
            }).error(function(data, status) { //error
                if(status == 400) {
                    $scope.errorReset = "Aucun utilisateur correspond au nom donné.";
                }
                else {
                    $scope.error = "Problème de connexion, vérifier votre connexion internet.";
                }
            });
    };
    
    /**
     * Show reset password form
     * @param {type} show
     */
    $scope.showResetPassword = function(show) {
        $scope.displayResetForm = show;
        if(show) {
            $scope.modal = resetModal;
        }
        else {
            $scope.modal = connectModal;
        }
    };
    
    resetModal.action = $scope.resetPassword;
    connectModal.action = $scope.connect;
    
    /**
     * Modal info
     */
    $scope.modal = connectModal;
    
    /**
     * And function, because xhtml not allow & in attributes
     * @param {type} x
     * @param {type} y
     * @returns {boolean} x && y
     */
    $scope.and = function(x,y) {
        return x && y;
    };
  }]);