/**
 * Service to manage the cart
 */
angular.module('common').factory('cartService', ['$http', function($http) {
    
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
     * Add a product to the cart
     * @param {Product} product
     */
    service.addProduct = function(product) {
        $http.post("http://localhost:8080//webresources/cart/add", product.id)
            .success(function(data) {
                //If the product is already in the cart, we increase its quantity
                if(service.cart.products.indexOf(product) !== -1) { 
                    product.quantity++;
                }
                else { //Else, new product for the cart
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
        $http.post("http://localhost:8080//webresources/cart/changeQuantity", {"id": product.id, "quantity": quantity})
                .success(function(data) {
            });
    };
    
    /**
     * Delete a product from the cart
     * @param {Product} product
     */
    service.deleteProduct = function(product) {
        $http.post("http://localhost:8080//webresources/cart/delete", product.id)
            .success(function(data) {
            });
    };
    
    return service;
}]);


