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

package nz.co.testamation.core.reader;

import nz.co.testamation.common.util.StringConcat;
import org.apache.commons.lang3.StringUtils;

import static nz.co.testamation.common.util.StringUtil.wrapInSingleQuote;

public class WhereClauseBuilder {

    private final StringConcat concat = new StringConcat( " and " );

    public WhereClauseBuilder() {
    }

    public static WhereClauseBuilder where( String column, String value ) {
        return new WhereClauseBuilder().and( column, value );
    }

    public WhereClauseBuilder and( String column, String value ) {
        return and( column, value, "=" );
    }

    private WhereClauseBuilder and( String column, String value, String operator ) {
        concat.appendIf( StringUtils.isNotBlank( value ),
            String.format( "%s %s %s", column, operator, wrapInSingleQuote( value ) )
        );
        return this;
    }

    public WhereClauseBuilder andNotNull( String column ) {
        concat.appendIf( StringUtils.isNotBlank( column ), column + " is not null" );
        return this;
    }

    public WhereClauseBuilder andNull( String column ) {
        concat.appendIf( StringUtils.isNotBlank( column ), column + " is null" );
        return this;
    }

    public WhereClauseBuilder andNotEqual( String column, String value ) {
        return and( column, value, "!=" );
    }


    public String toString() {
        return StringUtils.isNotBlank( concat.toString() ) ? " where " + concat.toString() : "";
    }
}
