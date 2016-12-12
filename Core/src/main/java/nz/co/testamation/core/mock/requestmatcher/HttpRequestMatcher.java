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

package nz.co.testamation.core.mock.requestmatcher;

import com.google.common.collect.ImmutableMap;
import nz.co.testamation.common.util.StringConcat;
import nz.co.testamation.core.mock.HttpServletRequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestMatcher<T extends HttpRequestMatcher> implements RequestMatcher<HttpServletRequestWrapper> {

    private static final Logger logger = LoggerFactory.getLogger( HttpRequestMatcher.class );
    private final Map<String, Matcher<String>> expectedHeaders = new HashMap<>();
    private final Map<String, Matcher<String>> expectedParams = new HashMap<>();
    private Matcher<String> uriMatcher;
    private Matcher<String> contentTypeMatcher;
    private Matcher<HttpMethod> httpMethodMatcher;
    private ImmutableMap<String, String[]> actualParams;
    private final List<String> excludeHeaders = new ArrayList<>();
    private String actualBody;


    @Override
    public boolean matches( HttpServletRequestWrapper request ) throws Exception {
        actualParams = ImmutableMap.copyOf( request.getParameterMap() );
        actualBody = request.getBody();

        if ( httpMethodMatcher != null && !httpMethodMatcher.matches( HttpMethod.valueOf( request.getMethod() ) ) ) {
            return false;
        }

        if ( uriMatcher != null && !uriMatcher.matches( request.getRequestURI() ) ) {
            return false;
        }

        if ( contentTypeMatcher != null && !contentTypeMatcher.matches( request.getContentType() ) ) {
            return false;
        }

        for ( Map.Entry<String, Matcher<String>> entry : expectedHeaders.entrySet() ) {
            String headerValue = request.getHeader( entry.getKey() );
            if ( StringUtils.isBlank( headerValue ) ) {
                logger.error( "No header '" + entry.getKey() + "' in request as expected for " + getClass().getSimpleName() );
                return false;
            } else {
                if ( ( entry.getValue() != null ) && !entry.getValue().matches( headerValue ) ) {
                    return false;
                }
            }
        }

        for ( String excludeHeader : excludeHeaders ) {
            if ( request.getHeader( excludeHeader ) != null ) {
                logger.error( "Expected not to have header'" + excludeHeader + "' but header value was: " + request.getHeader( excludeHeader ) );
                return false;
            }
        }

        for ( Map.Entry<String, Matcher<String>> entry : expectedParams.entrySet() ) {
            String paramValue = request.getParameter( entry.getKey() );
            if ( StringUtils.isBlank( paramValue ) ) {
                return false;
            } else {
                if ( ( entry.getValue() != null ) && !entry.getValue().matches( paramValue ) ) {
                    return false;
                }
            }
        }

        return true;
    }


    public T withNoHeader( String excludeHeader ) {
        this.excludeHeaders.add( excludeHeader );
        return (T) this;
    }


    public T withMethodThat( Matcher<HttpMethod> httpMethodMatcher ) {
        this.httpMethodMatcher = httpMethodMatcher;
        return (T) this;
    }

    public T withUriThat( Matcher<String> matcher ) {
        uriMatcher = matcher;
        return (T) this;
    }

    public T withContentTypeThat( Matcher<String> matcher ) {
        contentTypeMatcher = matcher;
        return (T) this;
    }


    public T withHeader( String headerName, Matcher<String> matcher ) {
        if ( expectedHeaders.get( headerName ) != null ) {
            throw new IllegalArgumentException( "There is already an expectation for header " + expectedHeaders + " ->" + headerName );
        }
        expectedHeaders.put( headerName, matcher );
        return (T) this;
    }

    public T withParam( String paramName, Matcher<String> matcher ) {
        if ( expectedParams.get( paramName ) != null ) {
            throw new IllegalArgumentException( "There is already an expectation for param " + expectedParams + " ->" + paramName );
        }
        expectedParams.put( paramName, matcher );
        return (T) this;
    }

    @Override
    public String toString() {
        StringConcat concat = new StringConcat();
        concat.append( this.getClass().getSimpleName() + ": " );
        concat.appendIf( uriMatcher != null, "uriMatcher : " + uriMatcher );

        if ( !expectedParams.isEmpty() ) {
            concat.append( "\n and have params: " + expectedParams );
        }

        if ( !expectedHeaders.isEmpty() ) {
            concat.append( "\n and have headers: " + expectedHeaders );
        }

        if ( !excludeHeaders.isEmpty() ) {
            concat.append( "\n and does not have headers: " + excludeHeaders );
        }

        return concat.toString();
    }

    public static HttpRequestMatcher<HttpRequestMatcher> httpRequest() {
        return new HttpRequestMatcher<>();
    }

    public ImmutableMap<String, String[]> getActualParams() {
        return actualParams;
    }

    public String getActualBody() {
        return actualBody;
    }
}
