/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.openwms.common.comm.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.ip.tcp.connection.TcpConnection;
import org.springframework.integration.ip.tcp.connection.TcpMessageMapper;
import org.springframework.integration.support.MutableMessageHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.util.Assert;

import java.util.HashMap;

import static org.openwms.common.comm.CommConstants.CORE_INTEGRATION_MESSAGING;

/**
 * A CustomTcpMessageMapper.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
public class CustomTcpMessageMapper extends TcpMessageMapper {

    private final MessageConverter inboundMessageConverter;
    private final MessageConverter outboundMessageConverter;

    private static final Logger TELEGRAM_LOGGER = LoggerFactory.getLogger(CORE_INTEGRATION_MESSAGING);

    public CustomTcpMessageMapper(MessageConverter inboundMessageConverter, MessageConverter outboundMessageConverter) {
        Assert.notNull(inboundMessageConverter, "'inboundMessageConverter' must not be null");
        Assert.notNull(outboundMessageConverter, "'outboundMessageConverter' must not be null");
        this.inboundMessageConverter = inboundMessageConverter;
        this.outboundMessageConverter = outboundMessageConverter;
    }

    @Override
    public Message<?> toMessage(TcpConnection connection) throws Exception {
        Object data = connection.getPayload();
        TELEGRAM_LOGGER.trace("Incoming:" + data);
        if (data != null) {
            Message<?> message = this.inboundMessageConverter.toMessage(data, null);
            MutableMessageHeaders mmh = new MutableMessageHeaders(new HashMap<>());
            this.addStandardHeaders(connection, mmh);
            this.addCustomHeaders(connection, mmh);
            Message<?> msg = this.getMessageBuilderFactory().fromMessage(message).copyHeaders(mmh).build();
            return msg;
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("Null payload from connection " + connection.getConnectionId());
            }
            return null;
        }
    }

    @Override
    public Object fromMessage(Message<?> message) throws Exception {
        Object data = this.outboundMessageConverter.fromMessage(message, Object.class);
        TELEGRAM_LOGGER.trace("Outgoing:" + data);
        return data;
    }
}
