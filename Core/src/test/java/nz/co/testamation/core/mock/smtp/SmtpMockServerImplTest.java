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

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import nz.co.testamation.testcommon.template.MockitoTestTemplate;
import org.junit.Test;

import java.util.List;

public class SmtpMockServerImplTest {

    abstract class Template extends MockitoTestTemplate {
        SimpleSmtpServerFactory simpleSmtpServerFactory = mock( SimpleSmtpServerFactory.class );
        SimpleSmtpServer simpleSmtpServer = mock( SimpleSmtpServer.class );
        SmtpMessageWrapperFactory smtpMessageWrapperFactory = mock( SmtpMessageWrapperFactory.class );

    }

    @Test
    public void getReceivedMailsHappyDay() throws Exception {

        new Template() {
            List<SmtpMessageWrapper> actual;
            SmtpMessage smtpMessage1 = mock( SmtpMessage.class );
            SmtpMessage smtpMessage2 = mock( SmtpMessage.class );

            SmtpMessageWrapper smtpMessageWrapper1 = mock( SmtpMessageWrapper.class );
            SmtpMessageWrapper smtpMessageWrapper2 = mock( SmtpMessageWrapper.class );


            @Override
            protected void given() throws Exception {
                given( simpleSmtpServerFactory.create() ).thenReturn( simpleSmtpServer );
                given( simpleSmtpServer.getReceivedEmail() ).thenReturn( Lists.newArrayList( smtpMessage1, smtpMessage2 ).iterator() );
                given( smtpMessageWrapperFactory.create( smtpMessage1 ) ).thenReturn( smtpMessageWrapper1 );
                given( smtpMessageWrapperFactory.create( smtpMessage2 ) ).thenReturn( smtpMessageWrapper2 );
            }

            @Override
            protected void when() throws Exception {
                actual = new SmtpMockServerImpl( simpleSmtpServerFactory, smtpMessageWrapperFactory )
                    .getReceivedEmails();
            }

            @Override
            protected void then() throws Exception {
                assertThat( actual, equalTo( ImmutableList.of( smtpMessageWrapper1, smtpMessageWrapper2 ) ) );

            }

        }.run();

    }


}