/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.core;

/**
 *
 * @author yneos
 * @param <Result>
 */
public interface TaskListener<Result extends Object> {

    public void onPreExecute();

    public void doSuccess(Result result);

    public void onSuccess(Result result);

    public void onProgressUpdate(Integer... prgrss);

    public void onCancelled(Result result);

    public void onCancelled();

    public void onError(Throwable ex);

}
