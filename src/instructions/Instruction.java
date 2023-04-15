package instructions;

public enum Instruction {
    /* REGISTER DESIGNATIONS
    PROGRAM
        GEP (0) - General Purpose (Used as first parameter of floating point operations)
        FLP (1) - Floating Purpose (Used as second parameter of floating point operations)
        DAT (2) - Pointer Data
        RTN (3) - Return Value

    SYSTEM
        INS (4) - Instruction Pointer
        MEM (5) - Memory Read Pointer
        HEP (6) - Initial Heap Pointer
        STH (7) - Stack Head
        STB (8) - Stack Base
    */ 

    // Arithmatic Operations
    ADD_RR(0x00, 2),
    ADD_RM(0x01, 2),
    ADD_RI(0x02, 2),
    FADD(0x03, 0),

    SUB_RR(0x04, 2),
    SUB_RM(0x05, 2),
    SUB_RI(0x06, 2),
    FSUB(0x07, 0),

    MUL_RR(0x08, 2),
    MUL_RM(0x09, 2),
    MUL_RI(0x0a, 2),
    FMUL(0x0b, 0),

    DIV_RR(0x0c, 2),
    DIV_RM(0x0d, 2),
    DIV_RI(0x0e, 2),
    FDIV(0x0f, 0),

    MOD_RR(0x10, 2),
    MOD_RM(0x11, 2),
    MOD_RI(0x12, 2),
    FMOD(0x13, 0),

    EXP_RR(0x14, 2),
    EXP_RM(0x15, 2),
    EXP_RI(0x16, 2),
    FEXP(0x17, 0),

    // Bitwise Operations
    AND_RR(0x18, 2),
    AND_RM(0x19, 2),
    AND_RI(0x1a, 2),

    OR_RR(0x1b, 2),
    OR_RM(0x1c, 2),
    OR_RI(0x1d, 2),

    XOR_RR(0x1e, 2),
    XOR_RM(0x1f, 2),
    XOR_RI(0x20, 2),

    SHL_RR(0x21, 2),
    SHL_RM(0x22, 2),
    SHL_RI(0x23, 2),

    SHR_RR(0x24, 2),
    SHR_RM(0x25, 2),
    SHR_RI(0x26, 2),
    
    // Memory Operations
    MOV_RR(0x30, 2),
    MOV_RP(0x31, 2), // Moves a value into a register given a pointer in a different specified register
    MOV_RM(0x32, 2),
    MOV_RI(0x33, 2),

    PUT_MR(0x34, 2),
    PUT_PR(0x35, 2), // Moves a value into a back into memory given a pointer in a specified register
    PUT_PI(0x36, 2),

    PUSH(0x36, 1),
    POP(0x37, 1),

    // Sytem Flag Operations
    SFW(0x40, 0),
    SFL(0x41, 0),
    SFE(0x42, 0),

    // Jump Operations
    JMP(0x50, 1),
    JMPZ_R(0x51, 2),
    JMPZ_M(0x52, 2),
    FJMPZ(0x53, 1),
    
    JMPE_RR(0x54, 3),
    JMPE_RM(0x55, 3),
    JMPE_RI(0x56, 3),
    FJMPE(0x57, 1),

    JPML_RR(0x58, 3),
    JMPL_RM(0x59, 3),
    JMPL_RI(0x5a, 3),
    FJMPL(0x5b, 1),

    JMPLE_RR(0x5c, 3),
    JMPLE_RM(0x5d, 3),
    JMPLE_RI(0x5e, 3),
    FJMPLE(0x5f, 1),

    JMPGE_RR(0x60, 3),
    JMPGE_RM(0x61, 3),
    JMPGE_RI(0x62, 3),
    FJMPGE(0x63, 1),

    JMPG_RR(0x64, 3),
    JMPG_RM(0x65, 3),
    JMPG_RI(0x66, 3),
    FJMPG(0x67, 1),

    // Long Jump Operations
    LMP(0x68, 1),
    LMPZ_R(0x69, 2),
    LMPZ_M(0x6a, 2),
    FLMPZ(0x6b, 1),
    
    LMPE_RR(0x6c, 3),
    LMPE_RM(0x6d, 3),
    LMPE_RI(0x6e, 3),
    FLMPE(0x6f, 1),

    LPML_RR(0x70, 3),
    LMPL_RM(0x71, 3),
    LMPL_RI(0x72, 3),
    FLMPL(0x73, 1),

    LMPLE_RR(0x74, 3),
    LMPLE_RM(0x75, 3),
    LMPLE_RI(0x76, 3),
    FLMPLE(0x78, 1),

    LMPGE_RR(0x79, 3),
    LMPGE_RM(0x7a, 3),
    LMPGE_RI(0x7b, 3),
    FLMPGE(0x7c, 1),

    LMPG_RR(0x7d, 3),
    LMPG_RM(0x7e, 3),
    LMPG_RI(0x7f, 3),
    FLMPG(0x80, 1),
    
    // System Operatoins
    EXIT(0xf4, 0),
    ;
    public final int opCode;
    public final int paramValues;

    private Instruction(int opCode, int params) {
        this.opCode = opCode;
        this.paramValues = params;
    }

    public static Instruction instructionFromOpCode(int opCode) {
        for (Instruction instruction : values()) {
            if (instruction.opCode == opCode) return instruction;
        }
        return null;
    }
}
