/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.elasticsearch.hadoop.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.elasticsearch.hadoop.serialization.ParsingUtils;
import org.elasticsearch.hadoop.serialization.json.JacksonJsonParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParseBulkErrorsTest {

    @Test
    public void testParseItems() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader r = mapper.reader(Map.class);
        InputStream in = getClass().getResourceAsStream("bulk-error.json");
        JsonParser parser = mapper.getJsonFactory().createJsonParser(in);
        ParsingUtils.seek("items", new JacksonJsonParser(parser));

        for (Iterator<Map> iterator = r.readValues(parser); iterator.hasNext();) {
            Map map = iterator.next();
            String error = (String) ((Map) map.values().iterator().next()).get("error");
            assertNotNull(error);
            assertTrue(error.contains("document already exists"));
        }
    }
}
