productsModule.
  controller('productsCtrl', ['$scope', '$http', function($scope, $http) {
          
    $scope.product = {};
    $scope.search = {offset: 0, length: 30, orderBy: "id", input: ""};
    
    $scope.products = [];
    $scope.displayFeedbackDone = false;
    $scope.displayConnectionError = false;
    $scope.loadMore = true;
    
    
    $scope.addProduct = function(dismiss) {
        $scope.displayConnectionError = false;
        $http.post("http://localhost:8080//webresources/product/create", $scope.product)
            .success(function(data) { $scope.products.push(data); $scope.displayFeedbackDone = true; dismiss();})
            .error(function() { $scope.displayConnectionError = true; });
    };
    
    $scope.textSearch = function() {
        $scope.search.offset = 0;
        $scope.search.length = 30;
        $scope.loadMore = true;
        $scope.loadProducts();
    };
    
    $scope.loadProducts = function() {
        if($scope.loadMore) {
            $http.post("http://localhost:8080//webresources/product/all", $scope.search)
                .success(function(data) { 
                    if(data.length < $scope.search.length) {
                        $scope.loadMore = false;
                    }
                    
                    if($scope.search.offset === 0) {
                        $scope.products = [];
                    }
                    
                    $scope.search.offset += $scope.search.length;
                    
                    $scope.products = $scope.products.concat(data);
                });
         }
    };
  }]);