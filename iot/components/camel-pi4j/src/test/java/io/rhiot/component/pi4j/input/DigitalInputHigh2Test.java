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
package io.rhiot.component.pi4j.input;

import io.rhiot.component.pi4j.gpio.GPIOConsumer;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiGpioProvider;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.mockito.Mockito;

public class DigitalInputHigh2Test extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint mock;

    @Test
    public void consumeDigitalEventWithIncorrectState() throws Exception {

        GPIOConsumer pin = (GPIOConsumer)context.getRoute("test-route").getConsumer();

        mock.expectedMessageCount(0);

        pin.handleGpioPinDigitalStateChangeEvent(new GpioPinDigitalStateChangeEvent("CAMEL-EVENT", (GpioPin)pin.getPin(), PinState.LOW));

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                GpioProvider factory = Mockito.mock(RaspiGpioProvider.class);

                GpioFactory.setDefaultProvider(factory);
                from("pi4j-gpio://16?mode=DIGITAL_INPUT&state=HIGH").id("test-route").to("log:com.github.camellabs.component.pi4j?showAll=true&multiline=true")
                    .to("mock:result");

            }
        };
    }
}
