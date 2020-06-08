package com.example.docxddd.domain.folder.entity.bankdata;

import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.DocumentTypeAttributes;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static java.util.Optional.ofNullable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class BankDataAttributes extends DocumentTypeAttributes {

    private boolean ownerOfTheAccount;
    private BankAccountHolder accountHolder;

    private BankDataAttributes(boolean ownerOfTheAccount,
                               BankAccountHolder accountHolder) {

        this.ownerOfTheAccount = ownerOfTheAccount;
        this.accountHolder = accountHolder;
    }

    public static Result<BankDataAttributes> create(boolean ownerOfTheAccount,
                                                    BankAccountHolder accountHolder) {
        try {
            return Result.ok(new BankDataAttributes(ownerOfTheAccount, accountHolder));
        } catch (IllegalArgumentException ex) {
            return Result.error(ex.getMessage());
        }
    }

    @Override
    public DocumentTypeAttributes mergeWith(DocumentTypeAttributes attributes) {
        var attrs = (BankDataAttributes) attributes;

        // Merge aqui tem que percorrer os dados de accountholder
        return new BankDataAttributes(
                ownerOfTheAccount,
                ofNullable(accountHolder).orElse(attrs.accountHolder));
    }
}
