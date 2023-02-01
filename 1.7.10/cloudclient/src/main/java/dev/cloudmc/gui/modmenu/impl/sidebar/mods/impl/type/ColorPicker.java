/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.type;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.option.Option;
import dev.cloudmc.feature.setting.Setting;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.modmenu.impl.Panel;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.Button;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.Settings;
import dev.cloudmc.gui.modmenu.impl.sidebar.options.Options;
import dev.cloudmc.helpers.*;
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;

import java.awt.*;

public class ColorPicker extends Settings {

    private boolean dragSide;
    private boolean dragMain;
    private boolean open;
    private PositionHelper sidePosHelper = new PositionHelper(75);
    private PositionHelper mainPosHelperX = new PositionHelper(100);
    private PositionHelper mainPosHelperY = new PositionHelper(150);
    private Animate animate = new Animate();

    public ColorPicker(Setting setting, Button button, int y) {
        super(setting, button, y);
        open = false;
        animate.setEase(Easing.CUBIC_OUT).setMin(0).setMax(70).setSpeed(200);
    }

    @Override
    public void renderSetting(int mouseX, int mouseY) {
        if(open) {
            setH(100);
        } else {
            setH(25);
        }

        int getXW = button.getPanel().getX() + button.getPanel().getW();
        int getYH = button.getPanel().getY() + button.getPanel().getH() + getY();
        boolean rounded = Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled();

        Cloud.INSTANCE.fontHelper.size30.drawString(setting.getName(), button.getPanel().getX() + 20, getYH + 6, Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB());
        animate.update();

        if(open) {
            if(!animate.hasFinished()) {
                GLHelper.startScissor(getXW - 193, getYH + 25, 180, 70);
            }

            int offset = animate.getValueI() - 70;

            Helper2D.drawRoundedRectangle(getXW - 40, getYH + 25 + offset, 20, 70, 2, ClientStyle.getBackgroundColor(50).getRGB(), rounded ? 0 : -1);
            Helper2D.drawRoundedRectangle(getXW - 193, getYH + 25 + offset, 150, 70, 2, ClientStyle.getBackgroundColor(50).getRGB(), rounded ? 0 : -1);

            Helper2D.drawPicture(getXW - 38, getYH + 27 + offset, 16, 66, 0, "icon/hue.png");

            sidePosHelper.pre(setting.getSideSlider());

            if (dragSide) {
                setting.setSideSlider(mouseY - (getYH + 25));
                float sliderHeight = 65;
                if (setting.getSideSlider() < 0) {
                    setting.setSideSlider(0);
                } else if (setting.getSideSlider() > sliderHeight) {
                    setting.setSideSlider(sliderHeight);
                }
            }

            sidePosHelper.post(setting.getSideSlider());
            sidePosHelper.update();

            int sideColor = ColorHelper.getColorAtPixel(getXW - 35, getYH + 28 + setting.getSideSlider() + offset);
            if(animate.hasFinished())
                setting.setSideColor(ColorHelper.hexToRgb(sideColor));

            float sidePosY = getYH + 25 + setting.getSideSlider() + offset;
            Helper2D.drawRoundedRectangle(getXW - 40, (int) (sidePosHelper.isDirection() ?
                    sidePosY - sidePosHelper.getDifference() - sidePosHelper.getValue() :
                    sidePosY - sidePosHelper.getDifference() + sidePosHelper.getValue()
            ), 20, 5, 2, -1, rounded ? 0 : -1);
            Helper2D.drawHorizontalGradientRectangle(getXW - 191, getYH + 27 + offset, 146, 66, -1, setting.getSideColor().getRGB());
            Helper2D.drawGradientRectangle(getXW - 191, getYH + 27 + offset, 146, 66, 0x00000000, 0xff000000);

            mainPosHelperX.pre(setting.getMainSlider()[0]);
            mainPosHelperY.pre(setting.getMainSlider()[1]);

            if (dragMain) {
                setting.getMainSlider()[0] = mouseX - (getXW - 193);
                setting.getMainSlider()[1] = mouseY - (getYH + 25);
                float sliderWidth = 145;
                float sliderHeight = 65;
                if (setting.getMainSlider()[0] < 0) {
                    setting.getMainSlider()[0] = 0;
                } else if (setting.getMainSlider()[0] > sliderWidth) {
                    setting.getMainSlider()[0] = sliderWidth;
                }
                if (setting.getMainSlider()[1] < 0) {
                    setting.getMainSlider()[1] = 0;
                } else if (setting.getMainSlider()[1] > sliderHeight) {
                    setting.getMainSlider()[1] = sliderHeight;
                }
            }

            mainPosHelperX.post(setting.getMainSlider()[0]);
            mainPosHelperY.post(setting.getMainSlider()[1]);
            mainPosHelperX.update();
            mainPosHelperY.update();

            Color mainColor = setting.getColor();
            int color = ColorHelper.getColorAtPixel(getXW - 191 + setting.getMainSlider()[0], getYH + 28 + setting.getMainSlider()[1] + offset);
            if(animate.hasFinished())
                mainColor = new Color(
                        ColorHelper.hexToRgb(color).getRed(),
                        ColorHelper.hexToRgb(color).getGreen(),
                        ColorHelper.hexToRgb(color).getBlue(),
                        255
                );
            float mainPosX = getXW - 193 + setting.getMainSlider()[0];
            float mainPosY = getYH + 25 + setting.getMainSlider()[1] + offset;
            Helper2D.drawRoundedRectangle(
                    (int) (mainPosHelperX.isDirection() ?
                            mainPosX - mainPosHelperX.getDifference() - mainPosHelperX.getValue() :
                            mainPosX - mainPosHelperX.getDifference() + mainPosHelperX.getValue()
                    ),
                    (int) (mainPosHelperY.isDirection() ?
                            mainPosY - mainPosHelperY.getDifference() - mainPosHelperY.getValue() :
                            mainPosY - mainPosHelperY.getDifference() + mainPosHelperY.getValue()
                    ), 5, 5, 3, -1, 0
            );
            setting.setColor(mainColor);

            if(!animate.hasFinished()) {
                GLHelper.endScissor();
            }
        }

        String color = "R" + setting.getColor().getRed() + " G" + setting.getColor().getGreen() + " B" + setting.getColor().getBlue();
        Cloud.INSTANCE.fontHelper.size20.drawString(color, getXW - 45 - Cloud.INSTANCE.fontHelper.size20.getStringWidth(color), getYH + 9, -1);
        Helper2D.drawRoundedRectangle(getXW - 40, getYH + 2, 20, 20, 2, ClientStyle.getBackgroundColor(50).getRGB(), rounded ? 0 : -1);
        Helper2D.drawRectangle(getXW - 38, getYH + 4, 16, 16, setting.getColor().getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int getXW = button.getPanel().getX() + button.getPanel().getW();
        int getYH = button.getPanel().getY() + button.getPanel().getH() + getY();

        if(MathHelper.withinBox(button.getPanel().getX(), getYH, button.getPanel().getW(), 25, mouseX, mouseY)) {
            open = !open;
            animate.reset();
        } else if (MathHelper.withinBox(getXW - 40, getYH + 25, 20, 70, mouseX, mouseY)) {
            dragSide = true;
        } else if (MathHelper.withinBox(getXW - 193, getYH + 25, 150, 70, mouseX, mouseY)) {
            dragMain = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        dragSide = false;
        dragMain = false;
    }
}