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

public class StringUtilTest {

    @Test
    public void toNotNullStringReturnsEmptyStringIfNull() throws Exception {
        assertThat( StringUtil.toNotNullString( null ), equalTo( "" ) );
    }

    @Test
    public void toNotNullStringReturnsStringValueOfObjectIfNotNull() throws Exception {
        Object obj = new Object();
        assertThat( StringUtil.toNotNullString( obj ), equalTo( obj.toString() ) );
    }

    @Test
    public void truncateIfOverMaxLength() throws Exception {
        assertThat( StringUtil.truncate( "abc123456", 6 ), equalTo( "abc123" ) );
    }

    @Test
    public void truncateHandlesNull() throws Exception {
        assertThat( StringUtil.truncate( null, 6 ), equalTo( null ) );
    }

    @Test
    public void truncateDoNotTrucateIfNotOverMaxLength() throws Exception {
        assertThat( StringUtil.truncate( "xyz333", 6 ), equalTo( "xyz333" ) );
        assertThat( StringUtil.truncate( "abc2", 6 ), equalTo( "abc2" ) );
    }

    @Test
    public void equalsIgnoreCaseAndNullReturnsTrueForNullAndBlanks() throws Exception {
        assertThat( StringUtil.equalsIgnoreCaseAndNull( "", null ), equalTo( true ) );
        assertThat( StringUtil.equalsIgnoreCaseAndNull( null, null ), equalTo( true ) );
        assertThat( StringUtil.equalsIgnoreCaseAndNull( null, "" ), equalTo( true ) );
        assertThat( StringUtil.equalsIgnoreCaseAndNull( "", "" ), equalTo( true ) );
    }

    @Test
    public void equalsIgnoreCaseAndNullReturnsTrueForMatchingString() throws Exception {
        assertThat( StringUtil.equalsIgnoreCaseAndNull( "Saa", "sAa" ), equalTo( true ) );
        assertThat( StringUtil.equalsIgnoreCaseAndNull( "ss", "ss" ), equalTo( true ) );
    }

    @Test
    public void equalsIgnoreCaseAndNullReturnsFalseForNonMatchingString() throws Exception {
        assertThat( StringUtil.equalsIgnoreCaseAndNull( "xx", "x" ), equalTo( false ) );
        assertThat( StringUtil.equalsIgnoreCaseAndNull( "ss", "s s" ), equalTo( false ) );
    }

}