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

package nz.co.testamation.core.reader.pdf;

import nz.co.testamation.core.client.BrowserDriver;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.openqa.selenium.Cookie;

import java.util.Set;

public class BrowserCookieHttpContextProvider implements HttpContextProvider {
    private final BrowserDriver browserDriver;

    public BrowserCookieHttpContextProvider( BrowserDriver browserDriver ) {
        this.browserDriver = browserDriver;
    }

    @Override
    public HttpContext get() {
        BasicHttpContext localContext = new BasicHttpContext();
        Set<Cookie> cookies = browserDriver.getAllCookies();
        BasicCookieStore cookieStore = new BasicCookieStore();

        for ( Cookie cookie : cookies ) {
            BasicClientCookie clientCookie = new BasicClientCookie( cookie.getName(), cookie.getValue() );
            clientCookie.setDomain( cookie.getDomain() );
            clientCookie.setPath( cookie.getPath() );
            clientCookie.setExpiryDate( cookie.getExpiry() );
            cookieStore.addCookie( clientCookie );
        }
        localContext.setAttribute( HttpClientContext.COOKIE_STORE, cookieStore );
        return localContext;
    }
}
