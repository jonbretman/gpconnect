'use strict';

angular.module('gpConnect')
  .factory('AdvancedSearch', function($modal, $http, ProviderRouting) {

    var isModalClosed = true;

    var openAdvancedSearch = function(expression) {
      if (isModalClosed) {
        isModalClosed = false;

        var modalInstance = $modal.open({
          templateUrl: 'views/search/advanced-search-modal.html',
          size: 'lg',
          controller: 'AdvancedSearchController',
          resolve: {
            modal: function() {
              return {
                title: 'Advanced Search'
              };
            },
            searchParams: function() {
              var params = {};
              if (!isNaN(expression)) {
                params.nhsNumber = expression;
              } else {
                params.surname = expression;
              }

              return params;
            }
          }
        });
      }

      modalInstance.result.then(function() {
        isModalClosed = true;
      }, function() {
        isModalClosed = true;
      });
    };

    var searchByDetails = function (queryParams) {
      return $http.post(ProviderRouting.defaultPractice().apiEndpointURL + '/patients/advancedSearch', queryParams);
    };

    return {
      isModalClosed: isModalClosed,
      openAdvancedSearch: openAdvancedSearch,
      searchByDetails: searchByDetails
    }
  });
