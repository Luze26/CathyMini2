angular.module('index').
        controller('indexCtrls', ['$scope', '$http', function($scope, $http) {

                $scope.articles = [];

                /** If we continue to requests the server or not */
                $scope.loadMore = true;

                $scope.loadArticles = function() {
                    console.log("ici");
                    if ($scope.loadMore) {
                        $http.post("/webresources/article/all", $scope.search)
                                .success(function(data) {
                                    if (data.length < $scope.search.length) { //If there is no more product to load, end of the list
                                        $scope.loadMore = false;
                                    }
                                    if ($scope.search.offset === 0) { //If it's a new search, we reset the list
                                        $scope.articles = [];
                                    }

                                    $scope.search.offset += $scope.search.length; //Increment the offset
                                    $scope.articles = $scope.articles.concat(data); //Add last loaded products
                                });
                    }
                };

            }]);
