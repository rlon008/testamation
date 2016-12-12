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
import org.junit.Test;

import javax.mail.BodyPart;
import javax.mail.Multipart;

import static org.hamcrest.Matchers.startsWith;

public class MultipartMessageFactoryImplTest {


    abstract class Template extends MockitoTestTemplate {
        String textBody = SomeFixture.someString();
        String htmlBody = SomeFixture.someString();
        Email email = mock( Email.class );
        AttachmentBodyPartFactory attachmentBodyPartFactory = mock( AttachmentBodyPartFactory.class );

        EmailAttachment attachment1 = mock( EmailAttachment.class );
        EmailAttachment attachment2 = mock( EmailAttachment.class );
        BodyPart bodyPart1 = mock( BodyPart.class );
        BodyPart bodyPart2 = mock( BodyPart.class );
    }

    @Test
    public void happyDay() throws Exception {

        new Template() {
            Multipart actual;

            @Override
            protected void given() throws Exception {
                given( email.getHtmlBody() ).thenReturn( htmlBody );
                given( email.getTextBody() ).thenReturn( textBody );
                given( email.getAttachments() ).thenReturn( new EmailAttachment[]{attachment1, attachment2} );

                given( attachmentBodyPartFactory.create( attachment1 ) ).thenReturn( bodyPart1 );
                given( attachmentBodyPartFactory.create( attachment2 ) ).thenReturn( bodyPart2 );
            }

            @Override
            protected void when() throws Exception {
                actual = new MultipartMessageFactoryImpl( attachmentBodyPartFactory ).create( email );
            }

            @Override
            protected void then() throws Exception {
                assertThat( actual.getContentType(), startsWith( "multipart/alternative" ) );
                assertThat( actual.getBodyPart( 0 ).getContent(), equalTo( textBody ) );
                assertThat( actual.getBodyPart( 1 ).getContent(), equalTo( htmlBody ) );
                assertThat( actual.getBodyPart( 2 ), equalTo( bodyPart1 ) );
                assertThat( actual.getBodyPart( 3 ), equalTo( bodyPart2 ) );
            }

        }.run();

    }


    @Test
    public void createCorrectlyIfNoHtmlContent() throws Exception {

        new Template() {
            Multipart actual;

            @Override
            protected void given() throws Exception {
                given( email.getHtmlBody() ).thenReturn( null );
                given( email.getTextBody() ).thenReturn( textBody );
            }

            @Override
            protected void when() throws Exception {
                actual = new MultipartMessageFactoryImpl( attachmentBodyPartFactory ).create( email );
            }

            @Override
            protected void then() throws Exception {
                assertThat( actual.getContentType(), startsWith( "multipart/mixed" ) );
                assertThat( actual.getBodyPart( 0 ).getContent(), equalTo( textBody ) );
            }

        }.run();

    }


}