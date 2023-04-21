package app.adi_random.dealscraper.usecase

object MapUnitTypeUseCase {
    fun map(unit:String) = when(unit){
        "Unit" -> "BUC"
        else -> unit
    }
}