package com.example.docxddd.application;

import com.example.docxddd.domain.common.Result;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

public class TransactionalApplicationService {

    public <T> Result<T> rollbackResult(Result<T> errorResult) {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        return errorResult;
    }

    public <T> ResponseEntity<T> rollBackResponseEntity(ResponseEntity<T> responseEntity) {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        return responseEntity;
    }
}
