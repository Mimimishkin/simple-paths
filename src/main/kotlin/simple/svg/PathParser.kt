package simple.svg

import simple.svg.CommandType.*

internal fun parsePath(d: String): Path = tokenizePath(d)
    .insertMissingCommands()
    .splitInclusive { it.isCommand }
    .map { it[0].commandType to it.subList(1, it.size).map { arg -> arg.toFloat() } }
    .map { (commandType, args) -> commandType.makeCommand(args) }

private val commands = mapOf(*values().map { it.symbol to it }.toTypedArray(), 'z' to Close)

private val Char.isCommand get() = this in commands

private val String.isCommand get() = this[0].isCommand

private val String.commandType get() = commands[this[0]]!!

private val FLOAT_REGEX = Regex("[-+]?[0-9]*\\.?[0-9]+(?:[eE][-+]?[0-9]+)?")

private fun List<String>.insertMissingCommands(): List<String> {
    val newElements = this.toMutableList()
    if (newElements[0][0].let { it != MoveTo.symbol && it != MoveToRelative.symbol }) {
        newElements.addAll(0, listOf(MoveTo.symbol.toString(), "0", "0"))
    }

    val iterator = newElements.listIterator()
    var previous: CommandType = MoveTo

    while (iterator.hasNext()) {
        val next = iterator.next()

        if (next.isCommand) {
            val commandType = next.commandType
            repeat(commandType.argumentsCount) { iterator.next() }
            previous = commandType
        } else {
            iterator.previous()
            iterator.add(previous.next().symbol.toString())
            iterator.previous()
        }
    }

    return newElements
}

private fun splitArguments(c: String): List<String> {
    val tokenRanges = mutableListOf<IntRange>()

    tokenRanges += FLOAT_REGEX.findAll(c).map { it.range }

    if (tokenRanges.size > 0 && tokenRanges[0].first > 0) {
        tokenRanges.add(0, 0 until tokenRanges[0].first)
    } else if (tokenRanges.size == 0) {
        tokenRanges += c.indices
    }

    return tokenRanges.map { c.slice(it).trim() }
}

private fun tokenizePath(d: String): List<String> {
    // There's no regex split in kotlin that preserves the string being found,
    // so we have to operate on substrings. There may very well be a faster way
    val commandRanges = mutableListOf<IntRange>()
    var i = 0
    for (j in d.indices) {
        if (d[j].isCommand) {
            commandRanges += i until j
            i = j
        }
    }
    commandRanges += i until d.length

    // If the first character was a command, leave that 0-length range out
    if (commandRanges.size >= 1 && commandRanges[0] == IntRange(0, -1)) {
        commandRanges.removeAt(0)
    }

    return commandRanges.map { splitArguments(d.slice(it)) }.flatten()
}