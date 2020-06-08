package com.example.docxddd.infrastructure.config.jackson.serializer;

import com.example.docxddd.domain.Cpf;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CpfSerializer extends StdSerializer<Cpf> {

    public CpfSerializer() {
        this(null);
    }

    protected CpfSerializer(Class<Cpf> t) {
        super(t);
    }

    @Override
    public void serialize(Cpf cpf,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeString(cpf.getValue());
    }
}
