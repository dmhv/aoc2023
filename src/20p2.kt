import kotlin.system.exitProcess

fun main() {
    val lines = readInput("20")
    val modules = mutableMapOf<String, Module>()

    // filling out modules in the first pass
    for (line in lines) {
        val (moduleStr, _) = line.split(" -> ")

        if (moduleStr[0] == '%') {
            val moduleLabel = moduleStr.drop(1)
            modules[moduleLabel] = Flip(moduleLabel)
        } else if (moduleStr[0] == '&') {
            val moduleLabel = moduleStr.drop(1)
            modules[moduleLabel] = Conj(moduleLabel)
        } else {
            assert(moduleStr == "broadcaster") { "Unexpected module $moduleStr received!" }
            modules[moduleStr] = Broadcaster()
        }
    }

    // filling out module outputs in the second pass
    for (line in lines) {
        val (moduleStr, outputsStr) = line.split(" -> ")

        val moduleName = if (moduleStr[0] in "%&") moduleStr.drop(1) else moduleStr
        val thisModule = modules.getOrElse(moduleName) { throw Exception("Module $moduleName not found!") }
        val outputsSplit = outputsStr.split(", ")

        val dummies = outputsSplit.filter { it !in modules }
        dummies.forEach { modules[it] = Dummy(it) }

        val outputs = outputsSplit.map { modules.getOrElse(it) { throw Exception("Module $it not found!") } }
        outputs.forEach { thisModule.addOutput(it) }

        // need to attach inputs to the Conjunctors
        val conjunctors = outputs.filterIsInstance<Conj>()
        conjunctors.forEach { it.addInput(thisModule) }
    }

    val dummy = modules.values.first { it is Dummy }
    val conjunctor = modules.values.first { it is Conj && dummy in it.getOutputs() }
    val conjunctorInputs = modules.values.filter { conjunctor in it.getOutputs() }
    val conjunctorInputCycleLengths = mutableMapOf<Module, Long>()

    var buttonPress = 0L
    while (true) {
        buttonPress += 1
        val queue = ArrayDeque<Action>()
        val broadcaster = modules.getOrElse("broadcaster") { throw Exception("Broadcaster not found!") }
        queue.addLast(Action(from = broadcaster, to = broadcaster, pulse = Pulse.LOW))

        while (queue.isNotEmpty()) {
            val action = queue.removeFirst()

            // store cycle lengths, exit if done
            if (action.to == conjunctor && action.pulse == Pulse.HIGH) {
                if (conjunctorInputCycleLengths.size == conjunctorInputs.size) {
                    conjunctorInputCycleLengths.values.reduce { acc, i -> lcm(acc, i) }.println()
                    exitProcess(0)
                }
                conjunctorInputCycleLengths[action.from] = buttonPress
            }

            val module = action.to
            val processed = module.pulse(action)
            processed.forEach { queue.addLast(it) }
        }
    }
}
