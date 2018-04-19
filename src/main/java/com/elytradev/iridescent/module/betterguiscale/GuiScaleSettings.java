package com.elytradev.iridescent.module.betterguiscale;

import java.io.File;
import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiScaleSettings extends GuiScreen {
	public class GuiSliderAuto extends GuiSlider {

		public GuiSliderAuto(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, ISlider par) {
			super(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, par);
		}

		public GuiSliderAuto(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr) {
			super(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr);
		}

		public GuiSliderAuto(int id, int xPos, int yPos, String displayStr, double minVal, double maxVal, double currentVal, ISlider par) {
			super(id, xPos, yPos, displayStr, minVal, maxVal, currentVal, par);
		}
		
		@Override
		public void updateSlider() {
			super.updateSlider();
			displayString = displayString.replace(" 0x", " "+I18n.format("options.guiScale.auto"));
		}

	}
	public class GuiSliderGUI extends GuiSliderAuto {

		public GuiSliderGUI(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, ISlider par) {
			super(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, par);
		}

		public GuiSliderGUI(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr) {
			super(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr);
		}

		public GuiSliderGUI(int id, int xPos, int yPos, String displayStr, double minVal, double maxVal, double currentVal, ISlider par) {
			super(id, xPos, yPos, displayStr, minVal, maxVal, currentVal, par);
		}
		
		@Override
		public void updateSlider() {
			super.updateSlider();
			displayString = displayString.replace(" 1x", " 1x ("+I18n.format("options.guiScale.small")+")");
			displayString = displayString.replace(" 2x", " 2x ("+I18n.format("options.guiScale.normal")+")");
			displayString = displayString.replace(" 3x", " 3x ("+I18n.format("options.guiScale.large")+")");
		}

	}

	private final GuiScreen parent;
	
	private GuiSlider guiScale;
	private GuiSlider hudScale;
	private GuiSlider tooltipScale;
	
	public GuiScaleSettings(GuiScreen parent) {
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		buttonList.clear();
		buttonList.add(new GuiButton(200, width / 2 - 100, height - 27, I18n.format("gui.done")));
		buttonList.add(guiScale = new GuiSliderGUI(1, (width / 2) - 100, 40, 200, 20, "GUI Scale: ", "x", 0, 16, mc.gameSettings.guiScale, false, true));
		buttonList.add(hudScale = new GuiSliderAuto(2, (width / 2) - 100, 65, 98, 20, "HUD Scale: ", "x", 0, 16, ModuleBetterGuiScale.hudScale, false, true));
		buttonList.add(tooltipScale = new GuiSliderAuto(3, (width / 2) + 2, 65, 98, 20, "Tooltip Scale: ", "x", 0, 16, ModuleBetterGuiScale.tooltipScale, false, true));
		guiScale.updateSlider();
		hudScale.updateSlider();
		tooltipScale.updateSlider();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 200) {
			mc.displayGuiScreen(parent);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawCenteredString(this.fontRenderer, I18n.format("options.iridescent.scaleTitle"), width / 2, 5, -1);
		drawCenteredString(this.fontRenderer, "\u00A77"+I18n.format("options.iridescent.scaleSubtitle"), width / 2, 17, -1);
		if (guiScale.getValueInt() != mc.gameSettings.guiScale
				|| hudScale.getValueInt() != ModuleBetterGuiScale.hudScale
				|| tooltipScale.getValueInt() != ModuleBetterGuiScale.tooltipScale) {
			drawCenteredString(this.fontRenderer, I18n.format("options.iridescent.dirty"), width / 2, 100, -1);
		}
		int maxScale = ModuleBetterGuiScale.getMaxScale();
		if (guiScale.getValueInt() > maxScale
				|| hudScale.getValueInt() > maxScale
				|| tooltipScale.getValueInt() > maxScale) {
			drawCenteredString(this.fontRenderer, I18n.format("options.iridescent.tooBig", maxScale), width / 2, 112, 0xFFFF55);
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		mc.gameSettings.guiScale = guiScale.getValueInt();
		ModuleBetterGuiScale.hudScale = hudScale.getValueInt();
		ModuleBetterGuiScale.tooltipScale = tooltipScale.getValueInt();
		mc.gameSettings.saveOptions();
		Configuration cfg = new Configuration(new File("config/iridescent.cfg"));
		cfg.get("betterguiscale", "hudScale", 0).set(ModuleBetterGuiScale.hudScale);
		cfg.get("betterguiscale", "tooltipScale", 0).set(ModuleBetterGuiScale.tooltipScale);
		cfg.save();
	}

}
