package edu.brown.cs.ai.domain.oomdp.blockdude;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import edu.umbc.cs.maple.oomdp.Domain;
import edu.umbc.cs.maple.oomdp.ObjectInstance;
import edu.umbc.cs.maple.oomdp.visualizer.ObjectPainter;
import edu.umbc.cs.maple.oomdp.visualizer.StaticPainter;
import edu.umbc.cs.maple.oomdp.visualizer.Visualizer;

public class BlockdudeVisualizer {

	public static int			RENDERMAXX = 25;
	public static int			RENDERMAXY = 25;
	
	
	public static Visualizer getVisualizer(){
		
		Blockdude bd = new Blockdude();
		Domain d = bd.generateDomain();
		Visualizer v = new Visualizer();
		
		v.addObjectClassPainter(Blockdude.CLASSAGENT, new AgentPainter(d));
		v.addObjectClassPainter(Blockdude.CLASSBLOCK, new BlockPainter(d));
		v.addObjectClassPainter(Blockdude.CLASSEXIT, new ExitPainter(d));
		v.addObjectClassPainter(Blockdude.CLASSPLATFORM, new PlatformPainter(d));
		
		return v;
		
	}
	
	
	public static Visualizer getMemReducedVisualizer(){
		
		BlockdudeMemReduced bd = new BlockdudeMemReduced();
		Domain d = bd.generateDomain();
		Visualizer v = new Visualizer();
		
		v.addObjectClassPainter(BlockdudeMemReduced.CLASSAGENT, new AgentPainter(d));
		v.addObjectClassPainter(BlockdudeMemReduced.CLASSBLOCK, new BlockPainter(d));
		v.addObjectClassPainter(BlockdudeMemReduced.CLASSEXIT, new ExitPainter(d));
		
		v.addStaticPainter(new StaticPlatformPainer(d));
		
		
		return v;
		
	}
	
	static class AgentPainter extends ObjectPainter{

		public AgentPainter(Domain domain) {
			super(domain);
		}

		@Override
		public void paintObject(Graphics2D g2, ObjectInstance ob, float cWidth, float cHeight) {
			
			g2.setColor(Color.blue);
			
			float domainXScale = (RENDERMAXX + 1) - Blockdude.MINX;
			float domainYScale = (RENDERMAXY + 1) - Blockdude.MINY;
			
			//determine then normalized width
			float width = (1.0f / domainXScale) * cWidth;
			float height = (1.0f / domainYScale) * cHeight;
			
			float rx = ob.getDiscValForAttribute(Blockdude.ATTX)*width;
			float ry = cHeight - height - ob.getDiscValForAttribute(Blockdude.ATTY)*height;
			
			g2.fill(new Rectangle2D.Float(rx, ry, width, height));
			
			
			//draw eye for showing the direction of the agent
			g2.setColor(Color.orange);
			float eyeWidth = width*0.25f;
			float eyeHeight = height*0.25f;
			
			float ex = rx;
			if(ob.getDiscValForAttribute(Blockdude.ATTDIR) == 1){
				ex = (rx+width) - eyeWidth;
			}
			
			float ey = ry + 0.2f*height;
			
			g2.fill(new Rectangle2D.Float(ex, ey, eyeWidth, eyeHeight));
			
			
		}
		
		
	}
	
	
	static class BlockPainter extends ObjectPainter{

		public BlockPainter(Domain domain) {
			super(domain);
		}

		@Override
		public void paintObject(Graphics2D g2, ObjectInstance ob, float cWidth, float cHeight) {
			
			g2.setColor(Color.gray);
			
			float domainXScale = (RENDERMAXX + 1) - Blockdude.MINX;
			float domainYScale = (RENDERMAXY + 1) - Blockdude.MINY;
			
			//determine then normalized width
			float width = (1.0f / domainXScale) * cWidth;
			float height = (1.0f / domainYScale) * cHeight;
			
			float rx = ob.getDiscValForAttribute(Blockdude.ATTX)*width;
			float ry = cHeight - height - ob.getDiscValForAttribute(Blockdude.ATTY)*height;
			
			g2.fill(new Rectangle2D.Float(rx, ry, width, height));
			
			
		}
		
		
	}
	
	
	static class ExitPainter extends ObjectPainter{

		public ExitPainter(Domain domain) {
			super(domain);
		}

		@Override
		public void paintObject(Graphics2D g2, ObjectInstance ob, float cWidth, float cHeight) {
			
			g2.setColor(Color.black);
			
			float domainXScale = (RENDERMAXX + 1) - Blockdude.MINX;
			float domainYScale = (RENDERMAXY + 1) - Blockdude.MINY;
			
			//determine then normalized width
			float width = (1.0f / domainXScale) * cWidth;
			float height = (1.0f / domainYScale) * cHeight;
			
			float rx = ob.getDiscValForAttribute(Blockdude.ATTX)*width;
			float ry = cHeight - height - ob.getDiscValForAttribute(Blockdude.ATTY)*height;
			
			g2.fill(new Rectangle2D.Float(rx, ry, width, height));
			
			
		}
		
		
	}
	
	
	static class PlatformPainter extends ObjectPainter{

		public PlatformPainter(Domain domain) {
			super(domain);
		}

		@Override
		public void paintObject(Graphics2D g2, ObjectInstance ob, float cWidth, float cHeight) {
			
			g2.setColor(Color.green);
			
			float domainXScale = (RENDERMAXX + 1) - Blockdude.MINX;
			float domainYScale = (RENDERMAXY + 1) - Blockdude.MINY;
			
			
			float px = ob.getDiscValForAttribute(Blockdude.ATTX);
			float ph = ob.getDiscValForAttribute(Blockdude.ATTHEIGHT);
			
			
			//determine the normalized width
			float width = (1.0f / domainXScale) * cWidth;
			float height = (1.0f / domainYScale) * cHeight;
			
			float rx = px*width;
			float ry = cHeight - height - ph*height;
			
			g2.fill(new Rectangle2D.Float(rx, ry, width, height*(ph+1)));
			
			
		}
		
		
	}
	
	static class StaticPlatformPainer extends StaticPainter{

		public StaticPlatformPainer(Domain domain) {
			super(domain);
		}

		@Override
		public void paint(Graphics2D g2, float cWidth, float cHeight) {
			g2.setColor(Color.green);
			
			float domainXScale = (RENDERMAXX + 1) - Blockdude.MINX;
			float domainYScale = (RENDERMAXY + 1) - Blockdude.MINY;
			
			//determine the normalized width
			float width = (1.0f / domainXScale) * cWidth;
			float height = (1.0f / domainYScale) * cHeight;
			
			
			for(int px = 0; px < BlockdudeMemReduced.HEIGHTMAP.length; px++){
				int ph = BlockdudeMemReduced.HEIGHTMAP[px];
				if(ph > -1){
					
					//then we have a platform to draw
					float rx = px*width;
					float ry = cHeight - height - ph*height;
					
					g2.fill(new Rectangle2D.Float(rx, ry, width, height*(ph+1)));
					
				}
			}
			
			
		}
		
		
		
	}
	
	
}
