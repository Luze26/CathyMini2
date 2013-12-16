angular.module('common').
    controller('notificationCtrl', ['$scope', 'notificationService', function($scope, notificationService) {
    
        $scope.notificationService = notificationService;
        
  }]);
