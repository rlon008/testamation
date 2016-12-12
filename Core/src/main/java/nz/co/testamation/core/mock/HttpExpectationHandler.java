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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface HttpExpectationHandler {

    MockResponse getMatchingResponse( HttpServletRequest request ) throws Exception;

    void addExpectation( RequestMatcher<HttpServletRequestWrapper> requestMatcher, MockResponse response );

    OnGoingExpectationStart<HttpServletRequestWrapper> createExpectation();

    void complete( OnGoingExpectation onGoingExpectation );

    void reset();

    void verify();

    void verifyOnGoingExpectation();

    List<HttpServletRequestWrapper> getReceivedRequests();
}
