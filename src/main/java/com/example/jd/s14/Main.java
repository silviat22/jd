/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s14;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.jd.s14.dao.Coder;
import com.example.jd.s14.dao.CoderDao;

/**
 * Sample application using DAO
 */
//la freccia vuol dire: a dx c'è il body che viene eseguito correlato a quello che c'è nel metodo. Indica una funzione anonima
public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        CoderDao cd = new CoderDao();

        // create a new coder ...
        final long id = 501L;
        cd.save(new Coder(id, "Tom", "Jones", 99_999, 2_000));

        // ... then get it
        cd.get(id).ifPresentOrElse(coder -> { //prendi reference cd, invoca metodo get su id e su cd (?) dopo la condizione if, si passa una funzione (metodo) (alla funzione precedente)
            System.out.println("Get: " + coder);

            // update coder salary
            coder.setSalary(coder.getSalary() * 2);
            cd.update(coder);
            System.out.println("Update salary: " + coder);

            // rename a coder
            coder.setLastName("Hollz");
            cd.update(coder);
            System.out.println("Update renamed: " + coder);
        }, () -> {
            log.error("Unexpected! Can't get coder 501");
            System.out.println("No coder to work with!");
        });

        // delete the coder
        cd.delete(id);

        // ensure the coder is actually removed from the database
        cd.get(id).ifPresentOrElse(coder -> log.error("Unexpected! Coder is still alive: " + coder),
                () -> System.out.println("Coder correctly removed"));

        System.out.println("All coders");
        cd.getAll().forEach(System.out::println);
    }
}
