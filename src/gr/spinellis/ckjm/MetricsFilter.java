/*
 * (C) Copyright 2005 Diomidis Spinellis
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

package gr.spinellis.ckjm;

import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import org.apache.bcel.Repository;
import org.apache.bcel.Constants;
import org.apache.bcel.util.*;
import java.io.*;
import java.util.*;

/**
 * Convert a list of classes into their metrics.
 * Process standard input lines or command line arguments
 * containing a class file name or a jar file name,
 * followed by a space and a class file name.
 * Display on the standard output the name of each class, followed by its
 * six Chidamber Kemerer metrics:
 * WMC, DIT, NOC, CBO, RFC, LCOM
 *
 * @see ClassMetrics
 * @version $Revision: 1.9 $
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a>
 */
public class MetricsFilter {
    /** True if the measurements should include calls to the Java JDK into account */
    private static boolean includeJdk = false;

    /** True if the reports should only include public classes */
    private static boolean onlyPublic = false;

    /** Return true if the measurements should include calls to the Java JDK into account */
    public static boolean isJdkIncluded() { return includeJdk; }
    /** Return true if the measurements should include all classes */
    public static boolean includeAll() { return !onlyPublic; }


    static void processClass(ClassMetricsContainer cm, String clspec) {
    String jar = null;
     String className = clspec.replace('/', '.');

    int spaceIndex = clspec.indexOf(' ');
    if (spaceIndex != -1) {
        jar = clspec.substring(0, spaceIndex);
        className = clspec.substring(spaceIndex + 1);
    }

    JavaClass jc = loadClass(jar, className);
    if (jc != null) {
        visitClass(cm, jc);
    }
    }

    private static JavaClass loadClass(String jar, String className) {
        try {
            if (jar != null) {
                return new ClassParser(jar, className).parse();
            } else {
                return new ClassParser(className).parse();
            }
        } catch (IOException e) {
            if (jar != null) {
                System.err.println("Error loading " + className + " from " + jar + ": " + e);
            } else {
                System.err.println("Error loading " + className + ": " + e);
            }
            return null;
        }
    }

    private static void visitClass(ClassMetricsContainer cm, JavaClass jc) {
        ClassVisitor visitor = new ClassVisitor(jc, cm);
        visitor.start();
        visitor.end();
    }


    public static void runMetrics(String[] files, CkjmOutputHandler outputHandler) {
        ClassMetricsContainer cm = new ClassMetricsContainer();

        for (int i = 0; i < files.length; i++)
            processClass(cm, files[i]);
        cm.printMetrics(outputHandler);
    }

  
    public static void filter(String[] argv) {
        int argp = 0;

        processCommandLineArguments(argv, argp);

        ClassMetricsContainer cm = new ClassMetricsContainer();

        if (argv.length == argp) {
            readInput(cm);
        }

        processClassArguments(argv, argp, cm);

        printMetrics(cm);
    }

    private static void processCommandLineArguments(String[] argv, int argp) {
        if (argv.length > argp && argv[argp].equals("-s")) {
            includeJdk = true;
            argp++;
        }
        if (argv.length > argp && argv[argp].equals("-p")) {
            onlyPublic = true;
            argp++;
        }
    }

    private static void readInput(ClassMetricsContainer cm) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String s;
            while ((s = in.readLine()) != null)
                processClass(cm, s);
        } catch (Exception e) {
            System.err.println("Error reading line: " + e);
            System.exit(1);
        }
    }

    private static void processClassArguments(String[] argv, int argp, ClassMetricsContainer cm) {
        for (int i = argp; i < argv.length; i++)
            processClass(cm, argv[i]);
    }

   

    private static void printMetrics(ClassMetricsContainer cm) {
        CkjmOutputHandler handler = new PrintPlainResults(System.out);
        cm.printMetrics(handler);
    }
    
    public static void main(String[] argv) {
       filter(argv);
    }
  }

