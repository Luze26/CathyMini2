/**
 * Controller for the index page
 */
angular.module('index').
        controller('indexCtrl', ['$scope', '$http', '$rootScope', '$sce', '$sceDelegate', function($scope, $http, $rootScope, $sce, $sceDelegate) {
                
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


                $scope.Nom ="";
                $scope.Prenom ="";
                $scope.Mail ="";
                $scope.Age ="";
                $scope.Sujet ="";
                $scope.Message ="";
                
                /**
                 * Envoyer un mail
                 */
                $scope.envoyerMail = function() {
                    console.log("ici");
                    if ($scope.Sujet !== null && $scope.Message !== null) {
                        console.log("Nom : " + $scope.Nom + " Prenom : " + $scope.Prenom + " Mail : "+ $scope.Mail + " Age : " + $scope.Age + " Sujet : "+$scope.Sujet+ " Message : "+$scope.Message);
                        $scope.Nom ="";
                        $scope.Prenom ="";
                        $scope.Mail ="";
                        $scope.Age ="";
                        $scope.Sujet ="";
                        $scope.Message ="";
                    }
                };
                
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
                                    for(var i = 0; i < $scope.articles.length; i++){
                                        $scope.articles[i].html = $sce.trustAsHtml($scope.articles[i].detail);
                                        $sceDelegate.trustAs("html",$scope.articles[i].detail);
                                    }
                                });
                    }
                };
                
              

            }]);