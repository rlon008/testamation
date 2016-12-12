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

import com.google.common.collect.Lists;
import nz.co.testamation.common.util.StringConcat;
import nz.co.testamation.core.exception.ExpectationNotConsumedException;
import nz.co.testamation.core.exception.OnGoingExpectationNotCompleteException;
import nz.co.testamation.core.mock.requestmatcher.RequestMatcher;
import nz.co.testamation.core.mock.response.HttpError500Response;
import nz.co.testamation.core.mock.response.MockResponse;
import nz.co.testamation.core.waiting.WaitForEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpExpectationHandlerImpl implements HttpExpectationHandler {

    private Logger log = LoggerFactory.getLogger( HttpExpectationHandlerImpl.class );

    private final Map<RequestMatcher, MockResponse> expectations = new ConcurrentHashMap<>();
    private final List<OnGoingExpectation> onGoingExpectations = Lists.newArrayList();
    private final List<HttpServletRequestWrapper> receivedRequests = Lists.newArrayList();
    private String mockServerName;
    private final RequestWrapperFactory requestWrapperFactory;

    public HttpExpectationHandlerImpl( String mockServerName, RequestWrapperFactory requestWrapperFactory ) {
        this.mockServerName = mockServerName;
        this.requestWrapperFactory = requestWrapperFactory;
    }

    public HttpExpectationHandlerImpl( String mockServerName ) {
        this( mockServerName, new RequestWrapperFactoryImpl() );
    }


    public void reset() {
        onGoingExpectations.clear();
        expectations.clear();
        receivedRequests.clear();
    }

    public void verify() {
        new WaitForEmpty<>()
            .when( expectations::entrySet )
            .run();

        if ( !expectations.isEmpty() ) {
            StringConcat stringConcat = new StringConcat( "\n" );
            stringConcat.append( "\n***************************************************************************\n" );
            stringConcat.append( mockServerName + " - Expectations not consumed, remaining:" );
            expectations.keySet().forEach( ( RequestMatcher r ) -> stringConcat.append( "\t" + r.toString() ) );
            stringConcat.append( "\n***************************************************************************\n" );
            log.error( stringConcat.toString() );

            throw new ExpectationNotConsumedException();
        }

    }

    public void verifyOnGoingExpectation() {
        if ( !onGoingExpectations.isEmpty() ) {
            log.error( "\nIncomplete http server mocking.  " +
                "Make sure you call thenRespond().\n " +
                " E.g. when(httpMockServer).receives(new HttpRequestMatcher()).thenRespond(new MockHttpResponse()); " );
            throw new OnGoingExpectationNotCompleteException();
        }
    }


    @Override
    public MockResponse getMatchingResponse( HttpServletRequest request ) throws Exception {
        HttpServletRequestWrapper requestWrapper = requestWrapperFactory.create( request );
        log.info( mockServerName + " received request: \n" + requestWrapper );
        receivedRequests.add( requestWrapper );

        for ( Map.Entry<RequestMatcher, MockResponse> entry : expectations.entrySet() ) {
            if ( entry.getKey().matches( requestWrapper ) ) {
                expectations.remove( entry.getKey() );
                MockResponse response = entry.getValue();
                log.info( mockServerName + " responded: \n" + response );
                return response;
            }
        }

        log.error( getRequestDescription( requestWrapper ) );
        return new HttpError500Response();
    }


    @Override
    public OnGoingExpectationStart<HttpServletRequestWrapper> createExpectation() {
        OnGoingExpectation onGoingExpectation = new OnGoingExpectationImpl<HttpServletRequestWrapper>( this );
        this.onGoingExpectations.add( onGoingExpectation );
        return onGoingExpectation;
    }

    @Override
    public void complete( OnGoingExpectation onGoingExpectation ) {
        this.onGoingExpectations.remove( onGoingExpectation );
        addExpectation( onGoingExpectation.getRequestMatcher(), onGoingExpectation.getMockResponse() );
    }


    @Override
    public void addExpectation( RequestMatcher requestMatcher, MockResponse response ) {
        expectations.put( requestMatcher, response );
    }

    private String getRequestDescription( HttpServletRequestWrapper request ) {
        StringConcat stringConcat = new StringConcat( "\n" );
        stringConcat.append( "\n***************************************************************************\n" );
        stringConcat.append( mockServerName + " received unexpected request: " );
        stringConcat.append( request.toString() );
        stringConcat.append( "---------->\nRemaining expectations:" );
        expectations.keySet().forEach( ( RequestMatcher r ) -> stringConcat.append( "\t" + r.toString() ) );
        stringConcat.append( "\n***************************************************************************\n" );

        return stringConcat.toString();
    }

    @Override
    public List<HttpServletRequestWrapper> getReceivedRequests() {
        return receivedRequests;
    }
}
