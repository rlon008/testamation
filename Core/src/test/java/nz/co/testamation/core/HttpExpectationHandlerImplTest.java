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

package nz.co.testamation.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import nz.co.testamation.common.util.ReflectionUtil;
import nz.co.testamation.core.exception.ExpectationNotConsumedException;
import nz.co.testamation.core.exception.OnGoingExpectationNotCompleteException;
import nz.co.testamation.core.mock.*;
import nz.co.testamation.core.mock.requestmatcher.RequestMatcher;
import nz.co.testamation.core.mock.response.HttpError500Response;
import nz.co.testamation.core.mock.response.MockResponse;
import nz.co.testamation.testcommon.fixture.SomeFixture;
import nz.co.testamation.testcommon.template.MockitoTestTemplate;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpExpectationHandlerImplTest {

    abstract class Template extends MockitoTestTemplate {

        RequestWrapperFactory requestWrapperFactory = mock( RequestWrapperFactory.class );
        HttpExpectationHandlerImpl httpExpectationHandler = new HttpExpectationHandlerImpl( SomeFixture.someString(), requestWrapperFactory );

        HttpServletRequest httpServletRequest = mock( HttpServletRequest.class );


        RequestMatcher<HttpServletRequestWrapper> requestMatcher1 = mock( RequestMatcher.class );
        RequestMatcher<HttpServletRequestWrapper> requestMatcher2 = mock( RequestMatcher.class );
        RequestMatcher<HttpServletRequestWrapper> requestMatcher3 = mock( RequestMatcher.class );
        MockResponse mockResponse1 = mock( MockResponse.class );
        MockResponse mockResponse2 = mock( MockResponse.class );
        MockResponse mockResponse3 = mock( MockResponse.class );

        HttpServletRequestWrapper requestWrapper = mock( HttpServletRequestWrapper.class );
        OnGoingExpectationImpl onGoingExpectation = mock( OnGoingExpectationImpl.class );

    }


    @Test
    public void ifRequestMatchesExpectationThenReturnMatchingResponse() throws Exception {

        new Template() {
            MockResponse actual;


            @Override
            protected void given() throws Exception {
                Map<RequestMatcher, MockResponse> expectations = new LinkedHashMap<>();
                expectations.put( requestMatcher1, mockResponse1 );
                expectations.put( requestMatcher2, mockResponse2 );
                expectations.put( requestMatcher3, mockResponse3 );

                setField( httpExpectationHandler, "expectations", expectations );

                given( requestWrapperFactory.create( httpServletRequest ) ).thenReturn( requestWrapper );
                given( requestMatcher1.matches( requestWrapper ) ).thenReturn( false );
                given( requestMatcher2.matches( requestWrapper ) ).thenReturn( true );
                given( requestMatcher3.matches( requestWrapper ) ).thenReturn( false );

            }

            @Override
            protected void when() throws Exception {
                actual = httpExpectationHandler.getMatchingResponse( httpServletRequest );
            }

            @Override
            protected void then() throws Exception {
                assertThat( actual, equalTo( mockResponse2 ) );
                assertField( httpExpectationHandler, "expectations", equalTo( ImmutableMap.of( requestMatcher1, mockResponse1, requestMatcher3, mockResponse3 ) ) );
            }

        }.run();

    }

    @Test
    public void ifNoExpecationMatchThenReturnHttp500ErrorResponse() throws Exception {

        new Template() {

            MockResponse actual;
            Map<RequestMatcher, MockResponse> expectations = new LinkedHashMap<>();


            @Override
            protected void given() throws Exception {
                expectations.put( requestMatcher1, mockResponse1 );
                setField( httpExpectationHandler, "expectations", expectations );

                given( requestWrapperFactory.create( httpServletRequest ) ).thenReturn( requestWrapper );
                given( requestMatcher1.matches( requestWrapper ) ).thenReturn( false );

            }

            @Override
            protected void when() throws Exception {
                actual = httpExpectationHandler.getMatchingResponse( httpServletRequest );
            }

            @Override
            protected void then() throws Exception {
                assertThat( actual, Matchers.isA( HttpError500Response.class ) );
                assertField( httpExpectationHandler, "expectations", equalTo( ImmutableMap.of( requestMatcher1, mockResponse1 ) ) );
            }

        }.run();

    }

    @Test
    public void resetHappyDay() throws Exception {

        new Template() {

            @Override
            protected void given() throws Exception {

                Map<RequestMatcher, MockResponse> expectations = Maps.newHashMap();
                expectations.put( requestMatcher1, mockResponse1 );

                setField( httpExpectationHandler, "expectations", expectations );
                setField( httpExpectationHandler, "onGoingExpectations", Lists.newArrayList( mock( OnGoingExpectation.class ) ) );
            }

            @Override
            protected void when() throws Exception {
                httpExpectationHandler.reset();
            }

            @Override
            protected void then() throws Exception {
                assertField( httpExpectationHandler, "expectations", equalTo( ImmutableMap.of() ) );
                assertField( httpExpectationHandler, "onGoingExpectations", Matchers.empty() );
            }

        }.run();

    }

    @Test( expected = ExpectationNotConsumedException.class )
    public void verifyThrowsExceptionIfExpectationsIsNotEmpty() throws Exception {

        new Template() {
            @Override
            protected void given() throws Exception {
                Map<RequestMatcher, MockResponse> expectations = new LinkedHashMap<>();
                expectations.put( requestMatcher1, mockResponse1 );

                ReflectionUtil.setField( httpExpectationHandler, "expectations", expectations );
            }

            @Override
            protected void when() throws Exception {
                httpExpectationHandler.verify();
            }

        }.run();

    }

    @Test
    public void verifyDoNothingIfNoExpectation() throws Exception {

        new Template() {

            @Override
            protected void when() throws Exception {
                httpExpectationHandler.verify();
            }

        }.run();

    }

    @Test
    public void createBuilderHappyDay() throws Exception {

        new Template() {
            OnGoingExpectationStart actual;

            @Override
            protected void given() throws Exception {
            }

            @Override
            protected void when() throws Exception {
                actual = httpExpectationHandler.createExpectation();
            }

            @Override
            protected void then() throws Exception {
                assertField( actual, "requestMatcher", Matchers.nullValue() );
                assertField( actual, "httpExpectationHandler", equalTo( httpExpectationHandler ) );
                assertField( httpExpectationHandler, "onGoingExpectations", equalTo( ImmutableList.of( actual ) ) );
            }

        }.run();

    }

    @Test
    public void canCompleteOnGoingExpectation() throws Exception {

        new Template() {

            @Override
            protected void given() throws Exception {
                setField( httpExpectationHandler, "onGoingExpectations", Lists.newArrayList( onGoingExpectation ) );
                given( onGoingExpectation.getRequestMatcher() ).thenReturn( requestMatcher1 );
                given( onGoingExpectation.getMockResponse() ).thenReturn( mockResponse1 );
            }

            @Override
            protected void when() throws Exception {
                httpExpectationHandler.complete( onGoingExpectation );
            }

            @Override
            protected void then() throws Exception {
                assertField( httpExpectationHandler, "onGoingExpectations", Matchers.empty() );
                assertField( httpExpectationHandler, "expectations", equalTo( ImmutableMap.of( requestMatcher1, mockResponse1 ) ) );
            }

        }.run();
    }

    @Test
    public void verifyOnGoingExpectationDoNothingIfNoOnGoingExpectation() throws Exception {

        new Template() {

            @Override
            protected void when() throws Exception {
                httpExpectationHandler.verifyOnGoingExpectation();
            }


        }.run();

    }


    @Test( expected = OnGoingExpectationNotCompleteException.class )
    public void verifyOnGoingExpectationThrowsExceptionIfOnGoingExpectationIsNotComplete() throws Exception {

        new Template() {

            @Override
            protected void given() throws Exception {
                setField( httpExpectationHandler, "onGoingExpectations", Lists.newArrayList( onGoingExpectation ) );

            }

            @Override
            protected void when() throws Exception {
                httpExpectationHandler.verifyOnGoingExpectation();
            }


        }.run();

    }


}