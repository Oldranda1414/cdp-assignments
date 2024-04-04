package pcd.ass01.simtrafficexamplesconcurrent;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pcd.ass01.simengineconcurrent.AbstractAgent;
import pcd.ass01.simengineconcurrent.AbstractEnvironment;
import pcd.ass01.simengineconcurrent.SimulationListener;
import pcd.ass01.simtrafficbaseconcurrent.P2d;
import pcd.ass01.simtrafficbaseconcurrent.TrafficLight;
import pcd.ass01.simtrafficbaseconcurrent.agent.CarAgent;
import pcd.ass01.simtrafficbaseconcurrent.environment.Road;

import java.awt.*;
import java.awt.geom.Line2D;

import javax.swing.*;

public class RoadSimView extends JFrame implements SimulationListener {

	private RoadSimViewPanel panel;
	private static final int CAR_DRAW_SIZE = 10;
	
	public RoadSimView() {
		super("RoadSim View");
		setSize(1500,600);
			
		panel = new RoadSimViewPanel(1500,600); 
		panel.setSize(1500, 600);

		JPanel cp = new JPanel();
		LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);
		cp.add(BorderLayout.CENTER,panel);
		setContentPane(cp);		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
			
	}
	
	public void display() {
		SwingUtilities.invokeLater(() -> {
			this.setVisible(true);
		});
	}
	
	
	class RoadSimViewPanel extends JPanel {
		
		List<CarAgent> cars;
		List<Road> roads;
		List<TrafficLight> sems;
		
		public RoadSimViewPanel(int w, int h){
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);   
	        Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.clearRect(0,0,this.getWidth(),this.getHeight());
			
			if (roads != null) {
				for (var r: roads) {
					g2.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
				}
			}
			
			if (sems != null) {
				for (var s: sems) {
					if (s.isGreen()) {
						g.setColor(new Color(0, 255, 0, 255));
					} else if (s.isRed()) {
						g.setColor(new Color(255, 0, 0, 255));
					} else {
						g.setColor(new Color(255, 255, 0, 255));
					}
					g2.fillRect((int)(s.getPos().x()-5), (int)(s.getPos().y()-5), 10, 10);
				}
			}
			
			g.setColor(new Color(0, 0, 0, 255));

			if (cars != null) {
				for (var c: cars) {
					double pos = c.getPos();
					Road r = c.getRoad();
					V2d dir = V2d.makeV2d(r.getFrom(), r.getTo()).getNormalized().mul(pos);
					g2.drawOval((int)(r.getFrom().x() + dir.x() - CAR_DRAW_SIZE/2), (int)(r.getFrom().y() + dir.y() - CAR_DRAW_SIZE/2), CAR_DRAW_SIZE , CAR_DRAW_SIZE);
				}
			}
		}
		
		public void update(
			List<Road> roads, 
			List<CarAgent> cars,
			List<TrafficLight> sems
		) {
			this.roads = roads;
			this.cars = cars;
			this.sems = sems;
			repaint();
		}

		// private P2D mapEntityOnRoad(double position, Road road) {
		// 	return new P2d(, );
		// }
	}


	@Override
	public void notifyInit(int t, AbstractEnvironment<? extends AbstractAgent> env) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'notifyInit'");
	}

	@Override
	public void notifyStepDone(int t, AbstractEnvironment<? extends AbstractAgent> env) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'notifyStepDone'");
	}
}
