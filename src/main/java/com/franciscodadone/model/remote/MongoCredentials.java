package com.franciscodadone.model.remote;

import org.yaml.snakeyaml.Yaml;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MongoCredentials {

    public static Map<String, String> getCredentials() {
        String filePath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/ControlStock/database/mongoCredentials.yml";
        File file = new File(filePath);
        boolean isNew = false;

        if(!file.exists()) {
            try {
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
}
