package com.infosys.activities;

import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.infosys.R;

/**
 * Created by Administrator on 07-Sep-18.
 */

public class Feed implements Parcelable {
    private String title;
    private String description;
    private String imageHref;

    protected Feed(Parcel in) {
        title = in.readString();
        description = in.readString();
        imageHref = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(imageHref);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageHref() {
        return imageHref;
    }

    public void setImageHref(String imageHref) {
        this.imageHref = imageHref;
    }

    @BindingAdapter({"image"})
    public static void loadImage(ImageView imgTiffin, String image) {
        imgTiffin.setImageResource(R.drawable.ic_launcher_foreground);
        if (image == null || image.isEmpty()) {
            imgTiffin.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            imgTiffin.setTag(R.integer.object, image);
            Glide.with(imgTiffin.getContext()).load(image).into(imgTiffin);
        }
    }
}
