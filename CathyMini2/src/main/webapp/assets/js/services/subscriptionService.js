angular.module('common').factory('subscriptionService', ['$http', '$rootScope', 'consumerService', function($http, $rootScope, $consumerService) {
    
    var service = {};
    
    /**
     * Products in the sub
     */
    service.sub = [];

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
                        prod.quantity = prodColl[i].quantity;
                        service.sub[j].products.push(prod);
                        service.sub[j].price += (prodColl[i].product.quantity * prodColl[i].product.price);
                    }
                }
            }
        });
    } );
    
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
                for(var j=0;j<service.sub.length;j++){
                    if(service.sub[j].name === name){
                        for(i;i<service.sub[j].products.length;i++){
                            if(service.sub[j].products[i].id === product.id){
                                found = true;
                                service.sub[j].products[i].quantity++;
                            }
                        }
                        if(!found){ //Else, new product for the cart
                            product.quantity = 1;
                            service.sub[j].products.push(product);
                        }
                        service.sub[j].price += product.price;
                    }
                }
                
                 //Increment the total price
            });
    };
    
    /**
     * Change the quantity for a product already in the sub
     * @param {Product} product
     * @param {String} name
     */
    service.changeQuantity = function(product, name) {        
        $http.post("/webresources/cart/changeQuantityToSub", {"productId": product.id, "quantity": product.quantity, "name": name})
                .success(function(data) {
                for(var i =0;i<service.sub.length;i++){
                    if(service.sub[i].name === name){
                        service.sub[i].price += (data *product.price)-(product.quantity*product.price);
                    }
                }
                product.quantity = data;
            })
                .error(function(data) {
                    alert("Un problème lors du changement de quantité a été déclenché!");
                });
    };
    
    service.editName = function(oldName, newName) {
         $http.post("/webresources/cart/changeName", {"oldName": oldName, "newName": newName})
                .success(function(data) {
                for(var j=0;j<service.sub.length;j++){
                    if(service.sub[j].name === oldName){
                        service.sub[j].name = newName;
                    }
                }
            })
                .error(function(data) {
                    alert("Un problème lors du changement de quantité a été déclenché!");
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
                        service.sub[i].price += -(q *product.price);
                    }
                }
                
            });
    };
    
    /**
     * Delete a product from the sub
     * @param {Product} product
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
     * 
     */
    service.newSubscription = function(){
        $http.post("/webresources/cart/newSubscription")
            .success(function(data) {
                    var newS = {products: [], price: 0, name: data, nbJ: 21};
                    service.sub.push(newS);
            });
    };
    
    /**
     * Change the subscription's name
     * @param {String} name 
     */
    service.changeName = function(name, oldName){
        $http.post("/webresources/cart/changeName", name, oldName)
            .success(function(data) {
                if(data !==""){
                    for(var i = 0; i<service.sub.length;i++){
                         service.sub[i].name = data;
                    }
                }
            });
    };
    
    return service;
}]);
