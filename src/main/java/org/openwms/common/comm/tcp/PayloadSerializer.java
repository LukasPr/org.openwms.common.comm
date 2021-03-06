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
package org.openwms.common.comm.tcp;

import org.openwms.common.comm.CommConstants;
import org.openwms.common.comm.MessageProcessingException;
import org.openwms.common.comm.Payload;
import org.springframework.core.serializer.Serializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.openwms.common.comm.ParserUtils.padRight;

/**
 * A PayloadSerializer.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
@Component
public class PayloadSerializer<T extends Payload> implements Serializer<T> {

    private static final byte[] CRLF = "\r\n".getBytes();
    private final List<OSIPSerializer<T>> serializers;
    private Map<String, OSIPSerializer<T>> serializersMap;

    public PayloadSerializer(List<OSIPSerializer<T>> serializers) {
        this.serializers = serializers;
    }

    @PostConstruct
    void onPostConstruct() {
        serializersMap = serializers.stream().collect(Collectors.toMap(OSIPSerializer::getMessageIdentifier, p -> p));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(T obj, OutputStream outputStream) throws IOException {
        BufferedOutputStream os = new BufferedOutputStream(outputStream);

        OSIPSerializer<T> serializer = serializersMap.get(obj.getMessageIdentifier());
        if (serializer == null) {
            throw new MessageProcessingException(format("No serializer for message of type [%s] registered", obj.getMessageIdentifier()));
        }

        String res = serializer.serialize(obj);

        os.write(padRight(res, CommConstants.TELEGRAM_LENGTH, CommConstants.TELEGRAM_FILLER_CHARACTER).getBytes(Charset.defaultCharset()));
        os.write(CRLF);
        os.flush();
    }
}
