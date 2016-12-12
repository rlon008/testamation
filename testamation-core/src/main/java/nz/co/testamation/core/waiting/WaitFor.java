/*
 * Copyright 2016 Ratha Long
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

package nz.co.testamation.core.waiting;

import com.google.common.base.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitFor<T> {
    private static final Logger logger = LoggerFactory.getLogger( WaitFor.class );

    private final Predicate<T> predicate;
    private Task<T> task;
    private int maxRetryCount = 10;
    private int delayBetweenRetryMillis = 500;


    public WaitFor( Predicate<T> predicate ) {
        this.predicate = predicate;
    }

    public WaitFor<T> when( Task<T> task ) {
        this.task = task;
        return this;
    }

    public T run() {
        int retryCount = 0;
        T result;
        do {
            result = task.execute();
            if ( predicate.apply( result ) ) {
                return result;
            }
            retryCount++;
            try {
                Thread.sleep( delayBetweenRetryMillis );
            } catch ( InterruptedException e ) {
                throw new RuntimeException( e );
            }
        } while ( retryCount < maxRetryCount );

        logger.warn( "Condition not match after max retry count reached." );
        return result;
    }

}
