angular.module('products').
  controller('productsCtrl', ['$scope', '$http', '$timeout', '$upload', function($scope, $http, $location, userSettingsService, $timeout, $upload) {

      $scope.uploadRightAway = true;
      $scope.dataUrls = [];
      $scope.changeAngularVersion = function() {
        window.location.hash = $scope.angularVersion;
        window.location.reload(true);
      }
      $scope.hasUploader = function(index) {
        return $scope.upload[index] !== null;
      };
      $scope.abort = function(index) {
        $scope.upload[index].abort();
        $scope.upload[index] = null;
      };
      $scope.angularVersion = window.location.hash.length > 1 ? window.location.hash.substring(1) : '1.2.0';
      $scope.onFileSelect = function($files) {
        $scope.selectedFiles = [];
        $scope.progress = [];
        if ($scope.upload && $scope.upload.length > 0) {
          for (var i = 0; i < $scope.upload.length; i++) {
            if ($scope.upload[i] !== null) {
              $scope.upload[i].abort();
            }
          }
        }
        $scope.upload = [];
        $scope.uploadResult = [];
        $scope.selectedFiles = $files;
        $scope.dataUrls = [];
        for (var i = 0; i < $files.length; i++) {
          var $file = $files[i];
          if (window.FileReader && $file.type.indexOf('image') > -1) {
            var fileReader = new FileReader();
            fileReader.readAsDataURL($files[i]);
            function setPreview(fileReader, index) {
              fileReader.onloadend = function(e) {
                $scope.dataUrls[index] = e.target.result;
              }
            }
            setPreview(fileReader, i);
          }
          $scope.progress[i] = -1;
          if ($scope.uploadRightAway) {
            $scope.start(i);
          }
        }
      }

      $scope.start = function(index) {
        $http.post("/webresources/product/uploadFile", $scope.selectedFiles[index])
          .success(function(data) {
            console.log(data);
          });
        /*
         $scope.upload[index] = $upload.upload({
         url: 'assets/products/',
         headers: {'myHeaderKey': 'myHeaderVal'},
         /* formDataAppender: function(fd, key, val) {
         if (angular.isArray(val)) {
         angular.forEach(val, function(v) {
         fd.append(key, v);
         });
         } else {
         fd.append(key, val);
         }
         }, */
        /*        file: $scope.selectedFiles[index],
         fileFormDataName: 'myFile'
         }).then(function(response) {
         $scope.uploadResult.push(response.data.result);
         }, null, function(evt) {
         $scope.progress[index] = parseInt(100.0 * evt.loaded / evt.total);
         });*/
      }






      /** Search query */
      $scope.search = {offset: 0, length: 20, orderBy: "id", orderByASC: true, input: "", tampon: true,
        napkin: true, minPrice: 0, maxPrice: 100, brands: [], flux: [1, 2, 3, 4, 5, 6]};

      /** Products list */
      $scope.products = [];

      /** Success feedback*/
      $scope.feedbackOk = {display: false, text: ""};

      /** Connection error on modals */
      $scope.displayConnectionError = false;

      /** If we continue to requests the server or not */
      $scope.loadMore = true;

      /** Description of flux **/
      $scope.selects = [
        {
          "id": 1,
          "print": "<div class=\"drops-c drops-c-6 drops-active-1\"><div class=\"drops-inner\"></div></div><span>Mini</span>"
        },
        {
          "id": 2,
          "print": "<div class=\"drops-c drops-c-6 drops-active-2\"><div class=\"drops-inner\"></div></div><span>Normal</span>"
        },
        {
          "id": 3,
          "print": "<div class=\"drops-c drops-c-6 drops-active-3\"><div class=\"drops-inner\"></div></div><span>Normal+</span>"
        },
        {
          "id": 4,
          "print": "<div class=\"drops-c drops-c-6 drops-active-4\"><div class=\"drops-inner\"></div></div><span>Super</span>"
        },
        {
          "id": 5,
          "print": "<div class=\"drops-c drops-c-6 drops-active-5\"><div class=\"drops-inner\"></div></div><span>Super+</span>"
        },
        {
          "id": 6,
          "print": "<div class=\"drops-c drops-c-6 drops-active-6\"><div class=\"drops-inner\"></div></div><span>Extra</span>"
        }
      ];

      /**
       * Order by the product's list
       * @param {type} property
       */
      $scope.orderBy = function(property) {
        if (property === $scope.search.orderBy) {
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
        if ($scope.loadMore) {
          $http.post("/webresources/product/all", $scope.search)
            .success(function(data) {
              if (data.length < $scope.search.length) {
                $scope.loadMore = false;
              }

              if ($scope.search.offset === 0) {
                $scope.products = [];
              }

              $scope.search.offset += $scope.search.length;

              $scope.products = $scope.products.concat(data);
            });
        }
      };

      /**
       * Create the product
       */
      $scope.addProduct = function(dismiss) {
        $scope.displayConnectionError = false;
        $http.post("/webresources/product/create", $scope.addModal.product)
          .success(function(data) {
            $scope.addModal.product = {type: "Serviette", selectedItem: "1"};
            $scope.products.push(data);
            $scope.feedbackOk.display = true;
            $scope.feedbackOk.text = "You have correctly added the product : " + data.name;
            dismiss();
          })
          .error(function() {
            $scope.displayConnectionError = true;
          });
      };

      /**
       * Edit the product
       */
      $scope.editProduct = function(dismiss) {
        var editedProduct = $scope.editedProduct;
        $scope.displayConnectionError = false;
        $http.post("/webresources/product/edit", $scope.editModal.product)
          .success(function(data) {
            editedProduct.name = data.name;
            editedProduct.marque = data.marque;
            editedProduct.flux = data.flux;
            editedProduct.price = data.price;
            editedProduct.description = data.description;
            $scope.feedbackOk.display = true;
            $scope.feedbackOk.text = "You have correctly edited the product : " + data.name;
            dismiss()
          })
          .error(function() {
            $scope.displayConnectionError = true;
          });
      };

      /**
       * Delete the product
       * @param {Product} product to delete
       */
      $scope.deleteProduct = function(product) {
        $http.delete("/webresources/product/delete?id=" + product.id)
          .success(function() {
            //We delete the product client side from the list if it is in
            var index = $scope.products.indexOf(product);
            if (index > -1) { //If the product is still displayed, we delete it from the list
              $scope.products.splice(index, 1);
            }
            //Display success feedback
            $scope.feedbackOk.display = true;
            $scope.feedbackOk.text = "You have correctly deleted the product : " + product.name;
          })
          .error(function(data) {
            $scope.displayConnectionError = true; // display error feedback
          });
      };

      /**
       * Called when an edit modal is open, copy the product to edit in the modal
       * @param {Product} product to edit
       */
      $scope.initEditModal = function(product) {
        $scope.editedProduct = product;
        $scope.editModal.product.id = product.id;
        $scope.editModal.product.name = product.name;
        $scope.editModal.product.marque = product.marque;
        $scope.editModal.product.flux = product.flux;
        $scope.editModal.product.price = product.price;
        $scope.editModal.product.description = product.description;
        $scope.modal = $scope.editModal;
      };

      /* Essai d'autocomplétion
       // mock des cartes dans lesquelles rechercher
       $scope.cardslist = [
       {'name': 'Skylasher'},
       {'name': 'Thrashing Mossdog'},
       {'name': 'Zhur-Taa Druid'},
       {'name': 'Feral Animist'},
       {'name': 'Rubblebelt Maaka'},
       {'name': 'Mending Touch'},
       {'name': 'Weapon Surge'},
       {'name': 'Woodlot Crawler'},
       {'name': 'Phytoburst'},
       {'name': 'Smelt-Ward Gatekeepers'},
       {'name': 'Debt to the Deathless'},
       {'name': 'Woodlot Crawler'},
       {'name': 'Blaze Commando'},
       {'name': 'Uncovered Clues'}
       ];
       
       // saisie du nom de la carte
       $scope.card = null;
       */

      /** Modal information for add product **/
      $scope.addModal = {title: "Add product", okAction: $scope.addProduct, product: {type: "Serviette", flux: 1}, add: true};

      /** Modal information for edit product */
      $scope.editModal = {title: "Edit product", okAction: $scope.editProduct, product: {}, add: false};
    }]);
