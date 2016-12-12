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


import nz.co.testamation.common.util.StringUtil;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class MultipartMessageFactoryImpl implements MultipartMessageFactory {
    private final AttachmentBodyPartFactory attachmentBodyPartFactory;

    public MultipartMessageFactoryImpl() {
        this( new AttachmentBodyPartFactoryImpl() );
    }

    public MultipartMessageFactoryImpl( AttachmentBodyPartFactory attachmentBodyPartFactory ) {
        this.attachmentBodyPartFactory = attachmentBodyPartFactory;
    }

    @Override
    public Multipart create( Email email ) throws MessagingException {
        Multipart multipart = isNotBlank( email.getHtmlBody() ) ? new MimeMultipart("alternative") : new MimeMultipart();

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText( StringUtil.toNotNullString( email.getTextBody() ) );
        multipart.addBodyPart( textPart );

        if ( isNotBlank( email.getHtmlBody() ) ) {
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent( email.getHtmlBody(), "text/html" );
            multipart.addBodyPart( htmlPart );
        }

        if ( email.getAttachments() != null ) {
            for ( EmailAttachment attachment : email.getAttachments() ) {
                multipart.addBodyPart( attachmentBodyPartFactory.create( attachment ) );
            }
        }
        return multipart;
    }


}
