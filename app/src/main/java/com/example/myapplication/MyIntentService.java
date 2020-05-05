package com.example.myapplication;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.TransactionTooLargeException;
import android.os.UserHandle;
import android.util.EventLogTags;
import android.util.Log;
import android.util.StatsLog;

import java.util.ArrayList;

public  class MyIntentService extends IntentService {
    public static final String INDEX_FLAG="index_flag";
    private static final String TAG = "MyIntentService";
    public static UpdateUI updateUI;


    public static void setUpdateUI(UpdateUI updateUIInterface){
        updateUI=updateUIInterface;
    }

    public MyIntentService(){
        super("MyIntentService");
    }

    /**
     * 实现异步任务的方法
     * @param intent Activity传递过来的Intent,数据封装在intent中
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        //在子线程中进行网络请求
        Log.i(TAG, this.getClass().getSimpleName()+"---onHandleIntent: ------Thread:"+Thread.currentThread().getName());
        Message msg1 = new Message();
        msg1.what = intent.getIntExtra(INDEX_FLAG,0)+10;
        //通知主线程去更新UI
        if(updateUI!=null){
            updateUI.updateUI(msg1);
        }
    }
    //----------------------重写一下方法仅为测试------------------------------------------
    @Override
    public void onCreate() {
        Log.i(TAG,"onCreate");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(TAG,"onStart---startId："+startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"onStartCommand-----startId："+startId);
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind");
        return super.onBind(intent);
    }


    public interface UpdateUI{
        void updateUI(Message message);
    }

//    private final void sendServiceArgsLocked(ServiceRecord r, boolean execInFg,
//                                             boolean oomAdjusted) throws TransactionTooLargeException {
//        final int N = r.pendingStarts.size();
//        if (N == 0) {
//            return;
//        }
//
//        ArrayList<ServiceStartArgs> args = new ArrayList<>();
//
//        while (r.pendingStarts.size() > 0) {
//            ServiceRecord.StartItem si = r.pendingStarts.remove(0);
//            if (DEBUG_SERVICE) {
//                Slog.v(TAG_SERVICE, "Sending arguments to: "
//                        + r + " " + r.intent + " args=" + si.intent);
//            }
//            if (si.intent == null && N > 1) {
//                // If somehow we got a dummy null intent in the middle,
//                // then skip it.  DO NOT skip a null intent when it is
//                // the only one in the list -- this is to support the
//                // onStartCommand(null) case.
//                continue;
//            }
//            si.deliveredTime = SystemClock.uptimeMillis();
//            r.deliveredStarts.add(si);
//            si.deliveryCount++;
//            if (si.neededGrants != null) {
//                mAm.mUgmInternal.grantUriPermissionUncheckedFromIntent(si.neededGrants,
//                        si.getUriPermissionsLocked());
//            }
//            mAm.grantEphemeralAccessLocked(r.userId, si.intent, UserHandle.getAppId(r.appInfo.uid),
//                    UserHandle.getAppId(si.callingId));
//            bumpServiceExecutingLocked(r, execInFg, "start");
//            if (!oomAdjusted) {
//                oomAdjusted = true;
//                mAm.updateOomAdjLocked(r.app, true, OomAdjuster.OOM_ADJ_REASON_START_SERVICE);
//            }
//            if (r.fgRequired && !r.fgWaiting) {
//                if (!r.isForeground) {
//                    if (DEBUG_BACKGROUND_CHECK) {
//                        Slog.i(TAG, "Launched service must call startForeground() within timeout: " + r);
//                    }
//                    scheduleServiceForegroundTransitionTimeoutLocked(r);
//                } else {
//                    if (DEBUG_BACKGROUND_CHECK) {
//                        Slog.i(TAG, "Service already foreground; no new timeout: " + r);
//                    }
//                    r.fgRequired = false;
//                }
//            }
//            int flags = 0;
//            if (si.deliveryCount > 1) {
//                flags |= Service.START_FLAG_RETRY;
//            }
//            if (si.doneExecutingCount > 0) {
//                flags |= Service.START_FLAG_REDELIVERY;
//            }
//            args.add(new ServiceStartArgs(si.taskRemoved, si.id, flags, si.intent));
//        }
//
//        ParceledListSlice<ServiceStartArgs> slice = new ParceledListSlice<>(args);
//        slice.setInlineCountLimit(4);
//        Exception caughtException = null;
//        try {
//            r.app.thread.scheduleServiceArgs(r, slice);
//        } catch (TransactionTooLargeException e) {
//            if (DEBUG_SERVICE) Slog.v(TAG_SERVICE, "Transaction too large for " + args.size()
//                    + " args, first: " + args.get(0).args);
//            Slog.w(TAG, "Failed delivering service starts", e);
//            caughtException = e;
//        } catch (RemoteException e) {
//            // Remote process gone...  we'll let the normal cleanup take care of this.
//            if (DEBUG_SERVICE) Slog.v(TAG_SERVICE, "Crashed while sending args: " + r);
//            Slog.w(TAG, "Failed delivering service starts", e);
//            caughtException = e;
//        } catch (Exception e) {
//            Slog.w(TAG, "Unexpected exception", e);
//            caughtException = e;
//        }
//
//        if (caughtException != null) {
//            // Keep nesting count correct
//            final boolean inDestroying = mDestroyingServices.contains(r);
//            for (int i = 0; i < args.size(); i++) {
//                serviceDoneExecutingLocked(r, inDestroying, inDestroying);
//            }
//            if (caughtException instanceof TransactionTooLargeException) {
//                throw (TransactionTooLargeException)caughtException;
//            }
//        }
//    }


    /**
     * Note the name of this method should not be confused with the started services concept.
     * The "start" here means bring up the instance in the client, and this method is called
     * from bindService() as well.
     */
//    private final void realStartServiceLocked(ServiceRecord r,
//                                              ProcessRecord app, boolean execInFg) throws RemoteException {
//        if (app.thread == null) {
//            throw new RemoteException();
//        }
//        if (DEBUG_MU)
//            Slog.v(TAG_MU, "realStartServiceLocked, ServiceRecord.uid = " + r.appInfo.uid
//                    + ", ProcessRecord.uid = " + app.uid);
//        r.setProcess(app);
//        r.restartTime = r.lastActivity = SystemClock.uptimeMillis();
//
//        final boolean newService = app.services.add(r);
//        bumpServiceExecutingLocked(r, execInFg, "create");
//        mAm.updateLruProcessLocked(app, false, null);
//        updateServiceForegroundLocked(r.app, /* oomAdj= */ false);
//        mAm.updateOomAdjLocked(OomAdjuster.OOM_ADJ_REASON_START_SERVICE);
//
//        boolean created = false;
//        try {
//            if (LOG_SERVICE_START_STOP) {
//                String nameTerm;
//                int lastPeriod = r.shortInstanceName.lastIndexOf('.');
//                nameTerm = lastPeriod >= 0 ? r.shortInstanceName.substring(lastPeriod)
//                        : r.shortInstanceName;
//                EventLogTags.writeAmCreateService(
//                        r.userId, System.identityHashCode(r), nameTerm, r.app.uid, r.app.pid);
//            }
//            StatsLog.write(StatsLog.SERVICE_LAUNCH_REPORTED, r.appInfo.uid, r.name.getPackageName(),
//                    r.name.getClassName());
//            synchronized (r.stats.getBatteryStats()) {
//                r.stats.startLaunchedLocked();
//            }
//            mAm.notifyPackageUse(r.serviceInfo.packageName,
//                    PackageManager.NOTIFY_PACKAGE_USE_SERVICE);
//            app.forceProcessStateUpTo(ActivityManager.PROCESS_STATE_SERVICE);
//            app.thread.scheduleCreateService(r, r.serviceInfo,
//                    mAm.compatibilityInfoForPackage(r.serviceInfo.applicationInfo),
//                    app.getReportedProcState());
//            r.postNotification();
//            created = true;
//        } catch (DeadObjectException e) {
//            Slog.w(TAG, "Application dead when creating service " + r);
//            mAm.appDiedLocked(app);
//            throw e;
//        } finally {
//            if (!created) {
//                // Keep the executeNesting count accurate.
//                final boolean inDestroying = mDestroyingServices.contains(r);
//                serviceDoneExecutingLocked(r, inDestroying, inDestroying);
//
//                // Cleanup.
//                if (newService) {
//                    app.services.remove(r);
//                    r.setProcess(null);
//                }
//
//                // Retry.
//                if (!inDestroying) {
//                    scheduleServiceRestartLocked(r, false);
//                }
//            }
//        }
//
//        if (r.whitelistManager) {
//            app.whitelistManager = true;
//        }
//
//        requestServiceBindingsLocked(r, execInFg);
//
//        updateServiceClientActivitiesLocked(app, null, true);
//
//        if (newService && created) {
//            app.addBoundClientUidsOfNewService(r);
//        }
//
//        // If the service is in the started state, and there are no
//        // pending arguments, then fake up one so its onStartCommand() will
//        // be called.
//        if (r.startRequested && r.callStart && r.pendingStarts.size() == 0) {
//            r.pendingStarts.add(new ServiceRecord.StartItem(r, false, r.makeNextStartId(),
//                    null, null, 0));
//        }
//
//        sendServiceArgsLocked(r, execInFg, true);
//
//        if (r.delayed) {
//            if (DEBUG_DELAYED_STARTS) Slog.v(TAG_SERVICE, "REM FR DELAY LIST (new proc): " + r);
//            getServiceMapLocked(r.userId).mDelayedStartList.remove(r);
//            r.delayed = false;
//        }
//
//        if (r.delayedStop) {
//            // Oh and hey we've already been asked to stop!
//            r.delayedStop = false;
//            if (r.startRequested) {
//                if (DEBUG_DELAYED_STARTS) Slog.v(TAG_SERVICE,
//                        "Applying delayed stop (from start): " + r);
//                stopServiceLocked(r);
//            }
//        }
//    }
}