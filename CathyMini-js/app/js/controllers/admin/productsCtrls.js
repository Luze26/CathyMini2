angular.module('products.productsCtrls', ['infinite-scroll', '$strap.directives']).
  controller('productsCtrl', ['$scope', '$http', function($scope, $http) {
    $scope.product = {};
    $scope.modal = {};
    $scope.modal.test = function() { console.log("klmkklk"); };
    console.log("'mlmlmlmldm");
    $scope.addProdduct = function(dismiss) {
        console.log("kljlkmkmlkm");
        $http.post("http://localhost:8080//webresources/product/create", $scope.product)
            .success(function() {dismiss();});
    };
  }]);