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

package nz.co.testamation.core.mock.smtp;

import com.dumbster.smtp.SmtpMessage;
import com.google.common.collect.ImmutableSet;
import nz.co.testamation.common.util.StringConcat;

import java.util.Set;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SmtpMessageWrapper {

    public enum MessageHeaders {
        TO( "To" ),
        CC( "Cc" ),
        BCC( "Bcc" ),
        FROM( "From" ),
        REPLY_TO( "Reply-To" ),
        SUBJECT( "Subject" );

        private final String key;

        MessageHeaders( String key ) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

    }

    private static final Pattern ATTACHMENT_PATTERN = Pattern.compile( "Content-Type:\\s*([^;]*);[^\\n]*Content-Disposition:\\s*attachment;\\s*filename=([^\\n]*)\\n(.+?)------=", Pattern.MULTILINE | Pattern.DOTALL );

    private static final int FILE_NAME_GROUP = 2;
    private static final int DATA_GROUP = 3;
    private static final int CONTENT_TYPE_GROUP = 1;

    private final SmtpMessage message;

    public SmtpMessageWrapper( SmtpMessage message ) {
        this.message = message;
    }

    public String getFrom() {
        return message.getHeaderValue( MessageHeaders.FROM.getKey() );
    }

    public String getReplyTo() {
        return message.getHeaderValue( MessageHeaders.REPLY_TO.getKey() );
    }

    public ImmutableSet<String> getToSet() {
        return getAddressSet( message.getHeaderValue( MessageHeaders.TO.getKey() ) );
    }

    public Set<String> getCCSet() {
        return getAddressSet( message.getHeaderValue( MessageHeaders.CC.getKey() ) );
    }

    public Set<String> getBCC() {
        return getAddressSet( message.getHeaderValue( MessageHeaders.BCC.getKey() ) );
    }

    public String getSubject() {
        return message.getHeaderValue( MessageHeaders.SUBJECT.getKey() );
    }

    public String getBody() {
        return message.getBody();
    }


    private ImmutableSet<String> getAddressSet( String addresses ) {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        if ( addresses != null ) {
            for ( String address : addresses.split( "[;,]" ) ) {
                builder.add( address.trim() );
            }
        }
        return builder.build();
    }


    @Override
    public String toString() {
        StringConcat concat = new StringConcat( "\n" );
        concat.append( "SMTP Message" );
        String from = message.getHeaderValue( MessageHeaders.FROM.getKey() );
        concat.appendIf( isNotBlank( from ), "From: " + from );

        String to = message.getHeaderValue( MessageHeaders.TO.getKey() );
        concat.appendIf( isNotBlank( to ), "To: " + to );

        String cc = message.getHeaderValue( MessageHeaders.CC.getKey() );
        concat.appendIf( isNotBlank( cc ), "cc: " + cc );

        String bcc = message.getHeaderValue( MessageHeaders.BCC.getKey() );
        concat.appendIf( isNotBlank( bcc ), "bcc: " + bcc );

        String replyTo = message.getHeaderValue( MessageHeaders.REPLY_TO.getKey() );
        concat.appendIf( isNotBlank( replyTo ), "Reply to: " + replyTo );

        String subject = message.getHeaderValue( MessageHeaders.SUBJECT.getKey() );
        concat.appendIf( isNotBlank( subject ), "Subject: " + subject );

        String body = message.getBody();
        concat.appendIf( isNotBlank( body ), "Body: " + body );

        return concat.toString();
    }
}
