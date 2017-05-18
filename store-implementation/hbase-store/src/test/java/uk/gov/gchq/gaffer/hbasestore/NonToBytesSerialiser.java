/*
 * Copyright 2016 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.gchq.gaffer.hbasestore;

import uk.gov.gchq.gaffer.commonutil.CommonConstants;
import uk.gov.gchq.gaffer.exception.SerialisationException;
import uk.gov.gchq.gaffer.serialisation.Serialiser;
import java.io.UnsupportedEncodingException;

public class NonToBytesSerialiser implements Serialiser<Integer, byte[]> {


    @Override
    public byte[] serialiseNull() {
        return new byte[0];
    }

    @Override
    public boolean canHandle(final Class clazz) {
        return Integer.class.equals(clazz);
    }

    @Override
    public byte[] serialise(final Integer value) throws SerialisationException {
        try {
            return value.toString().getBytes(CommonConstants.ISO_8859_1_ENCODING);
        } catch (final UnsupportedEncodingException e) {
            throw new SerialisationException(e.getMessage(), e);
        }
    }

    @Override
    public Integer deserialise(final byte[] bytes) throws SerialisationException {
        try {
            return Integer.parseInt(new String(bytes, CommonConstants.ISO_8859_1_ENCODING));
        } catch (final NumberFormatException | UnsupportedEncodingException e) {
            throw new SerialisationException(e.getMessage(), e);
        }
    }

    @Override
    public Integer deserialiseEmpty() throws SerialisationException {
        return null;
    }

    @Override
    public boolean preservesObjectOrdering() {
        return true;
    }
}