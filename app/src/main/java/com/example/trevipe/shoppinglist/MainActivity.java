package com.example.trevipe.shoppinglist;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.regions.Regions;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.google.gson.*;
import java.util.List;
import android.app.AlertDialog;
import android.widget.EditText;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<ShoppingItem> shoppingList = null;
    ArrayAdapter<String> adapter = null;
    ListView lv = null;
    double getBudget=0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        shoppingList = new ArrayList<ShoppingItem>();
        ShoppingItem item1 = new ShoppingItem("banana", 5, 1, 1);
        shoppingList.add(item1);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, shoppingList);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }
        if (id == R.id.action_save) {
            String[] stArray = new String[shoppingList.size()];
            for(int a=0;a<stArray.length;a++) {
                stArray[a] = gson.toJson(shoppingList.get(a));
                System.out.println(stArray[a]);
            }
            String list_json = gson.toJson(stArray);
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:8c72e443-de30-49dc-88c2-4d550a4faba9", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );
            // Initialize the Cognito Sync client
            CognitoSyncManager syncClient = new CognitoSyncManager(
                    getApplicationContext(),
                    Regions.US_EAST_1, // Region
                    credentialsProvider);

// Create a record in a dataset and synchronize with the server
            Dataset dataset = syncClient.openOrCreateDataset("myDataset");
            dataset.put("jsonedString", list_json);
            dataset.synchronize(new DefaultSyncCallback() {
                @Override
                public void onSuccess(Dataset dataset, List newRecords) {
                    //Your handler code here
                }
            });
//            System.out.println("list_json contents: "+list_json);
//            String[] testArray = new String[stArray.length];
//            testArray = gson.fromJson(list_json, String[].class);
//            ShoppingItem[] testShoppingArray = new ShoppingItem[testArray.length];
//            for(int b=0;b<testArray.length;b++){
//                testShoppingArray[b]=gson.fromJson(testArray[b],ShoppingItem.class);
//                System.out.println(testShoppingArray[b]);
//            }
// store in SharedPreferences
//            String gsonId = "list"; // get storage key
//            editor.putString(gsonId, list_json);
//            editor.commit();
            adapter.clear();
            return true;
        }
        if (id == R.id.action_load) {
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:8c72e443-de30-49dc-88c2-4d550a4faba9", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );
            // Initialize the Cognito Sync client
            CognitoSyncManager syncClient = new CognitoSyncManager(
                    getApplicationContext(),
                    Regions.US_EAST_1, // Region
                    credentialsProvider);
            Dataset dataset = syncClient.openOrCreateDataset("myDataset");
            String value = dataset.get("jsonedString");
            // do the reverse operation
            String[] testArray = new String[gson.fromJson(value,String[].class).length];
            testArray = gson.fromJson(value, String[].class);
            for(int b=0;b<testArray.length;b++){
                shoppingList.add(gson.fromJson(testArray[b],ShoppingItem.class));
            }
            for (int a=0;a<shoppingList.size();a++){
                System.out.println("loading "+shoppingList.get(a));
            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, shoppingList);
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            return true;
        }
//// time flies...
//
        if (id==R.id.action_setParam) {
            AlertDialog.Builder paramBuilder = new AlertDialog.Builder(this);
            paramBuilder.setTitle("Set Budget");
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            final EditText budget = new EditText(this);
            budget.setHint("Enter Budget for Trip");
            layout.addView(budget);
            paramBuilder.setView(layout);
            paramBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        getBudget = Double.parseDouble(budget.getText().toString());
                    } catch (NumberFormatException e) {
                    }
                }
            });
            paramBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            paramBuilder.show();
            return true;
        }
        if (id == R.id.action_add) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Item");
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            final EditText itemName = new EditText(this);
            itemName.setHint("Item Name");
            layout.addView(itemName);
            final EditText itemPrice = new EditText(this);
            itemPrice.setHint("Item Price");
            layout.addView(itemPrice);
            final EditText itemPriority = new EditText(this);
            itemPriority.setHint("Item Priority");
            layout.addView(itemPriority);
            final EditText itemQuantity = new EditText(this);
            itemQuantity.setHint("Item Quantity");
            layout.addView(itemQuantity);
            builder.setView(layout);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = itemName.getText().toString();
                    double price = 0;
                    int quantity = 0;
                    int priority = 0;
                    try {
                        price = Double.parseDouble(itemPrice.getText().toString());
                    } catch (NumberFormatException e) {

                    }
                    try {
                        quantity = Integer.parseInt(itemQuantity.getText().toString());
                    } catch (NumberFormatException e) {

                    }
                    try {
                        priority = Integer.parseInt(itemPriority.getText().toString());
                    } catch (NumberFormatException e) {

                    }

                    ShoppingItem tempItem = new ShoppingItem(name, price, priority, quantity);
                    shoppingList.add(tempItem);
                    lv.setAdapter(adapter);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.trevipe.shoppinglist/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.trevipe.shoppinglist/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
