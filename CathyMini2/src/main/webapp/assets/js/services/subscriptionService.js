angular.module('common').factory('subscriptionService', ['$http', '$rootScope', 'consumerService', function($http, $rootScope, $consumerService) {
    
    var service = {};
    
    /**
     * Products in the sub
     */
    service.sub = {products: [], price: 0};

    service.nbJ = 21;
    /**
     * Number of different products in the sub
     * @returns {service.sub.products.length|Number}
     */
    service.nbProducts = function() {
        return service.sub.products.length;
    };

    
    
    $rootScope.$on('consumerConnect',service.consumerIsConnected = function (){
        $http.post("/webresources/cart/getSub")
        .success(function(data){
            service.sub.price = 0;
            if(data != null){
                service.sub.products = [];
                var prodColl = data.cartLineCollection;
                for( var i = 0;i<prodColl.length; i++){
                    var prod = prodColl[i].product;
                    prod.quantity = prodColl[i].quantity;
                    service.sub.products.push(prod);
                    service.sub.price += prodColl[i].product.price;
                }
            }
        });
    } );
    
     $rootScope.$on('consumerDisconnect',function (){
                service.sub.products = [];
                service.sub.price = 0;
        });
    
    /**
     * Add a product to the sub
     * @param {Product} product
     */
    service.addProduct = function(product) {
        $http.post("/webresources/cart/addProductToSub", product.id)
            .success(function(data) {
                //If the product is already in the sub, we increase its quantity
                var i = 0;
                var found = false;
                for(i;i<service.sub.products.length;i++){
                    if(service.sub.products[i].id === product.id){
                        found = true;
                        service.sub.products[i].quantity++;
                    }
                }                
                if(!found){ //Else, new product for the cart
                    product.quantity = 1;
                    service.sub.products.push(product);
                }
                service.sub.price += product.price; //Increment the total price
            });
    };
    
    /**
     * Change the quantity for a product already in the sub
     * @param {Product} product
     */
    service.changeQuantity = function(product) {        
        $http.post("/webresources/cart/changeQuantityToSub", {"productId": product.id, "quantity": product.quantity})
                .success(function(data) {
                product.quantity = data;
            })
                .error(function(data) {
                    alert("Un problème lors du changement de quantité a été déclenché!");
                })
    };
    
    /**
     * Delete a product from the sub
     * @param {Product} product
     */
    service.deleteProduct = function(product) {
        $http.post("/webresources/sub/deleteToSub", product.id)
            .success(function(data) {
                service.sub.products.splice(data,1);
                service.sub.price += -product.price;
            });
    };
    
    /**
     * Delete a product from the sub
     * @param {Product} product
     */
    service.changeNbJ = function(nbJ) {
        $http.post("/webresources/cart/changeNbJ", nbJ)
            .success(function(data) {
            service.nbJ = data;
            });
    };
    
    return service;
}]);
