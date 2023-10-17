package com.comunidadedevspace.taskbeats.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) //A chave será unica.Se eu tiver uma task com o mesmo nome "id" de variavel com a mesma id uma delas vair ser descartada
    val id: Int = 0,
    val title: String,
    val description: String
    ) : java.io.Serializable
