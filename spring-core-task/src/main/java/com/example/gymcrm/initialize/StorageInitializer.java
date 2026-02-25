package com.example.gymcrm.initialize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StorageInitializer {

    private static final Logger log = LoggerFactory.getLogger(StorageInitializer.class);

    @Value("${storage.init.file}")
    private Resource initFile;

    private final List<StoreInitializer> initializers;
    private Map<String, StoreInitializer> initializerMap;

    @Autowired
    public StorageInitializer(List<StoreInitializer> initializers) {
        this.initializers = initializers;
    }

    @PostConstruct
    public void initializeStorage() {
        initializerMap = initializers.stream()
                .collect(Collectors.toMap(StoreInitializer::scopeName, Function.identity()));

        if (!initFile.exists()) {
            log.warn("Initialization file not found: {}", initFile.getFilename());
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(initFile.getInputStream()))) {
            StoreInitializer currentInitializer = null;
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String sectionName = line.replace("[", "").replace("]", "");
                if (initializerMap.containsKey(sectionName)) {
                    currentInitializer = initializerMap.get(sectionName);
                } else if (currentInitializer != null) {
                    currentInitializer.parseLineAndSave(line);
                }
            }

            log.info("Storage initialized successfully from: {}", initFile.getFilename());
        } catch (Exception e) {
            log.error("Failed to read or parse initialization file: {}", initFile.getFilename(), e);
        }
    }
}