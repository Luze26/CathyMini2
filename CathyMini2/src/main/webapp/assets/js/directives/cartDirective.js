angular.module('common').directive('cartDirective', ['cartService', function(cartService) {
  return {
    restrict: 'E',
    replace: true,
    scope: {
    },
    link: function(scope, elm, attrs) {
        scope.nbProducts = cartService.nbProducts;
        
        scope.cartOpen = false;
        
        scope.cart = cartService.cart;
        
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
                    '<div>{{nbProducts()}} products</div>' +
                 '</div>' +
                 '<div id="cartPanel">' +
                    '<ul>' +
                        '<li ng-repeat="prod in cart.products">' +
                            '{{prod.name}} quantity : {{prod.quantity}}' +
                        '</li>' +
                    '</ul>' +
                    'Price: {{cart.price}} €' +
                 '</div>' +
              '</div>'
  };
}]);


