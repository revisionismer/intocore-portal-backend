package com.intocore.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DatabaseInfoTest {
	
    @Autowired
    private DataSource dataSource;

    @Test
    public void printDatabaseInfo() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            System.out.println("=== Database Info ===");
            System.out.println("Database Product Name: " + metaData.getDatabaseProductName());
            System.out.println("Database Product Version: " + metaData.getDatabaseProductVersion());
            System.out.println("Driver Name: " + metaData.getDriverName());
            System.out.println("Driver Version: " + metaData.getDriverVersion());
            System.out.println("JDBC Major Version: " + metaData.getJDBCMajorVersion());
            System.out.println("JDBC Minor Version: " + metaData.getJDBCMinorVersion());
            System.out.println("=====================");
        }
    }

}
