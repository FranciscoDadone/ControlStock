package com.franciscodadone.util;

import org.yaml.snakeyaml.Yaml;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private static Map<String, String> getConfiguration() {
        String filePath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/ControlStock/database/mongoCredentials.yml";
        File file = new File(filePath);
        boolean isNew = false;

        if(!file.exists()) {
            try {
                new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/ControlStock/database/").mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            isNew = true;
        }

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Yaml yaml = new Yaml();

        if(isNew) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("url", "");
            dataMap.put("password", "");
            dataMap.put("username", "");
            dataMap.put("database_file", "");
            dataMap.put("save_remote", "true");
            dataMap.put("remote_admin_terminal", "false");

            PrintWriter writer = null;
            try {
                writer = new PrintWriter(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            yaml.dump(dataMap, writer);
        }

        return yaml.load(inputStream);
    }

    public static String getUsername() {
        return getConfiguration().get("username");
    }

    public static String getPassword() {
        return getConfiguration().get("password");
    }

    public static String getUrl() {
        return getConfiguration().get("url");
    }

    public static String getDatabaseLocation() {
        return getConfiguration().get("database_file");
    }

    public static boolean getSaveRemote() {
        return Boolean.parseBoolean(getConfiguration().get("save_remote"));
    }

    public static boolean isRemoteAdminTerminal() {
        return Boolean.parseBoolean(getConfiguration().get("remote_admin_terminal"));
    }

}
