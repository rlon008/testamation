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

package nz.co.testamation.core.config;

import nz.co.testamation.common.time.Clock;
import nz.co.testamation.common.util.ReflectionUtil;
import nz.co.testamation.core.lifecycle.annotation.TearDown;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class FreezeTimeConfig extends AbstractConfig<LocalDateTime> {

    private final LocalDateTime freezeAt;
    private final Clock clock;

    private java.time.Clock originalUnderlyingClock;
    private ZoneId zoneId = ZoneId.systemDefault();

    public FreezeTimeConfig( LocalDateTime freezeAt, Clock clock ) {
        this.freezeAt = freezeAt;
        this.clock = clock;
    }

    public FreezeTimeConfig( Clock clock ) {
        this( clock.now(), clock );
    }

    @Override
    public LocalDateTime apply() {
        originalUnderlyingClock = ReflectionUtil.getFieldValue( clock, "delegate", java.time.Clock.class );
        ReflectionUtil.setField( clock, "delegate", java.time.Clock.fixed(
            freezeAt.atZone( zoneId ).toInstant(),
            zoneId
        ) );
        return freezeAt;
    }

    @TearDown
    public void tearDown() {
        ReflectionUtil.setField( clock, "delegate", originalUnderlyingClock );
    }
}
