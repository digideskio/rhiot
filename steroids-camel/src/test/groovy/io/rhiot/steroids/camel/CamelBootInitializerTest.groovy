/**
 * Licensed to the Rhiot under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The licenses this file to You under the Apache License, Version 2.0
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
package io.rhiot.steroids.camel

import io.rhiot.steroids.bootstrap.Bootstrap
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.mock.MockEndpoint
import org.junit.Test

import static io.rhiot.steroids.camel.CamelBootInitializer.camelContext

class CamelBootInitializerTest {

    @Test
    void shouldStartCamelRoute() {
        new Bootstrap().start()
        def mock = camelContext().getEndpoint('mock:test', MockEndpoint.class)
        mock.setExpectedMessageCount(1)

        // When
        camelContext().createProducerTemplate().sendBody('event-bus:mock', 'foo')

        // Then
        mock.assertIsSatisfied()
    }

}

@Route
class MyRoute extends RouteBuilder {

    @Override
    void configure() {
        from('event-bus:mock').to('mock:test')
    }

}