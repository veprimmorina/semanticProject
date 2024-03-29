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

package org.apache.jena.fuseki.validation.html;

import static org.apache.jena.fuseki.validation.html.ValidatorHtmlLib.*;
import static org.apache.jena.riot.SysRIOT.fmtMessage;

import java.io.*;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.jena.atlas.io.IO;
import org.apache.jena.fuseki.system.FusekiNetLib;
import org.apache.jena.riot.*;
import org.apache.jena.riot.system.ErrorHandler;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.riot.system.StreamRDFWriter;

/** Validate data - report in HTML */
public class DataValidatorHTML
{
    private DataValidatorHTML()
    { }

    static final String paramLineNumbers      = "linenumbers";
    static final String paramFormat           = "outputFormat";
    static final String paramIndirection      = "url";
    static final String paramData             = "data";
    static final String paramSyntax           = "languageSyntax";
    //static final String paramSyntaxExtended   = "extendedSyntax";

    public static void executeHTML(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        try {
            String syntax = FusekiNetLib.safeParameter(httpRequest, paramSyntax);
            if ( syntax == null || syntax.equals("") )
                syntax = RDFLanguages.NQUADS.getName();

            Lang language = RDFLanguages.shortnameToLang(syntax);
            if ( language == null ) {
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown syntax: " + syntax);
                return;
            }

            // Take the charset provided even if it is wrong because
            // HTML input maybe have been written in some default.
            // The HTML input and what the user typed should agree.
            Reader input = createInput(httpRequest, httpResponse);

            ServletOutputStream outStream = httpResponse.getOutputStream();
            ErrorHandlerMsg errorHandler = new ErrorHandlerMsg(outStream);

            // Capture logging errors.
            PrintStream stderr = System.err;
            System.setErr(new PrintStream(outStream));

            // Headers
            setHeaders(httpResponse);

            outStream.println("<html>");
            printHead(outStream, "Jena Data Validator Report");
            outStream.println("<body>");

            outStream.println("<h1>RIOT Parser Report</h1>");
            outStream.println("<p>Line and column numbers refer to the original input.</p>");
            outStream.println("<p>&nbsp;</p>");

            // Need to escape HTML.
            OutputStream output1 = new OutputStreamNoHTML(new BufferedOutputStream(outStream));
            StreamRDF output = StreamRDFWriter.getWriterStream(output1, Lang.NQUADS, RIOT.getContext());
            try {
                startFixed(outStream);
                @SuppressWarnings("deprecation")
                RDFParser parser = RDFParser.create()
                    .source(input)
                    .strict(true)
                    .lang(language)
                    .errorHandler(errorHandler)
                    .resolveURIs(false)
                    .build();
                RiotException exception = null;
                try {
                    output.start();
                    parser.parse(output);
                    output.finish();
                    output1.flush();
                    outStream.flush();
                    System.err.flush();
                } catch (RiotException ex) {
                    exception = ex;
                }
            }
            finally {
                finishFixed(outStream);
                System.err.flush();
                System.setErr(stderr);
            }

            outStream.println("</body>");
            outStream.println("</html>");
        } catch (Exception ex) {
            serviceLog.warn("Exception in validationRequest", ex);
        }
    }

    static final long LIMIT = 50000;

    static class OutputStreamNoHTML extends FilterOutputStream {

        public OutputStreamNoHTML(OutputStream out) {
            super(out);
        }

        static byte[] escLT = { '&', 'l', 't' , ';' };
        static byte[] escGT = { '&', 'g', 't' , ';' };
        static byte[] escAmp = { '&', 'a', 'm' , 'p', ';' };

        @Override
        public void write(int b) throws IOException {
            if ( b == '&' )      writeEsc(escAmp);
            else if ( b == '>' ) writeEsc(escGT);
            else if ( b == '<' ) writeEsc(escLT);
            else
                super.write(b);
        }

        private void writeEsc(byte[] bytes) throws IOException {
            for ( byte b : bytes )
                super.write(b);
        }
    }


    // Error handler that records messages
    private static class ErrorHandlerMsg implements ErrorHandler {
        private ServletOutputStream out;

        ErrorHandlerMsg(ServletOutputStream out) { this.out = out; }

        @Override
        public void warning(String message, long line, long col)
        { output(message, line, col, "Warning", "warning"); }

        // Attempt to continue.
        @Override
        public void error(String message, long line, long col)
        { output(message, line, col, "Error", "error"); }

        @Override
        public void fatal(String message, long line, long col)
        { output(message, line, col, "Fatal", "error"); throw new RiotException(fmtMessage(message, line, col)); }

        private void output(String message, long line, long col, String typeName, String className) {
            try {
                String str = fmtMessage(message, line, col);
                //String str = typeName+": "+message;
                str = htmlQuote(str);
                out.print("<div class=\""+className+"\">");
                out.print(str);
                out.print("</div>");
            } catch (IOException ex) { IO.exception(ex); }
        }
    }

    private static Reader createInput(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        Reader reader = null;
        String[] args = httpRequest.getParameterValues(paramData);
        if ( args == null || args.length == 0 ) {
            System.err.println("Not a form");
            // Not a form?
            reader = httpRequest.getReader();
        } else if ( args.length > 1 ) {
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Too many parameters for '" + paramData + "='");
            return null;
        } else {
            reader = new StringReader(args[0]);
        }

        if ( reader == null ) {
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Can't find data to validate");
            return null;
        }

        return reader;
    }
}
