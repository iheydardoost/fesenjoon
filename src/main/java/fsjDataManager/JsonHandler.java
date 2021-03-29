package fsjDataManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class JsonHandler {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public JsonHandler() {
    }

    private static void InitObjectMapper(){
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void jsonWrite(String address,Object value){
        try {
            File destinationFile = new File(address);
            if(!destinationFile.isFile()) warn();
            objectMapper.writeValue(destinationFile,value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void jsonAppend(String address,Object value){
        try {
            File destinationFile = new File(address);
            if(!destinationFile.isFile()) warn();
            objectMapper.writerWithDefaultPrettyPrinter().writeValuesAsArray(destinationFile).write(value);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
