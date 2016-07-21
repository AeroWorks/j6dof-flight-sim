package com.chrisali.javaflightsim.menus;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.chrisali.javaflightsim.controllers.SimulationController;
import com.chrisali.javaflightsim.datatransfer.FlightDataListener;
import com.chrisali.javaflightsim.instrumentpanel.ClosePanelListener;
import com.chrisali.javaflightsim.instrumentpanel.InstrumentPanel;
import com.chrisali.javaflightsim.menus.optionspanel.DisplayOptions;
import com.chrisali.javaflightsim.otw.RunWorld;

public class SimulationWindow extends JFrame {

	private static final long serialVersionUID = 7290660958478331031L;
	
	private InstrumentPanel instrumentPanel;
	private Canvas outTheWindowCanvas;
	
	private ClosePanelListener closePanelListener;
	
	public SimulationWindow(SimulationController controller) {
		super("Java Flight Simulator");
		
		//============================ Panels and Grid Bag Setup =================================
		
		//	--------------------------------------
		//  |	           OTW					 |
		//  |        							 |
		//  |____________________________________|
		//  | Padding |InstrumentPanel| Padding  |
		//	--------------------------------------
		
		JPanel instrumentPanelPanel = new JPanel();
		
		instrumentPanelPanel.setLayout(new GridBagLayout());

		setLayout(new GridBagLayout());
		
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.fill    = GridBagConstraints.BOTH;
		gc.gridy   = 0;
		gc.weighty = 100;
		gc.weightx = 100;
		
		//---------------------- Out the Window Canvas -------------------------------------------
		
		outTheWindowCanvas = new Canvas() {

			private static final long serialVersionUID = 6438710048789252704L;

			@Override
			public void addNotify() {
				super.addNotify();
			}

			@Override
			public void removeNotify() {
				super.removeNotify();
				try {
					controller.getOTWThread().join();
					RunWorld.requestClose();
				} catch (InterruptedException e) {}
			}
			
		};
		
		add(outTheWindowCanvas, gc);
		
		//------------------------- Instrument Panel ---------------------------------------------
		
		gc.gridy      = 1;

		add(instrumentPanelPanel,gc);
	
		instrumentPanel = new InstrumentPanel();
		JPanel padding = new JPanel();
		padding.setSize(instrumentPanel.getSize());
		padding.setMinimumSize(instrumentPanel.getSize());
		
		gc.gridwidth  = 3;
		gc.gridx      = 1;
		gc.weighty 	  = 1;
		gc.weightx 	  = 5;
		
		instrumentPanelPanel.add(instrumentPanel, gc);
		
		gc.gridx      = 0;
		gc.weightx    = 70;
		
		instrumentPanelPanel.add(padding, gc);
		
		gc.gridx      = 2;
		
		instrumentPanelPanel.add(padding, gc);
		
	
		//========================== Window Settings =============================================

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (closePanelListener != null)
					closePanelListener.panelWindowClosed();
			}
		});
		
		Dimension windowSize = new Dimension(controller.getDisplayOptions().get(DisplayOptions.DISPLAY_WIDTH), 
											 controller.getDisplayOptions().get(DisplayOptions.DISPLAY_HEIGHT));
		setSize(windowSize.width, windowSize.height);
		setMinimumSize(windowSize);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
	}
	
	/**
	 * @return {@link InstrumentPanel} object to set {@link FlightDataListener} to the instrument panel
	 * 	in {@link MainFrame}
	 */
	public InstrumentPanel getInstrumentPanel() {
		return instrumentPanel;
	}
	
	/**
	 * @return {@link Canvas} object used to render the {@link RunWorld} out the window display 
	 */
	public Canvas getOutTheWindowCanvas() {
		return outTheWindowCanvas;
	}

	/**
	 * Sets a listener to monitor for a window closing event so that the simulation can stop
	 * 
	 * @param closePanelListener
	 */
	public void setClosePanelListener(ClosePanelListener closePanelListener) {
		this.closePanelListener = closePanelListener;
	}
}
