import org.junit.Assert
import org.junit.Test

class FahrenheitTest {
    @Test
    @Throws(Exception::class)
    fun testFahrenheitCero() {
        val fahrenheit: fahrenheit = fahrenheit()
        val result: Any = fahrenheit.apply(mutableListOf(0) as List<Any>?)
        // Convertir 0°C a Fahrenheit: se espera 32°F
        Assert.assertEquals(32.0, (result as Double), 0.0001)
    }

    @Test
    @Throws(Exception::class)
    fun testFahrenheitCien() {
        val fahrenheit: fahrenheit = fahrenheit()
        val result: Any = fahrenheit.apply(mutableListOf(100) as List<Any>?)
        // Convertir 100°C a Fahrenheit: se espera 212°F
        Assert.assertEquals(212.0, (result as Double), 0.0001)
    }
}