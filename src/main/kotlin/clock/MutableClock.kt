package clock

import java.time.Duration
import java.time.Instant

class MutableClock: Clock {
    private var now = Instant.now()

    override fun instant(): Instant {
        return now
    }

    fun offset(offsetDuration: Duration) {
        this.now = now.plus(offsetDuration)
    }

    fun setInstant(now: Instant) {
        this.now = now
    }
}