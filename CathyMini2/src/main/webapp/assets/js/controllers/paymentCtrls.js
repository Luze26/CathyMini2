angular.module('payment').
    controller('paymentCtrl', ['$scope', '$http', 'cartService', 'consumerService', 
        function($scope, $http, cartService, consumerService) {
           
        $scope.cartService = cartService; // load the service in the scope
  
        $scope.btnList = ['Récaputulatif de la commande', 'Identification du client', 'Adresse de livraison',
            'Calcul des frais de port', 'Choix du mode de paiement'];
        $scope.lastBtnValue = ['Valider le panier', '', '', 'Accepter', 'Commander'];
        
        $scope.activeTab = 0;
    
        $scope.$on('consumerDisconnect', function() {
            window.location = "/index.xhtml";
        });
        
        $scope.$on('consumerConnect', function() {
            if($scope.activeTab === 1) {
                $scope.nextTab();
            }
        });
        
        $scope.$on('consumerSubscribe', function() {
            if($scope.activeTab === 1) {
                $scope.nextTab();
            }
        });
        
        $scope.nextTab = function(param) {
            if($scope.activeTab === 0) {
                // Cart validation
                if (consumerService.isConnected) {
                    $scope.activeTab += 2;
                } else {
                    $scope.activeTab++;
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
        
        $scope.lastTab = function() {
            if($scope.activeTab > 0) {
                $scope.activeTab--;
            }
        };
    
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
            } else {
                return '';
            }
        }
  }]);
