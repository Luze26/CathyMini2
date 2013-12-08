/**
 * Cart directive, used to display the cart tab
 */
angular.module('common').directive('subscriptionDirective', ['subscriptionService', function(subscriptionService) {
  return {
    restrict: 'E',
    replace: true,
    scope: {
    },
    link: function(scope, elm, attrs, ctrl) {      
        scope.subOpen = false; //If the tab is open or not
        
        scope.subscriptionService = subscriptionService; //Cart service

        /**
         * Toggle cart's tab
         */
        scope.toggleSub = function() {
            var position;
            if(scope.subOpen) {
                position = "-750px";
            }
            else {
                position = "0px";
            }
            
            scope.subOpen = !scope.subOpen;
            elm.animate({"right": position}, 300);
        };
    },
    template: '<div id="sub">' +
                '<div id="subTab" ng-click="toggleSub()">' +
                    '<i class="fa fa-shopping-cart fa-4x"></i>' +
                    '<div>{{subscriptionService.nbProducts()}} products</div>' +
                 '</div>' +
                 '<div id="subPanel">' +
                    '<ul>' +
                        '<li ng-repeat="prod in subscriptionService.cart.products">' +
                            '{{prod.name}} quantity : \n\
                        <input type="text" name="lname" ng-model="prod.quantity" ng-change="subscriptionService.changeQuantity(prod)"/> \n\
                <span>\n\
                                <img class="imgGallery" ng-click="subscriptionService.deleteProduct(prod)" src="/assets/product/supprimer.jpg"/>\n\
</span>' +
                        '</li>' +
                    '</ul>' +
                    'Price: {{subscriptionService.cart.price}} €' +
                 '</div>' +
              '</div>'
  };
}]);


