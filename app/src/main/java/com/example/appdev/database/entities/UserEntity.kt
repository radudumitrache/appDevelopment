package com.example.appdev.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
@Entity(tableName = "User")
data class UserEntity(
        @PrimaryKey(autoGenerate = true) val user_id: Int = 0,
        val email: String,
        val password: String,
        val profession: String,
        val age: String,
        val monthly_salary: Float,
        val preffered_currency: String
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readFloat(),
                parcel.readString() ?: ""
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(user_id)
                parcel.writeString(email)
                parcel.writeString(password)
                parcel.writeString(profession)
                parcel.writeString(age)
                parcel.writeFloat(monthly_salary)
                parcel.writeString(preffered_currency)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<UserEntity> {
                override fun createFromParcel(parcel: Parcel): UserEntity {
                        return UserEntity(parcel)
                }

                override fun newArray(size: Int): Array<UserEntity?> {
                        return arrayOfNulls(size)
                }
        }
}