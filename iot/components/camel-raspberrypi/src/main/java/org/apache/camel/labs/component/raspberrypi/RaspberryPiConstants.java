/**
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
package org.apache.camel.labs.component.raspberrypi;

/**
 * Constants Class
 */
public final class RaspberryPiConstants {
    public static final String LOG_COMPONENT = "log:org.apache.camel.labs.labs.component.raspberrypi?showAll=true&multiline=true";
    public static final String CAMEL_ID_ROUTE = "raspberry-pi";
    public static final String PROVIDER_NAME = "RaspberryPi GPIO Provider Mock";
    public static final String CAMEL_RBPI = "CamelPi4j";
    public static final String CAMEL_RBPI_PIN = CAMEL_RBPI + ".pin";
    public static final String CAMEL_RBPI_PIN_STATE = CAMEL_RBPI + ".pinState";
    public static final String CAMEL_RBPI_PIN_TYPE = CAMEL_RBPI + ".pinType";
    public static final String CAMEL_RBPI_PIN_VALUE = CAMEL_RBPI + ".pinValue";
    public static final String CAMEL_RBPI_URL_PATTERN = "((?<scheme>raspberrypi):(//)?)?(?<type>pin|i2c|serial|spi)(:|/)(?<id>[a-zA-Z0-9_-]+)";
    public static final String CAMEL_URL_ID = "id";
    public static final String CAMEL_URL_TYPE = "type";

    private RaspberryPiConstants() {
        // Constants class
    }
}