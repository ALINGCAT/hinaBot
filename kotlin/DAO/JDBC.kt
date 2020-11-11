package DAO

import ConfigKit
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

open class JDBC {
    companion object {
        val config = ConfigKit.getConfig("config.txt")
        var conn:Connection = DriverManager.getConnection(
                config.get("databasePath"),
                config.get("databaseUser"),
                config.get("databasePassword")
        )
        var stmt:Statement = conn.createStatement()
        fun init() {
            if (conn.isClosed) {
                conn = DriverManager.getConnection(
                        config.get("databasePath"),
                        config.get("databaseUser"),
                        config.get("databasePassword")
                )
                stmt = conn.createStatement()
            }
        }
        fun close() {
            if (!conn.isClosed) {
                conn.close()
                stmt.close()
            }
        }
    }
}