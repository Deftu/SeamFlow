package dev.deftu.seamflow.desktop.ui

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

object DefignColors {
    val Primary = Color(0xFFC33F3F)
    val Primary2 = Color(0xFFA63A3A)

    val DarkBg1 = Color(0xFF28282B)
    val DarkBg2 = Color(0xFF212125)
    val LightBg1 = Color(0xFFDDDDDD)
    val LightBg2 = Color(0xFFD1D1D1)

    val DarkText1 = Color(0xFFFDFBF9)
    val LightText1 = Color(0xFF28282B)

    val Error = Color(0xFFC92A43)

    val Light = lightColors(
        primary = Primary,
        primaryVariant = Primary2,
        secondary = Primary2,
        secondaryVariant = Primary2,

        background = LightBg1,
        surface = LightBg2,

        error = Error,

        onPrimary = DarkText1,
        onSecondary = DarkText1,
        onBackground = LightText1,
        onSurface = LightText1,
        onError = DarkText1,
    )

    val Dark = darkColors(
        primary = Primary,
        primaryVariant = Primary2,
        secondary = Primary2,
        secondaryVariant = Primary2,

        background = DarkBg2,
        surface = DarkBg1,

        error = Error,

        onPrimary = DarkText1,
        onSecondary = DarkText1,
        onBackground = DarkText1,
        onSurface = DarkText1,
        onError = DarkText1,
    )
}
