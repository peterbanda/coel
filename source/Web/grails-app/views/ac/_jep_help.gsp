		<gui:modal id="jep-help-modal" title="COEL Expression Language Help" class="modal-large">
      		<p>
                This page briefly describes all operators and functions supported by COEL Expression Language. This language is used application-wide to give users
                the freedom to manipulate and customize the chemical system and its interpretation in a flexible, dynamic, and safe way.
                It's supported for objects such as interaction series, translation series, reaction rates, performance measures, and genetic algorithms fitness functions,
            </p>
			<p>
                COEL Expression Language is based on Java Expression Parser (JEP), version 2.24, which is the last free version before JEP became a commercial product.
                We recommend first to familiarize with <a href="http://www.cse.msu.edu/SENS/Software/jep-2.23/doc/website/doc/doc_op_and_func.htm">JEP's syntax</a>
                since by the definition all JEP's operators and functions can be used in COEL.
            </p>
            <div class="alert alert-info" role="alert">
                <h4>Important</h4>
                <p>
                    There are two data types supported in COEL: double and n-dimensional array of doubles.
                </p>
                <p>
                    Booleans are represented by doubles 1 (true) and 0 (false).
                </p>
            </div>
			<p>JEP arithmetic operators:</p>
            <table style="padding-left: 100px">
                <tbody>
                    <g:render template="/ac/displayJepItem" model="['name':'Addition','code':'+']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Subtraction','code':'-']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Multiplication','code':'*']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Division','code':'/']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Modulus','code':'%']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Power','code':'^']" />
                </tbody>
            </table>
            </br>
            <p>JEP comparison operators:</p>
            <table style="padding-left: 100px">
                <tbody>
                    <g:render template="/ac/displayJepItem" model="['name':'Equal','code':'==']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Not Equal','code':'!=']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Less or Equal','code':'<=']" />
                    <g:render template="/ac/displayJepItem" model="['name':'More or Equal','code':'&gt;=']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Less Than','code':'<']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Greater Than','code':'&gt;']" />
                </tbody>
            </table>
            </br>
            <p>JEP Boolean operators:</p>
            <table style="padding-left: 100px">
                <tbody>
                    <g:render template="/ac/displayJepItem" model="['name':'Not','code':'!']" />
                    <g:render template="/ac/displayJepItem" model="['name':'And','code':'&&']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Or','code':'||']" />
                </tbody>
            </table>
            </br>
            <p>JEP functions:<p>
            <table style="padding-left: 100px">
                <tbody>
                    <g:render template="/ac/displayJepItem" model="['name':'Natural Logarithm','code':'ln()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Logarithm base 10','code':'log()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Angle','code':'angle()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Absolute Value / Magnitude','code':'abs()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Random number (between 0 and 1)','code':'rand()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Modulus','code':'mod()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Square Root','code':'sqrt()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Sine','code':'sin()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Cosine','code':'cos()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Tangent','code':'tan()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Arc Sine','code':'asin()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Arc Cosine','code':'acos()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Arc Tangent','code':'atan()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Hyperbolic Sine','code':'sinh()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Hyperbolic Cosine','code':'cosh()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Hyperbolic Tangent','code':'tanh()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Inverse Hyperbolic Sine','code':'asinh()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Inverse Hyperbolic Cosine','code':'acosh()']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Inverse Hyperbolic Tangent','code':'atanh()']" />
                </tbody>
            </table>
            </br>
            <p>COEL's extra functions:<p>
            <table style="padding-left: 100px">
                <tbody>
                    <g:render template="/ac/displayJepItem" model="['name':'If then','code':'if(cond, expr_1, expr_2)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Random number','code':'random(from, to)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Random number using normal distribution','code':'randomN(mean, std)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Create array','code':'array(size_1, ..., size_n)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Get element from array','code':'get(array, index_1, ..., index_n)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Set element in array','code':'set(array, index_1, ..., index_n, value)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Sum','code':'sum(array) or sum(value_1, ..., value_n)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Average','code':'avg(array) or avg(value_1, ..., value_n)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Min','code':'min(array) or min(value_1, ..., value_n)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Max','code':'max(array) or max(value_1, ..., value_n)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Count','code':'count(element, array) or count(element, value_1, ..., value_n)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'First element','code':'first(array) or first(value_1, ..., value_n)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Last element','code':'last(array) or last(value_1, ..., value_n)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Last positive element','code':'lastPositive(array) or lastPositive(value_1,..., value_n)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Middle element','code':'middle(array) or middle(value_1, ..., value_n)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Array length','code':'length(array)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Array length (returns array of sizes)','code':'lengths(array)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Shift elements by an offset','code':'shift(array, offset)']" />
                    <g:render template="/ac/displayJepItem" model="['name':'Equals','code':'equals(array_1, array_2) or equals(value_1, value_2)']" />
                </tbody>
            </table>
        </gui:modal>