package core

class Item(item_id: Int, item_rarity: String, item_name: String, item_description: String, drawable: Boolean,func: () -> Unit) {
    companion object{
        val instances = HashMap<String, Item>()
        val table = HashMap<Int, String>()
        fun addItem(item_id: Int, item_rarity: String, item_name: String, item_description: String, drawable: Boolean,func: () -> Unit) {
            var temp = Item(item_id, item_rarity, item_name, item_description, drawable, func)
            table.put(temp.id, temp.name)
            instances.put(temp.name, temp)
        }
        fun init() {
            addItem(1, "C", "牛乳", "营养全面的乳饮品,使用能回复从者的体力", true) {
            }
            addItem(10, "B", "硬糖", "水果味,能增加从者下次出战的攻击力", true) {
            }
            addItem(100, "A", "破防玉", "能破防的投掷玉,对敌人使用可以降低其防御力", true) {
            }
            addItem(1000, "S", "超级牛乳", "宝宝信赖,全家都爱,使用能增加从者体力上限", true) {
            }
            addItem(10000, "SS", "黑莲花", "使用能永久增加从者的攻击力,速度与体力上限", true) {
            }
            addItem(10001, "SS", "旧日灵媒", "呼唤旧日支配者所必须的媒介", true) {
            }
        }
    }
    val id = item_id
    val rarity = item_rarity
    val name = item_name
    val description = item_description
    val drawable = drawable
    val func = func
}