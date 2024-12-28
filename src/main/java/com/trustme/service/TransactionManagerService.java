package com.trustme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
public class TransactionManagerService {

    private final PlatformTransactionManager transactionManager;
    private TransactionStatus transactionStatus;

    public TransactionManagerService(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void startTransaction() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("CustomTransaction");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionStatus = transactionManager.getTransaction(def);
    }

    public void commitTransaction() {
        if (transactionStatus != null) {
            transactionManager.commit(transactionStatus);
        }
    }

    public void rollbackTransaction() {
        if (transactionStatus != null) {
            transactionManager.rollback(transactionStatus);
        }
    }
}
