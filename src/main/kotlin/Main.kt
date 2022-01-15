import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.assertThrows

data class SampleWithIsGetter (
    val isUSDListing: Boolean
)

fun main() {
    // Example to show that jackson serialization will fail when both conditions are fulfilled
    // 1. attribute name starts with a "is"
    // 2. attribute name after the "is" has continuous capital letters
    // The serialized json shows attribute name as "usdListing" instead of "isUSDListing"
    runFailed()

    // Workaround solution is to set the MapperFeature - USE_STD_BEAN_NAMING to true
    // USE_STD_BEAN_NAMING will ensure attribute naming will ensure attribute name will not
    // be lower-cased if it is followed by upper-cased letters.
    // Meaning, getURL() will return "URL" instead of "url"
    // USE_STD_BEAN_NAMING is false by default for backward compatibility
    runWorkaround()
}

fun runFailed(){
    val sample = SampleWithIsGetter(true)
    val json = jacksonObjectMapper().writeValueAsString(sample)
    println(
        """
            Example to show that jackson serialization will fail when both conditions are fulfilled
            1. attribute name starts with a "is"
            2. attribute name after the "is" has continuous capital letters
            The serialized json shows attribute name as "usdListing" instead of "isUSDListing"
        """.trimIndent()
    )
    println(json)
    assertThrows<MismatchedInputException> { jacksonObjectMapper().readValue(json, SampleWithIsGetter::class.java) }
}


fun runWorkaround(){
    val sample = SampleWithIsGetter(true)
    val objectMapper = jacksonMapperBuilder().configure(MapperFeature.USE_STD_BEAN_NAMING, true).build()
    val json = objectMapper.writeValueAsString(sample)
    val deserialized = objectMapper.readValue(json, SampleWithIsGetter::class.java)

    println(
        """
            
            Workaround solution is to set the MapperFeature - USE_STD_BEAN_NAMING to true
            USE_STD_BEAN_NAMING will ensure attribute naming will ensure attribute name will not
            be lower-cased if it is followed by upper-cased letters.
            Meaning, getURL() will return "URL" instead of "url"
            USE_STD_BEAN_NAMING is false by default for backward compatibility
        """.trimIndent()
    )

    println(json)
    assertThat(sample, equalTo(deserialized))
}