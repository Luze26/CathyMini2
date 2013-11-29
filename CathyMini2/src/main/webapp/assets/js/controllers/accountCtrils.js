angular.module('account')
.controller('accountSettingCtrl', ['$scope', 'consumerService', function($scope, consumerService) {

    $scope.consumer = consumerService.getConsumer();
    
}]);
