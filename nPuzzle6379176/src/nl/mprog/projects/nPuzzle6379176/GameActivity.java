package nl.mprog.projects.nPuzzle6379176;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.view.MenuItem;

@SuppressLint("NewApi") public class GameActivity extends Activity
{

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
    public ImageView emptyTile;
    public int emptyTileRow;
    public int emptyTileColumn;
    public int afterTimer;
    public Integer[][] tileIds =
    {
        {R.id.h11, R.id.h12, R.id.h13, R.id.h14, R.id.h15},
        {R.id.h21, R.id.h22, R.id.h23, R.id.h24, R.id.h25},
        {R.id.h31, R.id.h32, R.id.h33, R.id.h34, R.id.h35},
        {R.id.h41, R.id.h42, R.id.h43, R.id.h44, R.id.h45},
        {R.id.h51, R.id.h52, R.id.h53, R.id.h54, R.id.h55}
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

        reqWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        reqHeight= (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        //maak bitmap van img     
        Bitmap volimgbm = BitmapFactory.decodeResource(getResources(), imageIds[imgnr]);
        //scale bitmap met goede aspect
        Bitmap scaledimgbm = scaleBm(volimgbm);
        
        //menubutton
        maakMenu();
        
        //maak tiles
        createTiles(scaledimgbm, moeilijkheid);
        
        //laat tiles even zien
        /*try
        {
            synchronized(this)
            {
                wait(3000);
            }
        }
        catch(InterruptedException ex)
        {              
        }*/
        //maak "lege" tile
        emptyTile = (ImageView) findViewById(tileIds[0][0]);
        emptyTile.setVisibility(View.INVISIBLE);
        emptyTile.setTag(2);
        emptyTileRow = 0;
        emptyTileColumn = 0;
        afterTimer = 0;
        Runnable r = new Runnable()
        {
            @Override
            public void run()
            {
                hussleTiles();
                afterTimer = 1;
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 3000);

        //debug moeilijkheid
        TextView tv3 = (TextView) findViewById(R.id.testMoeilijkheid);
        tv3.setText("moeilijkheid: " + moeilijkheid);
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


    void hussleTiles()
    {

        //doe steeds random stap vanaf lege tile 
        //vind positie lege tile
        //probeer random of naar links
        //of naar rechts of naar top of naar bot
        //te gaan
        Random r = new Random();
        int count = 0, randj = 0, randi = 0;
        while(count < 1000)
        {
            randj = r.nextInt(moeilijkheid+3);
            randi = r.nextInt(moeilijkheid+3);
            ImageView v;
            v = (ImageView) findViewById(tileIds[randi][randj]);
            onClickTileMove(v);   
                count++;
        }
        //Bitmap bm=((BitmapDrawable)imageView.getDrawable()).getBitmap();
    }

    void createTiles(Bitmap scaledimgbm, final Integer moeilijkheid)
    {
        //aantal tiles
        int dimensieTiles = moeilijkheid + 3;
        //int aantalTiles = dimensieTiles * dimensieTiles;


        int pt1 = (int)(scaledimgbm.getWidth()/dimensieTiles);
        int pt2 = (int)(scaledimgbm.getHeight()/dimensieTiles);
        
        if(moeilijkheid == 0)
        {
            
            Bitmap [][] bitmapTiles =
            {
                //bitmap stukken boven
                {Bitmap.createBitmap(scaledimgbm, 1,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,1,pt1,pt2)},
                //bitmaps stukken midden
                {Bitmap.createBitmap(scaledimgbm, 1,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2,pt1,pt2)},
                //bitmaps stukken onder
                {Bitmap.createBitmap(scaledimgbm, 1,pt2+pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2+pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2+pt2,pt1,pt2)}
            };
            
            for(int i = 0; i < dimensieTiles; i++)
            {
                for(int j = 0; j < dimensieTiles; j++)
                {
                    ImageView imageTile;
                    imageTile = (ImageView) findViewById(tileIds[i][j]);
                    imageTile.setImageBitmap(bitmapTiles[i][j]);
                    imageTile.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if(afterTimer == 1)
                            {
                                onClickTileMove(v);
                            }
                        }
                    });
                }
            }
        }
        else if(moeilijkheid == 1)
        {
            
            Bitmap [][] bitmapTiles =
            {
                //bitmap stukken boven
                {Bitmap.createBitmap(scaledimgbm, 1,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,1,pt1,pt2)},
                //bitmaps stukken midden
                {Bitmap.createBitmap(scaledimgbm, 1,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,pt2,pt1,pt2)},
                //bitmaps stukken onder
                {Bitmap.createBitmap(scaledimgbm, 1,pt2+pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2+pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2+pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,pt2*2,pt1,pt2)},
                
                {Bitmap.createBitmap(scaledimgbm, 1,pt2*3,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2*3,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2*3,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,pt2*3,pt1,pt2)}  
            };
            
            for(int i = 0; i < dimensieTiles; i++)
            {
                for(int j = 0; j < dimensieTiles; j++)
                {
                    ImageView imageTile;
                    imageTile = (ImageView) findViewById(tileIds[i][j]);
                    imageTile.setImageBitmap(bitmapTiles[i][j]);
                    imageTile.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if(afterTimer == 1)
                            {
                                onClickTileMove(v);
                            }  
                        }
                    });
                }
            }
        }
        else if(moeilijkheid == 2)
        {
            
            Bitmap [][] bitmapTiles =
            {
                {Bitmap.createBitmap(scaledimgbm, 1,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*4,1,pt1,pt2)},

                {Bitmap.createBitmap(scaledimgbm, 1,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*4,pt2,pt1,pt2)},

                {Bitmap.createBitmap(scaledimgbm, 1,pt2*2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2*2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2*2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,pt2*2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*4,pt2*2,pt1,pt2)},

                {Bitmap.createBitmap(scaledimgbm, 1,pt2*3,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2*3,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2*3,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,pt2*3,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*4,pt2*3,pt1,pt2)},

                {Bitmap.createBitmap(scaledimgbm, 1,pt2*4,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2*4,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2*4,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,pt2*4,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*4,pt2*4,pt1,pt2)}
            };
            
            for(int i = 0; i < dimensieTiles; i++)
            {
                for(int j = 0; j < dimensieTiles; j++)
                {
                    ImageView imageTile;
                    imageTile = (ImageView) findViewById(tileIds[i][j]);
                    imageTile.setImageBitmap(bitmapTiles[i][j]);
                    imageTile.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if(afterTimer == 1)
                            {
                                onClickTileMove(v);
                            }   
                        }
                    });
                }
            }
        }
        
    }

    void onClickTileMove(View v)
    {
        int x = -1, y= -1;
        //als het niet de "lege" tile is
        if(v.getVisibility() == View.VISIBLE)
        {
            //loop over tile ids voor coordinaten geklikte view
            for(int i = 0; i < moeilijkheid+3; i++)
            {
                for(int j = 0; j < moeilijkheid+3; j++)
                {
                    if(v.getId()==tileIds[i][j])
                    {
                        x = i;
                        y = j;
                    }
                }
            }
            //kijk of lege tile eromheen is
            if(x-1 == emptyTileRow && y == emptyTileColumn ||
               x+1 == emptyTileRow && y == emptyTileColumn ||
               x == emptyTileRow && y-1 == emptyTileColumn ||
               x == emptyTileRow && y+1 == emptyTileColumn)
            {
                //wissel views om
                
                ImageView imageClicked = (ImageView) findViewById(tileIds[x][y]);
                Bitmap bitmap = ((BitmapDrawable)imageClicked.getDrawable()).getBitmap();
                emptyTile.setImageBitmap(bitmap);
                emptyTile.setVisibility(View.VISIBLE);
                imageClicked.setVisibility(View.INVISIBLE);
                emptyTile = imageClicked;
                emptyTileRow = x;
                emptyTileColumn = y;
            }
        }
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
    public void toRestart(View v)
    {
        //stuur id van img mee en moeilijkheid
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("imagebm", imgnr);
        intent.putExtra("moeilijkheid", moeilijkheid);
        startActivity(intent);
        finish();
    }
}
