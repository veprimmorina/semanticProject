/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.rdfxml.xmlinput1;

/**
 * This exception should only be seen in user code when using 
 * {@link SAX2RDF} or {@link SAX2Model}.
 * This is thrown after.error errors,
 * most importantly a {@link ARPErrorNumbers#ERR_SAX_FATAL_ERROR}.
 * User code in an error handler may throw a different exception.
 */
public class FatalParsingErrorException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -1098004693549165728L;
    // no message
}
