/*
 *
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
 *
*/
package org.seagrid.desktop.util.messaging;

import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SEAGridEventBus {
    private final static Logger logger = LoggerFactory.getLogger(SEAGridEventBus.class);

    private static SEAGridEventBus instance;

    private EventBus eventBus;

    private SEAGridEventBus(){
        this.eventBus = new EventBus();
    }

    public static SEAGridEventBus getInstance(){
        if(SEAGridEventBus.instance == null){
            SEAGridEventBus.instance = new SEAGridEventBus();
        }
        return SEAGridEventBus.instance;
    }

    public void register(Object subscriber){
        eventBus.register(subscriber);
    }

    public void unregister(Object subscriber){
        eventBus.unregister(subscriber);
    }

    public void post(SEAGridEvent event){
        eventBus.post(event);
    }
}