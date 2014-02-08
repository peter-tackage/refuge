package com.moac.android.refuge.database;

import android.database.SQLException;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.moac.android.refuge.model.PersistableObject;

import java.util.List;

public class DatabaseService {

    private final DatabaseHelper mDbHelper;

    public DatabaseService(DatabaseHelper helper) {
        mDbHelper = helper;
    }

    public <T extends PersistableObject> List<T> queryAll(Class<T> objClass) {
        return mDbHelper.queryAll(objClass);
    }

    public <T extends PersistableObject> T queryById(long id, Class<T> objClass) {
        return mDbHelper.queryById(id, objClass);
    }

    @SuppressWarnings("unchecked")
    public <T extends PersistableObject> T queryById(T obj) {
        return (T) mDbHelper.queryById(obj.getId(), obj.getClass());
    }

    public <T extends PersistableObject> long create(T obj) {
        return mDbHelper.create(obj, obj.getClass());
    }

    public <T extends PersistableObject> void update(T obj) {
        mDbHelper.update(obj, obj.getClass());
    }

    public <T extends PersistableObject> void delete(T obj) {
        mDbHelper.deleteById(obj.getId(), obj.getClass());
    }

    public <T extends PersistableObject> void delete(long id, Class<T> objClass) {
        mDbHelper.deleteById(id, objClass);
    }

    /**
     * Bespoke queries - examples
     */
//
//    public List<Restriction> queryAllRestrictionsForMemberId(long memberId) {
//        try {
//            return mDbHelper.getDaoEx(Restriction.class).queryBuilder()
//                    .where().eq(Restriction.Columns.MEMBER_ID_COLUMN, memberId)
//                    .query();
//        } catch(java.sql.SQLException e) {
//            throw new SQLException(e.getMessage());
//        }
//    }
//
//    public boolean queryIsRestricted(long fromMemberId, long toMemberId) {
//        try {
//            Restriction restriction = mDbHelper.getDaoEx(Restriction.class).queryBuilder()
//                    .where().eq(Restriction.Columns.MEMBER_ID_COLUMN, fromMemberId)
//                    .and().eq(Restriction.Columns.OTHER_MEMBER_ID_COLUMN, toMemberId)
//                    .queryForFirst();
//            return restriction != null;
//        } catch(java.sql.SQLException e) {
//            throw new SQLException(e.getMessage());
//        }
//    }
//
//    public List<Assignment> queryAllAssignmentsForGroup(long groupId) {
//        try {
//            QueryBuilder<Member, Long> groupMembersQuery =
//                    mDbHelper.getDaoEx(Member.class).queryBuilder();
//            groupMembersQuery.selectColumns(Member.Columns._ID).where().eq(Member.Columns.GROUP_ID_COLUMN, groupId);
//
//            return mDbHelper.getDaoEx(Assignment.class).queryBuilder()
//                    .where().in(Assignment.Columns.GIVER_MEMBER_ID_COLUMN, groupMembersQuery)
//                    .query();
//        } catch(java.sql.SQLException e) {
//            throw new SQLException(e.getMessage());
//        }
//    }
//
//    public boolean queryHasAssignmentsForGroup(long groupId) {
//        try {
//            QueryBuilder<Member, Long> groupMembersQuery =
//                    mDbHelper.getDaoEx(Member.class).queryBuilder();
//            groupMembersQuery.selectColumns(Member.Columns._ID).where().eq(Member.Columns.GROUP_ID_COLUMN, groupId);
//
//            return mDbHelper.getDaoEx(Assignment.class).queryBuilder()
//                    .where().in(Assignment.Columns.GIVER_MEMBER_ID_COLUMN, groupMembersQuery)
//                    .queryForFirst() != null;
//        } catch(java.sql.SQLException e) {
//            throw new SQLException(e.getMessage());
//        }
//    }
//
//    public Assignment queryAssignmentForMember(long _memberId) {
//        try {
//            QueryBuilder<Assignment, Long> assignmentQuery =
//                    mDbHelper.getDaoEx(Assignment.class).queryBuilder();
//
//            assignmentQuery.where().eq(Assignment.Columns.GIVER_MEMBER_ID_COLUMN, _memberId);
//            return assignmentQuery.queryForFirst();
//        } catch(java.sql.SQLException e) {
//            throw new SQLException(e.getMessage());
//        }
//    }
//
//    public List<Member> queryAllMembersForGroup(long groupId) {
//        try {
//            return mDbHelper.getDaoEx(Member.class).queryBuilder()
//                    .where().eq(Member.Columns.GROUP_ID_COLUMN, groupId)
//                    .query();
//        } catch(java.sql.SQLException e) {
//            throw new SQLException(e.getMessage());
//        }
//    }
//
//    public List<Member> queryAllMembersForGroupExcept(long groupId, long exceptMemberId) {
//        try {
//            return mDbHelper.getDaoEx(Member.class).queryBuilder()
//                    .where().eq(Member.Columns.GROUP_ID_COLUMN, groupId)
//                    .and().ne(Member.Columns._ID, exceptMemberId)
//                    .query();
//        } catch(java.sql.SQLException e) {
//            throw new SQLException(e.getMessage());
//        }
//    }
//
//    public Member queryMemberWithNameForGroup(long groupId, String name) {
//        try {
//            // Use SelectArg to ensure values are properly escaped
//
//
//        }
//    }
}