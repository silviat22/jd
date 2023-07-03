/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.jdbcx.JdbcDataSource;
import org.postgresql.ds.PGSimpleDataSource;
import org.sqlite.SQLiteDataSource;

import com.mysql.cj.jdbc.MysqlDataSource;

import oracle.jdbc.pool.OracleDataSource;

/**
 * JDBC Configuration
 */
public final class Config {
    private static final Logger log = LogManager.getLogger(Config.class);

    /**
     * The following static initializer should not be required
     */
//    static {
//        try {
//            // Class.forName("org.h2.Driver");
//            // Class.forName("com.mysql.cj.jdbc.Driver");
//            // Class.forName("oracle.jdbc.OracleDriver");
//            Class.forName("org.postgresql.Driver");
//        } catch (ClassNotFoundException cnfe) {
//            cnfe.printStackTrace();
//            throw new IllegalStateException("Can't load JDBC driver " + cnfe.getMessage());
//        }
//    }

    // public constants required for connection by DriverManager
    public static final String URL;
    public static final String USER;
    public static final String PASSWORD;

    private static final Dbms ACTIVE;
    private static final DataSource DS;

    private Config() { //scrivere file di configurazione tra database e eclipse???
    }
//inizializzazione da inizializzatore statico, connettere al database
    static { 
        String dbmsName = "h2";
        String url = "jdbc:h2:./hron";
        String user = "hron";
        String password = "password";

        try (InputStream is = Config.class.getClassLoader().getResourceAsStream("configuration.properties")) {
            Properties prop = new Properties();
            prop.load(is);
// leggere la chiave e mettere valori associati
            dbmsName = prop.getProperty("dbms.name");
            url = prop.getProperty("dbms.url");
            user = prop.getProperty("dbms.user");
            password = prop.getProperty("dbms.password");
        } catch (Exception ex) {
            log.error("Can't load configuration properties", ex);
        } finally {
            ACTIVE = switch (dbmsName) {
            case "mysql" -> Dbms.MYSQL;
            case "oracle" -> Dbms.ORACLE;
            case "postgres" -> Dbms.POSTGRES;
            case "sqlite" -> Dbms.SQLITE;
            default -> Dbms.H2;
            };

            URL = url;
            USER = user;
            PASSWORD = password;
        }

        DS = switch (ACTIVE) {
        case POSTGRES -> {
            PGSimpleDataSource pgds = new PGSimpleDataSource();
            pgds.setURL(URL);
            pgds.setUser(USER);
            pgds.setPassword(PASSWORD);

            yield pgds;
        }
        case ORACLE -> {
            try {
                OracleDataSource ods = new OracleDataSource();
                ods.setURL(URL);
                ods.setUser(USER);
                ods.setPassword(PASSWORD);

                yield ods;
            } catch (SQLException se) {
                throw new IllegalStateException("Can't set Oracle Data Source");
            }
        }
        case MYSQL -> {
            MysqlDataSource mds = new MysqlDataSource();
            mds.setURL(URL);
            mds.setUser(USER);
            mds.setPassword(PASSWORD);

            yield mds;
        }
        case H2 -> {
            JdbcDataSource hds = new JdbcDataSource();
            hds.setURL(URL);
            hds.setUser(USER);
            hds.setPassword(PASSWORD);

            yield hds;
        }
        case SQLITE -> {
            SQLiteDataSource pgds = new SQLiteDataSource();
            pgds.setUrl(URL);

            yield pgds;
        }

        };
    }

    static public Connection getLegacyConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    static public DataSource getDataSource() {
        return DS; //data source creato in config
    }

    public static boolean isH2() {
        return ACTIVE == Dbms.H2;
    }

    public static boolean isMySql() {
        return ACTIVE == Dbms.MYSQL;
    }

    public static boolean isOracle() {
        return ACTIVE == Dbms.ORACLE;
    }

    public static boolean isPostgres() {
        return ACTIVE == Dbms.POSTGRES;
    }

    public static boolean isPostgresLike() {
        return ACTIVE == Dbms.POSTGRES || ACTIVE == Dbms.H2;
    }

    public static boolean isSqLite() {
        return ACTIVE == Dbms.SQLITE;
    }

    public static boolean isStoredProcedureMissing() {
        return isSqLite() || isPostgres() || isH2();
    }
}
