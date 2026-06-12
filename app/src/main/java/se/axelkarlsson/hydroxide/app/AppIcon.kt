package se.axelkarlsson.hydroxide.app

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toDrawable
import se.axelkarlsson.hydroxide.APP_ICON_DEFAULT_FOREGROUND_SCALING_FACTOR
import se.axelkarlsson.hydroxide.APP_ICON_DEFAULT_ROUNDING_FACTOR

private fun scaled(drawable: Drawable, factor: Float, resources: Resources): Drawable {
    val width = drawable.intrinsicWidth
    val height = drawable.intrinsicHeight

    if (width < 0 || height < 0) {
        return drawable
    }

    val px = width / 2f
    val py = height / 2f

    val bitmap = createBitmap(width, height)

    val matrix = Matrix().apply {
        setScale(factor, factor, px, py)
    }

    val canvas = Canvas(bitmap).apply {
        setMatrix(matrix)
    }

    drawable.setBounds(0, 0, width, height)
    drawable.draw(canvas)

    return bitmap.toDrawable(resources)
}

class AppIcon(
    private val context: Context,
    val source: Drawable,
    val roundedness: Float = APP_ICON_DEFAULT_ROUNDING_FACTOR,
    val scaling: Float = APP_ICON_DEFAULT_FOREGROUND_SCALING_FACTOR
) {
    val drawable: Drawable
        get() {
            val bitmap: Bitmap = bitmap ?: return source
            return bitmap.toDrawable(context.resources)
        }

    val bitmap: Bitmap?
        get() {
            var bitmap: Bitmap? = if (source is BitmapDrawable) {
                source.bitmap
            } else {
                null
            }

            if (source is AdaptiveIconDrawable) {
                val layers =
                    arrayOf(
                        source.background,
                        scaled(source.foreground, scaling, context.resources)
                    )
                val layer = LayerDrawable(layers)

                val width = layer.intrinsicWidth
                val height = layer.intrinsicHeight

                if (width < 0 || height < 0) {
                    return null
                }

                val tmp = createBitmap(width, height)
                val canvas = Canvas(tmp)

                layer.setBounds(0, 0, canvas.width, canvas.height)
                layer.draw(canvas)

                bitmap = tmp
            }

            return bitmap
        }

    val rounded: Bitmap?
        get() {
            val tmp: Bitmap = bitmap ?: return null

            if (tmp.width < 0 || tmp.height < 0) {
                return null
            }

            val bitmap = createBitmap(tmp.width, tmp.height)
            val canvas = Canvas(bitmap)

            val rounded = RoundedBitmapDrawableFactory.create(context.resources, tmp)
            val radius = tmp.width * roundedness

            rounded.cornerRadius = radius

            rounded.setBounds(0, 0, canvas.width, canvas.height)
            rounded.draw(canvas)

            return bitmap
        }
}