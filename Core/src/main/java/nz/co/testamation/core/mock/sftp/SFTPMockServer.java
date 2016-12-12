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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;
import nz.co.testamation.core.lifecycle.annotation.Reset;
import nz.co.testamation.core.lifecycle.annotation.TestLifeCycle;
import org.apache.commons.io.FileUtils;
import org.apache.sshd.server.PublickeyAuthenticator;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@TestLifeCycle
public class SFTPMockServer {
    private final File rootDir;
    private final List<String> defaultDirectories = new ArrayList<>();
    private final MockPasswordAuthenticator passwordAuthenticator;
    private final ApacheMinaSFTPServer sftpServer;

    public SFTPMockServer( int port ) {
        this(
            port,
            Files.createTempDir(),
            new DummyPublicKeyAuthenticator(),
            new InMemoryPasswordAuthenticator()
        );
    }

    public SFTPMockServer(
        int port,
        File rootDir,
        PublickeyAuthenticator publickeyAuthenticator,
        MockPasswordAuthenticator mockPasswordAuthenticator
    ) {
        this.rootDir = rootDir;
        this.passwordAuthenticator = mockPasswordAuthenticator;
        this.sftpServer = new ApacheMinaSFTPServer( port, rootDir, publickeyAuthenticator, passwordAuthenticator );
    }

    @Reset
    public void reset() {
        try {
            FileUtils.cleanDirectory( rootDir );
            defaultDirectories.forEach( s -> createDirOrFile( s, true ) );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public void withExistingFile( String remoteFilePath, File file ) {
        try {
            Files.copy( file, createDirOrFile( remoteFilePath, false ) );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public void withExistingFile( String remoteFilePath, String content ) {
        try {
            StreamUtils.copy(
                new ByteArrayInputStream( content.getBytes( Charset.defaultCharset() ) ),
                new FileOutputStream( createDirOrFile( remoteFilePath, false ) )
            );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public SFTPMockServer withDefaultDirectory( String directoryStructure ) {
        this.defaultDirectories.add( directoryStructure );
        return this;
    }

    public SFTPMockServer withNewDirectory( String directoryStructure ) {
        createDirOrFile( directoryStructure, true );
        return this;
    }

    private File createDirOrFile( String filePath, boolean isDirectory ) {
        try {
            File file = new File( rootDir, filePath );
            Files.createParentDirs( file );
            boolean result = isDirectory ? file.mkdir() : file.createNewFile();
            if ( !result ) {
                throw new RuntimeException( "Cannot create directory or file: " + filePath );
            }
            return file;
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public SFTPMockServer withCredentials( String username, String password ) {
        passwordAuthenticator.addCredentials( username, password );
        return this;
    }

    public Path getFile( String path ) {
        try {
            List<Path> paths = java.nio.file.Files.walk( Paths.get( rootDir.getCanonicalPath(), path ) ).collect( Collectors.toList() );
            return Iterables.getOnlyElement( paths );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public List<Path> getFiles( String path ) {
        try {
            return java.nio.file.Files.walk( Paths.get( rootDir.getCanonicalPath(), path ) ).collect( Collectors.toList() );
        } catch ( IOException e ) {
            return ImmutableList.of();
        }
    }

}
