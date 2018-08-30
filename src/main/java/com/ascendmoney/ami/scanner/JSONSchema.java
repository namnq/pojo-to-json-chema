package com.ascendmoney.ami.scanner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import org.reflections.Reflections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;

public class JSONSchema {
    public static void main(String[] args) throws Exception {
        genJsonSchema();
    }

    private static String getJsonSchema(Class cl) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
        JsonSchema schema = schemaGen.generateSchema(cl);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
    }

    public static void genJsonSchema(String packageName) throws Exception {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(ToJsonSchema.class);
        for (Class cl : annotatedClasses) {
            ToJsonSchema schema = (ToJsonSchema) cl.getDeclaredAnnotation(ToJsonSchema.class);
            if (schema == null) {
                continue;
            }
            writeFile(schema.path(), cl.getSimpleName() + ".txt", getJsonSchema(cl));
        }
    }

    public static void genJsonSchema() throws Exception {
        genJsonSchema("com.ascendmoney");
    }

    private static void writeFile(String directoryName, String fileName, String value) {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdir();
        }
        if (directoryName.endsWith("/")) {
            directoryName = directoryName.substring(0, directoryName.length() - 1);
        }
        File file = new File(directoryName + "/" + fileName);
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(value);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
