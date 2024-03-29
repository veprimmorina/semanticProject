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

package org.apache.jena.sparql.sse.writers;

import java.util.Iterator;
import java.util.Map;

import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.sparql.core.Prologue;
import org.apache.jena.sparql.sse.Tags;
import org.apache.jena.sparql.util.FmtUtils;

public class WriterBasePrefix {
    private static final int NL = SSEWriteLib.NL;
    private static final int NoNL = SSEWriteLib.NoNL;
    private static final int NoSP = SSEWriteLib.NoSP;

    public static interface Fmt {
        void format();
    }

    /** Output, write the thing with formatter fmt */
    public static void output(IndentedWriter iWriter, Fmt fmt, Prologue prologue) {
        boolean printBase = false;

        boolean closeBase = printBase(iWriter, prologue);
        boolean closePrefix = printPrefix(iWriter, prologue);

        if ( fmt != null )
            fmt.format();

        if ( closeBase )
            SSEWriteLib.finish(iWriter, Tags.tagBase);
        if ( closePrefix )
            SSEWriteLib.finish(iWriter, Tags.tagPrefix);
        iWriter.ensureStartOfLine();
        iWriter.flush();
    }

    private static boolean printBase(IndentedWriter iWriter, Prologue prologue) {
        String baseURI = prologue.getBaseURI();

        if ( baseURI != null ) {
            SSEWriteLib.start(iWriter, Tags.tagBase, NoNL);
            iWriter.print(FmtUtils.stringForURI(baseURI));
            iWriter.println();
            return true;
        }
        return false;
    }

    private static boolean printPrefix(IndentedWriter iWriter, Prologue prologue) {
        PrefixMapping prefixMapping = prologue.getPrefixMapping();

        if ( prefixMapping != null ) {
            Map<String, String> m = prefixMapping.getNsPrefixMap();
            if ( !m.isEmpty() ) {
                int s = iWriter.getCol();
                SSEWriteLib.start(iWriter, Tags.tagPrefix, NoNL);
                SSEWriteLib.start(iWriter);

                // Indent to this col.
                int len = iWriter.getCurrentOffset();

                iWriter.incIndent(len);
                Iterator<String> iter = m.keySet().iterator();
                boolean first = true;
                for ( ; iter.hasNext() ; ) {
                    if ( !first )
                        iWriter.println();
                    first = false;
                    String prefix = iter.next();
                    String uri = prefixMapping.getNsPrefixURI(prefix);
                    // Base relative URI = but not prefix mappings!
                    uri = FmtUtils.stringForURI(uri, prologue.getBaseURI());
                    SSEWriteLib.start(iWriter);
                    iWriter.print(prefix);
                    iWriter.print(": ");
                    iWriter.print(uri);
                    SSEWriteLib.finish(iWriter);
                }
                iWriter.decIndent(len);
                SSEWriteLib.finish(iWriter);

                iWriter.ensureStartOfLine();
                return true;
            }
        }
        return false;
    }
}
