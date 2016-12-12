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

package nz.co.testamation.common.util;

import com.google.common.collect.Iterables;
import org.apache.commons.lang3.StringUtils;

public class ObjectUtil {

    public static boolean allNotEmpty( Object... objects ) {
        for ( Object object : objects ) {
            if ( object == null ) {
                return false;
            }
            if ( object instanceof CharSequence && StringUtils.isBlank( (CharSequence) object ) ) {
                return false;
            }

            if ( object instanceof Iterable && Iterables.isEmpty( (Iterable<?>) object ) ) {
                return false;
            }
        }
        return true;
    }
}
