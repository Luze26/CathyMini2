/**
 * Controller for the products list
 */
angular.module('products').
  controller('productsCtrl', ['$rootScope', '$scope', '$http', 'cartService', 'subscriptionService', function($rootScope, $scope, $http, cartService, subscriptionService) {
      
      /**
       * Set the tick on the navbar
       */
      $rootScope.header = "products";
      
      /** Search query */
      $scope.offset = 0;
      $scope.length = 20; 
      $scope.currentReqHolding = false;
      $scope.search = {orderBy: "flux", orderByASC: true, input: "", tampon: true,
        napkin: true, minPrice: 0, maxPrice: 20, brands: [], flux: [1, 2, 3, 4, 5, 6]};

      /** Path where product's image are stock */

      $scope.cheminImageProduit = "/assets/images/product/";

      /** Products list */
      $scope.products = [];
      
      /**Subscription list*/
      $scope.subService = subscriptionService;

      /** true if the search panel is displayed, false otherwise **/
      $scope.displaySearchPanel = true;
            
      /** Type list */
      $scope.productsType = [
        {
          "selected": false,
          "affichage": "Tampon"
        },
        {
          "selected": false,
          "affichage": "Serviette"
        }
      ];


      /** Description of flux **/
      $scope.flux = [
        {
          "id": "1",
          "print": "Mini",
          "check": true
        },
        {
          "id": "2",
          "print": "Normal",
          "check": true
        },
        {
          "id": "3",
          "print": "Normal+",
          "check": true
        },
        {
          "id": "4",
          "print": "Super",
          "check": true
        },
        {
          "id": "5",
          "print": "Super+",
          "check": true
        },
        {
          "id": "6",
          "print": "Extra",
          "check": true
        }
      ];

      /**
       * Brands list
       */
      $scope.brands = [
        {
          "id": "1",
          "print": "Vania",
          "check": true
        },
        {
          "id": "2",
          "print": "Tampax",
          "check": true
        },
        {
          "id": "3",
          "print": "Nett",
          "check": true
        },
        {
          "id": "4",
          "print": "Always",
          "check": true
        }
      ];

      /** If we continue to requests the server or not */
      $scope.loadMore = true;

      /**
       * Order by the product's list
       * @param {type} property, property on which the sort is done
       * @param {type} state, state oof the orderBy
       */
      $scope.orderBy = function(property) {
        if (property === $scope.search.orderBy) { //If the property is unchanged, we change the direction of the sort
          $scope.search.orderByASC = !$scope.search.orderByASC;
        }
        else {
          $scope.search.orderBy = property;
          $scope.search.orderByASC = true;
        }
      };

      /**
       * Refresh product list with the new search query
       */
      $scope.refreshSearch = function() {
        $scope.offset = 0;
        $scope.length = 20;
        $scope.loadMore = true;
        $scope.loadProducts();
      };

      /**
       * Call when we change value of "$scope.search.tampon"
       */
      $scope.changeTampon = function() {
        if($scope.search.tampon === false && $scope.search.napkin === false) {
          $scope.search.napkin = true;
        }
      };

      /**
       * Call when we change value of "$scope.search.napkin"
       */
      $scope.changeNapkin = function() {
        if($scope.search.napkin === false && $scope.search.tampon === false) {
          $scope.search.tampon = true;
        }
      };

      /**
       * 
       * Refresh list of selected brands
       */
      $scope.refreshBrand = function() {
        var brands = [];
        for (var i = 0; i < $scope.brands.length; i++) {
          if ($scope.brands[i].check) {
            brands.push($scope.brands[i].print);
          }
        }
        $scope.search.brands = brands;
      };
      
      /**
       * Enable or disable all brands
       * @param {type} true = enable, false = disable
       */
      $scope.allNoBrands = function(checked) {
        for (var i = 0; i < $scope.brands.length; i++) {
            var brand = $scope.brands[i];
            brand.check = checked;
        }
        if(checked) {
            $scope.refreshBrand();
        }
        else {
            $scope.products = [];
        }
      };
      
      /**
       * 
       * Refresh list of selected flux
       */
      $scope.refreshFlux = function() {
        var flux = [];
        for (var i = 0; i < $scope.flux.length; i++) {
          if ($scope.flux[i].check) {
            flux.push($scope.flux[i].id);
          }
        }
        $scope.search.flux = flux;
      };

      /**
       * Enable or disable all flux
       * @param {type} true = enable, false = disable
       */
      $scope.allNoFlux = function(checked) {
        for (var i = 0; i < $scope.flux.length; i++) {
            var flu = $scope.flux[i];
            flu.check = checked;
        }
        if(checked) {
            $scope.refreshFlux();
        }
        else {
            $scope.products = [];
        }
      };
      
      /**
       * Load products
       */
      $scope.loadProducts = function() {
        if ($scope.loadMore && !$scope.currentReqHolding) {
          $scope.currentReqHolding = true;
          var searchLimited = {orderBy: $scope.search.orderBy, orderByASC: $scope.search.orderByASC, input: $scope.search.input, tampon: $scope.search.tampon,
            napkin: $scope.search.napkin, minPrice: $scope.search.minPrice, maxPrice: $scope.search.maxPrice, brand: $scope.search.brand, flux: $scope.search.flux,
            offset: $scope.offset, length: $scope.length, brands: $scope.search.brands};
          $http.post("/webresources/product/all", searchLimited)
            .success(function(data) {
              if (data.length < $scope.length) { //If there is no more product to load, end of the list
                $scope.loadMore = false;
              }

              if ($scope.offset === 0) { //If it's a new search, we reset the list
                $scope.products = [];
              }

              $scope.offset += $scope.length; //Increment the offset
              $scope.products = $scope.products.concat(data); //Add last loaded products
              $scope.currentReqHolding = false;
            });
        }
      };
      
       /**
       * Add a product to the cart
       * @param {Product} product
       */
      $scope.addProductToCart = function(event, product) {
        if(event) {
            event.stopPropagation();
        }
        cartService.addProduct(product);
      };
      
      $scope.newSubscription = function(event){
        if(event) {
            event.stopPropagation();
        }
        subscriptionService.newSubscription();
      };
      
      /**
       * Add a product to the sub
       * @param {Product} product
       */
      $scope.addProductToSub = function(event, product, name) {
        if(event) {
            event.stopPropagation();
        }
        subscriptionService.addProduct(product, name);
      };
      
      /**
       * Display an overlay for the product
       * @param {type} product to display in the overlay
       */
      $scope.showProduct = function(product) {
        $scope.productOverlay = product;
      };
      
      /**
       * Watch if the search query change
       */
      $scope.$watch('search', $scope.refreshSearch, true);
    }]);
