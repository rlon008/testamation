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


import nz.co.testamation.common.util.StringConcat;

public class EmailAddresses {
    private static final String ADDRESS_SEPERATOR = ", ";

    private String from;
    private String replyTo;
    private String toAddresses;
    private String ccAddresses;
    private String bccAddresses;

    public EmailAddresses( String from ) {
        this( from, null, null, null, null );
    }

    public EmailAddresses( String from, String toAddresses ) {
        this( from, toAddresses, null, null, null );
    }

    public EmailAddresses( String from, String toAddresses, String ccAddresses, String bccAddresses, String replyTo ) {
        this.from = from;
        this.toAddresses = toAddresses;
        this.ccAddresses = ccAddresses;
        this.bccAddresses = bccAddresses;
        this.replyTo = replyTo;
    }


    public String getFrom() {
        return from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public String getToAddresses() {
        return toAddresses;
    }

    public String getCcAddresses() {
        return ccAddresses;
    }

    public String getBccAddresses() {
        return bccAddresses;
    }

    public EmailAddresses to( String additionalToAddresses ) {
        return to( additionalToAddresses, null, null );
    }

    public EmailAddresses to( String additionalToAddresses, String additionalCcAddresses ) {
        return to( additionalToAddresses, additionalCcAddresses, null );
    }

    public EmailAddresses to( String additionalToAddresses, String additionalCcAddresses, String additionalBccAddresses ) {
        return new EmailAddresses(
            getFrom(),
            concatenateAddresses( getToAddresses(), additionalToAddresses ),
            concatenateAddresses( getCcAddresses(), additionalCcAddresses ),
            concatenateAddresses( getBccAddresses(), additionalBccAddresses ),
            getReplyTo()
        );
    }

    private String concatenateAddresses( String originalAddresses, String additionalToAddresses ) {
        String s = new StringConcat( ADDRESS_SEPERATOR )
            .append( originalAddresses )
            .append( additionalToAddresses )
            .toNullString();
        return s;

    }

}
