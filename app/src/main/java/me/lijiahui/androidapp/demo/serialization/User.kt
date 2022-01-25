package me.lijiahui.androidapp.demo.serialization

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "first_name")
    @SerializedName("first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    @SerializedName("last_name")
    val lastName: String,
    @ColumnInfo(name = "avatar")
    val avatar: String,
)
