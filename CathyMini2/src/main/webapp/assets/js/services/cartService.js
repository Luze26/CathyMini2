angular.module('common').factory('cartService', ['$http', '$rootScope', 'consumerService', function($http, $rootScope, consumerService) {
    
    var service = {};
    
    /**
     * Products in the cart
     */
    service.cart = {products: [], price: 0};

    service.nbProducts = function() {
        return service.cart.products.length;
    };

    
    
    $rootScope.$on('consumerConnect',service.consumerIsConnected = function (){
        console.log("Dnas bonne fonction");
        $http.post("http://localhost:8080//webresources/cart/consumerIsConnected")
        .success(function(data){
        });
    } );
    

    
    service.addProduct = function(product) {
        $http.post("http://localhost:8080//webresources/cart/add", product.id)
            .success(function(data) {
                if(service.cart.products.indexOf(product) !== -1) {
                    product.quantity++;
                }
                else {
                    product.quantity = 1;
                    service.cart.products.push(product);
                }
                service.cart.price += product.price;
            });
    };
    
    service.changeQuantity = function(product) {
        $http.post("http://localhost:8080//webresources/cart/changeQuantity", {"id": product.id, "quantity": quantity})
                .success(function(data) {
            });
    };
    
    service.deleteProduct = function(product) {
        $http.post("http://localhost:8080//webresources/cart/delete", product.id)
            .success(function(data) {
            });
    };
    
    return service;
}]);


