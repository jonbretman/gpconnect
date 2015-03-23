'use strict';

angular.module('openehrPocApp')
  .factory('Contact', function ($http) {

    var patientIdOveride = 9999999000;

    var all = function (patientId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/contacts');
    };

    var update = function (patientId, composition) {
      return $http.put('/api/patients/' + (patientIdOveride || patientId) + '/contacts', composition);
    };

    return {
      all: all,
      update: update
    };
  });