package com.chizhikov.contactstest.unit

import androidx.test.filters.SmallTest
import com.chizhikov.contactstest.extensions.validateQuery
import org.junit.Test

import org.junit.Assert.*

@SmallTest
class SearchQueryValidatorTest {
    @Test
    fun `Empty query returns true`(){
        assertTrue(validateQuery(""))
    }

    @Test
    fun `Null query returns false`(){
        assertFalse(validateQuery(null))
    }

    @Test
    fun `Correct lowercase name returns true`(){
        assertTrue(validateQuery("pasha"))
    }

    @Test
    fun `Correct uppercase name returns true`(){
        assertTrue(validateQuery("PASHA"))
    }

    @Test
    fun `Correct simple name returns true`(){
        assertTrue(validateQuery("Pasha"))
    }

    @Test
    fun `Name with invalid characters returns false`(){
        assertFalse(validateQuery("/Pasha*"))
    }

    @Test
    fun `Correct name with dot returns true`(){
        assertTrue(validateQuery("Robert Jr."))
    }

    @Test
    fun `Correct long name returns false`(){
        assertTrue(validateQuery("Hubert Blaine Wolfeschlegelsteinhausenbergerdorff Sr. "))
    }

    @Test
    fun `Correct Russian name returns true`(){
        assertTrue(validateQuery("Паша"))
    }

    @Test
    fun `Correct US name returns true`(){
        assertTrue(validateQuery("Pashka"))
    }

    @Test
    fun `Correct simple number returns true`(){
        assertTrue(validateQuery("8911-904-73-41"))
    }

    @Test
    fun `Number with letters returns false`(){
        assertFalse(validateQuery("020-368a-6884"))
    }

    @Test
    fun `Number with invalid characters returns false`(){
        assertFalse(validateQuery("020-368****-6884"))
    }

    @Test
    fun `Correct Russian number returns true`(){
        assertTrue(validateQuery("+7(911)-904-73-41"))
    }

    @Test
    fun `Correct US number returns true`(){
        assertTrue(validateQuery("020-368-6884"))
    }
}
