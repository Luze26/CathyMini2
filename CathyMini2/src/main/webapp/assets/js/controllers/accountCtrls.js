/**
 * Controller for the account page
 */
angular.module('account')
.controller('accountSettingsCtrl', ['$scope', '$http', 'consumerService', 'notificationService', '$rootScope', function($scope, $http, consumerService, notificationService, $rootScope) {
    
    //////////////////////////////////////////////////
    //  ATTRIBUTES
    //////////////////////////////////////////////////
    
    /**
     * If the user is connected
     */
    $scope.userConnected = false;
    
    /**
     * Settings field
     */
    $scope.fields = [{label: "Nom", key: "username", type: "text", placeholder: "", success: false, loading: false, editable: true},
        {label: "E-mail", key: "mail", placeholder: "", type: "email", success: false, loading: false, editable: true}];
    

    /**
     * Set the tick on the navbar
     */
    $rootScope.header = "myAccount";
    
    /**
     * User's purchases and subscriptions
     */
    $scope.purchases;
    $scope.subscriptions;
    
    /**
     * Purchase and Subscription to display detail
     */
    $scope.selectedPurchase = -1;
    $scope.selectedSubscription = -1;

    
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
        $scope.userConnected = true;
        $scope.consumer = consumer;
        copyConsumer();
        clearConnectWatcher();
    });
    
    /**
     * Watch if the user disconnect, in this case redirect on the index
     */
    $scope.$on('consumerDisconnect', function() {
        $scope.userConnected = false;
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
        field.success = false;
        $http.post("/webresources/consumer/update", $scope.editConsumer).success(function(consumer) {
            field.loading = false;
            if(consumer) { //Consumer returned by the server
                field.success = true;
                $scope.consumer = consumer; //change the current consumer
                $scope.editConsumer[field.key] = $scope.consumer[field.key]; //change the edited field
            }
            
            var notif;
            switch(field.key) {
                case "username":
                    notif = "Je vous appelerai dorénavant " + consumer.username + ".";
                    break;
                case "mail":
                    notif = "Votre adresse e-mail a correctement été changée.";
                    break;
            }
            
            notificationService.displayMessage(notif);
        }).error(function(error ,status) {
            field.loading = false;
            if(status === 400) {
                if(error === "username already exist") {
                    field.error = " " + $scope.editConsumer[field.key] + " est déjà utilisé par un autre membre.";
                }
                else if(error === "mail already exist") {
                    field.error = " " + $scope.editConsumer[field.key] + " est déjà utilisée par un autre membre.";
                }
                $scope.editConsumer[field.key] = $scope.consumer[field.key]; //reset the edited value for the field
            }
            else {
                field.error = "Problème de connexion, vérifiez votre connexion internet.";
            }
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
    
    $scope.showPurchaseDetails = function(purchase) {
        if ($scope.selectedPurchase !== purchase) {
            $scope.selectedPurchase = purchase;
        } else {
            $scope.selectedPurchase = -1;
        }
    };
    
    $scope.showSubscriptionDetails = function(subscription) {
        if ($scope.selectedSubscription !== subscription) {
            $scope.selectedSubscription = subscription;
        } else {
            $scope.selectedSubscription = -1;
        }
    };
    
    /**
     * Get user's purchases
     */       
    $http.get("/webresources/purchase/purchases")
        .success(function(purchases) { 
            $scope.purchases = purchases;
        })
        .error(function(data, status, headers, config) { 
            purchase = {};
        });
        
    /**
     * Get user's subscriptions
     */       
    $http.get("/webresources/purchase/subscriptions")
        .success(function(subscriptions) { 
            $scope.subscriptions = subscriptions;
        })
        .error(function(data, status, headers, config) { 
            purchase = {};
        });
}]);
