package com.liferay.jenkins.results.parser;

import com.liferay.jenkins.results.parser.test.clazz.group.PlaywrightBatchTestClassGroup;
import org.json.JSONObject;

public class QAWebsitesPlaywrightBatchTestClassGroup  extends PlaywrightBatchTestClassGroup {
    public QAWebsitesPlaywrightBatchTestClassGroup(String batchName, PortalTestClassJob portalTestClassJob) {
        super(batchName, portalTestClassJob);
    }

    public QAWebsitesPlaywrightBatchTestClassGroup(JSONObject jsonObject, PortalTestClassJob portalTestClassJob) {

        super(jsonObject, portalTestClassJob);
    }
}
