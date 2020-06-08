package com.example.docxddd.application.folder.strategy;

import com.example.docxddd.domain.common.Result;

import java.util.Arrays;
import java.util.List;

public enum Context {

    OWNER,
    TENANT,
    RESIDENT;

    public static final List<Context> ALL = Arrays.asList(Context.values());

    public static Result<Context> getContextByName(String context) {
        try {
            return Result.ok(Context.valueOf(context));
        } catch (IllegalArgumentException ex) {
            return Result.error(ex.getMessage());
        }
    }
}
