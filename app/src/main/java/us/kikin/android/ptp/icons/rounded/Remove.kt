/*
 * Copyright 2024
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

public val Rounded.Remove: ImageVector
    get() {
        if (_Remove != null) {
            return _Remove!!
        }
        _Remove =
            ImageVector.Builder(
                name = "Remove",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 960f,
                viewportHeight = 960f,
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
                    pathFillType = PathFillType.NonZero,
                ) {
                    moveTo(240f, 520f)
                    quadToRelative(-17f, 0f, -28.5f, -11.5f)
                    reflectiveQuadTo(200f, 480f)
                    reflectiveQuadToRelative(11.5f, -28.5f)
                    reflectiveQuadTo(240f, 440f)
                    horizontalLineToRelative(480f)
                    quadToRelative(17f, 0f, 28.5f, 11.5f)
                    reflectiveQuadTo(760f, 480f)
                    reflectiveQuadToRelative(-11.5f, 28.5f)
                    reflectiveQuadTo(720f, 520f)
                    close()
                }
            }.build()
        return _Remove!!
    }

private var _Remove: ImageVector? = null
