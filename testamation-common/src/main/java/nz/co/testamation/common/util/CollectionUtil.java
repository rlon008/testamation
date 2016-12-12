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

import java.util.Collection;
import java.util.Optional;

public class CollectionUtil {

    public static <T extends Comparable> T getMax( Collection<T> collection ) {
        Optional<T> max = collection.stream().max( ( o1, o2 ) -> o1.compareTo( o2 ) );
        return max.isPresent() ? max.get() : null;
    }
}
