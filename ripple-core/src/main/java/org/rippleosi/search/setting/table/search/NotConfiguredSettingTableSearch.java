package org.rippleosi.search.setting.table.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.setting.table.model.SettingTableQuery;
import org.rippleosi.search.setting.table.model.SettingTableResults;

public class NotConfiguredSettingTableSearch implements SettingTableSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public SettingTableResults findAssociatedPatientData(SettingTableQuery tableQuery,
                                                         List<PatientSummary> patientSummaries) {
        throw ConfigurationException.unimplementedTransaction(SettingTableSearch.class);
    }
}