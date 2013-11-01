commonModule.factory('cartService', ['$http', function($http) {
    
    var service = {};
    
    /**
     * Products in the cart
     */
    service.products = [];
    
    service.nbProducts = function() {
        return service.products.length;
    };
    
    service.addProduct = function(product) {
        $http.post("http://localhost:8080//webresources/cart/add", product.id)
            .success(function(data) {
                service.products.push(product);
            });
    };
    
    return service;
}]);


