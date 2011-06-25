package es2.learning;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class LightingActivity extends Activity {
	ES2SurfaceView  view;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        view = new ES2SurfaceView(this);
        
        setContentView(view);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(Menu.NONE, 0, Menu.NONE, "Per Vertex");
    	menu.add(Menu.NONE, 1, Menu.NONE, "Per Pixel");
      return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	if (item.getItemId() == 0)
    	{
    		view.LoadProgram(item.getItemId());
    	} else if (item.getItemId() == 1)
    	{
    		view.LoadProgram(item.getItemId());
    	} else {	
    		return super.onOptionsItemSelected(item);
    	}
    	return true;
    }
}