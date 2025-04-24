package ru.mikhail.lab4_backend.mbeans

import ru.mikhail.lab4_backend.DotChecker
import javax.management.Notification
import javax.management.NotificationBroadcasterSupport

class DotStats : DotStatsMBean, NotificationBroadcasterSupport() {

    private var total = 0
    private var missed = 0
    private var sequenceNumber = 1L

    override fun getTotalDots() = total
    override fun getMissedDots() = missed

    fun registerDot(x: Float, y: Float, r: Float) {
        total++
        if (!DotChecker.checkDot(x, y, r)) missed++
    }

    fun sendOutOfBoundsNotification(x: Float, y: Float, r: Float) {
        sendNotification(
            Notification(
                "Dot out of bounds",
                this,
                sequenceNumber++,
                System.currentTimeMillis(),
                "Dot ($x,$y,$r) is out of allowed bounds"
            )
        )
    }
}
