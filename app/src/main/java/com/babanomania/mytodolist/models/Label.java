package com.babanomania.mytodolist.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by Shouvik on 31/08/2017.
 */

@Entity(nameInDb = "T_TD_LABEL")
public class Label {

    @Id(autoincrement = true)
    private Long _id;

    @NotNull
    private String label;

    public Label( String label ){
        this.label = label;
    }

    @Generated(hash = 667857581)
    public Label(Long _id, @NotNull String label) {
        this._id = _id;
        this.label = label;
    }

    @Generated(hash = 2137109701)
    public Label() {
    }

    @Override
    public String toString() {
        return this.label;
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

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


}
