package org.rippleosi.search.setting.table.search;

import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.common.model.RecordHeadline;
import org.rippleosi.search.common.model.SearchTablePatientDetails;
import org.rippleosi.search.setting.table.model.OpenEHRSettingResponse;

public class SettingTablePatientDetailsTransformer implements Transformer<PatientSummary, SearchTablePatientDetails> {

    private final OpenEHRSettingResponse[] openEhrResults;

    public SettingTablePatientDetailsTransformer(OpenEHRSettingResponse[] responseData) {
        this.openEhrResults = responseData;
    }

    @Override
    public SearchTablePatientDetails transform(PatientSummary patientSummary) {
        SearchTablePatientDetails details = new SearchTablePatientDetails();
        details.setSource("local");
        details.setSourceId(patientSummary.getId());
        details.setName(patientSummary.getName());
        details.setAddress(patientSummary.getAddress());
        details.setDateOfBirth(patientSummary.getDateOfBirth());
        details.setGender(patientSummary.getGender());
        details.setNhsNumber(patientSummary.getNhsNumber());

        for (OpenEHRSettingResponse result : openEhrResults) {
            OpenEHRSettingResponse associatedData;
            String nhsNumber = result.getNHSNumber();

            if (nhsNumber != null && nhsNumber.equals(patientSummary.getNhsNumber())) {
                associatedData = result;

                RecordHeadline vitalsHeadline = populateVitalsHeadline(associatedData);
                RecordHeadline ordersHeadline = populateOrdersHeadline(associatedData);
                RecordHeadline medsHeadline = populateMedsHeadline(associatedData);
                RecordHeadline resultsHeadline = populateResultsHeadline(associatedData);
                RecordHeadline treatmentsHeadline = populateTreatmentsHeadline(associatedData);

                details.setVitalsHeadline(vitalsHeadline);
                details.setOrdersHeadline(ordersHeadline);
                details.setMedsHeadline(medsHeadline);
                details.setResultsHeadline(resultsHeadline);
                details.setTreatmentsHeadline(treatmentsHeadline);
            }
        }

        return details;
    }

    private RecordHeadline populateVitalsHeadline(OpenEHRSettingResponse data) {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource("c4hOpenEHR");
        headline.setSourceId(data.getVitalsId());
        headline.setTotalEntries(data.getVitalsCount());
        headline.setLatestEntry(data.getVitalsDate());

        return headline;
    }

    private RecordHeadline populateOrdersHeadline(OpenEHRSettingResponse data) {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource("c4hOpenEHR");
        headline.setSourceId(data.getOrdersId());
        headline.setTotalEntries(data.getOrdersCount());
        headline.setLatestEntry(data.getOrdersDate());

        return headline;
    }

    private RecordHeadline populateMedsHeadline(OpenEHRSettingResponse data) {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource("c4hOpenEHR");
        headline.setSourceId(data.getMedsId());
        headline.setTotalEntries(data.getMedsCount());
        headline.setLatestEntry(data.getMedsDate());

        return headline;
    }

    private RecordHeadline populateResultsHeadline(OpenEHRSettingResponse data) {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource("c4hOpenEHR");
        headline.setSourceId(data.getLabsId());
        headline.setTotalEntries(data.getLabsCount());
        headline.setLatestEntry(data.getLabsDate());

        return headline;
    }

    private RecordHeadline populateTreatmentsHeadline(OpenEHRSettingResponse data) {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource("c4hOpenEHR");
        headline.setSourceId(data.getProceduresId());
        headline.setTotalEntries(data.getProceduresCount());
        headline.setLatestEntry(data.getProceduresDate());

        return headline;
    }
}