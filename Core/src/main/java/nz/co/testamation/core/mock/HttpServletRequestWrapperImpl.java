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

import nz.co.testamation.common.util.StringConcat;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class HttpServletRequestWrapperImpl implements HttpServletRequestWrapper {
    private final HttpServletRequest request;

    private final String body;

    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String[]> parameterMap;


    public HttpServletRequestWrapperImpl( HttpServletRequest request ) throws IOException {
        this.request = request;
        this.parameterMap = request.getParameterMap();

        Enumeration headerNames = request.getHeaderNames();
        while ( headerNames.hasMoreElements() ) {
            String key = (String) headerNames.nextElement();
            headers.put( key, request.getHeader( key ) );
        }

        this.body = FileCopyUtils.copyToString( new InputStreamReader( this.request.getInputStream() ) );

    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public String getMethod() {
        return request.getMethod();
    }

    @Override
    public String getRequestURI() {
        return request.getRequestURI();
    }

    @Override
    public String getHeader( String key ) {
        return request.getHeader( key );
    }

    @Override
    public String getContentType() {
        return request.getContentType();
    }

    @Override
    public String getParameter( String key ) {
        return request.getParameter( key );
    }

    @Override
    public String toString() {
        StringConcat concat = new StringConcat( "\n" );
        try {
            concat.append( request.getRequestURL().toString() );
        } catch ( Exception e ) {
            concat.append( "URL NOT AVAILABLE IN REQUEST" );
        }

        concat.append( "\nHeaders:" );
        for ( Map.Entry<String, String> entry : headers.entrySet() ) {
            concat.append( entry );
        }
        concat.append( "Parameters: {" );
        for ( Map.Entry<String, String[]> entry : parameterMap.entrySet() ) {
            concat.append( String.format( "%s: %s", entry.getKey(), getValue( entry.getValue() ) ) );
        }
        concat.append( "}" );
        concat.append( "Body: " + body );
        return concat.toString();
    }

    private String getValue( String[] values ) {
        StringConcat concat = new StringConcat( ", " );
        for ( String value : values ) {
            concat.append( value );
        }
        return concat.toString();
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }
}
