enum class State { OFF, ON }

enum class Pulse { HIGH, LOW }

data class Action(val from: Module, val to: Module, val pulse: Pulse)

abstract class Module(val label: String) {
    private val outputs = mutableListOf<Module>()

    fun addOutput(target: Module) {
        outputs.add(target)
    }

    fun getOutputs() = outputs.toList()

    abstract override fun toString(): String

    abstract fun pulse(a: Action): List<Action>

    fun sendToAllOutputs(pulse: Pulse): List<Action> {
        return buildList { outputs.forEach { m -> add(Action(this@Module, m, pulse)) } }
    }
}

class Flip(label: String) : Module(label) {
    private var state = State.OFF

    override fun toString(): String {
        return "Flip{$label} [$state]"
    }

    override fun pulse(a: Action): List<Action> {
        if (a.pulse == Pulse.HIGH) return listOf()

        // Pulse = LOW
        when (state) {
            State.OFF -> {
                state = State.ON
                return sendToAllOutputs(Pulse.HIGH)
            }

            State.ON -> {
                state = State.OFF
                return sendToAllOutputs(Pulse.LOW)
            }
        }
    }
}

class Conj(label: String) : Module(label) {
    private val inputsLast = mutableMapOf<Module, Pulse>()

    fun addInput(input: Module) {
        inputsLast[input] = Pulse.LOW
    }

    override fun toString(): String {
        return "Conj{$label} [${inputsLast.values.all { it == Pulse.HIGH }}]"
    }

    override fun pulse(a: Action): List<Action> {
        inputsLast[a.from] = a.pulse

        if (inputsLast.values.all { it == Pulse.HIGH }) {
            return sendToAllOutputs(Pulse.LOW)
        }

        return sendToAllOutputs(Pulse.HIGH)
    }
}

class Broadcaster : Module("broadcaster") {
    override fun toString() = "Broadcaster"

    override fun pulse(a: Action): List<Action> {
        return sendToAllOutputs(a.pulse)
    }
}

class Dummy(label: String) : Module(label) {
    override fun toString(): String {
        return "Dummy{$label}"
    }

    override fun pulse(a: Action): List<Action> {
        if (a.pulse == Pulse.LOW) throw Exception("Stop")
        return listOf()
    }
}

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

    var cntLowPulses = 0L
    var cntHighPulses = 0L

    for (buttonPress in 1..1000) {
        val queue = ArrayDeque<Action>()
        val broadcaster = modules.getOrElse("broadcaster") { throw Exception("Broadcaster not found!") }
        queue.addLast(Action(from = broadcaster, to = broadcaster, pulse = Pulse.LOW))

        while (queue.isNotEmpty()) {
            val action = queue.removeFirst()
            val module = action.to
            val processed = module.pulse(action)
            processed.forEach { queue.addLast(it) }
            if (action.pulse == Pulse.LOW) cntLowPulses += 1
            if (action.pulse == Pulse.HIGH) cntHighPulses += 1
        }
    }

    (cntLowPulses * cntHighPulses).println()
}
