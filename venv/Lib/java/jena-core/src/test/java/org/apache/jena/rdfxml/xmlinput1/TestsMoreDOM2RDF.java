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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;
import org.apache.jena.shared.JenaException;
import org.apache.jena.util.JenaXMLInput;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class TestsMoreDOM2RDF extends TestCase implements StatementHandler {

    int count = 0;

	public TestsMoreDOM2RDF(String name) {
		super(name);
	}

    static private DocumentBuilder domParser;

    static {
        try {
            DocumentBuilderFactory factory = JenaXMLInput.newDocumentBuilderFactory();
            factory.setNamespaceAware(true);
            domParser = factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException rte){
            throw new JenaException(rte);
        }
    }

	public void testDOMwithARP() throws SAXException, IOException {

        InputStream in = new FileInputStream("testing/wg/Class/conclusions001.rdf");
		Document document = domParser
				.parse(in,"http://www.example.org/");

		DOM2Model d2m = DOM2Model.createD2M("http://www.example.org/",null);

		d2m.getHandlers().setStatementHandler(this);

			try {
		        d2m.load(document);
			} finally {
				d2m.close();
			}

         assertEquals("Incorrect number of triples",3,count);

	}


    @Override
    public void statement(AResource subj, AResource pred, AResource obj) {
        count++;

    }


    @Override
    public void statement(AResource subj, AResource pred, ALiteral lit) {
        count++;

    }

}
