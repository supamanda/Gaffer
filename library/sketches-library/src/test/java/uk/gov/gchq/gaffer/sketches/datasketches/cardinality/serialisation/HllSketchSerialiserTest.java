/*
 * Copyright 2017-2019 Crown Copyright
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
package uk.gov.gchq.gaffer.sketches.datasketches.cardinality.serialisation;

import com.yahoo.sketches.hll.HllSketch;
import org.junit.Test;

import uk.gov.gchq.gaffer.exception.SerialisationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class HllSketchSerialiserTest {
    private static final HllSketchSerialiser SERIALISER = new HllSketchSerialiser();
    private static final double DELTA = 0.0000001D;

    @Test
    public void testSerialiseAndDeserialise() {
        final HllSketch sketch = new HllSketch(15);
        sketch.update("A");
        sketch.update("B");
        sketch.update("C");
        testSerialiser(sketch);

        final HllSketch emptySketch = new HllSketch(15);
        testSerialiser(emptySketch);
    }

    private void testSerialiser(final HllSketch sketch) {
        final double cardinality = sketch.getEstimate();
        final byte[] sketchSerialised;
        try {
            sketchSerialised = SERIALISER.serialise(sketch);
        } catch (final SerialisationException exception) {
            fail("A SerialisationException occurred");
            return;
        }

        final HllSketch sketchDeserialised;
        try {
            sketchDeserialised = SERIALISER.deserialise(sketchSerialised);
        } catch (final SerialisationException exception) {
            fail("A SerialisationException occurred");
            return;
        }
        assertEquals(cardinality, sketchDeserialised.getEstimate(), DELTA);
    }

    @Test
    public void testCanHandleHllSketch() {
        assertTrue(SERIALISER.canHandle(HllSketch.class));
        assertFalse(SERIALISER.canHandle(String.class));
    }
}
