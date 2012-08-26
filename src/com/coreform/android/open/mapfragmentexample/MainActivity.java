package com.coreform.android.open.mapfragmentexample;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button mExample01Button;
	private Button mExample02Button;
	private Button mExample03AButton;
	private Button mExample03BButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	mExample01Button = (Button) findViewById(R.id.example01Button);
    	mExample02Button = (Button) findViewById(R.id.example02Button);
    	mExample03AButton = (Button) findViewById(R.id.example03AButton);
    	mExample03BButton = (Button) findViewById(R.id.example03BButton);
    	
    	mExample01Button.setOnClickListener(new Example01ButtonOnClickListener());
    	mExample02Button.setOnClickListener(new Example02ButtonOnClickListener());
    	mExample03AButton.setOnClickListener(new Example03AButtonOnClickListener());
    	mExample03BButton.setOnClickListener(new Example03BButtonOnClickListener());
    }
    
    /*
     * METHODS
     */
    
    /*
     * INNER CLASSES
     */
    
    private class Example01ButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, Example01FragmentActivity.class);
			MainActivity.this.startActivity(intent);
		}
    }
    
    private class Example02ButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, Example02FragmentActivity.class);
			MainActivity.this.startActivity(intent);
		}
    }
    
    private class Example03AButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, Example03TabFragmentActivity.class);
			MainActivity.this.startActivity(intent);
		}
    }
    
    private class Example03BButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, Example03TabFragmentActivity.class);
			Bundle fragmentActivityStartupBundle = new Bundle();
			fragmentActivityStartupBundle.putString("targetTabTag", "tab2");
			intent.putExtras(fragmentActivityStartupBundle);
			MainActivity.this.startActivity(intent);
		}
    }
}
