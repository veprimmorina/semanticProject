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

package org.apache.jena.sparql.core;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.riot.system.PrefixMap;
import org.apache.jena.riot.system.PrefixMapSink;
import org.apache.jena.sparql.graph.GraphSink;


/**
 * An always empty {@link DatasetGraph} that accepts changes but ignores them.
 *
 * @see DatasetGraphZero - a DSG that does not allow changes.
 */
public class DatasetGraphSink extends DatasetGraphNull {

    public static DatasetGraph create() { return new DatasetGraphSink(); }

    @Override
    protected Graph createGraph() {
        return GraphSink.instance();
    }

    private DatasetGraphSink() {}

    // Ignore all updates.

    @Override
    public void add(Quad quad) { /* ignore */ }

    @Override
    public void delete(Quad quad) { /* ignore */ }

    @Override
    public void deleteAny(Node g, Node s, Node p, Node o) { /* ignore */ }

    @Override
    public void addGraph(Node graphName, Graph graph) { /* ignore */ }

    @Override
    public void removeGraph(Node graphName) { /* ignore */ }

    @Override
    public PrefixMap prefixes() {
        return PrefixMapSink.sink;
    }

}
