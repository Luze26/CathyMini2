/**
 * Controller for the products list
 */
angular.module('products').
  controller('productsCtrl', ['$rootScope', '$scope', '$http', 'cartService', 'subscriptionService', 'consumerService', function($rootScope, $scope, $http, cartService, subscriptionService, consumerService) {
      
      /**
       * Set the tick on the navbar
       */
      $rootScope.header = "products";
      
      /** Search query */
      $scope.offset = 0;
      $scope.length = 20; 
      $scope.currentReqHolding = false;
      $scope.search = {orderBy: "flux", orderByASC: true, input: "", tampon: true,
        napkin: true, minPrice: 0, maxPrice: 20, brands: ['Vania', 'Tampax', 'Always', 'Nett'], flux: [1, 2, 3, 4, 5, 6]};

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
       * @param {event} click event
       */
      $scope.orderBy = function(property, event) {
        if(event) {
            event.stopPropagation();
        }
        
        if (property === $scope.search.orderBy) { //If the property is unchanged, we change the direction of the sort
          $scope.search.orderByASC = !$scope.search.orderByASC;
        }
        else {
          $scope.search.orderBy = property;
          $scope.search.orderByASC = true;
        }
      };

      /**
       * If bouton all/no for flux or brands have been used, usefull for refresh search
       * @type Boolean|Boolean
       */
      var allNoDoneFlux = false;
      var allNoDoneBrands = false;
      
      /**
       * Refresh product list with the new search query
       */
      $scope.refreshSearch = function() {
        $scope.offset = 0;
        $scope.length = 20;
        $scope.loadMore = true;
        if(allNoDoneFlux && $scope.search.flux.length == 0 || allNoDoneBrands && $scope.search.brands.length == 0) { //If all no done have been used
            $scope.products = [];
        }
        else {
            $scope.loadProducts();
        }
      };

      /**
       * Call when we change value of "$scope.search.tampon"
       */
      $scope.changeTampon = function() {
        if($scope.search.tampon === false && $scope.search.napkin === false) {
          $scope.search.tampon = true;
        }
      };

      /**
       * Call when we change value of "$scope.search.napkin"
       */
      $scope.changeNapkin = function() {
        if($scope.search.napkin === false && $scope.search.tampon === false) {
          $scope.search.napkin = true;
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
        allNoDoneBrands = true;
        for (var i = 0; i < $scope.brands.length; i++) {
            var brand = $scope.brands[i];
            brand.check = checked;
        }
        $scope.refreshBrand();
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
        allNoDoneFlux = true;
        for (var i = 0; i < $scope.flux.length; i++) {
            var flu = $scope.flux[i];
            flu.check = checked;
        }
        $scope.refreshFlux();
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
       * Name of flux by number of drops
       * @param {type} nb
       * @returns {string} flux's name
       */
      $scope.getFluxName = function(nb) {
          switch(nb) {
              case 1:
                  return "Mini";
              case 2:
                  return "Normal";
              case 3:
                  return "Normal+";
              case 4:
                  return "Super";
              case 5:
                  return "Super+";
              case 6:
                  return "Extra";
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
       * return true if consumer can add a new Subscription, else if false
       */
      $scope.showAddSub = function() {
          var can = false;
          if(!consumerService.isConnected && subscriptionService.sub.length !== 0)
              can = true;
          $rootScope.$broadcast('showAddSub', can);
          return can;
      };
      
      /**
       * Watch if the search query change
       */
      $scope.$watch('search', $scope.refreshSearch, true);
      
      /**
       * And function, because xhtml not allow & in attributes
       * @param {type} x
       * @param {type} y
       * @returns {boolean} x && y
       */
      $scope.and = function(x,y) {
          return x && y;
      };
    }]);
