package core

class Draw {
    companion object {
        val pool = HashMap<String, MutableSet<Item>>()
        fun init() {
            pool["SS"] = mutableSetOf()
            pool["S"] = mutableSetOf()
            pool["A"] = mutableSetOf()
            pool["B"] = mutableSetOf()
            pool["C"] = mutableSetOf()
            for (e in Item.instances) if (e.value.drawable) pool[e.value.rarity]?.add(e.value)
        }
    }
}
