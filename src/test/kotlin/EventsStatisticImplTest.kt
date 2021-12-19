import clock.MutableClock
import event.EventsStatisticImpl
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Duration

class EventsStatisticImplTest {
    private lateinit var clock: MutableClock
    private lateinit var eventsStatistic: EventsStatisticImpl

    @Before
    fun setup() {
        clock = MutableClock()
        eventsStatistic = EventsStatisticImpl(clock)
    }

    @Test
    fun getEventStatisticByNameZeroTest() {
        assertEquals(0.0, eventsStatistic.getEventStatisticByName("test"), 0.0)
    }

    @Test
    fun getEventStatisticByNameMultipleInstantsTest() {
        val count = (1..10).random()
        for (i in 1..count) {
            eventsStatistic.incEvent("test")
        }

        assertEquals(count / 60.0, eventsStatistic.getEventStatisticByName("test"), 0.0)
    }

    @Test
    fun getEventStatisticByNameMultipleEventsTest() {
        val count1 = (1..10).random()
        val count2 = (1..10).random()
        val count3 = (1..10).random()

        for (i in 1..count1) {
            eventsStatistic.incEvent("test1")
        }
        for (i in 1..count2) {
            eventsStatistic.incEvent("test2")
        }
        for (i in 1..count3) {
            eventsStatistic.incEvent("test3")
        }

        assertEquals(count1 / 60.0, eventsStatistic.getEventStatisticByName("test1"), 0.0)
        assertEquals(count2 / 60.0, eventsStatistic.getEventStatisticByName("test2"), 0.0)
        assertEquals(count3 / 60.0, eventsStatistic.getEventStatisticByName("test3"), 0.0)
    }

    @Test
    fun getEventStatisticByNameOffsetTest() {
        eventsStatistic.incEvent("test1")
        clock.offset(Duration.ofHours(1))

        eventsStatistic.incEvent("test2")
        clock.offset(Duration.ofMinutes(30))

        assertEquals(0.0, eventsStatistic.getEventStatisticByName("test1"), 0.0)
        assertEquals(1.0 / 60, eventsStatistic.getEventStatisticByName("test2"), 0.0)
    }

    @Test
    fun getAllEventStatisticTest() {
        val count1 = (1..10).random()
        val count2 = (1..10).random()
        val count3 = (1..10).random()

        for (i in 1..count1) {
            eventsStatistic.incEvent("test1")
        }
        for (i in 1..count2) {
            eventsStatistic.incEvent("test2")
        }
        for (i in 1..count3) {
            eventsStatistic.incEvent("test3")
        }


        val expectedMap = hashMapOf(Pair("test1", count1 / 60.0), Pair("test2", count2 / 60.0), Pair("test3", count3 / 60.0))
        assertEquals(expectedMap, eventsStatistic.getAllEventStatistic())
    }
}