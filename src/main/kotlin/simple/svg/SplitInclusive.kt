package simple.svg

internal fun <T> Iterable<T>.splitInclusive(addToNext: Boolean = true, predicate: (T) -> Boolean): List<List<T>> {
    var subList = mutableListOf<T>()

    val split = fold(mutableListOf<List<T>>()) { list, current ->
        if (predicate(current)) {
            if (addToNext) {
                list += subList
                subList = mutableListOf(current)
            } else {
                list += subList + current
                subList = mutableListOf()
            }
        } else {
            subList += current
        }

        list
    }

    if (split.isNotEmpty() && split.first().isEmpty())
        split.removeAt(0)

    if (subList.isNotEmpty())
        split += subList

    return split
}