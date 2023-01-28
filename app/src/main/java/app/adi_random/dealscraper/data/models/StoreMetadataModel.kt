package app.adi_random.dealscraper.data.models

import androidx.room.ColumnInfo

data class StoreMetadataModel(val id: Int, @ColumnInfo(name="store_name") val name: String, val url: String)
