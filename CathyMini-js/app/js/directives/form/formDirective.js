var INTEGER_REGEXP = /^\-?\d*$/;
commonModule.directive('integer', function() {
  return {
    require: 'ngModel',
    link: function(scope, elm, attrs, ctrl) {
      ctrl.$parsers.unshift(function(viewValue) {
        if (INTEGER_REGEXP.test(viewValue)) {
          // it is valid
          ctrl.$setValidity('integer', true);
          return viewValue;
        } else {
          // it is invalid, return undefined (no model update)
          ctrl.$setValidity('integer', false);
          return undefined;
        }
      });
    }
  };
});

var FLOAT_REGEXP = /^\-?\d+((\.|\,)\d+)?$/;
commonModule.directive('smartFloat', function() {
  return {
    require: 'ngModel',
    link: function(scope, elm, attrs, ctrl) {        
      ctrl.$parsers.unshift(function(viewValue) {
        if (FLOAT_REGEXP.test(viewValue)) {
          var float = parseFloat(viewValue.replace(',', '.'));
          if(float > 0) {
            ctrl.$setValidity('float', true);
            return float;
          }
          ctrl.$setValidity('float', false);
          return undefined;
        } else {
          ctrl.$setValidity('float', false);
          return undefined;
        }
      });
    }
  };
});


