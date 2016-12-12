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

package nz.co.testamation.core.exception;

import org.junit.Test;

public class ExceptionRecorderImplTest {

    private static class SomeException extends Exception {
    }

    @Test( expected = RuntimeException.class )
    public void verifyThrowRecordedException() throws Exception {
        SomeException exception = new SomeException();
        ExceptionRecorderImpl exceptionRecorderManager = new ExceptionRecorderImpl();
        exceptionRecorderManager.record( exception );
        exceptionRecorderManager.verify();
    }

    @Test
    public void ifNoRecordedExceptionThenVerifyDoesNothing() throws Exception {
        new ExceptionRecorderImpl().verify();
    }

    @Test
    public void resetClearsRecordedException() throws Exception {

        SomeException exception = new SomeException();
        ExceptionRecorderImpl exceptionRecorderManager = new ExceptionRecorderImpl();
        exceptionRecorderManager.record( exception );
        exceptionRecorderManager.reset();
        exceptionRecorderManager.verify();
    }

}