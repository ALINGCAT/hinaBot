package dao

import core.Item

class InventoryDao : JDBC() {
    companion object {
        fun getItems(qq: Long): HashMap<Int, Int> {
            init()
            val rs = stmt.executeQuery("select * from inventory where owner = $qq")
            val temp = HashMap<Int, Int>()
            while (rs.next()) temp.put(rs.getInt("item_id"), rs.getInt("amount"))
            return temp
        }
        fun setAmount(qq: Long, item: Item, amount: Int) {
            stmt.execute("update inventory set amount = $amount where owner = $qq and item_id = ${item.id}")
        }
        fun firstGainItem(qq: Long, item: Item, amount: Int = 1) {
            stmt.execute("insert into inventory(owner, item_id, amount) values('$qq', '${item.id}', '$amount')")
        }
    }
}