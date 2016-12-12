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

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class StringConcatTest {
    String string1 = "string 1";
    String string2 = "string 2";
    String separator = ", ";

    @Test
    public void appendOneStringReturnsTheStringWithoutSeparator() throws Exception {
        assertThat(
            new StringConcat( separator ).append( string1 ).toString(),
            equalTo( string1 )
        );
    }

    @Test
    public void appendAddSeparatorCorrectly() throws Exception {
        assertThat(
            new StringConcat( separator ).append( string1 ).append( string2 ).toString(),
            equalTo( string1 + separator + string2 )
        );
    }

    @Test
    public void doNotAppendNull() throws Exception {
        assertThat(
            new StringConcat( separator ).append( null ).toString(),
            equalTo( "" )
        );
    }

    @Test
    public void doNotAppendBlankString() throws Exception {
        assertThat(
            new StringConcat( separator ).append( "hello" ).append( "" ).toString(),
            equalTo( "hello" )
        );
    }

    @Test
    public void appendIfAppendsIfConditionIsTrue() throws Exception {
        assertThat(
            new StringConcat( separator ).appendIf( true, string1 ).toString(),
            equalTo( string1 )
        );
    }

    @Test
    public void appendIfDoesNotAppendsIfConditionIsFalse() throws Exception {
        assertThat(
            new StringConcat( separator ).appendIf( false, string1 ).toString(),
            equalTo( "" )
        );
    }

}