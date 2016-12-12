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

import nz.co.testamation.testcommon.fixture.SomeFixture;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public class BaseUrlProviderImplTest {


    @Test
    public void baseUrlCreatedCorrectly() throws Exception {

        String host = SomeFixture.someString();
        String contextPath = SomeFixture.someString();
        int port = SomeFixture.someInt();
        BaseUrlProviderImpl baseUrlProvider = new BaseUrlProviderImpl( host, port, contextPath );
        MatcherAssert.assertThat( baseUrlProvider.getBaseUrl(), Matchers.equalTo( "http://" + host + ":" + port + "/" + contextPath ) );

    }

    @Test
    public void baseUrlCreatedIfHostStartsWithHttp() throws Exception {

        String host = "http://" + SomeFixture.someString();
        String contextPath = SomeFixture.someString();
        int port = SomeFixture.someInt();
        BaseUrlProviderImpl baseUrlProvider = new BaseUrlProviderImpl( host, port, contextPath );

        MatcherAssert.assertThat( baseUrlProvider.getBaseUrl(), Matchers.equalTo( host + ":" + port + "/" + contextPath ) );

    }

    @Test
    public void baseUrlCreatedIfContextPathStartsWithSlash() throws Exception {

        String host = "http://" + SomeFixture.someString();
        String contextPath = "/" + SomeFixture.someString();
        int port = SomeFixture.someInt();
        BaseUrlProviderImpl baseUrlProvider = new BaseUrlProviderImpl( host, port, contextPath );

        MatcherAssert.assertThat( baseUrlProvider.getBaseUrl(), Matchers.equalTo( host + ":" + port + contextPath ) );

    }


}