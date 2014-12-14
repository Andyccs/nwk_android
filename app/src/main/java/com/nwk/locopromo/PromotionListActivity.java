package com.nwk.locopromo;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class PromotionListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_HOME_AS_UP);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_promotion_list, menu);

        MenuItem menuItem = menu.findItem(R.id.action_favorite);

        //TODO check if the shop has been favorite
//        menuItem.collapseActionView();
//        menuItem.setActionView(null);
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //favorite the shop
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //unfavorite the shop
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            item.setActionView(R.layout.action_favorite);
            item.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.collapseActionView();
                    item.setActionView(null);
                }
            });
            item.expandActionView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
