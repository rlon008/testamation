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

package nz.co.testamation.core.lifecycle.annotation;

import java.lang.annotation.ElementType;

@java.lang.annotation.Target( { ElementType.METHOD } )
@java.lang.annotation.Retention( java.lang.annotation.RetentionPolicy.RUNTIME )

/**
 * TearDown methods are executed last in a test life cycle. All TearDown are guaranteed to get run even
 * if there's an exception during the running phase. TearDown can be annotated on any method of a LifeCycle
 * component or a Step or a Config.
 */
public @interface TearDown {
}
