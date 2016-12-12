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

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerWrapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * An Http mock server that starts a new jetty instance listening on the specified port.
 * Because the mock server is its own container, it is a cleaner separation from the production container.
 */
public class JettyHttpMockServer extends AbstractHttpMockServer {

    public JettyHttpMockServer( String mockName, int port ) throws Exception {
        super( mockName );
        init( port );
    }

    private void init( int port ) throws Exception {
        Server server = new Server( port );
        server.setHandler( new HandlerWrapper() {
            public void handle( String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException {
                try {
                    doHandle( request, response );
                    baseRequest.setHandled( true );
                } catch ( Exception e ) {
                    throw new ServletException( e );
                }
            }
        } );
        server.start();
    }

}
