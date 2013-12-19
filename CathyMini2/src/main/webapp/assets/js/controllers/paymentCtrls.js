/**
 * Controller for the payment chain
 */
angular.module('payment').
    controller('paymentCtrl', ['$scope', '$http', 'cartService', 'consumerService', 'notificationService', 
        function($scope, $http, cartService, consumerService, notificationService) {
        $scope.cartService = cartService; // load the service in the scope
        
        // Payment tabs buttons values
        $scope.btnList = ['Choix de la Commande', 'Résumé de la commande', 'Identification', 'Adresse de Livraison',
            'Frais de port', 'Mode de paiement'];
        $scope.lastBtnValue = ['', 'Valider la commande', '', '', 'Accepter', 'Terminer ma Commande'];
        
        // Current active tab
        $scope.activeTab = 0;
        
        // Payement data
        $scope.paymentData = {};
    
        // User do return to index when login out
        $scope.$on('consumerDisconnect', function() {
            window.location = "/index.xhtml";
        });
        
        // Listener for the login tab
        $scope.$on('consumerConnect', function() {
            if($scope.activeTab === 2) {
                $scope.nextTab();
            }
        });
        
        // Listener for the login tab
        $scope.$on('consumerSubscribe', function() {
            if($scope.activeTab === 2) {
                $scope.nextTab();
            }
        });
        
        $scope.selectAddr = function(addr) {
            $scope.paymentData.addressId = addr.id;
        };
        
        // Behaviour of the pager 'next'
        $scope.nextTab = function() {
            if($scope.activeTab === 0) {
                // Cart/Sub selection
                $scope.activeTab++;
            } else if($scope.activeTab === 1) {
                // Cart/Sub validation
                if (cartService.cart.price !== 0) {
                    if (consumerService.isConnected) {
                        $scope.activeTab += 2;
                    } else {
                        $scope.activeTab++;
                    }
                }
            } else if($scope.activeTab === 2) {
                // Login or subscribe
                if (consumerService.isConnected) {
                    $scope.activeTab++;
                }
                
            } else if($scope.activeTab === 3) {
                // Delivery Address selection
                $scope.activeTab++;
                
            } else if($scope.activeTab === 4) {
                // Shipping Cost validation
                $scope.activeTab++;
            } else if($scope.activeTab === 5) {
                // Purchase/Sub finalization
                
                console.log($scope.paymentData);
                $http.post("/webresources/purchase/createPurchase", $scope.paymentData)
                    .success(function() { 
                        $scope.activeTab = 0;
                        cartService.getCart();
                        $scope.cartService = cartService;
                        notificationService.displayMessage("Votre commande a bien été enregistrée et vous sere bientôt envoyée !")
                    })
                    .error(function(data, status, headers, config) {
                    });
            }
        };
        
        // Behaviour of the pager 'last'
        $scope.lastTab = function() {
            if($scope.activeTab > 0) {
                if ($scope.activeTab === 3) {
                    // Do not display login tab when user is connected
                    $scope.activeTab -= 2;
                } else {
                    $scope.activeTab--;
                }
            }
        };
    
        // Behaviour of tabs buttons head
        $scope.selectTab = function(select) {
            if(select <= $scope.activeTab) {
                $scope.activeTab = select;
            }
        };
        
        /* Following functions are used to apply 'disabled' and 'hidden' classes */
        $scope.getBtnClass = function(select) {
            return (select <= $scope.activeTab) ? '' : 'disabled';
        };
        
        $scope.getTabClass = function(select) {
            return ($scope.activeTab === select) ? 'show-anim' : 'hidden';
        };
        
        $scope.isLinkedTabActive = function(select) {
            return ($scope.activeTab === select) ? 'active' : '';
        };
        
        $scope.getLastBtnClass = function() {
            return ($scope.activeTab !== 0) ? '' : 'disabled';
        };
        
        $scope.getNextBtnClass = function() {
            if($scope.activeTab === 0 || $scope.activeTab === 2 || $scope.activeTab === 3) {
                return 'hidden';
            } else if(cartService.cart.price === 1) {
                return 'disabled';
            } else {
                return '';
            }
        };
  }]);
