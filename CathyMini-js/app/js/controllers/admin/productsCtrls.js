productsModule.
  controller('productsCtrl', ['$scope', '$http', function($scope, $http) {

    /** Search query */
    $scope.search = {offset: 0, length: 20, orderBy: "id", orderByASC: true, input: ""};
    
    /** Products list */
    $scope.products = [];
    
    /** Success feedback*/
    $scope.feedbackOk = {display: false, text: ""};
    
    /** Connection error on modals */
    $scope.displayConnectionError = false;
    
    /** If we continue to requests the server or not */
    $scope.loadMore = true;
    
    /**
     * Order by the product's list
     * @param {type} property
     */
    $scope.orderBy = function(property) {
        if(property === $scope.search.orderBy) {
            $scope.search.orderByASC = !$scope.search.orderByASC;
        }
        else {
            $scope.search.orderBy = property;
            $scope.search.orderByASC = true;
        }
        $scope.refreshSearch();
    };
    
    /**
     * Refresh product list with the new search query
     */
    $scope.refreshSearch = function() {
        $scope.search.offset = 0;
        $scope.search.length = 20;
        $scope.loadMore = true;
        $scope.loadProducts();
    };
    
    /**
     * Load products
     * @returns {undefined}
     */
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
    
    /**
     * Create the product
     * @param {type} dismiss function for the modal
     */
    $scope.addProduct = function(dismiss) {
        $scope.displayConnectionError = false;
        $http.post("http://localhost:8080//webresources/product/create", $scope.addModal.product)
            .success(function(data) { 
                $scope.addModal.product = {};
                $scope.products.push(data); 
                $scope.feedbackOk.display = true;  
                $scope.feedbackOk.text = "You have correctly added the product : " + data.name;
                dismiss();})
            .error(function() { 
                $scope.displayConnectionError = true; });
    };
    
    /**
     * Edit the product
     * @param {type} product to edit
     */
    $scope.editProduct = function(dismiss) {
        $scope.displayConnectionError = false;
        $http.post("http://localhost:8080//webresources/product/edit", $scope.editModal.product)
            .success(function(data) { 
                $scope.editedProduct.name = data.name; 
                $scope.editedProduct.price = data.price; 
                $scope.feedbackOk.display = true;  
                $scope.feedbackOk.text = "You have correctly edited the product : " + data.name;
                dismiss();})
            .error(function() { 
                $scope.displayConnectionError = true; });    
    };
    
    /**
     * Delete the product
     * @param {type} product to delete
     */
    $scope.deleteProduct = function(product) {
        $http.delete("http://localhost:8080//webresources/product/delete?id=" + product.id)
            .success(function() { 
                //We delete the product client side from the list if it is in
                var index = $scope.products.indexOf(product);
                if(index > -1) {
                    $scope.products.splice(index, 1);
                }
                $scope.feedbackOk.display = true;  
                $scope.feedbackOk.text = "You have correctly deleted the product : " + product.name; })
            .error(function(data) { $scope.displayConnectionError = true; });
    };
    
    /**
     * Called when an edit modal is open, copy the product to edit in the modal
     * @param {type} product to edit
     */
    $scope.initEditModal = function(product) {
        $scope.editedProduct = product;
        $scope.editModal.product.id = product.id;
        $scope.editModal.product.name = product.name; 
        $scope.editModal.product.price = product.price; 
        $scope.modal = $scope.editModal;
    };
    
    /** Modal information for add product */
    $scope.addModal = {title: "Add product", okAction: $scope.addProduct, product: {}};
    
    /** Modal information for edit product */
    $scope.editModal = {title: "Edit product", okAction: $scope.editProduct, product: {}};
  }]);