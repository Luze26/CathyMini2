angular.module('index').
        controller('indexCtrl', ['$scope', '$http', function($scope, $http) {

                $scope.articles = [];

                $scope.search = {offset: 0, length: 20};
    
                /** If we continue to requests the server or not */
                $scope.loadMore = true;

                $scope.loadArticles = function() {
                    if ($scope.loadMore) {
                        $http.post("/webresources/article/all", $scope.search)
                                .success(function(data) {
                                    if (data.length < $scope.search.length) { //If there is no more product to load, end of the list
                                        $scope.loadMore = false;
                                    }

                                    $scope.search.offset += $scope.search.length; //Increment the offset
                                    $scope.articles = $scope.articles.concat(data); //Add last loaded products
                                });
                    }
                };

            }]);
