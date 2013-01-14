/*
 * Copyright (C) 2012 salesforce.com, inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.auraframework;

import junit.framework.TestSuite;

import org.auraframework.test.TestInventory;
import org.auraframework.test.TestInventory.Type;
import org.auraframework.util.ServiceLocator;

public class AuraUtilUnitTestSuite {
    public static TestSuite suite() throws Exception {
        TestInventory inventory = ServiceLocator.get().get(TestInventory.class, "auraUtilTestInventory");
        TestSuite suite = inventory.getTestSuite(Type.UNIT);
        suite.setName("aura-util unit tests");
        return suite;
    }
}
