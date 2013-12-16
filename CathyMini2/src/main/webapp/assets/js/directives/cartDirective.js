/**
 * Cart directive, used to display the cart tab
 */
angular.module('common').directive('cartDirective', ['cartService', function(cartService) {
  return {
    restrict: 'E',
    replace: true,
    scope: {
    },
    link: function(scope, elm, attrs, ctrl) {      
        scope.cartOpen = false; //If the tab is open or not
        
        scope.cartService = cartService; //Cart service

        /**
         * Toggle cart's tab
         */
        scope.toggleCart = function() {
            var position;
            var zIndex;
            if(scope.cartOpen) {
                position = "-350px";
                zIndex = 1500;
            }
            else {
                position = "0px";
                zIndex = 500;
            }
            
            scope.cartOpen = !scope.cartOpen;
            elm.animate({"right": position}, 200);
            elm.animate({"z-index": zIndex}, 200);
        };
    },
    template: '<div id="cart">' +
                '<div id="cartTab" ng-click="toggleCart()">' +
                    '<i class="fa fa-shopping-cart fa-4x"></i>' +
                    '<div>{{cartService.nbProducts()}} products</div>' +
                 '</div>' +
                 '<div id="cartPanel">' +
                    '<ul>' +
                        '<li class="prodCart" ng-repeat="prod in cartService.cart.products">' +
                            ' <img class="imgCart" ng-src="/assets/product/{{prod.pictureUrl}}"/>'+

                            '{{prod.name}} quantity : '+
                        '<input type="text" class="inputQ" name="lname" ng-model="prod.quantity" ng-change="cartService.changeQuantity(prod)"/> \n\
                <span>\n\
                                <img class="imgDelete" ng-click="cartService.deleteProduct(prod)" src="/assets/product/supprimer.jpg"/>\n\
</span>' +
                        '</li>' +
                    '</ul>' +
                    'Price: {{cartService.cart.price}} €' +
                 '</div>' +
              '</div>'
  };
}]);


