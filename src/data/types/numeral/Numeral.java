package data.types.numeral;

import data.management.RedeclaredException;
import data.management.StackFrame;
import data.types.Primitive;
import instructions.Instruction;
import instructions.InstructionStack;

public abstract class Numeral implements Primitive {
    private StackFrame stackFrame;
    private int frameDepth;

    private final String name;

    public Numeral(StackFrame stackFrame, String name, InstructionStack iStack) throws RedeclaredException {
        this.name = name;
        this.stackFrame = stackFrame;
        stackFrame.append(this, iStack);
    }

    public abstract void operate(Numeral o, Operation operation, InstructionStack iStack) throws InoperableDataTypes;
    public abstract void operate(int o, Operation operation, InstructionStack iStack) throws InoperableDataTypes;

    public abstract boolean operable(Numeral o);

    public String getName() {
        return this.name;
    }

    public int getFrameDepth() {
        return this.frameDepth;
    }

    public void setFrameDepth(int depth) {
        this.frameDepth = depth;
    }

    public void putAdress(int register, InstructionStack iStack) {
        this.stackFrame.putAdress(this, register, iStack);
    }

    public void putValue(int register, InstructionStack iStack) {
        this.putAdress(2, iStack);
        iStack.push(Instruction.MOV_RP.opCode, register, 3);
    }
}
