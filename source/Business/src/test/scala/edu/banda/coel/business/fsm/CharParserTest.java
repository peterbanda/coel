package edu.banda.coel.business.fsm;

import java.util.List;

import junit.framework.TestCase;

import com.banda.core.fsm.CharParser;
import com.banda.core.fsm.FiniteStateMachineException;
import com.banda.core.fsm.action.BufferTransitionAction;
import org.junit.Test;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class CharParserTest extends TestCase {

	public enum State {
		None, UpperStrand, LowerStrand, DoubleStrand
	}

	@Test
	public void testParse() {
		CharParser<State> parser = new CharParser<State>(false);
		parser.addTransition(State.None, State.DoubleStrand, '[');
		parser.addTransition(State.None, State.UpperStrand, '<');
		parser.addTransition(State.None, State.LowerStrand, '{');

		parser.addTransition(State.DoubleStrand, State.DoubleStrand, CharParser.LETTERS, CharParser.ADD_CHAR_TO_BUFFER_ACTION);
		parser.addTransition(State.UpperStrand, State.UpperStrand, CharParser.ALPHA_NUMERIC_CHARS, CharParser.ADD_CHAR_TO_BUFFER_ACTION);
		parser.addTransition(State.LowerStrand, State.LowerStrand, CharParser.LOWER_CASE_LETTERS, CharParser.ADD_CHAR_TO_BUFFER_ACTION);

		parser.addTransition(State.DoubleStrand, State.None, ']', new BufferTransitionAction<Character>() {

			@Override
			public void run(List<Character> buffer, Character transitionLabel) {
				System.out.println("Double strand is: " + buffer.toString());
				buffer.clear();
			}
		});
		parser.addTransition(State.UpperStrand, State.None, '>', new BufferTransitionAction<Character>() {

			@Override
			public void run(List<Character> buffer, Character transitionLabel) {
				System.out.println("Upper strand is: " + buffer.toString());
				buffer.clear();
			}
		});
		parser.addTransition(State.LowerStrand, State.None, '}', new BufferTransitionAction<Character>() {

			@Override
			public void run(List<Character> buffer, Character transitionLabel) {
				System.out.println("Lower strand is: " + buffer.toString());
				buffer.clear();
			}
		});
		parser.addAcceptState(State.None);
		parser.setStartState(State.None);
		try {
			parser.parse("[aCaa]<a1>{bca}");
		} catch (FiniteStateMachineException exception) {
			System.out.println("Parsing failed due to: " + exception.getMessage());
		}
	}
}