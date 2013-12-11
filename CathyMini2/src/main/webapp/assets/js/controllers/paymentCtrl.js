angular.module('payment').
    controller('paymentCtrl', ['$scope', '$http', 'cartService', function($scope, $http, cartService) {
    
        $scope.paymentTab = ['#cart-payementTab', '#login-payementTab', '#address-payementTab', 
            '#shipping-payementTab', '#payment-payementTab'];
        
        $scope.paymentBtn = ['#cart-payementBtn', '#login-payementBtn', '#address-payementBtn', 
            '#shipping-payementBtn', '#payment-payementBtn'];
    
        $scope.selectTab = function(select) {
            if(!angular.element(select).hasClass('disabled')) {
                $scope.activeTab(select);
            }
        };
        
        $scope.nextTab = function() {
            var found = false;
            for (var i = 1; i < $scope.paymentBtn.length ; i++) {
                if(angular.element($scope.paymentBtn[i]).hasClass('disabled') && !found) {
                    $scope.activeTab($scope.paymentBtn[i]);
                    found = true;
                }
            }
        };
        
        $scope.lastTab = function() {
                var found = false;
                for (var i = 2; i < $scope.paymentBtn.length ; i++) {
                    if(angular.element($scope.paymentBtn[i]).hasClass('disabled') && !found) {
                        $scope.activeTab($scope.paymentBtn[i-2]);
                        found = true;
                    }
                }
                
                if (!found)
                    $scope.activeTab($scope.paymentBtn[$scope.paymentBtn.length-2]);
        };
    
        $scope.activeTab = function(select) {
            var found = false;
            for (var i = 0; i < $scope.paymentBtn.length ; i++) {
                
                if($scope.paymentBtn[i] === select) {
                    angular.element($scope.paymentTab[i]).removeClass('hidden');
                    angular.element($scope.paymentBtn[i]).removeClass('disabled');
                    found = true;
                } else if(!found) {
                    angular.element($scope.paymentTab[i]).addClass('hidden');
                    angular.element($scope.paymentBtn[i]).removeClass('disabled');
                } else {
                    angular.element($scope.paymentTab[i]).addClass('hidden');
                    angular.element($scope.paymentBtn[i]).addClass('disabled');
                }
            }
        };

  }]);
