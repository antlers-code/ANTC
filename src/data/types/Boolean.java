package data.types;

import data.management.RedeclaredException;
import data.management.StackFrame;
import instructions.Instruction;
import instructions.InstructionStack;

public class Boolean implements Primitive {
    public static final int SIZE = 1;

    public final String name;
    private StackFrame stackFrame;
    private int frameDepth;

    public Boolean(StackFrame stackFrame, String name, InstructionStack iStack) throws RedeclaredException {
        this.name = name;
        this.stackFrame = stackFrame;
        stackFrame.append(this, iStack);
    }

    public void and(Boolean o, InstructionStack iStack) {
        o.putValue(1, iStack);
        this.putValue(0, iStack);

        iStack.push(Instruction.AND_RR.opCode, 0, 1);

        iStack.push(Instruction.PUT_PR.opCode, 2, 0);
    }

    public void or(Boolean o, InstructionStack iStack) {
        o.putValue(1, iStack);
        this.putValue(0, iStack);

        iStack.push(Instruction.OR_RR.opCode, 0, 1);

        iStack.push(Instruction.PUT_PR.opCode, 2, 0);
    }

    public void xor(Boolean o, InstructionStack iStack) {
        o.putValue(1, iStack);
        this.putValue(0, iStack);

        iStack.push(Instruction.AND_RR.opCode, 0, 1);

        iStack.push(Instruction.PUT_PR.opCode, 2, 0);
    }

    public void not(InstructionStack iStack) {
        this.putValue(0, iStack);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    public int getFrameDepth() {
        return frameDepth;
    }

    @Override
    public void setFrameDepth(int depth) {
        this.frameDepth = depth;
    }

    @Override
    public void putAdress(int register, InstructionStack iStack) {
        this.stackFrame.putAdress(this, register, iStack);
    }

    @Override
    public void putValue(int register, InstructionStack iStack) {
        this.putAdress(2, iStack);
        iStack.push(Instruction.MOV_RP.opCode, register, 3);
    }
    
}
