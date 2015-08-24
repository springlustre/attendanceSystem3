
function sController($scope, $http) {
    $scope.results = [];
    $scope.input = "";
    $scope.doSearch = function() {
        var httpRequest = $http({
            method : 'GET',
            url : "/search/" + $scope.input,
        }).success(function(data, status) {
            $scope.results = data;
        }).error(function(arg) {
            alert("error ");
        });

    };
    // run the search when the page loads.
    $scope.doSearch();
}