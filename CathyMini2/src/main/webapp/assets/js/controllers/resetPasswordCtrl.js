/**
 * Controller for the reset password page
 */
angular.module('resetPassword')
.controller('resetPasswordCtrl', ['$scope', '$http', 'notificationService', function($scope, $http, notificationService) {
    
    function getParameterByName(name) {
        name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
        return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    }

    $scope.resetForm = {username: getParameterByName('username'), token: getParameterByName('token'), pwd: "", confirmPwd: ""};
    
    $scope.error = null;
    $scope.success = null;
    
    $scope.resetPassword = function() {
        $scope.error = null;
        $http.post("/webresources/consumer/resetPasswordForm", $scope.resetForm).success(function() {
            $scope.success = "Votre mot de passe a bien été changé.";
            notificationService.displayMessage("Vous pouvez maintenant utilisez votre nouveau mot de passe.");
        }).error(function(data, status) {
            if(status === 400) {
                if(data === "user not found") {
                    $scope.error = "Utilisateur pour lequel la demande de nouveau mot de passe n'est pas trouvable, vérifiez d'avoir suivi le bon lien.";
                }
                else {
                    $scope.error = "Token invalide, vérifier d'avoir bien suivi le lien du dernier message de nouveau mot de passe.";
                }
            }
            else {
                $scope.error = "Problème de connexion, vérifier votre connexion internet.";
            }
        });
    };
    
    /**
     * And function, because xhtml not allow & in attributes
     * @param {type} x
     * @param {type} y
     * @returns {boolean} x && y
     */
    $scope.and = function(x,y) {
        return x && y;
    };
}]);

