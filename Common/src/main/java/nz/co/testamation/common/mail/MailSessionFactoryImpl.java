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

import javax.mail.Session;
import java.util.Properties;

public class MailSessionFactoryImpl implements MailSessionFactory {
    private final Properties smtpProperties;

    public MailSessionFactoryImpl( String host, int port, long timeout, long connectionTimeout ) {
        this.smtpProperties = new Properties();
        smtpProperties.put( "mail.smtp.host", host );
        smtpProperties.put( "mail.smtp.port", port );
        smtpProperties.put( "mail.smtp.timeout", timeout );
        smtpProperties.put( "mail.smtp.connectiontimeout", connectionTimeout );
    }

    public MailSessionFactoryImpl( Properties smtpProperties ) {
        this.smtpProperties = smtpProperties;
    }

    @Override
    public Session create() {
        return Session.getInstance( smtpProperties );
    }
}
