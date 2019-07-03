package com.surveyor.app.surveyorapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.surveyor.app.surveyorapp.bean.JobAllSyncDataBean;
import com.surveyor.app.surveyorapp.bean.OptionBean;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    //https://www.simplifiedcoding.net/android-sync-sqlite-database-with-server/

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SurveyorApp";

    private static final String TABLE_TECHNICIAN = "tblTechnician";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_JOB_ID = "job_id";
    private static final String KEY_DATA = "data";
    private static final String KEY_IS_SYNC = "is_sync";
    private static final String KEY_IS_DELETED = "is_deleted";

    public static String CREATE_TABLE_TECHNICIAN = "CREATE TABLE " + TABLE_TECHNICIAN + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_ID + " TEXT,"
            + KEY_JOB_ID + " TEXT," + KEY_DATA + " TEXT,"
            + KEY_IS_DELETED + " INTEGER DEFAULT 0," // 0 = NOT DELETED, 1 = DELETED
            + KEY_IS_SYNC + " INTEGER DEFAULT 0)"; // 0 = SYNC , 1 = NOT SYNC

    private static final String TABLE_TEAM_LEADER = "tblTeamLeader";
    private static final String TL_ID = "id";
    private static final String TL_USER_ID = "user_id";
    private static final String TL_JOB_ID = "job_id";
    private static final String TL_DATA = "data";
    private static final String TL_IS_SYNC = "is_sync";
    private static final String TL_IS_SYNC_TM = "is_sync_tm";
    private static final String TL_IS_DELETED = "is_deleted";

    public static String CREATE_TABLE_TEAM_LEADER = "CREATE TABLE " + TABLE_TEAM_LEADER + "("
            + TL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TL_USER_ID + " TEXT,"
            + TL_JOB_ID + " TEXT," + TL_DATA + " TEXT,"
            + TL_IS_SYNC + " INTEGER DEFAULT 0," // 0 = SYNC , 1 = NOT SYNC
            + TL_IS_SYNC_TM + " INTEGER DEFAULT 0," // 0 = SYNC , 1 = NOT SYNC
            + TL_IS_DELETED + " INTEGER DEFAULT 0)"; // 0 = NOT DELETED, 1 = DELETED

    private static final String TABLE_REMARKS = "tblRemarks";
    private static final String REMARKS_ID = "id";
    private static final String REMARKS_SORT_ORDER = "sortorder";
    private static final String REMARKS_FIELD_TYPE = "fieldtype";
    private static final String REMARKS_OPTION_NAME = "optionname";
    private static final String REMARKS_IS_SYNC = "is_sync";

    public static String CREATE_TABLE_REMARKS = "CREATE TABLE " + TABLE_REMARKS + "("
            + REMARKS_ID + " INTEGER," + REMARKS_SORT_ORDER + " TEXT,"
            + REMARKS_FIELD_TYPE + " TEXT," + REMARKS_OPTION_NAME + " TEXT,"
            + REMARKS_IS_SYNC + " INTEGER DEFAULT 0)"; // 0 = SYNC, 1 = NOT SYNC, 2 = NEW ADDED

    private static final String TABLE_HABITATE = "tblHabitate";
    private static final String HABITATE_ID = "id";
    private static final String HABITATE_SORT_ORDER = "sortorder";
    private static final String HABITATE_FIELD_TYPE = "fieldtype";
    private static final String HABITATE_OPTION_NAME = "optionname";
    private static final String HABITATE_IS_SYNC = "is_sync";

    public static String CREATE_TABLE_HABITATE = "CREATE TABLE " + TABLE_HABITATE + "("
            + HABITATE_ID + " INTEGER," + HABITATE_SORT_ORDER + " TEXT,"
            + HABITATE_FIELD_TYPE + " TEXT," + HABITATE_OPTION_NAME + " TEXT,"
            + HABITATE_IS_SYNC + " INTEGER DEFAULT 0)"; // 0 = SYNC, 1 = NOT SYNC, 2 = NEW ADDED

    private static final String TABLE_FINDINGS = "tblFindings";
    private static final String FINDINGS_ID = "id";
    private static final String FINDINGS_SORT_ORDER = "sortorder";
    private static final String FINDINGS_FIELD_TYPE = "fieldtype";
    private static final String FINDINGS_OPTION_NAME = "optionname";
    private static final String FINDINGS_IS_SYNC = "is_sync";

    public static String CREATE_TABLE_FINDINGS = "CREATE TABLE " + TABLE_FINDINGS + "("
            + FINDINGS_ID + " INTEGER," + FINDINGS_SORT_ORDER + " TEXT,"
            + FINDINGS_FIELD_TYPE + " TEXT," + FINDINGS_OPTION_NAME + " TEXT,"
            + FINDINGS_IS_SYNC + " INTEGER DEFAULT 0)"; // 0 = SYNC, 1 = NOT SYNC, 2 = NEW ADDED

    private static final String TABLE_ACTION = "tblAction";
    private static final String ACTION_ID = "id";
    private static final String ACTION_SORT_ORDER = "sortorder";
    private static final String ACTION_FIELD_TYPE = "fieldtype";
    private static final String ACTION_OPTION_NAME = "optionname";
    private static final String ACTION_IS_SYNC = "is_sync";

    public static String CREATE_TABLE_ACTION = "CREATE TABLE " + TABLE_ACTION + "("
            + ACTION_ID + " INTEGER," + ACTION_SORT_ORDER + " TEXT,"
            + ACTION_FIELD_TYPE + " TEXT," + ACTION_OPTION_NAME + " TEXT,"
            + ACTION_IS_SYNC + " INTEGER DEFAULT 0)"; // 0 = SYNC, 1 = NOT SYNC, 2 = NEW ADDED

    private static final String TABLE_CAUSE = "tblCause";
    private static final String CAUSE_ID = "id";
    private static final String CAUSE_SORT_ORDER = "sortorder";
    private static final String CAUSE_FIELD_TYPE = "fieldtype";
    private static final String CAUSE_OPTION_NAME = "optionname";
    private static final String CAUSE_IS_SYNC = "is_sync";

    public static String CREATE_TABLE_CAUSE = "CREATE TABLE " + TABLE_CAUSE + "("
            + CAUSE_ID + " INTEGER," + CAUSE_SORT_ORDER + " TEXT,"
            + CAUSE_FIELD_TYPE + " TEXT," + CAUSE_OPTION_NAME + " TEXT,"
            + CAUSE_IS_SYNC + " INTEGER DEFAULT 0)"; // 0 = SYNC, 1 = NOT SYNC, 2 = NEW ADDED

    private static final String TABLE_BITCENTER = "tblBitcenter";
    private static final String BITCENTER_ID = "id";
    private static final String BITCENTER_SORT_ORDER = "sortorder";
    private static final String BITCENTER_FIELD_TYPE = "fieldtype";
    private static final String BITCENTER_OPTION_NAME = "optionname";
    private static final String BITCENTER_IS_SYNC = "is_sync";

    public static String CREATE_TABLE_BITCENTER = "CREATE TABLE " + TABLE_BITCENTER + "("
            + BITCENTER_ID + " INTEGER," + BITCENTER_SORT_ORDER + " TEXT,"
            + BITCENTER_FIELD_TYPE + " TEXT," + BITCENTER_OPTION_NAME + " TEXT,"
            + BITCENTER_IS_SYNC + " INTEGER DEFAULT 0)"; // 0 = SYNC, 1 = NOT SYNC, 2 = NEW ADDED

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TECHNICIAN);
        db.execSQL(CREATE_TABLE_TEAM_LEADER);
        db.execSQL(CREATE_TABLE_REMARKS);
        db.execSQL(CREATE_TABLE_FINDINGS);
        db.execSQL(CREATE_TABLE_ACTION);
        db.execSQL(CREATE_TABLE_CAUSE);
        db.execSQL(CREATE_TABLE_BITCENTER);
        db.execSQL(CREATE_TABLE_HABITATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TECHNICIAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM_LEADER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMARKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINDINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAUSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BITCENTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HABITATE);
        onCreate(db);
    }

    public int addJob(String userId, JobAllSyncDataBean bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USER_ID, userId);
        values.put(KEY_JOB_ID, bean.getJoborderid());
        values.put(KEY_DATA, new Gson().toJson(bean));
        // Inserting Row
        long ID = db.insert(TABLE_TECHNICIAN, null, values);
        db.close();
        return (int) ID;
    }

    public int updateJob(String userId, JobAllSyncDataBean bean, int sync) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USER_ID, userId);
        values.put(KEY_JOB_ID, bean.getJoborderid());
        values.put(KEY_DATA, new Gson().toJson(bean));
        values.put(KEY_IS_SYNC, sync);

        int i = db.update(TABLE_TECHNICIAN, values, KEY_JOB_ID + " = " + bean.getJoborderid(), null);
        db.close();
        return i;
    }

    public void deleteJob(String userId, JobAllSyncDataBean bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "DELETE FROM " + TABLE_TECHNICIAN + " WHERE "
                + KEY_JOB_ID + " = '" + bean.getJoborderid() + "' AND "
                + KEY_USER_ID + " = '" + userId + "'";
        db.execSQL(selectQuery);
        db.close();
    }

    public ArrayList<JobAllSyncDataBean> getAllJobs(String userId) {
        ArrayList<JobAllSyncDataBean> myList = new ArrayList<JobAllSyncDataBean>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TECHNICIAN + " WHERE "
                + KEY_USER_ID + " = '" + userId + "'" , null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    String data = cursor.getString(cursor.getColumnIndex(KEY_DATA)) ;
                    JobAllSyncDataBean bean = new Gson().fromJson(data, JobAllSyncDataBean.class);

                    myList.add(bean);
                } while (cursor.moveToNext());
            }
        }

        return myList;
    }

    public ArrayList<JobAllSyncDataBean> getAllUnsyncJobs(String userId) {
        ArrayList<JobAllSyncDataBean> myList = new ArrayList<JobAllSyncDataBean>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TECHNICIAN + " WHERE "
                + KEY_USER_ID + " = '" + userId + "'" + " AND "
                + " AND " + KEY_IS_SYNC + " = '1'" , null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String data = cursor.getString(cursor.getColumnIndex(KEY_DATA)) ;
                    JobAllSyncDataBean bean = new Gson().fromJson(data, JobAllSyncDataBean.class);
                    myList.add(bean);
                } while (cursor.moveToNext());
            }
        }

        return myList;
    }

    public JobAllSyncDataBean getSingleJob(String userId, String jobId){

        JobAllSyncDataBean bean = new JobAllSyncDataBean();

        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TECHNICIAN + " WHERE " + KEY_JOB_ID + " = '" + jobId + "' AND "
                + KEY_USER_ID + " = '" + userId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String data = cursor.getString(cursor.getColumnIndex(KEY_DATA)) ;
                    bean = new Gson().fromJson(data, JobAllSyncDataBean.class);
                } while (cursor.moveToNext());
            }
        }

        return bean;
    }

    public JobAllSyncDataBean getSingleUnsyncJob(String userId){

        JobAllSyncDataBean bean = new JobAllSyncDataBean();

        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TECHNICIAN + " WHERE "
                + KEY_USER_ID + " = '" + userId + "'" + " AND " + KEY_IS_SYNC + " = '1'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String data = cursor.getString(cursor.getColumnIndex(KEY_DATA)) ;
                    bean = new Gson().fromJson(data, JobAllSyncDataBean.class);
                } while (cursor.moveToNext());
            }
        }

        return bean;
    }

    public int getCount() {
        String countQuery = "SELECT * FROM " + TABLE_TECHNICIAN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public int getUnsyncJobCount(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TECHNICIAN + " WHERE "
                + KEY_USER_ID + " = '" + userId + "'"
                + " AND " + KEY_IS_SYNC + " = '1'" , null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public boolean isJobExist(String userId, String jobId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TECHNICIAN + " WHERE " + KEY_JOB_ID + " = '" + jobId + "' AND "
                + KEY_USER_ID + " = '" + userId + "'";

        Cursor cur = db.rawQuery(selectQuery, null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        db.close();
        return exist;
    }

    //teamleader
    public int addJobTL(String userId, JobAllSyncDataBean bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TL_USER_ID, userId);
        values.put(TL_JOB_ID, bean.getJoborderid());
        values.put(TL_DATA, new Gson().toJson(bean));
        // Inserting Row
        long ID = db.insert(TABLE_TEAM_LEADER, null, values);
        db.close();
        return (int) ID;
    }

    public int updateJobTLMain(String userId, JobAllSyncDataBean bean, int sync, int syncTM) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TL_USER_ID, userId);
        values.put(TL_JOB_ID, bean.getJoborderid());
        values.put(TL_DATA, new Gson().toJson(bean));
        values.put(TL_IS_SYNC, sync);
        values.put(TL_IS_SYNC_TM, syncTM);

        int i = db.update(TABLE_TEAM_LEADER, values, TL_JOB_ID + " = " + bean.getJoborderid(), null);
        db.close();
        return i;
    }

    public int updateJobTLForm(String userId, JobAllSyncDataBean bean, int sync) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TL_USER_ID, userId);
        values.put(TL_JOB_ID, bean.getJoborderid());
        values.put(TL_DATA, new Gson().toJson(bean));
        values.put(TL_IS_SYNC, sync);

        int i = db.update(TABLE_TEAM_LEADER, values, TL_JOB_ID + " = " + bean.getJoborderid(), null);
        db.close();
        return i;
    }

    public int updateJobTLTeamMember(String userId, JobAllSyncDataBean bean, int sync) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TL_USER_ID, userId);
        values.put(TL_JOB_ID, bean.getJoborderid());
        values.put(TL_DATA, new Gson().toJson(bean));
        values.put(TL_IS_SYNC_TM, sync);

        int i = db.update(TABLE_TEAM_LEADER, values, TL_JOB_ID + " = " + bean.getJoborderid(), null);
        db.close();
        return i;
    }

    public ArrayList<JobAllSyncDataBean> getAllJobsTL(String userId) {
        ArrayList<JobAllSyncDataBean> myList = new ArrayList<JobAllSyncDataBean>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TEAM_LEADER + " WHERE "
                + TL_USER_ID + " = '" + userId + "'" , null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String data = cursor.getString(cursor.getColumnIndex(TL_DATA)) ;
                    JobAllSyncDataBean bean = new Gson().fromJson(data, JobAllSyncDataBean.class);
                    myList.add(bean);
                } while (cursor.moveToNext());
            }
        }

        return myList;
    }

    public ArrayList<JobAllSyncDataBean> getAllUnsyncJobsTLForm(String userId) {
        ArrayList<JobAllSyncDataBean> myList = new ArrayList<JobAllSyncDataBean>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TEAM_LEADER + " WHERE "
                + TL_USER_ID + " = '" + userId + "'" + " AND "
                + TL_IS_SYNC + " = '1'" , null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String data = cursor.getString(cursor.getColumnIndex(TL_DATA)) ;
                    JobAllSyncDataBean bean = new Gson().fromJson(data, JobAllSyncDataBean.class);
                    myList.add(bean);
                } while (cursor.moveToNext());
            }
        }

        return myList;
    }

    public ArrayList<JobAllSyncDataBean> getAllUnsyncJobsTLTeamMember(String userId) {
        ArrayList<JobAllSyncDataBean> myList = new ArrayList<JobAllSyncDataBean>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TEAM_LEADER + " WHERE "
                + TL_USER_ID + " = '" + userId + "'" + " AND "
                + TL_IS_SYNC_TM + " = '1'" , null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String data = cursor.getString(cursor.getColumnIndex(TL_DATA)) ;
                    JobAllSyncDataBean bean = new Gson().fromJson(data, JobAllSyncDataBean.class);
                    myList.add(bean);
                } while (cursor.moveToNext());
            }
        }

        return myList;
    }

    public int getCountTL() {
        String countQuery = "SELECT * FROM " + TABLE_TEAM_LEADER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public boolean isJobExistTL(String userId, String jobId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TEAM_LEADER + " WHERE " + TL_JOB_ID + " = '" + jobId + "' AND "
                + TL_USER_ID + " = '" + userId + "'";

        Cursor cur = db.rawQuery(selectQuery, null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        db.close();
        return exist;
    }

    public int getUnsyncJobCountTL(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TEAM_LEADER + " WHERE "
                + TL_USER_ID + " = '" + userId + "'" + " AND " + TL_IS_SYNC + " = '1'" , null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public int getUnsyncJobCountTLTeamMember(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TEAM_LEADER + " WHERE "
                + TL_USER_ID + " = '" + userId + "'" + " AND " + TL_IS_SYNC_TM + " = '1'" , null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public JobAllSyncDataBean getSingleJobTL(String userId, String jobId){

        JobAllSyncDataBean bean = new JobAllSyncDataBean();

        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TEAM_LEADER + " WHERE " + TL_JOB_ID + " = '" + jobId + "' AND "
                + TL_USER_ID + " = '" + userId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String data = cursor.getString(cursor.getColumnIndex(TL_DATA)) ;
                    bean = new Gson().fromJson(data, JobAllSyncDataBean.class);
                    Log.e("CheckSyncBean",checkAllJobDataSync(bean) + "");
                } while (cursor.moveToNext());
            }
        }
        return bean;
    }

    private boolean checkAllJobDataSync(JobAllSyncDataBean bean) {
        int count = 0;
        for (int i = 0; i < bean.getAllSurvey().size(); i++) {
            if (bean.getAllSurvey().get(i).getIsSync() == 0) {
                count++;
            }
        }

        Log.e("CheckSyncBean 1 ",count + " " + bean.getAllSurvey().size());
        if (count == bean.getAllSurvey().size()) {
            return true;
        }
        return false;
    }

    //remarks
    public int addRemarks(OptionBean.Remarks bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(REMARKS_ID, bean.getId());
        values.put(REMARKS_SORT_ORDER, bean.getSortorder());
        values.put(REMARKS_FIELD_TYPE, bean.getFieldType());
        values.put(REMARKS_OPTION_NAME, bean.getOptionname());
        values.put(REMARKS_IS_SYNC, bean.getIsSync());
        // Inserting Row
        long ID = db.insert(TABLE_REMARKS, null, values);
        db.close();
        return (int) ID;
    }

    public void deleteRemarks() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_REMARKS);
        db.close();
    }

    public void deleteFindings() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_FINDINGS);
        db.close();
    }

    public void deleteAction() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ACTION);

        db.close();
    }

    public void deleteCause() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CAUSE);
        db.close();
    }

    public void deleteBitcenter() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BITCENTER);
        db.close();
    }

    public void deleteHabitate() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_HABITATE);
        db.close();
    }

    //

    public boolean isRemarksExist(String remarksId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_REMARKS + " WHERE " + REMARKS_ID + " = '" + remarksId + "'";
        Cursor cur = db.rawQuery(selectQuery, null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        db.close();
        return exist;
    }

    public int updateRemarks(OptionBean.Remarks bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(REMARKS_ID, bean.getId());
        values.put(REMARKS_SORT_ORDER, bean.getSortorder());
        values.put(REMARKS_FIELD_TYPE, bean.getFieldType());
        values.put(REMARKS_OPTION_NAME, bean.getOptionname());
        values.put(REMARKS_IS_SYNC, bean.getIsSync());

        int i = db.update(TABLE_REMARKS, values, REMARKS_ID + " = '" + bean.getId()+"'", null);
        db.close();
        return i;
    }

    public ArrayList<OptionBean.Remarks> getAllRemarks() {
        ArrayList<OptionBean.Remarks> myList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REMARKS, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    OptionBean optionBean = new OptionBean();
                    OptionBean.Remarks bean = optionBean.new Remarks();

                    bean.setId(cursor.getString(cursor.getColumnIndex(REMARKS_ID)));
                    bean.setSortorder(cursor.getString(cursor.getColumnIndex(REMARKS_SORT_ORDER)));
                    bean.setFieldType(cursor.getString(cursor.getColumnIndex(REMARKS_FIELD_TYPE)));
                    bean.setOptionname(cursor.getString(cursor.getColumnIndex(REMARKS_OPTION_NAME)));
                    bean.setIsSync(cursor.getInt(cursor.getColumnIndex(REMARKS_IS_SYNC)));

                    myList.add(bean);
                } while (cursor.moveToNext());
            }
        }

        return myList;
    }

    public int getRemarksCount() {
        String countQuery = "SELECT * FROM " + TABLE_REMARKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //finding
    public int addFindings(OptionBean.Findings bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FINDINGS_ID, bean.getId());
        values.put(FINDINGS_SORT_ORDER, bean.getSortorder());
        values.put(FINDINGS_FIELD_TYPE, bean.getFieldType());
        values.put(FINDINGS_OPTION_NAME, bean.getOptionname());
        values.put(FINDINGS_IS_SYNC, bean.getIsSync());
        // Inserting Row
        long ID = db.insert(TABLE_FINDINGS, null, values);
        db.close();
        return (int) ID;
    }

    public boolean isFindingsExist(String remarksId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_FINDINGS + " WHERE " + FINDINGS_ID + " = '" + remarksId + "'";

        Cursor cur = db.rawQuery(selectQuery, null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        db.close();
        return exist;
    }

    public int updateFindings(OptionBean.Findings bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FINDINGS_ID, bean.getId());
        values.put(FINDINGS_SORT_ORDER, bean.getSortorder());
        values.put(FINDINGS_FIELD_TYPE, bean.getFieldType());
        values.put(FINDINGS_OPTION_NAME, bean.getOptionname());
        values.put(FINDINGS_IS_SYNC, bean.getIsSync());

        int i = db.update(TABLE_FINDINGS, values, FINDINGS_ID + " = '" + bean.getId()+"'", null);
        db.close();
        return i;
    }

    public ArrayList<OptionBean.Findings> getAllFindings() {
        ArrayList<OptionBean.Findings> myList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FINDINGS, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    OptionBean optionBean = new OptionBean();
                    OptionBean.Findings bean = optionBean.new Findings();

                    bean.setId(cursor.getString(cursor.getColumnIndex(FINDINGS_ID)));
                    bean.setSortorder(cursor.getString(cursor.getColumnIndex(FINDINGS_SORT_ORDER)));
                    bean.setFieldType(cursor.getString(cursor.getColumnIndex(FINDINGS_FIELD_TYPE)));
                    bean.setOptionname(cursor.getString(cursor.getColumnIndex(FINDINGS_OPTION_NAME)));

                    bean.setIsSync(cursor.getInt(cursor.getColumnIndex(FINDINGS_IS_SYNC)));

                    myList.add(bean);
                } while (cursor.moveToNext());
            }
        }

        return myList;
    }

    public int getFindingsCount() {
        String countQuery = "SELECT * FROM " + TABLE_FINDINGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //action
    public int addAction(OptionBean.ActionTaken bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ACTION_ID, bean.getId());
        values.put(ACTION_SORT_ORDER, bean.getSortorder());
        values.put(ACTION_FIELD_TYPE, bean.getFieldType());
        values.put(ACTION_OPTION_NAME, bean.getOptionname());
        values.put(ACTION_IS_SYNC, bean.getIsSync());

        // Inserting Row
        long ID = db.insert(TABLE_ACTION, null, values);
        db.close();
        return (int) ID;
    }

    public boolean isActionExist(String remarksId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ACTION + " WHERE " + ACTION_ID + " = '" + remarksId + "'";
        Cursor cur = db.rawQuery(selectQuery, null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        db.close();
        return exist;
    }

    public int updateAction(OptionBean.ActionTaken bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ACTION_ID, bean.getId());
        values.put(ACTION_SORT_ORDER, bean.getSortorder());
        values.put(ACTION_FIELD_TYPE, bean.getFieldType());
        values.put(ACTION_OPTION_NAME, bean.getOptionname());
        values.put(ACTION_IS_SYNC, bean.getIsSync());

        int i = db.update(TABLE_ACTION, values, ACTION_ID + " = '" + bean.getId()+"'", null);
        db.close();
        return i;
    }

    public ArrayList<OptionBean.ActionTaken> getAllAction() {
        ArrayList<OptionBean.ActionTaken> myList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ACTION, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    OptionBean optionBean = new OptionBean();
                    OptionBean.ActionTaken bean = optionBean.new ActionTaken();

                    bean.setId(cursor.getString(cursor.getColumnIndex(ACTION_ID)));
                    bean.setSortorder(cursor.getString(cursor.getColumnIndex(ACTION_SORT_ORDER)));
                    bean.setFieldType(cursor.getString(cursor.getColumnIndex(ACTION_FIELD_TYPE)));
                    bean.setOptionname(cursor.getString(cursor.getColumnIndex(ACTION_OPTION_NAME)));
                    bean.setIsSync(cursor.getInt(cursor.getColumnIndex(ACTION_IS_SYNC)));

                    myList.add(bean);
                } while (cursor.moveToNext());
            }
        }

        return myList;
    }

    public int getActionCount() {
        String countQuery = "SELECT * FROM " + TABLE_ACTION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //cause
    public int addCause(OptionBean.ProbableCauseOfBurrows bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CAUSE_ID, bean.getId());
        values.put(CAUSE_SORT_ORDER, bean.getSortorder());
        values.put(CAUSE_FIELD_TYPE, bean.getFieldType());
        values.put(CAUSE_OPTION_NAME, bean.getOptionname());
        values.put(CAUSE_IS_SYNC, bean.getIsSync());
        // Inserting Row
        long ID = db.insert(TABLE_CAUSE, null, values);
        db.close();
        return (int) ID;
    }

    public boolean isCauseExist(String remarksId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CAUSE + " WHERE " + CAUSE_ID + " = '" + remarksId + "'";
        Cursor cur = db.rawQuery(selectQuery, null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        db.close();
        return exist;
    }

    public int updateCause(OptionBean.ProbableCauseOfBurrows bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CAUSE_ID, bean.getId());
        values.put(CAUSE_SORT_ORDER, bean.getSortorder());
        values.put(CAUSE_FIELD_TYPE, bean.getFieldType());
        values.put(CAUSE_OPTION_NAME, bean.getOptionname());
        values.put(CAUSE_IS_SYNC, bean.getIsSync());

        int i = db.update(TABLE_CAUSE, values, CAUSE_ID + " = '" + bean.getId()+"'", null);
        db.close();
        return i;
    }

    public ArrayList<OptionBean.ProbableCauseOfBurrows> getAllCause() {
        ArrayList<OptionBean.ProbableCauseOfBurrows> myList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CAUSE, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    OptionBean optionBean = new OptionBean();
                    OptionBean.ProbableCauseOfBurrows bean = optionBean.new ProbableCauseOfBurrows();

                    bean.setId(cursor.getString(cursor.getColumnIndex(CAUSE_ID)));
                    bean.setSortorder(cursor.getString(cursor.getColumnIndex(CAUSE_SORT_ORDER)));
                    bean.setFieldType(cursor.getString(cursor.getColumnIndex(CAUSE_FIELD_TYPE)));
                    bean.setOptionname(cursor.getString(cursor.getColumnIndex(CAUSE_OPTION_NAME)));
                    bean.setIsSync(cursor.getInt(cursor.getColumnIndex(CAUSE_IS_SYNC)));

                    myList.add(bean);
                } while (cursor.moveToNext());
            }
        }

        return myList;
    }

    public int getCauseCount() {
        String countQuery = "SELECT * FROM " + TABLE_CAUSE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //bitcenter
    public int addBitcenter(OptionBean.BinCenter bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BITCENTER_ID, bean.getId());
        values.put(BITCENTER_SORT_ORDER, bean.getSortorder());
        values.put(BITCENTER_FIELD_TYPE, bean.getFieldType());
        values.put(BITCENTER_OPTION_NAME, bean.getOptionname());
        values.put(BITCENTER_IS_SYNC, bean.getIsSync());
        // Inserting Row
        long ID = db.insert(TABLE_BITCENTER, null, values);
        db.close();
        return (int) ID;
    }

    public boolean isBitcenterExist(String remarksId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_BITCENTER + " WHERE " + BITCENTER_ID + " = '" + remarksId + "'";
        Cursor cur = db.rawQuery(selectQuery, null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        db.close();
        return exist;
    }

    public int updateBitcenter(OptionBean.BinCenter bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BITCENTER_ID, bean.getId());
        values.put(BITCENTER_SORT_ORDER, bean.getSortorder());
        values.put(BITCENTER_FIELD_TYPE, bean.getFieldType());
        values.put(BITCENTER_OPTION_NAME, bean.getOptionname());
        values.put(BITCENTER_IS_SYNC, bean.getIsSync());

        int i = db.update(TABLE_BITCENTER, values, BITCENTER_ID + " = '" + bean.getId()+"'", null);
        db.close();
        return i;
    }

    public ArrayList<OptionBean.BinCenter> getAllBitcenter() {
        ArrayList<OptionBean.BinCenter> myList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BITCENTER, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    OptionBean optionBean = new OptionBean();
                    OptionBean.BinCenter bean = optionBean.new BinCenter();

                    bean.setId(cursor.getString(cursor.getColumnIndex(BITCENTER_ID)));
                    bean.setSortorder(cursor.getString(cursor.getColumnIndex(BITCENTER_SORT_ORDER)));
                    bean.setFieldType(cursor.getString(cursor.getColumnIndex(BITCENTER_FIELD_TYPE)));
                    bean.setOptionname(cursor.getString(cursor.getColumnIndex(BITCENTER_OPTION_NAME)));
                    bean.setIsSync(cursor.getInt(cursor.getColumnIndex(BITCENTER_IS_SYNC)));

                    myList.add(bean);
                } while (cursor.moveToNext());
            }
        }

        return myList;
    }

    public int getBitcenterCount() {
        String countQuery = "SELECT * FROM " + TABLE_BITCENTER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //habitate
    public int addHabitate(OptionBean.Habitate bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(HABITATE_ID, bean.getId());
        values.put(HABITATE_SORT_ORDER, bean.getSortorder());
        values.put(HABITATE_FIELD_TYPE, bean.getFieldType());
        values.put(HABITATE_OPTION_NAME, bean.getOptionname());
        values.put(HABITATE_IS_SYNC, bean.getIsSync());
        // Inserting Row
        long ID = db.insert(TABLE_HABITATE, null, values);
        db.close();
        return (int) ID;
    }

    public boolean isHabitateExist(String remarksId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String remarks = remarksId.replace("'","''");

        String selectQuery = "SELECT * FROM " + TABLE_HABITATE + " WHERE " + HABITATE_ID + " = '" + remarksId + "'";
        Cursor cur = db.rawQuery(selectQuery, null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        db.close();
        return exist;
    }

    public int updateHabitate(OptionBean.Habitate bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(HABITATE_ID, bean.getId());
        values.put(HABITATE_SORT_ORDER, bean.getSortorder());
        values.put(HABITATE_FIELD_TYPE, bean.getFieldType());
        values.put(HABITATE_OPTION_NAME, bean.getOptionname());
        values.put(HABITATE_IS_SYNC, bean.getIsSync());

        int i = db.update(TABLE_HABITATE, values, HABITATE_ID + " = '" + bean.getId()+"'", null);
        db.close();
        return i;
    }

    public ArrayList<OptionBean.Habitate> getAllHabitate() {
        ArrayList<OptionBean.Habitate> myList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HABITATE, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    OptionBean optionBean = new OptionBean();
                    OptionBean.Habitate bean = optionBean.new Habitate();

                    bean.setId(cursor.getString(cursor.getColumnIndex(HABITATE_ID)));
                    bean.setSortorder(cursor.getString(cursor.getColumnIndex(HABITATE_SORT_ORDER)));
                    bean.setFieldType(cursor.getString(cursor.getColumnIndex(HABITATE_FIELD_TYPE)));
                    bean.setOptionname(cursor.getString(cursor.getColumnIndex(HABITATE_OPTION_NAME)));
                    bean.setIsSync(cursor.getInt(cursor.getColumnIndex(HABITATE_IS_SYNC)));

                    myList.add(bean);
                } while (cursor.moveToNext());
            }
        }

        return myList;
    }

    public int getHabitateCount() {
        String countQuery = "SELECT * FROM " + TABLE_HABITATE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    /* public ArrayList<InsertBean> getDates() {
        ArrayList<InsertBean> myList = new ArrayList<InsertBean>();
        String selectQuery = "SELECT DISTINCT " + KEY_REMINDER_DATE + "," + KEY_REMINDER_DATE_DISPLAY
        + " FROM " + TABLE_TECHNICIAN + " ORDER BY " + KEY_REMINDER_DATE + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    InsertBean bean = new InsertBean();
                    try {
                        String reminderDate = cursor.getString(cursor.getColumnIndex(KEY_REMINDER_DATE));
                        bean.setReminderDate(SharedObjects.convertDateTime(reminderDate,SharedObjects.FormatDateYMD,SharedObjects.FormatDateDMY));
                        String reminderDateDisplay = cursor.getString(cursor.getColumnIndex(KEY_REMINDER_DATE_DISPLAY));
                        bean.setReminderDateDisplay(reminderDateDisplay);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    myList.add(bean);
                } while (cursor.moveToNext());
            }
        }

        return myList;
    }

    public ArrayList<InsertBean> getRecordsByDates(String date) {
        ArrayList<InsertBean> myList = new ArrayList<InsertBean>();
        String selectQuery = "SELECT DISTINCT " + KEY_REMINDER_DATE + "," + KEY_REMINDER_DATE_DISPLAY + " FROM " + TABLE_TECHNICIAN
                + " WHERE " + KEY_REMINDER_DATE + " = '" + date + "'"
                + " ORDER BY " + KEY_REMINDER_DATE + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    InsertBean bean = new InsertBean();

                    try {
                        String reminderDate = cursor.getString(cursor.getColumnIndex(KEY_REMINDER_DATE));
                        bean.setReminderDate(SharedObjects.convertDateTime(reminderDate,SharedObjects.FormatDateYMD,SharedObjects.FormatDateDMY));
                        String reminderDateDisplay = cursor.getString(cursor.getColumnIndex(KEY_REMINDER_DATE_DISPLAY));
                        bean.setReminderDateDisplay(reminderDateDisplay);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    myList.add(bean);
                } while (cursor.moveToNext());
            }
        }

        return myList;
    }

    public ArrayList<InsertBean> getOverdue(String date) {
        ArrayList<InsertBean> myList = new ArrayList<InsertBean>();
        String selectQuery = "SELECT * FROM " + TABLE_TECHNICIAN
                + " WHERE " + KEY_REMINDER_DATE + " <= '" + date + "'"
                + " ORDER BY " + KEY_REMINDER_DATE + " DESC";

        Log.e("Select query ",selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    InsertBean bean = new InsertBean();

                    bean.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    bean.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                    bean.setTodo((cursor.getString(cursor.getColumnIndex(KEY_TODO))));
                    bean.setReminderTime((cursor.getString(cursor.getColumnIndex(KEY_REMINDER_TIME))));

                    try {
                        String reminderDate = cursor.getString(cursor.getColumnIndex(KEY_REMINDER_DATE));
                        bean.setReminderDate(SharedObjects.convertDateTime(reminderDate,SharedObjects.FormatDateYMD,SharedObjects.FormatDateDMY));

                        String reminderDateDisplay = cursor.getString(cursor.getColumnIndex(KEY_REMINDER_DATE_DISPLAY));
                        bean.setReminderDateDisplay(reminderDateDisplay);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    myList.add(bean);
                } while (cursor.moveToNext());
            }
        }

        return myList;
    }

    public ArrayList<InsertBean> getUpcoming(String date) {
        ArrayList<InsertBean> myList = new ArrayList<InsertBean>();
        String selectQuery = "SELECT * FROM " + TABLE_TECHNICIAN
                + " WHERE " + KEY_REMINDER_DATE + " > '" + date + "'"
                + " ORDER BY " + KEY_REMINDER_DATE + " DESC";

        Log.e("Select query ",selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    InsertBean bean = new InsertBean();

                    bean.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    bean.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                    bean.setTodo((cursor.getString(cursor.getColumnIndex(KEY_TODO))));
                    bean.setReminderTime((cursor.getString(cursor.getColumnIndex(KEY_REMINDER_TIME))));

                    try {
                        String reminderDate = cursor.getString(cursor.getColumnIndex(KEY_REMINDER_DATE));
                        bean.setReminderDate(SharedObjects.convertDateTime(reminderDate,SharedObjects.FormatDateYMD,SharedObjects.FormatDateDMY));

                        String reminderDateDisplay = cursor.getString(cursor.getColumnIndex(KEY_REMINDER_DATE_DISPLAY));
                        bean.setReminderDateDisplay(reminderDateDisplay);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    myList.add(bean);
                } while (cursor.moveToNext());
            }
        }

        return myList;
    }

    public ArrayList<InsertBean> getPaymentByDate(String remindDate) {
        ArrayList<InsertBean> myList = new ArrayList<InsertBean>();

        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TECHNICIAN + " WHERE " + KEY_REMINDER_DATE + " = '" + remindDate + "'" ;

        Log.e("Query : ",selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

*//*        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TECHNICIAN + " WHERE " + KEY_DATE +
                " BETWEEN CAST('" + startDate  + "' AS DATE) " + " AND CAST('" + endDate  + "' AS DATE)", null);*//*

*//*        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TECHNICIAN + " WHERE " + KEY_DATE +
                " >= '" + startDate + "'" + " AND " + KEY_DATE + " <= '" + endDate + "'", null);*//*

        //CAST('2009-12-15' AS DATE)
        //WHERE column_name BETWEEN value1 AND value2;
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    InsertBean bean = new InsertBean();

                    bean.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    bean.setTodo((cursor.getString(cursor.getColumnIndex(KEY_TODO))));
                    bean.setStatus((cursor.getString(cursor.getColumnIndex(KEY_STATUS))));
                    bean.setReminderTime((cursor.getString(cursor.getColumnIndex(KEY_REMINDER_TIME))));

                    try {
                        String reminderDate = cursor.getString(cursor.getColumnIndex(KEY_REMINDER_DATE));
                        bean.setReminderDate(SharedObjects.convertDateForDisplay(reminderDate));
                        String reminderDateDisplay = cursor.getString(cursor.getColumnIndex(KEY_REMINDER_DATE_DISPLAY));
                        bean.setReminderDateDisplay(reminderDateDisplay);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    myList.add(bean);
                } while (cursor.moveToNext());
            }
        }

        return myList;
    }*/

}