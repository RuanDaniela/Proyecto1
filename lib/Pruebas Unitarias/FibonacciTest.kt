import org.junit.Assert
import org.junit.Test

class FibonacciTest {
    @Test
    @Throws(Exception::class)
    fun testFibonacciCero() {
        val fibonacci = Fibonacci()
        val result = fibonacci.apply(mutableListOf<Any>(0))
        // Según la implementación: F(0) = 0
        Assert.assertEquals(0, result)
    }

    @Test
    @Throws(Exception::class)
    fun testFibonacciUno() {
        val fibonacci = Fibonacci()
        val result = fibonacci.apply(mutableListOf<Any>(1))
        // Se espera que F(1) = 1
        Assert.assertEquals(1, result)
    }

    @Test
    @Throws(Exception::class)
    fun testFibonacciCinco() {
        val fibonacci = Fibonacci()
        val result = fibonacci.apply(mutableListOf<Any>(5))
        // Se espera que F(5) = 5 (0, 1, 1, 2, 3, 5)
        Assert.assertEquals(5, result)
    }

    @Test
    @Throws(Exception::class)
    fun testFibonacciSiete() {
        val fibonacci = Fibonacci()
        val result = fibonacci.apply(mutableListOf<Any>(7))
        // Se espera que F(7) = 13 (0, 1, 1, 2, 3, 5, 8, 13)
        Assert.assertEquals(13, result)
    }
}