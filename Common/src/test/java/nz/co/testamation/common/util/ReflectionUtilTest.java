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


public class ReflectionUtilTest {

    class Sample {
        private final String fieldA;

        Sample( String fieldA ) {
            this.fieldA = fieldA;
        }
    }

    @Test
    public void getFieldValueReturnsCorrectFieldValue() throws Exception {
        String someString = "some string";
        assertThat( ReflectionUtil.getFieldValue( new Sample( someString ), "fieldA" ), equalTo( someString ) );

    }


    @Test( expected = RuntimeException.class )
    public void getFieldHandleException() throws Exception {
        ReflectionUtil.getFieldValue( new Object(), "someInvalidField" );

    }

    @Test
    public void setFieldWorksCorrectly() throws Exception {
        String newString = "some new string";
        Sample obj = new Sample( "some string" );
        ReflectionUtil.setField( obj, "fieldA", newString );
        assertThat( obj.fieldA, equalTo( newString ) );


    }


    @Test( expected = RuntimeException.class )
    public void setFieldHandleException() throws Exception {
        ReflectionUtil.setField( new Object(), "someInvalidField", "some value" );
    }

}