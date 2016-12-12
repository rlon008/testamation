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

import nz.co.testamation.core.lifecycle.annotation.*;
import nz.co.testamation.core.mock.requestmatcher.RequestMatcher;
import nz.co.testamation.core.mock.response.MockResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@TestLifeCycle
public abstract class AbstractHttpMockServer implements HttpMockServer {

    private final HttpExpectationHandler httpExpectationHandler;
    private final MockResponseHandler mockResponseHandler;

    public AbstractHttpMockServer( HttpExpectationHandler httpExpectationHandler, MockResponseHandler mockResponseHandler ) {
        this.httpExpectationHandler = httpExpectationHandler;
        this.mockResponseHandler = mockResponseHandler;
    }

    public AbstractHttpMockServer( String mockName ) {
        this( new HttpExpectationHandlerImpl( mockName ), new MockHttpResponseHandler() );
    }

    protected void doHandle( HttpServletRequest request, HttpServletResponse response ) throws Exception {
        mockResponseHandler.handle( response, httpExpectationHandler.getMatchingResponse( request ) );
    }

    @Override
    public void expect( RequestMatcher<HttpServletRequestWrapper> requestMatcher, MockResponse response ) {
        httpExpectationHandler.addExpectation( requestMatcher, response );
    }

    @Override
    public OnGoingExpectationStart<HttpServletRequestWrapper> createExpectation() {
        return httpExpectationHandler.createExpectation();
    }

    @Reset
    public void reset() {
        httpExpectationHandler.reset();
    }

    @AfterStep
    @AfterWhen
    public void verify() {
        httpExpectationHandler.verify();
    }


    @AfterExternalBehaviour
    public void verifyOnGoingExpectation() {
        httpExpectationHandler.verifyOnGoingExpectation();
    }

    @Override
    public List<HttpServletRequestWrapper> getReceivedRequests() {
        return httpExpectationHandler.getReceivedRequests();
    }

}
