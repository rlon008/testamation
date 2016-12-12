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

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.co.testamation.core.mock.HttpServletRequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;

public class JsonRequestMatcher<T extends JsonRequestMatcher> extends AttributeRequestMatcher<T> {

    private static Logger logger = LoggerFactory.getLogger( JsonRequestMatcher.class );

    public JsonRequestMatcher() {
        withContentTypeThat( equalTo( MediaType.APPLICATION_JSON_VALUE ) );
    }

    @Override
    protected Map<String, Object> parseRequest( HttpServletRequestWrapper request ) {
        try {
            logger.info( "Received JSON content: " + request.getBody() );

            if ( StringUtils.isBlank( request.getBody() ) ) {
                return new HashMap<>();
            }

            return new ObjectMapper().readValue( request.getBody(), Map.class );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }
}
