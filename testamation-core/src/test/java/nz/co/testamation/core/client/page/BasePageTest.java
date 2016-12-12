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

package nz.co.testamation.core.client.page;

import nz.co.testamation.core.client.BrowserDriver;
import nz.co.testamation.testcommon.fixture.SomeFixture;
import nz.co.testamation.testcommon.template.MockitoTestTemplate;
import org.junit.Test;

public class BasePageTest {

    abstract class Template extends MockitoTestTemplate {
        BaseUrlProvider baseUrlProvider = mock( BaseUrlProvider.class );
        BrowserDriver browserDriver = mock( BrowserDriver.class );


    }

    @Test
    public void visitWithNoParam() throws Exception {

        new Template() {
            String uri = SomeFixture.someString();
            BasePage basePage = new BasePage( uri );
            String baseUrl = SomeFixture.someString();

            @Override
            protected void given() throws Exception {
                setField( basePage, "baseUrlProvider", baseUrlProvider );
                setField( basePage, "browserDriver", browserDriver );

                given( baseUrlProvider.getBaseUrl() ).thenReturn( baseUrl );
            }

            @Override
            protected void when() throws Exception {
                basePage.visit();
            }

            @Override
            protected void then() throws Exception {
                verify( browserDriver ).open(baseUrl + uri);
            }

        }.run();

    }


}