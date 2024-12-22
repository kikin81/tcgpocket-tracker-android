package us.kikin.android.ptp.icons.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import us.kikin.android.ptp.icons.PtpIcons.Rounded

public val Rounded.FilterList: ImageVector
    get() {
        if (_Filter_list != null) {
            return _Filter_list!!
        }
        _Filter_list = ImageVector.Builder(
            name = "Filter_list",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(440f, 720f)
                quadToRelative(-17f, 0f, -28.5f, -11.5f)
                reflectiveQuadTo(400f, 680f)
                reflectiveQuadToRelative(11.5f, -28.5f)
                reflectiveQuadTo(440f, 640f)
                horizontalLineToRelative(80f)
                quadToRelative(17f, 0f, 28.5f, 11.5f)
                reflectiveQuadTo(560f, 680f)
                reflectiveQuadToRelative(-11.5f, 28.5f)
                reflectiveQuadTo(520f, 720f)
                close()
                moveTo(280f, 520f)
                quadToRelative(-17f, 0f, -28.5f, -11.5f)
                reflectiveQuadTo(240f, 480f)
                reflectiveQuadToRelative(11.5f, -28.5f)
                reflectiveQuadTo(280f, 440f)
                horizontalLineToRelative(400f)
                quadToRelative(17f, 0f, 28.5f, 11.5f)
                reflectiveQuadTo(720f, 480f)
                reflectiveQuadToRelative(-11.5f, 28.5f)
                reflectiveQuadTo(680f, 520f)
                close()
                moveTo(160f, 320f)
                quadToRelative(-17f, 0f, -28.5f, -11.5f)
                reflectiveQuadTo(120f, 280f)
                reflectiveQuadToRelative(11.5f, -28.5f)
                reflectiveQuadTo(160f, 240f)
                horizontalLineToRelative(640f)
                quadToRelative(17f, 0f, 28.5f, 11.5f)
                reflectiveQuadTo(840f, 280f)
                reflectiveQuadToRelative(-11.5f, 28.5f)
                reflectiveQuadTo(800f, 320f)
                close()
            }
        }.build()
        return _Filter_list!!
    }

private var _Filter_list: ImageVector? = null
