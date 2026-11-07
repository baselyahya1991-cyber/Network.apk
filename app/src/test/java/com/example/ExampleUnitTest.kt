package com.example

import com.example.data.model.NetworkGen
import com.example.data.model.NetworkModeItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testNetworkGenerationBadges() {
    assertEquals("2G", NetworkGen.G2.label)
    assertEquals("3G", NetworkGen.G3.label)
    assertEquals("4G LTE", NetworkGen.G4.label)
    assertEquals("5G NR", NetworkGen.G5.label)
  }

  @Test
  fun testNetworkModesDefinitions() {
    val modes = listOf(
      NetworkModeItem(
        id = "mode_2g_only",
        titleAr = "2G فقط",
        titleEn = "2G Only",
        codeName = "GSM_ONLY",
        generation = NetworkGen.G2,
        descriptionAr = "",
        descriptionEn = "",
        systemModeValue = 1
      ),
      NetworkModeItem(
        id = "mode_4g_only",
        titleAr = "4G LTE فقط",
        titleEn = "4G LTE Only",
        codeName = "LTE_ONLY",
        generation = NetworkGen.G4,
        descriptionAr = "",
        descriptionEn = "",
        systemModeValue = 11
      ),
      NetworkModeItem(
        id = "mode_5g_only",
        titleAr = "5G فقط",
        titleEn = "5G Only",
        codeName = "NR_ONLY",
        generation = NetworkGen.G5,
        descriptionAr = "",
        descriptionEn = "",
        systemModeValue = 23
      )
    )

    assertEquals(3, modes.size)
    assertEquals(11, modes[1].systemModeValue)
    assertEquals(NetworkGen.G4, modes[1].generation)
    assertEquals(23, modes[2].systemModeValue)
    assertEquals(NetworkGen.G5, modes[2].generation)
  }
}
