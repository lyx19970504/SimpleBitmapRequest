package com.example.neglide;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

public class RequestManager {

    private static final String TAG = "RequestManager";

    private LinkedBlockingQueue<BitmapRequest> linkedBlockingQueue = new LinkedBlockingQueue<>();
    private static RequestManager requestManager;

    private RequestDispatcher[] requestDispatchers;

    public RequestManager(){
        start();
    }

    public static RequestManager getInstance(){
        if(requestManager == null){
            requestManager = new RequestManager();
        }
        return requestManager;
    }

    public void addBitmapRequest(BitmapRequest request){
        if(request != null && !linkedBlockingQueue.contains(request)){
            linkedBlockingQueue.add(request);
            Log.d(TAG, "addBitmapRequest: ");
        }
    }

    private void start() {
        stop();
        startAllDispatcher();
    }

    private void stop() {
        if(requestDispatchers != null && requestDispatchers.length > 0){
            for (RequestDispatcher requestDispatcher : requestDispatchers){
                if(!requestDispatcher.isInterrupted()){
                    requestDispatcher.interrupt();
                }
            }
        }
    }

    private void startAllDispatcher() {
        int threadCount = Runtime.getRuntime().availableProcessors();
        requestDispatchers = new RequestDispatcher[threadCount];
        Log.d(TAG, "startAllDispatcher: " + threadCount);
        for (int i = 0; i < threadCount; i++) {
            RequestDispatcher requestDispatcher = new RequestDispatcher(linkedBlockingQueue);
            requestDispatcher.start();
            requestDispatchers[i] = requestDispatcher;
        }
    }
}
