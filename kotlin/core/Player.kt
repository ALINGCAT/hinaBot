package core

import core.item.Item
import dao.EssDao
import dao.InventoryDao
import tools.CDate
import tools.Key
import java.time.DayOfWeek
import java.time.LocalDateTime

class Player(QQ: Long, Coins: Int, LastSignIn: LocalDateTime) {
    companion object {
        lateinit var instances: HashMap<Long, Player>
        fun init() {
            instances = EssDao.getPlayers()
            for (e in instances) e.value.initInventory()
        }
        fun addNewPlayer(qq: Long) {
            EssDao.addPlayer(qq)
            instances[qq] = Player(qq, 50, LocalDateTime.now())
            InventoryDao.firstGainItem(qq, Item.instances[Key["猫猫招待卷"]]!!)
        }
    }
    private val qq: Long = QQ
    var coins = Coins
    var lastSignIn = LastSignIn
    val inventory = HashMap<Item, Int>()
    fun addCoins(amount: Int) {
        coins += amount
        EssDao.setCoins(qq, coins)
    }
    fun initInventory() {
        for (e in InventoryDao.getItems(qq)) Item.instances[Key[e.key]]?.let { inventory.put(it, e.value) }
    }
    fun gainItem(item: Item, amount: Int = 1) {
        if (inventory[item] == null) {
            InventoryDao.firstGainItem(qq, item)
            inventory[item] = 1
        } else {
            val total = inventory[item]!! + amount
            InventoryDao.setAmount(qq, item, total)
            inventory[item] = total
        }
    }
    fun signIn(): HashMap<String, String>? {
        val now = LocalDateTime.now()
        if (lastSignIn.dayOfYear == now.dayOfYear && lastSignIn.year == now.year) return null
        else {
            val result = HashMap<String, String>()
            val temp = Reward.perWeekSignIn[now.dayOfWeek.value-1]
            addCoins(50)
            gainItem(temp)
            EssDao.updateLastSignIn(qq)
            lastSignIn = LocalDateTime.now()
            result["day"] = CDate.toChinese(now.dayOfWeek)
            result["reward"] = temp.toString()
            return result
        }
    }
    fun draw(times: Int = 1): HashMap<Item, Int>? {
        val cost = 50 * times
        if (coins < cost) return null
        addCoins(-cost)
        val result = HashMap<Item, Int>()
        for (i in (1..times)) {
            val temp: Item = when((1..100).random()) {
                1 -> Item.drawPool["SS"]?.random()!!
                in 2..7 -> Item.drawPool["S"]?.random()!!
                in 8..20 -> Item.drawPool["A"]?.random()!!
                in 21..50 -> Item.drawPool["B"]?.random()!!
                else -> Item.drawPool["C"]?.random()!!
            }
            if (result[temp] == null) result[temp] = 1
            else result[temp]?.plus(1)?.let { result.replace(temp, it) }
        }
        for (e in result) {
            if (inventory[e.key] == null) {
                InventoryDao.firstGainItem(qq, e.key, e.value)
                inventory[e.key] = e.value
            }
            else {
                val temp = inventory[e.key]!! + e.value
                InventoryDao.setAmount(qq, e.key, temp)
                inventory[e.key] = temp
            }
        }
        return result
    }
}