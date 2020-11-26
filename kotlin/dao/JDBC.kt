package dao

import ConfigKit
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

open class JDBC {
    companion object {
        private val config = ConfigKit.getConfig("config.txt")
        private var conn:Connection = DriverManager.getConnection(
                config["databasePath"],
                config["databaseUser"],
                config["databasePassword"]
        )
        var stmt:Statement = conn.createStatement()
        fun init() {
            if (conn.isClosed) {
                conn = DriverManager.getConnection(
                        config["databasePath"],
                        config["databaseUser"],
                        config["databasePassword"]
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