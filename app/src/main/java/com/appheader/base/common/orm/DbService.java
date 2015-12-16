package com.appheader.base.common.orm;

import android.content.Context;
import android.util.Log;

import com.appheader.base.application.MApplication;
import com.appheader.db.DaoSession;
import com.appheader.db.GreenArticle;
import com.appheader.db.GreenArticleDao;

import java.util.List;

public class DbService {
      
    private static final String TAG = DbService.class.getSimpleName();  
    private static DbService instance;  
    private static Context appContext;
    private DaoSession mDaoSession;
    private GreenArticleDao mGreenArticleDao;
      
      
    private DbService() {  
    }  
  
    public static DbService getInstance(Context context) {  
        if (instance == null) {  
            instance = new DbService();  
            if (appContext == null){  
                appContext = context.getApplicationContext();  
            }  
            instance.mDaoSession = MApplication.getDaoSession(context);
            instance.mGreenArticleDao = instance.mDaoSession.getGreenArticleDao();
        }  
        return instance;  
    }  
      
      
    public GreenArticle loadGreenArticle(long id) {
        return mGreenArticleDao.load(id);
    }  
      
    public List<GreenArticle> loadAllGreenArticle(){
        return mGreenArticleDao.loadAll();
    }  
      
    /** 
     * query list with where clause 
     * ex: begin_date_time >= ? AND end_date_time <= ? 
     * @param where where clause, include 'where' word 
     * @param params query parameters 
     * @return 
     */  
      
    public List<GreenArticle> queryGreenArticle(String where, String... params){
        return mGreenArticleDao.queryRaw(where, params);
    }  
      
      
    /** 
     * insert or update note 
     * @param note 
     * @return insert or update note id 
     */  
    public long saveGreenArticle(GreenArticle note){
        return mGreenArticleDao.insertOrReplace(note);
    }  
      
      
    /** 
     * insert or update noteList use transaction 
     * @param list 
     */  
    public void saveGreenArticleLists(final List<GreenArticle> list){
            if(list == null || list.isEmpty()){  
                 return;  
            }
        mGreenArticleDao.getSession().runInTx(new Runnable() {
            @Override  
            public void run() {  
                for(int i=0; i<list.size(); i++){
                    GreenArticle note = list.get(i);
                    mGreenArticleDao.insertOrReplace(note);
                }  
            }  
        });  
          
    }  
      
    /** 
     * delete all note 
     */  
    public void deleteAllGreenArticle(){
        mGreenArticleDao.deleteAll();
    }  
      
    /** 
     * delete note by id 
     * @param id 
     */  
    public void deleteGreenArticle(long id){
        mGreenArticleDao.deleteByKey(id);
        Log.i(TAG, "delete");
    }  
      
    public void deleteGreenArticle(GreenArticle note){
        mGreenArticleDao.delete(note);
    }  
      
}  