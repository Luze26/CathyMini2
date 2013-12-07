angular.module('products').
        controller('productsCtrl', ['$scope', '$http', 'cartService', function($scope, $http, cartService) {

                $scope.selectedItem = [ 1, 2, 3, 4, 5, 6];

                /** Search query */
                $scope.search = {offset: 0, length: 20, orderBy: "id", orderByASC: true, input: "", tampon: true, 
                    napkin: true, minPrice: 0, maxPrice: 100, brand: "", flux: $scope.selectedItem};


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
                        "id": 1,
                        "print": "Mini",
                        "check": true
                      },
                      {
                        "id": 2,
                        "print": "Normal",
                        "check": true
                      },
                      {
                        "id": 3,
                        "print": "Normal+",
                        "check": true
                      },
                      {
                        "id": 4,
                        "print": "Super",
                        "check": true
                      },
                      {
                        "id": 5,
                        "print": "Super+",
                        "check": true
                      },
                      {
                        "id": 6,
                        "print": "Extra",
                        "check": true
                      }
                    ];
                    
                    
                    
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
                 * 
                 * Refresh list of selected flux
                 */
                $scope.refreshFlux = function() {
                    console.log("flux");
                    $scope.selectedItem = [];
                    for(var x =0; x < $scope.selects.length; x++) {
                        console.log("test");
                        if($scope.selects[x].check) {
                            $scope.selectedItem.push($scope.selects[x].id);
                            alert($scope.selects[x].id);
                        }
                    }    
                    $scope.refreshSearch();
                };
                
                /**
                 * Load products
                 */
                $scope.loadProducts = function() {
                    if ($scope.loadMore) {
                        $http.post("http://localhost:8080//webresources/product/all", $scope.search)
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
                
                
                
                
                 $scope.listArticle = [];
        
        $scope.getArticle = function() {      
        /*    var xmlDoc = xmlParse(new URL("http://localhost:8080/assets/product/listeProduit.xml"));
            var markers = xmlDoc.documentElement.getElementsByTagName("article");

            for (var i = 0; i < markers.length; i++) {
                            var x = markers[i].textContent;
                            $scope.listArticle = $scope.listArticle.concat(x);
                            alert(x);
             }*/
            alert(test);
        };
                
                
                
                
                /**
                 * Add a product to the cart
                 * @param {Product} product
                 */
                $scope.addProductToCart = function(product) {
                    cartService.addProduct(product);
                };
            }]);    
