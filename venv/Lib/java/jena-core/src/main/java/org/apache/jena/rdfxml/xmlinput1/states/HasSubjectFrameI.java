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

import org.apache.jena.rdfxml.xmlinput1.impl.ANode;

public interface HasSubjectFrameI extends FrameI {
  void aPredAndObj(ANode p, ANode o);
  
  /**
   * Create a triple r rdf:subj * .
   * The structure of the parser means that three triples
   * in the quad are produced by the property element code,
   * but the final triple is produced by its parent.
   * @param r The node that is the reification.
   */
  void makeSubjectReificationWith(ANode r);
}
