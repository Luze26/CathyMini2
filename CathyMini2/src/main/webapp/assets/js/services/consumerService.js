angular.module('common').factory('consumerService', ['$http', '$rootScope', '$q', function($http, $rootScope, $q) {
    
    var service = {};
    
    service.isConnected = false;
    
    service.consumer = {};
    
    /**
     * Logout a connected user
     */
    service.disconnect = function() {        
        $http.post("http://localhost:8080//webresources/consumer/logout")
            .success(function() { 
                service.isConnected = false;
            })
            .error(function(data, status, headers, config) { 
                service.isConnected = false;
            });
    };
    
    service.connect = function(consumer) {     
        var deferred = $q.defer();
        $http.post("http://localhost:8080//webresources/consumer/connect", consumer)
                .success(function() {
                    service.consumer.username = consumer.user;
                    service.isConnected = true;
                    deferred.resolve();
                    $rootScope.$broadcast('consumerConnect');
                    console.log("apres broadcast");
                })
                .error(function(data, status, headers, config) { 
                    if (status === 400) {
                        deferred.reject(data);
                    }
                    else {
                        deferred.reject("Connection error");
                    }
                });
        return deferred.promise;
    };
    
    service.subscribe = function(subscriber) {
        var deferred = $q.defer();
        $http.post("http://localhost:8080//webresources/consumer/suscribe", subscriber)
            .success(function() { 
                service.consumer.login = subscriber.username;
                deferred.resolve();})
            .error(function(data, status, headers, config) {
                if (status === 400) {
                    deferred.reject(data);
                } else {
                    deferred.reject("Connection error");
                }
            });
        return deferred.promise;
    };
    
    /**
     * Get user session infos if the user is connected
     */
    service.getCurrentUser = function() {
        $http.get("http://localhost:8080//webresources/consumer/seeCurrent")
            .success(function(user) { 
                if (user === "") {
                    service.isConnected = false;
                } else {
                    service.isConnected = true;
                    service.consumer = user;
                }
            })
            .error(function(data, status, headers, config) { 
                service.isConnected = false;
            });
    };
    
    service.getCurrentUser();
    
    return service;
}]);