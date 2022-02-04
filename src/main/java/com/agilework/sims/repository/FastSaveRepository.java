package com.agilework.sims.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface FastSaveRepository<T>  {

    <S extends T, I extends Iterable<S>> I insertAll(I iterable);

    <S extends T, I extends Iterable<S>> I updateAll(I iterable);
}
