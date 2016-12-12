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

package nz.co.testamation.core.mock.sftp;

import org.apache.sshd.server.session.ServerSession;

import java.util.HashMap;
import java.util.Map;

public class InMemoryPasswordAuthenticator implements MockPasswordAuthenticator {
    Map<String, String> credentials = new HashMap<>();

    @Override
    public boolean authenticate( String username, String password, ServerSession session ) {
        return credentials.containsKey( username ) && credentials.get( username ).equals( password );
    }

    @Override
    public void addCredentials( String username, String password ) {
        credentials.put( username, password );
    }
}
