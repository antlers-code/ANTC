import data.management.RedeclaredException;
import data.management.StackFrame;
import data.types.numeral.Byte;
import data.types.numeral.InoperableDataTypes;
import data.types.numeral.Operation;
import data.types.numeral.Short;
import finalization.BlueprintGenerator;
import instructions.InstructionStack;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

public class Main {
    public static void main(String[] args) throws RedeclaredException, InoperableDataTypes {
        StackFrame stackFrame = new StackFrame();
        InstructionStack instructionStack = new InstructionStack();

        Short testShort = new Short(stackFrame, "short", instructionStack);
        Byte testByte = new Byte(stackFrame, "byte", instructionStack);

        testShort.operate(testByte, Operation.ADD, instructionStack);
        testShort.operate(12, Operation.SUB, instructionStack);

        // Postprocessing

        int rawSize = instructionStack.getStack().size();
        instructionStack.cleanup();
        int cleanupSize = instructionStack.getStack().size();
        
        float efficiency = 100f * cleanupSize / rawSize;
        String cleanupReport;
        if (efficiency > 100) {
            cleanupReport = "\u001B[31m";
        } else if (efficiency > 90) {
            cleanupReport = "\u001B[33m";
        } else {
            cleanupReport = "\u001B[32m";
        }
        cleanupReport += "(" + (Math.round(efficiency * 10) / 10) + "%)\u001B[0m";

        instructionStack.compress();
        int size = instructionStack.getStack().size();

        efficiency = 100f * size / cleanupSize;
        String compressionReport;
        if (efficiency > 100) {
            compressionReport = "\u001B[31m";
        } else if (efficiency > 90) {
            compressionReport = "\u001B[33m";
        } else {
            compressionReport = "\u001B[32m";
        }
        compressionReport += "(" + (Math.round(efficiency * 10) / 10) + "%)\u001B[0m";

        // Exporting
        int driveSize;
        if (size <= 16) {
            driveSize = 16;
        } else if (size <= 64) {
            driveSize = 64;
        } else if (size <= 256) {
            driveSize = 256;
        } else {
            driveSize = 1024;
        }

        String bps = BlueprintGenerator.getBlueprintString(instructionStack.getRaw(), driveSize);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(bps), null);

        System.out.println();
        System.out.println("\u001B[0mBlueprint String (" + driveSize + " byte drive): \u001B[36m" + bps.substring(0, 21) + "...\u001B[33m (copied to clipboard)\u001B[0m");
        System.out.println("Machine Code: \u001B[36m" + instructionStack + "\u001B[0m");
        System.out.println();
        System.out.println("\u001B[0mCompilation Information:");
        System.out.println("\tRaw\t\t\u001B[33m" + rawSize + " \u001B[0mbytes");
        System.out.println("\tCleaned\t\t\u001B[33m" + cleanupSize + " \u001B[0mbytes " + cleanupReport);
        System.out.println("\tCompressed\t\u001B[33m" + size + " \u001B[0mbytes " + compressionReport);
        System.out.println();
    }
}