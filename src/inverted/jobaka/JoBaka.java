package inverted.jobaka;

import inverted.jobaka.supportClass.ImageRenderClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class JoBaka extends Activity implements OnSeekBarChangeListener {

	// final VunglePub vunglePub = VunglePub.getInstance();
	final String vungle_app_id = "554238b3956353531800002e";

	final String inMobi_app_id = "a6366895d005429d91e98743a027b747";

	private static final int REQUEST_SHARE_ACTION = 45;
	ImageView imgView;
	ImageView imgViewDialog;
	ImageRenderClass imgObject;
	String localAbsoluteFilePath;
	final int seekBarIds[] = { R.id.seekBarRed, R.id.seekBarGreen,
			R.id.seekBarBlue };
	SeekBar seekBar;

	private int redColor = 255;
	private int greenColor = 200;
	private int blueColor = 20;
	private String picturePath;
	private BitmapFactory.Options options;
	private int screenHeight;
	private int backBtnPressCount = 0;
	private long prevbackPressed;
	// private InterstitialAd mInterstitialAd;
	static int screenWidth;

	// private AdView adView;
	// IMInterstitial interstitial;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_jo_baka);

		Display display = getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();

		if ((screenHeight - screenWidth) > (int) (screenHeight * 0.15)) {
			LayoutParams params = new LayoutParams(screenWidth,
					(int) (screenHeight * 0.15));
			findViewById(R.id.controlPanel).setLayoutParams(params);
		}

		imgView = (ImageView) findViewById(R.id.mainImage);
		LayoutParams params = new LayoutParams((int) (screenWidth * 0.95),
				(int) (screenWidth * 0.95));
		imgView.setLayoutParams(params);
		imgView.setMaxWidth((int) (screenWidth * 0.97));
		imgView.setMaxHeight((int) (screenWidth * 0.97));

		imgObject = new ImageRenderClass(getApplicationContext(), screenWidth);

		findViewById(R.id.save).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imgObject.savePicture(imgView);
			}
		});

		findViewById(R.id.custom).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imgObject = new ImageRenderClass(getApplicationContext(),
						screenWidth);
				launchTypeSelector();
				// imgObject.createImage();
				// imgView.setImageBitmap(imgObject.foreground);
			}
		});

		findViewById(R.id.share).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				localAbsoluteFilePath = saveImageLocally(imgObject.foreground);
				if (localAbsoluteFilePath != null
						&& localAbsoluteFilePath != "") {

					Intent shareIntent = new Intent(Intent.ACTION_SEND);
					Uri phototUri = Uri.parse(localAbsoluteFilePath);
					shareIntent.setData(phototUri);
					shareIntent.setType("image/jpg");
					shareIntent.putExtra(Intent.EXTRA_STREAM, phototUri);
					startActivityForResult(
							Intent.createChooser(shareIntent, "Share Via"),
							REQUEST_SHARE_ACTION);
				}

			}
		});
		launchEditTextDialog();
		imgView.setImageResource(R.drawable.impress_kar);

		/*
		 * // initialize the Publisher SDK vunglePub.init(this, vungle_app_id);
		 * 
		 * vunglePub.setEventListeners(vungleListener);
		 * 
		 * // create a new AdConfig object final AdConfig overrideConfig = new
		 * AdConfig();
		 * 
		 * // set any configuration options you like.
		 * overrideConfig.setIncentivized(true);
		 * overrideConfig.setSoundEnabled(false);
		 * 
		 * mInterstitialAd = new InterstitialAd(this);
		 * mInterstitialAd.setAdUnitId
		 * ("ca-app-pub-1532662721500088/8939142259"); requestNewInterstitial();
		 * 
		 * findViewById(R.id.ad).setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { if (interstitial.getState()
		 * == IMInterstitial.State.READY) { interstitial.show(); } else {
		 * Toast.makeText(getApplicationContext(), "In Mobi not prepareed",
		 * Toast.LENGTH_LONG).show(); } if (mInterstitialAd.isLoaded()) {
		 * mInterstitialAd.show(); } else {
		 * Toast.makeText(getApplicationContext(), "Vungle not prepareed",
		 * Toast.LENGTH_LONG).show(); } } });
		 * 
		 * mInterstitialAd.setAdListener(new AdListener() {
		 * 
		 * @Override public void onAdClosed() { requestNewInterstitial(); } });
		 * InMobi.initialize(this, inMobi_app_id); IMBanner banner = (IMBanner)
		 * findViewById(R.id.banner);
		 * banner.setRefreshInterval(IMBanner.REFRESH_INTERVAL_OFF);
		 * banner.loadBanner();
		 * 
		 * interstitial = new IMInterstitial(this, inMobi_app_id);
		 * interstitial.loadInterstitial();
		 */
	}

	/*
	 * private void requestNewInterstitial() { AdRequest adRequest = new
	 * AdRequest.Builder().build();
	 * 
	 * mInterstitialAd.loadAd(adRequest);
	 * 
	 * }
	 */
	private void launchEditHandDialog() {
		LayoutInflater li = LayoutInflater.from(JoBaka.this);
		View promptsView = li.inflate(R.layout.hand_selector, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				JoBaka.this);
		final ImageView IV = (ImageView) promptsView
				.findViewById(R.id.mainHandImage);
		ImageView hand = (ImageView) findViewById(R.id.handImage1);
		imgObject.setBakaHandType(ImageRenderClass.HNAD_1);
		IV.setImageBitmap(imgObject.getBakaHandImage());
		LayoutParams params = new LayoutParams((int) (screenHeight * 0.15),
				(int) (screenHeight * 0.15));
		IV.setLayoutParams(params);

		// alertDialogBuilder.setTitle("");
		alertDialogBuilder.setView(promptsView);
		final AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.setTitle("Select hand gesture");
		OnClickListener handClickListner = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.handImage1:
					imgObject.setBakaHandType(ImageRenderClass.HNAD_1);
					break;
				case R.id.handImage2:
					imgObject.setBakaHandType(ImageRenderClass.HNAD_2);
					break;
				case R.id.handImage3:
					imgObject.setBakaHandType(ImageRenderClass.HNAD_3);
					break;
				case R.id.handImage4:
					imgObject.setBakaHandType(ImageRenderClass.HNAD_4);
					break;
				case R.id.handImage5:
					imgObject.setBakaHandType(ImageRenderClass.HNAD_5);
					break;
				case R.id.handImage6:
					imgObject.setBakaHandType(ImageRenderClass.HNAD_6);
					break;
				case R.id.handImage7:
					imgObject.setBakaHandType(ImageRenderClass.HNAD_7);
					break;
				case R.id.handImage8:
					imgObject.setBakaHandType(ImageRenderClass.HNAD_8);
					break;
				default:
					imgObject.setBakaHandType(ImageRenderClass.HNAD_1);
					break;
				}
				IV.setImageBitmap(imgObject.getBakaHandImage());
			}
		};

		params = new LayoutParams((int) (screenHeight * 0.10),
				(int) (screenHeight * 0.10));
		hand = (ImageView) promptsView.findViewById(R.id.handImage1);
		hand.setOnClickListener(handClickListner);
		hand.setLayoutParams(params);

		hand = (ImageView) promptsView.findViewById(R.id.handImage2);
		hand.setOnClickListener(handClickListner);
		hand.setLayoutParams(params);
		hand = (ImageView) promptsView.findViewById(R.id.handImage3);
		hand.setOnClickListener(handClickListner);
		hand.setLayoutParams(params);
		hand = (ImageView) promptsView.findViewById(R.id.handImage4);
		hand.setOnClickListener(handClickListner);
		hand.setLayoutParams(params);
		hand = (ImageView) promptsView.findViewById(R.id.handImage5);
		hand.setOnClickListener(handClickListner);
		hand.setLayoutParams(params);
		hand = (ImageView) promptsView.findViewById(R.id.handImage6);
		hand.setOnClickListener(handClickListner);
		hand.setLayoutParams(params);
		hand = (ImageView) promptsView.findViewById(R.id.handImage7);
		hand.setOnClickListener(handClickListner);
		hand.setLayoutParams(params);
		hand = (ImageView) promptsView.findViewById(R.id.handImage8);
		hand.setOnClickListener(handClickListner);
		hand.setLayoutParams(params);
		IV.setImageBitmap(imgObject.getBakaHandImage());

		Button next = (Button) promptsView.findViewById(R.id.select);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				launchLineTypeDialog();
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

	@SuppressWarnings("deprecation")
	private void launchEditTextDialog() {
		LayoutInflater li = LayoutInflater.from(JoBaka.this);
		View promptsView = li.inflate(R.layout.edit_text, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				JoBaka.this);
		// alertDialogBuilder.setTitle("");
		alertDialogBuilder.setView(promptsView);

		final EditText title = (EditText) promptsView.findViewById(R.id.title);
		final EditText line1 = (EditText) promptsView.findViewById(R.id.line1);
		final EditText line2 = (EditText) promptsView.findViewById(R.id.line2);
		final LinearLayout line2LL = (LinearLayout) promptsView
				.findViewById(R.id.line2LL);
		if (imgObject.getBakaImageLineType() != ImageRenderClass.DOUBLE_LINE_IMAGE)
			line2LL.setVisibility(View.GONE);
		if (imgObject.getBakaImageType() == ImageRenderClass.BAKUDI_TYPE_IMAGE) {
			title.setText("Jo Bakudi");
		} else {
			title.setText("Jo Baka");
		}
		final AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.setTitle("Your meme message");
		alertDialog.setButton("Create", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (title.getText().toString() == null
						|| title.getText().toString().equalsIgnoreCase("")
						|| line1.getText().toString() == null
						|| line1.getText().toString().equalsIgnoreCase("")) {
					return;
				}
				System.out.println(title.getText().toString() + "   "
						+ line1.getText().toString() + "   "
						+ line2.getText().toString());

				imgObject.setMessages(title.getText().toString(), line1
						.getText().toString(), line2.getText().toString());
				imgObject.createImage();
				imgView.setImageBitmap(imgObject.foreground);
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

	protected void launchTypeSelector() {
		LayoutInflater li = LayoutInflater.from(JoBaka.this);
		View promptsView = li.inflate(R.layout.type_selector, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				JoBaka.this);
		alertDialogBuilder.setTitle("Select Meme Type");
		alertDialogBuilder.setView(promptsView);

		final LinearLayout customType = (LinearLayout) promptsView
				.findViewById(R.id.customType);
		final LinearLayout bakaType = (LinearLayout) promptsView
				.findViewById(R.id.bakaType);
		final LinearLayout bakudiType = (LinearLayout) promptsView
				.findViewById(R.id.bakudiType);
		final AlertDialog alertDialog = alertDialogBuilder.create();
		customType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imgObject.setBakaImageType(ImageRenderClass.CUSTOM_TYPE_IMAGE);
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
				alertDialog.dismiss();
			}
		});

		bakaType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imgObject.setBakaImageType(ImageRenderClass.BAKA_TYPE_IMAGE);
				launchEditHandDialog();

				alertDialog.dismiss();
			}
		});

		bakudiType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imgObject.setBakaImageType(ImageRenderClass.BAKUDI_TYPE_IMAGE);
				launchEditHandDialog();
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

	protected void launchLineTypeDialog() {
		LayoutInflater li = LayoutInflater.from(JoBaka.this);
		View promptsView = li.inflate(R.layout.text_direction, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				JoBaka.this);
		alertDialogBuilder.setTitle("Select Text Organization");
		alertDialogBuilder.setView(promptsView);

		final LinearLayout singleType = (LinearLayout) promptsView
				.findViewById(R.id.single);
		final LinearLayout doubleType = (LinearLayout) promptsView
				.findViewById(R.id.doubleType);
		final LinearLayout curved = (LinearLayout) promptsView
				.findViewById(R.id.curved);
		final AlertDialog alertDialog = alertDialogBuilder.create();
		singleType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imgObject
						.setBakaImageLineType(ImageRenderClass.SINGLE_LINE_IMAGE);
				launchCustomBackgroundColorDialog();
				alertDialog.dismiss();
			}
		});

		doubleType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imgObject
						.setBakaImageLineType(ImageRenderClass.DOUBLE_LINE_IMAGE);
				launchCustomBackgroundColorDialog();
				alertDialog.dismiss();
			}
		});

		curved.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imgObject
						.setBakaImageLineType(ImageRenderClass.CURVED_LINE_IMAGE);
				launchCustomBackgroundColorDialog();
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

	protected static final int RESULT_LOAD_IMAGE = 100;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_IMAGE = 1;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			// extract selected Image path
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			System.out.println(picturePath);
			options = new BitmapFactory.Options();
			try {
				// First decode with inJustDecodeBounds=true to check
				// dimensions
				options.inJustDecodeBounds = true;
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				BitmapFactory.decodeFile(picturePath, options);

				// Calculate inSampleSize
				options.inSampleSize = calculateInSampleSize(options, 750, 750);

				// Decode bitmap with inSampleSize set
				options.inJustDecodeBounds = false;
				imgObject.setBakaImage(BitmapFactory.decodeFile(picturePath,
						options));
				imgObject.setBakaImageType(ImageRenderClass.CUSTOM_TYPE_IMAGE);
				System.out.println(""
						+ (BitmapFactory.decodeFile(picturePath, options))
								.getWidth());
				System.out.println(""
						+ (BitmapFactory.decodeFile(picturePath, options))
								.getHeight());
				launchLineTypeDialog();

			} catch (Exception e) {
				System.out.println(e.getMessage());
				generateToast("Sorry!!\nMemory odf your phone is not sufficent for processing");
			}
		}

		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// successfully captured the image
				// display it in image view
				imgObject.setBakaImageType(ImageRenderClass.CUSTOM_TYPE_IMAGE);
				imgObject.setBakaImage(BitmapFactory.decodeFile(picturePath,
						options));
				launchLineTypeDialog();
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	private void generateToast(String msg) {
		Toast.makeText(JoBaka.this, msg, Toast.LENGTH_LONG).show();
	}

	@SuppressLint("NewApi")
	private void launchCustomBackgroundColorDialog() {
		LayoutInflater li = LayoutInflater.from(JoBaka.this);
		View promptsView = li.inflate(R.layout.personalise_background, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				JoBaka.this);

		alertDialogBuilder.setTitle("Select background color");

		alertDialogBuilder.setView(promptsView);
		redColor = 255;
		greenColor = 200;
		blueColor = 20;
		for (int i = 0; i < seekBarIds.length; i++) {
			seekBar = (SeekBar) promptsView.findViewById(seekBarIds[i]);
			initaliseSeekBar(seekBar);
		}

		imgViewDialog = (ImageView) promptsView
				.findViewById(R.id.personalizePreview);
		imgViewDialog.setImageDrawable(new ColorDrawable(Color.rgb(redColor,
				greenColor, blueColor)));

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Set",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								imgObject.setBackGroundColor(Color.rgb(
										redColor, greenColor, blueColor));
								launchCustomFontColorDialog();
								dialog.dismiss();
							}
						})
				.setNegativeButton("Default Color",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								redColor = 255;
								greenColor = 200;
								blueColor = 20;
								imgObject.setBackGroundColor(Color.rgb(
										redColor, greenColor, blueColor));
								launchCustomFontColorDialog();
								dialog.dismiss();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	@SuppressLint("NewApi")
	private void launchCustomFontColorDialog() {
		LayoutInflater li = LayoutInflater.from(JoBaka.this);
		View promptsView = li.inflate(R.layout.personalise_background, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				JoBaka.this);
		redColor = 2;
		greenColor = 2;
		blueColor = 2;

		alertDialogBuilder.setTitle("Select font color");

		alertDialogBuilder.setView(promptsView);

		for (int i = 0; i < seekBarIds.length; i++) {
			seekBar = (SeekBar) promptsView.findViewById(seekBarIds[i]);
			initaliseSeekBar(seekBar);
		}

		imgViewDialog = (ImageView) promptsView
				.findViewById(R.id.personalizePreview);
		imgViewDialog.setImageDrawable(new ColorDrawable(Color.rgb(redColor,
				greenColor, blueColor)));

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Set",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								imgObject.setFontColor(Color.rgb(redColor,
										greenColor, blueColor));
								launchEditTextDialog();
								dialog.dismiss();
							}
						})
				.setNegativeButton("Default Color",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								redColor = 2;
								greenColor = 2;
								blueColor = 2;
								imgObject.setFontColor(Color.rgb(redColor,
										greenColor, blueColor));
								launchEditTextDialog();
								dialog.dismiss();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void initaliseSeekBar(SeekBar seekBar) {
		seekBar.setOnSeekBarChangeListener(JoBaka.this);
		switch (seekBar.getId()) {
		case R.id.seekBarRed:
			seekBar.setProgress(redColor);
			break;
		case R.id.seekBarGreen:
			seekBar.setProgress(greenColor);
			break;
		case R.id.seekBarBlue:
			seekBar.setProgress(blueColor);
			break;
		default:
			seekBar.setProgress(68);
			break;
		}
	};

	private String saveImageLocally(Bitmap _bitmap) {
		File outputFile = new File(Environment.getExternalStorageDirectory()
				+ "/Jo_Baka_Image.JPEG");
		if (outputFile.exists()) {
			outputFile.delete();
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), "Error eccountered",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
		try {
			FileOutputStream out = new FileOutputStream(outputFile);
			_bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
			out.close();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Error eccountered",
					Toast.LENGTH_LONG).show();
		}

		return outputFile.getAbsolutePath();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_jo_baka, menu);
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int val, boolean flag) {
		if (flag == true) {
			switch (seekBar.getId()) {

			case R.id.seekBarRed:
				redColor = val;
				break;
			case R.id.seekBarGreen:
				greenColor = val;
				break;
			case R.id.seekBarBlue:
				blueColor = val;
				break;
			}
			imgViewDialog.setImageDrawable(new ColorDrawable(Color.rgb(
					redColor, greenColor, blueColor)));
		}

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		showRateDialog();
		// super.onBackPressed();
		if (backBtnPressCount == 0) {
			backBtnPressCount++;
			prevbackPressed = System.currentTimeMillis();
			Toast.makeText(getApplicationContext(), "Press again to exit",
					Toast.LENGTH_LONG).show();
		} else if (System.currentTimeMillis() - prevbackPressed < 3000) {
			// app_launched();
			// super.onBackPressed();
		} else
			backBtnPressCount = 0;
	}

	private final static String APP_TITLE = "Jo Baka";
	private final static int DAYS_UNTIL_PROMPT = 3;
	private final static int LAUNCHES_UNTIL_PROMPT = 1;

	public void app_launched() {
		SharedPreferences prefs = JoBaka.this.getSharedPreferences("apprater",
				0);
		if (prefs.getBoolean("dontshowagain", false)) {
			return;
		}

		SharedPreferences.Editor editor = prefs.edit();

		// Increment launch counter
		long launch_count = prefs.getLong("launch_count", 0) + 1;
		editor.putLong("launch_count", launch_count);

		// Get date of first launch
		Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
		if (date_firstLaunch == 0) {
			date_firstLaunch = System.currentTimeMillis();
			editor.putLong("date_firstlaunch", date_firstLaunch);
		}

		// Wait at least n days before opening
		if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
			if (System.currentTimeMillis() >= date_firstLaunch
					+ (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
				showRateDialog();
			}
		}
		// showRateDialog();
		editor.commit();
	}

	public void showRateDialog() {
		LinearLayout ll = new LinearLayout(JoBaka.this);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				JoBaka.this);
		alertDialogBuilder.setView(ll);
		final AlertDialog dialog = alertDialogBuilder.create();
		System.out.println("hello" + dialog.toString());

		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setBackgroundColor(Color.rgb(35, 85, 120));

		TextView tv = new TextView(JoBaka.this);
		tv.setText("Jo Baka, tane app vaprva ni maja aavi hoy to ekdam zakkas riveiw aapi de.");
		tv.setWidth(screenWidth);
		tv.setTextSize(20.0f);
		tv.setTextColor(Color.WHITE);
		tv.setPadding(4, 10, 4, 10);
		ll.addView(tv);

		Button b1 = new Button(JoBaka.this);
		b1.setText("Rate " + APP_TITLE);
		b1.setBackgroundColor(Color.rgb(8, 183, 202));
		b1.setPadding(4, 10, 4, 20);

		b1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setData(Uri.parse("market://details?id=inverted.jobaka"));
				startActivity(intent);
				finish();
			}
		});
		ll.addView(b1);

		Button b2 = new Button(JoBaka.this);
		b2.setText("Not Now");
		b2.setBackgroundColor(Color.rgb(45, 102, 202));
		b2.setPadding(4, 10, 4, 20);

		b2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// vunglePub.playAd();
				dialog.dismiss();
			}
		});
		ll.addView(b2);

		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
//		vunglePub.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		vunglePub.onResume();
	}

	/*
	 * private final EventListener vungleListener = new EventListener() {
	 * 
	 * @Override public void onVideoView(boolean isCompletedView, int
	 * watchedMillis, int videoDurationMillis) { // Called each time a video
	 * completes. isCompletedView is true if >= // 80% of the video was watched.
	 * }
	 * 
	 * @Override public void onAdStart() { // Called before playing an ad. }
	 * 
	 * @Override public void onAdUnavailable(String reason) { // Called when
	 * VunglePub.playAd() was called but no ad is available // to show to the
	 * user. }
	 * 
	 * @Override public void onAdEnd(boolean wasCallToActionClicked) { // Called
	 * when the user leaves the ad and control is returned to // your
	 * application. }
	 * 
	 * @Override public void onAdPlayableChanged(boolean isAdPlayable) { //
	 * Called when ad playability changes. } };
	 */
}
