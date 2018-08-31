package com.ascendmoney.ami.scanner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;

/**
 * @author nguyenquangnam
 *
 */
public class JSONSchema {
    public static void main(String[] args) throws Exception {
        genAllClassToJsonSchema("com.ascendmoney.ami.scanner", null);
    }

    private static String getJsonSchema(Class cl) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
        JsonSchema schema = schemaGen.generateSchema(cl);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
    }

    /**Need add annotation @ToJsonSchema to run
     * @param packageName
     * @throws Exception
     */
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

    /** 
     * Don't need annotation to run
     * @param packageName
     * @param path
     *            default /tmp/ascend
     * @throws Exception
     */
    public static void genAllClassToJsonSchema(String packageName, String path) throws Exception {
        // Reflections reflections = new Reflections(packageName);
        if (packageName == null || "".equals(packageName)) {
            packageName = "com.ascendmoney";
        }
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(
                        false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));
        Set<Class<? extends Object>> annotatedClasses = reflections.getSubTypesOf(Object.class);
        for (Class cl : annotatedClasses) {
            writeFile(path, cl.getSimpleName() + ".txt", getJsonSchema(cl));
        }
    }

    /**
     * Need add annotation @ToJsonSchema to run
     * @throws Exception
     */
    public static void genJsonSchema() throws Exception {
        genJsonSchema("com.ascendmoney");
    }

    private static void writeFile(String directoryName, String fileName, String value) {
        if (directoryName == null || "".equals(directoryName)) {
            directoryName = "/tmp/Ascend";
        }
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
        System.out.println("file writed: " + directoryName + "/" + fileName);
    }
}
