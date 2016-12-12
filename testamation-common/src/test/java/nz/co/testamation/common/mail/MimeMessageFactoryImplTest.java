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

import nz.co.testamation.testcommon.fixture.SomeFixture;
import nz.co.testamation.testcommon.template.MockitoTestTemplate;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import java.util.Properties;

import static org.hamcrest.Matchers.nullValue;

public class MimeMessageFactoryImplTest {

    abstract class Template extends MockitoTestTemplate {

        String subject = SomeFixture.someString();
        String from = SomeFixture.someEmail();
        String replyTo = SomeFixture.someEmail();
        String to1 = SomeFixture.someEmail();
        String to2 = SomeFixture.someEmail();
        String cc = SomeFixture.someEmail();
        String bcc = SomeFixture.someEmail();
        Email email = mock( Email.class );
        MailSessionFactory mailSessionFactory = mock( MailSessionFactory.class );
        MultipartMessageFactory multipartMessageFactory = mock( MultipartMessageFactory.class );

        Message actual;
        Session session = Session.getInstance( new Properties() );
        Multipart multipart = mock( Multipart.class );
        EmailAddresses emailAddresses = mock( EmailAddresses.class );


    }

    @Test
    public void happyDay() throws Exception {

        new Template() {
            @Override
            protected void given() throws Exception {
                given( mailSessionFactory.create( ) ).thenReturn( session );
                given( multipartMessageFactory.create( email ) ).thenReturn( multipart );
                given( email.getEmailAddresses() ).thenReturn( emailAddresses );
                given( emailAddresses.getFrom() ).thenReturn( from );
                given( emailAddresses.getReplyTo() ).thenReturn( replyTo );
                given( emailAddresses.getToAddresses() ).thenReturn( to1 + ", " + to2 );
                given( emailAddresses.getCcAddresses() ).thenReturn( cc );
                given( emailAddresses.getBccAddresses() ).thenReturn( bcc );
                given( email.getSubject() ).thenReturn( subject );

            }

            @Override
            protected void when() throws Exception {
                actual = new MimeMessageFactoryImpl( mailSessionFactory, multipartMessageFactory ).create( email );
            }

            @Override
            protected void then() throws Exception {
                assertThat( actual.getSession(), equalTo( session ) );
                assertThat( actual.getSubject(), equalTo( subject ) );
                assertThat( actual.getFrom().length, equalTo( 1 ) );
                assertThat( actual.getFrom()[ 0 ].toString(), equalTo( from ) );

                assertThat( actual.getReplyTo().length, equalTo( 1 ) );
                assertThat( actual.getReplyTo()[ 0 ].toString(), equalTo( replyTo ) );

                assertThat( actual.getRecipients( Message.RecipientType.TO ).length, equalTo( 2 ) );
                assertThat( actual.getRecipients( Message.RecipientType.TO )[ 0 ].toString(), equalTo( to1 ) );
                assertThat( actual.getRecipients( Message.RecipientType.TO )[ 1 ].toString(), equalTo( to2 ) );

                assertThat( actual.getRecipients( Message.RecipientType.CC ).length, equalTo( 1 ) );
                assertThat( actual.getRecipients( Message.RecipientType.CC )[ 0 ].toString(), equalTo( cc ) );

                assertThat( actual.getRecipients( Message.RecipientType.BCC ).length, equalTo( 1 ) );
                assertThat( actual.getRecipients( Message.RecipientType.BCC )[ 0 ].toString(), equalTo( bcc ) );

                assertThat( actual.getSentDate(), Matchers.notNullValue() );
                assertThat( actual.getContent(), equalTo( multipart ) );
            }

        }.run();

    }

    @Test
    public void createWithFromAndToAddressesOnly() throws Exception {

        new Template() {

            @Override
            protected void given() throws Exception {
                given( mailSessionFactory.create( ) ).thenReturn( session );
                given( multipartMessageFactory.create( email ) ).thenReturn( multipart );

                given( email.getEmailAddresses() ).thenReturn( emailAddresses );
                given( emailAddresses.getFrom() ).thenReturn( from );
                given( emailAddresses.getToAddresses() ).thenReturn( to1 );
                given( email.getSubject() ).thenReturn( subject );

            }

            @Override
            protected void when() throws Exception {
                actual = new MimeMessageFactoryImpl( mailSessionFactory, multipartMessageFactory ).create( email );

            }

            @Override
            protected void then() throws Exception {
                assertThat( actual.getSession(), equalTo( session ) );
                assertThat( actual.getSubject(), equalTo( subject ) );
                assertThat( actual.getFrom().length, equalTo( 1 ) );
                assertThat( actual.getFrom()[ 0 ].toString(), equalTo( from ) );

                assertThat( actual.getReplyTo().length, equalTo( 1 ) );
                assertThat( actual.getReplyTo()[0].toString(), equalTo( from ) );

                assertThat( actual.getRecipients( Message.RecipientType.TO ).length, equalTo( 1 ) );
                assertThat( actual.getRecipients( Message.RecipientType.TO )[ 0 ].toString(), equalTo( to1 ) );
                assertThat( actual.getRecipients( Message.RecipientType.CC ), nullValue() );
                assertThat( actual.getRecipients( Message.RecipientType.BCC ), nullValue() );
                assertThat( actual.getContent(), equalTo( multipart ) );
            }

        }.run();

    }


}