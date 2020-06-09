package com.example.docxddd.domain.folder;

import com.example.docxddd.domain.common.Result;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// TODO - Pensar numa de ter somente attachments válidos
// TODO - Criar um AttachmentDto e validar a existência de cada um, gerando um Attachment
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class Attachment {

    public static final int ZERO_PAGES = 0;
    public static final int ONE_PAGE = 1;
    public static final int TWO_PAGES = 2;
    public static final int UNLIMITED_PAGES = Integer.MAX_VALUE;

    private String fileName;

    private String contentType;

    private Integer pageNumber;

    private Attachment(String fileName,
                       String contentType,
                       Integer pageNumber) {

        if (fileName == null) {
            throw new IllegalArgumentException("fileName cannot be null");
        }

        if (contentType == null) {
            throw new IllegalArgumentException("contentType cannot be null");
        }

        if (pageNumber == null) {
            throw new IllegalArgumentException("pageNumber cannot be null");
        }

        if (pageNumber.equals(0)) {
            throw new IllegalArgumentException("pageNumber cannot be zero");
        }

        this.fileName = fileName;
        this.contentType = contentType;
        this.pageNumber = pageNumber;
    }

    public static Result<Attachment> create(String fileName,
                                            String contentType,
                                            Integer pageNumber) {
        try {
            return Result.ok(new Attachment(fileName, contentType, pageNumber));
        } catch (IllegalArgumentException ex) {
            return Result.error(ex.getMessage());
        }
    }
}
