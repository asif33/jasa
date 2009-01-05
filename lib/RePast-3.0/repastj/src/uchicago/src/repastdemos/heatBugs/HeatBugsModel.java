/*$$
 * Copyright (c) 1999, Trustees of the University of Chicago
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with 
 * or without modification, are permitted provided that the following 
 * conditions are met:
 *
 *	 Redistributions of source code must retain the above copyright notice,
 *	 this list of conditions and the following disclaimer.
 *
 *	 Redistributions in binary form must reproduce the above copyright notice,
 *	 this list of conditions and the following disclaimer in the documentation
 *	 and/or other materials provided with the distribution.
 *
 * Neither the name of the University of Chicago nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE TRUSTEES OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *$$*/
package uchicago.src.repastdemos.heatBugs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.event.SliderListener;
import uchicago.src.sim.gui.ColorMap;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.Value2DDisplay;
import uchicago.src.sim.space.Object2DTorus;
import cern.jet.random.Uniform;

/**
 * A translation of the Swarm example simulation Heat Bugs. The heat bugs
 * simulation consists of heat bugs - simple agents that absorb and expel heat
 * and a heatspace which diffuses this heat into the area surrounding the
 * bug. Heat bugs have an ideal temperature and will move about the space
 * in attempt to achieve this idea temperature.
 *
 * @author Swarm Project and Nick Collier
 * @version $Revision$ $Date$
 */

public class HeatBugsModel extends SimModelImpl {

  private int numBugs = 100;
  private double evapRate = 0.99;
  private double diffusionConstant = 1.0;
  private int worldXSize = 100;
  private int worldYSize = 100;
  private int minIdealTemp = 17000;
  private int maxIdealTemp = 31000;
  private int minOutputHeat = 3000;
  private int maxOutputHeat = 10000;
  private float randomMoveProbability = 0.0f;

  //private int pauseVal = -1;
  private Schedule schedule;
  private ArrayList heatBugList = new ArrayList ();
  private Object2DTorus world;
  private HeatSpace space;

  private DisplaySurface dsurf;

  public HeatBugsModel () {
  }

  private void buildModel () {
    space = new HeatSpace (diffusionConstant, evapRate, worldXSize, worldYSize);
    world = new Object2DTorus (space.getSizeX (), space.getSizeY ());

    for (int i = 0; i < numBugs; i++) {
      int idealTemp = Uniform.staticNextIntFromTo (minIdealTemp, maxIdealTemp);
      int outputHeat = Uniform.staticNextIntFromTo (minOutputHeat, maxOutputHeat);
      int x, y;

      do {
        x = Uniform.staticNextIntFromTo (0, space.getSizeX () - 1);
        y = Uniform.staticNextIntFromTo (0, space.getSizeY () - 1);
      } while (world.getObjectAt (x, y) != null);

      HeatBug bug = new HeatBug (space, world, x, y, idealTemp, outputHeat,
                                 randomMoveProbability);
      world.putObjectAt (x, y, bug);
      heatBugList.add (bug);
    }
  }

  private void buildDisplay () {

    Object2DDisplay agentDisplay = new Object2DDisplay (world);
    agentDisplay.setObjectList (heatBugList);

    // 64 shades of red
    ColorMap map = new ColorMap ();
    for (int i = 0; i < 64; i++) {
      map.mapColor (i, i / 63.0, 0, 0);
    }

    Value2DDisplay heatDisplay = new Value2DDisplay (space, map);
    heatDisplay.setZeroTransparent (true);
    heatDisplay.setDisplayMapping (512, 0);
    dsurf.addDisplayable (heatDisplay, "Heat Space");
    dsurf.addDisplayableProbeable (agentDisplay, "Bugs");
//    dsurf.setSnapshotFileName("./heatBugPic");

    // UNCOMMENT BELOW TO CREATE A MOVIE
    //dsurf.setMovieName("./HeatBugMovie", DisplaySurface.QUICK_TIME);

    addSimEventListener (dsurf);
  }

  public void step () {
    space.diffuse ();
    for (int i = 0; i < heatBugList.size (); i++) {
      HeatBug bug = (HeatBug) heatBugList.get (i);
      bug.step ();
    }

    space.update ();
    dsurf.updateDisplay ();
  }

  private void buildSchedule () {
    schedule.scheduleActionBeginning (1, this, "step");

    /*
     * Older style hand coded inner classes, replaced by the step method
     * and the single line of schedule code above.

    class HeatBugsRunner extends BasicAction {
      public void execute() {
        space.diffuse();
        for (int i = 0; i < heatBugList.size(); i++) {
          HeatBug bug = (HeatBug)heatBugList.get(i);
          bug.step();
        }
        space.update();
        dsurf.updateDisplay();



        //System.out.println("seed: " + getRngSeed());
        //System.out.println(Uniform.staticNextIntFromTo(0, 100));
        //System.out.println(normal.nextDouble());

      }
    };
    */
  }

  public void begin () {
    //setRngSeed(1972L);
    buildModel ();
    buildDisplay ();
    buildSchedule ();
    dsurf.display ();
  }

  public void setup () {

    if (dsurf != null)
      dsurf.dispose ();
    dsurf = null;
    schedule = null;
    System.gc ();

    dsurf = new DisplaySurface (this, "Heat Bugs Display");
    registerDisplaySurface ("Main", dsurf);
    schedule = new Schedule (1);

    numBugs = 100;
    evapRate = 0.99;
    diffusionConstant = 1.0;
    worldXSize = 100;
    worldYSize = 100;
    minIdealTemp = 17000;
    maxIdealTemp = 31000;
    minOutputHeat = 3000;
    maxOutputHeat = 10000;
    randomMoveProbability = 0.0f;

    //pauseVal = -1;

    heatBugList = new ArrayList ();
    world = null;
    space = null;
    setupCustomAction ();
  }

  public String[] getInitParam () {
    String[] params = {"evapRate", "diffusionConstant", "maxIdealTemp",
                       "minIdealTemp", "maxOutputHeat", "minOutputHeat", "worldXSize",
                       "worldYSize", "numBugs", "Pause", "Model"};
    return params;
  }

  public Schedule getSchedule () {
    return schedule;
  }

  public String getName () {
    return "HeatBugs";
  }

  private void setupCustomAction () {

    modelManipulator.init ();

    // this adds a button to the Custom Action tab that
    // will set the heat space to 0 heat when clicked
    modelManipulator.addButton ("Deep Freeze", new ActionListener () {
      public void actionPerformed (ActionEvent evt) {
        for (int i = 0; i < space.getSizeX (); i++) {
          for (int j = 0; j < space.getSizeY (); j++) {
            space.putValueAt (i, j, 0);
          }
        }
        space.update ();
      }
    });

    // this will add a slider to the Custom Action tab that will
    // increment the output heat of each bug by a the slider amount.
    // The code doesn't really work when trying to decrement the heat
    // by sliding the slider to the left. But it is a good example
    // of what you can do with a slider.
    modelManipulator.addSlider ("Increment Heat", 0, 100, 10, new SliderListener () {
      public void execute () {
        for (int i = 0; i < heatBugList.size (); i++) {
          HeatBug bug = (HeatBug) heatBugList.get (i);
          int outputHeat = bug.getOutputHeat ();
          if (isSlidingLeft && !isAdjusting) {
            outputHeat -= outputHeat * value * .01;
          } else if (!isSlidingLeft && !isAdjusting) {
            outputHeat += outputHeat * value * .01;
          }
          bug.setOutputHeat (outputHeat);
        }
      }
    });
  }

  // properties
  public int getNumBugs () {
    return numBugs;
  }

  public void setNumBugs (int numBugs) {
    this.numBugs = numBugs;
  }

  public double getEvapRate () {
    return evapRate;
  }

  public void setEvapRate (double rate) {
    evapRate = rate;
  }

  public double getDiffusionConstant () {
    return diffusionConstant;
  }

  public void setDiffusionConstant (double constant) {
    diffusionConstant = constant;
  }

  public int getWorldXSize () {
    return worldXSize;
  }

  public void setWorldXSize (int size) {
    worldXSize = size;
  }

  public int getWorldYSize () {
    return worldYSize;
  }

  public void setWorldYSize (int size) {
    worldYSize = size;
  }

  public int getMinIdealTemp () {
    return minIdealTemp;
  }

  public void setMinIdealTemp (int temp) {
    minIdealTemp = temp;
  }

  public int getMaxIdealTemp () {
    return maxIdealTemp;
  }

  public void setMaxIdealTemp (int temp) {
    maxIdealTemp = temp;
  }

  public int getMinOutputHeat () {
    return minOutputHeat;
  }

  public void setMinOutputHeat (int heat) {
    minOutputHeat = heat;
  }

  public int getMaxOutputHeat () {
    return maxOutputHeat;
  }

  public void setMaxOutputHeat (int heat) {
    maxOutputHeat = heat;
  }

  public float getRandomMoveProbability () {
    return randomMoveProbability;
  }

  public void setRandomMoveProbability (float prob) {
    randomMoveProbability = prob;
  }

  public static void main (String[] args) {
    uchicago.src.sim.engine.SimInit init = new uchicago.src.sim.engine.SimInit ();
    HeatBugsModel model = new HeatBugsModel ();
    if (args.length > 0)
      init.loadModel (model, args[0], false);
    else
      init.loadModel (model, null, false);
  }
}
