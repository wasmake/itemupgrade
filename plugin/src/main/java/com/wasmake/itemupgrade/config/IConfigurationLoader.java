package com.wasmake.itemupgrade.config;

public interface IConfigurationLoader {
    <T> void save(T config, String path);
    void saveDefaults(Class clazz, String path);
    <T extends AbstractConfiguration> T load(Class<T> clazz, String path);

    <T> T updateConfig(Class<T> clazz, String path);
}
