package com.example.docxddd.domain.folder.entity.bankdata;

import com.example.docxddd.domain.Cpf;
import com.example.docxddd.domain.Name;
import com.example.docxddd.domain.common.Result;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class BankAccountHolder {

    private Name fullName;

    private Cpf cpf;

    private BankAccountHolder(Name fullName,
                              Cpf cpf) {

        if (fullName == null) {
            throw new IllegalArgumentException("fullName cannot be null");
        }

        if (cpf == null) {
            throw new IllegalArgumentException("cpf cannot be null");
        }

        this.fullName = fullName;
        this.cpf = cpf;
    }

    public static Result<BankAccountHolder> create(Name fullName,
                                                    Cpf cpf) {
        try {
            return Result.ok(new BankAccountHolder(fullName, cpf));
        } catch (IllegalArgumentException ex) {
            return Result.error(ex.getMessage());
        }
    }
}
