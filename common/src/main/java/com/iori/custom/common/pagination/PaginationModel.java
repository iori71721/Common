package com.iori.custom.common.pagination;

import android.util.Log;

import androidx.annotation.IntDef;

import com.iori.custom.common.executeModel.SingleExecuteModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @useage
 * 1.constructor and setup callback
 * 2.setup init info,example
 *   paginationModel.setPreLoadCount(5);
 *   paginationModel.setPageCount(10);
 *   paginationModel.setInitPageIndex(0);
 * 3.{@link #getData(int)} to start get pagination and lock model to request repeat pagination
 * 4.call finish method to unlock model when data get finish
 *   example:when get load data finish call {@link #finishGetLocalData(List, Task)}
 * @param <D> pagination data type
 */
public class PaginationModel<D> {
    public static final String TAG=PaginationModel.class.getName();
    private int remoteTotalCount;
    private int currentLocalCount;
    private int currentRemoteCount;
    /**
     * if server init page index is 0,will setup -1
     */
    private int initPageIndex=-1;
    private int currentPageIndex;
    private int pageCount=300;
    private int preLoadCount=150;
    protected final List<D> localLoadDatas =new ArrayList<>(500);
    protected final List<D> remoteLoadDatas =new ArrayList<>(500);
    private PaginationModelCallback modelCallback;
    private SingleExecuteModel singleExecuteModel=new SingleExecuteModel();
    private final BlockingQueue<Task> waitExecuteTasks=new LinkedBlockingQueue<>();
    private final Map<String,Task> waitExecuteTasksMap=new HashMap<>(100);
    private Task currentExecuteTask;

    private boolean loadData(int index){
        if(currentLocalCount == 0){
            return true;
        }else{
            return currentLocalCount < remoteTotalCount && preload(index);
        }
    }

    private boolean preload(int index){
        return currentLocalCount-index <= preLoadCount;
    }

    private void addTaskAndExecute(int requestPageIndex, int pageCount, int currentLocalCount){
        TaskInfo taskInfo=new TaskInfo(requestPageIndex,pageCount,currentLocalCount);
        Task task=new Task(taskInfo,modelCallback,this);
        synchronized (waitExecuteTasks){
            waitExecuteTasks.add(task);
            waitExecuteTasksMap.put(task.ID,task);
            if(waitExecuteTasks.size() > 0){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            waitExecuteTasks.take().execute();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    public void getData(int index){
        if(loadData(index)){
            singleExecuteModel.setSingleExecuteBehavior(new SingleExecuteModel.SingleExecuteBehavior() {
                @Override
                public void execute() {
                    if(modelCallback != null){
                        int requestPageIndex;
                        if(currentLocalCount == 0){
                            requestPageIndex=initPageIndex+1;
                        }else{
                            requestPageIndex=currentPageIndex+1;
                        }
                        addTaskAndExecute(requestPageIndex,pageCount,currentLocalCount);
                    }
                }
            });
            singleExecuteModel.execute();
        }
    }

    public void cancelTask(String taskID){
        Task cancelTask;
        synchronized (waitExecuteTasksMap){
            cancelTask=waitExecuteTasksMap.get(taskID);
            if(cancelTask != null){
                cancelTask.cancel();
            }
        }
    }

    public void reset(){
        remoteTotalCount=0;
        currentLocalCount =0;
        currentPageIndex=0;
        currentRemoteCount=0;
        synchronized (localLoadDatas){
            localLoadDatas.clear();
        }
        synchronized (remoteLoadDatas){
            remoteLoadDatas.clear();
        }
        synchronized (waitExecuteTasks){
            waitExecuteTasks.clear();
        }
        synchronized (waitExecuteTasksMap){
            for(Task cancelTask:waitExecuteTasksMap.values()){
                cancelTask(cancelTask.getID());
                removeFinishTaskAndUnLockModel(cancelTask.getID());
            }
            waitExecuteTasksMap.clear();
        }
    }

    public PaginationModelCallback getModelCallback() {
        return modelCallback;
    }

    public void setModelCallback(PaginationModelCallback modelCallback) {
        this.modelCallback = modelCallback;
    }

    public void finishGetLocalData(List<D> localDatas,Task executeTask){
        if(!executeTask.isCancel()) {
            currentLocalCount += updateGetLocalData(localDatas);
        }else{
            if(executeTask != null){
                Log.i(TAG, "finishGetLocalData: cancel task id "+executeTask.getID());
            }
        }
    }

    /**
     *
     * @param localDatas
     * @return update count
     */
    protected int updateGetLocalData(List<D> localDatas){
        if(localDatas != null) {
            synchronized (localLoadDatas) {
                localLoadDatas.addAll(localDatas);
                return localDatas.size();
            }
        }else{
            return 0;
        }
    }

    /**
     *
     * @param remoteDatas
     * @param remoteTotalCount
     * @param pageIndex
     * @param pageCount
     * @return update count
     */
    protected int updateGetRemoteData(List<D> remoteDatas,int remoteTotalCount,int pageIndex,int pageCount){
        if(remoteDatas != null){
            synchronized (remoteLoadDatas){
                remoteLoadDatas.addAll(remoteDatas);
                return remoteDatas.size();
            }
        }else{
            return 0;
        }
    }

    public void finishGetRemoteData(List<D> remoteDatas,int remoteTotalCount,int pageIndex,int pageCount,Task executeTask){
        if(!executeTask.isCancel()) {

            currentPageIndex = pageIndex;
            this.remoteTotalCount = remoteTotalCount;
            currentRemoteCount += updateGetRemoteData(remoteDatas, remoteTotalCount, pageIndex, pageCount);
            if (currentLocalCount != currentRemoteCount) {
                currentLocalCount = currentRemoteCount;
            }
            executeTask.finish();
        }else{
            if(executeTask != null) {
                Log.i(TAG, "finishGetRemoteData: cancel task id "+executeTask.getID());
            }
        }
        removeFinishTaskAndUnLockModel(executeTask.getID());
    }

    public void failGetRemoteData(Task executeTask){
        executeTask.fail();
        removeFinishTaskAndUnLockModel(executeTask.getID());
    }

    /**
     * finish task is finish,cancel,fail
     */
    private void removeFinishTaskAndUnLockModel(String taskID){
        synchronized (waitExecuteTasksMap){
            waitExecuteTasksMap.remove(taskID);
            singleExecuteModel.finish();
        }
    }

    public void setInitPageIndex(int initPageIndex) {
        this.initPageIndex = initPageIndex;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setPreLoadCount(int preLoadCount) {
        this.preLoadCount = preLoadCount;
    }

    public int getInitPageIndex() {
        return initPageIndex;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getPreLoadCount() {
        return preLoadCount;
    }

    public int getRemoteTotalCount() {
        return remoteTotalCount;
    }

    public int getCurrentLocalCount() {
        return currentLocalCount;
    }

    public int getCurrentRemoteCount() {
        return currentRemoteCount;
    }

    public Task getCurrentExecuteTask() {
        return currentExecuteTask;
    }

    public static class Task{
        public static final int IDLE=0;
        public static final int EXECUTE=1;
        public static final int FINISH=2;
        public static final int CANCEL=4;
        public static final int FAIL=5;
        private final int invalidInt=-1;
        private final TaskInfo taskInfo;
        private final String ID;
        private final PaginationModelCallback modelCallback;
        private final PaginationModel paginationModel;
        private long cancelTime=invalidInt;
        private @Status int status=IDLE;

        public Task(TaskInfo taskInfo,PaginationModelCallback modelCallback,PaginationModel paginationModel) {
            ID = UUID.randomUUID().toString();
            this.taskInfo = taskInfo;
            this.modelCallback=modelCallback;
            this.paginationModel=paginationModel;
        }

        public void execute(){
            if(modelCallback != null) {
                if(isCancel()) {
                    modelCallback.cancel(paginationModel, this);
                    paginationModel.removeFinishTaskAndUnLockModel(ID);
                }else{
                    paginationModel.currentExecuteTask=this;
                    status=EXECUTE;
                    modelCallback.getLocalData(paginationModel, taskInfo.currentLocalCount, taskInfo.pageCount,this);
                    modelCallback.getRemoteData(paginationModel, taskInfo.requestPageIndex, taskInfo.pageCount,this);
                }
            }
        }

        private void finish(){
            status=FINISH;
        }

        private void cancel(){
            cancelTime= Calendar.getInstance().getTimeInMillis();
            status=CANCEL;
        }

        private void fail(){
            status=FAIL;
        }

        private boolean isCancel(){
            return status == CANCEL && cancelTime != invalidInt;
        }

        private boolean isFail(){
            return status == FAIL;
        }

        public TaskInfo getTaskInfo() {
            return taskInfo;
        }

        public String getID() {
            return ID;
        }

        public int getStatus() {
            return status;
        }

        @IntDef({IDLE,EXECUTE,FINISH,CANCEL,FAIL})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Status{}
    }

    public static class TaskInfo{
        private int requestPageIndex;
        private int pageCount;
        private int currentLocalCount;

        public TaskInfo(int requestPageIndex, int pageCount, int currentLocalCount) {
            this.requestPageIndex = requestPageIndex;
            this.pageCount = pageCount;
            this.currentLocalCount = currentLocalCount;
        }
    }

    public static interface PaginationModelCallback<D>{
        void getLocalData(PaginationModel<D> paginationModel,int currentLocalCount,int getCount,Task executeTask);
        void getRemoteData(PaginationModel<D> paginationModel,int pageIndex,int pageCount,Task executeTask);
        void cancel(PaginationModel<D> paginationModel,Task cancelTask);
    }
}
