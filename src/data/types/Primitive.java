package data.types;

import instructions.InstructionStack;

public interface Primitive {
    public String getName();
    public int getSize();

    public int getFrameDepth();
    public void setFrameDepth(int depth);

    public void putAdress(int register, InstructionStack iStack);
    public void putValue(int register, InstructionStack iStack);
}
