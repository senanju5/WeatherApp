package com.example.weatherapp

import com.example.weatherapp.utils.CitiUtils.isValidStateOrCity
import org.junit.Test

import org.junit.Assert.*

/**
 * Test class to check citi utils function
 */
class CityTest {
        @Test
        fun testCityContainsQuery() {
            assertFalse(isValidStateOrCity("new"))
            assertFalse(isValidStateOrCity( "los"))
            assertTrue(isValidStateOrCity( "Dallas"))
            assertTrue(isValidStateOrCity( "phoenix"))
            assertFalse(isValidStateOrCity( "Miami"))
        }

        @Test
        fun testCaseSensitivity() {
            assertTrue(isValidStateOrCity("CHICAGO"))
            assertFalse(isValidStateOrCity( "chicagoa"))
        }
    }
