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

package nz.co.testamation.common;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClockImpl implements Clock {

    private final java.time.Clock delegate;

    public ClockImpl( java.time.Clock delegate ) {
        this.delegate = delegate;
    }

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now( delegate );
    }

    @Override
    public long currentTimestampMillis() {
        return delegate.millis();
    }

    @Override
    public LocalDate today() {
        return LocalDate.now( delegate );
    }
}
