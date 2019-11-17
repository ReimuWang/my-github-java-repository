package org.reimuwang.testspringboot.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@Slf4j
public class TestDataSource implements CommandLineRunner {

    @Autowired
    JdbcTemplate fooJdbcTemplate;

    @Autowired
    JdbcTemplate barJdbcTemplate;

    @Override
    public void run(String... args) throws SQLException {
        fooJdbcTemplate.execute("CREATE TABLE FOO (ID INT IDENTITY, NAME VARCHAR(64))");
        fooJdbcTemplate.update("INSERT INTO FOO (ID, NAME) VALUES (1, 'foo1')");
        fooJdbcTemplate.update("INSERT INTO FOO (ID, NAME) VALUES (2, 'foo2')");
        fooJdbcTemplate.queryForList("SELECT * FROM FOO").forEach(row -> log.info("[foo]" + row.toString()));

        barJdbcTemplate.execute("CREATE TABLE BAR (ID INT IDENTITY, NAME VARCHAR(64))");
        barJdbcTemplate.update("INSERT INTO BAR (ID, NAME) VALUES (1, 'bar1')");
        barJdbcTemplate.update("INSERT INTO BAR (ID, NAME) VALUES (2, 'bar2')");
        barJdbcTemplate.queryForList("SELECT * FROM BAR").forEach(row -> log.info("[bar]" + row.toString()));
    }
}
