# jackson-kotlin-test

This repo is a demonstration of an issue with the serialization of POJO into json string by the object mapper in Jackson-databind, since v11.

When one of the attributes is a "is getter", e.g. "isUSDListing", and the letters after the "is" are consecutively upper-cased, e.g. "isUSDListing" instead of "isUsdListing", The serialized result is not correct.

## Example

**POJO:**

data class SampleWithIsGetter (
    val isUSDListing: Boolean
)

**Expected Result:**

{ isUSDListing: true }

**Actual Result:**

{ usdListing: true }

