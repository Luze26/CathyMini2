/**
 * Service to manage connection and consumer information
 */
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
    
    var connectUser = function(user) {
        if(!service.isConnected) {
            service.consumer.username = user.username;
            service.isConnected = true;
            $rootScope.$broadcast('consumerConnect', user);
        }        
    };
    
    /**
     * Logout a connected user
     */
    service.disconnect = function() {        
        $http.post("/webresources/consumer/logout")
            .success(function() { 
                service.isConnected = false;
                $rootScope.$broadcast('consumerDisconnect');
            })
            .error(function(data, status, headers, config) { 
                service.isConnected = false;
                $rootScope.$broadcast('consumerDisconnect');
            });
    };
    
    /**
     * Try to connect the user
     * @param {type} consumer, username and password
     * @returns {promise}
     */
    service.connect = function(consumer) {     
        var deferred = $q.defer();
        $http.post("/webresources/consumer/connect", consumer)
                .success(function(user) { //success
                    connectUser(user);
                    deferred.resolve();
                    $rootScope.$broadcast('consumerConnect');
                })
                .error(function(data, status, headers, config) {  // error
                    deferred.reject({status: status, data: data});
                    $rootScope.$broadcast('consumerConnect');
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
        $http.post("/webresources/consumer/subscribe", subscriber)
            .success(function(user) { 
                connectUser(user);
                deferred.resolve();
                $rootScope.$broadcast('consumerSubscribe');
            })
            .error(function(data, status, headers, config) {
                deferred.reject({status: status, data: data});
                $rootScope.$broadcast('consumerSubscribe');
            });
        return deferred.promise;
    };
    
    /**
     * Get current user information on the server
     */
    service.getCurrentUser = function() {
        $http.get("/webresources/consumer/seeCurrent")
            .success(function(user) { // success
                if(user) {
                    connectUser(user);
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
    
    service.addAddress = function(address) {
        return $http.post("/webresources/consumer/addAddress", address);
    };
    
    service.editAddress = function(address) {
        return $http.post("/webresources/consumer/editAddress", address);
    };
    
    service.getAddress = function() {
        return $http.get("/webresources/consumer/address");
    };
    
    return service;
}]);
