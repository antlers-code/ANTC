package util;

public class Util {
    public static int immediateSize(int immediate) {
        double log2 = Math.log(immediate) / Math.log(2);
        int trueSize = (int) Math.ceil(log2) + 1;

        if (trueSize <= 8) {
            return 1;
        } else if (trueSize <= 16) {
            return 2;
        } else {
            return 4;
        }
    }

    public static int[] byteBreak(int immediate) {
        int size = immediateSize(immediate);
        int[] brokenBytes = new int[size];

        for (int i = 0; i < size; i++) {
            brokenBytes[0] = (int) ((immediate >> (i * 8)) % 256);
        }
        return brokenBytes;
    }
}
