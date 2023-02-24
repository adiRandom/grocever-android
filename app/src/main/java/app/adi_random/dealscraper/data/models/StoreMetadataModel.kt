package app.adi_random.dealscraper.data.models

import androidx.room.ColumnInfo

data class StoreMetadataModel(
    @ColumnInfo(name = "store_id") val id: Int,
    @ColumnInfo(name = "store_name") val name: String,
    @ColumnInfo(name = "store_url") val url: String
) {}
