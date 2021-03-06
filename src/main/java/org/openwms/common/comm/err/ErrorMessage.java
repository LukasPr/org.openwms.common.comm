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
package org.openwms.common.comm.err;

import org.openwms.common.comm.CommConstants;
import org.openwms.common.comm.ParserUtils;
import org.openwms.common.comm.Payload;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import static org.openwms.common.comm.ParserUtils.asDate;

/**
 * An ErrorMessage signals any error or failure situation from an external system and to external systems.
 * 
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
public class ErrorMessage extends Payload implements Serializable {

    /** Message identifier {@value} . */
    public static final String IDENTIFIER = "ERR_";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessageIdentifier() {
        return IDENTIFIER;
    }

    /**
     * A Builder.
     * 
     * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
     */
    public static class Builder {

        private final ErrorMessage message;

        /**
         * Create a new Builder.
         */
        public Builder() {
            this.message = new ErrorMessage();
        }

        /**
         * Add an error code to the message.
         * 
         * @param errorCode
         *            The error code
         * @return The builder
         */
        public Builder withErrorCode(String errorCode) {
            message.setErrorCode(errorCode);
            return this;
        }

        /**
         * Add the date of creation in an expected format as defined in {@link CommConstants#DATE_FORMAT_PATTERN}.
         *
         * @param createDate
         *            The creation date as String
         * @return The builder
         */
        public Builder withCreateDate(String createDate) throws ParseException {
            message.setCreated(asDate(createDate));
            return this;
        }

        /**
         * Add a new instance of Date to the Message.
         * 
         * @return The builder
         */
        public Builder withCreateDate() {
            message.setCreated(new Date());
            return this;
        }

        /**
         * Build and return the Message.
         * 
         * @return The Message
         */
        public ErrorMessage build() {
            return message;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWithoutReply() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asString() {
        return IDENTIFIER + getErrorCode() +
                ParserUtils.asString(super.getCreated());
    }
}
