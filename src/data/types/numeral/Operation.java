package data.types.numeral;

import instructions.Instruction;

public enum Operation {
    
    SET, ADD, SUB, MUL, DIV, MOD, EXP, AND, OR, XOR, SHL, SHR;
    
    protected static Instruction RRFromOperation(Operation operation) {
        Instruction instruction = null;
        switch (operation) {
            case SET -> instruction = Instruction.MOV_RR;
            case ADD -> instruction = Instruction.ADD_RR;
            case SUB -> instruction = Instruction.SUB_RR;
            case MUL -> instruction = Instruction.MUL_RR;
            case DIV -> instruction = Instruction.DIV_RR;
            case MOD -> instruction = Instruction.MOD_RR;
            case EXP -> instruction = Instruction.EXP_RR;
            case AND -> instruction = Instruction.AND_RR;
            case OR -> instruction = Instruction.OR_RR;
            case XOR -> instruction = Instruction.XOR_RR;
            case SHL -> instruction = Instruction.SHL_RR;
            case SHR -> instruction = Instruction.SHR_RR;
        }
        return instruction;
    }
    protected static Instruction RIFromOperation(Operation operation) {
        Instruction instruction = null;
        switch (operation) {
            case SET -> instruction = Instruction.MOV_RI;
            case ADD -> instruction = Instruction.ADD_RI;
            case SUB -> instruction = Instruction.SUB_RI;
            case MUL -> instruction = Instruction.MUL_RI;
            case DIV -> instruction = Instruction.DIV_RI;
            case MOD -> instruction = Instruction.MOD_RI;
            case EXP -> instruction = Instruction.EXP_RI;
            case AND -> instruction = Instruction.AND_RI;
            case OR -> instruction = Instruction.OR_RI;
            case XOR -> instruction = Instruction.XOR_RI;
            case SHL -> instruction = Instruction.SHL_RI;
            case SHR -> instruction = Instruction.SHR_RI;
        }
        return instruction;
    }
}
