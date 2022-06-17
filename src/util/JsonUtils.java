package util;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import config.AppConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.DefectStatusRecord;
import model.OsType;
import model.ProjectType;
import model.TestCase;
import model.TestDevice;
import model.TestProject;
import model.User;
import model.UserRole;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//</editor-fold>

/**
 *
 * @author takacs.gergely
 */
public class JsonUtils {

//<editor-fold defaultstate="collapsed" desc="TEST PROJECT">
    public static void saveTestProjectJSON(TestProject tp) {
        JSONObject jobj = new JSONObject();
        mapProjectToJSON(tp, jobj);
        writeJSONToFile(jobj, tp.getProjectFolderPath());
    }

    static void writeJSONToFile(JSONObject jobj, String projectFolderPath) {
        try {
            String path = projectFolderPath + AppConfig.PROJECT_PROPERTIES_JSON_FILENAME;
            try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(path), "UTF-8")) {
                osw.write(formatJSONString(jobj));
                osw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void writeBugListJSONArrsyToFile(JSONArray jobj, String defectLogFolderLocation) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(defectLogFolderLocation), "UTF-8");
            osw.write(formatJSONString(jobj));
            osw.flush();
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    static JSONObject getTestProjectJSON(TestProject tp) {
        JSONObject jsonObject = null;
        try {
            JSONParser parser = new JSONParser();

            jsonObject = (JSONObject) parser.parse(new FileReader(tp.getProjectFolderPath() + AppConfig.PROJECT_PROPERTIES_JSON_FILENAME));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ParseException ex) {
            Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonObject;
    }

    static JSONObject getTestProjectJSON(String folderPath) {
        JSONObject jsonObject = null;
        File file = new File(folderPath + AppConfig.PROJECT_PROPERTIES_JSON_FILENAME);

        JSONParser parser = new JSONParser();
        try {
            jsonObject = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException | ParseException ex) {
            Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jsonObject;
    }

    public static TestProject getTestProjectFromJSON(String folderPath) {
        JSONObject obj = getTestProjectJSON(folderPath);
        TestProject tp = new TestProject(
                (String) obj.get("projectName"),
                getLocalDateFromJSONDate((String) obj.get("DATE_STARTED")),
                null);
        tp.setProjectFolderPath((String) obj.get("ProjectFolderPath"));
        setDeadline(tp, obj);
        tp.setAppLocation((String) obj.get("appLocation"));
        tp.setCodeNAme((String) obj.get("codeNAme"));
        tp.addNewVersionNumber((String) obj.get("versionNumber"));
        tp.setProjectType(ProjectType.getProjectTypeBasedOnName((String) obj.get("projectType")));
        tp.setBrowserTestedOn((String) obj.get("browserOrOperationSystemTestedOn"));
        return tp;
    }

    static void mapProjectToJSON(TestProject tp, JSONObject jobj) {
        jobj.put("projectName", tp.getProjectName());
        jobj.put("codeNAme", tp.getCodeNAme());
        jobj.put("ProjectFolderPath", tp.getProjectFolderPath());
        jobj.put("DATE_STARTED", localDateToJSON(tp.getDateStarted()));
        jobj.put("deadline", localDateToJSON(tp.getProjectDeadline()));
        jobj.put("projectType", tp.getProjectType().getProjectTypeName());
        jobj.put("versionNumber", tp.getLatestVersionNumber());
        jobj.put("appLocation", tp.getAppLocation());
        jobj.put("browserOrOperationSystemTestedOn", tp.getBrowserTestedOn());
    }

    public static String getProjectPathPropertyFromJson(String folderPath) {
        JSONObject obj = getTestProjectJSON(folderPath);
        return (String) obj.get("ProjectFolderPath");
    }

    public static void modifyJsonProjectPathProperty(String validProjectPath) {
        JSONObject jobj = getTestProjectJSON(validProjectPath);
        jobj.put("ProjectFolderPath", validProjectPath);        
        writeJSONToFile(jobj, validProjectPath);
    }

    private static void setDeadline(TestProject tp, JSONObject obj) {
        if (obj.containsKey("deadline") && null != obj.get("deadline")) {
            tp.setProjectDeadline(getLocalDateFromJSONDate((String) obj.get("deadline")));
        }
    }

    public static String getProjectNameFromProjectJson(String projectPath) {
        String jsonPath = projectPath + File.separator;
        JSONObject obj = getTestProjectJSON(jsonPath);

        if (null != obj) {
            return (String) obj.get("projectName");
        } else {
            return null;
        }
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="BUG LIST">
    public static void saveProjectBugListJSON(TestProject tp) {
        List<TestCase> failedTestCases = tp.getFailedTestcases();
        JSONArray jSONArray = createBugListJSONArray(failedTestCases);

        writeBugListJSONArrsyToFile(jSONArray, tp.getDefectLogFolderLocation());
    }

    private static JSONArray createBugListJSONArray(List<TestCase> failedTestCases) {
        JSONArray bugList = new JSONArray();

        for (TestCase ftc : failedTestCases) {
            JSONObject bugJSON = new JSONObject();
            bugJSON.put("testCaseId", ftc.getTestCaseId());
            JSONArray statushistoryJSON = getHistory(ftc.getDefectLog().getDefectStatusHistory());
            bugJSON.put("defectStatusHistory", statushistoryJSON);
        }

        return bugList;
    }

    private static JSONArray getHistory(List<DefectStatusRecord> defectStatusHistory) {
        JSONArray jSONArray = new JSONArray();
        for (DefectStatusRecord record : defectStatusHistory) {
            JSONObject obj = mapBugRecordToJSON(record);
            jSONArray.add(obj);
        }
        return jSONArray;

    }

    private static JSONObject mapBugRecordToJSON(DefectStatusRecord record) {
        JSONObject obj = new JSONObject();
        obj.put("date", localDateToJSON(record.getDate()));
        obj.put("defectStatus", record.getDefectStatus().getName());
        obj.put("versionNumber", record.getProductVersionNumber());

        return obj;
    }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="USERS">

    public static void saveSetofUsersToJSON(Set<User> users) {
        JSONArray jsonArray = new JSONArray();

        for (User user : users) {
            JSONObject jobj = new JSONObject();
            mapUserToJSON(user, jobj);
            jsonArray.add(jobj);
        }

        writeUsersJSONArrayToFile(jsonArray);
        System.out.println("Users saved");
    }

    static void mapUserToJSON(User user, JSONObject jobj) {
        jobj.put("userKey", user.getUserKey());
        jobj.put("fullname", user.getFullname());
        jobj.put("role", user.getRole().getName());
    }

    public static Set<User> getUsersFromJson() {
        Set<User> userList = new LinkedHashSet<>();
        JSONArray jArray = null;
        try {
            JSONParser parser = new JSONParser();
            String usersJsonPath
                    = app.Main.controller
                            .getTestCenter()
                            .getFolderStructure()
                            .getTestCenterLocation()
                    + AppConfig.USER_LIST_JSON_FILE_NAME;

            jArray = (JSONArray) parser.parse(new InputStreamReader(new FileInputStream(usersJsonPath), "UTF-8"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ParseException ex) {
            Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Object object : jArray) {
            JSONObject jobj = (JSONObject) object;
            User u = new User(
                    (String) jobj.get("userKey"),
                    (String) jobj.get("fullname"),
                    UserRole.getUserRoleByName((String) jobj.get("role")));
            userList.add(u);
        }
        return userList;
    }

    static void writeUsersJSONArrayToFile(JSONArray jArray) {
        try {
            String savePath = app.Main.controller.getTestCenter().getFolderStructure().getTestCenterLocation()
                    + AppConfig.USER_LIST_JSON_FILE_NAME;

            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(savePath), "UTF-8");
            String formattedJsonString = jArray.toJSONString().replaceAll("},", ("}," + System.lineSeparator()));
            osw.write(formattedJsonString);
            osw.flush();
            osw.close();
        } catch (IOException e) {
        }
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="DEVICES">
    public static void saveDevicesToJSON(List<TestDevice> devices) {
        JSONArray jsonArray = new JSONArray();

        for (TestDevice device : devices) {
            JSONObject jobj = new JSONObject();
            mapDeviceToJSON(device, jobj);
            jsonArray.add(jobj);
        }

        writeDevicesJSONArrayToFile(jsonArray);
        System.out.println("Devices list saved");
    }

    static void mapDeviceToJSON(TestDevice device, JSONObject jobj) {
        System.out.println("Device: " + device.toString());
        jobj.put("name", device.getName());
        jobj.put("brand", device.getBrand());
        jobj.put("model", device.getModel());
        jobj.put("osVersion", device.getOsVersion());
        jobj.put("deviceType", device.getOsType().getName());
        jobj.put("cpu", device.getCpu());
        jobj.put("ramSize", String.valueOf(device.getRamSize()));
        jobj.put("notes", device.getNotes());
    }

    public static List<TestDevice> getTestDevicesFromJson() {
        List<TestDevice> devices = new ArrayList<>();
        JSONArray devicesjsonArray = getDevicesJsonArray(getDevicesJsonPath());

        for (Object object : devicesjsonArray) {
            JSONObject jobj = (JSONObject) object;
            TestDevice device = new TestDevice();
            device.setName(setIfPresent(jobj, "name"));
            device.setBrand(setIfPresent(jobj, "brand"));
            device.setModel(setIfPresent(jobj, "model"));
            device.setOsType(OsType.getOsTypeByName((String) jobj.get("deviceType")));
            device.setOsVersion(setIfPresent(jobj, "osVersion"));
            device.setCpu(setIfPresent(jobj, "cpu"));
            device.setRamSize(setRamSizeIfPresent(jobj, "ramSize"));
            device.setNotes(setIfPresent(jobj, "notes"));
            devices.add(device);
        }
        return devices;
    }

    static void writeDevicesJSONArrayToFile(JSONArray jArray) {
        try {
            String savePath = app.Main.controller.getTestCenter().getFolderStructure().getTestCenterLocation()
                    + AppConfig.TEST_DEVICES_JSON_FILENAME;

            try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(savePath), "UTF-8")) {
                String formattedJsonString = jArray.toJSONString().replaceAll("},", ("}," + System.lineSeparator()));
                osw.write(formattedJsonString);
                osw.flush();
            }
        } catch (IOException e) {
        }
    }

    private static String setIfPresent(JSONObject jobj, String attribute) {
        if (null != jobj.get(attribute)) {
            return (String) jobj.get(attribute);
        } else {
            return "";
        }
    }

    private static int setRamSizeIfPresent(JSONObject jobj, String ramSize) {
        if (null != jobj.get(ramSize)) {
            return Integer.parseInt((String) jobj.get(ramSize));
        } else {
            return 1;
        }
    }

    private static String getDevicesJsonPath() {
        return app.Main.controller
                .getTestCenter()
                .getFolderStructure()
                .getTestCenterLocation()
                + AppConfig.TEST_DEVICES_JSON_FILENAME;
    }

    private static JSONArray getDevicesJsonArray(String devicesJsonPath) {
        JSONParser parser = new JSONParser();
        JSONArray jArr = null;
        try {
            jArr = (JSONArray) parser.parse(new InputStreamReader(new FileInputStream(devicesJsonPath), "UTF-8"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ParseException ex) {
            Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jArr;

    }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="UTILS">

    static LocalDate getLocalDateFromJSONDate(String jsonDate) {
        LocalDate ld = LocalDate.parse(jsonDate.replaceAll("_", "-"));
        return ld;
    }

    static String localDateToJSON(LocalDate localDate) {
        String str = localDate.toString();
        return str.replaceAll("-", "_");
    }

    static String formatJSONString(JSONObject jobj) {
        StringBuilder sb = new StringBuilder();
        sb.append('"').append(',').append(System.lineSeparator()).append('"');
        return jobj.toJSONString().replaceAll("\",\"", sb.toString());
    }

    static String formatJSONString(JSONArray array) {
        StringBuilder sb = new StringBuilder();
        sb.append('"').append(',').append(System.lineSeparator()).append('"');
        return array.toJSONString().replaceAll("\",\"", sb.toString());
    }

//</editor-fold>
}
