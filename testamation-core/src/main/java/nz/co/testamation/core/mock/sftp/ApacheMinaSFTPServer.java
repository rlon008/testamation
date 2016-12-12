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

import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.PublickeyAuthenticator;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.sftp.subsystem.SftpSubsystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApacheMinaSFTPServer {

    public ApacheMinaSFTPServer( int port, File rootDir, PublickeyAuthenticator publickeyAuthenticator, PasswordAuthenticator passwordAuthenticator ) {
        try {
            SshServer sshd = SshServer.setUpDefaultServer();
            sshd.setPort( port );
            sshd.setKeyPairProvider( new SimpleGeneratorHostKeyProvider( "hostkey.ser" ) );
            sshd.setPublickeyAuthenticator( publickeyAuthenticator );
            sshd.setPasswordAuthenticator( passwordAuthenticator );
            sshd.setCommandFactory( new ScpCommandFactory() );

            List<NamedFactory<Command>> namedFactoryList = new ArrayList<>();
            namedFactoryList.add( new SftpSubsystem.Factory() );
            sshd.setSubsystemFactories( namedFactoryList );

            System.out.println( "SFTP virtual drive: " + rootDir.getCanonicalPath() );
            sshd.setFileSystemFactory( new VirtualFileSystemFactory( rootDir.getCanonicalPath() ) );
            sshd.start();
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }
}
