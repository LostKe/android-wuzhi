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
        super(context, "wuzhi.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table diary(_id integer primary key autoincrement, key varchar(50), content text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //版本更新时使用
    }

    public void insertDiary(String key,String text){
        //插入之前删除
        SQLiteDatabase db=this.getWritableDatabase();
        String sql="insert into diary (key,content) values(?,?)";
        Object[] args={key,text};
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

    public void clearDiary(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("diary", null, null);
        db.close();
    }

}
