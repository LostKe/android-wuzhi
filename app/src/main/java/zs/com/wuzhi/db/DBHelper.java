package zs.com.wuzhi.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangshuqing on 16/8/23.
 */
public class DBHelper  extends SQLiteOpenHelper {

    public static int mPageSize=15;

    public static void setPageSize(int pageSize){
        mPageSize=pageSize;
    }

    public DBHelper(Context context) {
        super(context, "wuzhi.db", null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        createTable(db);
    }

    private void createTable(SQLiteDatabase db){
        String sql_diary="create table if not exists diary(_id integer primary key autoincrement, key varchar(50), content text)";
        db.execSQL(sql_diary);
        String sql_follow="create table if not exists  follow(_id integer primary key autoincrement, pid varchar(50))";
        db.execSQL(sql_follow);
        String sql_gesture="create table if not exists gesture (_id integer primary key autoincrement, gesture_key varchar(50))";
        db.execSQL(sql_gesture);
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

    public void insertGesture(String gesture_key){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("gesture",null,null);
        String sql="insert into gesture (gesture_key) values(?)";
        Object[] args={gesture_key};
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

    public String  findGestureKey(){
        String gesture_key="";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cur=db.rawQuery("select * from gesture",null);
        if(cur!=null && cur.getCount()>0){
            cur.moveToNext();
            gesture_key=cur.getString(cur.getColumnIndex("gesture_key"));
        }
        db.close();
        return gesture_key;
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

    /**
     * 分页查询
     * @param currentPage  请求第n页   n从0开始
     * @return
     */
    public List<String> pageQuery4Follow(int currentPage){
        List<String> result=new ArrayList<>();
        int index=currentPage*mPageSize;
        int end=(currentPage+1)*mPageSize;
        String limit=index+","+end;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cur=db.query("follow",new String[]{"pid"},null,null,null,null,null,limit);
        if(cur!=null && cur.getColumnCount()>0){
            while (cur.moveToNext()){
                String content=cur.getString(cur.getColumnIndex("pid"));
                result.add(content);
            }
        }
        db.close();
        return result;
    }


    public List<String> queryFollowAll(){
        List<String> result=new ArrayList<>();
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cur=db.rawQuery("select * from follow order by _id desc", null);
        if(cur!=null && cur.getColumnCount()>0){
            while (cur.moveToNext()){
                String content=cur.getString(cur.getColumnIndex("pid"));
                result.add(content);
            }

        }
        db.close();
        return result;
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
