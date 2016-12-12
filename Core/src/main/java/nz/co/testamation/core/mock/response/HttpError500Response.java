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

import javax.servlet.http.HttpServletResponse;

public class HttpError500Response extends MockHttpResponse<HttpError500Response> {

    public HttpError500Response() {
        withHttpStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
    }

    @Override
    public String getResponseBody() {
        return "";
    }

    public static HttpError500Response response500() {
        return new HttpError500Response();
    }
}
