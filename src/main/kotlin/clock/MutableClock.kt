package clock

import java.time.Duration
import java.time.Instant

class MutableClock: Clock {
    private val now = Instant.now()

    override fun instant(): Instant {
        return now
    }

    fun offset(offsetDuration: Duration) {
        now.plus(offsetDuration)
    }
}