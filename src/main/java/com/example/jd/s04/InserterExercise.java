/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s04;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.jd.Config;

/**
 * A raw hron service inserter
 */
public class InserterExercise {
    private static final Logger log = LogManager.getLogger(InserterExercise.class);
    // private static final String INSERT_SERVICE_BY_NAME_AND_LOCATION = null;

    // TODO: SQL code for insert
    private static final String INSERT_SERVICE_BY_NAME_AND_LOCATION = //
            "insert into service (name, location_id) values ('%s', %s);";

    public static void main(String[] args) {
        if (args.length != 2) { // solamente 2 args, service e location
            System.out.println("Pass me a service name and its location!");
            return;
        }

        DataSource ds = Config.getDataSource();
        try (Connection conn = ds.getConnection(); //
                Statement stmt = conn.createStatement()) { // ogg Java in cui metto codice SQL per eseguirlo
            // metodo format varargs, : str di partenza che verrà formattata, con
            // placeholder (%s: stringa ecc)( che verranno sostituiti da args 0/1
            String sql = String.format(INSERT_SERVICE_BY_NAME_AND_LOCATION, args[0], args[1]);
            int lines = stmt.executeUpdate(sql); // metodo x eseguire statement: lo manda sulla connessione al database,
                                                 // il db fa qualcosa e torna a JDBC che esegue il tutto
            System.out.printf("Insert executed, %d lines affected%n", lines);
        } catch (SQLException se) {
            log.fatal("Can't insert", se);
        }
    }
}
