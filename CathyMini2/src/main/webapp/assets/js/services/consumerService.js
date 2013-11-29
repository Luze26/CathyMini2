angular.module('common').factory('consumerService', ['$http', '$rootScope', '$q', function($http, $rootScope, $q) {
    
    var service = {};
    
    /**
     * If the user is connected
     * @type {Boolean}
     */
    service.isConnected = false;
    
    /**
     * Consumer information
     */
    service.consumer = {};
    
    /**
     * Logout a connected user
     */
    service.disconnect = function() {        
        $http.post("http://localhost:8080//webresources/consumer/logout")
            .success(function() { 
                service.isConnected = false;
                $rootScope.$broadcast('consumerDisconnect');
            })
            .error(function(data, status, headers, config) { 
                service.isConnected = false;
            });
    };
    
    /**
     * Try to connect the user
     * @param {type} consumer, username and password
     * @returns {promise}
     */
    service.connect = function(consumer) {     
        var deferred = $q.defer();
        $http.post("http://localhost:8080//webresources/consumer/connect", consumer)
                .success(function() { //success
                    service.consumer.username = consumer.user;
                    service.isConnected = true;
                    deferred.resolve();
                    $rootScope.$broadcast('consumerConnect');
                })
                .error(function(data, status, headers, config) {  // error
                    if (status === 400) {
                        deferred.reject(data);
                    }
                    else {
                        deferred.reject("Connection error");
                    }
                });
        return deferred.promise;
    };
    
    /**
     * Used to subscribe an user
     * @param {type} subscriber
     * @returns {promise}
     */
    service.subscribe = function(subscriber) {
        var deferred = $q.defer();
        $http.post("http://localhost:8080//webresources/consumer/suscribe", subscriber)
            .success(function() { 
                service.consumer.username = subscriber.username;
                service.isConnected = true;
                deferred.resolve();
                $rootScope.$broadcast('consumerConnect');
            })
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
     * Get current user information on the server
     */
    service.getCurrentUser = function() {
        $http.get("http://localhost:8080//webresources/consumer/seeCurrent")
            .success(function(username) { // success
                if (username === "") {
                    service.isConnected = false;
                } else {
                    service.isConnected = true;
                    service.consumer.username = username;
                }
            })
            .error(function(data, status, headers, config) { // error
                service.isConnected = false;
            });
    };
    
    /**
     * Request the server to get the user status
     */
    service.getCurrentUser();
    
    return service;
}]);
