package edu.banda.coel.domain

class TwoDimSymConfSpec {
    Integer latticeSize
    Integer alphabetSize

    static constraints = {
        latticeSize nullable: false, range: 1..1000
        alphabetSize nullable: false, range: 2..10
    }
}