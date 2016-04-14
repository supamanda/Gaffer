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

package gaffer.tuple.tuplen.impl;

import gaffer.tuple.tuplen.Tuple4;

public class Value4<A, B, C, D> extends Value3<A, B, C> implements Tuple4<A, B, C, D> {
    public Value4() {
        super(4);
    }

    /**
     * Pass-through constructor for larger tuple sizes.
     * @param size Tuple size.
     */
    protected Value4(final int size) {
        super(size);
        if (size < 4) {
            throw new IllegalArgumentException("Invalid size");
        }
    }

    @Override
    public D get3() {
        return (D) get(3);
    }

    @Override
    public void put3(final D d) {
        put(3, d);
    }
}
