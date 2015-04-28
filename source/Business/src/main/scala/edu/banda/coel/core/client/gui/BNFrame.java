package edu.banda.coel.core.client.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;

import com.banda.core.util.ConversionUtil;
import com.banda.core.util.FileUtil;
import com.banda.function.business.FunctionEvaluatorFactoryImpl;
import com.banda.function.enumerator.ListEnumeratorFactoryImpl;
import com.banda.function.evaluator.FunctionEvaluatorFactory;

import edu.banda.coel.business.BooleanNetworkBO;
import edu.banda.coel.business.SelfOrganizedMapBO;
import edu.banda.coel.core.client.rbn.InputOutputHandler;
import edu.banda.coel.core.domain.BooleanNetworkFactory;
import edu.banda.coel.core.domain.som.SelfOrganizedMapFactory;
import edu.banda.coel.core.util.ImageUtils;
import edu.banda.coel.domain.bn.BooleanNetwork;
import edu.banda.coel.domain.net.Node;
import edu.banda.coel.domain.som.SelfOrganizedMap;
import edu.banda.coel.domain.som.SomNode;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class BNFrame extends JFrame {

	private static final int DEFAULT_CONNECTIONS_PER_NODE = 1;
	private static final int DEFAULT_NUMBER_OF_NODES = 36;
	private static final int DEFAULT_SPEED = 100;
	private static final String START_BN_TEXT = "Start";
	private static final String STOP_BN_TEXT = "Stop";

	private JPanel mainPanel;
	private BN2dPanel bn2dPanel;
	private BN2dStateChangePanel bn2dStateChangePanel;
	private Som2dPanel som2dPanel;
	private GraphPanel graphPanel;
	private JPanel bnInfoPanel;
	private JPanel actionPanel;
	private JPanel exportPanel;

	private JLabel iterationLabel;
	private JLabel iterationValueLabel;
	private JLabel totalNeighDistLabel;
	private JLabel totalNeighDistValueLabel;
	private JLabel radiusLabel;
	private JLabel radiusValueLabel;
	private JLabel learningRateLabel;
	private JLabel learningRateValueLabel;
	private JLabel numberOfNodesLabel;
	private JTextField numberOfNodesTextField;		
	private JLabel connectionsPerNodeLabel;
	private JTextField connectionsPerNodeTextField;	
	private JLabel speedLabel;
	private JTextField speedTextField;
	private JLabel zoomLabel;
	private JTextField zoomTextField;	

	private JButton generateRandomBNButton;
	private JButton generateRandomAndOrBNButton;
	private JButton generate1dCycleBNButton;
	private JButton introduce2dSomTopologyButton;
	private JButton setRandomConfigButton;
	private JButton setSingleActiveNodeConfigButton;
	private JButton setAll0sConfigButton;
	private JButton setAll1sConfigButton;
	private JButton startPauseRandomButton;
	private JButton exportButton;
	private JCheckBox exportBn2DPicsCheckBox;
	private JCheckBox exportBn2DStateChangePicsCheckBox;
	private JCheckBox exportNetworkConnectionsPicsCheckBox;
	private Timer bnTimer;
	private Timer somTimer;
	private ActionListener bnTimerActionListener;
	
	private int iteration = 0;

	
    private volatile BooleanNetworkBO bnBO;
    private volatile SelfOrganizedMapBO<Double> somBO;
    private final FunctionEvaluatorFactory functionEvaluatorFactory = new FunctionEvaluatorFactoryImpl(
    		new ListEnumeratorFactoryImpl());

	public BNFrame(BooleanNetworkBO bnBO) {
		this();
		if (bnBO != null) {
			setBnBO(bnBO);
		}
	}

	public BNFrame() {
		setName("BNFrame");
	    setTitle("RBN Playground");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setPreferredSize(new Dimension(950, 650));
	    setMinimumSize(new Dimension(950, 650));
	    setVisible(true);
	    add(getMainPanel());
	    pack();
	}

	private void setBnBO(BooleanNetworkBO bnBO) {
	    this.bnBO = bnBO;
		getBN2dPanel().setBooleanNetwork(bnBO.getBn());
		getBN2dStateChangePanel().setBooleanNetwork(bnBO.getBn());
		getGraphPanel().setGraph(bnBO.getBn());
		repaint();
		getNumberOfNodesTextField().setText("" + bnBO.getNumberOfNodes());
		Integer c = bnBO.getBn().getConnectivityPerNode();
		getConnectionsPerNodeTextField().setText(c != null ? c.toString() : "");
		updateTotalNeighDistValueText();
	}

	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			GridBagConstraints infoPanelCons = new GridBagConstraints();
			GridBagConstraints bn2dPanelCons = new GridBagConstraints();
			GridBagConstraints bn2dStateChangePanelCons = new GridBagConstraints();
			GridBagConstraints som2dPanelCons = new GridBagConstraints();
			GridBagConstraints graphPanelCons = new GridBagConstraints();
			GridBagConstraints fillerCons = new GridBagConstraints();
			GridBagConstraints filler2Cons = new GridBagConstraints();
			GridBagConstraints actionPanelCons = new GridBagConstraints();

			infoPanelCons.gridx = 0;
			infoPanelCons.gridy = 0;
			infoPanelCons.gridwidth = 4;
			infoPanelCons.weightx = 1;
			infoPanelCons.anchor = GridBagConstraints.NORTH;
			infoPanelCons.fill = GridBagConstraints.HORIZONTAL;

			bn2dPanelCons.gridx = 0;
			bn2dPanelCons.gridy = 1;
//			bn2dPanelCons.weightx = 1;
//			bn2dPanelCons.weighty = 1;
			bn2dPanelCons.insets = new Insets(10, 20, 10, 10);
			bn2dPanelCons.anchor = GridBagConstraints.NORTHWEST;
//			bn2dPanelCons.fill = GridBagConstraints.BOTH;

			bn2dStateChangePanelCons.gridx = 1;
			bn2dStateChangePanelCons.gridy = 1;
			bn2dStateChangePanelCons.insets = new Insets(10, 80, 10, 10);
			bn2dStateChangePanelCons.anchor = GridBagConstraints.NORTHWEST;

			graphPanelCons.gridx = 0;
			graphPanelCons.gridy = 2;
			graphPanelCons.insets = new Insets(10, 20, 10, 10);
			graphPanelCons.anchor = GridBagConstraints.NORTHWEST;

			som2dPanelCons.gridx = 1;
			som2dPanelCons.gridy = 2;
//			som2dPanelCons.weightx = 1;
//			som2dPanelCons.weighty = 1;
//			som2dPanelCons.gridwidth = 2;
			som2dPanelCons.insets = new Insets(10, 80, 10, 10);
			som2dPanelCons.anchor = GridBagConstraints.NORTHWEST;
//			som2dPanelCons.fill = GridBagConstraints.BOTH;

			fillerCons.gridx = 2;
			fillerCons.gridy = 1;
			fillerCons.weightx = 1;
			fillerCons.weighty = 1;
			fillerCons.gridheight = 2;
			fillerCons.fill = GridBagConstraints.BOTH;
		
			filler2Cons.gridx = 0;
			filler2Cons.gridy = 3;
			filler2Cons.weightx = 1;
			filler2Cons.weighty = 1;
			filler2Cons.gridwidth = 4;
			filler2Cons.fill = GridBagConstraints.BOTH;

			actionPanelCons.gridx = 3;
			actionPanelCons.gridy = 1;
			actionPanelCons.weighty = 1;
			actionPanelCons.gridheight = 2;			
			actionPanelCons.anchor = GridBagConstraints.NORTH;
			actionPanelCons.insets = new Insets(10, 10, 10, 10);
			actionPanelCons.fill = GridBagConstraints.VERTICAL;
			

			mainPanel.add(getBnInfoPanel(), infoPanelCons);
			mainPanel.add(getBN2dPanel(), bn2dPanelCons);
			mainPanel.add(getBN2dStateChangePanel(), bn2dStateChangePanelCons);
			mainPanel.add(getSom2dPanel(), som2dPanelCons);
			mainPanel.add(new JLabel(), fillerCons);
			mainPanel.add(getGraphPanel(), graphPanelCons);
			mainPanel.add(getActionPanel(), actionPanelCons);
			mainPanel.add(new JLabel(), filler2Cons);
		}
		return mainPanel;
	}

	private BN2dPanel getBN2dPanel() {
		if (bn2dPanel == null) {
			bn2dPanel = new BN2dPanel();
//			bn2dPanel.setPreferredSize(new Dimension(400, 400));
		}
		return bn2dPanel;
	}

	private BN2dStateChangePanel getBN2dStateChangePanel() {
		if (bn2dStateChangePanel == null) {
			bn2dStateChangePanel = new BN2dStateChangePanel();
//			bn2dStateChangePanel.setPreferredSize(new Dimension(400, 400));
		}
		return bn2dStateChangePanel;
	}

	private Som2dPanel getSom2dPanel() {
		if (som2dPanel == null) {
			som2dPanel = new Som2dPanel();
//			som2dPanel.setPreferredSize(new Dimension(400, 400));
		}
		return som2dPanel;
	}

	private GraphPanel getGraphPanel() {
		if (graphPanel == null) {
			graphPanel = new GraphPanel();
//			graphPanel.setPreferredSize(new Dimension(400, 400));
		}
		return graphPanel;
	}

	private JPanel getActionPanel() {
		if (actionPanel == null) {
			actionPanel = new JPanel();
			actionPanel.setBorder(BorderFactory.createEtchedBorder());
			actionPanel.setLayout(new GridBagLayout());
			GridBagConstraints generateRandomBNButtonCons = new GridBagConstraints();
			GridBagConstraints generateRandomAndOrBNButtonCons = new GridBagConstraints();
			GridBagConstraints generate1dCycleBNButtonCons = new GridBagConstraints();
			GridBagConstraints setRandomConfigButtonCons = new GridBagConstraints();
			GridBagConstraints setSingleActiveNodeConfigButtonCons = new GridBagConstraints();
			GridBagConstraints setAll0sConfigButtonCons = new GridBagConstraints();
			GridBagConstraints setAll1sConfigButtonCons = new GridBagConstraints();
			GridBagConstraints startPauseRandomButtonCons = new GridBagConstraints();
			GridBagConstraints introduce2dSomTopologyButtonCons = new GridBagConstraints();
			GridBagConstraints exportPanelCons = new GridBagConstraints();

			generateRandomBNButtonCons.gridx = 0;
			generateRandomBNButtonCons.gridy = 0;
			generateRandomBNButtonCons.anchor = GridBagConstraints.NORTH;
			generateRandomBNButtonCons.insets = new Insets(10, 2, 0, 2);

			generateRandomAndOrBNButtonCons.gridx = 0;
			generateRandomAndOrBNButtonCons.gridy = 1;
			generateRandomAndOrBNButtonCons.anchor = GridBagConstraints.NORTH;
			generateRandomAndOrBNButtonCons.insets = new Insets(10, 2, 0, 2);
			
			generate1dCycleBNButtonCons.gridx = 0;
			generate1dCycleBNButtonCons.gridy = 2;
			generate1dCycleBNButtonCons.anchor = GridBagConstraints.NORTH;
			generate1dCycleBNButtonCons.insets = new Insets(10, 2, 0, 2);

			setRandomConfigButtonCons.gridx = 0;
			setRandomConfigButtonCons.gridy = 3;
			setRandomConfigButtonCons.anchor = GridBagConstraints.NORTH;
			setRandomConfigButtonCons.insets = new Insets(50, 2, 0, 2);

			setSingleActiveNodeConfigButtonCons.gridx = 0;
			setSingleActiveNodeConfigButtonCons.gridy = 4;
			setSingleActiveNodeConfigButtonCons.anchor = GridBagConstraints.NORTH;
			setSingleActiveNodeConfigButtonCons.insets = new Insets(10, 2, 0, 2);

			setAll0sConfigButtonCons.gridx = 0;
			setAll0sConfigButtonCons.gridy = 5;
			setAll0sConfigButtonCons.anchor = GridBagConstraints.NORTH;
			setAll0sConfigButtonCons.insets = new Insets(10, 2, 0, 2);

			setAll1sConfigButtonCons.gridx = 0;
			setAll1sConfigButtonCons.gridy = 6;
			setAll1sConfigButtonCons.anchor = GridBagConstraints.NORTH;
			setAll1sConfigButtonCons.insets = new Insets(10, 2, 0, 2);

			startPauseRandomButtonCons.gridx = 0;
			startPauseRandomButtonCons.gridy = 7;
			startPauseRandomButtonCons.anchor = GridBagConstraints.NORTH;
			startPauseRandomButtonCons.insets = new Insets(10, 2, 0, 2);
			
			introduce2dSomTopologyButtonCons.gridx = 0;
			introduce2dSomTopologyButtonCons.gridy = 8;
			introduce2dSomTopologyButtonCons.anchor = GridBagConstraints.NORTH;
			introduce2dSomTopologyButtonCons.insets = new Insets(30, 2, 0, 2);

			exportPanelCons.gridx = 0;
			exportPanelCons.gridy = 9;
			exportPanelCons.anchor = GridBagConstraints.NORTH;
			exportPanelCons.insets = new Insets(20, 2, 0, 2);

			actionPanel.add(getGenerateRandomBNButton(), generateRandomBNButtonCons);
			actionPanel.add(getGenerateRandomAndOrBNButton(), generateRandomAndOrBNButtonCons);
			actionPanel.add(getGenerate1dCycleBNButton(), generate1dCycleBNButtonCons);
			actionPanel.add(getSetRandomConfigButton(), setRandomConfigButtonCons);
			actionPanel.add(getSetSingleActiveNodeConfigButton(), setSingleActiveNodeConfigButtonCons);
			actionPanel.add(getSetAll0sConfigButton(), setAll0sConfigButtonCons);
			actionPanel.add(getSetAll1sConfigButton(), setAll1sConfigButtonCons);			
			actionPanel.add(getStartPauseRandomButton(), startPauseRandomButtonCons);
			actionPanel.add(getIntroduce2dSomTopologyButton(), introduce2dSomTopologyButtonCons);
			actionPanel.add(getExportPanel(), exportPanelCons);
    	}
		return actionPanel;
	}

	private JPanel getExportPanel() {
		if (exportPanel == null) {
			exportPanel = new JPanel();
			exportPanel.setBorder(BorderFactory.createTitledBorder("Export"));
			exportPanel.setLayout(new GridBagLayout());
			GridBagConstraints exportButtonCons = new GridBagConstraints();
			GridBagConstraints exportBn2DPicsPicsCheckBoxCons = new GridBagConstraints();
			GridBagConstraints exportBn2DStateChangePicsCheckBoxCons = new GridBagConstraints();
			GridBagConstraints exportNetworkConnectionsPicsCheckBoxCons = new GridBagConstraints();

			exportButtonCons.gridx = 0;
			exportButtonCons.gridy = 0;
			exportButtonCons.anchor = GridBagConstraints.NORTH;
			exportButtonCons.insets = new Insets(2, 2, 2, 2);

			exportBn2DPicsPicsCheckBoxCons.gridx = 0;
			exportBn2DPicsPicsCheckBoxCons.gridy = 1;
			exportBn2DPicsPicsCheckBoxCons.anchor = GridBagConstraints.NORTHWEST;
			exportBn2DPicsPicsCheckBoxCons.insets = new Insets(2, 2, 2, 2);

			exportBn2DStateChangePicsCheckBoxCons.gridx = 0;
			exportBn2DStateChangePicsCheckBoxCons.gridy = 2;
			exportBn2DStateChangePicsCheckBoxCons.anchor = GridBagConstraints.NORTHWEST;
			exportBn2DStateChangePicsCheckBoxCons.insets = new Insets(2, 2, 2, 2);

			exportNetworkConnectionsPicsCheckBoxCons.gridx = 0;
			exportNetworkConnectionsPicsCheckBoxCons.gridy = 3;
			exportNetworkConnectionsPicsCheckBoxCons.anchor = GridBagConstraints.NORTHWEST;
			exportNetworkConnectionsPicsCheckBoxCons.insets = new Insets(2, 2, 2, 2);

			exportPanel.add(getExportButton(), exportButtonCons);
			exportPanel.add(getExportBn2DPicsCheckBox(), exportBn2DPicsPicsCheckBoxCons);
			exportPanel.add(getExportBn2DStateChangePicsCheckBox(), exportBn2DStateChangePicsCheckBoxCons);
			exportPanel.add(getExportNetworkConnectionsPicsCheckBox(), exportNetworkConnectionsPicsCheckBoxCons);
    	}
		return exportPanel;
	}

	private JPanel getBnInfoPanel() {
		if (bnInfoPanel == null) {
			bnInfoPanel = new JPanel();
			bnInfoPanel.setLayout(new GridBagLayout());
			GridBagConstraints iterationLabelCons = new GridBagConstraints();
			GridBagConstraints iterationValueLabelCons = new GridBagConstraints();
			GridBagConstraints totalNeighDistLabelCons = new GridBagConstraints();
			GridBagConstraints totalNeighDistValueLabelCons = new GridBagConstraints();
			GridBagConstraints radiusLabelCons = new GridBagConstraints();
			GridBagConstraints radiusValueLabelCons = new GridBagConstraints();
			GridBagConstraints learningRateLabelCons = new GridBagConstraints();
			GridBagConstraints learningRateValueLabelCons = new GridBagConstraints();
			GridBagConstraints numberOfNodesLabelCons = new GridBagConstraints();
			GridBagConstraints numberOfNodesTextFieldCons = new GridBagConstraints();
			GridBagConstraints connectionsPerNodeLabelCons = new GridBagConstraints();
			GridBagConstraints connectionsPerNodeTextFieldCons = new GridBagConstraints();
			GridBagConstraints speedLabelCons = new GridBagConstraints();
			GridBagConstraints speedTextFieldCons = new GridBagConstraints();
			GridBagConstraints fillerCons = new GridBagConstraints();

			iterationLabelCons.gridx = 0;
			iterationLabelCons.gridy = 0;
			iterationLabelCons.anchor = GridBagConstraints.WEST;
			iterationLabelCons.insets = new Insets(2, 20, 2, 2);

			iterationValueLabelCons.gridx = 1;
			iterationValueLabelCons.gridy = 0;
			iterationValueLabelCons.anchor = GridBagConstraints.WEST;
			iterationValueLabelCons.insets = new Insets(2, 2, 2, 2);

			totalNeighDistLabelCons.gridx = 2;
			totalNeighDistLabelCons.gridy = 0;
			totalNeighDistLabelCons.anchor = GridBagConstraints.WEST;
			totalNeighDistLabelCons.insets = new Insets(2, 20, 2, 2);

			totalNeighDistValueLabelCons.gridx = 3;
			totalNeighDistValueLabelCons.gridy = 0;
			totalNeighDistValueLabelCons.anchor = GridBagConstraints.WEST;
			totalNeighDistValueLabelCons.insets = new Insets(2, 2, 2, 2);

			radiusLabelCons.gridx = 4;
			radiusLabelCons.gridy = 0;
			radiusLabelCons.anchor = GridBagConstraints.WEST;
			radiusLabelCons.insets = new Insets(2, 20, 2, 2);

			radiusValueLabelCons.gridx = 5;
			radiusValueLabelCons.gridy = 0;
			radiusValueLabelCons.anchor = GridBagConstraints.WEST;
			radiusValueLabelCons.insets = new Insets(2, 2, 2, 2);

			learningRateLabelCons.gridx = 6;
			learningRateLabelCons.gridy = 0;
			learningRateLabelCons.anchor = GridBagConstraints.WEST;
			learningRateLabelCons.insets = new Insets(2, 20, 2, 2);

			learningRateValueLabelCons.gridx = 7;
			learningRateValueLabelCons.gridy = 0;
			learningRateValueLabelCons.anchor = GridBagConstraints.WEST;
			learningRateValueLabelCons.insets = new Insets(2, 2, 2, 2);

			fillerCons.gridx = 8;
			fillerCons.gridy = 0;
			fillerCons.weightx = 1;
			fillerCons.fill = GridBagConstraints.HORIZONTAL;

			numberOfNodesLabelCons.gridx = 9;
			numberOfNodesLabelCons.gridy = 0;
			numberOfNodesLabelCons.anchor = GridBagConstraints.EAST;
			numberOfNodesLabelCons.insets = new Insets(2, 2, 2, 2);

			numberOfNodesTextFieldCons.gridx = 10;
			numberOfNodesTextFieldCons.gridy = 0;
			numberOfNodesTextFieldCons.anchor = GridBagConstraints.EAST;
			numberOfNodesTextFieldCons.insets = new Insets(2, 2, 2, 2);

			connectionsPerNodeLabelCons.gridx = 11;
			connectionsPerNodeLabelCons.gridy = 0;
			connectionsPerNodeLabelCons.anchor = GridBagConstraints.EAST;
			connectionsPerNodeLabelCons.insets = new Insets(2, 2, 2, 2);

			connectionsPerNodeTextFieldCons.gridx = 12;
			connectionsPerNodeTextFieldCons.gridy = 0;
			connectionsPerNodeTextFieldCons.anchor = GridBagConstraints.EAST;
			connectionsPerNodeTextFieldCons.insets = new Insets(2, 2, 2, 2);

			speedLabelCons.gridx = 13;
			speedLabelCons.gridy = 0;
			speedLabelCons.anchor = GridBagConstraints.EAST;
			speedLabelCons.insets = new Insets(2, 2, 2, 2);

			speedTextFieldCons.gridx = 14;
			speedTextFieldCons.gridy = 0;
			speedTextFieldCons.anchor = GridBagConstraints.EAST;
			speedTextFieldCons.insets = new Insets(2, 2, 2, 2);

			bnInfoPanel.add(getIterationLabel(), iterationLabelCons);
			bnInfoPanel.add(getIterationValueLabel(), iterationValueLabelCons);
			bnInfoPanel.add(getTotalNeighDistLabel(), totalNeighDistLabelCons);
			bnInfoPanel.add(getTotalNeighDistValueLabel(), totalNeighDistValueLabelCons);
			bnInfoPanel.add(getRadiusLabel(), radiusLabelCons);
			bnInfoPanel.add(getRadiusValueLabel(), radiusValueLabelCons);
			bnInfoPanel.add(getLearningRateLabel(), learningRateLabelCons);
			bnInfoPanel.add(getLearningRateValueLabel(), learningRateValueLabelCons);			
			bnInfoPanel.add(new JLabel(), fillerCons);
			bnInfoPanel.add(getNumberOfNodesLabel(), numberOfNodesLabelCons);
			bnInfoPanel.add(getNumberOfNodesTextField(), numberOfNodesTextFieldCons);
			bnInfoPanel.add(getConnectionsPerNodeLabel(), connectionsPerNodeLabelCons);
			bnInfoPanel.add(getConnectionsPerNodeTextField(), connectionsPerNodeTextFieldCons);
			bnInfoPanel.add(getSpeedLabel(), speedLabelCons);
			bnInfoPanel.add(getSpeedTextField(), speedTextFieldCons);
		}
		return bnInfoPanel;
	}

	private JLabel getIterationLabel() {
		if (iterationLabel == null) {
			iterationLabel = new JLabel();
			iterationLabel.setText("Iteration");
		}
		return iterationLabel;
	}

	private JLabel getIterationValueLabel() {
		if (iterationValueLabel == null) {
			iterationValueLabel = new JLabel();
			iterationValueLabel.setText("" + iteration);
		}
		return iterationValueLabel;
	}

	private JLabel getTotalNeighDistLabel() {
		if (totalNeighDistLabel == null) {
			totalNeighDistLabel = new JLabel();
			totalNeighDistLabel.setText("Distance");
		}
		return totalNeighDistLabel;
	}

	private JLabel getTotalNeighDistValueLabel() {
		if (totalNeighDistValueLabel == null) {
			totalNeighDistValueLabel = new JLabel();
			totalNeighDistValueLabel.setText("");
		}
		return totalNeighDistValueLabel;
	}

	private JLabel getRadiusLabel() {
		if (radiusLabel == null) {
			radiusLabel = new JLabel();
			radiusLabel.setText("Radius");
		}
		return radiusLabel;
	}

	private JLabel getRadiusValueLabel() {
		if (radiusValueLabel == null) {
			radiusValueLabel = new JLabel();
			radiusValueLabel.setText("0");
		}
		return radiusValueLabel;
	}

	private JLabel getLearningRateLabel() {
		if (learningRateLabel == null) {
			learningRateLabel = new JLabel();
			learningRateLabel.setText("LearningRate");
		}
		return learningRateLabel;
	}

	private JLabel getLearningRateValueLabel() {
		if (learningRateValueLabel == null) {
			learningRateValueLabel = new JLabel();
			learningRateValueLabel.setText("0");
		}
		return learningRateValueLabel;
	}

	private JLabel getNumberOfNodesLabel() {
		if (numberOfNodesLabel == null) {
			numberOfNodesLabel = new JLabel();
			numberOfNodesLabel.setText("Number of nodes");
		}
		return numberOfNodesLabel;
	}

	private JTextField getNumberOfNodesTextField() {
		if (numberOfNodesTextField == null) {
			numberOfNodesTextField = new JTextField();
			numberOfNodesTextField.setText("" + DEFAULT_NUMBER_OF_NODES);
			numberOfNodesTextField.setMinimumSize(new Dimension(60,25));
			numberOfNodesTextField.setPreferredSize(new Dimension(60,25));
		}
		return numberOfNodesTextField;
	}

	private JLabel getConnectionsPerNodeLabel() {
		if (connectionsPerNodeLabel == null) {
			connectionsPerNodeLabel = new JLabel();
			connectionsPerNodeLabel.setText("Connections per node");
		}
		return connectionsPerNodeLabel;
	}

	private JTextField getConnectionsPerNodeTextField() {
		if (connectionsPerNodeTextField == null) {
			connectionsPerNodeTextField = new JTextField();
			connectionsPerNodeTextField.setText("" + DEFAULT_CONNECTIONS_PER_NODE);
			connectionsPerNodeTextField.setMinimumSize(new Dimension(60,25));
			connectionsPerNodeTextField.setPreferredSize(new Dimension(60,25));
		}
		return connectionsPerNodeTextField;
	}

	private JLabel getSpeedLabel() {
		if (speedLabel == null) {
			speedLabel = new JLabel();
			speedLabel.setText("Delay");
		}
		return speedLabel;
	}

	private JTextField getSpeedTextField() {
		if (speedTextField == null) {
			speedTextField = new JTextField();
			speedTextField.setText("" + DEFAULT_SPEED);
			speedTextField.setMinimumSize(new Dimension(60,25));
			speedTextField.setPreferredSize(new Dimension(60,25));
			speedTextField.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						Integer newSpeed = Integer.parseInt(speedTextField.getText());
						getBnTimer().setDelay(newSpeed);
					} catch (NumberFormatException exception) {
						speedTextField.setText("" + DEFAULT_SPEED);
					}
				} 
			});
		}
		return speedTextField;
	}

	private JButton getGenerateRandomBNButton() {
		if (generateRandomBNButton == null) {
			generateRandomBNButton = new JButton();
			generateRandomBNButton.setText("Generate Random BN");
			generateRandomBNButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					performGenerateRandomBN();
				}
			});
		}
		return generateRandomBNButton;
	}


	private JButton getGenerateRandomAndOrBNButton() {
		if (generateRandomAndOrBNButton == null) {
			generateRandomAndOrBNButton = new JButton();
			generateRandomAndOrBNButton.setText("Generate Random AND / OR BN");
			generateRandomAndOrBNButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					performGenerateRandomAndOrBN();
				}
			});
		}
		return generateRandomAndOrBNButton;
	}

	private JButton getGenerate1dCycleBNButton() {
		if (generate1dCycleBNButton == null) {
			generate1dCycleBNButton = new JButton();
			generate1dCycleBNButton.setText("Generate 1D Cycle BN");
			generate1dCycleBNButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					perform1dCycleBN();
				}
			});
		}
		return generate1dCycleBNButton;
	}

	private JButton getIntroduce2dSomTopologyButton() {
		if (introduce2dSomTopologyButton == null) {
			introduce2dSomTopologyButton = new JButton();
			introduce2dSomTopologyButton.setText("Introduce 2D SOM Topology");
			introduce2dSomTopologyButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					performIntroduce2dSomTopology();
				}
			});
		}
		return introduce2dSomTopologyButton;
	}

	private JButton getSetRandomConfigButton() {
		if (setRandomConfigButton == null) {
			setRandomConfigButton = new JButton();
			setRandomConfigButton.setText("Set Random Config");
			setRandomConfigButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					performSetRandomConfig();
				}
			});
		}
		return setRandomConfigButton;
	}

	private JButton getSetSingleActiveNodeConfigButton() {
		if (setSingleActiveNodeConfigButton == null) {
			setSingleActiveNodeConfigButton = new JButton();
			setSingleActiveNodeConfigButton.setText("Set Single Active Node");
			setSingleActiveNodeConfigButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					performSetSingleActiveNodeConfig();
				}
			});
		}
		return setSingleActiveNodeConfigButton;
	}

	private JButton getSetAll0sConfigButton() {
		if (setAll0sConfigButton == null) {
			setAll0sConfigButton = new JButton();
			setAll0sConfigButton.setText("Set All 0s Config");
			setAll0sConfigButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					performSetAll0sConfig();
				}
			});
		}
		return setAll0sConfigButton;
	}

	private JButton getSetAll1sConfigButton() {
		if (setAll1sConfigButton == null) {
			setAll1sConfigButton = new JButton();
			setAll1sConfigButton.setText("Set All 1s Config");
			setAll1sConfigButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					performSetAll1sConfig();
				}
			});
		}
		return setAll1sConfigButton;
	}

	private JButton getExportButton() {
		if (exportButton == null) {
			exportButton = new JButton();
			exportButton.setText("Export BN");
			exportButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					performExportBN();
				}
			});
		}
		return exportButton;
	}

	private JButton getStartPauseRandomButton() {
		if (startPauseRandomButton == null) {
			startPauseRandomButton = new JButton();
			startPauseRandomButton.setText(START_BN_TEXT);
			startPauseRandomButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (startPauseRandomButton.getText().equals(START_BN_TEXT)) {
						performStartBN();
					} else {
						performStopBN();
					}
				}
			});
		}
		return startPauseRandomButton;
	}

	private JCheckBox getExportBn2DPicsCheckBox() {
		if (exportBn2DPicsCheckBox == null) {
			exportBn2DPicsCheckBox = new JCheckBox();
			exportBn2DPicsCheckBox.setText("BN 2D Pics");
		}
		return exportBn2DPicsCheckBox;
	}

	private JCheckBox getExportBn2DStateChangePicsCheckBox() {
		if (exportBn2DStateChangePicsCheckBox == null) {
			exportBn2DStateChangePicsCheckBox = new JCheckBox();
			exportBn2DStateChangePicsCheckBox.setText("BN 2D Changed State Pics");
		}
		return exportBn2DStateChangePicsCheckBox;
	}

	private JCheckBox getExportNetworkConnectionsPicsCheckBox() {
		if (exportNetworkConnectionsPicsCheckBox == null) {
			exportNetworkConnectionsPicsCheckBox = new JCheckBox();
			exportNetworkConnectionsPicsCheckBox.setText("Network Connections");
		}
		return exportNetworkConnectionsPicsCheckBox;
	}

	private Timer getBnTimer() {
		if (bnTimer == null) {
			bnTimer = new Timer(DEFAULT_SPEED, getBnTimerActionListener());
			bnTimer.setInitialDelay(0);
		}
		return bnTimer;
	}

    private ActionListener getBnTimerActionListener() {
    	if (bnTimerActionListener == null) {
    		bnTimerActionListener = new ActionListener() {
			
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				if (bnBO != null) {
    					updateIterationValueText();
    					repaint();
    					if (exportBn2DPicsCheckBox.isSelected()) {
    						String fileName = getExportBn2DStatesFileName();
    						FileUtil.getInstance().checkDirectory(fileName);
    						ImageUtils.setComponentAsImage(bn2dPanel, fileName, "png");
    					}
    					if (exportBn2DStateChangePicsCheckBox.isSelected()) {
    						String fileName = getExportBn2DChangedStatesFileName();
    						FileUtil.getInstance().checkDirectory(fileName);
    						ImageUtils.setComponentAsImage(bn2dStateChangePanel, fileName, "png");
    					}
    					bnBO.updateNodesStates();
    					iteration++;
    				}
    			}
			};
    	}
    	return bnTimerActionListener;
    }

	private Timer getSomTimer() {
		if (somTimer == null) {
			somTimer = new Timer(5, new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (somBO != null) {
						if (iteration % 50 == 0) {
							repaint();
							if (exportNetworkConnectionsPicsCheckBox.isSelected()) {
								String fileName = getExportNetworkConnectionsFileName();
								FileUtil.getInstance().checkDirectory(fileName);
								ImageUtils.setComponentAsImage(graphPanel, fileName, "png");
							}
						}
						if (!somBO.learnOneStep()) {
							getSomTimer().stop();
							JOptionPane.showMessageDialog(BNFrame.this,"SOM work done!");
						}
						updateIterationValueText();
//						if (iteration % 5 == 0) {
						// now when topology is found we have to set it back to BN
						List<Double[]> inputs = new ArrayList<Double[]>();
						for (Node<Boolean> bnNode : bnBO.getNodes()) {
							inputs.add(ConversionUtil.convertToDouble(bnBO.getNodeNeighborhoodFlagsWithReflexivity(bnNode)));
						}
						
//						List<SomNode<Double>> bmuNodes = somBO.getTradeOffRankBmuNodes(inputs);
//						List<SomNode<Double>> bmuNodes = somBO.getBestMatchingUnitsReverse(inputs);
						List<SomNode<Double>> bmuNodes = somBO.getDistanceBasedBmuNodes(inputs);
//						List<SomNode<Double>> bmuNodes = somBO.getInputDistanceBasedBmuNodes(inputs);
						Iterator<SomNode<Double>> bmuNodesIterator = bmuNodes.iterator();
						for (Node<Boolean> bnNode : bnBO.getNodes()) {
							bnNode.setLocation(bmuNodesIterator.next().getLocation());
						}						
//						}
						updateTotalNeighDistValueText();
						updateRadiusValueText();
						updateLearningRateValueText();
						iteration++;
					}
				}
			});
			somTimer.setInitialDelay(0);
		}
		return somTimer;
	}

	private String getExportBn2DStatesFileName() {
		return "Export/bn_2D_states_" + getIterationWithLeadingZeros() + ".png";
	}

	private String getExportBn2DChangedStatesFileName() {
		return "Export/bn_2D_changed_states_" + getIterationWithLeadingZeros() + ".png";
	}

	private String getExportNetworkConnectionsFileName() {
		return "Export/bn_2D_network_" + getIterationWithLeadingZeros() + ".png";
	}

	private String getIterationWithLeadingZeros() {
		int totalSize = 6;
		String iterationString = "" + iteration;
		StringBuffer sb = new StringBuffer();
		int zerosNum = totalSize - iterationString.length();
		for (int i = 0; i < zerosNum; i++) {
			sb.append('0');
		}
		sb.append(iterationString);
		return sb.toString();
	}

	private void updateIterationValueText() {
		getIterationValueLabel().setText("" + iteration);
	}

	private void updateTotalNeighDistValueText() {
		if (bnBO != null) {
			getTotalNeighDistValueLabel().setText("" + bnBO.getTotalNeighborDistance());
		}
	}

	private void updateRadiusValueText() {
		if (somBO != null) {
			getRadiusValueLabel().setText("" + Math.floor(100 * somBO.getNeighborhoodRadius()) / 100);
		}
	}

	private void updateLearningRateValueText() {
		if (somBO != null) {
			getLearningRateValueLabel().setText("" + Math.floor(100 * somBO.getLearningRate()) / 100);
		}
	}

/////////////////////
// BUSINESS METHODS
////////////////////

	private void performGenerateRandomBN() {
		Integer numberOfNodes = Integer.parseInt(getNumberOfNodesTextField().getText());
		Integer connectionsPerNode = Integer.parseInt(getConnectionsPerNodeTextField().getText());
		BooleanNetworkFactory bnFactory = getBooleanNetworkFactory();
		BooleanNetwork bn = bnFactory.createRandomBNWithFixK(numberOfNodes, connectionsPerNode, true);

		BooleanNetworkBO bnBO = new BooleanNetworkBO(bn, functionEvaluatorFactory);
		bnBO.set2dTopology();
		bnBO.setRandomStates();
		bnBO.assignIdentifiers();

		iteration = 0;
		updateIterationValueText();
		updateTotalNeighDistValueText();

		setBnBO(bnBO);
	}

	private void performGenerateRandomAndOrBN() {
		String nodesNumString = getNumberOfNodesTextField().getText();
		if (nodesNumString == null || nodesNumString.trim().isEmpty()) {
			showMessage("The number of nodes must be specified.");
			return;
		}
		Integer numberOfNodes = Integer.parseInt(nodesNumString);
		String connectionsPerNodeString = getConnectionsPerNodeTextField().getText();
		if (connectionsPerNodeString == null || connectionsPerNodeString.trim().isEmpty()) {
			showMessage("Connections per node must be specified.");
			return;
		}
		Integer connectionsPerNode = Integer.parseInt(connectionsPerNodeString);
		BooleanNetworkFactory bnFactory = getBooleanNetworkFactory();
		BooleanNetwork bn = bnFactory.createRandomBNWithMixAND_ORFun(numberOfNodes, connectionsPerNode, true);

		BooleanNetworkBO bnBO = new BooleanNetworkBO(bn, functionEvaluatorFactory);
		bnBO.set2dTopology();
		bnBO.setRandomStates();
		bnBO.assignIdentifiers();

		iteration = 0;
		updateIterationValueText();
		updateTotalNeighDistValueText();

		setBnBO(bnBO);
	}

	private void perform1dCycleBN() {
		Integer numberOfNodes = Integer.parseInt(getNumberOfNodesTextField().getText());
		BooleanNetworkFactory bnFactory = getBooleanNetworkFactory();
		BooleanNetwork bn = bnFactory.create1dCycleBNWithRandomFunctions(numberOfNodes);

		bnBO = new BooleanNetworkBO(bn, functionEvaluatorFactory);
		bnBO.set2dTopology();
		bnBO.setRandomStates();
		bnBO.assignIdentifiers();

		iteration = 0;
		updateIterationValueText();
		updateTotalNeighDistValueText();

		setBnBO(bnBO);
	}

	private void performSetRandomConfig() {
		if (bnBO == null) {
			showMessage("No boolean network generated / selected!");	
		} else {
			bnBO.setRandomStates();
			iteration = 0;
			repaint();
			updateIterationValueText();
		}
	}

	private void performSetSingleActiveNodeConfig() {
		if (bnBO == null) {
			showMessage("No boolean network generated / selected!");	
		} else {
			bnBO.setStatesToSingleActiveNode();
			iteration = 0;
			repaint();
			updateIterationValueText();
		}
	}

	private void performSetAll0sConfig() {
		if (bnBO == null) {
			showMessage("No boolean network generated / selected!");	
		} else {
			bnBO.setStatesToZero();
			iteration = 0;
			repaint();
			updateIterationValueText();
		}
	}

	private void performSetAll1sConfig() {
		if (bnBO == null) {
			showMessage("No boolean network generated / selected!");	
		} else {
			bnBO.setStatesToOne();
			iteration = 0;
			repaint();
			updateIterationValueText();
		}
	}

	private void performExportBN() {
		if (bnBO == null) {
			showMessage("No boolean network generated / selected!");	
		} else {
			InputOutputHandler ioHandler = InputOutputHandler.getInstance();
//			ioHandler.storeAdjacencyMatrix(bnBO);
//			ioHandler.storeFunctions(bnBO);
			ioHandler.storeStates(bnBO);
			showMessage("BN successfully exported.");	
		}
	}

	private void performIntroduce2dSomTopology() {
		if (bnBO == null) {
			showMessage("No boolean network generated / selected!");	
		} else {
			SelfOrganizedMap<Double> som = SelfOrganizedMapFactory.getInstance().createRectangularSelfOrganizedMap(
					ConversionUtil.convertToDouble(bnBO.getAllNodeNeighborhoodFlagsWithReflexivity()), true);

			somBO = new SelfOrganizedMapBO<Double>(som);
			somBO.initializeWeights();
			iteration = 0;
			getSom2dPanel().setSom(som);
			getSomTimer().start();
		}
	}

	private void performStartBN() {
		if (bnBO == null) {
			showMessage("No boolean network generated / selected!");	
		} else {
			getBnTimer().start();
			startPauseRandomButton.setText(STOP_BN_TEXT);
			generateRandomBNButton.setEnabled(false);
			generateRandomAndOrBNButton.setEnabled(false);
			generate1dCycleBNButton.setEnabled(false);
//			setRandomConfigButton.setEnabled(false);
		}
	}

	private void performStopBN() {
		if (bnBO == null) {
			showMessage("No boolean network generated / selected!");	
		} else {
			getBnTimer().stop();
			startPauseRandomButton.setText(START_BN_TEXT);
			generateRandomBNButton.setEnabled(true);
			generateRandomAndOrBNButton.setEnabled(true);
			generate1dCycleBNButton.setEnabled(true);
		}
	}

	private BooleanNetworkFactory getBooleanNetworkFactory() {
		return BooleanNetworkFactory.getInstance();
	}

	public static BNFrame createBNFrame(BooleanNetworkBO bnBO) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException e) {
			new RuntimeException("Look & feel problem occured!");
		} catch (InstantiationException e) {
			new RuntimeException("Look & feel problem occured!");
		} catch (IllegalAccessException e) {
			new RuntimeException("Look & feel problem occured!");
		} catch (UnsupportedLookAndFeelException e) {
			new RuntimeException("Look & feel problem occured!");
		}
		return new BNFrame(bnBO);
	}

	private void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);		
	}
} 