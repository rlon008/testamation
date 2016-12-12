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

package nz.co.testamation.core.mock;

import nz.co.testamation.core.mock.requestmatcher.RequestMatcher;
import nz.co.testamation.core.mock.response.MockResponse;
import nz.co.testamation.testcommon.template.MockitoTestTemplate;
import org.junit.Test;

import static org.mockito.internal.verification.VerificationModeFactory.times;

public class OnGoingExpectationImplTest {

    abstract class Template extends MockitoTestTemplate {
        RequestMatcher<HttpServletRequestWrapper> requestMatcher = mock( RequestMatcher.class );
        HttpExpectationHandler httpExpectationHandler = mock( HttpExpectationHandler.class );
        OnGoingExpectationImpl onGoingExpectation = new OnGoingExpectationImpl( httpExpectationHandler );

        MockResponse mockResponse = mock( MockResponse.class );

    }


    @Test
    public void thenResponseCorrectIfRequestMatcherSet() throws Exception {

        new Template() {

            @Override
            protected void given() throws Exception {
                onGoingExpectation.receives( requestMatcher );
            }

            @Override
            protected void when() throws Exception {
                onGoingExpectation.thenRespond( mockResponse );
            }

            @Override
            protected void then() throws Exception {
                assertThat( onGoingExpectation.getMockResponse(), equalTo( mockResponse ) );
                assertThat( onGoingExpectation.getRequestMatcher(), equalTo( requestMatcher ) );
                verify( httpExpectationHandler ).complete( onGoingExpectation );
            }

        }.run();

    }

    @Test
    public void ifTimesIsMoreThanOneThenAddAdditionalExpectation() throws Exception {

        new Template() {

            @Override
            protected void given() throws Exception {
                onGoingExpectation.receives( requestMatcher );
                onGoingExpectation.times( 3 );
            }

            @Override
            protected void when() throws Exception {
                onGoingExpectation.thenRespond( mockResponse );
            }

            @Override
            protected void then() throws Exception {
                assertThat( onGoingExpectation.getMockResponse(), equalTo( mockResponse ) );
                assertThat( onGoingExpectation.getRequestMatcher(), equalTo( requestMatcher ) );
                verify( httpExpectationHandler ).complete( onGoingExpectation );
                verify( httpExpectationHandler, times( 2 ) ).addExpectation( requestMatcher, mockResponse );
            }

        }.run();

    }


    @Test(expected = RuntimeException.class)
    public void thenResponseBlowUpIfRequestMatcherNotSet() throws Exception {

        new Template() {

            @Override
            protected void when() throws Exception {
                onGoingExpectation.thenRespond( mockResponse );
            }


        }.run();

    }


}