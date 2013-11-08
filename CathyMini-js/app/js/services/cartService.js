commonModule.factory('cartService', ['$http', function($http) {
    
    var service = {};
    
    /**
     * Products in the cart
     */
    service.cart = {products: [], price: 0};

    service.nbProducts = function() {
        return service.cart.products.length;
    };
    
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
    
    return service;
}]);


