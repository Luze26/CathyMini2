commonModule.factory('searchService', ['$http', function($http) {
    
    var service = {};
    
    /**
     * Products in the cart
     */
    //service.search = {products: [], price: 0};

    service.nbProducts = function() {
        return service.cart.products.length;
    };
    
    return service;
}]);


/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


