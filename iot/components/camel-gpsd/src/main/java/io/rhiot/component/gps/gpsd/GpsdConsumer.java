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

import java.util.Date;

import de.taimos.gpsd4java.api.ObjectListener;
import de.taimos.gpsd4java.backend.GPSdEndpoint;
import de.taimos.gpsd4java.types.IGPSObject;
import de.taimos.gpsd4java.types.TPVObject;
import io.rhiot.component.gps.bu353.ClientGpsCoordinates;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Gpsd consumer.
 */
public class GpsdConsumer extends DefaultConsumer {

    private final static Logger LOG = LoggerFactory.getLogger(GpsdConsumer.class);
    
    public GpsdConsumer(GpsdEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }
    
    
    @Override
    protected void doStart() throws Exception {
        log.info("Starting GPSD consumer.");
            
        try {

            GPSdEndpoint gpsd4javaEndpoint = getEndpoint().getGpsd4javaEndpoint();
            //todo register listeners in registry 
            gpsd4javaEndpoint.addListener(new ObjectListener() {

                @Override
                public void handleTPV(final TPVObject tpv) {
                    Exchange exchange = createOutOnlyExchangeWithBodyAndHeaders(getEndpoint(), 
                            new ClientGpsCoordinates(new Date(new Double(tpv.getTimestamp()).longValue()), tpv.getLatitude(), tpv.getLongitude()), tpv);
                    try {
                        LOG.debug("Consuming Time-Position-Velocity : {}", tpv);
                        //todo register listeners to handle, for this payload or Distance. (mind overlap with other geo-fencing stuff)
                        
                        getProcessor().process(exchange);
                    } catch (Exception e) {
                        exchange.setException(e);
                    } 
                }
            });

            gpsd4javaEndpoint.start();
            log.info("Started GPSD consumer.");

            LOG.info("GPSD Version: {}", gpsd4javaEndpoint.version());

            gpsd4javaEndpoint.watch(true, true);
            
        } catch (Exception e) {
            getExceptionHandler().handleException(e);
        }
    }

    @Override
    public GpsdEndpoint getEndpoint() {
        return (GpsdEndpoint) super.getEndpoint();
    }

    @Override
    protected void doStop() throws Exception {
        getEndpoint().getGpsd4javaEndpoint().stop();

        super.doStop();
    }

    protected Exchange createOutOnlyExchangeWithBodyAndHeaders(org.apache.camel.Endpoint endpoint, ClientGpsCoordinates messageBody, IGPSObject gpsObject) {
        Exchange exchange = endpoint.createExchange(ExchangePattern.OutOnly);
        Message message = exchange.getIn();
        message.setHeader("io.rhiot.gpsd.host", getEndpoint().getHost());
        message.setHeader("io.rhiot.gpsd.port", getEndpoint().getPort());
        message.setHeader("io.rhiot.gpsd.gpsObject", gpsObject);
        message.setBody(messageBody);
        return exchange;
    }
    
}