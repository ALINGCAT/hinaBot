package core

import dao.EssDao
import dao.InventoryDao
import java.time.LocalDateTime

class GameMain {
    companion object {
        fun init() {
            Item.init()
            Draw.init()
            initData()
        }
        fun initData() {
            Player.init()
        }
        fun isFirstSignIn(qq: Long):Boolean {
            return if (Player.instances[qq] == null) {
                EssDao.addPlayer(qq)
                Player.instances[qq] = Player(qq, 50, LocalDateTime.now(), 1)
                true
            }
            else false
        }
        fun isAlreadySignIn(qq: Long): Boolean {
            val temp = Player.instances[qq]!!.lastSignIn
            val now = LocalDateTime.now()
            return temp.dayOfYear == now.dayOfYear && temp.year == now.year
        }
        fun getLot(qq: Long, isSignIn: Boolean = false): HashMap<String, String>? {
            if (!isSignIn) {
                if (Player.instances[qq]!!.coins < 10) return null
                else Player.instances[qq]!!.addCoins(-10)
            }
            var lot = "最下签"
            var increment = 0
            when ((1..100).random()) {
                1 -> { lot = "最上签"; increment = 50 }
                in 2..20 -> { lot = "大吉"; increment = (17..20).random() }
                in 21..40 -> { lot = "中吉"; increment = (12..15).random() }
                in 41..60 -> { lot = "小吉"; increment = (8..10).random() }
                in 61..80 -> { lot = "小凶"; increment = (3..5).random() }
                in 81..99 -> { lot = "大凶"; increment = (1..2).random() }
            }
            if (isSignIn) Player.instances[qq]!!.updateSignIn(increment)
            else Player.instances[qq]!!.addCoins(increment)
            val result = HashMap<String, String>()
            result["lot"] = lot
            result["increment"] = increment.toString()
            result["total"] = Player.instances[qq]!!.coins.toString()
            result["accumulation"] = Player.instances[qq]!!.accumulation.toString()
            return result
        }
        fun draw(qq: Long): Item? {
            if (Player.instances[qq]?.coins ?: 0 < 50) return null
            Player.instances[qq]!!.addCoins(-50)
            var result: Item
            when((1..100).random()) {
                1 -> result = Draw.pool["SS"]?.random()!!
                in 2..7 -> result = Draw.pool["S"]?.random()!!
                in 8..20 -> result = Draw.pool["A"]?.random()!!
                in 21..50 -> result = Draw.pool["B"]?.random()!!
                else -> result = Draw.pool["C"]?.random()!!
            }
            if (Player.instances[qq]!!.inventory[result] == null) {
                InventoryDao.firstGainItem(qq, result)
                Player.instances[qq]!!.inventory.put(result, 1)
            } else {
                val temp = Player.instances[qq]!!.inventory[result]!! + 1
                InventoryDao.setAmount(qq, result, temp)
                Player.instances[qq]!!.inventory[result] = temp
            }
            return result
        }
        fun drawTenTimes(qq: Long): HashMap<Item, Int>? {
            if (Player.instances[qq]!!.coins < 500) return null
            Player.instances[qq]!!.addCoins(-500)
            val result = HashMap<Item, Int>()
            for (i in (1..10)) {
                var temp: Item
                when((1..100).random()) {
                    1 -> temp = Draw.pool["SS"]?.random()!!
                    in 2..7 -> temp = Draw.pool["S"]?.random()!!
                    in 8..20 -> temp = Draw.pool["A"]?.random()!!
                    in 21..50 -> temp = Draw.pool["B"]?.random()!!
                    else -> temp = Draw.pool["C"]?.random()!!
                }
                if (result[temp] == null) result[temp] = 1
                else result[temp]?.plus(1)?.let { result.replace(temp, it) }
            }
            for (e in result) {
                if (Player.instances[qq]!!.inventory[e.key] == null) {
                    InventoryDao.firstGainItem(qq, e.key, e.value)
                    Player.instances[qq]!!.inventory.put(e.key, e.value)
                }
                else {
                    val temp = Player.instances[qq]!!.inventory[e.key]!! + e.value
                    InventoryDao.setAmount(qq, e.key, temp)
                    Player.instances[qq]!!.inventory[e.key] = temp
                }
            }
            return result
        }
    }
}