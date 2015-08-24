/**
 * Created by lustre on 2015/8/10.
 */

function SearchController($scope, $http) {
    $scope.results = [];
    $scope.input = "";
    $scope.modifyDiv = false;

    $scope.doSearch = function(staff_id) {
        alert(staff_id)
        $scope.modifyDiv=!$scope.modifyDiv
        $scope.modifyDiv = true;
        var httpRequest = $http({
            method : 'GET',
            url : "/search/" + staff_id,
        }).success(function(data, status) {
            $scope.results = data;
        }).error(function(arg) {
            alert("error ");
        });

    };
// run the search when the page loads.
    $scope.doSearch();
}