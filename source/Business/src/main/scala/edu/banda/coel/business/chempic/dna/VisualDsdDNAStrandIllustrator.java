package edu.banda.coel.business.chempic.dna;

import com.banda.core.fsm.CharParser;
import com.banda.core.fsm.action.BufferTransitionAction;
import edu.banda.coel.CoelRuntimeException;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author © Peter Banda
 * @since 2013
 */
public class VisualDsdDNAStrandIllustrator extends DNAStrandIllustrator {

    public enum VisualDSDState {
        Initial,

        UpperStrandFirst,
        UpperStrandFirstEnd,
        LowerStrandFirst,
        LowerStrandFirstEnd,
        UpperLowerStrandFirst,
        UpperLowerStrandFirstEnd,
        LowerUpperStrandFirst,
        LowerUpperStrandFirstEnd,

        Ready,
        DoubleStrand,
        LowerStrand,
        LowerStrandEnd,
        UpperStrand,
        UpperStrandEnd,

        UpperGap,
        LowerGap,

        UpperGapAfterLowerStrand,
        LowerGapAfterLowerStrand,
        UpperGapAfterUpperStrand,
        LowerGapAfterUpperStrand,

        LowerStrandAfterUpperGap,
        LowerStrandAfterLowerGap,
        UpperStrandAfterUpperGap,
        UpperStrandAfterLowerGap,

        UpperStrandLast,
        UpperStrandLastEnd,
        LowerStrandLast,
        LowerStrandLastEnd,

        DoubleStrandExpected
    }

    private static final Character TOEHOLD_SYMBOL = '^';
    private static final Character COMPLEMENT_SYMBOL = '*';
    private static final Set<Character> ACCEPTABLE_RAW_DOMAIN_CHARS;

    static {
        ACCEPTABLE_RAW_DOMAIN_CHARS = new HashSet<Character>();
        ACCEPTABLE_RAW_DOMAIN_CHARS.addAll(CharParser.ALPHA_NUMERIC_CHARS);
        ACCEPTABLE_RAW_DOMAIN_CHARS.add(TOEHOLD_SYMBOL);
        ACCEPTABLE_RAW_DOMAIN_CHARS.add(COMPLEMENT_SYMBOL);
    }

    private static final Color TOEHOLD_COLOR = Color.RED;
    private static final Color LONG_DOMAIN_COLOR = Color.LIGHT_GRAY;

    private CharParser<VisualDSDState> parser;
    private Collection<String> rawDomainLabelsToProcess = new ArrayList<String>();
    private int maxX = 0;
    private int minY = 0;
    private int maxY = 0;

    public VisualDsdDNAStrandIllustrator(int xPos, int yPos, Graphics2D canvas) {
        super(xPos, yPos, canvas);
        createParser();
    }

    private void createParser() {
        // This function is called when a ' ' is read in the input
        BufferTransitionAction<Character> cacheRawDomainLabelAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                String label = StringUtils.join(buffer, "");
                if (!label.isEmpty())
                    rawDomainLabelsToProcess.add(label);
                buffer.clear();
            }
        };

        BufferTransitionAction<Character> singleUpperStrandAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                rawDomainLabelsToProcess.add(StringUtils.join(buffer, ""));
                buffer.clear();
                for (String rawDomainLabel : rawDomainLabelsToProcess) {
                    final DNADomainType domainType = getDNADomainType(rawDomainLabel);
                    final String domainLabel = getDomainLabel(rawDomainLabel);
                    final Color color = domainType == DNADomainType.Short ? TOEHOLD_COLOR : LONG_DOMAIN_COLOR;
                    drawDomain(color, domainType, DNADomainOrientation.Horizontal, domainLabel, true);
                    movePosition(domainType, DNADomainOrientation.Horizontal);
                }

                rawDomainLabelsToProcess.clear();
            }
        };

        BufferTransitionAction<Character> singleLowerStrandAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                rawDomainLabelsToProcess.add(StringUtils.join(buffer, ""));
                buffer.clear();
                yPos += getGapBetweenHorizonalDomains();
                for (String rawDomainLabel : rawDomainLabelsToProcess) {
                    final DNADomainType domainType = getDNADomainType(rawDomainLabel);
                    final String domainLabel = getDomainLabel(rawDomainLabel);
                    final Color color = domainType == DNADomainType.Short ? TOEHOLD_COLOR : LONG_DOMAIN_COLOR;
                    drawDomain(color, domainType, DNADomainOrientation.Horizontal, domainLabel, false);
                    movePosition(domainType, DNADomainOrientation.Horizontal);
                }
                yPos -= getGapBetweenHorizonalDomains();

                rawDomainLabelsToProcess.clear();
            }
        };

        final BufferTransitionAction<Character> rightUpperStrandAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                rawDomainLabelsToProcess.add(StringUtils.join(buffer, ""));
                buffer.clear();

                // Original x and y positions are stored before overhangs are drawn
                int xOriginalPos = xPos;
                int yOriginalPos = yPos;
                for (String rawDomainLabel : rawDomainLabelsToProcess) {
                    final DNADomainType domainType = getDNADomainType(rawDomainLabel);
                    final String domainLabel = getDomainLabel(rawDomainLabel);
                    final Color color = domainType == DNADomainType.Short ? TOEHOLD_COLOR : LONG_DOMAIN_COLOR;
                    final DNADomainOrientation orientation = DNADomainOrientation.UpRightDiagonal;

                    // Draw upper diagonal strand
                    drawDomain(color, domainType, orientation, domainLabel, true);

                    // Move the x-position and y-position over for the next domain
                    movePosition(domainType, orientation);
                }
                updateXMax();
                updateYMin();

                // After drawing overhangs, return back to our main drawing line
                xPos = xOriginalPos;
                yPos = yOriginalPos;

                rawDomainLabelsToProcess.clear();
            }
        };

        // Almost the same as above function
        // DownLeft and DownRight and placing text below instead of UpLeft/UpRight and placing text above
        final BufferTransitionAction<Character> rightLowerStrandAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                rawDomainLabelsToProcess.add(StringUtils.join(buffer, ""));
                buffer.clear();

                // Original x and y positions are stored before overhangs are drawn
                int xOriginalPos = xPos;
                int yOriginalPos = yPos;
                for (String rawDomainLabel : rawDomainLabelsToProcess) {
                    final DNADomainType domainType = getDNADomainType(rawDomainLabel);
                    final String domainLabel = getDomainLabel(rawDomainLabel);
                    final Color color = domainType == DNADomainType.Short ? TOEHOLD_COLOR : LONG_DOMAIN_COLOR;
                    final DNADomainOrientation orientation = DNADomainOrientation.DownRightDiagonal;

                    // Draw lower diagonal strand
                    yPos += getGapBetweenHorizonalDomains();
                    drawDomain(color, domainType, orientation, domainLabel, false);
                    yPos -= getGapBetweenHorizonalDomains();

                    // Move the x-position and y-position over for the next domain
                    movePosition(domainType, orientation);
                }
                updateXMax();
                updateYMax();

                // After drawing overhangs, return back to our main drawing line
                xPos = xOriginalPos;
                yPos = yOriginalPos;

                rawDomainLabelsToProcess.clear();
            }
        };

        final BufferTransitionAction<Character> firstUpperStrandAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                rawDomainLabelsToProcess.add(StringUtils.join(buffer, ""));
                buffer.clear();

                // Original x and y positions are stored before overhangs are drawn
                int xOriginalPos = xPos;
                int yOriginalPos = yPos;

                List<String> reverseDomainLabels = new ArrayList<String>(rawDomainLabelsToProcess);
                Collections.reverse(reverseDomainLabels);

                // Determine new x and y positions (without drawing)
                for (String rawDomainLabel : reverseDomainLabels) {
                    final DNADomainType domainType = getDNADomainType(rawDomainLabel);
                    final DNADomainOrientation orientation = DNADomainOrientation.UpLeftDiagonal;
                    movePosition(domainType, orientation);
                }

                xPos = xOriginalPos + (xOriginalPos - xPos);
                xOriginalPos = xPos;

                yPos = yOriginalPos;

                for (String rawDomainLabel : reverseDomainLabels) {
                    final DNADomainType domainType = getDNADomainType(rawDomainLabel);
                    final String domainLabel = getDomainLabel(rawDomainLabel);
                    final Color color = domainType == DNADomainType.Short ? TOEHOLD_COLOR : LONG_DOMAIN_COLOR;
                    final DNADomainOrientation orientation = DNADomainOrientation.UpLeftDiagonal;

                    // Draw upper diagonal strand
                    drawDomain(color, domainType, orientation, domainLabel, true);

                    // Move the x-position and y-position over for the next domain
                    movePosition(domainType, orientation);
                }
                updateYMin();

                // After drawing overhangs, return back to our main drawing line
                xPos = xOriginalPos;
                yPos = yOriginalPos;

                rawDomainLabelsToProcess.clear();
            }
        };

        final BufferTransitionAction<Character> firstLowerStrandAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                rawDomainLabelsToProcess.add(StringUtils.join(buffer, ""));
                buffer.clear();

                int xOriginalPos = xPos;
                int yOriginalPos = yPos;

                List<String> reverseDomainLabels = new ArrayList<String>(rawDomainLabelsToProcess);
                Collections.reverse(reverseDomainLabels);

                // Determine new x and y positions (without drawing)
                for (String rawDomainLabel : reverseDomainLabels) {
                    final DNADomainType domainType = getDNADomainType(rawDomainLabel);
                    final DNADomainOrientation orientation = DNADomainOrientation.DownLeftDiagonal;
                    movePosition(domainType, orientation);
                }

                xPos = xOriginalPos + (xOriginalPos - xPos);
                xOriginalPos = xPos;

                yPos = yOriginalPos;

                for (String rawDomainLabel : reverseDomainLabels) {
                    final DNADomainType domainType = getDNADomainType(rawDomainLabel);
                    final String domainLabel = getDomainLabel(rawDomainLabel);
                    final Color color = domainType == DNADomainType.Short ? TOEHOLD_COLOR : LONG_DOMAIN_COLOR;
                    final DNADomainOrientation orientation = DNADomainOrientation.DownLeftDiagonal;

                    // Draw lower diagonal strand
                    yPos += getGapBetweenHorizonalDomains();
                    drawDomain(color, domainType, orientation, domainLabel, false);
                    yPos -= getGapBetweenHorizonalDomains();

                    // Move the x-position and y-position over for the next domain
                    movePosition(domainType, orientation);
                }
                updateYMax();

                // After drawing overhangs, return back to our main drawing line
                xPos = xOriginalPos;
                yPos = yOriginalPos;

                rawDomainLabelsToProcess.clear();
            }
        };

        final BufferTransitionAction<Character> leftUpperStrandAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                rawDomainLabelsToProcess.add(StringUtils.join(buffer, ""));
                buffer.clear();

                // Original x and y positions are stored before overhangs are drawn
                int xOriginalPos = xPos;
                int yOriginalPos = yPos;

                List<String> reverseDomainLabels = new ArrayList<String>(rawDomainLabelsToProcess);
                Collections.reverse(reverseDomainLabels);

                for (String rawDomainLabel : reverseDomainLabels) {
                    final DNADomainType domainType = getDNADomainType(rawDomainLabel);
                    final String domainLabel = getDomainLabel(rawDomainLabel);
                    final Color color = domainType == DNADomainType.Short ? TOEHOLD_COLOR : LONG_DOMAIN_COLOR;
                    final DNADomainOrientation orientation = DNADomainOrientation.UpLeftDiagonal;

                    // Draw upper diagonal strand
                    drawDomain(color, domainType, orientation, domainLabel, true);

                    // Move the x-position and y-position over for the next domain
                    movePosition(domainType, orientation);
                }
                updateYMin();

                // After drawing overhangs, return back to our main drawing line
                xPos = xOriginalPos;
                yPos = yOriginalPos;

                rawDomainLabelsToProcess.clear();
            }
        };

        final BufferTransitionAction<Character> leftLowerStrandAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                rawDomainLabelsToProcess.add(StringUtils.join(buffer, ""));
                buffer.clear();

                int xOriginalPos = xPos;
                int yOriginalPos = yPos;

                List<String> reverseDomainLabels = new ArrayList<String>(rawDomainLabelsToProcess);
                Collections.reverse(reverseDomainLabels);

                for (String rawDomainLabel : reverseDomainLabels) {
                    final DNADomainType domainType = getDNADomainType(rawDomainLabel);
                    final String domainLabel = getDomainLabel(rawDomainLabel);
                    final Color color = domainType == DNADomainType.Short ? TOEHOLD_COLOR : LONG_DOMAIN_COLOR;
                    final DNADomainOrientation orientation = DNADomainOrientation.DownLeftDiagonal;

                    // Draw lower diagonal strand
                    yPos += getGapBetweenHorizonalDomains();
                    drawDomain(color, domainType, orientation, domainLabel, false);
                    yPos -= getGapBetweenHorizonalDomains();

                    // Move the x-position and y-position over for the next domain
                    movePosition(domainType, orientation);
                }
                updateYMax();

                // After drawing overhangs, return back to our main drawing line
                xPos = xOriginalPos;
                yPos = yOriginalPos;

                rawDomainLabelsToProcess.clear();
            }
        };

        BufferTransitionAction<Character> doubleStrandAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                rawDomainLabelsToProcess.add(StringUtils.join(buffer, ""));
                buffer.clear();

                for (String rawDomainLabel : rawDomainLabelsToProcess) {
                    final DNADomainType domainType = getDNADomainType(rawDomainLabel);
                    final String domainLabel = getDomainLabel(rawDomainLabel);
                    final String complementDomainLabel = getComplementDomainLabel(domainLabel);
                    final Color color = domainType == DNADomainType.Short ? TOEHOLD_COLOR : LONG_DOMAIN_COLOR;

                    // Draw top horizontal strand
                    drawDomain(color, domainType, DNADomainOrientation.Horizontal, domainLabel, true);

                    // Draw bottom horizontal strand
                    yPos += getGapBetweenHorizonalDomains();
                    drawDomain(color, domainType, DNADomainOrientation.Horizontal, complementDomainLabel, false);
                    yPos -= getGapBetweenHorizonalDomains();

                    // Move the x-position and y-position over for the next domain
                    movePosition(domainType, DNADomainOrientation.Horizontal);
                }

                rawDomainLabelsToProcess.clear();
            }
        };

        //Single Colon
        final BufferTransitionAction<Character> lowerConcatenationAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                int yOriginalPos = yPos;
                yPos += getGapBetweenHorizonalDomains();
                drawDomain(LONG_DOMAIN_COLOR, DNADomainType.Short, DNADomainOrientation.Horizontal, "", false);
                movePosition(DNADomainType.Gap, DNADomainOrientation.Horizontal);
                yPos = yOriginalPos;
            }
        };

        //Double Colon
        final BufferTransitionAction<Character> upperConcatenationAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                drawDomain(LONG_DOMAIN_COLOR, DNADomainType.Short, DNADomainOrientation.Horizontal, "", false);
                movePosition(DNADomainType.Gap, DNADomainOrientation.Horizontal);
            }
        };

        BufferTransitionAction<Character> upperConcatenationLabeledAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                rawDomainLabelsToProcess.add(StringUtils.join(buffer, ""));
                buffer.clear();

                for (String rawDomainLabel : rawDomainLabelsToProcess) {
                    final DNADomainType domainType = getDNADomainType(rawDomainLabel);
                    final String domainLabel = getDomainLabel(rawDomainLabel);
                    final Color color = domainType == DNADomainType.Short ? TOEHOLD_COLOR : LONG_DOMAIN_COLOR;
                    drawDomain(color, domainType, DNADomainOrientation.Horizontal, domainLabel, true);
                    movePosition(domainType, DNADomainOrientation.Horizontal);
                }

                rawDomainLabelsToProcess.clear();
            }
        };

        BufferTransitionAction<Character> lowerConcatenationLabeledAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                rawDomainLabelsToProcess.add(StringUtils.join(buffer, ""));
                buffer.clear();

                int yOriginalPos = yPos;
                yPos += getGapBetweenHorizonalDomains();

                for (String rawDomainLabel : rawDomainLabelsToProcess) {
                    final DNADomainType domainType = getDNADomainType(rawDomainLabel);
                    final String domainLabel = getDomainLabel(rawDomainLabel);
                    final Color color = domainType == DNADomainType.Short ? TOEHOLD_COLOR : LONG_DOMAIN_COLOR;
                    drawDomain(color, domainType, DNADomainOrientation.Horizontal, domainLabel, false);
                    movePosition(domainType, DNADomainOrientation.Horizontal);
                }

                yPos = yOriginalPos;
                rawDomainLabelsToProcess.clear();
            }
        };

        BufferTransitionAction<Character> lowerConcatenationAndUpperStrandAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                lowerConcatenationAction.run(buffer, transitionLabel);
                leftUpperStrandAction.run(buffer, transitionLabel);
            }
        };

        BufferTransitionAction<Character> upperConcatenationAndLowerStrandAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                upperConcatenationAction.run(buffer, transitionLabel);
                leftLowerStrandAction.run(buffer, transitionLabel);
            }
        };

        BufferTransitionAction<Character> upperStrandAndLowerConcatenationAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                rightUpperStrandAction.run(buffer, transitionLabel);
                lowerConcatenationAction.run(buffer, transitionLabel);
            }
        };

        BufferTransitionAction<Character> lowerStrandAndUpperConcatenationAction = new BufferTransitionAction<Character>() {

            @Override
            public void run(List<Character> buffer, Character transitionLabel) {
                rightLowerStrandAction.run(buffer, transitionLabel);
                upperConcatenationAction.run(buffer, transitionLabel);
            }
        };

        parser = new CharParser<VisualDSDState>(false);

        // Initial
        parser.addTransition(VisualDSDState.Initial, VisualDSDState.UpperStrandFirst, '<');
        parser.addTransition(VisualDSDState.Initial, VisualDSDState.LowerStrandFirst, '{');
        parser.addTransition(VisualDSDState.Initial, VisualDSDState.DoubleStrand, '[');

        // US First
        parser.addTransition(VisualDSDState.UpperStrandFirst, VisualDSDState.UpperStrandFirst, ACCEPTABLE_RAW_DOMAIN_CHARS, CharParser.ADD_CHAR_TO_BUFFER_ACTION);
        parser.addTransition(VisualDSDState.UpperStrandFirst, VisualDSDState.UpperStrandFirst, ' ', cacheRawDomainLabelAction);
        parser.addTransition(VisualDSDState.UpperStrandFirst, VisualDSDState.UpperStrandFirstEnd, '>');
        parser.addTransition(VisualDSDState.UpperStrandFirstEnd, VisualDSDState.DoubleStrand, '[', firstUpperStrandAction);
        parser.addTransition(VisualDSDState.UpperStrandFirstEnd, VisualDSDState.UpperLowerStrandFirst, '{', firstUpperStrandAction);
        parser.addTransition(VisualDSDState.UpperStrandFirstEnd, VisualDSDState.Ready, '$', singleUpperStrandAction);

        // LS First
        parser.addTransition(VisualDSDState.LowerStrandFirst, VisualDSDState.LowerStrandFirst, ACCEPTABLE_RAW_DOMAIN_CHARS, CharParser.ADD_CHAR_TO_BUFFER_ACTION);
        parser.addTransition(VisualDSDState.LowerStrandFirst, VisualDSDState.LowerStrandFirst, ' ', cacheRawDomainLabelAction);
        parser.addTransition(VisualDSDState.LowerStrandFirst, VisualDSDState.LowerStrandFirstEnd, '}');
        parser.addTransition(VisualDSDState.LowerStrandFirstEnd, VisualDSDState.DoubleStrand, '[', firstLowerStrandAction);
        parser.addTransition(VisualDSDState.LowerStrandFirstEnd, VisualDSDState.LowerUpperStrandFirst, '<', firstLowerStrandAction);
        parser.addTransition(VisualDSDState.LowerStrandFirstEnd, VisualDSDState.Ready, '$', singleLowerStrandAction);

        // US LS First
        parser.addTransition(VisualDSDState.UpperLowerStrandFirst, VisualDSDState.UpperLowerStrandFirst, ACCEPTABLE_RAW_DOMAIN_CHARS, CharParser.ADD_CHAR_TO_BUFFER_ACTION);
        parser.addTransition(VisualDSDState.UpperLowerStrandFirst, VisualDSDState.UpperLowerStrandFirst, ' ', cacheRawDomainLabelAction);
        parser.addTransition(VisualDSDState.UpperLowerStrandFirst, VisualDSDState.UpperLowerStrandFirstEnd, '}');
        parser.addTransition(VisualDSDState.UpperLowerStrandFirstEnd, VisualDSDState.DoubleStrand, '[', leftLowerStrandAction);

        // LS US First
        parser.addTransition(VisualDSDState.LowerUpperStrandFirst, VisualDSDState.LowerUpperStrandFirst, ACCEPTABLE_RAW_DOMAIN_CHARS, CharParser.ADD_CHAR_TO_BUFFER_ACTION);
        parser.addTransition(VisualDSDState.LowerUpperStrandFirst, VisualDSDState.LowerUpperStrandFirst, ' ', cacheRawDomainLabelAction);
        parser.addTransition(VisualDSDState.LowerUpperStrandFirst, VisualDSDState.LowerUpperStrandFirstEnd, '>');
        parser.addTransition(VisualDSDState.LowerUpperStrandFirstEnd, VisualDSDState.DoubleStrand, '[', leftUpperStrandAction);

        // Ready
        parser.addTransition(VisualDSDState.Ready, VisualDSDState.DoubleStrand, '[');
        parser.addTransition(VisualDSDState.Ready, VisualDSDState.UpperStrand, '<');
        parser.addTransition(VisualDSDState.Ready, VisualDSDState.LowerStrand, '{');
        parser.addTransition(VisualDSDState.Ready, VisualDSDState.Ready, '$');

        // DS
        parser.addTransition(VisualDSDState.DoubleStrand, VisualDSDState.DoubleStrand, ACCEPTABLE_RAW_DOMAIN_CHARS, CharParser.ADD_CHAR_TO_BUFFER_ACTION);
        parser.addTransition(VisualDSDState.DoubleStrand, VisualDSDState.DoubleStrand, ' ', cacheRawDomainLabelAction);
        parser.addTransition(VisualDSDState.DoubleStrand, VisualDSDState.Ready, ']', doubleStrandAction);
        parser.addTransition(VisualDSDState.DoubleStrand, VisualDSDState.Ready, '$', doubleStrandAction);
        parser.addTransition(VisualDSDState.DoubleStrandExpected, VisualDSDState.DoubleStrand, '[');

        // US
        parser.addTransition(VisualDSDState.UpperStrand, VisualDSDState.UpperStrand, ACCEPTABLE_RAW_DOMAIN_CHARS, CharParser.ADD_CHAR_TO_BUFFER_ACTION);
        parser.addTransition(VisualDSDState.UpperStrand, VisualDSDState.UpperStrand, ' ', cacheRawDomainLabelAction);
        parser.addTransition(VisualDSDState.UpperStrand, VisualDSDState.UpperStrandEnd, '>');
        parser.addTransition(VisualDSDState.UpperStrandEnd, VisualDSDState.Ready, '$', rightUpperStrandAction);

        // LS
        parser.addTransition(VisualDSDState.LowerStrand, VisualDSDState.LowerStrand, ACCEPTABLE_RAW_DOMAIN_CHARS, CharParser.ADD_CHAR_TO_BUFFER_ACTION);
        parser.addTransition(VisualDSDState.LowerStrand, VisualDSDState.LowerStrand, ' ', cacheRawDomainLabelAction);
        parser.addTransition(VisualDSDState.LowerStrand, VisualDSDState.LowerStrandEnd, '}');
        parser.addTransition(VisualDSDState.LowerStrandEnd, VisualDSDState.Ready, '$', rightLowerStrandAction);

        // US After LS Strand (allowed only as last)
        parser.addTransition(VisualDSDState.LowerStrandEnd, VisualDSDState.UpperStrandLast, '<', rightLowerStrandAction);
        parser.addTransition(VisualDSDState.UpperStrandLast, VisualDSDState.UpperStrandLast, ACCEPTABLE_RAW_DOMAIN_CHARS, CharParser.ADD_CHAR_TO_BUFFER_ACTION);
        parser.addTransition(VisualDSDState.UpperStrandLast, VisualDSDState.UpperStrandLast, ' ', cacheRawDomainLabelAction);
        parser.addTransition(VisualDSDState.UpperStrandLast, VisualDSDState.UpperStrandLastEnd, '>', rightUpperStrandAction);
        parser.addTransition(VisualDSDState.UpperStrandLastEnd, VisualDSDState.Ready, '$');

        // LS After US (allowed only as last)
        parser.addTransition(VisualDSDState.UpperStrandEnd, VisualDSDState.LowerStrandLast, '{', rightUpperStrandAction);
        parser.addTransition(VisualDSDState.LowerStrandLast, VisualDSDState.LowerStrandLast, ACCEPTABLE_RAW_DOMAIN_CHARS, CharParser.ADD_CHAR_TO_BUFFER_ACTION);
        parser.addTransition(VisualDSDState.LowerStrandLast, VisualDSDState.LowerStrandLast, ' ', cacheRawDomainLabelAction);
        parser.addTransition(VisualDSDState.LowerStrandLast, VisualDSDState.LowerStrandLastEnd, '}', rightLowerStrandAction);
        parser.addTransition(VisualDSDState.LowerStrandLastEnd, VisualDSDState.Ready, '$');

        // Colon after US
        parser.addTransition(VisualDSDState.UpperStrandEnd, VisualDSDState.UpperGapAfterUpperStrand, ':');
        parser.addTransition(VisualDSDState.UpperGapAfterUpperStrand, VisualDSDState.DoubleStrand, '[', upperStrandAndLowerConcatenationAction);
        parser.addTransition(VisualDSDState.UpperGapAfterUpperStrand, VisualDSDState.LowerGapAfterUpperStrand, ':', upperConcatenationLabeledAction);
        parser.addTransition(VisualDSDState.LowerGapAfterUpperStrand, VisualDSDState.DoubleStrand, '[');

        // Colon after LS
        parser.addTransition(VisualDSDState.LowerStrandEnd, VisualDSDState.UpperGapAfterLowerStrand, ':');
        parser.addTransition(VisualDSDState.UpperGapAfterLowerStrand, VisualDSDState.DoubleStrand, '[', lowerConcatenationLabeledAction);
        parser.addTransition(VisualDSDState.UpperGapAfterLowerStrand, VisualDSDState.LowerGapAfterLowerStrand, ':', lowerStrandAndUpperConcatenationAction);
        parser.addTransition(VisualDSDState.LowerGapAfterLowerStrand, VisualDSDState.DoubleStrand, '[');

        // Fresh colons
        parser.addTransition(VisualDSDState.Ready, VisualDSDState.UpperGap, ':');
        parser.addTransition(VisualDSDState.UpperGap, VisualDSDState.LowerGap, ':');
        parser.addTransition(VisualDSDState.UpperGap, VisualDSDState.DoubleStrand, '[', lowerConcatenationAction);
        parser.addTransition(VisualDSDState.LowerGap, VisualDSDState.DoubleStrand, '[', upperConcatenationAction);

        // LS after colon
        parser.addTransition(VisualDSDState.UpperGap, VisualDSDState.LowerStrandAfterUpperGap, '{');
        parser.addTransition(VisualDSDState.LowerStrandAfterUpperGap, VisualDSDState.LowerStrandAfterUpperGap, ACCEPTABLE_RAW_DOMAIN_CHARS, CharParser.ADD_CHAR_TO_BUFFER_ACTION);
        parser.addTransition(VisualDSDState.LowerStrandAfterUpperGap, VisualDSDState.LowerStrandAfterUpperGap, ' ', cacheRawDomainLabelAction);
        parser.addTransition(VisualDSDState.LowerStrandAfterUpperGap, VisualDSDState.DoubleStrandExpected, '}', lowerConcatenationLabeledAction);

        parser.addTransition(VisualDSDState.LowerGap, VisualDSDState.LowerStrandAfterLowerGap, '{');
        parser.addTransition(VisualDSDState.LowerStrandAfterLowerGap, VisualDSDState.LowerStrandAfterLowerGap, ACCEPTABLE_RAW_DOMAIN_CHARS, CharParser.ADD_CHAR_TO_BUFFER_ACTION);
        parser.addTransition(VisualDSDState.LowerStrandAfterLowerGap, VisualDSDState.LowerStrandAfterLowerGap, ' ', cacheRawDomainLabelAction);
        parser.addTransition(VisualDSDState.LowerStrandAfterLowerGap, VisualDSDState.DoubleStrandExpected, '}', upperConcatenationAndLowerStrandAction);

        // US after colon
        parser.addTransition(VisualDSDState.UpperGap, VisualDSDState.UpperStrandAfterUpperGap, '<');
        parser.addTransition(VisualDSDState.UpperStrandAfterUpperGap, VisualDSDState.UpperStrandAfterUpperGap, ACCEPTABLE_RAW_DOMAIN_CHARS, CharParser.ADD_CHAR_TO_BUFFER_ACTION);
        parser.addTransition(VisualDSDState.UpperStrandAfterUpperGap, VisualDSDState.UpperStrandAfterUpperGap, ' ', cacheRawDomainLabelAction);
        parser.addTransition(VisualDSDState.UpperStrandAfterUpperGap, VisualDSDState.DoubleStrandExpected, '>', lowerConcatenationAndUpperStrandAction);

        parser.addTransition(VisualDSDState.LowerGap, VisualDSDState.UpperStrandAfterLowerGap, '<');
        parser.addTransition(VisualDSDState.UpperStrandAfterLowerGap, VisualDSDState.UpperStrandAfterLowerGap, ACCEPTABLE_RAW_DOMAIN_CHARS, CharParser.ADD_CHAR_TO_BUFFER_ACTION);
        parser.addTransition(VisualDSDState.UpperStrandAfterLowerGap, VisualDSDState.UpperStrandAfterLowerGap, ' ', cacheRawDomainLabelAction);
        parser.addTransition(VisualDSDState.UpperStrandAfterLowerGap, VisualDSDState.DoubleStrandExpected, '>', upperConcatenationLabeledAction);

        // Initial and accept states
        parser.setStartState(VisualDSDState.Initial);
        parser.addAcceptState(VisualDSDState.Ready);
    }

    private String getComplementDomainLabel(String domainLabel) {
        if (isComplement(domainLabel)) {
            return StringUtils.remove(domainLabel, COMPLEMENT_SYMBOL);
        }
        return domainLabel + COMPLEMENT_SYMBOL;
    }

    private String getDomainLabel(String rawDomainLabel) {
        return StringUtils.remove(rawDomainLabel, TOEHOLD_SYMBOL);
    }

    private DNADomainType getDNADomainType(String rawDomainLabel) {
        return hasTag(rawDomainLabel, TOEHOLD_SYMBOL) ? DNADomainType.Short : DNADomainType.Long;
    }

    private boolean isComplement(String rawDomainLabel) {
        return hasTag(rawDomainLabel, COMPLEMENT_SYMBOL);
    }

    private boolean hasTag(String domainLabel, Character tag) {
        final int occurrence = StringUtils.countMatches(domainLabel, tag.toString());
        if (occurrence == 1) {
            return true;
        }
        if (occurrence > 1) {
            throw new CoelRuntimeException("DNA domain label " + domainLabel + " not valid since it contains multiple occurances of '" + tag + "'.");
        }
        return false;
    }

    private void updateXMax() {
        if (maxX < xPos) maxX = xPos;
    }

    private void updateYMin() {
        if (minY > yPos) minY = yPos;
    }

    private void updateYMax() {
        if (maxY < yPos) maxY = yPos;
    }

    public int getMaxX() {
        if (maxX < xPos)
            return xPos;
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public void resetMaxX() {
        maxX = 0;
    }

    @Override
    public void drawDnaStrand(String dnaStrand) {
        drawingEnabled = true;
        rawDomainLabelsToProcess.clear();
        parser.parse(dnaStrand + '$');
    }

    @Override
    public void processDnaStrand(String dnaStrand) {
        drawingEnabled = false;
        rawDomainLabelsToProcess.clear();
        parser.parse(dnaStrand + '$');
    }
}