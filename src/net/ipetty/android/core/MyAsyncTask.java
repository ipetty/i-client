package net.ipetty.android.core;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 异步任务封装，未完待续：TODO:
 * @author xiao
 *
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public abstract  class MyAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	private Exception e = null;  
    private Context context;  
  
    public MyAsyncTask(Context context) {  
        super();  
        this.context = context;  
    }  
    /** 
     * 直接填写要加入的任务即可，不需要关心异常处理。 
     * 注意：禁止在此处调用super.doInBackground()否则会出现无限递归，导致stackOverFlow异常 
     * @param params 
     * @return 
     * @throws Exception 
     */  
    protected abstract Result doInBackgroundX(Params... params) throws Exception;  
  
    /** 
     * 返回的结果不需要判断是否为空 
     *注意：禁止在此处调用super.doPostExecute()否则会出现无限递归，导致stackOverFlow异常 
     * @param result 
     */  
    protected abstract void onPostExecuteX(Result result);  
  
    /** 
     * 显示刷新界面 
     */  
    protected abstract void showRefreshView();  
  
    @Override  
    protected void onPreExecute() {  
        super.onPreExecute();  
        //显示刷新界面  
        showRefreshView();  
       
    }  
  
    @Override  
    protected Result doInBackground(Params... params) {  
        try {  
            return doInBackgroundX(params);  
        } catch (Exception e) {  
            this.e = e;  
            e.printStackTrace();  
            return null;  
        }  
  
    }  
  
    @Override  
    protected void onPostExecute(Result result) {  
        super.onPostExecute(result);  
        //如果当前任务已经取消了，则直接返回  
        if(isCancelled()){  
            return;  
        }  
        if (result == null) {  
           
            //显示刷新界面  
            showRefreshView();  
        } else {  
            onPostExecuteX(result);  
        }  
    }  
    
}
