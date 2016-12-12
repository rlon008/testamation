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

import nz.co.testamation.testcommon.template.MockitoTestTemplate;
import org.junit.Test;

import javax.mail.Message;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class AsyncEmailClientTest {

    abstract class Template extends MockitoTestTemplate {
        Email email = mock( Email.class );
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = mock( ScheduledThreadPoolExecutor.class );
        MimeMessageFactory mimeMessageFactory = mock( MimeMessageFactory.class );
        SendEmailJobFactory sendEmailJobFactory = mock( SendEmailJobFactory.class );

        AsyncEmailClient client = new AsyncEmailClient( scheduledThreadPoolExecutor, mimeMessageFactory, sendEmailJobFactory );

    }

    @Test
    public void happyDay() throws Exception {

        new Template() {
            Message message = mock( Message.class );
            Runnable job = mock( Runnable.class );

            @Override
            protected void given() throws Exception {
                given( mimeMessageFactory.create( email ) ).thenReturn( message );
                given( sendEmailJobFactory.create( message ) ).thenReturn( job );
            }

            @Override
            protected void when() throws Exception {
                client.send( email );

            }

            @Override
            protected void then() throws Exception {
                verify( scheduledThreadPoolExecutor ).execute( job );
            }

        }.run();

    }


}