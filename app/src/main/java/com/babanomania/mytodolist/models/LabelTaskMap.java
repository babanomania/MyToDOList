package com.babanomania.mytodolist.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by Shouvik on 31/08/2017.
 */

@Entity(nameInDb = "T_TD_LBL_TSK_MAP" )
public class LabelTaskMap {

    @Id(autoincrement = true)
    private Long _id;

    @NotNull
    private Long labelId;

    @NotNull
    private Long taskId;

    @Generated(hash = 1304675353)
    public LabelTaskMap(Long _id, @NotNull Long labelId, @NotNull Long taskId) {
        this._id = _id;
        this.labelId = labelId;
        this.taskId = taskId;
    }

    @Generated(hash = 1989580141)
    public LabelTaskMap() {
    }

    @Override
    public String toString() {
        return this._id.toString();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getLabelId() {
        return this.labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

}
