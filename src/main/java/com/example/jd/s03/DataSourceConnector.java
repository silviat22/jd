/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s03;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.jd.Config;
//i data source li vado a prendere dal config
/**
 * A Hello JDBC by data source
 */
public class DataSourceConnector {
    private static final Logger log = LogManager.getLogger(DataSourceConnector.class);

    /**
     * Connect to the current data source, get and log DB info then terminate
     * 
     * @param args not used
     */
    public static void main(String[] args) { //dimmi qual è il data source x lavorarci su
        log.trace("Connecting ...");
        DataSource ds = Config.getDataSource(); //crea oggetto data source

        try (Connection conn = ds.getConnection()) { //try with resources x connettere l'oggetto data source e lavorarci col database
            DatabaseMetaData dmd = conn.getMetaData(); //metadata: info su database, vedi giù

            String db = dmd.getDatabaseProductName(); 
            String version = dmd.getDatabaseProductVersion();

            String catalog = conn.getCatalog();
            String schema = conn.getSchema();

            System.out.printf("Connected to %s version %s, catalog %s, schema %s", db, version, catalog, schema);
        } catch (SQLException e) {
            log.fatal("Can't get database info", e);
            System.out.println("Can't get database info");
        }
    }
}
