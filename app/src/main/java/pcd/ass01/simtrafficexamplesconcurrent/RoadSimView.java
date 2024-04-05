package pcd.ass01.simtrafficexamplesconcurrent;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pcd.ass01.simengineconcurrent.AbstractAgent;
import pcd.ass01.simengineconcurrent.AbstractEnvironment;
import pcd.ass01.simengineconcurrent.SimulationListener;
import pcd.ass01.simtrafficbaseconcurrent.P2d;
import pcd.ass01.simtrafficbaseconcurrent.entity.TrafficLight;
import pcd.ass01.simtrafficbaseconcurrent.V2d;
import pcd.ass01.simtrafficbaseconcurrent.entity.CarAgent;
import pcd.ass01.simtrafficbaseconcurrent.environment.Road;
import pcd.ass01.simtrafficbaseconcurrent.environment.RoadsEnv;
import pcd.ass01.utils.Pair;
import java.awt.*;
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
			
			var maxBounds = new Pair<Double, Double>(
				roads.stream()
					.map(road -> {return road.getTo().x() > road.getFrom().x() ? road.getTo().x() : road.getFrom().x();})
					.reduce((r1, r2) -> {return r1 > r2 ? r1 : r2;}).get(),
				roads.stream()
					.map(road -> {return road.getTo().y() > road.getFrom().y() ? road.getTo().y() : road.getFrom().y();})
					.reduce((r1, r2) -> {return r1 > r2 ? r1 : r2;}).get() * 1.5d //it's times 1.5 because we dont want the longest road to be in a side of the screen
			);

			if (roads != null) {
				for (var r: roads) {
					g2.drawLine(
						(int)mapValue(r.getFrom().x(), 0, maxBounds.getFirst(), 0, this.getWidth()), 
						(int)mapValue(r.getFrom().y(), 0, maxBounds.getSecond(), 0, this.getHeight()), 
						(int)mapValue(r.getTo().x(), 0, maxBounds.getFirst(), 0, this.getWidth()), 
						(int)mapValue(r.getTo().y(), 0, maxBounds.getSecond(), 0, this.getHeight())
					);
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
					var point = mapEntityOnRoad(s.getCurrentPosition(), s.getRoad());
					var mappedPoint = new P2d(
						mapValue(point.x(), 0, maxBounds.getFirst(), 0, this.getWidth()),
						mapValue(point.y(), 0, maxBounds.getSecond(), 0, this.getHeight())
					);
					g2.fillRect((int)mappedPoint.x() - 5, (int)mappedPoint.y() - 5, 10, 10);
				}
			}
			
			g.setColor(new Color(0, 0, 0, 255));

			if (cars != null) {
				for (var c: cars) {
					double pos = c.getCurrentPosition();
					Road r = c.getRoad();
					var mappedRoad = new Pair<P2d, P2d>(
						new P2d(
							mapValue(r.getFrom().x(), 0, maxBounds.getFirst(), 0, this.getWidth()), 
							mapValue(r.getFrom().y(), 0, maxBounds.getSecond(), 0, this.getHeight())
						), new P2d(
							mapValue(r.getTo().x(), 0, maxBounds.getFirst(), 0, this.getWidth()), 
							mapValue(r.getTo().y(), 0, maxBounds.getSecond(), 0, this.getHeight())
						)
					);
					V2d dir = V2d.makeV2d(mappedRoad.getFirst(), mappedRoad.getSecond()).getNormalized().mul(pos);
					var point = mapEntityOnRoad(mappedRoad.getFirst().x() + dir.x(), r);
					g2.drawOval(
						(int)(mapValue(point.x(), 0, maxBounds.getFirst(), 0, this.getWidth()) - CAR_DRAW_SIZE/2), 
						(int)(mapValue(point.y(), 0, maxBounds.getSecond(), 0, this.getHeight()) - CAR_DRAW_SIZE/2), CAR_DRAW_SIZE , CAR_DRAW_SIZE
					);
				}
			}
		}

		private double mapValue(double value, double fromX, double fromY, double toX, double toY) {
			return toX + ((value - fromX) / (fromY - fromX)) * (toY - toX);
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

		private P2d mapEntityOnRoad(double position, Road road) {
			double segmentLength = Math.sqrt(Math.pow(road.getTo().x() - road.getFrom().x(), 2) + Math.pow(road.getTo().y() - road.getFrom().y(), 2));
			double percentage = position / segmentLength;
			double newX = road.getFrom().x() + (road.getTo().x() - road.getFrom().x()) * percentage;
			double newY = road.getFrom().y() + (road.getTo().y() - road.getFrom().y()) * percentage;
			return new P2d(newX, newY);
		}
	}


	@Override
	public void notifyInit(int t, AbstractEnvironment<? extends AbstractAgent> env) { }

	@Override
	public void notifyStepDone(int t, AbstractEnvironment<? extends AbstractAgent> env) {
		var e = ((RoadsEnv) env);
		panel.update(e.getRoads(), e.getAgentInfo(), e.getTrafficLights());
	}
}
