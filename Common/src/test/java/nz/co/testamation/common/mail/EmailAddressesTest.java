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
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class EmailAddressesTest {
    String additionalToAddresses = SomeFixture.someString();
    String additionalToAddresses2 = SomeFixture.someString();
    String additionalCcAddresses = SomeFixture.someString();
    String additionalCcAddresses2 = SomeFixture.someString();
    String additionalBccAddresses = SomeFixture.someString();
    String from = SomeFixture.someEmail();

    @Test
    public void addAdditionalToAddresses() throws Exception {
        EmailAddresses actual = new EmailAddresses( from ).to( additionalToAddresses );
        assertThat( actual.getFrom(), equalTo( from ) );
        assertThat( actual.getToAddresses(), equalTo( additionalToAddresses ) );
        assertThat( actual.getReplyTo(), Matchers.nullValue() );
        assertThat( actual.getCcAddresses(), Matchers.nullValue() );
        assertThat( actual.getBccAddresses(), Matchers.nullValue() );
    }

    @Test
    public void addAdditionalToAddressesAndCC() throws Exception {
        EmailAddresses actual = new EmailAddresses( from ).to( additionalToAddresses, additionalCcAddresses );
        assertThat( actual.getFrom(), equalTo( from ) );
        assertThat( actual.getToAddresses(), equalTo( additionalToAddresses ) );
        assertThat( actual.getReplyTo(), Matchers.nullValue() );
        assertThat( actual.getCcAddresses(), equalTo( additionalCcAddresses ) );
        assertThat( actual.getBccAddresses(), Matchers.nullValue() );
    }

    @Test
    public void addAdditionalToAddressesAndCCAndBcc() throws Exception {
        EmailAddresses actual = new EmailAddresses( from ).to( additionalToAddresses, additionalCcAddresses, additionalBccAddresses );
        assertThat( actual.getFrom(), equalTo( from ) );
        assertThat( actual.getToAddresses(), equalTo( additionalToAddresses ) );
        assertThat( actual.getReplyTo(), Matchers.nullValue() );
        assertThat( actual.getCcAddresses(), equalTo( additionalCcAddresses ) );
        assertThat( actual.getBccAddresses(), equalTo( additionalBccAddresses ) );
    }

    @Test
    public void addAdditionalAddressesChainsCorrectly() throws Exception {
        EmailAddresses actual = new EmailAddresses( from )
            .to( additionalToAddresses, additionalCcAddresses)
            .to( additionalToAddresses2, additionalCcAddresses2, additionalBccAddresses );
        assertThat( actual.getFrom(), equalTo( from ) );
        assertThat( actual.getToAddresses(), equalTo( additionalToAddresses + ", " + additionalToAddresses2 ) );
        assertThat( actual.getCcAddresses(), equalTo( additionalCcAddresses + ", " + additionalCcAddresses2 ) );
        assertThat( actual.getBccAddresses(), equalTo( additionalBccAddresses ) );

    }


}