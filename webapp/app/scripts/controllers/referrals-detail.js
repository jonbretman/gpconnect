'use strict';

angular.module('gpConnect')
  .controller('ReferralsDetailCtrl', function ($scope, $stateParams, $modal, $location, $state, usSpinnerService, PatientService, Referral) {

    Referral.findDetails($stateParams.patientId, $stateParams.referralId).then(function (result) {
      $scope.referral = result.data;
      usSpinnerService.stop('referralsDetail-spinner');
    });

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/referrals/referrals-modal.html',
        size: 'lg',
        controller: 'ReferralsModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit Referral'
            };
          },
          referral: function () {
            return angular.copy($scope.referral);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (referral) {
        referral.dateOfReferral = new Date(referral.dateOfReferral);
        referral.dateOfReferral.setMinutes(referral.dateOfReferral.getMinutes() - referral.dateOfReferral.getTimezoneOffset());

        var toUpdate = {
          sourceId: referral.sourceId,
          author: referral.author,
          clinicalSummary: referral.clinicalSummary,
          dateCreated: new Date(referral.dateCreated),
          dateOfReferral: referral.dateOfReferral,
          reason: referral.reason,
          referralFrom: referral.referralFrom,
          referralTo: referral.referralTo,
          source: referral.source
        };

        Referral.update($scope.patient.id, toUpdate).then(function () {
          setTimeout(function () {
            $state.go('referrals-detail', {
              patientId: $scope.patient.id,
              referralId: referral.sourceId,
              page: $scope.currentPage
            });
          }, 2000);
        });
      });
    };

  });
