package com.example.gymcrm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.stream.IntStream;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.example.gymcrm.controller") // optional if already scanned
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        IntStream.range(0, converters.size())
                .filter(i -> converters.get(i) instanceof MappingJackson2HttpMessageConverter)
                .forEach(i -> converters.set(i, new MappingJackson2HttpMessageConverter(objectMapper)));
    }
}