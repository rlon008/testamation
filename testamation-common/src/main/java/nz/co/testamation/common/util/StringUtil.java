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

package nz.co.testamation.common.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

    public static String toNotNullString( Object object ) {
        if ( object == null ) {
            return "";
        }
        return String.valueOf( object );
    }

    public static String wrapIn( String value, String wrapper ) {
        return String.format( "%s%s%s", wrapper, value, wrapper );
    }

    public static String truncateAndRightPad( String value, int size ) {
        return StringUtils.rightPad( StringUtil.truncate( value, size ), size );
    }

    public static String truncate( String value, int maxLength ) {
        if ( maxLength < 0 ) {
            throw new IllegalArgumentException( "Expected maxLength to be > 0, but was: " + maxLength );
        }
        if ( value == null || value.length() <= maxLength ) {
            return value;
        }
        return value.substring( 0, maxLength );
    }

    public static String wrapInSingleQuote( String value ) {
        return wrapIn( value, "'" );
    }

    public static boolean allHaveContent( String... values ) {
        for ( String value : values ) {
            if ( StringUtils.isBlank( value ) ) {
                return false;
            }
        }
        return true;
    }

    public static boolean equalsIgnoreCaseAndNull( String str1, String str2 ) {
        if ( StringUtils.isBlank( str1 ) && StringUtils.isBlank( str2 ) ) {
            return true;
        }
        return StringUtils.equalsIgnoreCase( str1, str2 );
    }
}
