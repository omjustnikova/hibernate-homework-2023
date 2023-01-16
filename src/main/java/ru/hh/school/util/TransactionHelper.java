package ru.hh.school.util;

import javax.persistence.EntityManager;
import javax.swing.plaf.PanelUI;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;
import java.util.function.Supplier;

public class TransactionHelper {
    private final SessionFactory sessionFactory;
    public TransactionHelper(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public <T> T inTransaction(Supplier<T> supplier) {
        Optional<Transaction> transaction = beginTransaction();
        try {
            T result = supplier.get();
            transaction.ifPresent(Transaction::commit);
            return result;
        } catch (RuntimeException e) {
            transaction
                    .filter(Transaction::isActive)
                    .ifPresent(Transaction::rollback);
            throw e;
        }
    }

    public void inTransaction(Runnable runnable) {
        inTransaction(() -> {
            runnable.run();
            return null;
        });
    }

    public void update(Object o) {
        sessionFactory.getCurrentSession().update(o);
    }

    private Optional<Transaction> beginTransaction() {
        Transaction transaction = sessionFactory.getCurrentSession().getTransaction();
        if (transaction.isActive()) {
            return Optional.empty();
        }
        transaction.begin();
        return Optional.of(transaction);
    }


}
