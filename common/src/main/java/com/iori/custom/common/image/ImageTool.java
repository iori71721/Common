package com.iori.custom.common.image;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

public class ImageTool {
    /**
     * ref https://codertw.com/android-%E9%96%8B%E7%99%BC/353563/
     * @param context
     * @param uri
     * @return
     */
    public static String parsePath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
// DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
// ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
// DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);

                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
// MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
// MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
// File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static int calcInSampleSize(int srcWidth,int srcHeight,int outputWidth, int outputHeight){
        int inSampleSize = 1;
        if (srcWidth > outputHeight && srcWidth > outputWidth) {
            inSampleSize  = srcWidth /outputWidth;
        } else if(srcWidth <outputHeight  && srcHeight >outputHeight  ){
            inSampleSize  = srcHeight /outputHeight ;
        }

        if(inSampleSize  <=0){
            inSampleSize  =1;
        }
        return inSampleSize;
    }

    public static @Nullable Bitmap readBitmapInSizeByPath(String path, int outputWidth, int outputHeight){
        Bitmap readBitmap=null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;
        int inSampleSize = com.iori.custom.common.image.ImageTool.calcInSampleSize(srcWidth,srcHeight,outputWidth,outputHeight);

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        readBitmap=BitmapFactory.decodeFile(path, options);

        return readBitmap;
    }

    public static @Nullable Bitmap resizeBitmap(Bitmap bitmap,int outputWidth, int outputHeight){
        if(bitmap == null){
            return bitmap;
        }

        int inSampleSize = com.iori.custom.common.image.ImageTool.calcInSampleSize(bitmap.getWidth(),bitmap.getHeight()
                ,outputWidth,outputHeight);
        Matrix scaleMatrix=new Matrix();
        float scale=1.0f/inSampleSize;
        scaleMatrix.postScale(scale,scale);

        Bitmap resizeBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),scaleMatrix,true);
        return resizeBitmap;
    }

    /**
     * ref https://www.itread01.com/p/39352.html
     * @param bitmap
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        //圓形圖片寬高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //正方形的邊長
        int r = 0;
        //取最短邊做邊長
        if (width > height) {
            r = height;
        } else {
            r = width;
        }
        //構建一個bitmap
        Bitmap backgroundBmp = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        //new一個Canvas,在backgroundBmp上畫圖
        Canvas canvas = new Canvas(backgroundBmp);
        Paint paint = new Paint();
        //設定邊緣光滑,去掉鋸齒
        paint.setAntiAlias(true);
        //寬高相等,即正方形
        RectF rect = new RectF(0, 0, r, r);
        //通過制定的rect畫一個圓角矩形,當圓角X軸方向的半徑等於Y軸方向的半徑時,
        //且都等於r/2時,畫出來的圓角矩形就是圓形
        canvas.drawRoundRect(rect, r / 2, r / 2, paint);
        //設定當兩個圖形相交時的模式,SRC_IN為取SRC圖形相交的部分,多餘的將被去掉
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //canvas將bitmap畫在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, paint);
        //返回已經繪畫好的backgroundBmp
        return backgroundBmp;
    }
}
