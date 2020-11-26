package core

import dao.EssDao
import dao.InventoryDao
import java.time.LocalDateTime

class Player(QQ: Long, Coins: Int, LastSignIn: LocalDateTime, Accumulation: Int) {
    companion object {
        lateinit var instances: HashMap<Long, Player>
        fun init() {
            instances = EssDao.getPlayers()
            for (e in instances) e.value.initInventory()
        }
    }
    val qq: Long = QQ
    var coins = Coins
    var lastSignIn = LastSignIn
    var accumulation = Accumulation
    val inventory = HashMap<Item, Int>()
    fun addCoins(amount: Int) {
        coins += amount
        EssDao.setCoins(qq, coins)
    }
    fun updateSignIn(LotCoins: Int) {
        lastSignIn = LocalDateTime.now()
        accumulation++
        EssDao.updateLastSignIn(qq, accumulation)
        addCoins(LotCoins + accumulation)
    }
    fun initInventory() {
        for (e in InventoryDao.getItems(qq)) Item.instances[Item.table[e.key]]?.let { inventory.put(it, e.value) }
    }
}