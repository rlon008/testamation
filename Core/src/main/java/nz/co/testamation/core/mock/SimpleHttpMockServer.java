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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple spring controller implementation binds to '/mock/**' in the current running container.
 * Because it is running under the same container, configurations such as filters, etc of the container
 * may affect the handling of the request.
 * <p>
 * For a HTTP mock server that is completely separate, consider using JettyHttpMockServer or TomcatHttpMockServer
 *
 * <p>
 * This class is intended to be used as a sample, it is recommended that you create your own mock server
 * controller with the appropriate request mapping.
 */
@Controller
public class SimpleHttpMockServer extends AbstractHttpMockServer {

    public SimpleHttpMockServer( String mockName ) {
        super( mockName );
    }

    @RequestMapping( "/mock/**" )
    public void handle( HttpServletRequest request, HttpServletResponse response ) throws Exception {
        doHandle( request, response );
    }


}
