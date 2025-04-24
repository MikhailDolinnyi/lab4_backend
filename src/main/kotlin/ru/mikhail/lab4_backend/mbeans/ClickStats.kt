package ru.mikhail.lab4_backend.mbeans

class ClickStats: ClickStatsMBean {

    private var lastClickTime: Long = -1
    private var totalInterval: Long = 0
    private var count: Int = 0

    fun registerClick(){

        val now = System.currentTimeMillis()
        if(lastClickTime != -1L){
            totalInterval += now - lastClickTime
            count++
        }
        lastClickTime = now
    }

    override fun getAverageClickInterval(): Double {
        return if (count == 0) 0.0 else totalInterval / count.toDouble()
    }
}