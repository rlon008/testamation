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

import nz.co.testamation.common.Clock;
import nz.co.testamation.common.util.ReflectionUtil;
import nz.co.testamation.core.lifecycle.annotation.TearDown;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimePassesConfig extends AbstractConfig<LocalDateTime> {

    private final Duration offset;
    private final Clock clock;
    private java.time.Clock originalUnderlyingClock;

    public TimePassesConfig( Duration offset, Clock clock ) {
        this.offset = offset;
        this.clock = clock;
    }

    @Override
    public LocalDateTime apply() {
        originalUnderlyingClock = ReflectionUtil.getFieldValue( clock, "delegate", java.time.Clock.class );
        ReflectionUtil.setField( clock, "delegate", java.time.Clock.offset( originalUnderlyingClock, offset ) );
        return clock.now();
    }

    @TearDown
    public void tearDown() {
        ReflectionUtil.setField( clock, "delegate", originalUnderlyingClock );
    }
}
