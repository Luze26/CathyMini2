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
        scope.toggleCart = function() {
            var position;
            if(scope.cartOpen) {
                position = "-350px";
            }
            else {
                position = "0px";
            }
            
            scope.cartOpen = !scope.cartOpen;
            if(!scope.subOpen) {
                elm.animate({"right": position}, 200);
            }
            else {
                scope.subOpen = false;
            }
        };
        
        /**
         * Toggle sub's tab
         */
        scope.toggleSub = function() {
            var position;
            if(scope.subOpen) {
                position = "-350px";
            }
            else {
                position = "0px";
            }
            
            scope.subOpen = !scope.subOpen;
            if(!scope.cartOpen) {
                elm.animate({"right": position}, 200);
            }
            else {
               scope.cartOpen = false; 
            }
        };
        
        scope.selectedSub = null;
        
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
        * Called when a field is edited
        * @param {type} field edited
        */
       scope.showEdit = function() {
           scope.show = false;
       };
       
       scope.cancelEdit = function() {
           scope.show = true;
       };
       
       scope.editName = function(){
           subscriptionService.editName(scope.oldName, scope.nameTemp);
           scope.show = true;
       };
       
      
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
       
       scope.changeNameTemp = function (name) {
           scope.nameTemp = name;
       };
       
       scope.show = true;
       
       scope.showEditButton = true;
       
       scope.oldName = null;
        
        scope.cheminImageProduit = "/assets/images/product/"
    },
    template: '<div id="cart">' +
                '<div id="cartTabs">' + 
                    '<div id="cartTab" ng-class="{\'active\': cartOpen}" ng-click="toggleCart()">' +
                        '<i class="fa fa-shopping-cart fa-4x"></i>' +
                        '<div>{{cartService.nbProducts()}} products</div>' +
                     '</div>' +
                     '<div id="subTab" ng-class="{\'active\': subOpen}" ng-click="toggleSub()">' +
                        '<i class="fa fa-shopping-cart fa-4x"></i>' +
                        '<div>{{subService.nbProducts()}} products</div>' +
                     '</div>' +
                 '</div>' +
                 '<div id="cartPanel">' +
                    '<div ng-show="cartOpen">' +
                        '<ul>' +
                            '<li class="prodCart" ng-repeat="prod in cartService.cart.products">' +
                                ' <img class="imgCart" ng-src="{{cheminImageProduit}}{{prod.pictureUrl}}"/>'+
            '{{prod.name}} quantity : \n\
<input type="text" class="inputQ" name="lname" ng-model="prod.quantity" ng-change="cartService.changeQuantity(prod)"/> \n\
<span>\n\
<img class="deleteProduct" ng-click="cartService.deleteProduct(prod)" src="{{cheminImageProduit}}supprimer.jpg"/>\n\
</span>' +
                            '</li>' +
                        '</ul>' +
                        'Price: {{cartService.cart.price}} €' +
                    '</div>' +
                    '<div ng-show="subOpen">' +
                        '<ul>' +
                            '<input type="text" class="inputQ" name="subname" ng-model="subService.sub.name" ng-change="subService.changeName(subService.sub)">{{subService.sub.name}}</input> '+
                            '<select class="selectSub" ng-change="changeSelection()" ng-model="selectedSub" ng-options="s.name for s in subService.sub">'+
                            '</select>'+
                            '<button type="button" ng-hide="showEditButton" ng-click="showEdit()">Editer</button>'+
                            '<div ng-hide="show" >'+
                                    '<input name="input" type="text" class="account-input col-xs-5" ng-model="name" ng-change="changeNameTemp(name)" required="required" />'+
                                    '<button type="button" class="account-btn btn col-xs-2" ng-click="cancelEdit()">Annuler</button>'+
                                    '<button type="button" class="account-btn btn btn-primary col-xs-2" ng-click="editName()">Editer</button>'+
                            '</div>'+
                            '<li class="prodCart" ng-repeat="prod in getSubProducts()">' +
                                ' <img class="imgCart" ng-src="{{cheminImageProduit}}{{prod.pictureUrl}}"/>'+
                                '{{prod.name}} quantity : \n\
<input type="text" class="inputQ" name="lname" ng-model="prod.quantity" ng-change="subService.changeQuantity(prod)"/> \n\
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
