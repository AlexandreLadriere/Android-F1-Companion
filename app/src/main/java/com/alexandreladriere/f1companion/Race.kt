package com.alexandreladriere.f1companion

data class Race(
    val season: String,
    val round: String,
    val url: String,
    val raceName: String,
    val circuit: Circuit, // impact majuscule?
    val date: String,
    val time: String,
    val firstPractice: Session,
    val secondPractice: Session,
    val thirdPractice: Session = Session(),
    val qualifying: Session = Session()
)
