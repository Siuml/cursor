package com.booktrade.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
public class SqlRepairRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SqlRepairRunner.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) {
        try (Connection conn = dataSource.getConnection()) {
            String driverName = conn.getMetaData().getDriverName();
            log.info("SqlRepairRunner: connected to {}", driverName);

            if (driverName == null || !driverName.contains("MySQL")) {
                log.info("SqlRepairRunner: skipping (not MySQL: {})", driverName);
                return;
            }

            // 1. Create all missing tables
            log.info("SqlRepairRunner: creating tables if missing...");
            runScript(conn, "sql/railway-repair-create.sql");

            // 2. Add missing columns
            addColumnIfMissing(conn, "user", "deleted");
            addColumnIfMissing(conn, "book", "deleted");

            log.info("SqlRepairRunner: Railway MySQL repair complete!");

        } catch (Exception e) {
            log.error("SqlRepairRunner: repair failed: {}", e.getMessage(), e);
        }
    }

    private void addColumnIfMissing(Connection conn, String table, String column) {
        try (Statement checkStmt = conn.createStatement()) {
            ResultSet rs = checkStmt.executeQuery(
                "SELECT COUNT(*) FROM information_schema.COLUMNS " +
                "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = '" + table +
                "' AND COLUMN_NAME = '" + column + "'");
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            checkStmt.close();

            if (count == 0) {
                try (Statement alterStmt = conn.createStatement()) {
                    alterStmt.execute("ALTER TABLE `" + table + "` ADD COLUMN `" +
                        column + "` TINYINT NOT NULL DEFAULT 0");
                    log.info("SqlRepairRunner: added `{}`.`{}` column", table, column);
                }
            } else {
                log.info("SqlRepairRunner: `{}`.`{}` already exists, skipping", table, column);
            }
        } catch (Exception e) {
            log.warn("SqlRepairRunner: failed to check/add {}.{}: {}", table, column, e.getMessage());
        }
    }

    private void runScript(Connection conn, String scriptPath) {
        try {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                new ClassPathResource(scriptPath));
            populator.setContinueOnError(true);
            populator.setIgnoreFailedDrops(true);
            populator.populate(conn);
            log.info("SqlRepairRunner: script {} executed", scriptPath);
        } catch (Exception e) {
            log.warn("SqlRepairRunner: script {} failed: {}", scriptPath, e.getMessage());
        }
    }
}
