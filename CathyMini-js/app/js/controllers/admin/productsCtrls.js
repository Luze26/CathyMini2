productsModule.
  controller('productsCtrl', ['$scope', '$http', function($scope, $http) {
          
    $scope.product = {};
    $scope.products = [];
    $scope.displayFeedbackDone = false;
    $scope.displayConnectionError = false;
    $scope.loadMore = true;
    $scope.offset = 0;
    $scope.length = 30;
    
    $scope.addProduct = function(dismiss) {
        $scope.displayConnectionError = false;
        $http.post("http://localhost:8080//webresources/product/create", $scope.product)
            .success(function(data) { $scope.products.push(data); $scope.displayFeedbackDone = true; dismiss();})
            .error(function() { $scope.displayConnectionError = true; });
    };
    
    $scope.loadProducts = function() {
        if($scope.loadMore) {
            $http.get("http://localhost:8080//webresources/product/all?offset=" + $scope.offset + "&length=" + $scope.length)
                .success(function(data) { 
                    if(data.length < $scope.length) {
                        $scope.loadMore = false;
                    }
                    $scope.offset += $scope.length;
                    $scope.products = $scope.products.concat(data);
                });
         }
    };
  }]);