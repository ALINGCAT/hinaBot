package core.item

import tools.Key

abstract class Item(Rarity: String, Name: String, Type: String, Description: String) {
    companion object {
        val instances = HashMap<Key, Item>()
        val drawPool = mapOf<String, MutableSet<Item>>(
                "SS" to mutableSetOf(),
                "S" to mutableSetOf(),
                "A" to mutableSetOf(),
                "B" to mutableSetOf(),
                "C" to mutableSetOf()
        )
        fun init() {
            Consumables.init()
            Materials.init()
            Other.init()
        }
    }
    abstract val id: Int
    val rarity = Rarity
    val name = Name
    val type = Type
    val description = Description
    fun toItem(drawable: Boolean = false): Item {
        instances[Key.combine(this.id, this.name)] = this
        if (drawable) when(this.rarity) {
            "SS" -> drawPool["SS"]?.add(this)
            "S" -> drawPool["S"]?.add(this)
            "A" -> drawPool["A"]?.add(this)
            "B" -> drawPool["B"]?.add(this)
            "C" -> drawPool["C"]?.add(this)
        }
        return this
    }
    override fun toString(): String {
        return "[${rarity}]${name}"
    }
}