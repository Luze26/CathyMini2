angular.module('common').factory('subscriptionService', ['$http', '$rootScope', 'consumerService', '$q', 'notificationService', function($http, $rootScope, $consumerService, $q, notificationService) {
    
    var service = {};
    
    /**
     * Products in the sub
     */
    service.sub = [];

    var refreshPrice = function(sub) {
        var price = 0;
        for(var i = 0; i < sub.products.length; i++) {
            price += sub.products[i].quantity * sub.products[i].price;
        }
        sub.price = price;
    };
    
    /**
     * Number of different products in the sub
     * @returns {service.sub.products.length|Number}
     */
    service.nbProducts = function(name) {
        for(var i = 0;i < service.sub.length; i++){
            if(service.sub[i] === name)
                return service.sub[i].products.length;
        }
        return "";
    };

    /**
     * function called when the site is load to fill the subscription
     */
    $http.post("/webresources/cart/getSub")
        .success(function(data){
            service.sub.price = 0;
            if(data !== null){
                service.sub = [];
                for(var j = 0; j < data.length; j++){
                    var prodColl = data[j].cartLineCollection;
                    service.sub.push({products: [], price: 0, name: data[j].name, nbJ: data[j].nbJ});
                    for( var i = 0;i<prodColl.length; i++){
                        var prod = prodColl[i].product;
                        prod.quantity = parseInt(prodColl[i].quantity);
                        service.sub[j].products.push(prod);
                        service.sub[j].price += (prodColl[i].product.quantity * prodColl[i].product.price);
                    }
                }
                $rootScope.$broadcast('subLoaded');
            }
        });
    
    /**
     * function called when a consumer is connected
     */
    $rootScope.$on('consumerConnect',service.consumerIsConnected = function (){
        $http.post("/webresources/cart/getSub")
        .success(function(data){
            service.sub.price = 0;
            if(data !== null){
                service.sub = [];
                for(var j = 0; j < data.length; j++){
                    var prodColl = data[j].cartLineCollection;
                    service.sub.push({products: [], price: 0, name: data[j].name, nbJ: data[j].nbJ});
                    for( var i = 0;i<prodColl.length; i++){
                        var prod = prodColl[i].product;
                        prod.quantity = parseInt(prodColl[i].quantity);
                        service.sub[j].products.push(prod);
                        service.sub[j].price += (prodColl[i].product.quantity * prodColl[i].product.price);
                    }
                }
            }
        });
    } );
    
    /**
     * function called when the consumer disconnect
     */
     $rootScope.$on('consumerDisconnect',function (){
                service.sub = [];
                //service.sub.price = 0;
        });
    
    /**
     * Add a product to the sub
     * @param {Product} product
     * @param {String} name
     */
    service.addProduct = function(product, name) {
        var quantity = 1;
        $http.post("/webresources/cart/addProductToSub", {"productId": product.id, "quantity": quantity, "name":name})
            .success(function(data) {
                //If the product is already in the sub, we increase its quantity
                var i = 0;
                var found = false;
                var notif;
                for(var j=0;j<service.sub.length;j++){
                    if(service.sub[j].name === name){
                        for(i;i<service.sub[j].products.length;i++){
                            if(service.sub[j].products[i].id === product.id){
                                found = true;
                                service.sub[j].products[i].quantity++;
                                notif = "Et un "+service.sub[j].products[i].name+" de plus dans l'abonnement "+service.sub[j].name;
                            }
                        }
                        if(!found){ //Else, new product for the cart
                            product.quantity = 1;
                            service.sub[j].products.push(product);
                            notif = "Le produit "+service.sub[j].products[i].name+" a été ajouté à l'abonnement "+service.sub[j].name+" !";
                        }
                        refreshPrice(service.sub[j]);
                        notificationService.displayMessage(notif);
                        return;
                    }
                }
            });
    };
    
    /**
     * Change the quantity for a product already in the sub
     * @param {Product} product
     * @param {String} name
     */
    service.changeQuantity = function(product, name) {    
        if(product.quantity < 0) {
            product.quantity = 0;
            return;
        }
        $http.post("/webresources/cart/changeQuantityToSub", {"productId": product.id, "quantity": product.quantity, "name": name})
                .success(function(data) {
                for(var i =0;i<service.sub.length;i++){
                    if(service.sub[i].name === name){
                        for(var j = 0; j<service.sub[i].products.length;j++){
                            if(service.sub[i].products[j].id === product.id){
                               service.sub[i].products[j].quantity = parseInt(data);
                               notificationService.displayMessage("Le nombre de "+service.sub[i].products[j].name+" dans l'abonnement "+service.sub[i].name+" a été modifié");

                            }
                        }
                        refreshPrice(service.sub[i]);
                        return;
                    }
                }
                //product.quantity = data;
            })
            .error(function(data) {
                notificationService.displayMessage("Un problème lors du changement de quantité a été déclenché!");
            });
    };
    
    /**
     * called when the user want to change a subscription name
     * @param {string} oldName
     * @param {string} newName
     * @returns {undefined}
     */
    service.editName = function(oldName, newName) {
         $http.post("/webresources/cart/changeName", {"oldName": oldName, "newName": newName})
                .success(function(data) {
                for(var j=0;j<service.sub.length;j++){
                    if(service.sub[j].name === oldName){
                        service.sub[j].name = newName;
                        notificationService.displayMessage("L'abonnement "+oldName+" s'appelle maintenant "+newName+" !");

                    }
                }
            })
            .error(function(data) {
                notificationService.displayMessage("Un problème lors du changement de nom a été déclenché!");
            });          
       };
    
    /**
     * Delete a product from the sub
     * @param {Product} product
     */
    service.deleteProduct = function(product, name) {
        var q = product.quantity;
        $http.post("/webresources/cart/deleteToSub", {"productId": product.id, "quantity": product.quantity, "name": name})
            .success(function(data) {
                for(var i =0;i<service.sub.length;i++){
                    if(service.sub[i].name === name){
                        service.sub[i].products.splice(data,1);
                        notificationService.displayMessage("Le produit "+product.name+" a bien été supprimé de l'abonnement "+service.sub[i].name+" !");
                        refreshPrice(service.sub[i]);
                        return;
                    }
                }
                
            });
    };
    
    /**
     * called when the user want to change the number of day
     * @param {int} nbJ
     * @param {String} name
     * @returns {undefined}
     */
    service.changeNbJ = function(nbJ, name) {
        $http.post("/webresources/cart/changeNbJ", nbJ, name)
            .success(function(data) {
                for(var i = 0;i<service.sub.length;i++){
                    if(service.sub[i].name === name){
                        service.sub[i].nbJ = data;
                    }
                }                
            });
    };
    
    /**
     * function called when the user create a new subscription
     */
    service.newSubscription = function(){
        var deferred = $q.defer();
        $http.post("/webresources/cart/newSubscription")
            .success(function(data) {
                    var newS = {products: [], price: 0, name: data, nbJ: 21};
                    service.sub.push(newS);
                    deferred.resolve(newS);
            });
        return deferred.promise;
    };
    
    
    return service;
}]);
