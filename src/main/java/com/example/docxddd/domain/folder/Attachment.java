package com.example.docxddd.domain.folder;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Attachment {

    public static final Integer PAGE_ONE = 1;
    public static final Integer PAGE_TWO = 2;

    private String fileName;

    private String contentType;

    private Integer pageNumber;
}
