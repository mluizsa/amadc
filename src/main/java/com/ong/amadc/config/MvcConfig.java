package com.ong.amadc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${amadc.upload.diretorio:./uploads}")
    private String diretorioUpload;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path pastaUpload = Paths.get(diretorioUpload).toAbsolutePath();

        // Permite acessar as fotos via http://localhost:8080/uploads/NOME_DO_ARQUIVO.jpg
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + pastaUpload.toString() + "/");
    }
}