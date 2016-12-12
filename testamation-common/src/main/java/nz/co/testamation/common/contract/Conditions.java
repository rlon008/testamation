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

package nz.co.testamation.common.contract;

import com.google.common.collect.Iterables;
import org.apache.commons.lang3.StringUtils;

public class Conditions {
    public static void ensureTrue( boolean condition ) {
        ensureTrue( condition, "Expected condition to be true but was false" );
    }


    public static void ensureTrue( boolean condition, String message ) {
        if ( !condition ) {
            throw new IllegalStateException( message );
        }
    }

    public static <T> T notNull( T notNull, String message ) {
        if ( notNull == null ) {
            throw new IllegalStateException( message );
        }
        return notNull;
    }

    public static <T> T notNull( T notNull ) {
        return notNull( notNull, "Expected argument to not be null" );
    }

    public static void ensureNull( Object obj ) {
        ensureNull( obj, "Expected argument to be null" );
    }

    public static void ensureNull( Object obj, String message ) {
        if ( obj != null ) {
            throw new IllegalStateException( message );
        }
    }

    public static void ensureFalse( boolean condition ) {
        if ( condition ) {
            throw new IllegalStateException( "Expected condition to be false but was true" );
        }
    }

    public static <T> T notEmpty( T obj ) {
        if ( obj instanceof CharSequence && StringUtils.isBlank( (CharSequence) obj ) ) {
            throw new IllegalStateException( "Expected char sequence to not be empty" );
        }

        if ( obj instanceof Iterable && Iterables.isEmpty( (Iterable<?>) obj ) ) {
            throw new IllegalStateException( "Expected iterable to not be empty" );
        }

        return Conditions.notNull( obj );
    }

    public static void ensureAllNotNull( Object... objects ) {
        for ( Object object : objects ) {
            notNull( object );
        }
    }

    public static void ensureAllTrue( boolean... conditions ) {
        for ( boolean condition : conditions ) {
            ensureTrue( condition );
        }
    }
}
