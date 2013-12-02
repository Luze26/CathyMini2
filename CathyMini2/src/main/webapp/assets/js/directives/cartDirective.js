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
        
        scope.cart = cartService.cart; //Cart service
        
        /**
         * Toggle cart's tab
         */
        scope.toggleCart = function() {
            var position;
            if(scope.cartOpen) {
                position = "-350px";
            }
            else {
                position = "0px";
            }
            
            scope.cartOpen = !scope.cartOpen;
            elm.animate({"right": position}, 200);
        };
    },
    template: '<div id="cart">' +
                '<div id="cartTab" ng-click="toggleCart()">' +
                    '<i class="fa fa-shopping-cart fa-4x"></i>' +
                    '<div>{{cart.nbProducts()}} products</div>' +
                 '</div>' +
                 '<div id="cartPanel">' +
                    '<ul>' +
                        '<li ng-repeat="prod in cart.products">' +
                            '{{prod.name}} quantity : \n\
                        <input type="text" name="lname" value="{{prod.quantity}}" onchange="cartPanel.cart.changeQuantity(prod.quantity)"/> \n\
                <span>\n\
                                <img class="imgGallery" ng-click="$parent.cart.deleteProduct(prod)" src="/assets/product/supprimer.jpg"/>\n\
</span>' +
                        '</li>' +
                    '</ul>' +
                    'Price: {{cart.price}} €' +
                 '</div>' +
              '</div>'
  };
}]);


