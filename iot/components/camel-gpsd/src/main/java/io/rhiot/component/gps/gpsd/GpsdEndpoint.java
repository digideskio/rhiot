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
package io.rhiot.component.gps.gpsd;

import de.taimos.gpsd4java.backend.GPSdEndpoint;
import de.taimos.gpsd4java.backend.ResultParser;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a Gpsd endpoint.
 */
@UriEndpoint(scheme = "gpsd", title = "Gpsd", syntax="gpsd:name", consumerClass = GpsdConsumer.class, label = "Gpsd")
public class GpsdEndpoint extends DefaultEndpoint {
    
    @UriPath @Metadata(required = "true")
    private String name;
    @UriParam(defaultValue = "2947")
    private int port = 2947;
    @UriParam(defaultValue = "localhost")
    private String host = "10.0.0.13";

    private GPSdEndpoint gpsd4javaEndpoint;
    
    public GpsdEndpoint() {
    }

    public GpsdEndpoint(String uri, GpsdComponent component) {
        super(uri, component);
    }

    public GpsdEndpoint(String endpointUri) {
        super(endpointUri);
    }

    // Producer/consumer factories

    @Override
    public Producer createProducer() throws Exception {
        throw new UnsupportedOperationException("GPSD component supports only consumer endpoints.");
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        GpsdConsumer consumer = new GpsdConsumer(this, processor);
        configureConsumer(consumer);
        return consumer;
    }

    // Life cycle

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        gpsd4javaEndpoint = new GPSdEndpoint(host, port, new ResultParser());
    }

    // Configuration

    @Override
    public boolean isSingleton() {
        return true;
    }

    // Configuration getters and setters


    public GPSdEndpoint getGpsd4javaEndpoint() {
        return gpsd4javaEndpoint;
    }
    
}