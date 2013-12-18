angular.module('common').factory('cartService', ['$http', '$rootScope', 'consumerService', function($http, $rootScope, $consumerService) {
    
    var service = {};
    
    /**
     * Products in the cart
     */
    service.cart = {products: [], price: 0};

    /**
     * Number of different products in the cart
     * @returns {service.cart.products.length|Number}
     */
    service.nbProducts = function() {
        return service.cart.products.length;
    };
    
    
    service.getCart = function() {
        $http.post("/webresources/cart/getCart")
        .success(function(data){
            service.cart.price = 0;
            if(data !== null){
                service.cart.products = [];
                var prodColl = data.cartLineCollection;
                if(prodColl != null){
                    for( var i = 0;i<prodColl.length; i++){
                        var prod = prodColl[i].product;
                        prod.quantity = prodColl[i].quantity;
                        service.cart.products.push(prod);
                        service.cart.price += (prodColl[i].product.price * prodColl[i].product.quantity);
                    }
                }
            }
        });
    };
    service.getCart();
    
    /**
     * load the cart when the consumer been connected
     */
    $rootScope.$on('consumerConnect',service.consumerIsConnected = function (){
        service.getCart();
    });
    
     $rootScope.$on('consumerDisconnect',function (){
                service.cart.products = [];
                service.cart.price = 0;
        });
    
    /**
     * Add a product to the cart
     * @param {Product} product
     */
    service.addProduct = function(product) {
        $http.post("/webresources/cart/addProductToCart", product.id)
            .success(function(data) {
                //If the product is already in the cart, we increase its quantity
                var i = 0;
                var found = false;
                for(i;i<service.cart.products.length;i++){
                    if(service.cart.products[i].id === product.id){
                        found = true;
                        service.cart.products[i].quantity++;
                    }
                }                
                if(!found){ //Else, new product for the cart
                    product.quantity = 1;
                    service.cart.products.push(product);
                }
                service.cart.price += product.price; //Increment the total price
            });
    };
    
    /**
     * Change the quantity for a product already in the cart
     * @param {Product} product
     */
    service.changeQuantity = function(product) {        
        $http.post("/webresources/cart/changeQuantityToCart", {"productId": product.id, "quantity": product.quantity})
                .success(function(data) {
                service.cart.price += (data *product.price)-(product.quantity*product.price);
                product.quantity = data;
            })
                .error(function(data) {
                    alert("Un problème lors du changement de quantité a été déclenché!");
                });
    };
    
    /**
     * Delete a product from the cart
     * @param {Product} product
     */
    service.deleteProduct = function(product) {
        var q = product.quantity;
        $http.post("/webresources/cart/deleteToCart", product.id)
            .success(function(data) {
                service.cart.products.splice(data,1);
                service.cart.price += -(q*product.price);
            });
    };
    
    return service;
}]);


