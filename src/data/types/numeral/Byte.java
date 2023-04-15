package data.types.numeral;

import data.management.RedeclaredException;
import data.management.StackFrame;
import instructions.Instruction;
import instructions.InstructionStack;
import util.Util;

public class Byte extends Numeral {
    public static final int SIZE = 1;
    public static final Class[] OPERABLE_CLASSES = {Byte.class};

    public Byte(StackFrame stackFrame, String name, InstructionStack iStack) throws RedeclaredException {
        super(stackFrame, name, iStack);
    }

    @Override
    public int getSize() {
        return Byte.SIZE;
    }

    @Override
    public void operate(Numeral o, Operation operation, InstructionStack iStack) throws InoperableDataTypes {
        if (!operable(o)) throw new InoperableDataTypes("Cannot perform operation " + operation.toString() + " on Byte and " + o.getClass().toString());
        
        iStack.push(Instruction.SFW.opCode);
        o.putValue(1, iStack);
        this.putValue(0, iStack);

        iStack.push(Operation.RRFromOperation(operation).opCode, 0, 1);

        iStack.push(Instruction.PUT_PR.opCode, 2);
    }

    @Override
    public void operate(int o, Operation operation, InstructionStack iStack) {
        iStack.push(Instruction.SFW.opCode);
        this.putValue(0, iStack);
        
        int[] brokenBytes = Util.byteBreak(o);
        switch(brokenBytes.length) {
            case 2 -> iStack.push(Instruction.SFL.opCode);
            case 4 -> iStack.push(Instruction.SFE.opCode);
        }
        
        iStack.push(Operation.RIFromOperation(operation).opCode, 0);
        iStack.push(brokenBytes);

        iStack.push(Instruction.SFW.opCode);
        iStack.push(Instruction.PUT_PR.opCode, 2);
    }

    @Override
    public boolean operable(Numeral o) {
        Class objectclass = o.getClass();
        for (Class operableClass : OPERABLE_CLASSES) {
            if (operableClass.equals(objectclass)) return true;
        }
        return false;
    }
}
