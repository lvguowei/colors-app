package com.guowei.colorsapp.ui.colors

import android.graphics.Color
import com.google.common.truth.Truth.assertThat
import com.guowei.colorsapp.usecase.Colors

import org.junit.Test

class ColorsUiModelTest {

    @Test
    fun `Empty ColorsUiModel tests`() {
        val SUT = ColorsUiModel.Empty
        assertThat(SUT.currentIndex).isEqualTo(-1)
        assertThat(SUT.next).isNull()
        assertThat(SUT.previous).isNull()
        assertThat(SUT.bgColor).isEqualTo(Color.WHITE)
        assertThat(SUT.colorSet).isNull()
        assertThat(SUT.currentColorLocal).isNull()
        assertThat(SUT.currentColorServer).isNull()
    }

    @Test
    fun `ColorsUiModel tests - current color in the middle`() {
        val currentColorServer = Colors.colorSet[2]
        val currentColorLocal = Colors.colorSet[3]
        val SUT = ColorsUiModel(currentColorServer, currentColorLocal, Colors.colorSet)

        assertThat(SUT.currentIndex).isEqualTo(3)

        assertThat(SUT.next).isEqualTo(
            ColorsUiModel(
                currentColorServer,
                Colors.colorSet[4],
                Colors.colorSet
            )
        )

        assertThat(SUT.previous).isEqualTo(
            ColorsUiModel(
                currentColorServer,
                Colors.colorSet[2],
                Colors.colorSet
            )
        )
    }

    @Test
    fun `ColorsUiModel tests - current color is last`() {
        val currentColorServer = Colors.colorSet[2]
        val currentColorLocal = Colors.colorSet.last()
        val SUT = ColorsUiModel(currentColorServer, currentColorLocal, Colors.colorSet)

        // test next color
        assertThat(SUT.next).isNull()

        // test previous color
        assertThat(SUT.previous).isEqualTo(
            ColorsUiModel(
                currentColorServer,
                Colors.colorSet[Colors.colorSet.size - 2],
                Colors.colorSet
            )
        )
    }

    @Test
    fun `ColorsUiModel tests - current color is first`() {
        val currentColorServer = Colors.colorSet[2]
        val currentColorLocal = Colors.colorSet.first()
        val SUT = ColorsUiModel(currentColorServer, currentColorLocal, Colors.colorSet)

        // test next color
        assertThat(SUT.next).isEqualTo(
            ColorsUiModel(
                currentColorServer,
                Colors.colorSet[1],
                Colors.colorSet
            )
        )

        // test previous color
        assertThat(SUT.previous).isNull()
    }
}