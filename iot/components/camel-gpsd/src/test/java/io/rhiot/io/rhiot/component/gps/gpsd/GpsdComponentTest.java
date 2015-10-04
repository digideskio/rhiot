/**
 * Licensed to the Rhiot under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.rhiot.io.rhiot.component.gps.gpsd;

import de.taimos.gpsd4java.types.TPVObject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class GpsdComponentTest extends CamelTestSupport {
    

    @Test
    @Ignore("Ignoring for now, should detect if pi is available and listening on 2947")
    public void testGpsd() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:foo");
        mock.expectedMinimumMessageCount(9);
        
        //Should get at least 9 messages within 10 seconds
//        assertMockEndpointsSatisfied(10, TimeUnit.SECONDS);
        Thread.sleep(1000000);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("gpsd://gpsSpeedTest?host=10.0.0.15&port=2947").routeId("gpsdSpeed")
                    .process(exchange -> {
                        TPVObject tpvObject = exchange.getIn().getHeader("io.rhiot.gpsd.gpsObject", TPVObject.class);
                        if (tpvObject.getSpeed() > 0) {
                            log.warn("Moving at [{}] meters/second, course [{}]", tpvObject.getSpeed(), tpvObject.getCourse());
                        } else {
                            log.info("GPS is Stationary");
                        }
                    });
            }
        };
    }
}
