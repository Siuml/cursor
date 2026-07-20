package com.booktrade.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
public class SqlRepairRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SqlRepairRunner.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) {
        log.info("SqlRepairRunner starting...");
        try (Connection conn = dataSource.getConnection()) {
            String driverName = conn.getMetaData().getDriverName();
            log.info("SqlRepairRunner: driver = {}", driverName);

            if (driverName == null || !driverName.toLowerCase().contains("mysql")) {
                log.info("SqlRepairRunner: not MySQL, skipping SQL repair");
                return;
            }

            String[][] fixes = {
                {"user",    "deleted", "TINYINT NOT NULL DEFAULT 0"},
                {"book",    "deleted", "TINYINT NOT NULL DEFAULT 0"},
            };

            for (String[] fix : fixes) {
                tryAddColumn(conn, fix[0], fix[1], fix[2]);
            }

            log.info("SqlRepairRunner: all repairs complete!");

        } catch (Exception e) {
            log.error("SqlRepairRunner: connection/setup failed: {}", e.getMessage());
        }
    }

    private void tryAddColumn(Connection conn, String table, String column, String definition) {
        String sql = "ALTER TABLE `" + table + "` ADD COLUMN `" + column + "` " + definition;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            log.info("SqlRepairRunner: SUCCESS - added `{}`.`{}`", table, column);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && msg.toLowerCase().contains("duplicate column")) {
                log.info("SqlRepairRunner: `{}`.`{}` already exists, ok", table, column);
            } else {
                log.warn("SqlRepairRunner: failed `{}`.`{}` - {}", table, column,
                    msg != null ? msg.substring(0, Math.min(100, msg.length())) : "unknown");
            }
        }
    }
}
