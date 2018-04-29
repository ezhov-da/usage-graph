package ru.ezhov.graph;

import edu.uci.ics.jung.samples.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by ezhov_da on 25.04.2018.
 */
public class Test {
	private static final Logger LOG = Logger.getLogger(Test.class.getName());

	public static void main(final String[] args) {
		final List<Example> examples = Arrays.asList(
			new Example(
				"AddNodeDemo",
				new Runnable() {
					@Override
					public void run() {
						AddNodeDemo.main(args);
					}
				}
			),
			new Example(
				"AnimatingAddNodeDemo",
				new Runnable() {
					@Override
					public void run() {
						AnimatingAddNodeDemo.main(args);
					}
				}
			),
			new Example(
				"AnnotationsDemo",
				new Runnable() {
					@Override
					public void run() {
						AnnotationsDemo.main(args);
					}
				}
			),
			new Example(
				"BalloonLayoutDemo",
				new Runnable() {
					@Override
					public void run() {
						BalloonLayoutDemo.main(args);
					}
				}
			),
			new Example(
				"ClusteringDemo",
				new Runnable() {
					@Override
					public void run() {
						try {
							ClusteringDemo.main(args);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			),
			new Example(
				"DrawnIconVertexDemo",
				new Runnable() {
					@Override
					public void run() {
						DrawnIconVertexDemo.main(args);
					}
				}
			),
			new Example(
				"EdgeLabelDemo",
				new Runnable() {
					@Override
					public void run() {
						EdgeLabelDemo.main(args);
					}
				}
			),
			new Example(
				"GraphEditorDemo",
				new Runnable() {
					@Override
					public void run() {
						GraphEditorDemo.main(args);
					}
				}
			),
			new Example(
				"GraphFromGraphMLDemo",
				new Runnable() {
					@Override
					public void run() {

					}
				}
			),
			new Example(
				"GraphFromGraphMLDemo",
				new Runnable() {
					@Override
					public void run() {
						try {
							GraphFromGraphMLDemo.main(args);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			),
			new Example(
				"GraphZoomScrollPaneDemo",
				new Runnable() {
					@Override
					public void run() {
						GraphZoomScrollPaneDemo.main(args);
					}
				}
			),
			new Example(
				"ImageEdgeLabelDemo",
				new Runnable() {
					@Override
					public void run() {
						ImageEdgeLabelDemo.main(args);
					}
				}
			),
			new Example(
				"InternalFrameSatelliteViewDemo",
				new Runnable() {
					@Override
					public void run() {
						InternalFrameSatelliteViewDemo.main(args);
					}
				}
			),
			new Example(
				"L2RTreeLayoutDemo",
				new Runnable() {
					@Override
					public void run() {
						L2RTreeLayoutDemo.main(args);
					}
				}
			),
			new Example(
				"LensDemo",
				new Runnable() {
					@Override
					public void run() {
						LensDemo.main(args);
					}
				}
			),
			new Example(
				"LensVertexImageShaperDemo",
				new Runnable() {
					@Override
					public void run() {
						LensVertexImageShaperDemo.main(args);
					}
				}
			),
			new Example(
				"MinimumSpanningTreeDemo",
				new Runnable() {
					@Override
					public void run() {
						MinimumSpanningTreeDemo.main(args);
					}
				}
			),
			new Example(
				"MultiViewDemo",
				new Runnable() {
					@Override
					public void run() {
						MultiViewDemo.main(args);
					}
				}
			),
			new Example(
				"PersistentLayoutDemo",
				new Runnable() {
					@Override
					public void run() {
						PersistentLayoutDemo.main(args);
					}
				}
			),
			new Example(
				"PluggableRendererDemo",
				new Runnable() {
					@Override
					public void run() {
						PluggableRendererDemo.main(args);
					}
				}
			),
			new Example(
				"RadialTreeLensDemo",
				new Runnable() {
					@Override
					public void run() {
						RadialTreeLensDemo.main(args);
					}
				}
			),
			new Example(
				"SatelliteViewDemo",
				new Runnable() {
					@Override
					public void run() {
						SatelliteViewDemo.main(args);
					}
				}
			),
			new Example(
				"ShowLayouts",
				new Runnable() {
					@Override
					public void run() {
						ShowLayouts.main(args);
					}
				}
			),
			new Example(
				"ShortestPathDemo",
				new Runnable() {
					@Override
					public void run() {
						ShortestPathDemo.main(args);
					}
				}
			),
			new Example(
				"SimpleGraphDraw",
				new Runnable() {
					@Override
					public void run() {
						try {
							SimpleGraphDraw.main(args);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			),
			new Example(
				"SubLayoutDemo",
				new Runnable() {
					@Override
					public void run() {
						SubLayoutDemo.main(args);
					}
				}
			),
			new Example(
				"TreeCollapseDemo",
				new Runnable() {
					@Override
					public void run() {
						TreeCollapseDemo.main(args);
					}
				}
			),
			new Example(
				"TwoModelDemo",
				new Runnable() {
					@Override
					public void run() {
						TwoModelDemo.main(args);
					}
				}
			),
			new Example(
				"TreeLayoutDemo",
				new Runnable() {
					@Override
					public void run() {
						TreeLayoutDemo.main(args);
					}
				}
			),
			new Example(
				"VertexCollapseDemo",
				new Runnable() {
					@Override
					public void run() {
						VertexCollapseDemo.main(args);
					}
				}
			),
			new Example(
				"VertexCollapseDemoWithLayouts",
				new Runnable() {
					@Override
					public void run() {
						VertexCollapseDemoWithLayouts.main(args);
					}
				}
			),
			new Example(
				"UnicodeLabelDemo",
				new Runnable() {
					@Override
					public void run() {
						UnicodeLabelDemo.main(args);
					}
				}
			),
			new Example(
				"VertexImageShaperDemo",
				new Runnable() {
					@Override
					public void run() {
						VertexImageShaperDemo.main(args);
					}
				}
			),
			new Example(
				"VertexLabelAsShapeDemo",
				new Runnable() {
					@Override
					public void run() {
						VertexLabelAsShapeDemo.main(args);
					}
				}
			),
			new Example(
				"VertexLabelPositionDemo",
				new Runnable() {
					@Override
					public void run() {
						VertexLabelPositionDemo.main(args);
					}
				}
			),
			new Example(
				"VisualizationImageServerDemo",
				new Runnable() {
					@Override
					public void run() {
						VisualizationImageServerDemo.main(args);
					}
				}
			),
			new Example(
				"WorldMapGraphDemo",
				new Runnable() {
					@Override
					public void run() {
						WorldMapGraphDemo.main(args);
					}
				}
			)
		);


		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame();

				JPanel panel = new JPanel(new GridLayout(examples.size(), 1));

				for (final Example example : examples) {
					JButton button = new JButton(example.name);
					button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							example.runnable.run();
						}
					});
					panel.add(button);
				}

				frame.add(panel);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	private static class Example {
		private String name;
		private Runnable runnable;

		public Example(String name, Runnable runnable) {
			this.name = name;
			this.runnable = runnable;
		}
	}
}
