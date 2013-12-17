/**
 * Service to manage connection and consumer information
 */
angular.module('common').factory('consumerService', ['$http', '$rootScope', '$q', function($http, $rootScope, $q) {
    
    var service = {};
    
    //////////////////////////////////////////////////
    //  ATTRIBUTES
    //////////////////////////////////////////////////
    
    /**
     * If the user is connected
     * @type {Boolean}
     */
    service.isConnected = false;
    
    /**
     * Consumer information
     */
    service.consumer = {};
    
    //////////////////////////////////////////////////
    //  USER METHODS
    //////////////////////////////////////////////////
    
    /**
     * Connect a user
     * @param {type} user
     * @returns {undefined}
     */
    var connectUser = function(user) {
        if(!service.isConnected) { //If the user is not already connected
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
                })
                .error(function(data, status, headers, config) {  // error
                    deferred.reject({status: status, data: data});
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
    
    //////////////////////////////////////////////////
    //  ADDRESS METHODS
    //////////////////////////////////////////////////
    
    /**
     * Add an address to the user
     * @param {type} address to add
     * @returns {promise}
     */
    service.addAddress = function(address) {
        return $http.post("/webresources/consumer/addAddress", address);
    };
    
    /**
     * Edit an address
     * @param {type} address to edit
     * @returns {promise}
     */
    service.editAddress = function(address) {
        return $http.post("/webresources/consumer/editAddress", address);
    };
    
    /**
     * Delete an address
     * @param {type} address to delete
     * @returns {promise}
     */
    service.deleteAddress = function(address) {
        return $http.post("/webresources/consumer/deleteAddress", address);
    };
    
    /**
     * Get the list of address
     * @returns {promise}
     */
    service.getAddress = function() {
        return $http.get("/webresources/consumer/address");
    };
    
    /**
     * Reset password
     * @returns {promise}
     */
    service.resetPassword = function(username) {
        return $http.post("/webresources/consumer/resetPassword", username);
    }
    
    //////////////////////////////////////////////////
    //  INITIALIZATION
    //////////////////////////////////////////////////
    
    /**
     * Request the server to get the user status
     */
    service.getCurrentUser();
    
    return service;
}]);
