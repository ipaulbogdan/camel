/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.dsl.xml.jaxb.definition;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.ExtendedCamelContext;
import org.apache.camel.spi.Resource;
import org.apache.camel.support.PluginHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoadRouteFromXmlWithInterceptTest extends ContextTestSupport {

    @Override
    public boolean isUseRouteBuilder() {
        return false;
    }

    @Test
    public void testLoadRouteFromXmlWithIntercept() throws Exception {
        ExtendedCamelContext ecc = context.getCamelContextExtension();
        Resource resource
                = ecc.getResourceLoader().resolveResource("org/apache/camel/dsl/xml/jaxb/definition/barInterceptorRoute.xml");
        PluginHelper.getRoutesLoader(ecc).loadRoutes(resource);
        context.start();

        assertNotNull(context.getRoute("bar"), "Loaded bar route should be there");
        assertEquals(1, context.getRoutes().size());

        // test that loaded route works
        getMockEndpoint("mock:bar").expectedBodiesReceived("Bye World");
        getMockEndpoint("mock:intercept").expectedBodiesReceived("Bye World");

        template.sendBody("direct:bar", "Bye World");

        assertMockEndpointsSatisfied();
    }
}
