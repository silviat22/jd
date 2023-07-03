/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s14.dao;

import java.util.List;
import java.util.Optional;
//qui si dichiarano i dati tramite i metodi
public interface Dao<T> { //interfaccia generica, segnalate da <>, parametrizzata rispetto a un tipo, simile a lista

    Optional<T> get(long id); //risultato del CoderDao

    List<T> getAll();

    void save(T t);

    void update(T t);

    void delete(long id);
}