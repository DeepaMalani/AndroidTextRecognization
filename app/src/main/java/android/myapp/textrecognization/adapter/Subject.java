package android.myapp.textrecognization.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class Subject implements Parcelable {

    private String name;
    private String grade;

    // Constructor
    public Subject(String name, String grade){

        this.name = name;
        this.grade = grade;
    }

    // Read from parcel
    public Subject(Parcel in){
        this.name = in.readString();
        this.grade =  in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    //Write to the parcel
    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeString(name);
        parcel.writeString(grade);
    }

}
