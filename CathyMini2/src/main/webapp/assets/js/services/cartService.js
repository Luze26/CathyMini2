angular.module('common').factory('cartService', ['$http', '$rootScope', 'consumerService', 'notificationService', function($http, $rootScope, $consumerService, notificationService) {
    
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
    
    var refreshPrice = function() {
        var price = 0;
        for(var i = 0; i < service.cart.products.length; i++) {
            price += service.cart.products[i].quantity * service.cart.products[i].price;
        }
        service.cart.price = price;
    };
    
    /**
     * function to load the cart
     * @returns {undefined}
     */
    service.getCart = function() {
        $http.post("/webresources/cart/getCart")
        .success(function(data){
            service.cart.price = 0;
            if(data !== null && data !== ""){
                service.cart.products = [];
                var prodColl = data.cartLineCollection;
                if(prodColl){
                    for( var i = 0;i<prodColl.length; i++){
                        var prod = prodColl[i].product;
                        prod.quantity = parseInt(prodColl[i].quantity);
                        service.cart.products.push(prod);
                        service.cart.price += (prodColl[i].product.price * prodColl[i].product.quantity);
                    }
                }
                refreshPrice();
            }
        });
    };
    
    /**
     * load the cart
     */
    service.getCart();
    
    /**
     * load the cart when the consumer been connected
     */
    $rootScope.$on('consumerConnect',service.consumerIsConnected = function (){
        service.getCart();
    });
    
    /**
     * function called when a user disconnect
     */
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
                var notif;
                for(i;i<service.cart.products.length;i++){
                    if(service.cart.products[i].id === product.id){
                        found = true;
                        service.cart.products[i].quantity++;
                        notif = "Et un "+service.cart.products[i].name+" de plus dans le panier !";
                    }
                }                
                if(!found){ //Else, new product for the cart
                    product.quantity = 1;
                    service.cart.products.push(product);
                    notif = "Le produit "+service.cart.products[i].name+" a été ajouté au panier !";
                }
                notificationService.displayMessage(notif);
                refreshPrice(); //Increment the total price
            });
    };
    
    /**
     * Change the quantity for a product already in the cart
     * @param {Product} product
     */
    service.changeQuantity = function(product) {
        if(product.quantity < 0) {
            product.quantity = 0;
            refreshPrice();
            return;
        }
        $http.post("/webresources/cart/changeQuantityToCart", {"productId": product.id, "quantity": product.quantity})
                .success(function(data) {
                product.quantity = parseInt(data);
                refreshPrice();
                notificationService.displayMessage("Le nombre de "+product.name+" dans le panier a été modifié");
            })
            .error(function(data) {
                notificationService.displayMessage("Un problème lors du changement de quantité a été déclenché!");
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
                notificationService.displayMessage("Le produit "+product.name+" a bien été supprimé du panier !");
            });
    };
    
    return service;
}]);


