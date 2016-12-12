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

package nz.co.testamation.hibernate;

import com.google.common.base.Predicate;
import nz.co.testamation.core.TestTemplate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class HibernateTestTemplate extends TestTemplate {

    @Autowired
    HibernateEntityTemplate hibernateEntityTemplate;


    public void doWithNewEntity( HibernateEntityTemplate.EntityWork work ) {
        hibernateEntityTemplate.doWithNewEntity( work );
    }

    public <T> T doInHibernate( HibernateEntityTemplate.Work<T> work ) {
        return hibernateEntityTemplate.doInHibernate( work );
    }

    public void doInHibernateNoResult( HibernateEntityTemplate.WorkNoResult work ) {
        hibernateEntityTemplate.doInHibernateNoResult( work );
    }

    public <T> void waitForInHibernate( HibernateEntityTemplate.Work<T> work, Predicate<T> predicate ) {
        hibernateEntityTemplate.waitForInHibernate( work, predicate );
    }

    public <T> T reload( Session session, T obj ) {
        return hibernateEntityTemplate.reload( session, obj );
    }
}
