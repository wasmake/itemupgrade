package com.wasmake.itemupgrade.config.annotations.comment;

import com.wasmake.itemupgrade.config.AbstractConfiguration;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ConfigCommentProcessor {

    public static void process(AbstractConfiguration abstractConfiguration){
        // Search for fields with @ConfigComment annotation
        // Add comments to the configuration object
        ConfigHeading heading = abstractConfiguration.getClass().getAnnotation(ConfigHeading.class);
        if(heading != null){
            abstractConfiguration.addHeading(heading.value());
        }
        for(Field field : abstractConfiguration.getClass().getDeclaredFields()){
            if(field.isAnnotationPresent(ConfigComment.class)){
                ConfigComment annotation = field.getAnnotation(ConfigComment.class);
                abstractConfiguration.addComment(field.getName().strip(), annotation.value());
            }
            // Check if objects also have comments inside their fields
            // Add comments to the configuration object
            innerFields(field, abstractConfiguration);
        }

    }

    public static void innerFields(Field parentField, AbstractConfiguration abstractConfiguration){
        parentField.setAccessible(true);
        if(!parentField.getType().isPrimitive() && !parentField.getType().equals(String.class) && !parentField.getType().equals(List.class) && !parentField.getType().equals(Map.class)){
            try {
                Object object = parentField.get(abstractConfiguration);
                if(object != null) {
                    for(Field field : object.getClass().getDeclaredFields()){
                        if(field.isAnnotationPresent(ConfigComment.class)){
                            ConfigComment annotation = field.getAnnotation(ConfigComment.class);
                            abstractConfiguration.addComment(field.getName().strip(), annotation.value());
                        }
                        innerFields(field, abstractConfiguration);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void insertComments(AbstractConfiguration abstractConfiguration, String filePath){
        // Read the file and get the YAML keys
        // Insert the comments before the YAML keys
        // Write the file

        File file = new File(filePath);
        if(!file.exists()){
            return;
        }

        // Get the file content as a string
        try {
            String fileContent = new String(Files.readAllBytes(file.toPath()));
            String[] lines = fileContent.split("\\n");
            List<String> newFileContent = new ArrayList<>();
            if (abstractConfiguration.getHeadings().length > 0) {
                String separator = "# ------------------------------------------------------------------------ #";
                newFileContent.add(separator);
                // Append # to the headings
                for (String heading : abstractConfiguration.getHeadings()) {
                    newFileContent.add("# " + center(heading, separator.length() - 4) + " #");
                }
                newFileContent.add(separator);
            }
            for(String line : lines){
                // Check if the line is a YAML key
                if(line.equals("---")){
                    continue;
                }
                if(line.contains(":")){
                    // Get the key
                    String key = (line.split(":")[0]).strip();
                    System.out.println("Key: " + key + " " + " Value: " + line.substring(line.indexOf(key)));
                    // Get the comments for the key
                    String[] comments = abstractConfiguration.getCommentsForField(key);
                    System.out.println("Comments: " + Arrays.toString(comments));
                    if(comments != null){
                        // Add the comments to the file content
                        for(String comment : comments){
                            // Get same identation as the key
                            String identation = line.substring(0, line.indexOf(key));
                            newFileContent.add(identation + "# " + comment);
                        }
                    }
                }
                newFileContent.add(line);
            }
            System.out.println("==================+");
            System.out.println(newFileContent.toString());
            System.out.println("==================+");
            // Write and wait for the file to be written
            FileOutputStream fileOutputStream = new FileOutputStream(file.getPath());
            FileDescriptor fileDescriptor = fileOutputStream.getFD();
            fileOutputStream.write(String.join(System.lineSeparator(), newFileContent).getBytes());
            fileOutputStream.flush();
            fileDescriptor.sync();
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String center(String text, int len){
        String out = String.format("%"+len+"s%s%"+len+"s", "",text,"");
        float mid = (out.length()/2);
        float start = mid - (len/2);
        float end = start + len;
        return out.substring((int)start, (int)end);
    }
}
