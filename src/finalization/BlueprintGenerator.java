package finalization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class BlueprintGenerator {
    private record Entity(int entity_number, String name, JSONObject position, JSONObject items) {
        public JSONObject map() {
            JSONObject dummyObject = new JSONObject();
            dummyObject.put("entity_number", entity_number);
            dummyObject.put("name", name);
            dummyObject.put("position", position);
            dummyObject.put("items", items);
            return dummyObject;
        }
    }
    private record Position(int x, int y) {
        public JSONObject map() {
            JSONObject dummyObject = new JSONObject();
            dummyObject.put("x", x);
            dummyObject.put("y", y);
            return dummyObject;
        }
    }

    private static int TOTAL_BEACONS = 0;

    public static String getBlueprintString(int[] instructionStream, int driveSize) {
        TOTAL_BEACONS = 0;
        JSONObject bufferJSON = new JSONObject();
        JSONObject blueprintJSON = new JSONObject();
        JSONArray entities = new JSONArray();

        double squareLog = Math.log(driveSize) / Math.log(4);
        int collumnHeight = (int) (4 * Math.pow(2, squareLog));
        driveSize *= 2;
        int[] placementPointer = {0, 0};
        for (int index = 0; index < driveSize; index++) {
            if (index / 2 < instructionStream.length) {
                int subinstruction = (instructionStream[index / 2] >> (4 * (index % 2))) % 16;
                entities.add(generateBeacon(placementPointer[0], placementPointer[1], subinstruction).map());
            } else {
                entities.add(generateBeacon(placementPointer[0], placementPointer[1], 0).map());
            }

            if ((index / collumnHeight) % 2 == 0) {
                if ((index + 1) % collumnHeight == 0) {
                    placementPointer[0] += 17;
                    continue;
                }
                placementPointer[1] -= 3;
                continue;
            }
            if ((index + 1) % collumnHeight == 0) {
                placementPointer[0] += 23;
                continue;
            }
            placementPointer[1] += 3;
        }
        blueprintJSON.put("entities", entities);
        blueprintJSON.put("item", "blueprint");
        blueprintJSON.put("version", 281479276658688l);
        blueprintJSON.put("label", "Compiled Script");

        bufferJSON.put("blueprint", blueprintJSON);
        String inflatedString = bufferJSON.toString();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DeflaterOutputStream dos = new DeflaterOutputStream(baos);

            //System.out.println(inflatedString);
            
            dos.write(inflatedString.getBytes());
            dos.flush();
            dos.close();

            byte[] outArray = baos.toByteArray();
            return "0" + Base64.getEncoder().encodeToString(outArray);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private static Entity generateBeacon(int x, int y, int halfByte) {
        Position entityPosition = new Position(x, y);
        
        JSONObject items = new JSONObject();
        switch (halfByte) {
            case 1 -> {
                items.put("speed-module", 1);
                items.put("speed-module-2", 1);
            }
            case 2 -> {
                items.put("speed-module", 1);
                items.put("speed-module-3", 1);
            }
            case 3 -> {
                items.put("speed-module", 1);
                items.put("effectivity-module", 1);
            }
            case 4 -> {
                items.put("speed-module", 1);
                items.put("effectivity-module-2", 1);
            }
            case 5 -> {
                items.put("speed-module", 1);
                items.put("effectivity-module-3", 1);
            }
            case 6 -> {
                items.put("speed-module-2", 1);
                items.put("speed-module-3", 1);
            }
            case 7 -> {
                items.put("speed-module-2", 1);
                items.put("effectivity-module", 1);
            }
            case 8 -> {
                items.put("speed-module-2", 1);
                items.put("effectivity-module-2", 1);
            }
            case 9 -> {
                items.put("speed-module-2", 1);
                items.put("effectivity-module-3", 1);
            }
            case 10 -> {
                items.put("speed-module-3", 1);
                items.put("effectivity-module", 1);
            }
            case 11 -> {
                items.put("speed-module-3", 1);
                items.put("effectivity-module-2", 1);
            }
            case 12 -> {
                items.put("speed-module-3", 1);
                items.put("effectivity-module-3", 1);
            }
            case 13 -> {
                items.put("effectivity-module", 1);
                items.put("effectivity-module-2", 1);
            }
            case 14 -> {
                items.put("effectivity-module", 1);
                items.put("effectivity-module-3", 1);
            }
            case 15 -> {
                items.put("effectivity-module-2", 1);
                items.put("effectivity-module-3", 1);
            }
        }

        TOTAL_BEACONS++;
        return new Entity(TOTAL_BEACONS, "beacon", entityPosition.map(), items);
    }
}
