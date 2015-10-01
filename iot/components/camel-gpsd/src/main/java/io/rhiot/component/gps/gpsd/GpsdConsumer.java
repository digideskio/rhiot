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
import de.taimos.gpsd4java.types.TPVObject;
import io.rhiot.component.gps.bu353.ClientGpsCoordinates;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Gpsd consumer.
 */
public class GpsdConsumer extends DefaultConsumer {

    private final static Logger LOG = LoggerFactory.getLogger(GpsdConsumer.class);
    
    private volatile GpsdEndpoint endpoint;

    public GpsdConsumer(GpsdEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }
    
    
    @Override
    protected void doStart() throws Exception {
        log.debug("Started GPSD consumer.");
            
        try {

            GPSdEndpoint gpsd4javaEndpoint = getEndpoint().getGpsd4javaEndpoint();
            //todo register listeners in registry 
            gpsd4javaEndpoint.addListener(new ObjectListener() {

                @Override
                public void handleTPV(final TPVObject tpv) {
                    Exchange exchange = endpoint.createExchange();
                    try {
                        LOG.info("Consuming TPV: {}", tpv);
                        //todo register listeners to handle, for this payload or Distance. (mind overlap with other geo-fencing stuff)
                        
                        // todo 2 Also consider if we want exposure to the real gpsd4java Pojo or to add fields to ClientGpsCoordinates for speed, bearing etc. 
                        // Maybe even both Pojos on the exchange, the original as a property and our Pojo as the payload 
                        exchange.getOut().setBody(new ClientGpsCoordinates(new Date(new Double(tpv.getTimestamp()).longValue()), 
                                tpv.getLatitude(), tpv.getLongitude())); 
                        getProcessor().process(exchange);
                    } catch (Exception e) {
                        exchange.setException(e);
                    } 
                }
            });

            gpsd4javaEndpoint.start();

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
    
}