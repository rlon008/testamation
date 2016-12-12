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

package nz.co.testamation.common.mail;

import org.apache.commons.lang3.StringUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

public class MimeMessageFactoryImpl implements MimeMessageFactory {

    private final Session session;
    private MultipartMessageFactory multipartMessageFactory;


    public MimeMessageFactoryImpl( MailSessionFactory mailSessionFactory, MultipartMessageFactory multipartMessageFactory ) {
        this.multipartMessageFactory = multipartMessageFactory;
        this.session = mailSessionFactory.create();
    }

    @Override
    public Message create( Email email ) {
        try {
            EmailAddresses emailAddresses = email.getEmailAddresses();

            MimeMessage mimeMessage = new MimeMessage( session );
            mimeMessage.setSubject( email.getSubject() );
            mimeMessage.setFrom( new InternetAddress( emailAddresses.getFrom() ) );

            if ( StringUtils.isNotBlank( emailAddresses.getReplyTo() ) ) {
                mimeMessage.setReplyTo( InternetAddress.parse( emailAddresses.getReplyTo() ) );
            }

            addRecipients( mimeMessage, Message.RecipientType.TO, emailAddresses.getToAddresses() );
            addRecipients( mimeMessage, Message.RecipientType.CC, emailAddresses.getCcAddresses() );
            addRecipients( mimeMessage, Message.RecipientType.BCC, emailAddresses.getBccAddresses() );

            mimeMessage.setContent( multipartMessageFactory.create( email ) );
            mimeMessage.setSentDate( new Date() );

            return mimeMessage;
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }

    }

    private void addRecipients( MimeMessage mimeMessage, Message.RecipientType type, String emailAddresses ) throws MessagingException {
        if ( StringUtils.isNotBlank( emailAddresses ) ) {
            mimeMessage.addRecipients( type, emailAddresses );
        }
    }
}
