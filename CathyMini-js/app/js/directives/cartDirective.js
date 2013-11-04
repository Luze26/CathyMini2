commonModule.directive('cartDirective', ['cartService', function(cartService) {
  return {
    restrict: 'E',
    replace: true,
    scope: {
    },
    link: function(scope, elm, attrs, ctrl) {
        scope.nbProducts = cartService.nbProducts;
        
        scope.cartOpen = false;
        
        scope.products = cartService.products;
        
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
                    '<i class="fa fa-shopping-cart fa-3"></i>{{nbProducts()}} products' +
                 '</div>' +
                 '<div id="cartPanel">' +
                    '<ul>' +
                        '<li ng-repeat="prod in products">' +
                            '{{prod.name}}' +
                        '</li>' +
                    '</ul>' +
                 '</div>' +
              '</div>'
  };
}]);


