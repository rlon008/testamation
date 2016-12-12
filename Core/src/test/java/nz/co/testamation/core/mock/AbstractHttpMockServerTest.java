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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AbstractHttpMockServerTest {

    abstract class Template extends MockitoTestTemplate {
        MockResponseHandler mockResponseHandler = mock( MockResponseHandler.class );
        HttpExpectationHandler httpExpectationHandler = mock( HttpExpectationHandler.class );
        AbstractHttpMockServer httpMockServer = new AbstractHttpMockServer( httpExpectationHandler, mockResponseHandler ) {
        };

        RequestMatcher<HttpServletRequestWrapper> requestMatcher = mock( RequestMatcher.class );
        MockResponse mockResponse = mock( MockResponse.class );
    }

    @Test
    public void handleHappyDay() throws Exception {

        new Template() {
            HttpServletRequest request = mock( HttpServletRequest.class );
            HttpServletResponse response = mock( HttpServletResponse.class );

            @Override
            protected void given() throws Exception {
                given( httpExpectationHandler.getMatchingResponse( request ) ).thenReturn( mockResponse );
            }

            @Override
            protected void when() throws Exception {
                httpMockServer.doHandle( request, response );
            }

            @Override
            protected void then() throws Exception {
                verify( mockResponseHandler ).handle( response, mockResponse );
            }

        }.run();

    }


    @Test
    public void expectHappyDay() throws Exception {

        new Template() {

            @Override
            protected void when() throws Exception {
                httpMockServer.expect( requestMatcher, mockResponse );
            }

            @Override
            protected void then() throws Exception {
                verify( httpExpectationHandler ).addExpectation( requestMatcher, mockResponse );
            }

        }.run();

    }


    @Test
    public void receivesHappyDay() throws Exception {

        new Template() {
            OnGoingExpectationStart onGoingExpectationStart = mock( OnGoingExpectationStart.class );
            OnGoingExpectationStart actual;

            @Override
            protected void given() throws Exception {
                given( httpExpectationHandler.createExpectation() ).thenReturn( onGoingExpectationStart );
            }

            @Override
            protected void when() throws Exception {
                actual = httpMockServer.createExpectation();
            }

            @Override
            protected void then() throws Exception {
                assertThat( actual, equalTo( onGoingExpectationStart ) );

            }

        }.run();

    }


}