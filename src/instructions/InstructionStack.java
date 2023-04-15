package instructions;

import java.util.ArrayList;
import java.util.LinkedList;

public class InstructionStack {
    private LinkedList<Integer> instructionStack;

    public InstructionStack() {
        this.instructionStack = new LinkedList<>();
    }

    public void push(int instruction) {
        this.instructionStack.add(instruction);
    }

    public void push(int... instructions) {
        for (int instruction : instructions) {
            this.instructionStack.add(instruction);
        }
    }

    public void append(InstructionStack instructionStack) {
        for (int instruction : instructionStack.getStack()) {
            this.instructionStack.add(instruction);
        }
    }

    public void cleanup() {
        ArrayList<Integer> instructionLocations = new ArrayList<>();

        for (int i = 0; i < this.instructionStack.size(); i++) {
            instructionLocations.add(i);
            i += Instruction.instructionFromOpCode(this.instructionStack.get(i)).paramValues;
        }


        // Redundant expression elimination
        Instruction sizeFlag = Instruction.SFE;
        for (int locationIndex = 0; locationIndex < instructionLocations.size(); locationIndex++) {
            boolean eliminate = false;

            Instruction instruction = Instruction.instructionFromOpCode(this.instructionStack.get(locationIndex));
            int location = instructionLocations.get(locationIndex);

            switch (instruction) {
                // Useless arithmetic
                case ADD_RI, SUB_RI, SHL_RI, SHR_RI -> {
                    if (this.instructionStack.get(location + 2) == 0) eliminate = true;
                }
                case MUL_RI, DIV_RI, EXP_RI -> {
                    if (this.instructionStack.get(location + 2) == 1) eliminate = true;
                }
                case PUSH, POP -> {
                    if (this.instructionStack.get(location + 1) == 0) eliminate = true;
                }

                // Repeated size flag decleration
                case SFW, SFL, SFE -> {
                    if (sizeFlag.equals(instruction)) {
                        eliminate = true;
                    } else {
                        sizeFlag = instruction;
                    }
                }
                default -> {}
            }

            if (eliminate) {
                for (int count = 0; count < instruction.paramValues + 1; count++) this.instructionStack.remove(location);
                for (int futureInstruction = locationIndex + 1; futureInstruction < instructionLocations.size(); futureInstruction++) {
                    instructionLocations.set(futureInstruction, instructionLocations.get(futureInstruction) - instruction.paramValues - 1);
                }
                instructionLocations.remove(locationIndex); locationIndex--;
            }
        }
    }

    private class Node {
        public boolean designation;
        public Node parent;
        public Node[] children;
        public int frequency;

        public Node(int frequency) {
            this.frequency = frequency;
        }

        @Override
        public String toString() {
            return "f:" + this.frequency;
        }
    }
    private class Leaf extends Node {
        public final int key;

        public Leaf(int key) {
            super(1);
            this.key = key;
        }

        @Override
        public String toString() {
            return "f:" + this.frequency + " k:" + this.key;
        }
    }
    private class MappedLeaf {
        public final int key;
        public final String map;

        public MappedLeaf(Leaf leaf) {
            this.key = leaf.key;
            this.map = MappedLeaf.getMap(leaf);
        }

        private static String getMap(Node node) {
            if (node.parent == null) return "";
            if (node.designation) {
                return "1" + getMap(node.parent);
            } else {
                return "0" + getMap(node.parent);
            }
        }
    }
    private static String getTree(Node node) {
        if (node.children == null) {
            Leaf castedNode = (Leaf) node;
            String bitstring = Integer.toBinaryString(castedNode.key);

            for (int i = 0; i < 8 - bitstring.length(); i++) bitstring = "0" + bitstring;

            return "1" + bitstring;
        } else {
            return "0" + getTree(node.children[0]) + getTree(node.children[1]);
        }
    }
    public void compress() {
        ArrayList<Leaf> leafs = new ArrayList<>();

        for (int instruction : this.instructionStack) {
            boolean matchingLeaf = false;
            for (Leaf leaf : leafs) {
                if (leaf.key == instruction) {
                    leaf.frequency++;
                    matchingLeaf = true;
                    break;
                }
            }
            if (!matchingLeaf) leafs.add(new Leaf(instruction));
        }
        leafs.sort((l1, l2) -> Integer.compare(l1.frequency, l2.frequency));

        ArrayList<Node> treeBuilder = new ArrayList<>();
        treeBuilder.addAll(leafs);
        while (treeBuilder.size() > 1) {
            Node fNode = treeBuilder.get(0);
            Node sNode = treeBuilder.get(1);

            Node parentNode = new Node(fNode.frequency + sNode.frequency);
            parentNode.children = new Node[]{fNode, sNode};
            fNode.parent = parentNode; fNode.designation = false;
            sNode.parent = parentNode; sNode.designation = true;

            treeBuilder.remove(fNode);
            treeBuilder.remove(sNode);

            boolean inserted = false;
            for (int i = 0; i < treeBuilder.size(); i++) {
                if (parentNode.frequency < treeBuilder.get(i).frequency) {
                    treeBuilder.add(i, parentNode);
                    inserted = true;
                    break;
                }
            }
            if (!inserted) treeBuilder.add(parentNode);
        }
        
        MappedLeaf[] leafMap = new MappedLeaf[leafs.size()];
        for (int i = 0; i < leafMap.length; i++) {
            leafMap[i] = new MappedLeaf(leafs.get(i));
        }

        String bitStream = getTree(treeBuilder.get(0));
        for (int instruction : this.instructionStack) {
            for (MappedLeaf mappedLeaf : leafMap) {
                if (mappedLeaf.key == instruction) {
                    bitStream += mappedLeaf.map;
                    break;
                }
            }
        }
        
        int[] instructionBytes = new int[(int) Math.ceil(bitStream.length() / 8f)];
        for (int i = 0; i < bitStream.length(); i+=8) {
            String byteString = bitStream.substring(i, Math.min(i + 8, bitStream.length()));
            instructionBytes[i / 8] = Integer.parseInt(byteString, 2);
        }
        this.instructionStack.clear();
        for (int instruction : instructionBytes) {
            instructionStack.add(instruction);
        }
    }

    @SuppressWarnings("unchecked")
    public LinkedList<Integer> getStack() {
        return (LinkedList<Integer>) this.instructionStack.clone();
    }

    public int[] getRaw() {
        int[] raw = new int[this.instructionStack.size()];
        for (int i = 0; i < this.instructionStack.size(); i++) {
            raw[i] = this.instructionStack.get(i);
        }
        return raw;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < this.instructionStack.size(); i++) {
            stringBuilder.append(Integer.toHexString(this.instructionStack.get(i)));
            if (i != this.instructionStack.size() - 1) stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }
}
