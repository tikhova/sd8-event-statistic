package event

import clock.Clock
import java.time.Duration
import java.time.Instant


class EventsStatisticImpl(private val clock: Clock): EventsStatistic {
    private val eventInstants: HashMap<String, MutableList<Instant>> = hashMapOf()

    override fun incEvent(name: String) {
        val instant = clock.instant()
        eventInstants.putIfAbsent(name, mutableListOf())
        eventInstants[name]?.add(instant)
    }

    private fun getEventStatistic(instants: List<Instant>, requestInstant: Instant): Double {
        val startInstant = requestInstant.minus(Duration.ofHours(1))
        var eventCount = 0
        for (instant in instants) {
            if (instant > startInstant && instant <= requestInstant) {
                eventCount++
            }
        }

        return eventCount / 60.0
    }

    override fun getEventStatisticByName(name: String): Double {
        val instant = clock.instant()
        val eventInstantsByName = eventInstants.getOrDefault(name, emptyList())

        return getEventStatistic(eventInstantsByName, instant)
    }

    override fun getAllEventStatistic(): Map<String, Double> {
        val instant = clock.instant()

        return eventInstants.mapValues { (_, instants) -> getEventStatistic(instants, instant) }
    }

    override fun printStatistic() {
        getAllEventStatistic().forEach {entry -> println("${entry.key}: ${entry.value}")}
    }
}
