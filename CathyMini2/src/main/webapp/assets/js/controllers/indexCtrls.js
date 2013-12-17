/**
 * Controller for the index page
 */
angular.module('index').
        controller('indexCtrl', ['$scope', '$http', '$rootScope', function($scope, $http, $rootScope) {
                
                /**
                 * Set the tick on the navbar
                 */
                $rootScope.header = "home";
                
                /**
                 * List of articles
                 */
                $scope.articles = [];

                /**
                 * Search object
                 */
                $scope.search = {offset: 0, length: 20};
    
                /** If we continue to requests the server or not */
                $scope.loadMore = true;
                
                /**
                 * Initialize the carousel
                 */
                $(document).ready(function() {
                    $('#myCarousel').carousel();
                });
                
                /**
                 * Load articles
                 */
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
