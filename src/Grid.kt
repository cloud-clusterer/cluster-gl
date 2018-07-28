class Grid<T>(val size: Float){
    private val cells: MutableMap<CellLocation, MutableList<T>> = mutableMapOf()
    private val locations: MutableMap<T, Pair<CellLocation, Int>> = mutableMapOf()

    fun register(node: T, location: Vector3){
        val x = (location.x / size).toInt()
        val y = (location.y / size).toInt()
        val nextLocation = CellLocation(x, y)
        val existingEntry = locations[node]
        if(existingEntry?.first != nextLocation){
            if(existingEntry != null){
                cells[existingEntry.first]!!.removeAt(existingEntry.second)
                locations.remove(node)
            }
            with(cells.getOr(nextLocation, create = mutableListOf())){
                this.add(node)
                locations[node] = nextLocation to (this.size-1)
            }
        }
    }

    fun nodesNear(node: T): List<T> = with(locations[node]) {
        if (this != null) cellsNear(this.first).flatMap{ cells[it] ?: mutableListOf() }
        else emptyList()
    }

    private fun cellsNear(location: CellLocation) =
       ((location.x - 1) .. (location.x + 1)).flatMap{ x ->
            ((location.y - 1) .. (location.y + 1)).map { y -> CellLocation(x, y) }
        }
}

private class CellLocation(val x: Int, val y: Int)

private fun <T, R> MutableMap<T,R>.getOr(key: T, create: R): R{
    if(!this.containsKey(key)) this[key] = create
    return this[key]!!
}