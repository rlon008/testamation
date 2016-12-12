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

package nz.co.testamation.core.lifecycle;

import com.google.common.collect.Lists;
import nz.co.testamation.core.TestTemplate;
import nz.co.testamation.core.lifecycle.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DynamicProxyLifeCyclePhaseFactory implements LifeCyclePhaseFactory {

    @Override
    public List<LifeCyclePhase> createReset( Object lifeCycle ) {
        return createLifeCyclePhases( lifeCycle, Reset.class );
    }

    @Override
    public List<LifeCyclePhase> createAfterStep( Object lifeCycle ) {
        return createLifeCyclePhases( lifeCycle, AfterStep.class );
    }

    @Override
    public List<LifeCyclePhase> createAfterGiven( Object lifeCycle ) {
        return createLifeCyclePhases( lifeCycle, AfterGiven.class );

    }

    @Override
    public List<LifeCyclePhase> createAfterExternalBehaviour( Object lifeCycle ) {
        return createLifeCyclePhases( lifeCycle, AfterExternalBehaviour.class );

    }

    @Override
    public List<LifeCyclePhase> createAfterWhen( Object lifeCycle ) {
        return createLifeCyclePhases( lifeCycle, AfterWhen.class );
    }

    @Override
    public List<LifeCyclePhase> createTearDown( Object lifeCycle ) {
        return createLifeCyclePhases( lifeCycle, TearDown.class );
    }

    @Override
    public LifeCyclePhase createTearDown( TestTemplate testTemplate ) {
        return testTemplate::tearDown;
    }


    private List<LifeCyclePhase> createLifeCyclePhases( Object obj, Class clazz ) {
        Method[] methods = obj.getClass().getMethods();

        ArrayList<LifeCyclePhase> lifeCycle = Lists.newArrayList();
        for ( Method method : methods ) {
            if ( method.isAnnotationPresent( clazz ) ) {
                lifeCycle.add( () -> method.invoke( obj ) );
            }
        }
        return lifeCycle;
    }

}
