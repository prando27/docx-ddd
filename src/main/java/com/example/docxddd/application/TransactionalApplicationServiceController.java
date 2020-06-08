package com.example.docxddd.application;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

// TODO - Classe criada para que a camada de serviço não precise lançar uma exceção para que o rollback da transação seja feito
public class TransactionalApplicationServiceController {

    public <T> ResponseEntity<T> rollBackResponseEntity(ResponseEntity<T> responseEntity) {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        return responseEntity;
    }
}
