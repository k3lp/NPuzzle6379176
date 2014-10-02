package nl.mprog.projects.nPuzzle6379176;

import java.util.Random;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.view.MenuItem;

@SuppressLint("NewApi") public class GameActivity extends Activity
{

    
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Moeilijkheid = "moeilijkheid"; 
    public static final String ImageNr = "imagenr";
    public static final String State = "state";
    SharedPreferences settings;
    
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
    public int emptyTileRow = 0;
    public int emptyTileColumn = 0;
    public int state = 0;
    public int restart = 0;
    public int afterTimer;
    public int[][] stateTiles  = new int[2][25];
    public int[][] tileIds =
    {
        {R.id.h11, R.id.h12, R.id.h13, R.id.h14, R.id.h15},
        {R.id.h21, R.id.h22, R.id.h23, R.id.h24, R.id.h25},
        {R.id.h31, R.id.h32, R.id.h33, R.id.h34, R.id.h35},
        {R.id.h41, R.id.h42, R.id.h43, R.id.h44, R.id.h45},
        {R.id.h51, R.id.h52, R.id.h53, R.id.h54, R.id.h55}
    };

    
    public int moeilijkheid = 0, imgnr = 0, dimensieTiles = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //krijg info van intents
        moeilijkheid = getIntent().getExtras().getInt("moeilijkheid");
        imgnr = getIntent().getExtras().getInt("imagebm");
        restart = getIntent().getExtras().getInt("restart");

        reqWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        reqHeight= (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        
        //sharedpreferences

        
        dimensieTiles = moeilijkheid + 3;
        
        //maak bitmap van img     
        Bitmap volimgbm = BitmapFactory.decodeResource(getResources(), imageIds[imgnr]);
        //scale bitmap met goede aspect
        Bitmap scaledimgbm = scaleBm(volimgbm);
        
        //menubutton
        maakMenu();
        
        settings = getSharedPreferences(MyPREFERENCES, 0);
        state = settings.getInt(State,0);
        //Log.v("chingchong", "+STATE ONCREATE VOOR TILES+" + state);
        
        
        if(state == 1)
        {
            //stateTiles vullen met tiles van eerdere state
            String savedString = settings.getString("string", "");
            StringTokenizer st = new StringTokenizer(savedString, ",");
            Log.v("chingchong", "dit zit erin RESUME: " + savedString);
            if(savedString != null && !savedString.isEmpty())
            {
                Log.v("chingchong", "+ ZIT IETS IN STRING +");
                System.out.print("state");
                //moet 50
                for (int j = 0; j < 25; j++)
                {
                        stateTiles[1][j] = Integer.parseInt(st.nextToken());
                        stateTiles[0][j] = Integer.parseInt(st.nextToken());
                        Log.v("onresumestring", ""+stateTiles[1][j]);
                        Log.v("onresumestring", ""+stateTiles[0][j]);
                }
            }
        }
        
        
        
        
        
        
        
        
        
        
        
        
        
        //maak tiles
        createTiles(scaledimgbm, moeilijkheid);

        //maak "lege" tile

        afterTimer = 0;
        
        
        settings = getSharedPreferences(MyPREFERENCES, 0);
        state = settings.getInt(State,0);
        //Log.v("chingchong", "+STATE ONCREATE NA TILES+" + state);
        if(state != 1)
        {
            emptyTile = (ImageView) findViewById(tileIds[0][0]);

            //Log.v("sdfasdf", "STATE MOET 0 ZIJN"+ state);
            //laat tiles even zien
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
        }
        else
        {
           // Log.v("sdfasdf", "STATE MOET 1 ZIJN ELSE"+ state);
            afterTimer = 1;
        }

        //debug moeilijkheid
        TextView tv3 = (TextView) findViewById(R.id.testMoeilijkheid);
        tv3.setText("moeilijkheid: " + moeilijkheid);
        //debug imgnr
        TextView tv4 = (TextView) findViewById(R.id.testImgnr);
        tv4.setText("imgnr: " + (imgnr+1));
    }

    
    
    
    @Override
    public void onPause()
    {
        super.onPause();
        Log.v("chingchong", "+ ON PAUSE +");
        settings = getSharedPreferences(MyPREFERENCES, 0);
        Editor editor = settings.edit();
        editor.clear();
        //Log.v("chingchong", "+CLEAR ONPAUSE+");
        //Log.v("chingchong", "+RESTAR = +" + restart);
        if(restart != 1)
        {
            //Log.v("chingchong", "+RESTAR IN IF = +" + restart);
            editor.putInt(Moeilijkheid, moeilijkheid);
            editor.putInt(ImageNr, imgnr);
            //Log.v("chingchong", "+ SET STATE+");
            editor.putInt(State, 1);
            
            
            //Log.v("chingchong", "+state onpause+");
            
            String savedString = "";
            
            
            StringBuilder str = new StringBuilder();

            for (int j = 0; j < 25; j++)
            {
                str.append(stateTiles[1][j]).append(",");
                str.append(stateTiles[0][j]).append(",");

                //Log.v("onpausestring", str.toString());
                savedString = str.toString();
            }
            //String savedString = str.toString();

            editor.putString("string", savedString);
            editor.commit();
            String test = settings.getString("string", "");
            Log.v("chingchong", "dit zit erin onpause: " + test);
            
            //settings.edit().putString("string", str.toString());


        }

        //Log.v("chingchong", "+ ON PAUSE +");
    }

    
   /* @Override
    public void onResume()
    {
        super.onResume();
                Log.v("chingchong", "+ ON RESUME +");
                settings = getSharedPreferences(MyPREFERENCES, 0);
                
                
                //stateTiles vullen met tiles van eerdere state
                String savedString = settings.getString("string", "");
                StringTokenizer st = new StringTokenizer(savedString, ",");
                Log.v("chingchong", "dit zit erin RESUME: " + savedString);
                if(savedString != null && !savedString.isEmpty())
                {
                    Log.v("chingchong", "+ ZIT IETS IN STRING +");
                    System.out.print("state");
                    //moet 50
                    for (int j = 0; j < 25; j++)
                    {
                            stateTiles[1][j] = Integer.parseInt(st.nextToken());
                            stateTiles[0][j] = Integer.parseInt(st.nextToken());
                            Log.v("onresumestring", ""+stateTiles[1][j]);
                            Log.v("onresumestring", ""+stateTiles[0][j]);
                    }
                }

    }*/
    
    
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

        //int aantalTiles = dimensieTiles * dimensieTiles;


        int pt1 = (int)(scaledimgbm.getWidth()/dimensieTiles);
        int pt2 = (int)(scaledimgbm.getHeight()/dimensieTiles);
        int statesCount = 0;
        int stateX = 0;
        int stateY = 0;
        if(moeilijkheid == 0)
        {
            
            final Bitmap [][] bitmapTiles =
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
                    if(state == 0)
                    {
                        if(i == 0 && j == 0)
                        {
                            imageTile.setImageBitmap(bitmapTiles[i][j]);
                            imageTile.setTag(bitmapTiles[i][j]);  
                            imageTile.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            imageTile.setImageBitmap(bitmapTiles[i][j]);
                            imageTile.setTag(bitmapTiles[i][j]);  
                        }
                    }
                    else if(state == 1)
                    {
                        Log.v("chingchong", "dit zit in stateTiles: ");
                        for(int p = 0; p < 25; p++)
                        {
                            Log.v("ditziterin creattiles", ""+stateTiles[0][p]);
                            Log.v("ditziterin creattiles", ""+stateTiles[1][p]);
                        }
                        Log.v("sdfasdf", "STATE CREATETILES 1 "+ state);
                        stateY = stateTiles[1][statesCount];
                        stateX = stateTiles[0][statesCount];
                        Log.v("sdfasdf", "STATE CREATETILES x"+ stateX);
                        Log.v("sdfasdf", "STATE CREATETILES y"+ stateY);
                        if(stateX < 0)
                        {
                            imageTile.setImageBitmap(bitmapTiles[stateX+6][stateY+6]);
                            imageTile.setTag(bitmapTiles[stateX+6][stateY+6]);
                            emptyTile = (ImageView) findViewById(tileIds[i][j]);
                            emptyTileRow = i;
                            emptyTileColumn = j;
                            imageTile.setVisibility(View.INVISIBLE);
                        }
                        else{
                            Log.v("sdfasdf", "STATE MOET 1 ZIJN"+ state);
                            imageTile.setImageBitmap(bitmapTiles[stateX][stateY]);
                            imageTile.setTag(bitmapTiles[stateX][stateY]);
                        }

                        statesCount++;
                    }
                    
                    
                    
                    imageTile.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if(afterTimer == 1)
                            {
                                onClickTileMove(v);
                                checkEndGame(bitmapTiles);
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
    
    
    void checkEndGame(Bitmap [][] bitmapTiles)
    {
        //staan alle bitmaps in volgorde
        //kijk naar elke tileID welke bitmap erin staat
        //vergelijk deze met bitmapTiles
        //als gelijk dan is alles in volgorde en end game
        //state opslaan met bitmapTiles id
        //
        int aantalTiles = dimensieTiles*dimensieTiles;
        int count = 0;
        int tileCount = 0;
        for(int i = 0; i < dimensieTiles; i++)
        {
            for(int j = 0; j < dimensieTiles; j++)
            {
                ImageView viewChecked = (ImageView) findViewById(tileIds[i][j]);
                Bitmap bitmapView = ((BitmapDrawable)viewChecked.getDrawable()).getBitmap();
                Bitmap bitmapOriginalView = bitmapTiles[i][j];

                if(bitmapView.sameAs(bitmapOriginalView))
                {
                    count++;
                }
                //System.out.println(count);
                //loop voor state
                for(int k = 0; k < dimensieTiles; k++)
                {
                    for(int l = 0; l < dimensieTiles; l++)
                    {
                        bitmapOriginalView = bitmapTiles[k][l];
                        if(bitmapView.sameAs(bitmapOriginalView))
                        {
                            stateTiles[0][tileCount]= k;
                            stateTiles[1][tileCount] = l;
                        }
                        //als het de lege tile is
                        if(viewChecked.getVisibility() == View.INVISIBLE)
                        {
                            stateTiles[0][tileCount]= k-6;
                            stateTiles[1][tileCount] = l-6;
                        }
                    }
                }
                tileCount++;
            }
        }
        /*for(int m = 0; m < 9; m++)
        {
                    System.out.println(stateTiles[0][m]);
                    System.out.println(stateTiles[1][m]);
        }*/
        if(count == (aantalTiles-1))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.endgame_text);
            builder.setTitle(R.string.endgame_title);
            builder.setPositiveButton(R.string.endgame_button, 
            new DialogInterface.OnClickListener()
            {
               @Override
               public void onClick(DialogInterface arg0, int arg1)
               {
                   settings = getSharedPreferences(MyPREFERENCES, 0);
                   Editor editor = settings.edit().clear();
                   //Log.v("chingchong", "+CLEAR CLEAR+");
                   // Necessary to clear first if we save preferences onPause. 
                   editor.clear();
                   //Log.v("chingchong", "+CLEAR CLEAR CLEAR+");
                   Intent intent = new Intent(GameActivity.this, MainActivity.class);
                   startActivity(intent);
                   finish();
               }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
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
                if(afterTimer == 1)
                {
                    PopupMenu popup = new PopupMenu(GameActivity.this, buttonMenu);
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu()); 
                    popup.show();  
                }
            }  
        });
    }


    public void toStart(MenuItem item)
    {
        
        restart = 1;
        settings = getSharedPreferences(MyPREFERENCES, 0);
        Editor editor = settings.edit();
        editor.clear();
        //Log.v("chingchong", "+ CLEAR TOSTART+");
        editor.putInt(State, 0);
        //Log.v("chingchong", "+ SET STATE RESTART+");
        editor.commit();
        int test = settings.getInt(State,0);
        //Log.v("chingchong", "+ ON TOSTART +");
        System.out.println(test);
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
