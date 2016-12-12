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

package nz.co.testamation.core.mock.response;

import nz.co.testamation.common.util.StringConcat;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public abstract class MockHttpResponse<T extends MockHttpResponse> implements MockResponse {

    private Map<String, String> headers = new HashMap<>();
    private int httpStatus = HttpServletResponse.SC_OK;
    private String contentType = "text/html";

    public T withHttpStatus( int httpStatus ) {
        this.httpStatus = httpStatus;
        return (T) this;
    }

    public T withContentType( String contentType ) {
        this.contentType = contentType;
        return (T) this;
    }

    public T withHeader( String key, String value ) {
        headers.put( key, value );
        return (T) this;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String toString() {
        StringConcat concat = new StringConcat( "\n" );
        concat.append( "Response: " + getClass().getSimpleName() );
        concat.append( "HTTP status: " + httpStatus );
        if ( !headers.isEmpty() ) {
            concat.append( "Headers: " );
            for ( Map.Entry<String, String> entry : headers.entrySet() ) {
                concat.append( entry.getKey() + "=" + entry.getValue() );

            }
            concat.append( "Headers End" );
        }
        concat.append( "Body: " + getResponseBody() );
        return concat.toString();
    }
}
