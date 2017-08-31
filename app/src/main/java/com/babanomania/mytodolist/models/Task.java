package com.babanomania.mytodolist.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by Shouvik on 23/08/2017.
 */

@Entity( nameInDb = "T_TD_TASK")
public class Task {

    @Id(autoincrement = true)
    private Long _id;

    @NotNull
    private String title;

    private Date date;

    @ToMany
    @JoinEntity(
            entity = LabelTaskMap.class,
            sourceProperty = "taskId",
            targetProperty = "labelId"
    )
    private List<Label> labelsWithThisTask;

    @Transient
    private List<Label> cachedLabels;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1469429066)
    private transient TaskDao myDao;


    public Task(String title, Date date) {
        this.title = title;
        this.date = date;
    }

    @Generated(hash = 13131675)
    public Task(Long _id, @NotNull String title, Date date) {
        this._id = _id;
        this.title = title;
        this.date = date;
    }

    @Generated(hash = 733837707)
    public Task() {
    }

    @Override
    public String toString() {
        return this.title + " - " + this.date;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }

    public List<Label> getCachedLabels() {

        synchronized (this) {
            if (this.cachedLabels == null) {
                this.cachedLabels = getLabelsWithThisTask();
            }
        }

        return cachedLabels;
    }

    public void setCachedLabels(List<Label> cachedLabels) {

        synchronized (this) {
            if (this.cachedLabels == null) {
                this.cachedLabels = getLabelsWithThisTask();
            }
        }

        this.cachedLabels = cachedLabels;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 407167342)
    public List<Label> getLabelsWithThisTask() {
        if (labelsWithThisTask == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LabelDao targetDao = daoSession.getLabelDao();
            List<Label> labelsWithThisTaskNew = targetDao
                    ._queryTask_LabelsWithThisTask(_id);
            synchronized (this) {
                if (labelsWithThisTask == null) {
                    labelsWithThisTask = labelsWithThisTaskNew;
                }
            }
        }
        return labelsWithThisTask;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1107123860)
    public synchronized void resetLabelsWithThisTask() {
        labelsWithThisTask = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1442741304)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskDao() : null;
    }

}
