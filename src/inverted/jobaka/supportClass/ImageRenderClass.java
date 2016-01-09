package inverted.jobaka.supportClass;

import inverted.jobaka.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageRenderClass {
	public static int BAKA_TYPE_IMAGE = 100;
	public static int BAKUDI_TYPE_IMAGE = 101;
	public static int CUSTOM_TYPE_IMAGE = 102;
	private int image_type = BAKA_TYPE_IMAGE;

	public static int SINGLE_LINE_IMAGE = 100;
	public static int DOUBLE_LINE_IMAGE = 101;
	public static int CURVED_LINE_IMAGE = 102;
	private int line_type;

	public static int HNAD_1 = R.drawable.hand_1;
	public static int HNAD_2 = R.drawable.hand_2;
	public static int HNAD_3 = R.drawable.hand_3;
	public static int HNAD_4 = R.drawable.hand_4;
	public static int HNAD_5 = R.drawable.hand_5;
	public static int HNAD_6 = R.drawable.hand_6;
	public static int HNAD_7 = R.drawable.hand_7;
	public static int HNAD_8 = R.drawable.hand_8;
	private int hand = HNAD_1;

	private static final int TOP = 1;
	private static final int BOTTOM = 2;
	int backgroundColor;
	Bitmap bakaImage;
	public Bitmap foreground;
	String title = "Jo Baka,";
	String messageLineOne = "Aane kehvay meme";
	String messageLineTwo = "Dhayn Rakh.";

	private String bakaMessage = "Bahu lakhyu.Thodu o6u kar.";

	String mCurrentPhotoPath;
	private Context context;

	private Path mArcTop = new Path();
	RectF ovalTop;
	int startAngleTop;
	int endAngleTop;

	private Path mArcBottom = new Path();
	RectF ovalBottom;
	int startAngleBottom;
	int endAngleBottom;

	private Paint mPaintText;
	private int FontColor;
	private Bitmap bakaHandImage;

	public ImageRenderClass(Context context, int width) {
		backgroundColor = Color.rgb(255, 200, 20);
		FontColor = Color.rgb(25, 2, 25);

		this.context = context;
		this.line_type = SINGLE_LINE_IMAGE;

		bakaImage = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.hello_baka_1);
		bakaImage = Bitmap.createScaledBitmap(bakaImage, 450, 450, true);
	}

	public void createImage() {
		if (image_type == BAKA_TYPE_IMAGE) {
			bakaImage = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.hello_baka_1);
			bakaImage = Bitmap.createScaledBitmap(bakaImage, 450, 450, true);
		} else if (image_type == BAKUDI_TYPE_IMAGE) {
			bakaImage = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.bakudi);
			bakaImage = Bitmap.createScaledBitmap(bakaImage, 450, 450, true);
		} else {
			int OriginalBitmapHeight = bakaImage.getHeight();
			int OriginalBitmapWidth = bakaImage.getWidth();

			if (OriginalBitmapHeight >= OriginalBitmapWidth) {
				bakaImage = Bitmap
						.createScaledBitmap(
								bakaImage,
								(int) (((float) OriginalBitmapWidth / OriginalBitmapHeight) * 450),
								450, true);
			} else {
				bakaImage = Bitmap
						.createScaledBitmap(
								bakaImage,
								450,
								(int) (((float) OriginalBitmapHeight / OriginalBitmapWidth) * 450),
								true);

			}

		}

		if (line_type == SINGLE_LINE_IMAGE) {
			createImageOneLiner();
		} else if (line_type == DOUBLE_LINE_IMAGE) {
			createImageDoubleType();
		} else if (line_type == CURVED_LINE_IMAGE) {
			createImageCurved();
		}
	}

	public void createImageOneLiner() {
		if (image_type == BAKA_TYPE_IMAGE) {
			bakaImage = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.hello_baka_1);
			bakaImage = Bitmap.createScaledBitmap(bakaImage, 450, 450, true);
		} else if (image_type == BAKUDI_TYPE_IMAGE) {
			bakaImage = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.bakudi);
			bakaImage = Bitmap.createScaledBitmap(bakaImage, 450, 450, true);
		} else {
			int OriginalBitmapHeight = bakaImage.getHeight();
			int OriginalBitmapWidth = bakaImage.getWidth();

			if (OriginalBitmapHeight >= OriginalBitmapWidth) {
				bakaImage = Bitmap
						.createScaledBitmap(
								bakaImage,
								(int) (((float) OriginalBitmapWidth / OriginalBitmapHeight) * 450),
								450, true);
			} else {
				bakaImage = Bitmap
						.createScaledBitmap(
								bakaImage,
								450,
								(int) (((float) OriginalBitmapHeight / OriginalBitmapWidth) * 450),
								true);

			}
		}
		bakaHandImage = getBakaHandImage();
		foreground = Bitmap.createBitmap(840, 840, Bitmap.Config.ARGB_8888);
		Bitmap bitmap = foreground;

		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(backgroundColor);
		canvas.drawBitmap(bakaHandImage, 420 - (bakaHandImage.getWidth() / 2),
				125, null);

		mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaintText.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaintText.setColor(FontColor);
		mPaintText.setTextSize(80);

		Point pntTitle = getTextBound(title, new Point(420, 60), mPaintText);
		while (pntTitle.x < 20) {
			mPaintText.setTextSize(mPaintText.getTextSize() - 2);
			pntTitle = getTextBound(title, new Point(420, 60), mPaintText);
		}
		canvas.drawText(title, pntTitle.x, pntTitle.y, mPaintText);
		mPaintText.setTextSize(80);

		Point pntOne = getTextBound(messageLineOne, new Point(420, 640),
				mPaintText);
		while (pntOne.x < 20) {
			mPaintText.setTextSize(mPaintText.getTextSize() - 2);
			pntOne = getTextBound(messageLineOne, new Point(420, 640),
					mPaintText);
		}
		canvas.drawText(messageLineOne, pntOne.x, pntOne.y, mPaintText);
		mPaintText.setTextSize(100);
	}

	public void createImageDoubleType() {
		if (image_type == BAKA_TYPE_IMAGE) {
			bakaImage = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.hello_baka_1);
			bakaImage = Bitmap.createScaledBitmap(bakaImage, 450, 450, true);
		} else if (image_type == BAKUDI_TYPE_IMAGE) {
			bakaImage = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.bakudi);
			bakaImage = Bitmap.createScaledBitmap(bakaImage, 450, 450, true);
		} else {
			int OriginalBitmapHeight = bakaImage.getHeight();
			int OriginalBitmapWidth = bakaImage.getWidth();

			if (OriginalBitmapHeight >= OriginalBitmapWidth) {
				bakaImage = Bitmap
						.createScaledBitmap(
								bakaImage,
								(int) (((float) OriginalBitmapWidth / OriginalBitmapHeight) * 450),
								450, true);
			} else {
				bakaImage = Bitmap
						.createScaledBitmap(
								bakaImage,
								450,
								(int) (((float) OriginalBitmapHeight / OriginalBitmapWidth) * 450),
								true);

			}
		}
		bakaHandImage = getBakaHandImage();
		foreground = Bitmap.createBitmap(840, 840, Bitmap.Config.ARGB_8888);
		Bitmap bitmap = foreground;

		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(backgroundColor);
		canvas.drawBitmap(bakaHandImage, 420 - (bakaHandImage.getWidth() / 2),
				125, null);

		mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaintText.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaintText.setColor(FontColor);
		mPaintText.setTextSize(80);

		Point pntTitle = getTextBound(title, new Point(420, 60), mPaintText);
		while (pntTitle.x < 20) {
			mPaintText.setTextSize(mPaintText.getTextSize() - 2);
			pntTitle = getTextBound(title, new Point(420, 60), mPaintText);
		}
		canvas.drawText(title, pntTitle.x, pntTitle.y, mPaintText);
		mPaintText.setTextSize(80);

		Point pntOne = getTextBound(messageLineOne, new Point(420, 640),
				mPaintText);
		while (pntOne.x < 20) {
			mPaintText.setTextSize(mPaintText.getTextSize() - 2);
			pntOne = getTextBound(messageLineOne, new Point(420, 640),
					mPaintText);
		}
		canvas.drawText(messageLineOne, pntOne.x, pntOne.y, mPaintText);
		mPaintText.setTextSize(80);

		Point pntTwo = getTextBound(messageLineTwo, new Point(420, 750),
				mPaintText);
		while (pntTwo.x < 20) {
			mPaintText.setTextSize(mPaintText.getTextSize() - 2);
			pntTwo = getTextBound(messageLineTwo, new Point(420, 750),
					mPaintText);
		}
		canvas.drawText(messageLineTwo, pntTwo.x, pntTwo.y, mPaintText);

		mPaintText.setTextSize(100);
	}

	private Point getTextBound(String string, Point point, Paint paint) {
		Rect bounds = new Rect();
		paint.getTextBounds(string, 0, string.length(), bounds);
		// while (bounds.width() > 820) {
		// }
		return new Point(point.x - (bounds.width() / 2), point.y
				+ (bounds.height() / 2));
	}

	public void createImageCurved() {
		if (image_type == BAKA_TYPE_IMAGE) {
			bakaImage = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.hello_baka_1);
			bakaImage = Bitmap.createScaledBitmap(bakaImage, 450, 450, true);
		} else if (image_type == BAKUDI_TYPE_IMAGE) {
			bakaImage = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.bakudi);
			bakaImage = Bitmap.createScaledBitmap(bakaImage, 450, 450, true);
		} else {
			int OriginalBitmapHeight = bakaImage.getHeight();
			int OriginalBitmapWidth = bakaImage.getWidth();

			if (OriginalBitmapHeight >= OriginalBitmapWidth) {
				bakaImage = Bitmap
						.createScaledBitmap(
								bakaImage,
								(int) (((float) OriginalBitmapWidth / OriginalBitmapHeight) * 450),
								450, true);
			} else {
				bakaImage = Bitmap
						.createScaledBitmap(
								bakaImage,
								450,
								(int) (((float) OriginalBitmapHeight / OriginalBitmapWidth) * 450),
								true);

			}
		}

		bakaHandImage = getBakaHandImage();

		foreground = Bitmap.createBitmap(840, 840, Bitmap.Config.ARGB_8888);
		Bitmap bitmap = foreground;

		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(backgroundColor);
		canvas.drawBitmap(bakaHandImage, 215, 175, null);

		mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaintText.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaintText.setColor(FontColor);
		mPaintText.setTextSize(80);

		if (measureAngle(title, TOP))
			canvas.drawTextOnPath(title, mArcTop, 0, 0, mPaintText);
		else {
			mPaintText.setTextSize(80);
			measureAngle(bakaMessage, TOP);
			canvas.drawTextOnPath(bakaMessage, mArcTop, 0, 0, mPaintText);
		}
		mPaintText.setTextSize(80);
		if (measureAngle(messageLineOne, BOTTOM))
			canvas.drawTextOnPath(messageLineOne, mArcBottom, 0, 0, mPaintText);
		else {
			mPaintText.setTextSize(80);
			measureAngle(bakaMessage, BOTTOM);
			canvas.drawTextOnPath(bakaMessage, mArcBottom, 0, 0, mPaintText);
		}
	}

	private boolean measureAngle(String str, int dir) {
		if (dir == BOTTOM) {
			ovalBottom = new RectF(15, 0, 825, 740);
		} else {
			ovalTop = new RectF(15, 100, 825, 840);
		}

		float textSize = mPaintText.getTextSize();
		float width = mPaintText.measureText(str);
		int radius = 310;
		double angle = width * 28.65 * 0.83 / radius;

		System.out.println(angle + "   " + width);
		// Toast.makeText(context, "" + angle + "   " + textSize,
		// Toast.LENGTH_LONG).show();
		if (angle >= 720) {
			return false;
		}
		while (angle > 80 && textSize > 20) {
			textSize -= 2;
			mPaintText.setTextSize(textSize);
			width = mPaintText.measureText(str);
			radius = 300;
			angle = width * 23.4912699595 / radius;
		}
		if (textSize < 10 || angle > 80) {
			return false;
		}
		System.out.println(angle + "   " + textSize);

		if (dir == TOP) {
			startAngleTop = (int) (270 - angle);
			endAngleTop = (int) angle * 2;
			System.out.println(startAngleTop + "   " + endAngleTop);
			mArcTop.addArc(ovalTop, startAngleTop, endAngleTop);
		} else {
			startAngleBottom = (int) (90 + angle);
			endAngleBottom = (int) angle * -2;
			System.out.println(startAngleBottom + "   " + endAngleBottom);
			mArcBottom.addArc(ovalBottom, startAngleBottom, endAngleBottom);
		}

		return true;
	}

	@SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "Jo Baka-" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir/* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	public void savePicture(ImageView imView) {
		try {
			BitmapDrawable btmpDr = (BitmapDrawable) imView.getDrawable();
			foreground = btmpDr.getBitmap();

			File image = createImageFile();
			FileOutputStream outStream;

			outStream = new FileOutputStream(image);
			foreground.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			/* 100 to keep full quality of the image */
			outStream.flush();
			outStream.close();

			// Refreshing SD card
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
					.parse("file://"
							+ Environment.getExternalStorageDirectory())));
			Toast.makeText(context, "successful" + "    " + mCurrentPhotoPath,
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(
					context,
					"Image could not be saved : Please ensure you have SD card installed "
							+ "properly\n" + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

	public void setBackGroundColor(int color) {
		this.backgroundColor = color;
	}

	public void setFontColor(int color) {
		this.FontColor = color;
		//
	}

	public void setBakaImage(Bitmap baka) {
		this.bakaImage = baka;
	}

	public void setBakaImageType(int type) {
		this.image_type = type;
	}

	public int getBakaImageType() {
		return this.image_type;
	}

	public void setBakaHandType(int type) {
		this.hand = type;
	}

	public int getBakaHandeType() {
		return this.hand;
	}

	public void setBakaImageLineType(int type) {
		this.line_type = type;
	}

	public int getBakaImageLineType() {
		return this.line_type;
	}

	public void setMessages(String title, String line1, String line2) {
		this.title = title;
		this.messageLineOne = line1;
		this.messageLineTwo = line2;
	}

	public Bitmap getBakaHandImage() {
		bakaHandImage = Bitmap.createBitmap(450, 450, Bitmap.Config.ARGB_8888);
		Bitmap bitmap = bakaHandImage;
		Bitmap bitmapImage = BitmapFactory.decodeResource(
				context.getResources(), hand);
		bitmapImage = Bitmap.createScaledBitmap(bitmapImage, 250, 250, true);

		if (image_type == BAKA_TYPE_IMAGE)
			bakaImage = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.plain_baka);
		else if (image_type == BAKUDI_TYPE_IMAGE)
			bakaImage = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.plain_bakudi);

		bakaImage = Bitmap.createScaledBitmap(bakaImage, 450, 450, true);

		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(backgroundColor);
		canvas.drawBitmap(bakaImage, 0, 0, null);
		if (image_type != CUSTOM_TYPE_IMAGE)
			canvas.drawBitmap(bitmapImage, -20, 180, null);

		return bakaHandImage;
	}
}
