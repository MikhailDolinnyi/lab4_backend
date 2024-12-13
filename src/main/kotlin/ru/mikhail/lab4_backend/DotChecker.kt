package ru.mikhail.lab4_backend

object DotChecker {

    fun checkDot(x: Float, y: Float, r: Float): Boolean {
        return checkFirstQuarter(x, y, r) || checkSecondQuarter(x, y, r) || checkFourthQuarter(x, y, r)
    }

    private fun checkFirstQuarter(x: Float, y: Float, r: Float): Boolean {
        return if (x >= 0 && y >= 0) {
            // Треугольник с основанием и высотой R/2
            y <= (-2 * x + r) // Уравнение прямой y = -2x + R
        } else {
            false
        }
    }

    private fun checkSecondQuarter(x: Float, y: Float, r: Float): Boolean {
        return if (x <= 0 && y >= 0) {
            // Полукруг радиуса R
            x * x + y * y <= r * r // Уравнение круга x^2 + y^2 = R^2
        } else {
            false
        }
    }

    private fun checkFourthQuarter(x: Float, y: Float, r: Float): Boolean {
        return if (x >= 0 && y <= 0) {
            // Rectangle with dimensions R/2 x R
            x <= r && y >= -r/2
        } else {
            false
        }
    }

}
