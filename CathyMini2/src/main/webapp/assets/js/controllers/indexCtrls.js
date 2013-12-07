angular.module('index').
        controller('indexCtrl', ['$scope', '$http',  function($scope) {

        $scope.articles = [];
        
         $scope.loadArticles = function() {
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
