package com.nimrag.kevin.aweweico.lib;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kevin on 2017/2/25.
 * 其实就是AsyncTask
 */

public abstract class WorkTask <Params, Progress, Result>{

    private static final String TAG = "WorkTask";

    private static final int CORE_IMAGE_POOL_SIZE = 10;
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int KEEP_ALIVE = 1;

    private Exception exception;

    private boolean cancelByUser;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(10);

    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
    public static final Executor IMAGE_POOL_EXECUTOR = Executors.newFixedThreadPool(CORE_IMAGE_POOL_SIZE, sThreadFactory);
    public static final Executor SERIAL_EXECUTOR = new SerialExecutor();

    private static final int MESSAGE_POST_RESULT = 0x1;
    private static final int MESSAGE_POST_PROGRESS = 0x2;

    private static final InternalHandler sHandler = new InternalHandler();

    private static volatile Executor sDefaultExecutor = SERIAL_EXECUTOR;
    private final WorkerRunnable<Params, Result> mWorker;
    private final FutureTask<Result> mFuture;

    private volatile Status mStatus = Status.PENDING;

    private final AtomicBoolean mTaskInvoked = new AtomicBoolean();

    private static class SerialExecutor implements Executor {
        final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
        Runnable mActive;

        public synchronized void execute(final Runnable r) {
            mTasks.offer(new Runnable() {
                @Override
                public void run() {
                    try {
                        r.run();
                    } finally {
                        scheduleNext();
                    }
                }
            });
            if (mActive == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            if((mActive = mTasks.poll()) != null) {
                THREAD_POOL_EXECUTOR.execute(mActive);
            }
        }
    }

    public enum Status {
        PENDING,
        RUNNING,
        FINISHED,
    }

    public static void init() {
        sHandler.getLooper();
    }

    private static void setsDefaultExecutor(Executor exec) {
        sDefaultExecutor = exec;
    }

    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    //public WorkTask(String taskId, ITaskManager taskManager) {
        //this();
        //this.taskId = taskId;
        //taskManager.addTask(this);
    //}

    public WorkTask() {
        mWorker = new WorkerRunnable<Params, Result>() {
            @Override
            public Result call() throws Exception {
                mTaskInvoked.set(true);

                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                return postResult(doInBackground(mParams));
            }
        };

        mFuture = new FutureTask<Result>(mWorker) {
            @Override
            protected void done() {
                try {
                    final Result result = get();
                    postResultIfNotInvoked(result);
                } catch (InterruptedException e) {
                    android.util.Log.w(TAG, e);
                } catch (ExecutionException e) {
                    throw new RuntimeException("An error occured while executing doInBackground()", e.getCause());
                } catch (CancellationException e) {
                    postResultIfNotInvoked(null);
                } catch (Throwable t) {
                    throw new RuntimeException("An error occured while executing " + "doInBackground", t);
                }
            }
        };
    }

    private void postResultIfNotInvoked(Result result) {
        final boolean wasTaskInvoked = mTaskInvoked.get();
        if(!wasTaskInvoked) {
            postResult(result);
        }
    }

    private Result postResult(Result result) {
        Message message = sHandler.obtainMessage(MESSAGE_POST_RESULT, new AsyncTaskResult<Result>(this, result));
        message.sendToTarget();
        return result;
    }

    public final Status getStatus() {
        return mStatus;
    }

    protected void onPrepare() {

    }

    protected void onFailure(Exception exception) {

    }

    protected void onSuccess(Result result) {

    }

    protected Params[] getParams() {
        return mWorker.mParams;
    }

    protected void onFinished() {

    }

    abstract public Result workInBackground(Params... params) throws Exception;

    private Result doInBackground(Params... params) {
        try {
            return workInBackground(params);
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
        }
        return null;
    }

    final protected void onPreExecute() {
        onPrepare();
    }

    final protected void onPostExecute(Result result) {
        if(exception == null) {
            onSuccess(result);
        } else if(exception != null) {
            onFailure(exception);
        }
        onFinished();
    }


    protected void onProgressUpdate(Progress... values) {

    }

    protected void onCancelled(Result result) {
        _onCancelled();
    }

    private void _onCancelled() {
        onCancelled();
        onFinished();
    }

    protected void onCancelled() {
        Log.d(TAG, String.format("%s --> onCancelled()", TextUtils.isEmpty(taskId) ? "run" : (taskId + "run")));
    }

    public boolean isCancelled() {
        return mFuture.isCancelled();
    }

    public boolean isCancelByUser() {
        return cancelByUser;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        cancelByUser = true;
        return mFuture.cancel(mayInterruptIfRunning);
    }

    public final Result get() throws InterruptedException, ExecutionException {
        return mFuture.get();
    }

    public final Result get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return mFuture.get(timeout, unit);
    }

    public final WorkTask<Params, Progress, Result> executeOnSerialExecutor(Params... params) {
        return executeOnExecutor(SERIAL_EXECUTOR, params);
    }

    public final WorkTask<Params, Progress, Result> executeOnImageExecutor(Params... params) {
        return executeOnExecutor(IMAGE_POOL_EXECUTOR, params);
    }

    public final WorkTask<Params, Progress, Result> executeOnExecutor(Executor exec, Params... params) {
        if (mStatus != Status.PENDING) {
            switch (mStatus) {
                case RUNNING:
                    throw new IllegalStateException("Cannot execute task:" + "the task is already running");
                case FINISHED:
                    throw new IllegalStateException("Cannot execute task:" + "the task has already been executed " + "(a task can be executed only once)");
            }
        }

        mStatus = Status.RUNNING;

        if (Looper.myLooper() == Looper.getMainLooper()) {
            onPreExecute();
        } else {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    onPreExecute();
                }
            });
        }

        mWorker.mParams = params;
        exec.execute(mFuture);

        return this;
    }

    public static void execute(Runnable runnable) {
        sDefaultExecutor.execute(runnable);
    }

    protected final void publishProgress(Progress... values) {
        if (!isCancelled()) {
            sHandler.obtainMessage(MESSAGE_POST_PROGRESS, new AsyncTaskResult<Progress>(this, values)).sendToTarget();
        }
    }

    private void finish(Result result) {
        if (!isCancelled()) {
            onCancelled(result);
        } else {
            onPostExecute(result);
        }
        mStatus = Status.FINISHED;
    }

    private static class InternalHandler extends Handler {
        InternalHandler() {
            super(Looper.myLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            AsyncTaskResult result = (AsyncTaskResult)msg.obj;
            switch (msg.what) {
                case MESSAGE_POST_RESULT:
                    result.mTask.finish(result.mData[0]);
                    break;
                case MESSAGE_POST_PROGRESS:
                    result.mTask.onProgressUpdate(result.mData);
            }
        }
    }

    private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
        Params[] mParams;
    }

    private static class AsyncTaskResult<Data> {
        final WorkTask mTask;
        final Data[] mData;

        AsyncTaskResult(WorkTask task, Data... data) {
            mTask = task;
            mData = data;
        }
    }
}
