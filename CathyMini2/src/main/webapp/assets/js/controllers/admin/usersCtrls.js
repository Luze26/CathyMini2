angular.module('users').
  controller('usersCtrl', ['$scope', '$http', function($scope, $http) {

      /** Search query */
      $scope.search = {offset: 0, length: 20, orderBy: "id", orderByASC: true, input: ""};

      /** Products list */
      $scope.users = [];

      /** Success feedback*/
      $scope.feedbackOk = {display: false, text: ""};

      /** Connection error on modals */
      $scope.displayConnectionError = false;

      /** If we continue to requests the server or not */
      $scope.loadMore = true;
      /**
       * Order by the user's list
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
        $scope.loadUsers();
      };

      /**
       * Load products
       * @returns {undefined}
       */
      $scope.loadUsers = function() {
        if ($scope.loadMore) {
          $http.post("/webresources/consumer/all", $scope.search)
            .success(function(data) {
              if (data.length < $scope.search.length) {
                $scope.loadMore = false;
              }

              if ($scope.search.offset === 0) {
                $scope.users = [];
              }

              $scope.search.offset += $scope.search.length;

              $scope.users = $scope.users.concat(data);
            });
        }
      };
      
      /**
       * Edit the user
       */
      $scope.editUser = function(dismiss) {
        var editedUser = $scope.editedProduct;
        $scope.displayConnectionError = false;
        $http.post("/webresources/consumer/edit", $scope.editModal.user)
          .success(function(data) {
            editedUser.username = data.username;
            editedUser.mail = data.mail;
            editedUser.address = data.address;
            $scope.feedbackOk.display = true;
            $scope.feedbackOk.text = "You have correctly edited the user : " + data.username;
            dismiss()
          })
          .error(function() {
            $scope.displayConnectionError = true;
          });
      };

      /**
       * Delete the user
       * @param {User} user to delete
       */
      $scope.deleteUser = function(user) {
        $http.delete("/webresources/consumer/deleteAdmin?id=" + user.id)
          .success(function(data) {
            //We delete the product client side from the list if it is in
            var index = $scope.users.indexOf(user);
            if (index > -1) { //If the product is still displayed, we delete it from the list
              $scope.users.splice(index, 1);
            }
            //Display success feedback
            $scope.feedbackOk.display = true;
            $scope.feedbackOk.text = data;
;          })
          .error(function(data) {
            $scope.feedbackOk.display = true;
            $scope.feedbackOK.text = data;
            $scope.displayConnectionError = true; // display error feedback
          });
      };

      /**
       * Called when an edit modal is open, copy the user to edit in the modal
       * @param {User} user to edit
       */
      $scope.initEditModal = function(user) {
        $scope.editedUser = user;
        $scope.editModal.user.id = user.id;
        $scope.editModal.user.username = user.username;
        $scope.editModal.user.mail = user.mail;
        $scope.editModal.user.address = user.address;
        $scope.modal = $scope.editModal;
      };

      /** Modal information for edit product */
      $scope.editModal = {title: "Edit user", okAction: $scope.editUser, user: {}, add: false};
    }]);