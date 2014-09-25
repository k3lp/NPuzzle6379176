package nl.mprog.projects.nPuzzle6379176;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.view.MenuItem;

@SuppressLint("NewApi") public class GameActivity extends Activity {
	
	private int reqHeight = 0, reqWidth = 0;
	public Integer[] imageIds =
	{
		R.drawable.puzzle_0,
		R.drawable.puzzle_1,
		R.drawable.puzzle_2,
		R.drawable.puzzle_3,
		R.drawable.puzzle_4,
		R.drawable.puzzle_5,
		R.drawable.puzzle_6,
		R.drawable.puzzle_7,
		R.drawable.puzzle_8
		//R.drawable.puzzle_9
	};
	public int moeilijkheid = 0, imgnr = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_game);
    	
    	//krijg info van intents
    	moeilijkheid = getIntent().getExtras().getInt("moeilijkheid");
    	imgnr = getIntent().getExtras().getInt("imagebm");
    	
        reqWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        reqHeight= (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        
    	//vraag img op met imgnr
    	LinearLayout gallery1 = (LinearLayout)findViewById(R.id.gallery1);
    	//maak bitmap van img     
        Bitmap volimgbm = BitmapFactory.decodeResource(getResources(), imageIds[imgnr]);
        
        //scale bitmap met goede aspect
        Bitmap scaledimgbm = scaleBm(volimgbm);
        //klaar met volimgbm, recycle
        //volimgbm.recycle();
        
    	//aantal tiles
    	int dimensieTiles = moeilijkheid + 3;
    	int aantalTiles = dimensieTiles * dimensieTiles;
        
    	
        int pt1 = (int)(scaledimgbm.getWidth()/dimensieTiles);
        int pt2 = (int)(scaledimgbm.getHeight()/dimensieTiles);
    	//maak tiles

    	//vul tiles met stukken bitmap
    	
    	
    	//maak layout
        LinearLayout layout = new LinearLayout(getApplicationContext());
        //layout.setLayoutParams(new LayoutParams(reqHeight, reqWidth));
        layout.setGravity(Gravity.CENTER);
        //vul layout met tiles
        ImageView imageView = new ImageView(getApplicationContext());
        //imageView.setLayoutParams(new LayoutParams(reqHeight, reqWidth));
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(scaledimgbm);
        layout.addView(imageView);
        //add layout
    	gallery1.addView(layout);
    	
    	if(moeilijkheid == 0)
    	{
    		maakStuks3(scaledimgbm);
    	}

    	
    	//menubutton
    	maakMenu();
    	
    	//debug moeilijkheid
    	TextView tv3 = (TextView) findViewById(R.id.testMoeilijkheid);
		tv3.setText("moeilijkheid: " + pt1);
		//debug imgnr
    	TextView tv4 = (TextView) findViewById(R.id.testImgnr);
		tv4.setText("imgnr: " + (imgnr+1));
    }
	

	Bitmap scaleBm(Bitmap volimgbm)
	{
	    int volimgbmWidth = volimgbm.getWidth(), volimgbmHeight = volimgbm.getHeight();
	    float scale;
	    //scale x en y !met aspect behouden!
	    float scaledimgbmx = (float) reqWidth / volimgbmWidth;
	    float scaledimgbmy = (float) reqHeight / volimgbmHeight;
	    
	    //kijk welke dimensie het grootst is
	    if(scaledimgbmx > scaledimgbmy)
	    {
	    	scale = scaledimgbmx;
	    } else
	    {
	    	scale = scaledimgbmy;
	    }

	    //scale
	    float scaledWidth = scale * volimgbmWidth;
	    float scaledHeight = scale * volimgbmHeight;

	    //pak linksboven punt in het midden  
	    float puntx = (reqWidth - scaledWidth) / 2;
	    float punty = (reqHeight - scaledHeight) / 2;

	    //snijd vierkant uit te grote gescalede bm
	    RectF vierkant = new RectF(puntx, punty, puntx + scaledWidth, punty + scaledHeight);

	    //maak bm van vierkant
	    Bitmap scaledbm = Bitmap.createBitmap(reqWidth, reqHeight, volimgbm.getConfig());
	    Canvas canvas = new Canvas(scaledbm);
	    canvas.drawBitmap(volimgbm, null, vierkant, null);

	    return scaledbm;
		
	}
	void maakStuks3(Bitmap scaledimgbm)
	{
		//aantal tiles
    	int dimensieTiles = moeilijkheid + 3;
    	int aantalTiles = dimensieTiles * dimensieTiles;
        
    	
        int pt1 = (int)(scaledimgbm.getWidth()/dimensieTiles);
        int pt2 = (int)(scaledimgbm.getHeight()/dimensieTiles);
        //bitmaps stukken boven
    	Bitmap stuk1 = Bitmap.createBitmap(scaledimgbm, 1,1,pt1,pt2);
    	Bitmap stuk2 = Bitmap.createBitmap(scaledimgbm, pt1,1,pt1,pt2);
    	Bitmap stuk3 = Bitmap.createBitmap(scaledimgbm, pt1*2,1,pt1,pt2);
    	
    	//bitmaps stukken midden
    	Bitmap stuk122 = Bitmap.createBitmap(scaledimgbm, 1,pt2,pt1,pt2);
    	Bitmap stuk222 = Bitmap.createBitmap(scaledimgbm, pt1,pt2,pt1,pt2);
    	Bitmap stuk322 = Bitmap.createBitmap(scaledimgbm, pt1*2,pt2,pt1,pt2);

    	
    	//bitmaps stukken onder
    	Bitmap stuk133 = Bitmap.createBitmap(scaledimgbm, 1,pt2+pt2,pt1,pt2);
    	Bitmap stuk233 = Bitmap.createBitmap(scaledimgbm, pt1,pt2+pt2,pt1,pt2);
    	Bitmap stuk333 = Bitmap.createBitmap(scaledimgbm, pt1*2,pt2+pt2,pt1,pt2);
    	
    	//klaar met volledige bm, recycle
    	//scaledimgbm.recycle();
	}
	
	
	
	//maak een menu bij de menubutton
	void maakMenu()
	{
    	final Button buttonMenu = (Button) findViewById(R.id.buttonMenu);  
        buttonMenu.setOnClickListener(new OnClickListener()
        {  
        	@Override  
        	public void onClick(View v)
        	{  
        		PopupMenu popup = new PopupMenu(GameActivity.this, buttonMenu);
        		popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu()); 
        		popup.show();  
        	}  
        });
	}
	

	
	public void toStart(MenuItem item)
	{
		//stuur id van img mee en moeilijkheid
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	
	public void toMakkelijker(MenuItem item)
	{
		if(moeilijkheid != 0)
		{
			moeilijkheid -= 1;
		}
		//stuur id van img mee en moeilijkheid
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("imagebm", imgnr);
		intent.putExtra("moeilijkheid", moeilijkheid);
		startActivity(intent);
		finish();
	}

	
	public void toMoeilijker(MenuItem item)
	{
		if(moeilijkheid != 2)
		{
			moeilijkheid += 1;
		}
		//stuur id van img mee en moeilijkheid
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("imagebm", imgnr);
		intent.putExtra("moeilijkheid", moeilijkheid);
		startActivity(intent);
		finish();
	}
	
	//restart deze activity met moeilijkheid etc
	public void toRestart(MenuItem item)
	{
		//stuur id van img mee en moeilijkheid
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("imagebm", imgnr);
		intent.putExtra("moeilijkheid", moeilijkheid);
		startActivity(intent);
		finish();
	}
}
