package edu.banda.coel.web

import com.banda.chemistry.domain.AcInteractionSeries
import com.banda.chemistry.domain.AcInteractionVariableAssignment
import com.banda.chemistry.domain.AcSpeciesSet
import com.banda.function.domain.TransitionTable
import com.banda.network.domain.NetworkFunction

/**
 * Created by peter on 6/9/15.
 */
class NetworkCommonService {

    def getTransitionTableInfo(NetworkFunction networkFunction, boolean lsbFirst) {
        def table = networkFunction.function

        def binaryOutput = table.outputs.collect{ output -> if (output) 1 else 0}.join("")
        if (!lsbFirst)
            binaryOutput = binaryOutput.reverse()

        def decimalOutput = new BigInteger(binaryOutput, 2).toString()
        def hexadecimalOutput = new BigInteger(binaryOutput, 2).toString(16)

        def text = "Id: " + networkFunction.id + "\n"
        text <<= "Name: " + networkFunction.name + "\n\n"
        text <<= "Transition Table\n"
        text <<= "-----------------------------------------------------------------------\n"
        text <<= " order:       " + ((lsbFirst) ? "lsb first" : "msb first") + "\n"
        text <<= " binary:      " + binaryOutput  + "\n"
        text <<= " decimal:     " + decimalOutput  + "\n"
        text <<= " hexadecimal: " + hexadecimalOutput  + "\n"
        text <<= "-----------------------------------------------------------------------\n"
        text
    }
}
