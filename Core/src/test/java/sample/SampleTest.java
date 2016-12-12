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

package sample;

import nz.co.testamation.core.IntegrationTestRunner;
import nz.co.testamation.core.TestTemplate;
import nz.co.testamation.core.mock.HttpMockServer;
import nz.co.testamation.core.mock.smtp.SmtpMockServer;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;

import static nz.co.testamation.core.mock.requestmatcher.HttpRequestMatcher.httpRequest;
import static nz.co.testamation.core.mock.response.HttpError500Response.response500;
import static org.hamcrest.Matchers.*;

/**
 * Sample test intended to be run in an IOC container
 *
 * @author Ratha Long, Paul Jacobs
 */
@Ignore
public class SampleTest {

    @Autowired
    IntegrationTestRunner runner;

    @Autowired
    LandingPage landingPage;

    @Autowired
    HttpMockServer googleMockServer;

    @Autowired
    SmtpMockServer smtpMockServer;

    @Test
    public void sampleTest() throws Exception {
        runner.run( new TestTemplate() {

            @Override
            public void externalBehaviours() throws Exception {
                when( googleMockServer )
                    .receives( httpRequest().withUriThat( endsWith( "/someservice/version/1.0" ) ) )
                    .thenRespond( response500() );
            }

            @Override
            public void when() throws Exception {
                landingPage.visit();
            }

            @Override
            public void then() throws Exception {
                assertThat( pageElementText( By.id( "heading" ) ), equalTo( "Hello World!" ) );
                assertThat( smtpMockServer.getReceivedEmail().getBody(), containsString( "Hello World!" ) );
            }
        } );

    }
}
