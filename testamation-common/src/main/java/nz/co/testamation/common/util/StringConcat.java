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

public class StringConcat {

    private String separator;
    private StringBuilder stringBuilder = new StringBuilder();

    public StringConcat() {
        this( "" );
    }

    public StringConcat( String separator ) {
        this.separator = separator;
    }

    public StringConcat appendIf( boolean condition, Object obj ) {
        if ( condition ) {
            if ( stringBuilder.toString().length() > 0 ) {
                stringBuilder.append( separator );
            }
            stringBuilder.append( obj.toString() );
        }
        return this;

    }

    public String toString() {
        return stringBuilder.toString();
    }

    public String toNullString() {
        return stringBuilder.length() == 0 ? null : stringBuilder.toString();
    }

    public StringConcat append( Object obj ) {
        if ( obj == null ) {
            return this;
        }
        if ( obj instanceof String && StringUtils.isBlank( (String) obj ) ) {
            return this;
        }
        return appendIf( true, obj );
    }

    public static String concat( String separator, Object... values ) {
        if ( values == null ) {
            return "";
        }
        StringConcat concat = new StringConcat( separator );
        for ( Object value : values ) {
            concat.append( value );
        }
        return concat.toString();
    }
}
