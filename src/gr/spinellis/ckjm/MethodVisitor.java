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

import org.apache.bcel.generic.*;
import org.apache.bcel.Constants;

import java.util.*;

/**
 * Visit a method calculating the class's Chidamber-Kemerer metrics.
 * A helper class for ClassVisitor.
 *
 * @see ClassVisitor
 * @version $Revision: 1.8 $
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a>
 */
class MethodVisitor extends EmptyVisitor {
    /** Method generation template. */
    private MethodGen mg;
    /* The class's constant pool. */
    private ConstantPoolGen cp;
    /** The visitor of the class the method visitor is in. */
    private ClassVisitor cv;
    /** The metrics of the class the method visitor is in. */
    private ClassMetrics cm;

    /** Constructor. */
    MethodVisitor(MethodGen m, ClassVisitor c, ArrayList<TreeSet<String>> methodInteractions) {
	mg  = m;
	cv = c;
	cp  = mg.getConstantPool();
	cm = cv.getMetrics();
    }

    /** Start the method's visit. */
    public void start() {
	if (notAbstractNotNative()) {
        handleInstructions();
    }
    }

    private void handleInstructions() {
        for (InstructionHandle ih = mg.getInstructionList().getStart(); ih != null; ih = ih.getNext()) {
        Instruction i = ih.getInstruction();

        if(!visitInstruction(i))
            i.accept(this);
        }

        CodeExceptionGen[] handlers = mg.getExceptionHandlers();
        /* Measuring decision: couple exceptions */
        coupleExceptions(handlers);
    }

    private boolean notAbstractNotNative() {
        return !mg.isAbstract() && !mg.isNative();
    }

    /** Visit a single instruction. */
    private boolean visitInstruction(Instruction i) {
	short opcode = i.getOpcode();

	return ((InstructionConstants.INSTRUCTIONS[opcode] != null) &&
	   !(i instanceof ConstantPushInstruction) &&
	   !(i instanceof ReturnInstruction));
    }

    /** Local variable use. */
    public void visitLocalVariableInstruction(LocalVariableInstruction i) {
	if(OpcodeNotEqualConstants(i))
	    cv.registerCoupling(i.getType(cp));
    }

    private static boolean OpcodeNotEqualConstants(LocalVariableInstruction i) {
        return i.getOpcode() != Constants.IINC;
    }

    /** Array use. */
    public void visitArrayInstruction(ArrayInstruction i) {
	cv.registerCoupling(i.getType(cp));
    }

    /** Field access. */
    public void visitFieldInstruction(FieldInstruction i) {
	cv.registerFieldAccess(i.getClassName(cp), i.getFieldName(cp));
	cv.registerCoupling(i.getFieldType(cp));
    }

    /** Method invocation. */
    public void visitInvokeInstruction(InvokeInstruction i) {
	Type[] argTypes   = i.getArgumentTypes(cp);
    registerCoupling(argTypes);
    cv.registerCoupling(i.getReturnType(cp));
	/* Measuring decision: measure overloaded methods separately */
	cv.registerMethodInvocation(i.getClassName(cp), i.getMethodName(cp), argTypes);
    }

    private void registerCoupling(Type[] argTypes) {
        for (int j = 0; j < argTypes.length; j++)
            cv.registerCoupling(argTypes[j]);
    }

    /** Visit an instanceof instruction. */
    public void visitINSTANCEOF(INSTANCEOF i) {
	cv.registerCoupling(i.getType(cp));
    }

    /** Visit checklast instruction. */
    public void visitCHECKCAST(CHECKCAST i) {
	cv.registerCoupling(i.getType(cp));
    }

    /** Visit return instruction. */
    public void visitReturnInstruction(ReturnInstruction i) {
	cv.registerCoupling(i.getType(cp));
    }

    private void coupleExceptions(CodeExceptionGen[] handlers) {
        for(int i = 0; i < handlers.length; i++) {
            Type t = handlers[i].getCatchType();
            if (tNotNULL(t))
            cv.registerCoupling(t);
        }
    }

    private static boolean tNotNULL(Type t) {
        return t != null;
    }
}
