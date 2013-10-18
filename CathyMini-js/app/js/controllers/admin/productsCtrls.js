angular.module('products.productsCtrls', ['infinite-scroll']).
  controller('addProductCtrl', ['$scope', '$http', function($scope, $http) {
  
    $scope.product = {};
    
    $scope.loadProducts = function() {
        
    };
    
    $scope.addProdduct = function() {
        $http.post("http://localhost:8080//webresources/product/create", $scope.product)
            .success(function() { console.log("llll\n\"");    })};
  }]);

