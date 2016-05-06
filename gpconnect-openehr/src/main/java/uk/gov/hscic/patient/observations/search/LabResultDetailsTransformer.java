/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package uk.gov.hscic.patient.observations.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.util.DateFormatter;
import uk.gov.hscic.patient.observations.model.ObservationDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class LabResultDetailsTransformer implements Transformer<Map<String, Object>, ObservationDetails> {

    private static final Logger logger = LoggerFactory.getLogger(LabResultDetailsTransformer.class);

    @Override
    public ObservationDetails transform(Map<String, Object> input) {

        Date sampleTaken = DateFormatter.toDate(MapUtils.getString(input, "sample_taken"));
        Date dateCreated = DateFormatter.toDate(MapUtils.getString(input, "date_created"));

        ObservationDetails labResult = new ObservationDetails();
        labResult.setSource("Marand");
        labResult.setSourceId(MapUtils.getString(input, "uid"));
        labResult.setTestName(MapUtils.getString(input, "test_name"));
        labResult.setSampleTaken(sampleTaken);
        labResult.setDateCreated(dateCreated);
        labResult.setAuthor(MapUtils.getString(input, "author"));
        labResult.setConclusion(MapUtils.getString(input, "conclusion"));
        labResult.setStatus(MapUtils.getString(input, "status"));

        List<ObservationDetails.TestResult> testResults = createTestResults(input);

        labResult.setTestResults(testResults);

        return labResult;
    }

    private List<ObservationDetails.TestResult> createTestResults(Map<String, Object> input) {

        List<Map<String, Object>> labResults = extractLabResults(input);

        return CollectionUtils.collect(labResults, new TestResultTransformer(), new ArrayList<>());
    }

    private List<Map<String, Object>> extractLabResults(Map<String, Object> input) {
        try {
            return (List<Map<String, Object>>)PropertyUtils.getNestedProperty(input, "test_panel.items");
        } catch (Exception ex) {
            logger.debug("{}: {}", ex.getClass().getName(), ex.getMessage());
            return Collections.emptyList();
        }
    }
}