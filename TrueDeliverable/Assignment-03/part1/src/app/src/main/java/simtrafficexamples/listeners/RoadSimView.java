package simtrafficexamples.listeners;

import java.util.List;
import java.util.Optional;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import simengine.AbstractSimulation.Resume;
import simengine.AbstractSimulation.StartSimulation;
import simengine.AbstractSimulation.Stop;
import simengine.SimulationListener;
import utils.P2d;
import simtrafficbase.entity.TrafficLight;
import utils.V2d;
import simtrafficbase.entity.CarAgent;
import simtrafficbase.environment.Road;
import simtrafficbase.environment.RoadsEnv;

import java.awt.*;
import actor.Command;

import javax.swing.*;

public class RoadSimView extends SimulationListener {

    public static record Show() implements Command {}

    private JFrame frame = new JFrame("RoadSim View");
	private RoadSimViewPanel panel;
	private static final int CAR_DRAW_SIZE = 10;
	
    public static Behavior<Command> create(ActorRef<Command> sim) {
        return Behaviors.setup(context -> new RoadSimView(context, sim));
    }

	private RoadSimView(ActorContext<Command> context, ActorRef<Command> sim) {
		super(context);
        frame.setSize(1500,600);
			
		panel = new RoadSimViewPanel(1500,600, sim, frame); 
		panel.setSize(1500, 600);

		JPanel cp = new JPanel();
		LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);
		cp.add(BorderLayout.CENTER,panel);
		frame.setContentPane(cp);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Show.class, this::display)
                .onMessage(ViewUpdate.class, this::onViewUpdate)
                .onMessage(SimulationFinished.class, this::onSimulationFinished)
                .build();
    }

	public Behavior<Command> display(Show command) {
		SwingUtilities.invokeLater(() -> {
			frame.setVisible(true);
		});
        return this;
	}
	
	
	class RoadSimViewPanel extends JPanel {
		
		List<CarAgent> cars;
		List<Road> roads;
		List<TrafficLight> sems;
		ActorRef<Command> simulation;
		JButton startButton;
		
		public RoadSimViewPanel(int w, int h, ActorRef<Command> sim, JFrame frame){
			this.simulation = sim;
			this.startButton = new JButton("Start");
			JTextField textField = new JTextField();
			JLabel label = new JLabel("Insert number of steps: ");
			textField.setText("1000");
			textField.setColumns(10);
			((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumberOnlyFilter());
			startButton.addActionListener((click) -> {
				if (startButton.getText().equals("Stop")) {
					startButton.setText("Resume");				
                    simulation.tell(new Stop());
				} else if (startButton.getText().equals("Start")) {
					this.remove(label);
					this.remove(textField);
                    simulation.tell(new StartSimulation(Integer.parseInt(textField.getText())));
					startButton.setText("Stop");
				} else {
                    simulation.tell(new Resume());
                    startButton.setText("Stop");				
				}
			});
			this.add(label);
			this.add(textField);
			this.add(startButton);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (roads == null) return;
	        Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.clearRect(0,0,this.getWidth(),this.getHeight());
			
			P2d downRightCorner = new P2d(
				roads.stream()
					.map(r -> r.getFrom().x() > r.getTo().x() ? r.getFrom().x() : r.getTo().x())
					.reduce((r1, r2) -> r1 > r2 ? r1 : r2).get(),
				roads.stream()
					.map(r -> r.getFrom().y() > r.getTo().y() ? r.getFrom().y() : r.getTo().y())
					.reduce((r1, r2) -> r1 > r2 ? r1 : r2).get()
			);

			double edgeX = downRightCorner.x();
			Optional<Road> roadOnEdgeX = roads.stream()
				.filter(r -> r.getFrom().x() == r.getTo().x())
				.filter(r -> r.getFrom().x() == edgeX)
				.findAny();
			if (roadOnEdgeX.isPresent()) {
				downRightCorner = new P2d(downRightCorner.x() * 2d, downRightCorner.y());
			}
			double edgeY = downRightCorner.y();
			Optional<Road> roadOnEdgeY = roads.stream()
				.filter(r -> r.getFrom().y() == r.getTo().y())
				.filter(r -> r.getFrom().y() == edgeY)
				.findAny();
			
			if (roadOnEdgeY.isPresent()) {
				downRightCorner = new P2d(downRightCorner.x(), downRightCorner.y() * 2d);
			}

			if (roads != null) {
				for (Road r: roads) {
					g2.drawLine(
						(int)mapValue(r.getFrom().x(), 0, downRightCorner.x(), 0, this.getWidth()), 
						(int)mapValue(r.getFrom().y(), 0, downRightCorner.y(), 0, this.getHeight()), 
						(int)mapValue(r.getTo().x(), 0, downRightCorner.x(), 0, this.getWidth()), 
						(int)mapValue(r.getTo().y(), 0, downRightCorner.y(), 0, this.getHeight())
					);
				}
			}
			
			if (sems != null) {
				for (TrafficLight s: sems) {
					if (s.isGreen()) {
						g.setColor(new Color(0, 255, 0, 255));
					} else if (s.isRed()) {
						g.setColor(new Color(255, 0, 0, 255));
					} else {
						g.setColor(new Color(255, 255, 0, 255));
					}
					P2d point = mapEntityOnRoad(s.getCurrentPosition(), s.getRoad());
					g2.fillRect(
						(int)mapValue(point.x(), 0, downRightCorner.x(), 0, this.getWidth()) - 5, 
						(int)mapValue(point.y(), 0, downRightCorner.y(), 0, this.getHeight()) - 5, 
						10,
						10
					);
				}
			}
			
			g.setColor(new Color(0, 0, 0, 255));

			if (cars != null) {
				for (CarAgent c: cars) {
					double pos = c.getCurrentPosition();
					Road r = c.getRoad();
					V2d dir = V2d.makeV2d(r.getFrom(), r.getTo()).getNormalized().mul(pos);
					P2d point = mapEntityOnRoad(dir.abs(), r);
					g2.drawOval(
						(int)(mapValue(point.x() , 0, downRightCorner.x(), 0, this.getWidth()) - CAR_DRAW_SIZE/2), 
						(int)(mapValue(point.y() , 0, downRightCorner.y(), 0, this.getHeight()) - CAR_DRAW_SIZE/2), 
						CAR_DRAW_SIZE, 
						CAR_DRAW_SIZE
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

	private class NumberOnlyFilter extends DocumentFilter {
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {

            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.insert(offset, string);

            if (test(sb.toString())) {
                super.insertString(fb, offset, string, attr);
            } else {
                // Do nothing, if the input is not a number
            }
        }

        private boolean test(String text) {
            return text.matches("\\d*"); // Match zero or more digits
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {

            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.replace(offset, offset + length, text);

            if (test(sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                // Do nothing, if the input is not a number
            }
        }

        @Override
        public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.delete(offset, offset + length);

            if (test(sb.toString())) {
                super.remove(fb, offset, length);
            } else {
                // Do nothing, if the input is not a number
            }
        }
    }

    @Override
    protected Behavior<Command> onViewUpdate(ViewUpdate command) {
        RoadsEnv e = ((RoadsEnv) command.env());
		panel.update(e.getRoads(), e.getAgentInfo(), e.getTrafficLights());
        return this;
    }

    @Override
    protected Behavior<Command> onSimulationFinished(SimulationFinished command) {
		this.panel.startButton.setEnabled(false);
        return this;
    }
}
