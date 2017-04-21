package edu.banda.coel.domain

class TwoDimSymConfSpec {
    Integer size
    Integer alphabetSize
    Boolean binaryAlphabetSize

    static constraints = {
        size nullable: false
        alphabetSize nullable: false
    }
}