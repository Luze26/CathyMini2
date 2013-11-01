angular.module('products').
        controller('productsCtrl', ['$scope', '$http', 'cartService', function($scope, $http, cartService) {

                /** Search query */
                $scope.search = {offset: 0, length: 20, orderBy: "id", orderByASC: true, input: ""};

                /** Products list */
                $scope.products = [];

                /** If we continue to requests the server or not */
                $scope.loadMore = true;

                /**
                 * Order by the product's list
                 * @param {type} property
                 */
                $scope.orderBy = function(property) {
                    if (property === $scope.search.orderBy) {
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
                 * Load products
                 * @returns {undefined}
                 */
                $scope.loadProducts = function() {
                    if ($scope.loadMore) {
                        $http.post("http://localhost:8080//webresources/product/all", $scope.search)
                                .success(function(data) {
                                    if (data.length < $scope.search.length) {
                                        $scope.loadMore = false;
                                    }

                                    if ($scope.search.offset === 0) {
                                        $scope.products = [];
                                    }

                                    $scope.search.offset += $scope.search.length;

                                    $scope.products = $scope.products.concat(data);
                                });
                    }
                };
                
                $scope.addProductToCart = function(product) {
                    cartService.addProduct(product);
                };
            }]);