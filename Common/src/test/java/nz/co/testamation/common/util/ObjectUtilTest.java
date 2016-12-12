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

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ObjectUtilTest {

    @Test
    public void allNotEmptyReturnsTrueIfEmpty() throws Exception {
        assertThat( ObjectUtil.allNotEmpty(), equalTo( true ) );
    }

    @Test
    public void allNotEmptyReturnsTrueIfAllElementAreNotNull() throws Exception {
        assertThat( ObjectUtil.allNotEmpty( new Object(), "s", "s" ), equalTo( true ) );
    }

    @Test
    public void allNotEmptyReturnsFalseIfAnyElementIsNull() throws Exception {
        assertThat( ObjectUtil.allNotEmpty( new Object(), null, "s" ), equalTo( false ) );
    }

    @Test
    public void allNotEmptyReturnsFalseIfAnyElementIsEmptyString() throws Exception {
        assertThat( ObjectUtil.allNotEmpty( new Object(), "", "s" ), equalTo( false ) );
    }

    @Test
    public void allNotEmptyReturnsFalseIfAnyElementIsBlankString() throws Exception {
        assertThat( ObjectUtil.allNotEmpty( new Object(), "   ", "s" ), equalTo( false ) );
    }

    @Test
    public void allNotEmptyReturnsFalseIfAnyElementIsEmptyIterable() throws Exception {
        assertThat( ObjectUtil.allNotEmpty( new Object(), ImmutableList.of(), "s" ), equalTo( false ) );
    }

}