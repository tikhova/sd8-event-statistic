package event

import clock.Clock
import java.time.Duration
import java.time.Instant


class EventsStatisticImpl(private val clock: Clock): EventsStatistic {
    private var eventInstants: HashMap<String, MutableList<Instant>> = hashMapOf()

    override fun incEvent(name: String) {
        val instant = clock.instant()
        eventInstants.putIfAbsent(name, mutableListOf())
        eventInstants[name]?.add(instant)
    }

    override fun getEventStatisticByName(name: String): Double {
        removeOldEvents()
        val eventInstantsByName = eventInstants.getOrDefault(name, emptyList())

        return eventInstantsByName.size / 60.0
    }

    override fun getAllEventStatistic(): Map<String, Double> {
        removeOldEvents()

        return eventInstants.mapValues { (_, instants) -> instants.size / 60.0 }
    }

    override fun printStatistic() {
        getAllEventStatistic().forEach {entry -> println("${entry.key}: ${entry.value}")}
    }

    private fun removeOldEvents() {
        val requestInstant = clock.instant()
        val startInstant = requestInstant.minus(Duration.ofHours(1))
        eventInstants.forEach { (_, instants) -> instants.removeAll { instant -> instant <= startInstant } }
    }
}
