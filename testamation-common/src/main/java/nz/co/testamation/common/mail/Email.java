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

public class Email {
    private final EmailAddresses emailAddresses;
    private final String subject;
    private final String htmlBody;
    private final String textBody;
    private EmailAttachment[] attachments;

    public Email( EmailAddresses emailAddresses, String subject, String textBody, String htmlBody, EmailAttachment... attachments ) {
        this.emailAddresses = emailAddresses;
        this.subject = subject;
        this.htmlBody = htmlBody;
        this.textBody = textBody;
        this.attachments = attachments;
    }

    public Email( EmailAddresses emailAddresses, String subject, String textBody ) {
        this( emailAddresses, subject, textBody, null );
    }

    public EmailAddresses getEmailAddresses() {
        return emailAddresses;
    }

    public String getSubject() {
        return subject;
    }

    public String getHtmlBody() {
        return htmlBody;
    }

    public String getTextBody() {
        return textBody;
    }

    public EmailAttachment[] getAttachments() {
        return attachments;
    }
}
