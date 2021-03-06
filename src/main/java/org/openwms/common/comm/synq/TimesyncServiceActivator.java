/*
 * Copyright 2018 Heiko Scherrer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.common.comm.synq;

import org.ameba.annotation.Measured;
import org.openwms.common.comm.CommConstants;
import org.openwms.common.comm.api.RespondingServiceActivator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

import java.util.function.Function;

/**
 * A TimesyncServiceActivator is activated on SYNC telegrams and delegates
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
@MessageEndpoint("timesyncServiceActivator")
class TimesyncServiceActivator implements RespondingServiceActivator<TimesyncRequest, TimesyncResponse> {

    /** The name of the MessageChannel used as input-channel of this message processor. */
    static final String INPUT_CHANNEL_NAME = TimesyncRequest.IDENTIFIER + CommConstants.CHANNEL_SUFFIX;

    private final Function<GenericMessage<TimesyncRequest>, Message<TimesyncResponse>> handler;
    private final ApplicationContext ctx;

    @Autowired
    public TimesyncServiceActivator(Function<GenericMessage<TimesyncRequest>, Message<TimesyncResponse>> handler, ApplicationContext ctx) {
        this.handler = handler;
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageChannel getChannel() {
        return ctx.getBean(INPUT_CHANNEL_NAME, MessageChannel.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getChannelName() {
        return INPUT_CHANNEL_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    @ServiceActivator(inputChannel = INPUT_CHANNEL_NAME, outputChannel = "enrichedOutboundChannel")
    public Message<TimesyncResponse> wakeUp(GenericMessage<TimesyncRequest> message) {
        return handler.apply(message);
    }
}
