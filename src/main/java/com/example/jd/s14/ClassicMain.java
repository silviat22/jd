/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s14;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.jd.s14.dao.Coder;
import com.example.jd.s14.dao.CoderDao;

/**
 * Sample application using DAO
 */
public class ClassicMain {
    private static final Logger log = LogManager.getLogger(ClassicMain.class);

    public static void main(String[] args) {
        CoderDao cd = new CoderDao();

        // create a new coder ...
        final long id = 501L; // abbiamo creato un nuovo coder con atributi e lo salviamo persistente
        Coder x = new Coder(id, "Tom", "Jones", 99_999, 2_000);
        cd.save(x);

        // ... then get it tramite id 501 ne creiamo uno nuovo in memoria, ne avremo 2 con id diverso (id di Eclipse, oggetti in Java)
        Coder fiveOone = cd.legacyGet(id); //legacy è una notazione vecchia
        if (fiveOone == null) {
            log.error("Unexpected! Can't get the coder " + id);
            System.out.println("Coder has not been saved correctly!");
            return;
        } else {
            System.out.println("Get: " + fiveOone);
        }

        // update coder salary
        fiveOone.setSalary(fiveOone.getSalary() * 2);
        cd.update(fiveOone);
        System.out.println("Update salary: " + fiveOone);

        // rename the coder
        fiveOone.setLastName("Hollz");
        cd.update(fiveOone);
        System.out.println("Update renamed: " + fiveOone);

        // delete the coder
        cd.delete(id);

        // ensure the coder is actually removed from the database
        fiveOone = cd.legacyGet(id);
        if (fiveOone == null) {
            System.out.println("Coder correctly removed");
        } else {
            System.out.println("Unexpected! Coder is still alive: " + fiveOone);
        }

        System.out.println("All coders");
        List<Coder> coders = cd.getAll();
        for (Coder coder : coders) {
            System.out.println(coder);
        }
    }
}
