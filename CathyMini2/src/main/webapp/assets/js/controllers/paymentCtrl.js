angular.module('payment').
    controller('paymentCtrl', ['$scope', '$http', 'cartService', function($scope, $http, cartService) {
    
        // HTML Elements Ids
        $scope.paymentTabId = ['#cart-payementTab', '#login-payementTab', '#address-payementTab', 
            '#shipping-payementTab', '#payment-payementTab'];
        $scope.paymentBtnId = ['#cart-payementBtn', '#login-payementBtn', '#address-payementBtn', 
            '#shipping-payementBtn', '#payment-payementBtn'];
        $scope.LastNextBtnId = ['#payment-lastBtn','#payment-nextBtn'];
        
        // Initialize state variable
        $scope.currentTab = 0;
        $scope.NbElts = $scope.paymentBtnId.length;
    
        $scope.selectTab = function(select) {
            if(!angular.element($scope.paymentBtnId[select]).hasClass('disabled')) {
                $scope.activeTab(select);
            }
        };
        
        $scope.nextTab = function() {
            if ($scope.currentTab !== $scope.NbElts-1) {
                $scope.activeTab($scope.currentTab + 1);
            }
        };
        
        $scope.lastTab = function() {
            if ($scope.currentTab !== 0) {
                $scope.activeTab($scope.currentTab - 1);
            }
        };
    
        $scope.activeTab = function(select) {
            $scope.currentTab = select;
            
            for (var i = 0; i < $scope.NbElts ; i++) {
                if (i < select) {
                    angular.element($scope.paymentTabId[i]).addClass('hidden');
                    angular.element($scope.paymentBtnId[i]).removeClass('disabled');
                } else if (i === select) {
                    angular.element($scope.paymentTabId[i]).removeClass('hidden');
                    angular.element($scope.paymentBtnId[i]).removeClass('disabled');
                } else {
                    angular.element($scope.paymentTabId[i]).addClass('hidden');
                    angular.element($scope.paymentBtnId[i]).addClass('disabled');
                }
            }
            
            if ($scope.currentTab === 0) {
                angular.element($scope.LastNextBtnId[0]).addClass('disabled');
                angular.element($scope.LastNextBtnId[1]).removeClass('disabled');
            } else if ($scope.currentTab === $scope.NbElts-1) {
                angular.element($scope.LastNextBtnId[0]).removeClass('disabled');
                angular.element($scope.LastNextBtnId[1]).addClass('disabled');
            } else {
                angular.element($scope.LastNextBtnId[0]).removeClass('disabled');
                angular.element($scope.LastNextBtnId[1]).removeClass('disabled');
            }
        };

        // Initialize start HTML document properties
        $scope.activeTab($scope.currentTab);
  }]);
