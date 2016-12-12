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

package nz.co.testamation.common.time;

import nz.co.testamation.testcommon.fixture.SomeFixture;
import org.junit.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ClockImplTest {

    ZoneId zoneId = ZoneId.systemDefault();
    LocalDateTime dateTime = SomeFixture.someDateTime();
    ClockImpl clock = new ClockImpl( Clock.fixed( dateTime.atZone( zoneId ).toInstant(), zoneId ) );

    @Test
    public void now() throws Exception {
        assertThat( clock.now(), equalTo( dateTime ) );
    }

    @Test
    public void today() throws Exception {
        assertThat( clock.today(), equalTo( dateTime.toLocalDate() ) );
    }

    @Test
    public void millis() throws Exception {
        assertThat( clock.currentTimestampMillis(), equalTo( dateTime.atZone( zoneId ).toInstant().toEpochMilli() ) );
    }


}