package model

data class Info(val title: String,val type: String,val otherProperties: Map<String, String> = emptyMap(), val size: Double = 1.0)
data class NodeTree(val nodes: List<Node>, val links: List<NodeLink>)
data class Node(val name: String, val info: Info, val uuid: String)
data class NodeLink(val uuidA: String, val uuidB: String, val direction: LinkDirection)
enum class LinkDirection{ FORWARD, BACKWARD, BOTH }