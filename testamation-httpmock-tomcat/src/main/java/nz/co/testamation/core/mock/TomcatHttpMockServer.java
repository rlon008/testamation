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

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * An Http mock server that starts a new Tomcat instance listening on the specified port.
 * Because the mock server is its own container, it is a cleaner separation from the production container.
 */
public class TomcatHttpMockServer extends AbstractHttpMockServer {

    public TomcatHttpMockServer( String mockName, int port ) {
        super( mockName );
        try {
            init( port, mockName );
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    private void init( int port, String mockName ) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort( port );
        Context context = tomcat.addContext( "", System.getProperty( "java.io.tmpdir" ) );
        tomcat.addServlet( "", mockName, new HttpServlet() {
            @Override
            protected void service( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
                try {
                    doHandle( req, resp );
                } catch ( Exception e ) {
                    throw new ServletException( e );
                }
            }
        } );
        context.addServletMapping( "/*", mockName );
        tomcat.start();
    }

}
