package gr.spinellis.ckjm;

import java.util.HashSet;

public class CoupledClasses {
    /**
     * Coupled classes: classes that use this class
     */
    HashSet<String> afferentCoupledClasses;

    public CoupledClasses() {
        afferentCoupledClasses = new HashSet<>();
    }

    /**
     * Add a class to the set of classes that depend on this class
     */
    public void addAfferentCoupling(String name) {
        afferentCoupledClasses.add(name);
    }
}