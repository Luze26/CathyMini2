angular.module('payment').
    controller('paymentCtrl', ['$scope', '$http', 'cartService', 'consumerService', 
        function($scope, $http, cartService, consumerService) {
        $scope.cartService = cartService; // load the service in the scope
        
        // Redirect user if his cart is empty when loading the payment page
        /*if (cartService.cart.price === 0) {
            window.location = "/index.xhtml";
        }*/
        
        // Payment tabs buttons values
        $scope.btnList = ['Récaputulatif de la commande', 'Identification du client', 'Adresse de livraison',
            'Calcul des frais de port', 'Choix du mode de paiement'];
        $scope.lastBtnValue = ['Valider le panier', '', '', 'Accepter', 'Commander'];
        
        // Current active tab
        $scope.activeTab = 0;
    
        // User do return to index when login out
        $scope.$on('consumerDisconnect', function() {
            window.location = "/index.xhtml";
        });
        
        // Listener for the login tab
        $scope.$on('consumerConnect', function() {
            if($scope.activeTab === 1) {
                $scope.nextTab();
            }
        });
        
        // Listener for the login tab
        $scope.$on('consumerSubscribe', function() {
            if($scope.activeTab === 1) {
                $scope.nextTab();
            }
        });
        
        // Behaviour of the pager 'next'
        $scope.nextTab = function(param) {
            if($scope.activeTab === 0) {
                // Cart validation
                if (cartService.cart.price !== 0) {
                    if (consumerService.isConnected) {
                        $scope.activeTab += 2;
                    } else {
                        $scope.activeTab++;
                    }
                }
            } else if($scope.activeTab === 1) {
                // Login or subscribe
                if (consumerService.isConnected) {
                    $scope.activeTab++;
                }
                
            } else if($scope.activeTab === 2) {
                // Verification de l'adresse de livraison
                $scope.activeTab++;
                
            } else if($scope.activeTab === 3) {
                // Validation des frais de transports
                $scope.activeTab++;
                
            } if($scope.activeTab === 4) {
                // Verificaction des informations de paiement et commande
            }
        };
        
        // Behaviour of the pager 'last'
        $scope.lastTab = function() {
            if($scope.activeTab > 0) {
                if ($scope.activeTab === 2) {
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
            return ($scope.activeTab === select) ? '' : 'hidden';
        };
        
        $scope.isLinkedTabActive = function(select) {
            return ($scope.activeTab === select) ? 'active' : '';
        };
        
        $scope.getLastBtnClass = function() {
            return ($scope.activeTab !== 0) ? '' : 'disabled';
        };
        
        $scope.getNextBtnClass = function() {
            if($scope.activeTab === 1 || $scope.activeTab === 2) {
                return 'hidden';
            } else if(cartService.cart.price === 0) {
                return 'disabled';
            } else {
                return '';
            }
        }
  }]);
