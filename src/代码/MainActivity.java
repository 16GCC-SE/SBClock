package com.example.administrator.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ListActivity implements SearchView.OnQueryTextListener {
    SimpleAdapter listAdapter;
    int index = 0;
    PopupWindow mPopupWindow = null;
    ArrayList<HashMap<String,String>> showlist,list = Utils.getList();
    DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this, "memorandum_db");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SearchView searchview = (SearchView)findViewById(R.id.searchView);
        searchview.setOnQueryTextListener(this);

        list = Utils.getList();
        if(list.isEmpty())
            loadFromDatabase(list);
        Utils.MillisToDate(list);
        listAdapter = new SimpleAdapter(this,list,R.layout.list_item,new String[]{"datetime","content"},
                new int[]{R.id.datetime,R.id.content});
        setListAdapter(listAdapter);
        Button button = (Button)findViewById(R.id.createButton);
        button.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Utils.DateToMillis(list);
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                Bundle b = new Bundle();
                b.putString("datetime", "");
                b.putString("content", "");
                b.putString("alerttime","");
                intent.putExtra("android.intent.extra.INTENT", b);
                startActivity(intent);
            }
        });
        getListView().setOnItemClickListener(new ListItemClickListener ());
        getListView().setOnItemLongClickListener(new ItemLongClickListener());
    }

    class ItemLongClickListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {
            index = position;

            View popupView = getLayoutInflater().inflate(R.layout.popupwindow,null);
            mPopupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
            //mPopupWindow.setAnimationStyle(R.style.popupAnimation);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable ());
            mPopupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);

            Button deleteButton = (Button)popupView.findViewById(R.id.deleteButton);
            Button shareButton = (Button)popupView.findViewById(R.id.shareButton);

            deleteButton.setOnClickListener(new View.OnClickListener(){
                //长按某个任务，下面就会弹出删除的按钮，点击即可删除此任务，备忘录中再也查找不到此任务
                @Override
                public void onClick(View v) {
                    deleteItem(index);
                    mPopupWindow.dismiss();
                }

            });
            //分享按钮
            shareButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    shareItem(index);
                    mPopupWindow.dismiss();
                }
            });
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.class.getProtectionDomain (), menu);
        return false;
    }

    private void loadFromDatabase(ArrayList<HashMap<String,String>> list){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("user", new String[] { "datetime", "content","alerttime" }, null,
                null, null, null,"datetime desc");
        while (cursor.moveToNext()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                String datetime = cursor.getString(0);
                String content = cursor.getString(1);
                String alerttime = cursor.getString(2);
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("datetime", datetime);
                map.put("content", content);
                map.put("alerttime", alerttime);
                list.add(map);
            }
        }
    }
    class ListItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Intent itemintent = new Intent(MainActivity.this,EditActivity.class);
            Utils.DateToMillis(list);
            Bundle b = new Bundle();
            b.putString("datetime", Utils.getItem(position).get("datetime"));
            b.putString("content", Utils.getItem(position).get("content"));
            b.putString("alerttime", Utils.getItem(position).get("alerttime"));
            b.putInt("index", position);
            itemintent.putExtra("android.intent.extra.INTENT", b);
            startActivity(itemintent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (true) {
            return true;
        }
        return true;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Utils.sort();
        Utils.MillisToDate(list);
        getListView().setOnItemClickListener(new ListItemClickListener());
        listAdapter = new SimpleAdapter(this,list,R.layout.list_item,new String[]{"datetime","content"},
                new int[]{R.id.datetime,R.id.content});
        setListAdapter(listAdapter);
    }
    @Override
    public void onBackPressed() {
        Utils.DateToMillis(list);
        this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //初始化载入数据库的数据
        list = Utils.getList();
        if(newText != null){
            showlist = new ArrayList<HashMap<String,String>>();
            for(int i=0;i<list.size();i++){
                String content = list.get(i).get("content");
                if(content.contains(newText)){
                    HashMap<String,String> map = list.get(i);
                    map.put("id", String.valueOf(i));
                    showlist.add(map);
                }
            }
            //先检查缓存，若没有数据再从数据库加载
            Utils.MillisToDate(list);
            listAdapter = new SimpleAdapter(this,showlist,R.layout.list_item,new String[]{"datetime","content"},
                    new int[]{R.id.datetime,R.id.content});
            setListAdapter(listAdapter);//将备忘录数据显示出来
            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener () {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent searchintent = new Intent(MainActivity.this,EditActivity.class);
                    Utils.DateToMillis(list);
                    Bundle b = new Bundle();
                    b.putString("datetime", showlist.get(position).get("datetime"));
                    b.putString("content", showlist.get(position).get("content"));
                    b.putString("alerttime",showlist.get(position).get("alerttime"));
                    b.putInt("index", Integer.parseInt(showlist.get(position).get("id")));
                    searchintent.putExtra("android.intent.extra.INTENT", b);
                    startActivity(searchintent);                                //����ת����Activity
                }
            });
        }
        return false;
    }

    private boolean deleteItem(int position){
        Utils.DateToMillis(list);
        ListView listview = getListView();
        String deleteDatetime = ((HashMap<String, String>)(listview.getItemAtPosition(index))).get("datetime").toString();
        Utils.getList().remove(index);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteUtils sqlite = new SQLiteUtils();
        sqlite.delete(dbHelper, deleteDatetime);
        Utils.sort();
        Utils.MillisToDate(list);
        listAdapter.notifyDataSetChanged();//更新ListView的数据显示
        return true;
    }

    private void shareItem(int index) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, ""+Utils.getItem(index).get("content"));
        startActivity(Intent.createChooser(intent, ""));
    }

}
