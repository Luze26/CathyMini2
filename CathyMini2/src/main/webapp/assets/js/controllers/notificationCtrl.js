/**
 * Notification controller, used to display notifications
 */
angular.module('common').
    controller('notificationCtrl', ['$scope', 'notificationService', function($scope, notificationService) {
    
        $scope.notificationService = notificationService;
        
  }]);
