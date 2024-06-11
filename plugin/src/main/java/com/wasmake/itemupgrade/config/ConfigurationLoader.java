package com.wasmake.itemupgrade.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.wasmake.itemupgrade.config.annotations.Config;
import com.wasmake.itemupgrade.config.annotations.comment.ConfigCommentProcessor;
import com.wasmake.itemupgrade.config.exceptions.ConfigurationParsingException;
import com.wasmake.itemupgrade.config.mapper.CustomSpigotMapper;
import com.wasmake.itemupgrade.utils.ItemStackSerializer;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationLoader implements IConfigurationLoader {
    private final ObjectMapper mapper;
    private final JsonFactory factory = new YAMLFactory();

    public ConfigurationLoader() {
        mapper = YAMLMapper.builder().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER).build();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
        //mapper.registerModule(new GuavaModule());
        mapper.registerModule(new AfterburnerModule());
        mapper.registerModule(new ParameterNamesModule());
        mapper.registerModule(new Jdk8Module());
        //mapper.registerModule(new CustomSpigotMapper());

        SimpleModule module = new SimpleModule();
        module.addSerializer(ItemStack.class, new ItemStackSerializer());
        module.addDeserializer(ItemStack.class, new ItemStackSerializer.ItemStackDeserializer());
        mapper.registerModule(module);

        mapper.setPropertyNamingStrategy(new PropertyNamingStrategy());
        // Allow raw values in YAML
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true);

        // Allow YAML in factory
        factory.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        factory.configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true);
        factory.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        // Remove line --- from the beginning of the file
        factory.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
    }

    public <T extends AbstractConfiguration> T loadOrCreate(Class<T> config, String path) {
        // Check if folder exists, if not create it
        if (!Files.exists(Paths.get(path))) {
            try {
                Files.createDirectories(Paths.get(path));
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory " + path);
            }
        }
        // Check if file exists
        Config configAnnotation = config.getAnnotation(Config.class);
        if (configAnnotation == null) {
            throw new IllegalArgumentException("Class " + config.getName() + " is not annotated with @Configuration");
        } else {
            path += "/" + configAnnotation.fileName() + ".yml";
        }
        if (!Files.exists(Paths.get(path))) {
            // Create file
            saveDefaults(config, path);
        }
        return load(config, path);
    }

    public <T> void save(T config, String file) {
        try {
            new File(file).createNewFile();
        } catch (IOException e) {
            System.out.println("Could not create file " + file);
        }
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(file))) {
            JsonNode node = mapper.valueToTree(config);
            JsonGenerator generator = factory.createGenerator(outputStream);
            mapper.writeTree(generator, node);

            // Insert comments to the file, transform yml file to string, insert comments, write to file
            ConfigCommentProcessor.insertComments((AbstractConfiguration) config, file);
        } catch (JsonProcessingException e) {
            throw ConfigurationParsingException.builder("Json processing exception")
                    .setDetail(e.getMessage())
                    .setLocation(e.getLocation())
                    .setCause(e)
                    .build(file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveDefaults(Class clazz, String path) {
        System.out.println("Creating default configuration file " + path);
        try {
            AbstractConfiguration config = (AbstractConfiguration) clazz.newInstance();
            config.setLoader(this);
            ConfigCommentProcessor.process(config);
            save(config, path);
        } catch (InstantiationException | IllegalAccessException e) {
            System.out.println("Failed to save defaults for " + clazz.getName() + e);
        }
    }

    @Override
    public <T extends AbstractConfiguration> T load(Class<T> clazz, String path) {
        try (InputStream inputStream = new FileInputStream(path)) {
            // Check JsonNode readTree to collect all errors
            JsonParser parser = createParser(inputStream);
            JsonNode node = mapper.readTree(parser);
            if (node == null) {
                throw new RuntimeException("Failed to load configuration file " + path);
            }
            T config = mapper.readValue(new TreeTraversingParser(node), clazz);
            config.setLoader(this);
            config.setFilePath(path);
            return config;
        } catch (UnrecognizedPropertyException e) {
            final List<String> properties = e.getKnownPropertyIds().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
            throw ConfigurationParsingException.builder("Unrecognized field")
                    .setFieldPath(e.getPath())
                    .setLocation(e.getLocation())
                    .addSuggestions(properties)
                    .setSuggestionBase(e.getPropertyName())
                    .setCause(e)
                    .build(path);
        } catch (InvalidFormatException e) {
            final String sourceType = e.getValue().getClass().getSimpleName();
            final String targetType = e.getTargetType().getSimpleName();
            throw ConfigurationParsingException.builder("Incorrect type of value")
                    .setDetail("is of type: " + sourceType + ", expected: " + targetType)
                    .setLocation(e.getLocation())
                    .setFieldPath(e.getPath())
                    .setCause(e)
                    .build(path);
        } catch (JsonMappingException e) {
            throw ConfigurationParsingException.builder("Failed to parse configuration")
                    .setDetail(e.getMessage())
                    .setFieldPath(e.getPath())
                    .setLocation(e.getLocation())
                    .setCause(e)
                    .build(path);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot load config", ex);
        }
    }

    @Override
    public <T> T updateConfig(Class<T> clazz, String path) {
        return null;
    }

    private JsonParser createParser(InputStream inputStream) throws IOException {
        return factory.createParser(inputStream);
    }
}
