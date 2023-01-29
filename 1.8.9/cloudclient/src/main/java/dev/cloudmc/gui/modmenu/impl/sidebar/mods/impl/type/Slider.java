/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.type;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.setting.Setting;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.Button;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.Settings;
import dev.cloudmc.helpers.Helper2D;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;

public class Slider extends Settings {

    private boolean drag;
    private float difference;
    private boolean direction = false;
    private float sliderPos;
    private final int sliderWidth = 150;
    private Animate animate = new Animate();

    public Slider(Setting setting, Button button, int y) {
        super(setting, button, y);
        animate.setEase(Easing.CUBIC_OUT).setMin(0).setSpeed(125);
        sliderPos = setting.getCurrentNumber() / (setting.getMaxNumber() / sliderWidth);
    }

    /**
     * Renders the Slider Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderSetting(int mouseX, int mouseY) {
        Cloud.INSTANCE.fontHelper.size30.drawString(
                setting.getName(),
                button.getPanel().getX() + 20,
                button.getPanel().getY() + button.getPanel().getH() + getY() + 6,
                Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB()
        );
        Cloud.INSTANCE.fontHelper.size20.drawString(
                String.valueOf(MathHelper.round(setting.getCurrentNumber(), 1)),
                button.getPanel().getX() + button.getPanel().getW() - 175 -
                        Cloud.INSTANCE.fontHelper.size20.getStringWidth(String.valueOf(MathHelper.round(setting.getCurrentNumber(), 1))),
                button.getPanel().getY() + button.getPanel().getH() + getY() + 9,
                Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB()
        );

        Helper2D.drawRoundedRectangle(
                button.getPanel().getX() + button.getPanel().getW() - sliderWidth - 20,
                button.getPanel().getY() + button.getPanel().getH() + getY() + 10,
                150, 5, 2, ClientStyle.getBackgroundColor(50).getRGB(),
                Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
        );

        float preMove = sliderPos;

        if (drag) {
            sliderPos = mouseX - (button.getPanel().getX() + button.getPanel().getW() - sliderWidth - 20);
            if(sliderPos < 0) {
                sliderPos = 0;
            } else if(sliderPos > sliderWidth){
                sliderPos = sliderWidth;
            }
            setting.setCurrentNumber(sliderPos * (setting.getMaxNumber() / sliderWidth));
        }

        float move = sliderPos;

        if(move != preMove) {
            difference = move - preMove;
            if(difference > 0) {
                direction = false;
                animate.setMax(difference);
            } else if (difference < 0) {
                direction = true;
                animate.setMax(-difference);
            }
            animate.reset();
        }

        animate.update();

        float xPos = (button.getPanel().getX() + button.getPanel().getW() - sliderWidth - 20) + sliderPos - 3;
        Helper2D.drawRoundedRectangle(
                (int) (direction ? xPos - difference - animate.getValueI() : xPos - difference + animate.getValueI()),
                button.getPanel().getY() + button.getPanel().getH() + getY() + 5,
                7, 16, 1, ClientStyle.getBackgroundColor(80).getRGB(),
                Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
        );
    }

    /**
     * Changes the drag variable if the slider is pressed
     *
     * @param mouseX      The current X position of the mouse
     * @param mouseY      The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MathHelper.withinBox(
                button.getPanel().getX() + button.getPanel().getW() - 170,
                button.getPanel().getY() + button.getPanel().getH() + getY() + 5,
                150, 16, mouseX, mouseY)
        ) {
            drag = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        drag = false;
    }
}
