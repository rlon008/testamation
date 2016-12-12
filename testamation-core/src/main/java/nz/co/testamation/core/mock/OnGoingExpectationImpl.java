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

public class OnGoingExpectationImpl<T> implements OnGoingExpectation<T> {

    private RequestMatcher<T> requestMatcher;
    private HttpExpectationHandler httpExpectationHandler;
    private MockResponse response;
    private int times = 1;

    public OnGoingExpectationImpl( HttpExpectationHandler httpExpectationHandler ) {
        this.httpExpectationHandler = httpExpectationHandler;
    }

    @Override
    public OnGoingExpectation receives( RequestMatcher<T> requestMatcher ) {
        this.requestMatcher = requestMatcher;
        return this;
    }

    @Override
    public OnGoingExpectation times( int times ) {
        this.times = times;
        return this;
    }

    @Override
    public void thenRespond( MockResponse response ) {
        if ( requestMatcher == null ) {
            throw new RuntimeException( "Please call receives(RequestMatcher<HttpServletRequestWrapper>) first." );
        }
        this.response = response;
        httpExpectationHandler.complete( this  );

        for ( int i = 1; i < times; i++ ) {
            httpExpectationHandler.addExpectation( (RequestMatcher) requestMatcher, this.response );
        }
    }

    @Override
    public MockResponse getMockResponse() {
        return response;
    }

    @Override
    public RequestMatcher<T> getRequestMatcher() {
        return requestMatcher;
    }
}
