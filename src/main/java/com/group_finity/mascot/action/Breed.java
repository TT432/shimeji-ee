package com.group_finity.mascot.action;

import com.group_finity.mascot.Main;
import com.group_finity.mascot.Mascot;
import com.group_finity.mascot.animation.Animation;
import com.group_finity.mascot.exception.BehaviorInstantiationException;
import com.group_finity.mascot.exception.CantBeAliveException;
import com.group_finity.mascot.exception.LostGroundException;
import com.group_finity.mascot.exception.VariableException;
import com.group_finity.mascot.script.VariableMap;

import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Original Author: Yuki Yamada of Group Finity (http://www.group-finity.com/Shimeji/)
 * Currently developed by Shimeji-ee Group.
 */
public class Breed extends Animate {

    private static final Logger log = Logger.getLogger(Breed.class.getName());

    public static final String PARAMETER_BORNX = "BornX";

    private static final int DEFAULT_BORNX = 0;

    public static final String PARAMETER_BORNY = "BornY";

    private static final int DEFAULT_BORNY = 0;

    public static final String PARAMETER_BORNBEHAVIOR = "BornBehavior";

    private static final String DEFAULT_BORNBEHAVIOR = "";

    public static final String PARAMETER_BORNMASCOT = "BornMascot";

    private static final String DEFAULT_BORNMASCOT = "";

    private int scaling;

    public Breed(final List<Animation> animations, final VariableMap params) {
        super(animations, params);
    }

    @Override
    public void init(final Mascot mascot) throws VariableException {
        super.init(mascot);

        scaling = Integer.parseInt(Main.getInstance().getProperties().getProperty("Scaling", "1"));
    }

    @Override
    protected void tick() throws LostGroundException, VariableException {

        super.tick();

        if (Boolean.parseBoolean(Main.getInstance().getProperties().getProperty("Breeding", "true")) && getTime() == getAnimation().getDuration() - 1) {
            breed();
        }
    }

    private void breed() throws VariableException {
        String childType = Main.getInstance().getConfiguration(getBornMascot()) != null ? getBornMascot() : getMascot().getImageSet();

        final Mascot mascot = new Mascot(childType);

        log.log(Level.INFO, "Breed Mascot ({0},{1},{2})", new Object[]{getMascot(), this, mascot});

        if (getMascot().isLookRight()) {
            mascot.setAnchor(new Point(getMascot().getAnchor().x - (getBornX() * scaling),
                    getMascot().getAnchor().y + (getBornY() * scaling)));
        } else {
            mascot.setAnchor(new Point(getMascot().getAnchor().x + (getBornX() * scaling),
                    getMascot().getAnchor().y + (getBornY() * scaling)));
        }
        mascot.setLookRight(getMascot().isLookRight());

        try {
            mascot.setBehavior(Main.getInstance().getConfiguration(childType).buildBehavior(getBornBehavior()));

            getMascot().getManager().add(mascot);

        } catch (final BehaviorInstantiationException e) {
            log.log(Level.SEVERE, "Fatal Exception", e);
            Main.showError(Main.getInstance().getLanguageBundle().getString("FailedCreateNewShimejiErrorMessage") + "\n" + e.getMessage() + "\n" + Main.getInstance().getLanguageBundle().getString("SeeLogForDetails"));
            mascot.dispose();
        } catch (final CantBeAliveException e) {
            log.log(Level.SEVERE, "Fatal Exception", e);
            Main.showError(Main.getInstance().getLanguageBundle().getString("FailedCreateNewShimejiErrorMessage") + "\n" + e.getMessage() + "\n" + Main.getInstance().getLanguageBundle().getString("SeeLogForDetails"));
            mascot.dispose();
        }
    }

    private int getBornY() throws VariableException {
        return eval(PARAMETER_BORNY, Number.class, DEFAULT_BORNY).intValue();
    }

    private int getBornX() throws VariableException {
        return eval(PARAMETER_BORNX, Number.class, DEFAULT_BORNX).intValue();
    }

    private String getBornBehavior() throws VariableException {
        return eval(PARAMETER_BORNBEHAVIOR, String.class, DEFAULT_BORNBEHAVIOR);
    }

    private String getBornMascot() throws VariableException {
        return eval(PARAMETER_BORNMASCOT, String.class, DEFAULT_BORNMASCOT);
    }
}
