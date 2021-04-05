package fsjDataManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

public class JsonHandler {
    public static ObjectMapper mapper = new ObjectMapper();

    public JsonHandler() {
    }

    public static void InitMapper(){
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(new JSR310Module());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

}
