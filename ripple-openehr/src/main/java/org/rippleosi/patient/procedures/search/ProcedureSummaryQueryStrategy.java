package org.rippleosi.patient.procedures.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.procedures.model.ProcedureSummaries;

/**
 */
public class ProcedureSummaryQueryStrategy extends AbstractQueryStrategy<ProcedureSummaries> {

    ProcedureSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "b_a/description[at0001]/items[at0002]/value/value as procedure_name, " +
                "b_a/time/value as procedure_date " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
                "contains ACTION b_a[openEHR-EHR-ACTION.procedure.v1] " +
                "where a/name/value='Procedures list' ";
    }

    @Override
    public ProcedureSummaries transform(List<Map<String, Object>> resultSet) {

        List<ProcedureSummaries.Procedure> procedureList = new ArrayList<>();

        for (Map<String, Object> data : resultSet) {
            ProcedureSummaries.Procedure procedure = new ProcedureSummaries.Procedure();

            String uid = MapUtils.getString(data, "uid");
            String name = MapUtils.getString(data, "procedure_name");
            String dateAsString = MapUtils.getString(data, "procedure_date");
            Date date = DateFormatter.toDate(dateAsString);

            procedure.setId(uid);
            procedure.setName(name);
            procedure.setDate(date);
            procedure.setSource("openehr");

            procedureList.add(procedure);
        }

        ProcedureSummaries summaries = new ProcedureSummaries();
        summaries.setProcedures(procedureList);

        return summaries;
    }
}
