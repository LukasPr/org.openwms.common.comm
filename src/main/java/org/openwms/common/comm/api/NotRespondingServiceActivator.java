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
package org.openwms.common.comm.api;

import org.openwms.common.comm.Payload;
import org.springframework.messaging.support.GenericMessage;

/**
 * A NotRespondingServiceActivator.
 *
 * @param <T> A type of incoming Payload
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
public interface NotRespondingServiceActivator<T extends Payload> extends CustomServiceActivator {

    /**
     * Wake up a service, processor or bean an that accepts incoming messages of type <tt>T</tt>.
     *
     * @param message The message to forward
     */
    void wakeUp(GenericMessage<T> message);
}
