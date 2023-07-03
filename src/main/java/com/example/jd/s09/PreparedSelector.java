/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s09;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.jd.Config;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * From PreparedStatement to ResultSet to List of JavaBean
 */
public class PreparedSelector { //? = argomenti attesi
    private static final Logger log = LogManager.getLogger(PreparedSelector.class);

    private static final String GET_CODERS_BY_SALARY = """
            SELECT e.first_name, e.last_name, e.hired, e.salary
            FROM employee e JOIN department d USING (department_id)
            WHERE d.name = 'IT' AND e.salary >= ?
            ORDER BY e.salary DESC""";
    private static final String GET_CODERS_HIRED_BEFORE = """
            SELECT e.first_name, e.last_name, e.hired, e.salary
            FROM employee e JOIN department d USING (department_id)
            WHERE d.name = 'IT' AND  hired < ?
            ORDER BY hired""";
    private static final String GET_CODERS_BY_LETTER = """
            SELECT e.first_name, e.last_name, e.hired, e.salary
            FROM employee e JOIN department d USING (department_id)
            WHERE d.name = 'IT' AND (first_name LIKE ? OR last_name LIKE ?)
            ORDER BY salary DESC""";

    private DataSource ds;

    public PreparedSelector() {
        this.ds = Config.getDataSource();
    }

    public List<Coder> getCodersHiredBefore(LocalDate limit) throws SQLException {
        try (Connection conn = ds.getConnection();
                PreparedStatement prepStmt = conn.prepareStatement(GET_CODERS_HIRED_BEFORE)) {
            prepStmt.setObject(1, limit); //setObject intrrodotto recentemente per evitare di scrivere tutti gli oggetti da inserire 1 a 1 (es. str o date)

            // not supported by Oracle JDBC
            log.debug("PreparedStatement: {}", prepStmt);

            List<Coder> result = new ArrayList<>();
            //c'è un altro try w/resources perché c'è setobject per preparare lo stmt e si spezza in 2
            try (ResultSet rs = prepStmt.executeQuery()) {
                while (rs.next()) { //leggere result set colonna x colonna
                    Coder coder = new Coder(rs.getString(1), rs.getString(2), rs.getObject(3, LocalDate.class),
                            rs.getInt(4));
                    result.add(coder);
                }
            }

            log.debug(result);
            return result;
        }
    }

    public List<Coder> getCodersBySalary(double lower) throws SQLException {
        try (Connection conn = ds.getConnection();
                PreparedStatement prepStmt = conn.prepareStatement(GET_CODERS_BY_SALARY)) {
            prepStmt.setDouble(1, lower);

            // not supported by Oracle JDBC
            log.debug("PreparedStatement: {}", prepStmt);

            List<Coder> result = new ArrayList<>();
            try (ResultSet rs = prepStmt.executeQuery()) {
                while (rs.next()) {
                    Coder coder = new Coder(rs.getString(1), rs.getString(2), rs.getObject(3, LocalDate.class),
                            rs.getDouble(4));
                    result.add(coder);
                }
            }

            log.debug(result);
            return result;
        }
    }

    public List<Coder> getCodersWithLetterIn(char letter) throws SQLException {
        try (Connection conn = ds.getConnection();
                PreparedStatement prepStmt = conn.prepareStatement(GET_CODERS_BY_LETTER)) {

            // quotation in string is managed by PreparedStatement
            String pattern = "%" + letter + "%";
            prepStmt.setString(1, pattern);
            prepStmt.setString(2, pattern);

            // not supported by Oracle JDBC
            log.debug(prepStmt);
            List<Coder> results = new ArrayList<>();

            try (ResultSet rs = prepStmt.executeQuery()) {
                while (rs.next()) {
                    Coder coder = new Coder(rs.getString(1), rs.getString(2), rs.getObject(3, LocalDate.class),
                            rs.getDouble(4));
                    results.add(coder);
                }
            }

            return results;
        }
    }
}