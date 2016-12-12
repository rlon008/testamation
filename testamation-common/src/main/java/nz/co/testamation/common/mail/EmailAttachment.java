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

package nz.co.testamation.common.mail;

public class EmailAttachment {

    private final byte[] data;
    private final String contentType;
    private final String fileName;

    public EmailAttachment( byte[] data, String contentType, String fileName ) {
        this.data = data;
        this.contentType = contentType;
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return String.format( "Attachment{data:%s, contentType:'%s', fileName=:'%s'}", data, contentType, fileName );
    }
}
