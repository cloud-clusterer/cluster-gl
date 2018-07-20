data class Node(
        val name: String,
        val stroke: Stroke,
        val fill: Fill,
        val text: String,
        val properties: Map<String, String> = emptyMap()
)