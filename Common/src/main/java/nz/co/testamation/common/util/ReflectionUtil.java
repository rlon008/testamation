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

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

public class ReflectionUtil {

    public static Object getFieldValue( Object obj, String fieldName ) {
        try {
            return getField( obj, fieldName ).get( obj );
        } catch ( IllegalAccessException e ) {
            throw new RuntimeException( e );
        }
    }


    public static void setField( Object obj, String fieldName, Object value ) {
        try {
            getField( obj, fieldName ).set( obj, value );
        } catch ( IllegalAccessException e ) {
            throw new RuntimeException( e );
        }
    }

    private static Field getField( Object obj, String fieldName ) {
        return FieldUtils.getField( obj.getClass(), fieldName, true );
    }

    public static <T> T getFieldValue( Object obj, String fieldName, Class<T> clazz ) {
        return (T) getFieldValue( obj, fieldName );
    }


}
