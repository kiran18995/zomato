
package io.mountblue.zomato.module.gooutmodule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Collection {

    @SerializedName("collection")
    @Expose
    private Collection_ collection;

    public Collection_ getCollection() {
        return collection;
    }

    public void setCollection(Collection_ collection) {
        this.collection = collection;
    }

}
