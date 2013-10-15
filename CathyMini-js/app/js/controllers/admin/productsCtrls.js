angular.module('products.productsCtrls', []).
  controller('addProductCtrl', ['$scope', '$http', function($scope, $http) {
  
    $scope.addProdduct = function() {
        $http.post("http://localhost:8080//webresources/product/create", {name: "test", price: "tt"})
            .success(function() { console.log("llll");})
    };
  }]);

