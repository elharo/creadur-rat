/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rat.license;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.apache.rat.config.parameters.Component;
import org.apache.rat.config.parameters.Description;
import org.apache.rat.testhelpers.TestingMatcher;
import org.junit.jupiter.api.Test;

public class SimpleLicenseTest {

    @Test
    public void descriptionTest() {
        SimpleLicense lic = new SimpleLicense(
                ILicenseFamily.builder().setLicenseFamilyCategory("familyId")
                        .setLicenseFamilyName("TestingLicense: familyId").build(),
                new TestingMatcher(), "These are the notes", "My testing license", "TestingId");
        Description underTest = lic.getDescription();
        assertEquals(Component.Type.License, underTest.getType());
        assertEquals("My testing license", underTest.getCommonName());
        assertEquals("", underTest.getDescription());
        assertNull(underTest.getParamValue(lic));
        Map<String, Description> children = underTest.getChildren();
        assertTrue(children.containsKey("id"));
        assertTrue(children.containsKey("name"));
        assertTrue(children.containsKey("notes"));
        assertTrue(children.containsKey("TestingMatcher"));
    }
}
