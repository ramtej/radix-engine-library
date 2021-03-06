/*
 * (C) Copyright 2020 Radix DLT Ltd
 *
 * Radix DLT Ltd licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the
 * License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.  See the License for the specific
 * language governing permissions and limitations under the License.
 */

package com.radixdlt.serialization.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

/**
 * Serializer for conversion from {@code String} data
 * to the appropriate JSON encoding.
 */
class JacksonJsonStringSerializer extends StdSerializer<String> {
	private static final long serialVersionUID = -2472482347700365657L;

	JacksonJsonStringSerializer() {
		this(null);
	}

	JacksonJsonStringSerializer(Class<String> t) {
		super(t);
	}

	@Override
	public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		jgen.writeString(JacksonCodecConstants.STR_STR_VALUE + value);
	}
}
