/**
 * Cart directive, used to display the cart tab
 */
angular.module('common').directive('cartDirective', ['$rootScope', 'cartService', 'subscriptionService', '$timeout', function($rootScope, cartService, subscriptionService, $timeout) {
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
       
       scope.selectedSub = null;
       
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
           //scope.selectedSub = scope.nameTemp;
           scope.show = true;
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
       $rootScope.$on('showAddSub', function (event, can){
           scope.showAddS = can;
       });
       
       /**
        * called when the user charge the subscription with something already existing in it
        */
       $rootScope.$on('subLoaded', function (){
           $timeout(function() {
                scope.selectedSub = scope.subService.sub[0];
                scope.showEditButton = false;
            }, 500);
       });
       
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
       
       //scope.changeSelection();
       
       /**
        * called when user want to change the quatity to a product in subscription
        * @param {Product} prod
        */
       scope.changeQuantitySub = function (prod) {
           subscriptionService.changeQuantity(prod, scope.selectedSub.name);
       };

       scope.newSub = function() {
           var promise = subscriptionService.newSubscription();
           promise.then(function(data) {
               scope.selectedSub = data;
               scope.showEditButton = false;
           });
       };
    },
    template: '<div id="cart">' +
                '<div id="cartTabs" ng-click="prevent($event)">' + 
                    '<div id="cartTab" ng-class="{\'active\': cartOpen}" ng-click="toggleCart($event)" title="Panier">' +
                        '<i class="fa fa-shopping-cart fa-4x"></i>' +
                        '<div ng-show="cartService.nbProducts() == 0">Panier tout vide</div>' +
                        '<div ng-show="cartService.nbProducts() != 0" ng-cloak="ng-cloak">{{cartService.nbProducts()}} produits dans le panier</div>' +
                     '</div>' +
                     '<div id="subTab" ng-class="{\'active\': subOpen}" ng-click="toggleSub($event)" title="Abonnements">' +
                        '<img style="margin-top: 5px;" src="/assets/images/order.png" alt="abonnement"/>' +
                        '<div>Abonnements</div>' +
                     '</div>' +
                 '</div>' +
                 '<div id="cartPanel" ng-click="prevent($event)">' +
                    '<div ng-show="cartOpen">' +
                        '<div class="empty-basket" ng-show="cartService.nbProducts() == 0" ng-cloak="ng-cloak">Votre panier est tout vide.<br/>Il se sent très triste.</div>' +
                        '<table class="table table-striped table-bordered table-hover product-list">' +
                            '<tr class="prodCart" ng-repeat="prod in cartService.cart.products">' +
                                '<td><img class="imgCart" ng-src="/assets/images/product/{{prod.pictureUrl}}"/></td>'+
                                '<td>{{prod.name}}</td>' +
                                '<td><input type="number" class="inputQ" name="lname" ng-model="prod.quantity" ng-change="cartService.changeQuantity(prod)"/></td>' +
                                '<td title="Enlever du panier">\n\
                                <img title="Enelver du panier" class="deleteProduct" ng-click="cartService.deleteProduct(prod)" src="/assets/images/remove.png"/>\n\
                                </td>' +
                            '</tr>' +
                        '</table>' +
                        '<div ng-hide="cartService.nbProducts() == 0"><span style="font-style: 14px; font-weight: bold;">Total: <span class="price">{{cartService.cart.price}} €</span></span>' +
                        '<a style="margin-top:20px; margin-right: 20px;" class="pull-right btn btn-primary" href="/payment.xhtml">Terminer vos commandes</a></div>'+
                    '</div>' +
                    '<div ng-show="subOpen">' +
                        '<div class="empty-basket" ng-show="subService.sub.length == 0">' +
                            'Vous n\'avez pas encore d\'abonnement.' +
                            '<a style="margin-top: 5px; margin-right: 5px; margin-bottom: 5px;" title="Nouvel abonnement" class="btn btn-primary smaller-btn" ng-hide="showAddS" ng-click="newSub()"><img src="/assets/images/add.png" alt="Ajouter"/> Créer mon abonnement</a>' +
                        '</div>' +
                        '<div ng-hide="subService.sub.length == 0">' +
                            '<select style="margin-top: 5px; margin-left: 5px; margin-right: 5px;" class="selectSub" ng-change="changeSelection()" ng-model="selectedSub" ng-options="s.name for s in subService.sub">'+
                            '</select>'+
                            '<button title="Changer le nom" style="margin-right: 5px; margin-top: 5px; margin-bottom: 5px;" class="btn btn-primary smaller-btn" type="button" ng-hide="showEditButton" ng-click="showEdit()"><img src="/assets/images/edit.png" alt="Editer"/>Changer le nom</button>'+
                            '<a style="margin-top: 5px; margin-right: 5px; margin-bottom: 5px;" title="Nouvel abonnement" class="btn btn-primary smaller-btn" ng-hide="showAddS" ng-click="newSub()"><img src="/assets/images/add.png" alt="Ajouter"/></a>'+
                            '<div ng-hide="show">'+
                                    '<input style="margin-left: 5px;" name="input" type="text" class="col-xs-5" ng-model="name" ng-change="changeNameTemp(name)" required="required" />'+
                                    '<button title="Annuler" type="button" class="account-btn btn smaller-btn col-xs-3" ng-click="cancelEdit()">Annuler</button>'+
                                    '<button title="Editer" type="button" class="account-btn btn smaller-btn btn-primary col-xs-3" ng-click="editName()">Editer</button>'+
                            '</div>'+
                            '<table class="table table-striped table-bordered table-hover product-list">' +
                                '<tr class="prodCart" ng-repeat="prod in getSubProducts()">' +
                                    '<td><img class="imgCart" ng-src="/assets/images/product/{{prod.pictureUrl}}"/></td>'+
                                    '<td>{{prod.name}}</td>' +
                                    '<td><input type="number" class="inputQ" name="lname" value="prod.quantity" ng-model="prod.quantity" ng-change="changeQuantitySub(prod)"/></td>\n\
                                    <td>\n\
                                    <img title="Enelever de l\'abonnement" class="deleteProduct" ng-click="subService.deleteProduct(prod, getNameSub())" src="/assets/images/remove.png"/>\n\
                                    </td>' +
                                '</tr>' +
                            '</table>' +
                            '<div ng-show="getSubProducts().length != 0" ng-cloak="ng-cloak">' +
                                '<span style="font-size: 14px; font-weight: bold;">Total: <span class="price">{{getPriceSub()}} €</span></span>' +
                                '<a title="Terminer votre commande" style="margin-top:20px; margin-right: 20px;" class="pull-right btn btn-primary" href="/payment.xhtml">Terminer vos commandes</a>'+
                            '</div>' +
                            '<div class="empty-basket" ng-show="getSubProducts().length == 0" ng-cloak="ng-cloak">' +
                                'Abonnement bien vide.' +
                            '</div>' +
                        '</div>' + 
                    '</div>' +
                 '</div>' +
              '</div>'
  };
}]);
