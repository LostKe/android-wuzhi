package zs.com.wuzhi.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhangshuqing on 16/8/23.
 */
public class DBHelper  extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "wuzhi.db", null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_diary="create table diary(_id integer primary key autoincrement, key varchar(50), content text)";
        db.execSQL(sql_diary);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql_follow="create table follow(_id integer primary key autoincrement, pid varchar(50))";
        db.execSQL(sql_follow);
    }

    public void insertDiary(String key,String text){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql="insert into diary (key,content) values(?,?)";
        Object[] args={key,text};
        db.execSQL(sql,args);
        db.close();
    }

    public void insertFollow(String pid){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql="insert into follow (pid) values(?)";
        Object[] args={pid};
        db.execSQL(sql,args);
        db.close();
    }

    public String  findContent(String key){
        String content="";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cur=db.rawQuery("select content from diary where key=?", new String[]{key});
        if(cur!=null && cur.getCount()>0){
            cur.moveToNext();
            content=cur.getString(cur.getColumnIndex("content"));


        }
        db.close();
        return content;
    }

    public boolean isExistFollow(String pid){
        boolean b=false;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cur=db.rawQuery("select * from follow where pid=?", new String[]{pid});
        if(cur!=null && cur.getCount()>0){
            b=true;
        }
        db.close();
        return b;
    }

    public void deleteFollowByPid(String pid){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("follow","pid=?",new String[]{pid});
        db.close();
    }

    public void clearDiary(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("diary", null, null);
        db.close();
    }

}
