/**
 * Cart directive, used to display the cart tab
 */
angular.module('common').directive('cartDirective', ['$rootScope', 'cartService', 'subscriptionService', function($rootScope, cartService, subscriptionService) {
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
            scope.$apply(function() {
                if (scope.cartOpen) {
                    scope.toggleCart();
                }
                else if(scope.subOpen) {
                    scope.toggleSub();
                }
            });
        });
        
        /**
         * Prevent click on cart to toggle cart
         * @param {type} event
         */
        scope.prevent = function(event) {
            event.stopPropagation();
        };
        
        scope.selectedSub = null;
        
        /**
         * return the list of product of the selected subscription
         * @returns {@exp;sub@pro;products|unresolved}
         */
        scope.getSubProducts = function() {
            for(var i = 0; i < subscriptionService.sub.length; i++) {
                var sub = subscriptionService.sub[i];
                if(scope.selectedSub !== null){
                    if(sub.name === scope.selectedSub.name) {
                        return sub.products;
                    }
                }
                else{
                    return null;
                }
            }
        };
        
        /**
         * return the name of the selected subscription
         * @returns {unresolved|@exp;sub@pro;name}
         */
        scope.getNameSub = function() {
            for(var i = 0; i < subscriptionService.sub.length; i++) {
                var sub = subscriptionService.sub[i];
                if(scope.selectedSub !== null){
                    if(sub.name === scope.selectedSub.name) {
                        return sub.name;
                    }
                }
                else{
                    return null;
                }
            }
            
        };
        
        /**
         * return the price of the selected subscription
         * @returns {@exp;sub@pro;price|unresolved}
         */
        scope.getPriceSub = function() {
            for(var i = 0; i < subscriptionService.sub.length; i++) {
                var sub = subscriptionService.sub[i];
                if(scope.selectedSub !== null){
                    if(sub.name === scope.selectedSub.name) {
                        return sub.price;
                    }
                }
                else{
                    return null;
                }
            }
        };
        
        /**
        * called when the user want to edit the subscription's name
        */
       scope.showEdit = function() {
           scope.show = false;
       };
       
       /**
        * called when the user cancel the subscription's name's edit
        */
       scope.cancelEdit = function() {
           scope.show = true;
       };
       
       /**
        * called when the user edit the name of the subscription
        */
       scope.editName = function(){
           subscriptionService.editName(scope.oldName, scope.nameTemp);
           scope.show = true;
       };
       
       /**
        * called when the user change the subscription selection
        */
       scope.changeSelection = function () {
           if(scope.selectedSub !== null){
               scope.showEditButton = false;
               scope.oldName = scope.selectedSub.name;
               
           }
           else{
               scope.showEditButton = true;
               scope.oldName = null;
           }
       };
       
       /**
        * called when the user change the value of textinput of the new subscription name 
        * @param {string} name
        */
       scope.changeNameTemp = function (name) {
           scope.nameTemp = name;
       };
       
       /**
        * called when the user change something about the different subscription
        */
       $rootScope.$on('showAddSub',scope.showAddSub = function (event, can){
           scope.showAddS = can;
       });
       
       /**
        * called when user want to change the quatity to a product in subscription
        * @param {Product} prod
        */
       scope.changeQuantitySub = function (prod) {
           subscriptionService.changeQuantity(prod, scope.selectedSub.name);
       }
       
       /**
        * true is we can't see the edit panel
        */
       scope.show = true;
       
       /**
        * true when we can't see the edit button
        */
       scope.showEditButton = true;
       
       /**
        * have the oldname of subscription when the user want to change it
        */
       scope.oldName = null;
       
       scope.cheminImageProduit = "/assets/images/product/"
    },
    template: '<div id="cart">' +
                '<div id="cartTabs" ng-click="prevent($event)">' + 
                    '<div id="cartTab" ng-class="{\'active\': cartOpen}" ng-click="toggleCart($event)" title="Panier">' +
                        '<i class="fa fa-shopping-cart fa-4x"></i>' +
                        '<div ng-show="cartService.nbProducts() == 0">Panier bien vide</div>' +
                        '<div ng-show="cartService.nbProducts() != 0" ng-cloak="ng-cloak">{{cartService.nbProducts()}} produits dans le panier</div>' +
                     '</div>' +
                     '<div id="subTab" ng-class="{\'active\': subOpen}" ng-click="toggleSub($event)" title="Abonnements">' +
                        '<img src="/assets/images/order.png" alt="abonnement"/>' +
                        '<div ng-show="selectedSub">{{selectedSub.name}}</div>' +
                        '<div ng-hide="selectedSub">Abonnement</div>' +
                     '</div>' +
                 '</div>' +
                 '<div id="cartPanel" ng-click="prevent($event)">' +
                    '<div ng-show="cartOpen">' +
                        '<table class="table table-striped table-bordered table-hover product-list">' +
                            '<tr class="prodCart" ng-repeat="prod in cartService.cart.products">' +
                                '<td><img class="imgCart" ng-src="{{cheminImageProduit}}{{prod.pictureUrl}}"/></td>'+
                                '<td>{{prod.name}}</td>' +
                                '<td><input type="text" class="inputQ" name="lname" ng-model="prod.quantity" ng-change="cartService.changeQuantity(prod)"/></td>' +
                                '<td title="Enlever du panier">\n\
                                <img class="deleteProduct" ng-click="cartService.deleteProduct(prod)" src="/assets/images/remove.png"/>\n\
                                </td>' +
                            '</tr>' +
                        '</table>' +
                        '<span style="font-style: 14px; font-weight: bold;">Total: <span class="price">{{cartService.cart.price}} €</span></span>' +
                    '</div>' +
                    '<div ng-show="subOpen">' +
                        '<a class="btn" ng-hide="showAddS" ng-click="subService.newSubscription()">New subscription</a>'+
                        '<select class="selectSub" ng-change="changeSelection()" ng-model="selectedSub" ng-options="s.name for s in subService.sub">'+
                        '</select>'+
                        '<button type="button" ng-hide="showEditButton" ng-click="showEdit()">Editer</button>'+
                        '<div ng-hide="show" >'+
                                '<input name="input" type="text" class="account-input col-xs-5" ng-model="name" ng-change="changeNameTemp(name)" required="required" />'+
                                '<button type="button" class="account-btn btn col-xs-2" ng-click="cancelEdit()">Annuler</button>'+
                                '<button type="button" class="account-btn btn btn-primary col-xs-2" ng-click="editName()">Editer</button>'+
                        '</div>'+
                        '<ul>' +
                            '<li class="prodCart" ng-repeat="prod in getSubProducts()">' +
                                ' <img class="imgCart" ng-src="{{cheminImageProduit}}{{prod.pictureUrl}}"/>'+
                                '{{prod.name}} quantity : \n\
                                <input type="text" class="inputQ" name="lname" value="prod.quantity" ng-model="prod.quantity" ng-change="changeQuantitySub(prod)"/> \n\
                                <span>\n\
                                <img class="deleteProduct" ng-click="subService.deleteProduct(prod, getNameSub())" src="{{cheminImageProduit}}supprimer.jpg"/>\n\
                                </span>' +
                            '</li>' +
                        '</ul>' +
                        'Price: {{getPriceSub()}} €' +
                    '</div>' +
                 '</div>' +
              '</div>'
  };
}]);
