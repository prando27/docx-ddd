package com.example.docxddd.infrastructure;

import com.example.docxddd.domain.folder.DocumentTypeAttributes;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class DocumentAttributesSerializer extends StdSerializer<DocumentTypeAttributes> {

    public DocumentAttributesSerializer() {
        this(null);
    }

    protected DocumentAttributesSerializer(Class<DocumentTypeAttributes> t) {
        super(t);
    }

    @Override
    public void serialize(DocumentTypeAttributes documentTypeAttributes,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeOmittedField("clazz");
    }
}
