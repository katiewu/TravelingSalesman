package edu.upenn.cis573.travelingsalesman;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class GameActivity extends ActionBarActivity {

    private int numLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.numLocations = getIntent().getIntExtra("numLocations", 4);
        setContentView(R.layout.play_game);
    }

    public int getNumLocations(){
        return numLocations;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return true;
    }

    /*
    This method is called when the user chooses something in the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_clear) {
            GameView gv = (GameView)findViewById(R.id.gameView);
//            gv.segments.clear();
            gv.clearSegments();
            gv.invalidate();
            return true;
        }
        else if (id == R.id.menu_quit) {
            finish();
            return true;
        } else if (id == R.id.menu_undo) {
            GameView gv = (GameView)findViewById(R.id.gameView);
            if(gv.segmentsSize() > 0){
                gv.removeLastSegment();
            }
            else {
                Toast.makeText(gv.getContext(), "There's nothing to undo.", Toast.LENGTH_LONG).show();
            }
            gv.invalidate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
