package edu.banda.coel.domain

class TwoDimSymCARunSpec {
    Integer latticeSize
    Integer symmetryShiftX
    Integer symmetryShiftY
    Integer runTime
//    Integer neighborhoodRadius

    static constraints = {
        latticeSize nullable: false, range: 2..100
        symmetryShiftX nullable: false, range: 0..100
        symmetryShiftY nullable: false, range: 0..100
        runTime nullable: false, range: 1..500
//        neighborhoodRadius nullable: false, range: 1..3
    }
}