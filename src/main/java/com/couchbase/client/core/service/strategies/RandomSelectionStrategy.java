/**
 * Copyright (C) 2014 Couchbase, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALING
 * IN THE SOFTWARE.
 */
package com.couchbase.client.core.service.strategies;

import com.couchbase.client.core.endpoint.Endpoint;
import com.couchbase.client.core.message.CouchbaseRequest;
import com.couchbase.client.core.state.LifecycleState;

import java.util.Random;

/**
 * Selects the {@link Endpoint} based on a random selection of connected {@link Endpoint}s.
 *
 * @author Michael Nitschinger
 * @since 1.0
 */
public class RandomSelectionStrategy implements SelectionStrategy {

    /**
     * Random number generator, statically initialized and designed to be reused.
     */
    private static final Random RANDOM = new Random();

    /**
     * The number of times to try to find a suitable endpoint before returning without success.
     */
    private static final int MAX_TRIES = 100;

    @Override
    public Endpoint select(final CouchbaseRequest request, final Endpoint[] endpoints) {
        int numEndpoints = endpoints.length;
        if (numEndpoints == 0) {
            return null;
        }

        for (int i = 0; i < MAX_TRIES; i++) {
            int rand = RANDOM.nextInt(endpoints.length);
            Endpoint endpoint = endpoints[rand];
            if (endpoint.isState(LifecycleState.CONNECTED)) {
                return endpoint;
            }
        }

        return null;
    }
}
