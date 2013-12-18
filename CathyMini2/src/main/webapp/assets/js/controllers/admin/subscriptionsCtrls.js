angular.module('subscriptions').
  controller('subscriptionsCtrl', ['$scope', '$http', function($scope, $http) {

      /** Search query */
      $scope.search = {offset: 0, length: 20, orderBy: "id", orderByASC: true};

      /** Subscription list */
      $scope.subscriptions = [];

      /** Success feedback*/
      $scope.feedbackOk = {display: false, text: ""};

      /** Connection error on modals */
      $scope.displayConnectionError = false;

      /** If we continue to requests the server or not */
      $scope.loadMore = true;

      $scope.showSubscriptionDetails = function(subscription) {
        if ($scope.selectedSubscription !== subscription) {
          $scope.selectedSubscription = subscription;
        } else {
          $scope.selectedSubscription = -1;
        }
      };

      /**
       * Order by the subscription's list
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
        $scope.loadSubscriptions();
      };

      /**
       * Load products
       * @returns {undefined}
       */
      $scope.loadSubscriptions = function() {
        if ($scope.loadMore) {
          $http.post("/webresources/purchase/allSubscriptions")
            .success(function(data) {
              if (data.length < $scope.search.length) {
                $scope.loadMore = false;
              }

              if ($scope.search.offset === 0) {
                $scope.subscriptions = [];
              }

              $scope.search.offset += $scope.search.length;

              $scope.subscriptions = $scope.subscriptions.concat(data);
              console.log(data);
              console.log($scope.subscriptions);
            });
        }
      };

      /**
       * Delete the user
       * @param {Subscription} subscription to delete
       */
      $scope.deleteSubscription = function(subscription) {
        /*
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
            ;
          })
          .error(function(data) {
            $scope.feedbackOk.display = true;
            $scope.feedbackOK.text = data;
            $scope.displayConnectionError = true; // display error feedback
          });
          */
      };

    }]);