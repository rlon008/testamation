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

import nz.co.testamation.core.mock.response.MockResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class MockHttpResponseHandler implements MockResponseHandler {

    @Override
    public void handle( HttpServletResponse response, MockResponse mockResponse ) {
        try {
            if ( !mockResponse.getHeaders().isEmpty() ) {
                for ( Map.Entry<String, String> entry : mockResponse.getHeaders().entrySet() ) {
                    response.addHeader( entry.getKey(), entry.getValue() );
                }
            }
            response.setStatus( mockResponse.getHttpStatus() );
            response.setContentType( mockResponse.getContentType() );
            response.getWriter().write( mockResponse.getResponseBody() );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

}
