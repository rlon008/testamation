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

public class BaseUrlProviderImpl implements BaseUrlProvider {
    private String baseUrl;

    public BaseUrlProviderImpl( String host, int port, String contextPath ) {
        this( getBaseUrl( host, port, contextPath ) );
    }

    public BaseUrlProviderImpl( String baseUrl ) {
        this.baseUrl = baseUrl;
    }

    private static String getBaseUrl( String host, int port, String contextPath ) {
        if ( !host.startsWith( "http://" ) ) {
            host = "http://" + host;
        }
        if ( !contextPath.startsWith( "/" ) ) {
            contextPath = "/" + contextPath;
        }
        return String.format( "%s:%s%s", host, port, contextPath );
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }
}
