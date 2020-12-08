package core.item

class Consumables(Rarity: String, Name: String, Description: String)
    : Item(Rarity, Name, "消耗品", Description) {
    companion object {
        var index = 30000
        fun init() {
            Consumables("A", "牛奶",
                    "北野武(划去),可以赠与猫猫").toItem()
        }
    }
    override val id: Int = index++
}