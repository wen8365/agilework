package com.agilework.sims.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Method "saveAll" in JpaRepository will call "save" in a loop.
 * And "save" will first select the record, then do insert if not exists,
 * or do update otherwise.
 * For avoiding check ahead insert/update, I re-implement these two method here.
 * @param <T> Type of entity which will be saved.
 */
@Component
public class FastSaveRepositoryImpl<T> implements FastSaveRepository<T> {

    @PersistenceContext
    private EntityManager em;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Transactional
    public <S extends T, I extends Iterable<S>> I insertAll(I iterable) {
        perform(iterable.iterator(), em::persist);
        return iterable;
    }

    @Transactional
    public <S extends T, I extends Iterable<S>> I updateAll(I iterable) {
        perform(iterable.iterator(), em::merge);
        return iterable;
    }

    private <S extends T> void perform(Iterator<S> iterator, Consumer<S> consumer) {
        int count = 0;
        while (iterator.hasNext()) {
            consumer.accept(iterator.next());
            count++;
            if (count % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }
        if (count % batchSize != 0) {
            em.flush();
            em.clear();
        }
    }
}
