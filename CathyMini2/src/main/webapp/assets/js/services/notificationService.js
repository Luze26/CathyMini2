angular.module('common').factory('notificationService', ['$timeout', function($timeout) {
      var service = {};
      
      service.message = null;
      
      service.displayMessage = function(message, timeout) {
          var timer = timeout;
          
          service.message = message;
          if(!timeout) {
              timer = 3000;
          }
          
          $timeout(function() {
              if(service.message == message) {
                  service.message = null;
              }
          }, timer);
      };
      
      return service;
}]);


