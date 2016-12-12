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

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ConditionsTest {


    @Test( expected = IllegalStateException.class )
    public void notEmptyBlowUpIfNull() throws Exception {
        Conditions.notEmpty( null );
    }


    @Test
    public void notEmptyBlowUpIfEmptyList() throws Exception {
        try {
            Conditions.notEmpty( ImmutableList.of() );

        } catch ( IllegalStateException e ) {
            assertThat( e.getMessage(), equalTo( "Expected iterable to not be empty" ) );

        }

    }


    @Test
    public void notEmptyBlowUpIfEmptyString() throws Exception {
        try {
            Conditions.notEmpty( "" );

        } catch ( IllegalStateException e ) {
            assertThat( e.getMessage(), equalTo( "Expected char sequence to not be empty" ) );

        }

    }


    @Test
    public void notEmptyHappyDay() throws Exception {
        Object obj = new Object();
        assertThat( Conditions.notEmpty( obj ), equalTo( obj ) );


        String str = "Some string";
        assertThat( Conditions.notEmpty( str ), equalTo( str ) );


        List list = ImmutableList.of( "" );
        assertThat( Conditions.notEmpty( list ), equalTo( list ) );
    }


}