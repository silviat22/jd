/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s14.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegionDao implements Dao<Region> {
    private static final Logger log = LogManager.getLogger(RegionDao.class);

    private static final String GET_BY_PK = """
            SELECT region_id, name
            FROM region
            WHERE region_id = ?""";
    private static final String GET_ALL = """
            SELECT region_id, name
            FROM region""";
    private static final String INSERT = """
            INSERT INTO region (region_id, name) VALUES
                (?, ?)""";
    private static final String UPDATE_BY_ID = """
            UPDATE region
            SET name = ?
            WHERE region_id = ?""";
    private static final String DELETE = """
            DELETE FROM region
            WHERE region_id = ?""";

    @Override
    public List<Region> getAll() {
        List<Region> results = new ArrayList<>();

        try (Connection conn = Connector.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(GET_ALL)) {
            while (rs.next()) {
                Region region = new Region(rs.getLong(1), rs.getString(2));
                results.add(region);
            }
        } catch (SQLException se) {
            log.error("Can't get all regions", se);
        }

        return results;
    }

    @Override
    public Optional<Region> get(long id) {
        try (Connection conn = Connector.getConnection(); //
                PreparedStatement ps = conn.prepareStatement(GET_BY_PK)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Region my = new Region(rs.getLong(1), rs.getString(2));
                    return Optional.of(my);
                }
            }
        } catch (SQLException se) {
            log.error("Can't get coder " + id, se);
        }

        return Optional.empty();
    }
    
    public Region legacyGet(long id) {
        try (Connection conn = Connector.getConnection(); //
                PreparedStatement ps = conn.prepareStatement(GET_BY_PK)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Region(rs.getLong(1), rs.getString(2));
                }
            }
        } catch (SQLException se) {
            log.error("Can't get region " + id, se);
        }

        return null;
    }

    @Override
    public void save(Region region) {
        try (Connection conn = Connector.getConnection(); //
                PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setLong(1, region.getId());
            ps.setString(2, region.getName());
            ps.executeUpdate();
        } catch (SQLException se) {
            log.error("Can't save region " + region.getId(), se);
        }

    }

    @Override
    public void update(Region region) {
        try (Connection conn = Connector.getConnection(); //
                PreparedStatement ps = conn.prepareStatement(UPDATE_BY_ID)) {
            ps.setLong(1, region.getId());
            ps.setString(2, region.getName());
            ps.executeUpdate();
            int count = ps.executeUpdate();
            if (count != 1) {
                log.warn("Updated " + count + " lines for " + region);
            }
        } catch (SQLException se) {
            log.error("Can't update region " + region.getId(), se);
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
