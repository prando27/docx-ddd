package com.example.docxddd.infrastructure.config.jackson;

import com.example.docxddd.domain.Cpf;
import com.example.docxddd.domain.Email;
import com.example.docxddd.domain.Name;
import com.example.docxddd.infrastructure.config.jackson.serializer.CpfSerializer;
import com.example.docxddd.infrastructure.config.jackson.serializer.EmailSerializer;
import com.example.docxddd.infrastructure.config.jackson.serializer.NameSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ObjectMapperConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return ObjectMapperConfig.createObjectMapper();
    }

    public static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

//        mapper.addMixIn(Document.class, DocumentMixIn.class);
//        mapper.addMixIn(DocumentTypeAttributes.class, DocumentTypeAttributesMixIn.class);

        mapper.registerModule(new Jdk8Module());

        SimpleModule valueObjectsModule = new SimpleModule();
        valueObjectsModule.addSerializer(Name.class, new NameSerializer());
        valueObjectsModule.addSerializer(Cpf.class, new CpfSerializer());
        valueObjectsModule.addSerializer(Email.class, new EmailSerializer());
        mapper.registerModule(valueObjectsModule);

        return mapper;
    }

//    private SimpleModule buildValueObjectModel() {
//        SimpleModule valueObjectsModule = new SimpleModule();
////        valueObjectsModule.addSerializer(Name.class, new NameSerializer());
////        valueObjectsModule.addSerializer(Cpf.class, new CpfSerializer());
////        valueObjectsModule.addSerializer(Email.class, new EmailSerializer());
//        valueObjectsModule.addSerializer(DocumentTypeAttributes.class, new DocumentAttributesSerializer());
//
//        return valueObjectsModule;
//    }

//    private SimpleModule buildValueObjectModel() {
//        SimpleModule valueObjectsModule = new SimpleModule();
//        valueObjectsModule.addSerializer(Name.class, new NameSerializer());
//        valueObjectsModule.addSerializer(Cpf.class, new CpfSerializer());
//        valueObjectsModule.addSerializer(Email.class, new EmailSerializer());
//
//        return valueObjectsModule;
//    }
}
