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
            var zIndex;
            if(scope.subOpen) {
                position = "-350px";
                zIndex = 500;
            }
            else {
                position = "0px";
                zIndex = 1500;
            }
            
            scope.subOpen = !scope.subOpen;
            elm.animate({"right": position}, 200);
            
            elm.animate({"z-index": zIndex}, 200);
        };
    },
    template: '<div id="sub">' +
                '<div id="subTab" ng-click="toggleSub()">' +
                    '<i class="fa fa-shopping-sub fa-4x"></i>' +
                    '<div>{{subscriptionService.nbProducts()}} products</div>' +
                 '</div>' +
                 '<div id="subPanel">' +
                    '<ul>' +
                        '<li ng-repeat="prod in subscriptionService.sub.products">' +
                            '{{prod.name}} quantity : \n\
                        <input type="text" name="lname" ng-model="prod.quantity" ng-change="subscriptionService.changeQuantity(prod)"/> \n\
                <span>\n\
                                <img class="imgDelete" ng-click="subscriptionService.deleteProduct(prod)" src="/assets/product/supprimer.jpg"/>\n\
</span>' +
                        '</li>' +
                    '</ul>' +
                    'Price: {{subscriptionService.sub.price}} € </br>' +
                   'fréquence commande : \n\
                        <input type="text" name="lname" ng-model="nbJ" ng-change="subscriptionService.changeNbJ(nbJ)"/> '+

                 '</div>' +
              '</div>'
  };
}]);


