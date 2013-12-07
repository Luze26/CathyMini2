angular.module('index').
        controller('indexCtrl', ['$scope', '$http',  function($scope) {

        $scope.listArticle = [];
        
        $scope.getArticle = function() {      
        /*    var xmlDoc = xmlParse(new URL("http://localhost:8080/assets/product/listeProduit.xml"));
            var markers = xmlDoc.documentElement.getElementsByTagName("article");

            for (var i = 0; i < markers.length; i++) {
                            var x = markers[i].textContent;
                            $scope.listArticle = $scope.listArticle.concat(x);
                            alert(x);
             }*/
            alert(test);
        };
 
}]);    
