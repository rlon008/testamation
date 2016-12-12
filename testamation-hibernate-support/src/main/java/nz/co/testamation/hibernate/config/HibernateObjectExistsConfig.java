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

package nz.co.testamation.hibernate.config;

import java.io.Serializable;

public class HibernateObjectExistsConfig<T> extends AbstractHibernateEntityConfig<T> {

    private T hibernateObj;

    public HibernateObjectExistsConfig( T hibernateObj ) {
        this.hibernateObj = hibernateObj;
    }


    @Override
    public T apply() {
        return doInHibernate( session -> {
            Serializable id = session.save( hibernateObj );
            return (T) session.load( hibernateObj.getClass(), id );
        } );
    }

}
