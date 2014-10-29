Tips to help with web development in the new UI
--

JS - don't use toSource() on an object in debugging..using JSON.stringify()


$scope.$emit('configureShowPatientList', false); to hide the patient list


use something like this in your controllers. When the app is loaded, securityService.user will be set ..just
watch it, and when it becomes available, your controller can run it's code dependent on that data.

	$scope.$watch(function() {
		  return securityService.getUser();
		}, function(newVal) {
		  $scope.me = newVal;
		}, true);




