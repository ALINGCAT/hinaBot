package tools

class Key {
    companion object {
        private val intToKey = HashMap<Int, Key>()
        private val stringToKey = HashMap<String, Key>()
        fun combine(id: Int, name: String): Key {
            val key = Key()
            intToKey[id] = key
            stringToKey[name] = key
            return key
        }
        operator fun get(id: Int): Key? {
            return intToKey[id]
        }
        operator fun get(name: String): Key? {
            return stringToKey[name]
        }
    }
}