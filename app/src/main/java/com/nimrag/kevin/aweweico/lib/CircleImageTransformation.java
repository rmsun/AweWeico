package com.nimrag.kevin.aweweico.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

/**
 * Created by kevin on 2017/3/8.
 */

public class CircleImageTransformation implements Transformation {
    // A unique key for the transformation, used for caching purposes.
    private static final String KEY = "circleImageTransformation";

    @Override
    public Bitmap transform(Bitmap source) {

        int minEdge = Math.min(source.getWidth(), source.getHeight());
        int dx = (source.getWidth() - minEdge) / 2;
        int dy = (source.getHeight() - minEdge) / 2;

        // init shader
        Shader shader = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        matrix.setTranslate(-dx, -dy);
        shader.setLocalMatrix(matrix);

        // init paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(shader);

        // create and draw circle bitmap
        Bitmap output = Bitmap.createBitmap(minEdge, minEdge, source.getConfig());
        Canvas canvas = new Canvas(output);
        canvas.drawOval(new RectF(0, 0, minEdge, minEdge), paint);

        // recycle the source bitmap, because we already generate a new one
        source.recycle();

        return output;
    }

    @Override
    public String key() {
        return KEY;
    }
}
