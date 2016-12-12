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

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class AsyncEmailClient implements EmailClient {
    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private final MimeMessageFactory mimeMessageFactory;
    private final SendEmailJobFactory sendEmailJobFactory;


    public AsyncEmailClient( MimeMessageFactory mimeMessageFactory ) {
        this( new ScheduledThreadPoolExecutor( 1 ), mimeMessageFactory, new SendEmailJobFactoryImpl() );
    }

    public AsyncEmailClient(
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor,
        MimeMessageFactory mimeMessageFactory,
        SendEmailJobFactory sendEmailJobFactory
    ) {
        this.scheduledThreadPoolExecutor = scheduledThreadPoolExecutor;
        this.mimeMessageFactory = mimeMessageFactory;
        this.sendEmailJobFactory = sendEmailJobFactory;
    }

    @Override
    public void send( Email email ) {
        scheduledThreadPoolExecutor.execute( sendEmailJobFactory.create(
            mimeMessageFactory.create( email )
        ) );
    }
}
