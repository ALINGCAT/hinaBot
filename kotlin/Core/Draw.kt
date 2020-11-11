package Core

class Draw {
    companion object {
        val pool = HashMap<String, MutableSet<Item>>()
        fun init() {
            pool.put("SS", mutableSetOf())
            pool.put("S", mutableSetOf())
            pool.put("A", mutableSetOf())
            pool.put("B", mutableSetOf())
            pool.put("C", mutableSetOf())
            for (e in Item.instances) if (e.value.drawable) pool[e.value.rarity]?.add(e.value)
        }
    }
}
