/**
 * Cart directive, used to display the cart tab
 */
angular.module('common').directive('cartDirective', ['cartService', 'subscriptionService', function(cartService, subscriptionService) {
  return {
    restrict: 'E',
    replace: true,
    scope: {
    },

    link: function(scope, elm, attrs, ctrl) {
        scope.cartOpen = false; //If the cart tab is open or not
        scope.subOpen = false; //If the sub tab is open or not

        scope.cartService = cartService; //Cart service

        scope.subService = subscriptionService;

        /**
         * Toggle cart's tab
         */
        scope.toggleCart = function(event) {
            var position;
            if (scope.cartOpen) {
                position = "-350px";
            }
            else {
                position = "0px";
            }

            scope.cartOpen = !scope.cartOpen;
            if (!scope.subOpen) {
                elm.animate({"right": position}, 200);
            }
            else {
                scope.subOpen = false;
            }
        };

        /**
         * Toggle sub's tab
         */
        scope.toggleSub = function(event) {
            var position;
            if (scope.subOpen) {
                position = "-350px";
            }
            else {
                position = "0px";
            }

            scope.subOpen = !scope.subOpen;
            if (!scope.cartOpen) {
                elm.animate({"right": position}, 200);
            }
            else {
                scope.cartOpen = false;
            }
        };
        
        /**
         * Watch click on body to close tabs
         */
        angular.element("body").click(function() {
            if (scope.cartOpen) {
                scope.toggleCart();
            }
            else if(scope.subOpen) {
                scope.toggleSub();
            }
        });
        
        /**
         * Prevent click on cart to toggle cart
         * @param {type} event
         */
        scope.prevent = function(event) {
            event.stopPropagation();
        };
    },
    template: '<div id="cart">' +
                '<div id="cartTabs" ng-click="prevent($event)">' + 
                    '<div id="cartTab" ng-class="{\'active\': cartOpen}" ng-click="toggleCart($event)">' +
                        '<i class="fa fa-shopping-cart fa-4x"></i>' +
                        '<div>{{cartService.nbProducts()}} products</div>' +
                     '</div>' +
                     '<div id="subTab" ng-class="{\'active\': subOpen}" ng-click="toggleSub($event)">' +
                        '<i class="fa fa-shopping-cart fa-4x"></i>' +
                        '<div>{{subService.nbProducts()}} products</div>' +
                     '</div>' +
                 '</div>' +
                 '<div id="cartPanel" ng-click="prevent($event)">' +
                    '<div ng-show="cartOpen">' +
                        '<ul>' +
                            '<li class="prodCart" ng-repeat="prod in cartService.cart.products">' +
                                ' <img class="imgCart" ng-src="/assets/product/{{prod.pictureUrl}}"/>'+
            '{{prod.name}} quantity : \n\
<input type="text" class="inputQ" name="lname" ng-model="prod.quantity" ng-change="cartService.changeQuantity(prod)"/> \n\
<span>\n\
<img class="deleteProduct" ng-click="cartService.deleteProduct(prod)" src="/assets/product/supprimer.jpg"/>\n\
</span>' +
                            '</li>' +
                        '</ul>' +
                        'Price: {{cartService.cart.price}} €' +
                    '</div>' +
                    '<div ng-show="subOpen">' +
                        '<ul>' +
                            '<li class="prodCart" ng-repeat="prod in subService.sub.products">' +
                                ' <img class="imgCart" ng-src="/assets/product/{{prod.pictureUrl}}"/>'+
                                '{{prod.name}} quantity : \n\
<input type="text" class="inputQ" name="lname" ng-model="prod.quantity" ng-change="subService.changeQuantity(prod)"/> \n\
<span>\n\
<img class="deleteProduct" ng-click="subService.deleteProduct(prod)" src="/assets/product/supprimer.jpg"/>\n\
</span>' +

                            '</li>' +
                        '</ul>' +
                        'Price: {{subService.sub.price}} €' +
                    '</div>' +
                 '</div>' +
              '</div>'
  };
}]);
