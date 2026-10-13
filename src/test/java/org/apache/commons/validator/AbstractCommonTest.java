/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * Consolidates reading in XML config file into parent class.
 */
public abstract class AbstractCommonTest {

    /** apache Logging interface instance */
    protected Log log = LogFactory.getLog(getClass());
	// init after reread the logging configuration in logManager.readConfiguration!
    // Test Dauer: 31.675 s , erheblich länger mit logging.properties: 04:19 min
    static Logger LOG;
    static LogManager logManager = LogManager.getLogManager(); // Singleton
    static {
        URL url = AbstractCommonTest.class.getClassLoader().getResource("logging.properties");
        try {
            File file = new File(url.toURI());
            logManager.readConfiguration(new FileInputStream(file));
        } catch (IOException | URISyntaxException e) {
            LOG = Logger.getLogger(AbstractCommonTest.class.getName());
            LOG.warning(e.getMessage());
        }
        LOG = Logger.getLogger(AbstractCommonTest.class.getName());
    }
    /**
     * Resources used for validation tests.
     */
    protected ValidatorResources resources;
    protected String name;

    /**
     * Load {@code ValidatorResources} from validator-numeric.xml.
     */
    protected void loadResources(final String file) throws IOException, SAXException {
        // Load resources
        LOG.config("Load resources from "+file);
        try (InputStream in = this.getClass().getResourceAsStream(file)) {
            resources = new ValidatorResources(in);
        }
    }
}
