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
    
    /**
     * function to load the cart
     * @returns {undefined}
     */
    service.getCart = function() {
        $http.post("/webresources/cart/getCart")
        .success(function(data){
            service.cart.price = 0;
            if(data !== null){
                service.cart.products = [];
                var prodColl = data.cartLineCollection;
                if(prodColl !== null){
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
               /* var diff = data - product.quantity;
                var notif;
                if(diff<0){
                    notif = -diff+" "+product.name+" ont été enlevé au panier";
                }
                else
                    notif = diff+" "+product.name+" ont été ajouté au panier";*/
                product.quantity = data;
                notificationService.displayMessage("Le nombre de "+product.name+" dans le panier a été modifié");
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
                notificationService.displayMessage("Le produit "+product.name+" a bien été supprimé du panier !");
            });
    };
    
    return service;
}]);


