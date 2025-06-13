package ru.netology.shop.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "password"
        );
    }

    @SneakyThrows
    public static String getStatus() {
        String dataSQL = "SELECT status FROM payment_entity;";
        try (var connection = getConnection()) {
            return runner.query(connection, dataSQL, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static void cleanTables() {
        try (var connection = getConnection()) {
            runner.execute(connection, "DELETE FROM payment_entity;");
            runner.execute(connection, "DELETE FROM order_entity;");
            runner.execute(connection, "DELETE FROM credit_request_entity;");
        }
    }
}
