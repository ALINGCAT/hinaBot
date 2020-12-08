package dao

import core.Player
import java.time.LocalDateTime

class EssDao : JDBC() {
    companion object {
        fun getPlayers(): HashMap<Long, Player> {
            init()
            val rs = stmt.executeQuery("select * from qq")
            val players = HashMap<Long, Player>()
            while (rs.next()) {
                players[rs.getLong("id")] = Player(
                        rs.getLong("id"),
                        rs.getInt("coins"),
                        rs.getTimestamp("lastsignin").toLocalDateTime()
                )
            }
            return players
        }
        fun addPlayer(qq:Long) {
            init()
            stmt.execute("insert into qq(id, coins) values('$qq', '50')")
        }
        fun updateLastSignIn(qq:Long) {
            val now = LocalDateTime.now()
            init()
            stmt.execute("update qq set lastsignin = '$now' where id = $qq")
        }
        fun setCoins(qq:Long, coins:Int) {
            init()
            stmt.execute("update qq set coins = '$coins' where id = $qq")
        }
    }
}