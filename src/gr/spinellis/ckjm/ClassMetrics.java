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

import java.util.HashSet;

/**
 * Store details needed for calculating a class's Chidamber-Kemerer metrics.
 * Most fields in this class are set by ClassVisitor.
 * This class also encapsulates some policy decision regarding metrics
 * measurement.
 *
 * @see ClassVisitor
 * @version $Revision: 1.12 $
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a>
 */
public class ClassMetrics {
    private final CoupledClasses coupledClasses = new CoupledClasses();
    private final Metrics metrics = new Metrics();
    private boolean visited;
    private boolean isPublicClass;

    ClassMetrics() {
        metrics.wmc = 0;
        metrics.noc = 0;
        metrics.setCbo(0);
        metrics.npm = 0;
	visited = false;
        coupledClasses.afferentCoupledClasses = new HashSet<String>();
    }

    //getters
    public boolean isVisited() { return visited; }
    public int getWmc() {
        return metrics.getWmc();
    }
    public int getNoc() {
        return metrics.getNoc();
    }
    public int getRfc() {
        return metrics.getRfc();
    }
    public int getDit() {
        return metrics.getDit();
    }
    public int getCbo() {
        return metrics.getCbo();
    }
    public int getLcom() {
        return metrics.getLcom();
    }
    public int getCa() { return coupledClasses.afferentCoupledClasses.size(); }
    public int getNpm() {
        return metrics.getNpm();
    }
    public boolean isPublic() { return isPublicClass; }

    //setters
    public void setLcom(int l) {
        metrics.setLcom(l);
    }
    public void setCbo(int c) {
        metrics.setCbo(c);
    }
    public void setDit(int d) {
        metrics.setDit(d);
    }
    public void setRfc(int r) {
        metrics.setRfc(r);
    }
    public void incNoc() {
        metrics.incNoc();
    }
    public void incWmc() {
        metrics.incWmc();
    }
    public void setVisited() { visited = true; }
    public void setPublic() { isPublicClass = true; }
    public void incNpm() {
        metrics.incNpm();
    }
    public void addAfferentCoupling(String name) {
        coupledClasses.addAfferentCoupling(name);
    }

    public static boolean isJdkClass(String s) {
	return (s.startsWith("java.") ||
		s.startsWith("javax.") ||
		s.startsWith("org.omg.") ||
		s.startsWith("org.w3c.dom.") ||
		s.startsWith("org.xml.sax."));
    }

    public String toString() {
	return (
            metrics.getWmc() +
		" " + metrics.getDit() +
		" " + metrics.getNoc() +
		" " + metrics.getCbo() +
		" " + metrics.getRfc() +
		" " + metrics.getLcom() +
		" " + getCa()+
		" " + metrics.getNpm());
    }

}
