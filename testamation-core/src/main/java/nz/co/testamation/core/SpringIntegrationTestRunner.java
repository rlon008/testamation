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

package nz.co.testamation.core;

import nz.co.testamation.core.lifecycle.DynamicProxyLifeCyclePhaseFactory;
import nz.co.testamation.core.lifecycle.LifeCyclePhaseFactory;
import nz.co.testamation.core.lifecycle.annotation.TestLifeCycle;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public class SpringIntegrationTestRunner extends AbstractTestRunner {

    private final ApplicationContext applicationContext;

    public SpringIntegrationTestRunner( ApplicationContext applicationContext, LifeCyclePhaseFactory lifeCyclePhaseFactory ) {
        super( lifeCyclePhaseFactory );
        this.applicationContext = applicationContext;
    }

    public SpringIntegrationTestRunner( ApplicationContext applicationContext ) {
        this( applicationContext, new DynamicProxyLifeCyclePhaseFactory() );
    }

    @Override
    protected Map<String, Object> getTestLifeCycleBeans() {
        return applicationContext.getBeansWithAnnotation( TestLifeCycle.class );
    }

    @Override
    protected void autowireBean( Object obj ) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean( obj );
    }


}
