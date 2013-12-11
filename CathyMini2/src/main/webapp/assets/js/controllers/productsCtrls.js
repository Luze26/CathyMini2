angular.module('products').
  controller('productsCtrl', ['$scope', '$http', 'cartService', function($scope, $http, cartService) {

      /** Search query */
      $scope.search = {offset: 0, length: 20, orderBy: "id", orderByASC: true, input: "", tampon: true,
        napkin: true, minPrice: 0, maxPrice: 100, brand: "", flux: [1, 2, 3, 4, 5, 6]};

      /** Path where product's image are stock */
      $scope.cheminImageProduit = "/assets/product/"

      /** Products list */
      $scope.products = [];

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
      $scope.selects = [
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

      $scope.tmp = true;

      $scope.brands = [
        {
          "id": "1",
          "print": "Vania",
          "check": true
        },
        {
          "id": "2",
          "print": "Nana",
          "check": true
        }
      ];

      /** If we continue to requests the server or not */
      $scope.loadMore = true;

      /**
       * Order by the product's list
       * @param {type} property, property on which the sort is done
       */
      $scope.orderBy = function(property) {
        if (property === $scope.search.orderBy) { //If the property is unchanged, we change the direction of the sort
          $scope.search.orderByASC = !$scope.search.orderByASC;
        }
        else {
          $scope.search.orderBy = property;
          $scope.search.orderByASC = true;
        }
        $scope.refreshSearch();
      };

      /**
       * Refresh product list with the new search query
       */
      $scope.refreshSearch = function() {
        $scope.search.offset = 0;
        $scope.search.length = 20;
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
        $scope.refreshSearch();
      };

      /**
       * Call when we change value of "$scope.search.napkin"
       */
      $scope.changeNapkin = function() {
        if($scope.search.napkin === false && $scope.search.tampon === false) {
          $scope.search.tampon = true;
        }
        $scope.refreshSearch();
      };

      /**
       * 
       * Refresh list of selected flux
       */
      $scope.refreshFlux = function() {
        $scope.search.flux = [];
        for (var i = 0; i < $scope.selects.length; i++) {
          if ($scope.selects[i].check) {
            $scope.search.flux.push($scope.selects[i].id);
          }
        }
        
        $scope.refreshSearch();
      };

      /**
       * Load products
       */
      $scope.loadProducts = function() {
        if ($scope.loadMore) {
          $http.post("/webresources/product/all", $scope.search)
            .success(function(data) {
              if (data.length < $scope.search.length) { //If there is no more product to load, end of the list
                $scope.loadMore = false;
              }

              if ($scope.search.offset === 0) { //If it's a new search, we reset the list
                $scope.products = [];
              }

              $scope.search.offset += $scope.search.length; //Increment the offset
              $scope.products = $scope.products.concat(data); //Add last loaded products
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
      
      $scope.showProduct = function(product) {
        $scope.productOverlay = product;
      };
    }]);
