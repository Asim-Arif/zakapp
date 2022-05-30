package com.example.asim.rest_man;

import android.os.Parcel;
import android.os.Parcelable;

public class DealsData implements Parcelable {
    int iDealID;
    int iDealNo;
    int iPizzaCount;
    int iSaladCount;
    int iPizzas[];
    int iPizzas_Qty[];  //This Array is created as Can't figure out how to handle Multi-Dimensional Arrays with Parceable.
    int iSalads[];


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.iDealID);
        dest.writeInt(this.iDealNo);
        dest.writeInt(this.iPizzaCount);
        dest.writeInt(this.iSaladCount);
        dest.writeIntArray(this.iPizzas);
        dest.writeIntArray(this.iPizzas_Qty);
        dest.writeIntArray(this.iSalads);
    }

    public DealsData() {
    }

    protected DealsData(Parcel in) {
        this.iDealID = in.readInt();
        this.iDealNo = in.readInt();
        this.iPizzaCount = in.readInt();
        this.iSaladCount = in.readInt();
        this.iPizzas = in.createIntArray();
        this.iPizzas_Qty = in.createIntArray();
        this.iSalads = in.createIntArray();
    }

    public static final Creator<DealsData> CREATOR = new Creator<DealsData>() {
        @Override
        public DealsData createFromParcel(Parcel source) {
            return new DealsData(source);
        }

        @Override
        public DealsData[] newArray(int size) {
            return new DealsData[size];
        }
    };
}
