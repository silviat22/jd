/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s14.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoderDao implements Dao<Coder> { //coder sostituisce la T nel DAO
    private static final Logger log = LogManager.getLogger(CoderDao.class);
//prendi la riga con questi where 
    private static final String GET_BY_PK = """ 
            SELECT employee_id, first_name, last_name, phone, hired, salary
            FROM employee
            WHERE department_id = 6 AND employee_id = ?""";
    private static final String GET_ALL = """
            SELECT employee_id, first_name, last_name, phone, hired, salary
            FROM employee
            WHERE department_id = 6""";
    private static final String INSERT = """
            INSERT INTO employee (employee_id, first_name, last_name, phone, hired, job_id, salary, manager_id, department_id) VALUES
                (?, ?, ?, ?, ?, 15, ?, 103, 6)""";
    private static final String UPDATE_BY_ID = """
            UPDATE employee
            SET first_name = ?, last_name = ?, phone = ?, hired = ?, salary = ?
            WHERE department_id = 6 AND employee_id = ?""";
    private static final String DELETE = """
            DELETE FROM employee
            WHERE department_id = 6 AND employee_id = ?""";

    @Override
    public List<Coder> getAll() {
        List<Coder> results = new ArrayList<>();

        try (Connection conn = Connector.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(GET_ALL)) {
            while (rs.next()) {
                Coder coder = new Coder(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                        rs.getObject(5, LocalDate.class), rs.getDouble(6));
                results.add(coder);
            }
        } catch (SQLException se) {
            log.error("Can't get all coders", se);
        }

        return results;
    }

    @Override
    public Optional<Coder> get(long id) {
        try (Connection conn = Connector.getConnection(); //
                PreparedStatement ps = conn.prepareStatement(GET_BY_PK)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Coder my = new Coder(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                            rs.getObject(5, LocalDate.class), rs.getDouble(6));
                    return Optional.of(my);
                }
            }
        } catch (SQLException se) {
            log.error("Can't get coder " + id, se);
        }

        return Optional.empty();
    }

    public Coder legacyGet(long id) {
        try (Connection conn = Connector.getConnection(); //
                PreparedStatement ps = conn.prepareStatement(GET_BY_PK)) {
            ps.setLong(1, id); //metti id al posto del 1° punto di domanda del prepared stmt
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Coder(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                            rs.getObject(5, LocalDate.class), rs.getDouble(6));
                }
            }
        } catch (SQLException se) {
            log.error("Can't get coder " + id, se);
        }

        return null;
    }

    @Override
    public void save(Coder coder) {
        try (Connection conn = Connector.getConnection(); //
                PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setLong(1, coder.getId());
            ps.setString(2, coder.getFirstName());
            ps.setString(3, coder.getLastName());
            ps.setInt(4, coder.getPhone());
            ps.setObject(5, coder.getHired());
            ps.setDouble(6, coder.getSalary());
            ps.executeUpdate(); //qui ci deve essere un comando DML, cioè insert e delete NON select: stiamo avorando su righe non result set
        } catch (SQLException se) {
            log.error("Can't save coder " + coder.getId(), se);
        }
    }

    @Override
    public void update(Coder coder) {
        try (Connection conn = Connector.getConnection(); //
                PreparedStatement ps = conn.prepareStatement(UPDATE_BY_ID)) {
            ps.setString(1, coder.getFirstName());
            ps.setString(2, coder.getLastName());
            ps.setInt(3, coder.getPhone());
            ps.setObject(4, coder.getHired());
            ps.setDouble(5, coder.getSalary());
            ps.setLong(6, coder.getId());
            int count = ps.executeUpdate();
            if (count != 1) { //1 coder, se sono 2 errore
                log.warn("Updated " + count + " lines for " + coder);
            }
        } catch (SQLException se) {
            log.error("Can't update coder " + coder.getId(), se);
        }
    }

    @Override
    public void delete(long id) {
        try (Connection conn = Connector.getConnection(); //
                PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setLong(1, id);
            int count = ps.executeUpdate();
            if (count != 1) {
                log.warn("Deleted " + count + " lines for " + id);
            }
        } catch (SQLException se) {
            log.error("Can't delete coder " + id, se);
        }
    }
}
