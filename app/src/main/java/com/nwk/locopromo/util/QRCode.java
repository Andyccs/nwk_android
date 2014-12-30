package com.nwk.locopromo.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
/**
 * Created by andyccs on 31/12/14.
 */
public class QRCode {

    public static Bitmap generate(int width, int height, String content) throws WriterException, IOException {
        BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE,width, height);
        Bitmap bitmap = writeToStream(bitMatrix);

        return bitmap;
    }

    public static Bitmap writeToStream(BitMatrix matrix){

        int width = matrix.getWidth();
        int height = matrix.getHeight();

        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        Paint onColor = new Paint();
        onColor.setARGB(255,0,0,0);
        Paint offColor = new Paint();
        offColor.setARGB(255,255,255,255);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                c.drawPoint(x,y, matrix.get(x, y) ? onColor : offColor);
            }
        }
        return b;
    }
}
