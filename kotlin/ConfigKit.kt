import java.io.File

class ConfigKit {
    companion object {
        fun getConfig(path:CharSequence):HashMap<String, String> {
            val table = HashMap<String,String>()
            File(path.toString()).forEachLine {
                val temp = it.split('=')
                table.put(temp.get(0),temp.get(1))
            }
            return table
        }
    }
}