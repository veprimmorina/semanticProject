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

package org.apache.jena.rdfxml.xmlinput1.states;

import org.apache.jena.rdfxml.xmlinput1.impl.AbsXMLContext;
import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;

public class WantEmpty extends Frame {

    public WantEmpty(FrameI s, AbsXMLContext x) {
        super(s, x);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXParseException {
        warning(ERR_SYNTAX_ERROR,"The attributes on this property element, are not permitted with any content; expecting end element tag.");
    }

    @Override
    public FrameI startElement(String uri, String localName, String rawName,
            Attributes atts) throws SAXParseException {
        warning(ERR_SYNTAX_ERROR,"XML element <"+rawName+"> inside an empty property element, whose attributes prohibit any content.");
        return this;
    }

}
