package simple.svg

val Path.reversed: Path
    get() {
    val originalPaths = simplified.subPaths

    val reversedPaths = originalPaths.asReversed().map { commands ->
        val allArguments = commands.flatMap { it.arguments }.asReversed()
        val  types = commands
            .map { it.type }
            .splitInclusive { it == CommandType.MoveTo }
            .asReversed()
            .map { it.asReversed() }
            .map { if (it[0] == CommandType.Close) it.toMutableList().apply { add(lastIndex, CommandType.Close) }.drop(1) else it }
            .flatMap { listOf(CommandType.MoveTo) + it.dropLast(1) }

        var used = 0
        types.map { type ->
            val range = used until  (used + type.argumentsCount)
            used += type.argumentsCount
            type.makeCommand(allArguments.slice(range))
        }
    }

    return reversedPaths.fullPath
}