package data.management;

import java.util.ArrayList;

import data.types.Primitive;
import instructions.Instruction;
import instructions.InstructionStack;
import util.Util;

public class StackFrame {
    private ArrayList<Primitive> primitives;

    public StackFrame() {
        this.primitives = new ArrayList<>();
    }

    public void append(Primitive primitive, InstructionStack iStack) throws RedeclaredException {
        for (Primitive existingPrimitive : this.primitives) {
            if (primitive.getName().equals(existingPrimitive.getName())) throw new RedeclaredException("Variable " + primitive.getName() + " has already been defined in this scope!");
        }

        primitive.setFrameDepth(0);

        int sizeShift = primitive.getSize();
        for (Primitive oldPrimitive : this.primitives) {
            oldPrimitive.setFrameDepth(oldPrimitive.getFrameDepth() + sizeShift);
        }

        iStack.push(Instruction.PUSH.opCode, primitive.getSize());
    }

    public void putAdress(String primName, int register, InstructionStack iStack) throws UndeclaredException {
        Primitive primitive = null;
        for (Primitive containedPrimitive : this.primitives) {
            if (containedPrimitive.getName().equals(primName)) primitive = containedPrimitive;
        }
        if (primitive == null) throw new UndeclaredException("Variable " + primName + " has not been defined in current scope.");

        putAdress(primitive, register, iStack);
    }

    public void putAdress(Primitive primitive, int register, InstructionStack iStack) {
        iStack.push(Instruction.MOV_RR.opCode, register, 6);

        int[] instructionDepth = Util.byteBreak(primitive.getFrameDepth());
        switch (instructionDepth.length) {
            case 1 -> iStack.push(Instruction.SFW.opCode);
            case 2 -> iStack.push(Instruction.SFL.opCode);
            default -> iStack.push(Instruction.SFE.opCode);
        }
        iStack.push(Instruction.ADD_RI.opCode, register);
        iStack.push(instructionDepth);
    }
}
