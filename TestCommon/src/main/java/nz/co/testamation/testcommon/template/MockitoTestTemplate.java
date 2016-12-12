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

package nz.co.testamation.testcommon.template;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;

import java.util.List;

public class MockitoTestTemplate {
    private List mocks = Lists.newArrayList();

    private InOrder inOrder;

    private Throwable whenException = null;


    public final void run() throws Exception {
        try {
            given();
        } catch ( Throwable e ) {
            throw new Error( "Unable to prepare test", e );
        }
        Throwable thenException = null;
        try {
            when();
        } catch ( Throwable e ) {
            whenException = e;
        }
        try {
            then();
        } catch ( Throwable e ) {
            thenException = e;
        }

        Mockito.validateMockitoUsage();
        handleExceptions( whenException, thenException );
    }

    public static void handleExceptions( Throwable whenException, Throwable thenException ) throws Exception {
        if ( ( whenException != null ) && ( thenException != null ) ) {
            whenException.printStackTrace();
            System.err.println();
            System.err.println( "when() threw exception above. then() threw exception below." );
            throwException( thenException );
        } else if ( whenException != null ) {
            throwException( whenException );
        } else if ( thenException != null ) {
            throwException( thenException );
        }
    }

    public static void throwException( Throwable e ) throws Exception {
        if ( e instanceof Error ) {
            throw (Error) e;
        } else if ( e instanceof RuntimeException ) {
            throw (RuntimeException) e;
        } else if ( e instanceof Exception ) {
            throw (Exception) e;
        } else {
            throw new UnsupportedOperationException( "Unhandled subclass of Throwable", e );
        }
    }

    protected void given() throws Exception {
    }

    protected void when() throws Exception {
    }

    protected void then() throws Exception {
    }

    protected Exception expectedException() {
        if ( whenException == null ) {
            throw new Error( "There was no exception in when()" );
        }
        return (Exception) whenException;
    }


    public <T> OngoingStubbing<T> given( T value ) {
        return Mockito.when( value );
    }

    public Stubber doAnswer( Answer answer ) {
        return Mockito.doAnswer( answer );
    }


    public <T> T mock( Class<T> toMock ) {
        T mock = Mockito.mock( toMock );
        mocks.add( mock );
        inOrder = Mockito.inOrder( Iterables.toArray( mocks, Object.class ) );
        return mock;
    }


    protected <E> E expectedException( Class<E> exceptionClass ) {
        return exceptionClass.cast( whenException );
    }

    public <T> T verifyInOrder( T mock ) {
        return inOrder.verify( mock );
    }


    public <T> T verifyNever( T mock ) {
        return Mockito.verify( mock, Mockito.never() );
    }

    public <T> T verify( T mock, VerificationMode mode ) {
        return Mockito.verify( mock, mode );
    }

    public <T> T verify( T mock ) {
        return Mockito.verify( mock );
    }

    public void verifyZeroInteractions( Object... mocks ) {
        Mockito.verifyZeroInteractions( mocks );
    }

    public void verifyNoMoreInteractions( Object... mocks ) {
        Mockito.verifyNoMoreInteractions( mocks );
    }

    public org.mockito.stubbing.Stubber doThrow( Throwable toBeThrown ) {
        return Mockito.doThrow( toBeThrown );
    }

    public <T> void assertThat( T actual, Matcher<? extends T> matcher ) {
        MatcherAssert.assertThat( actual, (Matcher<? super T>) matcher );
    }

    public void assertField( Object object, String fieldName, Matcher matcher ) {
        MatcherAssert.assertThat( FieldUtil.getFieldValue( object, fieldName ), matcher );
    }

    public static <T> Matcher<T> equalTo( T operand ) {
        return Matchers.equalTo( operand );
    }

    public void setField( Object obj, String fieldName, Object value ) {
        FieldUtil.setField( obj, fieldName, value );
    }

    public <T> T getField( Object obj, String fieldName, Class<T> clazz ) {
        return FieldUtil.getFieldValue( obj, fieldName, clazz );
    }


}
