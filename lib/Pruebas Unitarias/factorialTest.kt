import LispEvaluator.EvaluatorException
import org.junit.Assert
import org.junit.Test
import java.util.*

class FactorialTest {
    @Test
    @Throws(Exception::class)
    fun testFactorialCero() {
        val factorial: factorial = factorial()
        val result: Any = factorial.apply(mutableListOf(0) as List<Any>?)
        // Se espera que 0! sea 1
        Assert.assertEquals(1, result)
    }

    @Test
    @Throws(Exception::class)
    fun testFactorialCinco() {
        val factorial: factorial = factorial()
        val result: Any = factorial.apply(mutableListOf(5) as List<Any>?)
        // Se espera que 5! sea 120
        Assert.assertEquals(120, result)
    }

    @Test(expected = EvaluatorException::class)
    @Throws(Exception::class)
    fun testFactorialNegativo() {
        val factorial: factorial = factorial()
        // Se espera que al pasar un número negativo se lance una excepción
        factorial.apply(Arrays.asList(-1) as List<Any>?)
    }
}