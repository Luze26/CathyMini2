commonModule.directive('searchDirective', ['searchService', '$http', function(searchService, $http) {
  return {
    restrict: 'E',
    replace: true,
    scope: {
        products: "=productslist"
    },
    link: function(scope, elm, attrs) {
        
        scope.searchOpen = false;
        
        //scope.search = searchService.search;
        
        /** Search query */
        scope.search = {offset: 0, length: 20, orderBy: "id", orderByASC: true, input: "", tampon: true, napkin: true, minPrice: 0, maxPrice: 100};
                
        /** Type list */
        scope.productsType = [
            {
                "selected": false,
                "affichage": "Tampon"
            },
            {
                "selected": false,
                "affichage": "Serviette"
            }
        ];

        /** If we continue to requests the server or not */
        scope.loadMore = true;
        /**
        * Order by the product's list
        * @param {type} property
        */
        scope.orderBy = function(property) {
            if (property === scope.search.orderBy) {
                scope.search.orderByASC = !scope.search.orderByASC;
            } else {
                scope.search.orderBy = property;
                scope.search.orderByASC = true;
            }
            scope.refreshSearch();
        };

        /**
        * Refresh product list with the new search query
        */
        scope.refreshSearch = function() {
            console.log(scope.products);
            scope.search.offset = 0;
            scope.search.length = 20;
            scope.loadMore = true;
            scope.loadProducts();
        };

       /**
       * Load products
       * @returns {undefined}
       */
       scope.loadProducts = function() {
            if (scope.loadMore) {
                $http.post("http://localhost:8080//webresources/product/all", scope.search).success(function(data) {
                    if (data.length < scope.search.length) {
                        scope.loadMore = false;
                    }
                    if (scope.search.offset === 0) {
                        scope.products = [];
                    }
                    scope.search.offset += scope.search.length;
                    scope.products = scope.products.concat(data);
               });
            }
        };
                
        scope.toggleSearch = function() {
            var position;
            if(scope.searchOpen) {
                position = "-400px";
            }
            else {
                position = "0px";
            }
            
            scope.searchOpen = !scope.searchOpen;
            
            
            elm.animate({"left": position}, 200);
        };
    },
    template: '<div id="search">'+
            '<div id="searchPanel">' +
            '<form action="#" id="search-form">' +
                '<div>' +
                    '<label>Rechecher par nom </label>' +
                    '<input type="text" class="searchTable" ng-model="search.input" placeholder="Search a product" ng-change="refreshSearch()" />' +
                '</div>' +
                '<div>' +
                    '<label>Type du produit </label>' +
                    '<div class="btn-group" bs-buttons-checkbox="bs-buttons-checkbox">' +
                        '<!--     <div ng-repeat="type in productsType" class="btn-group" bs-buttons-checkbox="bs-buttons-checkbox">' +
                            '<button type="button" value="true" class="btn" ng-model="type.selected" ng-change="refreshSearch()">type.affichage</button>' +
                        '</div>    -->' +
                        '<button type="button" value="true" class="btn" ng-model="search.napkin" ng-change="refreshSearch()">Serviette</button>' +
                        '<button type="button" value="true" class="btn" ng-model="search.tampon" ng-change="refreshSearch()">Tampon</button>' +
                    '</div>' +
                '</div>' +
                '<div>' +
                    '<label for="pPrice">Rechecher par prix </label>' +
                    '<div class="controls input-prepend">' +
                        '<input type="text" ng-model="search.minPrice" min="0" placeholder="Minimum price" smart-float="smart-float" class="searchTable" ng-change="refreshSearch()" />' +
                    '<span class="add-on">€</span>' +
                    '</div>' +
                    '<div class="controls input-prepend">' +
                        '<input type="text" ng-model="search.maxPrice" min="0" placeholder="Maximum price" smart-float="smart-float" class="searchTable" ng-change="refreshSearch()" />' +
                        '<span class="add-on">€</span>' +
                    '</div>' +
                '</div>' +
            '</form>' +
            '</div>'+
                '<div id="searchTab" ng-click="toggleSearch()">' +
                        '<div>S</div>' +
                        '<div>E</div>' +
                        '<div>A</div>' +
                        '<div>R</div>' +
                        '<div>C</div>' +
                        '<div>H</div>' +
                '</div>' + 
            '</div>'
               /* '<div id="cartTab" ng-click="toggleCart()">' +
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
                 '</div>' +*/
              
  };
}]);


