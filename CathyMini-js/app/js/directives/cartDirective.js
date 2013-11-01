commonModule.directive('cartDirective', ['cartService', function(cartService) {
  return {
    restrict: 'E',
    replace: true,
    link: function(scope, elm, attrs, ctrl) {
        scope.nbProducts = cartService.nbProducts;
    },
    template: "<div>Cart: {{nbProducts()}} products</div>"
  };
}]);


