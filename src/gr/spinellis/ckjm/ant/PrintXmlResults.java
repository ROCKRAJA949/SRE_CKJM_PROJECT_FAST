/*
 * (C) Copyright 2005 Diomidis Spinellis, Julien Rentrop
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package gr.spinellis.ckjm.ant;

import gr.spinellis.ckjm.CkjmOutputHandler;
import gr.spinellis.ckjm.ClassMetrics;

import java.io.PrintStream;

/**
 * XML output formatter
 *
 * @author Julien Rentrop
 */
public class PrintXmlResults implements CkjmOutputHandler {
    private PrintStream p;

    public PrintXmlResults(PrintStream p) {
        this.p = p;
    }

    public void printHeader() {
        p.println("<?xml version=\"1.0\"?>");
        p.println("<ckjm>");
    }

    public void handleClass(String name, ClassMetrics c) {
       p.print("<class>\n" +
               "<name>" + name + "</name>\n" +
               formatMetric("wmc", c.getWmc()) +
               formatMetric("dit", c.getDit()) +
               formatMetric("noc", c.getNoc()) +
               formatMetric("cbo", c.getCbo()) +
               formatMetric("rfc", c.getRfc()) +
               formatMetric("lcom", c.getLcom()) +
               formatMetric("ca", c.getCa()) +
               formatMetric("npm", c.getNpm()) +
               "</class>\n");
   }

    private String formatMetric(String metricName, Object value) {
        return "<" + metricName + ">" + value + "</" + metricName + ">\n";
    }

    public void printFooter () {
        p.println("</ckjm>");
    }
}
