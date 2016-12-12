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

import nz.co.testamation.core.mock.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;

public class FormPostRequestMatcher<T extends FormPostRequestMatcher> extends AttributeRequestMatcher<T> {

    private static Logger logger = LoggerFactory.getLogger( FormPostRequestMatcher.class );

    public FormPostRequestMatcher() {
        withContentTypeThat( equalTo( MediaType.APPLICATION_FORM_URLENCODED_VALUE ) );
    }

    @Override
    protected Map<String, Object> parseRequest( HttpServletRequestWrapper request ) {
        return (Map) request.getParameterMap();
    }
}
